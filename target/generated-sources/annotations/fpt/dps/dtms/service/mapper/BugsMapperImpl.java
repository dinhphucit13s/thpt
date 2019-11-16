package fpt.dps.dtms.service.mapper;

import fpt.dps.dtms.domain.Attachments;
import fpt.dps.dtms.domain.Bugs;
import fpt.dps.dtms.domain.Notes;
import fpt.dps.dtms.domain.Tasks;
import fpt.dps.dtms.service.dto.AttachmentsDTO;
import fpt.dps.dtms.service.dto.BugsDTO;
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
public class BugsMapperImpl implements BugsMapper {

    @Autowired
    private TasksMapper tasksMapper;
    @Autowired
    private NotesMapper notesMapper;
    @Autowired
    private AttachmentsMapper attachmentsMapper;

    @Override
    public List<Bugs> toEntity(List<BugsDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<Bugs> list = new ArrayList<Bugs>( dtoList.size() );
        for ( BugsDTO bugsDTO : dtoList ) {
            list.add( toEntity( bugsDTO ) );
        }

        return list;
    }

    @Override
    public List<BugsDTO> toDto(List<Bugs> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<BugsDTO> list = new ArrayList<BugsDTO>( entityList.size() );
        for ( Bugs bugs : entityList ) {
            list.add( toDto( bugs ) );
        }

        return list;
    }

    @Override
    public BugsDTO toDto(Bugs bugs) {
        if ( bugs == null ) {
            return null;
        }

        BugsDTO bugsDTO = new BugsDTO();

        String name = bugsTasksName( bugs );
        if ( name != null ) {
            bugsDTO.setTasksName( name );
        }
        bugsDTO.setNotes( notesSetToNotesDTOSet( bugs.getNotes() ) );
        bugsDTO.setAttachments( attachmentsSetToAttachmentsDTOSet( bugs.getAttachments() ) );
        Long id = bugsTasksId( bugs );
        if ( id != null ) {
            bugsDTO.setTasksId( id );
        }
        bugsDTO.setCreatedBy( bugs.getCreatedBy() );
        bugsDTO.setCreatedDate( bugs.getCreatedDate() );
        bugsDTO.setLastModifiedBy( bugs.getLastModifiedBy() );
        bugsDTO.setLastModifiedDate( bugs.getLastModifiedDate() );
        bugsDTO.setId( bugs.getId() );
        bugsDTO.setDescription( bugs.getDescription() );
        bugsDTO.setCode( bugs.getCode() );
        bugsDTO.setStage( bugs.getStage() );
        bugsDTO.setIteration( bugs.getIteration() );
        bugsDTO.setStatus( bugs.getStatus() );
        bugsDTO.setResolution( bugs.getResolution() );
        bugsDTO.setPhysicalPath( bugs.getPhysicalPath() );

        return bugsDTO;
    }

    @Override
    public Bugs toEntity(BugsDTO bugsDTO) {
        if ( bugsDTO == null ) {
            return null;
        }

        Bugs bugs = new Bugs();

        bugs.setTasks( tasksMapper.fromId( bugsDTO.getTasksId() ) );
        bugs.setCreatedBy( bugsDTO.getCreatedBy() );
        bugs.setCreatedDate( bugsDTO.getCreatedDate() );
        bugs.setLastModifiedBy( bugsDTO.getLastModifiedBy() );
        bugs.setLastModifiedDate( bugsDTO.getLastModifiedDate() );
        bugs.setAttachments( attachmentsDTOSetToAttachmentsSet( bugsDTO.getAttachments() ) );
        bugs.setId( bugsDTO.getId() );
        bugs.setDescription( bugsDTO.getDescription() );
        bugs.setCode( bugsDTO.getCode() );
        bugs.setIteration( bugsDTO.getIteration() );
        bugs.setStage( bugsDTO.getStage() );
        bugs.setStatus( bugsDTO.getStatus() );
        bugs.setResolution( bugsDTO.getResolution() );
        bugs.setPhysicalPath( bugsDTO.getPhysicalPath() );
        bugs.setNotes( notesDTOSetToNotesSet( bugsDTO.getNotes() ) );

        return bugs;
    }

    private String bugsTasksName(Bugs bugs) {
        if ( bugs == null ) {
            return null;
        }
        Tasks tasks = bugs.getTasks();
        if ( tasks == null ) {
            return null;
        }
        String name = tasks.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }

    protected Set<NotesDTO> notesSetToNotesDTOSet(Set<Notes> set) {
        if ( set == null ) {
            return null;
        }

        Set<NotesDTO> set1 = new HashSet<NotesDTO>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( Notes notes : set ) {
            set1.add( notesMapper.toDto( notes ) );
        }

        return set1;
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

    private Long bugsTasksId(Bugs bugs) {
        if ( bugs == null ) {
            return null;
        }
        Tasks tasks = bugs.getTasks();
        if ( tasks == null ) {
            return null;
        }
        Long id = tasks.getId();
        if ( id == null ) {
            return null;
        }
        return id;
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

    protected Set<Notes> notesDTOSetToNotesSet(Set<NotesDTO> set) {
        if ( set == null ) {
            return null;
        }

        Set<Notes> set1 = new HashSet<Notes>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( NotesDTO notesDTO : set ) {
            set1.add( notesMapper.toEntity( notesDTO ) );
        }

        return set1;
    }
}
