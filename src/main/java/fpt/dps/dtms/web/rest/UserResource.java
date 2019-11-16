package fpt.dps.dtms.web.rest;

import fpt.dps.dtms.config.Constants;
import com.codahale.metrics.annotation.Timed;
import fpt.dps.dtms.domain.User;
import fpt.dps.dtms.repository.UserRepository;
import fpt.dps.dtms.repository.search.UserSearchRepository;
import fpt.dps.dtms.security.AuthoritiesConstants;
import fpt.dps.dtms.service.MailService;
import fpt.dps.dtms.service.UserService;
import fpt.dps.dtms.service.dto.UserDTO;
import fpt.dps.dtms.service.util.AppConstants;
import fpt.dps.dtms.service.dto.BusinessUnitManagerDTO;
import fpt.dps.dtms.service.dto.ProjectUsersDTO;
import fpt.dps.dtms.service.dto.SelectDTO;
import fpt.dps.dtms.web.rest.errors.BadRequestAlertException;
import fpt.dps.dtms.web.rest.errors.EmailAlreadyUsedException;
import fpt.dps.dtms.web.rest.errors.LoginAlreadyUsedException;
import fpt.dps.dtms.web.rest.util.HeaderUtil;
import fpt.dps.dtms.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import java.io.IOException;
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
@RequestMapping("/api")
public class UserResource {

    private final Logger log = LoggerFactory.getLogger(UserResource.class);

    private static final String ENTITY_NAME = "user";

    private final UserRepository userRepository;

    private final UserService userService;

    private final MailService mailService;

    private final UserSearchRepository userSearchRepository;

    public UserResource(UserRepository userRepository, UserService userService, MailService mailService, UserSearchRepository userSearchRepository) {

        this.userRepository = userRepository;
        this.userService = userService;
        this.mailService = mailService;
        this.userSearchRepository = userSearchRepository;
    }

