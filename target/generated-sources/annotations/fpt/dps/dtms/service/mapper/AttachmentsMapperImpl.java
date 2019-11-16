package fpt.dps.dtms.service.mapper;

import fpt.dps.dtms.domain.Attachments;
import fpt.dps.dtms.domain.Bugs;
import fpt.dps.dtms.domain.Comments;
import fpt.dps.dtms.domain.Issues;
import fpt.dps.dtms.domain.Mail;
import fpt.dps.dtms.domain.Notes;
import fpt.dps.dtms.domain.TmsPost;
import fpt.dps.dtms.service.dto.AttachmentsDTO;
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
public class AttachmentsMapperImpl implements AttachmentsMapper {

    @Autowired
    private BugsMapper bugsMapper;
    @Autowired
    private NotesMapper notesMapper;
    @Autowired
    private IssuesMapper issuesMapper;
    @Autowired
    private MailMapper mailMapper;
    @Autowired
    private CommentsMapper commentsMapper;
    @Autowired
    private TmsPostMapper tmsPostMapper;

    @Override
    public List<Attachments> toEntity(List<AttachmentsDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<Attachments> list = new ArrayList<Attachments>( dtoList.size() );
        for ( AttachmentsDTO attachmentsDTO : dtoList ) {
            list.add( toEntity( attachmentsDTO ) );
        }

        return list;
    }

    @Override
    public List<AttachmentsDTO> toDto(List<Attachments> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<AttachmentsDTO> list = new ArrayList<AttachmentsDTO>( entityList.size() );
        for ( Attachments attachments : entityList ) {
            list.add( toDto( attachments ) );
        }

        return list;
    }

    @Override
    public AttachmentsDTO toDto(Attachments attachments) {
        if ( attachments == null ) {
            return null;
        }

        AttachmentsDTO attachmentsDTO = new AttachmentsDTO();

        Long id = attachmentsIssuesId( attachments );
        if ( id != null ) {
            attachmentsDTO.setIssuesId( id );
        }
        String description = attachmentsBugsDescription( attachments );
        if ( description != null ) {
            attachmentsDTO.setBugsDescription( description );
        }
        Long id1 = attachmentsNotesId( attachments );
        if ( id1 != null ) {
            attachmentsDTO.setNotesId( id1 );
        }
        String name = attachmentsIssuesName( attachments );
        if ( name != null ) {
            attachmentsDTO.setIssuesName( name );
        }
        Long id2 = attachmentsBugsId( attachments );
        if ( id2 != null ) {
            attachmentsDTO.setBugsId( id2 );
        }
        String description1 = attachmentsNotesDescription( attachments );
        if ( description1 != null ) {
            attachmentsDTO.setNotesDescription( description1 );
        }
        Long id3 = attachmentsCommentId( attachments );
        if ( id3 != null ) {
            attachmentsDTO.setCommentId( id3 );
        }
        Long id4 = attachmentsMailId( attachments );
        if ( id4 != null ) {
            attachmentsDTO.setMailId( id4 );
        }
        Long id5 = attachmentsPostId( attachments );
        if ( id5 != null ) {
            attachmentsDTO.setPostId( id5 );
        }
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

    @Override
    public Attachments toEntity(AttachmentsDTO attachmentsDTO) {
        if ( attachmentsDTO == null ) {
            return null;
        }

        Attachments attachments = new Attachments();

        attachments.setBugs( bugsMapper.fromId( attachmentsDTO.getBugsId() ) );
        attachments.setNotes( notesMapper.fromId( attachmentsDTO.getNotesId() ) );
        attachments.setMail( mailMapper.fromId( attachmentsDTO.getMailId() ) );
        attachments.setPost( tmsPostMapper.fromId( attachmentsDTO.getPostId() ) );
        attachments.setComment( commentsMapper.fromId( attachmentsDTO.getCommentId() ) );
        attachments.setIssues( issuesMapper.fromId( attachmentsDTO.getIssuesId() ) );
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

    private Long attachmentsIssuesId(Attachments attachments) {
        if ( attachments == null ) {
            return null;
        }
        Issues issues = attachments.getIssues();
        if ( issues == null ) {
            return null;
        }
        Long id = issues.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String attachmentsBugsDescription(Attachments attachments) {
        if ( attachments == null ) {
            return null;
        }
        Bugs bugs = attachments.getBugs();
        if ( bugs == null ) {
            return null;
        }
        String description = bugs.getDescription();
        if ( description == null ) {
            return null;
        }
        return description;
    }

    private Long attachmentsNotesId(Attachments attachments) {
        if ( attachments == null ) {
            return null;
        }
        Notes notes = attachments.getNotes();
        if ( notes == null ) {
            return null;
        }
        Long id = notes.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String attachmentsIssuesName(Attachments attachments) {
        if ( attachments == null ) {
            return null;
        }
        Issues issues = attachments.getIssues();
        if ( issues == null ) {
            return null;
        }
        String name = issues.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }

    private Long attachmentsBugsId(Attachments attachments) {
        if ( attachments == null ) {
            return null;
        }
        Bugs bugs = attachments.getBugs();
        if ( bugs == null ) {
            return null;
        }
        Long id = bugs.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String attachmentsNotesDescription(Attachments attachments) {
        if ( attachments == null ) {
            return null;
        }
        Notes notes = attachments.getNotes();
        if ( notes == null ) {
            return null;
        }
        String description = notes.getDescription();
        if ( description == null ) {
            return null;
        }
        return description;
    }

    private Long attachmentsCommentId(Attachments attachments) {
        if ( attachments == null ) {
            return null;
        }
        Comments comment = attachments.getComment();
        if ( comment == null ) {
            return null;
        }
        Long id = comment.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private Long attachmentsMailId(Attachments attachments) {
        if ( attachments == null ) {
            return null;
        }
        Mail mail = attachments.getMail();
        if ( mail == null ) {
            return null;
        }
        Long id = mail.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private Long attachmentsPostId(Attachments attachments) {
        if ( attachments == null ) {
            return null;
        }
        TmsPost post = attachments.getPost();
        if ( post == null ) {
            return null;
        }
        Long id = post.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
