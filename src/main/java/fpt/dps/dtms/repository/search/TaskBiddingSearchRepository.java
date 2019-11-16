package fpt.dps.dtms.repository.search;

import fpt.dps.dtms.domain.TaskBidding;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the TaskBidding entity.
 */
public interface TaskBiddingSearchRepository extends ElasticsearchRepository<TaskBidding, Long> {
}
