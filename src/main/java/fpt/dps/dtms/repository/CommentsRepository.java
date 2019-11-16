package fpt.dps.dtms.repository;

import fpt.dps.dtms.domain.Comments;
import fpt.dps.dtms.service.dto.CommentsDTO;

import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;


/**
 * Spring Data JPA repository for the Comments entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CommentsRepository extends JpaRepository<Comments, Long> {

	@Query("SELECT cmt FROM Comments cmt WHERE cmt.post.id = :postId")
	List<Comments> getCommentByPostId(@Param("postId") Long postId);
}
