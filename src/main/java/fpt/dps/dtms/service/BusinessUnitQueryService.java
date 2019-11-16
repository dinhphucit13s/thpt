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

import fpt.dps.dtms.domain.BusinessUnit;
import fpt.dps.dtms.domain.*; // for static metamodels
import fpt.dps.dtms.repository.BusinessUnitRepository;
import fpt.dps.dtms.repository.search.BusinessUnitSearchRepository;
import fpt.dps.dtms.service.dto.BusinessUnitCriteria;

import fpt.dps.dtms.service.dto.BusinessUnitDTO;
import fpt.dps.dtms.service.dto.BusinessUnitMSCDTO;
import fpt.dps.dtms.service.mapper.BusinessUnitMSCMapper;
import fpt.dps.dtms.service.mapper.BusinessUnitMapper;

/**
 * Service for executing complex queries for BusinessUnit entities in the database.
 * The main input is a {@link BusinessUnitCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link BusinessUnitDTO} or a {@link Page} of {@link BusinessUnitDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class BusinessUnitQueryService extends QueryService<BusinessUnit> {

    private final Logger log = LoggerFactory.getLogger(BusinessUnitQueryService.class);


    private final BusinessUnitRepository businessUnitRepository;

    private final BusinessUnitMapper businessUnitMapper;
    
    private final BusinessUnitMSCMapper businessUnitMSCMapper;

    private final BusinessUnitSearchRepository businessUnitSearchRepository;

    public BusinessUnitQueryService(BusinessUnitRepository businessUnitRepository, BusinessUnitMapper businessUnitMapper, BusinessUnitSearchRepository businessUnitSearchRepository,
    		BusinessUnitMSCMapper businessUnitMSCMapper) {
        this.businessUnitRepository = businessUnitRepository;
        this.businessUnitMapper = businessUnitMapper;
        this.businessUnitSearchRepository = businessUnitSearchRepository;
        this.businessUnitMSCMapper = businessUnitMSCMapper;
    }

    /**
     * Return a {@link List} of {@link BusinessUnitDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<BusinessUnitDTO> findByCriteria(BusinessUnitCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<BusinessUnit> specification = createSpecification(criteria);
        return businessUnitMapper.toDto(businessUnitRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link BusinessUnitDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<BusinessUnitDTO> findByCriteria(BusinessUnitCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<BusinessUnit> specification = createSpecification(criteria);
        final Page<BusinessUnit> result = businessUnitRepository.findAll(specification, page);
        return result.map(businessUnitMapper::toDto);
    }

    /**
     * Function to convert BusinessUnitCriteria to a {@link Specifications}
     */
    private Specifications<BusinessUnit> createSpecification(BusinessUnitCriteria criteria) {
        Specifications<BusinessUnit> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), BusinessUnit_.id));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), BusinessUnit_.code));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), BusinessUnit_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), BusinessUnit_.description));
            }
        }
        return specification;
    }

	public List<BusinessUnitDTO> findAll() {
		List<BusinessUnit> businessUnits = this.businessUnitRepository.findAll();
		return this.businessUnitMapper.toDto(businessUnits);
	}
	
	public List<BusinessUnitMSCDTO> findAllForMSC() {
		List<BusinessUnit> businessUnits = this.businessUnitRepository.findAll();
		List<BusinessUnitMSCDTO> listBusinessUnitMSC = businessUnitMSCMapper.toDto(businessUnits);
		return this.businessUnitMSCMapper.toDto(businessUnits);
	}

	public List<BusinessUnitDTO> findListEffectBUByUserLogin(String userLogin) {
		LocalDate today = LocalDate.now();
		List<BusinessUnit> businessUnits = this.businessUnitRepository.findListEffectBUByUserLogin(userLogin.toLowerCase(), today);
		return this.businessUnitMapper.toDto(businessUnits);
	}

}
