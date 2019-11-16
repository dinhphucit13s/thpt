package fpt.dps.dtms.repository;

import fpt.dps.dtms.domain.BusinessUnitManager;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the BusinessUnitManager entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BusinessUnitManagerRepository extends JpaRepository<BusinessUnitManager, Long>, JpaSpecificationExecutor<BusinessUnitManager> {

    @Query("select business_unit_manager from BusinessUnitManager business_unit_manager where business_unit_manager.manager.login = ?#{principal.username}")
    List<BusinessUnitManager> findByManagerIsCurrentUser();

}
