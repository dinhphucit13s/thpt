package fpt.dps.dtms.service.mapper;

import fpt.dps.dtms.domain.BusinessUnit;
import fpt.dps.dtms.service.dto.BusinessUnitMSCDTO;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2019-10-04T23:36:19+0700",
    comments = "version: 1.2.0.Final, compiler: Eclipse JDT (IDE) 1.2.100.v20160418-1457, environment: Java 1.8.0_111 (Oracle Corporation)"
)
@Component
public class BusinessUnitMSCMapperImpl implements BusinessUnitMSCMapper {

    @Override
    public BusinessUnitMSCDTO toDto(BusinessUnit arg0) {
        if ( arg0 == null ) {
            return null;
        }

        BusinessUnitMSCDTO businessUnitMSCDTO = new BusinessUnitMSCDTO();

        businessUnitMSCDTO.setId( arg0.getId() );
        businessUnitMSCDTO.setCode( arg0.getCode() );
        businessUnitMSCDTO.setName( arg0.getName() );

        return businessUnitMSCDTO;
    }

    @Override
    public List<BusinessUnitMSCDTO> toDto(List<BusinessUnit> arg0) {
        if ( arg0 == null ) {
            return null;
        }

        List<BusinessUnitMSCDTO> list = new ArrayList<BusinessUnitMSCDTO>( arg0.size() );
        for ( BusinessUnit businessUnit : arg0 ) {
            list.add( toDto( businessUnit ) );
        }

        return list;
    }

    @Override
    public BusinessUnit toEntity(BusinessUnitMSCDTO arg0) {
        if ( arg0 == null ) {
            return null;
        }

        BusinessUnit businessUnit = new BusinessUnit();

        businessUnit.setCode( arg0.getCode() );
        businessUnit.setId( arg0.getId() );
        businessUnit.setName( arg0.getName() );

        return businessUnit;
    }

    @Override
    public List<BusinessUnit> toEntity(List<BusinessUnitMSCDTO> arg0) {
        if ( arg0 == null ) {
            return null;
        }

        List<BusinessUnit> list = new ArrayList<BusinessUnit>( arg0.size() );
        for ( BusinessUnitMSCDTO businessUnitMSCDTO : arg0 ) {
            list.add( toEntity( businessUnitMSCDTO ) );
        }

        return list;
    }
}
