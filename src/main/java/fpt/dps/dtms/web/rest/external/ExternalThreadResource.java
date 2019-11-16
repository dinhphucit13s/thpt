package fpt.dps.dtms.web.rest.external;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

import fpt.dps.dtms.service.TmsThreadQueryService;
import fpt.dps.dtms.service.TmsThreadService;
import fpt.dps.dtms.service.dto.TmsThreadDTO;
import fpt.dps.dtms.web.rest.util.PaginationUtil;
import fpt.dps.dtms.web.rest.vm.external.QuestionAndAnswerVM;
import io.github.jhipster.web.util.ResponseUtil;

@RestController
@RequestMapping("/api/external")
public class ExternalThreadResource {
	private final Logger log = LoggerFactory.getLogger(ExternalUserResource.class);
	private final TmsThreadService tmsThreadService;
	private final TmsThreadQueryService tmsThreadQueryService;
	public ExternalThreadResource(TmsThreadService tmsThreadService, TmsThreadQueryService tmsThreadQueryService) {
		this.tmsThreadService = tmsThreadService;
		this.tmsThreadQueryService = tmsThreadQueryService;
	}
	
    /**
     * conghk
     * GET  /issues : create issues.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of bugs in body
     * @throws IOException 
     */
    @RequestMapping(value = "/thread/create", method = RequestMethod.POST,
    headers = {"content-type=multipart/mixed","content-type=multipart/form-data", "content-type=application/octet-stream",
    		"content-type=application/json", "content-type=application/x-www-form-urlencoded"})
    @Timed
    public ResponseEntity<TmsThreadDTO> createQuestionAndAnswer(@RequestPart(value = "questionAndAnswer", required = true) QuestionAndAnswerVM payload) throws IOException {
        log.debug("REST request to create issues");
        TmsThreadDTO tmsThreadDTO = this.tmsThreadService.createQuestionAndAnswer(payload);
        return new ResponseEntity<>(tmsThreadDTO, HttpStatus.OK);
    }
    
    /**
     * GET  /thread/:projectId/:filter : get thread by project "id" and filter.
     *
     * @param id the id of the TmsThreadDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the TmsThreadDTO, or with status 404 (Not Found)
     * @PreAuthorize("@jhiAuth.hasPermission('NOTES', 'VIEW')")
     */
    @GetMapping("/thread/{projectId}/{filter}")
    @Timed
    public ResponseEntity<List<TmsThreadDTO>> getQuestionAndAnswer(@PathVariable("projectId") Long projectId,
    		@PathVariable("filter") String filter, Pageable pageable) {
        log.debug("REST EXTERNAL request to get thread by filter ", filter);
        Page<TmsThreadDTO> page = this.tmsThreadQueryService.findQuestionAndAnswer(projectId, filter, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/external/thread/{projectId}/{filter}");
        
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    
    /**
     * PUT  /thread/updateViews : update view of thread thread by thread "id".
     *
     * @param id the id of the TmsThreadDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the TmsThreadDTO, or with status 404 (Not Found)
     * @PreAuthorize("@jhiAuth.hasPermission('NOTES', 'VIEW')")
     */
    @PutMapping("/thread/updateViews")
    @Timed
    public ResponseEntity<Void> updateViewsOfThread(@RequestParam("threadId") Long threadId) {
        log.debug("REST EXTERNAL request to update thread ", threadId);
        this.tmsThreadService.updateViewsOfThread(threadId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    /**
     * PUT  /thread/updateViews : update view of thread thread by thread "id".
     *
     * @param id the id of the TmsThreadDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the TmsThreadDTO, or with status 404 (Not Found)
     * @PreAuthorize("@jhiAuth.hasPermission('NOTES', 'VIEW')")
     */
    @PutMapping("/thread/changeThreadToClose")
    @Timed
    public ResponseEntity<Void> updateThreadToClose(@RequestParam("threadId") Long threadId) {
        log.debug("REST EXTERNAL request to update thread ", threadId);
        this.tmsThreadService.updateThreadToClose(threadId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    /**
     * GET  /tms-threads/:id : get the "id" tmsThread.
     *
     * @param id the id of the tmsThreadDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the tmsThreadDTO, or with status 404 (Not Found)
     */
    @GetMapping("/thread/{id}")
    @Timed
    public ResponseEntity<TmsThreadDTO> getTmsThread(@PathVariable Long id) {
        log.debug("REST request to get TmsThread : {}", id);
        TmsThreadDTO tmsThreadDTO = tmsThreadService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(tmsThreadDTO));
    }
    
    /**
     * PUT  /thread/updateViews : update view of thread thread by thread "id".
     *
     * @param id the id of the TmsThreadDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the TmsThreadDTO, or with status 404 (Not Found)
     * @PreAuthorize("@jhiAuth.hasPermission('NOTES', 'VIEW')")
     */
    @PutMapping("/thread/changeTitleOfThread")
    @Timed
    public ResponseEntity<TmsThreadDTO> updateTitleOfThread(@RequestParam("id") Long id, @RequestParam("title") String title) {
        log.debug("REST EXTERNAL request to update thread ", id);
        TmsThreadDTO tmsThreadDTO = this.tmsThreadService.updateTitleOfThread(id, title);
        return ResponseEntity.ok().body(tmsThreadDTO);
    }
}
