package fpt.dps.dtms.repository.search;

import fpt.dps.dtms.domain.ProjectBugListDefault;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the ProjectBugListDefault entity.
 */
public interface ProjectBugListDefaultSearchRepository extends ElasticsearchRepository<ProjectBugListDefault, Long> {
}
