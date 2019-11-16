package fpt.dps.dtms.service.mapper;

import fpt.dps.dtms.domain.*;
import fpt.dps.dtms.service.dto.IssuesDTO;
import fpt.dps.dtms.service.dto.MailDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Mail and its DTO MailDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface MailMapper extends EntityMapper<MailDTO, Mail> {

	@Mapping(source = "attachments", target = "attachments")
	MailDTO toDto(Mail mail);
    default Mail fromId(Long id) {
        if (id == null) {
            return null;
        }
        Mail mail = new Mail();
        mail.setId(id);
        return mail;
    }
}
