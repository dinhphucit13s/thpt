package fpt.dps.dtms.service;


import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import io.github.jhipster.service.QueryService;
import io.github.jhipster.service.filter.StringFilter;
import fpt.dps.dtms.domain.*; // for static metamodels
import fpt.dps.dtms.repository.BugsRepository;
import fpt.dps.dtms.repository.PackagesRepository;
import fpt.dps.dtms.repository.TasksRepository;
import fpt.dps.dtms.repository.search.TasksSearchRepository;
import fpt.dps.dtms.service.dto.BugsDTO;
import fpt.dps.dtms.service.dto.CustomReportsDTO;
import fpt.dps.dtms.service.dto.PackagesDTO;
import fpt.dps.dtms.service.dto.ProjectUsersDTO;
import fpt.dps.dtms.service.dto.SelectDTO;
import fpt.dps.dtms.service.dto.TaskBiddingDTO;
import fpt.dps.dtms.service.dto.TasksCriteria;

import fpt.dps.dtms.service.dto.TasksDTO;
import fpt.dps.dtms.service.mapper.TasksMapper;
import fpt.dps.dtms.service.util.AppConstants;
import fpt.dps.dtms.service.util.ExcelGenerator;
import fpt.dps.dtms.service.util.FieldConfigService;
import fpt.dps.dtms.web.rest.vm.FieldConfigVM;
import fpt.dps.dtms.web.rest.vm.TMSDynamicCustomFieldVM;
import fpt.dps.dtms.web.rest.vm.TMSMonthViewVM;
import fpt.dps.dtms.domain.enumeration.TaskSeverity;
import fpt.dps.dtms.domain.enumeration.TaskPriority;
import fpt.dps.dtms.domain.enumeration.TaskAvailability;
import fpt.dps.dtms.domain.enumeration.OPStatus;
import fpt.dps.dtms.domain.enumeration.ProjectStatus;
import fpt.dps.dtms.domain.enumeration.ReviewStatus;
import fpt.dps.dtms.domain.enumeration.FixStatus;
import fpt.dps.dtms.domain.enumeration.ReviewStatus;
import fpt.dps.dtms.domain.enumeration.FIStatus;
import fpt.dps.dtms.domain.enumeration.ErrorSeverity;
import fpt.dps.dtms.domain.enumeration.TaskStatus;

