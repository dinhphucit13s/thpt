package fpt.dps.dtms.repository;

import fpt.dps.dtms.domain.BusinessUnit;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;


/**
 * Spring Data JPA repository for the BusinessUnit entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BusinessUnitRepository extends JpaRepository<BusinessUnit, Long>, JpaSpecificationExecutor<BusinessUnit> {

	BusinessUnit findOneBycode(String buCode);

	@Query("SELECT pu.project.businessUnit FROM ProjectUsers pu where pu.userLogin = :userLogin AND (:today BETWEEN pu.startDate AND pu.endDate) "
			+ "AND pu.project.status <> 'CLOSED' GROUP BY pu.project.businessUnit.id")
	List<BusinessUnit> findListEffectBUByUserLogin(@Param("userLogin") String userLogin, @Param("today") LocalDate today);

}
