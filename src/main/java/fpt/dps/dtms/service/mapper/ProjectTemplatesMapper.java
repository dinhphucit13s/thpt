package fpt.dps.dtms.service.mapper;

import fpt.dps.dtms.domain.*;
import fpt.dps.dtms.service.dto.ProjectTemplatesDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity ProjectTemplates and its DTO ProjectTemplatesDTO.
 */
@Mapper(componentModel = "spring", uses = {BusinessLineMapper.class})
public interface ProjectTemplatesMapper extends EntityMapper<ProjectTemplatesDTO, ProjectTemplates> {

    @Mapping(source = "businessLine.id", target = "businessLineId")
    @Mapping(source = "businessLine.name", target = "businessLineName")
    ProjectTemplatesDTO toDto(ProjectTemplates projectTemplates);

    @Mapping(source = "businessLineId", target = "businessLine")
    @Mapping(target = "projects", ignore = true)
    ProjectTemplates toEntity(ProjectTemplatesDTO projectTemplatesDTO);

    default ProjectTemplates fromId(Long id) {
        if (id == null) {
            return null;
        }
        ProjectTemplates projectTemplates = new ProjectTemplates();
        projectTemplates.setId(id);
        return projectTemplates;
    }
}
