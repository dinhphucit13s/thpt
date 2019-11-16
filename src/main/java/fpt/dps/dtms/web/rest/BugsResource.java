package fpt.dps.dtms.web.rest;
import fpt.dps.dtms.domain.Bugs;
import com.codahale.metrics.annotation.Timed;
import fpt.dps.dtms.service.BugsService;
import fpt.dps.dtms.service.BugsQueryService;
import fpt.dps.dtms.web.rest.errors.BadRequestAlertException;
import fpt.dps.dtms.web.rest.util.HeaderUtil;
import fpt.dps.dtms.web.rest.util.PaginationUtil;
import fpt.dps.dtms.service.dto.AttachmentsDTO;
import fpt.dps.dtms.service.dto.BugsDTO;
import fpt.dps.dtms.service.dto.ProjectUsersDTO;
import fpt.dps.dtms.service.dto.TasksDTO;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Bugs.
 */
@RestController
@RequestMapping("/api")
public class BugsResource {

    private final Logger log = LoggerFactory.getLogger(BugsResource.class);

    private static final String ENTITY_NAME = "bugs";

    private final BugsService bugsService;
    
    private final BugsQueryService bugsQueryService;

    public BugsResource(BugsService bugsService, BugsQueryService bugsQueryService) {
        this.bugsService = bugsService;
        this.bugsQueryService = bugsQueryService;
    }

    /**
     * POST  /bugs : Create a new bugs.
     *
     * @param bugsDTO the bugsDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new bugsDTO, or with status 400 (Bad Request) if the bugs has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/bugs")
    @Timed
    public ResponseEntity<BugsDTO> createBugs(@Valid @RequestBody BugsDTO bugsDTO) throws URISyntaxException {
        log.debug("REST request to save Bugs : {}", bugsDTO);
        if (bugsDTO.getId() != null) {
            throw new BadRequestAlertException("A new bugs cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BugsDTO result = bugsService.save(bugsDTO);
        return ResponseEntity.created(new URI("/api/bugs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /bugs : Updates an existing bugs.
     *
     * @param bugsDTO the bugsDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated bugsDTO,
     * or with status 400 (Bad Request) if the bugsDTO is not valid,
     * or with status 500 (Internal Server Error) if the bugsDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/bugs")
    @Timed
    public ResponseEntity<BugsDTO> updateBugs(@Valid @RequestBody BugsDTO bugsDTO) throws URISyntaxException {
        log.debug("REST request to update Bugs : {}", bugsDTO);
        if (bugsDTO.getId() == null) {
            return createBugs(bugsDTO);
        }
        BugsDTO result = bugsService.save(bugsDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, bugsDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /bugs : get all the bugs.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of bugs in body
     */
    @GetMapping("/bugs")
    @Timed
    public ResponseEntity<List<BugsDTO>> getAllBugs(Pageable pageable) {
        log.debug("REST request to get a page of Bugs");
        Page<BugsDTO> page = bugsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/bugs");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    
    @GetMapping("/bugs-list-by-Task")
    @Timed
    public ResponseEntity<List<Bugs>> getAllBugsByTaskId(@RequestParam(value = "taskId", required = false) Long taskId, Pageable pageable) {
        log.debug("REST request to get a page of Bugs");
        Page<Bugs> page = bugsQueryService.findBugsByTaskId(taskId, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/bugs-list-by-Task");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /bugs/:id : get the "id" bugs.
     *
     * @param id the id of the bugsDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the bugsDTO, or with status 404 (Not Found)
     */
    @GetMapping("/bugs/{id}")
    @Timed
    public ResponseEntity<BugsDTO> getBugs(@PathVariable Long id) {
        log.debug("REST request to get Bugs : {}", id);
        BugsDTO bugsDTO = bugsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(bugsDTO));
    }
    
    @GetMapping("/bugs-count-bug-user-assign")
    @Timed
    public ResponseEntity<Integer> countBugByUserAssign(@RequestParam(value = "taskId", required = false) Long taskId,
    		@RequestParam(value = "rowRV", required = false) String rowRV) {
        log.debug("Count bug by tasks Id : {}", taskId);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(bugsQueryService.countBugByUserAssign(taskId, rowRV)));
    }
    
    @PostMapping("/bugs-count-List-user-assign")
    @Timed
    public ResponseEntity<String> countBugByUserAssignList(@Valid @RequestBody List<TasksDTO> tasksDTOs,
    		@RequestParam(value = "userLogin", required = false) String userLogin) {
        log.debug("Count bug by userLogin : {}", userLogin);
        
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(bugsQueryService.countBugByUserAssignList(tasksDTOs, userLogin)));
    }

    /**
     * DELETE  /bugs/:id : delete the "id" bugs.
     *
     * @param id the id of the bugsDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/bugs/{id}")
    @Timed
    public ResponseEntity<Void> deleteBugs(@PathVariable Long id) {
        log.debug("REST request to delete Bugs : {}", id);
        bugsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
    
    /**
     * GET  /notes/tasks/:id : get notes the by task "id".
     *
     * @param id the id of the notesDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the notesDTO, or with status 404 (Not Found)
     * @PreAuthorize("@jhiAuth.hasPermission('NOTES', 'VIEW')")
     */
    @GetMapping("/bugs/tasks/{id}")
    @Timed
    public ResponseEntity<List<BugsDTO>> getBugsByTaskId(@PathVariable Long id) {
        log.debug("REST EXTERNAL request to get Bugs by tasks Id : {}", id);
        List<BugsDTO> listBugs = new ArrayList<>();

        listBugs = bugsQueryService.findBugsByTaskId(id);
        return new ResponseEntity<>(listBugs, HttpStatus.OK);
    }

    /**
     * SEARCH  /_search/bugs?query=:query : search for the bugs corresponding
     * to the query.
     *
     * @param query the query of the bugs search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/bugs")
    @Timed
    public ResponseEntity<List<BugsDTO>> searchBugs(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Bugs for query {}", query);
        Page<BugsDTO> page = bugsService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/bugs");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    
    /**
     * conghk
     * GET  /bugs : get all bug list defaults.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of bugs in body
     */
    @GetMapping("/bugs/attachment")
    @Timed
    public ResponseEntity<List<AttachmentsDTO>> getAttachmentByBugId(@RequestParam(value = "bugId", required = false) Long bugId) {
        log.debug("REST request to get a attachment list of Bugs");
        List<AttachmentsDTO> attachmentList = new ArrayList<>();
        attachmentList = this.bugsService.getAttachmentByBugId(bugId);
        return new ResponseEntity<>(attachmentList, HttpStatus.OK);
    }

}
