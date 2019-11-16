package fpt.dps.dtms.service.mapper;

import fpt.dps.dtms.domain.*;
import fpt.dps.dtms.service.dto.TMSCustomFieldScreenDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity TMSCustomFieldScreen and its DTO TMSCustomFieldScreenDTO.
 */
@Mapper(componentModel = "spring", uses = {TMSCustomFieldMapper.class, ProjectWorkflowsMapper.class})
public interface TMSCustomFieldScreenMapper extends EntityMapper<TMSCustomFieldScreenDTO, TMSCustomFieldScreen> {
	
	@Mapping(source = "tmsCustomField.id", target = "tmsCustomFieldId")
	@Mapping(source = "tmsCustomField.entityData", target = "entityData")
    @Mapping(source = "projectWorkflows.id", target = "projectWorkflowsId")
    @Mapping(source = "projectWorkflows.name", target = "projectWorkflowsName")
    TMSCustomFieldScreenDTO toDto(TMSCustomFieldScreen tMSCustomFieldScreen);
	
	@Mapping(source = "tmsCustomFieldId", target = "tmsCustomField")
    @Mapping(source = "projectWorkflowsId", target = "projectWorkflows")
    TMSCustomFieldScreen toEntity(TMSCustomFieldScreenDTO tMSCustomFieldScreenDTO);

    default TMSCustomFieldScreen fromId(Long id) {
        if (id == null) {
            return null;
        }
        TMSCustomFieldScreen tMSCustomFieldScreen = new TMSCustomFieldScreen();
        tMSCustomFieldScreen.setId(id);
        return tMSCustomFieldScreen;
    }
}
