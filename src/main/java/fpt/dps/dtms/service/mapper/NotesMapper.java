package fpt.dps.dtms.service.mapper;

import fpt.dps.dtms.domain.*;
import fpt.dps.dtms.service.dto.NotesDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Notes and its DTO NotesDTO.
 */
@Mapper(componentModel = "spring", uses = {TasksMapper.class, BugsMapper.class, AttachmentsMapper.class})
public interface NotesMapper extends EntityMapper<NotesDTO, Notes> {

    @Mapping(source = "tasks.id", target = "tasksId")
    @Mapping(source = "tasks.name", target = "tasksName")
    @Mapping(source = "bug.id", target = "bugId")
    NotesDTO toDto(Notes notes);

    @Mapping(source = "tasksId", target = "tasks")
    @Mapping(source = "bugId", target = "bug")
    Notes toEntity(NotesDTO notesDTO);

    default Notes fromId(Long id) {
        if (id == null) {
            return null;
        }
        Notes notes = new Notes();
        notes.setId(id);
        return notes;
    }
}
