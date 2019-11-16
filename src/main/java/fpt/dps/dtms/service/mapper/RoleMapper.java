package fpt.dps.dtms.service.mapper;

import org.mapstruct.*;

import fpt.dps.dtms.domain.Authority;
import fpt.dps.dtms.domain.AuthorityResource;
import fpt.dps.dtms.service.dto.AuthorityResourceDTO;
import fpt.dps.dtms.web.rest.vm.AuthorityVM;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {})
public interface RoleMapper {
	AuthorityVM authorityToAuthorityVM(Authority authority);

    List<AuthorityVM> authoritiesToAuthorityVMs(List<Authority> authorities);

    List<Authority> authorityDTOsToAuthorities(List<AuthorityVM> authorityDTOs);

    AuthorityResourceDTO resourceToAuthorityResourceDTO(AuthorityResource resource);

    Set<AuthorityResourceDTO> resourcesToAuthorityResourceDTOs(Set<AuthorityResource> resources);

    default Authority authorityDTOToAuthority(AuthorityVM authorityDTO) {
    	Authority auth = new Authority();
    	auth.setName(authorityDTO.getName());
    	return auth;
    }

    default Set<AuthorityResource> resoursesFromAuthorityResourceDTOs(Set<AuthorityResourceDTO> resourceDTOs, Authority auth) {
        return resourceDTOs.stream().map(resourceDTO -> {
            return resourceDTOToResource(resourceDTO, auth);
        }).collect(Collectors.toSet());
    }

    default AuthorityResource resourceDTOToResource(AuthorityResourceDTO resourceDTO, Authority auth) {
    	AuthorityResource res = new AuthorityResource();
        res.setId(resourceDTO.getId());
        res.setName(resourceDTO.getName());
        res.setPermission(resourceDTO.getPermission());
        res.setAuthorityName(auth.getName());
        return res;
    }
}