package fpt.dps.dtms.repository.search;

import fpt.dps.dtms.domain.MailReceiver;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the MailReceiver entity.
 */
public interface MailReceiverSearchRepository extends ElasticsearchRepository<MailReceiver, Long> {
}
