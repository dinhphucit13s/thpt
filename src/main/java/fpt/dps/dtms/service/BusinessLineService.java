package fpt.dps.dtms.service;

import fpt.dps.dtms.domain.BusinessLine;
import fpt.dps.dtms.repository.BusinessLineRepository;
import fpt.dps.dtms.repository.search.BusinessLineSearchRepository;
import fpt.dps.dtms.service.dto.BusinessLineDTO;
import fpt.dps.dtms.service.mapper.BusinessLineMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing BusinessLine.
 */
@Service
@Transactional
public class BusinessLineService {

    private final Logger log = LoggerFactory.getLogger(BusinessLineService.class);

    private final BusinessLineRepository businessLineRepository;

    private final BusinessLineMapper businessLineMapper;

    private final BusinessLineSearchRepository businessLineSearchRepository;

    public BusinessLineService(BusinessLineRepository businessLineRepository, BusinessLineMapper businessLineMapper, BusinessLineSearchRepository businessLineSearchRepository) {
        this.businessLineRepository = businessLineRepository;
        this.businessLineMapper = businessLineMapper;
        this.businessLineSearchRepository = businessLineSearchRepository;
    }

    /**
     * Save a businessLine.
     *
     * @param businessLineDTO the entity to save
     * @return the persisted entity
     */
    public BusinessLineDTO save(BusinessLineDTO businessLineDTO) {
        log.debug("Request to save BusinessLine : {}", businessLineDTO);
        BusinessLine businessLine = businessLineMapper.toEntity(businessLineDTO);
        businessLine = businessLineRepository.save(businessLine);
        BusinessLineDTO result = businessLineMapper.toDto(businessLine);
        businessLineSearchRepository.save(businessLine);
        return result;
    }

    /**
     * Get all the businessLines.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<BusinessLineDTO> findAll(Pageable pageable) {
        log.debug("Request to get all BusinessLines");
        return businessLineRepository.findAll(pageable)
            .map(businessLineMapper::toDto);
    }

    /**
     * Get one businessLine by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public BusinessLineDTO findOne(Long id) {
        log.debug("Request to get BusinessLine : {}", id);
        BusinessLine businessLine = businessLineRepository.findOne(id);
        return businessLineMapper.toDto(businessLine);
    }

    /**
     * Delete the businessLine by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete BusinessLine : {}", id);
        businessLineRepository.delete(id);
        businessLineSearchRepository.delete(id);
    }

    /**
     * Search for the businessLine corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<BusinessLineDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of BusinessLines for query {}", query);
        Page<BusinessLine> result = businessLineSearchRepository.findByNameLike(query, pageable);
        return result.map(businessLineMapper::toDto);
    }
}
