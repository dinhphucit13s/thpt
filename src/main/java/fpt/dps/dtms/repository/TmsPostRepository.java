package fpt.dps.dtms.repository;

import fpt.dps.dtms.domain.TmsPost;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;


/**
 * Spring Data JPA repository for the TmsPost entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TmsPostRepository extends JpaRepository<TmsPost, Long> {

	@Query ("SELECT posts FROM TmsPost posts WHERE posts.thread.id = :threadId and posts.id <> :exceptId")
	Page<TmsPost> findAnswer(@Param("threadId") Long threadId, @Param("exceptId") Long exceptId, Pageable pageable);
}
