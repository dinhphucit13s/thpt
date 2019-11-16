package fpt.dps.dtms.service.mapper;

import fpt.dps.dtms.domain.AuthorityResource;
import fpt.dps.dtms.service.dto.AuthorityResourceDTO;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2019-10-04T10:12:01+0700",
    comments = "version: 1.2.0.Final, compiler: Eclipse JDT (IDE) 1.2.100.v20160418-1457, environment: Java 1.8.0_111 (Oracle Corporation)"
)
@Component
public class AuthorityResourceMapperImpl implements AuthorityResourceMapper {

    @Override
    public AuthorityResource toEntity(AuthorityResourceDTO dto) {
        if ( dto == null ) {
            return null;
        }

        AuthorityResource authorityResource = new AuthorityResource();

        authorityResource.setCreatedBy( dto.getCreatedBy() );
        authorityResource.setCreatedDate( dto.getCreatedDate() );
        authorityResource.setLastModifiedBy( dto.getLastModifiedBy() );
        authorityResource.setLastModifiedDate( dto.getLastModifiedDate() );
        authorityResource.setId( dto.getId() );
        authorityResource.setName( dto.getName() );
        authorityResource.setPermission( dto.getPermission() );
        authorityResource.setAuthorityName( dto.getAuthorityName() );

        return authorityResource;
    }

    @Override
    public AuthorityResourceDTO toDto(AuthorityResource entity) {
        if ( entity == null ) {
            return null;
        }

        AuthorityResourceDTO authorityResourceDTO = new AuthorityResourceDTO();

        authorityResourceDTO.setCreatedBy( entity.getCreatedBy() );
        authorityResourceDTO.setCreatedDate( entity.getCreatedDate() );
        authorityResourceDTO.setLastModifiedBy( entity.getLastModifiedBy() );
        authorityResourceDTO.setLastModifiedDate( entity.getLastModifiedDate() );
        authorityResourceDTO.setId( entity.getId() );
        authorityResourceDTO.setName( entity.getName() );
        authorityResourceDTO.setPermission( entity.getPermission() );
        authorityResourceDTO.setAuthorityName( entity.getAuthorityName() );

        return authorityResourceDTO;
    }

    @Override
    public List<AuthorityResource> toEntity(List<AuthorityResourceDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<AuthorityResource> list = new ArrayList<AuthorityResource>( dtoList.size() );
        for ( AuthorityResourceDTO authorityResourceDTO : dtoList ) {
            list.add( toEntity( authorityResourceDTO ) );
        }

        return list;
    }

    @Override
    public List<AuthorityResourceDTO> toDto(List<AuthorityResource> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<AuthorityResourceDTO> list = new ArrayList<AuthorityResourceDTO>( entityList.size() );
        for ( AuthorityResource authorityResource : entityList ) {
            list.add( toDto( authorityResource ) );
        }

        return list;
    }
}
