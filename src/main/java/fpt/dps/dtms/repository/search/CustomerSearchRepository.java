package fpt.dps.dtms.repository.search;

import fpt.dps.dtms.domain.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Customer entity.
 */
public interface CustomerSearchRepository extends ElasticsearchRepository<Customer, Long> {
	/**
	 * Get list Customer by query search
	 * 
	 * @param query
	 * @param pageable
	 * @return
	 */
	Page<Customer> findByNameLike(String query, Pageable pageable);
}
