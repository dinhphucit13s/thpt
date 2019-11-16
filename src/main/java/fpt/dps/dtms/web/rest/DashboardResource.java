package fpt.dps.dtms.web.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

import fpt.dps.dtms.service.DashboardService;
import fpt.dps.dtms.service.PackagesQueryService;
import fpt.dps.dtms.service.PackagesService;
import fpt.dps.dtms.service.dto.PackagesCriteria;
import fpt.dps.dtms.service.dto.PackagesDTO;
import fpt.dps.dtms.service.dto.TasksDTO;
import fpt.dps.dtms.web.rest.util.PaginationUtil;
import fpt.dps.dtms.web.rest.vm.FieldConfigVM;
import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing Packages.
 */
@RestController
@RequestMapping("/api")
public class DashboardResource {

    private final Logger log = LoggerFactory.getLogger(DashboardResource.class);

    private static final String ENTITY_NAME = "dashboard";

    private final PackagesService packagesService;

    private final PackagesQueryService packagesQueryService;
    
    private final DashboardService dashboardService;

    public DashboardResource(PackagesService packagesService, PackagesQueryService packagesQueryService, DashboardService dashboardService) {
        this.packagesService = packagesService;
        this.packagesQueryService = packagesQueryService;
        this.dashboardService = dashboardService;
    }

