package fpt.dps.dtms.service;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fpt.dps.dtms.domain.Authority;
import fpt.dps.dtms.domain.AuthorityResource;
import fpt.dps.dtms.repository.AuthorityRepository;
import fpt.dps.dtms.repository.AuthorityResourceRepository;
import fpt.dps.dtms.service.dto.AuthorityDTO;
import fpt.dps.dtms.service.dto.UserDTO;
import fpt.dps.dtms.service.mapper.AuthorityMapper;
import fpt.dps.dtms.service.mapper.AuthorityResourceMapper;
import fpt.dps.dtms.service.mapper.RoleMapper;
import fpt.dps.dtms.web.rest.errors.InternalServerErrorException;
import fpt.dps.dtms.web.rest.vm.AuthorityVM;
import fpt.dps.dtms.web.rest.vm.UserAuthorityVM;
import fpt.dps.dtms.security.AuthorityResourceConstants;


/**
 * Service Implementation for managing Environment.
 */
@Service
@Transactional
public class RoleService {

    private final Logger log = LoggerFactory.getLogger(RoleService.class);

    private final AuthorityRepository authrityRepository;
    
    private final AuthorityResourceRepository authorityResourceRepository;
    
    private final AuthorityMapper authorityMapper;
    
    
    private final UserService userService;


    public RoleService(AuthorityRepository authrityRepository, AuthorityResourceRepository authorityResourceRepository, AuthorityMapper authorityMapper, UserService userService) {
        this.authrityRepository = authrityRepository;
        this.authorityResourceRepository = authorityResourceRepository;
        this.authorityMapper = authorityMapper;
        this.userService = userService;
    }
    
    public AuthorityDTO save(AuthorityVM authorityDTO) {
        log.debug("Request to save Authority : {}", authorityDTO);
        Authority auth = new Authority();
        auth.setName(authorityDTO.getName());
        auth = authrityRepository.save(auth);
        
        Set<AuthorityResource> resources = new HashSet<AuthorityResource>();
        resources = authorityDTO.getResources();
        
        authorityResourceRepository.deleteAuthorityResourcesyByAuthorityName(authorityDTO.getName());
        
        for (AuthorityResource res: resources) {
        	authorityResourceRepository.save(res);
        }
        
        return authorityMapper.toDto(auth);
    }
    
    @Transactional(readOnly = true)
    public Page<AuthorityDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Environments");
        return authrityRepository.findAllNonSystemAuthority(pageable)
            .map(authorityMapper::toDto);
    }

    /**
     * Get one authority  by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public AuthorityVM findOne(String name) {
        log.debug("Request to get Authority Permission: {}", name);
        AuthorityVM auth = new AuthorityVM();
        auth.setName(name);
        Set<AuthorityResource> res = authorityResourceRepository.findByAuthorityName(name);
        auth.setResources(res);
     
        return auth;
    }
    
    @Transactional(readOnly = true)
    public UserAuthorityVM findPermissionOfCurrentUser() {
        log.debug("Request to get Authority Permission: current user");
        UserAuthorityVM auth = new UserAuthorityVM();
        
        UserDTO currentUser = userService.getUserWithAuthorities()
                .map(UserDTO::new)
                .orElseThrow(() -> new InternalServerErrorException("User could not be found"));
        
        auth.setUsername(currentUser.getLogin());
        Set<AuthorityResource> resources = new HashSet<AuthorityResource>();
        
        for (String resource: AuthorityResourceConstants.AUTHORITY_RESOURCES) {
        	AuthorityResource res = new AuthorityResource();
        	res.setName(resource);
        	res.setPermission(0);
        	res.setAuthorityName(currentUser.getLogin());
        	resources.add(res);
        }
    
        
        for (String authority: currentUser.getAuthorities()) {
        	Set<AuthorityResource> ress = authorityResourceRepository.findByAuthorityName(authority);
        	for (AuthorityResource sourceResource: ress) {
        		for (AuthorityResource destResource: resources) {
        			if(destResource.getName().equals(sourceResource.getName())) {
        				if (sourceResource.getPermission() > destResource.getPermission()) {
        					destResource.setPermission(sourceResource.getPermission());
        				}
        			}
        		}
        	}
        }
        
        auth.setResources(resources);
     
        return auth;
    }

    /**
     * Delete the environment by id.
     *
     * @param id the id of the entity
     */
    public void delete(String name) {
        log.debug("Request to delete Authority : {}", name);
        authrityRepository.delete(name);
        authorityResourceRepository.deleteAuthorityResourcesyByAuthorityName(name);
    }

}