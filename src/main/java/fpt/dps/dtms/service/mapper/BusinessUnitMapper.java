package fpt.dps.dtms.service.mapper;

import fpt.dps.dtms.domain.*;
import fpt.dps.dtms.service.dto.BusinessUnitDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity BusinessUnit and its DTO BusinessUnitDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface BusinessUnitMapper extends EntityMapper<BusinessUnitDTO, BusinessUnit> {



    default BusinessUnit fromId(Long id) {
        if (id == null) {
            return null;
        }
        BusinessUnit businessUnit = new BusinessUnit();
        businessUnit.setId(id);
        return businessUnit;
    }
}
