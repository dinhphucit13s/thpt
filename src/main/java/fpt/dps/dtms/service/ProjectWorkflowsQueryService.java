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

import fpt.dps.dtms.domain.ProjectWorkflows;
import fpt.dps.dtms.domain.*; // for static metamodels
import fpt.dps.dtms.repository.ProjectWorkflowsRepository;
import fpt.dps.dtms.repository.search.ProjectWorkflowsSearchRepository;
import fpt.dps.dtms.service.dto.ProjectWorkflowsCriteria;

import fpt.dps.dtms.service.dto.ProjectWorkflowsDTO;
import fpt.dps.dtms.service.mapper.ProjectWorkflowsMapper;

/**
 * Service for executing complex queries for ProjectWorkflows entities in the database.
 * The main input is a {@link ProjectWorkflowsCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ProjectWorkflowsDTO} or a {@link Page} of {@link ProjectWorkflowsDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProjectWorkflowsQueryService extends QueryService<ProjectWorkflows> {

    private final Logger log = LoggerFactory.getLogger(ProjectWorkflowsQueryService.class);


    private final ProjectWorkflowsRepository projectWorkflowsRepository;

    private final ProjectWorkflowsMapper projectWorkflowsMapper;

    private final ProjectWorkflowsSearchRepository projectWorkflowsSearchRepository;

    public ProjectWorkflowsQueryService(ProjectWorkflowsRepository projectWorkflowsRepository, ProjectWorkflowsMapper projectWorkflowsMapper, ProjectWorkflowsSearchRepository projectWorkflowsSearchRepository) {
        this.projectWorkflowsRepository = projectWorkflowsRepository;
        this.projectWorkflowsMapper = projectWorkflowsMapper;
        this.projectWorkflowsSearchRepository = projectWorkflowsSearchRepository;
    }

    /**
     * Return a {@link List} of {@link ProjectWorkflowsDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ProjectWorkflowsDTO> findByCriteria(ProjectWorkflowsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<ProjectWorkflows> specification = createSpecification(criteria);
        return projectWorkflowsMapper.toDto(projectWorkflowsRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ProjectWorkflowsDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ProjectWorkflowsDTO> findByCriteria(ProjectWorkflowsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<ProjectWorkflows> specification = createSpecification(criteria);
        final Page<ProjectWorkflows> result = projectWorkflowsRepository.findAll(specification, page);
        return result.map(projectWorkflowsMapper::toDto);
    }

    /**
     * Function to convert ProjectWorkflowsCriteria to a {@link Specifications}
     */
    private Specifications<ProjectWorkflows> createSpecification(ProjectWorkflowsCriteria criteria) {
        Specifications<ProjectWorkflows> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), ProjectWorkflows_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), ProjectWorkflows_.name));
            }
            if (criteria.getStep() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStep(), ProjectWorkflows_.step));
            }
            if (criteria.getNextURI() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNextURI(), ProjectWorkflows_.nextURI));
            }
            if (criteria.getActivity() != null) {
                specification = specification.and(buildStringSpecification(criteria.getActivity(), ProjectWorkflows_.activity));
            }
            if (criteria.getProjectTemplatesId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getProjectTemplatesId(), ProjectWorkflows_.projectTemplates, ProjectTemplates_.id));
            }
        }
        return specification;
    }

}
