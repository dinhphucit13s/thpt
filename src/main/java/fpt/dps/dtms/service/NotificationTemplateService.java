package fpt.dps.dtms.service;

import fpt.dps.dtms.domain.NotificationTemplate;
import fpt.dps.dtms.domain.enumeration.NotificationCategory;
import fpt.dps.dtms.repository.NotificationTemplateRepository;
import fpt.dps.dtms.repository.search.NotificationTemplateSearchRepository;
import fpt.dps.dtms.service.dto.NotificationTemplateDTO;
import fpt.dps.dtms.service.mapper.NotificationTemplateMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing NotificationTemplate.
 */
@Service
@Transactional
public class NotificationTemplateService {

    private final Logger log = LoggerFactory.getLogger(NotificationTemplateService.class);

    private final NotificationTemplateRepository notificationTemplateRepository;

    private final NotificationTemplateMapper notificationTemplateMapper;

    private final NotificationTemplateSearchRepository notificationTemplateSearchRepository;

    public NotificationTemplateService(NotificationTemplateRepository notificationTemplateRepository, NotificationTemplateMapper notificationTemplateMapper, NotificationTemplateSearchRepository notificationTemplateSearchRepository) {
        this.notificationTemplateRepository = notificationTemplateRepository;
        this.notificationTemplateMapper = notificationTemplateMapper;
        this.notificationTemplateSearchRepository = notificationTemplateSearchRepository;
    }

    /**
     * Save a notificationTemplate.
     *
     * @param notificationTemplateDTO the entity to save
     * @return the persisted entity
     */
    public NotificationTemplateDTO save(NotificationTemplateDTO notificationTemplateDTO) {
        log.debug("Request to save NotificationTemplate : {}", notificationTemplateDTO);
        NotificationTemplate notificationTemplate = notificationTemplateMapper.toEntity(notificationTemplateDTO);
        notificationTemplate = notificationTemplateRepository.save(notificationTemplate);
        NotificationTemplateDTO result = notificationTemplateMapper.toDto(notificationTemplate);
        notificationTemplateSearchRepository.save(notificationTemplate);
        return result;
    }

    /**
     * Get all the notificationTemplates.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<NotificationTemplateDTO> findAll(Pageable pageable) {
        log.debug("Request to get all NotificationTemplates");
        return notificationTemplateRepository.findAll(pageable)
            .map(notificationTemplateMapper::toDto);
    }

    /**
     * Get one notificationTemplate by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public NotificationTemplateDTO findOne(Long id) {
        log.debug("Request to get NotificationTemplate : {}", id);
        NotificationTemplate notificationTemplate = notificationTemplateRepository.findOne(id);
        return notificationTemplateMapper.toDto(notificationTemplate);
    }

    /**
     * Delete the notificationTemplate by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete NotificationTemplate : {}", id);
        notificationTemplateRepository.delete(id);
        notificationTemplateSearchRepository.delete(id);
    }

    /**
     * Search for the notificationTemplate corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<NotificationTemplateDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of NotificationTemplates for query {}", query);
        Page<NotificationTemplate> result = notificationTemplateSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(notificationTemplateMapper::toDto);
    }

	public String getContentTemplate(NotificationCategory type) {
		// TODO Auto-generated method stub
		return this.notificationTemplateRepository.getContentTemplate(type);
	}
}
