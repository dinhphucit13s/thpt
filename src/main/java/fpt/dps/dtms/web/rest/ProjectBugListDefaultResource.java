package fpt.dps.dtms.web.rest;

import com.codahale.metrics.annotation.Timed;
import fpt.dps.dtms.service.ProjectBugListDefaultService;
import fpt.dps.dtms.service.ProjectBugListDefaultQueryService;
import fpt.dps.dtms.web.rest.errors.BadRequestAlertException;
import fpt.dps.dtms.web.rest.util.HeaderUtil;
import fpt.dps.dtms.web.rest.util.PaginationUtil;
import fpt.dps.dtms.service.dto.ProjectBugListDefaultDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing ProjectBugListDefault.
 */
@RestController
@RequestMapping("/api")
public class ProjectBugListDefaultResource {

    private final Logger log = LoggerFactory.getLogger(ProjectBugListDefaultResource.class);

    private static final String ENTITY_NAME = "projectBugListDefault";

    private final ProjectBugListDefaultService projectBugListDefaultService;
    
    private final ProjectBugListDefaultQueryService projectBugListDefaultQueryService;

    public ProjectBugListDefaultResource(ProjectBugListDefaultService projectBugListDefaultService, ProjectBugListDefaultQueryService projectBugListDefaultQueryService) {
        this.projectBugListDefaultService = projectBugListDefaultService;
        this.projectBugListDefaultQueryService = projectBugListDefaultQueryService;
    }

    /**
     * POST  /project-bug-list-defaults : Create a new projectBugListDefault.
     *
     * @param projectBugListDefaultDTO the projectBugListDefaultDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new projectBugListDefaultDTO, or with status 400 (Bad Request) if the projectBugListDefault has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/project-bug-list-defaults")
    @Timed
    public ResponseEntity<List<ProjectBugListDefaultDTO>> createProjectBugListDefault(@Valid @RequestBody List<ProjectBugListDefaultDTO> projectBugListDefaultDTO) throws URISyntaxException {
        log.debug("REST request to save ProjectBugListDefault : {}", projectBugListDefaultDTO);
        List<ProjectBugListDefaultDTO> result = new ArrayList<>();
        for (ProjectBugListDefaultDTO projectBugList : projectBugListDefaultDTO) {
            if (projectBugList.getId() != null) {
                throw new BadRequestAlertException("A new projectUsers cannot already have an ID", ENTITY_NAME, "idexists");
            }
            result.add(projectBugListDefaultService.save(projectBugList));
        }
        return ResponseEntity.created(new URI("/api/project-bug-list-defaults/" + result.get(0).getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.get(0).getId().toString()))
            .body(result);
    }
    
    public ResponseEntity<ProjectBugListDefaultDTO> createProjectBugListDefault(@Valid @RequestBody ProjectBugListDefaultDTO projectBugListDefaultDTO) throws URISyntaxException {
        log.debug("REST request to save ProjectBugListDefault : {}", projectBugListDefaultDTO);
        if (projectBugListDefaultDTO.getId() != null) {
            throw new BadRequestAlertException("A new projectBugListDefault cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProjectBugListDefaultDTO result = projectBugListDefaultService.save(projectBugListDefaultDTO);
        return ResponseEntity.created(new URI("/api/project-bug-list-defaults/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }
    
    /**
     * PUT  /project-bug-list-defaults : Updates an existing projectBugListDefault.
     *
     * @param projectBugListDefaultDTO the projectBugListDefaultDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated projectBugListDefaultDTO,
     * or with status 400 (Bad Request) if the projectBugListDefaultDTO is not valid,
     * or with status 500 (Internal Server Error) if the projectBugListDefaultDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/project-bug-list-defaults")
    @Timed
    public ResponseEntity<ProjectBugListDefaultDTO> updateProjectBugListDefault(@Valid @RequestBody ProjectBugListDefaultDTO projectBugListDefaultDTO) throws URISyntaxException {
        log.debug("REST request to update ProjectBugListDefault : {}", projectBugListDefaultDTO);
        if (projectBugListDefaultDTO.getId() == null) {
            return createProjectBugListDefault(projectBugListDefaultDTO);
        }
        ProjectBugListDefaultDTO result = projectBugListDefaultService.save(projectBugListDefaultDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, projectBugListDefaultDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /project-bug-list-defaults : get all the projectBugListDefaults.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of projectBugListDefaults in body
     */
    @GetMapping("/project-bug-list-defaults")
    @Timed
    public ResponseEntity<List<ProjectBugListDefaultDTO>> getAllProjectBugListDefaults(Pageable pageable) {
        log.debug("REST request to get a page of ProjectBugListDefaults");
        Page<ProjectBugListDefaultDTO> page = projectBugListDefaultService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/project-bug-list-defaults");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    
    /**
     * GET  /project-bug-list-defaults : get all the projectBugListDefaults by project ID.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of projectBugListDefaults in body
     */
    @GetMapping("/project-bug-list-defaults-by-projectId")
    @Timed
    public ResponseEntity<List<ProjectBugListDefaultDTO>> getAllProjectBugListDefaultsByProjectId(Pageable pageable, 
    		@RequestParam(value = "projectId", required = false) Long projectId) {
        log.debug("REST request to get a page of ProjectBugListDefaults");
        Page<ProjectBugListDefaultDTO> page = projectBugListDefaultQueryService.getAllProjectBugListDefaultsByProjectId(pageable, projectId);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/project-bug-list-defaults-by-projectId");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /project-bug-list-defaults/:id : get the "id" projectBugListDefault.
     *
     * @param id the id of the projectBugListDefaultDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the projectBugListDefaultDTO, or with status 404 (Not Found)
     */
    @GetMapping("/project-bug-list-defaults/{id}")
    @Timed
    public ResponseEntity<ProjectBugListDefaultDTO> getProjectBugListDefault(@PathVariable Long id) {
        log.debug("REST request to get ProjectBugListDefault : {}", id);
        ProjectBugListDefaultDTO projectBugListDefaultDTO = projectBugListDefaultService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(projectBugListDefaultDTO));
    }

    /**
     * DELETE  /project-bug-list-defaults/:id : delete the "id" projectBugListDefault.
     *
     * @param id the id of the projectBugListDefaultDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/project-bug-list-defaults/{id}")
    @Timed
    public ResponseEntity<Void> deleteProjectBugListDefault(@PathVariable Long id) {
        log.debug("REST request to delete ProjectBugListDefault : {}", id);
        projectBugListDefaultService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/project-bug-list-defaults?query=:query : search for the projectBugListDefault corresponding
     * to the query.
     *
     * @param query the query of the projectBugListDefault search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/project-bug-list-defaults")
    @Timed
    public ResponseEntity<List<ProjectBugListDefaultDTO>> searchProjectBugListDefaults(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of ProjectBugListDefaults for query {}", query);
        Page<ProjectBugListDefaultDTO> page = projectBugListDefaultService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/project-bug-list-defaults");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
