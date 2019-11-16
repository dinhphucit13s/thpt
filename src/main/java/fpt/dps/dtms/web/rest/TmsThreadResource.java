package fpt.dps.dtms.web.rest;

import com.codahale.metrics.annotation.Timed;
import fpt.dps.dtms.service.TmsThreadService;
import fpt.dps.dtms.web.rest.errors.BadRequestAlertException;
import fpt.dps.dtms.web.rest.util.HeaderUtil;
import fpt.dps.dtms.web.rest.util.PaginationUtil;
import fpt.dps.dtms.web.rest.vm.external.QuestionAndAnswerVM;
import fpt.dps.dtms.service.dto.TmsThreadDTO;
import fpt.dps.dtms.service.dto.TmsThreadCriteria;
import fpt.dps.dtms.service.TmsThreadQueryService;
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

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing TmsThread.
 */
@RestController
@RequestMapping("/api")
public class TmsThreadResource {

    private final Logger log = LoggerFactory.getLogger(TmsThreadResource.class);

    private static final String ENTITY_NAME = "tmsThread";

    private final TmsThreadService tmsThreadService;

    private final TmsThreadQueryService tmsThreadQueryService;

    public TmsThreadResource(TmsThreadService tmsThreadService, TmsThreadQueryService tmsThreadQueryService) {
        this.tmsThreadService = tmsThreadService;
        this.tmsThreadQueryService = tmsThreadQueryService;
    }

    /**
     * POST  /tms-threads : Create a new tmsThread.
     *
     * @param tmsThreadDTO the tmsThreadDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new tmsThreadDTO, or with status 400 (Bad Request) if the tmsThread has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/tms-threads")
    @Timed
    public ResponseEntity<TmsThreadDTO> createTmsThread(@Valid @RequestBody TmsThreadDTO tmsThreadDTO) throws URISyntaxException {
        log.debug("REST request to save TmsThread : {}", tmsThreadDTO);
        if (tmsThreadDTO.getId() != null) {
            throw new BadRequestAlertException("A new tmsThread cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TmsThreadDTO result = tmsThreadService.save(tmsThreadDTO);
        return ResponseEntity.created(new URI("/api/tms-threads/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }
    
    /**
     * conghk
     * GET  /issues : create issues.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of bugs in body
     * @throws IOException 
     */
    @RequestMapping(value = "/tms-threads/create", method = RequestMethod.POST,
    headers = {"content-type=multipart/mixed","content-type=multipart/form-data", "content-type=application/octet-stream",
    		"content-type=application/json", "content-type=application/x-www-form-urlencoded"})
    @Timed
    public ResponseEntity<TmsThreadDTO> createQuestionAndAnswer(@RequestPart(value = "questionAndAnswer", required = true) QuestionAndAnswerVM payload) throws IOException {
        log.debug("REST request to create issues");
        TmsThreadDTO tmsThreadDTO = this.tmsThreadService.createQuestionAndAnswer(payload);
        return new ResponseEntity<>(tmsThreadDTO, HttpStatus.OK);
    }

    /**
     * PUT  /tms-threads : Updates an existing tmsThread.
     *
     * @param tmsThreadDTO the tmsThreadDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated tmsThreadDTO,
     * or with status 400 (Bad Request) if the tmsThreadDTO is not valid,
     * or with status 500 (Internal Server Error) if the tmsThreadDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/tms-threads")
    @Timed
    public ResponseEntity<TmsThreadDTO> updateTmsThread(@Valid @RequestBody TmsThreadDTO tmsThreadDTO) throws URISyntaxException {
        log.debug("REST request to update TmsThread : {}", tmsThreadDTO);
        if (tmsThreadDTO.getId() == null) {
            return createTmsThread(tmsThreadDTO);
        }
        TmsThreadDTO result = tmsThreadService.save(tmsThreadDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, tmsThreadDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /tms-threads : get all the tmsThreads.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of tmsThreads in body
     */
    @GetMapping("/tms-threads")
    @Timed
    public ResponseEntity<List<TmsThreadDTO>> getAllTmsThreads(TmsThreadCriteria criteria, Pageable pageable) {
        log.debug("REST request to get TmsThreads by criteria: {}", criteria);
        Page<TmsThreadDTO> page = tmsThreadQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/tms-threads");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /tms-threads/:id : get the "id" tmsThread.
     *
     * @param id the id of the tmsThreadDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the tmsThreadDTO, or with status 404 (Not Found)
     */
    @GetMapping("/tms-threads/{id}")
    @Timed
    public ResponseEntity<TmsThreadDTO> getTmsThread(@PathVariable Long id) {
        log.debug("REST request to get TmsThread : {}", id);
        TmsThreadDTO tmsThreadDTO = tmsThreadService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(tmsThreadDTO));
    }
    
