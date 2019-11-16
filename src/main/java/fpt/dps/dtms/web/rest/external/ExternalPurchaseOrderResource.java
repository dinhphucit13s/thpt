package fpt.dps.dtms.web.rest.external;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

import fpt.dps.dtms.domain.enumeration.MONITORINGROLE;
import fpt.dps.dtms.domain.enumeration.PositionMonitoring;
import fpt.dps.dtms.domain.enumeration.TaskStatus;
import fpt.dps.dtms.security.AuthoritiesConstants;
import fpt.dps.dtms.security.SecurityUtils;
import fpt.dps.dtms.service.DtmsMonitoringService;
import fpt.dps.dtms.service.PurchaseOrdersQueryService;
import fpt.dps.dtms.service.PurchaseOrdersService;
import fpt.dps.dtms.service.dto.DtmsMonitoringDTO;
import fpt.dps.dtms.service.dto.ProjectsCriteria;
import fpt.dps.dtms.service.dto.PurchaseOrdersCriteria;
import fpt.dps.dtms.service.dto.PurchaseOrdersDTO;
import fpt.dps.dtms.service.dto.SelectDTO;
import fpt.dps.dtms.service.util.AppConstants;
import fpt.dps.dtms.web.rest.errors.BadRequestAlertException;
import fpt.dps.dtms.web.rest.errors.InternalServerErrorException;
import fpt.dps.dtms.web.rest.util.HeaderUtil;
import fpt.dps.dtms.web.rest.util.PaginationUtil;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing purchaseOrders.
 */
@RestController
@RequestMapping("/api/external")
public class ExternalPurchaseOrderResource {

	private final Logger log = LoggerFactory.getLogger(ExternalPurchaseOrderResource.class);

	private static final String ENTITY_NAME = "purchaseOrders";

	private final PurchaseOrdersQueryService purchaseOrdersQueryService;
	
	private final PurchaseOrdersService purchaseOrdersService;
	
	private final DtmsMonitoringService dtmsMonitoringService;

    public ExternalPurchaseOrderResource(PurchaseOrdersQueryService purchaseOrdersQueryService, PurchaseOrdersService purchaseOrdersService, 
    		DtmsMonitoringService dtmsMonitoringService) {
        this.purchaseOrdersService = purchaseOrdersService;
        this.purchaseOrdersQueryService = purchaseOrdersQueryService;
        this.dtmsMonitoringService = dtmsMonitoringService;
	}
    
