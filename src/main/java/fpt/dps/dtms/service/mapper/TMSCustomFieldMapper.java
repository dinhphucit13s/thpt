package fpt.dps.dtms.service.mapper;

import fpt.dps.dtms.domain.*;
import fpt.dps.dtms.service.dto.TMSCustomFieldDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity TMSCustomField and its DTO TMSCustomFieldDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TMSCustomFieldMapper extends EntityMapper<TMSCustomFieldDTO, TMSCustomField> {



    default TMSCustomField fromId(Long id) {
        if (id == null) {
            return null;
        }
        TMSCustomField tMSCustomField = new TMSCustomField();
        tMSCustomField.setId(id);
        return tMSCustomField;
    }
}
