package fpt.dps.dtms.service.mapper;

import fpt.dps.dtms.domain.ProjectTemplates;
import fpt.dps.dtms.domain.ProjectUsers;
import fpt.dps.dtms.domain.Projects;
import fpt.dps.dtms.domain.PurchaseOrders;
import fpt.dps.dtms.service.dto.PurchaseOrdersDTO;
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
public class PurchaseOrdersMapperImpl implements PurchaseOrdersMapper {

    @Autowired
    private ProjectsMapper projectsMapper;
    @Autowired
    private ProjectUsersMapper projectUsersMapper;
    @Autowired
    private ProjectTemplatesMapper projectTemplatesMapper;

    @Override
    public List<PurchaseOrders> toEntity(List<PurchaseOrdersDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<PurchaseOrders> list = new ArrayList<PurchaseOrders>( dtoList.size() );
        for ( PurchaseOrdersDTO purchaseOrdersDTO : dtoList ) {
            list.add( toEntity( purchaseOrdersDTO ) );
        }

        return list;
    }

    @Override
    public List<PurchaseOrdersDTO> toDto(List<PurchaseOrders> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<PurchaseOrdersDTO> list = new ArrayList<PurchaseOrdersDTO>( entityList.size() );
        for ( PurchaseOrders purchaseOrders : entityList ) {
            list.add( toDto( purchaseOrders ) );
        }

        return list;
    }

    @Override
    public PurchaseOrdersDTO toDto(PurchaseOrders purchaseOrders) {
        if ( purchaseOrders == null ) {
            return null;
        }

        PurchaseOrdersDTO purchaseOrdersDTO = new PurchaseOrdersDTO();

        Long id = purchaseOrdersPurchaseOrderLeadId( purchaseOrders );
        if ( id != null ) {
            purchaseOrdersDTO.setPurchaseOrderLeadId( id );
        }
        Long id1 = purchaseOrdersProjectTemplatesId( purchaseOrders );
        if ( id1 != null ) {
            purchaseOrdersDTO.setProjectTemplatesId( id1 );
        }
        String name = purchaseOrdersProjectName( purchaseOrders );
        if ( name != null ) {
            purchaseOrdersDTO.setProjectName( name );
        }
        Long id2 = purchaseOrdersProjectId( purchaseOrders );
        if ( id2 != null ) {
            purchaseOrdersDTO.setProjectId( id2 );
        }
        String name1 = purchaseOrdersProjectTemplatesName( purchaseOrders );
        if ( name1 != null ) {
            purchaseOrdersDTO.setProjectTemplatesName( name1 );
        }
        String userLogin = purchaseOrdersPurchaseOrderLeadUserLogin( purchaseOrders );
        if ( userLogin != null ) {
            purchaseOrdersDTO.setPurchaseOrderLeadUserLogin( userLogin );
        }
        purchaseOrdersDTO.setCreatedBy( purchaseOrders.getCreatedBy() );
        purchaseOrdersDTO.setCreatedDate( purchaseOrders.getCreatedDate() );
        purchaseOrdersDTO.setLastModifiedBy( purchaseOrders.getLastModifiedBy() );
        purchaseOrdersDTO.setLastModifiedDate( purchaseOrders.getLastModifiedDate() );
        purchaseOrdersDTO.setId( purchaseOrders.getId() );
        purchaseOrdersDTO.setName( purchaseOrders.getName() );
        purchaseOrdersDTO.setStatus( purchaseOrders.getStatus() );
        purchaseOrdersDTO.setStartTime( purchaseOrders.getStartTime() );
        purchaseOrdersDTO.setEndTime( purchaseOrders.getEndTime() );
        purchaseOrdersDTO.setDescription( purchaseOrders.getDescription() );
        purchaseOrdersDTO.setReviewRatio( purchaseOrders.getReviewRatio() );

        return purchaseOrdersDTO;
    }

    @Override
    public PurchaseOrders toEntity(PurchaseOrdersDTO purchaseOrdersDTO) {
        if ( purchaseOrdersDTO == null ) {
            return null;
        }

        PurchaseOrders purchaseOrders = new PurchaseOrders();

        purchaseOrders.setProjectTemplates( projectTemplatesMapper.fromId( purchaseOrdersDTO.getProjectTemplatesId() ) );
        purchaseOrders.setProject( projectsMapper.fromId( purchaseOrdersDTO.getProjectId() ) );
        purchaseOrders.setPurchaseOrderLead( projectUsersMapper.fromId( purchaseOrdersDTO.getPurchaseOrderLeadId() ) );
        purchaseOrders.setCreatedBy( purchaseOrdersDTO.getCreatedBy() );
        purchaseOrders.setCreatedDate( purchaseOrdersDTO.getCreatedDate() );
        purchaseOrders.setLastModifiedBy( purchaseOrdersDTO.getLastModifiedBy() );
        purchaseOrders.setLastModifiedDate( purchaseOrdersDTO.getLastModifiedDate() );
        purchaseOrders.setId( purchaseOrdersDTO.getId() );
        purchaseOrders.setName( purchaseOrdersDTO.getName() );
        purchaseOrders.setStatus( purchaseOrdersDTO.getStatus() );
        purchaseOrders.setStartTime( purchaseOrdersDTO.getStartTime() );
        purchaseOrders.setEndTime( purchaseOrdersDTO.getEndTime() );
        purchaseOrders.setDescription( purchaseOrdersDTO.getDescription() );
        purchaseOrders.setReviewRatio( purchaseOrdersDTO.getReviewRatio() );

        return purchaseOrders;
    }

    private Long purchaseOrdersPurchaseOrderLeadId(PurchaseOrders purchaseOrders) {
        if ( purchaseOrders == null ) {
            return null;
        }
        ProjectUsers purchaseOrderLead = purchaseOrders.getPurchaseOrderLead();
        if ( purchaseOrderLead == null ) {
            return null;
        }
        Long id = purchaseOrderLead.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private Long purchaseOrdersProjectTemplatesId(PurchaseOrders purchaseOrders) {
        if ( purchaseOrders == null ) {
            return null;
        }
        ProjectTemplates projectTemplates = purchaseOrders.getProjectTemplates();
        if ( projectTemplates == null ) {
            return null;
        }
        Long id = projectTemplates.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String purchaseOrdersProjectName(PurchaseOrders purchaseOrders) {
        if ( purchaseOrders == null ) {
            return null;
        }
        Projects project = purchaseOrders.getProject();
        if ( project == null ) {
            return null;
        }
        String name = project.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }

    private Long purchaseOrdersProjectId(PurchaseOrders purchaseOrders) {
        if ( purchaseOrders == null ) {
            return null;
        }
        Projects project = purchaseOrders.getProject();
        if ( project == null ) {
            return null;
        }
        Long id = project.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String purchaseOrdersProjectTemplatesName(PurchaseOrders purchaseOrders) {
        if ( purchaseOrders == null ) {
            return null;
        }
        ProjectTemplates projectTemplates = purchaseOrders.getProjectTemplates();
        if ( projectTemplates == null ) {
            return null;
        }
        String name = projectTemplates.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }

    private String purchaseOrdersPurchaseOrderLeadUserLogin(PurchaseOrders purchaseOrders) {
        if ( purchaseOrders == null ) {
            return null;
        }
        ProjectUsers purchaseOrderLead = purchaseOrders.getPurchaseOrderLead();
        if ( purchaseOrderLead == null ) {
            return null;
        }
        String userLogin = purchaseOrderLead.getUserLogin();
        if ( userLogin == null ) {
            return null;
        }
        return userLogin;
    }
}