    /**
     * POST  /purchase-orders : Create a new purchaseOrders.
     *
     * @param purchaseOrdersDTO the purchaseOrdersDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new purchaseOrdersDTO, or with status 400 (Bad Request) if the purchaseOrders has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/purchase-orders")
    @Timed
    @PreAuthorize("@jhiAuth.hasPermission('PURCHASE_ORDERS', 'CREATE')")
    public ResponseEntity<PurchaseOrdersDTO> createPurchaseOrders(@Valid @RequestBody PurchaseOrdersDTO purchaseOrdersDTO) throws URISyntaxException {
        log.debug("REST request to save PurchaseOrders : {}", purchaseOrdersDTO);
        if (purchaseOrdersDTO.getId() != null) {
            throw new BadRequestAlertException("A new purchaseOrders cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PurchaseOrdersDTO result = purchaseOrdersService.save(purchaseOrdersDTO);
        return ResponseEntity.created(new URI("/api/purchase-orders/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /purchase-orders : Updates an existing purchaseOrders.
     *
     * @param purchaseOrdersDTO the purchaseOrdersDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated purchaseOrdersDTO,
     * or with status 400 (Bad Request) if the purchaseOrdersDTO is not valid,
     * or with status 500 (Internal Server Error) if the purchaseOrdersDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/purchase-orders")
    @Timed
    @PreAuthorize("@jhiAuth.hasPermission('PURCHASE_ORDERS', 'EDIT')")
    public ResponseEntity<PurchaseOrdersDTO> updatePurchaseOrders(@Valid @RequestBody PurchaseOrdersDTO purchaseOrdersDTO) throws URISyntaxException {
        log.debug("REST request to update PurchaseOrders : {}", purchaseOrdersDTO);
        if (purchaseOrdersDTO.getId() == null) {
            return createPurchaseOrders(purchaseOrdersDTO);
        }
        PurchaseOrdersDTO result = purchaseOrdersService.save(purchaseOrdersDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, purchaseOrdersDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /purchase-orders : get all the purchaseOrders.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of purchaseOrders in body
     */
    @GetMapping("/purchase-orders")
    @Timed
    @PreAuthorize("@jhiAuth.hasPermission('PURCHASE_ORDERS', 'VIEW')")
    public ResponseEntity<List<PurchaseOrdersDTO>> getAllPurchaseOrders(PurchaseOrdersCriteria criteria, 
    		@RequestParam(value = "projectId", required = false) Long projectId,
    		Pageable pageable) {
        log.debug("REST request to get PurchaseOrders by criteria: {}", criteria);
        Page<PurchaseOrdersDTO> page = purchaseOrdersQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/purchase-orders");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    
    /**
     * get key-value for select by PurchaseOrder.
     */
    @GetMapping("/purchase-orders-selects")
    @Timed
    @PreAuthorize("@jhiAuth.hasPermission('PURCHASE_ORDERS', 'VIEW')")
    public ResponseEntity<List<SelectDTO>> getAllPurchaseOrdersForSelects(PurchaseOrdersCriteria criteria, 
    		@RequestParam(value = "projectId", required = false) Long projectId,
    		Pageable pageable) {
        log.debug("REST request to get PurchaseOrders by criteria: {}", criteria);
        List<SelectDTO> page = purchaseOrdersQueryService.findByCriteria(criteria);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }
    
    /**
     * 
     */
    @GetMapping("/purchase-orders-projectId")
    @Timed
    @PreAuthorize("@jhiAuth.hasPermission('PURCHASE_ORDERS', 'VIEW')")
    public ResponseEntity<List<PurchaseOrdersDTO>> getAllPurchaseOrdersByProjectId(
    		@RequestParam(value = "projectId", required = false) Long projectId,
    		Pageable pageable, ProjectsCriteria criteria) {
        log.debug("REST request to get PurchaseOrders by project Id");
        Page<PurchaseOrdersDTO> page = purchaseOrdersQueryService.getAllPurchaseOrdersByProjectId(projectId, pageable, criteria);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/purchase-orders-projectId");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * 
     */
    @GetMapping("/purchase-orders-monitoring-projectId")
    @Timed
    @PreAuthorize("@jhiAuth.hasPermission('PURCHASE_ORDERS', 'VIEW')")
    public ResponseEntity<List<PurchaseOrdersDTO>> getPurchaseOrdersWithMonitoringByProjectId(
    		@RequestParam(value = "projectId", required = false) Long projectId,
    		Pageable pageable, ProjectsCriteria criteria) {
        log.debug("REST request to get PurchaseOrders by project Id");
        Page<PurchaseOrdersDTO> page = purchaseOrdersQueryService.getPurchaseOrdersWithMonitoringByProjectId(projectId, pageable, criteria);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/purchase-orders-projectId");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    
    /**
     * 
     */
    @GetMapping("/purchase-order/bidding")
    @Timed
    public ResponseEntity<List<PurchaseOrdersDTO>> getListPurchaseOrderBiddingTask(
    		@RequestParam("projectId") Long projectId) {
        log.debug("REST request to get PurchaseOrders by project Id");
        List<PurchaseOrdersDTO> result = purchaseOrdersQueryService.getListPurchaseOrderBiddingTaskByProject(projectId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    
    /**
     * GET /purchase*/

    /**
     * GET  /purchase-orders/:id : get the "id" purchaseOrders.
     *
     * @param id the id of the purchaseOrdersDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the purchaseOrdersDTO, or with status 404 (Not Found)
     */
    @GetMapping("/purchase-orders/{id}")
    @Timed
    @PreAuthorize("@jhiAuth.hasPermission('PURCHASE_ORDERS', 'VIEW')")
    public ResponseEntity<PurchaseOrdersDTO> getPurchaseOrders(@PathVariable Long id) {
        log.debug("REST request to get PurchaseOrders : {}", id);
        PurchaseOrdersDTO purchaseOrdersDTO = purchaseOrdersService.findOne(id);
        
        if (!SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN)) {
        	String userLogin = SecurityUtils.getCurrentUserLogin()
    				.orElseThrow(() -> new InternalServerErrorException("Current user login not found"));
        	DtmsMonitoringDTO dtmsMonitoringDedicatePJ = dtmsMonitoringService.findByAllCondition(PositionMonitoring.PROJECT, purchaseOrdersDTO.getProjectId(), userLogin, MONITORINGROLE.ROLE_DEDICATED);
        	if (dtmsMonitoringDedicatePJ != null) {
        		purchaseOrdersDTO.setDtmsMonitoringProject(dtmsMonitoringDedicatePJ);
        	} else {
        		DtmsMonitoringDTO dtmsMonitoringWatcherPJ = dtmsMonitoringService.findByAllCondition(PositionMonitoring.PROJECT, purchaseOrdersDTO.getProjectId(), userLogin, MONITORINGROLE.ROLE_WATCHER);
				if (dtmsMonitoringWatcherPJ != null) {
					purchaseOrdersDTO.setDtmsMonitoringProject(dtmsMonitoringWatcherPJ);
				}
        	} 	
        }
        
        String[] watcherUsersPO = dtmsMonitoringService.getArraysDtmsMonitoringUsers(PositionMonitoring.PURCHASE_ORDER, id, MONITORINGROLE.ROLE_WATCHER);
        purchaseOrdersDTO.setWatcherUsersPO(watcherUsersPO);
		
		String[] dedicatedUsersPO = dtmsMonitoringService.getArraysDtmsMonitoringUsers(PositionMonitoring.PURCHASE_ORDER, id, MONITORINGROLE.ROLE_DEDICATED);
		purchaseOrdersDTO.setDedicatedUsersPO(dedicatedUsersPO);
        
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(purchaseOrdersDTO));
    }

    /**
     * DELETE  /purchase-orders/:id : delete the "id" purchaseOrders.
     *
     * @param id the id of the purchaseOrdersDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/purchase-orders/{id}")
    @Timed
    @PreAuthorize("@jhiAuth.hasPermission('PURCHASE_ORDERS', 'DELETE')")
    public ResponseEntity<Void> deletePurchaseOrders(@PathVariable Long id) {
        log.debug("REST request to delete PurchaseOrders : {}", id);
        try {
        	purchaseOrdersService.delete(id);
        	return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();        	
        } catch(Exception e) {
        	return ResponseEntity.badRequest().headers(HeaderUtil.serviceFailureAlert(ENTITY_NAME, AppConstants.IN_USED, "A purchase order cannot delete")).build();
        }
    }

    /**
     * SEARCH  /_search/purchase-orders?query=:query : search for the purchaseOrders corresponding
     * to the query.
     *
     * @param query the query of the purchaseOrders search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/purchase-orders")
    @Timed
    @PreAuthorize("@jhiAuth.hasPermission('PURCHASE_ORDERS', 'VIEW')")
    public ResponseEntity<List<PurchaseOrdersDTO>> searchPurchaseOrders(@RequestParam Long projectId, @RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of PurchaseOrders for query {}", query);
        Page<PurchaseOrdersDTO> page = purchaseOrdersService.search(projectId, query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/purchase-orders");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}