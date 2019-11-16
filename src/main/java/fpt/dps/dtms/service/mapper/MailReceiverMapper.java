package fpt.dps.dtms.service.mapper;

import fpt.dps.dtms.domain.*;
import fpt.dps.dtms.service.dto.MailReceiverDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity MailReceiver and its DTO MailReceiverDTO.
 */
@Mapper(componentModel = "spring", uses = {MailMapper.class})
public interface MailReceiverMapper extends EntityMapper<MailReceiverDTO, MailReceiver> {

    @Mapping(source = "mail.id", target = "mailId")
    @Mapping(source = "mail.subject", target = "mailSubject")
    MailReceiverDTO toDto(MailReceiver mailReceiver);

    @Mapping(source = "mailId", target = "mail")
    MailReceiver toEntity(MailReceiverDTO mailReceiverDTO);

    default MailReceiver fromId(Long id) {
        if (id == null) {
            return null;
        }
        MailReceiver mailReceiver = new MailReceiver();
        mailReceiver.setId(id);
        return mailReceiver;
    }
}
