package fpt.dps.dtms.service;


import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import fpt.dps.dtms.domain.TmsThread;
import fpt.dps.dtms.domain.*; // for static metamodels
import fpt.dps.dtms.repository.TmsThreadRepository;
import fpt.dps.dtms.repository.search.TmsThreadSearchRepository;
import fpt.dps.dtms.security.SecurityUtils;
import fpt.dps.dtms.service.dto.TmsThreadCriteria;

import fpt.dps.dtms.service.dto.TmsThreadDTO;
import fpt.dps.dtms.service.mapper.TmsThreadMapper;

/**
 * Service for executing complex queries for TmsThread entities in the database.
 * The main input is a {@link TmsThreadCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TmsThreadDTO} or a {@link Page} of {@link TmsThreadDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TmsThreadQueryService extends QueryService<TmsThread> {

    private final Logger log = LoggerFactory.getLogger(TmsThreadQueryService.class);


    private final TmsThreadRepository tmsThreadRepository;

    private final TmsThreadMapper tmsThreadMapper;

    private final TmsThreadSearchRepository tmsThreadSearchRepository;

    public TmsThreadQueryService(TmsThreadRepository tmsThreadRepository, TmsThreadMapper tmsThreadMapper, TmsThreadSearchRepository tmsThreadSearchRepository) {
        this.tmsThreadRepository = tmsThreadRepository;
        this.tmsThreadMapper = tmsThreadMapper;
        this.tmsThreadSearchRepository = tmsThreadSearchRepository;
    }

    /**
     * Return a {@link List} of {@link TmsThreadDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TmsThreadDTO> findByCriteria(TmsThreadCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<TmsThread> specification = createSpecification(criteria);
        return tmsThreadMapper.toDto(tmsThreadRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link TmsThreadDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TmsThreadDTO> findByCriteria(TmsThreadCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<TmsThread> specification = createSpecification(criteria);
        final Page<TmsThread> result = tmsThreadRepository.findAll(specification, page);
        return result.map(tmsThreadMapper::toDto);
    }
    
    public Page<TmsThreadDTO> findQuestionAndAnswer(Long projectId, String filter, Pageable page) {
    	String currentUserLogin = SecurityUtils.getCurrentUserLogin().get().toLowerCase();
    	Page<TmsThread> result = this.tmsThreadRepository.findQuestionAndAnswer(projectId, filter, currentUserLogin, page);
    	return result.map(tmsThreadMapper::toDto);
    }

    /**
     * Function to convert TmsThreadCriteria to a {@link Specifications}
     */
    private Specifications<TmsThread> createSpecification(TmsThreadCriteria criteria) {
        Specifications<TmsThread> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), TmsThread_.id));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), TmsThread_.title));
            }
            if (criteria.getViews() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getViews(), TmsThread_.views));
            }
            if (criteria.getAnswers() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAnswers(), TmsThread_.answers));
            }
            if (criteria.getClosed() != null) {
                specification = specification.and(buildSpecification(criteria.getClosed(), TmsThread_.closed));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), TmsThread_.status));
            }
            if (criteria.getProjectsId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getProjectsId(), TmsThread_.projects, Projects_.id));
            }
            if (criteria.getAssigneeId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getAssigneeId(), TmsThread_.assignee, ProjectUsers_.id));
            }
        }
        return specification;
    }
    
}
