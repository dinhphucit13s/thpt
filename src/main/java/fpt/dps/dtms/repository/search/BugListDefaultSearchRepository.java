package fpt.dps.dtms.repository.search;

import fpt.dps.dtms.domain.BugListDefault;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the BugListDefault entity.
 */
public interface BugListDefaultSearchRepository extends ElasticsearchRepository<BugListDefault, Long> {
	/**
	 * Get list BugListDefault by query search
	 * 
	 * @param query
	 * @param pageable
	 * @return
	 */
	Page<BugListDefault> findByDescriptionLike(String query, Pageable pageable);
}
