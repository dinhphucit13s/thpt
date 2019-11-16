package fpt.dps.dtms.service.mapper;

import fpt.dps.dtms.domain.*;
import fpt.dps.dtms.service.dto.TasksDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Tasks and its DTO TasksDTO.
 */
@Mapper(componentModel = "spring", uses = {PackagesMapper.class})
public interface TasksMapper extends EntityMapper<TasksDTO, Tasks> {

    @Mapping(source = "packages.id", target = "packagesId")
    @Mapping(source = "packages.name", target = "packagesName")
    TasksDTO toDto(Tasks tasks);

    @Mapping(target = "tmsCustomFieldScreenValues", ignore = true)
    @Mapping(source = "packagesId", target = "packages")
    Tasks toEntity(TasksDTO tasksDTO);

    default Tasks fromId(Long id) {
        if (id == null) {
            return null;
        }
        Tasks tasks = new Tasks();
        tasks.setId(id);
        return tasks;
    }
}
