package fpt.dps.dtms.service;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import fpt.dps.dtms.domain.Packages;
import fpt.dps.dtms.domain.ProjectUsers;
import fpt.dps.dtms.domain.PurchaseOrders;
import fpt.dps.dtms.domain.TaskBidding;
import fpt.dps.dtms.domain.TaskBiddingTrackingTime;
import fpt.dps.dtms.domain.TaskTrackingTime;
import fpt.dps.dtms.domain.Tasks;
import fpt.dps.dtms.domain.enumeration.BiddingScope;
import fpt.dps.dtms.domain.enumeration.BiddingStatus;
import fpt.dps.dtms.domain.enumeration.FIStatus;
import fpt.dps.dtms.domain.enumeration.FixStatus;
import fpt.dps.dtms.domain.enumeration.OPStatus;
import fpt.dps.dtms.domain.enumeration.ReviewStatus;
import fpt.dps.dtms.domain.enumeration.TaskStatus;
import fpt.dps.dtms.repository.TaskBiddingRepository;
import fpt.dps.dtms.repository.search.TaskBiddingSearchRepository;
import fpt.dps.dtms.service.dto.TaskBiddingDTO;
import fpt.dps.dtms.service.util.AppConstants;
import fpt.dps.dtms.service.util.CommonFunction;

/**
 * Service Implementation for managing Tasks.
 *
 * @author TuHP
 */
@Service
public class TasksProcessingService {

	private final Logger log = LoggerFactory.getLogger(TasksProcessingService.class);

	private final RuntimeService runtimeService;

	private final TaskService taskService;

	private final HistoryService historyService;

	private final TaskTrackingTimeService taskTrackingTimeService;

	private final TaskBiddingTrackingTimeService taskBiddingTrackingTimeService;

	private final TaskBiddingRepository taskBiddingRepository;

	private final TaskBiddingSearchRepository taskBiddingSearchRepository;

	public TasksProcessingService(RuntimeService runtimeService, TaskService taskService,
			TaskTrackingTimeService taskTrackingTimeService, HistoryService historyService,
			TaskBiddingTrackingTimeService taskBiddingTrackingTimeService, TaskBiddingRepository taskBiddingRepository,
			TaskBiddingSearchRepository taskBiddingSearchRepository) {
		this.runtimeService = runtimeService;
		this.taskService = taskService;
		this.historyService = historyService;
		this.taskTrackingTimeService = taskTrackingTimeService;
		this.taskBiddingTrackingTimeService = taskBiddingTrackingTimeService;
		this.taskBiddingRepository = taskBiddingRepository;
		this.taskBiddingSearchRepository = taskBiddingSearchRepository;
	}

	/**
	 * Process task base on template work flow.
	 *
	 * @param processKey
	 * @param tasks
	 * @return processResult which include a user will be notified and task's status
	 *         of user
	 */
	public Map<String, String> startProcessing(String processKey, Tasks tasks, Packages packages,
			TaskBiddingDTO taskBiddingDTO) {
		log.debug("Start: Process a Workflow of {}", processKey);
		ProcessInstance processInstance;
		String processInstanceId = tasks.getData();
		boolean isDefaultFlow = true;
		Map<String, String> processResult = new HashMap<String, String>();

		// Check whether task was processed or not. If not the system will start process
		if (processInstanceId == null || processInstanceId.equals("")) {
			isDefaultFlow = false;
			processInstance = runtimeService.startProcessInstanceByKey(processKey);
			tasks.setData(processInstance.getId());
		} else {
			runtimeService.activateProcessInstanceById(processInstanceId);
			processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId)
					.singleResult();
		}

