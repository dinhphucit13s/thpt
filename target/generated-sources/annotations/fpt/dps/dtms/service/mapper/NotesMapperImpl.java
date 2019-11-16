package fpt.dps.dtms.service.mapper;

import fpt.dps.dtms.domain.Attachments;
import fpt.dps.dtms.domain.Bugs;
import fpt.dps.dtms.domain.Notes;
import fpt.dps.dtms.domain.Tasks;
import fpt.dps.dtms.service.dto.AttachmentsDTO;
import fpt.dps.dtms.service.dto.NotesDTO;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2019-10-04T10:12:00+0700",
    comments = "version: 1.2.0.Final, compiler: Eclipse JDT (IDE) 1.2.100.v20160418-1457, environment: Java 1.8.0_111 (Oracle Corporation)"
)
@Component
public class NotesMapperImpl implements NotesMapper {

    @Autowired
    private TasksMapper tasksMapper;
    @Autowired
    private BugsMapper bugsMapper;
    @Autowired
    private AttachmentsMapper attachmentsMapper;

    @Override
    public List<Notes> toEntity(List<NotesDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<Notes> list = new ArrayList<Notes>( dtoList.size() );
        for ( NotesDTO notesDTO : dtoList ) {
            list.add( toEntity( notesDTO ) );
        }

        return list;
    }

    @Override
    public List<NotesDTO> toDto(List<Notes> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<NotesDTO> list = new ArrayList<NotesDTO>( entityList.size() );
        for ( Notes notes : entityList ) {
            list.add( toDto( notes ) );
        }

        return list;
    }

    @Override
    public NotesDTO toDto(Notes notes) {
        if ( notes == null ) {
            return null;
        }

        NotesDTO notesDTO = new NotesDTO();

        String name = notesTasksName( notes );
        if ( name != null ) {
            notesDTO.setTasksName( name );
        }
        Long id = notesBugId( notes );
        if ( id != null ) {
            notesDTO.setBugId( id );
        }
        Long id1 = notesTasksId( notes );
        if ( id1 != null ) {
            notesDTO.setTasksId( id1 );
        }
        notesDTO.setCreatedBy( notes.getCreatedBy() );
        notesDTO.setCreatedDate( notes.getCreatedDate() );
        notesDTO.setLastModifiedBy( notes.getLastModifiedBy() );
        notesDTO.setLastModifiedDate( notes.getLastModifiedDate() );
        notesDTO.setAttachments( attachmentsSetToAttachmentsDTOSet( notes.getAttachments() ) );
        notesDTO.setId( notes.getId() );
        notesDTO.setDescription( notes.getDescription() );

        return notesDTO;
    }

    @Override
    public Notes toEntity(NotesDTO notesDTO) {
        if ( notesDTO == null ) {
            return null;
        }

        Notes notes = new Notes();

        notes.setBug( bugsMapper.fromId( notesDTO.getBugId() ) );
        notes.setTasks( tasksMapper.fromId( notesDTO.getTasksId() ) );
        notes.setCreatedBy( notesDTO.getCreatedBy() );
        notes.setCreatedDate( notesDTO.getCreatedDate() );
        notes.setLastModifiedBy( notesDTO.getLastModifiedBy() );
        notes.setLastModifiedDate( notesDTO.getLastModifiedDate() );
        notes.setAttachments( attachmentsDTOSetToAttachmentsSet( notesDTO.getAttachments() ) );
        notes.setId( notesDTO.getId() );
        notes.setDescription( notesDTO.getDescription() );

        return notes;
    }

    private String notesTasksName(Notes notes) {
        if ( notes == null ) {
            return null;
        }
        Tasks tasks = notes.getTasks();
        if ( tasks == null ) {
            return null;
        }
        String name = tasks.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }

    private Long notesBugId(Notes notes) {
        if ( notes == null ) {
            return null;
        }
        Bugs bug = notes.getBug();
        if ( bug == null ) {
            return null;
        }
        Long id = bug.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private Long notesTasksId(Notes notes) {
        if ( notes == null ) {
            return null;
        }
        Tasks tasks = notes.getTasks();
        if ( tasks == null ) {
            return null;
        }
        Long id = tasks.getId();
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
