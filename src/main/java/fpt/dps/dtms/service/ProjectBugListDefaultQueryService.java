package fpt.dps.dtms.service;

import fpt.dps.dtms.domain.ProjectBugListDefault;
import fpt.dps.dtms.repository.ProjectBugListDefaultRepository;
import fpt.dps.dtms.repository.search.ProjectBugListDefaultSearchRepository;
import fpt.dps.dtms.service.dto.ProjectBugListDefaultDTO;
import fpt.dps.dtms.service.mapper.ProjectBugListDefaultMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;
import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing ProjectBugListDefault.
 */
@Service
@Transactional(readOnly = true)
public class ProjectBugListDefaultQueryService extends QueryService<ProjectBugListDefault>{

    private final Logger log = LoggerFactory.getLogger(ProjectBugListDefaultService.class);

    private final ProjectBugListDefaultRepository projectBugListDefaultRepository;

    private final ProjectBugListDefaultMapper projectBugListDefaultMapper;

    private final ProjectBugListDefaultSearchRepository projectBugListDefaultSearchRepository;

    public ProjectBugListDefaultQueryService(ProjectBugListDefaultRepository projectBugListDefaultRepository, ProjectBugListDefaultMapper projectBugListDefaultMapper, ProjectBugListDefaultSearchRepository projectBugListDefaultSearchRepository) {
        this.projectBugListDefaultRepository = projectBugListDefaultRepository;
        this.projectBugListDefaultMapper = projectBugListDefaultMapper;
        this.projectBugListDefaultSearchRepository = projectBugListDefaultSearchRepository;
    }

    /**
     * get all the projectBugListDefaults by project ID.
     *
     * @param projectBugListDefaultDTO the entity to save
     * @return the persisted entity
     */
    @Transactional(readOnly = true)
    public Page<ProjectBugListDefaultDTO> getAllProjectBugListDefaultsByProjectId(Pageable page, Long projectId) {
        log.debug("find by projectID");
        return projectBugListDefaultRepository.getAllProjectBugListDefaultsByProjectId(page, projectId)
        		.map(projectBugListDefaultMapper::toDto);
    }
}
