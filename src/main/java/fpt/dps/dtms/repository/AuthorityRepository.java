package fpt.dps.dtms.repository;

import fpt.dps.dtms.domain.Authority;
import fpt.dps.dtms.service.dto.AuthorityDTO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

/**
 * Spring Data JPA repository for the Authority entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {
	@Query("SELECT  au FROM Authority au WHERE (au.name NOT LIKE '%ROLE_ADMIN%' AND au.name NOT LIKE 'ROLE_ROBOT')")
	Page<Authority> findAllNonSystemAuthority(Pageable pageable);
	
}
