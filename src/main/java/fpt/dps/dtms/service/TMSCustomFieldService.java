package fpt.dps.dtms.service;

import fpt.dps.dtms.domain.TMSCustomField;
import fpt.dps.dtms.repository.TMSCustomFieldRepository;
import fpt.dps.dtms.repository.search.TMSCustomFieldSearchRepository;
import fpt.dps.dtms.service.dto.TMSCustomFieldDTO;
import fpt.dps.dtms.service.mapper.TMSCustomFieldMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing TMSCustomField.
 */
@Service
@Transactional
public class TMSCustomFieldService {

    private final Logger log = LoggerFactory.getLogger(TMSCustomFieldService.class);

    private final TMSCustomFieldRepository tMSCustomFieldRepository;

    private final TMSCustomFieldMapper tMSCustomFieldMapper;

    private final TMSCustomFieldSearchRepository tMSCustomFieldSearchRepository;

    public TMSCustomFieldService(TMSCustomFieldRepository tMSCustomFieldRepository, TMSCustomFieldMapper tMSCustomFieldMapper, TMSCustomFieldSearchRepository tMSCustomFieldSearchRepository) {
        this.tMSCustomFieldRepository = tMSCustomFieldRepository;
        this.tMSCustomFieldMapper = tMSCustomFieldMapper;
        this.tMSCustomFieldSearchRepository = tMSCustomFieldSearchRepository;
    }

    /**
     * Save a tMSCustomField.
     *
     * @param tMSCustomFieldDTO the entity to save
     * @return the persisted entity
     */
    public TMSCustomFieldDTO save(TMSCustomFieldDTO tMSCustomFieldDTO) {
        log.debug("Request to save TMSCustomField : {}", tMSCustomFieldDTO);
        TMSCustomField tMSCustomField = tMSCustomFieldMapper.toEntity(tMSCustomFieldDTO);
        tMSCustomField = tMSCustomFieldRepository.save(tMSCustomField);
        TMSCustomFieldDTO result = tMSCustomFieldMapper.toDto(tMSCustomField);
        tMSCustomFieldSearchRepository.save(tMSCustomField);
        return result;
    }

    /**
     * Get all the tMSCustomFields.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<TMSCustomFieldDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TMSCustomFields");
        return tMSCustomFieldRepository.findAll(pageable)
            .map(tMSCustomFieldMapper::toDto);
    }

    /**
     * Get one tMSCustomField by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public TMSCustomFieldDTO findOne(Long id) {
        log.debug("Request to get TMSCustomField : {}", id);
        TMSCustomField tMSCustomField = tMSCustomFieldRepository.findOne(id);
        return tMSCustomFieldMapper.toDto(tMSCustomField);
    }

    /**
     * Delete the tMSCustomField by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete TMSCustomField : {}", id);
        tMSCustomFieldRepository.delete(id);
        tMSCustomFieldSearchRepository.delete(id);
    }

    /**
     * Search for the tMSCustomField corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<TMSCustomFieldDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of TMSCustomFields for query {}", query);
        Page<TMSCustomField> result = tMSCustomFieldSearchRepository.findByEntityDataLike(query, pageable);
        return result.map(tMSCustomFieldMapper::toDto);
    }
}
