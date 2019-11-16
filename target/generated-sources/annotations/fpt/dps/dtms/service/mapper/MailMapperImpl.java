package fpt.dps.dtms.service.mapper;

import fpt.dps.dtms.domain.Attachments;
import fpt.dps.dtms.domain.Mail;
import fpt.dps.dtms.service.dto.AttachmentsDTO;
import fpt.dps.dtms.service.dto.MailDTO;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2019-10-04T10:12:01+0700",
    comments = "version: 1.2.0.Final, compiler: Eclipse JDT (IDE) 1.2.100.v20160418-1457, environment: Java 1.8.0_111 (Oracle Corporation)"
)
@Component
public class MailMapperImpl implements MailMapper {

    @Override
    public Mail toEntity(MailDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Mail mail = new Mail();

        mail.setCreatedBy( dto.getCreatedBy() );
        mail.setCreatedDate( dto.getCreatedDate() );
        mail.setLastModifiedBy( dto.getLastModifiedBy() );
        mail.setLastModifiedDate( dto.getLastModifiedDate() );
        mail.setId( dto.getId() );
        mail.setFrom( dto.getFrom() );
        mail.setSubject( dto.getSubject() );
        mail.setBody( dto.getBody() );
        mail.setStartTime( dto.getStartTime() );
        mail.setEndTime( dto.getEndTime() );
        mail.setAttachments( attachmentsDTOSetToAttachmentsSet( dto.getAttachments() ) );

        return mail;
    }

    @Override
    public List<Mail> toEntity(List<MailDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<Mail> list = new ArrayList<Mail>( dtoList.size() );
        for ( MailDTO mailDTO : dtoList ) {
            list.add( toEntity( mailDTO ) );
        }

        return list;
    }

    @Override
    public List<MailDTO> toDto(List<Mail> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<MailDTO> list = new ArrayList<MailDTO>( entityList.size() );
        for ( Mail mail : entityList ) {
            list.add( toDto( mail ) );
        }

        return list;
    }

    @Override
    public MailDTO toDto(Mail mail) {
        if ( mail == null ) {
            return null;
        }

        MailDTO mailDTO = new MailDTO();

        mailDTO.setAttachments( attachmentsSetToAttachmentsDTOSet( mail.getAttachments() ) );
        mailDTO.setCreatedBy( mail.getCreatedBy() );
        mailDTO.setCreatedDate( mail.getCreatedDate() );
        mailDTO.setLastModifiedBy( mail.getLastModifiedBy() );
        mailDTO.setLastModifiedDate( mail.getLastModifiedDate() );
        mailDTO.setId( mail.getId() );
        mailDTO.setFrom( mail.getFrom() );
        mailDTO.setSubject( mail.getSubject() );
        mailDTO.setBody( mail.getBody() );
        mailDTO.setStartTime( mail.getStartTime() );
        mailDTO.setEndTime( mail.getEndTime() );

        return mailDTO;
    }

    protected Attachments attachmentsDTOToAttachments(AttachmentsDTO attachmentsDTO) {
        if ( attachmentsDTO == null ) {
            return null;
        }

        Attachments attachments = new Attachments();

        attachments.setCreatedBy( attachmentsDTO.getCreatedBy() );
        attachments.setCreatedDate( attachmentsDTO.getCreatedDate() );
        attachments.setLastModifiedBy( attachmentsDTO.getLastModifiedBy() );
        attachments.setLastModifiedDate( attachmentsDTO.getLastModifiedDate() );
        attachments.setId( attachmentsDTO.getId() );
        attachments.setFilename( attachmentsDTO.getFilename() );
        attachments.setDiskFile( attachmentsDTO.getDiskFile() );
        attachments.setFileType( attachmentsDTO.getFileType() );

        return attachments;
    }

    protected Set<Attachments> attachmentsDTOSetToAttachmentsSet(Set<AttachmentsDTO> set) {
        if ( set == null ) {
            return null;
        }

        Set<Attachments> set1 = new HashSet<Attachments>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( AttachmentsDTO attachmentsDTO : set ) {
            set1.add( attachmentsDTOToAttachments( attachmentsDTO ) );
        }

        return set1;
    }

    protected AttachmentsDTO attachmentsToAttachmentsDTO(Attachments attachments) {
        if ( attachments == null ) {
            return null;
        }

        AttachmentsDTO attachmentsDTO = new AttachmentsDTO();

        attachmentsDTO.setCreatedBy( attachments.getCreatedBy() );
        attachmentsDTO.setCreatedDate( attachments.getCreatedDate() );
        attachmentsDTO.setLastModifiedBy( attachments.getLastModifiedBy() );
        attachmentsDTO.setLastModifiedDate( attachments.getLastModifiedDate() );
        attachmentsDTO.setId( attachments.getId() );
        attachmentsDTO.setFilename( attachments.getFilename() );
        attachmentsDTO.setDiskFile( attachments.getDiskFile() );
        attachmentsDTO.setFileType( attachments.getFileType() );

        return attachmentsDTO;
    }

    protected Set<AttachmentsDTO> attachmentsSetToAttachmentsDTOSet(Set<Attachments> set) {
        if ( set == null ) {
            return null;
        }

        Set<AttachmentsDTO> set1 = new HashSet<AttachmentsDTO>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( Attachments attachments : set ) {
            set1.add( attachmentsToAttachmentsDTO( attachments ) );
        }

        return set1;
    }
}
