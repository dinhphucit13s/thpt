package fpt.dps.dtms.service;

import fpt.dps.dtms.domain.TMSCustomFieldScreen;
import fpt.dps.dtms.repository.TMSCustomFieldScreenRepository;
import fpt.dps.dtms.repository.search.TMSCustomFieldScreenSearchRepository;
import fpt.dps.dtms.service.dto.TMSCustomFieldScreenDTO;
import fpt.dps.dtms.service.mapper.TMSCustomFieldScreenMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing TMSCustomFieldScreen.
 */
@Service
@Transactional
public class TMSCustomFieldScreenService {

    private final Logger log = LoggerFactory.getLogger(TMSCustomFieldScreenService.class);

    private final TMSCustomFieldScreenRepository tMSCustomFieldScreenRepository;

    private final TMSCustomFieldScreenMapper tMSCustomFieldScreenMapper;

    private final TMSCustomFieldScreenSearchRepository tMSCustomFieldScreenSearchRepository;

    public TMSCustomFieldScreenService(TMSCustomFieldScreenRepository tMSCustomFieldScreenRepository, TMSCustomFieldScreenMapper tMSCustomFieldScreenMapper, TMSCustomFieldScreenSearchRepository tMSCustomFieldScreenSearchRepository) {
        this.tMSCustomFieldScreenRepository = tMSCustomFieldScreenRepository;
        this.tMSCustomFieldScreenMapper = tMSCustomFieldScreenMapper;
        this.tMSCustomFieldScreenSearchRepository = tMSCustomFieldScreenSearchRepository;
    }

    /**
     * Save a tMSCustomFieldScreen.
     *
     * @param tMSCustomFieldScreenDTO the entity to save
     * @return the persisted entity
     */
    public TMSCustomFieldScreenDTO save(TMSCustomFieldScreenDTO tMSCustomFieldScreenDTO) {
        log.debug("Request to save TMSCustomFieldScreen : {}", tMSCustomFieldScreenDTO);
        TMSCustomFieldScreen tMSCustomFieldScreen = tMSCustomFieldScreenMapper.toEntity(tMSCustomFieldScreenDTO);
        tMSCustomFieldScreen = tMSCustomFieldScreenRepository.save(tMSCustomFieldScreen);
        TMSCustomFieldScreenDTO result = tMSCustomFieldScreenMapper.toDto(tMSCustomFieldScreen);
        tMSCustomFieldScreenSearchRepository.save(tMSCustomFieldScreen);
        return result;
    }

    /**
     * Get all the tMSCustomFieldScreens.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<TMSCustomFieldScreenDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TMSCustomFieldScreens");
        return tMSCustomFieldScreenRepository.findAll(pageable)
            .map(tMSCustomFieldScreenMapper::toDto);
    }

    /**
     * Get one tMSCustomFieldScreen by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public TMSCustomFieldScreenDTO findOne(Long id) {
        log.debug("Request to get TMSCustomFieldScreen : {}", id);
        TMSCustomFieldScreen tMSCustomFieldScreen = tMSCustomFieldScreenRepository.findOne(id);
        return tMSCustomFieldScreenMapper.toDto(tMSCustomFieldScreen);
    }

    /**
     * Delete the tMSCustomFieldScreen by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete TMSCustomFieldScreen : {}", id);
        tMSCustomFieldScreenRepository.delete(id);
        tMSCustomFieldScreenSearchRepository.delete(id);
    }

    /**
     * Search for the tMSCustomFieldScreen corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<TMSCustomFieldScreenDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of TMSCustomFieldScreens for query {}", query);
        Page<TMSCustomFieldScreen> result = tMSCustomFieldScreenSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(tMSCustomFieldScreenMapper::toDto);
    }
}
