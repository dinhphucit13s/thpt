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

import fpt.dps.dtms.domain.BusinessLine;
import fpt.dps.dtms.domain.*; // for static metamodels
import fpt.dps.dtms.repository.BusinessLineRepository;
import fpt.dps.dtms.repository.search.BusinessLineSearchRepository;
import fpt.dps.dtms.service.dto.BusinessLineCriteria;

import fpt.dps.dtms.service.dto.BusinessLineDTO;
import fpt.dps.dtms.service.mapper.BusinessLineMapper;

/**
 * Service for executing complex queries for BusinessLine entities in the database.
 * The main input is a {@link BusinessLineCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link BusinessLineDTO} or a {@link Page} of {@link BusinessLineDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class BusinessLineQueryService extends QueryService<BusinessLine> {

    private final Logger log = LoggerFactory.getLogger(BusinessLineQueryService.class);


    private final BusinessLineRepository businessLineRepository;

    private final BusinessLineMapper businessLineMapper;

    private final BusinessLineSearchRepository businessLineSearchRepository;

    public BusinessLineQueryService(BusinessLineRepository businessLineRepository, BusinessLineMapper businessLineMapper, BusinessLineSearchRepository businessLineSearchRepository) {
        this.businessLineRepository = businessLineRepository;
        this.businessLineMapper = businessLineMapper;
        this.businessLineSearchRepository = businessLineSearchRepository;
    }

    /**
     * Return a {@link List} of {@link BusinessLineDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<BusinessLineDTO> findByCriteria(BusinessLineCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<BusinessLine> specification = createSpecification(criteria);
        return businessLineMapper.toDto(businessLineRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link BusinessLineDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<BusinessLineDTO> findByCriteria(BusinessLineCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<BusinessLine> specification = createSpecification(criteria);
        final Page<BusinessLine> result = businessLineRepository.findAll(specification, page);
        return result.map(businessLineMapper::toDto);
    }

    /**
     * Function to convert BusinessLineCriteria to a {@link Specifications}
     */
    private Specifications<BusinessLine> createSpecification(BusinessLineCriteria criteria) {
        Specifications<BusinessLine> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), BusinessLine_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), BusinessLine_.name));
            }
        }
        return specification;
    }

}
