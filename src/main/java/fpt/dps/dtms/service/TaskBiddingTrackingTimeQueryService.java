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

import fpt.dps.dtms.domain.TaskBiddingTrackingTime;
import fpt.dps.dtms.domain.*; // for static metamodels
import fpt.dps.dtms.repository.TaskBiddingTrackingTimeRepository;
import fpt.dps.dtms.repository.search.TaskBiddingTrackingTimeSearchRepository;
import fpt.dps.dtms.service.dto.TaskBiddingTrackingTimeCriteria;

import fpt.dps.dtms.service.dto.TaskBiddingTrackingTimeDTO;
import fpt.dps.dtms.service.mapper.TaskBiddingTrackingTimeMapper;
import fpt.dps.dtms.domain.enumeration.BiddingScope;

/**
 * Service for executing complex queries for TaskBiddingTrackingTime entities in the database.
 * The main input is a {@link TaskBiddingTrackingTimeCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TaskBiddingTrackingTimeDTO} or a {@link Page} of {@link TaskBiddingTrackingTimeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TaskBiddingTrackingTimeQueryService extends QueryService<TaskBiddingTrackingTime> {

    private final Logger log = LoggerFactory.getLogger(TaskBiddingTrackingTimeQueryService.class);


    private final TaskBiddingTrackingTimeRepository taskBiddingTrackingTimeRepository;

    private final TaskBiddingTrackingTimeMapper taskBiddingTrackingTimeMapper;

    private final TaskBiddingTrackingTimeSearchRepository taskBiddingTrackingTimeSearchRepository;

    public TaskBiddingTrackingTimeQueryService(TaskBiddingTrackingTimeRepository taskBiddingTrackingTimeRepository, TaskBiddingTrackingTimeMapper taskBiddingTrackingTimeMapper, TaskBiddingTrackingTimeSearchRepository taskBiddingTrackingTimeSearchRepository) {
        this.taskBiddingTrackingTimeRepository = taskBiddingTrackingTimeRepository;
        this.taskBiddingTrackingTimeMapper = taskBiddingTrackingTimeMapper;
        this.taskBiddingTrackingTimeSearchRepository = taskBiddingTrackingTimeSearchRepository;
    }

    /**
     * Return a {@link List} of {@link TaskBiddingTrackingTimeDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TaskBiddingTrackingTimeDTO> findByCriteria(TaskBiddingTrackingTimeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<TaskBiddingTrackingTime> specification = createSpecification(criteria);
        return taskBiddingTrackingTimeMapper.toDto(taskBiddingTrackingTimeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link TaskBiddingTrackingTimeDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TaskBiddingTrackingTimeDTO> findByCriteria(TaskBiddingTrackingTimeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<TaskBiddingTrackingTime> specification = createSpecification(criteria);
        final Page<TaskBiddingTrackingTime> result = taskBiddingTrackingTimeRepository.findAll(specification, page);
        return result.map(taskBiddingTrackingTimeMapper::toDto);
    }

    /**
     * Function to convert TaskBiddingTrackingTimeCriteria to a {@link Specifications}
     */
    private Specifications<TaskBiddingTrackingTime> createSpecification(TaskBiddingTrackingTimeCriteria criteria) {
        Specifications<TaskBiddingTrackingTime> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), TaskBiddingTrackingTime_.id));
            }
            if (criteria.getUserLogin() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUserLogin(), TaskBiddingTrackingTime_.userLogin));
            }
            if (criteria.getRole() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRole(), TaskBiddingTrackingTime_.role));
            }
            if (criteria.getStartTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStartTime(), TaskBiddingTrackingTime_.startTime));
            }
            if (criteria.getEndTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEndTime(), TaskBiddingTrackingTime_.endTime));
            }
            if (criteria.getStartStatus() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStartStatus(), TaskBiddingTrackingTime_.startStatus));
            }
            if (criteria.getEndStatus() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEndStatus(), TaskBiddingTrackingTime_.endStatus));
            }
            if (criteria.getDuration() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDuration(), TaskBiddingTrackingTime_.duration));
            }
            if (criteria.getBiddingScope() != null) {
                specification = specification.and(buildSpecification(criteria.getBiddingScope(), TaskBiddingTrackingTime_.biddingScope));
            }
            if (criteria.getTaskId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getTaskId(), TaskBiddingTrackingTime_.task, Tasks_.id));
            }
        }
        return specification;
    }

}
