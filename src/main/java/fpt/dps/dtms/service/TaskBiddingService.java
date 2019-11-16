package fpt.dps.dtms.service;

import fpt.dps.dtms.biddingjobs.BiddingTaskHolding;
import fpt.dps.dtms.domain.ProjectTemplates;
import fpt.dps.dtms.domain.PurchaseOrders;
import fpt.dps.dtms.domain.TaskBidding;
import fpt.dps.dtms.domain.Tasks;
import fpt.dps.dtms.domain.enumeration.BiddingScope;
import fpt.dps.dtms.domain.enumeration.BiddingStatus;
import fpt.dps.dtms.domain.enumeration.FIStatus;
import fpt.dps.dtms.domain.enumeration.FixStatus;
import fpt.dps.dtms.domain.enumeration.OPStatus;
import fpt.dps.dtms.domain.enumeration.ReviewStatus;
import fpt.dps.dtms.domain.enumeration.TaskStatus;
import fpt.dps.dtms.repository.PurchaseOrdersRepository;
import fpt.dps.dtms.repository.TaskBiddingRepository;
import fpt.dps.dtms.repository.TasksRepository;
import fpt.dps.dtms.repository.search.TaskBiddingSearchRepository;
import fpt.dps.dtms.security.SecurityUtils;
import fpt.dps.dtms.service.dto.ProjectWorkflowsCriteria;
import fpt.dps.dtms.service.dto.ProjectWorkflowsDTO;
import fpt.dps.dtms.service.dto.TaskBiddingDTO;
import fpt.dps.dtms.service.dto.TasksDTO;
import fpt.dps.dtms.service.mapper.TaskBiddingMapper;
import fpt.dps.dtms.service.quartz.ScheduleJobService;
import fpt.dps.dtms.service.util.AppConstants;
import fpt.dps.dtms.service.util.FieldConfigService;
import fpt.dps.dtms.web.rest.vm.FieldConfigVM;
import fpt.dps.dtms.web.rest.vm.IdListVM;
import fpt.dps.dtms.web.rest.vm.TMSDynamicCustomFieldVM;
import fpt.dps.dtms.web.rest.vm.external.MultiFieldConfigVM;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.elasticsearch.index.query.QueryBuilders.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Service Implementation for managing TaskBidding.
 */
@Service
@Transactional
public class TaskBiddingService {

    private static final Integer DEFAULT_HOLDING_TIME = 5;

	private final Logger log = LoggerFactory.getLogger(TaskBiddingService.class);

    private final TaskBiddingRepository taskBiddingRepository;

    private final TaskBiddingMapper taskBiddingMapper;

    private final TasksService tasksService;

    private final TaskBiddingSearchRepository taskBiddingSearchRepository;

    private final PurchaseOrdersRepository purchaseOrdersRepository;

    private final ProjectWorkflowsQueryService projectWorkflowsQueryService;

    private final TasksRepository tasksRepository;

    private final FieldConfigService fieldConfigService;

    private final RuntimeService runtimeService;

    private final TaskService taskService;

    @Autowired
	private ScheduleJobService scheduleJobService;

    public TaskBiddingService(TaskBiddingRepository taskBiddingRepository, TaskBiddingMapper taskBiddingMapper,
    		TaskBiddingSearchRepository taskBiddingSearchRepository, TasksRepository tasksRepository,
    		FieldConfigService fieldConfigService, TasksService tasksService, PurchaseOrdersRepository purchaseOrdersRepository,
    		ProjectWorkflowsQueryService projectWorkflowsQueryService, RuntimeService runtimeService, TaskService taskService) {
        this.taskBiddingRepository = taskBiddingRepository;
        this.taskBiddingMapper = taskBiddingMapper;
        this.taskBiddingSearchRepository = taskBiddingSearchRepository;
        this.tasksRepository = tasksRepository;
        this.fieldConfigService = fieldConfigService;
        this.tasksService = tasksService;
        this.purchaseOrdersRepository = purchaseOrdersRepository;
        this.projectWorkflowsQueryService = projectWorkflowsQueryService;
        this.runtimeService = runtimeService;
        this.taskService = taskService;
    }

