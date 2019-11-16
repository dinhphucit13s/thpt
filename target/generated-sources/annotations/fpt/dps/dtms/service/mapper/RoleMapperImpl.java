package fpt.dps.dtms.service.mapper;

import fpt.dps.dtms.domain.Authority;
import fpt.dps.dtms.domain.AuthorityResource;
import fpt.dps.dtms.service.dto.AuthorityResourceDTO;
import fpt.dps.dtms.web.rest.vm.AuthorityVM;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2019-10-04T10:12:00+0700",
    comments = "version: 1.2.0.Final, compiler: Eclipse JDT (IDE) 1.2.100.v20160418-1457, environment: Java 1.8.0_111 (Oracle Corporation)"
)
@Component
public class RoleMapperImpl implements RoleMapper {

    @Override
    public AuthorityVM authorityToAuthorityVM(Authority authority) {
        if ( authority == null ) {
            return null;
        }

        AuthorityVM authorityVM = new AuthorityVM();

        authorityVM.setName( authority.getName() );

        return authorityVM;
    }

    @Override
    public List<AuthorityVM> authoritiesToAuthorityVMs(List<Authority> authorities) {
        if ( authorities == null ) {
            return null;
        }

        List<AuthorityVM> list = new ArrayList<AuthorityVM>( authorities.size() );
        for ( Authority authority : authorities ) {
            list.add( authorityToAuthorityVM( authority ) );
        }

        return list;
    }

    @Override
    public List<Authority> authorityDTOsToAuthorities(List<AuthorityVM> authorityDTOs) {
        if ( authorityDTOs == null ) {
            return null;
        }

        List<Authority> list = new ArrayList<Authority>( authorityDTOs.size() );
        for ( AuthorityVM authorityVM : authorityDTOs ) {
            list.add( authorityDTOToAuthority( authorityVM ) );
        }

        return list;
    }

    @Override
    public AuthorityResourceDTO resourceToAuthorityResourceDTO(AuthorityResource resource) {
        if ( resource == null ) {
            return null;
        }

        AuthorityResourceDTO authorityResourceDTO = new AuthorityResourceDTO();

        authorityResourceDTO.setCreatedBy( resource.getCreatedBy() );
        authorityResourceDTO.setCreatedDate( resource.getCreatedDate() );
        authorityResourceDTO.setLastModifiedBy( resource.getLastModifiedBy() );
        authorityResourceDTO.setLastModifiedDate( resource.getLastModifiedDate() );
        authorityResourceDTO.setId( resource.getId() );
        authorityResourceDTO.setName( resource.getName() );
        authorityResourceDTO.setPermission( resource.getPermission() );
        authorityResourceDTO.setAuthorityName( resource.getAuthorityName() );

        return authorityResourceDTO;
    }

    @Override
    public Set<AuthorityResourceDTO> resourcesToAuthorityResourceDTOs(Set<AuthorityResource> resources) {
        if ( resources == null ) {
            return null;
        }

        Set<AuthorityResourceDTO> set = new HashSet<AuthorityResourceDTO>( Math.max( (int) ( resources.size() / .75f ) + 1, 16 ) );
        for ( AuthorityResource authorityResource : resources ) {
            set.add( resourceToAuthorityResourceDTO( authorityResource ) );
        }

        return set;
    }
}
