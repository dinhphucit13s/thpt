package fpt.dps.dtms.service;

import fpt.dps.dtms.domain.BusinessUnitManager;
import fpt.dps.dtms.repository.BusinessUnitManagerRepository;
import fpt.dps.dtms.repository.search.BusinessUnitManagerSearchRepository;
import fpt.dps.dtms.service.dto.BusinessUnitManagerDTO;
import fpt.dps.dtms.service.dto.BusinessUnitManagerMSCDTO;
import fpt.dps.dtms.service.mapper.BusinessUnitManagerMSCMapper;
import fpt.dps.dtms.service.mapper.BusinessUnitManagerMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

import java.util.List;

/**
 * Service Implementation for managing BusinessUnitManager.
 */
@Service
@Transactional
public class BusinessUnitManagerService {

    private final Logger log = LoggerFactory.getLogger(BusinessUnitManagerService.class);

    private final BusinessUnitManagerRepository businessUnitManagerRepository;

    private final BusinessUnitManagerMapper businessUnitManagerMapper;
    
    private final BusinessUnitManagerMSCMapper businessUnitManagerMSCMapper;

    private final BusinessUnitManagerSearchRepository businessUnitManagerSearchRepository;

    public BusinessUnitManagerService(BusinessUnitManagerRepository businessUnitManagerRepository, BusinessUnitManagerMapper businessUnitManagerMapper, BusinessUnitManagerSearchRepository businessUnitManagerSearchRepository,
    		BusinessUnitManagerMSCMapper businessUnitManagerMSCMapper) {
        this.businessUnitManagerRepository = businessUnitManagerRepository;
        this.businessUnitManagerMapper = businessUnitManagerMapper;
        this.businessUnitManagerSearchRepository = businessUnitManagerSearchRepository;
        this.businessUnitManagerMSCMapper = businessUnitManagerMSCMapper;
    }

    /**
     * Save a businessUnitManager.
     *
     * @param businessUnitManagerDTO the entity to save
     * @return the persisted entity
     */
    public BusinessUnitManagerDTO save(BusinessUnitManagerDTO businessUnitManagerDTO) {
        log.debug("Request to save BusinessUnitManager : {}", businessUnitManagerDTO);
        BusinessUnitManager businessUnitManager = businessUnitManagerMapper.toEntity(businessUnitManagerDTO);
        businessUnitManager = businessUnitManagerRepository.save(businessUnitManager);
        BusinessUnitManagerDTO result = businessUnitManagerMapper.toDto(businessUnitManager);
        businessUnitManagerSearchRepository.save(businessUnitManager);
        return result;
    }

    /**
     * Get all the businessUnitManagers.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<BusinessUnitManagerDTO> findAll(Pageable pageable) {
        log.debug("Request to get all BusinessUnitManagers");
        return businessUnitManagerRepository.findAll(pageable)
            .map(businessUnitManagerMapper::toDto);
    }
    
    @Transactional(readOnly = true)
    public List<BusinessUnitManagerMSCDTO> findAlForMSCl() {
        log.debug("Request to get all BusinessUnitManagers");
        List<BusinessUnitManager> listBusinessUnitManager = businessUnitManagerRepository.findAll();
        return businessUnitManagerMSCMapper.toDto(listBusinessUnitManager);
    }

    /**
     * Get one businessUnitManager by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public BusinessUnitManagerDTO findOne(Long id) {
        log.debug("Request to get BusinessUnitManager : {}", id);
        BusinessUnitManager businessUnitManager = businessUnitManagerRepository.findOne(id);
        return businessUnitManagerMapper.toDto(businessUnitManager);
    }

    /**
     * Delete the businessUnitManager by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete BusinessUnitManager : {}", id);
        businessUnitManagerRepository.delete(id);
        businessUnitManagerSearchRepository.delete(id);
    }

    /**
     * Search for the businessUnitManager corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<BusinessUnitManagerDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of BusinessUnitManagers for query {}", query);
        Page<BusinessUnitManager> result = businessUnitManagerSearchRepository.findByBusinessUnitNameLike(query, pageable);
        return result.map(businessUnitManagerMapper::toDto);
    }
}
