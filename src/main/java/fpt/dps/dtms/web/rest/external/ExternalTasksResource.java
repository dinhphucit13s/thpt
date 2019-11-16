package fpt.dps.dtms.web.rest.external;

import com.codahale.metrics.annotation.Timed;
import fpt.dps.dtms.service.TasksService;
import fpt.dps.dtms.web.rest.errors.BadRequestAlertException;
import fpt.dps.dtms.web.rest.util.HeaderUtil;
import fpt.dps.dtms.web.rest.util.PaginationUtil;
import fpt.dps.dtms.web.rest.vm.FieldConfigVM;
import fpt.dps.dtms.web.rest.vm.TMSDynamicCustomFieldVM;
import fpt.dps.dtms.web.rest.vm.TMSMonthViewVM;
import fpt.dps.dtms.web.rest.vm.external.MultiTasksReOpenVM;
import fpt.dps.dtms.service.dto.TasksDTO;
import fpt.dps.dtms.service.dto.BugsDTO;
import fpt.dps.dtms.service.dto.TasksCriteria;
import fpt.dps.dtms.security.SecurityUtils;
import fpt.dps.dtms.service.BugsQueryService;
import fpt.dps.dtms.service.TaskTrackingTimeService;
import fpt.dps.dtms.service.TasksQueryService;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.web.util.ResponseUtil;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author ngocvx1
 * REST EXTERNAL controller for managing Tasks.
 */
@RestController
@RequestMapping("/api/external")
public class ExternalTasksResource {

    private final Logger log = LoggerFactory.getLogger(ExternalTasksResource.class);

    private static final String ENTITY_NAME = "tasks";

    private final TasksService tasksService;

    private final TasksQueryService tasksQueryService;
    
    
    private final TaskTrackingTimeService taskTrackingTimeService;

    public ExternalTasksResource(TasksService tasksService, TasksQueryService tasksQueryService, TaskTrackingTimeService taskTrackingTimeService) {
        this.tasksService = tasksService;
        this.tasksQueryService = tasksQueryService;
        this.taskTrackingTimeService = taskTrackingTimeService;
    }

