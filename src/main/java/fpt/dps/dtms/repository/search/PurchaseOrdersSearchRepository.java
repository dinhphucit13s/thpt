package fpt.dps.dtms.repository.search;

import fpt.dps.dtms.domain.PurchaseOrders;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Spring Data Elasticsearch repository for the PurchaseOrders entity.
 */
public interface PurchaseOrdersSearchRepository extends ElasticsearchRepository<PurchaseOrders, Long> {
	
	//@Query("{\"bool\" : {\"must\" : [ {\"field\" : {\"name\" : \"TOKYO\"}}]}}")
	@Query("{\"bool\": {\"must\": [{\"fields\": {\"project.id\": \"?0\"}}]}}")
	Page<PurchaseOrders> getAllPurchaseOrdersByProject(String name, Pageable pageable);
	
	/**
	 * Get list PurchaseOrders by projectId
	 * 
	 * @param projectId
	 * @param pageable
	 * @return Page<PurchaseOrders>
	 * 
	 * @author TuHP
	 */
	Page<PurchaseOrders> findByProjectIdAndNameLike(Long projectId, String query, Pageable pageable);
	
	/**
	 * Get list PurchaseOrders by projectId and the login user is the lead of PO
	 * 
	 * @param projectId
	 * @param poLeadId
	 * @param pageable
	 * @return
	 */
	Page<PurchaseOrders> findByProjectIdAndPurchaseOrderLeadIdAndNameLike(Long projectId, Long poLeadId, String query, Pageable pageable);
}
