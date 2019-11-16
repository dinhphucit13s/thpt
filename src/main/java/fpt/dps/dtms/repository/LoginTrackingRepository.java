package fpt.dps.dtms.repository;

import fpt.dps.dtms.domain.LoginTracking;
import org.springframework.stereotype.Repository;

import java.time.Instant;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;


/**
 * Spring Data JPA repository for the LoginTracking entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LoginTrackingRepository extends JpaRepository<LoginTracking, Long>, JpaSpecificationExecutor<LoginTracking> {

	/**
	 * Find the first login record on a day .
	 * */
	LoginTracking findFirstByLoginAndStartTimeAfterOrderByStartTimeAsc(String login, Instant startTime);

	/**
	 * Find the latest login record.
	 */
	LoginTracking findFirstByLoginOrderByStartTimeDesc(String login);
	
	@Query("select loginTrack from LoginTracking loginTrack where loginTrack.id = (select min(track.id) from LoginTracking track where track.login =:userLogin and track.startTime >=:date)")
	LoginTracking getStartUser(@Param("userLogin") String userLogin, @Param("date") Instant date);
	
	@Query("select loginTrack from LoginTracking loginTrack where loginTrack.id = (select max(track.id) from LoginTracking track where track.login =:userLogin and track.endTime >=:date)")
	LoginTracking getEndUser(@Param("userLogin") String userLogin, @Param("date") Instant date);
}
