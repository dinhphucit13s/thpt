package fpt.dps.dtms.service.mapper;

import fpt.dps.dtms.domain.CustomReports;
import fpt.dps.dtms.service.dto.CustomReportsDTO;
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
public class CustomReportsMapperImpl implements CustomReportsMapper {

    @Override
    public CustomReports toEntity(CustomReportsDTO dto) {
        if ( dto == null ) {
            return null;
        }

        CustomReports customReports = new CustomReports();

        customReports.setCreatedBy( dto.getCreatedBy() );
        customReports.setCreatedDate( dto.getCreatedDate() );
        customReports.setLastModifiedBy( dto.getLastModifiedBy() );
        customReports.setLastModifiedDate( dto.getLastModifiedDate() );
        customReports.setId( dto.getId() );
        customReports.setUserLogin( dto.getUserLogin() );
        customReports.setPageName( dto.getPageName() );
        customReports.setValue( dto.getValue() );

        return customReports;
    }

    @Override
    public CustomReportsDTO toDto(CustomReports entity) {
        if ( entity == null ) {
            return null;
        }

        CustomReportsDTO customReportsDTO = new CustomReportsDTO();

        customReportsDTO.setCreatedBy( entity.getCreatedBy() );
        customReportsDTO.setCreatedDate( entity.getCreatedDate() );
        customReportsDTO.setLastModifiedBy( entity.getLastModifiedBy() );
        customReportsDTO.setLastModifiedDate( entity.getLastModifiedDate() );
        customReportsDTO.setId( entity.getId() );
        customReportsDTO.setUserLogin( entity.getUserLogin() );
        customReportsDTO.setPageName( entity.getPageName() );
        customReportsDTO.setValue( entity.getValue() );

        return customReportsDTO;
    }

    @Override
    public List<CustomReports> toEntity(List<CustomReportsDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<CustomReports> list = new ArrayList<CustomReports>( dtoList.size() );
        for ( CustomReportsDTO customReportsDTO : dtoList ) {
            list.add( toEntity( customReportsDTO ) );
        }

        return list;
    }

    @Override
    public List<CustomReportsDTO> toDto(List<CustomReports> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<CustomReportsDTO> list = new ArrayList<CustomReportsDTO>( entityList.size() );
        for ( CustomReports customReports : entityList ) {
            list.add( toDto( customReports ) );
        }

        return list;
    }
}
