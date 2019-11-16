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
import fpt.dps.dtms.security.SecurityUtils;
import fpt.dps.dtms.service.dto.AttachmentsDTO;
import fpt.dps.dtms.service.dto.TmsPostDTO;
import fpt.dps.dtms.service.dto.TmsThreadDTO;
import fpt.dps.dtms.service.mapper.AttachmentsMapper;
import fpt.dps.dtms.service.mapper.TmsPostMapper;

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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing TmsPost.
 */
@Service
@Transactional
public class TmsPostService {

    private final Logger log = LoggerFactory.getLogger(TmsPostService.class);

    private final TmsPostRepository tmsPostRepository;

    private final TmsPostMapper tmsPostMapper;

    private final TmsPostSearchRepository tmsPostSearchRepository;
    
    private final TmsThreadRepository tmsThreadRepository;
    
    private final TmsThreadSearchRepository threadSearchRepository;
    
    private final AttachmentsRepository attachmentsRepository;
    
    private final AttachmentsSearchRepository attachmentsSearchRepository;
    
    private final AttachmentsMapper attachmentsMapper;
    
    private final StorageService storageService;

    public TmsPostService(TmsPostRepository tmsPostRepository, TmsPostMapper tmsPostMapper, TmsPostSearchRepository tmsPostSearchRepository,
    		TmsThreadRepository tmsThreadRepository, AttachmentsRepository attachmentsRepository, AttachmentsSearchRepository attachmentsSearchRepository,
    		AttachmentsMapper attachmentsMapper, StorageService storageService, TmsThreadSearchRepository threadSearchRepository) {
        this.tmsPostRepository = tmsPostRepository;
        this.tmsPostMapper = tmsPostMapper;
        this.tmsPostSearchRepository = tmsPostSearchRepository;
        this.tmsThreadRepository = tmsThreadRepository;
        this.attachmentsRepository = attachmentsRepository;
        this.attachmentsSearchRepository = attachmentsSearchRepository;
        this.attachmentsMapper = attachmentsMapper;
        this.storageService = storageService;
        this.threadSearchRepository = threadSearchRepository;
    }

    /**
     * Save a tmsPost.
     *
     * @param tmsPostDTO the entity to save
     * @return the persisted entity
     */
    public TmsPostDTO save(TmsPostDTO tmsPostDTO) {
        log.debug("Request to save TmsPost : {}", tmsPostDTO);
        TmsPost tmsPost = tmsPostMapper.toEntity(tmsPostDTO);
        tmsPost = tmsPostRepository.save(tmsPost);
        TmsPostDTO result = tmsPostMapper.toDto(tmsPost);
        tmsPostSearchRepository.save(tmsPost);
        return result;
    }

    /**
     * Get all the tmsPosts.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<TmsPostDTO> findAll() {
        log.debug("Request to get all TmsPosts");
        return tmsPostRepository.findAll().stream()
            .map(tmsPostMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one tmsPost by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public TmsPostDTO findOne(Long id) {
        log.debug("Request to get TmsPost : {}", id);
        TmsPost tmsPost = tmsPostRepository.findOne(id);
        return tmsPostMapper.toDto(tmsPost);
    }

    /**
     * Delete the tmsPost by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete TmsPost : {}", id);
        tmsPostRepository.delete(id);
        tmsPostSearchRepository.delete(id);
    }

    /**
     * Search for the tmsPost corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<TmsPostDTO> search(String query) {
        log.debug("Request to search TmsPosts for query {}", query);
        return StreamSupport
            .stream(tmsPostSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(tmsPostMapper::toDto)
            .collect(Collectors.toList());
    }

	/**
	 * @param payload
	 * @return
	 * @throws IOException
	 */
	public TmsPostDTO createPosts(TmsPostDTO payload) throws IOException {
		TmsPost post = new TmsPost();
		TmsThread thread = this.tmsThreadRepository.findOne(payload.getThreadId());
		thread.setAnswers(thread.getAnswers() + 1);
		thread = this.tmsThreadRepository.save(thread);
		this.threadSearchRepository.save(thread);
		post.setContent(payload.getContent());
		post.setComments(0);
		post.setThread(thread);
		post = this.tmsPostRepository.save(post);
		this.tmsPostSearchRepository.save(post);
		
		Set<AttachmentsDTO> attachmentsDTOs = payload.getAttachments();
		if (!CollectionUtils.isEmpty(attachmentsDTOs)) {
			Set<Attachments> attachments = new HashSet<>();
			Attachments attachment;
			for (AttachmentsDTO attachmentsDTO : attachmentsDTOs) {
				attachment = this.processAttachments(attachmentsDTO, post);
				attachment = this.attachmentsRepository.save(attachment);
				this.attachmentsSearchRepository.save(attachment);
				attachments.add(attachment);
			}
			post.setAttachments(attachments);
		}
		return this.tmsPostMapper.toDto(post);
	}

	/**
	 * @param payload
	 * @return
	 * @throws IOException
	 */
	public TmsPostDTO updatePosts(TmsPostDTO payload) throws IOException {
		TmsPost post = this.tmsPostRepository.findOne(payload.getId());
		post.setContent(payload.getContent());
		
		Set<Attachments> attachments = post.getAttachments();
		Set<AttachmentsDTO> attachmentsAppend = payload.getAttachmentsAppend();
		Attachments attachment;
		if (!CollectionUtils.isEmpty(attachmentsAppend)) {
			for (AttachmentsDTO attachmentsDTO : attachmentsAppend) {
				attachment = this.processAttachments(attachmentsDTO, post);
				this.attachmentsRepository.save(attachment);
				this.attachmentsSearchRepository.save(attachment);
				attachments.add(attachment);
			}
		}
		
		Set<AttachmentsDTO> attachmentsRemove = payload.getAttachmentsRemove();
		if (!CollectionUtils.isEmpty(attachmentsRemove)) {
			for (AttachmentsDTO attachmentsDTO : attachmentsRemove) {
				attachment = this.attachmentsRepository.findOne(attachmentsDTO.getId());
				this.storageService.deleteFile(attachmentsDTO.getDiskFile());
				this.attachmentsRepository.delete(attachmentsDTO.getId());
				this.attachmentsSearchRepository.delete(attachmentsDTO.getId());
				attachments.remove(attachment);
			}
		}
		
		post.setAttachments(attachments);
		post = this.tmsPostRepository.save(post);
		this.tmsPostSearchRepository.save(post);
		return this.tmsPostMapper.toDto(post);
	}
	
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
	 * @param threadId
	 * @param pageable
	 * @return
	 */
	public Page<TmsPostDTO> findAnswer(Long threadId, Pageable pageable, Long exceptId) {
		Page<TmsPost> result = this.tmsPostRepository.findAnswer(threadId, exceptId, pageable);
		return result.map(tmsPostMapper::toDto);
	}
}
