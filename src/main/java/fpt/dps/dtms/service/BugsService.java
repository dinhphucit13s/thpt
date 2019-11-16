package fpt.dps.dtms.service;

import fpt.dps.dtms.domain.Attachments;
import fpt.dps.dtms.domain.BugListDefault;
import fpt.dps.dtms.domain.Bugs;
import fpt.dps.dtms.domain.Notes;
import fpt.dps.dtms.domain.ProjectBugListDefault;
import fpt.dps.dtms.domain.Projects;
import fpt.dps.dtms.domain.Tasks;
import fpt.dps.dtms.domain.enumeration.BugResolution;
import fpt.dps.dtms.domain.enumeration.BugStatus;
import fpt.dps.dtms.repository.AttachmentsRepository;
import fpt.dps.dtms.repository.BugListDefaultRepository;
import fpt.dps.dtms.repository.BugsRepository;
import fpt.dps.dtms.repository.ProjectsRepository;
import fpt.dps.dtms.repository.TasksRepository;
import fpt.dps.dtms.repository.search.AttachmentsSearchRepository;
import fpt.dps.dtms.repository.search.BugsSearchRepository;
import fpt.dps.dtms.service.dto.AttachmentsDTO;
import fpt.dps.dtms.service.dto.BugListDefaultDTO;
import fpt.dps.dtms.service.dto.BugsDTO;
import fpt.dps.dtms.service.dto.NotesDTO;
import fpt.dps.dtms.service.dto.ProjectBugListDefaultDTO;
import fpt.dps.dtms.service.mapper.AttachmentsMapper;
import fpt.dps.dtms.service.mapper.BugListDefaultMapper;
import fpt.dps.dtms.service.mapper.BugsMapper;
import fpt.dps.dtms.service.mapper.ProjectBugListDefaultMapper;
import fpt.dps.dtms.service.mapper.ProjectsMapper;
import fpt.dps.dtms.web.rest.errors.BadRequestAlertException;
import fpt.dps.dtms.web.rest.vm.external.BugAttachmentVM;
import fpt.dps.dtms.web.rest.vm.external.LogBugVM;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import static org.elasticsearch.index.query.QueryBuilders.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Service Implementation for managing Bugs.
 */
@Service
@Transactional
public class BugsService {

    private final Logger log = LoggerFactory.getLogger(BugsService.class);

    private final BugsRepository bugsRepository;

    private final BugsMapper bugsMapper;

    private final BugsSearchRepository bugsSearchRepository;
    
    private final TasksRepository tasksRepository;
    
    private final BugListDefaultRepository bugListDefaultRepository;
    
    private final BugListDefaultMapper bugListDefaultMapper;
    
    private final ProjectBugListDefaultMapper projectBugListDefaultMapper;
    
    private final AttachmentsRepository attachmentsRepository;
    
    private final AttachmentsSearchRepository attachmentsSearchRepository;
    
    private final AttachmentsMapper attachmentsMapper;
    
    private final StorageService storageService; 
    
    private final NotesService notesService;

    public BugsService(BugsRepository bugsRepository, BugsMapper bugsMapper, BugsSearchRepository bugsSearchRepository, TasksRepository tasksRepository,
    		BugListDefaultRepository bugListDefaultRepository, BugListDefaultMapper bugListDefaultMapper, ProjectBugListDefaultMapper projectBugListDefaultMapper,
    		AttachmentsRepository attachmentsRepository, AttachmentsSearchRepository attachmentsSearchRepository,
    		StorageService storageService, AttachmentsMapper attachmentsMapper, NotesService notesService) {
    	this.attachmentsMapper = attachmentsMapper;
    	this.attachmentsSearchRepository = attachmentsSearchRepository;
        this.bugsRepository = bugsRepository;
        this.tasksRepository = tasksRepository;
        this.bugsMapper = bugsMapper;
        this.bugListDefaultRepository = bugListDefaultRepository;
        this.bugListDefaultMapper = bugListDefaultMapper;
        this.projectBugListDefaultMapper = projectBugListDefaultMapper;
        this.bugsSearchRepository = bugsSearchRepository;
        this.attachmentsRepository = attachmentsRepository;
        this.storageService = storageService;
        this.notesService = notesService;
    }

