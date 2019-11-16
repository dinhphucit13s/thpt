package fpt.dps.dtms.repository.search;

import fpt.dps.dtms.domain.TMSLogHistory;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the TMSLogHistory entity.
 */
public interface TMSLogHistorySearchRepository extends ElasticsearchRepository<TMSLogHistory, Long> {
}
