package fpt.dps.dtms.repository;

import fpt.dps.dtms.domain.Notes;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Notes entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NotesRepository extends JpaRepository<Notes, Long>, JpaSpecificationExecutor<Notes> {

}
