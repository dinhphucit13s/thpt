package fpt.dps.dtms.service;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import fpt.dps.dtms.domain.TMSCustomFieldScreenValue;
import fpt.dps.dtms.domain.*; // for static metamodels
import fpt.dps.dtms.repository.TMSCustomFieldScreenValueRepository;
import fpt.dps.dtms.repository.search.TMSCustomFieldScreenValueSearchRepository;
import fpt.dps.dtms.service.dto.TMSCustomFieldScreenValueCriteria;

import fpt.dps.dtms.service.dto.TMSCustomFieldScreenValueDTO;
import fpt.dps.dtms.service.mapper.TMSCustomFieldScreenValueMapper;

/**
 * Service for executing complex queries for TMSCustomFieldScreenValue entities in the database.
 * The main input is a {@link TMSCustomFieldScreenValueCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TMSCustomFieldScreenValueDTO} or a {@link Page} of {@link TMSCustomFieldScreenValueDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TMSCustomFieldScreenValueQueryService extends QueryService<TMSCustomFieldScreenValue> {

    private final Logger log = LoggerFactory.getLogger(TMSCustomFieldScreenValueQueryService.class);


    private final TMSCustomFieldScreenValueRepository tMSCustomFieldScreenValueRepository;

    private final TMSCustomFieldScreenValueMapper tMSCustomFieldScreenValueMapper;

    private final TMSCustomFieldScreenValueSearchRepository tMSCustomFieldScreenValueSearchRepository;

    public TMSCustomFieldScreenValueQueryService(TMSCustomFieldScreenValueRepository tMSCustomFieldScreenValueRepository, TMSCustomFieldScreenValueMapper tMSCustomFieldScreenValueMapper, TMSCustomFieldScreenValueSearchRepository tMSCustomFieldScreenValueSearchRepository) {
        this.tMSCustomFieldScreenValueRepository = tMSCustomFieldScreenValueRepository;
        this.tMSCustomFieldScreenValueMapper = tMSCustomFieldScreenValueMapper;
        this.tMSCustomFieldScreenValueSearchRepository = tMSCustomFieldScreenValueSearchRepository;
    }

    /**
     * Return a {@link List} of {@link TMSCustomFieldScreenValueDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TMSCustomFieldScreenValueDTO> findByCriteria(TMSCustomFieldScreenValueCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<TMSCustomFieldScreenValue> specification = createSpecification(criteria);
        return tMSCustomFieldScreenValueMapper.toDto(tMSCustomFieldScreenValueRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link TMSCustomFieldScreenValueDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TMSCustomFieldScreenValueDTO> findByCriteria(TMSCustomFieldScreenValueCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<TMSCustomFieldScreenValue> specification = createSpecification(criteria);
        final Page<TMSCustomFieldScreenValue> result = tMSCustomFieldScreenValueRepository.findAll(specification, page);
        return result.map(tMSCustomFieldScreenValueMapper::toDto);
    }
    
    /**
     * get all tmsCustomFieldScreenValue by package id
     * @param packId
     * @return
     */
    @Transactional(readOnly = true)
    public List<TMSCustomFieldScreenValueDTO> getAllTMSCustomFieldScreenValueByPackId(Long packId) {
    	log.debug("find by packId : {}, page: {}", packId);
    	return tMSCustomFieldScreenValueMapper.toDto(tMSCustomFieldScreenValueRepository.getAllTMSCustomFieldScreenValueByPackId(packId));
    }
    
    /**
     * get all tmsCustomFieldScreenValue by task id
     * @param TaskId
     * @return
     */
    @Transactional(readOnly = true)
    public List<TMSCustomFieldScreenValueDTO> getAllTMSCustomFieldScreenValueByTaskId(Long TaskId) {
    	log.debug("find by packId : {}, page: {}", TaskId);
    	return tMSCustomFieldScreenValueMapper.toDto(tMSCustomFieldScreenValueRepository.getAllTMSCustomFieldScreenValueByTaskId(TaskId));
    }
    
    /**
     * get all tmsCustomFieldScreenValue by package id and tmsScreenId
     * @param packId
     * @return
     */
    @Transactional(readOnly = true)
    public TMSCustomFieldScreenValueDTO getTMSCustomFieldScreenValueByPackageOrTaskAndScreen(Long id, Long tmsScreenId, String screen) {
    	log.debug("find by id : {}, page: {}", id);
    	TMSCustomFieldScreenValue tmsCustomFieldScreenValue = new TMSCustomFieldScreenValue();
    	if (screen.equals("Package")) {
    		tmsCustomFieldScreenValue = tMSCustomFieldScreenValueRepository.getTMSCustomFieldScreenValueByPackageAndScreen(id, tmsScreenId);
    	} else if (screen.equals("Task")) {
    		tmsCustomFieldScreenValue = tMSCustomFieldScreenValueRepository.getTMSCustomFieldScreenValueByTaskAndScreen(id, tmsScreenId);
    	} else {}
    	return tMSCustomFieldScreenValueMapper.toDto(tmsCustomFieldScreenValue);
    }

    /**
     * Function to convert TMSCustomFieldScreenValueCriteria to a {@link Specifications}
     */
    private Specifications<TMSCustomFieldScreenValue> createSpecification(TMSCustomFieldScreenValueCriteria criteria) {
        Specifications<TMSCustomFieldScreenValue> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), TMSCustomFieldScreenValue_.id));
            }
            if (criteria.getValue() != null) {
                specification = specification.and(buildStringSpecification(criteria.getValue(), TMSCustomFieldScreenValue_.value));
            }
            if (criteria.getPurchaseOrdersId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getPurchaseOrdersId(), TMSCustomFieldScreenValue_.purchaseOrders, PurchaseOrders_.id));
            }
            if (criteria.getPackagesId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getPackagesId(), TMSCustomFieldScreenValue_.packages, Packages_.id));
            }
            if (criteria.getTasksId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getTasksId(), TMSCustomFieldScreenValue_.tasks, Tasks_.id));
            }
            if (criteria.getTmsCustomFieldId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getTmsCustomFieldId(), TMSCustomFieldScreenValue_.tmsCustomFieldScreen, TMSCustomFieldScreen_.id));
            }
        }
        return specification;
    }

}
