package fpt.dps.dtms.web.rest;

import com.codahale.metrics.annotation.Timed;
import fpt.dps.dtms.service.TMSCustomFieldScreenValueService;
import fpt.dps.dtms.web.rest.errors.BadRequestAlertException;
import fpt.dps.dtms.web.rest.util.HeaderUtil;
import fpt.dps.dtms.web.rest.util.PaginationUtil;
import fpt.dps.dtms.service.dto.TMSCustomFieldScreenValueDTO;
import fpt.dps.dtms.service.dto.TMSCustomFieldScreenValueCriteria;
import fpt.dps.dtms.service.TMSCustomFieldScreenValueQueryService;
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
 * REST controller for managing TMSCustomFieldScreenValue.
 */
@RestController
@RequestMapping("/api")
public class TMSCustomFieldScreenValueResource {

    private final Logger log = LoggerFactory.getLogger(TMSCustomFieldScreenValueResource.class);

    private static final String ENTITY_NAME = "tMSCustomFieldScreenValue";

    private final TMSCustomFieldScreenValueService tMSCustomFieldScreenValueService;

    private final TMSCustomFieldScreenValueQueryService tMSCustomFieldScreenValueQueryService;

    public TMSCustomFieldScreenValueResource(TMSCustomFieldScreenValueService tMSCustomFieldScreenValueService, TMSCustomFieldScreenValueQueryService tMSCustomFieldScreenValueQueryService) {
        this.tMSCustomFieldScreenValueService = tMSCustomFieldScreenValueService;
        this.tMSCustomFieldScreenValueQueryService = tMSCustomFieldScreenValueQueryService;
    }

    /**
     * POST  /tms-custom-field-screen-values : Create a new tMSCustomFieldScreenValue.
     *
     * @param tMSCustomFieldScreenValueDTO the tMSCustomFieldScreenValueDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new tMSCustomFieldScreenValueDTO, or with status 400 (Bad Request) if the tMSCustomFieldScreenValue has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/tms-custom-field-screen-values")
    @Timed
    @PreAuthorize("@jhiAuth.hasPermission('TMS_CUSTOMER_FIELD', 'CREATE')")
    public ResponseEntity<TMSCustomFieldScreenValueDTO> createTMSCustomFieldScreenValue(@Valid @RequestBody TMSCustomFieldScreenValueDTO tMSCustomFieldScreenValueDTO) throws URISyntaxException {
        log.debug("REST request to save TMSCustomFieldScreenValue : {}", tMSCustomFieldScreenValueDTO);
        if (tMSCustomFieldScreenValueDTO.getId() != null) {
            throw new BadRequestAlertException("A new tMSCustomFieldScreenValue cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TMSCustomFieldScreenValueDTO result = tMSCustomFieldScreenValueService.save(tMSCustomFieldScreenValueDTO);
        return ResponseEntity.created(new URI("/api/tms-custom-field-screen-values/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /tms-custom-field-screen-values : Updates an existing tMSCustomFieldScreenValue.
     *
     * @param tMSCustomFieldScreenValueDTO the tMSCustomFieldScreenValueDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated tMSCustomFieldScreenValueDTO,
     * or with status 400 (Bad Request) if the tMSCustomFieldScreenValueDTO is not valid,
     * or with status 500 (Internal Server Error) if the tMSCustomFieldScreenValueDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/tms-custom-field-screen-values")
    @Timed
    @PreAuthorize("@jhiAuth.hasPermission('TMS_CUSTOMER_FIELD', 'EDIT')")
    public ResponseEntity<TMSCustomFieldScreenValueDTO> updateTMSCustomFieldScreenValue(@Valid @RequestBody TMSCustomFieldScreenValueDTO tMSCustomFieldScreenValueDTO) throws URISyntaxException {
        log.debug("REST request to update TMSCustomFieldScreenValue : {}", tMSCustomFieldScreenValueDTO);
        if (tMSCustomFieldScreenValueDTO.getId() == null) {
            return createTMSCustomFieldScreenValue(tMSCustomFieldScreenValueDTO);
        }
        TMSCustomFieldScreenValueDTO result = tMSCustomFieldScreenValueService.save(tMSCustomFieldScreenValueDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, tMSCustomFieldScreenValueDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /tms-custom-field-screen-values : get all the tMSCustomFieldScreenValues.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of tMSCustomFieldScreenValues in body
     */
    @GetMapping("/tms-custom-field-screen-values")
    @Timed
    @PreAuthorize("@jhiAuth.hasPermission('TMS_CUSTOMER_FIELD', 'VIEW')")
    public ResponseEntity<List<TMSCustomFieldScreenValueDTO>> getAllTMSCustomFieldScreenValues(TMSCustomFieldScreenValueCriteria criteria, Pageable pageable) {
        log.debug("REST request to get TMSCustomFieldScreenValues by criteria: {}", criteria);
        Page<TMSCustomFieldScreenValueDTO> page = tMSCustomFieldScreenValueQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/tms-custom-field-screen-values");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /tms-custom-field-screen-values/:id : get the "id" tMSCustomFieldScreenValue.
     *
     * @param id the id of the tMSCustomFieldScreenValueDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the tMSCustomFieldScreenValueDTO, or with status 404 (Not Found)
     */
    @GetMapping("/tms-custom-field-screen-values/{id}")
    @Timed
    @PreAuthorize("@jhiAuth.hasPermission('TMS_CUSTOMER_FIELD', 'VIEW')")
    public ResponseEntity<TMSCustomFieldScreenValueDTO> getTMSCustomFieldScreenValue(@PathVariable Long id) {
        log.debug("REST request to get TMSCustomFieldScreenValue : {}", id);
        TMSCustomFieldScreenValueDTO tMSCustomFieldScreenValueDTO = tMSCustomFieldScreenValueService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(tMSCustomFieldScreenValueDTO));
    }

    /**
     * DELETE  /tms-custom-field-screen-values/:id : delete the "id" tMSCustomFieldScreenValue.
     *
     * @param id the id of the tMSCustomFieldScreenValueDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/tms-custom-field-screen-values/{id}")
    @Timed
    @PreAuthorize("@jhiAuth.hasPermission('TMS_CUSTOMER_FIELD', 'DELETE')")
    public ResponseEntity<Void> deleteTMSCustomFieldScreenValue(@PathVariable Long id) {
        log.debug("REST request to delete TMSCustomFieldScreenValue : {}", id);
        tMSCustomFieldScreenValueService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/tms-custom-field-screen-values?query=:query : search for the tMSCustomFieldScreenValue corresponding
     * to the query.
     *
     * @param query the query of the tMSCustomFieldScreenValue search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/tms-custom-field-screen-values")
    @Timed
    @PreAuthorize("@jhiAuth.hasPermission('TMS_CUSTOMER_FIELD', 'VIEW')")
    public ResponseEntity<List<TMSCustomFieldScreenValueDTO>> searchTMSCustomFieldScreenValues(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of TMSCustomFieldScreenValues for query {}", query);
        Page<TMSCustomFieldScreenValueDTO> page = tMSCustomFieldScreenValueService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/tms-custom-field-screen-values");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