    /**
     * Save a taskBidding.
     *
     * @param taskBiddingDTO the entity to save
     * @return the persisted entity
     */
    public TaskBiddingDTO save(TaskBiddingDTO taskBiddingDTO) {
        log.debug("Request to save TaskBidding : {}", taskBiddingDTO);
        TasksDTO tasksDTO = taskBiddingDTO.getTask();
        tasksDTO = this.tasksService.save(tasksDTO, taskBiddingDTO);

        // Maybe task bidding was be removed if next round was have assignee
        TaskBidding taskBidding = this.taskBiddingRepository.findOne(taskBiddingDTO.getId());
        if (taskBidding != null) {
        	if (tasksDTO.getStatus().equals(TaskStatus.DONE)) {
            	taskBidding.setBiddingStatus(BiddingStatus.DONE);
            } else if(taskBiddingDTO.getBiddingStatus().equals(BiddingStatus.HOLDING)
            		&& this.getCurrentStatus(tasksDTO, taskBiddingDTO.getBiddingRound().toLowerCase()).equals(AppConstants.DOING)) {
            	taskBidding.setBiddingStatus(BiddingStatus.DOING);
            }
        	taskBidding = taskBiddingRepository.save(taskBidding);
        	taskBiddingSearchRepository.save(taskBidding);
            TaskBiddingDTO result = taskBiddingMapper.toDto(taskBidding);
            return result;
        }

        return null;
    }

	String getCurrentStatus(TasksDTO tasksDTO, String round) {
		try {
			switch (round) {
			case AppConstants.ROUND_REVIEW1:
				return tasksDTO.getReview1Status().toString();
			case AppConstants.ROUND_REVIEW2:
				return tasksDTO.getReview2Status().toString();
			case AppConstants.ROUND_FIXER:
				return tasksDTO.getFixStatus().toString();
			case AppConstants.ROUND_FI:
				return tasksDTO.getFiStatus().toString();
		}
		return tasksDTO.getOpStatus().toString();
		} catch (NullPointerException e) {
			return "NA";
		}
	}

