package fpt.dps.dtms.service;

import fpt.dps.dtms.domain.Attachments;
import fpt.dps.dtms.domain.Bugs;
import fpt.dps.dtms.domain.Comments;
import fpt.dps.dtms.domain.Notes;
import fpt.dps.dtms.domain.TmsPost;
import fpt.dps.dtms.repository.AttachmentsRepository;
import fpt.dps.dtms.repository.CommentsRepository;
import fpt.dps.dtms.repository.TmsPostRepository;
import fpt.dps.dtms.repository.search.AttachmentsSearchRepository;
import fpt.dps.dtms.repository.search.CommentsSearchRepository;
import fpt.dps.dtms.repository.search.TmsPostSearchRepository;
import fpt.dps.dtms.service.dto.AttachmentsDTO;
import fpt.dps.dtms.service.dto.CommentsDTO;
import fpt.dps.dtms.service.dto.NotesDTO;
import fpt.dps.dtms.service.mapper.AttachmentsMapper;
import fpt.dps.dtms.service.mapper.CommentsMapper;
import fpt.dps.dtms.web.rest.errors.BadRequestAlertException;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.security.access.prepost.PostAuthorize;
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
 * Service Implementation for managing Comments.
 */
@Service
@Transactional
public class CommentsService {

    private final Logger log = LoggerFactory.getLogger(CommentsService.class);

    private final CommentsRepository commentsRepository;

    private final CommentsMapper commentsMapper;

    private final CommentsSearchRepository commentsSearchRepository;
    
    private final AttachmentsSearchRepository attachmentsSearchRepository;
    
    private final StorageService storageService;
    
    private final AttachmentsMapper attachmentsMapper;
    
    private final AttachmentsRepository attachmentsRepository;
    
    private TmsPostRepository tmsPostRepository;
    
    private TmsPostSearchRepository tmsPostSearchRepository;

    public CommentsService(CommentsRepository commentsRepository, CommentsMapper commentsMapper, CommentsSearchRepository commentsSearchRepository,
    		AttachmentsSearchRepository attachmentsSearchRepository, StorageService storageService, AttachmentsMapper attachmentsMapper,
    		AttachmentsRepository attachmentsRepository, TmsPostRepository tmsPostRepository, TmsPostSearchRepository tmsPostSearchRepository) {
        this.commentsRepository = commentsRepository;
        this.commentsMapper = commentsMapper;
        this.commentsSearchRepository = commentsSearchRepository;
        this.attachmentsSearchRepository = attachmentsSearchRepository;
        this.attachmentsMapper = attachmentsMapper;
        this.attachmentsRepository = attachmentsRepository;
        this.storageService = storageService;
        this.tmsPostRepository = tmsPostRepository;
        this.tmsPostSearchRepository = tmsPostSearchRepository;
    }

    /**
     * Save a comments.
     *
     * @param commentsDTO the entity to save
     * @return the persisted entity
     */
    public CommentsDTO save(CommentsDTO commentsDTO) {
        log.debug("Request to save Comments : {}", commentsDTO);
        Comments comments = commentsMapper.toEntity(commentsDTO);
        comments = commentsRepository.save(comments);
        CommentsDTO result = commentsMapper.toDto(comments);
        commentsSearchRepository.save(comments);
        return result;
    }