    /**
     * POST  /users  : Creates a new user.
     * <p>
     * Creates a new user if the login and email are not already used, and sends an
     * mail with an activation link.
     * The user needs to be activated on creation.
     *
     * @param userDTO the user to create
     * @return the ResponseEntity with status 201 (Created) and with body the new user, or with status 400 (Bad Request) if the login or email is already in use
     * @throws URISyntaxException if the Location URI syntax is incorrect
     * @throws BadRequestAlertException 400 (Bad Request) if the login or email is already in use
     */
    @PostMapping("/users")
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<User> createUser(@Valid @RequestBody UserDTO userDTO) throws URISyntaxException {
        log.debug("REST request to save User : {}", userDTO);
        if (userDTO.getId() != null) {
            throw new BadRequestAlertException("A new user cannot already have an ID", "userManagement", "idexists");
            // Lowercase the user login before comparing with database
        } else if (userRepository.findOneByLogin(userDTO.getLogin().toLowerCase()).isPresent()) {
            throw new LoginAlreadyUsedException();
        } else if (userRepository.findOneByEmailIgnoreCase(userDTO.getEmail()).isPresent()) {
            throw new EmailAlreadyUsedException();
        } else {
            User newUser = userService.createUser(userDTO);
            //mailService.sendCreationEmail(newUser);
            return ResponseEntity.created(new URI("/api/users/" + newUser.getLogin()))
                .headers(HeaderUtil.createAlert( "userManagement.created", newUser.getLogin()))
                .body(newUser);
        }
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
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<UserDTO> updateUser(@Valid @RequestBody UserDTO userDTO) {
        log.debug("REST request to update User : {}", userDTO);
        Optional<User> existingUser = userRepository.findOneByEmailIgnoreCase(userDTO.getEmail());
        if (existingUser.isPresent() && (!existingUser.get().getId().equals(userDTO.getId()))) {
            throw new EmailAlreadyUsedException();
        }
        existingUser = userRepository.findOneByLogin(userDTO.getLogin().toLowerCase());
        if (existingUser.isPresent() && (!existingUser.get().getId().equals(userDTO.getId()))) {
            throw new LoginAlreadyUsedException();
        }
        Optional<UserDTO> updatedUser = userService.updateUser(userDTO);

        return ResponseUtil.wrapOrNotFound(updatedUser,
            HeaderUtil.createAlert("userManagement.updated", userDTO.getLogin()));
    }

    /**
     * GET /users : get all users.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and with body all users
     */
    @GetMapping("/users")
    @Timed
    public ResponseEntity<List<UserDTO>> getAllUsers(Pageable pageable) {
        final Page<UserDTO> page = userService.getAllManagedUsers(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/users");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET /users : get all users.
     *
     * @return the ResponseEntity with status 200 (OK) and with body all users
     */
    @GetMapping("/all-users")
    @Timed
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        final List<UserDTO> listUsers = userService.getAllManagedUsers();
        return new ResponseEntity<>(listUsers, HttpStatus.OK);
    }

    /**
     * GET /users : get all users for selectbox.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and with body all users
     */
    /*@GetMapping("/users-selects")
    @Timed
    public ResponseEntity<List<SelectDTO>> getAllUsersForSelects() {
        List<SelectDTO> page = userService.getAllUsersForSelects();
        return new ResponseEntity<>(page, HttpStatus.OK);
    }*/

    /**
     * GET /users-for-project-user : get all users for ProjectUser.
     *
     * @param query
     * @return the ResponseEntity with status 200 (OK) and with body all users
     */
    @GetMapping("/users-for-project-user")
    @Timed
    public ResponseEntity<List<User>> getAllUsersForProjectUser(@RequestParam String query) {
    	log.debug("REST getAllUsersForProjectUser:", query);
        final List<User> list = userService.getAllManagedUsersForProjectUser(Long.valueOf(query));
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/users-online")
    @Timed
    public ResponseEntity<List<Map<String, Object>>> getAllUserByProjectId(@RequestParam String query, Pageable pageable) {
    	log.debug("REST getAllUsersForProjectUser:", query);
    	List<Map<String, Object>> result = userService.getAllUserByProjectId(Long.valueOf(query), pageable);
    	int totalPage = Integer.parseInt(result.get(result.size() - 1).get("total").toString());
        result.remove(result.size() - 1);
    	Page<Map<String, Object>> page = new PageImpl<>(result, pageable, totalPage);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/users-online");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/users/condition")
    @Timed
    public ResponseEntity<List<Map<String, Object>>> getAllUserByCondition(
    		@RequestParam(value="packId", required = false) Long packId,
    		@RequestParam(value="purchaseOrderId", required = false) Long purchaseOrderId,
    		@RequestParam(value="projectId", required = false) Long projectId,
    		Pageable pageable) {
    	log.debug("REST get all Users in Project or PurchaseOrder or Package.");
    	List<Map<String, Object>> result = userService.getAllUserByCondition(projectId, purchaseOrderId, packId, pageable);
    	int totalPage = Integer.parseInt(result.get(result.size() - 1).get("total").toString());
        result.remove(result.size() - 1);
    	Page<Map<String, Object>> page = new PageImpl<>(result, pageable, totalPage);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/users/condition");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * Export members management to excel file
     * @param projectId
     * @return excel file
     * @author HoiHT1
     */
    @GetMapping("/user/export")
	@Timed
	public ResponseEntity<InputStreamResource> download(HttpServletResponse response,
			@RequestParam(value="packId", required = false) Long packId,
    		@RequestParam(value="purchaseOrderId", required = false) Long purchaseOrderId,
    		@RequestParam(value="projectId", required = false) Long projectId) throws IOException {
		log.info("REST request to export template : {}", projectId);
		InputStreamResource in = this.userService.exportExcel(projectId, purchaseOrderId, packId);
		HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=members_management_export.xlsx");

		 return ResponseEntity
	                .ok()
	                .headers(headers)
	                .body(in);
	}

    /**
     * GET /users/:login/member : get the "login" user.
     *
     * @param login the login of the user to find
     * @return the ResponseEntity with status 200 (OK) and with body the "login" user, or with status 404 (Not Found)
     */
    @GetMapping("/users/{login}/member")
    @Timed
    public ResponseEntity<User> getOneUserByLogin(@PathVariable String login) {
    	log.debug("REST getOneUserByLogin:", login);
        User user = userService.getOneUserByLogin(login);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(user));
    }

    /**
     * @return a string list of the all of the roles
     */
    @GetMapping("/users/authorities")
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public List<SelectDTO> getAuthorities() {
        return userService.getAuthorities();
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

    /**
     * DELETE /users/:login : delete the "login" User.
     *
     * @param login the login of the user to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/users/{login:" + Constants.LOGIN_REGEX + "}")
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<Void> deleteUser(@PathVariable String login) {
        log.debug("REST request to delete User: {}", login);
        try {
        	userService.deleteUser(login);
        	return ResponseEntity.ok().headers(HeaderUtil.createAlert( "userManagement.deleted", login)).build();
        } catch(Exception e) {
        	return ResponseEntity.badRequest().headers(HeaderUtil.serviceFailureAlert(ENTITY_NAME, AppConstants.IN_USED, "A user cannot delete")).build();
        }
    }

    @GetMapping("/users-search")
    @Timed
    public ResponseEntity<List<User>> findUsersBySearch(@RequestParam String query, Pageable pageable) {
    	log.debug("REST getAllUsersForProjectUser:", query);
        final Page<User> list = userService.findUsersBySearch(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, list, "/api/users-search");
        return new ResponseEntity<>(list.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/users-search-mail")
    @Timed
    public ResponseEntity<List<User>> findAllUsersBySearch(@RequestParam String query) {
    	log.debug("REST getAllUsersForProjectUser:", query);
        final List<User> list = userService.findAllUsersBySearch(query);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    /**
     * SEARCH /_search/users/:query : search for the User corresponding
     * to the query.
     *
     * @param query the query to search
     * @return the result of the search
     */
    @GetMapping("/_search/users")
    @Timed
    public ResponseEntity<List<UserDTO>> search(@RequestParam String query, Pageable pageable) {
    	log.debug("REST request to search for a page of Users for query {}", query);
        Page<UserDTO> page = userService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/users");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * SEARCH /_search/users/:query : search for the User corresponding
     * to the query.
     *
     * @param query the query to search
     * @return the result of the search
     */
    @GetMapping("/_search/users-online")
    @Timed
    public ResponseEntity<List<Map<String, Object>>> searchMemberManagement(@RequestParam String query, Pageable pageable) {
    	log.debug("REST request to search for a page of Users for query {}", query);
    	List<Map<String, Object>> result = userService.searchMemberManagement(query, pageable);
    	int totalPage = Integer.parseInt(result.get(result.size() - 1).get("total").toString());
        result.remove(result.size() - 1);
    	Page<Map<String, Object>> page = new PageImpl<>(result, pageable, totalPage);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/users-online");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * IMPORT USER /users/import: import users from excel file
     *
     * @param file the file contains users need to import into the system
     * @return the message after import successfully ors fail
     * @author TuHP
     */
    @PostMapping("/users/import")
    public ResponseEntity<String> userImport(@RequestParam("excelFile") MultipartFile file) {
    	log.info("Import user from file {}", file.getOriginalFilename());
    	try {
    		int result = userService.importUser(file);
    		return ResponseEntity.ok().headers(HeaderUtil.importAlert("User Management", String.valueOf(result))).build();
		} catch (Exception e) {
			return ResponseEntity.badRequest().headers(HeaderUtil.serviceFailureAlert("User Management", AppConstants.IMPORT_FAILED, "Import was failed")).build();
		}
	}
}
