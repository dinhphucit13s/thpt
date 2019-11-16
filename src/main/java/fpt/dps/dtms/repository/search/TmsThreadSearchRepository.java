package fpt.dps.dtms.repository.search;

import fpt.dps.dtms.domain.TmsThread;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the TmsThread entity.
 */
public interface TmsThreadSearchRepository extends ElasticsearchRepository<TmsThread, Long> {
}
