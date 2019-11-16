package fpt.dps.dtms.repository;

import fpt.dps.dtms.domain.TaskTrackingTime;
import fpt.dps.dtms.domain.Tasks;
import fpt.dps.dtms.domain.ProjectUsers;

import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Map;


import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;


/**
 * Spring Data JPA repository for the TaskTrackingTime entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TaskTrackingTimeRepository extends JpaRepository<TaskTrackingTime, Long>, JpaSpecificationExecutor<TaskTrackingTime> {

	//@Query("select t from TaskTrackingTime t where t.taskId = :taskId and t.userLogin = :userLogin and t.endTime = NULL order by t.id desc")
	//TaskTrackingTime findTaskIdAndUserLogin(@Param("taskId")Long id,@Param("userLogin") String userLogin);
	TaskTrackingTime findTop1ByTaskIdAndUserLoginAndEndTimeOrderByIdDesc(Long taskId, String userLogin, Instant endTime);
	
	@Query(value="select * from task_tracking_time as track where "
			+ "(track.task_id =:taskId) and "
			+ "track.id = ( select max(task_tracking_time.id) "
			+ "from task_tracking_time)", nativeQuery = true)
	TaskTrackingTime findTaskTrackingByRole(@Param("taskId")Long taskId);
	
	/**
	 * get effort user by project id
	 * @param projectId
	 * @param userLogin
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	@Query("SELECT SUM(taskTT.duration) FROM TaskTrackingTime taskTT WHERE taskTT.taskId IN (SELECT task.id FROM Tasks task WHERE task.packages.purchaseOrders.project.id =:projectId)"
			+ "AND (taskTT.userLogin =:userLogin)"
			+ "AND (taskTT.startTime BETWEEN :startDate AND :endDate)")
	Integer sumActualEffortOfUser(@Param("projectId")Long projectId, @Param("userLogin")String userLogin, @Param("startDate") Instant startDate, @Param("endDate") Instant endDate);
	
	/**
	 * get effort user by project id and role user
	 * @param projectId
	 * @param userLogin
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	@Query("SELECT SUM(taskTT.duration) FROM TaskTrackingTime taskTT WHERE taskTT.taskId IN (SELECT task.id FROM Tasks task WHERE task.packages.purchaseOrders.project.id =:projectId)"
			+ "AND (taskTT.userLogin =:userLogin)"
			+ "AND (taskTT.startTime BETWEEN :startDate AND :endDate)"
			+ "AND (taskTT.role =:role)")
	Integer sumActualEffortOfUserByProjectIdAndRole(@Param("projectId")Long projectId, @Param("userLogin")String userLogin, @Param("role") String role, @Param("startDate") Instant startDate, @Param("endDate") Instant endDate);
	
	/**
	 * get effort user by purchaseOrder id and role
	 * @param purId
	 * @param userLogin
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	@Query("SELECT SUM(taskTT.duration) FROM TaskTrackingTime taskTT WHERE taskTT.taskId IN (SELECT task.id FROM Tasks task WHERE task.packages.purchaseOrders.id =:purId)"
			+ "AND (taskTT.userLogin =:userLogin)"
			+ "AND (taskTT.startTime BETWEEN :startDate AND :endDate)"
			+ "AND (taskTT.role =:role)")
	Integer sumActualEffortOfUserForPurchase(@Param("purId")Long purId, @Param("userLogin")String userLogin, @Param("role") String role, @Param("startDate") Instant startDate, @Param("endDate") Instant endDate);
	
	/**
	 * get effort user by package id and role
	 * @param projectId
	 * @param userLogin
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	@Query("SELECT SUM(taskTT.duration) FROM TaskTrackingTime taskTT WHERE taskTT.taskId IN (SELECT task.id FROM Tasks task WHERE task.packages.id =:packId)"
			+ "AND (taskTT.userLogin =:userLogin)"
			+ "AND (taskTT.startTime BETWEEN :startDate AND :endDate)"
			+ "AND (taskTT.role =:role)")
	Integer sumActualEffortOfUserAndRoleForPackage(@Param("packId")Long packId, @Param("userLogin")String userLogin, @Param("role") String role, @Param("startDate") Instant startDate, @Param("endDate") Instant endDate);
	
	/**
	 * get effort user by package id
	 * @param projectId
	 * @param userLogin
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	@Query("SELECT SUM(taskTT.duration) FROM TaskTrackingTime taskTT WHERE taskTT.taskId IN (SELECT task.id FROM Tasks task WHERE task.packages.id =:packId)"
			+ "AND (taskTT.userLogin =:userLogin)"
			+ "AND (taskTT.startTime BETWEEN :startDate AND :endDate)")
	Integer sumActualEffortOfUserForPackage(@Param("packId")Long packId, @Param("userLogin")String userLogin, @Param("startDate") Instant startDate, @Param("endDate") Instant endDate);
	
	//get all role user by packageId
    @Query("select distinct task.op from Tasks task where task.packages.id =:packageId")
	List<String> getAllUserWithRoleOpByPackageId(@Param("packageId") Long packageId);
    
    @Query("select distinct task.review1 from Tasks task where task.packages.id =:packageId")
	List<String> getAllUserWithRoleReview1ByPackageId(@Param("packageId") Long packageId);
    
    @Query("select distinct task.review2 from Tasks task where task.packages.id =:packageId")
	List<String> getAllUserWithRoleReview2ByPackageId(@Param("packageId") Long packageId);
    
    @Query("select distinct task.fixer from Tasks task where task.packages.id =:packageId")
	List<String> getAllUserWithRoleFixerByPackageId(@Param("packageId") Long packageId);
    
    @Query("select distinct task.fi from Tasks task where task.packages.id =:packageId")
	List<String> getAllUserWithRoleFiByPackageId(@Param("packageId") Long packageId);
    
    //get all role user by purchaseOrderId
    @Query("select distinct task.op from Tasks task where task.packages.purchaseOrders.id =:purchaseOrderId")
	List<String> getAllUserWithRoleOpByPurchaseOrderId(@Param("purchaseOrderId") Long purchaseOrderId);
    
    @Query("select distinct task.review1 from Tasks task where task.packages.purchaseOrders.id =:purchaseOrderId")
	List<String> getAllUserWithRoleReview1ByPurchaseOrderId(@Param("purchaseOrderId") Long purchaseOrderId);
    
    @Query("select distinct task.review2 from Tasks task where task.packages.purchaseOrders.id =:purchaseOrderId")
	List<String> getAllUserWithRoleReview2ByPurchaseOrderId(@Param("purchaseOrderId") Long purchaseOrderId);
    
    @Query("select distinct task.fixer from Tasks task where task.packages.purchaseOrders.id =:purchaseOrderId")
	List<String> getAllUserWithRoleFixerByPurchaseOrderId(@Param("purchaseOrderId") Long purchaseOrderId);
    
    @Query("select distinct task.fi from Tasks task where task.packages.purchaseOrders.id =:purchaseOrderId")
	List<String> getAllUserWithRoleFiByPurchaseOrderId(@Param("purchaseOrderId") Long purchaseOrderId);
    
    //get all role user by projectId
    @Query("select distinct task.op from Tasks task where task.packages.purchaseOrders.project.id =:projectId"
    		+ " AND (task.op is not null)")
	List<String> getAllUserWithRoleOpByProjectId(@Param("projectId") Long projectId);
    
    @Query("select distinct task.review1 from Tasks task where task.packages.purchaseOrders.project.id =:projectId"
    		+ " AND (task.review1 is not null)")
	List<String> getAllUserWithRoleReview1ByProjectId(@Param("projectId") Long projectId);
    
    @Query("select distinct task.review2 from Tasks task where task.packages.purchaseOrders.project.id =:projectId"
    		+ " AND (task.review2 is not null)")
	List<String> getAllUserWithRoleReview2ByProjectId(@Param("projectId") Long projectId);
    
    @Query("select distinct task.fixer from Tasks task where task.packages.purchaseOrders.project.id =:projectId"
    		+ " AND (task.fixer is not null)")
	List<String> getAllUserWithRoleFixerByProjectId(@Param("projectId") Long projectId);
    
    @Query("select distinct task.fi from Tasks task where task.packages.purchaseOrders.project.id =:projectId"
    		+ " AND (task.fi is not null)")
	List<String> getAllUserWithRoleFiByProjectId(@Param("projectId") Long projectId);
    
    @Query("select sum(pu.effortPlan) from ProjectUsers pu where pu.userLogin in :listUser")
	Float getSumEffortUser(@Param("listUser") Collection<String> listUser);
    
    @Query("select pu from ProjectUsers pu where pu.project.id =:projectId"
    		+ " AND (pu.roleName <> 'PM')"
    		+ " AND (pu.roleName <> 'TEAMLEAD')")
    List<ProjectUsers> getProjectUserByProjectId(@Param("projectId") Long projectId);
    
    /**
     * Get all TasktrackingTimes by Task id
     * @param taskId
     * @return
     * @author TuHP
     */
    List<TaskTrackingTime> findAllByTaskId(Long taskId);
    
    /**
     * Get all TasktrackingTimes by Task id and role
     * @param taskId
     * @param role
     * @return
     * @author HoiHT1
     */
    List<TaskTrackingTime> findAllByTaskIdAndRole(Long taskId, String role);

    List<TaskTrackingTime> findAllByTaskIdAndRoleAndEndStatus(Long taskId, String role, String status);

    /**
     * Count all row by round
     * @param taskId
     * @param role
     * @param status
     * @return
     */
    Integer countAllByTaskIdAndRoleAndEndStatus(Long taskId, String role, String status);

    @Query(value="select exists(select * from task_tracking_time as taskTracking where taskTracking.task_id =:taskId)", nativeQuery = true)
    Integer checkTaskIdExist(@Param("taskId") Long taskId);

    @Query("select track from TaskTrackingTime track where track.id = (select max(test.id) from TaskTrackingTime test where test.taskId =:taskId and test.userLogin <>:userLogin)")
    TaskTrackingTime findTaskTrackingBeforeByTaskIdAndUserLogin(@Param("taskId")Long taskId, @Param("userLogin")String userLogin);

    @Query("select distinct taskTracking.userLogin from TaskTrackingTime taskTracking where taskTracking.taskId =:taskId and taskTracking.role =:role")
    List<String> getListUser(@Param("taskId") Long taskId,@Param("role") String role);
    
    @Query(value="SELECT jhi_role FROM task_tracking_time where task_id = :taskId and jhi_role <> :currentRound order by id desc limit 1", nativeQuery=true)
	String getPreviousRound(@Param("taskId") Long taskId, @Param("currentRound") String currentRound);
}
