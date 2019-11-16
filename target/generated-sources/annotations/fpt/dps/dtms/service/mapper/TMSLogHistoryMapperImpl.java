package fpt.dps.dtms.service.mapper;

import fpt.dps.dtms.domain.Packages;
import fpt.dps.dtms.domain.Projects;
import fpt.dps.dtms.domain.PurchaseOrders;
import fpt.dps.dtms.domain.TMSLogHistory;
import fpt.dps.dtms.domain.Tasks;
import fpt.dps.dtms.service.dto.TMSLogHistoryDTO;
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
public class TMSLogHistoryMapperImpl implements TMSLogHistoryMapper {

    @Autowired
    private ProjectsMapper projectsMapper;
    @Autowired
    private PurchaseOrdersMapper purchaseOrdersMapper;
    @Autowired
    private PackagesMapper packagesMapper;
    @Autowired
    private TasksMapper tasksMapper;

    @Override
    public List<TMSLogHistory> toEntity(List<TMSLogHistoryDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<TMSLogHistory> list = new ArrayList<TMSLogHistory>( dtoList.size() );
        for ( TMSLogHistoryDTO tMSLogHistoryDTO : dtoList ) {
            list.add( toEntity( tMSLogHistoryDTO ) );
        }

        return list;
    }

    @Override
    public List<TMSLogHistoryDTO> toDto(List<TMSLogHistory> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<TMSLogHistoryDTO> list = new ArrayList<TMSLogHistoryDTO>( entityList.size() );
        for ( TMSLogHistory tMSLogHistory : entityList ) {
            list.add( toDto( tMSLogHistory ) );
        }

        return list;
    }

    @Override
    public TMSLogHistoryDTO toDto(TMSLogHistory tMSLogHistory) {
        if ( tMSLogHistory == null ) {
            return null;
        }

        TMSLogHistoryDTO tMSLogHistoryDTO = new TMSLogHistoryDTO();

        String name = tMSLogHistoryPackagesName( tMSLogHistory );
        if ( name != null ) {
            tMSLogHistoryDTO.setPackagesName( name );
        }
        Long id = tMSLogHistoryProjectsId( tMSLogHistory );
        if ( id != null ) {
            tMSLogHistoryDTO.setProjectsId( id );
        }
        Long id1 = tMSLogHistoryPurchaseOrdersId( tMSLogHistory );
        if ( id1 != null ) {
            tMSLogHistoryDTO.setPurchaseOrdersId( id1 );
        }
        Long id2 = tMSLogHistoryTasksId( tMSLogHistory );
        if ( id2 != null ) {
            tMSLogHistoryDTO.setTasksId( id2 );
        }
        Long id3 = tMSLogHistoryPackagesId( tMSLogHistory );
        if ( id3 != null ) {
            tMSLogHistoryDTO.setPackagesId( id3 );
        }
        String name1 = tMSLogHistoryTasksName( tMSLogHistory );
        if ( name1 != null ) {
            tMSLogHistoryDTO.setTasksName( name1 );
        }
        String name2 = tMSLogHistoryPurchaseOrdersName( tMSLogHistory );
        if ( name2 != null ) {
            tMSLogHistoryDTO.setPurchaseOrdersName( name2 );
        }
        String name3 = tMSLogHistoryProjectsName( tMSLogHistory );
        if ( name3 != null ) {
            tMSLogHistoryDTO.setProjectsName( name3 );
        }
        tMSLogHistoryDTO.setCreatedBy( tMSLogHistory.getCreatedBy() );
        tMSLogHistoryDTO.setCreatedDate( tMSLogHistory.getCreatedDate() );
        tMSLogHistoryDTO.setLastModifiedBy( tMSLogHistory.getLastModifiedBy() );
        tMSLogHistoryDTO.setLastModifiedDate( tMSLogHistory.getLastModifiedDate() );
        tMSLogHistoryDTO.setId( tMSLogHistory.getId() );
        tMSLogHistoryDTO.setAction( tMSLogHistory.getAction() );
        tMSLogHistoryDTO.setOldValue( tMSLogHistory.getOldValue() );
        tMSLogHistoryDTO.setNewValue( tMSLogHistory.getNewValue() );

        return tMSLogHistoryDTO;
    }

