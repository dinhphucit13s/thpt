package fpt.dps.dtms.service;

import fpt.dps.dtms.domain.ProjectWorkflows;
import fpt.dps.dtms.domain.TaskTrackingTime;
import fpt.dps.dtms.domain.Tasks;
import fpt.dps.dtms.domain.ProjectUsers;
import fpt.dps.dtms.repository.TaskTrackingTimeRepository;
import fpt.dps.dtms.repository.search.TaskTrackingTimeSearchRepository;
import io.github.jhipster.service.QueryService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Service Implementation for managing TaskTrackingTime.
 */
@Service
@Transactional
public class TaskTrackingTimeQueryService extends QueryService<TaskTrackingTimeService>{

    private final Logger log = LoggerFactory.getLogger(TaskTrackingTimeQueryService.class);

    private final TaskTrackingTimeRepository taskTrackingTimeRepository;

    private final TaskTrackingTimeSearchRepository taskTrackingTimeSearchRepository;
    

    public TaskTrackingTimeQueryService(TaskTrackingTimeRepository taskTrackingTimeRepository, TaskTrackingTimeSearchRepository taskTrackingTimeSearchRepository) {
        this.taskTrackingTimeRepository = taskTrackingTimeRepository;
        this.taskTrackingTimeSearchRepository = taskTrackingTimeSearchRepository;
    }

