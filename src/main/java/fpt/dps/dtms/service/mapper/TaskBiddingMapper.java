package fpt.dps.dtms.service.mapper;

import fpt.dps.dtms.domain.*;
import fpt.dps.dtms.service.dto.TaskBiddingDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity TaskBidding and its DTO TaskBiddingDTO.
 */
@Mapper(componentModel = "spring", uses = {TasksMapper.class, TasksMapper.class})
public interface TaskBiddingMapper extends EntityMapper<TaskBiddingDTO, TaskBidding> {

    // @Mapping(source = "task.id", target = "taskId")
    @Mapping(source = "task.packages.purchaseOrders.project.biddingHoldTime", target = "biddingHoldTime")
    @Mapping(source = "task.packages.purchaseOrders.id", target = "purchaseOrdersId")
    @Mapping(source = "task.packages.purchaseOrders.name", target = "purchaseOrdersName")
    @Mapping(source = "task.packages.purchaseOrders.project.id", target = "projectId")
    @Mapping(source = "task.packages.purchaseOrders.project.name", target = "projectName")
    @Mapping(source = "task.packages.purchaseOrders.project.projectLead.userLogin", target = "teamLead")
    TaskBiddingDTO toDto(TaskBidding taskBidding);

    TaskBidding toEntity(TaskBiddingDTO taskBiddingDTO);

    default TaskBidding fromId(Long id) {
        if (id == null) {
            return null;
        }
        TaskBidding taskBidding = new TaskBidding();
        taskBidding.setId(id);
        return taskBidding;
    }
}
