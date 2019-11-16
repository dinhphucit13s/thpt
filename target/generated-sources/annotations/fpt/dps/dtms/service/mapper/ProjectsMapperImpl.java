package fpt.dps.dtms.service.mapper;

import fpt.dps.dtms.domain.BusinessUnit;
import fpt.dps.dtms.domain.Customer;
import fpt.dps.dtms.domain.ProjectTemplates;
import fpt.dps.dtms.domain.ProjectUsers;
import fpt.dps.dtms.domain.Projects;
import fpt.dps.dtms.service.dto.ProjectsDTO;
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
public class ProjectsMapperImpl implements ProjectsMapper {

    @Autowired
    private ProjectTemplatesMapper projectTemplatesMapper;
    @Autowired
    private ProjectUsersMapper projectUsersMapper;
    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private BusinessUnitMapper businessUnitMapper;

    @Override
    public List<Projects> toEntity(List<ProjectsDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<Projects> list = new ArrayList<Projects>( dtoList.size() );
        for ( ProjectsDTO projectsDTO : dtoList ) {
            list.add( toEntity( projectsDTO ) );
        }

        return list;
    }

    @Override
    public List<ProjectsDTO> toDto(List<Projects> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<ProjectsDTO> list = new ArrayList<ProjectsDTO>( entityList.size() );
        for ( Projects projects : entityList ) {
            list.add( toDto( projects ) );
        }

        return list;
    }

    @Override
    public ProjectsDTO toDto(Projects projects) {
        if ( projects == null ) {
            return null;
        }

        ProjectsDTO projectsDTO = new ProjectsDTO();

        String name = projectsBusinessUnitName( projects );
        if ( name != null ) {
            projectsDTO.setBusinessUnitName( name );
        }
        Long id = projectsProjectLeadId( projects );
        if ( id != null ) {
            projectsDTO.setProjectLeadId( id );
        }
        String userLogin = projectsProjectLeadUserLogin( projects );
        if ( userLogin != null ) {
            projectsDTO.setProjectLeadUserLogin( userLogin );
        }
        Long id1 = projectsCustomerId( projects );
        if ( id1 != null ) {
            projectsDTO.setCustomerId( id1 );
        }
        Long id2 = projectsProjectTemplatesId( projects );
        if ( id2 != null ) {
            projectsDTO.setProjectTemplatesId( id2 );
        }
        String name1 = projectsProjectTemplatesName( projects );
        if ( name1 != null ) {
            projectsDTO.setProjectTemplatesName( name1 );
        }
        String name2 = projectsCustomerName( projects );
        if ( name2 != null ) {
            projectsDTO.setCustomerName( name2 );
        }
        Long id3 = projectsBusinessUnitId( projects );
        if ( id3 != null ) {
            projectsDTO.setBusinessUnitId( id3 );
        }
        projectsDTO.setCreatedBy( projects.getCreatedBy() );
        projectsDTO.setCreatedDate( projects.getCreatedDate() );
        projectsDTO.setLastModifiedBy( projects.getLastModifiedBy() );
        projectsDTO.setLastModifiedDate( projects.getLastModifiedDate() );
        projectsDTO.setId( projects.getId() );
        projectsDTO.setCode( projects.getCode() );
        projectsDTO.setName( projects.getName() );
        projectsDTO.setType( projects.getType() );
        projectsDTO.setStatus( projects.getStatus() );
        projectsDTO.setStartTime( projects.getStartTime() );
        projectsDTO.setEndTime( projects.getEndTime() );
        projectsDTO.setBiddingHoldTime( projects.getBiddingHoldTime() );
        projectsDTO.setDescription( projects.getDescription() );

        return projectsDTO;
    }

    @Override
    public Projects toEntity(ProjectsDTO projectsDTO) {
        if ( projectsDTO == null ) {
            return null;
        }

        Projects projects = new Projects();

        projects.setProjectTemplates( projectTemplatesMapper.fromId( projectsDTO.getProjectTemplatesId() ) );
        projects.setProjectLead( projectUsersMapper.fromId( projectsDTO.getProjectLeadId() ) );
        projects.setBusinessUnit( businessUnitMapper.fromId( projectsDTO.getBusinessUnitId() ) );
        projects.setCustomer( customerMapper.fromId( projectsDTO.getCustomerId() ) );
        projects.setCreatedBy( projectsDTO.getCreatedBy() );
        projects.setCreatedDate( projectsDTO.getCreatedDate() );
        projects.setLastModifiedBy( projectsDTO.getLastModifiedBy() );
        projects.setLastModifiedDate( projectsDTO.getLastModifiedDate() );
        projects.setId( projectsDTO.getId() );
        projects.setCode( projectsDTO.getCode() );
        projects.setName( projectsDTO.getName() );
        projects.setType( projectsDTO.getType() );
        projects.setStatus( projectsDTO.getStatus() );
        projects.setStartTime( projectsDTO.getStartTime() );
        projects.setEndTime( projectsDTO.getEndTime() );
        projects.setBiddingHoldTime( projectsDTO.getBiddingHoldTime() );
        projects.setDescription( projectsDTO.getDescription() );

        return projects;
    }

    private String projectsBusinessUnitName(Projects projects) {
        if ( projects == null ) {
            return null;
        }
        BusinessUnit businessUnit = projects.getBusinessUnit();
        if ( businessUnit == null ) {
            return null;
        }
        String name = businessUnit.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }

    private Long projectsProjectLeadId(Projects projects) {
        if ( projects == null ) {
            return null;
        }
        ProjectUsers projectLead = projects.getProjectLead();
        if ( projectLead == null ) {
            return null;
        }
        Long id = projectLead.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String projectsProjectLeadUserLogin(Projects projects) {
        if ( projects == null ) {
            return null;
        }
        ProjectUsers projectLead = projects.getProjectLead();
        if ( projectLead == null ) {
            return null;
        }
        String userLogin = projectLead.getUserLogin();
        if ( userLogin == null ) {
            return null;
        }
        return userLogin;
    }

    private Long projectsCustomerId(Projects projects) {
        if ( projects == null ) {
            return null;
        }
        Customer customer = projects.getCustomer();
        if ( customer == null ) {
            return null;
        }
        Long id = customer.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private Long projectsProjectTemplatesId(Projects projects) {
        if ( projects == null ) {
            return null;
        }
        ProjectTemplates projectTemplates = projects.getProjectTemplates();
        if ( projectTemplates == null ) {
            return null;
        }
        Long id = projectTemplates.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String projectsProjectTemplatesName(Projects projects) {
        if ( projects == null ) {
            return null;
        }
        ProjectTemplates projectTemplates = projects.getProjectTemplates();
        if ( projectTemplates == null ) {
            return null;
        }
        String name = projectTemplates.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }

    private String projectsCustomerName(Projects projects) {
        if ( projects == null ) {
            return null;
        }
        Customer customer = projects.getCustomer();
        if ( customer == null ) {
            return null;
        }
        String name = customer.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }

    private Long projectsBusinessUnitId(Projects projects) {
        if ( projects == null ) {
            return null;
        }
        BusinessUnit businessUnit = projects.getBusinessUnit();
        if ( businessUnit == null ) {
            return null;
        }
        Long id = businessUnit.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
