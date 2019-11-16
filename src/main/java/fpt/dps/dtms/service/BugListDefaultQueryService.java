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

import fpt.dps.dtms.domain.BugListDefault;
import fpt.dps.dtms.domain.*; // for static metamodels
import fpt.dps.dtms.repository.BugListDefaultRepository;
import fpt.dps.dtms.repository.search.BugListDefaultSearchRepository;
import fpt.dps.dtms.service.dto.BugListDefaultCriteria;

import fpt.dps.dtms.service.dto.BugListDefaultDTO;
import fpt.dps.dtms.service.mapper.BugListDefaultMapper;

/**
 * Service for executing complex queries for BugListDefault entities in the database.
 * The main input is a {@link BugListDefaultCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link BugListDefaultDTO} or a {@link Page} of {@link BugListDefaultDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class BugListDefaultQueryService extends QueryService<BugListDefault> {

    private final Logger log = LoggerFactory.getLogger(BugListDefaultQueryService.class);


    private final BugListDefaultRepository bugListDefaultRepository;

    private final BugListDefaultMapper bugListDefaultMapper;

    private final BugListDefaultSearchRepository bugListDefaultSearchRepository;

    public BugListDefaultQueryService(BugListDefaultRepository bugListDefaultRepository, BugListDefaultMapper bugListDefaultMapper, BugListDefaultSearchRepository bugListDefaultSearchRepository) {
        this.bugListDefaultRepository = bugListDefaultRepository;
        this.bugListDefaultMapper = bugListDefaultMapper;
        this.bugListDefaultSearchRepository = bugListDefaultSearchRepository;
    }

    /**
     * Return a {@link List} of {@link BugListDefaultDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<BugListDefaultDTO> findByCriteria(BugListDefaultCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<BugListDefault> specification = createSpecification(criteria);
        return bugListDefaultMapper.toDto(bugListDefaultRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link BugListDefaultDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<BugListDefaultDTO> findByCriteria(BugListDefaultCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<BugListDefault> specification = createSpecification(criteria);
        final Page<BugListDefault> result = bugListDefaultRepository.findAll(specification, page);
        return result.map(bugListDefaultMapper::toDto);
    }

    /**
     * Function to convert BugListDefaultCriteria to a {@link Specifications}
     */
    private Specifications<BugListDefault> createSpecification(BugListDefaultCriteria criteria) {
        Specifications<BugListDefault> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), BugListDefault_.id));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), BugListDefault_.description));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), BugListDefault_.status));
            }
            if (criteria.getBusinessLineId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getBusinessLineId(), BugListDefault_.businessLine, BusinessLine_.id));
            }
        }
        return specification;
    }

}
