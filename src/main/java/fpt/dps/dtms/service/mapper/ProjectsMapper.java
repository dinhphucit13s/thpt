package fpt.dps.dtms.service.mapper;

import fpt.dps.dtms.domain.*;
import fpt.dps.dtms.service.dto.ProjectsDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Projects and its DTO ProjectsDTO.
 */
@Mapper(componentModel = "spring", uses = {ProjectTemplatesMapper.class, ProjectUsersMapper.class, CustomerMapper.class, BusinessUnitMapper.class})
public interface ProjectsMapper extends EntityMapper<ProjectsDTO, Projects> {

    @Mapping(source = "projectTemplates.id", target = "projectTemplatesId")
    @Mapping(source = "projectTemplates.name", target = "projectTemplatesName")
    @Mapping(source = "projectLead.id", target = "projectLeadId")
    @Mapping(source = "projectLead.userLogin", target = "projectLeadUserLogin")
    @Mapping(source = "customer.id", target = "customerId")
    @Mapping(source = "customer.name", target = "customerName")
    @Mapping(source = "businessUnit.id", target = "businessUnitId")
    @Mapping(source = "businessUnit.name", target = "businessUnitName")
    ProjectsDTO toDto(Projects projects);

    @Mapping(source = "projectTemplatesId", target = "projectTemplates")
    @Mapping(source = "projectLeadId", target = "projectLead")
    @Mapping(target = "purchaseOrders", ignore = true)
    @Mapping(target = "projectUsers", ignore = true)
    @Mapping(source = "customerId", target = "customer")
    @Mapping(source = "businessUnitId", target = "businessUnit")
    @Mapping(target = "projectBugListDefaults", ignore = true)
    Projects toEntity(ProjectsDTO projectsDTO);

    default Projects fromId(Long id) {
        if (id == null) {
            return null;
        }
        Projects projects = new Projects();
        projects.setId(id);
        return projects;
    }
}
