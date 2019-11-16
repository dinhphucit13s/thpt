package fpt.dps.dtms.repository;

import fpt.dps.dtms.domain.AuthorityResource;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;


/**
 * Spring Data JPA repository for the AuthorityResource entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AuthorityResourceRepository extends JpaRepository<AuthorityResource, Long>, JpaSpecificationExecutor<AuthorityResource> {
	
	@Query
	Set<AuthorityResource> findByAuthorityName(String name);
	
	@Transactional
	@Modifying
	@Query("delete from AuthorityResource ar where ar.authorityName =:name")
	void deleteAuthorityResourcesyByAuthorityName(@Param("name") String name);
}
