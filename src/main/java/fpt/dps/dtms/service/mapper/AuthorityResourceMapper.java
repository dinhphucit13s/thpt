package fpt.dps.dtms.service.mapper;

import fpt.dps.dtms.domain.*;
import fpt.dps.dtms.service.dto.AuthorityResourceDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity AuthorityResource and its DTO AuthorityResourceDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface AuthorityResourceMapper extends EntityMapper<AuthorityResourceDTO, AuthorityResource> {



    default AuthorityResource fromId(Long id) {
        if (id == null) {
            return null;
        }
        AuthorityResource authorityResource = new AuthorityResource();
        authorityResource.setId(id);
        return authorityResource;
    }
}
