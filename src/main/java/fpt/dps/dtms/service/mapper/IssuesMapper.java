package fpt.dps.dtms.service.mapper;

import fpt.dps.dtms.domain.*;
import fpt.dps.dtms.service.dto.IssuesDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Issues and its DTO IssuesDTO.
 */
@Mapper(componentModel = "spring", uses = {PurchaseOrdersMapper.class, ProjectsMapper.class, AttachmentsMapper.class})
public interface IssuesMapper extends EntityMapper<IssuesDTO, Issues> {

    @Mapping(source = "purchaseOrder.id", target = "purchaseOrderId")
    @Mapping(source = "purchaseOrder.name", target = "purchaseOrderName")
    @Mapping(source = "projects.id", target = "projectsId")
    @Mapping(source = "projects.name", target = "projectsName")
    @Mapping(source = "attachments", target = "attachments")
    IssuesDTO toDto(Issues issues);

    @Mapping(source = "purchaseOrderId", target = "purchaseOrder")
    @Mapping(source = "projectsId", target = "projects")
    @Mapping(target = "attachments", ignore = true)
    Issues toEntity(IssuesDTO issuesDTO);

    default Issues fromId(Long id) {
        if (id == null) {
            return null;
        }
        Issues issues = new Issues();
        issues.setId(id);
        return issues;
    }
}
