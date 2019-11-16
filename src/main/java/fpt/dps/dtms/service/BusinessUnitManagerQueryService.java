package fpt.dps.dtms.service;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import fpt.dps.dtms.domain.BusinessUnitManager;
import fpt.dps.dtms.domain.*; // for static metamodels
import fpt.dps.dtms.repository.BusinessUnitManagerRepository;
import fpt.dps.dtms.repository.search.BusinessUnitManagerSearchRepository;
import fpt.dps.dtms.service.dto.BusinessUnitManagerCriteria;

import fpt.dps.dtms.service.dto.BusinessUnitManagerDTO;
import fpt.dps.dtms.service.mapper.BusinessUnitManagerMapper;

/**
 * Service for executing complex queries for BusinessUnitManager entities in the database.
 * The main input is a {@link BusinessUnitManagerCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link BusinessUnitManagerDTO} or a {@link Page} of {@link BusinessUnitManagerDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class BusinessUnitManagerQueryService extends QueryService<BusinessUnitManager> {

    private final Logger log = LoggerFactory.getLogger(BusinessUnitManagerQueryService.class);


    private final BusinessUnitManagerRepository businessUnitManagerRepository;

    private final BusinessUnitManagerMapper businessUnitManagerMapper;

    private final BusinessUnitManagerSearchRepository businessUnitManagerSearchRepository;

    public BusinessUnitManagerQueryService(BusinessUnitManagerRepository businessUnitManagerRepository, BusinessUnitManagerMapper businessUnitManagerMapper, BusinessUnitManagerSearchRepository businessUnitManagerSearchRepository) {
        this.businessUnitManagerRepository = businessUnitManagerRepository;
        this.businessUnitManagerMapper = businessUnitManagerMapper;
        this.businessUnitManagerSearchRepository = businessUnitManagerSearchRepository;
    }

    /**
     * Return a {@link List} of {@link BusinessUnitManagerDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<BusinessUnitManagerDTO> findByCriteria(BusinessUnitManagerCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<BusinessUnitManager> specification = createSpecification(criteria);
        return businessUnitManagerMapper.toDto(businessUnitManagerRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link BusinessUnitManagerDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<BusinessUnitManagerDTO> findByCriteria(BusinessUnitManagerCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<BusinessUnitManager> specification = createSpecification(criteria);
        final Page<BusinessUnitManager> result = businessUnitManagerRepository.findAll(specification, page);
        return result.map(businessUnitManagerMapper::toDto);
    }

    /**
     * Function to convert BusinessUnitManagerCriteria to a {@link Specifications}
     */
    private Specifications<BusinessUnitManager> createSpecification(BusinessUnitManagerCriteria criteria) {
        Specifications<BusinessUnitManager> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), BusinessUnitManager_.id));
            }
            if (criteria.getStartTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStartTime(), BusinessUnitManager_.startTime));
            }
            if (criteria.getEndTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEndTime(), BusinessUnitManager_.endTime));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), BusinessUnitManager_.description));
            }
            if (criteria.getBusinessUnitId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getBusinessUnitId(), BusinessUnitManager_.businessUnit, BusinessUnit_.id));
            }
            if (criteria.getManagerId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getManagerId(), BusinessUnitManager_.manager, User_.id));
            }
        }
        return specification;
    }

}
