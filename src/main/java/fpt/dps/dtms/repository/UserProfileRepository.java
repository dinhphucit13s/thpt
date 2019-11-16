package fpt.dps.dtms.repository;

import fpt.dps.dtms.domain.UserProfile;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;


/**
 * Spring Data JPA repository for the UserProfile entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
	@Query("select userPf from UserProfile userPf where userPf.user.id =:userId")
	UserProfile findByUserId(@Param("userId") Long userId);
}
