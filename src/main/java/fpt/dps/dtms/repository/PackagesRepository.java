package fpt.dps.dtms.repository;

import fpt.dps.dtms.domain.Packages;
import fpt.dps.dtms.domain.Tasks;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import fpt.dps.dtms.domain.enumeration.OPStatus;
import fpt.dps.dtms.domain.enumeration.ReviewStatus;
import fpt.dps.dtms.domain.enumeration.FIStatus;
import fpt.dps.dtms.domain.enumeration.TaskStatus;

import org.springframework.data.jpa.repository.*;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Set;


/**
 * Spring Data JPA repository for the Packages entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PackagesRepository extends JpaRepository<Packages, Long>, JpaSpecificationExecutor<Packages> {
	@Query("select pack from Packages pack where pack.purchaseOrders.id =:purchaseOrderId")
	Page<Packages> findAllPackagesPurchaseOrderId(@Param("purchaseOrderId") Long purchaseOrderId, Pageable pageable);
	
	@Query("select pack from Packages pack where pack.purchaseOrders.id =:purchaseOrderId")
	List<Packages> findAllPackagesByPurchaseOrderId(@Param("purchaseOrderId") Long purchaseOrderId);
	
	/*
	 * Count tasks by Packages id
	 * get all tasks by package, SUM Task
	 */
	@Query("select count(task.id) from Tasks task where task.packages.id =:packageId and task.status <> 'CANCEL'")
	Long findAllTaskByPackagesId(@Param("packageId") Long packageId);
	
	@Query("select task from Tasks task where task.packages.id =:packageId and task.opStatus in :status")
	List<Tasks> findAllOpStatusByPackagesId(@Param("packageId") Long packageId, @Param("status") Collection<OPStatus> status);
	
	@Query("select task from Tasks task where task.packages.id =:packageId and (:review1Status = NULL or task.review1Status in :review1Status) "
			+ "and (:review2Status = NULL or task.review2Status in :review2Status)")
	List<Tasks> findAllReviewerStatusByPackagesId(@Param("packageId") Long packageId, @Param("review1Status") Collection<ReviewStatus> review1Status, 
			@Param("review2Status") Collection<ReviewStatus> review2Status);
	
	@Query("select task from Tasks task where task.packages.id =:packageId and task.fiStatus in :status")
	List<Tasks> findAllFiStatusByPackagesId(@Param("packageId") Long packageId, @Param("status") Collection<FIStatus> fiStatus);
	
	@Query("select task from Tasks task where task.packages.id =:packageId and task.status in :status")
	List<Tasks> findAllTaskStatusByPackagesId(@Param("packageId") Long packageId, @Param("status") Collection<TaskStatus> taskStatus);
	
	@Query("select package from Packages package where package.name = :name and package.purchaseOrders.id = :poID")
	Packages findPackageByNameAndPurchaseOrderId(@Param("name") String name, @Param("poID") Long poID);
	
	/**3
	 * Get all package which is late
	 * */
	@Query("select package from Packages package where package.purchaseOrders.project.id = :projectId "
			+ "and package.endTime < CURRENT_TIMESTAMP "
			+ "and (select count(task) from Tasks task where task.packages.id = package.id and task.status not in :taskStatus) > 0")
	Page<Packages> findLatedPackagesRelatingProjectId(@Param("projectId") Long projectId, @Param("taskStatus") Collection<TaskStatus> taskStatus, Pageable pageable);
	
	@Query("select count(p) from Packages p where p.purchaseOrders.project.id = :projectId")
	Integer getSizePackageRelatingToProject(@Param("projectId") Long projectId);
	
	// search package by Name
	@Query("select pack from Packages pack where pack.name LIKE CONCAT('%',:packName,'%')")
	Page<Packages> searchPackagesByName(@Param("packName") String packName, Pageable pageable);
	
	@Query("select pack from Packages pack where pack.id in (select task.packages.id from Tasks task where task.packages.purchaseOrders.project.id =:projectId and (task.op =:member or task.review1 =:member or task.review2 =:member or task.fixer =:member or task.fi =:member))")
	Page<Packages> getAllPackage(@Param("member") String member, @Param("projectId") Long projectId, Pageable pageable);
	
	/**
	 * Count task by packageId
	 * @param packageId
	 * @return
	 */
	@Query("select count(task) from Tasks task where task.packages.id =:packageId and (task.op =:member or task.review1 =:member or task.review2 =:member or task.fixer =:member or task.fi =:member)")
	Integer getSizeTasksAssignToPackage(@Param("packageId") Long packageId, @Param("member") String member);

	@Query("select pack from Packages pack where pack.purchaseOrders.id =:purchaseOrderId")
	List<Packages> findListPackageBiddingTask(@Param("purchaseOrderId") Long purchaseOrderId);
}
