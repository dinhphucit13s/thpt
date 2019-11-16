package fpt.dps.dtms.repository.search;

import fpt.dps.dtms.domain.TMSCustomFieldScreenValue;

import java.util.List;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the TMSCustomFieldScreenValue entity.
 */
public interface TMSCustomFieldScreenValueSearchRepository extends ElasticsearchRepository<TMSCustomFieldScreenValue, Long> {
	List<TMSCustomFieldScreenValue> findAllByPackagesId(Long packagesId);
	
	List<TMSCustomFieldScreenValue> findAllByTasksId(Long tasksId);
}
