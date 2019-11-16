package fpt.dps.dtms.repository;

import fpt.dps.dtms.domain.DtmsMonitoring;
import fpt.dps.dtms.domain.Tasks;
import fpt.dps.dtms.domain.enumeration.MONITORINGROLE;
import fpt.dps.dtms.domain.enumeration.PositionMonitoring;

import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;


/**
 * Spring Data JPA repository for the DtmsMonitoring entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DtmsMonitoringRepository extends JpaRepository<DtmsMonitoring, Long>, JpaSpecificationExecutor<DtmsMonitoring> {

	@Query("select monitor from DtmsMonitoring monitor where monitor.position =:position and monitor.positionId =:positionId and monitor.role =:role")
	List<DtmsMonitoring> getDtmsMonitoringUsers(@Param("position") PositionMonitoring position, @Param("positionId") Long positionId, @Param("role") MONITORINGROLE role);

	@Query("select monitor from DtmsMonitoring monitor where monitor.position =:position and monitor.members =:userLogin ")
	List<DtmsMonitoring> getDtmsMonitoringByUserLogin(@Param("position") PositionMonitoring position, @Param("userLogin") String userLogin);

	@Query("select monitor from DtmsMonitoring monitor where monitor.position =:position and monitor.members =:userLogin ")
	List<DtmsMonitoring> getDtmsMonitoringByUserLoginAndPosition(@Param("position") PositionMonitoring position, @Param("userLogin") String userLogin);
	
	@Query("select monitor from DtmsMonitoring monitor where monitor.position =:position and monitor.positionId =:positionId and monitor.members =:userLogin and monitor.role =:role")
	DtmsMonitoring findByAllCondition(@Param("position") PositionMonitoring position, @Param("positionId")Long positionId, @Param("userLogin") String userLogin, @Param("role") MONITORINGROLE role);
}
