package fpt.dps.dtms.web.rest.external;

import fpt.dps.dtms.config.Constants;
import com.codahale.metrics.annotation.Timed;
import fpt.dps.dtms.domain.User;
import fpt.dps.dtms.repository.UserRepository;
import fpt.dps.dtms.repository.search.UserSearchRepository;
import fpt.dps.dtms.security.AuthoritiesConstants;
import fpt.dps.dtms.security.SecurityUtils;
import fpt.dps.dtms.service.MailService;
import fpt.dps.dtms.service.StorageService;
import fpt.dps.dtms.service.UserService;
import fpt.dps.dtms.service.dto.BugsDTO;
import fpt.dps.dtms.service.dto.ProjectUsersDTO;
import fpt.dps.dtms.service.dto.UserDTO;
import fpt.dps.dtms.web.rest.errors.BadRequestAlertException;
import fpt.dps.dtms.web.rest.errors.EmailAlreadyUsedException;
import fpt.dps.dtms.web.rest.errors.InternalServerErrorException;
import fpt.dps.dtms.web.rest.errors.InvalidPasswordException;
import fpt.dps.dtms.web.rest.errors.LoginAlreadyUsedException;
import fpt.dps.dtms.web.rest.util.HeaderUtil;
import fpt.dps.dtms.web.rest.util.PaginationUtil;
import fpt.dps.dtms.web.rest.vm.ManagedUserVM;
import io.github.jhipster.web.util.ResponseUtil;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing users.
 * <p>
 * This class accesses the User entity, and needs to fetch its collection of authorities.
 * <p>
 * For a normal use-case, it would be better to have an eager relationship between User and Authority,
 * and send everything to the client side: there would be no View Model and DTO, a lot less code, and an outer-join
 * which would be good for performance.
 * <p>
 * We use a View Model and a DTO for 3 reasons:
 * <ul>
 * <li>We want to keep a lazy association between the user and the authorities, because people will
 * quite often do relationships with the user, and we don't want them to get the authorities all
 * the time for nothing (for performance reasons). This is the #1 goal: we should not impact our users'
 * application because of this use-case.</li>
 * <li> Not having an outer join causes n+1 requests to the database. This is not a real issue as
 * we have by default a second-level cache. This means on the first HTTP call we do the n+1 requests,
 * but then all authorities come from the cache, so in fact it's much better than doing an outer join
 * (which will get lots of data from the database, for each HTTP call).</li>
 * <li> As this manages users, for security reasons, we'd rather have a DTO layer.</li>
 * </ul>
 * <p>
 * Another option would be to have a specific JPA entity graph to handle this case.
 */
@RestController
@RequestMapping("/api/external")
public class ExternalUserResource {

    private final Logger log = LoggerFactory.getLogger(ExternalUserResource.class);

    private final UserRepository userRepository;

    private final UserService userService;

    private final MailService mailService;

    private final UserSearchRepository userSearchRepository;
    
    private final PasswordEncoder passwordEncoder;
    
    private final StorageService storageService;

    public ExternalUserResource(UserRepository userRepository, UserService userService, MailService mailService, UserSearchRepository userSearchRepository,
    		PasswordEncoder passwordEncoder, StorageService storageService) {

        this.userRepository = userRepository;
        this.userService = userService;
        this.mailService = mailService;
        this.userSearchRepository = userSearchRepository;
        this.passwordEncoder = passwordEncoder;
        this.storageService = storageService;
    }

