package fpt.dps.dtms.repository;

import fpt.dps.dtms.domain.Issues;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;


/**
 * Spring Data JPA repository for the Issues entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IssuesRepository extends JpaRepository<Issues, Long>, JpaSpecificationExecutor<Issues> {
	@Query("select issues from Issues issues where issues.projects.code = 'TM_CAMPAIGN'")
	Page<Issues> getAllIssuesByCampaign(Pageable pageable);

	@Query("select count(issues) from Issues issues where issues.projects.code = 'TM_CAMPAIGN' and issues.status = 'ACCEPTED'")
	Integer countIssueAccepted();

	@Query("SELECT issues FROM Issues issues WHERE issues.projects.id =:projectId AND issues.createdBy =:userLogin ORDER BY issues.id DESC")
    	Page<Issues> findIssueByProjectIdAndUser(@Param("projectId") Long projectId, @Param("userLogin") String userLogin, Pageable pageable);
}
