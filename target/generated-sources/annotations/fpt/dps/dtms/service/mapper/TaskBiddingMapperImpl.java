package fpt.dps.dtms.service.mapper;

import fpt.dps.dtms.domain.Packages;
import fpt.dps.dtms.domain.ProjectUsers;
import fpt.dps.dtms.domain.Projects;
import fpt.dps.dtms.domain.PurchaseOrders;
import fpt.dps.dtms.domain.TaskBidding;
import fpt.dps.dtms.domain.Tasks;
import fpt.dps.dtms.service.dto.TaskBiddingDTO;
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
public class TaskBiddingMapperImpl implements TaskBiddingMapper {

    @Autowired
    private TasksMapper tasksMapper;

    @Override
    public List<TaskBidding> toEntity(List<TaskBiddingDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<TaskBidding> list = new ArrayList<TaskBidding>( dtoList.size() );
        for ( TaskBiddingDTO taskBiddingDTO : dtoList ) {
            list.add( toEntity( taskBiddingDTO ) );
        }

        return list;
    }

    @Override
    public List<TaskBiddingDTO> toDto(List<TaskBidding> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<TaskBiddingDTO> list = new ArrayList<TaskBiddingDTO>( entityList.size() );
        for ( TaskBidding taskBidding : entityList ) {
            list.add( toDto( taskBidding ) );
        }

        return list;
    }

    @Override
    public TaskBiddingDTO toDto(TaskBidding taskBidding) {
        if ( taskBidding == null ) {
            return null;
        }

        TaskBiddingDTO taskBiddingDTO = new TaskBiddingDTO();

        Long id = taskBiddingTaskPackagesPurchaseOrdersId( taskBidding );
        if ( id != null ) {
            taskBiddingDTO.setPurchaseOrdersId( id );
        }
        String name = taskBiddingTaskPackagesPurchaseOrdersName( taskBidding );
        if ( name != null ) {
            taskBiddingDTO.setPurchaseOrdersName( name );
        }
        String name1 = taskBiddingTaskPackagesPurchaseOrdersProjectName( taskBidding );
        if ( name1 != null ) {
            taskBiddingDTO.setProjectName( name1 );
        }
        Integer biddingHoldTime = taskBiddingTaskPackagesPurchaseOrdersProjectBiddingHoldTime( taskBidding );
        if ( biddingHoldTime != null ) {
            taskBiddingDTO.setBiddingHoldTime( biddingHoldTime );
        }
        Long id1 = taskBiddingTaskPackagesPurchaseOrdersProjectId( taskBidding );
        if ( id1 != null ) {
            taskBiddingDTO.setProjectId( id1 );
        }
        String userLogin = taskBiddingTaskPackagesPurchaseOrdersProjectProjectLeadUserLogin( taskBidding );
        if ( userLogin != null ) {
            taskBiddingDTO.setTeamLead( userLogin );
        }
        taskBiddingDTO.setCreatedBy( taskBidding.getCreatedBy() );
        taskBiddingDTO.setCreatedDate( taskBidding.getCreatedDate() );
        taskBiddingDTO.setLastModifiedBy( taskBidding.getLastModifiedBy() );
        taskBiddingDTO.setLastModifiedDate( taskBidding.getLastModifiedDate() );
        taskBiddingDTO.setId( taskBidding.getId() );
        taskBiddingDTO.setBiddingScope( taskBidding.getBiddingScope() );
        taskBiddingDTO.setBiddingStatus( taskBidding.getBiddingStatus() );
        taskBiddingDTO.setTask( tasksMapper.toDto( taskBidding.getTask() ) );
        taskBiddingDTO.setBiddingRound( taskBidding.getBiddingRound() );

        return taskBiddingDTO;
    }

