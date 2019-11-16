package fpt.dps.dtms.repository;

import fpt.dps.dtms.domain.ProjectTemplates;
import org.springframework.stereotype.Repository;

import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the ProjectTemplates entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProjectTemplatesRepository extends JpaRepository<ProjectTemplates, Long>, JpaSpecificationExecutor<ProjectTemplates> {	
}
