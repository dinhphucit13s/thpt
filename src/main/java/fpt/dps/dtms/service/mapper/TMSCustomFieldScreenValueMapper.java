package fpt.dps.dtms.service.mapper;

import fpt.dps.dtms.domain.*;
import fpt.dps.dtms.service.dto.TMSCustomFieldScreenValueDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity TMSCustomFieldScreenValue and its DTO TMSCustomFieldScreenValueDTO.
 */
@Mapper(componentModel = "spring", uses = {PurchaseOrdersMapper.class, PackagesMapper.class, TasksMapper.class, TMSCustomFieldScreenMapper.class})
public interface TMSCustomFieldScreenValueMapper extends EntityMapper<TMSCustomFieldScreenValueDTO, TMSCustomFieldScreenValue> {

    @Mapping(source = "purchaseOrders.id", target = "purchaseOrdersId")
    @Mapping(source = "packages.id", target = "packagesId")
    @Mapping(source = "tasks.id", target = "tasksId")
    @Mapping(source = "TMSCustomFieldScreen.id", target = "tmsCustomFieldScreenId")
    TMSCustomFieldScreenValueDTO toDto(TMSCustomFieldScreenValue tMSCustomFieldScreenValue);

    @Mapping(source = "purchaseOrdersId", target = "purchaseOrders")
    @Mapping(source = "packagesId", target = "packages")
    @Mapping(source = "tasksId", target = "tasks")
    @Mapping(source = "tmsCustomFieldScreenId", target = "TMSCustomFieldScreen")
    TMSCustomFieldScreenValue toEntity(TMSCustomFieldScreenValueDTO tMSCustomFieldScreenValueDTO);

    default TMSCustomFieldScreenValue fromId(Long id) {
        if (id == null) {
            return null;
        }
        TMSCustomFieldScreenValue tMSCustomFieldScreenValue = new TMSCustomFieldScreenValue();
        tMSCustomFieldScreenValue.setId(id);
        return tMSCustomFieldScreenValue;
    }
}
