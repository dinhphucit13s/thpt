package fpt.dps.dtms.repository.search;

import fpt.dps.dtms.domain.TaskBiddingTrackingTime;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the TaskBiddingTrackingTime entity.
 */
public interface TaskBiddingTrackingTimeSearchRepository extends ElasticsearchRepository<TaskBiddingTrackingTime, Long> {
}
