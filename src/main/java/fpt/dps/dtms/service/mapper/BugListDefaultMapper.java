package fpt.dps.dtms.service.mapper;

import fpt.dps.dtms.domain.*;
import fpt.dps.dtms.service.dto.BugListDefaultDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity BugListDefault and its DTO BugListDefaultDTO.
 */
@Mapper(componentModel = "spring", uses = {BusinessLineMapper.class})
public interface BugListDefaultMapper extends EntityMapper<BugListDefaultDTO, BugListDefault> {

    @Mapping(source = "businessLine.id", target = "businessLineId")
    @Mapping(source = "businessLine.name", target = "businessLineName")
    BugListDefaultDTO toDto(BugListDefault bugListDefault);

    @Mapping(source = "businessLineId", target = "businessLine")
    BugListDefault toEntity(BugListDefaultDTO bugListDefaultDTO);

    default BugListDefault fromId(Long id) {
        if (id == null) {
            return null;
        }
        BugListDefault bugListDefault = new BugListDefault();
        bugListDefault.setId(id);
        return bugListDefault;
    }
}
