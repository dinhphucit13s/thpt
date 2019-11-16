package fpt.dps.dtms.web.rest;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

import fpt.dps.dtms.domain.enumeration.MONITORINGROLE;
import fpt.dps.dtms.domain.enumeration.PositionMonitoring;
import fpt.dps.dtms.security.AuthoritiesConstants;
import fpt.dps.dtms.security.SecurityUtils;
import fpt.dps.dtms.service.DtmsMonitoringService;
import fpt.dps.dtms.service.ProjectsQueryService;
import fpt.dps.dtms.service.ProjectsService;
import fpt.dps.dtms.service.dto.BugListDefaultDTO;
import fpt.dps.dtms.service.dto.DtmsMonitoringDTO;
import fpt.dps.dtms.service.dto.ProjectsCriteria;
import fpt.dps.dtms.service.dto.ProjectsDTO;
import fpt.dps.dtms.service.util.AppConstants;
import fpt.dps.dtms.web.rest.errors.BadRequestAlertException;
import fpt.dps.dtms.web.rest.errors.InternalServerErrorException;
import fpt.dps.dtms.web.rest.util.HeaderUtil;
import fpt.dps.dtms.web.rest.util.PaginationUtil;
import fpt.dps.dtms.web.rest.vm.ProjectBugListDefaultsVM;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing Projects.
 */
@RestController
@RequestMapping("/api")
public class ProjectsResource {

	private final Logger log = LoggerFactory.getLogger(ProjectsResource.class);

	private static final String ENTITY_NAME = "projects";

	private final ProjectsService projectsService;

	private final ProjectsQueryService projectsQueryService;

	private final DtmsMonitoringService dtmsMonitoringService;
	
    public ProjectsResource(ProjectsService projectsService, ProjectsQueryService projectsQueryService, DtmsMonitoringService dtmsMonitoringService) {
        this.projectsService = projectsService;
        this.projectsQueryService = projectsQueryService;
        this.dtmsMonitoringService = dtmsMonitoringService;
	}

	/**
	 * POST /projects : Create a new projects.
	 *
	 * @param projectsDTO
	 *            the projectsDTO to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new
	 *         projectsDTO, or with status 400 (Bad Request) if the projects has
	 *         already an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PostMapping("/projects")
	@Timed
	@PreAuthorize("@jhiAuth.hasPermission('PROJECT', 'CREATE')")
	public ResponseEntity<ProjectsDTO> createProjects(@Valid @RequestBody ProjectsDTO projectsDTO)
			throws URISyntaxException {
		log.debug("REST request to save Projects : {}", projectsDTO);
		if (projectsDTO.getId() != null) {
			throw new BadRequestAlertException("A new projects cannot already have an ID", ENTITY_NAME, "idexists");
		}
		ProjectsDTO result = projectsService.save(projectsDTO);
		return ResponseEntity.created(new URI("/api/projects/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString())).body(result);
	}

	/**
	 * PUT /projects : Updates an existing projects.
	 *
	 * @param projectsDTO
	 *            the projectsDTO to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         projectsDTO, or with status 400 (Bad Request) if the projectsDTO is
	 *         not valid, or with status 500 (Internal Server Error) if the
	 *         projectsDTO couldn't be updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PutMapping("/projects")
	@Timed
	@PreAuthorize("@jhiAuth.hasPermission('PROJECT', 'EDIT')")
	public ResponseEntity<ProjectsDTO> updateProjects(@Valid @RequestBody ProjectsDTO projectsDTO)
			throws URISyntaxException {
		log.debug("REST request to update Projects : {}", projectsDTO);
		if (projectsDTO.getId() == null) {
			return createProjects(projectsDTO);
		}
		ProjectsDTO result = projectsService.save(projectsDTO);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, projectsDTO.getId().toString())).body(result);
	}

	/**
	 * GET /projects : get all the projects.
	 *
	 * @param pageable
	 *            the pagination information
	 * @param criteria
	 *            the criterias which the requested entities should match
	 * @return the ResponseEntity with status 200 (OK) and the list of projects in
	 *         body
	 */
	@GetMapping("/projects")
	@Timed
	@PreAuthorize("@jhiAuth.hasPermission('PROJECT', 'VIEW')")
	public ResponseEntity<List<ProjectsDTO>> getAllProjects(ProjectsCriteria criteria, Pageable pageable) {
		log.debug("REST request to get Projects by criteria: {}", criteria);
		if (!SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN)) {
			final String userLogin = SecurityUtils.getCurrentUserLogin()
					.orElseThrow(() -> new InternalServerErrorException("Current user login not found"));
			StringFilter projectLeadUserLogin = new StringFilter();
			projectLeadUserLogin.setEquals(userLogin);
			criteria.setProjectLeadUserLogin(projectLeadUserLogin);
		}
		Page<ProjectsDTO> page = projectsQueryService.findByCriteria(criteria, pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/projects");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * GET /projects : get all the projects reference to dtmsMonitoring table.
	 *
	 * @param pageable
	 *            the pagination information
	 * @param criteria
	 *            the criterias which the requested entities should match
	 * @return the ResponseEntity with status 200 (OK) and the list of projects in
	 *         body
	 */
	@GetMapping("/projects-monitoring")
	@Timed
	@PreAuthorize("@jhiAuth.hasPermission('PROJECT', 'VIEW')")
	public ResponseEntity<List<ProjectsDTO>> getAllProjectsWithMonitoring(ProjectsCriteria criteria, Pageable pageable) {
		log.debug("REST request to get Projects by criteria: {}", criteria);
		Page<ProjectsDTO> page;
		if (!SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN)) {
			final String userLogin = SecurityUtils.getCurrentUserLogin()
					.orElseThrow(() -> new InternalServerErrorException("Current user login not found"));
			StringFilter projectLeadUserLogin = new StringFilter();
			projectLeadUserLogin.setEquals(userLogin);
			criteria.setProjectLeadUserLogin(projectLeadUserLogin);
			page = projectsQueryService.getAllProjectWithMonitoring(criteria, pageable);
		} else {
			page = projectsQueryService.findByCriteria(criteria, pageable);
		}
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/projects");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}
	
	/**
	 * get list project by user login and role user reference to dtmsMonitoring table for purchase oder view.
	 */
	@GetMapping("/projects-list-po")
    @Timed
    @PreAuthorize("@jhiAuth.hasPermission('PROJECT', 'VIEW')")
    public ResponseEntity<List<ProjectsDTO>> findByUserLoginAndRoleUserForPO(ProjectsCriteria criteria) {
		List<ProjectsDTO> list = new ArrayList<ProjectsDTO>();
        if (!SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN)) {
			final String userLogin = SecurityUtils.getCurrentUserLogin()
					.orElseThrow(() -> new InternalServerErrorException("Current user login not found"));
			list = projectsQueryService.findByUserLoginAndRoleUserForPO(userLogin);
		}else {

			list = projectsQueryService.findByCriteria(criteria);
		}

        return new ResponseEntity<>(list, HttpStatus.OK);
    }

