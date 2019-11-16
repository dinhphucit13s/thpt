package fpt.dps.dtms.repository.search;

import fpt.dps.dtms.domain.ProjectTemplates;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the ProjectTemplates entity.
 */
public interface ProjectTemplatesSearchRepository extends ElasticsearchRepository<ProjectTemplates, Long> {
	/**
	 * Get list ProjectTemplates by query
	 * 
	 * @param query
	 * @param pageable
	 * @return
	 */
	Page<ProjectTemplates> findByNameLike(String query, Pageable pageable);
}
