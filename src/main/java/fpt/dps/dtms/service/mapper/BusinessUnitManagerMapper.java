package fpt.dps.dtms.service.mapper;

import fpt.dps.dtms.domain.*;
import fpt.dps.dtms.service.dto.BusinessUnitManagerDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity BusinessUnitManager and its DTO BusinessUnitManagerDTO.
 */
@Mapper(componentModel = "spring", uses = {BusinessUnitMapper.class, UserMapper.class})
public interface BusinessUnitManagerMapper extends EntityMapper<BusinessUnitManagerDTO, BusinessUnitManager> {

    @Mapping(source = "businessUnit.id", target = "businessUnitId")
    @Mapping(source = "businessUnit.name", target = "businessUnitName")
    @Mapping(source = "manager.id", target = "managerId")
    @Mapping(source = "manager.login", target = "managerLogin")
    BusinessUnitManagerDTO toDto(BusinessUnitManager businessUnitManager);

    @Mapping(source = "businessUnitId", target = "businessUnit")
    @Mapping(source = "managerId", target = "manager")
    BusinessUnitManager toEntity(BusinessUnitManagerDTO businessUnitManagerDTO);

    default BusinessUnitManager fromId(Long id) {
        if (id == null) {
            return null;
        }
        BusinessUnitManager businessUnitManager = new BusinessUnitManager();
        businessUnitManager.setId(id);
        return businessUnitManager;
    }
}
