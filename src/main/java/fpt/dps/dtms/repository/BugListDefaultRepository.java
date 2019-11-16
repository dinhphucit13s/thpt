package fpt.dps.dtms.repository;

import fpt.dps.dtms.domain.BugListDefault;
import fpt.dps.dtms.domain.ProjectBugListDefault;

import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;


/**
 * Spring Data JPA repository for the BugListDefault entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BugListDefaultRepository extends JpaRepository<BugListDefault, Long>, JpaSpecificationExecutor<BugListDefault> {
	
	@Query(value = "select buglist from BugListDefault buglist where buglist.id NOT IN"
			+ " (select pbl.bugListDefault.id from ProjectBugListDefault pbl where pbl.project.id =:projectId)"
			+ " and buglist.status = 1")
	List<BugListDefault> getAllBugListDefaultsUnExistInProject(@Param("projectId") Long projectId);

	@Query("SELECT pbl FROM ProjectBugListDefault pbl WHERE pbl.project.id =:projectId AND pbl.bugListDefault.status ='1' AND pbl.code not in"
			+ " (SELECT bug.code FROM Bugs bug WHERE bug.tasks.id =:tasksId AND bug.stage =:stage ) ORDER BY pbl.code")
	List<ProjectBugListDefault> getBugListDefaultByProject(@Param("projectId") Long projectId, @Param("tasksId") Long tasksId, @Param("stage") String stage);

}
