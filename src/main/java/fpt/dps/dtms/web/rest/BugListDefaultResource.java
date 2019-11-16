package fpt.dps.dtms.web.rest;

import com.codahale.metrics.annotation.Timed;
import fpt.dps.dtms.service.BugListDefaultService;
import fpt.dps.dtms.web.rest.errors.BadRequestAlertException;
import fpt.dps.dtms.web.rest.util.HeaderUtil;
import fpt.dps.dtms.web.rest.util.PaginationUtil;
import fpt.dps.dtms.service.dto.BugListDefaultDTO;
import fpt.dps.dtms.service.util.AppConstants;
import fpt.dps.dtms.service.dto.BugListDefaultCriteria;
import fpt.dps.dtms.domain.User;
import fpt.dps.dtms.service.BugListDefaultQueryService;
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

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing BugListDefault.
 */
@RestController
@RequestMapping("/api")
public class BugListDefaultResource {

    private final Logger log = LoggerFactory.getLogger(BugListDefaultResource.class);

    private static final String ENTITY_NAME = "bugListDefault";

    private final BugListDefaultService bugListDefaultService;

    private final BugListDefaultQueryService bugListDefaultQueryService;

    public BugListDefaultResource(BugListDefaultService bugListDefaultService, BugListDefaultQueryService bugListDefaultQueryService) {
        this.bugListDefaultService = bugListDefaultService;
        this.bugListDefaultQueryService = bugListDefaultQueryService;
    }

    /**
     * POST  /bug-list-defaults : Create a new bugListDefault.
     *
     * @param bugListDefaultDTO the bugListDefaultDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new bugListDefaultDTO, or with status 400 (Bad Request) if the bugListDefault has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/bug-list-defaults")
    @Timed
    @PreAuthorize("@jhiAuth.hasPermission('BUG_LIST_DEFAULT', 'CREATE')")
    public ResponseEntity<BugListDefaultDTO> createBugListDefault(@Valid @RequestBody BugListDefaultDTO bugListDefaultDTO) throws URISyntaxException {
        log.debug("REST request to save BugListDefault : {}", bugListDefaultDTO);
        if (bugListDefaultDTO.getId() != null) {
            throw new BadRequestAlertException("A new bugListDefault cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BugListDefaultDTO result = bugListDefaultService.save(bugListDefaultDTO);
        return ResponseEntity.created(new URI("/api/bug-list-defaults/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /bug-list-defaults : Updates an existing bugListDefault.
     *
     * @param bugListDefaultDTO the bugListDefaultDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated bugListDefaultDTO,
     * or with status 400 (Bad Request) if the bugListDefaultDTO is not valid,
     * or with status 500 (Internal Server Error) if the bugListDefaultDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/bug-list-defaults")
    @Timed
    @PreAuthorize("@jhiAuth.hasPermission('BUG_LIST_DEFAULT', 'EDIT')")
    public ResponseEntity<BugListDefaultDTO> updateBugListDefault(@Valid @RequestBody BugListDefaultDTO bugListDefaultDTO) throws URISyntaxException {
        log.debug("REST request to update BugListDefault : {}", bugListDefaultDTO);
        if (bugListDefaultDTO.getId() == null) {
            return createBugListDefault(bugListDefaultDTO);
        }
        BugListDefaultDTO result = bugListDefaultService.save(bugListDefaultDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, bugListDefaultDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /bug-list-defaults : get all the bugListDefaults.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of bugListDefaults in body
     */
    @GetMapping("/bug-list-defaults")
    @Timed
    @PreAuthorize("@jhiAuth.hasPermission('BUG_LIST_DEFAULT', 'VIEW')")
    public ResponseEntity<List<BugListDefaultDTO>> getAllBugListDefaults(BugListDefaultCriteria criteria, Pageable pageable) {
        log.debug("REST request to get BugListDefaults by criteria: {}", criteria);
        Page<BugListDefaultDTO> page = bugListDefaultQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/bug-list-defaults");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /bug-list-defaults/:id : get the "id" bugListDefault.
     *
     * @param id the id of the bugListDefaultDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the bugListDefaultDTO, or with status 404 (Not Found)
     */
    @GetMapping("/bug-list-defaults/{id}")
    @Timed
    @PreAuthorize("@jhiAuth.hasPermission('BUG_LIST_DEFAULT', 'VIEW')")
    public ResponseEntity<BugListDefaultDTO> getBugListDefault(@PathVariable Long id) {
        log.debug("REST request to get BugListDefault : {}", id);
        BugListDefaultDTO bugListDefaultDTO = bugListDefaultService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(bugListDefaultDTO));
    }

    /**
     * DELETE  /bug-list-defaults/:id : delete the "id" bugListDefault.
     *
     * @param id the id of the bugListDefaultDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/bug-list-defaults/{id}")
    @Timed
    @PreAuthorize("@jhiAuth.hasPermission('BUG_LIST_DEFAULT', 'DELETE')")
    public ResponseEntity<Void> deleteBugListDefault(@PathVariable Long id) {
        log.debug("REST request to delete BugListDefault : {}", id);
        try {
        	bugListDefaultService.delete(id);
        	return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
        } catch(Exception e) {
        	return ResponseEntity.badRequest().headers(HeaderUtil.serviceFailureAlert(ENTITY_NAME, AppConstants.IN_USED, "A BugListDefault cannot delete")).build();
        }
    }

    /**
     * SEARCH  /_search/bug-list-defaults?query=:query : search for the bugListDefault corresponding
     * to the query.
     *
     * @param query the query of the bugListDefault search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/bug-list-defaults")
    @Timed
    @PreAuthorize("@jhiAuth.hasPermission('BUG_LIST_DEFAULT', 'VIEW')")
    public ResponseEntity<List<BugListDefaultDTO>> searchBugListDefaults(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of BugListDefaults for query {}", query);
        Page<BugListDefaultDTO> page = bugListDefaultService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/bug-list-defaults");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    
    /**
     * GET /users-for-project-user : get all users for ProjectUser.
     * 
     * @param query
     * @return the ResponseEntity with status 200 (OK) and with body all users
     */
    @GetMapping("/bug-list-defaults-unExist")
    @Timed
    public ResponseEntity<List<BugListDefaultDTO>> getAllBugUnExistProject(@RequestParam("projectId") Long projectId) {
    	log.debug("REST getAllUsersForProjectUser:", projectId);
        final List<BugListDefaultDTO> list = bugListDefaultService.getAllBugListDefaultsUnExistInProject(projectId);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

}
