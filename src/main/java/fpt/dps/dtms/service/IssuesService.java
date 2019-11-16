package fpt.dps.dtms.service;

import fpt.dps.dtms.domain.Attachments;
import fpt.dps.dtms.domain.Bugs;
import fpt.dps.dtms.domain.Issues;
import fpt.dps.dtms.domain.Mail;
import fpt.dps.dtms.domain.Projects;
import fpt.dps.dtms.domain.enumeration.IssueStatus;
import fpt.dps.dtms.repository.AttachmentsRepository;
import fpt.dps.dtms.repository.IssuesRepository;
import fpt.dps.dtms.repository.ProjectsRepository;
import fpt.dps.dtms.repository.search.AttachmentsSearchRepository;
import fpt.dps.dtms.repository.search.IssuesSearchRepository;
import fpt.dps.dtms.service.dto.AttachmentsDTO;
import fpt.dps.dtms.service.dto.IssuesDTO;
import fpt.dps.dtms.service.dto.MailDTO;
import fpt.dps.dtms.service.mapper.AttachmentsMapper;
import fpt.dps.dtms.service.mapper.IssuesMapper;
import fpt.dps.dtms.web.rest.errors.BadRequestAlertException;

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
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

/**
 * Service Implementation for managing Issues.
 */
@Service
@Transactional
public class IssuesService {

    private final Logger log = LoggerFactory.getLogger(IssuesService.class);

    private final IssuesRepository issuesRepository;

    private final IssuesMapper issuesMapper;

    private final IssuesSearchRepository issuesSearchRepository;
    
    private final StorageService storageService;
    
    private final AttachmentsRepository attachmentsRepository;
    
    private final AttachmentsSearchRepository attachmentsSearchRepository;
    
    private final AttachmentsMapper attachmentsMapper;
    
    private final ProjectsRepository projectsRepository;

    public IssuesService(IssuesRepository issuesRepository, IssuesMapper issuesMapper,
    		IssuesSearchRepository issuesSearchRepository, StorageService storageService,
    		AttachmentsRepository attachmentsRepository, AttachmentsSearchRepository attachmentsSearchRepository,
    		ProjectsRepository projectsRepository, AttachmentsMapper attachmentsMapper) {
        this.issuesRepository = issuesRepository;
        this.issuesMapper = issuesMapper;
        this.issuesSearchRepository = issuesSearchRepository;
        this.storageService = storageService;
        this.attachmentsRepository = attachmentsRepository;
        this.attachmentsSearchRepository = attachmentsSearchRepository;
        this.projectsRepository = projectsRepository;
        this.attachmentsMapper = attachmentsMapper;
    }

    /**
     * Save a issues.
     *
     * @param issuesDTO the entity to save
     * @return the persisted entity
     */
    public IssuesDTO save(IssuesDTO issuesDTO) {
        log.debug("Request to save Issues : {}", issuesDTO);
        Issues issues = issuesMapper.toEntity(issuesDTO);
        issues = issuesRepository.save(issues);
        IssuesDTO result = issuesMapper.toDto(issues);
        issuesSearchRepository.save(issues);
        return result;
    }

