package fpt.dps.dtms.repository.search;

import fpt.dps.dtms.domain.Packages;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Packages entity.
 */
public interface PackagesSearchRepository extends ElasticsearchRepository<Packages, Long> {

	Page<Packages> findByPurchaseOrdersIdAndNameLike(Long purchaseOrderId, String query, Pageable pageable);
}
