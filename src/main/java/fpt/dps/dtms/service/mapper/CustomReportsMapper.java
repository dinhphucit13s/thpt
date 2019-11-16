package fpt.dps.dtms.service.mapper;

import org.mapstruct.Mapper;

import fpt.dps.dtms.domain.CustomReports;
import fpt.dps.dtms.domain.TMSCustomField;
import fpt.dps.dtms.service.dto.CustomReportsDTO;
import fpt.dps.dtms.service.dto.TMSCustomFieldDTO;

/**
 * Mapper for the entity TMSCustomField and its DTO TMSCustomFieldDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CustomReportsMapper extends EntityMapper<CustomReportsDTO, CustomReports>{
	default CustomReports fromId(Long id) {
        if (id == null) {
            return null;
        }
        CustomReports customReports = new CustomReports();
        customReports.setId(id);
        return customReports;
    }
}
