package fpt.dps.dtms.web.rest;

import com.codahale.metrics.annotation.Timed;
import fpt.dps.dtms.service.ProjectTemplatesService;
import fpt.dps.dtms.web.rest.errors.BadRequestAlertException;
import fpt.dps.dtms.web.rest.util.HeaderUtil;
import fpt.dps.dtms.web.rest.util.PaginationUtil;
import fpt.dps.dtms.service.dto.ProjectTemplatesDTO;
import fpt.dps.dtms.service.util.AppConstants;
import fpt.dps.dtms.service.dto.ProjectTemplatesCriteria;
import fpt.dps.dtms.service.ProjectTemplatesQueryService;
import io.github.jhipster.web.util.ResponseUtil;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing ProjectTemplates.
 */
@RestController
@RequestMapping("/api")
public class ProjectTemplatesResource {

    private final Logger log = LoggerFactory.getLogger(ProjectTemplatesResource.class);

    private static final String ENTITY_NAME = "projectTemplates";

    private final ProjectTemplatesService projectTemplatesService;

    private final ProjectTemplatesQueryService projectTemplatesQueryService;

    public ProjectTemplatesResource(ProjectTemplatesService projectTemplatesService, ProjectTemplatesQueryService projectTemplatesQueryService) {
        this.projectTemplatesService = projectTemplatesService;
        this.projectTemplatesQueryService = projectTemplatesQueryService;
    }

    /**
     * POST  /project-templates : Create a new projectTemplates.
     *
     * @param projectTemplatesDTO the projectTemplatesDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new projectTemplatesDTO, or with status 400 (Bad Request) if the projectTemplates has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/project-templates")
    @Timed
    @PreAuthorize("@jhiAuth.hasPermission('PROJECT_TEMPLATES', 'CREATE')")
    public ResponseEntity<ProjectTemplatesDTO> createProjectTemplates(@Valid @RequestBody ProjectTemplatesDTO projectTemplatesDTO) throws URISyntaxException {
        log.debug("REST request to save ProjectTemplates : {}", projectTemplatesDTO);
        if (projectTemplatesDTO.getId() != null) {
            throw new BadRequestAlertException("A new projectTemplates cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProjectTemplatesDTO result = projectTemplatesService.save(projectTemplatesDTO);
        return ResponseEntity.created(new URI("/api/project-templates/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /project-templates : Updates an existing projectTemplates.
     *
     * @param projectTemplatesDTO the projectTemplatesDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated projectTemplatesDTO,
     * or with status 400 (Bad Request) if the projectTemplatesDTO is not valid,
     * or with status 500 (Internal Server Error) if the projectTemplatesDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/project-templates")
    @Timed
    @PreAuthorize("@jhiAuth.hasPermission('PROJECT_TEMPLATES', 'EDIT')")
    public ResponseEntity<ProjectTemplatesDTO> updateProjectTemplates(@Valid @RequestBody ProjectTemplatesDTO projectTemplatesDTO) throws URISyntaxException {
        log.debug("REST request to update ProjectTemplates : {}", projectTemplatesDTO);
        if (projectTemplatesDTO.getId() == null) {
            return createProjectTemplates(projectTemplatesDTO);
        }
        ProjectTemplatesDTO result = projectTemplatesService.save(projectTemplatesDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, projectTemplatesDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /project-templates : get all the projectTemplates.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of projectTemplates in body
     */
    @GetMapping("/project-templates")
    @Timed
    @PreAuthorize("@jhiAuth.hasPermission('PROJECT_TEMPLATES', 'VIEW')")
    public ResponseEntity<List<ProjectTemplatesDTO>> getAllProjectTemplates(ProjectTemplatesCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ProjectTemplates by criteria: {}", criteria);
        Page<ProjectTemplatesDTO> page = projectTemplatesQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/project-templates");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /project-templates/:id : get the "id" projectTemplates.
     *
     * @param id the id of the projectTemplatesDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the projectTemplatesDTO, or with status 404 (Not Found)
     */
    @GetMapping("/project-templates/{id}")
    @Timed
    @PreAuthorize("@jhiAuth.hasPermission('PROJECT_TEMPLATES', 'VIEW')")
    public ResponseEntity<ProjectTemplatesDTO> getProjectTemplates(@PathVariable Long id) {
        log.debug("REST request to get ProjectTemplates : {}", id);
        ProjectTemplatesDTO projectTemplatesDTO = projectTemplatesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(projectTemplatesDTO));
    }
    
    /**
     * DELETE  /project-templates/:id : delete the "id" projectTemplates.
     *
     * @param id the id of the projectTemplatesDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/project-templates/{id}")
    @Timed
    @PreAuthorize("@jhiAuth.hasPermission('PROJECT_TEMPLATES', 'VIEW')")
    public ResponseEntity<Void> deleteProjectTemplates(@PathVariable Long id) {
        log.debug("REST request to delete ProjectTemplates : {}", id);
        try {
        	projectTemplatesService.delete(id);
        	return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
        } catch(Exception e) {
        	return ResponseEntity.badRequest().headers(HeaderUtil.serviceFailureAlert(ENTITY_NAME, AppConstants.IN_USED, "A Project Templates cannot delete")).build();
        }
    }

    /**
     * SEARCH  /_search/project-templates?query=:query : search for the projectTemplates corresponding
     * to the query.
     *
     * @param query the query of the projectTemplates search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/project-templates")
    @Timed
    @PreAuthorize("@jhiAuth.hasPermission('PROJECT_TEMPLATES', 'VIEW')")
    public ResponseEntity<List<ProjectTemplatesDTO>> searchProjectTemplates(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of ProjectTemplates for query {}", query);
        Page<ProjectTemplatesDTO> page = projectTemplatesService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/project-templates");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    
    @GetMapping("/project-templates/export")
	@Timed
	public ResponseEntity<InputStreamResource> download(HttpServletResponse response,
			@RequestParam(value = "templateId", required = true) Long templateId) throws IOException {
		log.info("REST request to export template : {}", templateId);
		InputStreamResource in = this.projectTemplatesService.exportExcel(templateId);
		HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=task_management_template.xlsx");
		
		 return ResponseEntity
	                .ok()
	                .headers(headers)
	                .body(in);
	}

}
