package fpt.dps.dtms.repository.search;

import fpt.dps.dtms.domain.TMSCustomFieldScreen;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the TMSCustomFieldScreen entity.
 */
public interface TMSCustomFieldScreenSearchRepository extends ElasticsearchRepository<TMSCustomFieldScreen, Long> {
}
