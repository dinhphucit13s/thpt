package fpt.dps.dtms.service.mapper;

import fpt.dps.dtms.domain.*;
import fpt.dps.dtms.service.dto.DtmsMonitoringDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity DtmsMonitoring and its DTO DtmsMonitoringDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface DtmsMonitoringMapper extends EntityMapper<DtmsMonitoringDTO, DtmsMonitoring> {



    default DtmsMonitoring fromId(Long id) {
        if (id == null) {
            return null;
        }
        DtmsMonitoring dtmsMonitoring = new DtmsMonitoring();
        dtmsMonitoring.setId(id);
        return dtmsMonitoring;
    }
}
