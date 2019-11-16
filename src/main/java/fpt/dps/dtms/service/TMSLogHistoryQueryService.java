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

import fpt.dps.dtms.domain.TMSLogHistory;
import fpt.dps.dtms.domain.*; // for static metamodels
import fpt.dps.dtms.repository.TMSLogHistoryRepository;
import fpt.dps.dtms.repository.search.TMSLogHistorySearchRepository;
import fpt.dps.dtms.service.dto.TMSLogHistoryCriteria;

import fpt.dps.dtms.service.dto.TMSLogHistoryDTO;
import fpt.dps.dtms.service.mapper.TMSLogHistoryMapper;

/**
 * Service for executing complex queries for TMSLogHistory entities in the database.
 * The main input is a {@link TMSLogHistoryCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TMSLogHistoryDTO} or a {@link Page} of {@link TMSLogHistoryDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TMSLogHistoryQueryService extends QueryService<TMSLogHistory> {

    private final Logger log = LoggerFactory.getLogger(TMSLogHistoryQueryService.class);


    private final TMSLogHistoryRepository tMSLogHistoryRepository;

    private final TMSLogHistoryMapper tMSLogHistoryMapper;

    private final TMSLogHistorySearchRepository tMSLogHistorySearchRepository;

    public TMSLogHistoryQueryService(TMSLogHistoryRepository tMSLogHistoryRepository, TMSLogHistoryMapper tMSLogHistoryMapper, TMSLogHistorySearchRepository tMSLogHistorySearchRepository) {
        this.tMSLogHistoryRepository = tMSLogHistoryRepository;
        this.tMSLogHistoryMapper = tMSLogHistoryMapper;
        this.tMSLogHistorySearchRepository = tMSLogHistorySearchRepository;
    }

    /**
     * Return a {@link List} of {@link TMSLogHistoryDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TMSLogHistoryDTO> findByCriteria(TMSLogHistoryCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<TMSLogHistory> specification = createSpecification(criteria);
        return tMSLogHistoryMapper.toDto(tMSLogHistoryRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link TMSLogHistoryDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TMSLogHistoryDTO> findByCriteria(TMSLogHistoryCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<TMSLogHistory> specification = createSpecification(criteria);
        final Page<TMSLogHistory> result = tMSLogHistoryRepository.findAll(specification, page);
        return result.map(tMSLogHistoryMapper::toDto);
    }

    /**
     * Function to convert TMSLogHistoryCriteria to a {@link Specifications}
     */
    private Specifications<TMSLogHistory> createSpecification(TMSLogHistoryCriteria criteria) {
        Specifications<TMSLogHistory> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), TMSLogHistory_.id));
            }
            if (criteria.getAction() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAction(), TMSLogHistory_.action));
            }
            if (criteria.getProjectsId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getProjectsId(), TMSLogHistory_.projects, Projects_.id));
            }
            if (criteria.getPurchaseOrdersId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getPurchaseOrdersId(), TMSLogHistory_.purchaseOrders, PurchaseOrders_.id));
            }
            if (criteria.getPackagesId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getPackagesId(), TMSLogHistory_.packages, Packages_.id));
            }
            if (criteria.getTasksId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getTasksId(), TMSLogHistory_.tasks, Tasks_.id));
            }
        }
        return specification;
    }

}
