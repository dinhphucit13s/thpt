package fpt.dps.dtms.web.rest;

import com.codahale.metrics.annotation.Timed;
import fpt.dps.dtms.service.TaskBiddingService;
import fpt.dps.dtms.web.rest.errors.BadRequestAlertException;
import fpt.dps.dtms.web.rest.util.HeaderUtil;
import fpt.dps.dtms.web.rest.util.PaginationUtil;
import fpt.dps.dtms.web.rest.vm.FieldConfigVM;
import fpt.dps.dtms.web.rest.vm.IdListVM;
import fpt.dps.dtms.web.rest.vm.TMSDynamicCustomFieldVM;
import fpt.dps.dtms.web.rest.vm.external.MultiFieldConfigVM;
import fpt.dps.dtms.service.dto.TaskBiddingDTO;
import fpt.dps.dtms.service.dto.TasksDTO;
import fpt.dps.dtms.service.util.AppConstants;
import fpt.dps.dtms.service.util.FieldConfigService;
import fpt.dps.dtms.service.dto.TaskBiddingCriteria;
import fpt.dps.dtms.service.TaskBiddingQueryService;
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
 * REST controller for managing TaskBidding.
 */
@RestController
@RequestMapping("/api")
public class TaskBiddingResource {

    private final Logger log = LoggerFactory.getLogger(TaskBiddingResource.class);

    private static final String ENTITY_NAME = "taskBidding";

    private final TaskBiddingService taskBiddingService;

    private final TaskBiddingQueryService taskBiddingQueryService;

    private final FieldConfigService fieldConfigService;

    public TaskBiddingResource(TaskBiddingService taskBiddingService, TaskBiddingQueryService taskBiddingQueryService,
    		FieldConfigService fieldConfigService) {
        this.taskBiddingService = taskBiddingService;
        this.taskBiddingQueryService = taskBiddingQueryService;
        this.fieldConfigService = fieldConfigService;
    }

    /**
     * POST  /task-biddings : Create a new taskBidding.
     *
     * @param taskBiddingDTO the taskBiddingDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new taskBiddingDTO, or with status 400 (Bad Request) if the taskBidding has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/task-biddings")
    @Timed
    public ResponseEntity<TaskBiddingDTO> createTaskBidding(@Valid @RequestBody TaskBiddingDTO taskBiddingDTO) throws URISyntaxException {
        log.debug("REST request to save TaskBidding : {}", taskBiddingDTO);
        if (taskBiddingDTO.getId() != null) {
            throw new BadRequestAlertException("A new taskBidding cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TaskBiddingDTO result = taskBiddingService.save(taskBiddingDTO);
        return ResponseEntity.created(new URI("/api/task-biddings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PostMapping("/task-biddings/create/{classify}")
    @Timed
    public ResponseEntity<IdListVM> createTaskBidding(@PathVariable("classify") String classify, @RequestBody IdListVM idList) throws URISyntaxException {
        log.debug("REST request to save TaskBidding : {}", idList);
        taskBiddingService.saveListTaskBidding(idList, classify);

        return ResponseEntity.ok().body(idList);
    }

    /**
     *
     *
     */
    @GetMapping("/task-biddings-mode")
    @Timed
    public ResponseEntity<List<TaskBiddingDTO>> getAllTasksBiddingByMode(@RequestParam("projectId") Long projectId,
    		@RequestParam("poId") Long poId, @RequestParam("packageId") Long packageId, @RequestParam("modeBidding") String modeBidding,
    		@RequestParam("userLogin") String userLogin, Pageable pageable) {
        log.debug("REST request to get Tasks by criteria: {}", projectId);
        Page<TaskBiddingDTO> page = this.taskBiddingQueryService.findAllTasksBiddingByMode(projectId, poId, packageId, modeBidding, userLogin, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/tasks-bidding");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);

    }

    @GetMapping("/task-biddings/fields/{proId}")
    @Timed
    public ResponseEntity<List<FieldConfigVM>> getTaskFieldConfig(@PathVariable("proId") Long proId) {
        log.debug("REST request to get all Packages relate to Project : {}", proId);
        TMSDynamicCustomFieldVM tmsDynamicCustomFieldVM = this.fieldConfigService.getAllFieldConfig(proId, AppConstants.TASK_ENTITY);
        // List<FieldConfigVM> objects = this.fieldConfigService.getAllFieldConfig(proId, AppConstants.TASK_ENTITY);
        List<FieldConfigVM> objects = tmsDynamicCustomFieldVM.getFieldConfigVMs();
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(objects));
    }

