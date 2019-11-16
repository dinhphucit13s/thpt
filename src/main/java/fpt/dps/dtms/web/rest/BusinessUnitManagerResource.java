package fpt.dps.dtms.web.rest;

import com.codahale.metrics.annotation.Timed;
import fpt.dps.dtms.service.BusinessUnitManagerService;
import fpt.dps.dtms.web.rest.errors.BadRequestAlertException;
import fpt.dps.dtms.web.rest.util.HeaderUtil;
import fpt.dps.dtms.web.rest.util.PaginationUtil;
import fpt.dps.dtms.service.dto.BusinessUnitManagerDTO;
import fpt.dps.dtms.service.dto.BusinessUnitManagerCriteria;
import fpt.dps.dtms.service.BusinessUnitManagerQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing BusinessUnitManager.
 */
@RestController
@RequestMapping("/api")
public class BusinessUnitManagerResource {

    private final Logger log = LoggerFactory.getLogger(BusinessUnitManagerResource.class);

    private static final String ENTITY_NAME = "businessUnitManager";

    private final BusinessUnitManagerService businessUnitManagerService;

    private final BusinessUnitManagerQueryService businessUnitManagerQueryService;

    public BusinessUnitManagerResource(BusinessUnitManagerService businessUnitManagerService, BusinessUnitManagerQueryService businessUnitManagerQueryService) {
        this.businessUnitManagerService = businessUnitManagerService;
        this.businessUnitManagerQueryService = businessUnitManagerQueryService;
    }

    /**
     * POST  /business-unit-managers : Create a new businessUnitManager.
     *
     * @param businessUnitManagerDTO the businessUnitManagerDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new businessUnitManagerDTO, or with status 400 (Bad Request) if the businessUnitManager has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/business-unit-managers")
    @Timed
    public ResponseEntity<BusinessUnitManagerDTO> createBusinessUnitManager(@Valid @RequestBody BusinessUnitManagerDTO businessUnitManagerDTO) throws URISyntaxException {
        log.debug("REST request to save BusinessUnitManager : {}", businessUnitManagerDTO);
        if (businessUnitManagerDTO.getId() != null) {
            throw new BadRequestAlertException("A new businessUnitManager cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BusinessUnitManagerDTO result = businessUnitManagerService.save(businessUnitManagerDTO);
        return ResponseEntity.created(new URI("/api/business-unit-managers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /business-unit-managers : Updates an existing businessUnitManager.
     *
     * @param businessUnitManagerDTO the businessUnitManagerDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated businessUnitManagerDTO,
     * or with status 400 (Bad Request) if the businessUnitManagerDTO is not valid,
     * or with status 500 (Internal Server Error) if the businessUnitManagerDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/business-unit-managers")
    @Timed
    public ResponseEntity<BusinessUnitManagerDTO> updateBusinessUnitManager(@Valid @RequestBody BusinessUnitManagerDTO businessUnitManagerDTO) throws URISyntaxException {
        log.debug("REST request to update BusinessUnitManager : {}", businessUnitManagerDTO);
        if (businessUnitManagerDTO.getId() == null) {
            return createBusinessUnitManager(businessUnitManagerDTO);
        }
        BusinessUnitManagerDTO result = businessUnitManagerService.save(businessUnitManagerDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, businessUnitManagerDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /business-unit-managers : get all the businessUnitManagers.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of businessUnitManagers in body
     */
    @GetMapping("/business-unit-managers")
    @Timed
    public ResponseEntity<List<BusinessUnitManagerDTO>> getAllBusinessUnitManagers(BusinessUnitManagerCriteria criteria, Pageable pageable) {
        log.debug("REST request to get BusinessUnitManagers by criteria: {}", criteria);
        Page<BusinessUnitManagerDTO> page = businessUnitManagerQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/business-unit-managers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /business-unit-managers/:id : get the "id" businessUnitManager.
     *
     * @param id the id of the businessUnitManagerDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the businessUnitManagerDTO, or with status 404 (Not Found)
     */
    @GetMapping("/business-unit-managers/{id}")
    @Timed
    public ResponseEntity<BusinessUnitManagerDTO> getBusinessUnitManager(@PathVariable Long id) {
        log.debug("REST request to get BusinessUnitManager : {}", id);
        BusinessUnitManagerDTO businessUnitManagerDTO = businessUnitManagerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(businessUnitManagerDTO));
    }

    /**
     * DELETE  /business-unit-managers/:id : delete the "id" businessUnitManager.
     *
     * @param id the id of the businessUnitManagerDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/business-unit-managers/{id}")
    @Timed
    public ResponseEntity<Void> deleteBusinessUnitManager(@PathVariable Long id) {
        log.debug("REST request to delete BusinessUnitManager : {}", id);
        businessUnitManagerService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/business-unit-managers?query=:query : search for the businessUnitManager corresponding
     * to the query.
     *
     * @param query the query of the businessUnitManager search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/business-unit-managers")
    @Timed
    public ResponseEntity<List<BusinessUnitManagerDTO>> searchBusinessUnitManagers(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of BusinessUnitManagers for query {}", query);
        Page<BusinessUnitManagerDTO> page = businessUnitManagerService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/business-unit-managers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
