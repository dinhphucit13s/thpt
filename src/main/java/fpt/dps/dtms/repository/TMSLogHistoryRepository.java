package fpt.dps.dtms.repository;

import fpt.dps.dtms.domain.TMSLogHistory;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the TMSLogHistory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TMSLogHistoryRepository extends JpaRepository<TMSLogHistory, Long>, JpaSpecificationExecutor<TMSLogHistory> {

}
