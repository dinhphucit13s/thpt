package fpt.dps.dtms.service.mapper;

import fpt.dps.dtms.domain.*;
import fpt.dps.dtms.service.dto.TMSLogHistoryDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity TMSLogHistory and its DTO TMSLogHistoryDTO.
 */
@Mapper(componentModel = "spring", uses = {ProjectsMapper.class, PurchaseOrdersMapper.class, PackagesMapper.class, TasksMapper.class})
public interface TMSLogHistoryMapper extends EntityMapper<TMSLogHistoryDTO, TMSLogHistory> {

    @Mapping(source = "projects.id", target = "projectsId")
    @Mapping(source = "projects.name", target = "projectsName")
    @Mapping(source = "purchaseOrders.id", target = "purchaseOrdersId")
    @Mapping(source = "purchaseOrders.name", target = "purchaseOrdersName")
    @Mapping(source = "packages.id", target = "packagesId")
    @Mapping(source = "packages.name", target = "packagesName")
    @Mapping(source = "tasks.id", target = "tasksId")
    @Mapping(source = "tasks.name", target = "tasksName")
    TMSLogHistoryDTO toDto(TMSLogHistory tMSLogHistory);

    @Mapping(source = "projectsId", target = "projects")
    @Mapping(source = "purchaseOrdersId", target = "purchaseOrders")
    @Mapping(source = "packagesId", target = "packages")
    @Mapping(source = "tasksId", target = "tasks")
    TMSLogHistory toEntity(TMSLogHistoryDTO tMSLogHistoryDTO);

    default TMSLogHistory fromId(Long id) {
        if (id == null) {
            return null;
        }
        TMSLogHistory tMSLogHistory = new TMSLogHistory();
        tMSLogHistory.setId(id);
        return tMSLogHistory;
    }
}
