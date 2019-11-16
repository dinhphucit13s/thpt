package fpt.dps.dtms.service.mapper;

import fpt.dps.dtms.domain.Notification;
import fpt.dps.dtms.service.dto.NotificationDTO;
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
public class NotificationMapperImpl implements NotificationMapper {

    @Override
    public Notification toEntity(NotificationDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Notification notification = new Notification();

        notification.setCreatedBy( dto.getCreatedBy() );
        notification.setCreatedDate( dto.getCreatedDate() );
        notification.setLastModifiedBy( dto.getLastModifiedBy() );
        notification.setLastModifiedDate( dto.getLastModifiedDate() );
        notification.setId( dto.getId() );
        notification.setFrom( dto.getFrom() );
        notification.setTo( dto.getTo() );
        notification.setBody( dto.getBody() );
        notification.setStatus( dto.isStatus() );

        return notification;
    }

    @Override
    public NotificationDTO toDto(Notification entity) {
        if ( entity == null ) {
            return null;
        }

        NotificationDTO notificationDTO = new NotificationDTO();

        notificationDTO.setCreatedBy( entity.getCreatedBy() );
        notificationDTO.setCreatedDate( entity.getCreatedDate() );
        notificationDTO.setLastModifiedBy( entity.getLastModifiedBy() );
        notificationDTO.setLastModifiedDate( entity.getLastModifiedDate() );
        notificationDTO.setId( entity.getId() );
        notificationDTO.setFrom( entity.getFrom() );
        notificationDTO.setTo( entity.getTo() );
        notificationDTO.setBody( entity.getBody() );
        notificationDTO.setStatus( entity.isStatus() );

        return notificationDTO;
    }

    @Override
    public List<Notification> toEntity(List<NotificationDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<Notification> list = new ArrayList<Notification>( dtoList.size() );
        for ( NotificationDTO notificationDTO : dtoList ) {
            list.add( toEntity( notificationDTO ) );
        }

        return list;
    }

    @Override
    public List<NotificationDTO> toDto(List<Notification> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<NotificationDTO> list = new ArrayList<NotificationDTO>( entityList.size() );
        for ( Notification notification : entityList ) {
            list.add( toDto( notification ) );
        }

        return list;
    }
}
