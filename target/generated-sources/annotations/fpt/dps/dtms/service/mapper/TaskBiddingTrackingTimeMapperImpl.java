package fpt.dps.dtms.service.mapper;

import fpt.dps.dtms.domain.TaskBiddingTrackingTime;
import fpt.dps.dtms.domain.Tasks;
import fpt.dps.dtms.service.dto.TaskBiddingTrackingTimeDTO;
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
public class TaskBiddingTrackingTimeMapperImpl implements TaskBiddingTrackingTimeMapper {

    @Autowired
    private TasksMapper tasksMapper;

    @Override
    public List<TaskBiddingTrackingTime> toEntity(List<TaskBiddingTrackingTimeDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<TaskBiddingTrackingTime> list = new ArrayList<TaskBiddingTrackingTime>( dtoList.size() );
        for ( TaskBiddingTrackingTimeDTO taskBiddingTrackingTimeDTO : dtoList ) {
            list.add( toEntity( taskBiddingTrackingTimeDTO ) );
        }

        return list;
    }

    @Override
    public List<TaskBiddingTrackingTimeDTO> toDto(List<TaskBiddingTrackingTime> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<TaskBiddingTrackingTimeDTO> list = new ArrayList<TaskBiddingTrackingTimeDTO>( entityList.size() );
        for ( TaskBiddingTrackingTime taskBiddingTrackingTime : entityList ) {
            list.add( toDto( taskBiddingTrackingTime ) );
        }

        return list;
    }

    @Override
    public TaskBiddingTrackingTimeDTO toDto(TaskBiddingTrackingTime taskBiddingTrackingTime) {
        if ( taskBiddingTrackingTime == null ) {
            return null;
        }

        TaskBiddingTrackingTimeDTO taskBiddingTrackingTimeDTO = new TaskBiddingTrackingTimeDTO();

        String name = taskBiddingTrackingTimeTaskName( taskBiddingTrackingTime );
        if ( name != null ) {
            taskBiddingTrackingTimeDTO.setTaskName( name );
        }
        Long id = taskBiddingTrackingTimeTaskId( taskBiddingTrackingTime );
        if ( id != null ) {
            taskBiddingTrackingTimeDTO.setTaskId( id );
        }
        taskBiddingTrackingTimeDTO.setCreatedBy( taskBiddingTrackingTime.getCreatedBy() );
        taskBiddingTrackingTimeDTO.setCreatedDate( taskBiddingTrackingTime.getCreatedDate() );
        taskBiddingTrackingTimeDTO.setLastModifiedBy( taskBiddingTrackingTime.getLastModifiedBy() );
        taskBiddingTrackingTimeDTO.setLastModifiedDate( taskBiddingTrackingTime.getLastModifiedDate() );
        taskBiddingTrackingTimeDTO.setId( taskBiddingTrackingTime.getId() );
        taskBiddingTrackingTimeDTO.setUserLogin( taskBiddingTrackingTime.getUserLogin() );
        taskBiddingTrackingTimeDTO.setRole( taskBiddingTrackingTime.getRole() );
        taskBiddingTrackingTimeDTO.setStartTime( taskBiddingTrackingTime.getStartTime() );
        taskBiddingTrackingTimeDTO.setEndTime( taskBiddingTrackingTime.getEndTime() );
        taskBiddingTrackingTimeDTO.setStartStatus( taskBiddingTrackingTime.getStartStatus() );
        taskBiddingTrackingTimeDTO.setEndStatus( taskBiddingTrackingTime.getEndStatus() );
        taskBiddingTrackingTimeDTO.setDuration( taskBiddingTrackingTime.getDuration() );
        taskBiddingTrackingTimeDTO.setBiddingScope( taskBiddingTrackingTime.getBiddingScope() );

        return taskBiddingTrackingTimeDTO;
    }

    @Override
    public TaskBiddingTrackingTime toEntity(TaskBiddingTrackingTimeDTO taskBiddingTrackingTimeDTO) {
        if ( taskBiddingTrackingTimeDTO == null ) {
            return null;
        }

        TaskBiddingTrackingTime taskBiddingTrackingTime = new TaskBiddingTrackingTime();

        taskBiddingTrackingTime.setTask( tasksMapper.fromId( taskBiddingTrackingTimeDTO.getTaskId() ) );
        taskBiddingTrackingTime.setCreatedBy( taskBiddingTrackingTimeDTO.getCreatedBy() );
        taskBiddingTrackingTime.setCreatedDate( taskBiddingTrackingTimeDTO.getCreatedDate() );
        taskBiddingTrackingTime.setLastModifiedBy( taskBiddingTrackingTimeDTO.getLastModifiedBy() );
        taskBiddingTrackingTime.setLastModifiedDate( taskBiddingTrackingTimeDTO.getLastModifiedDate() );
        taskBiddingTrackingTime.setId( taskBiddingTrackingTimeDTO.getId() );
        taskBiddingTrackingTime.setUserLogin( taskBiddingTrackingTimeDTO.getUserLogin() );
        taskBiddingTrackingTime.setRole( taskBiddingTrackingTimeDTO.getRole() );
        taskBiddingTrackingTime.setStartTime( taskBiddingTrackingTimeDTO.getStartTime() );
        taskBiddingTrackingTime.setEndTime( taskBiddingTrackingTimeDTO.getEndTime() );
        taskBiddingTrackingTime.setStartStatus( taskBiddingTrackingTimeDTO.getStartStatus() );
        taskBiddingTrackingTime.setEndStatus( taskBiddingTrackingTimeDTO.getEndStatus() );
        taskBiddingTrackingTime.setDuration( taskBiddingTrackingTimeDTO.getDuration() );
        taskBiddingTrackingTime.setBiddingScope( taskBiddingTrackingTimeDTO.getBiddingScope() );

        return taskBiddingTrackingTime;
    }

    private String taskBiddingTrackingTimeTaskName(TaskBiddingTrackingTime taskBiddingTrackingTime) {
        if ( taskBiddingTrackingTime == null ) {
            return null;
        }
        Tasks task = taskBiddingTrackingTime.getTask();
        if ( task == null ) {
            return null;
        }
        String name = task.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }

    private Long taskBiddingTrackingTimeTaskId(TaskBiddingTrackingTime taskBiddingTrackingTime) {
        if ( taskBiddingTrackingTime == null ) {
            return null;
        }
        Tasks task = taskBiddingTrackingTime.getTask();
        if ( task == null ) {
            return null;
        }
        Long id = task.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
