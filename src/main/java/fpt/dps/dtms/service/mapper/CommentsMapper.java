package fpt.dps.dtms.service.mapper;

import fpt.dps.dtms.domain.*;
import fpt.dps.dtms.service.dto.CommentsDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Comments and its DTO CommentsDTO.
 */
@Mapper(componentModel = "spring", uses = {TmsPostMapper.class})
public interface CommentsMapper extends EntityMapper<CommentsDTO, Comments> {

    @Mapping(source = "post.id", target = "postId")
    CommentsDTO toDto(Comments comments);

    @Mapping(source = "postId", target = "post")
    Comments toEntity(CommentsDTO commentsDTO);

    default Comments fromId(Long id) {
        if (id == null) {
            return null;
        }
        Comments comments = new Comments();
        comments.setId(id);
        return comments;
    }
}
