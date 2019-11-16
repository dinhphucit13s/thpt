package fpt.dps.dtms.service.mapper;

import fpt.dps.dtms.domain.DtmsMonitoring;
import fpt.dps.dtms.service.dto.DtmsMonitoringDTO;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2019-10-04T10:12:00+0700",
    comments = "version: 1.2.0.Final, compiler: Eclipse JDT (IDE) 1.2.100.v20160418-1457, environment: Java 1.8.0_111 (Oracle Corporation)"
)
@Component
public class DtmsMonitoringMapperImpl implements DtmsMonitoringMapper {

    @Override
    public DtmsMonitoring toEntity(DtmsMonitoringDTO dto) {
        if ( dto == null ) {
            return null;
        }

        DtmsMonitoring dtmsMonitoring = new DtmsMonitoring();

        dtmsMonitoring.setCreatedBy( dto.getCreatedBy() );
        dtmsMonitoring.setCreatedDate( dto.getCreatedDate() );
        dtmsMonitoring.setLastModifiedBy( dto.getLastModifiedBy() );
        dtmsMonitoring.setLastModifiedDate( dto.getLastModifiedDate() );
        dtmsMonitoring.setId( dto.getId() );
        dtmsMonitoring.setPosition( dto.getPosition() );
        dtmsMonitoring.setPositionId( dto.getPositionId() );
        dtmsMonitoring.setRole( dto.getRole() );
        dtmsMonitoring.setMembers( dto.getMembers() );

        return dtmsMonitoring;
    }

    @Override
    public DtmsMonitoringDTO toDto(DtmsMonitoring entity) {
        if ( entity == null ) {
            return null;
        }

        DtmsMonitoringDTO dtmsMonitoringDTO = new DtmsMonitoringDTO();

        dtmsMonitoringDTO.setCreatedBy( entity.getCreatedBy() );
        dtmsMonitoringDTO.setCreatedDate( entity.getCreatedDate() );
        dtmsMonitoringDTO.setLastModifiedBy( entity.getLastModifiedBy() );
        dtmsMonitoringDTO.setLastModifiedDate( entity.getLastModifiedDate() );
        dtmsMonitoringDTO.setId( entity.getId() );
        dtmsMonitoringDTO.setPosition( entity.getPosition() );
        dtmsMonitoringDTO.setPositionId( entity.getPositionId() );
        dtmsMonitoringDTO.setRole( entity.getRole() );
        dtmsMonitoringDTO.setMembers( entity.getMembers() );

        return dtmsMonitoringDTO;
    }

    @Override
    public List<DtmsMonitoring> toEntity(List<DtmsMonitoringDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<DtmsMonitoring> list = new ArrayList<DtmsMonitoring>( dtoList.size() );
        for ( DtmsMonitoringDTO dtmsMonitoringDTO : dtoList ) {
            list.add( toEntity( dtmsMonitoringDTO ) );
        }

        return list;
    }

    @Override
    public List<DtmsMonitoringDTO> toDto(List<DtmsMonitoring> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<DtmsMonitoringDTO> list = new ArrayList<DtmsMonitoringDTO>( entityList.size() );
        for ( DtmsMonitoring dtmsMonitoring : entityList ) {
            list.add( toDto( dtmsMonitoring ) );
        }

        return list;
    }
}
