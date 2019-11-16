package fpt.dps.dtms.repository.search;

import fpt.dps.dtms.domain.DtmsMonitoring;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the DtmsMonitoring entity.
 */
public interface DtmsMonitoringSearchRepository extends ElasticsearchRepository<DtmsMonitoring, Long> {
}
