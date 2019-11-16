package fpt.dps.dtms.repository.search;

import fpt.dps.dtms.domain.NotificationTemplate;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the NotificationTemplate entity.
 */
public interface NotificationTemplateSearchRepository extends ElasticsearchRepository<NotificationTemplate, Long> {
}