    @Override
    public TaskBidding toEntity(TaskBiddingDTO taskBiddingDTO) {
        if ( taskBiddingDTO == null ) {
            return null;
        }

        TaskBidding taskBidding = new TaskBidding();

        taskBidding.setCreatedBy( taskBiddingDTO.getCreatedBy() );
        taskBidding.setCreatedDate( taskBiddingDTO.getCreatedDate() );
        taskBidding.setLastModifiedBy( taskBiddingDTO.getLastModifiedBy() );
        taskBidding.setLastModifiedDate( taskBiddingDTO.getLastModifiedDate() );
        taskBidding.setId( taskBiddingDTO.getId() );
        taskBidding.setBiddingRound( taskBiddingDTO.getBiddingRound() );
        taskBidding.setBiddingScope( taskBiddingDTO.getBiddingScope() );
        taskBidding.setBiddingStatus( taskBiddingDTO.getBiddingStatus() );
        taskBidding.setTask( tasksMapper.toEntity( taskBiddingDTO.getTask() ) );

        return taskBidding;
    }

    private Long taskBiddingTaskPackagesPurchaseOrdersId(TaskBidding taskBidding) {
        if ( taskBidding == null ) {
            return null;
        }
        Tasks task = taskBidding.getTask();
        if ( task == null ) {
            return null;
        }
        Packages packages = task.getPackages();
        if ( packages == null ) {
            return null;
        }
        PurchaseOrders purchaseOrders = packages.getPurchaseOrders();
        if ( purchaseOrders == null ) {
            return null;
        }
        Long id = purchaseOrders.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String taskBiddingTaskPackagesPurchaseOrdersName(TaskBidding taskBidding) {
        if ( taskBidding == null ) {
            return null;
        }
        Tasks task = taskBidding.getTask();
        if ( task == null ) {
            return null;
        }
        Packages packages = task.getPackages();
        if ( packages == null ) {
            return null;
        }
        PurchaseOrders purchaseOrders = packages.getPurchaseOrders();
        if ( purchaseOrders == null ) {
            return null;
        }
        String name = purchaseOrders.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }

    private String taskBiddingTaskPackagesPurchaseOrdersProjectName(TaskBidding taskBidding) {
        if ( taskBidding == null ) {
            return null;
        }
        Tasks task = taskBidding.getTask();
        if ( task == null ) {
            return null;
        }
        Packages packages = task.getPackages();
        if ( packages == null ) {
            return null;
        }
        PurchaseOrders purchaseOrders = packages.getPurchaseOrders();
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

    private Integer taskBiddingTaskPackagesPurchaseOrdersProjectBiddingHoldTime(TaskBidding taskBidding) {
        if ( taskBidding == null ) {
            return null;
        }
        Tasks task = taskBidding.getTask();
        if ( task == null ) {
            return null;
        }
        Packages packages = task.getPackages();
        if ( packages == null ) {
            return null;
        }
        PurchaseOrders purchaseOrders = packages.getPurchaseOrders();
        if ( purchaseOrders == null ) {
            return null;
        }
        Projects project = purchaseOrders.getProject();
        if ( project == null ) {
            return null;
        }
        Integer biddingHoldTime = project.getBiddingHoldTime();
        if ( biddingHoldTime == null ) {
            return null;
        }
        return biddingHoldTime;
    }

    private Long taskBiddingTaskPackagesPurchaseOrdersProjectId(TaskBidding taskBidding) {
        if ( taskBidding == null ) {
            return null;
        }
        Tasks task = taskBidding.getTask();
        if ( task == null ) {
            return null;
        }
        Packages packages = task.getPackages();
        if ( packages == null ) {
            return null;
        }
        PurchaseOrders purchaseOrders = packages.getPurchaseOrders();
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

    private String taskBiddingTaskPackagesPurchaseOrdersProjectProjectLeadUserLogin(TaskBidding taskBidding) {
        if ( taskBidding == null ) {
            return null;
        }
        Tasks task = taskBidding.getTask();
        if ( task == null ) {
            return null;
        }
        Packages packages = task.getPackages();
        if ( packages == null ) {
            return null;
        }
        PurchaseOrders purchaseOrders = packages.getPurchaseOrders();
        if ( purchaseOrders == null ) {
            return null;
        }
        Projects project = purchaseOrders.getProject();
        if ( project == null ) {
            return null;
        }
        ProjectUsers projectLead = project.getProjectLead();
        if ( projectLead == null ) {
            return null;
        }
        String userLogin = projectLead.getUserLogin();
        if ( userLogin == null ) {
            return null;
        }
        return userLogin;
    }
}
