package fpt.dps.dtms.repository.search;

import fpt.dps.dtms.domain.LoginTracking;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the LoginTracking entity.
 */
public interface LoginTrackingSearchRepository extends ElasticsearchRepository<LoginTracking, Long> {
}
