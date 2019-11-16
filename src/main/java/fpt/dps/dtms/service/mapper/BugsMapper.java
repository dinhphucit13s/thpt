package fpt.dps.dtms.service.mapper;

import fpt.dps.dtms.domain.*;
import fpt.dps.dtms.service.dto.BugsDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Bugs and its DTO BugsDTO.
 */
@Mapper(componentModel = "spring", uses = {TasksMapper.class, NotesMapper.class, AttachmentsMapper.class})
public interface BugsMapper extends EntityMapper<BugsDTO, Bugs> {

    @Mapping(source = "tasks.id", target = "tasksId")
    @Mapping(source = "tasks.name", target = "tasksName")
    @Mapping(source = "notes", target = "notes")
    @Mapping(source = "attachments", target = "attachments")
    BugsDTO toDto(Bugs bugs);

    @Mapping(source = "tasksId", target = "tasks")
//    @Mapping(target = "attachments", ignore = true)
//    @Mapping(target = "notes", ignore = true)
    Bugs toEntity(BugsDTO bugsDTO);

    default Bugs fromId(Long id) {
        if (id == null) {
            return null;
        }
        Bugs bugs = new Bugs();
        bugs.setId(id);
        return bugs;
    }
}
