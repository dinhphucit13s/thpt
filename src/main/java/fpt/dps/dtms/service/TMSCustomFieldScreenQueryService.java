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

import fpt.dps.dtms.domain.TMSCustomFieldScreen;
import fpt.dps.dtms.domain.*; // for static metamodels
import fpt.dps.dtms.repository.TMSCustomFieldScreenRepository;
import fpt.dps.dtms.repository.search.TMSCustomFieldScreenSearchRepository;
import fpt.dps.dtms.service.dto.TMSCustomFieldDTO;
import fpt.dps.dtms.service.dto.TMSCustomFieldScreenCriteria;

import fpt.dps.dtms.service.dto.TMSCustomFieldScreenDTO;
import fpt.dps.dtms.service.mapper.TMSCustomFieldScreenMapper;

/**
 * Service for executing complex queries for TMSCustomFieldScreen entities in the database.
 * The main input is a {@link TMSCustomFieldScreenCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TMSCustomFieldScreenDTO} or a {@link Page} of {@link TMSCustomFieldScreenDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TMSCustomFieldScreenQueryService extends QueryService<TMSCustomFieldScreen> {

    private final Logger log = LoggerFactory.getLogger(TMSCustomFieldScreenQueryService.class);


    private final TMSCustomFieldScreenRepository tMSCustomFieldScreenRepository;

    private final TMSCustomFieldScreenMapper tMSCustomFieldScreenMapper;

    private final TMSCustomFieldScreenSearchRepository tMSCustomFieldScreenSearchRepository;

    public TMSCustomFieldScreenQueryService(TMSCustomFieldScreenRepository tMSCustomFieldScreenRepository, TMSCustomFieldScreenMapper tMSCustomFieldScreenMapper, TMSCustomFieldScreenSearchRepository tMSCustomFieldScreenSearchRepository) {
        this.tMSCustomFieldScreenRepository = tMSCustomFieldScreenRepository;
        this.tMSCustomFieldScreenMapper = tMSCustomFieldScreenMapper;
        this.tMSCustomFieldScreenSearchRepository = tMSCustomFieldScreenSearchRepository;
    }

    /**
     * Return a {@link List} of {@link TMSCustomFieldScreenDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TMSCustomFieldScreenDTO> findByCriteria(TMSCustomFieldScreenCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<TMSCustomFieldScreen> specification = createSpecification(criteria);
        return tMSCustomFieldScreenMapper.toDto(tMSCustomFieldScreenRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link TMSCustomFieldScreenDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TMSCustomFieldScreenDTO> findByCriteria(TMSCustomFieldScreenCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<TMSCustomFieldScreen> specification = createSpecification(criteria);
        final Page<TMSCustomFieldScreen> result = tMSCustomFieldScreenRepository.findAll(specification, page);
        return result.map(tMSCustomFieldScreenMapper::toDto);
    }
    
    @Transactional(readOnly = true)
    public TMSCustomFieldScreenDTO getCustomFieldByWorkflowId(Long customFieldId, Long workflowId) {
    	return tMSCustomFieldScreenMapper.toDto(tMSCustomFieldScreenRepository.getTMSCustomFieldScreenByWFId(customFieldId, workflowId));
    }
    
    /**
     * get all TMSCustomFieldScreenDTO by workflow id
     * @param workflowId
     * @return
     */
    @Transactional(readOnly = true)
    public List<TMSCustomFieldScreenDTO> getListTMSCustomFieldScreenByWFId(Long workflowId) {
    	return tMSCustomFieldScreenMapper.toDto(tMSCustomFieldScreenRepository.getListTMSCustomFieldScreenByWFId(workflowId));
    }

    /**
     * Function to convert TMSCustomFieldScreenCriteria to a {@link Specifications}
     */
    private Specifications<TMSCustomFieldScreen> createSpecification(TMSCustomFieldScreenCriteria criteria) {
        Specifications<TMSCustomFieldScreen> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), TMSCustomFieldScreen_.id));
            }
            if (criteria.getSequence() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSequence(), TMSCustomFieldScreen_.sequence));
            }
            if (criteria.getTmsCustomFieldId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getTmsCustomFieldId(), TMSCustomFieldScreen_.tmsCustomField, TMSCustomField_.id));
            }
            if (criteria.getProjectWorkflowsId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getProjectWorkflowsId(), TMSCustomFieldScreen_.projectWorkflows, ProjectWorkflows_.id));
            }
        }
        return specification;
    }

}
