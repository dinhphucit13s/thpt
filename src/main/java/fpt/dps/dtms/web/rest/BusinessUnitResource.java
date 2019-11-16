package fpt.dps.dtms.web.rest;

import com.codahale.metrics.annotation.Timed;
import fpt.dps.dtms.service.BusinessUnitService;
import fpt.dps.dtms.web.rest.errors.BadRequestAlertException;
import fpt.dps.dtms.web.rest.util.HeaderUtil;
import fpt.dps.dtms.web.rest.util.PaginationUtil;
import fpt.dps.dtms.service.dto.BusinessUnitDTO;
import fpt.dps.dtms.service.util.AppConstants;
import fpt.dps.dtms.service.dto.BusinessUnitCriteria;
import fpt.dps.dtms.service.BusinessUnitQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing BusinessUnit.
 */
@RestController
@RequestMapping("/api")
public class BusinessUnitResource {

    private final Logger log = LoggerFactory.getLogger(BusinessUnitResource.class);

    private static final String ENTITY_NAME = "businessUnit";

    private final BusinessUnitService businessUnitService;

    private final BusinessUnitQueryService businessUnitQueryService;

    public BusinessUnitResource(BusinessUnitService businessUnitService, BusinessUnitQueryService businessUnitQueryService) {
        this.businessUnitService = businessUnitService;
        this.businessUnitQueryService = businessUnitQueryService;
    }

    /**
     * POST  /business-units : Create a new businessUnit.
     *
     * @param businessUnitDTO the businessUnitDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new businessUnitDTO, or with status 400 (Bad Request) if the businessUnit has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/business-units")
    @Timed
    @PreAuthorize("@jhiAuth.hasPermission('BUSINESS_UNIT', 'CREATE')")
    public ResponseEntity<BusinessUnitDTO> createBusinessUnit(@Valid @RequestBody BusinessUnitDTO businessUnitDTO) throws URISyntaxException {
        log.debug("REST request to save BusinessUnit : {}", businessUnitDTO);
        if (businessUnitDTO.getId() != null) {
            throw new BadRequestAlertException("A new businessUnit cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BusinessUnitDTO result = businessUnitService.save(businessUnitDTO);
        return ResponseEntity.created(new URI("/api/business-units/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /business-units : Updates an existing businessUnit.
     *
     * @param businessUnitDTO the businessUnitDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated businessUnitDTO,
     * or with status 400 (Bad Request) if the businessUnitDTO is not valid,
     * or with status 500 (Internal Server Error) if the businessUnitDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/business-units")
    @Timed
    @PreAuthorize("@jhiAuth.hasPermission('BUSINESS_UNIT', 'EDIT')")
    public ResponseEntity<BusinessUnitDTO> updateBusinessUnit(@Valid @RequestBody BusinessUnitDTO businessUnitDTO) throws URISyntaxException {
        log.debug("REST request to update BusinessUnit : {}", businessUnitDTO);
        if (businessUnitDTO.getId() == null) {
            return createBusinessUnit(businessUnitDTO);
        }
        BusinessUnitDTO result = businessUnitService.save(businessUnitDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, businessUnitDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /business-units : get all the businessUnits.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of businessUnits in body
     */
    @GetMapping("/business-units")
    @Timed
    @PreAuthorize("@jhiAuth.hasPermission('BUSINESS_UNIT', 'VIEW')")
    public ResponseEntity<List<BusinessUnitDTO>> getAllBusinessUnits(BusinessUnitCriteria criteria, Pageable pageable) {
        log.debug("REST request to get BusinessUnits by criteria: {}", criteria);
        Page<BusinessUnitDTO> page = businessUnitQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/business-units");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /business-units/:id : get the "id" businessUnit.
     *
     * @param id the id of the businessUnitDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the businessUnitDTO, or with status 404 (Not Found)
     */
    @GetMapping("/business-units/{id}")
    @Timed
    @PreAuthorize("@jhiAuth.hasPermission('BUSINESS_UNIT', 'VIEW')")
    public ResponseEntity<BusinessUnitDTO> getBusinessUnit(@PathVariable Long id) {
        log.debug("REST request to get BusinessUnit : {}", id);
        BusinessUnitDTO businessUnitDTO = businessUnitService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(businessUnitDTO));
    }

    /**
     * DELETE  /business-units/:id : delete the "id" businessUnit.
     *
     * @param id the id of the businessUnitDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/business-units/{id}")
    @Timed
    @PreAuthorize("@jhiAuth.hasPermission('BUSINESS_UNIT', 'DELETE')")
    public ResponseEntity<Void> deleteBusinessUnit(@PathVariable Long id) {
        log.debug("REST request to delete BusinessUnit : {}", id);
        try {
        	businessUnitService.delete(id);
        	return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
        } catch(Exception e) {
        	return ResponseEntity.badRequest().headers(HeaderUtil.serviceFailureAlert(ENTITY_NAME, AppConstants.IN_USED, "A business unit cannot delete")).build();
        }
    }

    /**
     * SEARCH  /_search/business-units?query=:query : search for the businessUnit corresponding
     * to the query.
     *
     * @param query the query of the businessUnit search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/business-units")
    @Timed
    @PreAuthorize("@jhiAuth.hasPermission('BUSINESS_UNIT', 'VIEW')")
    public ResponseEntity<List<BusinessUnitDTO>> searchBusinessUnits(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of BusinessUnits for query {}", query);
        Page<BusinessUnitDTO> page = businessUnitService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/business-units");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
