package fpt.dps.dtms.repository.search;

import fpt.dps.dtms.domain.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the User entity.
 */
public interface UserSearchRepository extends ElasticsearchRepository<User, Long> {
	/**
	 * Get list User by query search
	 * 
	 * @param query
	 * @param pageable
	 * @return
	 */
	Page<User> findByLoginLike(String query, Pageable pageable);
}
