package fpt.dps.dtms.repository.search;

import fpt.dps.dtms.domain.TMSCustomField;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the TMSCustomField entity.
 */
public interface TMSCustomFieldSearchRepository extends ElasticsearchRepository<TMSCustomField, Long> {
	/**
	 * Find all TMSCustomField by name in entityData.
	 * @param query
	 * @param pageable
	 * @return
	 */
	Page<TMSCustomField> findByEntityDataLike (String query, Pageable pageable);
}
