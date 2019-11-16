package fpt.dps.dtms.repository;

import fpt.dps.dtms.domain.Notification;
import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;


/**
 * Spring Data JPA repository for the Notification entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

	@Query("SELECT notif FROM Notification notif where ((:modeViewNotify = 'viewRead' AND notif.status = 1)"
			+ " OR (:modeViewNotify = 'viewUnread' AND notif.status = 0)"
			+ " OR (:modeViewNotify = 'viewBoth')) AND notif.to = :userName ORDER BY notif.id DESC")
	Page<Notification> findByUserName(@Param("userName") String userName, @Param("modeViewNotify") String modeViewNotify,
			Pageable pageable);
	
	@Query("SELECT count(notif) FROM Notification notif where notif.to = :userLogin AND notif.status = 0")
	Integer countNotificationUnread(@Param("userLogin")String userLogin);
}
