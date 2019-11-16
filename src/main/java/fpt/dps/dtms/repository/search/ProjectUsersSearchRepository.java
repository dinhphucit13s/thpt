package fpt.dps.dtms.repository.search;

import fpt.dps.dtms.domain.ProjectUsers;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the ProjectUsers entity.
 */
public interface ProjectUsersSearchRepository extends ElasticsearchRepository<ProjectUsers, Long> {
}
