package fpt.dps.dtms.service.mapper;

import fpt.dps.dtms.domain.ProjectTemplates;
import fpt.dps.dtms.domain.ProjectWorkflows;
import fpt.dps.dtms.service.dto.ProjectWorkflowsDTO;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2019-10-04T10:12:01+0700",
    comments = "version: 1.2.0.Final, compiler: Eclipse JDT (IDE) 1.2.100.v20160418-1457, environment: Java 1.8.0_111 (Oracle Corporation)"
)
@Component
public class ProjectWorkflowsMapperImpl implements ProjectWorkflowsMapper {

    @Autowired
    private ProjectTemplatesMapper projectTemplatesMapper;

    @Override
    public List<ProjectWorkflows> toEntity(List<ProjectWorkflowsDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<ProjectWorkflows> list = new ArrayList<ProjectWorkflows>( dtoList.size() );
        for ( ProjectWorkflowsDTO projectWorkflowsDTO : dtoList ) {
            list.add( toEntity( projectWorkflowsDTO ) );
        }

        return list;
    }

    @Override
    public List<ProjectWorkflowsDTO> toDto(List<ProjectWorkflows> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<ProjectWorkflowsDTO> list = new ArrayList<ProjectWorkflowsDTO>( entityList.size() );
        for ( ProjectWorkflows projectWorkflows : entityList ) {
            list.add( toDto( projectWorkflows ) );
        }

        return list;
    }

    @Override
    public ProjectWorkflowsDTO toDto(ProjectWorkflows projectWorkflows) {
        if ( projectWorkflows == null ) {
            return null;
        }

        ProjectWorkflowsDTO projectWorkflowsDTO = new ProjectWorkflowsDTO();

        String name = projectWorkflowsProjectTemplatesName( projectWorkflows );
        if ( name != null ) {
            projectWorkflowsDTO.setProjectTemplatesName( name );
        }
        Long id = projectWorkflowsProjectTemplatesId( projectWorkflows );
        if ( id != null ) {
            projectWorkflowsDTO.setProjectTemplatesId( id );
        }
        projectWorkflowsDTO.setCreatedBy( projectWorkflows.getCreatedBy() );
        projectWorkflowsDTO.setCreatedDate( projectWorkflows.getCreatedDate() );
        projectWorkflowsDTO.setLastModifiedBy( projectWorkflows.getLastModifiedBy() );
        projectWorkflowsDTO.setLastModifiedDate( projectWorkflows.getLastModifiedDate() );
        projectWorkflowsDTO.setId( projectWorkflows.getId() );
        projectWorkflowsDTO.setName( projectWorkflows.getName() );
        projectWorkflowsDTO.setStep( projectWorkflows.getStep() );
        projectWorkflowsDTO.setEntityDTO( projectWorkflows.getEntityDTO() );
        projectWorkflowsDTO.setInputDTO( projectWorkflows.getInputDTO() );
        projectWorkflowsDTO.setOpGridDTO( projectWorkflows.getOpGridDTO() );
        projectWorkflowsDTO.setPmGridDTO( projectWorkflows.getPmGridDTO() );
        projectWorkflowsDTO.setNextURI( projectWorkflows.getNextURI() );
        projectWorkflowsDTO.setDescription( projectWorkflows.getDescription() );
        projectWorkflowsDTO.setActivity( projectWorkflows.getActivity() );

        return projectWorkflowsDTO;
    }

    @Override
    public ProjectWorkflows toEntity(ProjectWorkflowsDTO projectWorkflowsDTO) {
        if ( projectWorkflowsDTO == null ) {
            return null;
        }

        ProjectWorkflows projectWorkflows = new ProjectWorkflows();

        projectWorkflows.setProjectTemplates( projectTemplatesMapper.fromId( projectWorkflowsDTO.getProjectTemplatesId() ) );
        projectWorkflows.setCreatedBy( projectWorkflowsDTO.getCreatedBy() );
        projectWorkflows.setCreatedDate( projectWorkflowsDTO.getCreatedDate() );
        projectWorkflows.setLastModifiedBy( projectWorkflowsDTO.getLastModifiedBy() );
        projectWorkflows.setLastModifiedDate( projectWorkflowsDTO.getLastModifiedDate() );
        projectWorkflows.setId( projectWorkflowsDTO.getId() );
        projectWorkflows.setName( projectWorkflowsDTO.getName() );
        projectWorkflows.setStep( projectWorkflowsDTO.getStep() );
        projectWorkflows.setEntityDTO( projectWorkflowsDTO.getEntityDTO() );
        projectWorkflows.setInputDTO( projectWorkflowsDTO.getInputDTO() );
        projectWorkflows.setOpGridDTO( projectWorkflowsDTO.getOpGridDTO() );
        projectWorkflows.setPmGridDTO( projectWorkflowsDTO.getPmGridDTO() );
        projectWorkflows.setNextURI( projectWorkflowsDTO.getNextURI() );
        projectWorkflows.setDescription( projectWorkflowsDTO.getDescription() );
        projectWorkflows.setActivity( projectWorkflowsDTO.getActivity() );

        return projectWorkflows;
    }

    private String projectWorkflowsProjectTemplatesName(ProjectWorkflows projectWorkflows) {
        if ( projectWorkflows == null ) {
            return null;
        }
        ProjectTemplates projectTemplates = projectWorkflows.getProjectTemplates();
        if ( projectTemplates == null ) {
            return null;
        }
        String name = projectTemplates.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }

    private Long projectWorkflowsProjectTemplatesId(ProjectWorkflows projectWorkflows) {
        if ( projectWorkflows == null ) {
            return null;
        }
        ProjectTemplates projectTemplates = projectWorkflows.getProjectTemplates();
        if ( projectTemplates == null ) {
            return null;
        }
        Long id = projectTemplates.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
