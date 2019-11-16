package fpt.dps.dtms.repository;

import fpt.dps.dtms.domain.TMSCustomField;
import fpt.dps.dtms.domain.TMSCustomFieldScreen;

import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;


/**
 * Spring Data JPA repository for the TMSCustomField entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TMSCustomFieldRepository extends JpaRepository<TMSCustomField, Long>, JpaSpecificationExecutor<TMSCustomField> {
	@Query("select customF from TMSCustomField customF where customF.entityData LIKE CONCAT('%',:search,'%')")
	List<TMSCustomField> getAllCustomFieldByQuerySearch(@Param("search") String search);
	
	@Query("select tmsScreen.tmsCustomField from TMSCustomFieldScreen tmsScreen where tmsScreen.projectWorkflows.id =:workflowId")
	List<TMSCustomField> getListTMSCustomFieldScreenByWFId(@Param("workflowId") Long workflowId);
}
