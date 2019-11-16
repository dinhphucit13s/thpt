package fpt.dps.dtms.service.mapper;

import fpt.dps.dtms.domain.BugListDefault;
import fpt.dps.dtms.domain.ProjectBugListDefault;
import fpt.dps.dtms.domain.Projects;
import fpt.dps.dtms.service.dto.ProjectBugListDefaultDTO;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2019-10-04T10:12:00+0700",
    comments = "version: 1.2.0.Final, compiler: Eclipse JDT (IDE) 1.2.100.v20160418-1457, environment: Java 1.8.0_111 (Oracle Corporation)"
)
@Component
public class ProjectBugListDefaultMapperImpl implements ProjectBugListDefaultMapper {

    @Autowired
    private ProjectsMapper projectsMapper;
    @Autowired
    private BugListDefaultMapper bugListDefaultMapper;

    @Override
    public List<ProjectBugListDefault> toEntity(List<ProjectBugListDefaultDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<ProjectBugListDefault> list = new ArrayList<ProjectBugListDefault>( dtoList.size() );
        for ( ProjectBugListDefaultDTO projectBugListDefaultDTO : dtoList ) {
            list.add( toEntity( projectBugListDefaultDTO ) );
        }

        return list;
    }

    @Override
    public List<ProjectBugListDefaultDTO> toDto(List<ProjectBugListDefault> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<ProjectBugListDefaultDTO> list = new ArrayList<ProjectBugListDefaultDTO>( entityList.size() );
        for ( ProjectBugListDefault projectBugListDefault : entityList ) {
            list.add( toDto( projectBugListDefault ) );
        }

        return list;
    }

    @Override
    public ProjectBugListDefaultDTO toDto(ProjectBugListDefault projectBugListDefault) {
        if ( projectBugListDefault == null ) {
            return null;
        }

        ProjectBugListDefaultDTO projectBugListDefaultDTO = new ProjectBugListDefaultDTO();

        Long id = projectBugListDefaultBugListDefaultId( projectBugListDefault );
        if ( id != null ) {
            projectBugListDefaultDTO.setBugListDefaultId( id );
        }
        String name = projectBugListDefaultProjectName( projectBugListDefault );
        if ( name != null ) {
            projectBugListDefaultDTO.setProjectName( name );
        }
        String description = projectBugListDefaultBugListDefaultDescription( projectBugListDefault );
        if ( description != null ) {
            projectBugListDefaultDTO.setBugListDefaultDescription( description );
        }
        Long id1 = projectBugListDefaultProjectId( projectBugListDefault );
        if ( id1 != null ) {
            projectBugListDefaultDTO.setProjectId( id1 );
        }
        projectBugListDefaultDTO.setId( projectBugListDefault.getId() );
        projectBugListDefaultDTO.setCode( projectBugListDefault.getCode() );

        return projectBugListDefaultDTO;
    }

    @Override
    public ProjectBugListDefault toEntity(ProjectBugListDefaultDTO projectBugListDefaultDTO) {
        if ( projectBugListDefaultDTO == null ) {
            return null;
        }

        ProjectBugListDefault projectBugListDefault = new ProjectBugListDefault();

        projectBugListDefault.setProject( projectsMapper.fromId( projectBugListDefaultDTO.getProjectId() ) );
        projectBugListDefault.setBugListDefault( bugListDefaultMapper.fromId( projectBugListDefaultDTO.getBugListDefaultId() ) );
        projectBugListDefault.setId( projectBugListDefaultDTO.getId() );
        projectBugListDefault.setCode( projectBugListDefaultDTO.getCode() );

        return projectBugListDefault;
    }

    private Long projectBugListDefaultBugListDefaultId(ProjectBugListDefault projectBugListDefault) {
        if ( projectBugListDefault == null ) {
            return null;
        }
        BugListDefault bugListDefault = projectBugListDefault.getBugListDefault();
        if ( bugListDefault == null ) {
            return null;
        }
        Long id = bugListDefault.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String projectBugListDefaultProjectName(ProjectBugListDefault projectBugListDefault) {
        if ( projectBugListDefault == null ) {
            return null;
        }
        Projects project = projectBugListDefault.getProject();
        if ( project == null ) {
            return null;
        }
        String name = project.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }

    private String projectBugListDefaultBugListDefaultDescription(ProjectBugListDefault projectBugListDefault) {
        if ( projectBugListDefault == null ) {
            return null;
        }
        BugListDefault bugListDefault = projectBugListDefault.getBugListDefault();
        if ( bugListDefault == null ) {
            return null;
        }
        String description = bugListDefault.getDescription();
        if ( description == null ) {
            return null;
        }
        return description;
    }

    private Long projectBugListDefaultProjectId(ProjectBugListDefault projectBugListDefault) {
        if ( projectBugListDefault == null ) {
            return null;
        }
        Projects project = projectBugListDefault.getProject();
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