    /**
     * Save a bugs.
     *
     * @param bugsDTO the entity to save
     * @return the persisted entity
     */
    public BugsDTO save(BugsDTO bugsDTO) {
        log.debug("Request to save Bugs : {}", bugsDTO);
        Bugs bugs = bugsMapper.toEntity(bugsDTO);
        bugs = bugsRepository.save(bugs);
        BugsDTO result = bugsMapper.toDto(bugs);
        bugsSearchRepository.save(bugs);
        return result;
    }

    /**
     * Get all the bugs.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<BugsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Bugs");
        Page<Bugs> bugs = bugsRepository.findAll(pageable); 
        return bugs.map(bugsMapper::toDto);
    }

    /**
     * Get one bugs by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public BugsDTO findOne(Long id) {
        log.debug("Request to get Bugs : {}", id);
        Bugs bugs = bugsRepository.findOne(id);
        return bugsMapper.toDto(bugs);
    }

    /**
     * Delete the bugs by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Bugs : {}", id);
        Bugs bugs = this.bugsRepository.findOne(id);
        if (bugs != null) {
        	String[] foldersBugs = {"bugs", bugs.getId().toString()};
    	    Path folderPathBugs = storageService.init(foldersBugs);
    	    this.storageService.deleteAll(folderPathBugs);
    	    Set<Notes> notesList = bugs.getNotes();
    	    if (!CollectionUtils.isEmpty(notesList)) {
    	    	for (Notes notes : notesList) {
    	    		this.deleteAttachmentNotes(notes);
				}
    	    }
            this.bugsRepository.delete(id);
            this.bugsSearchRepository.delete(id);
        } else {
        	throw new BadRequestAlertException("A bugs is not exist in database", "bugs", "unexist");
        }
    }
    
    private void deleteAttachmentNotes(Notes note) {
    	String[] folders = {"notes", note.getId().toString()};
	    Path folderPath = storageService.init(folders);
	    this.storageService.deleteAll(folderPath);
    }

    /**
     * Search for the bugs corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<BugsDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Bugs for query {}", query);
        Page<Bugs> result = bugsSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(bugsMapper::toDto);
    }
    
    Attachments processAttachments(AttachmentsDTO attachmentsDTO, Bugs bug) throws IOException {
    	Attachments attachment = new Attachments();
    	byte[] imageByte= Base64.decodeBase64(attachmentsDTO.getValue());
    	if (imageByte != null) {
    		Date now = new Date();
    	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
    	    	
    	    String[] folders = {"bugs", bug.getId().toString()};
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
    	    attachment.setBugs(bug);
    	}
	    return attachment;
    }

	public List<ProjectBugListDefaultDTO> getBugListDefaultByProject(Long projectId, Long tasksId, String stage) {
		
			List<ProjectBugListDefault> projectBugListDefault = new ArrayList<>();
			projectBugListDefault = this.bugListDefaultRepository.getBugListDefaultByProject(projectId, tasksId, stage);
			
			return projectBugListDefaultMapper.toDto(projectBugListDefault);
	}

	public List<BugsDTO> createBugsOfTask(LogBugVM logBug) throws IOException {
		Tasks taskDomain = this.tasksRepository.findOne(logBug.getTaskId());
		List<BugsDTO> bugDtoList = new ArrayList<>();
		if (taskDomain != null) {
			Set<BugAttachmentVM> bugAttachmentVMs = logBug.getBugAttachments();
			if (!CollectionUtils.isEmpty(bugAttachmentVMs)) {
				Set<AttachmentsDTO> attachmentDtos;
				Set<NotesDTO>notesDtos; 
				Attachments attachmentsDomain;
				Bugs bugsDomain;
				BugsDTO bugDto;
				for (BugAttachmentVM bugAttachmentVM : bugAttachmentVMs) {
					bugsDomain = new Bugs();
					bugDto = new BugsDTO();
					bugsDomain.setTasks(taskDomain);
					bugsDomain.setDescription(bugAttachmentVM.getDescription());
					bugsDomain.setCode(bugAttachmentVM.getCode());
					bugsDomain.setStage(bugAttachmentVM.getStage());
					bugsDomain.setIteration(bugAttachmentVM.getIteration());
					bugsDomain.setStatus(bugAttachmentVM.getStatus());
					bugsDomain.setResolution(bugAttachmentVM.getResolution());
					bugsDomain.setPhysicalPath(bugAttachmentVM.getPhysicalPath());
					bugsDomain = this.bugsRepository.save(bugsDomain);
					this.bugsSearchRepository.save(bugsDomain);
					bugDto = bugsMapper.toDto(bugsDomain);
					bugDtoList.add(bugDto);
					attachmentDtos = bugAttachmentVM.getAttachments();
					if (!CollectionUtils.isEmpty(attachmentDtos)) {
						for (AttachmentsDTO attachmentDto : attachmentDtos) {
							attachmentsDomain = new Attachments();
							attachmentsDomain = this.processAttachments(attachmentDto, bugsDomain);
							this.attachmentsRepository.save(attachmentsDomain);
							this.attachmentsSearchRepository.save(attachmentsDomain);
						}
					}
					notesDtos = bugAttachmentVM.getNotes();
					if (!CollectionUtils.isEmpty(notesDtos)) {
						for (NotesDTO notesDto: notesDtos) {
							notesDto.setBugId(bugsDomain.getId());
							this.notesService.createBugNotes(notesDto);
						}
					}
				}
			}
		} else {
			throw new BadRequestAlertException("A task cannot exist", "tasks", "unexist");
		}
		return bugDtoList;
	}

	public List<AttachmentsDTO> getAttachmentByBugId(Long bugId) {
		List<Attachments> attachmentListDomain = this.attachmentsRepository.findByParentId(bugId, "BUG");
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

	public List<BugsDTO> updateBugsOpen(List<BugsDTO> bugs) throws IOException {
		Bugs bugsDomain;
		List<BugsDTO> bugsDtoList = new ArrayList<>();
		Set<Attachments> attachmentsDomainSet;
		Set<AttachmentsDTO> attachmentsAppend;
		Set<AttachmentsDTO> attachmentsRemove;
		Attachments attachmentsDomain;
		for (BugsDTO bugsDTO : bugs) {
			bugsDomain = this.bugsRepository.findOne(bugsDTO.getId());
			if(bugsDomain != null) {
				bugsDomain.setDescription(bugsDTO.getDescription());
				bugsDomain.setPhysicalPath(bugsDTO.getPhysicalPath());
				bugsDomain.setStatus(bugsDTO.getStatus());
				bugsDomain.setResolution(bugsDTO.getResolution());
				attachmentsDomainSet = bugsDomain.getAttachments();
				
				attachmentsAppend = bugsDTO.getAttachmentsAppend();
				if (!CollectionUtils.isEmpty(attachmentsAppend)) {
					for (AttachmentsDTO attachmentsDTO : attachmentsAppend) {
						attachmentsDomain = new Attachments();
						attachmentsDomain = this.processAttachments(attachmentsDTO, bugsDomain);
						this.attachmentsRepository.save(attachmentsDomain);
						this.attachmentsSearchRepository.save(attachmentsDomain);
						attachmentsDomainSet.add(attachmentsDomain);
					}
				}
				
				attachmentsRemove = bugsDTO.getAttachmentsRemove();
				if (!CollectionUtils.isEmpty(attachmentsRemove)) {
					for (AttachmentsDTO attachmentsDTO : attachmentsRemove) {
						attachmentsDomain = this.attachmentsRepository.findOne(attachmentsDTO.getId());
						this.storageService.deleteFile(attachmentsDTO.getDiskFile());
						this.attachmentsRepository.delete(attachmentsDTO.getId());
						this.attachmentsSearchRepository.delete(attachmentsDTO.getId());
						attachmentsDomainSet.remove(attachmentsDomain);
					}
				}
				bugsDomain.setAttachments(attachmentsDomainSet);
				bugsDomain = this.bugsRepository.save(bugsDomain);
				this.bugsSearchRepository.save(bugsDomain);
				bugsDtoList.add(this.bugsMapper.toDto(bugsDomain));
			}
		}
		return bugsDtoList;
	}
}
