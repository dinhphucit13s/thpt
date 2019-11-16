package fpt.dps.dtms.service.mapper;

import fpt.dps.dtms.domain.*;
import fpt.dps.dtms.service.dto.PackagesDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Packages and its DTO PackagesDTO.
 */
@Mapper(componentModel = "spring", uses = {PurchaseOrdersMapper.class})
public interface PackagesMapper extends EntityMapper<PackagesDTO, Packages> {

    @Mapping(source = "purchaseOrders.id", target = "purchaseOrdersId")
    @Mapping(source = "purchaseOrders.name", target = "purchaseOrdersName")
    PackagesDTO toDto(Packages packages);

    @Mapping(target = "tmsCustomFieldScreenValues", ignore = true)
    @Mapping(source = "purchaseOrdersId", target = "purchaseOrders")
    @Mapping(target = "tasks", ignore = true)
    Packages toEntity(PackagesDTO packagesDTO);

    default Packages fromId(Long id) {
        if (id == null) {
            return null;
        }
        Packages packages = new Packages();
        packages.setId(id);
        return packages;
    }
}
