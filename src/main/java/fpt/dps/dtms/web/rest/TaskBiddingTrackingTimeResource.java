package fpt.dps.dtms.web.rest;

import com.codahale.metrics.annotation.Timed;
import fpt.dps.dtms.service.TaskBiddingTrackingTimeService;
import fpt.dps.dtms.web.rest.errors.BadRequestAlertException;
import fpt.dps.dtms.web.rest.util.HeaderUtil;
import fpt.dps.dtms.web.rest.util.PaginationUtil;
import fpt.dps.dtms.service.dto.TaskBiddingTrackingTimeDTO;
import fpt.dps.dtms.service.dto.TaskBiddingTrackingTimeCriteria;
import fpt.dps.dtms.service.TaskBiddingTrackingTimeQueryService;
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

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing TaskBiddingTrackingTime.
 */
@RestController
@RequestMapping("/api")
public class TaskBiddingTrackingTimeResource {

    private final Logger log = LoggerFactory.getLogger(TaskBiddingTrackingTimeResource.class);

    private static final String ENTITY_NAME = "taskBiddingTrackingTime";

    private final TaskBiddingTrackingTimeService taskBiddingTrackingTimeService;

    private final TaskBiddingTrackingTimeQueryService taskBiddingTrackingTimeQueryService;

    public TaskBiddingTrackingTimeResource(TaskBiddingTrackingTimeService taskBiddingTrackingTimeService, TaskBiddingTrackingTimeQueryService taskBiddingTrackingTimeQueryService) {
        this.taskBiddingTrackingTimeService = taskBiddingTrackingTimeService;
        this.taskBiddingTrackingTimeQueryService = taskBiddingTrackingTimeQueryService;
    }

    /**
     * POST  /task-bidding-tracking-times : Create a new taskBiddingTrackingTime.
     *
     * @param taskBiddingTrackingTimeDTO the taskBiddingTrackingTimeDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new taskBiddingTrackingTimeDTO, or with status 400 (Bad Request) if the taskBiddingTrackingTime has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
//    @PostMapping("/task-bidding-tracking-times")
//    @Timed
//    public ResponseEntity<TaskBiddingTrackingTimeDTO> createTaskBiddingTrackingTime(@Valid @RequestBody TaskBiddingTrackingTimeDTO taskBiddingTrackingTimeDTO) throws URISyntaxException {
//        log.debug("REST request to save TaskBiddingTrackingTime : {}", taskBiddingTrackingTimeDTO);
//        if (taskBiddingTrackingTimeDTO.getId() != null) {
//            throw new BadRequestAlertException("A new taskBiddingTrackingTime cannot already have an ID", ENTITY_NAME, "idexists");
//        }
//        TaskBiddingTrackingTimeDTO result = taskBiddingTrackingTimeService.save(taskBiddingTrackingTimeDTO);
//        return ResponseEntity.created(new URI("/api/task-bidding-tracking-times/" + result.getId()))
//            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
//            .body(result);
//    }

    /**
     * PUT  /task-bidding-tracking-times : Updates an existing taskBiddingTrackingTime.
     *
     * @param taskBiddingTrackingTimeDTO the taskBiddingTrackingTimeDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated taskBiddingTrackingTimeDTO,
     * or with status 400 (Bad Request) if the taskBiddingTrackingTimeDTO is not valid,
     * or with status 500 (Internal Server Error) if the taskBiddingTrackingTimeDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
//    @PutMapping("/task-bidding-tracking-times")
//    @Timed
//    public ResponseEntity<TaskBiddingTrackingTimeDTO> updateTaskBiddingTrackingTime(@Valid @RequestBody TaskBiddingTrackingTimeDTO taskBiddingTrackingTimeDTO) throws URISyntaxException {
//        log.debug("REST request to update TaskBiddingTrackingTime : {}", taskBiddingTrackingTimeDTO);
//        if (taskBiddingTrackingTimeDTO.getId() == null) {
//            return createTaskBiddingTrackingTime(taskBiddingTrackingTimeDTO);
//        }
//        TaskBiddingTrackingTimeDTO result = taskBiddingTrackingTimeService.save(taskBiddingTrackingTimeDTO);
//        return ResponseEntity.ok()
//            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, taskBiddingTrackingTimeDTO.getId().toString()))
//            .body(result);
//    }

    /**
     * GET  /task-bidding-tracking-times : get all the taskBiddingTrackingTimes.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of taskBiddingTrackingTimes in body
     */
    @GetMapping("/task-bidding-tracking-times")
    @Timed
    public ResponseEntity<List<TaskBiddingTrackingTimeDTO>> getAllTaskBiddingTrackingTimes(TaskBiddingTrackingTimeCriteria criteria, Pageable pageable) {
        log.debug("REST request to get TaskBiddingTrackingTimes by criteria: {}", criteria);
        Page<TaskBiddingTrackingTimeDTO> page = taskBiddingTrackingTimeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/task-bidding-tracking-times");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /task-bidding-tracking-times/:id : get the "id" taskBiddingTrackingTime.
     *
     * @param id the id of the taskBiddingTrackingTimeDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the taskBiddingTrackingTimeDTO, or with status 404 (Not Found)
     */
    @GetMapping("/task-bidding-tracking-times/{id}")
    @Timed
    public ResponseEntity<TaskBiddingTrackingTimeDTO> getTaskBiddingTrackingTime(@PathVariable Long id) {
        log.debug("REST request to get TaskBiddingTrackingTime : {}", id);
        TaskBiddingTrackingTimeDTO taskBiddingTrackingTimeDTO = taskBiddingTrackingTimeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(taskBiddingTrackingTimeDTO));
    }

    /**
     * DELETE  /task-bidding-tracking-times/:id : delete the "id" taskBiddingTrackingTime.
     *
     * @param id the id of the taskBiddingTrackingTimeDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/task-bidding-tracking-times/{id}")
    @Timed
    public ResponseEntity<Void> deleteTaskBiddingTrackingTime(@PathVariable Long id) {
        log.debug("REST request to delete TaskBiddingTrackingTime : {}", id);
        taskBiddingTrackingTimeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/task-bidding-tracking-times?query=:query : search for the taskBiddingTrackingTime corresponding
     * to the query.
     *
     * @param query the query of the taskBiddingTrackingTime search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/task-bidding-tracking-times")
    @Timed
    public ResponseEntity<List<TaskBiddingTrackingTimeDTO>> searchTaskBiddingTrackingTimes(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of TaskBiddingTrackingTimes for query {}", query);
        Page<TaskBiddingTrackingTimeDTO> page = taskBiddingTrackingTimeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/task-bidding-tracking-times");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
