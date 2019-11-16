package fpt.dps.dtms.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import fpt.dps.dtms.domain.CustomReports;
import fpt.dps.dtms.domain.Packages;
import fpt.dps.dtms.domain.Projects;

/**
 * Spring Data JPA repository for the CustomReports entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CustomReportsRepository extends JpaRepository<CustomReports, Long>, JpaSpecificationExecutor<CustomReports>{
	@Query("select customReports from CustomReports customReports where customReports.pageName =:pageName And customReports.userLogin =:userLogin")
	CustomReports findCustomReportByPageName(@Param("pageName") String pageName, @Param("userLogin") String userLogin);
}
