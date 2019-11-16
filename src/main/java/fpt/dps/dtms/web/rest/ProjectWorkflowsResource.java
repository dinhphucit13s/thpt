package fpt.dps.dtms.web.rest;

import com.codahale.metrics.annotation.Timed;
import fpt.dps.dtms.service.ProjectWorkflowsService;
import fpt.dps.dtms.web.rest.errors.BadRequestAlertException;
import fpt.dps.dtms.web.rest.util.HeaderUtil;
import fpt.dps.dtms.web.rest.util.PaginationUtil;
import fpt.dps.dtms.web.rest.vm.TaskWorkflowVM;
import fpt.dps.dtms.service.dto.ProjectWorkflowsDTO;
import fpt.dps.dtms.service.dto.ProjectWorkflowsCriteria;
import fpt.dps.dtms.service.ProjectWorkflowsQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing ProjectWorkflows.
 */
@RestController
@RequestMapping("/api")
public class ProjectWorkflowsResource {

    private final Logger log = LoggerFactory.getLogger(ProjectWorkflowsResource.class);

    private static final String ENTITY_NAME = "projectWorkflows";

    private final ProjectWorkflowsService projectWorkflowsService;

    private final ProjectWorkflowsQueryService projectWorkflowsQueryService;

    public ProjectWorkflowsResource(ProjectWorkflowsService projectWorkflowsService, ProjectWorkflowsQueryService projectWorkflowsQueryService) {
        this.projectWorkflowsService = projectWorkflowsService;
        this.projectWorkflowsQueryService = projectWorkflowsQueryService;
    }

    /**
     * POST  /project-workflows : Create a new projectWorkflows.
     *
     * @param projectWorkflowsDTO the projectWorkflowsDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new projectWorkflowsDTO, or with status 400 (Bad Request) if the projectWorkflows has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/project-workflows")
    @Timed
    @PreAuthorize("@jhiAuth.hasPermission('PROJECT_WORK_FLOW', 'CREATE')")
    public ResponseEntity<List<ProjectWorkflowsDTO>> createProjectWorkflows(@Valid @RequestBody List<ProjectWorkflowsDTO> projectWorkflowsDTOs) throws URISyntaxException {
        log.debug("REST request to save ProjectWorkflows : {}", projectWorkflowsDTOs);
        List<ProjectWorkflowsDTO> result = new ArrayList<>();
        for (ProjectWorkflowsDTO projectWorkflowsDTO : projectWorkflowsDTOs) {
        	if (projectWorkflowsDTO.getId() != null) {
                throw new BadRequestAlertException("A new projectWorkflows cannot already have an ID", ENTITY_NAME, "idexists");
            }
            result.add(projectWorkflowsService.save(projectWorkflowsDTO));
		}

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.get(0).getId().toString()))
            .body(result);
    }

    /**
     * PUT  /project-workflows : Updates an existing projectWorkflows.
     *
     * @param projectWorkflowsDTO the projectWorkflowsDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated projectWorkflowsDTO,
     * or with status 400 (Bad Request) if the projectWorkflowsDTO is not valid,
     * or with status 500 (Internal Server Error) if the projectWorkflowsDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/project-workflows")
    @Timed
    @PreAuthorize("@jhiAuth.hasPermission('PROJECT_WORK_FLOW', 'EDIT')")
    public ResponseEntity<List<ProjectWorkflowsDTO>> updateProjectWorkflows(@Valid @RequestBody List<ProjectWorkflowsDTO> projectWorkflowsDTOs) throws URISyntaxException {
        log.debug("REST request to update ProjectWorkflows : {}", projectWorkflowsDTOs);
        List<ProjectWorkflowsDTO> result = new ArrayList<>();
        for (ProjectWorkflowsDTO projectWorkflowsDTO : projectWorkflowsDTOs) {
        	if (projectWorkflowsDTO.getId() == null) {
        		return createProjectWorkflows(projectWorkflowsDTOs);
            }
            result.add(projectWorkflowsService.save(projectWorkflowsDTO));
		}
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, result.get(0).getId().toString()))
            .body(result);
    }

    /**
     * GET  /project-workflows : get all the projectWorkflows.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of projectWorkflows in body
     */
    @GetMapping("/project-workflows")
    @Timed
    @PreAuthorize("@jhiAuth.hasPermission('PROJECT_WORK_FLOW', 'VIEW')")
    public ResponseEntity<List<ProjectWorkflowsDTO>> getAllProjectWorkflows(ProjectWorkflowsCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ProjectWorkflows by criteria: {}", criteria);
        Page<ProjectWorkflowsDTO> page = projectWorkflowsQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/project-workflows");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /project-workflows/:id : get the "id" projectWorkflows.
     *
     * @param id the id of the projectWorkflowsDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the projectWorkflowsDTO, or with status 404 (Not Found)
     */
    @GetMapping("/project-workflows/{id}")
    @Timed
    @PreAuthorize("@jhiAuth.hasPermission('PROJECT_WORK_FLOW', 'VIEW')")
    public ResponseEntity<ProjectWorkflowsDTO> getProjectWorkflows(@PathVariable Long id) {
        log.debug("REST request to get ProjectWorkflows : {}", id);
        ProjectWorkflowsDTO projectWorkflowsDTO = projectWorkflowsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(projectWorkflowsDTO));
    }

