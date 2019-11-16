package fpt.dps.dtms.service;

import fpt.dps.dtms.domain.Attachments;
import fpt.dps.dtms.repository.AttachmentsRepository;
import fpt.dps.dtms.repository.search.AttachmentsSearchRepository;
import fpt.dps.dtms.service.dto.AttachmentsDTO;
import fpt.dps.dtms.service.mapper.AttachmentsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Attachments.
 */
@Service
@Transactional
public class AttachmentsService {

    private final Logger log = LoggerFactory.getLogger(AttachmentsService.class);

    private final AttachmentsRepository attachmentsRepository;

    private final AttachmentsMapper attachmentsMapper;

    private final AttachmentsSearchRepository attachmentsSearchRepository;
    
    private final StorageService storageService;

    public AttachmentsService(AttachmentsRepository attachmentsRepository, AttachmentsMapper attachmentsMapper,
    		AttachmentsSearchRepository attachmentsSearchRepository, StorageService storageService) {
        this.attachmentsRepository = attachmentsRepository;
        this.attachmentsMapper = attachmentsMapper;
        this.attachmentsSearchRepository = attachmentsSearchRepository;
        this.storageService = storageService;
    }

    /**
     * Save a attachments.
     *
     * @param attachmentsDTO the entity to save
     * @return the persisted entity
     */
    public AttachmentsDTO save(AttachmentsDTO attachmentsDTO) {
        log.debug("Request to save Attachments : {}", attachmentsDTO);
        Attachments attachments = attachmentsMapper.toEntity(attachmentsDTO);
        attachments = attachmentsRepository.save(attachments);
        AttachmentsDTO result = attachmentsMapper.toDto(attachments);
        attachmentsSearchRepository.save(attachments);
        return result;
    }

    /**
     * Get all the attachments.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<AttachmentsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Attachments");
        return attachmentsRepository.findAll(pageable)
            .map(attachmentsMapper::toDto);
    }

    /**
     * Get one attachments by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public AttachmentsDTO findOne(Long id) {
        log.debug("Request to get Attachments : {}", id);
        Attachments attachments = attachmentsRepository.findOne(id);
        return attachmentsMapper.toDto(attachments);
    }

    /**
     * Delete the attachments by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Attachments : {}", id);
        attachmentsRepository.delete(id);
        attachmentsSearchRepository.delete(id);
    }

    /**
     * Search for the attachments corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<AttachmentsDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Attachments for query {}", query);
        Page<Attachments> result = attachmentsSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(attachmentsMapper::toDto);
    }

	public String getValuesBase64(Long id) {
		Attachments attachment = this.attachmentsRepository.findOne(id);
		return this.storageService.loadAttachment(attachment.getDiskFile());
	}
}
