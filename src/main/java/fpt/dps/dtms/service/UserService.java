package fpt.dps.dtms.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import fpt.dps.dtms.config.Constants;
import fpt.dps.dtms.domain.Authority;
import fpt.dps.dtms.domain.BusinessUnit;
import fpt.dps.dtms.domain.BusinessUnitManager;
import fpt.dps.dtms.domain.ProjectUsers;
import fpt.dps.dtms.domain.User;
import fpt.dps.dtms.domain.UserProfile;
import fpt.dps.dtms.domain.PurchaseOrders;
import fpt.dps.dtms.domain.Projects;
import fpt.dps.dtms.repository.AuthorityRepository;
import fpt.dps.dtms.repository.TaskTrackingTimeRepository;
import fpt.dps.dtms.repository.UserRepository;
import fpt.dps.dtms.repository.search.UserProfileSearchRepository;
import fpt.dps.dtms.repository.search.UserSearchRepository;
import fpt.dps.dtms.security.AuthoritiesConstants;
import fpt.dps.dtms.security.SecurityUtils;
import fpt.dps.dtms.service.dto.BusinessUnitManagerDTO;
import fpt.dps.dtms.service.dto.LoginTrackingDTO;
import fpt.dps.dtms.service.dto.ProjectUsersDTO;
import fpt.dps.dtms.service.dto.SelectDTO;
import fpt.dps.dtms.service.dto.UserDTO;
import fpt.dps.dtms.service.dto.msc.UserMSCDTO;
import fpt.dps.dtms.service.mapper.ProjectUsersMapper;
import fpt.dps.dtms.service.mapper.UserMapper;
import fpt.dps.dtms.service.mapper.msc.UserMSCMapper;
import fpt.dps.dtms.service.util.ExcelGenerator;
import fpt.dps.dtms.service.util.RandomUtil;
import fpt.dps.dtms.web.rest.errors.EmailAlreadyUsedException;
import fpt.dps.dtms.web.rest.errors.LoginAlreadyUsedException;
import fpt.dps.dtms.web.rest.vm.TimeZoneVM;

/**
 * Service class for managing users.
 */
