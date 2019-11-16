package fpt.dps.dtms.web.rest.external;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import fpt.dps.dtms.domain.enumeration.ProjectStatus;
import fpt.dps.dtms.domain.enumeration.TaskStatus;
import fpt.dps.dtms.security.AuthoritiesConstants;
import fpt.dps.dtms.security.SecurityUtils;
import fpt.dps.dtms.service.ProjectsQueryService;
import fpt.dps.dtms.service.ProjectsService;
import fpt.dps.dtms.service.dto.ProjectsCriteria;
import fpt.dps.dtms.service.dto.ProjectsDTO;
import fpt.dps.dtms.web.rest.errors.BadRequestAlertException;
import fpt.dps.dtms.web.rest.errors.InternalServerErrorException;
import fpt.dps.dtms.web.rest.util.HeaderUtil;
import fpt.dps.dtms.web.rest.util.PaginationUtil;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing Projects.
 */
@RestController
@RequestMapping("/api/external")
public class ExternalProjectsResource {

	private final Logger log = LoggerFactory.getLogger(ExternalProjectsResource.class);

	private static final String ENTITY_NAME = "projects";

	private final ProjectsService projectsService;

	private final ProjectsQueryService projectsQueryService;

    public ExternalProjectsResource(ProjectsService projectsService, ProjectsQueryService projectsQueryService) {
        this.projectsService = projectsService;
        this.projectsQueryService = projectsQueryService;
	}
    
    /**
     * Get list project by user login
     * @param userLogin
     * @return
     */
	@GetMapping("/projects-list")
    @Timed
    public ResponseEntity<List<ProjectsDTO>> findByUserLoginAndRoleUser(@RequestParam("userLogin") String userLogin) {
		log.debug("Get list project of user: " + userLogin);
		List<ProjectsDTO> list = new ArrayList<ProjectsDTO>();
		list = projectsQueryService.findByUserLoginAndRoles(userLogin);
        
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
	
    /**
     * Get list project by business unit id
     * @param business unit id
     * @return
     */
	@GetMapping("/projects-list/business-units")
    @Timed
    public ResponseEntity<List<ProjectsDTO>> findByBusinessUnit(@RequestParam("buId") Long buId) {
		log.debug("Get list project of BU: " + buId);
		List<ProjectsDTO> list = this.projectsQueryService.findByBusinessUnit(buId);
        
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
	
	/**
	 * Get list project by user login and role OP.
	 * @param userLogin
	 * @return
	 */
	@GetMapping("/op-projects-list")
    @Timed
    public ResponseEntity<List<ProjectsDTO>> findByUserLoginAndRolesOP(@RequestParam("userLogin") String userLogin) {
		log.debug("Get list project role OP of user: " + userLogin);
		List<ProjectsDTO> list = new ArrayList<ProjectsDTO>();
		list = projectsQueryService.findByUserLoginAndRoleOP(userLogin);
        
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
	
	/**
	 * Get feedback project of user.
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
}