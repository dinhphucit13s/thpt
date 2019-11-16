package fpt.dps.dtms.service.mapper;

import fpt.dps.dtms.domain.BusinessLine;
import fpt.dps.dtms.service.dto.BusinessLineDTO;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2019-10-04T10:11:59+0700",
    comments = "version: 1.2.0.Final, compiler: Eclipse JDT (IDE) 1.2.100.v20160418-1457, environment: Java 1.8.0_111 (Oracle Corporation)"
)
@Component
public class BusinessLineMapperImpl implements BusinessLineMapper {

    @Override
    public BusinessLine toEntity(BusinessLineDTO dto) {
        if ( dto == null ) {
            return null;
        }

        BusinessLine businessLine = new BusinessLine();

        businessLine.setCreatedBy( dto.getCreatedBy() );
        businessLine.setCreatedDate( dto.getCreatedDate() );
        businessLine.setLastModifiedBy( dto.getLastModifiedBy() );
        businessLine.setLastModifiedDate( dto.getLastModifiedDate() );
        businessLine.setId( dto.getId() );
        businessLine.setName( dto.getName() );
        businessLine.setDescription( dto.getDescription() );

        return businessLine;
    }

    @Override
    public BusinessLineDTO toDto(BusinessLine entity) {
        if ( entity == null ) {
            return null;
        }

        BusinessLineDTO businessLineDTO = new BusinessLineDTO();

        businessLineDTO.setCreatedBy( entity.getCreatedBy() );
        businessLineDTO.setCreatedDate( entity.getCreatedDate() );
        businessLineDTO.setLastModifiedBy( entity.getLastModifiedBy() );
        businessLineDTO.setLastModifiedDate( entity.getLastModifiedDate() );
        businessLineDTO.setId( entity.getId() );
        businessLineDTO.setName( entity.getName() );
        businessLineDTO.setDescription( entity.getDescription() );

        return businessLineDTO;
    }

    @Override
    public List<BusinessLine> toEntity(List<BusinessLineDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<BusinessLine> list = new ArrayList<BusinessLine>( dtoList.size() );
        for ( BusinessLineDTO businessLineDTO : dtoList ) {
            list.add( toEntity( businessLineDTO ) );
        }

        return list;
    }

    @Override
    public List<BusinessLineDTO> toDto(List<BusinessLine> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<BusinessLineDTO> list = new ArrayList<BusinessLineDTO>( entityList.size() );
        for ( BusinessLine businessLine : entityList ) {
            list.add( toDto( businessLine ) );
        }

        return list;
    }
}
