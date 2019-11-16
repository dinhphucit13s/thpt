package fpt.dps.dtms.repository;

import fpt.dps.dtms.domain.PurchaseOrders;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import org.springframework.data.jpa.repository.*;
import java.util.List;


/**
 * Spring Data JPA repository for the PurchaseOrders entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PurchaseOrdersRepository extends JpaRepository<PurchaseOrders, Long>, JpaSpecificationExecutor<PurchaseOrders> {
	
	/**
	 * get all purchase-order by project id
	 */
	@Query("select po from PurchaseOrders po where po.project.id =:projectId")
	Page<PurchaseOrders> getAllPurchaseOrdersByProjectId(@Param("projectId") Long projectId, Pageable pageable);
	
	@Query("select po from PurchaseOrders po where po.project.id =:projectId")
	List<PurchaseOrders> getAllPurchaseOrdersByProjectId(@Param("projectId") Long projectId);
	
	@Query(value="SELECT po.* FROM purchase_orders po INNER JOIN projects pj ON po.project_id = pj.id AND pj.id = :projectId "
			+ "INNER JOIN project_users pu ON pj.id = pu.project_id AND pu.user_login = :userLogin " 
			+ "WHERE (pu.id = pj.project_lead_id) OR (po.purchase_order_lead_id = pu.id)", nativeQuery=true)
	List<PurchaseOrders> getListPurchaseOrderBiddingTask(@Param("projectId") Long projectId, @Param("userLogin") String userLogin);
	
	/**
	 * get page purchase-order by project id and teamlead id
	 */
	@Query("select po from PurchaseOrders po where po.project.id =:projectId and po.purchaseOrderLead.id =:teamLeadId")
	Page<PurchaseOrders> getAllPurchaseOrdersByProjectIdAndTeamLead(@Param("projectId") Long projectId, @Param("teamLeadId") Long teamLeadId, Pageable pageable);
	
	/**
	 * get all purchase-order by project id and teamlead id
	 */
	@Query("select po from PurchaseOrders po where po.project.id =:projectId and po.purchaseOrderLead.id =:teamLeadId")
	List<PurchaseOrders> getAllPurchaseOrdersByProjectIdAndTeamLead(@Param("projectId") Long projectId, @Param("teamLeadId") Long teamLeadId);
	
	/**
	 * get number of purchase-orders by project id
	 */
	@Query("select count(po) from PurchaseOrders po where po.project.id =:projectId")
	Integer getSizePurchaseOrdersRelatingToProject(@Param("projectId") Long projectId);
	
	/** get select PO destination 
	 *  which includes all the PO 
	 *  (has PROCESSING, OPEN status)
	 *  PhuVD3
	 */
	@Query("select po from PurchaseOrders po where po.project.id =:projectId and (status = 'OPEN' or status = 'PROCESSING')")
	List<PurchaseOrders> getListPurchaseOrdersClone(@Param("projectId") Long projectId);

	@Query("select po from PurchaseOrders po where po.project.id =:projectId and po.projectTemplates is not null")
	List<PurchaseOrders> getPurchaseOrdersHaveWorkFlow(@Param("projectId") Long projectId);
}