    /**
     * PUT  /task-biddings : Updates an existing taskBidding.
     *
     * @param taskBiddingDTO the taskBiddingDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated taskBiddingDTO,
     * or with status 400 (Bad Request) if the taskBiddingDTO is not valid,
     * or with status 500 (Internal Server Error) if the taskBiddingDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/task-biddings")
    @Timed
    public ResponseEntity<TaskBiddingDTO> updateTaskBidding(@Valid @RequestBody TaskBiddingDTO taskBiddingDTO) throws URISyntaxException {
        log.debug("REST request to update TaskBidding : {}", taskBiddingDTO);
        if (taskBiddingDTO.getId() == null) {
            return createTaskBidding(taskBiddingDTO);
        }
        TaskBiddingDTO result = taskBiddingService.save(taskBiddingDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, taskBiddingDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /task-biddings : get all the taskBiddings.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of taskBiddings in body
     */
    @GetMapping("/task-biddings")
    @Timed
    public ResponseEntity<List<TaskBiddingDTO>> getAllTaskBiddings(TaskBiddingCriteria criteria, Pageable pageable) {
        log.debug("REST request to get TaskBiddings by criteria: {}", criteria);
        Page<TaskBiddingDTO> page = taskBiddingQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/task-biddings");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /task-biddings/:id : get the "id" taskBidding.
     *
     * @param id the id of the taskBiddingDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the taskBiddingDTO, or with status 404 (Not Found)
     */
    @GetMapping("/task-biddings/{id}")
    @Timed
    public ResponseEntity<TaskBiddingDTO> getTaskBidding(@PathVariable Long id) {
        log.debug("REST request to get TaskBidding : {}", id);
        TaskBiddingDTO taskBiddingDTO = taskBiddingService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(taskBiddingDTO));
    }

    /**
     * DELETE  /task-biddings/:id : delete the "id" taskBidding.
     *
     * @param id the id of the taskBiddingDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/task-biddings/{id}")
    @Timed
    public ResponseEntity<Void> deleteTaskBidding(@PathVariable Long id) {
        log.debug("REST request to delete TaskBidding : {}", id);
        taskBiddingService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

	@GetMapping("/task-biddings/multi-fields")
    @Timed
    public ResponseEntity<List<MultiFieldConfigVM>> getTaskMultiFieldConfig(@RequestParam("projectId") Long projectId, @RequestParam("purchaseOrderId") Long purchaseOrderId) {
        log.debug("REST request to get all Packages relate to Project : {}", projectId);
        List<MultiFieldConfigVM> objects = this.taskBiddingService.getTaskMultiFieldConfig(projectId, purchaseOrderId);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(objects));
    }

    /**
     * DELETE  /task-biddings/:id : delete the "id" taskBidding.
     *
     * @param id the id of the taskBiddingDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @PutMapping("/task-un-biddings")
    @Timed
    public ResponseEntity<IdListVM> unTaskBidding(@RequestBody IdListVM idList) {
        log.debug("REST request to unbidding TaskBidding");
        List<Long> idArray = idList.getIdList();
        for (Long id : idArray) {
        	this.taskBiddingService.delete(id);
		}
        return ResponseEntity.ok().body(idList);
    }

    /**
     * SEARCH  /_search/task-biddings?query=:query : search for the taskBidding corresponding
     * to the query.
     *
     * @param query the query of the taskBidding search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/task-biddings")
    @Timed
    public ResponseEntity<List<TaskBiddingDTO>> searchTaskBiddings(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of TaskBiddings for query {}", query);
        Page<TaskBiddingDTO> page = taskBiddingService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/task-biddings");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
