package fpt.dps.dtms.repository.search;

import fpt.dps.dtms.domain.Mail;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Mail entity.
 */
public interface MailSearchRepository extends ElasticsearchRepository<Mail, Long> {
}
