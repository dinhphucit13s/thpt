package fpt.dps.dtms.service.mapper;

import fpt.dps.dtms.domain.Attachments;
import fpt.dps.dtms.domain.Issues;
import fpt.dps.dtms.domain.Projects;
import fpt.dps.dtms.domain.PurchaseOrders;
import fpt.dps.dtms.service.dto.AttachmentsDTO;
import fpt.dps.dtms.service.dto.IssuesDTO;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2019-10-04T10:12:00+0700",
    comments = "version: 1.2.0.Final, compiler: Eclipse JDT (IDE) 1.2.100.v20160418-1457, environment: Java 1.8.0_111 (Oracle Corporation)"
)
@Component
public class IssuesMapperImpl implements IssuesMapper {

    @Autowired
    private PurchaseOrdersMapper purchaseOrdersMapper;
    @Autowired
    private ProjectsMapper projectsMapper;
    @Autowired
    private AttachmentsMapper attachmentsMapper;

    @Override
    public List<Issues> toEntity(List<IssuesDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<Issues> list = new ArrayList<Issues>( dtoList.size() );
        for ( IssuesDTO issuesDTO : dtoList ) {
            list.add( toEntity( issuesDTO ) );
        }

        return list;
    }

    @Override
    public List<IssuesDTO> toDto(List<Issues> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<IssuesDTO> list = new ArrayList<IssuesDTO>( entityList.size() );
        for ( Issues issues : entityList ) {
            list.add( toDto( issues ) );
        }

        return list;
    }

    @Override
    public IssuesDTO toDto(Issues issues) {
        if ( issues == null ) {
            return null;
        }

        IssuesDTO issuesDTO = new IssuesDTO();

        Long id = issuesProjectsId( issues );
        if ( id != null ) {
            issuesDTO.setProjectsId( id );
        }
        issuesDTO.setAttachments( attachmentsSetToAttachmentsDTOSet( issues.getAttachments() ) );
        String name = issuesProjectsName( issues );
        if ( name != null ) {
            issuesDTO.setProjectsName( name );
        }
        Long id1 = issuesPurchaseOrderId( issues );
        if ( id1 != null ) {
            issuesDTO.setPurchaseOrderId( id1 );
        }
        String name1 = issuesPurchaseOrderName( issues );
        if ( name1 != null ) {
            issuesDTO.setPurchaseOrderName( name1 );
        }
        issuesDTO.setCreatedBy( issues.getCreatedBy() );
        issuesDTO.setCreatedDate( issues.getCreatedDate() );
        issuesDTO.setLastModifiedBy( issues.getLastModifiedBy() );
        issuesDTO.setLastModifiedDate( issues.getLastModifiedDate() );
        issuesDTO.setId( issues.getId() );
        issuesDTO.setName( issues.getName() );
        issuesDTO.setDescription( issues.getDescription() );
        issuesDTO.setStatus( issues.getStatus() );

        return issuesDTO;
    }

    @Override
    public Issues toEntity(IssuesDTO issuesDTO) {
        if ( issuesDTO == null ) {
            return null;
        }

        Issues issues = new Issues();

        issues.setPurchaseOrder( purchaseOrdersMapper.fromId( issuesDTO.getPurchaseOrderId() ) );
        issues.setProjects( projectsMapper.fromId( issuesDTO.getProjectsId() ) );
        issues.setCreatedBy( issuesDTO.getCreatedBy() );
        issues.setCreatedDate( issuesDTO.getCreatedDate() );
        issues.setLastModifiedBy( issuesDTO.getLastModifiedBy() );
        issues.setLastModifiedDate( issuesDTO.getLastModifiedDate() );
        issues.setId( issuesDTO.getId() );
        issues.setName( issuesDTO.getName() );
        issues.setDescription( issuesDTO.getDescription() );
        issues.setStatus( issuesDTO.getStatus() );

        return issues;
    }

    private Long issuesProjectsId(Issues issues) {
        if ( issues == null ) {
            return null;
        }
        Projects projects = issues.getProjects();
        if ( projects == null ) {
            return null;
        }
        Long id = projects.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    protected Set<AttachmentsDTO> attachmentsSetToAttachmentsDTOSet(Set<Attachments> set) {
        if ( set == null ) {
            return null;
        }

        Set<AttachmentsDTO> set1 = new HashSet<AttachmentsDTO>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( Attachments attachments : set ) {
            set1.add( attachmentsMapper.toDto( attachments ) );
        }

        return set1;
    }

    private String issuesProjectsName(Issues issues) {
        if ( issues == null ) {
            return null;
        }
        Projects projects = issues.getProjects();
        if ( projects == null ) {
            return null;
        }
        String name = projects.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }

    private Long issuesPurchaseOrderId(Issues issues) {
        if ( issues == null ) {
            return null;
        }
        PurchaseOrders purchaseOrder = issues.getPurchaseOrder();
        if ( purchaseOrder == null ) {
            return null;
        }
        Long id = purchaseOrder.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String issuesPurchaseOrderName(Issues issues) {
        if ( issues == null ) {
            return null;
        }
        PurchaseOrders purchaseOrder = issues.getPurchaseOrder();
        if ( purchaseOrder == null ) {
            return null;
        }
        String name = purchaseOrder.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }
}
