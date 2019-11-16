package fpt.dps.dtms.service.mapper;

import fpt.dps.dtms.domain.*;
import fpt.dps.dtms.service.dto.TmsThreadDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity TmsThread and its DTO TmsThreadDTO.
 */
@Mapper(componentModel = "spring", uses = {ProjectsMapper.class, ProjectUsersMapper.class, TmsPostMapper.class})
public interface TmsThreadMapper extends EntityMapper<TmsThreadDTO, TmsThread> {

    @Mapping(source = "projects.id", target = "projectsId")
    @Mapping(source = "projects.name", target = "projectsName")
    @Mapping(source = "assignee.id", target = "assigneeId")
    @Mapping(source = "assignee.userLogin", target = "assigneeUserLogin")
    @Mapping(target = "posts", ignore = true)
    TmsThreadDTO toDto(TmsThread tmsThread);

    @Mapping(source = "projectsId", target = "projects")
    @Mapping(source = "assigneeId", target = "assignee")
    TmsThread toEntity(TmsThreadDTO tmsThreadDTO);

    default TmsThread fromId(Long id) {
        if (id == null) {
            return null;
        }
        TmsThread tmsThread = new TmsThread();
        tmsThread.setId(id);
        return tmsThread;
    }
}
