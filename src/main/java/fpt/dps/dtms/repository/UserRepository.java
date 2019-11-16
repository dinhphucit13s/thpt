package fpt.dps.dtms.repository;

import fpt.dps.dtms.domain.ProjectUsers;
import fpt.dps.dtms.domain.User;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.time.Instant;

/**
 * Spring Data JPA repository for the User entity.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    String USERS_BY_LOGIN_CACHE = "usersByLogin";

    String USERS_BY_EMAIL_CACHE = "usersByEmail";

    Optional<User> findOneByActivationKey(String activationKey);

    List<User> findAllByActivatedIsFalseAndCreatedDateBefore(Instant dateTime);

    Optional<User> findOneByResetKey(String resetKey);

    Optional<User> findOneByEmailIgnoreCase(String email);
    
    Optional<User> findOneByLoginOrEmailIgnoreCase(String login, String email);

    Optional<User> findOneByLogin(String login);
    
    User findByLogin(String login);
    
    Optional<User> findOneByLoginAndAuthoritiesName(String login, String RoleName);

    @EntityGraph(attributePaths = "authorities")
    Optional<User> findOneWithAuthoritiesById(Long id);

    @EntityGraph(attributePaths = "authorities")
    @Cacheable(cacheNames = USERS_BY_LOGIN_CACHE)
    Optional<User> findOneWithAuthoritiesByLogin(String login);

    @EntityGraph(attributePaths = "authorities")
    @Cacheable(cacheNames = USERS_BY_EMAIL_CACHE)
    Optional<User> findOneWithAuthoritiesByEmail(String email);

    Page<User> findAllByLoginNot(Pageable pageable, String login);
    
    List<User> findAllByLoginNot(String login);
    
    @Query(value = "SELECT * FROM jhi_user where login not in (select user_login from project_users where project_id =?) and jhi_user.activated = true", nativeQuery = true)
    List<User> finnAllByNotInProjectUser(Long id);
    
    @Query("SELECT user FROM User user join ProjectUsers pu on user.login = pu.userLogin WHERE pu.project.id =:id")
    Page<User> findAllUserByProjectId(@Param("id") Long id, Pageable pageable);
    
    @Query("SELECT user FROM User user "
    		+ "join ProjectUsers pu on user.login = pu.userLogin "
    		+ "join PurchaseOrders po on po.project.id = pu.project.id "
    		+ "join Packages p on p.purchaseOrders.id = po.id "
    		+ "WHERE pu.project.id =:id or po.id =:id or p.id =:id")
    List<User> getListUserByProjectId(@Param("id") Long id);
    
    @Query("SELECT user FROM User user WHERE user.login LIKE CONCAT('%',:search,'%')")
    Page<User> findUsersBySearch(@Param("search") String search, Pageable pageable);
    
    @Query("SELECT user FROM User user WHERE user.login LIKE CONCAT('%',:search,'%')")
    List<User> findAllUsersBySearch(@Param("search") String search);
    
    @Query("SELECT pu FROM ProjectUsers pu JOIN User user ON pu.userLogin = user.login AND pu.project.id = :projectId"
    		+ " WHERE user.activated = 1")
	List<ProjectUsers> findUserActivatedByProject(@Param("projectId") Long projectId);
    
    @Query("SELECT user.id FROM User user WHERE user.login =:login")
    Long findUserLogin(@Param("login") String login);
}