	/**
	 * get list project by user login and role user.
	 */
	@GetMapping("/projects-list")
    @Timed
    @PreAuthorize("@jhiAuth.hasPermission('PROJECT', 'VIEW')")
    public ResponseEntity<List<ProjectsDTO>> findByUserLoginAndRoleUser(ProjectsCriteria criteria) {
		List<ProjectsDTO> list = new ArrayList<ProjectsDTO>();
        if (!SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN)) {
			final String userLogin = SecurityUtils.getCurrentUserLogin()
					.orElseThrow(() -> new InternalServerErrorException("Current user login not found"));
			list = projectsQueryService.findByUserLoginAndRoleUser(userLogin);
		}else {

			list = projectsQueryService.findByCriteria(criteria);
		}

        return new ResponseEntity<>(list, HttpStatus.OK);
    }

	/**
	 * GET /projects/:id : get the "id" projects.
	 *
	 * @param id
	 *            the id of the projectsDTO to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the
	 *         projectsDTO, or with status 404 (Not Found)
	 */
	@GetMapping("/projects/{id}")
	@Timed
	@PreAuthorize("@jhiAuth.hasPermission('PROJECT', 'VIEW')")
	public ResponseEntity<ProjectsDTO> getProjects(@PathVariable Long id) {
		log.debug("REST request to get Projects : {}", id);
		ProjectsDTO projectsDTO = projectsService.findOne(id);
		
		String[] watcherUsers = dtmsMonitoringService.getArraysDtmsMonitoringUsers(PositionMonitoring.PROJECT, id, MONITORINGROLE.ROLE_WATCHER);
		projectsDTO.setWatcherUsers(watcherUsers);
		
		String[] dedicatedUsers = dtmsMonitoringService.getArraysDtmsMonitoringUsers(PositionMonitoring.PROJECT, id, MONITORINGROLE.ROLE_DEDICATED);
		projectsDTO.setDedicatedUsers(dedicatedUsers);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(projectsDTO));
	}

	/**
	 * DELETE /projects/:id : delete the "id" projects.
	 *
	 * @param id
	 *            the id of the projectsDTO to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/projects/{id}")
	@Timed
	@PreAuthorize("@jhiAuth.hasPermission('PROJECT', 'DELETE')")
	public ResponseEntity<Void> deleteProjects(@PathVariable Long id) {
		log.debug("REST request to delete Projects : {}", id);
		try {
			projectsService.delete(id);
			return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
        } catch(Exception e) {
        	return ResponseEntity.badRequest().headers(HeaderUtil.serviceFailureAlert(ENTITY_NAME, AppConstants.IN_USED, "A project cannot delete")).build();
        }
		
	}

	/**
	 * SEARCH /_search/projects?query=:query : search for the projects corresponding
	 * to the query.
	 *
	 * @param query
	 *            the query of the projects search
	 * @param pageable
	 *            the pagination information
	 * @return the result of the search
	 */
	@GetMapping("/_search/projects")
	@Timed
	@PreAuthorize("@jhiAuth.hasPermission('PROJECT', 'VIEW')")
	public ResponseEntity<List<ProjectsDTO>> searchProjects(@RequestParam String query, Pageable pageable) {
		log.debug("REST request to search for a page of Projects for query {}", query);
		Page<ProjectsDTO> page = projectsService.search(query, pageable);
		HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/projects");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}
	
    /**
     * GET  /project-users : get all the projectUsers.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of projectUsers in body
     */
	@GetMapping("/projects/bug-list-defaults")
    @Timed
    @PreAuthorize("@jhiAuth.hasPermission('PROJECT_USERS', 'VIEW')")
    public ResponseEntity<List<BugListDefaultDTO>> getAllProjectBugListDefaults(@RequestParam(value = "projectId", required = false) Long projectId, ProjectsCriteria criteria, Pageable pageable) {
    	log.debug("projectId: ",projectId.toString());
    		Page<BugListDefaultDTO> page = projectsService.findBugListDefaultsByProjectId(projectId, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/projects/bug-list-defaults");
            return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
	
    /**
     * GET  /project-users : get all the projectUsers.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of projectUsers in body
     */
	@GetMapping("/_search/projects-bug-list-default")
    @Timed
    @PreAuthorize("@jhiAuth.hasPermission('PROJECT_USERS', 'VIEW')")
    public ResponseEntity<List<BugListDefaultDTO>> getAllProjectBugListDefaultsBySearch(@RequestParam(value = "searchValue", required = false) String searchValue,
    		@RequestParam(value = "proId", required = false) Long proId, ProjectsCriteria criteria, Pageable pageable) {
    	log.debug("projectId: ",proId.toString());
    		Page<BugListDefaultDTO> page = projectsService.findBugListDefaultsByProjectIdFollowSearch(searchValue ,proId, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/_search/projects-bug-list-default");
            return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
	

	
    @PostMapping("/projects-create-bug-list-default")
    @Timed
    @PreAuthorize("@jhiAuth.hasPermission('PROJECT_USERS', 'CREATE')")
    public ResponseEntity<ProjectsDTO> createProjectUsers(@RequestBody ProjectBugListDefaultsVM projectBugListDefaultsVM) throws URISyntaxException {
        log.debug("REST request to save ProjectUsers : {}", projectBugListDefaultsVM);
        ProjectsDTO result = projectsService.updateBugListDefaultOfProject(projectBugListDefaultsVM);
        return ResponseEntity.created(new URI("/api/projects/bug-list-defaults?projectId=" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }
    
    /**
     * DELETE  /projects/bug-list-defaults/delete/:projectId/:bugListDefaultId : delete the projectBugListDefaults
     * 
     * @param id the id of the projectUsersDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/projects/bug-list-defaults/delete/{projectId}/{bugListDefaultId}")
    @Timed
    @PreAuthorize("@jhiAuth.hasPermission('PROJECT_USERS', 'DELETE')")
    public ResponseEntity<Void> deleteProjectUsers(@PathVariable("projectId") Long projectId,
    		@PathVariable("bugListDefaultId") Long bugListDefaultId) {
        log.debug("REST request to delete ProjectBugListDefaults : {}", projectId.toString() + bugListDefaultId.toString());
        projectsService.deleteProjectBugListDefaults(projectId, bugListDefaultId);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, projectId.toString() + bugListDefaultId.toString())).build();
    }
    
    @GetMapping("/projects/export")
	@Timed
	public ResponseEntity<InputStreamResource> download(HttpServletResponse response,
			@RequestParam(value = "projectId", required = true) Long projectId) throws IOException {
		log.info("REST request to export template : {}", projectId);
		InputStreamResource in = this.projectsService.exportExcel(projectId);
		HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=task_management_template.xlsx");
		
		 return ResponseEntity
	                .ok()
	                .headers(headers)
	                .body(in);
	}
    
    /**
	 * Get feedback project
	 * @param userLogin
	 * @return
	 */
	@GetMapping("/feedbackProject")
    @Timed
    public ResponseEntity<ProjectsDTO> getFeedbackProject() {
		log.debug("Get feedback project of user");
		ProjectsDTO result = this.projectsService.getFeedbackProject();
        
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
	
//	/**
//	 * get list project by user login and role user.
//	 */
//	@GetMapping("/projects/getProjectBidding")
//    @Timed
//    public ResponseEntity<List<ProjectsDTO>> getListProjectBiddingTaskOP(@RequestParam("userLogin") String userLogin,
//    		@RequestParam("modeBidding") String modeBidding) {
//		List<ProjectsDTO> list = new ArrayList<ProjectsDTO>();
//			list = projectsQueryService.findListProjectBiddingTaskOP(userLogin, modeBidding);
//
//        return new ResponseEntity<>(list, HttpStatus.OK);
//    }
	
	/**
	 * get list project by user login and role user.
	 */
	@GetMapping("/projects/getProjectBidding")
    @Timed
    public ResponseEntity<List<ProjectsDTO>> getListProjectBiddingTaskPM(@RequestParam("userLogin") String userLogin) {
		List<ProjectsDTO> list = projectsQueryService.findListProjectBiddingTaskPM(userLogin);

        return new ResponseEntity<>(list, HttpStatus.OK);
    }
}
