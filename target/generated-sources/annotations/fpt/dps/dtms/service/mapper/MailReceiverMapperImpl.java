package fpt.dps.dtms.service.mapper;

import fpt.dps.dtms.domain.Mail;
import fpt.dps.dtms.domain.MailReceiver;
import fpt.dps.dtms.service.dto.MailReceiverDTO;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2019-10-04T10:12:00+0700",
    comments = "version: 1.2.0.Final, compiler: Eclipse JDT (IDE) 1.2.100.v20160418-1457, environment: Java 1.8.0_111 (Oracle Corporation)"
)
@Component
public class MailReceiverMapperImpl implements MailReceiverMapper {

    @Autowired
    private MailMapper mailMapper;

    @Override
    public List<MailReceiver> toEntity(List<MailReceiverDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<MailReceiver> list = new ArrayList<MailReceiver>( dtoList.size() );
        for ( MailReceiverDTO mailReceiverDTO : dtoList ) {
            list.add( toEntity( mailReceiverDTO ) );
        }

        return list;
    }

    @Override
    public List<MailReceiverDTO> toDto(List<MailReceiver> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<MailReceiverDTO> list = new ArrayList<MailReceiverDTO>( entityList.size() );
        for ( MailReceiver mailReceiver : entityList ) {
            list.add( toDto( mailReceiver ) );
        }

        return list;
    }

    @Override
    public MailReceiverDTO toDto(MailReceiver mailReceiver) {
        if ( mailReceiver == null ) {
            return null;
        }

        MailReceiverDTO mailReceiverDTO = new MailReceiverDTO();

        String subject = mailReceiverMailSubject( mailReceiver );
        if ( subject != null ) {
            mailReceiverDTO.setMailSubject( subject );
        }
        Long id = mailReceiverMailId( mailReceiver );
        if ( id != null ) {
            mailReceiverDTO.setMailId( id );
        }
        mailReceiverDTO.setCreatedBy( mailReceiver.getCreatedBy() );
        mailReceiverDTO.setCreatedDate( mailReceiver.getCreatedDate() );
        mailReceiverDTO.setLastModifiedBy( mailReceiver.getLastModifiedBy() );
        mailReceiverDTO.setLastModifiedDate( mailReceiver.getLastModifiedDate() );
        mailReceiverDTO.setId( mailReceiver.getId() );
        mailReceiverDTO.setFrom( mailReceiver.getFrom() );
        mailReceiverDTO.setTo( mailReceiver.getTo() );
        mailReceiverDTO.setStatus( mailReceiver.isStatus() );

        return mailReceiverDTO;
    }

    @Override
    public MailReceiver toEntity(MailReceiverDTO mailReceiverDTO) {
        if ( mailReceiverDTO == null ) {
            return null;
        }

        MailReceiver mailReceiver = new MailReceiver();

        mailReceiver.setMail( mailMapper.fromId( mailReceiverDTO.getMailId() ) );
        mailReceiver.setCreatedBy( mailReceiverDTO.getCreatedBy() );
        mailReceiver.setCreatedDate( mailReceiverDTO.getCreatedDate() );
        mailReceiver.setLastModifiedBy( mailReceiverDTO.getLastModifiedBy() );
        mailReceiver.setLastModifiedDate( mailReceiverDTO.getLastModifiedDate() );
        mailReceiver.setId( mailReceiverDTO.getId() );
        mailReceiver.setFrom( mailReceiverDTO.getFrom() );
        mailReceiver.setTo( mailReceiverDTO.getTo() );
        mailReceiver.setStatus( mailReceiverDTO.isStatus() );

        return mailReceiver;
    }

    private String mailReceiverMailSubject(MailReceiver mailReceiver) {
        if ( mailReceiver == null ) {
            return null;
        }
        Mail mail = mailReceiver.getMail();
        if ( mail == null ) {
            return null;
        }
        String subject = mail.getSubject();
        if ( subject == null ) {
            return null;
        }
        return subject;
    }

    private Long mailReceiverMailId(MailReceiver mailReceiver) {
        if ( mailReceiver == null ) {
            return null;
        }
        Mail mail = mailReceiver.getMail();
        if ( mail == null ) {
            return null;
        }
        Long id = mail.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
