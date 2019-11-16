package fpt.dps.dtms.service.mapper;

import fpt.dps.dtms.domain.*;
import fpt.dps.dtms.service.dto.PurchaseOrdersDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity PurchaseOrders and its DTO PurchaseOrdersDTO.
 */
@Mapper(componentModel = "spring", uses = {ProjectsMapper.class, ProjectUsersMapper.class, ProjectTemplatesMapper.class})
public interface PurchaseOrdersMapper extends EntityMapper<PurchaseOrdersDTO, PurchaseOrders> {

    @Mapping(source = "project.id", target = "projectId")
    @Mapping(source = "project.name", target = "projectName")
    @Mapping(source = "purchaseOrderLead.id", target = "purchaseOrderLeadId")
    @Mapping(source = "purchaseOrderLead.userLogin", target = "purchaseOrderLeadUserLogin")
    @Mapping(source = "projectTemplates.id", target = "projectTemplatesId")
    @Mapping(source = "projectTemplates.name", target = "projectTemplatesName")
    PurchaseOrdersDTO toDto(PurchaseOrders purchaseOrders);

    @Mapping(source = "projectTemplatesId", target = "projectTemplates")
    @Mapping(target = "tmsCustomFieldScreenValues", ignore = true)
    @Mapping(source = "projectId", target = "project")
    @Mapping(source = "purchaseOrderLeadId", target = "purchaseOrderLead")
    @Mapping(target = "packages", ignore = true)
    PurchaseOrders toEntity(PurchaseOrdersDTO purchaseOrdersDTO);

    default PurchaseOrders fromId(Long id) {
        if (id == null) {
            return null;
        }
        PurchaseOrders purchaseOrders = new PurchaseOrders();
        purchaseOrders.setId(id);
        return purchaseOrders;
    }
}