    /**
     * GET  /packages : get all the packages.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of packages in body
     */
    @GetMapping("/dashboad")
    @Timed
    public ResponseEntity<List<PackagesDTO>> getDataForDashboard(PackagesCriteria criteria, 
    		@RequestParam(value = "purchaseOrderId", required = false) Long purchaseOrderId, Pageable pageable) {
        log.debug("REST request to get Packages by criteria: {}", criteria);
        if (purchaseOrderId != null) {
        	Page<PackagesDTO> page = packagesQueryService.findPackagesByPurchaseOrderId(purchaseOrderId, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/packages");
            return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
        }	 else {
        	Page<PackagesDTO> page = packagesQueryService.findByCriteria(criteria, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/packages");
            return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
        }
    }
    
    /**
     * GET  /dashboard/projects/:id : get the "id" project.
     *
     * @param id the id of the project to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the packages, or with status 404 (Not Found)
     */
    @GetMapping("/dashboard/projects/{id}/packages-late")
    @Timed
    @SuppressWarnings("unchecked")
    public ResponseEntity<List<Map<String, Object>>> getAllLatedPackagesRelateToProject(@PathVariable Long id, Pageable pageable) {
    	Map<String, Object> map = dashboardService.getAllLatedPackagesRelateToProject(id, pageable);
        log.debug("REST request to get all Packages relate to Project : {}", id);
		List<Map<String, Object>> result = (List<Map<String, Object>>) map.get("result");
        Page<Map<String, Object>> page = new PageImpl<>(result); 
        HttpHeaders headers = (HttpHeaders) map.get("headers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    
    /**
     * GET /dashboard/fields/:id/packages-late get the "id" project
     * @param id the id of the project to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the package fields, or with status 404 (Not Found)
     */
    @GetMapping("/dashboard/fields/{id}/packages-late")
    @Timed
    public ResponseEntity<List<FieldConfigVM>> getPackagesFieldConfigForDashboardRelateToProject(@PathVariable Long id) {
        log.debug("REST request to get all Packages relate to Project : {}", id);
        List<FieldConfigVM> objects = dashboardService.getPackageFieldConfigForDashboard(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(objects));
    }
    
    /**
     * GET  /dashboard/projects/:id/purchase-orders : get the "id" project.
     *
     * @param id the id of the project to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the number of tasks, or with status 404 (Not Found)
     */
    @GetMapping("/dashboard/projects/{id}/tasks-late")
    @Timed
    public ResponseEntity<List<Map<String, Object>>> getAllLatedTasksRelatingProjectId(@PathVariable Long id, Pageable pageable) {
        log.debug("REST request to get all Packages relate to Project : {}", id);
        Map<String, Object> map = dashboardService.getAllLatedTasksRelateToProject(id, pageable);
        List<Map<String, Object>> result = (List<Map<String, Object>>) map.get("result");
        Page<Map<String, Object>> page = new PageImpl<>(result); 
        HttpHeaders headers = (HttpHeaders) map.get("headers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    
    /**
     * GET /dashboard/fields/:id/tasks-late get the "id" project
     * @param id the id of the project to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the packages, or with status 404 (Not Found)
     */
    @GetMapping("/dashboard/fields/{id}/tasks-late")
    @Timed
    public ResponseEntity<List<FieldConfigVM>> getTasksFieldConfigForDashboardRelateToProject(@PathVariable Long id) {
        log.debug("REST request to get all Packages relate to Project : {}", id);
        List<FieldConfigVM> objects = dashboardService.getTasksFieldConfigForDashboard();
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(objects));
    }
    
    /**
     * GET  /dashboard/projects/:id/purchase-orders : get the "id" project.
     *
     * @param id the id of the project to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the number of tasks, or with status 404 (Not Found)
     */
    @GetMapping("/dashboard/projects/{id}/tasks-unassign")
    @Timed
    public ResponseEntity<List<Map<String, Object>>> getAllUnAssignTasksRelatingProjectId(@PathVariable Long id, Pageable pageable) {
        log.debug("REST request to get all Packages relate to Project : {}", id);
        Map<String, Object> map = dashboardService.getAllUnAssignTasksRelateToProject(id, pageable);
        List<Map<String, Object>> result = (List<Map<String, Object>>) map.get("result");
        Page<Map<String, Object>> page = new PageImpl<>(result);
        HttpHeaders headers = (HttpHeaders) map.get("headers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    
    /**
     * GET /dashboard/fields/:id/tasks-late get the "id" project
     * @param id the id of the project to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the packages, or with status 404 (Not Found)
     */
    @GetMapping("/dashboard/fields/{id}/tasks-unassign")
    @Timed
    public ResponseEntity<List<FieldConfigVM>> getUnAssignTasksFieldConfigForDashboardRelateToProject(@PathVariable Long id) {
        log.debug("REST request to get all Packages relate to Project : {}", id);
        List<FieldConfigVM> objects = dashboardService.getUnAssignTasksFieldConfigForDashboard();
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(objects));
    }
    
    /**
     * GET  /dashboard/projects/:id/user : get the "id" project.
     *
     * @param id the id of the project to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the number of resource allocate to project, or with status 404 (Not Found)
     */
    @GetMapping("/dashboard/projects/{id}/users")
    @Timed
    public ResponseEntity<Integer> getAllUsersRelateToProject(@PathVariable Long id) {
        log.debug("REST request to get all Packages relate to Project : {}", id);
        Integer result = dashboardService.getAllUsersRelateToProject(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(result));
    }
    
    /**
     * GET  /dashboard/projects/:id/package : get the "id" project.
     *
     * @param id the id of the project to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the number of package, or with status 404 (Not Found)
     */
    @GetMapping("/dashboard/projects/{id}/packages")
    @Timed
    public ResponseEntity<Integer> getAllPackageRelateToProject(@PathVariable Long id) {
        log.debug("REST request to get all Packages relate to Project : {}", id);
        Integer result = dashboardService.getAllPackagesRelateToProject(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(result));
    }
    
    /**
     * GET  /dashboard/projects/:id/tasks : get the "id" project.
     *
     * @param id the id of the project to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the number of tasks, or with status 404 (Not Found)
     */
    @GetMapping("/dashboard/projects/{id}/tasks")
    @Timed
    public ResponseEntity<Integer> getAllTasksRelateToProject(@PathVariable Long id) {
        log.debug("REST request to get all Packages relate to Project : {}", id);
        Integer result = dashboardService.getAllTasksRelateToProject(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(result));
    }
    
    /**
     * GET  /dashboard/projects/:id/purchase-orders : get the "id" project.
     *
     * @param id the id of the project to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the number of tasks, or with status 404 (Not Found)
     */
    @GetMapping("/dashboard/projects/{id}/purchase-orders")
    @Timed
    public ResponseEntity<Integer> getAllPurchaseOrdersRelateToProject(@PathVariable Long id) {
        log.debug("REST request to get all Packages relate to Project : {}", id);
        Integer result = dashboardService.getAllPurchaseOrdersRelateToProject(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(result));
    }
    
    
    @GetMapping("/dashboard/tasks/{projectId}/{filter}")
    @Timed
    public ResponseEntity<List<TasksDTO>> getTasksByFilter(@PathVariable("projectId") Long projectId, @PathVariable("filter") String filter) {
        log.debug("REST request to get all Packages relate to Project : {}", projectId);
        List<TasksDTO> result = dashboardService.getTasksByFilter(projectId, filter);
        return ResponseEntity.ok().body(result);
    }
}