	/**
     * Get all the taskBiddings.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<TaskBiddingDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TaskBiddings");
        return taskBiddingRepository.findAll(pageable)
            .map(taskBiddingMapper::toDto);
    }

    /**
     * Get one taskBidding by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public TaskBiddingDTO findOne(Long id) {
        log.debug("Request to get TaskBidding : {}", id);
        TaskBidding taskBidding = taskBiddingRepository.findOne(id);
        return taskBiddingMapper.toDto(taskBidding);
    }

    /**
     * Delete the taskBidding by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete TaskBidding : {}", id);
        taskBiddingRepository.delete(id);
        taskBiddingSearchRepository.delete(id);
    }

    /**
     * Search for the taskBidding corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<TaskBiddingDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of TaskBiddings for query {}", query);
        Page<TaskBidding> result = taskBiddingSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(taskBiddingMapper::toDto);
    }

	public void saveListTaskBidding(IdListVM idList, String classify) {
        log.debug("Request to save TaskBidding : {}");

        List<Long> idArr = idList.getIdList();
        Tasks tasks;
        TaskBidding taskBidding;

        // Declare variable to get project workflow
        PurchaseOrders purchaseOrders;
        ProjectTemplates projectTemplate;
        ProjectWorkflowsCriteria criteria;
        LongFilter projectTemplateFilter;
        StringFilter nameFilter;
        List<ProjectWorkflowsDTO> projectWorkflowsDTOs;
        String processKey;
        ProcessInstance processInstance;
		String processInstanceId;
		String currentRound;
		boolean isDefaultFlow = true;
		String nextRound = StringUtils.EMPTY;
		String status = StringUtils.EMPTY;
		Map<String, Object> taskParams;

        for (Long id : idArr) {
        	taskBidding = new TaskBidding();
        	tasks = this.tasksRepository.findOne(id);

        	// TODO: START: set bidding round follow workflow
        	processInstanceId = tasks.getData();
        	if (StringUtils.isNotEmpty(processInstanceId)) {
        		runtimeService.activateProcessInstanceById(processInstanceId);
    			processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId)
    					.singleResult();
        	} else {
        		isDefaultFlow = false;
        		purchaseOrders = tasks.getPackages().getPurchaseOrders();
    			projectTemplate = purchaseOrders.getProjectTemplates();
    			if (projectTemplate == null) {
    				projectTemplate = purchaseOrders.getProject().getProjectTemplates();
    			}
    			criteria = new ProjectWorkflowsCriteria();
    			projectTemplateFilter = new LongFilter();
    			projectTemplateFilter.setEquals(projectTemplate.getId());
    			criteria.setProjectTemplatesId(projectTemplateFilter);

    			nameFilter = new StringFilter();
    			nameFilter.setEquals("Task");
    			criteria.setName(nameFilter);

    			projectWorkflowsDTOs = this.projectWorkflowsQueryService.findByCriteria(criteria);
    			processKey = projectWorkflowsDTOs.get(0).getActivity();

    			processInstance = runtimeService.startProcessInstanceByKey(processKey);
    			tasks.setData(processInstance.getId());
        	}

        	taskParams = new HashMap<String, Object>();
        	currentRound = processInstance.getActivityId();
        	Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
			log.debug("Processing Task [" + task.getName() + "]");

			status = getCurrentStatus(tasks, currentRound);
			taskParams.put("status", status);
			taskService.complete(task.getId(), taskParams);

			processInstance = runtimeService.createProcessInstanceQuery()
					.processInstanceId(processInstance.getId()).singleResult();
			nextRound = processInstance != null ? processInstance.getActivityId() : AppConstants.END;
			if (!isDefaultFlow) {

				while (AppConstants.DONE.equals(status)) {
					task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
					currentRound = nextRound;
					status = getCurrentStatus(tasks, currentRound);
					taskParams.put("status", status);
					taskService.complete(task.getId(), taskParams);
					processInstance = runtimeService.createProcessInstanceQuery()
							.processInstanceId(processInstance.getId()).singleResult();

					if (processInstance != null) {
						nextRound = processInstance.getActivityId();
					} else {
						nextRound = AppConstants.END;
						break;
					}
				}
			}

			if (!AppConstants.END.equals(nextRound)) {
				runtimeService.suspendProcessInstanceById(processInstance.getId());
			}

			if (status.equals(AppConstants.NA)) {
				taskBidding.setBiddingRound(currentRound);
			}
			// TODO: END: set bidding round follow workflow

        	taskBidding.setTask(tasks);
        	taskBidding.setBiddingScope(this.getBiddingScopeByClassify(classify));
        	taskBidding.setBiddingStatus(BiddingStatus.NA);
        	taskBidding = taskBiddingRepository.save(taskBidding);
        	taskBiddingSearchRepository.save(taskBidding);
		}
	}

	public void createNewTaskBidding(Tasks task, BiddingScope scope, BiddingStatus status) {
		TaskBidding taskBidding = new TaskBidding();
    	taskBidding.setTask(task);
    	taskBidding.setBiddingScope(scope);
    	taskBidding.setBiddingStatus(status);
    	taskBidding = taskBiddingRepository.save(taskBidding);
    	taskBiddingSearchRepository.save(taskBidding);
	}

	private BiddingScope getBiddingScopeByClassify(String classify) {
		switch (classify) {
		case "PROJECT":
			return BiddingScope.PROJECT;
		case "BU":
			return BiddingScope.BU;
		case "PUBLIC":
			return BiddingScope.PUBLIC;
		default:
			return null;
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
			return "NA";
		}
	}

	/**
	 * conghk
	 * @param userLogin
	 * @return
	 */
	public HashMap<String, Object> getTaskHolding(String userLogin) {

		TaskBidding taskBiddingDomain = this.taskBiddingRepository.findHoldingTaskByUser(userLogin);
		if (taskBiddingDomain != null) {
			HashMap<String, Object> result = new HashMap<>();
//			List<FieldConfigVM> fieldConfigVMs = this.fieldConfigService.
//					getAllFieldConfig(taskBiddingDomain.getTask().getPackages().
//							getPurchaseOrders().getProject().getId(), AppConstants.TASK_ENTITY);
			TMSDynamicCustomFieldVM tmsDynamicCustomFieldVM = this.fieldConfigService.getAllFieldConfig(taskBiddingDomain.getTask().getPackages().
					getPurchaseOrders().getProject().getId(), AppConstants.TASK_ENTITY);
			List<FieldConfigVM> fieldConfigVMs = tmsDynamicCustomFieldVM.getFieldConfigVMs();
			result.put("taskBiddingHolding", this.taskBiddingMapper.toDto(taskBiddingDomain));
			result.put("fieldConfigVMs", fieldConfigVMs);
			return result;
		}
		return null;
	}

