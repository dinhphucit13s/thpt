package fpt.dps.dtms.repository;

import fpt.dps.dtms.domain.NotificationTemplate;
import fpt.dps.dtms.domain.enumeration.NotificationCategory;

import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;


/**
 * Spring Data JPA repository for the NotificationTemplate entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NotificationTemplateRepository extends JpaRepository<NotificationTemplate, Long> {

	@Query("SELECT nt.template FROM NotificationTemplate nt WHERE nt.type = :type")
	String getContentTemplate(@Param("type")NotificationCategory type);

}
