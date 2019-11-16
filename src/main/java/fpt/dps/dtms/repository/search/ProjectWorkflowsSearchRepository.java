package fpt.dps.dtms.repository.search;

import fpt.dps.dtms.domain.ProjectWorkflows;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the ProjectWorkflows entity.
 */
public interface ProjectWorkflowsSearchRepository extends ElasticsearchRepository<ProjectWorkflows, Long> {
}
