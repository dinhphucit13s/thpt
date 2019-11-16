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

import fpt.dps.dtms.domain.TMSCustomField;
import fpt.dps.dtms.domain.*; // for static metamodels
import fpt.dps.dtms.repository.TMSCustomFieldRepository;
import fpt.dps.dtms.repository.search.TMSCustomFieldSearchRepository;
import fpt.dps.dtms.service.dto.TMSCustomFieldCriteria;

import fpt.dps.dtms.service.dto.TMSCustomFieldDTO;
import fpt.dps.dtms.service.mapper.TMSCustomFieldMapper;

/**
 * Service for executing complex queries for TMSCustomField entities in the database.
 * The main input is a {@link TMSCustomFieldCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TMSCustomFieldDTO} or a {@link Page} of {@link TMSCustomFieldDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TMSCustomFieldQueryService extends QueryService<TMSCustomField> {

    private final Logger log = LoggerFactory.getLogger(TMSCustomFieldQueryService.class);


    private final TMSCustomFieldRepository tMSCustomFieldRepository;

    private final TMSCustomFieldMapper tMSCustomFieldMapper;

    private final TMSCustomFieldSearchRepository tMSCustomFieldSearchRepository;

    public TMSCustomFieldQueryService(TMSCustomFieldRepository tMSCustomFieldRepository, TMSCustomFieldMapper tMSCustomFieldMapper, TMSCustomFieldSearchRepository tMSCustomFieldSearchRepository) {
        this.tMSCustomFieldRepository = tMSCustomFieldRepository;
        this.tMSCustomFieldMapper = tMSCustomFieldMapper;
        this.tMSCustomFieldSearchRepository = tMSCustomFieldSearchRepository;
    }

    /**
     * Return a {@link List} of {@link TMSCustomFieldDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TMSCustomFieldDTO> findByCriteria(TMSCustomFieldCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<TMSCustomField> specification = createSpecification(criteria);
        return tMSCustomFieldMapper.toDto(tMSCustomFieldRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link TMSCustomFieldDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TMSCustomFieldDTO> findByCriteria(TMSCustomFieldCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<TMSCustomField> specification = createSpecification(criteria);
        final Page<TMSCustomField> result = tMSCustomFieldRepository.findAll(specification, page);
        return result.map(tMSCustomFieldMapper::toDto);
    }

    /**
     * search TMSCustomField
     * @param query
     * @return
     */
    @Transactional(readOnly = true)
    public Boolean checkNameExists(String query) {
    	String queryHeaderName = "\"field\":\"" + query + "\"";
    	List<TMSCustomField> tmsCustomFields = tMSCustomFieldRepository.getAllCustomFieldByQuerySearch(queryHeaderName);
    	if (tmsCustomFields.size() > 0) {
    		return true;
    	}
    	return false;
    }
    
    /**
     * search TMSCustomField
     * @param query
     * @return
     */
    @Transactional(readOnly = true)
    public List<TMSCustomFieldDTO> getAllCustomFieldByQuerySearch(String query) {
    	List<TMSCustomField> tmsCustomFields = tMSCustomFieldRepository.getAllCustomFieldByQuerySearch(query);
    	return tMSCustomFieldMapper.toDto(tmsCustomFields);
    }

    @Transactional(readOnly = true)
    public List<TMSCustomFieldDTO> getAllCustomFieldByWorkflowId(Long workflowId) {
    	return tMSCustomFieldMapper.toDto(tMSCustomFieldRepository.getListTMSCustomFieldScreenByWFId(workflowId));
    }

    /**
     * Function to convert TMSCustomFieldCriteria to a {@link Specifications}
     */
    private Specifications<TMSCustomField> createSpecification(TMSCustomFieldCriteria criteria) {
        Specifications<TMSCustomField> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), TMSCustomField_.id));
            }
        }
        return specification;
    }

}
