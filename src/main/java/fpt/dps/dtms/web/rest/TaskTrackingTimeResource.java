package fpt.dps.dtms.web.rest;

import com.codahale.metrics.annotation.Timed;
import fpt.dps.dtms.domain.TaskTrackingTime;
import fpt.dps.dtms.service.TaskTrackingTimeQueryService;
import fpt.dps.dtms.service.TaskTrackingTimeService;
import fpt.dps.dtms.service.util.AppConstants;
import fpt.dps.dtms.web.rest.errors.BadRequestAlertException;
import fpt.dps.dtms.web.rest.util.HeaderUtil;
import fpt.dps.dtms.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing TaskTrackingTime.
 */
@RestController
@RequestMapping("/api")
public class TaskTrackingTimeResource {

    private final Logger log = LoggerFactory.getLogger(TaskTrackingTimeResource.class);

    private static final String ENTITY_NAME = "taskTrackingTime";

    private final TaskTrackingTimeService taskTrackingTimeService;
    
    private final TaskTrackingTimeQueryService taskTrackingTimeQueryService;

    public TaskTrackingTimeResource(TaskTrackingTimeService taskTrackingTimeService, TaskTrackingTimeQueryService taskTrackingTimeQueryService) {
        this.taskTrackingTimeService = taskTrackingTimeService;
        this.taskTrackingTimeQueryService = taskTrackingTimeQueryService;
    }

    /**
     * POST  /task-tracking-times : Create a new taskTrackingTime.
     *
     * @param taskTrackingTime the taskTrackingTime to create
     * @return the ResponseEntity with status 201 (Created) and with body the new taskTrackingTime, or with status 400 (Bad Request) if the taskTrackingTime has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/task-tracking-times")
    @Timed
    public ResponseEntity<TaskTrackingTime> createTaskTrackingTime(@Valid @RequestBody TaskTrackingTime taskTrackingTime) throws URISyntaxException {
        log.debug("REST request to save TaskTrackingTime : {}", taskTrackingTime);
        if (taskTrackingTime.getId() != null) {
            throw new BadRequestAlertException("A new taskTrackingTime cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TaskTrackingTime result = taskTrackingTimeService.save(taskTrackingTime);
        return ResponseEntity.created(new URI("/api/task-tracking-times/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /task-tracking-times : Updates an existing taskTrackingTime.
     *
     * @param taskTrackingTime the taskTrackingTime to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated taskTrackingTime,
     * or with status 400 (Bad Request) if the taskTrackingTime is not valid,
     * or with status 500 (Internal Server Error) if the taskTrackingTime couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/task-tracking-times")
    @Timed
    public ResponseEntity<TaskTrackingTime> updateTaskTrackingTime(@Valid @RequestBody TaskTrackingTime taskTrackingTime) throws URISyntaxException {
        log.debug("REST request to update TaskTrackingTime : {}", taskTrackingTime);
        if (taskTrackingTime.getId() == null) {
            return createTaskTrackingTime(taskTrackingTime);
        }
        TaskTrackingTime result = taskTrackingTimeService.save(taskTrackingTime);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, taskTrackingTime.getId().toString()))
            .body(result);
    }

    /**
     * GET  /task-tracking-times : get all the taskTrackingTimes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of taskTrackingTimes in body
     */
    @GetMapping("/task-tracking-times")
    @Timed
    public ResponseEntity<List<TaskTrackingTime>> getAllTaskTrackingTimes(Pageable pageable) {
        log.debug("REST request to get a page of TaskTrackingTimes");
        Page<TaskTrackingTime> page = taskTrackingTimeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/task-tracking-times");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /task-tracking-times/:id : get the "id" taskTrackingTime.
     *
     * @param id the id of the taskTrackingTime to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the taskTrackingTime, or with status 404 (Not Found)
     */
    @GetMapping("/task-tracking-times/{id}")
    @Timed
    public ResponseEntity<TaskTrackingTime> getTaskTrackingTime(@PathVariable Long id) {
        log.debug("REST request to get TaskTrackingTime : {}", id);
        TaskTrackingTime taskTrackingTime = taskTrackingTimeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(taskTrackingTime));
    }

    /**
     * DELETE  /task-tracking-times/:id : delete the "id" taskTrackingTime.
     *
     * @param id the id of the taskTrackingTime to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/task-tracking-times/{id}")
    @Timed
    public ResponseEntity<Void> deleteTaskTrackingTime(@PathVariable Long id) {
        log.debug("REST request to delete TaskTrackingTime : {}", id);
        try {
        	taskTrackingTimeService.delete(id);
        	return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
        } catch(Exception e) {
        	return ResponseEntity.badRequest().headers(HeaderUtil.serviceFailureAlert(ENTITY_NAME, AppConstants.IN_USED, "A Task Tracking Time cannot delete")).build();
        }
    }
    
    @GetMapping("/task-tracking-times/effort")
    @Timed
    public ResponseEntity<List<Map<String, Object>>> getEffortOfMonth(
    		@RequestParam(value="packId", required = false) Long packId,
    		@RequestParam(value="purchaseOrderId", required = false) Long purchaseOrderId,
    		@RequestParam(value="projectId", required = false) Long projectId) {
    	log.debug("REST request to get bug tracking of tasks : {}", packId);
    	List<Map<String, Object>> result = taskTrackingTimeQueryService.countEffortByMonth(projectId, purchaseOrderId, packId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    
    /**
     * SEARCH  /_search/task-tracking-times?query=:query : search for the taskTrackingTime corresponding
     * to the query.
     *
     * @param query the query of the taskTrackingTime search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/task-tracking-times")
    @Timed
    public ResponseEntity<List<TaskTrackingTime>> searchTaskTrackingTimes(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of TaskTrackingTimes for query {}", query);
        Page<TaskTrackingTime> page = taskTrackingTimeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/task-tracking-times");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
