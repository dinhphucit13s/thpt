package fpt.dps.dtms.repository;

import fpt.dps.dtms.domain.Attachments;
import fpt.dps.dtms.domain.Bugs;

import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;


/**
 * Spring Data JPA repository for the Attachments entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AttachmentsRepository extends JpaRepository<Attachments, Long>, JpaSpecificationExecutor<Attachments> {
	@Query("select atm from Attachments atm where atm.bugs.id =:bugsId")
	Page<Attachments> findAllAttachmentsByBugsId(@Param("bugsId") Long bugsId, Pageable pageable);

	@Query("select atm from Attachments atm where atm.issues.id =:id")
	List<Attachments> findAllAttachmentsByIssuesId(@Param("id") Long id);

	@Query("select atm from Attachments atm where (atm.bugs.id =:parentId and :key = 'BUG')"
			+ " or (atm.notes.id = :parentId and :key ='NOTES')"
			+ " or (atm.issues.id =:parentId and :key = 'ISSUES')"
			+ "or (atm.mail.id =:parentId and :key = 'MAIL')")
	List<Attachments> findByParentId(@Param("parentId") Long parentId, @Param("key") String key);
}
