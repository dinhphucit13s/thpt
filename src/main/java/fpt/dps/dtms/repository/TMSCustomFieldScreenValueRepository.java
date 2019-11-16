package fpt.dps.dtms.repository;

import fpt.dps.dtms.domain.TMSCustomFieldScreenValue;
import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;


/**
 * Spring Data JPA repository for the TMSCustomFieldScreenValue entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TMSCustomFieldScreenValueRepository extends JpaRepository<TMSCustomFieldScreenValue, Long>, JpaSpecificationExecutor<TMSCustomFieldScreenValue> {
	@Query("select tmsCustomFieldScreenValue from TMSCustomFieldScreenValue tmsCustomFieldScreenValue where tmsCustomFieldScreenValue.packages.id =:packId")
	List<TMSCustomFieldScreenValue> getAllTMSCustomFieldScreenValueByPackId(@Param("packId") Long packId);
	
	@Query("select tmsCustomFieldScreenValue from TMSCustomFieldScreenValue tmsCustomFieldScreenValue where tmsCustomFieldScreenValue.tasks.id =:TaskId")
	List<TMSCustomFieldScreenValue> getAllTMSCustomFieldScreenValueByTaskId(@Param("TaskId") Long TaskId);
	
	@Query("select tmsCustomFieldScreenValue from TMSCustomFieldScreenValue tmsCustomFieldScreenValue where tmsCustomFieldScreenValue.packages.id =:packId and tmsCustomFieldScreenValue.tmsCustomFieldScreen.id =:tmsScreenId")
	TMSCustomFieldScreenValue getTMSCustomFieldScreenValueByPackageAndScreen(@Param("packId") Long packId, @Param("tmsScreenId") Long tmsScreenId);
	
	@Query("select tmsCustomFieldScreenValue from TMSCustomFieldScreenValue tmsCustomFieldScreenValue where tmsCustomFieldScreenValue.tasks.id =:taskId and tmsCustomFieldScreenValue.tmsCustomFieldScreen.id =:tmsScreenId")
	TMSCustomFieldScreenValue getTMSCustomFieldScreenValueByTaskAndScreen(@Param("taskId") Long taskId, @Param("tmsScreenId") Long tmsScreenId);
}
