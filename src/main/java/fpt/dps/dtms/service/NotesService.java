package fpt.dps.dtms.service;

import fpt.dps.dtms.domain.Attachments;
import fpt.dps.dtms.domain.Bugs;
import fpt.dps.dtms.domain.Notes;
import fpt.dps.dtms.repository.AttachmentsRepository;
import fpt.dps.dtms.repository.BugsRepository;
import fpt.dps.dtms.repository.NotesRepository;
import fpt.dps.dtms.repository.search.AttachmentsSearchRepository;
import fpt.dps.dtms.repository.search.NotesSearchRepository;
import fpt.dps.dtms.service.dto.AttachmentsDTO;
import fpt.dps.dtms.service.dto.NotesDTO;
import fpt.dps.dtms.service.mapper.AttachmentsMapper;
import fpt.dps.dtms.service.mapper.NotesMapper;
import fpt.dps.dtms.web.rest.errors.BadRequestAlertException;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import static org.elasticsearch.index.query.QueryBuilders.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Service Implementation for managing Notes.
 */
@Service
@Transactional
public class NotesService {

    private final Logger log = LoggerFactory.getLogger(NotesService.class);

    private final NotesRepository notesRepository;

    private final NotesMapper notesMapper;

    private final NotesSearchRepository notesSearchRepository;
    
    private final AttachmentsRepository attachmentsRepository;
    
    private final BugsRepository bugsRepository;
    
    private final AttachmentsSearchRepository attachmentsSearchRepository;
    
    private final StorageService storageService;
    
    private final AttachmentsMapper attachmentsMapper;

    public NotesService(NotesRepository notesRepository, NotesMapper notesMapper, StorageService storageService,
    		NotesSearchRepository notesSearchRepository, AttachmentsRepository attachmentsRepository,
    		AttachmentsSearchRepository attachmentsSearchRepository, BugsRepository bugsRepository,
    		AttachmentsMapper attachmentsMapper) {
    	this.attachmentsMapper = attachmentsMapper;
    	this.bugsRepository = bugsRepository;
    	this.attachmentsSearchRepository = attachmentsSearchRepository;
    	this.storageService = storageService;
    	this.attachmentsRepository = attachmentsRepository;
        this.notesRepository = notesRepository;
        this.notesMapper = notesMapper;
        this.notesSearchRepository = notesSearchRepository;
    }

    /**
     * Save a notes.
     *
     * @param notesDTO the entity to save
     * @return the persisted entity
     */
    public NotesDTO save(NotesDTO notesDTO) {
        log.debug("Request to save Notes : {}", notesDTO);
        Notes notes = notesMapper.toEntity(notesDTO);
        notes = notesRepository.save(notes);
        NotesDTO result = notesMapper.toDto(notes);
        notesSearchRepository.save(notes);
        return result;
    }

