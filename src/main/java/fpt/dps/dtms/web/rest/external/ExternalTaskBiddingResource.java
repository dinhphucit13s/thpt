package fpt.dps.dtms.web.rest.external;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.quartz.SchedulerException;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

import fpt.dps.dtms.security.SecurityUtils;
import fpt.dps.dtms.service.TaskBiddingQueryService;
import fpt.dps.dtms.service.TaskBiddingService;
import fpt.dps.dtms.service.dto.TaskBiddingDTO;
import fpt.dps.dtms.web.rest.errors.BadRequestAlertException;
import fpt.dps.dtms.web.rest.util.HeaderUtil;
import fpt.dps.dtms.service.util.AppConstants;
import fpt.dps.dtms.service.util.FieldConfigService;
import fpt.dps.dtms.web.rest.util.PaginationUtil;
import fpt.dps.dtms.web.rest.vm.FieldConfigVM;
import fpt.dps.dtms.web.rest.vm.external.MultiFieldConfigVM;
import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing Task Bidding.
 */
@RestController
@RequestMapping("/api/external")
public class ExternalTaskBiddingResource {
    private final Logger log = LoggerFactory.getLogger(ExternalBugsResource.class);

    private static final String ENTITY_NAME = "task-bidding";

    private final TaskBiddingService taskBiddingService;

    private final TaskBiddingQueryService taskBiddingQueryService;

    private final FieldConfigService fieldConfigService;

	public ExternalTaskBiddingResource(TaskBiddingService taskBiddingService,
			TaskBiddingQueryService taskBiddingQueryService, FieldConfigService fieldConfigService) {
		this.taskBiddingService = taskBiddingService;
		this.taskBiddingQueryService = taskBiddingQueryService;
		this.fieldConfigService = fieldConfigService;
	}

