package fpt.dps.dtms.service.mapper;

import fpt.dps.dtms.domain.*;
import fpt.dps.dtms.service.dto.NotificationTemplateDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity NotificationTemplate and its DTO NotificationTemplateDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface NotificationTemplateMapper extends EntityMapper<NotificationTemplateDTO, NotificationTemplate> {



    default NotificationTemplate fromId(Long id) {
        if (id == null) {
            return null;
        }
        NotificationTemplate notificationTemplate = new NotificationTemplate();
        notificationTemplate.setId(id);
        return notificationTemplate;
    }
}
