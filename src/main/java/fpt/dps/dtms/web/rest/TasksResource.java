package fpt.dps.dtms.web.rest;

import com.codahale.metrics.annotation.Timed;

import fpt.dps.dtms.service.TasksService;
import fpt.dps.dtms.web.rest.errors.BadRequestAlertException;
import fpt.dps.dtms.web.rest.util.HeaderUtil;
import fpt.dps.dtms.web.rest.util.PaginationUtil;
import fpt.dps.dtms.service.dto.TasksDTO;
import fpt.dps.dtms.service.util.AppConstants;
import fpt.dps.dtms.service.util.CommonFunction;
import fpt.dps.dtms.service.dto.PurchaseOrdersDTO;
import fpt.dps.dtms.service.dto.SelectDTO;
import fpt.dps.dtms.service.dto.TaskBiddingDTO;
import fpt.dps.dtms.service.dto.TasksCriteria;
import fpt.dps.dtms.service.PackagesQueryService;
import fpt.dps.dtms.service.TasksQueryService;
import io.github.jhipster.web.util.ResponseUtil;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import fpt.dps.dtms.web.rest.vm.FieldConfigVM;
import fpt.dps.dtms.web.rest.vm.IdListVM;
import fpt.dps.dtms.web.rest.vm.TMSDynamicCustomFieldVM;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Tasks.
 */
@RestController
@RequestMapping("/api")
public class TasksResource {

    private final Logger log = LoggerFactory.getLogger(TasksResource.class);

    private static final String ENTITY_NAME = "tasks";

    private final TasksService tasksService;

    private final TasksQueryService tasksQueryService;

    public TasksResource(TasksService tasksService, TasksQueryService tasksQueryService) {
        this.tasksService = tasksService;
        this.tasksQueryService = tasksQueryService;
    }

