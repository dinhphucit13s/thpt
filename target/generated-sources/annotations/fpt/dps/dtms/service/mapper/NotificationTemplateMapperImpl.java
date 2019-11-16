package fpt.dps.dtms.service.mapper;

import fpt.dps.dtms.domain.NotificationTemplate;
import fpt.dps.dtms.service.dto.NotificationTemplateDTO;
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
public class NotificationTemplateMapperImpl implements NotificationTemplateMapper {

    @Override
    public NotificationTemplate toEntity(NotificationTemplateDTO dto) {
        if ( dto == null ) {
            return null;
        }

        NotificationTemplate notificationTemplate = new NotificationTemplate();

        notificationTemplate.setCreatedBy( dto.getCreatedBy() );
        notificationTemplate.setCreatedDate( dto.getCreatedDate() );
        notificationTemplate.setLastModifiedBy( dto.getLastModifiedBy() );
        notificationTemplate.setLastModifiedDate( dto.getLastModifiedDate() );
        notificationTemplate.setId( dto.getId() );
        notificationTemplate.setType( dto.getType() );
        notificationTemplate.setTemplate( dto.getTemplate() );
        notificationTemplate.setDescription( dto.getDescription() );

        return notificationTemplate;
    }

    @Override
    public NotificationTemplateDTO toDto(NotificationTemplate entity) {
        if ( entity == null ) {
            return null;
        }

        NotificationTemplateDTO notificationTemplateDTO = new NotificationTemplateDTO();

        notificationTemplateDTO.setCreatedBy( entity.getCreatedBy() );
        notificationTemplateDTO.setCreatedDate( entity.getCreatedDate() );
        notificationTemplateDTO.setLastModifiedBy( entity.getLastModifiedBy() );
        notificationTemplateDTO.setLastModifiedDate( entity.getLastModifiedDate() );
        notificationTemplateDTO.setId( entity.getId() );
        notificationTemplateDTO.setType( entity.getType() );
        notificationTemplateDTO.setTemplate( entity.getTemplate() );
        notificationTemplateDTO.setDescription( entity.getDescription() );

        return notificationTemplateDTO;
    }

    @Override
    public List<NotificationTemplate> toEntity(List<NotificationTemplateDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<NotificationTemplate> list = new ArrayList<NotificationTemplate>( dtoList.size() );
        for ( NotificationTemplateDTO notificationTemplateDTO : dtoList ) {
            list.add( toEntity( notificationTemplateDTO ) );
        }

        return list;
    }

    @Override
    public List<NotificationTemplateDTO> toDto(List<NotificationTemplate> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<NotificationTemplateDTO> list = new ArrayList<NotificationTemplateDTO>( entityList.size() );
        for ( NotificationTemplate notificationTemplate : entityList ) {
            list.add( toDto( notificationTemplate ) );
        }

        return list;
    }
}
