package fpt.dps.dtms.repository;

import fpt.dps.dtms.domain.Bugs;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
/**
 * Spring Data JPA repository for the Bugs entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BugsRepository extends JpaRepository<Bugs, Long>, JpaSpecificationExecutor<Bugs> {
	
	@Query("select bug from Bugs bug where bug.tasks.id =:taskId")
	Page<Bugs> findAllBugByTaskId(@Param("taskId") Long taskId, Pageable pageable);
	
	@Query("select count(bug) from Bugs bug where bug.tasks.id =:taskId and (bug.status = 'OPEN' or bug.status = 'REOPEN') and bug.createdBy =:userLogin")
	Integer countBugOpenByTaskIdAndUserLog(@Param("taskId") Long taskId, @Param("userLogin") String userLogin);
	
	// count task has bug
	@Query(value = "select count(task.id)\r\n" + 
			"from packages as pack join tasks as task on pack.id = task.packages_id\r\n" + 
			"where task.id in (\r\n" + 
			"select bug.tasks_id\r\n" + 
			"from bugs as bug\r\n" + 
			") and pack.id =:packId", nativeQuery = true)
	Integer countTaskHasBug(@Param("packId") Long packId);

	@Query("SELECT bug From Bugs bug WHERE bug.tasks.id = :taskId ORDER BY bug.code")
	List<Bugs> findBugsByTaskId(@Param("taskId") Long taskId);
	
	@Query("select count(bug) from Bugs bug where bug.tasks.id =:taskId and bug.stage =:rowRV")
	Integer countBugByUserAssign(@Param("taskId") Long taskId, @Param("rowRV") String rowRV);
	
	@Query("select count(bug) from Bugs bug where bug.tasks.id =:taskId")
	Integer countBugByTasksId(@Param("taskId") Long taskId);
	
	@Query("select count(bug) from Bugs bug where bug.tasks.id =:taskId and bug.stage =:stage")
	Integer countBugRVFIByTasksId(@Param("taskId") Long taskId, @Param("stage") String stage);
	
}
