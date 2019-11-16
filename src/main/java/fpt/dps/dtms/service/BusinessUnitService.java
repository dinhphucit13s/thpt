package fpt.dps.dtms.service;

import fpt.dps.dtms.domain.BusinessUnit;
import fpt.dps.dtms.repository.BusinessUnitRepository;
import fpt.dps.dtms.repository.search.BusinessUnitSearchRepository;
import fpt.dps.dtms.service.dto.BusinessUnitDTO;
import fpt.dps.dtms.service.mapper.BusinessUnitMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing BusinessUnit.
 */
@Service
@Transactional
public class BusinessUnitService {

    private final Logger log = LoggerFactory.getLogger(BusinessUnitService.class);

    private final BusinessUnitRepository businessUnitRepository;

    private final BusinessUnitMapper businessUnitMapper;

    private final BusinessUnitSearchRepository businessUnitSearchRepository;

    public BusinessUnitService(BusinessUnitRepository businessUnitRepository, BusinessUnitMapper businessUnitMapper, BusinessUnitSearchRepository businessUnitSearchRepository) {
        this.businessUnitRepository = businessUnitRepository;
        this.businessUnitMapper = businessUnitMapper;
        this.businessUnitSearchRepository = businessUnitSearchRepository;
    }

    /**
     * Save a businessUnit.
     *
     * @param businessUnitDTO the entity to save
     * @return the persisted entity
     */
    public BusinessUnitDTO save(BusinessUnitDTO businessUnitDTO) {
        log.debug("Request to save BusinessUnit : {}", businessUnitDTO);
        BusinessUnit businessUnit = businessUnitMapper.toEntity(businessUnitDTO);
        businessUnit = businessUnitRepository.save(businessUnit);
        BusinessUnitDTO result = businessUnitMapper.toDto(businessUnit);
        businessUnitSearchRepository.save(businessUnit);
        return result;
    }

    /**
     * Get all the businessUnits.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<BusinessUnitDTO> findAll(Pageable pageable) {
        log.debug("Request to get all BusinessUnits");
        return businessUnitRepository.findAll(pageable)
            .map(businessUnitMapper::toDto);
    }

    /**
     * Get one businessUnit by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public BusinessUnitDTO findOne(Long id) {
        log.debug("Request to get BusinessUnit : {}", id);
        BusinessUnit businessUnit = businessUnitRepository.findOne(id);
        return businessUnitMapper.toDto(businessUnit);
    }

    /**
     * Delete the businessUnit by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete BusinessUnit : {}", id);
        businessUnitRepository.delete(id);
        businessUnitSearchRepository.delete(id);
    }

    /**
     * Search for the businessUnit corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<BusinessUnitDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of BusinessUnits for query {}", query);
        Page<BusinessUnit> result = businessUnitSearchRepository.findByNameLike(query, pageable);
        return result.map(businessUnitMapper::toDto);
    }
}
