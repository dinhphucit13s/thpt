package fpt.dps.dtms.service.mapper;

import fpt.dps.dtms.domain.TMSCustomField;
import fpt.dps.dtms.service.dto.TMSCustomFieldDTO;
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
public class TMSCustomFieldMapperImpl implements TMSCustomFieldMapper {

    @Override
    public TMSCustomField toEntity(TMSCustomFieldDTO dto) {
        if ( dto == null ) {
            return null;
        }

        TMSCustomField tMSCustomField = new TMSCustomField();

        tMSCustomField.setCreatedBy( dto.getCreatedBy() );
        tMSCustomField.setCreatedDate( dto.getCreatedDate() );
        tMSCustomField.setLastModifiedBy( dto.getLastModifiedBy() );
        tMSCustomField.setLastModifiedDate( dto.getLastModifiedDate() );
        tMSCustomField.setId( dto.getId() );
        tMSCustomField.setEntityData( dto.getEntityData() );

        return tMSCustomField;
    }

    @Override
    public TMSCustomFieldDTO toDto(TMSCustomField entity) {
        if ( entity == null ) {
            return null;
        }

        TMSCustomFieldDTO tMSCustomFieldDTO = new TMSCustomFieldDTO();

        tMSCustomFieldDTO.setCreatedBy( entity.getCreatedBy() );
        tMSCustomFieldDTO.setCreatedDate( entity.getCreatedDate() );
        tMSCustomFieldDTO.setLastModifiedBy( entity.getLastModifiedBy() );
        tMSCustomFieldDTO.setLastModifiedDate( entity.getLastModifiedDate() );
        tMSCustomFieldDTO.setId( entity.getId() );
        tMSCustomFieldDTO.setEntityData( entity.getEntityData() );

        return tMSCustomFieldDTO;
    }

    @Override
    public List<TMSCustomField> toEntity(List<TMSCustomFieldDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<TMSCustomField> list = new ArrayList<TMSCustomField>( dtoList.size() );
        for ( TMSCustomFieldDTO tMSCustomFieldDTO : dtoList ) {
            list.add( toEntity( tMSCustomFieldDTO ) );
        }

        return list;
    }

    @Override
    public List<TMSCustomFieldDTO> toDto(List<TMSCustomField> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<TMSCustomFieldDTO> list = new ArrayList<TMSCustomFieldDTO>( entityList.size() );
        for ( TMSCustomField tMSCustomField : entityList ) {
            list.add( toDto( tMSCustomField ) );
        }

        return list;
    }
}
