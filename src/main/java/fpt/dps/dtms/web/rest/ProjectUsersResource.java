package fpt.dps.dtms.web.rest;

import com.codahale.metrics.annotation.Timed;
import fpt.dps.dtms.service.ProjectUsersService;
import fpt.dps.dtms.service.TasksQueryService;
import fpt.dps.dtms.web.rest.errors.BadRequestAlertException;
import fpt.dps.dtms.web.rest.errors.InternalServerErrorException;
import fpt.dps.dtms.web.rest.util.HeaderUtil;
import fpt.dps.dtms.web.rest.util.PaginationUtil;
import fpt.dps.dtms.service.dto.ProjectUsersDTO;
import fpt.dps.dtms.service.dto.ProjectsCriteria;
import fpt.dps.dtms.service.dto.ProjectsDTO;
import fpt.dps.dtms.service.dto.SelectDTO;
import fpt.dps.dtms.service.util.AppConstants;
import fpt.dps.dtms.service.dto.ProjectUsersCriteria;
import fpt.dps.dtms.security.AuthoritiesConstants;
import fpt.dps.dtms.security.SecurityUtils;
import fpt.dps.dtms.service.ProjectUsersQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import fpt.dps.dtms.service.dto.SelectDTO;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing ProjectUsers.
 */
@RestController
@RequestMapping("/api")
public class ProjectUsersResource {

    private final Logger log = LoggerFactory.getLogger(ProjectUsersResource.class);

    private static final String ENTITY_NAME = "projectUsers";

    private final ProjectUsersService projectUsersService;

    private final ProjectUsersQueryService projectUsersQueryService;
    
    private final TasksQueryService tasksQueryService;

    public ProjectUsersResource(ProjectUsersService projectUsersService, ProjectUsersQueryService projectUsersQueryService, TasksQueryService tasksQueryService) {
        this.projectUsersService = projectUsersService;
        this.projectUsersQueryService = projectUsersQueryService;
        this.tasksQueryService = tasksQueryService;
    }

