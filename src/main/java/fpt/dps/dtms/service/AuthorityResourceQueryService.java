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

import fpt.dps.dtms.domain.AuthorityResource;
import fpt.dps.dtms.domain.*; // for static metamodels
import fpt.dps.dtms.repository.AuthorityResourceRepository;
import fpt.dps.dtms.repository.search.AuthorityResourceSearchRepository;
import fpt.dps.dtms.service.dto.AuthorityResourceCriteria;

import fpt.dps.dtms.service.dto.AuthorityResourceDTO;
import fpt.dps.dtms.service.mapper.AuthorityResourceMapper;

/**
 * Service for executing complex queries for AuthorityResource entities in the database.
 * The main input is a {@link AuthorityResourceCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AuthorityResourceDTO} or a {@link Page} of {@link AuthorityResourceDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AuthorityResourceQueryService extends QueryService<AuthorityResource> {

    private final Logger log = LoggerFactory.getLogger(AuthorityResourceQueryService.class);


    private final AuthorityResourceRepository authorityResourceRepository;

    private final AuthorityResourceMapper authorityResourceMapper;

    private final AuthorityResourceSearchRepository authorityResourceSearchRepository;

    public AuthorityResourceQueryService(AuthorityResourceRepository authorityResourceRepository, AuthorityResourceMapper authorityResourceMapper, AuthorityResourceSearchRepository authorityResourceSearchRepository) {
        this.authorityResourceRepository = authorityResourceRepository;
        this.authorityResourceMapper = authorityResourceMapper;
        this.authorityResourceSearchRepository = authorityResourceSearchRepository;
    }

    /**
     * Return a {@link List} of {@link AuthorityResourceDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AuthorityResourceDTO> findByCriteria(AuthorityResourceCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<AuthorityResource> specification = createSpecification(criteria);
        return authorityResourceMapper.toDto(authorityResourceRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link AuthorityResourceDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AuthorityResourceDTO> findByCriteria(AuthorityResourceCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<AuthorityResource> specification = createSpecification(criteria);
        final Page<AuthorityResource> result = authorityResourceRepository.findAll(specification, page);
        return result.map(authorityResourceMapper::toDto);
    }

    /**
     * Function to convert AuthorityResourceCriteria to a {@link Specifications}
     */
    private Specifications<AuthorityResource> createSpecification(AuthorityResourceCriteria criteria) {
        Specifications<AuthorityResource> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), AuthorityResource_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), AuthorityResource_.name));
            }
            if (criteria.getPermission() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPermission(), AuthorityResource_.permission));
            }
            if (criteria.getAuthorityName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAuthorityName(), AuthorityResource_.authorityName));
            }
        }
        return specification;
    }

}
