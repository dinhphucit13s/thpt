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


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing ProjectBugListDefault.
 */
@Service
@Transactional
public class ProjectBugListDefaultService {

    private final Logger log = LoggerFactory.getLogger(ProjectBugListDefaultService.class);

    private final ProjectBugListDefaultRepository projectBugListDefaultRepository;

    private final ProjectBugListDefaultMapper projectBugListDefaultMapper;

    private final ProjectBugListDefaultSearchRepository projectBugListDefaultSearchRepository;

    public ProjectBugListDefaultService(ProjectBugListDefaultRepository projectBugListDefaultRepository, ProjectBugListDefaultMapper projectBugListDefaultMapper, ProjectBugListDefaultSearchRepository projectBugListDefaultSearchRepository) {
        this.projectBugListDefaultRepository = projectBugListDefaultRepository;
        this.projectBugListDefaultMapper = projectBugListDefaultMapper;
        this.projectBugListDefaultSearchRepository = projectBugListDefaultSearchRepository;
    }

    /**
     * Save a projectBugListDefault.
     *
     * @param projectBugListDefaultDTO the entity to save
     * @return the persisted entity
     */
    public ProjectBugListDefaultDTO save(ProjectBugListDefaultDTO projectBugListDefaultDTO) {
        log.debug("Request to save ProjectBugListDefault : {}", projectBugListDefaultDTO);
        ProjectBugListDefault projectBugListDefault = projectBugListDefaultMapper.toEntity(projectBugListDefaultDTO);
        projectBugListDefault = projectBugListDefaultRepository.save(projectBugListDefault);
        ProjectBugListDefaultDTO result = projectBugListDefaultMapper.toDto(projectBugListDefault);
        projectBugListDefaultSearchRepository.save(projectBugListDefault);
        return result;
    }

    /**
     * Get all the projectBugListDefaults.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ProjectBugListDefaultDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ProjectBugListDefaults");
        return projectBugListDefaultRepository.findAll(pageable)
            .map(projectBugListDefaultMapper::toDto);
    }

    /**
     * Get one projectBugListDefault by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public ProjectBugListDefaultDTO findOne(Long id) {
        log.debug("Request to get ProjectBugListDefault : {}", id);
        ProjectBugListDefault projectBugListDefault = projectBugListDefaultRepository.findOne(id);
        return projectBugListDefaultMapper.toDto(projectBugListDefault);
    }

    /**
     * Delete the projectBugListDefault by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ProjectBugListDefault : {}", id);
        projectBugListDefaultRepository.delete(id);
        projectBugListDefaultSearchRepository.delete(id);
    }

    /**
     * Search for the projectBugListDefault corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ProjectBugListDefaultDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ProjectBugListDefaults for query {}", query);
        Page<ProjectBugListDefault> result = projectBugListDefaultSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(projectBugListDefaultMapper::toDto);
    }
}
