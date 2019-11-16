package fpt.dps.dtms.service.mapper;

import fpt.dps.dtms.domain.*;
import fpt.dps.dtms.service.dto.TmsPostDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity TmsPost and its DTO TmsPostDTO.
 */
@Mapper(componentModel = "spring", uses = {TmsThreadMapper.class, AttachmentsMapper.class})
public interface TmsPostMapper extends EntityMapper<TmsPostDTO, TmsPost> {

    @Mapping(source = "thread.id", target = "threadId")
    @Mapping(source = "attachments", target = "attachments")
    TmsPostDTO toDto(TmsPost tmsPost);

    @Mapping(source = "threadId", target = "thread")
    TmsPost toEntity(TmsPostDTO tmsPostDTO);

    default TmsPost fromId(Long id) {
        if (id == null) {
            return null;
        }
        TmsPost tmsPost = new TmsPost();
        tmsPost.setId(id);
        return tmsPost;
    }
}
