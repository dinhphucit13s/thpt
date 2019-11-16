package fpt.dps.dtms.service.mapper;

import fpt.dps.dtms.domain.BusinessUnit;
import fpt.dps.dtms.service.dto.BusinessUnitDTO;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2019-10-04T10:12:01+0700",
    comments = "version: 1.2.0.Final, compiler: Eclipse JDT (IDE) 1.2.100.v20160418-1457, environment: Java 1.8.0_111 (Oracle Corporation)"
)
@Component
public class BusinessUnitMapperImpl implements BusinessUnitMapper {

    @Override
    public BusinessUnit toEntity(BusinessUnitDTO dto) {
        if ( dto == null ) {
            return null;
        }

        BusinessUnit businessUnit = new BusinessUnit();

        businessUnit.setCreatedBy( dto.getCreatedBy() );
        businessUnit.setCreatedDate( dto.getCreatedDate() );
        businessUnit.setLastModifiedBy( dto.getLastModifiedBy() );
        businessUnit.setLastModifiedDate( dto.getLastModifiedDate() );
        businessUnit.setId( dto.getId() );
        businessUnit.setCode( dto.getCode() );
        businessUnit.setName( dto.getName() );
        businessUnit.setDescription( dto.getDescription() );

        return businessUnit;
    }

    @Override
    public BusinessUnitDTO toDto(BusinessUnit entity) {
        if ( entity == null ) {
            return null;
        }

        BusinessUnitDTO businessUnitDTO = new BusinessUnitDTO();

        businessUnitDTO.setCreatedBy( entity.getCreatedBy() );
        businessUnitDTO.setCreatedDate( entity.getCreatedDate() );
        businessUnitDTO.setLastModifiedBy( entity.getLastModifiedBy() );
        businessUnitDTO.setLastModifiedDate( entity.getLastModifiedDate() );
        businessUnitDTO.setId( entity.getId() );
        businessUnitDTO.setCode( entity.getCode() );
        businessUnitDTO.setName( entity.getName() );
        businessUnitDTO.setDescription( entity.getDescription() );

        return businessUnitDTO;
    }

    @Override
    public List<BusinessUnit> toEntity(List<BusinessUnitDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<BusinessUnit> list = new ArrayList<BusinessUnit>( dtoList.size() );
        for ( BusinessUnitDTO businessUnitDTO : dtoList ) {
            list.add( toEntity( businessUnitDTO ) );
        }

        return list;
    }

    @Override
    public List<BusinessUnitDTO> toDto(List<BusinessUnit> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<BusinessUnitDTO> list = new ArrayList<BusinessUnitDTO>( entityList.size() );
        for ( BusinessUnit businessUnit : entityList ) {
            list.add( toDto( businessUnit ) );
        }

        return list;
    }
}
