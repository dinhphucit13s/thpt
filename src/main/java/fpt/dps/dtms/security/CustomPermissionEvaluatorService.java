package fpt.dps.dtms.security;

import fpt.dps.dtms.domain.AuthorityResource;
import fpt.dps.dtms.repository.AuthorityResourceRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * Custom service to evaluate authorization requests
 * Usage:
 * '@PreAuthorize("@jhiAuth.hasPermission('USER_RESOURCE', 'GET')")'
 */
@Component("jhiAuth")
public class CustomPermissionEvaluatorService {

    private final Logger log = LoggerFactory.getLogger(CustomPermissionEvaluatorService.class);
    
    private final AuthorityResourceRepository authorityResourceRepository;
    
    public CustomPermissionEvaluatorService(AuthorityResourceRepository authorityResourceRepository) {
    	this.authorityResourceRepository = authorityResourceRepository;
    }

    /**
     * Evaluates permission for the given resource and access
     *
     * @param resource
     * @param access
     * @return
     */
    public boolean hasPermission(String resource, String access){
    	boolean hasPermission = false;
        CustomUserDetails user = getCurrentUser();
        if (user.getUsername().equalsIgnoreCase("admin")) {
        	hasPermission = true;
        } else {
        	hasPermission = user != null && hasAccessToResource(user, access, resource);;
        }
        return hasPermission;
    }

    /**
     * get the current user token from spring SecurityContextHolder
     * @return
     */
    private CustomUserDetails getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails tmp = (UserDetails) auth.getPrincipal();
        Set<AuthorityResource> sar = new HashSet<AuthorityResource>();
        for (GrantedAuthority authority: tmp.getAuthorities() ) {
        	Set<AuthorityResource> ar = authorityResourceRepository.findByAuthorityName(authority.getAuthority());
        	sar.addAll(ar);
        } 
        
        if (auth.getPrincipal() instanceof UserDetails) {
        	CustomUserDetails cud = new CustomUserDetails(tmp.getUsername(), tmp.getPassword(), tmp.getAuthorities(), sar); 
        			 
            return cud;
        } else {
            throw new AccessDeniedException("No Valid User Found");
        }
    }

    /**
     * checks if the resource has grants in any roles for the user
     *
     * @param user
     * @param access
     * @param resourceName
     * @return
     */
    private boolean hasAccessToResource(CustomUserDetails user, String access, String resourceName) {

        final boolean[] hasAccess = {false};
        Set<AuthorityResource> resources = user.getResources();
        if (resources != null && !resources.isEmpty()){
            resources.forEach(resource -> {
                if (hasGrants(resource, resourceName, access)){
                    log.debug("Role has access to resources: {} ", resourceName);
                    hasAccess[0] = true;
                }
            });
        }
        return hasAccess[0];
    }

    /**
     * checks for grant for HTTP methods
     * 0 = NONE
     * 1 = VIEW,
     * 2 = CREATE,
     * 3 = EDIT,
     * 4 = DELETE
     *
     * @param resource
     * @param resourceName
     * @param access
     * @return
     */
    private boolean hasGrants(AuthorityResource resource, String resourceName, String access) {

        int grant;
        switch (access) {
            case "VIEW":
                grant = 1;
                break;
            case "CREATE":
                grant = 2;
                break;
            case "EDIT":
                grant = 3;
                break;
            case "DELETE":
                grant = 4;
                break;
            default:
                grant = 0;
        }
        
        if(resource.getName().contentEquals(resourceName)){
        	return resource.getPermission() >= grant;
        }
        
//        return true;
        return false;
    }
}