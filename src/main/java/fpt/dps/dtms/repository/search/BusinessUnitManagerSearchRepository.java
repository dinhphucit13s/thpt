package fpt.dps.dtms.repository.search;

import fpt.dps.dtms.domain.BusinessUnitManager;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the BusinessUnitManager entity.
 */
public interface BusinessUnitManagerSearchRepository extends ElasticsearchRepository<BusinessUnitManager, Long> {
	/**
	 * Get list BusinessUnitManager by query search
	 * 
	 * @param query
	 * @param pageable
	 * @return
	 */
	Page<BusinessUnitManager> findByBusinessUnitNameLike(String query, Pageable pageable);
}
