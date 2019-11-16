package fpt.dps.dtms.repository;

import fpt.dps.dtms.domain.BusinessLine;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the BusinessLine entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BusinessLineRepository extends JpaRepository<BusinessLine, Long>, JpaSpecificationExecutor<BusinessLine> {

}
