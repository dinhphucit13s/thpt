package fpt.dps.dtms.service.mapper;

import fpt.dps.dtms.domain.Attachments;
import fpt.dps.dtms.domain.Comments;
import fpt.dps.dtms.domain.TmsPost;
import fpt.dps.dtms.service.dto.AttachmentsDTO;
import fpt.dps.dtms.service.dto.CommentsDTO;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2019-10-04T10:12:01+0700",
    comments = "version: 1.2.0.Final, compiler: Eclipse JDT (IDE) 1.2.100.v20160418-1457, environment: Java 1.8.0_111 (Oracle Corporation)"
)
@Component
public class CommentsMapperImpl implements CommentsMapper {

    @Autowired
    private TmsPostMapper tmsPostMapper;

    @Override
    public List<Comments> toEntity(List<CommentsDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<Comments> list = new ArrayList<Comments>( dtoList.size() );
        for ( CommentsDTO commentsDTO : dtoList ) {
            list.add( toEntity( commentsDTO ) );
        }

        return list;
    }

    @Override
    public List<CommentsDTO> toDto(List<Comments> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<CommentsDTO> list = new ArrayList<CommentsDTO>( entityList.size() );
        for ( Comments comments : entityList ) {
            list.add( toDto( comments ) );
        }

        return list;
    }

    @Override
    public CommentsDTO toDto(Comments comments) {
        if ( comments == null ) {
            return null;
        }

        CommentsDTO commentsDTO = new CommentsDTO();

        Long id = commentsPostId( comments );
        if ( id != null ) {
            commentsDTO.setPostId( id );
        }
        commentsDTO.setCreatedBy( comments.getCreatedBy() );
        commentsDTO.setCreatedDate( comments.getCreatedDate() );
        commentsDTO.setLastModifiedBy( comments.getLastModifiedBy() );
        commentsDTO.setLastModifiedDate( comments.getLastModifiedDate() );
        commentsDTO.setAttachments( attachmentsSetToAttachmentsDTOSet( comments.getAttachments() ) );
        commentsDTO.setId( comments.getId() );
        commentsDTO.setContent( comments.getContent() );

        return commentsDTO;
    }

    @Override
    public Comments toEntity(CommentsDTO commentsDTO) {
        if ( commentsDTO == null ) {
            return null;
        }

        Comments comments = new Comments();

        comments.setPost( tmsPostMapper.fromId( commentsDTO.getPostId() ) );
        comments.setCreatedBy( commentsDTO.getCreatedBy() );
        comments.setCreatedDate( commentsDTO.getCreatedDate() );
        comments.setLastModifiedBy( commentsDTO.getLastModifiedBy() );
        comments.setLastModifiedDate( commentsDTO.getLastModifiedDate() );
        comments.setAttachments( attachmentsDTOSetToAttachmentsSet( commentsDTO.getAttachments() ) );
        comments.setId( commentsDTO.getId() );
        comments.setContent( commentsDTO.getContent() );

        return comments;
    }

    private Long commentsPostId(Comments comments) {
        if ( comments == null ) {
            return null;
        }
        TmsPost post = comments.getPost();
        if ( post == null ) {
            return null;
        }
        Long id = post.getId();
        if ( id == null ) {
            return null;
        }
        return id;
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
}
