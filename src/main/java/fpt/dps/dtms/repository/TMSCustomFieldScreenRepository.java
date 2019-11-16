package fpt.dps.dtms.repository;

import fpt.dps.dtms.domain.TMSCustomField;
import fpt.dps.dtms.domain.TMSCustomFieldScreen;
import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;


/**
 * Spring Data JPA repository for the TMSCustomFieldScreen entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TMSCustomFieldScreenRepository extends JpaRepository<TMSCustomFieldScreen, Long>, JpaSpecificationExecutor<TMSCustomFieldScreen> {
	@Query("select tmsScreen from TMSCustomFieldScreen tmsScreen where tmsScreen.projectWorkflows.id =:workflowId")
	List<TMSCustomFieldScreen> getListTMSCustomFieldScreenByWFId(@Param("workflowId") Long workflowId);
	
	@Query("select tmsScreen from TMSCustomFieldScreen tmsScreen where tmsScreen.tmsCustomField.id =:customFieldId and tmsScreen.projectWorkflows.id =:workflowId")
	TMSCustomFieldScreen getTMSCustomFieldScreenByWFId(@Param("customFieldId") Long customFieldId, @Param("workflowId") Long workflowId);
}