    /**
     * Get all the issues.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<IssuesDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Issues");
        return issuesRepository.findAll(pageable)
            .map(issuesMapper::toDto);
    }

    /**
     * Get one issues by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public IssuesDTO findOne(Long id) {
        log.debug("Request to get Issues : {}", id);
        Issues issues = issuesRepository.findOne(id);
        return issuesMapper.toDto(issues);
    }

    /**
     * Delete the issues by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Issues : {}", id);
        Issues issue = this.issuesRepository.findOne(id);
        if (issue != null) {
        	String[] folders = {"issues", issue.getId().toString()};
    	    Path folderPath = storageService.init(folders);
    	    this.storageService.deleteAll(folderPath);
    	    issuesRepository.delete(id);
            issuesSearchRepository.delete(id);
        } else {
        	throw new BadRequestAlertException("A issues is not exist in database", "issues", "unexist");
        }
    }

    /**
     * Search for the issues corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<IssuesDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Issues for query {}", query);
        Page<Issues> result = issuesSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(issuesMapper::toDto);
    }

	/**
	 * Create issue of project
	 * 
	 * @param IssuesDTO
	 * @return IssuesDTO
	 * @throws IOException
	 */
	public IssuesDTO createIssues(IssuesDTO payload) throws IOException {
		Issues issues;
		Set<AttachmentsDTO> attachmentDtos;
		Set<Attachments> attachments;
		Set<AttachmentsDTO> attachmentsAppend;
		Set<AttachmentsDTO> attachmentsRemove;
		Attachments attachment;
		
		if (payload.getId() == null) {
			issues = new Issues();
			Projects project = this.projectsRepository.findOne(payload.getProjectsId());
			issues.setName(payload.getName());
			issues.setDescription(payload.getDescription());
			issues.setStatus(IssueStatus.NA);
			issues.setProjects(project);
			issues = this.issuesRepository.save(issues);
			this.issuesSearchRepository.save(issues);
			attachmentDtos = payload.getAttachments();
			
			if(!CollectionUtils.isEmpty(attachmentDtos)) {
				attachments = new HashSet<>();
				for (AttachmentsDTO attachmentDto : attachmentDtos) {
					attachment = this.processAttachments(attachmentDto, issues);
					attachment = this.attachmentsRepository.save(attachment);
					this.attachmentsSearchRepository.save(attachment);
					attachments.add(attachment);
				}
				issues.setAttachments(attachments);
			}
		} else {
			issues = this.issuesRepository.findOne(payload.getId());
			issues.setDescription(payload.getDescription());
			issues.setName(payload.getName());
			issues.setStatus(payload.getStatus());
			attachments = issues.getAttachments();
			
			attachmentsAppend = payload.getAttachmentsAppend();
			if (!CollectionUtils.isEmpty(attachmentsAppend)) {
				for (AttachmentsDTO attachmentsDTO : attachmentsAppend) {
					attachment = this.processAttachments(attachmentsDTO, issues);
					this.attachmentsRepository.save(attachment);
					this.attachmentsSearchRepository.save(attachment);
					attachments.add(attachment);
				}
			}
			
			attachmentsRemove = payload.getAttachmentsRemove();
			if (!CollectionUtils.isEmpty(attachmentsRemove)) {
				for (AttachmentsDTO attachmentsDTO : attachmentsRemove) {
					attachment = this.attachmentsRepository.findOne(attachmentsDTO.getId());
					this.storageService.deleteFile(attachmentsDTO.getDiskFile());
					this.attachmentsRepository.delete(attachmentsDTO.getId());
					this.attachmentsSearchRepository.delete(attachmentsDTO.getId());
					attachments.remove(attachment);
				}
			}
			issues.setAttachments(attachments);
			issues = this.issuesRepository.save(issues);
			this.issuesSearchRepository.save(issues);
		}
		return issuesMapper.toDto(issues);
	}
	
    private Attachments processAttachments(AttachmentsDTO attachmentsDTO, Issues issues) throws IOException {
    	Attachments attachment = new Attachments();
    	byte[] imageByte= Base64.decodeBase64(attachmentsDTO.getValue());
    	if (imageByte != null) {
    		Date now = new Date();
    	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
    	    	
    	    String[] folders = {"issues", issues.getId().toString()};
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
    	    attachment.setIssues(issues);
    	}
	    return attachment;
    }

	public List<AttachmentsDTO> getAttachmentByIssuesId(Long issuesId) {
		List<Attachments> attachmentListDomain = this.attachmentsRepository.findByParentId(issuesId, "ISSUES");
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
		
	/**
     * Create a feedback.
     *
     */
    public IssuesDTO createFeedback(IssuesDTO issuesDTO, List<MultipartFile> files) {
        log.debug("Request to save feedback : {}", issuesDTO);
        
        Projects project = this.projectsRepository.findOne(issuesDTO.getProjectsId());
        
        Issues issues = new Issues();
        issues.setName(issuesDTO.getName());
        issues.setDescription(issuesDTO.getDescription());
        issues.setStatus(IssueStatus.NA);
        issues.setProjects(project);
        issues = issuesRepository.save(issues);
        
        Set<Attachments> attachments = new HashSet<>();
        if(files.size() > 0) {
	        Attachments attachment;
	        String[] folders = {"issues", issues.getId().toString()};
	        for (MultipartFile file : files) {
	        	attachment = new Attachments();
	        	String diskFile = storageService.store(file, folders);
	        	attachment.setIssues(issues);
	        	attachment.setDiskFile(diskFile);
	        	attachment.setFilename(file.getOriginalFilename());
	        	attachment.setFileType(file.getContentType());
	        	attachmentsRepository.save(attachment);
	        	attachmentsSearchRepository.save(attachment);
	        	attachments.add(attachment);
			}
        }
        
        issues.setAttachments(attachments);
        issues = this.issuesRepository.save(issues);
        this.issuesSearchRepository.save(issues);
        IssuesDTO result = issuesMapper.toDto(issues);
        
        return result;
    }
}
