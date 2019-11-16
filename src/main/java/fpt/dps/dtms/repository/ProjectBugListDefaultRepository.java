package fpt.dps.dtms.repository;

import fpt.dps.dtms.domain.ProjectBugListDefault;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Spring Data JPA repository for the ProjectBugListDefault entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProjectBugListDefaultRepository extends JpaRepository<ProjectBugListDefault, Long>, JpaSpecificationExecutor<ProjectBugListDefault>{
	@Query("select pbl from ProjectBugListDefault pbl where pbl.project.id =:projectId")
	Page<ProjectBugListDefault> getAllProjectBugListDefaultsByProjectId(Pageable pageable, @Param("projectId") Long projectId);
}
