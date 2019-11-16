package fpt.dps.dtms.service.mapper;

import fpt.dps.dtms.domain.*;
import fpt.dps.dtms.service.dto.LoginTrackingDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity LoginTracking and its DTO LoginTrackingDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface LoginTrackingMapper extends EntityMapper<LoginTrackingDTO, LoginTracking> {



    default LoginTracking fromId(Long id) {
        if (id == null) {
            return null;
        }
        LoginTracking loginTracking = new LoginTracking();
        loginTracking.setId(id);
        return loginTracking;
    }
}