    /**
     * Get all project-workflow by template Id.
     * @param templateId
     * @param pageable
     * @return
     * @author  ThuyCA
     */

    @GetMapping("/project-workflows-by-template/{templateId}")
    @Timed
    @PreAuthorize("@jhiAuth.hasPermission('PROJECT_WORK_FLOW', 'VIEW')")
    public ResponseEntity<List<ProjectWorkflowsDTO>> getAllProjectWorkflowsByTemplateId(@PathVariable Long templateId, Pageable pageable) {
        log.debug("REST request to get ProjectWorkflows by templateID: {}", templateId);
        Page<ProjectWorkflowsDTO> page = projectWorkflowsService.findByProjectTemplatesId(templateId, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "api/project-workflows-by-template");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * Get all deployed processes
     * @return
     */

    @GetMapping("/project-workflows-processes")
    @Timed
    @PreAuthorize("@jhiAuth.hasPermission('PROJECT_WORK_FLOW', 'VIEW')")
    public ResponseEntity<List<TaskWorkflowVM>> getAllProcessesTemplate() {
        log.debug("REST request to get all process template");
        List<TaskWorkflowVM> taskWorkflowVMs = null;
    	try {
			taskWorkflowVMs = projectWorkflowsService.getDeploymentResources();
		} catch (Exception e) {
			e.printStackTrace();
		}
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(taskWorkflowVMs));
    }

    /**
     * DELETE  /project-workflows/:id : delete the "id" projectWorkflows.
     *
     * @param id the id of the projectWorkflowsDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/project-workflows/{id}")
    @Timed
    @PreAuthorize("@jhiAuth.hasPermission('PROJECT_WORK_FLOW', 'DELETE')")
    public ResponseEntity<Void> deleteProjectWorkflows(@PathVariable Long id) {
        log.debug("REST request to delete ProjectWorkflows : {}", id);
        projectWorkflowsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/project-workflows?query=:query : search for the projectWorkflows corresponding
     * to the query.
     *
     * @param query the query of the projectWorkflows search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/project-workflows")
    @Timed
    @PreAuthorize("@jhiAuth.hasPermission('PROJECT_WORK_FLOW', 'VIEW')")
    public ResponseEntity<List<ProjectWorkflowsDTO>> searchProjectWorkflows(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of ProjectWorkflows for query {}", query);
        Page<ProjectWorkflowsDTO> page = projectWorkflowsService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/project-workflows");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