		// Check process instance is exits and task still in process flow
		if (processInstance != null && !processInstance.isEnded()) {
			Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
			log.debug("Processing Task [" + task.getName() + "]");

			Map<String, Object> taskParams = new HashMap<String, Object>();
			String currentRound = processInstance.getActivityId();
			String nextRound = StringUtils.EMPTY;
			String userIsNotificated = StringUtils.EMPTY;
			String status = StringUtils.EMPTY;
			if (TaskStatus.CANCEL.equals(tasks.getStatus())) {
				taskParams.put("status", AppConstants.TASK_CANCEL);
				taskService.complete(task.getId(), taskParams);
				Map<String, String> tempData = setDoneStatusForCurrentRound(tasks, currentRound);
				userIsNotificated = tempData.get("user");
				status = tempData.get("status");
			} else {
				status = getCurrentStatus(tasks, currentRound);
				taskParams = this.detechDs11Process(taskParams, processKey, currentRound, status, tasks, taskBiddingDTO);
				taskParams.put("status", status);
				taskService.complete(task.getId(), taskParams);
				
				if (!isDefaultFlow) {
					processInstance = runtimeService.createProcessInstanceQuery()
							.processInstanceId(processInstance.getId()).singleResult();
					nextRound = processInstance != null ? processInstance.getActivityId() : AppConstants.END;

					while (AppConstants.DONE.equals(status)) {
						task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
						currentRound = nextRound;
						status = getCurrentStatus(tasks, currentRound);
						taskParams = this.detechDs11Process(taskParams, processKey, currentRound, status, tasks, taskBiddingDTO);
						taskParams.put("status", status);
						taskService.complete(task.getId(), taskParams);
						processInstance = runtimeService.createProcessInstanceQuery()
								.processInstanceId(processInstance.getId()).singleResult();

						nextRound = processInstance != null ? processInstance.getActivityId() : AppConstants.END;
						
						if (AppConstants.END.equals(nextRound)) {
							break;
						}

					}
				}
			}
			
			if (isDefaultFlow) {
				processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstance.getId())
						.singleResult();
				nextRound = processInstance != null ? processInstance.getActivityId() : AppConstants.END;
			}
			
			if (taskBiddingDTO != null) {
				this.taskBiddingTrackingTime(tasks, currentRound, status, taskBiddingDTO.getBiddingScope());
			} else {
				this.taskTrackingTime(tasks, currentRound, status);
			}

			// get all active activities of the process instance
			if (TaskStatus.CANCEL.equals(tasks.getStatus())) {
				processResult.put("user", userIsNotificated);
				processResult.put("status", AppConstants.MESSAGE_CANCEL);
				processResult.put("nextRound", currentRound);
			} else if (TaskStatus.PENDING.equals(tasks.getStatus())) {
				userIsNotificated = this.getLeadOfTasks(packages);
				processResult.put("user", userIsNotificated);
				processResult.put("status", AppConstants.PENDING);
				processResult.put("nextRound", currentRound);

			} else if (status.equals(AppConstants.NOT_GOOD)) {
				// Set REOPEN status for the previous round
				userIsNotificated = setReOpenStatusForPreviousRound(tasks, nextRound);

				// Judgment delete or create task bidding
				this.judgmentSaveTaskBidding(taskBiddingDTO, userIsNotificated, tasks, nextRound);

				processResult.put("user", userIsNotificated);
				processResult.put("status", AppConstants.MESSAGE_REOPEN);
				processResult.put("nextRound", nextRound);
			} else if (status.equals(AppConstants.RE_ASSIGN)) {
				userIsNotificated = setReOpenStatusForPreviousRound(tasks, AppConstants.ROUND_OP);
				processResult.put("user", userIsNotificated);
				processResult.put("status", AppConstants.MESSAGE_REOPEN);
				processResult.put("nextRound", AppConstants.ROUND_OP);
			} else if (status.equals(AppConstants.RE_ASSIGN_REVIEW1)) {
				userIsNotificated = setOpenStatusForNextRound(tasks, AppConstants.ROUND_REVIEW1);
				processResult.put("user", userIsNotificated);
				processResult.put("status", AppConstants.MESSAGE_OPEN);
				processResult.put("nextRound", AppConstants.ROUND_REVIEW1);
			} else if (!nextRound.equals(currentRound)) {
				// Set OPEN status for the next round
				userIsNotificated = setOpenStatusForNextRound(tasks, nextRound);

				// Judgment delete or create task bidding
				this.judgmentSaveTaskBidding(taskBiddingDTO, userIsNotificated, tasks, nextRound);
				processResult.put("user", userIsNotificated);
				processResult.put("status", AppConstants.MESSAGE_OPEN);
				processResult.put("nextRound", nextRound);
			} else {
				processResult.put("nextRound", currentRound);
			}