/**
 * Service for executing complex queries for Tasks entities in the database.
 * The main input is a {@link TasksCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TasksDTO} or a {@link Page} of {@link TasksDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TasksQueryService extends QueryService<Tasks> {

    private final Logger log = LoggerFactory.getLogger(TasksQueryService.class);


    private final TasksRepository tasksRepository;

    private final TasksMapper tasksMapper;
    
    private final BugsRepository bugsRepository;

    private final TasksSearchRepository tasksSearchRepository;
    
    private final FieldConfigService fieldConfigService;
    
    private final ProjectUsersQueryService projectUsersQueryService;
    
    private final ExcelGenerator excelGenerator;
    
    private final TMSCustomFieldScreenValueQueryService tMSCustomFieldScreenValueQueryService;
    
    private final BugsQueryService bugsQueryService;
    
    private final TaskTrackingTimeService taskTrackingTimeService;
    
    private final CustomReportsQueryService customReportsQueryService;

    public TasksQueryService(TasksRepository tasksRepository, TasksMapper tasksMapper,
    		TasksSearchRepository tasksSearchRepository, BugsRepository bugsRepository,
    		ProjectUsersQueryService projectUsersQueryService, ExcelGenerator excelGenerator, TMSCustomFieldScreenValueQueryService tMSCustomFieldScreenValueQueryService,
    		FieldConfigService fieldConfigService, BugsQueryService bugsQueryService, TaskTrackingTimeService taskTrackingTimeService,
    		CustomReportsQueryService customReportsQueryService) {
    	this.fieldConfigService = fieldConfigService;
        this.tasksRepository = tasksRepository;
        this.tasksMapper = tasksMapper;
        this.tasksSearchRepository = tasksSearchRepository;
        this.bugsRepository = bugsRepository;
        this.projectUsersQueryService = projectUsersQueryService;
        this.excelGenerator = excelGenerator;
        this.tMSCustomFieldScreenValueQueryService = tMSCustomFieldScreenValueQueryService;
        this.bugsQueryService = bugsQueryService;
        this.taskTrackingTimeService = taskTrackingTimeService;
        this.customReportsQueryService = customReportsQueryService;
    }
    
    
    /**
     * Return a list Task UnAssign By Project
     * @param projectId
     * @param page
     * @return
     * @author KimHQ
     */
    @Transactional(readOnly = true)
    public Page<TasksDTO> getAllTaskUnAssign(List<String> workFlow, Long projectId,Long purchaseOrderId,Long packageId, Pageable page) {
        log.debug("get all task un-assign by project: {}, page: {}", projectId, page);
        final Page<Tasks> result = tasksRepository.findAllTaskUnAssign(workFlow, projectId,purchaseOrderId,packageId, page);
        return result.map(tasksMapper::toDto);
    }
    
    /**
     * Return a {@link List} of {@link TasksDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TasksDTO> findByCriteria(TasksCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Tasks> specification = createSpecification(criteria);
        return tasksMapper.toDto(tasksRepository.findAll(specification));
    }
    
    /**
     * ngocvx1
     * Return a {@link List} of {@link TasksDTO} which matches the criteria from the database
     * @param criteria The object which holds one of filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TasksDTO> findByCriteriaTypeOr(TasksCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Tasks> specification = createSpecificationTypeOr(criteria);
        return tasksMapper.toDto(tasksRepository.findAll(specification));
    }

    /**
     * ngocvx1
     * Find Tasks of user By Role OP
     * @param packageId
     * @param page
     * @return
     */
    @Transactional(readOnly = true)
    public List<TasksDTO> findTasksByRoleOP(String user) {
        log.debug("find Tasks of {} by Role OP", user);
        //return tasksRepository.findTasksOfUserByRoleOP(user).map(tasksMapper::toDto);
        return tasksMapper.toDto(tasksRepository.findTasksOfUserByRoleOP(user));

    }
    
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getBugTrackingMembers(Long projectId, Long purchaseOrderId, Long packId, Instant fromDate, Instant toDate, Pageable pageable) {
        log.debug("find Tasks of {} by Role member", projectId);
        Page<ProjectUsersDTO> pageUser = projectUsersQueryService.getAllUsersByPrjectId(projectId, pageable);
        List<ProjectUsersDTO> listUser = pageUser.getContent();
        List<Map<String, Object>> result = listUser.stream().map(user ->{
        	Map<String, Object> object = new HashMap<String, Object>();
        	Integer bugs = 0;
            Integer taskBug = 0;
            Boolean flag = true;
            Boolean notTask = false;
            List<Tasks> listTask = new ArrayList<>();
            if (purchaseOrderId != null) {
            	listTask = tasksRepository.getAllTaskAssignByPO(user.getUserLogin(), purchaseOrderId, fromDate, toDate);
            	if (listTask.size() == 0) {
            		notTask = true;
            	}
            } else if (packId != null) {
            	listTask = tasksRepository.getAllTaskAssignByPackageId(user.getUserLogin(), packId, fromDate, toDate);
            	if (listTask.size() == 0) {
            		notTask = true;
            	}
            } else {
            	listTask = tasksRepository.getAllTaskAssignByProject(user.getUserLogin(), projectId, fromDate, toDate);
            }
            String userLogin = StringUtils.EMPTY;
            for (Tasks task:listTask) {
            	userLogin = user.getUserLogin();
            	if (userLogin.equalsIgnoreCase(task.getOp())) {
            		int count = tasksRepository.countBugByTaskAndUserAssign(task.getId(), "review1");
            		if (count != 0 && flag) {
            			taskBug ++;
            			flag = false;
            		}
            		bugs += count;
            	}
            	if (userLogin.equalsIgnoreCase(task.getReview1())) {
            		int count = tasksRepository.countBugByTaskAndUserAssign(task.getId(), "review2");
            		if (count != 0 && flag) {
            			taskBug ++;
            			flag = false;
            		}
            		bugs += count;
            	}
            	if (userLogin.equalsIgnoreCase(task.getReview2())) {
            		int count = tasksRepository.countBugByTaskAndUserAssign(task.getId(), "fi");
            		if (count != 0 && flag) {
            			taskBug ++;
            			flag = false;
            		}
            		bugs += count;
            	}
            	//else {}
            	flag = true;
            }
            if (!notTask) {
            	object.put("userLogin", user.getUserLogin());
            	object.put("totalTasks", listTask.size());
            	object.put("totalTaskBug", taskBug);
            	object.put("totalBugs", bugs);
            	object.put("ratioTask", getRatioTask(Double.valueOf(taskBug.toString()), Double.valueOf(String.valueOf(listTask.size()))) + " %");
            }
            return object;
        }).collect(Collectors.toList());
        List<Map<String, Object>> filterUser = result.stream().filter(user ->{
        	if (user.size() == 0) {
        		return false;
        	}
        	return true;
        }).collect(Collectors.toList());
        Map<String, Object> totalPages = new HashMap<String, Object>();
        totalPages.put("total", pageUser.getTotalElements());
        filterUser.add(totalPages);
        return filterUser;
    }
    
    public String getRatioTask(Double header, Double sum) {
    	if (sum != 0) {
    		Double result = (header/sum)*100;
    		if (result != 0) {
        		return String.format("%.2f", result);
        	} else {
        		return "0";
        	}
    	} else {
    		return "0";
    	}
    }
    
    /**
     * ngocvx1
     * Find Tasks of user by roles OP in project
     * @param roles of user
     * @param userLogin
     * @param projects id of project
     * @return
     */
    @Transactional(readOnly = true)
    public List<TasksDTO> findTasksByRolesInProjects(String roles, String userLogin, Long projects) {
        log.debug("find Tasks of {} by Role OP in project {}", userLogin, projects);
        
        List<TasksDTO> listTaskResult = new ArrayList<TasksDTO>();
        
        List<TaskStatus> taskStatus = new ArrayList<TaskStatus>();
        taskStatus.add(TaskStatus.OPEN);
        taskStatus.add(TaskStatus.DOING);
        taskStatus.add(TaskStatus.PENDING);
        
        List<OPStatus> statusOP = new ArrayList<OPStatus>();
        statusOP.add(OPStatus.OPEN);
        statusOP.add(OPStatus.DOING);
        statusOP.add(OPStatus.PENDING);
        statusOP.add(OPStatus.REOPEN);
        
        List<ReviewStatus> statusReview = new ArrayList<ReviewStatus>();
        statusReview.add(ReviewStatus.OPEN);
        statusReview.add(ReviewStatus.DOING);
        statusReview.add(ReviewStatus.REOPEN);
        statusReview.add(ReviewStatus.PENDING);
        
        List<FIStatus> statusFI = new ArrayList<FIStatus>();
        statusFI.add(FIStatus.OPEN);
        statusFI.add(FIStatus.DOING);
    	
        switch (roles) {
		case "OPERATOR":
			listTaskResult = tasksMapper.toDto(tasksRepository.findTasksOfUserByRolesOPInProject(userLogin, projects, statusOP, taskStatus));
			break;
		case "REVIEWER":
			listTaskResult = tasksMapper.toDto(tasksRepository.findTasksOfUserByRolesReviewerInProject(userLogin, projects, statusReview, taskStatus));
			break;
		case "FI":
			listTaskResult = tasksMapper.toDto(tasksRepository.findTasksOfUserByRolesFIInProject(userLogin, projects, statusFI, taskStatus));
			break;
		default:
			listTaskResult = tasksMapper.toDto(tasksRepository.findTasksOfUserByRolesOPInProject(userLogin, projects, statusOP, taskStatus));
			break;
		}
        return listTaskResult;

    }
    
    /**
     * ngocvx1
     * Find Tasks of user by roles OP in project with paging
     * @param roles of user
     * @param userLogin
     * @param projects id of project
     * @return
     */
    @Transactional(readOnly = true)
    public Page<TasksDTO> findTasksByRolesInProjectsWithPaging(String roles, String userLogin, Long projects, Long purchaseOrders,
    		Long packages, String step, Pageable pageable) {
        log.debug("find Tasks of {} by Role OP in project {} with paging.", userLogin, projects);
        
        List<TaskStatus> taskStatus = new ArrayList<TaskStatus>();
        taskStatus.add(TaskStatus.OPEN);
        taskStatus.add(TaskStatus.DOING);
        taskStatus.add(TaskStatus.PENDING);
        
        List<OPStatus> statusOP = new ArrayList<OPStatus>();
        statusOP.add(OPStatus.OPEN);
        statusOP.add(OPStatus.DOING);
        statusOP.add(OPStatus.PENDING);
        statusOP.add(OPStatus.REOPEN);
        
        List<ReviewStatus> statusReview = new ArrayList<ReviewStatus>();
        statusReview.add(ReviewStatus.OPEN);
        statusReview.add(ReviewStatus.DOING);
        statusReview.add(ReviewStatus.REOPEN);
        statusReview.add(ReviewStatus.PENDING);
        
        List<FIStatus> statusFI = new ArrayList<FIStatus>();
        statusFI.add(FIStatus.OPEN);
        statusFI.add(FIStatus.DOING);
        statusFI.add(FIStatus.PENDING);
        
        List<FixStatus> statusFix = new ArrayList<FixStatus>();
        statusFix.add(FixStatus.OPEN);
        statusFix.add(FixStatus.DOING);
        statusFix.add(FixStatus.PENDING);
        statusFix.add(FixStatus.REOPEN);
        Page<TasksDTO> pageTasks = null; 
        switch (roles) {
		case "OPERATOR":
			pageTasks = tasksRepository.findTasksOfUserByRolesOPInProjectPaging(userLogin, projects, purchaseOrders, packages, statusOP, statusFix, taskStatus, pageable).map(tasksMapper::toDto);
			break;
		case "REVIEWER":
			pageTasks = tasksRepository.findTasksOfUserByRolesReviewerInProjectPaging(userLogin, projects, purchaseOrders, packages, step, statusReview, taskStatus, pageable).map(tasksMapper::toDto);
			break;
		case "FI":
			pageTasks = tasksRepository.findTasksOfUserByRolesFIInProjectPaging(userLogin, projects, purchaseOrders, packages, statusFI, taskStatus, pageable).map(tasksMapper::toDto);
			break;
		default:
			pageTasks = tasksRepository.findTasksOfUserByRolesOPInProjectPaging(userLogin, projects, purchaseOrders, packages, statusOP, statusFix, taskStatus, pageable).map(tasksMapper::toDto);
			break;
		}
       
        List<TasksDTO> listTasks = pageTasks.getContent();

        /*Labeling row  incase Task has bug waiting to fix
        and  incase Task had bug fixed and waiting to review
        START*/
        List<BugsDTO> listBugs;
        for (TasksDTO task: listTasks) {
        	listBugs = new ArrayList<>();
        	task.setBugCount(bugsQueryService.countBugByTasksId(task.getId()));
        	task.setBugCountRV1(bugsQueryService.countBugRVFIByTasksId(task.getId(), "review1"));
        	task.setBugCountRV2(bugsQueryService.countBugRVFIByTasksId(task.getId(), "review2"));
        	task.setBugCountFI(bugsQueryService.countBugRVFIByTasksId(task.getId(), "fi"));
        	listBugs = null;
        }
        /*END*/

        for (TasksDTO task: listTasks) {
        	task.setTmsCustomFieldScreenValueDTO(tMSCustomFieldScreenValueQueryService.getAllTMSCustomFieldScreenValueByTaskId(task.getId()));
        }
        return pageTasks;
    }
    
    /**
     * ngocvx1
     * Find Tasks done of user by roles OP in project
     * @param roles of user
     * @param userLogin
     * @param projects id of project
     * @return
     */
    @Transactional(readOnly = true)
    public List<TasksDTO> findTasksDoneByRolesInProjects(String roles, String userLogin, Long projects) {
        log.debug("find Tasks of {} by Role OP in project {}", userLogin, projects);
        
        List<TasksDTO> listTaskResult = new ArrayList<TasksDTO>();
        
        List<TaskStatus> taskStatus = new ArrayList<TaskStatus>();
        taskStatus.add(TaskStatus.OPEN);
        taskStatus.add(TaskStatus.DOING);
        taskStatus.add(TaskStatus.PENDING);
        taskStatus.add(TaskStatus.DONE);
        taskStatus.add(TaskStatus.CANCEL);
        taskStatus.add(TaskStatus.CLOSED);
        
        
        List<OPStatus> statusOP = new ArrayList<OPStatus>();
        statusOP.add(OPStatus.DONE);
        
        List<ReviewStatus> statusReview = new ArrayList<ReviewStatus>();
        statusReview.add(ReviewStatus.DONE);
        statusReview.add(ReviewStatus.NOT_GOOD);
        
        List<FIStatus> statusFI = new ArrayList<FIStatus>();
        statusFI.add(FIStatus.DONE);
        statusFI.add(FIStatus.NOT_GOOD);
    	
        switch (roles) {
		case "OPERATOR":
			listTaskResult = tasksMapper.toDto(tasksRepository.findTasksOfUserByRolesOPInProject(userLogin, projects, statusOP, taskStatus));
			break;
		case "REVIEWER":
			listTaskResult = tasksMapper.toDto(tasksRepository.findTasksOfUserByRolesReviewerInProject(userLogin, projects, statusReview, taskStatus));
			break;
		case "FI":
			listTaskResult = tasksMapper.toDto(tasksRepository.findTasksOfUserByRolesFIInProject(userLogin, projects, statusFI, taskStatus));
			break;
		default:
			listTaskResult = tasksMapper.toDto(tasksRepository.findTasksOfUserByRolesOPInProject(userLogin, projects, statusOP, taskStatus));
			break;
		}
        return listTaskResult;

    }
    
    /**
     * ngocvx1
     * Find Tasks done of user by roles OP in project
     * @param roles of user
     * @param userLogin
     * @param projects id of project
     * @return
     * @throws ParseException 
     */
    @Transactional(readOnly = true)
    public Page<TasksDTO> findTasksDoneByRolesInProjectsWithPaging(String roles, String userLogin, Long projects, String beginTime, String endTime, Pageable pageable) throws ParseException {
        log.debug("find Tasks of {} by DONE in project {}", userLogin, projects);
        Instant from = null;
        Instant to = null;
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		if (StringUtils.isNotEmpty(beginTime)) {
			from = sf.parse(beginTime).toInstant().atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay().toInstant(ZoneOffset.UTC);
		} 

		if (StringUtils.isNotEmpty(endTime)) {
			to = sf.parse(endTime).toInstant().atZone(ZoneId.systemDefault()).toLocalDate().atTime(LocalTime.MAX).toInstant(ZoneOffset.UTC);
		}
        
        List<TaskStatus> taskStatus = new ArrayList<TaskStatus>();
        taskStatus.add(TaskStatus.OPEN);
        taskStatus.add(TaskStatus.DOING);
        taskStatus.add(TaskStatus.PENDING);
        taskStatus.add(TaskStatus.DONE);
        taskStatus.add(TaskStatus.CANCEL);
        taskStatus.add(TaskStatus.CLOSED);
        
        List<OPStatus> statusOP = new ArrayList<OPStatus>();
        statusOP.add(OPStatus.DONE);
        
        List<ReviewStatus> statusReview = new ArrayList<ReviewStatus>();
        statusReview.add(ReviewStatus.DONE);
        statusReview.add(ReviewStatus.NOT_GOOD);
        
        List<FIStatus> statusFI = new ArrayList<FIStatus>();
        statusFI.add(FIStatus.DONE);
        statusFI.add(FIStatus.NOT_GOOD);
        
        List<FixStatus> statusFix = new ArrayList<FixStatus>();
        statusFix.add(FixStatus.DONE);
        Page<TasksDTO> pageTasks = null; 
        switch (roles) {
		case "OPERATOR": 
			pageTasks = tasksRepository.findTasksDoneOfUserByRolesOPInProjectPaging(userLogin, projects, statusOP, statusFix, from, to, taskStatus, pageable).map(tasksMapper::toDto);
			break;
		case "REVIEWER":
			pageTasks = tasksRepository.findTasksDoneOfUserByRolesReviewerInProjectPaging(userLogin, projects, statusReview, from, to, taskStatus, pageable).map(tasksMapper::toDto);
			break;
		case "FI":
			pageTasks = tasksRepository.findTasksDoneOfUserByRolesFIInProjectPaging(userLogin, projects, statusFI, from, to, taskStatus, pageable).map(tasksMapper::toDto);
			break;
		default:
			pageTasks = tasksRepository.findTasksDoneOfUserByRolesOPInProjectPaging(userLogin, projects, statusOP, statusFix, from, to, taskStatus, pageable).map(tasksMapper::toDto);
			break;
		}
        
        List<TasksDTO> listTasks = pageTasks.getContent();
        for (TasksDTO task: listTasks) {
        	task.setTmsCustomFieldScreenValueDTO(tMSCustomFieldScreenValueQueryService.getAllTMSCustomFieldScreenValueByTaskId(task.getId()));
        }
        return pageTasks;
    }
    
    /**
     * 
     * @param roles: roles of user
     * @param userLogin
     * @param listStatus: list status to select
     * @return
     */
    @Transactional(readOnly = true)
    public List<TasksDTO> findTaskByRolesAndListStatusOfUser(String roles, String userLogin, String listStatus) {
    	String[] statuses = listStatus.split("_");
    	List<Tasks> result = null;
    	switch (roles.toLowerCase()) {
    	case "operator":
    		List<OPStatus> OPStatuses = new ArrayList<OPStatus>();
    		for(String status : statuses) {
    			for(OPStatus st : OPStatus.values()) {
    				if(status.equalsIgnoreCase(st.toString())) {
    					OPStatuses.add(st);
    					break;
    				}
    			}
    		}
    		result = tasksRepository.findTaskRolesOPInListStatusOfUser(userLogin, OPStatuses);
    		return tasksMapper.toDto(result);
    	case "review1":
    		List<ReviewStatus> RV1Statuses = new ArrayList<ReviewStatus>();
    		for(String status : statuses) {
    			for(ReviewStatus st : ReviewStatus.values()) {
    				if(status.equalsIgnoreCase(st.toString())) {
    					RV1Statuses.add(st);
    					break;
    				}
    			}
    		}
    		result = tasksRepository.findTaskRolesRV1InListStatusOfUser(userLogin, RV1Statuses);
    		return tasksMapper.toDto(result);
    	case "review2":
    		List<ReviewStatus> RV2Statuses = new ArrayList<ReviewStatus>();
    		for(String status : statuses) {
    			for(ReviewStatus st : ReviewStatus.values()) {
    				if(status.equalsIgnoreCase(st.toString())) {
    					RV2Statuses.add(st);
    					break;
    				}
    			}
    		}
    		result = tasksRepository.findTaskRolesRV2InListStatusOfUser(userLogin, RV2Statuses);
    		return tasksMapper.toDto(result);
    	case "fixer":
    		List<FixStatus> FixStatuses = new ArrayList<FixStatus>();
    		for(String status : statuses) {
    			for(FixStatus st : FixStatus.values()) {
    				if(status.equalsIgnoreCase(st.toString())) {
    					FixStatuses.add(st);
    					break;
    				}
    			}
    		}
    		result = tasksRepository.findTaskRolesFixerInListStatusOfUser(userLogin, FixStatuses);
    		
    	case "fi":
    		List<FIStatus> FiStatuses = new ArrayList<FIStatus>();
    		for(String status : statuses) {
    			for(FIStatus st : FIStatus.values()) {
    				if(status.equalsIgnoreCase(st.toString())) {
    					FiStatuses.add(st);
    					break;
    				}
    			}
    		}
    		result = tasksRepository.findTaskRolesFIInListStatusOfUser(userLogin, FiStatuses);
    		return tasksMapper.toDto(result);
    	default:
    		return null;
    	}
    }
    
    /**
     * @author ngocvx1
     * @param userLogin
     * @return
     */
    @Transactional(readOnly = true)
	public Integer countTasksDoingOfUserLogin(String userLogin) {
        List<TaskStatus> taskStatus = new ArrayList<TaskStatus>();
        taskStatus.add(TaskStatus.OPEN);
        taskStatus.add(TaskStatus.DOING);
        taskStatus.add(TaskStatus.PENDING);

		return this.tasksRepository.countTasksDoingOfUserLogin(userLogin, taskStatus);
	}
    
    /**
     * conghk
     * @param userLogin
     * @return
     */
    public Integer countTaskLateInTheEndDate(String userLogin) {

        List<String> taskStatus = Arrays.asList(AppConstants.OPEN, AppConstants.DOING, AppConstants.PENDING);

        List<String> roundStatus = Arrays.asList(AppConstants.OPEN, AppConstants.DOING, AppConstants.PENDING, AppConstants.REOPEN);

    	return this.tasksRepository.countTaskLateInTheEndDate(userLogin, taskStatus, roundStatus);
    }
    
    /**
    * @author ngocvx1
    * @param userLogin
    * @return
    */
   @Transactional(readOnly = true)
	public Integer countTasksPendingOfUserLogin(String userLogin) {
		return this.tasksRepository.countTasksPendingOfUserLogin(userLogin);
	}

    /**
     * Return a {@link Page} of {@link TasksDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TasksDTO> findByCriteria(TasksCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Tasks> specification = createSpecification(criteria);
        final Page<Tasks> result = tasksRepository.findAll(specification, page);
        return result.map(tasksMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<TasksDTO> findTasksByPackageId(Long packageId, Pageable page) {
        log.debug("find Tasks by Package Id");
        Page<TasksDTO> tasksDTO = tasksRepository.getAllTasksByPackageId(packageId, page).map(tasksMapper::toDto);
        List<TasksDTO> listTasks = tasksDTO.getContent();
        for (TasksDTO task: listTasks) {
        	task.setTmsCustomFieldScreenValueDTO(tMSCustomFieldScreenValueQueryService.getAllTMSCustomFieldScreenValueByTaskId(task.getId()));
        }
        return tasksDTO;
    }
    
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getBugTrackingTasks(Long projectId, Long purchaseOrderId, Long packageId, Instant fromDate, Instant toDate, String userLogin, Pageable page) {
    	Page<Tasks> pageTasks = null;
    	boolean checkNumberRound = true;
    	if (packageId != null) {
    		pageTasks = tasksRepository.getAllTasksByPackageId(packageId, fromDate, toDate, page);
    	}
    	else if (purchaseOrderId != null) {
    		pageTasks = tasksRepository.getAllTasksByPurchaseOrderId(purchaseOrderId, fromDate, toDate, page);
    	} else {
    		pageTasks = tasksRepository.getAllTasksByProjectId(projectId, fromDate, toDate, page);
    	}
        List<Tasks> tasks = pageTasks.getContent();
        List<String> listColumns = columnsDynamicCustomReport(userLogin);
        List<Map<String, Object>> result = tasks.stream().map(t ->{
        	Map<String, Object> object = new HashMap<String, Object>();
        	object.put("name", t.getName());
        	object.put("frame", t.getFrame());
        	object.put("data", t.getData());
            object.put("file_name", t.getFileName());
            object.put("type", t.getType());
            object.put("availability", t.getAvailability());
            object.put("actual_object", t.getActualObject());
            object.put("duration", t.getDuration());
            object.put("target", t.getTarget());
            object.put("error_quantity", t.getErrorQuantity());
            object.put("error_severity", t.getErrorSeverity());
            object.put("parent", t.getParent());
        	object.put("totalBugsTask", tasksRepository.countBugByTaskId(t.getId()));
        	object.put("totalBugOpen", tasksRepository.countBugOpenByTaskId(t.getId()));
        	this.putValueTrackingTime(object, t.getId(), AppConstants.ROUND_OP, "op");
        	this.putValueTrackingTime(object, t.getId(), AppConstants.ROUND_REVIEW1, "review1");
        	if (listColumns.size() > 0) {
        		for (String column: listColumns) {
            		if (column.equals("Review2")) {
            			this.putValueTrackingTime(object, t.getId(), AppConstants.ROUND_REVIEW2, "review2");
            		} else if (column.equals("Fi")) {
            			this.putValueTrackingTime(object, t.getId(), AppConstants.ROUND_FI, "fi");
            		} else if (column.equals("Fixer")) {
            			this.putValueTrackingTime(object, t.getId(), AppConstants.ROUND_FIXER, "fixer");
            		}
            	}
        	}
        	if (tasks.get(0).getId() == t.getId()) {
        		object.put("countOpRound", countNumberRecordOfRound(tasks, null, AppConstants.ROUND_OP, "Op"));
                object.put("countReview1Round", countNumberRecordOfRound(tasks, null, AppConstants.ROUND_REVIEW1, "Review1"));
                object.put("countReview2Round", countNumberRecordOfRound(tasks, listColumns, AppConstants.ROUND_REVIEW2, "Review2"));
                object.put("countFiRound", countNumberRecordOfRound(tasks, listColumns, AppConstants.ROUND_FI, "Fi"));
                object.put("countFixerRound", countNumberRecordOfRound(tasks, listColumns, AppConstants.ROUND_FIXER, "Fixer"));
        	}
        	return object;
        }).collect(Collectors.toList());
        Map<String, Object> totalPages = new HashMap<String, Object>();
        totalPages.put("total", pageTasks.getTotalElements());
        result.add(totalPages);
        return result;
    }
    
    /**
     * Get all value tracking time by round
     * @param object
     * @param taskId
     * @param round
     * @param nameRound
     */
    private void putValueTrackingTime(Map<String, Object> object, Long taskId, String round, String nameRound) {
    	List<TaskTrackingTime> taskTrackingTimes = this.taskTrackingTimeService.findAllByTaskIdAndRoleAndEndStatus(taskId, round, "DONE");
    	if (taskTrackingTimes.size() > 0) {
    		int i = 1;
    		for(TaskTrackingTime taskTracking: taskTrackingTimes) {
    			String name = nameRound + i;
    			String effort = nameRound + i + "Effort";
    			String startTime = nameRound + i + "StartTime";
    			String endTime = nameRound + i + "EndTime";
    			object.put(name, taskTracking.getUserLogin());
            	object.put(effort, getFixerEffort(taskTracking));
            	object.put(startTime, taskTracking.getStartTime());
            	object.put(endTime, taskTracking.getEndTime());
            	i ++;
    		}
    	}
    }
    
    /**
     * Count round in list Task.
     * @param tasks
     * @return
     */
    private int countNumberRecordOfRound(List<Tasks> tasks, List<String> listColumns, String round, String nameRound) {
    	int countRowNumber = 0;
    	if (listColumns == null) {
    		for(Tasks task: tasks) {
	    		List<TaskTrackingTime> taskTrackingTimes = this.taskTrackingTimeService.findAllByTaskIdAndRoleAndEndStatus(task.getId(), round, "DONE");
	        	if (taskTrackingTimes.size() > countRowNumber) {
	        		countRowNumber = taskTrackingTimes.size();
	        	}
	    	}
    	} else {
    		if (listColumns.size() > 0) {
        		for (String column: listColumns) {
        			if (column.equals(nameRound)) {
        				for(Tasks task: tasks) {
        		    		List<TaskTrackingTime> taskTrackingTimes = this.taskTrackingTimeService.findAllByTaskIdAndRoleAndEndStatus(task.getId(), round, "DONE");
        		        	if (taskTrackingTimes.size() > countRowNumber) {
        		        		countRowNumber = taskTrackingTimes.size();
        		        	}
        		    	}
        			}
        		}
        	}
    	}
    	return countRowNumber;
    }
    
    /**
	 * Get member effort by role
	 * @param taskTrackingTimes
	 * @param userLogin
	 * @return
	 */
	private int getFixerEffort(TaskTrackingTime TaskTracking) {
		return TaskTracking.getDuration() != null ? TaskTracking.getDuration() : 0;
	}
    
    /**
     * Get effort by round
     * @param taskId
     * @param round
     * @return
     */
    private int getMemberEffort(Long taskId, String round) {
    	List<TaskTrackingTime> taskTrackingTimes = this.taskTrackingTimeService.findAllByTaskId(taskId);
    	int duration = 0;
		for (TaskTrackingTime taskTrackingTime : taskTrackingTimes) {
			if(round.equals(taskTrackingTime.getRole())) {
				if (taskTrackingTime.getDuration() != null) {
					duration += taskTrackingTime.getDuration();
				}
			}
		}
		return duration;
    }
    
    /**
     * Get all columns custom report.
     * @return
     */
    private List<String> columnsDynamicCustomReport(String userLogin) {
		List<String> columns = new ArrayList<>();
		CustomReportsDTO customReportDTO = customReportsQueryService.findCustomReportsByPageName("TrackingTasks", userLogin);
		if (customReportDTO != null) {
			ObjectMapper mapper = new ObjectMapper();
			try {
				ArrayNode nodes = (ArrayNode) mapper.readTree(customReportDTO.getValue());
				for (int i = 0; i < nodes.size(); i++) {
					columns.add(nodes.get(i).textValue());
				}
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return columns;
	}
    
    @Transactional(readOnly = true)
    public InputStreamResource exportTrackingManagementTasks(Long projectId, Long purchaseOrderId, Long packageId, Instant fromDate, Instant toDate, String userLogin) throws IOException {
    	List<Tasks> tasks = new ArrayList<>();
    	if (packageId != null) {
    		tasks = tasksRepository.getAllTasksByPackageId(packageId, fromDate, toDate);
    	}
    	else if (purchaseOrderId != null) {
    		tasks = tasksRepository.getAllTasksByPurchaseOrderId(purchaseOrderId, fromDate, toDate);
    	} else {
    		tasks = tasksRepository.getAllTasksByProjectId(projectId, fromDate, toDate);
    	}
    	InputStreamResource inputStreamResource = null;
    	if(tasks != null) {
    		inputStreamResource = new InputStreamResource(excelGenerator.exportTrackingManagementTasks(tasks, userLogin));
    	}
        
        return inputStreamResource;
    }
    
    @Transactional(readOnly = true)
    public SelectDTO taskRatioBugs(Long projectId, Long purchaseOrderId, Long packageId, Instant fromDate, Instant toDate) {
    	List<Tasks> listTasks = new ArrayList<>();
    	int taskBug = 0;
    	int totalBugs = 0;
    	if (projectId != null) {
    		listTasks = tasksRepository.getAllTasksByProjectId(projectId, fromDate, toDate);
    	}
    	else if (purchaseOrderId != null) {
    		listTasks = tasksRepository.getAllTasksByPurchaseOrderId(purchaseOrderId, fromDate, toDate);
    	} else {
    		listTasks = tasksRepository.getAllTasksByPackageId(packageId, fromDate, toDate);
    	}
        for (Tasks task: listTasks) {
        	if (tasksRepository.countBugByTaskId(task.getId()) != 0) {
        		taskBug ++;
        	}
        	/*get total bug of tasks*/ 
        	int taskBugs = tasksRepository.countBugByTaskId(task.getId());
        	totalBugs = taskBugs + totalBugs;
        }
        SelectDTO obj = new SelectDTO();
        obj.setId(null);
        obj.setTotalBug(String.valueOf(totalBugs));
        obj.setTotalTasks(String.valueOf(listTasks.size()));
    	obj.setName(getRatioTask(Double.valueOf(taskBug), Double.valueOf(String.valueOf(listTasks.size()))) + "%");
    	obj.setTotalTasksPerBugs(getRatioTask(Double.valueOf(totalBugs), Double.valueOf(String.valueOf(listTasks.size()))) + "%");
    	return obj;
    }
    
    /**
     * conghk
     * Find Tasks of user by roles OP in project
     * @param packageId
     * @param page
     * @return
     */
    @Transactional(readOnly = true)
    public List<TMSMonthViewVM> findTasksByUserLogin(String userLogin, int month) {
        log.debug("find Tasks of ", userLogin);
        List<Tasks> listTaskResult = new ArrayList<>();
        listTaskResult = tasksRepository.findTasksByUserLogin(userLogin, month);
        List<TMSMonthViewVM> tmsMonthViewList = new ArrayList<>();
        if(!CollectionUtils.isEmpty(listTaskResult)) {
        
	        TMSMonthViewVM tmsMonthView = new TMSMonthViewVM();
	        ZoneId zone = ZoneId.systemDefault();
	        LocalTime workStart = LocalTime.of(8, 0);
			LocalTime workEnd = LocalTime.of(17, 0);
			LocalTime beginRest = LocalTime.of(12, 0);
			LocalTime endRest = LocalTime.of(13, 0);
			ZonedDateTime startTask = listTaskResult.get(0).getEstimateStartTime().atZone(zone);
			ZonedDateTime endTask;
			ZonedDateTime startHour;
			ZonedDateTime endHour;
			ZonedDateTime dateIndex = listTaskResult.get(0).getEstimateStartTime().atZone(zone);
			ZonedDateTime lastDateIndex = listTaskResult.get(listTaskResult.size()-1).getEstimateStartTime().atZone(zone);
			Instant viewDate = null;
			int doneTask = 0;
			int totalTask = 0;
			int inProcessTask = 0;
			long totalHours = 0;
			
	        for (Tasks tasks : listTaskResult) {
	        	
				startTask = tasks.getEstimateStartTime().atZone(zone);
				
				endTask = tasks.getEstimateEndTime().atZone(zone);
				
				// TODO THE SAME CODE BEGIN
				if(startTask.toLocalDate().isAfter(dateIndex.toLocalDate())) {
					
					tmsMonthView.setStart(viewDate);
					tmsMonthView.setTotalTask(totalTask);
					tmsMonthView.setTotalHours(totalHours);
					tmsMonthView.setDoneTask(doneTask);
					tmsMonthView.setInProcessTask(inProcessTask);
					tmsMonthViewList.add(tmsMonthView);
					
					tmsMonthView = new TMSMonthViewVM();
					dateIndex = startTask;
					totalTask = 0;
					totalHours = 0;
					doneTask = 0;
					inProcessTask = 0;
				}
				totalTask++;
				viewDate = startTask.toInstant();
				if (userLogin.equals(tasks.getOp())) {
					if(tasks.getOpStatus().equals(OPStatus.DONE)) {
						doneTask++;
					} else {
						inProcessTask++;
					}
				}
				
				if (userLogin.equals(tasks.getReview1())) {
					if(tasks.getReview1Status().equals(ReviewStatus.DONE)) {
						doneTask++;
					} else {
						inProcessTask++;
					}
				}
				
				if (userLogin.equals(tasks.getReview2())) {
					if(tasks.getReview2Status().equals(ReviewStatus.DONE)) {
						doneTask++;
					} else {
						inProcessTask++;
					}
				}
				
				if (userLogin.equals(tasks.getFi())) {
					if(tasks.getFiStatus().equals(FIStatus.DONE)) {
						doneTask++;
					} else {
						inProcessTask++;
					}
				}
				// TODO THE SAME CODE END
				
				startHour = startTask;
				if (startTask.toLocalTime().isBefore(workStart)) { // before 8 AM
				    startHour = startTask.with(workStart); // set time to 8 AM
				} else if (startTask.toLocalTime().isAfter(workEnd)) { // after 5 PM
				    startHour = startTask.with(workEnd); // set time to 5 PM
				}
				
				endHour = endTask;
				if (endTask.toLocalTime().isAfter(workEnd)) { // after 5 PM
				    endHour = endTask.with(workEnd); // set time to 5 PM
				} else if (endTask.toLocalTime().isBefore(workStart)) { // before 8 AM
				    endHour = endTask.with(workStart); // set time to 8 AM
				}
				
				while(startHour.isBefore(endHour)) {
				    if (startHour.toLocalDate().equals(endHour.toLocalDate())) { // same day
				        totalHours += ChronoUnit.MILLIS.between(startHour, endHour);
				        totalHours = this.minusHoursBetweenRest(totalHours, beginRest, endRest, startTask, endTask);
				        
				        break;
				    } else {
				        ZonedDateTime endOfDay = startHour.with(workEnd); // 5PM of the day
				        totalHours += ChronoUnit.MILLIS.between(startHour, endOfDay);
				        
				        startHour = startHour.plusDays(1).with(workStart); // go to next day
				        
				        totalHours = this.minusHoursBetweenRest(totalHours, beginRest, endRest, startTask, endOfDay);
				        
				        startTask = startHour;
				        
				        // TODO THE SAME CODE BEGIN
				        if(startTask.toLocalDate().isAfter(dateIndex.toLocalDate())) {
				        	
							tmsMonthView.setStart(viewDate);
							tmsMonthView.setTotalTask(totalTask);
							tmsMonthView.setTotalHours(totalHours);
							tmsMonthView.setDoneTask(doneTask);
							tmsMonthView.setInProcessTask(inProcessTask);
							tmsMonthViewList.add(tmsMonthView);
							
							tmsMonthView = new TMSMonthViewVM();
							dateIndex = startTask;
							totalTask = 0;
							totalHours = 0;
							doneTask = 0;
							inProcessTask = 0;
						}
						totalTask++;
						viewDate = startTask.toInstant();
						if (userLogin.equals(tasks.getOp())) {
							if(tasks.getOpStatus().equals(OPStatus.DONE)) {
								doneTask++;
							} else {
								inProcessTask++;
							}
						}
						
						if (userLogin.equals(tasks.getReview1())) {
							if(tasks.getReview1Status().equals(ReviewStatus.DONE)) {
								doneTask++;
							} else {
								inProcessTask++;
							}
						}
						
						if (userLogin.equals(tasks.getReview2())) {
							if(tasks.getReview2Status().equals(ReviewStatus.DONE)) {
								doneTask++;
							} else {
								inProcessTask++;
							}
						}
						
						if (userLogin.equals(tasks.getFi())) {
							if(tasks.getFiStatus().equals(FIStatus.DONE)) {
								doneTask++;
							} else {
								inProcessTask++;
							}
						}
						//TODO THE SAME CODE END
				    }
				}
			}
	        
	        if(!dateIndex.toLocalDate().isBefore(lastDateIndex.toLocalDate())) {
				tmsMonthView.setStart(viewDate);
				tmsMonthView.setTotalTask(totalTask);
				tmsMonthView.setTotalHours(totalHours);
				tmsMonthView.setDoneTask(doneTask);
				tmsMonthView.setInProcessTask(inProcessTask);
				tmsMonthViewList.add(tmsMonthView);
			}
        }
        return tmsMonthViewList;
    }
    
    public List<TasksDTO> findTasksOfDay(String userLogin, String dateFormat) {
    	List<TasksDTO> listTaskResult = new ArrayList<TasksDTO>();
    	 
    	listTaskResult = tasksMapper.toDto(tasksRepository.findTasksOfDay(userLogin, dateFormat));
    	 
		return listTaskResult;
	}
    
    private long minusHoursBetweenRest(long totalHours, LocalTime beginRest, LocalTime endRest, ZonedDateTime startTask, ZonedDateTime endTask) {
    	if(startTask.toLocalTime().isAfter(beginRest)
    			&& startTask.toLocalTime().isBefore(endRest)) {
    		totalHours -= ChronoUnit.MILLIS.between(startTask, startTask.with(endRest));
    	}
    	
    	if(endTask.toLocalTime().isAfter(beginRest)
    			&& endTask.toLocalTime().isBefore(endRest)) {
    		totalHours -= ChronoUnit.MILLIS.between(endTask.with(beginRest), endTask);
    	}
    	
    	if(startTask.toLocalTime().isBefore(beginRest)
    			&& endTask.toLocalTime().isAfter(endRest)) {
    		totalHours -= ChronoUnit.MILLIS.between(beginRest, endRest);
    	}
    	
    	return totalHours;
    }

	private void setDataToTms(ZonedDateTime startTask, ZonedDateTime dateIndex, TMSMonthViewVM tmsMonthView, Instant viewDate,
    		int totalTask, long totalHours, int doneTask, int inProcessTask, List<TMSMonthViewVM> tmsMonthViewList,
    		String userLogin, Tasks tasks) {
    	
    	if(startTask.toLocalDate().isAfter(dateIndex.toLocalDate())) {
			tmsMonthView.setStart(viewDate);
			tmsMonthView.setTotalTask(totalTask);
			tmsMonthView.setTotalHours(totalHours);
			tmsMonthView.setDoneTask(doneTask);
			tmsMonthView.setInProcessTask(inProcessTask);
			tmsMonthViewList.add(tmsMonthView);
			
			tmsMonthView = new TMSMonthViewVM();
			dateIndex = startTask;
			totalTask = 0;
			totalHours = 0;
			doneTask = 0;
			inProcessTask = 0;
		}
		totalTask++;
		viewDate = startTask.toInstant();
		if (userLogin.equals(tasks.getOp())) {
			if(tasks.getOpStatus().equals(OPStatus.DONE)) {
				doneTask++;
			} else {
				inProcessTask++;
			}
		}
		
		if (userLogin.equals(tasks.getReview1())) {
			if(tasks.getReview1Status().equals(ReviewStatus.DONE)) {
				doneTask++;
			} else {
				inProcessTask++;
			}
		}
		
		if (userLogin.equals(tasks.getReview2())) {
			if(tasks.getReview2Status().equals(ReviewStatus.DONE)) {
				doneTask++;
			} else {
				inProcessTask++;
			}
		}
		
		if (userLogin.equals(tasks.getFi())) {
			if(tasks.getFiStatus().equals(FIStatus.DONE)) {
				doneTask++;
			} else {
				inProcessTask++;
			}
		}
    }
	
    /**
     * Function to convert TasksCriteria to a {@link Specifications}
     */
    private Specifications<Tasks> createSpecification(TasksCriteria criteria) {
        Specifications<Tasks> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Tasks_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Tasks_.name));
            }
            if (criteria.getSeverity() != null) {
                specification = specification.and(buildSpecification(criteria.getSeverity(), Tasks_.severity));
            }
            if (criteria.getPriority() != null) {
                specification = specification.and(buildSpecification(criteria.getPriority(), Tasks_.priority));
            }
            if (criteria.getData() != null) {
                specification = specification.and(buildStringSpecification(criteria.getData(), Tasks_.data));
            }
            if (criteria.getFileName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFileName(), Tasks_.fileName));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getType(), Tasks_.type));
            }
            if (criteria.getAvailability() != null) {
                specification = specification.and(buildSpecification(criteria.getAvailability(), Tasks_.availability));
            }
            if (criteria.getFrame() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFrame(), Tasks_.frame));
            }
            if (criteria.getActualObject() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getActualObject(), Tasks_.actualObject));
            }
            if (criteria.getOpStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getOpStatus(), Tasks_.opStatus));
            }
            if (criteria.getEstimateStartTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEstimateStartTime(), Tasks_.estimateStartTime));
            }
            if (criteria.getEstimateEndTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEstimateEndTime(), Tasks_.estimateEndTime));
            }
            if (criteria.getOpStartTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getOpStartTime(), Tasks_.opStartTime));
            }
            if (criteria.getOpEndTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getOpEndTime(), Tasks_.opEndTime));
            }
            if (criteria.getReview1Status() != null) {
                specification = specification.and(buildSpecification(criteria.getReview1Status(), Tasks_.review1Status));
            }
            if (criteria.getReview1StartTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getReview1StartTime(), Tasks_.review1StartTime));
            }
            if (criteria.getReview1EndTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getReview1EndTime(), Tasks_.review1EndTime));
            }
            if (criteria.getFixStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getFixStatus(), Tasks_.fixStatus));
            }
            if (criteria.getFixStartTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFixStartTime(), Tasks_.fixStartTime));
            }
            if (criteria.getFixEndTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFixEndTime(), Tasks_.fixEndTime));
            }
            if (criteria.getReview2Status() != null) {
                specification = specification.and(buildSpecification(criteria.getReview2Status(), Tasks_.review2Status));
            }
            if (criteria.getReview2StartTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getReview2StartTime(), Tasks_.review2StartTime));
            }
            if (criteria.getReview2EndTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getReview2EndTime(), Tasks_.review2EndTime));
            }
            if (criteria.getFiStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getFiStatus(), Tasks_.fiStatus));
            }
            if (criteria.getFiStartTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFiStartTime(), Tasks_.fiStartTime));
            }
            if (criteria.getFiEndTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFiEndTime(), Tasks_.fiEndTime));
            }
            if (criteria.getDuration() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDuration(), Tasks_.duration));
            }
            if (criteria.getTarget() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTarget(), Tasks_.target));
            }
            if (criteria.getErrorQuantity() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getErrorQuantity(), Tasks_.errorQuantity));
            }
            if (criteria.getErrorSeverity() != null) {
                specification = specification.and(buildSpecification(criteria.getErrorSeverity(), Tasks_.errorSeverity));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), Tasks_.status));
            }
            if (criteria.getParent() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getParent(), Tasks_.parent));
            }
            if (criteria.getOp() != null) {
                specification = specification.and(buildStringSpecification(criteria.getOp(), Tasks_.op));
            }
            if (criteria.getReview1() != null) {
                specification = specification.and(buildStringSpecification(criteria.getReview1(), Tasks_.review1));
            }
            if (criteria.getReview2() != null) {
                specification = specification.and(buildStringSpecification(criteria.getReview2(), Tasks_.review2));
            }
            if (criteria.getFixer() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFixer(), Tasks_.fixer));
            }
            if (criteria.getFi() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFi(), Tasks_.fi));
            }
            if (criteria.getTmsCustomFieldScreenValueId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getTmsCustomFieldScreenValueId(), Tasks_.tmsCustomFieldScreenValues, TMSCustomFieldScreenValue_.id));
            }
            if (criteria.getPackagesId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getPackagesId(), Tasks_.packages, Packages_.id));
            }
        }
        return specification;
    }
    
    
    
    /**
     * ngocvx1
     * Function to convert TasksCriteria to a {@link Specifications}
     */
    private Specifications<Tasks> createSpecificationTypeOr(TasksCriteria criteria) {
        Specifications<Tasks> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.or(buildSpecification(criteria.getId(), Tasks_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.or(buildStringSpecification(criteria.getName(), Tasks_.name));
            }
            if (criteria.getSeverity() != null) {
                specification = specification.or(buildSpecification(criteria.getSeverity(), Tasks_.severity));
            }
            if (criteria.getPriority() != null) {
                specification = specification.or(buildSpecification(criteria.getPriority(), Tasks_.priority));
            }
            if (criteria.getData() != null) {
                specification = specification.or(buildStringSpecification(criteria.getData(), Tasks_.data));
            }
            if (criteria.getFileName() != null) {
                specification = specification.or(buildStringSpecification(criteria.getFileName(), Tasks_.fileName));
            }
            if (criteria.getType() != null) {
                specification = specification.or(buildStringSpecification(criteria.getType(), Tasks_.type));
            }
            if (criteria.getAvailability() != null) {
                specification = specification.or(buildSpecification(criteria.getAvailability(), Tasks_.availability));
            }
            if (criteria.getFrame() != null) {
                specification = specification.or(buildRangeSpecification(criteria.getFrame(), Tasks_.frame));
            }
            if (criteria.getActualObject() != null) {
                specification = specification.or(buildRangeSpecification(criteria.getActualObject(), Tasks_.actualObject));
            }
            if (criteria.getOpStatus() != null) {
                specification = specification.or(buildSpecification(criteria.getOpStatus(), Tasks_.opStatus));
            }
            if (criteria.getEstimateStartTime() != null) {
                specification = specification.or(buildRangeSpecification(criteria.getEstimateStartTime(), Tasks_.estimateStartTime));
            }
            if (criteria.getEstimateEndTime() != null) {
                specification = specification.or(buildRangeSpecification(criteria.getEstimateEndTime(), Tasks_.estimateEndTime));
            }
            if (criteria.getOpStartTime() != null) {
                specification = specification.or(buildRangeSpecification(criteria.getOpStartTime(), Tasks_.opStartTime));
            }
            if (criteria.getOpEndTime() != null) {
                specification = specification.or(buildRangeSpecification(criteria.getOpEndTime(), Tasks_.opEndTime));
            }
            if (criteria.getReview1Status() != null) {
                specification = specification.or(buildSpecification(criteria.getReview1Status(), Tasks_.review1Status));
            }
            if (criteria.getReview1StartTime() != null) {
                specification = specification.or(buildRangeSpecification(criteria.getReview1StartTime(), Tasks_.review1StartTime));
            }
            if (criteria.getReview1EndTime() != null) {
                specification = specification.or(buildRangeSpecification(criteria.getReview1EndTime(), Tasks_.review1EndTime));
            }
            if (criteria.getFixStatus() != null) {
                specification = specification.or(buildSpecification(criteria.getFixStatus(), Tasks_.fixStatus));
            }
            if (criteria.getFixStartTime() != null) {
                specification = specification.or(buildRangeSpecification(criteria.getFixStartTime(), Tasks_.fixStartTime));
            }
            if (criteria.getFixEndTime() != null) {
                specification = specification.or(buildRangeSpecification(criteria.getFixEndTime(), Tasks_.fixEndTime));
            }
            if (criteria.getReview2Status() != null) {
                specification = specification.or(buildSpecification(criteria.getReview2Status(), Tasks_.review2Status));
            }
            if (criteria.getReview2StartTime() != null) {
                specification = specification.or(buildRangeSpecification(criteria.getReview2StartTime(), Tasks_.review2StartTime));
            }
            if (criteria.getReview2EndTime() != null) {
                specification = specification.or(buildRangeSpecification(criteria.getReview2EndTime(), Tasks_.review2EndTime));
            }
            if (criteria.getFiStatus() != null) {
                specification = specification.or(buildSpecification(criteria.getFiStatus(), Tasks_.fiStatus));
            }
            if (criteria.getFiStartTime() != null) {
                specification = specification.or(buildRangeSpecification(criteria.getFiStartTime(), Tasks_.fiStartTime));
            }
            if (criteria.getFiEndTime() != null) {
                specification = specification.or(buildRangeSpecification(criteria.getFiEndTime(), Tasks_.fiEndTime));
            }
            if (criteria.getDuration() != null) {
                specification = specification.or(buildRangeSpecification(criteria.getDuration(), Tasks_.duration));
            }
            if (criteria.getTarget() != null) {
                specification = specification.or(buildRangeSpecification(criteria.getTarget(), Tasks_.target));
            }
            if (criteria.getErrorQuantity() != null) {
                specification = specification.or(buildRangeSpecification(criteria.getErrorQuantity(), Tasks_.errorQuantity));
            }
            if (criteria.getErrorSeverity() != null) {
                specification = specification.or(buildSpecification(criteria.getErrorSeverity(), Tasks_.errorSeverity));
            }
            if (criteria.getStatus() != null) {
                specification = specification.or(buildSpecification(criteria.getStatus(), Tasks_.status));
            }
            if (criteria.getParent() != null) {
                specification = specification.or(buildRangeSpecification(criteria.getParent(), Tasks_.parent));
            }
            if (criteria.getOp() != null) {
                specification = specification.or(buildStringSpecification(criteria.getOp(), Tasks_.op));
            }
            if (criteria.getReview1() != null) {
                specification = specification.or(buildStringSpecification(criteria.getReview1(), Tasks_.review1));
            }
            if (criteria.getReview2() != null) {
                specification = specification.or(buildStringSpecification(criteria.getReview2(), Tasks_.review2));
            }
            if (criteria.getFixer() != null) {
                specification = specification.or(buildStringSpecification(criteria.getFixer(), Tasks_.fixer));
            }
            if (criteria.getFi() != null) {
                specification = specification.or(buildStringSpecification(criteria.getFi(), Tasks_.fi));
            }
            if (criteria.getTmsCustomFieldScreenValueId() != null) {
                specification = specification.or(buildReferringEntitySpecification(criteria.getTmsCustomFieldScreenValueId(), Tasks_.tmsCustomFieldScreenValues, TMSCustomFieldScreenValue_.id));
            }
            if (criteria.getPackagesId() != null) {
                specification = specification.or(buildReferringEntitySpecification(criteria.getPackagesId(), Tasks_.packages, Packages_.id));
            }
        }
        return specification;
    }
    
    private boolean hasRound(Long id, String round) {
		List<FieldConfigVM> fieldConfigVMs = getTaskFieldConfig(id);
		for (FieldConfigVM fieldConfigVM : fieldConfigVMs) {
			if(fieldConfigVM.getField().equals(round)) {
				return true;
			}
		}
		return false;
	}
    
    private List<FieldConfigVM> getTaskFieldConfig(Long id) {
		// List<FieldConfigVM> fieldConfigVMs = this.fieldConfigService.getAllFieldConfig(id, AppConstants.TASK_ENTITY);
		TMSDynamicCustomFieldVM tmsDynamicCustomFieldVM = this.fieldConfigService.getAllFieldConfig(id, AppConstants.TASK_ENTITY);
		List<FieldConfigVM> fieldConfigVMs = tmsDynamicCustomFieldVM.getFieldConfigVMs();
		return fieldConfigVMs;
	}
}
