package fpt.dps.dtms.service.mapper;

import fpt.dps.dtms.domain.*;
import fpt.dps.dtms.service.dto.ProjectBugListDefaultDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity ProjectBugListDefault and its DTO ProjectBugListDefaultDTO.
 */
@Mapper(componentModel = "spring", uses = {ProjectsMapper.class, BugListDefaultMapper.class})
public interface ProjectBugListDefaultMapper extends EntityMapper<ProjectBugListDefaultDTO, ProjectBugListDefault> {

    @Mapping(source = "project.id", target = "projectId")
    @Mapping(source = "project.name", target = "projectName")
    @Mapping(source = "bugListDefault.id", target = "bugListDefaultId")
    @Mapping(source = "bugListDefault.description", target = "bugListDefaultDescription")
    ProjectBugListDefaultDTO toDto(ProjectBugListDefault projectBugListDefault);

    @Mapping(source = "projectId", target = "project")
    @Mapping(source = "bugListDefaultId", target = "bugListDefault")
    ProjectBugListDefault toEntity(ProjectBugListDefaultDTO projectBugListDefaultDTO);

    default ProjectBugListDefault fromId(Long id) {
        if (id == null) {
            return null;
        }
        ProjectBugListDefault projectBugListDefault = new ProjectBugListDefault();
        projectBugListDefault.setId(id);
        return projectBugListDefault;
    }
}
