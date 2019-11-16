package fpt.dps.dtms.service.mapper;

import fpt.dps.dtms.domain.Attachments;
import fpt.dps.dtms.domain.TmsPost;
import fpt.dps.dtms.domain.TmsThread;
import fpt.dps.dtms.service.dto.AttachmentsDTO;
import fpt.dps.dtms.service.dto.TmsPostDTO;
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
public class TmsPostMapperImpl implements TmsPostMapper {

    @Autowired
    private TmsThreadMapper tmsThreadMapper;
    @Autowired
    private AttachmentsMapper attachmentsMapper;

    @Override
    public List<TmsPost> toEntity(List<TmsPostDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<TmsPost> list = new ArrayList<TmsPost>( dtoList.size() );
        for ( TmsPostDTO tmsPostDTO : dtoList ) {
            list.add( toEntity( tmsPostDTO ) );
        }

        return list;
    }

    @Override
    public List<TmsPostDTO> toDto(List<TmsPost> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<TmsPostDTO> list = new ArrayList<TmsPostDTO>( entityList.size() );
        for ( TmsPost tmsPost : entityList ) {
            list.add( toDto( tmsPost ) );
        }

        return list;
    }

    @Override
    public TmsPostDTO toDto(TmsPost tmsPost) {
        if ( tmsPost == null ) {
            return null;
        }

        TmsPostDTO tmsPostDTO = new TmsPostDTO();

        Long id = tmsPostThreadId( tmsPost );
        if ( id != null ) {
            tmsPostDTO.setThreadId( id );
        }
        tmsPostDTO.setAttachments( attachmentsSetToAttachmentsDTOSet( tmsPost.getAttachments() ) );
        tmsPostDTO.setCreatedBy( tmsPost.getCreatedBy() );
        tmsPostDTO.setCreatedDate( tmsPost.getCreatedDate() );
        tmsPostDTO.setLastModifiedBy( tmsPost.getLastModifiedBy() );
        tmsPostDTO.setLastModifiedDate( tmsPost.getLastModifiedDate() );
        tmsPostDTO.setId( tmsPost.getId() );
        tmsPostDTO.setContent( tmsPost.getContent() );
        tmsPostDTO.setComments( tmsPost.getComments() );

        return tmsPostDTO;
    }

    @Override
    public TmsPost toEntity(TmsPostDTO tmsPostDTO) {
        if ( tmsPostDTO == null ) {
            return null;
        }

        TmsPost tmsPost = new TmsPost();

        tmsPost.setThread( tmsThreadMapper.fromId( tmsPostDTO.getThreadId() ) );
        tmsPost.setCreatedBy( tmsPostDTO.getCreatedBy() );
        tmsPost.setCreatedDate( tmsPostDTO.getCreatedDate() );
        tmsPost.setLastModifiedBy( tmsPostDTO.getLastModifiedBy() );
        tmsPost.setLastModifiedDate( tmsPostDTO.getLastModifiedDate() );
        tmsPost.setAttachments( attachmentsDTOSetToAttachmentsSet( tmsPostDTO.getAttachments() ) );
        tmsPost.setId( tmsPostDTO.getId() );
        tmsPost.setContent( tmsPostDTO.getContent() );
        tmsPost.setComments( tmsPostDTO.getComments() );

        return tmsPost;
    }

    private Long tmsPostThreadId(TmsPost tmsPost) {
        if ( tmsPost == null ) {
            return null;
        }
        TmsThread thread = tmsPost.getThread();
        if ( thread == null ) {
            return null;
        }
        Long id = thread.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    protected Set<AttachmentsDTO> attachmentsSetToAttachmentsDTOSet(Set<Attachments> set) {
        if ( set == null ) {
            return null;
        }

        Set<AttachmentsDTO> set1 = new HashSet<AttachmentsDTO>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( Attachments attachments : set ) {
            set1.add( attachmentsMapper.toDto( attachments ) );
        }

        return set1;
    }

    protected Set<Attachments> attachmentsDTOSetToAttachmentsSet(Set<AttachmentsDTO> set) {
        if ( set == null ) {
            return null;
        }

        Set<Attachments> set1 = new HashSet<Attachments>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( AttachmentsDTO attachmentsDTO : set ) {
            set1.add( attachmentsMapper.toEntity( attachmentsDTO ) );
        }

        return set1;
    }
}
