package fpt.dps.dtms.service;

import fpt.dps.dtms.domain.TMSLogHistory;
import fpt.dps.dtms.repository.TMSLogHistoryRepository;
import fpt.dps.dtms.repository.search.TMSLogHistorySearchRepository;
import fpt.dps.dtms.service.dto.TMSLogHistoryDTO;
import fpt.dps.dtms.service.mapper.TMSLogHistoryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing TMSLogHistory.
 */
@Service
@Transactional
public class TMSLogHistoryService {

    private final Logger log = LoggerFactory.getLogger(TMSLogHistoryService.class);

    private final TMSLogHistoryRepository tMSLogHistoryRepository;

    private final TMSLogHistoryMapper tMSLogHistoryMapper;

    private final TMSLogHistorySearchRepository tMSLogHistorySearchRepository;

    public TMSLogHistoryService(TMSLogHistoryRepository tMSLogHistoryRepository, TMSLogHistoryMapper tMSLogHistoryMapper, TMSLogHistorySearchRepository tMSLogHistorySearchRepository) {
        this.tMSLogHistoryRepository = tMSLogHistoryRepository;
        this.tMSLogHistoryMapper = tMSLogHistoryMapper;
        this.tMSLogHistorySearchRepository = tMSLogHistorySearchRepository;
    }

    /**
     * Save a tMSLogHistory.
     *
     * @param tMSLogHistoryDTO the entity to save
     * @return the persisted entity
     */
    public TMSLogHistoryDTO save(TMSLogHistoryDTO tMSLogHistoryDTO) {
        log.debug("Request to save TMSLogHistory : {}", tMSLogHistoryDTO);
        TMSLogHistory tMSLogHistory = tMSLogHistoryMapper.toEntity(tMSLogHistoryDTO);
        tMSLogHistory = tMSLogHistoryRepository.save(tMSLogHistory);
        TMSLogHistoryDTO result = tMSLogHistoryMapper.toDto(tMSLogHistory);
        tMSLogHistorySearchRepository.save(tMSLogHistory);
        return result;
    }

    /**
     * Get all the tMSLogHistories.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<TMSLogHistoryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TMSLogHistories");
        return tMSLogHistoryRepository.findAll(pageable)
            .map(tMSLogHistoryMapper::toDto);
    }

    /**
     * Get one tMSLogHistory by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public TMSLogHistoryDTO findOne(Long id) {
        log.debug("Request to get TMSLogHistory : {}", id);
        TMSLogHistory tMSLogHistory = tMSLogHistoryRepository.findOne(id);
        return tMSLogHistoryMapper.toDto(tMSLogHistory);
    }

    /**
     * Delete the tMSLogHistory by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete TMSLogHistory : {}", id);
        tMSLogHistoryRepository.delete(id);
        tMSLogHistorySearchRepository.delete(id);
    }

    /**
     * Search for the tMSLogHistory corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<TMSLogHistoryDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of TMSLogHistories for query {}", query);
        Page<TMSLogHistory> result = tMSLogHistorySearchRepository.search(queryStringQuery(query), pageable);
        return result.map(tMSLogHistoryMapper::toDto);
    }
}