    @Override
    public TMSLogHistory toEntity(TMSLogHistoryDTO tMSLogHistoryDTO) {
        if ( tMSLogHistoryDTO == null ) {
            return null;
        }

        TMSLogHistory tMSLogHistory = new TMSLogHistory();

        tMSLogHistory.setProjects( projectsMapper.fromId( tMSLogHistoryDTO.getProjectsId() ) );
        tMSLogHistory.setPackages( packagesMapper.fromId( tMSLogHistoryDTO.getPackagesId() ) );
        tMSLogHistory.setPurchaseOrders( purchaseOrdersMapper.fromId( tMSLogHistoryDTO.getPurchaseOrdersId() ) );
        tMSLogHistory.setTasks( tasksMapper.fromId( tMSLogHistoryDTO.getTasksId() ) );
        tMSLogHistory.setCreatedBy( tMSLogHistoryDTO.getCreatedBy() );
        tMSLogHistory.setCreatedDate( tMSLogHistoryDTO.getCreatedDate() );
        tMSLogHistory.setLastModifiedBy( tMSLogHistoryDTO.getLastModifiedBy() );
        tMSLogHistory.setLastModifiedDate( tMSLogHistoryDTO.getLastModifiedDate() );
        tMSLogHistory.setId( tMSLogHistoryDTO.getId() );
        tMSLogHistory.setAction( tMSLogHistoryDTO.getAction() );
        tMSLogHistory.setOldValue( tMSLogHistoryDTO.getOldValue() );
        tMSLogHistory.setNewValue( tMSLogHistoryDTO.getNewValue() );

        return tMSLogHistory;
    }

    private String tMSLogHistoryPackagesName(TMSLogHistory tMSLogHistory) {
        if ( tMSLogHistory == null ) {
            return null;
        }
        Packages packages = tMSLogHistory.getPackages();
        if ( packages == null ) {
            return null;
        }
        String name = packages.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }

    private Long tMSLogHistoryProjectsId(TMSLogHistory tMSLogHistory) {
        if ( tMSLogHistory == null ) {
            return null;
        }
        Projects projects = tMSLogHistory.getProjects();
        if ( projects == null ) {
            return null;
        }
        Long id = projects.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private Long tMSLogHistoryPurchaseOrdersId(TMSLogHistory tMSLogHistory) {
        if ( tMSLogHistory == null ) {
            return null;
        }
        PurchaseOrders purchaseOrders = tMSLogHistory.getPurchaseOrders();
        if ( purchaseOrders == null ) {
            return null;
        }
        Long id = purchaseOrders.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private Long tMSLogHistoryTasksId(TMSLogHistory tMSLogHistory) {
        if ( tMSLogHistory == null ) {
            return null;
        }
        Tasks tasks = tMSLogHistory.getTasks();
        if ( tasks == null ) {
            return null;
        }
        Long id = tasks.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private Long tMSLogHistoryPackagesId(TMSLogHistory tMSLogHistory) {
        if ( tMSLogHistory == null ) {
            return null;
        }
        Packages packages = tMSLogHistory.getPackages();
        if ( packages == null ) {
            return null;
        }
        Long id = packages.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String tMSLogHistoryTasksName(TMSLogHistory tMSLogHistory) {
        if ( tMSLogHistory == null ) {
            return null;
        }
        Tasks tasks = tMSLogHistory.getTasks();
        if ( tasks == null ) {
            return null;
        }
        String name = tasks.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }

    private String tMSLogHistoryPurchaseOrdersName(TMSLogHistory tMSLogHistory) {
        if ( tMSLogHistory == null ) {
            return null;
        }
        PurchaseOrders purchaseOrders = tMSLogHistory.getPurchaseOrders();
        if ( purchaseOrders == null ) {
            return null;
        }
        String name = purchaseOrders.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }

    private String tMSLogHistoryProjectsName(TMSLogHistory tMSLogHistory) {
        if ( tMSLogHistory == null ) {
            return null;
        }
        Projects projects = tMSLogHistory.getProjects();
        if ( projects == null ) {
            return null;
        }
        String name = projects.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }
}
