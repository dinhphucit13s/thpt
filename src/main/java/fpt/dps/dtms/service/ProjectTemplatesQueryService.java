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

import fpt.dps.dtms.domain.ProjectTemplates;
import fpt.dps.dtms.domain.*; // for static metamodels
import fpt.dps.dtms.repository.ProjectTemplatesRepository;
import fpt.dps.dtms.repository.search.ProjectTemplatesSearchRepository;
import fpt.dps.dtms.service.dto.ProjectTemplatesCriteria;

import fpt.dps.dtms.service.dto.ProjectTemplatesDTO;
import fpt.dps.dtms.service.mapper.ProjectTemplatesMapper;

/**
 * Service for executing complex queries for ProjectTemplates entities in the database.
 * The main input is a {@link ProjectTemplatesCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ProjectTemplatesDTO} or a {@link Page} of {@link ProjectTemplatesDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProjectTemplatesQueryService extends QueryService<ProjectTemplates> {

    private final Logger log = LoggerFactory.getLogger(ProjectTemplatesQueryService.class);


    private final ProjectTemplatesRepository projectTemplatesRepository;

    private final ProjectTemplatesMapper projectTemplatesMapper;

    private final ProjectTemplatesSearchRepository projectTemplatesSearchRepository;

    public ProjectTemplatesQueryService(ProjectTemplatesRepository projectTemplatesRepository, ProjectTemplatesMapper projectTemplatesMapper, ProjectTemplatesSearchRepository projectTemplatesSearchRepository) {
        this.projectTemplatesRepository = projectTemplatesRepository;
        this.projectTemplatesMapper = projectTemplatesMapper;
        this.projectTemplatesSearchRepository = projectTemplatesSearchRepository;
    }

    /**
     * Return a {@link List} of {@link ProjectTemplatesDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ProjectTemplatesDTO> findByCriteria(ProjectTemplatesCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<ProjectTemplates> specification = createSpecification(criteria);
        return projectTemplatesMapper.toDto(projectTemplatesRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ProjectTemplatesDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ProjectTemplatesDTO> findByCriteria(ProjectTemplatesCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<ProjectTemplates> specification = createSpecification(criteria);
        final Page<ProjectTemplates> result = projectTemplatesRepository.findAll(specification, page);
        return result.map(projectTemplatesMapper::toDto);
    }
    
    /**
     * Function to convert ProjectTemplatesCriteria to a {@link Specifications}
     */
    private Specifications<ProjectTemplates> createSpecification(ProjectTemplatesCriteria criteria) {
        Specifications<ProjectTemplates> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), ProjectTemplates_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), ProjectTemplates_.name));
            }
            if (criteria.getBusinessLineId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getBusinessLineId(), ProjectTemplates_.businessLine, BusinessLine_.id));
            }
            if (criteria.getProjectId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getProjectId(), ProjectTemplates_.projects, Projects_.id));
            }
        }
        return specification;
    }

}
