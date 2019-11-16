package fpt.dps.dtms.web.rest.external;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.codahale.metrics.annotation.Timed;

import fpt.dps.dtms.domain.enumeration.ProjectStatus;
import fpt.dps.dtms.domain.enumeration.TaskStatus;
import fpt.dps.dtms.security.AuthoritiesConstants;
import fpt.dps.dtms.security.SecurityUtils;
import fpt.dps.dtms.service.PackagesQueryService;
import fpt.dps.dtms.service.PackagesService;
import fpt.dps.dtms.service.ProjectsQueryService;
import fpt.dps.dtms.service.ProjectsService;
import fpt.dps.dtms.service.dto.PackagesCriteria;
import fpt.dps.dtms.service.dto.PackagesDTO;
import fpt.dps.dtms.service.dto.ProjectsCriteria;
import fpt.dps.dtms.service.dto.ProjectsDTO;
import fpt.dps.dtms.service.dto.TasksDTO;
import fpt.dps.dtms.service.util.AppConstants;
import fpt.dps.dtms.web.rest.errors.BadRequestAlertException;
import fpt.dps.dtms.web.rest.errors.InternalServerErrorException;
import fpt.dps.dtms.web.rest.util.HeaderUtil;
import fpt.dps.dtms.web.rest.util.PaginationUtil;
import fpt.dps.dtms.web.rest.vm.FieldConfigVM;
import fpt.dps.dtms.web.rest.vm.TMSDynamicCustomFieldVM;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing Projects.
 */
@RestController
@RequestMapping("/api/external")
public class ExternalPackageResource {

	private final Logger log = LoggerFactory.getLogger(ExternalPackageResource.class);

	private static final String ENTITY_NAME = "package";

	private final PackagesService packagesService;
	
	private final PackagesQueryService packagesQueryService;

    public ExternalPackageResource(PackagesQueryService packagesQueryService, PackagesService packagesService ) {
        this.packagesQueryService = packagesQueryService;
        this.packagesService = packagesService;
	}
    
    /**
     * POST  /packages : Create a new packages.
     *
     * @param packagesDTO the packagesDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new packagesDTO, or with status 400 (Bad Request) if the packages has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/packages")
    @Timed
    @PreAuthorize("@jhiAuth.hasPermission('PACKAGE', 'CREATE')")
    public ResponseEntity<PackagesDTO> createPackages(@Valid @RequestBody PackagesDTO packagesDTO) throws URISyntaxException {
        log.debug("REST request to save Packages : {}", packagesDTO);
        if (packagesDTO.getId() != null) {
            throw new BadRequestAlertException("A new packages cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PackagesDTO result = packagesService.save(packagesDTO);
        return ResponseEntity.created(new URI("/api/packages/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /packages : Updates an existing packages.
     *
     * @param packagesDTO the packagesDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated packagesDTO,
     * or with status 400 (Bad Request) if the packagesDTO is not valid,
     * or with status 500 (Internal Server Error) if the packagesDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/packages")
    @Timed
    @PreAuthorize("@jhiAuth.hasPermission('PACKAGE', 'EDIT')")
    public ResponseEntity<PackagesDTO> updatePackages(@Valid @RequestBody PackagesDTO packagesDTO) throws URISyntaxException {
        log.debug("REST request to update Packages : {}", packagesDTO);
        if (packagesDTO.getId() == null) {
            return createPackages(packagesDTO);
        }
        PackagesDTO result = packagesService.save(packagesDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, packagesDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /packages : get all the packages.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of packages in body
     */
    @GetMapping("/packages")
    @Timed
    @PreAuthorize("@jhiAuth.hasPermission('PACKAGE', 'VIEW')")
    public ResponseEntity<List<PackagesDTO>> getAllPackages(PackagesCriteria criteria, 
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
    
    @GetMapping("/packages/fields/{id}")
    @Timed
    @PreAuthorize("@jhiAuth.hasPermission('PACKAGE', 'VIEW')")
    public ResponseEntity<List<FieldConfigVM>> getPackagesFieldConfig(@PathVariable Long id) {
        log.debug("REST request to get all Packages relate to Project : {}", id);
        List<FieldConfigVM> objects = packagesService.getPackageFieldConfig(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(objects));
    }
    
    @GetMapping("/packages/dynamic-fields/{id}")
    @Timed
    @PreAuthorize("@jhiAuth.hasPermission('PACKAGE', 'VIEW')")
    public ResponseEntity<TMSDynamicCustomFieldVM> getPackagesDynamicFieldConfig(@PathVariable Long id) {
        log.debug("REST request to get all Packages relate to Project : {}", id);
        TMSDynamicCustomFieldVM objects = packagesService.getPackageDynamicFieldConfig(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(objects));
    }
    
    /**
     * GET  /packages : get all the packages for selectbox.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of packages in body
     */
    /*@GetMapping("/packages-selects")
    @Timed
    public ResponseEntity<List<SelectDTO>> getAllPackagesSelects(PackagesCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Packages by criteria: {}", criteria);
        Page<SelectDTO> page = packagesQueryService.findByCriteriaSelect(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/packages-selects");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }*/

    /**
     * GET  /packages/:id : get the "id" packages.
     *
     * @param id the id of the packagesDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the packagesDTO, or with status 404 (Not Found)
     */
    @GetMapping("/packages/{id}")
    @Timed
    @PreAuthorize("@jhiAuth.hasPermission('PACKAGE', 'VIEW')")
    public ResponseEntity<PackagesDTO> getPackages(@PathVariable Long id) {
        log.debug("REST request to get Packages : {}", id);
        PackagesDTO packagesDTO = packagesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(packagesDTO));
    }
    