    /**
     * Save a taskTrackingTime.
     *
     * @param taskTrackingTime the entity to save
     * @return the persisted entity
     */
    public TaskTrackingTime save(TaskTrackingTime taskTrackingTime) {
        log.debug("Request to save TaskTrackingTime : {}", taskTrackingTime);
        TaskTrackingTime result = taskTrackingTimeRepository.save(taskTrackingTime);
        taskTrackingTimeSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the taskTrackingTimes.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<TaskTrackingTime> findAll(Pageable pageable) {
        log.debug("Request to get all TaskTrackingTimes");
        return taskTrackingTimeRepository.findAll(pageable);
    }

    /**
     * Get one taskTrackingTime by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public TaskTrackingTime findOne(Long id) {
        log.debug("Request to get TaskTrackingTime : {}", id);
        return taskTrackingTimeRepository.findOne(id);
    }

    /**
     * Delete the taskTrackingTime by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete TaskTrackingTime : {}", id);
        taskTrackingTimeRepository.delete(id);
        taskTrackingTimeSearchRepository.delete(id);
    }
    
    /**
     * get number name distinct
     * @param taskId
     * @param role
     * @return
     */
    @Transactional(readOnly = true)
    public List<String> countUserLogin(Long taskId, String role) {
    	return taskTrackingTimeRepository.getListUser(taskId, role);
    }
    
    /**
     * Effort
     * @param projectId
     * @param purchaseOrderId
     * @param packageId
     * @return
     */
    @Transactional(readOnly = true)
    public List<Map<String, Object>> countEffortByMonth(Long projectId, Long purchaseOrderId, Long packageId) {
    	List<LocalDate> listLocalDate = listLocalMonths();
    	List<String> roundOp;
    	List<String> roundRV1;
    	List<String> roundRV2;
    	List<String> roundFixer;
    	List<String> roundFi;
    	List<ProjectUsers> listProjectUser = taskTrackingTimeRepository.getProjectUserByProjectId(projectId);
    	if (purchaseOrderId != null) {
    		roundOp = taskTrackingTimeRepository.getAllUserWithRoleOpByPurchaseOrderId(purchaseOrderId);
    		roundRV1 = taskTrackingTimeRepository.getAllUserWithRoleReview1ByPurchaseOrderId(purchaseOrderId);
    		roundRV2 = taskTrackingTimeRepository.getAllUserWithRoleReview2ByPurchaseOrderId(purchaseOrderId);
    		roundFixer = taskTrackingTimeRepository.getAllUserWithRoleFixerByPurchaseOrderId(purchaseOrderId);
    		roundFi = taskTrackingTimeRepository.getAllUserWithRoleFiByPurchaseOrderId(purchaseOrderId);
    	}
    	else if (packageId != null) {
    		roundOp = taskTrackingTimeRepository.getAllUserWithRoleOpByPackageId(packageId);
    		roundRV1 = taskTrackingTimeRepository.getAllUserWithRoleReview1ByPackageId(packageId);
    		roundRV2 = taskTrackingTimeRepository.getAllUserWithRoleReview2ByPackageId(packageId);
    		roundFixer = taskTrackingTimeRepository.getAllUserWithRoleFixerByPackageId(packageId);
    		roundFi = taskTrackingTimeRepository.getAllUserWithRoleFiByPackageId(packageId);
    	} else {
    		roundOp = taskTrackingTimeRepository.getAllUserWithRoleOpByProjectId(projectId);
    		roundRV1 = taskTrackingTimeRepository.getAllUserWithRoleReview1ByProjectId(projectId);
    		roundRV2 = taskTrackingTimeRepository.getAllUserWithRoleReview2ByProjectId(projectId);
    		roundFixer = taskTrackingTimeRepository.getAllUserWithRoleFixerByProjectId(projectId);
    		roundFi = taskTrackingTimeRepository.getAllUserWithRoleFiByProjectId(projectId);
    	}
    	List<Map<String, Object>> result = listLocalDate.stream().map(day ->{
    		Map<String, Object> object = new HashMap<String, Object>();
    		LocalDate localDateDay = getEndDayOfMonth(day);
    		object.put("month", day.getMonth().name());
    		object.put("op", totalEffortOfRound(roundOp, projectId, purchaseOrderId, packageId, "op", day, localDateDay));
    		object.put("review1", totalEffortOfRound(roundRV1, projectId, purchaseOrderId, packageId, "review1", day, localDateDay));
    		object.put("review2", totalEffortOfRound(roundRV2, projectId, purchaseOrderId, packageId, "review2", day, localDateDay));
    		object.put("fixer", totalEffortOfRound(roundFixer, projectId, purchaseOrderId, packageId, "fixer", day, localDateDay));
    		object.put("fi", totalEffortOfRound(roundFi, projectId, purchaseOrderId, packageId, "fi", day, localDateDay));
    		object.put("totalEffortPlan", totalEffortPlanOfMonth(listProjectUser, day));
    		object.put("totalActualEffort", totalActualEffortOfMonth(projectId, purchaseOrderId, packageId, roundOp, roundRV1, roundRV2, roundFixer, roundFi, day, localDateDay));
    		return object;
    	}).collect(Collectors.toList());
    	result.add(totalEffortOfYear(listLocalDate, projectId, purchaseOrderId, packageId, roundOp, roundRV1, roundRV2, roundFixer, roundFi));
    	return result;
    }
    
    /**
     * Get total effort of Year
     * @param listMonth
     * @param projectId
     * @param purchaseOrderId
     * @param packageId
     * @param roundOp
     * @param roundRV1
     * @param roundRV2
     * @param roundFixer
     * @param roundFi
     * @return
     */
    private Map<String, Object> totalEffortOfYear(List<LocalDate> listMonth, Long projectId, Long purchaseOrderId, Long packageId, List<String> roundOp, List<String> roundRV1, List<String> roundRV2, List<String> roundFixer, List<String> roundFi) {
    	float totalOp = 0;
    	float totalRV1 = 0;
    	float totalRV2 = 0;
    	float totalFixer = 0;
    	float totalFi = 0;
    	float totalEffortPlan = 0;
    	float totalActualEffort = 0;
    	List<ProjectUsers> listProjectUser = taskTrackingTimeRepository.getProjectUserByProjectId(projectId);
    	for (LocalDate month: listMonth) {
    		LocalDate localDateDay = getEndDayOfMonth(month);
    		totalOp += Float.parseFloat(totalEffortOfRound(roundOp, projectId, purchaseOrderId, packageId, "op", month, localDateDay));
    		totalRV1 += Float.parseFloat(totalEffortOfRound(roundRV1, projectId, purchaseOrderId, packageId, "review1", month, localDateDay));
    		totalRV2 += Float.parseFloat(totalEffortOfRound(roundRV2, projectId, purchaseOrderId, packageId, "review2", month, localDateDay));
    		totalFixer += Float.parseFloat(totalEffortOfRound(roundFixer, projectId, purchaseOrderId, packageId, "fixer", month, localDateDay));
    		totalFi += Float.parseFloat(totalEffortOfRound(roundFi, projectId, purchaseOrderId, packageId, "fi", month, localDateDay));
    		totalEffortPlan += totalEffortPlanOfMonth(listProjectUser, month);
    		totalActualEffort += Float.parseFloat(totalActualEffortOfMonth(projectId, purchaseOrderId, packageId, roundOp, roundRV1, roundRV2, roundFixer, roundFi, month, localDateDay));
    	}
    	Map<String, Object> totalRow = new HashMap<String, Object>();
    	totalRow.put("month", "Total");
    	totalRow.put("op", totalOp);
    	totalRow.put("review1", totalRV1);
    	totalRow.put("review2", totalRV2);
    	totalRow.put("fixer", totalFixer);
    	totalRow.put("fi", totalFi);
    	totalRow.put("totalEffortPlan", totalEffortPlan);
    	totalRow.put("totalActualEffort", totalActualEffort);
    	return totalRow;
    }
    
    /**
     * Get end day in month
     * @param localDateMonth
     * @return
     */
    public LocalDate getEndDayOfMonth(LocalDate localDateMonth) {
    	return localDateMonth.withDayOfMonth(
    			localDateMonth.getMonth().length(localDateMonth.isLeapYear()));
    }
    
    /**
     * List all month in current year
     * @return
     */
    private List<LocalDate> listLocalMonths() {
    	int year = Calendar.getInstance().get(Calendar.YEAR);
    	LocalDate localDate = LocalDate.of(year, 01 , 01);
    	List<LocalDate> listLocalDate = new ArrayList<LocalDate>();
    	for (int i = 1; i < 13; i++) {
    		LocalDate localDateMonth = localDate.withMonth(i);
    		listLocalDate.add(localDateMonth);
    	}
    	return listLocalDate;
    }
    
    /**
     * Total effort by all round
     * @param roundOP
     * @param roundRV1
     * @param roundRV2
     * @param roundFixer
     * @param roundFi
     * @return
     */
    private String totalEffortOfMonths(List<String> roundOP, List<String> roundRV1, List<String> roundRV2, List<String> roundFixer, List<String> roundFi) {
    	float sumEffortPlan = 0;
    	if (roundOP.size() > 0) {
    		Float effortRoundOp = taskTrackingTimeRepository.getSumEffortUser(roundOP);
    		if (effortRoundOp != null) {
        		sumEffortPlan += effortRoundOp;
        	}
    	} else if (roundRV1.size() > 0) {
    		Float effortRoundRV1 = taskTrackingTimeRepository.getSumEffortUser(roundRV1);
    		if (effortRoundRV1 != null) {
        		sumEffortPlan += effortRoundRV1;
        	}
    	}else if (roundRV2.size() > 0) {
    		Float effortRoundRV2 = taskTrackingTimeRepository.getSumEffortUser(roundRV2);
    		if (effortRoundRV2 != null) {
        		sumEffortPlan += effortRoundRV2;
        	}
    	}else if (roundFixer.size() > 0) {
    		Float effortRoundFixer = taskTrackingTimeRepository.getSumEffortUser(roundFixer);
    		if (effortRoundFixer != null) {
        		sumEffortPlan += effortRoundFixer;
        	}
    	}else if (roundFi.size() > 0) {
    		Float effortRoundFi = taskTrackingTimeRepository.getSumEffortUser(roundFi);
    		if (effortRoundFi != null) {
        		sumEffortPlan += effortRoundFi;
        	}
    	}
    	if (sumEffortPlan != 0) {
    		return String.format("%.2f", sumEffortPlan);
    	}
    	return "0";
    }
    
    /**
     * Total effort plan of month
     * @param listProjectUser
     * @param monthLocal
     * @return
     */
    private Float totalEffortPlanOfMonth(List<ProjectUsers> listProjectUser, LocalDate monthLocal) {
    	float totalEffortOfMonth = 0;
    	for (ProjectUsers pu: listProjectUser) {
    		totalEffortOfMonth += getDatesBetween(pu, monthLocal);
    	}
    	return totalEffortOfMonth;
    }
    
    /**
     * Total effort plan by User
     * @param user
     * @param monthLocal
     * @return
     */
    public Float getDatesBetween(ProjectUsers user, LocalDate monthLocal) {
        long numOfDaysBetween = ChronoUnit.DAYS.between(user.getStartDate(), user.getEndDate()) + 1;
        long listDayOfMonth = 0;
        List<LocalDate> listDate = IntStream.iterate(0, i -> i + 1)
          .limit(numOfDaysBetween)
          .mapToObj(i -> user.getStartDate().plusDays(i))
          .collect(Collectors.toList());
        for(LocalDate localDate:listDate) {
        	if (localDate.getMonthValue() == monthLocal.getMonthValue()) {
        		Date date = java.sql.Date.valueOf(localDate);
        		if (date.getDay() != 0 && date.getDay() != 6) {
        			listDayOfMonth ++;
        		}
        	}
    	}
        return listDayOfMonth*user.getEffortPlan();
    }
    
    /**
     * Total effort by role and project Id
     * @param round
     * @param projectId
     * @param role
     * @param startDate
     * @param endDate
     * @return
     */
    private String totalEffortOfRound(List<String> round, Long projectId, Long purchaseOrderId, Long packageId, String role, LocalDate startDate, LocalDate endDate) {
    	int totalEffort = 0;
    	LocalDateTime endDateTime = endDate.atTime(23,59,59);
    	for (String op: round) {
    		Integer effortOp = 0;
    		if (purchaseOrderId != null) {
    			effortOp = taskTrackingTimeRepository.sumActualEffortOfUserForPurchase(purchaseOrderId, op, role, startDate.atStartOfDay(ZoneId.systemDefault()).toInstant(), endDateTime.atZone(ZoneId.systemDefault()).toInstant());
    		} else if (packageId != null) {
    			effortOp = taskTrackingTimeRepository.sumActualEffortOfUserAndRoleForPackage(packageId, op, role, startDate.atStartOfDay(ZoneId.systemDefault()).toInstant(), endDateTime.atZone(ZoneId.systemDefault()).toInstant());
    		} else {
    			effortOp = taskTrackingTimeRepository.sumActualEffortOfUserByProjectIdAndRole(projectId, op, role, startDate.atStartOfDay(ZoneId.systemDefault()).toInstant(), endDateTime.atZone(ZoneId.systemDefault()).toInstant());
    		}
    		
    		if (effortOp != null) {
    			totalEffort += effortOp;
    		}
    	}
    	if (totalEffort != 0) {
    		return String.format("%.2f", (float)totalEffort/60);
    	}
    	return "0";
    }
    
    /**
     * Total effort by month
     * @param projectId
     * @param roundOp
     * @param roundRV1
     * @param roundRV2
     * @param roundFixer
     * @param roundFi
     * @param startDate
     * @param endDate
     * @return
     */
    private String totalActualEffortOfMonth(Long projectId, Long purchaseOrderId, Long packageId, List<String> roundOp, List<String> roundRV1, List<String> roundRV2, List<String> roundFixer, List<String> roundFi, LocalDate startDate, LocalDate endDate) {
    	float totalActualEffort = 0;
    	totalActualEffort += Float.parseFloat(totalEffortOfRound(roundOp, projectId, purchaseOrderId, packageId, "op", startDate, endDate));
    	totalActualEffort += Float.parseFloat(totalEffortOfRound(roundRV1, projectId, purchaseOrderId, packageId, "review1", startDate, endDate));
    	totalActualEffort += Float.parseFloat(totalEffortOfRound(roundRV2, projectId, purchaseOrderId, packageId, "review2", startDate, endDate));
    	totalActualEffort += Float.parseFloat(totalEffortOfRound(roundFixer, projectId, purchaseOrderId, packageId, "fixer", startDate, endDate));
    	totalActualEffort += Float.parseFloat(totalEffortOfRound(roundFi, projectId, purchaseOrderId, packageId, "fi", startDate, endDate));
    	return String.format("%.2f", totalActualEffort);
    }
    
    /**
     * Sum actual effort tasks by projectId
     * @param id
     * @param userLogin
     * @param startDate
     * @param endDate
     * @return
     */
    public String sumActualEffort(Long id, String userLogin, LocalDate startDate, LocalDate endDate) {
        log.debug("Request to delete TaskTrackingTime : {}", id);
        LocalDateTime endDateTime = endDate.atTime(23,59,59);
        Integer intActualEffort = taskTrackingTimeRepository.sumActualEffortOfUser(id, userLogin, startDate.atStartOfDay(ZoneId.systemDefault()).toInstant(), endDateTime.atZone(ZoneId.systemDefault()).toInstant());
        if (intActualEffort != null) {
        	return String.format("%.2f", (float)intActualEffort/60);
        }
        return "0";
    }
    
    /**
     * Get effort by package id
     * @param id
     * @param userLogin
     * @param startDate
     * @param endDate
     * @return
     */
    public String sumActualEffortByPackage(Long id, String userLogin, LocalDate startDate, LocalDate endDate) {
        log.debug("Request to delete TaskTrackingTime : {}", id);
        LocalDateTime endDateTime = endDate.atTime(23,59,59);
        Integer intActualEffort = taskTrackingTimeRepository.sumActualEffortOfUserForPackage(id, userLogin, startDate.atStartOfDay(ZoneId.systemDefault()).toInstant(), endDateTime.atZone(ZoneId.systemDefault()).toInstant());
        if (intActualEffort != null) {
        	return String.format("%.2f", (float)intActualEffort/60);
        }
        return "0";
    }
    
    /**
     * Search for the taskTrackingTime corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<TaskTrackingTime> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of TaskTrackingTimes for query {}", query);
        Page<TaskTrackingTime> result = taskTrackingTimeSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }

	public TaskTrackingTime findTaskIdAndUserLogin(Long id, String userLogin) {
		return this.taskTrackingTimeRepository.findTop1ByTaskIdAndUserLoginAndEndTimeOrderByIdDesc(id, userLogin, null);
	}
}
