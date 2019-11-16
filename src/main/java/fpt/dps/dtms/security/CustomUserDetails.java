package fpt.dps.dtms.security;

import fpt.dps.dtms.domain.AuthorityResource;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.Set;

/**
 * Created by Deepu on 11-02-2016.
 */
public class CustomUserDetails extends User {

    private Set<AuthorityResource> resources;

    public CustomUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities, Set<AuthorityResource> resources) {
        super(username, password, authorities);
        this.resources = resources;
    }

    public Set<AuthorityResource> getResources() {
        return resources;
    }

    public void setResources(Set<AuthorityResource> resources) {
        this.resources = resources;
    }
}
