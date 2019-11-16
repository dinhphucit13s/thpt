package fpt.dps.dtms.service.mapper;

import org.mapstruct.Mapper;

import fpt.dps.dtms.domain.BusinessUnit;
import fpt.dps.dtms.service.dto.BusinessUnitDTO;
import fpt.dps.dtms.service.dto.BusinessUnitMSCDTO;

/**
 * Mapper for the entity BusinessUnit and its DTO BusinessUnitDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface BusinessUnitMSCMapper extends EntityMapper<BusinessUnitMSCDTO, BusinessUnit>{
	default BusinessUnit fromId(Long id) {
        if (id == null) {
            return null;
        }
        BusinessUnit businessUnit = new BusinessUnit();
        businessUnit.setId(id);
        return businessUnit;
    }
}
