package fpt.dps.dtms.service;

import fpt.dps.dtms.domain.Attachments;
import fpt.dps.dtms.domain.Issues;
import fpt.dps.dtms.domain.TmsPost;
import fpt.dps.dtms.domain.TmsThread;
import fpt.dps.dtms.repository.AttachmentsRepository;
import fpt.dps.dtms.repository.TmsPostRepository;
import fpt.dps.dtms.repository.TmsThreadRepository;
import fpt.dps.dtms.repository.search.AttachmentsSearchRepository;
import fpt.dps.dtms.repository.search.TmsPostSearchRepository;
import fpt.dps.dtms.repository.search.TmsThreadSearchRepository;
import fpt.dps.dtms.service.dto.AttachmentsDTO;
import fpt.dps.dtms.service.dto.TmsPostDTO;
import fpt.dps.dtms.service.dto.TmsThreadDTO;
import fpt.dps.dtms.service.mapper.TmsPostMapper;
import fpt.dps.dtms.service.mapper.TmsThreadMapper;
import fpt.dps.dtms.web.rest.errors.BadRequestAlertException;
import fpt.dps.dtms.web.rest.vm.external.QuestionAndAnswerVM;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections4.CollectionUtils;

/**
 * Service Implementation for managing TmsThread.
 */
@Service
@Transactional
public class TmsThreadService {

    private final Logger log = LoggerFactory.getLogger(TmsThreadService.class);

    private final TmsThreadRepository tmsThreadRepository;

    private final TmsThreadMapper tmsThreadMapper;
    
    private final TmsPostMapper tmsPostMapper;

    private final TmsThreadSearchRepository tmsThreadSearchRepository;
    
    private final StorageService storageService;
    
    private final AttachmentsRepository attachmentsRepository;
    
    private final AttachmentsSearchRepository attachmentsSearchRepository;
    
    private final TmsPostRepository tmsPostRepository;
    
    private final TmsPostSearchRepository tmsPostSearchRepository;

    public TmsThreadService(TmsThreadRepository tmsThreadRepository, StorageService storageService,
    		TmsThreadMapper tmsThreadMapper, TmsThreadSearchRepository tmsThreadSearchRepository,
    		AttachmentsRepository attachmentsRepository, TmsPostRepository tmsPostRepository, TmsPostMapper tmsPostMapper,
    		AttachmentsSearchRepository attachmentsSearchRepository, TmsPostSearchRepository tmsPostSearchRepository) {
        this.tmsThreadRepository = tmsThreadRepository;
        this.tmsThreadMapper = tmsThreadMapper;
        this.tmsThreadSearchRepository = tmsThreadSearchRepository;
        this.storageService = storageService;
        this.attachmentsRepository = attachmentsRepository;
        this.attachmentsSearchRepository = attachmentsSearchRepository;
        this.tmsPostRepository = tmsPostRepository;
        this.tmsPostSearchRepository = tmsPostSearchRepository;
        this.tmsPostMapper = tmsPostMapper;
    }

    /**
     * Save a tmsThread.
     *
     * @param tmsThreadDTO the entity to save
     * @return the persisted entity
     */
    public TmsThreadDTO save(TmsThreadDTO tmsThreadDTO) {
        log.debug("Request to save TmsThread : {}", tmsThreadDTO);
        TmsThread tmsThread = tmsThreadMapper.toEntity(tmsThreadDTO);
        tmsThread = tmsThreadRepository.save(tmsThread);
        TmsThreadDTO result = tmsThreadMapper.toDto(tmsThread);
        tmsThreadSearchRepository.save(tmsThread);
        return result;
    }

