package fpt.dps.dtms.web.rest;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

import fpt.dps.dtms.service.TasksQueryService;
import fpt.dps.dtms.service.TasksService;
import fpt.dps.dtms.service.dto.SelectDTO;
import fpt.dps.dtms.service.util.AppConstants;
import fpt.dps.dtms.service.util.CommonFunction;
import fpt.dps.dtms.web.rest.util.PaginationUtil;

/**
 * REST controller for managing Tasks.
 */
@RestController
@RequestMapping("/api")
public class TrackingManagementResource {

    private final Logger log = LoggerFactory.getLogger(TrackingManagementResource.class);

    private static final String ENTITY_NAME = "tasks";

    private final TasksQueryService tasksQueryService;

    public TrackingManagementResource(TasksQueryService tasksQueryService) {
        this.tasksQueryService = tasksQueryService;
    }
    
    @GetMapping("/tasks/bug-tracking-task")
    @Timed
    public ResponseEntity<List<Map<String, Object>>> getBugTrackingTasks(
    		@RequestParam(value="packId", required = false) Long packId,
    		@RequestParam(value="purchaseOrderId", required = false) Long purchaseOrderId,
    		@RequestParam(value="projectId", required = false) Long projectId,
    		@RequestParam(value="from", required = false) String from,
    		@RequestParam(value="to", required = false) String to,
    		@RequestParam(value="userLogin", required = false) String userLogin,
    		Pageable pageable) {
    	log.debug("REST request to get bug tracking of tasks : {}", packId);
//    	Instant fromDateInstant = CommonFunction.convertLocalDateToInstant(from);
//    	Instant toDateInstant = CommonFunction.convertLocalDateToInstant(to);
    	Instant fromDateInstant = CommonFunction.convertLocalDateToInstant(from);
     	Instant toDateInstant = CommonFunction.convertLocalDateToInstantPlusOne(to);
    	List<Map<String, Object>> result = tasksQueryService.getBugTrackingTasks(projectId, purchaseOrderId, packId, fromDateInstant, toDateInstant, userLogin, pageable);
        int totalPage = Integer.parseInt(result.get(result.size() - 1).get("total").toString());
        result.remove(result.size() - 1);
    	Page<Map<String, Object>> page = new PageImpl<>(result, pageable, totalPage);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/tasks/bug-tracking-task");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    
    @GetMapping("/tasks/ratio-bug")
    @Timed
    public SelectDTO taskRatioBugs(
    		@RequestParam(value="packId", required = false) Long packId,
    		@RequestParam(value="purchaseOrderId", required = false) Long purchaseOrderId,
    		@RequestParam(value="projectId", required = false) Long projectId,
    		@RequestParam(value="from", required = false) String from,
    		@RequestParam(value="to", required = false) String to) {
//    	Instant fromDateInstant = CommonFunction.convertLocalDateToInstant(from);
//    	Instant toDateInstant = CommonFunction.convertLocalDateToInstant(to);
    	Instant fromDateInstant = CommonFunction.convertLocalDateToInstant(from);
     	Instant toDateInstant = CommonFunction.convertLocalDateToInstantPlusOne(to);
    	return tasksQueryService.taskRatioBugs(projectId, purchaseOrderId, packId, fromDateInstant, toDateInstant);
    }
    
    @GetMapping("/tasks/tracking-member")
    @Timed
    public ResponseEntity<List<Map<String, Object>>> getBugTrackingMembers(@RequestParam(value="member", required = false) String member,
    		@RequestParam(value="packId", required = false) Long packId,
    		@RequestParam(value="projectId", required = false) Long projectId,
    		@RequestParam(value="purchaseOrderId", required = false) Long purchaseOrderId,
    		@RequestParam(value="from", required = false) String from,
    		@RequestParam(value="to", required = false) String to,
    		Pageable pageable) {
    	log.debug("find all task assign for member: {}", member);
//    	Instant fromDateInstant = CommonFunction.convertLocalDateToInstant(from);
//    	Instant toDateInstant = CommonFunction.convertLocalDateToInstant(to);
    	Instant fromDateInstant = CommonFunction.convertLocalDateToInstant(from);
     	Instant toDateInstant = CommonFunction.convertLocalDateToInstantPlusOne(to);
    	List<Map<String, Object>> result = tasksQueryService.getBugTrackingMembers(projectId, purchaseOrderId, packId, fromDateInstant, toDateInstant, pageable);
    	int totalPage = Integer.parseInt(result.get(result.size() - 1).get("total").toString());
        result.remove(result.size() - 1);
    	Page<Map<String, Object>> page = new PageImpl<>(result, pageable, totalPage);
    	HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/tasks/tracking-member");
    	return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * Export tasks to excel file
     * @param packageId
     * @return excel file
     * @author TuHP 
     */
    @GetMapping("/tracking-management/tasks/export") 
	@Timed
	public ResponseEntity<InputStreamResource> trackingManagementTaskExport(
			@RequestParam(value="packId", required = false) Long packId,
    		@RequestParam(value="purchaseOrderId", required = false) Long purchaseOrderId,
    		@RequestParam(value="projectId", required = false) Long projectId,
    		@RequestParam(value="from", required = false) String from,
    		@RequestParam(value="to", required = false) String to,
    		@RequestParam(value="userLogin", required = false) String userLogin) throws IOException {
    	if(packId == 0) packId = null;
    	if(purchaseOrderId == 0) purchaseOrderId = null;
    	//convert String to LocalDate
//    	Instant fromDateInstant = CommonFunction.convertLocalDateToInstant(from);
//    	Instant toDateInstant = CommonFunction.convertLocalDateToInstant(to);
    	Instant fromDateInstant = CommonFunction.convertLocalDateToInstant(from);
     	Instant toDateInstant = CommonFunction.convertLocalDateToInstantPlusOne(to);
		InputStreamResource in = this.tasksQueryService.exportTrackingManagementTasks(projectId, purchaseOrderId, packId, fromDateInstant, toDateInstant, userLogin);
		HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=Task_Tracking_Manegement_Export.xlsx");
		
		 return ResponseEntity
	                .ok()
	                .headers(headers)
	                .body(in);
	}
}
