package fpt.dps.dtms.service;

import fpt.dps.dtms.domain.ProjectWorkflows;
import fpt.dps.dtms.repository.ProjectWorkflowsRepository;
import fpt.dps.dtms.repository.search.ProjectWorkflowsSearchRepository;
import fpt.dps.dtms.service.dto.ProjectWorkflowsDTO;
import fpt.dps.dtms.service.dto.TMSCustomFieldDTO;
import fpt.dps.dtms.service.dto.TMSCustomFieldScreenDTO;
import fpt.dps.dtms.service.mapper.ProjectWorkflowsMapper;
import fpt.dps.dtms.service.util.ActivitiService;
import fpt.dps.dtms.web.rest.vm.TaskWorkflowVM;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

import java.util.List;

/**
 * Service Implementation for managing ProjectWorkflows.
 */
@Service
@Transactional
public class ProjectWorkflowsService {

    private final Logger log = LoggerFactory.getLogger(ProjectWorkflowsService.class);

    private final ProjectWorkflowsRepository projectWorkflowsRepository;

    private final ProjectWorkflowsMapper projectWorkflowsMapper;

    private final ProjectWorkflowsSearchRepository projectWorkflowsSearchRepository;

    private final ActivitiService activitiService;

    private final TMSCustomFieldService tMSCustomFieldService;

    private final TMSCustomFieldQueryService tMSCustomFieldQueryService;

    private final TMSCustomFieldScreenService tMSCustomFieldScreenService;

    private final TMSCustomFieldScreenQueryService tMSCustomFieldScreenQueryService;

    public ProjectWorkflowsService(ProjectWorkflowsRepository projectWorkflowsRepository, ProjectWorkflowsMapper projectWorkflowsMapper, ProjectWorkflowsSearchRepository projectWorkflowsSearchRepository, ActivitiService activitiService,
    		TMSCustomFieldService tMSCustomFieldService, TMSCustomFieldScreenService tMSCustomFieldScreenService, TMSCustomFieldScreenQueryService tMSCustomFieldScreenQueryService, TMSCustomFieldQueryService tMSCustomFieldQueryService) {
        this.projectWorkflowsRepository = projectWorkflowsRepository;
        this.projectWorkflowsMapper = projectWorkflowsMapper;
        this.projectWorkflowsSearchRepository = projectWorkflowsSearchRepository;
        this.activitiService = activitiService;
        this.tMSCustomFieldService = tMSCustomFieldService;
        this.tMSCustomFieldScreenService = tMSCustomFieldScreenService;
        this.tMSCustomFieldScreenQueryService = tMSCustomFieldScreenQueryService;
        this.tMSCustomFieldQueryService = tMSCustomFieldQueryService;
    }

    /**
     * Save a projectWorkflows.
     *
     * @param projectWorkflowsDTO the entity to save
     * @return the persisted entity
     */
    public ProjectWorkflowsDTO save(ProjectWorkflowsDTO projectWorkflowsDTO) {
        log.debug("Request to save ProjectWorkflows : {}", projectWorkflowsDTO);
        ProjectWorkflows projectWorkflows = projectWorkflowsMapper.toEntity(projectWorkflowsDTO);
        projectWorkflows = projectWorkflowsRepository.save(projectWorkflows);
        if (projectWorkflowsDTO.getTmsCustomFields().size() > 0) {
        	for (TMSCustomFieldScreenDTO tmsCustomFieldScreenDTO: projectWorkflowsDTO.getTmsCustomFields()) {
        		if (tmsCustomFieldScreenDTO.getId() != null) {
        			tMSCustomFieldScreenService.save(tmsCustomFieldScreenDTO);
        		} else {
        			tmsCustomFieldScreenDTO.setProjectWorkflowsId(projectWorkflows.getId());
        			tmsCustomFieldScreenDTO.setProjectWorkflowsName(projectWorkflows.getName());
        			tMSCustomFieldScreenService.save(tmsCustomFieldScreenDTO);
        		}
        	}
        }
        ProjectWorkflowsDTO result = projectWorkflowsMapper.toDto(projectWorkflows);
        projectWorkflowsSearchRepository.save(projectWorkflows);
        return result;
    }

    /**
     * Get all the projectWorkflows.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ProjectWorkflowsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ProjectWorkflows");
        return projectWorkflowsRepository.findAll(pageable)
            .map(projectWorkflowsMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<ProjectWorkflowsDTO> findByProjectTemplatesId(Long projectTemplatesId, Pageable pageable) {
        log.debug("Request to get all ProjectWorkflows");
        Page<ProjectWorkflowsDTO> result = projectWorkflowsRepository.findByProjectTemplatesId(projectTemplatesId, pageable)
            .map(projectWorkflowsMapper::toDto);
        List<ProjectWorkflowsDTO> listProjectWorkflows = result.getContent();
        for (ProjectWorkflowsDTO wf: listProjectWorkflows) {
        	List<TMSCustomFieldScreenDTO> tmsCustomFieldScreenDTOs = tMSCustomFieldScreenQueryService.getListTMSCustomFieldScreenByWFId(wf.getId());
        	wf.setTmsCustomFields(tmsCustomFieldScreenDTOs);
        }
        return result;
    }

    /**
     * Get one projectWorkflows by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public ProjectWorkflowsDTO findOne(Long id) {
        log.debug("Request to get ProjectWorkflows : {}", id);
        ProjectWorkflows projectWorkflows = projectWorkflowsRepository.findOne(id);
        return projectWorkflowsMapper.toDto(projectWorkflows);
    }

    /**
     * Delete the projectWorkflows by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ProjectWorkflows : {}", id);
        projectWorkflowsRepository.delete(id);
        projectWorkflowsSearchRepository.delete(id);
    }

    /**
     * Search for the projectWorkflows corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ProjectWorkflowsDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ProjectWorkflows for query {}", query);
        Page<ProjectWorkflows> result = projectWorkflowsSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(projectWorkflowsMapper::toDto);
    }

    //@Transactional(readOnly = true)
	public List<TaskWorkflowVM> getDeploymentResources() throws Exception {
		return activitiService.getDeploymentResources();
	}
}
