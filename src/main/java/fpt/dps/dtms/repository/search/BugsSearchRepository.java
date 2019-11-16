package fpt.dps.dtms.repository.search;

import fpt.dps.dtms.domain.Bugs;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Bugs entity.
 */
public interface BugsSearchRepository extends ElasticsearchRepository<Bugs, Long> {
}
