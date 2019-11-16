package fpt.dps.dtms.repository.search;

import fpt.dps.dtms.domain.TmsPost;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the TmsPost entity.
 */
public interface TmsPostSearchRepository extends ElasticsearchRepository<TmsPost, Long> {
}
