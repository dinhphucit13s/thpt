package fpt.dps.dtms.service.mapper;

import fpt.dps.dtms.domain.*;
import fpt.dps.dtms.service.dto.TaskBiddingTrackingTimeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity TaskBiddingTrackingTime and its DTO TaskBiddingTrackingTimeDTO.
 */
@Mapper(componentModel = "spring", uses = {TasksMapper.class})
public interface TaskBiddingTrackingTimeMapper extends EntityMapper<TaskBiddingTrackingTimeDTO, TaskBiddingTrackingTime> {

    @Mapping(source = "task.id", target = "taskId")
    @Mapping(source = "task.name", target = "taskName")
    TaskBiddingTrackingTimeDTO toDto(TaskBiddingTrackingTime taskBiddingTrackingTime);

    @Mapping(source = "taskId", target = "task")
    TaskBiddingTrackingTime toEntity(TaskBiddingTrackingTimeDTO taskBiddingTrackingTimeDTO);

    default TaskBiddingTrackingTime fromId(Long id) {
        if (id == null) {
            return null;
        }
        TaskBiddingTrackingTime taskBiddingTrackingTime = new TaskBiddingTrackingTime();
        taskBiddingTrackingTime.setId(id);
        return taskBiddingTrackingTime;
    }
}
