package fpt.dps.dtms.service.mapper;

import fpt.dps.dtms.domain.*;
import fpt.dps.dtms.service.dto.PurchaseOrdersDTO;
import fpt.dps.dtms.service.dto.SelectDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity PurchaseOrders and its DTO PurchaseOrdersDTO.
 */
@Mapper(componentModel = "spring", uses = {ProjectsMapper.class, ProjectUsersMapper.class})
public interface SelectMapper extends EntityMapper<SelectDTO, PurchaseOrders> {

	SelectDTO toDto(PurchaseOrders purchaseOrders);
}
