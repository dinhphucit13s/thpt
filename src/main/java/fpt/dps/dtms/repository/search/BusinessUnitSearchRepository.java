package fpt.dps.dtms.repository.search;

import fpt.dps.dtms.domain.BusinessUnit;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the BusinessUnit entity.
 */
public interface BusinessUnitSearchRepository extends ElasticsearchRepository<BusinessUnit, Long> {
	/**
	 * Get list BusinessUnit by query search
	 * 
	 * @param query
	 * @param pageable
	 * @return
	 */
	Page<BusinessUnit> findByNameLike(String query, Pageable pageable);
}
