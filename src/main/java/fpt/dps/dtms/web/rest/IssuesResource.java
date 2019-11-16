package fpt.dps.dtms.web.rest;

import com.codahale.metrics.annotation.Timed;
import fpt.dps.dtms.service.IssuesService;
import fpt.dps.dtms.web.rest.errors.BadRequestAlertException;
import fpt.dps.dtms.web.rest.util.HeaderUtil;
import fpt.dps.dtms.web.rest.util.PaginationUtil;
import fpt.dps.dtms.service.dto.IssuesDTO;
import fpt.dps.dtms.service.util.AppConstants;
import fpt.dps.dtms.service.dto.AttachmentsDTO;
import fpt.dps.dtms.service.dto.IssuesCriteria;
import fpt.dps.dtms.service.IssuesQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Issues.
 */
@RestController
@RequestMapping("/api")
public class IssuesResource {

    private final Logger log = LoggerFactory.getLogger(IssuesResource.class);

    private static final String ENTITY_NAME = "issues";

    private final IssuesService issuesService;

    private final IssuesQueryService issuesQueryService;

    public IssuesResource(IssuesService issuesService, IssuesQueryService issuesQueryService) {
        this.issuesService = issuesService;
        this.issuesQueryService = issuesQueryService;
    }

    /**
     * POST  /issues : Create a new issues.
     *
     * @param issuesDTO the issuesDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new issuesDTO, or with status 400 (Bad Request) if the issues has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/issues")
    @Timed
    public ResponseEntity<IssuesDTO> createIssues(@Valid @RequestBody IssuesDTO issuesDTO) throws URISyntaxException {
        log.debug("REST request to save Issues : {}", issuesDTO);
        if (issuesDTO.getId() != null) {
            throw new BadRequestAlertException("A new issues cannot already have an ID", ENTITY_NAME, "idexists");
        }
        IssuesDTO result = issuesService.save(issuesDTO);
        return ResponseEntity.created(new URI("/api/issues/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }
    
    /**
     * POST  /issues : Create a new issues.
     *
     * @param issuesDTO the issuesDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new issuesDTO, or with status 400 (Bad Request) if the issues has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/feedback")
    @Timed
    public ResponseEntity<IssuesDTO> createFeedback(@RequestParam(value = "attachments", required = false) List<MultipartFile> files, 
    		@Valid @RequestPart(value = "feedback", required = true) IssuesDTO issuesDTO) throws URISyntaxException {
        log.debug("REST request to save Feedback : {}", issuesDTO);
        if (issuesDTO.getId() != null) {
            throw new BadRequestAlertException("A new issues cannot already have an ID", ENTITY_NAME, "idexists");
        }
        IssuesDTO result = issuesService.createFeedback(issuesDTO, files);
        return ResponseEntity.created(new URI("/feedback/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }
    

    /**
     * PUT  /issues : Updates an existing issues.
     *
     * @param issuesDTO the issuesDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated issuesDTO,
     * or with status 400 (Bad Request) if the issuesDTO is not valid,
     * or with status 500 (Internal Server Error) if the issuesDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/issues")
    @Timed
    public ResponseEntity<IssuesDTO> updateIssues(@Valid @RequestBody IssuesDTO issuesDTO) throws URISyntaxException {
        log.debug("REST request to update Issues : {}", issuesDTO);
        if (issuesDTO.getId() == null) {
            return createIssues(issuesDTO);
        }
        //IssuesDTO result = issuesService.save(issuesDTO);
        IssuesDTO result = new IssuesDTO();
		try {
			result = issuesService.createIssues(issuesDTO);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, issuesDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /issues : get all the issues.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of issues in body
     */
    @GetMapping("/issues")
    @Timed
    public ResponseEntity<List<IssuesDTO>> getAllIssues(IssuesCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Issues by criteria: {}", criteria);
        Page<IssuesDTO> page = issuesQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/issues");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    
    /**
     * GET  /issues : get all the issues by project name.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of issues in body
     */
    @GetMapping("/issues-campaign")
    @Timed
    public ResponseEntity<List<IssuesDTO>> getAllIssuesByCampaign(Pageable pageable) {
        Page<IssuesDTO> page = issuesQueryService.getAllIssuesByCampaign(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/issues-campaign");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/issues-campaign-money")
    @Timed
    public ResponseEntity<Map<String, Object>> trackingIssuesTM_Campaign(@RequestParam String money) {
    	Map<String, Object> page = issuesQueryService.trackingIssuesTM_Campaign(money);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }
    
    /**
     * GET  /issues/:id : get the "id" issues.
     *
     * @param id the id of the issuesDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the issuesDTO, or with status 404 (Not Found)
     */
    @GetMapping("/issues/{id}")
    @Timed
    public ResponseEntity<IssuesDTO> getIssues(@PathVariable Long id) {
        log.debug("REST request to get Issues : {}", id);
        IssuesDTO issuesDTO = issuesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(issuesDTO));
    }
    
    @GetMapping("/issues-find/{id}")
    @Timed
    public ResponseEntity<Map<String, Object>> getIssuesCampaign(@PathVariable Long id) {
        log.debug("REST request to get Issues : {}", id);
        Map<String, Object> issuesDTO = issuesQueryService.findIssuesAttach(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(issuesDTO));
    }

    /**
     * DELETE  /issues/:id : delete the "id" issues.
     *
     * @param id the id of the issuesDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/issues/{id}")
    @Timed
    public ResponseEntity<Void> deleteIssues(@PathVariable Long id) {
        log.debug("REST request to delete Issues : {}", id);
        try {
        	issuesService.delete(id);
        	return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
        } catch(Exception e) {
        	return ResponseEntity.badRequest().headers(HeaderUtil.serviceFailureAlert(ENTITY_NAME, AppConstants.IN_USED, "A Issues cannot delete")).build();
        }
    }

    /**
     * SEARCH  /_search/issues?query=:query : search for the issues corresponding
     * to the query.
     *
     * @param query the query of the issues search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/issues")
    @Timed
    public ResponseEntity<List<IssuesDTO>> searchIssues(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Issues for query {}", query);
        Page<IssuesDTO> page = issuesService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/issues");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    
    /**
     *  Get issues create by userLogin in project
     * @param id
     * @param userLogin
     * @param pageable
     * @return list issues
     */
    @GetMapping("/issues/{projectId}/{userLogin}")
    @Timed
    public ResponseEntity<List<IssuesDTO>> getFeedbackByUser(@PathVariable("projectId") Long id,
    		@PathVariable("userLogin") String userLogin, Pageable pageable) {
        log.debug("REST request to get feedback by user ", userLogin);
        Page<IssuesDTO> page = this.issuesQueryService.findIssueByProjectIdAndUser(id, userLogin, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/issues/{projectId}/{userLogin}");
        
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    
    /**
     * GET  /issues : get attachment of issues.
     * 
     */
    @GetMapping("/issues/attachment")
    @Timed
    public ResponseEntity<List<AttachmentsDTO>> getAttachmentByIssuesId(@RequestParam(value = "issuesId", required = false) Long issuesId) {
        log.debug("REST request to get a attachment list of issuesId");
        List<AttachmentsDTO> attachmentList = new ArrayList<>();
        attachmentList = this.issuesService.getAttachmentByIssuesId(issuesId);
        return new ResponseEntity<>(attachmentList, HttpStatus.OK);
    }

}