    /**
     * Get all the notes.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<NotesDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Notes");
        return notesRepository.findAll(pageable)
            .map(notesMapper::toDto);
    }

    /**
     * Get one notes by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public NotesDTO findOne(Long id) {
        log.debug("Request to get Notes : {}", id);
        Notes notes = notesRepository.findOne(id);
        return notesMapper.toDto(notes);
    }

    /**
     * Delete the notes by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Notes : {}", id);
        
        Notes note = this.notesRepository.findOne(id);
        if (note != null) {
        	String[] folders = {"notes", note.getId().toString()};
    	    Path folderPath = storageService.init(folders);
    	    this.storageService.deleteAll(folderPath);
            Bugs bug = note.getBug();
    		Set<Notes> notes = bug.getNotes();
    		notes.remove(note);
    		bug.setNotes(notes);
    		this.bugsRepository.save(bug);
        } else {
        	throw new BadRequestAlertException("A notes is not exist in database", "notes", "unexist");
        }
        
    }

    /**
     * Search for the notes corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<NotesDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Notes for query {}", query);
        Page<Notes> result = notesSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(notesMapper::toDto);
    }

	public NotesDTO createBugNotes(NotesDTO notesDto) throws IOException {
		Notes note = this.notesMapper.toEntity(notesDto);
		note = this.notesRepository.save(note);
		Bugs bug = this.bugsRepository.findOne(notesDto.getBugId());
		Set<Notes> notes = bug.getNotes();
		notes.add(note);
		bug.setNotes(notes);
		this.bugsRepository.save(bug);
        NotesDTO result = this.notesMapper.toDto(note);
        this.notesRepository.save(notes);
        this.notesSearchRepository.save(notes);
        
		Set<AttachmentsDTO> attachments =  notesDto.getAttachments();
		if (!CollectionUtils.isEmpty(attachments)) {
			Attachments attachmentsDomain;
			for (AttachmentsDTO attachmentsDTO : attachments) {
				attachmentsDomain = new Attachments();
				attachmentsDomain = this.processAttachments(attachmentsDTO, note);
				this.attachmentsRepository.save(attachmentsDomain);
				this.attachmentsSearchRepository.save(attachmentsDomain);
			}
		}
		result.setAttachments(attachments);
		
		return result;
	}
	
    private Attachments processAttachments(AttachmentsDTO attachmentsDTO, Notes note) throws IOException {
    	Attachments attachment = new Attachments();
    	byte[] imageByte= Base64.decodeBase64(attachmentsDTO.getValue());
    	if (imageByte != null) {
    		Date now = new Date();
    	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
    	    	
    	    String[] folders = {"notes", note.getId().toString()};
    	    Path folderPath = storageService.init(folders);
    	    Path path = folderPath.resolve(dateFormat.format(now) + "_" + attachmentsDTO.getFilename());
    	    Files.write(path, imageByte);
    	    Resource resource = new UrlResource(path.toUri());
    	    String physicalPath = null;
    	    if(resource.exists() || resource.isReadable()) {
                physicalPath = path.toFile().getPath();
                if(physicalPath.contains("\\")) {
                	physicalPath = physicalPath.replaceFirst("filestorage\\\\", "");
                }
                else if(physicalPath.contains("/")) {
                	physicalPath = physicalPath.replaceFirst("filestorage/", "");
                }
            }

    	    attachment.setFilename(dateFormat.format(now) + "_" + attachmentsDTO.getFilename());
    	    attachment.setFileType(attachmentsDTO.getFileType());
    	    attachment.setDiskFile(physicalPath);
    	    attachment.setNotes(note);
    	}
	    return attachment;
    }


	public List<AttachmentsDTO> getAttachmentByNotesId(Long notesId) {
		List<Attachments> attachmentListDomain = this.attachmentsRepository.findByParentId(notesId, "NOTES");
		List<AttachmentsDTO> attachmentDtos = new ArrayList<>();
		if (!CollectionUtils.isEmpty(attachmentListDomain)) {
			AttachmentsDTO attachmentsDto;
			for (Attachments attachment : attachmentListDomain) {
				attachmentsDto = new AttachmentsDTO();
				attachmentsDto = attachmentsMapper.toDto(attachment);
				attachmentsDto.setValue(this.storageService.loadAttachment(attachment.getDiskFile()));
				attachmentDtos.add(attachmentsDto);
			}
		}
		return attachmentDtos;
	}

	public NotesDTO updateNotes(NotesDTO notesDto) throws IOException {
		Notes note = this.notesRepository.findOne(notesDto.getId());
		if (note != null) {
			Set<Attachments> attachmentSet = note.getAttachments();
			note = this.notesMapper.toEntity(notesDto);
			Attachments attachmentsDomain;
	        Set<AttachmentsDTO> attachmentsAppend = notesDto.getAttachmentsAppend();
			if (!CollectionUtils.isEmpty(attachmentsAppend)) {
				for (AttachmentsDTO attachmentsDTO : attachmentsAppend) {
					attachmentsDomain = new Attachments();
					attachmentsDomain = this.processAttachments(attachmentsDTO, note);
					this.attachmentsRepository.save(attachmentsDomain);
					this.attachmentsSearchRepository.save(attachmentsDomain);
					attachmentSet.add(attachmentsDomain);
				}
			}
			
			Set<AttachmentsDTO> attachmentsRemove = notesDto.getAttachmentsRemove();
			if (!CollectionUtils.isEmpty(attachmentsRemove)) {
				for (AttachmentsDTO attachmentsDTO : attachmentsRemove) {
					attachmentsDomain = this.attachmentsRepository.findOne(attachmentsDTO.getId());
					this.storageService.deleteFile(attachmentsDTO.getDiskFile());
					this.attachmentsRepository.delete(attachmentsDTO.getId());
					this.attachmentsSearchRepository.delete(attachmentsDTO.getId());
					attachmentSet.remove(attachmentsDomain);
				}
			}
			note.setAttachments(attachmentSet);
			note = this.notesRepository.save(note);
	        this.notesSearchRepository.save(note);
		}
		NotesDTO result = this.notesMapper.toDto(note);
		
		return result;
	}
}
