package fpt.dps.dtms.repository;

import fpt.dps.dtms.domain.ProjectUsers;
import fpt.dps.dtms.domain.Projects;
import fpt.dps.dtms.domain.User;
import fpt.dps.dtms.domain.enumeration.FIStatus;
import fpt.dps.dtms.domain.enumeration.ProjectRoles;

import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import java.util.Optional;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;


/**
 * Spring Data JPA repository for the ProjectUsers entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProjectUsersRepository extends JpaRepository<ProjectUsers, Long>, JpaSpecificationExecutor<ProjectUsers> {

	@Query("select pu from ProjectUsers pu where pu.userLogin = :login and pu.project.id = :projectID")
	ProjectUsers findProjectUsers(@Param("login") String login, @Param("projectID") Long projectID);

	@Query("select pu from ProjectUsers pu where pu.project.id = :projectID and pu.userLogin <> pu.project.projectLead.userLogin")
	Page<ProjectUsers> findByProjectIdAndExcludeSpecifyUser(@Param("projectID") Long projectID, Pageable page);
	
	/** 
	 * Get a ProjectUsers who is the PM in the project
	 * @param userLogin
	 * @param projectId
	 * @return ProjectUsers
	 * 
	 * @author TuHP
	 */
	@Query("select pu from ProjectUsers pu where pu.project.id =:projectId And pu.userLogin =:userLogin And pu.roleName = 'PM'")
    ProjectUsers findByUserLoginAndRolePM(@Param("userLogin") String userLogin, @Param("projectId") Long projectId);
	
	/** 
	 * Get a ProjectUsers who is the TeamLead in the project
	 * @param userLogin
	 * @param projectId
	 * @return ProjectUsers
	 * 
	 * @author TuHP
	 */
	@Query("select pu from ProjectUsers pu where pu.project.id =:projectId And pu.userLogin =:userLogin And pu.roleName = 'TEAMLEAD'")
    ProjectUsers findByUserLoginAndRoleTEAMLEAD(@Param("userLogin") String userLogin, @Param("projectId") Long projectId);
	
	@Query("select pu from ProjectUsers pu where pu.project.id = :projectID and (pu.roleName = 'PM' or pu.roleName ='TEAMLEAD') and (pu.userLogin <> pu.project.projectLead.userLogin)")
	List<ProjectUsers> findByProjectIdAndExcludeSpecifyUserRole(@Param("projectID") Long projectID);

	@Query("select pu from ProjectUsers pu where pu.project.id = :projectID and pu.userLogin <> pu.project.projectLead.userLogin and pu.userLogin like CONCAT('%',:userLogin,'%')")
	Page<ProjectUsers> findByUserLoginLikeAndProjectId(@Param("userLogin")String userLogin, @Param("projectID") Long projectID, Pageable page);

	@Query("select count(pu) from ProjectUsers pu where pu.project.id = :projectID")
	Integer getSizeUserRelatingToProject(@Param("projectID") Long projectID);
	
	@Query("select pu.project from ProjectUsers pu where pu.userLogin =:userLogin and pu.roleName =:projectRoles")
	List<Projects> findByUserLoginAndRoleUser(@Param("userLogin") String userLogin,@Param("projectRoles") ProjectRoles projectRoles);
	
	@Query("select pu from ProjectUsers pu where pu.project.id =:id and (select count(u) from User u where u.login = pu.userLogin and u.activated = true)>0")
	List<ProjectUsers> getAllUsersForSelects(@Param("id") Long id);
	
	@Query("select pu from ProjectUsers pu where pu.project.id =:id")
	Page<ProjectUsers> getAllUsersByPrjectId(@Param("id") Long id, Pageable page);
	
	@Query("select pu.userLogin from ProjectUsers pu where pu.project.id =:id")
	List<String> getAllUserLoginByProjectId(@Param("id") Long id);
	
	@Query("select pu from ProjectUsers pu where pu.userLogin =:userLogin")
	Page<ProjectUsers> findHistoryByUserLogin(@Param("userLogin") String userLogin, Pageable page);
}
