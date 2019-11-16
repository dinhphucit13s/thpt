package fpt.dps.dtms.repository.search;

import fpt.dps.dtms.domain.Notes;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Notes entity.
 */
public interface NotesSearchRepository extends ElasticsearchRepository<Notes, Long> {
}