    /**
     * POST  /project-users : Create a new projectUsers.
     *
     * @param projectUsersDTO the projectUsersDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new projectUsersDTO, or with status 400 (Bad Request) if the projectUsers has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/project-users")
    @Timed
    @PreAuthorize("@jhiAuth.hasPermission('PROJECT_USERS', 'CREATE')")
    public ResponseEntity<List<ProjectUsersDTO>> createProjectUsers(@Valid @RequestBody List<ProjectUsersDTO> projectUsersDTOs) throws URISyntaxException {
        log.debug("REST request to save ProjectUsers : {}", projectUsersDTOs);
        List<ProjectUsersDTO> result = new ArrayList<>();
        for (ProjectUsersDTO projectUsersDTO : projectUsersDTOs) {
            if (projectUsersDTO.getId() != null) {
                throw new BadRequestAlertException("A new projectUsers cannot already have an ID", ENTITY_NAME, "idexists");
            }
            result.add(projectUsersService.save(projectUsersDTO));
        }
        return ResponseEntity.created(new URI("/api/project-users/" + result.get(0).getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.get(0).getId().toString()))
            .body(result);
    }
    
    public ResponseEntity<ProjectUsersDTO> createProjectUsers(@Valid @RequestBody ProjectUsersDTO projectUsersDTO) throws URISyntaxException {
        log.debug("REST request to save ProjectUsers : {}", projectUsersDTO);
        if (projectUsersDTO.getId() != null) {
            throw new BadRequestAlertException("A new projectUsers cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProjectUsersDTO result = projectUsersService.save(projectUsersDTO);
        return ResponseEntity.created(new URI("/api/project-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /project-users : Updates an existing projectUsers.
     *
     * @param projectUsersDTO the projectUsersDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated projectUsersDTO,
     * or with status 400 (Bad Request) if the projectUsersDTO is not valid,
     * or with status 500 (Internal Server Error) if the projectUsersDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/project-users")
    @Timed
    @PreAuthorize("@jhiAuth.hasPermission('PROJECT_USERS', 'EDIT')")
    public ResponseEntity<ProjectUsersDTO> updateProjectUsers(@Valid @RequestBody ProjectUsersDTO projectUsersDTO) throws URISyntaxException {
        log.debug("REST request to update ProjectUsers : {}", projectUsersDTO);
        if (projectUsersDTO.getId() == null) {
            return createProjectUsers(projectUsersDTO);
        }
        ProjectUsersDTO result = projectUsersService.save(projectUsersDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, projectUsersDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /project-users : get all the projectUsers.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of projectUsers in body
     */
    @GetMapping("/project-users-list")
    @Timed
    @PreAuthorize("@jhiAuth.hasPermission('PROJECT_USERS', 'VIEW')")
    public ResponseEntity<List<ProjectUsersDTO>> getAllProjectUsers(@RequestParam(value = "query", required = false) String query, ProjectUsersCriteria criteria, Pageable pageable) {
    	log.debug("projectId: ",query.toString());
    	if(pageable != null) {
    		// Page<ProjectUsersDTO> page = projectUsersQueryService.findByProjectIdAndExcludeSpecifyUser(Long.valueOf(query), pageable);
    		Page<ProjectUsersDTO> page = projectUsersQueryService.getAllUsersByPrjectId(Long.valueOf(query), pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/project-users-list");
            return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    	} else {
    		// List<ProjectUsersDTO> page = projectUsersQueryService.findByProjectIdAndExcludeSpecifyUserRole(Long.valueOf(query));getAllMemberByProjectId
    		List<ProjectUsersDTO> page = projectUsersQueryService.getAllMemberByProjectId(Long.valueOf(query));
            return new ResponseEntity<>(page, HttpStatus.OK);
    	}
    }
    
    @GetMapping("/project-users-selects")
    @Timed
    public ResponseEntity<List<SelectDTO>> getAllUsersForSelects(@RequestParam(value = "query", required = false) Long id) {
        List<SelectDTO> page = projectUsersQueryService.getAllUsersForSelects(id);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }
    
    @GetMapping("/project-users-by-role")
    @Timed
    @PreAuthorize("@jhiAuth.hasPermission('PROJECT_USERS', 'VIEW')")
    public ResponseEntity<List<ProjectUsersDTO>> getAllProjectUsersRole(@RequestParam(value = "query", required = false) String query) {
    	log.debug("projectId: ",query.toString());
    	List<ProjectUsersDTO> page = projectUsersQueryService.findByProjectIdAndExcludeSpecifyUserRole(Long.valueOf(query));
        return new ResponseEntity<>(page, HttpStatus.OK);
    }
    
    @GetMapping("/project-users-by-role-select")
    @Timed
    @PreAuthorize("@jhiAuth.hasPermission('PROJECT_USERS', 'VIEW')")
    public ResponseEntity<List<SelectDTO>> getAllProjectUsersRoleSelect(@RequestParam(value = "query", required = false) String query) {
    	List<SelectDTO> page = projectUsersQueryService.findByProjectIdAndExcludeSpecifyUserRoleForSelect(Long.valueOf(query));
        return new ResponseEntity<>(page, HttpStatus.OK);
    }
    
    @GetMapping("/project-users-by-user")
    @Timed
    @PreAuthorize("@jhiAuth.hasPermission('PROJECT_USERS', 'VIEW')")
    public ResponseEntity<List<ProjectUsersDTO>> findHistoryByUserLogin(@RequestParam(value = "userLogin", required = false) String userLogin, Pageable pageable) {
    	Page<ProjectUsersDTO> page = projectUsersQueryService.findHistoryByUserLogin(userLogin, pageable);
    	HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/project-users-by-user");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    
    /**
     * GET  /project-users/:id : get the "id" projectUsers.
     *
     * @param id the id of the projectUsersDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the projectUsersDTO, or with status 404 (Not Found)
     */
    @GetMapping("/project-users/{id}")
    @Timed
    @PreAuthorize("@jhiAuth.hasPermission('PROJECT_USERS', 'VIEW')")
    public ResponseEntity<ProjectUsersDTO> getProjectUsers(@PathVariable Long id) {
        log.debug("REST request to get ProjectUsers : {}", id);
        ProjectUsersDTO projectUsersDTO = projectUsersService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(projectUsersDTO));
    }

    /**
     * DELETE  /project-users/:id : delete the "id" projectUsers.
     *
     * @param id the id of the projectUsersDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/project-users/{id}")
    @Timed
    @PreAuthorize("@jhiAuth.hasPermission('PROJECT_USERS', 'DELETE')")
    public ResponseEntity<Void> deleteProjectUsers(@PathVariable Long id) {
        log.debug("REST request to delete ProjectUsers : {}", id);
        projectUsersService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
    
    /**
     * SEARCH  /_search/project-users?query=:query : search for the projectUsers corresponding
     * to the query.
     *
     * @param query the query of the projectUsers search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/project-users")
    @Timed
    @PreAuthorize("@jhiAuth.hasPermission('PROJECT_USERS', 'VIEW')")
    public ResponseEntity<List<ProjectUsersDTO>> searchProjectUsers(@RequestParam String[] query, Pageable pageable) {
        log.debug("REST request to search for a page of ProjectUsers for query {}", query[0]);
        Page<ProjectUsersDTO> page = projectUsersService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query[0], page, "/api/_search/project-user");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    
    /**
     * Import project user from excel file
     * @param file: excel file
     * @param projectId
     * */
    @RequestMapping(value = "/project-users/import", method = RequestMethod.POST,
    	    headers = {"content-type=application/octet-stream", "content-type=multipart/mixed","content-type=multipart/form-data", 
    	    		"content-type=application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",})
    @Timed
    public ResponseEntity<Map<String,List<ProjectUsersDTO>>> importProjectUsers(@RequestParam("excelFile") MultipartFile file, @Valid @RequestPart(value = "projectId", required = true) Long projectId) {
    	try {
    		log.info("Import user from file {} to projectId {}", file.getOriginalFilename(), projectId);
    		 Map<String, List<ProjectUsersDTO>> result  = projectUsersService.importProjectUsers(file, projectId);
    		 return ResponseEntity.ok(result);
//    		return ResponseEntity.ok().headers(HeaderUtil.importAlert(ENTITY_NAME, String.valueOf(successProjectUsers.size()))).build();
		} catch (Exception e) {
			return ResponseEntity.badRequest().headers(HeaderUtil.serviceFailureAlert(ENTITY_NAME, AppConstants.IMPORT_FAILED, "Import was failed")).build();
		}
	}
    
    /**
     * Export template import Project User to excel file
     * @param packageId
     * @return excel file
     */
    @GetMapping("/project-users/download-template")
	@Timed
	public ResponseEntity<InputStreamResource> downloadTemplate(HttpServletResponse response) throws IOException {
		log.info("REST request to download template import Project User");
		InputStreamResource in = this.projectUsersService.exportTemplate();
		HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=template_ProjectUsers.xlsx");
		
		 return ResponseEntity
	                .ok()
	                .headers(headers)
	                .body(in);
	}
    
    @GetMapping("/project-users/allocation")
    @Timed
    public ResponseEntity<List<Map<String, Object>>> allocation(@RequestParam Long id, Pageable pageable) {
    	log.debug("REST getAllUsersForProjectUser:", id);
    	List<Map<String, Object>> result = projectUsersQueryService.getAllUserAllocation(id, pageable);
    	int totalPage = Integer.parseInt(result.get(result.size() - 1).get("total").toString());
        result.remove(result.size() - 1);
    	Page<Map<String, Object>> page = new PageImpl<>(result, pageable, totalPage);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/project-users/allocation");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    
    /**
     * Export members management to excel file
     * @param projectId
     * @return excel file
     * @author HoiHT1 
     */
    @GetMapping("/allocation/export")
	@Timed
	public ResponseEntity<InputStreamResource> download(HttpServletResponse response,
			@RequestParam(value = "projectId", required = true) Long projectId) throws IOException {
		log.info("REST request to export template : {}", projectId);
		InputStreamResource in = this.projectUsersService.exportExcel(projectId);
		HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=allocation_export.xlsx");
		
		 return ResponseEntity
	                .ok()
	                .headers(headers)
	                .body(in);
	}
}