    /**
     * DELETE  /packages/:id : delete the "id" packages.
     *
     * @param id the id of the packagesDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/packages/{id}")
    @Timed
    @PreAuthorize("@jhiAuth.hasPermission('PACKAGE', 'DELETE')")
    public ResponseEntity<Void> deletePackages(@PathVariable Long id) {
        log.debug("REST request to delete Packages : {}", id);
        try {
        	packagesService.delete(id);
        	return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
        } catch(Exception e) {
        	return ResponseEntity.badRequest().headers(HeaderUtil.serviceFailureAlert(ENTITY_NAME, AppConstants.IN_USED, "A package cannot delete")).build();
        }
    }
    
    @GetMapping("/packages/allocation")
    @Timed
    public ResponseEntity<List<Map<String, Object>>> allocation(@RequestParam Long id, Pageable pageable) {
    	log.debug("REST getAllUsersForProjectUser:", id);
    	List<Map<String, Object>> result = packagesQueryService.getEffortByPakage(id, pageable);
    	int totalPage = Integer.parseInt(result.get(result.size() - 1).get("total").toString());
        result.remove(result.size() - 1);
    	Page<Map<String, Object>> page = new PageImpl<>(result, pageable, totalPage);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/packages/allocation");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * SEARCH  /_search/packages?query=:query : search for the packages corresponding
     * to the query.
     *
     * @param query the query of the packages search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/packages")
    @Timed
    @PreAuthorize("@jhiAuth.hasPermission('PACKAGE', 'VIEW')")
    public ResponseEntity<List<PackagesDTO>> searchPackages(@RequestParam Long purchaseOrderId, @RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Packages for query {}", query);
        Page<PackagesDTO> page = packagesService.search(purchaseOrderId, query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/packages");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    
    @GetMapping("/packages-search")
    @Timed
    @PreAuthorize("@jhiAuth.hasPermission('PACKAGE','VIEW')")
    public ResponseEntity<List<PackagesDTO>> searchPackagesByName(@RequestParam String packName, Pageable pageable) {
        log.debug("REST request to search for a page of Packages for query {}", packName);
        Page<PackagesDTO> page = packagesQueryService.searchPackagesByName(packName, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(packName, page, "/api/packages-search");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/packages/import", method = RequestMethod.POST,
    	    headers = {"content-type=application/octet-stream", "content-type=multipart/mixed","content-type=multipart/form-data", 
    	    		"content-type=application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",})
    @Timed
    @PreAuthorize("@jhiAuth.hasPermission('PACKAGE', 'CREATE')")
    public ResponseEntity<String> packageImport(@RequestParam("excelFile") MultipartFile file, @Valid @RequestPart(value = "poId", required = true) Long poID) {
    	log.info(poID.toString());
    	try {
    		packagesService.autoGenPackages_Tasks(file, poID);
    		return ResponseEntity.ok().headers(HeaderUtil.importAlert(ENTITY_NAME, poID.toString())).build();
		} catch (Exception e) {
			return ResponseEntity.badRequest().headers(HeaderUtil.serviceFailureAlert(ENTITY_NAME, AppConstants.IMPORT_FAILED, "Import was failed")).build();			
		}
	}
    
    /**
     * PUT  /packages/:id : get the "id" packages.
     *
     * @param id the id of the package delivery
     * @return the ResponseEntity with status 200 (OK) and with body the packagesDTO, or with status 404 (Not Found)
     */
    @PutMapping("/packages/{id}/delivery")
    @Timed
    @PreAuthorize("@jhiAuth.hasPermission('PACKAGE', 'EDIT')")
    public ResponseEntity<List<TasksDTO>> deliveryPackages(@PathVariable Long id) {
        log.debug("REST request to delivery tasks in Packages : {}", id);
        PackagesDTO packagesDTO = packagesService.findOne(id);
        if (packagesDTO != null) {
        	List<TasksDTO> listTasksDelivery =  packagesService.delivertyTasksInPackage(id);
        	return new ResponseEntity<>(listTasksDelivery, HttpStatus.OK);
        }
        return ResponseEntity.badRequest().headers(HeaderUtil.serviceFailureAlert(ENTITY_NAME, AppConstants.NOT_FOUND, "Package id is not found.")).build();
    }
    
    /**
     * GET  /packages : get all the packages.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of packages in body
     */
    @GetMapping("/packages/bidding")
    @Timed
    public ResponseEntity<List<PackagesDTO>> getListPackageBiddingTask(@RequestParam("purchaseOrderId") Long purchaseOrderId) {
        log.debug("REST request to get Packages by criteria: {}", purchaseOrderId);
        List<PackagesDTO> result = packagesQueryService.findListPackageBiddingTask(purchaseOrderId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}