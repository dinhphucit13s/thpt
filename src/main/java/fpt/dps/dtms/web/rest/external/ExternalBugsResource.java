package fpt.dps.dtms.web.rest.external;

import java.io.IOException;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.codahale.metrics.annotation.Timed;
import com.mysql.fabric.xmlrpc.base.Array;

import fpt.dps.dtms.domain.enumeration.BugStatus;
import fpt.dps.dtms.domain.enumeration.BugResolution;
import fpt.dps.dtms.service.BugsQueryService;
import fpt.dps.dtms.service.BugsService;
import fpt.dps.dtms.service.dto.AttachmentsDTO;
import fpt.dps.dtms.service.dto.BugListDefaultDTO;
import fpt.dps.dtms.service.dto.BugsCriteria;
import fpt.dps.dtms.service.dto.BugsDTO;
import fpt.dps.dtms.service.dto.NotesDTO;
import fpt.dps.dtms.service.dto.ProjectBugListDefaultDTO;
import fpt.dps.dtms.web.rest.errors.BadRequestAlertException;
import fpt.dps.dtms.web.rest.util.HeaderUtil;
import fpt.dps.dtms.web.rest.util.PaginationUtil;
import fpt.dps.dtms.web.rest.vm.external.LogBugVM;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing Bugs.
 */
@RestController
@RequestMapping("/api/external")
public class ExternalBugsResource {

    private final Logger log = LoggerFactory.getLogger(ExternalBugsResource.class);

    private static final String ENTITY_NAME = "bugs";

    private final BugsService bugsService;

    private final BugsQueryService bugsQueryService;

    public ExternalBugsResource(BugsService bugsService, BugsQueryService bugsQueryService) {
        this.bugsService = bugsService;
        this.bugsQueryService = bugsQueryService;
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
     * ngocvx1
     * GET  /notes/tasks/:id/:stage : get bugs of task for fixer by task "id" and "stage".
     *
     * @param id the id of the notesDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the notesDTO, or with status 404 (Not Found)
     * @PreAuthorize("@jhiAuth.hasPermission('NOTES', 'VIEW')")
     */
    @GetMapping("/bugs/tasks/{id}/{stage}")
    @Timed
    public ResponseEntity<List<BugsDTO>> getBugsForFixerByTaskIdAndStage(@PathVariable Long id, @PathVariable String stage) {
        log.debug("REST EXTERNAL request to get Bugs for fixer(OP) by tasks Id : {}", id);
        List<BugsDTO> listBugs = new ArrayList<>();

        BugsCriteria bugsCriteria = new BugsCriteria();
        
        LongFilter tasksIdFilter  = new LongFilter();
        tasksIdFilter.setEquals(id);
        bugsCriteria.setTasksId(tasksIdFilter);
        
        StringFilter stageFilter = new StringFilter();
        stageFilter.setEquals(stage);
        bugsCriteria.setStage(stageFilter);

        listBugs = bugsQueryService.findByCriteria(bugsCriteria);
        return new ResponseEntity<>(listBugs, HttpStatus.OK);
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
    @GetMapping("/bugs-projectBugList")
    @Timed
    public ResponseEntity<List<ProjectBugListDefaultDTO>> getBugListDefaultByProject(@RequestParam("projectId") Long projectId,
    		@RequestParam("tasksId") Long tasksId, @RequestParam("stage") String stage) {
        log.debug("REST request to get a bug list defaults of Project");
        List<ProjectBugListDefaultDTO> listBugDefaults = bugsService.getBugListDefaultByProject(projectId, tasksId, stage);
        return new ResponseEntity<>(listBugDefaults, HttpStatus.OK);
    }
    
    /**
     * conghk
     * GET  /bugs : get all bug list defaults.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of bugs in body
     * @throws IOException 
     */
    @RequestMapping(value = "/bugs/create", method = RequestMethod.POST,
    headers = {"content-type=multipart/mixed","content-type=multipart/form-data", "content-type=application/octet-stream",
    		"content-type=application/json", "content-type=application/x-www-form-urlencoded"})
    @Timed
    public ResponseEntity<List<BugsDTO>> createBugsOfTask(@RequestPart(value = "logBug", required = true) LogBugVM logBug) throws IOException {
        log.debug("REST request to create bug of task :" + logBug.getTaskId());
        List<BugsDTO> bugDtoList = bugsService.createBugsOfTask(logBug);
        return new ResponseEntity<>(bugDtoList, HttpStatus.OK);
    }
    
    /**
     * conghk
     * GET  /bugs : get all bug list defaults.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of bugs in body
     * @throws IOException 
     */
    @RequestMapping(value = "/bugs/updateBugsOpen", method = RequestMethod.POST,
    headers = {"content-type=multipart/mixed","content-type=multipart/form-data", "content-type=application/octet-stream",
    		"content-type=application/json", "content-type=application/x-www-form-urlencoded"})
    @Timed
    public ResponseEntity<List<BugsDTO>> updateBugsOpen(@RequestPart(value = "bugs", required = true) List<BugsDTO> bugs) throws IOException {
        log.debug("REST request to update bugs open bug of task :" + bugs.size());
        List<BugsDTO> bugDtoList = bugsService.updateBugsOpen(bugs);
        return new ResponseEntity<>(bugDtoList, HttpStatus.OK);
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
            return new ResponseEntity<BugsDTO>(HttpStatus.BAD_REQUEST);
        }
        BugsDTO result = bugsService.save(bugsDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, bugsDTO.getId().toString()))
            .body(result);
    }
    
    @RequestMapping(value = "/bugs/update", method = RequestMethod.PUT,
    headers = {"content-type=multipart/mixed","content-type=multipart/form-data", "content-type=application/octet-stream",
    		"content-type=application/json", "content-type=application/x-www-form-urlencoded"})
    @Timed
    public ResponseEntity<List<BugsDTO>> updateListBugs(@RequestPart(value = "bugsInfo", required = true) List<BugsDTO> bugsDTO, @RequestPart(value="round", required = false) String round) throws URISyntaxException {
    	log.debug("REST request to update Bugs : {}", bugsDTO);
        List<BugsDTO> resultList = new ArrayList<>();
        for (BugsDTO bugs: bugsDTO) {
            resultList.add(bugsService.save(bugs));
        }
//        return ResponseEntity.created(new URI("/api/bugs/update/" + resultList.get(0).getId()))
//                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, resultList.get(0).getId().toString()))
//                .body(resultList);
        return ResponseEntity.ok().body(resultList);
    }
    
    /**
     * Count bugs status 'OPEN' and 'REOPEN' by tasks Id and user create bugs
     * @param taskId
     * @param userLogin
     * @return quantity bug Open
     */
    
    @GetMapping("/bugs/count-open")
    @Timed
    public ResponseEntity<Integer> countBugOpenByTaskIdAndUserLog(@RequestParam(value = "taskId") Long taskId, @RequestParam(value = "userLogin") String userLogin) {
        log.debug("Count bug OPEN by tasks Id : {} and create by: {}", taskId, userLogin);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(bugsQueryService.countBugOpenByTaskIdAndUserLog(taskId, userLogin)));
    }
}