    /**
     * PUT /users : Updates an existing User.
     *
     * @param userDTO the user to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated user
     * @throws EmailAlreadyUsedException 400 (Bad Request) if the email is already in use
     * @throws LoginAlreadyUsedException 400 (Bad Request) if the login is already in use
     */
    @PutMapping("/users")
    @Timed
    public void updateUser(@Valid @RequestBody UserDTO userDTO) {
        log.debug("REST request to update User : {}", userDTO);
        final String userLogin = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new InternalServerErrorException("Current user login not found"));
        Optional<User> existingUser = userRepository.findOneByEmailIgnoreCase(userDTO.getEmail());
        if (existingUser.isPresent() && (!existingUser.get().getId().equals(userDTO.getId()))) {
            throw new EmailAlreadyUsedException();
        }
        Optional<User> user = userRepository.findOneByLogin(userLogin);
        if (!user.isPresent()) {
            throw new InternalServerErrorException("User could not be found");
        }
        userService.updateUserProfile(userDTO);
    }

    /**
     * GET /users/:login : get the "login" user.
     *
     * @param login the login of the user to find
     * @return the ResponseEntity with status 200 (OK) and with body the "login" user, or with status 404 (Not Found)
     */
    @GetMapping("/users/{login:" + Constants.LOGIN_REGEX + "}")
    @Timed
    public ResponseEntity<UserDTO> getUser(@PathVariable String login) {
        log.debug("REST request to get User : {}", login);
        UserDTO userDto = userService.getUserWithAuthoritiesByLogin(login);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(userDto));
        /*return ResponseUtil.wrapOrNotFound(
            userService.getUserWithAuthoritiesByLogin(login)
                .map(UserDTO::new));*/
    }
    
    
    @RequestMapping(value = "/users/change-avatar", method = RequestMethod.POST,
    	    headers = {"content-type=multipart/mixed","content-type=multipart/form-data", "content-type=application/octet-stream",
    	    		"content-type=application/json", "content-type=application/x-www-form-urlencoded"})
    @Timed
    public void changeAvatar(@RequestParam(value = "file", required=true) MultipartFile file) throws URISyntaxException {
    	final String userLogin = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new InternalServerErrorException("Current user login not found"));
        Optional<User> user = userRepository.findOneByLogin(userLogin);
        if (!user.isPresent()) {
            throw new InternalServerErrorException("User could not be found");
        }else {
        	String avatarURL = user.get().getImageUrl();
        	if( avatarURL == null || avatarURL == "" || ( avatarURL != null && storageService.deleteFile(avatarURL))) {
	        	String[] folders = {"profiles", userLogin};
	        	avatarURL = storageService.store(file, folders);
	        	userService.changeAvatar(avatarURL);
        	}
        }
    }
    
    /**
     * POST  /account/change-password : changes the current user's password
     *
     * @param password the new password
     * @throws InvalidPasswordException 400 (Bad Request) if the new password is incorrect
     */
    @PostMapping(path = "/account/change-password")
    @Timed
    public void changePassword(@RequestBody Map<String, String> password) {
    	final String userLogin = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new InternalServerErrorException("Current user login not found"));
        Optional<User> user = userRepository.findOneByLogin(userLogin);
        if (!user.isPresent()) {
            throw new InternalServerErrorException("User could not be found");
        }else {
        	if(userService.checkIfValidOldPassword(user.get(), password.get("currentPassword"))) {
        		if (!checkPasswordLength(password.get("newPassword"))) {
                    throw new InvalidPasswordException();
                }
                userService.changePassword(password.get("newPassword"));
        	}else {
        		throw new InvalidPasswordException();
        	}
        }
   }
    
    @GetMapping(path = "/users/avatar")
    @ResponseBody
	public ResponseEntity<String> loadAvatar() {
    	log.debug("request load to load avatar : ");
    	final String userLogin = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new InternalServerErrorException("Current user login not found"));
        Optional<User> user = userRepository.findOneByLogin(userLogin);
        if (!user.isPresent()) {
            throw new InternalServerErrorException("User could not be found");
        }else {
        	String file = null;
        	if(user.get().getImageUrl() != null) {
        		file = storageService.loadFile(user.get().getImageUrl());
        	}
        	return ResponseEntity.ok()
    				//.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
    				.body(file);
        }
	}
    
    /**
     * GET /users/activated/:projectId : get the user activated in project
     *
     * @param login the login of the user to find
     * @return the ResponseEntity with status 200 (OK) and with body the "login" user, or with status 404 (Not Found)
     */
    @GetMapping("/users/activated/{projectId}")
    @Timed
    public ResponseEntity<List<ProjectUsersDTO>> getUserActivatedByProject(@PathVariable("projectId") Long projectId) {
        log.debug("REST request to get User : {}", projectId);
        List<ProjectUsersDTO> result = this.userService.getUserActivatedByProject(projectId);
        return ResponseEntity.ok().body(result);
    }
    
	private static boolean checkPasswordLength(String password) {
	    return !StringUtils.isEmpty(password) &&
	        password.length() >= ManagedUserVM.PASSWORD_MIN_LENGTH &&
	        password.length() <= ManagedUserVM.PASSWORD_MAX_LENGTH;
	}
}