@Service
@Transactional
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserSearchRepository userSearchRepository;

    private final UserProfileService userProfileService;

    private final AuthorityRepository authorityRepository;

    private final LoginTrackingQueryService loginTrackingQueryService;
    
    private final TimeZoneService timeZoneService;

    private final CacheManager cacheManager;

    private final ExcelGenerator excelGenerator;

    private final UserMapper userMapper;
    
    private final UserMSCMapper userMSCMapper;

    private final ProjectUsersMapper projectUsersMapper;
    
    private final UserProfileSearchRepository userProfileSearchRepository;
    
    private final TaskTrackingTimeRepository taskTrackingTimeRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserSearchRepository userSearchRepository, AuthorityRepository authorityRepository, CacheManager cacheManager,
    		ExcelGenerator excelGenerator, LoginTrackingQueryService loginTrackingQueryService, UserMapper userMapper, ProjectUsersMapper projectUsersMapper,
    		UserProfileService userProfileService, UserProfileSearchRepository userProfileSearchRepository, TaskTrackingTimeRepository taskTrackingTimeRepository, TimeZoneService timeZoneService,
    		UserMSCMapper userMSCMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userSearchRepository = userSearchRepository;
        this.authorityRepository = authorityRepository;
        this.cacheManager = cacheManager;
        this.excelGenerator = excelGenerator;
        this.loginTrackingQueryService = loginTrackingQueryService;
        this.userMapper = userMapper;
        this.projectUsersMapper = projectUsersMapper;
        this.userProfileService = userProfileService;
        this.timeZoneService = timeZoneService;
        this.userProfileSearchRepository = userProfileSearchRepository;
        this.taskTrackingTimeRepository = taskTrackingTimeRepository;
        this.userMSCMapper = userMSCMapper;
    }

    public Optional<User> activateRegistration(String key) {
        log.debug("Activating user for activation key {}", key);
        return userRepository.findOneByActivationKey(key)
            .map(user -> {
                // activate given user for the registration key.
                user.setActivated(true);
                user.setActivationKey(null);
                userSearchRepository.save(user);
                cacheManager.getCache(UserRepository.USERS_BY_LOGIN_CACHE).evict(user.getLogin());
                cacheManager.getCache(UserRepository.USERS_BY_EMAIL_CACHE).evict(user.getEmail());
                log.debug("Activated user: {}", user);
                return user;
            });
    }

    public Optional<User> completePasswordReset(String newPassword, String key) {
       log.debug("Reset user password for reset key {}", key);

       return userRepository.findOneByResetKey(key)
           .filter(user -> user.getResetDate().isAfter(Instant.now().minusSeconds(86400)))
           .map(user -> {
                user.setPassword(passwordEncoder.encode(newPassword));
                user.setResetKey(null);
                user.setResetDate(null);
                cacheManager.getCache(UserRepository.USERS_BY_LOGIN_CACHE).evict(user.getLogin());
                cacheManager.getCache(UserRepository.USERS_BY_EMAIL_CACHE).evict(user.getEmail());
                return user;
           });
    }

    public Optional<User> requestPasswordReset(String mail) {
        return userRepository.findOneByEmailIgnoreCase(mail)
            .filter(User::getActivated)
            .map(user -> {
                user.setResetKey(RandomUtil.generateResetKey());
                user.setResetDate(Instant.now());
                cacheManager.getCache(UserRepository.USERS_BY_LOGIN_CACHE).evict(user.getLogin());
                cacheManager.getCache(UserRepository.USERS_BY_EMAIL_CACHE).evict(user.getEmail());
                return user;
            });
    }

    public User registerUser(UserDTO userDTO, String password) {

        User newUser = new User();
        Authority authority = authorityRepository.findOne(AuthoritiesConstants.USER);
        Set<Authority> authorities = new HashSet<>();
        String encryptedPassword = passwordEncoder.encode(password);
        newUser.setLogin(userDTO.getLogin());
        // new user gets initially a generated password
        newUser.setPassword(encryptedPassword);
        newUser.setFirstName(userDTO.getFirstName());
        newUser.setLastName(userDTO.getLastName());
        newUser.setEmail(userDTO.getEmail());
        newUser.setImageUrl(userDTO.getImageUrl());
        newUser.setLangKey(userDTO.getLangKey());
        // new user is not active
        newUser.setActivated(false);
        // new user gets registration key
        newUser.setActivationKey(RandomUtil.generateActivationKey());
        authorities.add(authority);
        newUser.setAuthorities(authorities);
        userRepository.save(newUser);
        userSearchRepository.save(newUser);
        cacheManager.getCache(UserRepository.USERS_BY_LOGIN_CACHE).evict(newUser.getLogin());
        cacheManager.getCache(UserRepository.USERS_BY_EMAIL_CACHE).evict(newUser.getEmail());
        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }

    public User createUser(UserDTO userDTO) {
        User user = new User();
        UserProfile userProfile = new UserProfile();
        user.setLogin(userDTO.getLogin().toLowerCase());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setImageUrl(userDTO.getImageUrl());
        user.setAbout(userDTO.getAbout());
        user.setPhone(userDTO.getPhone());
        user.setDob(userDTO.getDob());
        BusinessUnit businessUnit = new BusinessUnit();
        businessUnit.setId(userDTO.getBusinessUnitId());
        user.setBusinessUnit(businessUnit);
        user.setContractType(userDTO.getContractType());
        if (userDTO.getLangKey() == null) {
            user.setLangKey(Constants.DEFAULT_LANGUAGE); // default language
        } else {
            user.setLangKey(userDTO.getLangKey());
        }
        //if (userDTO.getAuthorities() != null) {
        Set<String> _authorities = userDTO.getAuthorities();
        if(!_authorities.contains(AuthoritiesConstants.USER)){
        	_authorities.add(AuthoritiesConstants.USER);
        }
        Set<Authority> authorities = userDTO.getAuthorities().stream()
            .map(authorityRepository::findOne)
            .collect(Collectors.toSet());
        user.setAuthorities(authorities);
        String encryptedPassword = passwordEncoder.encode(userDTO.getPassword());
        user.setPassword(encryptedPassword);
        user.setResetKey(RandomUtil.generateResetKey());
        user.setResetDate(Instant.now());
        user.setActivated(true);        
        userRepository.save(user);
        userSearchRepository.save(user);
        //save userProfile
        userProfile = userDTO.getUserProfile() == null ? userProfile : userDTO.getUserProfile();        
        userProfile.setUser(user);
        userProfileService.save(userProfile);
        userProfileSearchRepository.save(userProfile);
        cacheManager.getCache(UserRepository.USERS_BY_LOGIN_CACHE).evict(user.getLogin());
        cacheManager.getCache(UserRepository.USERS_BY_EMAIL_CACHE).evict(user.getEmail());
        log.debug("Created Information for User: {}", user);
        return user;
    }

    /**
     * Update basic information (first name, last name, email, language) for the current user.
     *
     * @param firstName first name of user
     * @param lastName last name of user
     * @param email email id of user
     * @param langKey language key
     * @param imageUrl image URL of user
     */
    public void updateUserProfile(UserDTO userDTO) {
        SecurityUtils.getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .ifPresent(user -> {
                user.setFirstName(userDTO.getFirstName());
                user.setLastName(userDTO.getLastName());
                user.setEmail(userDTO.getEmail());
                user.setLangKey(userDTO.getLangKey());
                user.setDob(userDTO.getDob());
                user.setPhone(userDTO.getPhone());
                user.setAbout(userDTO.getAbout());
                userSearchRepository.save(user);
                
                // save userProfile
                UserProfile userProfile = new UserProfile(); 
                userProfile = userDTO.getUserProfile();
                if (userProfile != null) {
                	userProfile.setUser(user);
                } else {
                	userProfile = new UserProfile();
                	userProfile.setUser(user);
                }
                userProfileService.save(userProfile);
                
                cacheManager.getCache(UserRepository.USERS_BY_LOGIN_CACHE).evict(user.getLogin());
                cacheManager.getCache(UserRepository.USERS_BY_EMAIL_CACHE).evict(user.getEmail());
                log.debug("Changed Information for User: {}", user);
            });
    }

    /**
     * Update all information for a specific user, and return the modified user.
     *
     * @param userDTO user to update
     * @return updated user
     */
    public Optional<UserDTO> updateUser(UserDTO userDTO) {
        return Optional.of(userRepository
            .findOne(userDTO.getId()))
            .map(user -> {
                user.setLogin(userDTO.getLogin());
                user.setFirstName(userDTO.getFirstName());
                user.setLastName(userDTO.getLastName());
                user.setEmail(userDTO.getEmail());
                user.setImageUrl(userDTO.getImageUrl());
                user.setActivated(userDTO.isActivated());
                user.setLangKey(userDTO.getLangKey());
                user.setAbout(userDTO.getAbout());
                user.setPhone(userDTO.getPhone());
                user.setDob(userDTO.getDob());
                BusinessUnit businessUnit = new BusinessUnit();
                businessUnit.setId(userDTO.getBusinessUnitId());
                user.setBusinessUnit(businessUnit);
                user.setContractType(userDTO.getContractType());
                Set<Authority> managedAuthorities = user.getAuthorities();
                managedAuthorities.clear();
                userDTO.getAuthorities().stream()
                    .map(authorityRepository::findOne)
                    .forEach(managedAuthorities::add);
                userSearchRepository.save(user);
                
                // save userProfile
                UserProfile userProfile = new UserProfile(); 
                userProfile = userDTO.getUserProfile();
                if (userProfile != null) {
                	userProfile.setUser(user);
                } else {
                	userProfile = userProfileService.findByUserId(userDTO.getId());
                	if(userProfile == null) {
                		userProfile = new UserProfile();
                	}
                	userProfile.setUser(user);
                }
                userProfileService.save(userProfile);

                UserProfile userProfileDate = userProfileService.findByUserId(userDTO.getId());
                Instant dateOfUser = Instant.parse(user.getLastModifiedDate().toString());
                Instant dateOfUserPro = Instant.parse(userProfileDate.getLastModifiedDate().toString());
                int valueDate = dateOfUser.compareTo(dateOfUserPro); 
                if (valueDate < 0) {
                	user.setLastModifiedDate(userProfileDate.getLastModifiedDate());
                	userSearchRepository.save(user);
                }
                cacheManager.getCache(UserRepository.USERS_BY_LOGIN_CACHE).evict(user.getLogin());
                cacheManager.getCache(UserRepository.USERS_BY_EMAIL_CACHE).evict(user.getEmail());
                log.debug("Changed Information for User: {}", user);
                return user;
            })
            .map(UserDTO::new);
        
    }

    public void deleteUser(String login) {
        userRepository.findOneByLogin(login).ifPresent(user -> {
            userRepository.delete(user);
            userSearchRepository.delete(user);
            cacheManager.getCache(UserRepository.USERS_BY_LOGIN_CACHE).evict(user.getLogin());
            cacheManager.getCache(UserRepository.USERS_BY_EMAIL_CACHE).evict(user.getEmail());
            log.debug("Deleted User: {}", user);
        });
    }

    public void changePassword(String password) {
        SecurityUtils.getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .ifPresent(user -> {
                String encryptedPassword = passwordEncoder.encode(password);
                user.setPassword(encryptedPassword);
                cacheManager.getCache(UserRepository.USERS_BY_LOGIN_CACHE).evict(user.getLogin());
                cacheManager.getCache(UserRepository.USERS_BY_EMAIL_CACHE).evict(user.getEmail());
                log.debug("Changed password for User: {}", user);
            });
    }

    public void changePasswordUser(String password, String userChangePass) {
    	User user = userRepository.findByLogin(userChangePass);
                String encryptedPassword = passwordEncoder.encode(password);
                user.setPassword(encryptedPassword);
                cacheManager.getCache(UserRepository.USERS_BY_LOGIN_CACHE).evict(user.getLogin());
                cacheManager.getCache(UserRepository.USERS_BY_EMAIL_CACHE).evict(user.getEmail());
                log.debug("Changed password for User: {}", user);
    }

    public void changeAvatar(String avatarURL) {
        SecurityUtils.getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .ifPresent(user -> {
            	user.setImageUrl(avatarURL);
                cacheManager.getCache(UserRepository.USERS_BY_LOGIN_CACHE).evict(user.getLogin());
                cacheManager.getCache(UserRepository.USERS_BY_EMAIL_CACHE).evict(user.getEmail());
                log.debug("Changed password for User: {}", user);
            });
    }

    public void updateStatus(String login, boolean status) {
    	userRepository.findOneByLogin(login).ifPresent(user -> {
            user.setStatus(status);
            cacheManager.getCache(UserRepository.USERS_BY_LOGIN_CACHE).evict(user.getLogin());
            cacheManager.getCache(UserRepository.USERS_BY_EMAIL_CACHE).evict(user.getEmail());
            log.debug("Changed status for User: {}", user);
        });
    }

    @Transactional(readOnly = true)
    public Page<User> findUsersBySearch(String search, Pageable pageable) {
        return userRepository.findUsersBySearch(search, pageable);
    }

    @Transactional(readOnly = true)
    public List<User> findAllUsersBySearch(String search) {
        return userRepository.findAllUsersBySearch(search);
    }

    @Transactional(readOnly = true)
    public Optional<User> getOneUserByLogin(String login, String roleName) {
        return userRepository.findOneByLoginAndAuthoritiesName(login, roleName);
    }

    public User getOneUserByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    @Transactional(readOnly = true)
    public Page<UserDTO> getAllManagedUsers(Pageable pageable) {
        return userRepository.findAllByLoginNot(pageable, Constants.ANONYMOUS_USER).map(UserDTO::new);
    }
    
    @Transactional(readOnly = true)
    public List<UserDTO> getAllManagedUsers() {
        return this.userMapper.usersToUserDTOs(userRepository.findAllByLoginNot(Constants.ANONYMOUS_USER));
    }

    /**
     * get all User for MSC
     * @return
     */
    @Transactional(readOnly = true)
    public List<UserMSCDTO> getAllManagedUsersForMSC() {
        return this.userMSCMapper.usersToUserMSCDTOs(userRepository.findAllByLoginNot(Constants.ANONYMOUS_USER));
    }

    /*@Transactional(readOnly = true)
    public List<SelectDTO> getAllUsersForSelects() {
    	PurchaseOrders purchaseOrders = purchaseOrdersRepository.findOne(poID);
		Projects project = purchaseOrders.getProject();
    	List<User> userList = userRepository.findAllByLoginNot(Constants.ANONYMOUS_USER);
    	List<SelectDTO> result = userList.stream().map(user ->{
        	SelectDTO obj = new SelectDTO();
        	obj.setId(user.getId());
        	obj.setName(user.getLogin());
        	return obj;
        }).collect(Collectors.toList());
        return result;
    }*/

    @Transactional(readOnly = true)
    public List<User> getAllManagedUsersForProjectUser(Long id) {
        return userRepository.finnAllByNotInProjectUser(id);
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> getAllUserByProjectId(Long id, Pageable pageable) {
    	Page<User> pageUser = userRepository.findAllUserByProjectId(id, pageable);
        return getMemberManagement(pageUser);
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> getMemberManagement(Page<User> pageUser) {
    	List<User> listUser = pageUser.getContent();
    	List<Map<String, Object>> result = listUser.stream().map(user ->{
    		Map<String, Object> object = new HashMap<String, Object>();

    		Instant timeNow = new Date().toInstant();
    		timeNow = timeNow.atZone(ZoneOffset.UTC).withHour(0).withMinute(0).withSecond(0).withNano(0).toInstant();
    		LoginTrackingDTO loginTrackingDTO = loginTrackingQueryService.getTimeLoginByUser(user.getLogin(), timeNow);
    		object.put("login", user.getLogin());
    		object.put("firstName", user.getFirstName());
    		object.put("lastName", user.getLastName());
    		object.put("email", user.getEmail());
    		object.put("status", user.isStatus());
    		if (loginTrackingDTO != null) {
    			object.put("startTime", loginTrackingDTO.getStartTime());
    		} else {
    			object.put("startTime", null);
    		}
    		return object;
    	}).collect(Collectors.toList());
    	Map<String, Object> totalPages = new HashMap<String, Object>();
        totalPages.put("total", pageUser.getTotalElements());
        result.add(totalPages);
        return result;
    }

    /**
	 * Create excel file for member management
	 *
	 * @param packageID
	 * @throws IOException
	 */
	public InputStreamResource exportExcel(Long projectId, Long purchaseOrderId, Long packageId) throws IOException {
		/*List<User> listUser = userRepository.getListUserByProjectId(projectId);*/
		List<Map<String, Object>> result = this.getAllUserByCondition(projectId, purchaseOrderId, packageId, null);
		// Remove last index
		if (result.size() > 0) {
			result.remove(result.size()-1);
		}
		
		if(CollectionUtils.isNotEmpty(result)) {
				ByteArrayInputStream in = excelGenerator.exportDataToExcel(result);
				return new InputStreamResource(in);
		}else {
			return null;
		}
	}

    @Transactional(readOnly = true)
    public UserDTO getUserWithAuthoritiesByLogin(String login) {
    	Optional<User> optUser = userRepository.findOneWithAuthoritiesByLogin(login);
    	UserDTO userDto = null;
    	if(optUser.isPresent()) {
    		userDto = userMapper.userToUserDTO(optUser.get());
    		UserProfile userProfile = userProfileService.findByUserId(userDto.getId());
            userDto.setUserProfile(userProfile);
           
    	}
        return userDto;
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthorities(Long id) {
        return userRepository.findOneWithAuthoritiesById(id);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthorities() {
        return SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneWithAuthoritiesByLogin);
    }

    /**
     * Not activated users should be automatically deleted after 3 days.
     * <p>
     * This is scheduled to get fired everyday, at 01:00 (am).
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void removeNotActivatedUsers() {
        List<User> users = userRepository.findAllByActivatedIsFalseAndCreatedDateBefore(Instant.now().minus(3, ChronoUnit.DAYS));
        for (User user : users) {
            log.debug("Deleting not activated user {}", user.getLogin());
            userRepository.delete(user);
            userSearchRepository.delete(user);
            cacheManager.getCache(UserRepository.USERS_BY_LOGIN_CACHE).evict(user.getLogin());
            cacheManager.getCache(UserRepository.USERS_BY_EMAIL_CACHE).evict(user.getEmail());
        }
    }

    /**
     * @return a list of all the authorities
     */
    public List<SelectDTO> getAuthorities() {
    	List<String> listAuthorities = authorityRepository.findAll().stream().map(Authority::getName).collect(Collectors.toList());
    	List<SelectDTO> result = listAuthorities.stream().map(user ->{
        	SelectDTO obj = new SelectDTO();
        	obj.setId(null);
        	obj.setName(user);
        	return obj;
        }).collect(Collectors.toList());
        return result;
    }

    /**
     * Check user input correct password or not
     * @param user
     * @param oldPassword
     * @return true/false
     */
    public boolean checkIfValidOldPassword(final User user, final String oldPassword) {
        return passwordEncoder.matches(oldPassword, user.getPassword());
    }

    /**
     * Import users from excel into database
     * @param file: The file contains users
     * @return the number of user was imported.
     * @throws IOException
     * @author TuHP
     */
	public int importUser(MultipartFile file) throws IOException {
		List<UserDTO> userDTOs = this.excelGenerator.getUsersFromFile(file);
		if(userDTOs.size() > 0) {
			for (UserDTO userDTO : userDTOs) {
				Optional<User> existingUser = userRepository.findOneByLoginOrEmailIgnoreCase(userDTO.getLogin().toLowerCase(), userDTO.getEmail());
		        if (existingUser.isPresent()) {
		            continue;
		        }
				this.createUser(userDTO);
			}
		}
		return userDTOs.size();
	}

	public List<ProjectUsersDTO> getUserActivatedByProject(Long projectId) {
		return this.projectUsersMapper.toDto(this.userRepository.findUserActivatedByProject(projectId));
	}

	@Transactional(readOnly = true)
    public Page<UserDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Users for query {}", query);
        return userSearchRepository.findByLoginLike(query, pageable).map(UserDTO::new);
    }

	@Transactional(readOnly = true)
    public List<Map<String, Object>> searchMemberManagement(String query, Pageable pageable) {
        log.debug("Request to search for a page of Users for query {}", query);
        Page<User> pageUser = userSearchRepository.findByLoginLike(query, pageable);
        return getMemberManagement(pageUser);
    }

	public List<Map<String, Object>> getAllUserByCondition(Long projectId, Long purchaseOrderId, Long packageId,
			Pageable pageable) {
		Set<String> listUserName = new HashSet<>();
		List<String> roundOp;
    	List<String> roundRV1;
    	List<String> roundRV2;
    	List<String> roundFixer;
    	List<String> roundFi;
    	if (purchaseOrderId != null) {
    		roundOp = taskTrackingTimeRepository.getAllUserWithRoleOpByPurchaseOrderId(purchaseOrderId);
    		roundRV1 = taskTrackingTimeRepository.getAllUserWithRoleReview1ByPurchaseOrderId(purchaseOrderId);
    		roundRV2 = taskTrackingTimeRepository.getAllUserWithRoleReview2ByPurchaseOrderId(purchaseOrderId);
    		roundFixer = taskTrackingTimeRepository.getAllUserWithRoleFixerByPurchaseOrderId(purchaseOrderId);
    		roundFi = taskTrackingTimeRepository.getAllUserWithRoleFiByPurchaseOrderId(purchaseOrderId);
    	}
    	else if (packageId != null) {
    		roundOp = taskTrackingTimeRepository.getAllUserWithRoleOpByPackageId(packageId);
    		roundRV1 = taskTrackingTimeRepository.getAllUserWithRoleReview1ByPackageId(packageId);
    		roundRV2 = taskTrackingTimeRepository.getAllUserWithRoleReview2ByPackageId(packageId);
    		roundFixer = taskTrackingTimeRepository.getAllUserWithRoleFixerByPackageId(packageId);
    		roundFi = taskTrackingTimeRepository.getAllUserWithRoleFiByPackageId(packageId);
    	} else {
    		Page<User> pageUser = userRepository.findAllUserByProjectId(projectId, pageable);
            return getMemberManagement(pageUser);
    	}
    	
    	listUserName.addAll(roundOp);
    	listUserName.addAll(roundRV1);
    	listUserName.addAll(roundRV2);
    	listUserName.addAll(roundFixer);
    	listUserName.addAll(roundFi);
    	listUserName.removeIf(u -> u == null);
    	
    	// get User by userLogin
    	List<User> listUsers = new ArrayList<>();
    	listUserName.forEach(u -> {
            User user = this.getOneUserByLogin(u.toString().toLowerCase());
            if (user != null) {
            	listUsers.add(user);
            }
        });
    	Page<User> pageUsers = new PageImpl<User>(listUsers, pageable, listUsers.size());
    	
		return getMemberManagement(pageUsers);
	}
}
