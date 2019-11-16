package fpt.dps.dtms.service.mapper;

import fpt.dps.dtms.domain.*;
import fpt.dps.dtms.service.dto.BusinessLineDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity BusinessLine and its DTO BusinessLineDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface BusinessLineMapper extends EntityMapper<BusinessLineDTO, BusinessLine> {



    default BusinessLine fromId(Long id) {
        if (id == null) {
            return null;
        }
        BusinessLine businessLine = new BusinessLine();
        businessLine.setId(id);
        return businessLine;
    }
}