	/**
	 * conghk
	 * @param taskBiddingDto
	 * @return
	 */
	public TaskBiddingDTO updateHoldingTaskByOp(TaskBiddingDTO taskBiddingDto) {
		Optional<String> currentUserLogin = SecurityUtils.getCurrentUserLogin();
		TaskBidding taskBiddingDomain = this.taskBiddingRepository.findOne(taskBiddingDto.getId());
		if (taskBiddingDomain.getBiddingStatus().equals(BiddingStatus.NA)) {
			taskBiddingDomain = this.setStepPic(taskBiddingDomain, taskBiddingDto.getBiddingRound(), currentUserLogin.get());
			taskBiddingDomain.setBiddingStatus(BiddingStatus.HOLDING);
			taskBiddingDomain = this.taskBiddingRepository.save(taskBiddingDomain);
			this.taskBiddingSearchRepository.save(taskBiddingDomain);
			this.rollbackHoldingTask(taskBiddingDomain, taskBiddingDto.getBiddingRound(), taskBiddingDto.getBiddingHoldTime());
		}
		return this.taskBiddingMapper.toDto(taskBiddingDomain);
	};

	/**
	 *  kimhq
	 * @param biddingScope
	 * @param projectId
	 * @param purchaseOrderId
	 * @param packageId
	 * @param page
	 * @return
	 */
    public Page<TaskBiddingDTO> findAllTasksBiddingByBiddingScope( String biddingScope,Long projectId,
    		Long purchaseOrderId,Long packageId, Pageable page, String step, String currentUserLogin) {
        log.debug("Request to search for a page of TaskBiddings for query {}", biddingScope);
        Page<TaskBidding> result = taskBiddingRepository.findAllTasksBiddingByBiddingScopes(BiddingScope.valueOf(biddingScope),
        		projectId, purchaseOrderId, packageId, page, step, currentUserLogin);
        return result.map(taskBiddingMapper::toDto);
    }

	/**
	 * conghk
	 * @param taskBiddingDomain
	 * @param step
	 * @param userLogin
	 * @return
	 */
	private TaskBidding setStepPic(TaskBidding taskBiddingDomain, String step, String userLogin) {
		switch (step) {
		case "review1":
			taskBiddingDomain.getTask().setReview1(userLogin.toLowerCase());
			taskBiddingDomain.getTask().setReview1Status(ReviewStatus.OPEN);
			break;
		case "review2":
			taskBiddingDomain.getTask().setReview2(userLogin.toLowerCase());
			taskBiddingDomain.getTask().setReview2Status(ReviewStatus.OPEN);
			break;
		case "fi":
			taskBiddingDomain.getTask().setFi(userLogin.toLowerCase());
			taskBiddingDomain.getTask().setFiStatus(FIStatus.OPEN);
			break;
		case "fixer":
			taskBiddingDomain.getTask().setFixer(userLogin.toLowerCase());
			taskBiddingDomain.getTask().setFixStatus(FixStatus.OPEN);
			break;
		default:
			taskBiddingDomain.getTask().setOp(userLogin.toLowerCase());
			taskBiddingDomain.getTask().setOpStatus(OPStatus.OPEN);
			break;
		}
		return taskBiddingDomain;
	}


	/**
	 * conghk
	 * @param bidding
	 * @param step
	 */
	private void rollbackHoldingTask(TaskBidding bidding, String step, Integer holdingTime) {
		if (holdingTime == null) {
			holdingTime = DEFAULT_HOLDING_TIME;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MINUTE, holdingTime);
		Date startTime = calendar.getTime();
		scheduleJobService.scheduleCronJob(bidding.getId().toString(), BiddingTaskHolding.class, startTime, null, step);
	}

