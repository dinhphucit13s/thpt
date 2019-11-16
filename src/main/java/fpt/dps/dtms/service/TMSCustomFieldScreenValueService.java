package fpt.dps.dtms.service;

import fpt.dps.dtms.domain.TMSCustomFieldScreenValue;
import fpt.dps.dtms.repository.TMSCustomFieldScreenValueRepository;
import fpt.dps.dtms.repository.search.TMSCustomFieldScreenValueSearchRepository;
import fpt.dps.dtms.service.dto.TMSCustomFieldScreenValueDTO;
import fpt.dps.dtms.service.mapper.TMSCustomFieldScreenValueMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing TMSCustomFieldScreenValue.
 */
@Service
@Transactional
public class TMSCustomFieldScreenValueService {

    private final Logger log = LoggerFactory.getLogger(TMSCustomFieldScreenValueService.class);

    private final TMSCustomFieldScreenValueRepository tMSCustomFieldScreenValueRepository;

    private final TMSCustomFieldScreenValueMapper tMSCustomFieldScreenValueMapper;

    private final TMSCustomFieldScreenValueSearchRepository tMSCustomFieldScreenValueSearchRepository;

    public TMSCustomFieldScreenValueService(TMSCustomFieldScreenValueRepository tMSCustomFieldScreenValueRepository, TMSCustomFieldScreenValueMapper tMSCustomFieldScreenValueMapper, TMSCustomFieldScreenValueSearchRepository tMSCustomFieldScreenValueSearchRepository) {
        this.tMSCustomFieldScreenValueRepository = tMSCustomFieldScreenValueRepository;
        this.tMSCustomFieldScreenValueMapper = tMSCustomFieldScreenValueMapper;
        this.tMSCustomFieldScreenValueSearchRepository = tMSCustomFieldScreenValueSearchRepository;
    }

    /**
     * Save a tMSCustomFieldScreenValue.
     *
     * @param tMSCustomFieldScreenValueDTO the entity to save
     * @return the persisted entity
     */
    public TMSCustomFieldScreenValueDTO save(TMSCustomFieldScreenValueDTO tMSCustomFieldScreenValueDTO) {
        log.debug("Request to save TMSCustomFieldScreenValue : {}", tMSCustomFieldScreenValueDTO);
        TMSCustomFieldScreenValue tMSCustomFieldScreenValue = tMSCustomFieldScreenValueMapper.toEntity(tMSCustomFieldScreenValueDTO);
        tMSCustomFieldScreenValue = tMSCustomFieldScreenValueRepository.save(tMSCustomFieldScreenValue);
        TMSCustomFieldScreenValueDTO result = tMSCustomFieldScreenValueMapper.toDto(tMSCustomFieldScreenValue);
        tMSCustomFieldScreenValueSearchRepository.save(tMSCustomFieldScreenValue);
        return result;
    }

    /**
     * Get all the tMSCustomFieldScreenValues.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<TMSCustomFieldScreenValueDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TMSCustomFieldScreenValues");
        return tMSCustomFieldScreenValueRepository.findAll(pageable)
            .map(tMSCustomFieldScreenValueMapper::toDto);
    }

    /**
     * Get one tMSCustomFieldScreenValue by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public TMSCustomFieldScreenValueDTO findOne(Long id) {
        log.debug("Request to get TMSCustomFieldScreenValue : {}", id);
        TMSCustomFieldScreenValue tMSCustomFieldScreenValue = tMSCustomFieldScreenValueRepository.findOne(id);
        return tMSCustomFieldScreenValueMapper.toDto(tMSCustomFieldScreenValue);
    }

    /**
     * Delete the tMSCustomFieldScreenValue by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete TMSCustomFieldScreenValue : {}", id);
        tMSCustomFieldScreenValueRepository.delete(id);
        tMSCustomFieldScreenValueSearchRepository.delete(id);
    }

    /**
     * Search for the tMSCustomFieldScreenValue corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<TMSCustomFieldScreenValueDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of TMSCustomFieldScreenValues for query {}", query);
        Page<TMSCustomFieldScreenValue> result = tMSCustomFieldScreenValueSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(tMSCustomFieldScreenValueMapper::toDto);
    }
}