    /**
     * POST  /tasks : Create a new tasks.
     *
     * @param tasksDTO the tasksDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new tasksDTO, or with status 400 (Bad Request) if the tasks has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/tasks")
    @Timed
    @PreAuthorize("@jhiAuth.hasPermission('TASK', 'CREATE')")
    public ResponseEntity<TasksDTO> createTasks(@Valid @RequestBody TasksDTO tasksDTO) throws URISyntaxException {
        log.debug("REST request to save Tasks : {}", tasksDTO);
        if (tasksDTO.getId() != null) {
            throw new BadRequestAlertException("A new tasks cannot already have an ID", ENTITY_NAME, "idexists");
        }
        tasksDTO.setData(StringUtils.EMPTY);
        TasksDTO result = tasksService.save(tasksDTO, null);
        return ResponseEntity.created(new URI("/api/tasks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /tasks : Updates an existing tasks.
     *
     * @param tasksDTO the tasksDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated tasksDTO,
     * or with status 400 (Bad Request) if the tasksDTO is not valid,
     * or with status 500 (Internal Server Error) if the tasksDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/tasks")
    @Timed
    @PreAuthorize("@jhiAuth.hasPermission('TASK', 'EDIT')")
    public ResponseEntity<TasksDTO> updateTasks(@Valid @RequestBody TasksDTO tasksDTO) throws URISyntaxException {
        log.debug("REST request to update Tasks : {}", tasksDTO);
        if (tasksDTO.getId() == null) {
            return createTasks(tasksDTO);
        }
        TasksDTO result = tasksService.save(tasksDTO, null);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, tasksDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /tasks : get all the tasks.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of tasks in body
     */
    @GetMapping("/tasks")
    @Timed
    @PreAuthorize("@jhiAuth.hasPermission('TASK', 'VIEW')")
    public ResponseEntity<List<TasksDTO>> getAllTasks(TasksCriteria criteria,
    		@RequestParam(value = "packageId", required = false) Long packageId,
    		Pageable pageable) {
        log.debug("REST request to get Tasks by criteria: {}", criteria);
        if(packageId != null) {
        	Page<TasksDTO> page = tasksQueryService.findTasksByPackageId(packageId, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/tasks");
            return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
        } else {
        	Page<TasksDTO> page = tasksQueryService.findByCriteria(criteria, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/tasks");
            return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
        }
    }

   /* @GetMapping("/tasks/bug-tracking-task")
    @Timed
    public ResponseEntity<List<Map<String, Object>>> getBugTrackingTasks(
    		@RequestParam(value="packId", required = false) Long packId,
    		@RequestParam(value="purchaseOrderId", required = false) Long purchaseOrderId,
    		@RequestParam(value="projectId", required = false) Long projectId,
    		Pageable pageable) {
    	log.debug("REST request to get bug tracking of tasks : {}", packId);
    	List<Map<String, Object>> result = tasksQueryService.getBugTrackingTasks(projectId, purchaseOrderId, packId, pageable);
        int totalPage = Integer.parseInt(result.get(result.size() - 1).get("total").toString());
        result.remove(result.size() - 1);
    	Page<Map<String, Object>> page = new PageImpl<>(result, pageable, totalPage);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/tasks/bug-tracking-task");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }*/

    /*@GetMapping("/tasks/ratio-bug")
    @Timed
    public SelectDTO taskRatioBugs(
    		@RequestParam(value="packId", required = false) Long packId,
    		@RequestParam(value="purchaseOrderId", required = false) Long purchaseOrderId,
    		@RequestParam(value="projectId", required = false) Long projectId) {
    	return tasksQueryService.taskRatioBugs(projectId, purchaseOrderId, packId);
    }*/

    @GetMapping("/tasks/fields/{id}")
    @Timed
    public ResponseEntity<List<FieldConfigVM>> getTaskFieldConfig(@PathVariable Long id) {
        log.debug("REST request to get all Packages relate to Project : {}", id);
        List<FieldConfigVM> objects = tasksService.getTaskFieldConfig(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(objects));
    }

    @GetMapping("/tasks/dynamic-fields/{id}")
    @Timed
    public ResponseEntity<TMSDynamicCustomFieldVM> getTaskDynamicFieldConfig(@PathVariable Long id) {
    	log.debug("REST request to get all Packages relate to Project : {}", id);
        TMSDynamicCustomFieldVM objects = tasksService.getTaskDynamicFieldConfig(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(objects));
    }

   /* @GetMapping("/tasks/tracking-member")
    @Timed
    public ResponseEntity<List<Map<String, Object>>> getBugTrackingMembers(@RequestParam(value="member", required = false) String member,
    		@RequestParam(value="packId", required = false) Long packId,
    		@RequestParam(value="projectId", required = false) Long projectId,
    		@RequestParam(value="purchaseOrderId", required = false) Long purchaseOrderId,
    		Pageable pageable) {
    	log.debug("find all task assign for member: {}", member);
    	List<Map<String, Object>> result = tasksQueryService.getBugTrackingMembers(projectId, purchaseOrderId, packId, pageable);
    	int totalPage = Integer.parseInt(result.get(result.size() - 1).get("total").toString());
        result.remove(result.size() - 1);
    	Page<Map<String, Object>> page = new PageImpl<>(result, pageable, totalPage);
    	HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/tasks/tracking-member");
    	return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }*/

    /**
     * GET  /tasks/:id : get the "id" tasks.
     *
     * @param id the id of the tasksDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the tasksDTO, or with status 404 (Not Found)
     */
    @GetMapping("/tasks/{id}")
    @Timed
    @PreAuthorize("@jhiAuth.hasPermission('TASK', 'VIEW')")
    public ResponseEntity<TasksDTO> getTasks(@PathVariable Long id) {
        log.debug("REST request to get Tasks : {}", id);
        TasksDTO tasksDTO = tasksService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(tasksDTO));
    }

    /**
     * DELETE  /tasks/:id : delete the "id" tasks.
     *
     * @param id the id of the tasksDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/tasks/{id}")
    @Timed
    @PreAuthorize("@jhiAuth.hasPermission('TASK', 'DELETE')")
    public ResponseEntity<Void> deleteTasks(@PathVariable Long id) {
        log.debug("REST request to delete Tasks : {}", id);
        try {
        	tasksService.delete(id);
        	return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
        } catch(Exception e) {
        	return ResponseEntity.badRequest().headers(HeaderUtil.serviceFailureAlert(ENTITY_NAME, AppConstants.IN_USED, "A Tasks cannot delete")).build();
        }
    }

    /**
     * SEARCH  /_search/tasks?query=:query : search for the tasks corresponding
     * to the query.
     *
     * @param query the query of the tasks search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/tasks")
    @Timed
    @PreAuthorize("@jhiAuth.hasPermission('TASK', 'VIEW')")
    public ResponseEntity<List<TasksDTO>> searchTasks(@RequestParam Long packageId, 
    		@RequestParam String taskName, 
    		@RequestParam String taskStatus, 
    		@RequestParam String assignee, 
    		@RequestParam String description, 
    		@RequestParam String from,
    		@RequestParam String to,
    		Pageable pageable) {
        String query = String.format("%s| %s |%s | %s | %s | %s", taskName , taskStatus , assignee , description, from, to);
        Instant fromDateInstant = CommonFunction.convertLocalDateToInstant(from);
    	Instant toDateInstant = CommonFunction.convertLocalDateToInstantPlusOne(to);
    	log.debug("REST request to search for a page of Tasks for query {}", query);
        Page<TasksDTO> page = tasksService.search(packageId, taskName, taskStatus, assignee, description, fromDateInstant, toDateInstant, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/tasks");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * Export tasks to excel file
     * @param packageId
     * @return excel file
     * @author TuHP
     */
    @GetMapping("/tasks/export")
	@Timed
	public ResponseEntity<InputStreamResource> download(HttpServletResponse response,
			@RequestParam(value = "packageId", required = true) Long packageId,
			@RequestParam String taskName, 
    		@RequestParam String taskStatus, 
    		@RequestParam String assignee, 
    		@RequestParam String description, 
    		@RequestParam String from,
    		@RequestParam String to) throws IOException {
		log.info("REST request to export template : {}", packageId);
		Instant fromDateInstant = CommonFunction.convertLocalDateToInstant(from);
    	Instant toDateInstant = CommonFunction.convertLocalDateToInstantPlusOne(to);
		InputStreamResource in = this.tasksService.exportExcel(packageId, taskName, taskStatus, assignee, description, fromDateInstant, toDateInstant);
		HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=task_export.xlsx");

		 return ResponseEntity
	                .ok()
	                .headers(headers)
	                .body(in);
	}

    /**
     * Import task from excel file
     * @param file: excel file
     * @param packageID
     * @author TuHP
     * */
    @RequestMapping(value = "/tasks/import", method = RequestMethod.POST,
    	    headers = {"content-type=application/octet-stream", "content-type=multipart/mixed","content-type=multipart/form-data",
    	    		"content-type=application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",})
    @Timed
    public ResponseEntity<String> taskImport(@RequestParam("excelFile") MultipartFile file, @Valid @RequestPart(value = "packageId", required = true) Long packageID) {
    	try {
    		tasksService.updateTasks(file, packageID);
    		return ResponseEntity.ok().headers(HeaderUtil.importAlert(ENTITY_NAME, "successfully")).build();
		} catch (Exception e) {
			return ResponseEntity.badRequest().headers(HeaderUtil.serviceFailureAlert(ENTITY_NAME, AppConstants.IMPORT_FAILED, "Import was failed")).build();
		}
	}

    /**
     * this method get All task UnAssign By Project
     * @param projectId
     * @param pageable
     * @return
     * @author KimHQ
     */
     @GetMapping("/task/task-un-assign")
     @Timed
     public ResponseEntity<List<TasksDTO>> getAllTaskUnAssignByProject(@RequestParam("workFlow") List<String> workFlow, @RequestParam("projectId") Long projectId,
     		@RequestParam("purchaseOrderId") Long purchaseOrderId, @RequestParam("packageId") Long packageId,
     		Pageable pageable) {
         log.debug("get all task un-assign by project: {}, page: {}", projectId, pageable);
         Page<TasksDTO> page = tasksQueryService.getAllTaskUnAssign(workFlow, projectId, purchaseOrderId, packageId, pageable);
         HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/task-biddings");
         return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
     }

     /**
      * get List Task Clone
      * @param packageId
      * @param pageable
      * @return
      * @author PhuVD3
      */
      @GetMapping("/task/task-list-clone")
      @Timed
      public ResponseEntity<List<TasksDTO>> getTasksListClone(@RequestParam(value = "packageId") Long packageId) {
          log.debug("REST request to get Task by criteria: {}", packageId);
          List<TasksDTO> page = tasksService.findListTasksClone(packageId);
          return new ResponseEntity<>(page, HttpStatus.OK);
      }

      /**
       * moveTasks on packages
       * @param packageId
       * @param idList
       * @return
       * @throws URISyntaxException
       */
      @PostMapping("/tasks/moveTasks/{pacId}")
      @Timed
      public ResponseEntity<IdListVM> createTaskBidding(@PathVariable("pacId") Long packageId, @RequestBody IdListVM idList) throws URISyntaxException {
          log.debug("REST request to save Tasks have id: {}", idList);
          tasksService.moveTasksList(idList, packageId);
          return ResponseEntity.ok().body(idList);
      }
}
