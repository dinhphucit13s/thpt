package fpt.dps.dtms.repository.search;

import fpt.dps.dtms.domain.Tasks;
import fpt.dps.dtms.domain.enumeration.TaskStatus;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Tasks entity.
 */
public interface TasksSearchRepository extends ElasticsearchRepository<Tasks, Long> {

}
