package fpt.dps.dtms.service;


import java.time.Instant;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import fpt.dps.dtms.domain.LoginTracking;
import fpt.dps.dtms.domain.*; // for static metamodels
import fpt.dps.dtms.repository.LoginTrackingRepository;
import fpt.dps.dtms.repository.search.LoginTrackingSearchRepository;
import fpt.dps.dtms.service.dto.LoginTrackingCriteria;

import fpt.dps.dtms.service.dto.LoginTrackingDTO;
import fpt.dps.dtms.service.mapper.LoginTrackingMapper;

/**
 * Service for executing complex queries for LoginTracking entities in the database.
 * The main input is a {@link LoginTrackingCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link LoginTrackingDTO} or a {@link Page} of {@link LoginTrackingDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LoginTrackingQueryService extends QueryService<LoginTracking> {

    private final Logger log = LoggerFactory.getLogger(LoginTrackingQueryService.class);


    private final LoginTrackingRepository loginTrackingRepository;

    private final LoginTrackingMapper loginTrackingMapper;

    private final LoginTrackingSearchRepository loginTrackingSearchRepository;

    public LoginTrackingQueryService(LoginTrackingRepository loginTrackingRepository, LoginTrackingMapper loginTrackingMapper, LoginTrackingSearchRepository loginTrackingSearchRepository) {
        this.loginTrackingRepository = loginTrackingRepository;
        this.loginTrackingMapper = loginTrackingMapper;
        this.loginTrackingSearchRepository = loginTrackingSearchRepository;
    }

    /**
     * Return a {@link List} of {@link LoginTrackingDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<LoginTrackingDTO> findByCriteria(LoginTrackingCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<LoginTracking> specification = createSpecification(criteria);
        return loginTrackingMapper.toDto(loginTrackingRepository.findAll(specification));
    }
    
    /**
     * Get Time login by user
     *
     * @param userLogin the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public LoginTrackingDTO getTimeLoginByUser(String userLogin, Instant date) {
        log.debug("Request to get StartTime LoginTracking : {}", userLogin);
        LoginTracking loginTrackingStart = loginTrackingRepository.getStartUser(userLogin, date);
        return loginTrackingMapper.toDto(loginTrackingStart);
    }
    
    /**
     * Return a {@link Page} of {@link LoginTrackingDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<LoginTrackingDTO> findByCriteria(LoginTrackingCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<LoginTracking> specification = createSpecification(criteria);
        final Page<LoginTracking> result = loginTrackingRepository.findAll(specification, page);
        return result.map(loginTrackingMapper::toDto);
    }

    /**
     * Function to convert LoginTrackingCriteria to a {@link Specifications}
     */
    private Specifications<LoginTracking> createSpecification(LoginTrackingCriteria criteria) {
        Specifications<LoginTracking> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), LoginTracking_.id));
            }
            if (criteria.getLogin() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLogin(), LoginTracking_.login));
            }
            if (criteria.getStartTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStartTime(), LoginTracking_.startTime));
            }
            if (criteria.getEndTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEndTime(), LoginTracking_.endTime));
            }
        }
        return specification;
    }

}