    /**
     * PUT  /tasks : Updates an existing tasks.
     *
     * @param tasksDTO the tasksDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated tasksDTO,
     * or with status 400 (Bad Request)	 if the tasksDTO is not valid,
     * or with status 500 (Internal Server Error) if the tasksDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/tasks")
    @Timed
    public ResponseEntity<TasksDTO> updateTasks(@Valid @RequestBody TasksDTO tasksDTO) throws URISyntaxException {
        log.debug("REST EXTERNAL request to update Tasks : {}", tasksDTO);
        if (tasksDTO.getId() == null) {
        	throw new BadRequestAlertException("A task must be have an id", "updateTask", "idnotexists");
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
	  public ResponseEntity<List<TasksDTO>> getAllTasksBelongToLoginUser(TasksCriteria criteria,
	    		@RequestParam(value = "packageId", required = false) Long packageId,
			Pageable pageable) {
	  		log.debug("REST EXTERNAL request to get Tasks by criteria: {}", criteria);
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

	@GetMapping("/tasksuser")
	@Timed
	public ResponseEntity<List<TasksDTO>> getAllTasksBelongToLoginUserByRole(@RequestParam("login") String login,
			@RequestParam("role") String role) {
		log.debug("REST EXTERNAL request to get Task of login : {} and role: {}", login, role);

		TasksCriteria criteria = new TasksCriteria();
		switch(role) {
			case "op":
				// criteria.setOp(new StringFilter().setContains(login));
				// break;
				List<TasksDTO> listTask1 =  tasksQueryService.findTasksByRoleOP(login);
				return new ResponseEntity<>(listTask1, HttpStatus.OK);
			case "review":
				criteria.setReview1(new StringFilter().setContains(login));
				criteria.setReview2(new StringFilter().setContains(login));
				break;
			case "fi":
				criteria.setFi(new StringFilter().setContains(login));
				break;
			case "fixer":
				criteria.setFixer(new StringFilter().setContains(login));
				break;
		}

		List<TasksDTO> listTask =  tasksQueryService.findByCriteriaTypeOr(criteria);

		return new ResponseEntity<>(listTask, HttpStatus.OK);
	  }
	
	/**
	 * Get task of user by projectId and task roles.
	 * @param roles: task roles
	 * @param userLogin
	 * @param projects: projectId
	 * @param pageable
	 * @return
	 */
	@GetMapping("/tasks/{roles}/{userLogin}/{projects}")
	@Timed
	public ResponseEntity<List<TasksDTO>> getTasksOfUserLoginByRolesInProjects(@PathVariable("roles") String roles,
			@PathVariable("userLogin") String userLogin, @PathVariable("projects") Long projects, @RequestParam("purchaseOrders") Long purchaseOrders,
			@RequestParam("packages") Long packages, @RequestParam("step") String step, Pageable pageable) {
		log.debug("REST EXTERNAL request to get Task of: {} and roles {} in project {}.", userLogin, roles, projects);
		if (StringUtils.EMPTY.equals(step)) {
			step = null;
		}
		Page<TasksDTO> page = tasksQueryService.findTasksByRolesInProjectsWithPaging(roles, userLogin, projects, purchaseOrders, packages, step, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/external/tasks/{roles}/{userLogin}/{projects}/{purchaseOrders}");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	  }

	
	/**
	 * Get task DONE of user by projectId and task roles.
	 * @param roles: task roles
	 * @param userLogin
	 * @param projects: projectId
	 * @param pageable
	 * @return
	 * @throws ParseException 
	 */
	@GetMapping("/tasks-done/{roles}/{userLogin}/{projects}")
	@Timed
	public ResponseEntity<List<TasksDTO>> getTasksDoneOfUserLoginByRolesInProjects(@PathVariable("roles") String roles,
			@PathVariable("userLogin") String userLogin, @PathVariable("projects") Long projects, @RequestParam("beginTime") String beginTime,
			@RequestParam("endTime") String endTime, Pageable pageable) throws ParseException {
		log.debug("REST EXTERNAL request to get Task of: {} and roles {} in project {}.", userLogin, roles, projects);
		Page<TasksDTO> page = tasksQueryService.findTasksDoneByRolesInProjectsWithPaging(roles, userLogin, projects, beginTime, endTime, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/external/tasks-done/{roles}/{userLogin}/{projects}");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	  }
	
    /**
     * GET  /tasks/:id : get Task by "id" tasks.
     *
     * @param id the id of the tasksDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the tasksDTO, or with status 404 (Not Found)
     */
    @GetMapping("/tasks/{id}")
    @Timed
    public ResponseEntity<TasksDTO> getTasks(@PathVariable Long id) {
        log.debug("REST EXTERNAL request to get Tasks : {}", id);
        TasksDTO tasksDTO = tasksService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(tasksDTO));
    }

    /**
     * SEARCH  /_search/tasks?query=:query : search for the tasks corresponding
     * to the query.
     *
     * @param query the query of the tasks search
     * @param pageable the pagination information
     * @return the result of the search
     */
    /*@GetMapping("/_search/tasks")
    @Timed
    public ResponseEntity<List<TasksDTO>> searchTasks(@RequestParam String query, Pageable pageable) {
        log.debug("REST EXTERNAL request to search for a page of Tasks for query {}", query);
        Page<TasksDTO> page = tasksService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/tasks");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }*/
    
    /**
     * Get OpGridDTO by project id
     * 
     * @param id: project id
     * @return
     */
    @GetMapping("/tasks/fields/{id}/tasks-op")
    @Timed
    public ResponseEntity<TMSDynamicCustomFieldVM> getOPTasksFieldConfigOpGridDTO(@PathVariable Long id) {
        log.debug("REST EXTERNAL request to get getTasksFieldConfigOpGridDTO of project: {}", id);
        TMSDynamicCustomFieldVM fieldConfigVMs = this.tasksService.getTasksFieldConfigOpGridDTO(id); 

        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(fieldConfigVMs));
    }
    
    @GetMapping("/tasks/dynamic-fields/{purchase-orders-id}")
    @Timed
    public ResponseEntity<TMSDynamicCustomFieldVM> getTaskDynamicFieldConfig(@PathVariable("purchase-orders-id") Long poId) {
    	log.debug("REST request to get all Packages relate to Project : {}", poId);
        TMSDynamicCustomFieldVM objects = tasksService.getTaskDynamicFieldConfig(poId);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(objects));
    }
    
    /**
     * GET  /tasks/:userLogin/doing : count task doing of user
     *
     * @param userLogin 
     * @return the ResponseEntity with status 200 (OK) and with body the number of tasks, or with status 404 (Not Found)
     */
    @GetMapping("/tasks/{userLogin}/doing")
    @Timed
    public ResponseEntity<Integer> countTasksDoingOfUserLogin(@PathVariable String userLogin) {
        log.debug("REST EXTERNAL request to count Tasks doing of: {}", userLogin);
        Integer result = tasksQueryService.countTasksDoingOfUserLogin(userLogin);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(result));
    }
    
