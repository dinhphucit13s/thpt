package fpt.dps.dtms.service.mapper;

import fpt.dps.dtms.domain.*;
import fpt.dps.dtms.service.dto.ProjectUsersDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity ProjectUsers and its DTO ProjectUsersDTO.
 */
@Mapper(componentModel = "spring", uses = {ProjectsMapper.class})
public interface ProjectUsersMapper extends EntityMapper<ProjectUsersDTO, ProjectUsers> {

    @Mapping(source = "project.id", target = "projectId")
    @Mapping(source = "project.name", target = "projectName")
    ProjectUsersDTO toDto(ProjectUsers projectUsers);

    @Mapping(source = "projectId", target = "project")
    ProjectUsers toEntity(ProjectUsersDTO projectUsersDTO);

    default ProjectUsers fromId(Long id) {
        if (id == null) {
            return null;
        }
        ProjectUsers projectUsers = new ProjectUsers();
        projectUsers.setId(id);
        return projectUsers;
    }
}