			// If the activity does not end then pausing the process for the next call
			if (!AppConstants.END.equals(nextRound)) {
				runtimeService.suspendProcessInstanceById(processInstance.getId());
			}
		}
		return processResult;
	}

	private Map<String, Object> detechDs11Process(Map<String, Object> taskParams, String processKey, String currentRound, String status, Tasks tasks, TaskBiddingDTO taskBiddingDTO) {
		if (processKey.equals("ds11_task_processing")) {
			if (AppConstants.ROUND_FIXER.equals(currentRound) && AppConstants.DONE.equals(status)) {
				String previousRound = StringUtils.EMPTY;
				if (taskBiddingDTO != null) {
					previousRound = this.taskBiddingTrackingTimeService.getPreviousRound(tasks.getId(), currentRound);
				} else {
					previousRound = this.taskTrackingTimeService.getPreviousRound(tasks.getId(), currentRound);
				}
				
				if (AppConstants.ROUND_REVIEW1.equals(previousRound)) {
					taskParams.put("loop", 0);
				} else if (AppConstants.ROUND_REVIEW2.equals(previousRound)) {
					taskParams.put("loop", 1);
				}
			} else if (AppConstants.ROUND_REVIEW2.equals(currentRound) && AppConstants.NOT_GOOD.equals(status)) {
				tasks.setFixer(null);
				tasks.setFixStatus(FixStatus.NA);
			}
		}
		return taskParams;
	}

	private void judgmentSaveTaskBidding(TaskBiddingDTO taskBiddingDTO, String userIsNotificated, Tasks tasks,
			String nextRound) {
		TaskBiddingTrackingTime biddingTrackingTime = this.taskBiddingTrackingTimeService
				.findTaskIdAndRole(tasks.getId(), nextRound);
		String nextRoundStatus = this.getCurrentStatus(tasks, nextRound);
		TaskBidding taskBidding = this.taskBiddingRepository.findByTaskId(tasks.getId());
		if (taskBiddingDTO != null) {
			if (biddingTrackingTime == null && StringUtils.isNotEmpty(userIsNotificated) && taskBidding != null) {
				this.taskBiddingRepository.delete(taskBidding.getId());
				this.taskBiddingSearchRepository.delete(taskBidding.getId());

			} else if (biddingTrackingTime != null && !nextRoundStatus.equals(AppConstants.NA)) {
				taskBidding.setBiddingRound(nextRound);

			} else if (nextRoundStatus.equals(AppConstants.NA)) {
				taskBidding.setBiddingRound(nextRound);
				taskBidding.setBiddingStatus(BiddingStatus.NA);
			}
			
		} else if (biddingTrackingTime != null && StringUtils.isNotEmpty(userIsNotificated) && taskBidding == null) {
			taskBidding = new TaskBidding();
			taskBidding.setTask(tasks);
			taskBidding.setBiddingRound(nextRound);
			taskBidding.setBiddingScope(biddingTrackingTime.getBiddingScope());
			taskBidding.setBiddingStatus(BiddingStatus.DOING);
			taskBidding = taskBiddingRepository.save(taskBidding);
			// taskBiddingSearchRepository.save(taskBidding);
		} else if (taskBidding != null && nextRoundStatus.equals(AppConstants.NA)) {
			taskBidding.setBiddingRound(nextRound);
			taskBidding.setBiddingStatus(BiddingStatus.NA);
		}
	}

	/**
	 * Get the current status at current round when processing task
	 *
	 * @param tasks
	 * @param round
	 * @return current round status of task
	 */
	String getCurrentStatus(Tasks tasks, String round) {
		try {
			switch (round) {
			case AppConstants.ROUND_REVIEW1:
				return tasks.getReview1Status().toString();
			case AppConstants.ROUND_REVIEW2:
				return tasks.getReview2Status().toString();
			case AppConstants.ROUND_FIXER:
				return tasks.getFixStatus().toString();
			case AppConstants.ROUND_FI:
				return tasks.getFiStatus().toString();
			}
			return tasks.getOpStatus().toString();
		} catch (NullPointerException e) {
			return AppConstants.NA;
		}
	}

	/**
	 * Tracking operator working time
	 *
	 * @param tasks
	 * @param round
	 * @param status
	 */
	private void taskTrackingTime(Tasks tasks, String round, String status) {
		switch (round) {
		case AppConstants.ROUND_REVIEW1:
			saveTrackingTime(tasks, tasks.getId(), tasks.getReview1(), status, AppConstants.ROUND_REVIEW1);
			break;
		case AppConstants.ROUND_REVIEW2:
			saveTrackingTime(tasks, tasks.getId(), tasks.getReview2(), status, AppConstants.ROUND_REVIEW2);
			break;
		case AppConstants.ROUND_FIXER:
			saveTrackingTime(tasks, tasks.getId(), tasks.getFixer(), status, AppConstants.ROUND_FIXER);
			break;
		case AppConstants.ROUND_FI:
			saveTrackingTime(tasks, tasks.getId(), tasks.getFi(), status, AppConstants.ROUND_FI);
			break;
		default:
			saveTrackingTime(tasks, tasks.getId(), tasks.getOp(), status, AppConstants.ROUND_OP);
			break;
		}
	}

	/**
	 * Tracking operator working time
	 *
	 * @param tasks
	 * @param round
	 * @param status
	 */
	private void taskBiddingTrackingTime(Tasks tasks, String round, String status, BiddingScope biddingScope) {
		switch (round) {
		case AppConstants.ROUND_REVIEW1:
			saveBiddingTrackingTime(tasks, tasks.getReview1(), status, AppConstants.ROUND_REVIEW1, biddingScope);
			break;
		case AppConstants.ROUND_REVIEW2:
			saveBiddingTrackingTime(tasks, tasks.getReview2(), status, AppConstants.ROUND_REVIEW2, biddingScope);
			break;
		case AppConstants.ROUND_FIXER:
			saveBiddingTrackingTime(tasks, tasks.getFixer(), status, AppConstants.ROUND_FIXER, biddingScope);
			break;
		case AppConstants.ROUND_FI:
			saveBiddingTrackingTime(tasks, tasks.getFi(), status, AppConstants.ROUND_FI, biddingScope);
			break;
		default:
			saveBiddingTrackingTime(tasks, tasks.getOp(), status, AppConstants.ROUND_OP, biddingScope);
			break;
		}
	}

	/**
	 * @param tasks
	 * @param userLogin
	 * @param status
	 * @param role
	 */
	private void saveBiddingTrackingTime(Tasks tasks, String userLogin, String status, String role,
			BiddingScope biddingScope) {
		TaskBiddingTrackingTime taskBiddingTrackingTime = null;
		// If status is DOING then the system will insert new row and set start time
		// value
		// Else the system will update end time to record
		if (status == AppConstants.DOING) {
			taskBiddingTrackingTime = new TaskBiddingTrackingTime();
			taskBiddingTrackingTime.setTask(tasks);
			taskBiddingTrackingTime.setUserLogin(userLogin);
			taskBiddingTrackingTime.setRole(role);
			taskBiddingTrackingTime.setBiddingScope(biddingScope);
			taskBiddingTrackingTime.setStartTime(Instant.now());
			taskBiddingTrackingTime.setStartStatus(status);
			taskBiddingTrackingTimeService.save(taskBiddingTrackingTime);
			tasks.setStatus(TaskStatus.DOING);
		} else if (status == AppConstants.PENDING || status == AppConstants.DONE || status == AppConstants.NOT_GOOD) {
			taskBiddingTrackingTime = taskBiddingTrackingTimeService.findTaskIdAndUserLogin(tasks.getId(), userLogin);
			if (taskBiddingTrackingTime != null) {
				// update endtime
				Instant endtime = Instant.now();
				taskBiddingTrackingTime.setEndTime(endtime);
				taskBiddingTrackingTime.setEndStatus(status);
				int duration = CommonFunction.GetDurationBetweenTwoDays(taskBiddingTrackingTime.getStartTime(),
						endtime);
				taskBiddingTrackingTime.setDuration(duration);
				taskBiddingTrackingTimeService.save(taskBiddingTrackingTime);
			}
			if (status == AppConstants.PENDING) {
				tasks.setStatus(TaskStatus.PENDING);
			}
		}
	}

	/**
	 * Save tracking time to database
	 *
	 * @param tasks
	 * @param taskId
	 * @param userLogin
	 * @param status
	 * @param role
	 */
	private void saveTrackingTime(Tasks tasks, Long taskId, String userLogin, String status, String role) {
		TaskTrackingTime taskTrackingTime = null;
		// If status is DOING then the system will insert new row and set start time
		// value
		// Else the system will update end time to record
		if (status == AppConstants.DOING) {
			taskTrackingTime = new TaskTrackingTime();
			taskTrackingTime.setTaskId(taskId);
			taskTrackingTime.setUserLogin(userLogin);
			taskTrackingTime.setRole(role);
			taskTrackingTime.setStartTime(Instant.now());
			taskTrackingTime.setStartStatus(status);
			taskTrackingTimeService.save(taskTrackingTime);
			tasks.setStatus(TaskStatus.DOING);
		} else if (status == AppConstants.PENDING || status == AppConstants.DONE || status == AppConstants.NOT_GOOD) {
			taskTrackingTime = taskTrackingTimeService.findTaskIdAndUserLogin(taskId, userLogin);
			if (taskTrackingTime != null) {
				// update endtime
				Instant endtime = Instant.now();
				taskTrackingTime.setEndTime(endtime);
				taskTrackingTime.setEndStatus(status);
				int duration = CommonFunction.GetDurationBetweenTwoDays(taskTrackingTime.getStartTime(), endtime);
				taskTrackingTime.setDuration(duration);
				taskTrackingTimeService.save(taskTrackingTime);
			}
			if (status == AppConstants.PENDING) {
				tasks.setStatus(TaskStatus.PENDING);
			}
		}
	}

	/**
	 * Set Open status for next round after the current was done
	 *
	 * @param tasks
	 * @param round
	 * @return user who is notified
	 */
	private String setOpenStatusForNextRound(Tasks tasks, String round) {
		String userIsNotificated = "";
		switch (round) {
		case AppConstants.ROUND_REVIEW1:
			userIsNotificated = tasks.getReview1();
			if (StringUtils.isNotEmpty(userIsNotificated)) {
				tasks.setReview1Status(ReviewStatus.OPEN);
			} else {
				tasks.setReview1Status(ReviewStatus.NA);
			}
			break;
		case AppConstants.ROUND_REVIEW2:
			userIsNotificated = tasks.getReview2();
			if (StringUtils.isNotEmpty(userIsNotificated)) {
				tasks.setReview2Status(ReviewStatus.OPEN);
			} else {
				tasks.setReview2Status(ReviewStatus.NA);
			}

			break;
		case AppConstants.ROUND_FIXER:
			userIsNotificated = tasks.getFixer();
			if (StringUtils.isNotEmpty(userIsNotificated)) {
				tasks.setFixStatus(FixStatus.OPEN);
			} else {
				tasks.setFixStatus(FixStatus.NA);
			}

			break;
		case AppConstants.ROUND_FI:
			userIsNotificated = tasks.getFi();
			if (StringUtils.isNotEmpty(userIsNotificated)) {
				tasks.setFiStatus(FIStatus.OPEN);
			} else {
				tasks.setFiStatus(FIStatus.NA);
			}
			break;
		default:
			tasks.setData(null);
			tasks.setStatus(TaskStatus.DONE);
			break;
		}
		return userIsNotificated;
	}

	/**
	 * Set REOPEN status for previous round after the current row set NOTGOOD
	 *
	 * @param tasks
	 * @param round
	 * @return user who is notified
	 */
	private String setReOpenStatusForPreviousRound(Tasks tasks, String round) {
		String userIsNotificated = "";
		switch (round) {
		case "review1":
			userIsNotificated = tasks.getReview1();
			if (StringUtils.isNotEmpty(userIsNotificated)) {
				tasks.setReview1Status(ReviewStatus.REOPEN);
			} else {
				tasks.setReview1Status(ReviewStatus.NA);
			}
			break;
		case "review2":
			
			userIsNotificated = tasks.getReview2();
			if (StringUtils.isNotEmpty(userIsNotificated)) {
				tasks.setReview2Status(ReviewStatus.REOPEN);
			} else {
				tasks.setReview2Status(ReviewStatus.NA);
			}
			break;
		case "fixer":
			
			userIsNotificated = tasks.getFixer();
			if (StringUtils.isNotEmpty(userIsNotificated)) {
				tasks.setFixStatus(FixStatus.REOPEN);
			} else {
				tasks.setFixStatus(FixStatus.NA);
			}
			break;
		default:
			userIsNotificated = tasks.getOp();
			if (StringUtils.isNotEmpty(userIsNotificated)) {
				tasks.setOpStatus(OPStatus.REOPEN);
			} else {
				tasks.setOpStatus(OPStatus.NA);
			}
			break;
		}
		return userIsNotificated;
	}

	/**
	 * Set DONE status for current round in case the Tasks was Cancelled
	 *
	 * @param tasks
	 * @param round
	 * @return user who is notified
	 */
	private Map<String, String> setDoneStatusForCurrentRound(Tasks tasks, String round) {
		String userIsNotificated = StringUtils.EMPTY;
		String status = StringUtils.EMPTY;
		Map<String, String> tempData = new HashMap<String, String>();
		switch (round) {
		case AppConstants.ROUND_REVIEW1:
			status = tasks.getReview1Status().toString();
			if (!status.equals(AppConstants.NA) && !status.equals(AppConstants.OPEN)
					&& !status.equals(AppConstants.REOPEN)) {
				tasks.setReview1Status(ReviewStatus.DONE);
				status = AppConstants.DONE;
			}
			userIsNotificated = tasks.getReview1();
			break;
		case AppConstants.ROUND_REVIEW2:
			status = tasks.getReview2Status().toString();
			if (!status.equals(AppConstants.NA) && !status.equals(AppConstants.OPEN)
					&& !status.equals(AppConstants.REOPEN)) {
				tasks.setReview2Status(ReviewStatus.DONE);
				status = AppConstants.DONE;
			}
			userIsNotificated = tasks.getReview2();
			break;
		case AppConstants.ROUND_FIXER:
			status = tasks.getFixStatus().toString();
			if (!status.equals(AppConstants.NA) && !status.equals(AppConstants.OPEN)
					&& !status.equals(AppConstants.REOPEN)) {
				tasks.setFixStatus(FixStatus.DONE);
				status = AppConstants.DONE;
			}
			userIsNotificated = tasks.getFixer();
			break;
		case AppConstants.ROUND_FI:
			status = tasks.getFiStatus().toString();
			if (!status.equals(AppConstants.NA) && !status.equals(AppConstants.OPEN)
					&& !status.equals(AppConstants.REOPEN)) {
				tasks.setFiStatus(FIStatus.DONE);
				status = AppConstants.DONE;
			}
			userIsNotificated = tasks.getFi();
			break;
		default:
			status = tasks.getOpStatus().toString();
			if (!status.equals(AppConstants.NA) && !status.equals(AppConstants.OPEN)
					&& !status.equals(AppConstants.REOPEN)) {
				tasks.setOpStatus(OPStatus.DONE);
				status = AppConstants.DONE;
			}
			userIsNotificated = tasks.getOp();
			break;
		}
		tempData.put("status", status);
		tempData.put("user", userIsNotificated);
		return tempData;
	}

	/**
	 * Get Lead Of Tasks
	 *
	 * @param tasks
	 * @return lead of tasks
	 */
	private String getLeadOfTasks(Packages packages) {
		PurchaseOrders po = packages.getPurchaseOrders();
		ProjectUsers purchaseOrderLead = po.getPurchaseOrderLead();
		if (purchaseOrderLead != null) {
			return purchaseOrderLead.getUserLogin();
		}
		return po.getProject().getProjectLead().getUserLogin();
	}
}