    /**
     * GET  /tasks/:userLogin/doing : count task doing of user
     *
     * @param userLogin 
     * @return 
     * @return the ResponseEntity with status 200 (OK) and with body the number of tasks, or with status 404 (Not Found)
     */
    @GetMapping("/tasks/before-holding/{userLogin}")
    @Timed
    public ResponseEntity<HashMap<String, Object>> getConditionBeforeHoldingTask(@PathVariable String userLogin) {
        log.debug("REST EXTERNAL request to count Tasks doing of: {}", userLogin);
        Integer quantityTasksDoing = tasksQueryService.countTasksDoingOfUserLogin(userLogin);
        Integer quantityTasksLate = tasksQueryService.countTaskLateInTheEndDate(userLogin);
        HashMap<String, Object> condition = new HashMap<>();
        condition.put("quantityTasksDoing", quantityTasksDoing);
        condition.put("quantityTasksLate", quantityTasksLate);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(condition));
    }
    
    /**
     * GET  /tasks/:userLogin/pending : count task pending of user
     *
     * @param userLogin 
     * @return the ResponseEntity with status 200 (OK) and with body the number of tasks, or with status 404 (Not Found)
     */
    @GetMapping("/tasks/{userLogin}/pending")
    @Timed
    public ResponseEntity<Integer> countTasksPendingOfUserLogin(@PathVariable String userLogin) {
        log.debug("REST EXTERNAL request to count Tasks pending of: {}", userLogin);
        Integer result = tasksQueryService.countTasksPendingOfUserLogin(userLogin);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(result));
    }
    
	/**
	 * conghk
	 * @param userLogin
	 * @return
	 */
	@GetMapping("/tasks/all/{userLogin}/{month}")
	@Timed
	public ResponseEntity<List<TMSMonthViewVM>> getTasksByUserLogin(@PathVariable("userLogin") String userLogin,
			@PathVariable("month") int month) {
		log.debug("REST EXTERNAL request to get Task of user : ", userLogin);

		List<TMSMonthViewVM> listResources = new ArrayList<TMSMonthViewVM>();
		listResources =  tasksQueryService.findTasksByUserLogin(userLogin, month);

		return new ResponseEntity<>(listResources, HttpStatus.OK);
	  }
	
	/**
	 * conghk
	 * @param userLogin
	 * @return
	 */
	@GetMapping("/tasks/taskOfDay/{userLogin}/{dateFormat}")
	@Timed
	public ResponseEntity<List<TasksDTO>> showTasksOfDay(@PathVariable("userLogin") String userLogin,
			@PathVariable("dateFormat") String dateFormat) {
		log.debug("Get Task in the day of user : ", userLogin);

		List<TasksDTO> listTask = new ArrayList<TasksDTO>();
		listTask =  tasksQueryService.findTasksOfDay(userLogin, dateFormat);
		log.debug("listTask ", listTask);

		return new ResponseEntity<>(listTask, HttpStatus.OK);
	  }
	
	/**
	 * ngocvx1
	 * 
	 * @param roles: roles of user (op, review1, review2, fi, fixer)
	 * @param userLogin
	 * @param listStatus: list status to select (ex: "DOING_PENDING")
	 * @param pageable
	 * @return
	 */
	@GetMapping("/tasks/find/{roles}/{userLogin}/{listStatus}")
	@Timed
	public ResponseEntity<List<TasksDTO>> findTaskByRolesAndListStatusOfUser(@PathVariable("roles") String roles,
			@PathVariable("userLogin") String userLogin,
			@PathVariable("listStatus") String listStatus) {
		log.debug("Get Task by roles and list status task of user : ", userLogin); 
		List<TasksDTO> reuslt =  tasksQueryService.findTaskByRolesAndListStatusOfUser(roles, userLogin, listStatus);
		log.debug("listTask ", reuslt);

		return new ResponseEntity<>(reuslt, HttpStatus.OK);
	  }
	
    /**
     * conghk
     * GET  /bugs : get all bug list defaults.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of bugs in body
     * @throws IOException 
     */
    @RequestMapping(value = "/tasks/reopen-multi", method = RequestMethod.PUT,
    headers = {"content-type=multipart/mixed","content-type=multipart/form-data", "content-type=application/octet-stream",
    		"content-type=application/json", "content-type=application/x-www-form-urlencoded"})
    @Timed
    public ResponseEntity<List<TasksDTO>> reOpenMultiTasks(@RequestPart(value = "multiReOpen", required = true) MultiTasksReOpenVM multiTasksReOpenVM) throws IOException {
        log.debug("REST request to update bugs re open task");
        List<TasksDTO> taskDtoList = tasksService.updateReOpenMultiTasks(multiTasksReOpenVM);
        return new ResponseEntity<>(taskDtoList, HttpStatus.OK);
    }
    
	/**conghk
	 * @param step
	 * @return
	 */
	@GetMapping("/tasks/current-tasks-has-bugs/{step}/{packages}")
	@Timed
	public ResponseEntity<Map<String, Object>> getCurrentTasksHasBugByRound(@PathVariable("step") String step, @PathVariable("packages") Long packages) {
		log.debug("Get Task by roles and list status task of user : ", step);
		Map<String, Object> result = this.tasksService.getCurrentTasksHasBugByRound(step, packages);
		return new ResponseEntity<>(result, HttpStatus.OK);
	  }
	
    @GetMapping("/tasks/export-history")
  	@Timed
  	public ResponseEntity<InputStreamResource> exportTasksHistory(HttpServletResponse response,
  			@RequestParam("projectId") Long projectId, @RequestParam("roles") String roles,  @RequestParam("userLogin") String userLogin,
  			@RequestParam("beginTime") String beginTime, @RequestParam("endTime") String endTime) throws IOException, ParseException {
  		log.info("REST request to export template : {}", projectId);
  		InputStreamResource in = this.tasksService.exportExcelTasksHistory(projectId, roles, beginTime, endTime, userLogin);
  		HttpHeaders headers = new HttpHeaders();
          headers.add("Content-Disposition", "task_history_export_" + roles + ".xlsx");

  		 return ResponseEntity
  	                .ok()
  	                .headers(headers)
  	                .body(in);
  	}
}
