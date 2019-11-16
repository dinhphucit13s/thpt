package fpt.dps.dtms.service.mapper;

import fpt.dps.dtms.domain.*;
import fpt.dps.dtms.service.dto.ProjectWorkflowsDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity ProjectWorkflows and its DTO ProjectWorkflowsDTO.
 */
@Mapper(componentModel = "spring", uses = {ProjectTemplatesMapper.class})
public interface ProjectWorkflowsMapper extends EntityMapper<ProjectWorkflowsDTO, ProjectWorkflows> {

    @Mapping(source = "projectTemplates.id", target = "projectTemplatesId")
    @Mapping(source = "projectTemplates.name", target = "projectTemplatesName")
    ProjectWorkflowsDTO toDto(ProjectWorkflows projectWorkflows);

    @Mapping(source = "projectTemplatesId", target = "projectTemplates")
    ProjectWorkflows toEntity(ProjectWorkflowsDTO projectWorkflowsDTO);

    default ProjectWorkflows fromId(Long id) {
        if (id == null) {
            return null;
        }
        ProjectWorkflows projectWorkflows = new ProjectWorkflows();
        projectWorkflows.setId(id);
        return projectWorkflows;
    }
}