    /**
     * Get all the tmsThreads.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<TmsThreadDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TmsThreads");
        return tmsThreadRepository.findAll(pageable)
            .map(tmsThreadMapper::toDto);
    }

    /**
     * Get one tmsThread by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public TmsThreadDTO findOne(Long id) {
        log.debug("Request to get TmsThread : {}", id);
        TmsThread tmsThread = tmsThreadRepository.findOne(id);
        TmsThreadDTO threadDTO = tmsThreadMapper.toDto(tmsThread);
        
        // Get first post for main topic
        TmsPost tmsPost = tmsThread.getPosts().iterator().next();
        TmsPostDTO tmsPostDTO = this.tmsPostMapper.toDto(tmsPost);
        Set<TmsPostDTO> tmsPostDTOs = new HashSet<>();
        tmsPostDTOs.add(tmsPostDTO);
        threadDTO.setPosts(tmsPostDTOs);
        
        return threadDTO;
    }

    /**
     * Delete the tmsThread by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete TmsThread : {}", id);
        tmsThreadRepository.delete(id);
        tmsThreadSearchRepository.delete(id);
    }

    /**
     * Search for the tmsThread corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<TmsThreadDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of TmsThreads for query {}", query);
        Page<TmsThread> result = tmsThreadSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(tmsThreadMapper::toDto);
    }

	/**
	 * @param payload
	 * @return
	 * @throws IOException
	 */
	public TmsThreadDTO createQuestionAndAnswer(QuestionAndAnswerVM payload) throws IOException {
		log.debug("Request payload to create thread Q&A", payload);
		boolean status = payload.getStatus().equals("PUBLIC") ? true : false;
		TmsThreadDTO tmsThreadDto = new TmsThreadDTO();
		tmsThreadDto.setClosed(false);
		tmsThreadDto.setTitle(payload.getTitle());
		tmsThreadDto.setStatus(status);
		tmsThreadDto.setProjectsId(payload.getProjectsId());
		tmsThreadDto.setViews(0);
		tmsThreadDto.setAssigneeId(payload.getAssigneeId());
		tmsThreadDto.setAnswers(payload.getPosts().size());
		TmsThread thread = this.tmsThreadRepository
				.save(this.tmsThreadMapper.toEntity(tmsThreadDto));
		
		TmsPost post = new TmsPost();
		post.setContent(payload.getPosts().iterator().next().getContent());
		post.setComments(0);
		post.setThread(thread);
		post = this.tmsPostRepository.save(post);
		this.tmsPostSearchRepository.save(post);
		
		Set<AttachmentsDTO> attachmentsDtoList = payload.getPosts().iterator().next().getAttachments();
		
		if (!CollectionUtils.isEmpty(attachmentsDtoList)) {
			Attachments attachment;
			Set<Attachments> attachmentsList = new HashSet<>();
			for (AttachmentsDTO attachmentsDTO : attachmentsDtoList) {
				attachment = this.processAttachments(attachmentsDTO, post);
				attachment = this.attachmentsRepository.save(attachment);
				this.attachmentsSearchRepository.save(attachment);
				attachmentsList.add(attachment);
			}
		}
		
		return this.tmsThreadMapper.toDto(thread);
	}
	
    /**
     * @param attachmentsDTO
     * @param post
     * @return
     * @throws IOException
     */
    private Attachments processAttachments(AttachmentsDTO attachmentsDTO, TmsPost post) throws IOException {
    	Attachments attachment = new Attachments();
    	byte[] imageByte= Base64.decodeBase64(attachmentsDTO.getValue());
    	if (imageByte != null) {
    		Date now = new Date();
    	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
    	    	
    	    String[] folders = {"posts", post.getId().toString()};
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
    	    attachment.setPost(post);
    	}
	    return attachment;
    }

	/**
	 * Update view Of thread
	 * 
	 * @param threadId
	 */
	public void updateViewsOfThread(Long threadId) {
		TmsThread thread = this.tmsThreadRepository.findOne(threadId);
		if (thread != null) {
			Integer views = thread.getViews();
			thread.setViews(views + 1);
			this.tmsThreadRepository.save(thread);
			this.tmsThreadSearchRepository.save(thread);
		} else {
			throw new BadRequestAlertException("A thread is not exist in database", "tmsThread", "not-exist");
		}
	}

	/**
	 * @param threadId
	 */
	public void updateThreadToClose(Long threadId) {
		TmsThread thread = this.tmsThreadRepository.findOne(threadId);
		if (thread != null) {
			thread.setClosed(true);
			thread = this.tmsThreadRepository.save(thread);
			this.tmsThreadSearchRepository.save(thread);
		} else {
			throw new BadRequestAlertException("A thread is not exist in database", "tmsThread", "not-exist");
		}
	}

	public TmsThreadDTO updateTitleOfThread(Long id, String title) {
		TmsThread thread = this.tmsThreadRepository.findOne(id);
		thread.setTitle(title);;
		thread = this.tmsThreadRepository.save(thread);
		this.tmsThreadSearchRepository.save(thread);
		return this.tmsThreadMapper.toDto(thread);
	}
}
