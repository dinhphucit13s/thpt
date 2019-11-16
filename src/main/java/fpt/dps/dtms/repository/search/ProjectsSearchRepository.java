package fpt.dps.dtms.repository.search;

import fpt.dps.dtms.domain.Projects;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Projects entity.
 */
public interface ProjectsSearchRepository extends ElasticsearchRepository<Projects, Long> {
	/**
	 * Get list Project by query search
	 * 
	 * @param query
	 * @param pageable
	 * @return
	 */
	Page<Projects> findByNameLike(String query, Pageable pageable);

	/**
	 * Get list Project by the login user is project lead and query search
	 * 
	 * @param projectLeadId
	 * @param query
	 * @param pageable
	 * @return
	 */
	Page<Projects> findByProjectLeadUserLoginAndNameLike(String userLogin, String query, Pageable pageable);
}