    /**
     * Get all the comments.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<CommentsDTO> findAll() {
        log.debug("Request to get all Comments");
        return commentsRepository.findAll().stream()
            .map(commentsMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one comments by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public CommentsDTO findOne(Long id) {
        log.debug("Request to get Comments : {}", id);
        Comments comments = commentsRepository.findOne(id);
        return commentsMapper.toDto(comments);
    }

    /**
     * Delete the comments by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Comments : {}", id);
        Comments comments = this.commentsRepository.findOne(id);
        if (comments != null) {
        	String[] folders = {"comments", id.toString()};
    	    Path folderPath = storageService.init(folders);
    	    this.storageService.deleteAll(folderPath);
    	    TmsPost post = comments.getPost();
    	    
    	    this.commentsRepository.delete(id);
            this.commentsSearchRepository.delete(id);
            
            post.setComments(post.getComments() - 1);
            post = this.tmsPostRepository.save(post);
            this.tmsPostSearchRepository.save(post);
        } else {
        	throw new BadRequestAlertException("A comment is not exist in database", "notes", "unexist");
        }
        
    }

    /**
     * Search for the comments corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<CommentsDTO> search(String query) {
        log.debug("Request to search Comments for query {}", query);
        return StreamSupport
            .stream(commentsSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(commentsMapper::toDto)
            .collect(Collectors.toList());
    }

	public List<CommentsDTO> getCommentByPostId(Long postId) {
		List<Comments> comments = this.commentsRepository.getCommentByPostId(postId);
		return this.commentsMapper.toDto(comments);
	}

	public CommentsDTO createComments(CommentsDTO commentsDTO) throws IOException {
		Comments comments = this.commentsMapper.toEntity(commentsDTO);
		comments = this.commentsRepository.save(comments);
		this.commentsSearchRepository.save(comments);
		TmsPost posts = this.tmsPostRepository.findOne(commentsDTO.getPostId());
		posts.setComments(posts.getComments() + 1);
		posts = this.tmsPostRepository.save(posts);
		this.tmsPostSearchRepository.save(posts);
//		Set<Notes> notes = bug.getNotes();
//		notes.add(note);
//		bug.setNotes(notes);
//		this.bugsRepository.save(bug);
		
		Set<AttachmentsDTO> attachments =  commentsDTO.getAttachments();
		if (!CollectionUtils.isEmpty(attachments)) {
			Attachments attachmentsDomain;
			Set<Attachments> attachmentsList = new HashSet<>();
			for (AttachmentsDTO attachmentsDTO : attachments) {
				attachmentsDomain = new Attachments();
				attachmentsDomain = this.processAttachments(attachmentsDTO, comments);
				attachmentsDomain = this.attachmentsRepository.save(attachmentsDomain);
				this.attachmentsSearchRepository.save(attachmentsDomain);
				attachmentsList.add(attachmentsDomain);
			}
			comments.setAttachments(attachmentsList);
		}
		
		return this.commentsMapper.toDto(comments);
	}
	
    private Attachments processAttachments(AttachmentsDTO attachmentsDTO, Comments comments) throws IOException {
    	Attachments attachment = new Attachments();
    	byte[] imageByte= Base64.decodeBase64(attachmentsDTO.getValue());
    	if (imageByte != null) {
    		Date now = new Date();
    	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
    	    	
    	    String[] folders = {"comments", comments.getId().toString()};
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
    	    attachment.setComment(comments);
    	}
	    return attachment;
    }

	public CommentsDTO updateComments(CommentsDTO commentsDTO) throws IOException {
		Comments comments = this.commentsRepository.findOne(commentsDTO.getId());
		if (comments != null) {
			Set<Attachments> attachmentSet = comments.getAttachments();
			comments.setContent(commentsDTO.getContent());
			
			Attachments attachmentsDomain;
	        Set<AttachmentsDTO> attachmentsAppend = commentsDTO.getAttachmentsAppend();
			if (!CollectionUtils.isEmpty(attachmentsAppend)) {
				for (AttachmentsDTO attachmentsDTO : attachmentsAppend) {
					attachmentsDomain = new Attachments();
					attachmentsDomain = this.processAttachments(attachmentsDTO, comments);
					this.attachmentsRepository.save(attachmentsDomain);
					this.attachmentsSearchRepository.save(attachmentsDomain);
					attachmentSet.add(attachmentsDomain);
				}
			}
			
			Set<AttachmentsDTO> attachmentsRemove = commentsDTO.getAttachmentsRemove();
			if (!CollectionUtils.isEmpty(attachmentsRemove)) {
				for (AttachmentsDTO attachmentsDTO : attachmentsRemove) {
					attachmentsDomain = this.attachmentsRepository.findOne(attachmentsDTO.getId());
					this.storageService.deleteFile(attachmentsDTO.getDiskFile());
					this.attachmentsRepository.delete(attachmentsDTO.getId());
					this.attachmentsSearchRepository.delete(attachmentsDTO.getId());
					attachmentSet.remove(attachmentsDomain);
				}
			}
			comments.setAttachments(attachmentSet);
			comments = this.commentsRepository.save(comments);
	        this.commentsSearchRepository.save(comments);
		}
        
		return this.commentsMapper.toDto(comments);
	}
}
