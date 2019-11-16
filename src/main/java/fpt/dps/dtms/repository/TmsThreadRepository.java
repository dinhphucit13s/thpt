package fpt.dps.dtms.repository;

import fpt.dps.dtms.domain.TmsThread;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;


/**
 * Spring Data JPA repository for the TmsThread entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TmsThreadRepository extends JpaRepository<TmsThread, Long>, JpaSpecificationExecutor<TmsThread> {

	@Query("SELECT thread FROM TmsThread thread WHERE thread.projects.id = :projectId AND ((:filter='assignToMe'"
			+ " AND LOWER(thread.assignee.userLogin)=:currentUserLogin) OR (:filter='reportByMe' AND LOWER(thread.createdBy)=:currentUserLogin)"
			+ " OR (:filter='all')) ORDER BY thread.id DESC")
	Page<TmsThread> findQuestionAndAnswer(@Param("projectId") Long projectId, @Param("filter") String filter,
			@Param("currentUserLogin") String currentUserLogin, Pageable page);
}
