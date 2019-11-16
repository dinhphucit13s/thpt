package fpt.dps.dtms.repository;

import fpt.dps.dtms.domain.TaskBidding;
import fpt.dps.dtms.domain.Tasks;
import fpt.dps.dtms.domain.enumeration.BiddingScope;
import fpt.dps.dtms.domain.enumeration.BiddingStatus;
import fpt.dps.dtms.domain.enumeration.FIStatus;
import fpt.dps.dtms.domain.enumeration.FixStatus;
import fpt.dps.dtms.domain.enumeration.OPStatus;
import fpt.dps.dtms.domain.enumeration.ReviewStatus;

import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;


/**
 * Spring Data JPA repository for the TaskBidding entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TaskBiddingRepository extends JpaRepository<TaskBidding, Long>, JpaSpecificationExecutor<TaskBidding> {

	@Query("select taskBidding from TaskBidding taskBidding where taskBidding.biddingStatus in :biddingStatus "
			+ "and ((taskBidding.task.op is NULL or taskBidding.task.op = '') "
			+ "or (taskBidding.task.review1 is NULL or taskBidding.task.review1 = '') "
			+ "or (taskBidding.task.review2 is NULL or taskBidding.task.review2 = '') "
			+ "or (taskBidding.task.fi is NULL or taskBidding.task.fi = '')) "
			+ "and ((taskBidding.biddingScope = 'PROJECT' and :modeBidding = 'project') "
			+ "or (taskBidding.biddingScope = 'BU' and :modeBidding = 'businessUnit') "
			+ "or (taskBidding.biddingScope = 'PUBLIC' and :modeBidding = 'publicAll')) "
			+ "and (taskBidding.task.packages.id = :packageId "
			+ "or (:packageId is null and (taskBidding.task.packages.purchaseOrders.id = :poId "
			+ "or (:poId is null and taskBidding.task.packages.purchaseOrders.project.id = :projectId "
			+ "and :userLogin is not null))))")
	Page<TaskBidding> findAllTasksBiddingByMode(@Param("projectId") Long projectId, @Param("poId") Long poId,
			@Param("packageId") Long packageId, @Param("modeBidding") String modeBidding, @Param("biddingStatus") List<BiddingStatus> biddingStatus,
			@Param("userLogin") String userLogin, Pageable pageable);

	@Query(value="SELECT * FROM task_bidding WHERE bidding_status='HOLDING' and last_modified_by = :userLogin LIMIT 1", nativeQuery=true)
	TaskBidding findHoldingTaskByUser(@Param("userLogin") String userLogin);

	@Query("select taskBidding from TaskBidding taskBidding where taskBidding.task.packages.purchaseOrders.project.id =:projectId "
			+ "and taskBidding.biddingScope = :biddingScope "
			+ "and taskBidding.biddingRound is not null "
			+ "and (taskBidding.biddingStatus = 'NA' and taskBidding.task.status <> 'CANCEL' and taskBidding.task.status <> 'PENDING') "
			+ "and ((taskBidding.biddingRound <> 'fixer' and ((taskBidding.task.op <> :currentUserLogin or taskBidding.task.op is null) "
			+ "and (taskBidding.task.review1 <> :currentUserLogin or taskBidding.task.review1 is null) "
			+ "and (taskBidding.task.review2 <> :currentUserLogin or taskBidding.task.review2 is null) "
			+ "and (taskBidding.task.fixer <> :currentUserLogin or taskBidding.task.fixer is null) "
			+ "and (taskBidding.task.fi <> :currentUserLogin or taskBidding.task.fi is null))) "
			+ "or (taskBidding.biddingRound = 'fixer' and ((taskBidding.task.op <> :currentUserLogin or taskBidding.task.op is null) "
			+ "and (taskBidding.task.review1 <> :currentUserLogin or taskBidding.task.review1 is null) "
			+ "and (taskBidding.task.review2 <> :currentUserLogin or taskBidding.task.review2 is null) "
			+ "and (taskBidding.task.fi <> :currentUserLogin or taskBidding.task.fi is null)))) "
			+ "and (taskBidding.biddingRound = :step or ((:step is null or :step = '') and (taskBidding.task.packages.id = :packageId "
			+ "or (:packageId is null and (taskBidding.task.packages.purchaseOrders.id = :purchaseOrderId "
			+ "or (:purchaseOrderId is null and (taskBidding.task.packages.purchaseOrders.project.id = :projectId))))))) ")
	Page<TaskBidding> findAllTasksBiddingByBiddingScopes(@Param("biddingScope") BiddingScope biddingScope, @Param("projectId") Long projectId,
			@Param("purchaseOrderId") Long purchaseOrderId, @Param("packageId") Long packageId,
			Pageable pageable, @Param("step") String step, @Param("currentUserLogin") String currentUserLogin);
	
	@Query("select bidding from TaskBidding bidding where bidding.biddingStatus = 'DOING' "
			+ "and ((bidding.task.op =:userLogin and bidding.task.opStatus in :statusOP) "
			+ "or (bidding.task.fixer =:userLogin and bidding.task.fixStatus in :statusFix))")
    List<TaskBidding> getBiddingTaskOPAndFixByUser(@Param("userLogin") String userLogin, @Param("statusOP") Collection<OPStatus> statusOP,
    		@Param("statusFix") Collection<FixStatus> statusFix);

	@Query("select bidding from TaskBidding bidding where bidding.biddingStatus = 'DOING' "
			+ "and ((bidding.task.review1 =:userLogin and bidding.task.review1Status in :statusReview) "
				+ "or (bidding.task.review2 =:userLogin and bidding.task.review2Status in :statusReview))")
    List<TaskBidding> getBiddingTaskRVByUser(@Param("userLogin") String userLogin, @Param("statusReview") Collection<ReviewStatus> statusReview);

	@Query("select bidding from TaskBidding bidding where bidding.biddingStatus = 'DOING'"
			+ " and bidding.task.fi =:userLogin and bidding.task.fiStatus in :statusFI")
    List<TaskBidding> getBiddingTaskFIByUser(@Param("userLogin") String userLogin, @Param("statusFI") Collection<FIStatus> statusFI);

	TaskBidding findByTaskId(Long taskId);
}
