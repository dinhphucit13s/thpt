package fpt.dps.dtms.repository.search;

import fpt.dps.dtms.domain.TaskTrackingTime;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the TaskTrackingTime entity.
 */
public interface TaskTrackingTimeSearchRepository extends ElasticsearchRepository<TaskTrackingTime, Long> {
}
