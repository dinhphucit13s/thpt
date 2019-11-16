package fpt.dps.dtms.repository;

import fpt.dps.dtms.domain.BugListDefault;
import fpt.dps.dtms.domain.Projects;
import fpt.dps.dtms.domain.enumeration.ProjectRoles;
import fpt.dps.dtms.domain.enumeration.ProjectStatus;
import fpt.dps.dtms.service.dto.ProjectsDTO;

import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

/**
 * Spring Data JPA repository for the Projects entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProjectsRepository extends JpaRepository<Projects, Long>, JpaSpecificationExecutor<Projects> {
//    @Query("select distinct projects from Projects projects left join fetch projects.bugListDefaults")
//    List<Projects> findAllWithEagerRelationships();

    /*@Query("select projects from Projects projects left join fetch projects.bugListDefaults where projects.id =:id")
    Projects findOneWithEagerRelationships(@Param("id") Long id);*/

    /*@Query("select pro from Projects pro where exists (Select po.project from PurchaseOrders po where po.id = :poID)")
    Projects getProjectByPurchaseOrderId(@Param("poID") Long poID);*/

    @Query("Select po.project from PurchaseOrders po where po.id = :poID")
    Projects getProjectByPurchaseOrderId(@Param("poID") Long poID);

    @Query("select pu.project from ProjectUsers pu where pu.userLogin =:userLogin And (pu.roleName = 'PM' or pu.roleName = 'TEAMLEAD')")
    List<Projects> findByUserLoginAndRoleUser(@Param("userLogin") String userLogin);

    // get list project_id by user login and role PM.
    @Query("select pu.project from ProjectUsers pu where pu.userLogin =:userLogin And pu.roleName = 'PM' And pu.project.id =:projectId")
    Projects findByUserLoginAndRolePM(@Param("userLogin") String userLogin, @Param("projectId") Long projectId);

    @Query("select pu.project from ProjectUsers pu where pu.userLogin =:userLogin and pu.roleName in :roles and pu.project.status in :status "
    		+ "and (:today between pu.startDate and pu.endDate)")
    List<Projects> findByUserLoginAndRoles(@Param("userLogin") String userLogin, @Param("status") Collection<ProjectStatus> status,
    		@Param("roles") Collection<ProjectRoles> roles, @Param("today") LocalDate today);

    @Query("select pu.project from ProjectUsers pu where pu.userLogin =:userLogin and pu.roleName = 'OPERATOR' and pu.project.status in :status")
    List<Projects> findByUserLoginAndRolesOP(@Param("userLogin") String userLogin, @Param("status") Collection<ProjectStatus> status);

	Projects findByCode(String string);

	@Query("select pu.project from ProjectUsers pu where (LOWER(pu.userLogin)=:userLogin and :modeBidding = 'project')"
			+ " or (:modeBidding = 'businessUnit' and pu.project.businessUnit.id in (select u.businessUnit.id from User u where u.login = :userLogin)"
			+ " or (:modeBidding = 'publicAll')) group by pu.project.id order by pu.project.name")
	List<Projects> findListProjectBiddingTaskOP(@Param("userLogin") String userLogin, @Param("modeBidding") String modeBidding);

	@Query(value="SELECT pj.* FROM projects pj INNER JOIN project_users pu ON pj.id = pu.project_id AND pu.user_login = :userLogin "
			+ "INNER JOIN purchase_orders po ON pj.id = po.project_id WHERE pj.status <> CLOSED "
			+ "(pu.id = pj.project_lead_id) OR (po.purchase_order_lead_id = pu.id) "
			+ "GROUP BY pj.id", nativeQuery=true)
	List<Projects> findListProjectBiddingTaskPM(@Param("userLogin") String userLogin);
	
	@Query("select project from Projects project where "
    		+ "project in (select pu.project from ProjectUsers pu where pu.userLogin =:userLogin And (pu.roleName In ('PM','TEAMLEAD')) "
    		+ "Or project in (select po.project from PurchaseOrders po where "
    			+ "po.id In (select monitor.positionId from DtmsMonitoring monitor where monitor.position = 'PURCHASE_ORDER' and monitor.members =:userLogin))"
		    + "Or project.id In (select monitor.positionId from DtmsMonitoring monitor where monitor.position = 'PROJECT' and monitor.members =:userLogin))")
    List<Projects> findByUserLoginForPO(@Param("userLogin") String userLogin);
	
	
	@Query("SELECT p FROM Projects p where p.businessUnit.id = :buId and p.status in :status")
	List<Projects> findByBusinessUnit(@Param("buId") Long buId, @Param("status") List<ProjectStatus> status);

}