    /**
     * GET  /thread/:projectId/:filter : get thread by project "id" and filter.
     *
     * @param id the id of the TmsThreadDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the TmsThreadDTO, or with status 404 (Not Found)
     * @PreAuthorize("@jhiAuth.hasPermission('NOTES', 'VIEW')")
     */
    @GetMapping("/tms-threads/{projectId}/{filter}")
    @Timed
    public ResponseEntity<List<TmsThreadDTO>> getQuestionAndAnswer(@PathVariable("projectId") Long projectId,
    		@PathVariable("filter") String filter, Pageable pageable) {
        log.debug("REST EXTERNAL request to get thread by filter ", filter);
        Page<TmsThreadDTO> page = this.tmsThreadQueryService.findQuestionAndAnswer(projectId, filter, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/tms-threads/{projectId}/{filter}");
        
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    
    /**
     * PUT  /thread/updateViews : update view of thread thread by thread "id".
     *
     * @param id the id of the TmsThreadDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the TmsThreadDTO, or with status 404 (Not Found)
     * @PreAuthorize("@jhiAuth.hasPermission('NOTES', 'VIEW')")
     */
    @PutMapping("/tms-threads/updateViews")
    @Timed
    public ResponseEntity<Void> updateViewsOfThread(@RequestParam("threadId") Long threadId) {
        log.debug("REST EXTERNAL request to update thread ", threadId);
        this.tmsThreadService.updateViewsOfThread(threadId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * DELETE  /tms-threads/:id : delete the "id" tmsThread.
     *
     * @param id the id of the tmsThreadDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/tms-threads/{id}")
    @Timed
    public ResponseEntity<Void> deleteTmsThread(@PathVariable Long id) {
        log.debug("REST request to delete TmsThread : {}", id);
        tmsThreadService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/tms-threads?query=:query : search for the tmsThread corresponding
     * to the query.
     *
     * @param query the query of the tmsThread search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/tms-threads")
    @Timed
    public ResponseEntity<List<TmsThreadDTO>> searchTmsThreads(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of TmsThreads for query {}", query);
        Page<TmsThreadDTO> page = tmsThreadService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/tms-threads");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    
    /**
     * PUT  /thread/updateViews : update view of thread thread by thread "id".
     *
     * @param id the id of the TmsThreadDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the TmsThreadDTO, or with status 404 (Not Found)
     * @PreAuthorize("@jhiAuth.hasPermission('NOTES', 'VIEW')")
     */
    @PutMapping("/tms-threads/changeTitleOfThread")
    @Timed
    public ResponseEntity<TmsThreadDTO> updateTitleOfThread(@RequestParam("id") Long id, @RequestParam("title") String title) {
        log.debug("REST EXTERNAL request to update thread ", id);
        TmsThreadDTO tmsThreadDTO = this.tmsThreadService.updateTitleOfThread(id, title);
        return ResponseEntity.ok().body(tmsThreadDTO);
    }
    
    /**
     * PUT  /thread/updateViews : update view of thread thread by thread "id".
     *
     * @param id the id of the TmsThreadDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the TmsThreadDTO, or with status 404 (Not Found)
     * @PreAuthorize("@jhiAuth.hasPermission('NOTES', 'VIEW')")
     */
    @PutMapping("/tms-threads/changeThreadToClose")
    @Timed
    public ResponseEntity<Void> updateThreadToClose(@RequestParam("threadId") Long threadId) {
        log.debug("REST EXTERNAL request to update thread ", threadId);
        this.tmsThreadService.updateThreadToClose(threadId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
