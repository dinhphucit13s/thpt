package fpt.dps.dtms.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import fpt.dps.dtms.domain.BusinessUnitManager;
import fpt.dps.dtms.service.dto.BusinessUnitManagerMSCDTO;

/**
 * Mapper for the entity BusinessUnitManager and its DTO BusinessUnitManagerDTO.
 */
@Mapper(componentModel = "spring", uses = {BusinessUnitMapper.class, UserMapper.class})
public interface BusinessUnitManagerMSCMapper extends EntityMapper<BusinessUnitManagerMSCDTO, BusinessUnitManager>{
	@Mapping(source = "businessUnit.id", target = "businessUnitId")
    @Mapping(source = "businessUnit.name", target = "businessUnitName")
    @Mapping(source = "manager.id", target = "managerId")
    @Mapping(source = "manager.login", target = "managerLogin")
	BusinessUnitManagerMSCDTO toDto(BusinessUnitManager businessUnitManager);
	
	default BusinessUnitManager fromId(Long id) {
        if (id == null) {
            return null;
        }
        BusinessUnitManager businessUnitManager = new BusinessUnitManager();
        businessUnitManager.setId(id);
        return businessUnitManager;
    }
}
