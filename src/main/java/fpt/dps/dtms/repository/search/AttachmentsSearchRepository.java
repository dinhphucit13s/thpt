package fpt.dps.dtms.repository.search;

import fpt.dps.dtms.domain.Attachments;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Attachments entity.
 */
public interface AttachmentsSearchRepository extends ElasticsearchRepository<Attachments, Long> {
}