	public List<TaskBiddingDTO> getBiddingTasksOP(String userLogin) {
		List<OPStatus> statusOP = new ArrayList<OPStatus>();
        statusOP.add(OPStatus.OPEN);
        statusOP.add(OPStatus.DOING);
        statusOP.add(OPStatus.PENDING);
        statusOP.add(OPStatus.REOPEN);
        
        List<FixStatus> statusFix = new ArrayList<FixStatus>();
        statusFix.add(FixStatus.OPEN);
        statusFix.add(FixStatus.DOING);
        statusFix.add(FixStatus.PENDING);
        statusFix.add(FixStatus.REOPEN);
        
        List<TaskBidding> taskBiddingDomain = this.taskBiddingRepository.getBiddingTaskOPAndFixByUser(userLogin, statusOP, statusFix);
        return this.taskBiddingMapper.toDto(taskBiddingDomain);
    };
	public TaskBiddingDTO returnTaskByOp(TaskBiddingDTO taskBiddingDTO) {
		TaskBidding taskBidding = this.taskBiddingRepository.findOne(taskBiddingDTO.getId());
		if (taskBidding != null) {
			if (taskBidding.getBiddingStatus().equals(BiddingStatus.HOLDING)) {
				taskBidding.setBiddingStatus(BiddingStatus.NA);
				taskBidding = this.rollbackStepPic(taskBidding, taskBiddingDTO.getBiddingRound());
				taskBidding = this.taskBiddingRepository.save(taskBidding);
				this.taskBiddingSearchRepository.save(taskBidding);
			}
		}
		scheduleJobService.deleteJob(taskBiddingDTO.getId().toString(), taskBiddingDTO.getBiddingRound());
		return this.taskBiddingMapper.toDto(taskBidding);
	};

	private TaskBidding rollbackStepPic(TaskBidding taskBidding, String step) {
		switch (step) {
		case "review1":
			taskBidding.getTask().setReview1(null);
			taskBidding.getTask().setReview1Status(ReviewStatus.NA);
			break;
		case "review2":
			taskBidding.getTask().setReview2(null);
			taskBidding.getTask().setReview2Status(ReviewStatus.NA);
			break;
		case "fi":
			taskBidding.getTask().setFi(null);
			taskBidding.getTask().setFiStatus(FIStatus.NA);
			break;
		case "fixer":
			taskBidding.getTask().setFixer(null);
			taskBidding.getTask().setFixStatus(FixStatus.NA);
			break;
		default:
			taskBidding.getTask().setOp(null);
			taskBidding.getTask().setOpStatus(OPStatus.NA);
			break;
		}
		return taskBidding;
	}

	public TaskBidding getByTaskId(Long taskId) {
		return this.taskBiddingRepository.findByTaskId(taskId);
	}

	/**
	 * @param proId
	 * @param poId
	 * @return
	 */
	public List<MultiFieldConfigVM> getTaskMultiFieldConfig(Long proId, Long poId) {
		List<MultiFieldConfigVM> multiFieldConfigVMs = new ArrayList<>();
		MultiFieldConfigVM multiFieldConfigVM;
		List<FieldConfigVM> fieldConfigVMs;
		if (poId != null) {
			PurchaseOrders purchaseOrders = this.purchaseOrdersRepository.findOne(poId);
			TMSDynamicCustomFieldVM tmsDynamicCustomFieldVM = this.fieldConfigService.getAllDynamicFieldConfig(purchaseOrders, AppConstants.TASK_ENTITY);
			fieldConfigVMs = tmsDynamicCustomFieldVM.getFieldConfigVMs();

			multiFieldConfigVM = new MultiFieldConfigVM(poId.toString(), fieldConfigVMs);
			multiFieldConfigVMs.add(multiFieldConfigVM);
		} else {
			// fieldConfigVMs = this.fieldConfigService.getAllFieldConfig(proId, AppConstants.TASK_ENTITY);
			TMSDynamicCustomFieldVM tmsDynamicCustomFieldVM1 = this.fieldConfigService.getAllFieldConfig(proId, AppConstants.TASK_ENTITY);
			fieldConfigVMs = tmsDynamicCustomFieldVM1.getFieldConfigVMs();

			multiFieldConfigVM = new MultiFieldConfigVM("DEFAULT", fieldConfigVMs);
			multiFieldConfigVMs.add(multiFieldConfigVM);

			List<PurchaseOrders> purchaseOrderHaveWorkFlow = this.purchaseOrdersRepository.getPurchaseOrdersHaveWorkFlow(proId);
			for (PurchaseOrders purchaseOrders : purchaseOrderHaveWorkFlow) {
				TMSDynamicCustomFieldVM tmsDynamicCustomFieldVM = this.fieldConfigService.getAllDynamicFieldConfig(purchaseOrders, AppConstants.TASK_ENTITY);
				fieldConfigVMs = tmsDynamicCustomFieldVM.getFieldConfigVMs();

				multiFieldConfigVM = new MultiFieldConfigVM(purchaseOrders.getId().toString(), fieldConfigVMs);
				multiFieldConfigVMs.add(multiFieldConfigVM);
			}
		}
		return multiFieldConfigVMs;
	}
}
