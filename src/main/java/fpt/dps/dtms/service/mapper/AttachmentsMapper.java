package fpt.dps.dtms.service.mapper;

import fpt.dps.dtms.domain.*;
import fpt.dps.dtms.service.dto.AttachmentsDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Attachments and its DTO AttachmentsDTO.
 */
@Mapper(componentModel = "spring", uses = {BugsMapper.class, NotesMapper.class, IssuesMapper.class, MailMapper.class, CommentsMapper.class, TmsPostMapper.class})
public interface AttachmentsMapper extends EntityMapper<AttachmentsDTO, Attachments> {

    @Mapping(source = "bugs.id", target = "bugsId")
    @Mapping(source = "bugs.description", target = "bugsDescription")
    @Mapping(source = "notes.id", target = "notesId")
    @Mapping(source = "notes.description", target = "notesDescription")
    @Mapping(source = "issues.id", target = "issuesId")
    @Mapping(source = "issues.name", target = "issuesName")
    @Mapping(source = "mail.id", target = "mailId")
    @Mapping(source = "comment.id", target = "commentId")
    @Mapping(source = "post.id", target = "postId")
    AttachmentsDTO toDto(Attachments attachments);

    @Mapping(source = "bugsId", target = "bugs")
    @Mapping(source = "notesId", target = "notes")
    @Mapping(source = "issuesId", target = "issues")
    @Mapping(source = "mailId", target = "mail")
    @Mapping(source = "commentId", target = "comment")
    @Mapping(source = "postId", target = "post")
    Attachments toEntity(AttachmentsDTO attachmentsDTO);

    default Attachments fromId(Long id) {
        if (id == null) {
            return null;
        }
        Attachments attachments = new Attachments();
        attachments.setId(id);
        return attachments;
    }
}
