package fpt.dps.dtms.repository.search;

import fpt.dps.dtms.domain.BusinessLine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the BusinessLine entity.
 */
public interface BusinessLineSearchRepository extends ElasticsearchRepository<BusinessLine, Long> {
	/**
	 * Get list BusinessLine by query search
	 * 
	 * @param query
	 * @param pageable
	 * @return
	 */
	Page<BusinessLine>findByNameLike(String query, Pageable pageable);
}
