package fpt.dps.dtms.repository;

import fpt.dps.dtms.domain.TaskBiddingTrackingTime;

import org.springframework.stereotype.Repository;

import java.time.Instant;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;


/**
 * Spring Data JPA repository for the TaskBiddingTrackingTime entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TaskBiddingTrackingTimeRepository extends JpaRepository<TaskBiddingTrackingTime, Long>, JpaSpecificationExecutor<TaskBiddingTrackingTime> {
	
	TaskBiddingTrackingTime findTop1ByTaskIdAndUserLoginAndEndTimeOrderByIdDesc(Long taskId, String userLogin, Instant endTime);
	
	TaskBiddingTrackingTime findTop1ByTaskIdAndRole(Long taskId, String role);
	
	@Query(value="SELECT jhi_role FROM task_bidding_tracking_time where task_id = :taskId and jhi_role <> :currentRound order by id desc limit 1", nativeQuery=true)
	String getPreviousRound(@Param("taskId") Long taskId, @Param("currentRound") String currentRound);
}