    /**
     * GET  /task-bidding/getTaskHolding/:userLogin : get task bidding status holding
     *
     * @param userLogin is the info of user
     * @return the ResponseEntity with status 200 (OK)
     * @throws SchedulerException
     */
    @GetMapping("/task-bidding/getTaskHolding/{userLogin}")
    @Timed
    public ResponseEntity<HashMap<String, Object>> getTaskHolding(@PathVariable("userLogin") String userLogin) throws SchedulerException {
        log.debug("REST request to get a page of Bugs");
        HashMap<String, Object> result = this.taskBiddingService.getTaskHolding(userLogin);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/task-bidding/holdingByOP")
    @Timed
    public ResponseEntity<TaskBiddingDTO> updateHoldingTaskByOp(@RequestBody TaskBiddingDTO taskBidding) {
        log.debug("REST request to get a page of Bugs");
        TaskBiddingDTO result = this.taskBiddingService.updateHoldingTaskByOp(taskBidding);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/task-bidding/returnTask")
    @Timed
    public ResponseEntity<TaskBiddingDTO> returnTaskByOp(@RequestBody TaskBiddingDTO taskBidding) {
        log.debug("REST request to get a page of Bugs");
        TaskBiddingDTO result = this.taskBiddingService.returnTaskByOp(taskBidding);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

	/**
	 * Get all Task Bidding have not 'DONE' yet.
	 * @param biddingScope
	 * @param projectId
	 * @param purchaseOrderId
	 * @param packageId
	 * @param step
	 * @param pageable
	 * @return
	 */
	@GetMapping("/task-bidding")
	public ResponseEntity<List<TaskBiddingDTO>> findAllTasksBiddingByBiddingScope(@RequestParam("biddingScope") String biddingScope, @RequestParam("projectId") Long projectId,
			@RequestParam("purchaseOrderId") Long purchaseOrderId, @RequestParam("packageId") Long packageId, @RequestParam("step") String step, Pageable pageable){
		log.debug("REST request to get Tasks by criteria: {}", biddingScope);
		String currentUserLogin = SecurityUtils.getCurrentUserLogin().get().toLowerCase();
        Page<TaskBiddingDTO> page = this.taskBiddingService.findAllTasksBiddingByBiddingScope(biddingScope,projectId, purchaseOrderId,
        		packageId, pageable, step, currentUserLogin);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/tasks-bidding");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

    /**
     * GET  /task-bidding/op/:userLogin : get task bidding status holding
     *
     * @param userLogin is the info of user
     * @return the ResponseEntity with status 200 (OK)
     * @throws SchedulerException
     */
    @GetMapping("/task-bidding/op/{userLogin}")
    @Timed
    public ResponseEntity<List<TaskBiddingDTO>> getBiddingTaskOP(@PathVariable("userLogin") String userLogin) {
        log.debug("REST request to get list bidding task by user: " + userLogin);
        List<TaskBiddingDTO> listTaskBidding = this.taskBiddingQueryService.getBiddingTasksOP(userLogin);

//        for (TaskBiddingDTO taskBiddingDTO : listTaskBidding) {
//            //taskBiddingDTO.setStep("OP");
//            taskBiddingDTO.setPic(userLogin);
//        }
        return new ResponseEntity<>(listTaskBidding, HttpStatus.OK);
    }

    /**
     * GET  /task-bidding/:userLogin/:round : get task bidding status holding
     *
     * @param userLogin is the info of user
     * @return the ResponseEntity with status 200 (OK)
     * @throws SchedulerException
     */
    @GetMapping("/task-bidding/{userLogin}/{round}")
    @Timed
    public ResponseEntity<List<TaskBiddingDTO>> getAllBiddingTasksOfUserByRole(@PathVariable("userLogin") String userLogin, @PathVariable("round") String round) {
        log.debug("REST request to get list bidding task by user {} and round {}: ", userLogin, round);
        List<TaskBiddingDTO> listTaskBidding = this.taskBiddingQueryService.getAllBiddingTasksOfUserByRole(userLogin, round);
        return new ResponseEntity<>(listTaskBidding, HttpStatus.OK);
    }



//    /**
//     * GET  /task-bidding/op/:userLogin : get task bidding status holding
//     *
//     * @param userLogin is the info of user
//     * @return the ResponseEntity with status 200 (OK)
//     * @throws SchedulerException
//     */
//    @GetMapping("/task-bidding/review/{userLogin}")
//    @Timed
//    public ResponseEntity<List<TaskBiddingDTO>> getBiddingTaskRV(@PathVariable("userLogin") String userLogin) {
//        log.debug("REST request to get list bidding task by user: " + userLogin);
//        List<TaskBiddingDTO> listTaskBidding = this.taskBiddingQueryService.getAllTaskBiddingByRoundReview1(userLogin);
//        List<TaskBiddingDTO> listTaskBiddingRV2 = this.taskBiddingQueryService.getAllTaskBiddingByRoundReview2(userLogin);
//        listTaskBidding.addAll(listTaskBiddingRV2);
//        return new ResponseEntity<>(listTaskBidding, HttpStatus.OK);
//    }

//    /**
//     * GET  /task-bidding/op/:userLogin : get task bidding status holding
//     *
//     * @param userLogin is the info of user
//     * @return the ResponseEntity with status 200 (OK)
//     * @throws SchedulerException
//     */
//    @GetMapping("/task-bidding/{userLogin}/{round}")
//    @Timed
//    public ResponseEntity<List<TaskBiddingDTO>> getBiddingTask(@PathVariable("userLogin") String userLogin, @PathVariable("round") String round) {
//        log.debug("REST request to get list bidding task by user: " + userLogin);
//        List<TaskBiddingDTO> listTaskBidding = this.taskBiddingQueryService.getAllTaskBiddingByRound(userLogin, round);
//        return new ResponseEntity<>(listTaskBidding, HttpStatus.OK);
//    }

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
        log.debug("REST EXTERNAL request to update TaskBidding : {}", taskBiddingDTO);
        if (taskBiddingDTO.getId() == null) {
        	throw new BadRequestAlertException("A task bidding must be have an id", "updateTaskBidding", "idnotexists");
        }
        TaskBiddingDTO result = taskBiddingService.save(taskBiddingDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, taskBiddingDTO.getId().toString()))
            .body(result);
    }
}
