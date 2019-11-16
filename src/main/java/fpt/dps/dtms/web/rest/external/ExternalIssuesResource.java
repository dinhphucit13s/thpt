package fpt.dps.dtms.web.rest.external;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

import fpt.dps.dtms.service.IssuesQueryService;
import fpt.dps.dtms.service.IssuesService;
import fpt.dps.dtms.service.dto.AttachmentsDTO;
import fpt.dps.dtms.service.dto.BugsDTO;
import fpt.dps.dtms.service.dto.IssuesDTO;
import fpt.dps.dtms.web.rest.util.HeaderUtil;
import fpt.dps.dtms.web.rest.util.PaginationUtil;

/**
 * REST controller for managing Issues.
 */
@RestController
@RequestMapping("/api/external")
public class ExternalIssuesResource {
    private final Logger log = LoggerFactory.getLogger(ExternalBugsResource.class);

    private static final String ENTITY_NAME = "issues";
    
    private final IssuesService issuesService;
    
    private final IssuesQueryService issuesQueryService;

	public ExternalIssuesResource(IssuesService issuesService, IssuesQueryService issuesQueryService) {
		this.issuesService = issuesService;
		this.issuesQueryService = issuesQueryService;
	}
    
    /**
     * conghk
     * GET  /issues : create issues.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of bugs in body
     * @throws IOException 
     */
    @RequestMapping(value = "/issues/create", method = RequestMethod.POST,
    headers = {"content-type=multipart/mixed","content-type=multipart/form-data", "content-type=application/octet-stream",
    		"content-type=application/json", "content-type=application/x-www-form-urlencoded"})
    @Timed
    public ResponseEntity<IssuesDTO> createIssues(@RequestPart(value = "feedback", required = true) IssuesDTO payload) throws IOException {
        log.debug("REST request to create issues");
        IssuesDTO issuesDTO = issuesService.createIssues(payload);
        return new ResponseEntity<>(issuesDTO, HttpStatus.OK);
    }
    
    /**
     * GET  /notes/tasks/:id : get notes the by task "id".
     *
     * @param id the id of the notesDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the notesDTO, or with status 404 (Not Found)
     * @PreAuthorize("@jhiAuth.hasPermission('NOTES', 'VIEW')")
     */
    @GetMapping("/issues/{projectId}/{userLogin}")
    @Timed
    public ResponseEntity<List<IssuesDTO>> getFeedbackByUser(@PathVariable("projectId") Long id,
    		@PathVariable("userLogin") String userLogin, Pageable pageable) {
        log.debug("REST EXTERNAL request to get feedback by user ", userLogin);
        Page<IssuesDTO> page = this.issuesQueryService.findIssueByProjectIdAndUser(id, userLogin, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/external/issues/{projectId}/{userLogin}");
        
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    
    /**
     * conghk
     * GET  /issues : get attachment of issues.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of bugs in body
     */
    @GetMapping("/issues/attachment")
    @Timed
    public ResponseEntity<List<AttachmentsDTO>> getAttachmentByIssuesId(@RequestParam(value = "issuesId", required = false) Long issuesId) {
        log.debug("REST request to get a attachment list of issuesId");
        List<AttachmentsDTO> attachmentList = new ArrayList<>();
        attachmentList = this.issuesService.getAttachmentByIssuesId(issuesId);
        return new ResponseEntity<>(attachmentList, HttpStatus.OK);
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
        this.issuesService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
    
    /**
     * DELETE  /issues/:id : get the "id" issues.
     *
     * @param id the id of the issuesDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @GetMapping("/issues/{id}")
    @Timed
    public ResponseEntity<IssuesDTO> getFeedbackById(@PathVariable Long id) {
        log.debug("REST request to get Issues : {}", id);
        IssuesDTO issuesDTO = this.issuesService.findOne(id);
        return ResponseEntity.ok().body(issuesDTO);
    }
}
