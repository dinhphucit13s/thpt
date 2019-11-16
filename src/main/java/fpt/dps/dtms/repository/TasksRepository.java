package fpt.dps.dtms.repository;

import fpt.dps.dtms.domain.Packages;
import fpt.dps.dtms.domain.Projects;
import fpt.dps.dtms.domain.Tasks;
import fpt.dps.dtms.domain.enumeration.FIStatus;
import fpt.dps.dtms.domain.enumeration.FixStatus;
import fpt.dps.dtms.domain.enumeration.OPStatus;
import fpt.dps.dtms.domain.enumeration.ReviewStatus;
import fpt.dps.dtms.domain.enumeration.TaskStatus;

import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Tasks entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TasksRepository extends JpaRepository<Tasks, Long>, JpaSpecificationExecutor<Tasks> {
	@Query("select task from Tasks task where task.packages.id =:packageId And (:fromDate = NULL OR task.estimateEndTime > :fromDate) AND (:toDate = NULL OR task.estimateEndTime < :toDate)")
	Page<Tasks> getAllTasksByPackageId(@Param("packageId") Long packageId, @Param("fromDate") Instant fromDate, @Param("toDate") Instant toDate, Pageable pageable);

	@Query("select task from Tasks task where task.packages.id =:packageId")
	Page<Tasks> getAllTasksByPackageId(@Param("packageId") Long packageId, Pageable pageable);

	@Query("select task from Tasks task where task.packages.id =:packageId And (:fromDate = NULL OR task.estimateEndTime > :fromDate) AND (:toDate = NULL OR task.estimateEndTime < :toDate)")
	List<Tasks> getAllTasksByPackageId(@Param("packageId") Long packageId, @Param("fromDate") Instant fromDate, @Param("toDate") Instant toDate);

	@Query("select task from Tasks task where task.packages.purchaseOrders.id =:purchaseOrderId And (:fromDate = NULL OR task.estimateEndTime > :fromDate) AND (:toDate = NULL OR task.estimateEndTime < :toDate)")
	Page<Tasks> getAllTasksByPurchaseOrderId(@Param("purchaseOrderId") Long purchaseOrderId, @Param("fromDate") Instant fromDate, @Param("toDate") Instant toDate, Pageable pageable);

	@Query("select task from Tasks task where task.packages.purchaseOrders.id =:purchaseOrderId And (:fromDate = NULL OR task.estimateEndTime > :fromDate) AND (:toDate = NULL OR task.estimateEndTime < :toDate)")
	List<Tasks> getAllTasksByPurchaseOrderId(@Param("purchaseOrderId") Long purchaseOrderId, @Param("fromDate") Instant fromDate, @Param("toDate") Instant toDate);

	@Query("select task from Tasks task where task.packages.purchaseOrders.project.id =:projectId And (:fromDate = NULL OR task.estimateEndTime > :fromDate) AND (:toDate = NULL OR task.estimateEndTime < :toDate)")
	Page<Tasks> getAllTasksByProjectId(@Param("projectId") Long projectId, @Param("fromDate") Instant fromDate, @Param("toDate") Instant toDate, Pageable pageable);

	@Query("select task from Tasks task where task.packages.purchaseOrders.project.id =:projectId And (:fromDate = NULL OR task.estimateEndTime > :fromDate) AND (:toDate = NULL OR task.estimateEndTime < :toDate)")
	List<Tasks> getAllTasksByProjectId(@Param("projectId") Long projectId, @Param("fromDate") Instant fromDate, @Param("toDate") Instant toDate);

	@Query("select task from Tasks task where task.packages.id =:packageId And task.id In (select Max(task.id) from Tasks task group by task.name)")
	List<Tasks> groupTasksByTaskNameAndPackageId(@Param("packageId") Long packageId);

	/**
	 * Count task by projectId
	 * @param projectId
	 * @return
	 */
	@Query("select count(task) from Tasks task where task.packages.purchaseOrders.project.id =:projectId")
	Integer getSizeTasksRelatingToProject(@Param("projectId") Long projectId);

	@Query("select task from Tasks task where task.packages.purchaseOrders.project.id =:projectId and task.estimateEndTime < CURRENT_TIMESTAMP"
			+ "  and task.status in :status")
	Page<Tasks> findLatedTasksRelatingProjectId(@Param("projectId") Long projectId, @Param("status") Collection<TaskStatus> status , Pageable pageable);

	/**
	 * Count the number task which is doing by projectId
	 * @author TuHP
	 *
	 */
	@Query("select count(task) from Tasks task where ((task.op =:userLogin and task.opStatus = 'DOING') "
			+ "or (task.review1 =:userLogin and task.review1Status = 'DOING') "
			+ "or (task.review2 =:userLogin and task.review2Status = 'DOING') "
			+ "or (task.fi =:userLogin and task.fiStatus = 'DOING')) "
			+ "and task.packages.purchaseOrders.project.id =:projectId")
	Integer countTasksIsDoingInProject(@Param("userLogin") String userLogin, @Param("projectId") Long projectId);

	// ngocvx1
	@Query("select task from Tasks task where ((task.op =:userLogin and task.opStatus in :statusOP)"
			+ " or (task.fixer =:userLogin and task.fixStatus in :statusFix)) and task.status in :taskStatus"
			+ " and (task.packages.id = :packages or (:packages is null and (task.packages.purchaseOrders.id = :purchaseOrders"
			+ " or (:purchaseOrders is null and task.packages.purchaseOrders.project.id =:projects))))"
			+ " and task.id NOT IN (select bidding.task.id from TaskBidding bidding where bidding.biddingStatus='HOLDING' or bidding.biddingStatus='DOING')"
			+ " order by field(opStatus, 'OPEN', 'PENDING', 'REOPEN', 'DOING') DESC")
	Page<Tasks> findTasksOfUserByRolesOPInProjectPaging(@Param("userLogin") String userLogin, @Param("projects") Long projects, @Param("purchaseOrders") Long purchaseOrders,
			@Param("packages") Long packages, @Param("statusOP") Collection<OPStatus> statusOP, @Param("statusFix") Collection<FixStatus> statusFix,
			@Param("taskStatus") Collection<TaskStatus> taskStatus, Pageable pageable);

	@Query("select task from Tasks task where task.op =:userLogin and task.status in :taskStatus and task.opStatus in :statusOP and task.packages.purchaseOrders.project.id =:projects")
    List<Tasks> findTasksOfUserByRolesOPInProject(@Param("userLogin") String userLogin, @Param("projects") Long projects, @Param("statusOP") Collection<OPStatus> statusOP, @Param("taskStatus") Collection<TaskStatus> taskStatus);

	@Query("select task from Tasks task where ((:step is null and ((task.review1 =:userLogin and task.review1Status in :statusReview)"
			+ " or (task.review2 =:userLogin and task.review2Status in :statusReview)))"
			+ " or ((:step = 'review1' and task.review1 =:userLogin and task.review1Status in :statusReview) or (:step = 'review2' and task.review2 =:userLogin and task.review2Status in :statusReview)))"
			+ " and (task.packages.id = :packages or (:packages is null and (task.packages.purchaseOrders.id = :purchaseOrders"
			+ " or (:purchaseOrders is null and task.packages.purchaseOrders.project.id =:projects))))"
			+ " and task.status in :taskStatus"
			+ " and task.id NOT IN (select bidding.task.id from TaskBidding bidding where bidding.biddingStatus='HOLDING' or bidding.biddingStatus='DOING')"
			+ " order by field(review1Status, 'OPEN', 'PENDING', 'REOPEN', 'DOING') DESC,"
			+ " field(review2Status, 'OPEN', 'PENDING', 'REOPEN', 'DOING') DESC")
	Page<Tasks> findTasksOfUserByRolesReviewerInProjectPaging(@Param("userLogin") String userLogin, @Param("projects") Long projects, @Param("purchaseOrders") Long purchaseOrders,
			@Param("packages") Long packages, @Param("step") String step, @Param("statusReview") Collection<ReviewStatus> statusReview, @Param("taskStatus") Collection<TaskStatus> taskStatus, Pageable pageable);

	@Query("select task from Tasks task where (task.review1 =:userLogin and task.status in :taskStatus and task.review1Status in :statusReview and task.packages.purchaseOrders.project.id =:projects)"
			+ " or (task.review2 =:userLogin and task.status in :taskStatus and task.review2Status in :statusReview and task.packages.purchaseOrders.project.id =:projects)")
    List<Tasks> findTasksOfUserByRolesReviewerInProject(@Param("userLogin") String userLogin, @Param("projects") Long projects, @Param("statusReview") Collection<ReviewStatus> statusReview, @Param("taskStatus") Collection<TaskStatus> taskStatus);

	@Query("select task from Tasks task where task.fi =:userLogin and task.status in :taskStatus and task.fiStatus in :statusFI"
			+ " and (task.packages.id = :packages or (:packages is null and (task.packages.purchaseOrders.id = :purchaseOrders"
			+ " or (:purchaseOrders is null and task.packages.purchaseOrders.project.id =:projects))))"
			+ " and task.id NOT IN (select bidding.task.id from TaskBidding bidding where bidding.biddingStatus='HOLDING' or bidding.biddingStatus='DOING')")
	Page<Tasks> findTasksOfUserByRolesFIInProjectPaging(@Param("userLogin") String userLogin, @Param("projects") Long projects, @Param("purchaseOrders") Long purchaseOrders,
			@Param("packages") Long packages, @Param("statusFI") Collection<FIStatus> statusFI, @Param("taskStatus") Collection<TaskStatus> taskStatus, Pageable pageable);

	@Query("select task from Tasks task where task.fi =:userLogin and task.status in :taskStatus and task.fiStatus in :statusFI and task.packages.purchaseOrders.project.id =:projects")
    List<Tasks> findTasksOfUserByRolesFIInProject(@Param("userLogin") String userLogin, @Param("projects") Long projects, @Param("statusFI") Collection<FIStatus> statusFI, @Param("taskStatus") Collection<TaskStatus> taskStatus);

	@Query("select task from Tasks task where task.op =:user and (task.opStatus='OPEN' or task.opStatus='DOING' or task.opStatus='PENDING' or task.opStatus='REOPEN')")
    List<Tasks> findTasksOfUserByRoleOP(@Param("user") String user);

    @Query("select task from Tasks task where (task.review1 =:user and (task.review1Status='OPEN' or task.review1Status='DOING')) or (task.review2 =:user and (task.review2Status='OPEN' or task.review2Status='DOING'))")
    List<Tasks> findTasksOfUserByRoleReview(@Param("user") String user);

    @Query("select task from Tasks task where task.fi =:user and (task.fiStatus='OPEN' or task.fiStatus='DOING')")
    List<Tasks> findTasksOfUserByRoleFI(@Param("user") String user);

    @Query("select task from Tasks task where task.fixer =:user and (task.fixStatus='OPEN' or task.fixStatus='DOING' or task.fixStatus='REOPEN')")
    List<Tasks> findTasksOfUserByRoleFixer(@Param("user") String user);

    @Query("select count(task) from Tasks task where (task.op =:userLogin and task.status in :taskStatus and task.opStatus = 'DOING') or "
    		+ "(task.review1 =:userLogin and task.status in :taskStatus and task.review1Status = 'DOING') or "
    		+ "(task.review2 =:userLogin and task.status in :taskStatus and task.review2Status = 'DOING') or "
    		+ "(task.fixer =:userLogin and task.status in :taskStatus and task.fixStatus = 'DOING') or "
    		+ "(task.fi =:userLogin and task.status in :taskStatus and task.fiStatus = 'DOING')")
	Integer countTasksDoingOfUserLogin(@Param("userLogin") String userLogin, @Param("taskStatus") Collection<TaskStatus> taskStatus);

    @Query("select count(task) from Tasks task where (task.op =:userLogin and task.opStatus = 'PENDING') or "
    		+ "(task.review1 =:userLogin and task.review1Status = 'PENDING') or "
    		+ "(task.review2 =:userLogin and task.review2Status = 'PENDING') or "
    		+ "(task.fi =:userLogin and task.fiStatus = 'PENDING')")
	Integer countTasksPendingOfUserLogin(@Param("userLogin") String userLogin);

    @Query("select task from Tasks task where task.op =:userLogin and task.opStatus =:statusOP and task.packages.purchaseOrders.project.id =:projects")
    List<Tasks> findTasksWithStatusOfUserByRolesOPInProject(@Param("userLogin") String userLogin, @Param("projects") Long projects, @Param("statusOP") OPStatus statusOP);

    @Query("select task from Tasks task where task.packages.purchaseOrders.project.id =:projectId and "
    		+ "((:hasOP = true and task.op = NULL) or (:hasReview1 = true and task.review1 = NULL) or "
    		+ "(:hasReview2 = true and task.review2 = NULL) or (:hasFI = true and task.fi = NULL))")
    Page<Tasks> findTasksUnAssignRelateToProjecId(@Param("projectId")Long id,@Param("hasOP") boolean hasOP, @Param("hasReview1")boolean hasReview1, @Param("hasReview2")boolean hasReview2,
			@Param("hasFI")boolean hasFI, Pageable pageable);

    @Query("select task from Tasks task where task.op =:member or task.review1 =:member or task.review2 =:member")
    List<Tasks> findTaskAssignByMember(@Param("member") String member);

    @Query("select task from Tasks task where task.packages.id =:packId and (task.op =:member or task.review1 =:member or task.review2 =:member) And (:fromDate = NULL OR task.estimateEndTime > :fromDate) AND (:toDate = NULL OR task.estimateEndTime < :toDate)")
    List<Tasks> getAllTaskAssignByPackageId(@Param("member") String member, @Param("packId") Long packId,  @Param("fromDate") Instant fromDate, @Param("toDate") Instant toDate);

    @Query("select task from Tasks task where task.packages.purchaseOrders.id =:purchaseOrderId and (task.op =:member or task.review1 =:member or task.review2 =:member) And (:fromDate = NULL OR task.estimateEndTime > :fromDate) And (:toDate = NULL OR task.estimateEndTime < :toDate)")
    List<Tasks> getAllTaskAssignByPO(@Param("member") String member, @Param("purchaseOrderId") Long purchaseOrderId, @Param("fromDate") Instant fromDate, @Param("toDate") Instant toDate);

    @Query("select task from Tasks task where task.packages.purchaseOrders.project.id =:projectId and (task.op =:member or task.review1 =:member or task.review2 =:member) And (:fromDate = NULL OR task.estimateEndTime > :fromDate) AND (:toDate = NULL OR task.estimateEndTime < :toDate)")
    List<Tasks> getAllTaskAssignByProject(@Param("member") String member, @Param("projectId") Long projectId, @Param("fromDate") Instant fromDate, @Param("toDate") Instant toDate);

    @Query("select count(bug) from Bugs bug where bug.tasks.id =:taskId and bug.stage =:rowRV")
	Integer countBugByTaskAndUserAssign(@Param("taskId") Long taskId, @Param("rowRV") String rowRV);

    @Query("select count(bug) from Bugs bug where bug.tasks.id =:taskId")
	Integer countBugByTaskId(@Param("taskId") Long taskId);

    @Query("select count(bug) from Bugs bug where bug.tasks.id =:taskId and bug.status = 'OPEN'")
	Integer countBugOpenByTaskId(@Param("taskId") Long taskId);

    @Query(value = "select count(task.id)"
    		+ " from packages as pack join tasks as task on pack.id = task.packages_id"
    		+ " where task.id in (select bug.tasks_id from bugs as bug) and pack.id =:packId", nativeQuery = true)
	Integer countTaskHasBug(@Param("packId") Long packId);


    /**
     * @param user
     * @param month
     * @return
     * @author CongHK
     */
    @Query(value = "select * from tasks task where (month(task.estimate_start_time) = :month"
    		+ " or month(task.estimate_end_time) = :month)"
    		+ " and ((task.op =:user and task.op_status <> 'NA')"
    		+ " or (task.review_1 =:user and task.review_1_status <> 'NA')"
    		+ " or (task.review_2 =:user and task.review_2_status <> 'NA')"
    		+ " or (task.fi =:user and fi_status <> 'NA'))"
    		+ " order by task.estimate_start_time, task.estimate_end_time", nativeQuery = true)
    List<Tasks> findTasksByUserLogin(@Param("user") String user, @Param("month") int month);


    /**
     * @param userLogin
     * @param dateFormat
     * @return
     * @author CongHK
     */
    @Query(value ="SELECT * FROM tasks task WHERE (DATE_FORMAT(task.estimate_start_time, \"%Y-%m-%d\") = :dateFormat"
    		+ " OR DATE_FORMAT(task.estimate_end_time, \"%Y-%m-%d\") = :dateFormat"
    		+ " OR :dateFormat BETWEEN DATE_FORMAT(task.estimate_start_time, \"%Y-%m-%d\") AND DATE_FORMAT(task.estimate_end_time, \"%Y-%m-%d\"))"
    		+ " AND ((task.op = :userLogin AND task.op_status <> 'NA')"
    		+ " OR (task.review_1 = :userLogin AND task.review_1_status <> 'NA')"
    		+ " OR (task.review_2 = :userLogin AND task.review_2_status <> 'NA')"
    		+ " OR (task.fi = :userLogin AND fi_status <> 'NA'))"
    		+ " ORDER BY task.estimate_start_time, task.estimate_end_time ", nativeQuery = true)
	List<Tasks> findTasksOfDay(@Param("userLogin") String userLogin, @Param("dateFormat") String dateFormat);

    @Query(value ="SELECT * FROM tasks t INNER JOIN packages p ON t.packages_id = p.id"
    		+ " INNER JOIN purchase_orders po ON p.purchase_orders_id = po.id AND po.project_id = :projectId"
    		+ " WHERE ((DATE_FORMAT(:currentDateFormat, \"%Y-%m-%d\") BETWEEN DATE_FORMAT(t.estimate_start_time, \"%Y-%m-%d\")"
    		+ " AND DATE_FORMAT(t.estimate_end_time, \"%Y-%m-%d\"))"
    		+ " OR (DATE_FORMAT(:currentDateFormat, \"%Y-%m-%d\") = DATE_FORMAT(t.estimate_start_time, \"%Y-%m-%d\"))"
    		+ " OR (DATE_FORMAT(:currentDateFormat, \"%Y-%m-%d\") = DATE_FORMAT(t.estimate_end_time, \"%Y-%m-%d\")))"
    		+ " AND ((:filter ='tasksDone' AND t.status = 'DONE')"
    		+ " OR (:filter ='tasksLate' AND :currentDateFormat > t.estimate_end_time)"
    		+ " OR (:filter ='tasksInprogress' AND t.status <> 'DONE')"
    		+ " OR (:filter ='all')) ORDER BY t.estimate_start_time, t.estimate_end_time", nativeQuery=true)
    List<Tasks> findTasksByFilter(@Param("projectId") Long projectId, @Param("filter") String filter, @Param("currentDateFormat") String currentDateFormat);

    @Query("select task from Tasks task where task.op =:userLogin and task.opStatus in :OPStatuses")
	List<Tasks> findTaskRolesOPInListStatusOfUser(@Param("userLogin") String userLogin, @Param("OPStatuses") Collection<OPStatus> OPStatuses);

    @Query("select task from Tasks task where task.review1 =:userLogin and task.review1Status in :RV1Statuses")
    List<Tasks> findTaskRolesRV1InListStatusOfUser(@Param("userLogin") String userLogin, @Param("RV1Statuses") Collection<ReviewStatus> listStatus);

    @Query("select task from Tasks task where task.review2 =:userLogin and task.review2Status in :RV2Statuses")
    List<Tasks> findTaskRolesRV2InListStatusOfUser(@Param("userLogin") String userLogin, @Param("RV2Statuses") Collection<ReviewStatus> listStatus);

    @Query("select task from Tasks task where task.fixer =:userLogin and task.fixStatus in :FixStatuses")
    List<Tasks> findTaskRolesFixerInListStatusOfUser(@Param("userLogin") String userLogin, @Param("FixStatuses") Collection<FixStatus> listStatus);

    @Query("select task from Tasks task where task.fi =:userLogin and task.fiStatus in :FiStatuses")
    List<Tasks> findTaskRolesFIInListStatusOfUser(@Param("userLogin") String userLogin, @Param("FiStatuses") Collection<FIStatus> listStatus);

	/** This method will get a Task base on below conditions
	 * @param id
	 * @param taskName
	 * @param taskEstEndedTime
	 * @return
	 * @author TuHP
	 */
	Tasks findByPackagesIdAndNameAndEstimateEndTime(Long id, String taskName, Instant taskEstEndedTime);

	/**
	 * This method get Task UnAssigne for Task Bidding By Project
	 * @param projectId
	 * @param pageable
	 * @return
	 * @author KimHQ
	 */
	@Query("select task from Tasks task where task.packages.purchaseOrders.project.id =:projectId "
			+"and task.id NOT IN (SELECT taskBidding.task.id FROM TaskBidding taskBidding) "
			+"and (('op' in :roundList and (task.op is NULL or task.op = '')) "
			+"or ('review1' in :roundList and (task.review1 is NULL or task.review1 = '')) "
			+"or ('review2' in :roundList and (task.review2 is NULL or task.review2 = '')) "
			+"or ('fi' in :roundList and (task.fi is NULL or task.fi = '')) "
			+"or ('fixer' in :roundList and (task.fixer is NULL or task.fixer = ''))) "
			+"and (task.status <> 'CANCEL'and task.status <> 'DONE') "
			+"and (task.packages.id = :packageId "
			+"or (:packageId is null and (task.packages.purchaseOrders.id = :purchaseOrderId "
			+"or (:purchaseOrderId is null and task.packages.purchaseOrders.project.id = :projectId)))) "
			+"ORDER BY field(status,'DOING','PENDING','OPEN','CLOSED','NA') ASC")
	Page<Tasks> findAllTaskUnAssign(@Param("roundList") List<String> roundList, @Param("projectId") Long projectId, @Param("purchaseOrderId") Long purchaseOrderId, @Param("packageId") Long packageId,
			Pageable pageable);

	/**
	 * Get all task by the list of conditions
	 * @param packageId
	 * @param name
	 * @param listStatuses
	 * @param assignee
	 * @param description
	 * @param fromDate
	 * @param toDate
	 * @return
	 * @author TuHP
	 */
	@Query("select task from Tasks task where task.packages.id =:packageId And (:name = '' Or task.name like CONCAT(:name,'%')) And task.status in :statuses "
			+"And (:assignee = '' Or (task.op like CONCAT(:assignee,'%') Or task.review1 like CONCAT(:assignee,'%') Or task.review2 like CONCAT(:assignee,'%') Or task.fi like CONCAT(:assignee,'%') Or task.fixer like CONCAT(:assignee,'%'))) "
			+ "And (:description = '' Or task.description like CONCAT(:description,'%')) And (:fromDate = NULL OR task.estimateEndTime > :fromDate) AND (:toDate = NULL OR task.estimateEndTime < :toDate)")
	List<Tasks> getAllTasksByPackageIdAndNameAndStatusAndFromDateAndToDateAndAssigneeAnDescription(@Param("packageId") Long packageId, @Param("name") String name,
			@Param("statuses") Collection<TaskStatus> statuses, @Param("assignee") String assignee, @Param("description") String description,
			@Param("fromDate") Instant fromDate, @Param("toDate") Instant toDate);

	@Query(value="SELECT COUNT(t.id) FROM tasks t where t.status IN :taskStatus "
			+ "and ((t.op = :userLogin and t.op_status IN :roundStatus) "
			+ "or (t.review_1 = :userLogin and t.review_1_status IN :roundStatus) "
			+ "or (t.review_2 = :userLogin and t.review_2_status IN :roundStatus) "
			+ "or (t.fi = :userLogin and t.fi_status IN :roundStatus)) "
			+ "and DATE_FORMAT(t.estimate_end_time, \"%Y-%m-%d\") <= DATE_FORMAT(curdate(), \"%Y-%m-%d\")", nativeQuery=true)
	Integer countTaskLateInTheEndDate(@Param("userLogin") String userLogin, @Param("taskStatus") List<String> taskStatus, @Param("roundStatus") List<String> roundStatus);

	/**
	 * Get all task by the list of conditions
	 * @author PhuVD3
	 */
	@Query("select task from Tasks task where task.packages.id =:packageId and task.status = 'OPEN'")
	List<Tasks> getAllTasksByClone(@Param("packageId") Long packageId);

	/**
	 * Get all task by the list of conditions
	 * @author PhuVD3
	 */
	@Query("select task from Tasks task where task.packages.id =:packageId")
	List<Tasks> getAllTasksByClonePa(@Param("packageId") Long packageId);

	@Query(value="select count(t.id) from tasks t"
			+ " where t.packages_id =:packagesId"
			+ " and t.id in (select bug.tasks_id from bugs bug)"
			+ " and ((:step = 'review1' and t.review_1_status in :listStatus)"
			+ " or (:step = 'review2' and t.review_2_status in :listStatus)"
			+ " or (:step = 'fi' and t.fi_status in :listStatus))", nativeQuery=true)
	int countTaskHasBugInPackagesByRound(@Param("packagesId") Long packagesId, @Param("step") String step, @Param("listStatus") String[] listStatus);
	
	@Query(value="select count(t.id) from tasks t where t.packages_id =:packagesId", nativeQuery=true)
	int countTasksInPackages(@Param("packagesId") Long packagesId);
	
	@Query("select task from Tasks task where ((task.op =:userLogin and task.opStatus in :statusOP)"
	+ " or (task.fixer = :userLogin and task.fixStatus in :statusFix))"
	+ "	and task.status in :taskStatus and task.packages.purchaseOrders.project.id =:projects"
	+ " and ((:beginTime is not null and :beginTime"
	+ " between task.estimateStartTime and task.estimateEndTime)"
	+ " or (:endTime is not null and :endTime"
	+ " between task.estimateStartTime and task.estimateEndTime)"
	+ " or (:beginTime is not null and :endTime is not null"
	+ " and (task.estimateStartTime between :beginTime and :endTime)"
	+ " and task.estimateEndTime between :beginTime and :endTime) or (:beginTime is null and :endTime is null))"
	+ " and task.id NOT IN (select bidding.task.id from TaskBidding bidding where bidding.biddingStatus='HOLDING' or bidding.biddingStatus='DOING')"
	+ " order by field(opStatus, 'REOPEN', 'OPEN', 'PENDING', 'DOING') DESC")
	Page<Tasks> findTasksDoneOfUserByRolesOPInProjectPaging(@Param("userLogin") String userLogin, @Param("projects") Long projects,
	@Param("statusOP") Collection<OPStatus> statusOP, @Param("statusFix") Collection<FixStatus> statusFix,
	@Param("beginTime") Instant beginTime, @Param("endTime") Instant endTime,
	@Param("taskStatus") Collection<TaskStatus> taskStatus, Pageable pageable);
	
	@Query("select task from Tasks task where (task.review1 =:userLogin and task.status in :taskStatus and task.review1Status in :statusReview and task.packages.purchaseOrders.project.id =:projects)"
			+ " or (task.review2 =:userLogin and task.status in :taskStatus and task.review2Status in :statusReview and task.packages.purchaseOrders.project.id =:projects)"
			+ " and ((:beginTime is not null and :beginTime"
			+ " between task.estimateStartTime and task.estimateEndTime)"
			+ " or (:endTime is not null and :endTime"
			+ " between task.estimateStartTime and task.estimateEndTime)"
			+ " or (:beginTime is not null and :endTime is not null"
			+ " and (task.estimateStartTime between :beginTime and :endTime)"
			+ " and task.estimateEndTime between :beginTime and :endTime) or (:beginTime is null and :endTime is null))"
			+ " and task.id NOT IN (select bidding.task.id from TaskBidding bidding where bidding.biddingStatus='HOLDING' or bidding.biddingStatus='DOING')"
			+ " order by field(review1Status, 'OPEN', 'PENDING', 'REOPEN', 'DOING') DESC,"
			+ " field(review2Status, 'OPEN', 'PENDING', 'REOPEN', 'DOING') DESC")
	Page<Tasks> findTasksDoneOfUserByRolesReviewerInProjectPaging(@Param("userLogin") String userLogin, @Param("projects") Long projects,
			@Param("statusReview") Collection<ReviewStatus> statusReview, @Param("beginTime") Instant beginTime, @Param("endTime") Instant endTime,
			@Param("taskStatus") Collection<TaskStatus> taskStatus, Pageable pageable);
	
	@Query("select task from Tasks task where task.fi =:userLogin and task.status in :taskStatus and task.fiStatus in :statusFI and task.packages.purchaseOrders.project.id =:projects"
			+ " and ((:beginTime is not null and :beginTime"
			+ " between task.estimateStartTime and task.estimateEndTime)"
			+ " or (:endTime is not null and :endTime"
			+ " between task.estimateStartTime and task.estimateEndTime)"
			+ " or (:beginTime is not null and :endTime is not null"
			+ " and (task.estimateStartTime between :beginTime and :endTime)"
			+ " and task.estimateEndTime between :beginTime and :endTime) or (:beginTime is null and :endTime is null))"
			+ " and task.id NOT IN (select bidding.task.id from TaskBidding bidding where bidding.biddingStatus='HOLDING' or bidding.biddingStatus='DOING')"
			+ " order by field(fiStatus, 'OPEN', 'PENDING', 'DOING') DESC")
	Page<Tasks> findTasksDoneOfUserByRolesFIInProjectPaging(@Param("userLogin") String userLogin, @Param("projects") Long projects,
			@Param("statusFI") Collection<FIStatus> statusFI, @Param("beginTime") Instant beginTime, @Param("endTime") Instant endTime,
			@Param("taskStatus") Collection<TaskStatus> taskStatus, Pageable pageable);

	@Query("select task from Tasks task where ((task.op =:userLogin and task.opStatus in :statusOP)"
	+ " or (task.fixer = :userLogin and task.fixStatus in :statusFix))"
	+ "	and task.status in :taskStatus and task.packages.purchaseOrders.project.id =:projects"
	+ " and ((:beginTime is not null and :beginTime"
	+ " between task.estimateStartTime and task.estimateEndTime)"
	+ " or (:endTime is not null and :endTime"
	+ " between task.estimateStartTime and task.estimateEndTime)"
	+ " or (:beginTime is not null and :endTime is not null"
	+ " and (task.estimateStartTime between :beginTime and :endTime)"
	+ " and task.estimateEndTime between :beginTime and :endTime) or (:beginTime is null and :endTime is null))"
	+ " and task.id NOT IN (select bidding.task.id from TaskBidding bidding where bidding.biddingStatus='HOLDING' or bidding.biddingStatus='DOING')"
	+ " order by field(opStatus, 'REOPEN', 'OPEN', 'PENDING', 'DOING') DESC")
	List<Tasks> findTasksDoneOfUserByRolesOPInProject(@Param("userLogin") String userLogin, @Param("projects") Long projects,
	@Param("statusOP") Collection<OPStatus> statusOP, @Param("statusFix") Collection<FixStatus> statusFix,
	@Param("beginTime") Instant beginTime, @Param("endTime") Instant endTime,
	@Param("taskStatus") Collection<TaskStatus> taskStatus);
	
	@Query("select task from Tasks task where (task.review1 =:userLogin and task.status in :taskStatus and task.review1Status in :statusReview and task.packages.purchaseOrders.project.id =:projects)"
			+ " or (task.review2 =:userLogin and task.status in :taskStatus and task.review2Status in :statusReview and task.packages.purchaseOrders.project.id =:projects)"
			+ " and ((:beginTime is not null and :beginTime"
			+ " between task.estimateStartTime and task.estimateEndTime)"
			+ " or (:endTime is not null and :endTime"
			+ " between task.estimateStartTime and task.estimateEndTime)"
			+ " or (:beginTime is not null and :endTime is not null"
			+ " and (task.estimateStartTime between :beginTime and :endTime)"
			+ " and task.estimateEndTime between :beginTime and :endTime) or (:beginTime is null and :endTime is null))"
			+ " and task.id NOT IN (select bidding.task.id from TaskBidding bidding where bidding.biddingStatus='HOLDING' or bidding.biddingStatus='DOING')"
			+ " order by field(review1Status, 'OPEN', 'PENDING', 'REOPEN', 'DOING') DESC,"
			+ " field(review2Status, 'OPEN', 'PENDING', 'REOPEN', 'DOING') DESC")
	List<Tasks> findTasksDoneOfUserByRolesReviewerInProject(@Param("userLogin") String userLogin, @Param("projects") Long projects,
			@Param("statusReview") Collection<ReviewStatus> statusReview, @Param("beginTime") Instant beginTime, @Param("endTime") Instant endTime,
			@Param("taskStatus") Collection<TaskStatus> taskStatus);
	
	@Query("select task from Tasks task where task.fi =:userLogin and task.status in :taskStatus and task.fiStatus in :statusFI and task.packages.purchaseOrders.project.id =:projects"
			+ " and ((:beginTime is not null and :beginTime"
			+ " between task.estimateStartTime and task.estimateEndTime)"
			+ " or (:endTime is not null and :endTime"
			+ " between task.estimateStartTime and task.estimateEndTime)"
			+ " or (:beginTime is not null and :endTime is not null"
			+ " and (task.estimateStartTime between :beginTime and :endTime)"
			+ " and task.estimateEndTime between :beginTime and :endTime) or (:beginTime is null and :endTime is null))"
			+ " and task.id NOT IN (select bidding.task.id from TaskBidding bidding where bidding.biddingStatus='HOLDING' or bidding.biddingStatus='DOING')"
			+ " order by field(fiStatus, 'OPEN', 'PENDING', 'DOING') DESC")
	List<Tasks> findTasksDoneOfUserByRolesFIInProject(@Param("userLogin") String userLogin, @Param("projects") Long projects,
			@Param("statusFI") Collection<FIStatus> statusFI, @Param("beginTime") Instant beginTime, @Param("endTime") Instant endTime,
			@Param("taskStatus") Collection<TaskStatus> taskStatus);
}
