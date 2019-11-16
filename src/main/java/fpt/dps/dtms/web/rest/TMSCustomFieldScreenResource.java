package fpt.dps.dtms.web.rest;

import com.codahale.metrics.annotation.Timed;
import fpt.dps.dtms.service.TMSCustomFieldScreenService;
import fpt.dps.dtms.web.rest.errors.BadRequestAlertException;
import fpt.dps.dtms.web.rest.util.HeaderUtil;
import fpt.dps.dtms.web.rest.util.PaginationUtil;
import fpt.dps.dtms.service.dto.TMSCustomFieldScreenDTO;
import fpt.dps.dtms.service.dto.TMSCustomFieldScreenCriteria;
import fpt.dps.dtms.service.TMSCustomFieldScreenQueryService;
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
 * REST controller for managing TMSCustomFieldScreen.
 */
@RestController
@RequestMapping("/api")
public class TMSCustomFieldScreenResource {

    private final Logger log = LoggerFactory.getLogger(TMSCustomFieldScreenResource.class);

    private static final String ENTITY_NAME = "tMSCustomFieldScreen";

    private final TMSCustomFieldScreenService tMSCustomFieldScreenService;

    private final TMSCustomFieldScreenQueryService tMSCustomFieldScreenQueryService;

    public TMSCustomFieldScreenResource(TMSCustomFieldScreenService tMSCustomFieldScreenService, TMSCustomFieldScreenQueryService tMSCustomFieldScreenQueryService) {
        this.tMSCustomFieldScreenService = tMSCustomFieldScreenService;
        this.tMSCustomFieldScreenQueryService = tMSCustomFieldScreenQueryService;
    }

    /**
     * POST  /tms-custom-field-screens : Create a new tMSCustomFieldScreen.
     *
     * @param tMSCustomFieldScreenDTO the tMSCustomFieldScreenDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new tMSCustomFieldScreenDTO, or with status 400 (Bad Request) if the tMSCustomFieldScreen has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/tms-custom-field-screens")
    @Timed
    @PreAuthorize("@jhiAuth.hasPermission('TMS_CUSTOMER_FIELD', 'CREATE')")
    public ResponseEntity<TMSCustomFieldScreenDTO> createTMSCustomFieldScreen(@Valid @RequestBody TMSCustomFieldScreenDTO tMSCustomFieldScreenDTO) throws URISyntaxException {
        log.debug("REST request to save TMSCustomFieldScreen : {}", tMSCustomFieldScreenDTO);
        if (tMSCustomFieldScreenDTO.getId() != null) {
            throw new BadRequestAlertException("A new tMSCustomFieldScreen cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TMSCustomFieldScreenDTO result = tMSCustomFieldScreenService.save(tMSCustomFieldScreenDTO);
        return ResponseEntity.created(new URI("/api/tms-custom-field-screens/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /tms-custom-field-screens : Updates an existing tMSCustomFieldScreen.
     *
     * @param tMSCustomFieldScreenDTO the tMSCustomFieldScreenDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated tMSCustomFieldScreenDTO,
     * or with status 400 (Bad Request) if the tMSCustomFieldScreenDTO is not valid,
     * or with status 500 (Internal Server Error) if the tMSCustomFieldScreenDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/tms-custom-field-screens")
    @Timed
    @PreAuthorize("@jhiAuth.hasPermission('TMS_CUSTOMER_FIELD', 'EDIT')")
    public ResponseEntity<TMSCustomFieldScreenDTO> updateTMSCustomFieldScreen(@Valid @RequestBody TMSCustomFieldScreenDTO tMSCustomFieldScreenDTO) throws URISyntaxException {
        log.debug("REST request to update TMSCustomFieldScreen : {}", tMSCustomFieldScreenDTO);
        if (tMSCustomFieldScreenDTO.getId() == null) {
            return createTMSCustomFieldScreen(tMSCustomFieldScreenDTO);
        }
        TMSCustomFieldScreenDTO result = tMSCustomFieldScreenService.save(tMSCustomFieldScreenDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, tMSCustomFieldScreenDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /tms-custom-field-screens : get all the tMSCustomFieldScreens.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of tMSCustomFieldScreens in body
     */
    @GetMapping("/tms-custom-field-screens")
    @Timed
    @PreAuthorize("@jhiAuth.hasPermission('TMS_CUSTOMER_FIELD', 'VIEW')")
    public ResponseEntity<List<TMSCustomFieldScreenDTO>> getAllTMSCustomFieldScreens(TMSCustomFieldScreenCriteria criteria, Pageable pageable) {
        log.debug("REST request to get TMSCustomFieldScreens by criteria: {}", criteria);
        Page<TMSCustomFieldScreenDTO> page = tMSCustomFieldScreenQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/tms-custom-field-screens");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /tms-custom-field-screens/:id : get the "id" tMSCustomFieldScreen.
     *
     * @param id the id of the tMSCustomFieldScreenDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the tMSCustomFieldScreenDTO, or with status 404 (Not Found)
     */
    @GetMapping("/tms-custom-field-screens/{id}")
    @Timed
    @PreAuthorize("@jhiAuth.hasPermission('TMS_CUSTOMER_FIELD', 'VIEW')")
    public ResponseEntity<TMSCustomFieldScreenDTO> getTMSCustomFieldScreen(@PathVariable Long id) {
        log.debug("REST request to get TMSCustomFieldScreen : {}", id);
        TMSCustomFieldScreenDTO tMSCustomFieldScreenDTO = tMSCustomFieldScreenService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(tMSCustomFieldScreenDTO));
    }
    
    /**
     * get TMSCustomFieldScreen By custom field id and workflow id.
     * @param id
     * @param workflowId
     * @return
     */
    @GetMapping("/tms-custom-field-screens/{id}/{workflowId}")
    @Timed
    public ResponseEntity<TMSCustomFieldScreenDTO> findTMSCustomFieldScreenByWFIdAndCustomId(@PathVariable Long id, @PathVariable Long workflowId) {
        log.debug("REST request to get TMSCustomFieldScreen : {}", id);
        TMSCustomFieldScreenDTO tMSCustomFieldScreenDTO = tMSCustomFieldScreenQueryService.getCustomFieldByWorkflowId(id, workflowId);
        //return ResponseUtil.wrapOrNotFound(Optional.ofNullable(tMSCustomFieldScreenDTO));
        return new ResponseEntity<>(tMSCustomFieldScreenDTO, HttpStatus.OK);
    }

    /**
     * DELETE  /tms-custom-field-screens/:id : delete the "id" tMSCustomFieldScreen.
     *
     * @param id the id of the tMSCustomFieldScreenDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/tms-custom-field-screens/{id}")
    @Timed
    @PreAuthorize("@jhiAuth.hasPermission('TMS_CUSTOMER_FIELD', 'DELETE')")
    public ResponseEntity<Void> deleteTMSCustomFieldScreen(@PathVariable Long id) {
        log.debug("REST request to delete TMSCustomFieldScreen : {}", id);
        tMSCustomFieldScreenService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/tms-custom-field-screens?query=:query : search for the tMSCustomFieldScreen corresponding
     * to the query.
     *
     * @param query the query of the tMSCustomFieldScreen search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/tms-custom-field-screens")
    @Timed
    @PreAuthorize("@jhiAuth.hasPermission('TMS_CUSTOMER_FIELD', 'VIEW')")
    public ResponseEntity<List<TMSCustomFieldScreenDTO>> searchTMSCustomFieldScreens(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of TMSCustomFieldScreens for query {}", query);
        Page<TMSCustomFieldScreenDTO> page = tMSCustomFieldScreenService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/tms-custom-field-screens");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
