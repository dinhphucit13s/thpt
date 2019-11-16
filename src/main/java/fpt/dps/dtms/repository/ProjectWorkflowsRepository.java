package fpt.dps.dtms.repository;

import fpt.dps.dtms.domain.ProjectWorkflows;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;


/**
 * Spring Data JPA repository for the ProjectWorkflows entity.
 */
//@SuppressWarnings("unused")
@Repository
public interface ProjectWorkflowsRepository extends JpaRepository<ProjectWorkflows, Long>, JpaSpecificationExecutor<ProjectWorkflows> {
	
	@Query("Select workflows from ProjectWorkflows workflows where workflows.projectTemplates.id =:projectTemplatesId")
	Page<ProjectWorkflows> findByProjectTemplatesId(@Param("projectTemplatesId") Long projectTemplatesId, Pageable pageable);
	
}
