package fpt.dps.dtms.service.mapper;

import fpt.dps.dtms.domain.ProjectUsers;
import fpt.dps.dtms.domain.Projects;
import fpt.dps.dtms.service.dto.ProjectUsersDTO;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2019-10-04T10:11:59+0700",
    comments = "version: 1.2.0.Final, compiler: Eclipse JDT (IDE) 1.2.100.v20160418-1457, environment: Java 1.8.0_111 (Oracle Corporation)"
)
@Component
public class ProjectUsersMapperImpl implements ProjectUsersMapper {

    @Autowired
    private ProjectsMapper projectsMapper;

    @Override
    public List<ProjectUsers> toEntity(List<ProjectUsersDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<ProjectUsers> list = new ArrayList<ProjectUsers>( dtoList.size() );
        for ( ProjectUsersDTO projectUsersDTO : dtoList ) {
            list.add( toEntity( projectUsersDTO ) );
        }

        return list;
    }

    @Override
    public List<ProjectUsersDTO> toDto(List<ProjectUsers> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<ProjectUsersDTO> list = new ArrayList<ProjectUsersDTO>( entityList.size() );
        for ( ProjectUsers projectUsers : entityList ) {
            list.add( toDto( projectUsers ) );
        }

        return list;
    }

    @Override
    public ProjectUsersDTO toDto(ProjectUsers projectUsers) {
        if ( projectUsers == null ) {
            return null;
        }

        ProjectUsersDTO projectUsersDTO = new ProjectUsersDTO();

        String name = projectUsersProjectName( projectUsers );
        if ( name != null ) {
            projectUsersDTO.setProjectName( name );
        }
        Long id = projectUsersProjectId( projectUsers );
        if ( id != null ) {
            projectUsersDTO.setProjectId( id );
        }
        projectUsersDTO.setCreatedBy( projectUsers.getCreatedBy() );
        projectUsersDTO.setCreatedDate( projectUsers.getCreatedDate() );
        projectUsersDTO.setLastModifiedBy( projectUsers.getLastModifiedBy() );
        projectUsersDTO.setLastModifiedDate( projectUsers.getLastModifiedDate() );
        projectUsersDTO.setId( projectUsers.getId() );
        projectUsersDTO.setUserLogin( projectUsers.getUserLogin() );
        projectUsersDTO.setRoleName( projectUsers.getRoleName() );
        projectUsersDTO.setStartDate( projectUsers.getStartDate() );
        projectUsersDTO.setEndDate( projectUsers.getEndDate() );
        projectUsersDTO.setEffortPlan( projectUsers.getEffortPlan() );

        return projectUsersDTO;
    }

    @Override
    public ProjectUsers toEntity(ProjectUsersDTO projectUsersDTO) {
        if ( projectUsersDTO == null ) {
            return null;
        }

        ProjectUsers projectUsers = new ProjectUsers();

        projectUsers.setProject( projectsMapper.fromId( projectUsersDTO.getProjectId() ) );
        projectUsers.setCreatedBy( projectUsersDTO.getCreatedBy() );
        projectUsers.setCreatedDate( projectUsersDTO.getCreatedDate() );
        projectUsers.setLastModifiedBy( projectUsersDTO.getLastModifiedBy() );
        projectUsers.setLastModifiedDate( projectUsersDTO.getLastModifiedDate() );
        projectUsers.setId( projectUsersDTO.getId() );
        projectUsers.setUserLogin( projectUsersDTO.getUserLogin() );
        projectUsers.setRoleName( projectUsersDTO.getRoleName() );
        projectUsers.setStartDate( projectUsersDTO.getStartDate() );
        projectUsers.setEndDate( projectUsersDTO.getEndDate() );
        projectUsers.setEffortPlan( projectUsersDTO.getEffortPlan() );

        return projectUsers;
    }

    private String projectUsersProjectName(ProjectUsers projectUsers) {
        if ( projectUsers == null ) {
            return null;
        }
        Projects project = projectUsers.getProject();
        if ( project == null ) {
            return null;
        }
        String name = project.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }

    private Long projectUsersProjectId(ProjectUsers projectUsers) {
        if ( projectUsers == null ) {
            return null;
        }
        Projects project = projectUsers.getProject();
        if ( project == null ) {
            return null;
        }
        Long id = project.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
