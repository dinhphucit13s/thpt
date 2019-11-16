package fpt.dps.dtms.repository.search;

import fpt.dps.dtms.domain.Issues;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Issues entity.
 */
public interface IssuesSearchRepository extends ElasticsearchRepository<Issues, Long> {
}
