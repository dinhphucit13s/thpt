package fpt.dps.dtms.repository.search;

import fpt.dps.dtms.domain.AuthorityResource;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the AuthorityResource entity.
 */
public interface AuthorityResourceSearchRepository extends ElasticsearchRepository<AuthorityResource, Long> {
}
