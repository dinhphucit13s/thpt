package fpt.dps.dtms.web.rest;

import com.codahale.metrics.annotation.Timed;
import fpt.dps.dtms.service.TMSCustomFieldService;
import fpt.dps.dtms.web.rest.errors.BadRequestAlertException;
import fpt.dps.dtms.web.rest.util.HeaderUtil;
import fpt.dps.dtms.web.rest.util.PaginationUtil;
import fpt.dps.dtms.service.dto.TMSCustomFieldDTO;
import fpt.dps.dtms.service.util.AppConstants;
import fpt.dps.dtms.service.dto.TMSCustomFieldCriteria;
import fpt.dps.dtms.service.TMSCustomFieldQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing TMSCustomField.
 */
@RestController
@RequestMapping("/api")
public class TMSCustomFieldResource {

    private final Logger log = LoggerFactory.getLogger(TMSCustomFieldResource.class);

    private static final String ENTITY_NAME = "tMSCustomField";

    private final TMSCustomFieldService tMSCustomFieldService;

    private final TMSCustomFieldQueryService tMSCustomFieldQueryService;

    public TMSCustomFieldResource(TMSCustomFieldService tMSCustomFieldService, TMSCustomFieldQueryService tMSCustomFieldQueryService) {
        this.tMSCustomFieldService = tMSCustomFieldService;
        this.tMSCustomFieldQueryService = tMSCustomFieldQueryService;
    }

    /**
     * POST  /tms-custom-fields : Create a new tMSCustomField.
     *
     * @param tMSCustomFieldDTO the tMSCustomFieldDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new tMSCustomFieldDTO, or with status 400 (Bad Request) if the tMSCustomField has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/tms-custom-fields")
    @Timed
    public ResponseEntity<TMSCustomFieldDTO> createTMSCustomField(@RequestBody TMSCustomFieldDTO tMSCustomFieldDTO) throws URISyntaxException {
        log.debug("REST request to save TMSCustomField : {}", tMSCustomFieldDTO);
        if (tMSCustomFieldDTO.getId() != null) {
            throw new BadRequestAlertException("A new tMSCustomField cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TMSCustomFieldDTO result = tMSCustomFieldService.save(tMSCustomFieldDTO);
        return ResponseEntity.created(new URI("/api/tms-custom-fields/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /tms-custom-fields : Updates an existing tMSCustomField.
     *
     * @param tMSCustomFieldDTO the tMSCustomFieldDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated tMSCustomFieldDTO,
     * or with status 400 (Bad Request) if the tMSCustomFieldDTO is not valid,
     * or with status 500 (Internal Server Error) if the tMSCustomFieldDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/tms-custom-fields")
    @Timed
    public ResponseEntity<TMSCustomFieldDTO> updateTMSCustomField(@RequestBody TMSCustomFieldDTO tMSCustomFieldDTO) throws URISyntaxException {
        log.debug("REST request to update TMSCustomField : {}", tMSCustomFieldDTO);
        if (tMSCustomFieldDTO.getId() == null) {
            return createTMSCustomField(tMSCustomFieldDTO);
        }
        TMSCustomFieldDTO result = tMSCustomFieldService.save(tMSCustomFieldDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, tMSCustomFieldDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /tms-custom-fields : get all the tMSCustomFields.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of tMSCustomFields in body
     */
    @GetMapping("/tms-custom-fields")
    @Timed
    public ResponseEntity<List<TMSCustomFieldDTO>> getAllTMSCustomFields(TMSCustomFieldCriteria criteria, Pageable pageable) {
        log.debug("REST request to get TMSCustomFields by criteria: {}", criteria);
        Page<TMSCustomFieldDTO> page = tMSCustomFieldQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/tms-custom-fields");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /tms-custom-fields/:id : get the "id" tMSCustomField.
     *
     * @param id the id of the tMSCustomFieldDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the tMSCustomFieldDTO, or with status 404 (Not Found)
     */
    @GetMapping("/tms-custom-fields/{id}")
    @Timed
    public ResponseEntity<TMSCustomFieldDTO> getTMSCustomField(@PathVariable Long id) {
        log.debug("REST request to get TMSCustomField : {}", id);
        TMSCustomFieldDTO tMSCustomFieldDTO = tMSCustomFieldService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(tMSCustomFieldDTO));
    }

    @GetMapping("/tms-custom-fields-name-exists")
    @Timed
    public ResponseEntity<Boolean> checkNameExists(@RequestParam String query) {
        log.debug("REST request to get TMSCustomField : {}", query);
        Boolean tMSCustomFieldDTO = tMSCustomFieldQueryService.checkNameExists(query);
        return new ResponseEntity<>(tMSCustomFieldDTO, HttpStatus.OK);
    }
    
    @GetMapping("/tms-custom-fields-search")
    @Timed
    public ResponseEntity<List<TMSCustomFieldDTO>> getAllCustomFieldByQuerySearch(@RequestParam String query) {
        log.debug("REST request to get TMSCustomField : {}", query);
        List<TMSCustomFieldDTO> tMSCustomFieldDTO = tMSCustomFieldQueryService.getAllCustomFieldByQuerySearch(query);
        return new ResponseEntity<>(tMSCustomFieldDTO, HttpStatus.OK);
    }

    /**
     * DELETE  /tms-custom-fields/:id : delete the "id" tMSCustomField.
     *
     * @param id the id of the tMSCustomFieldDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/tms-custom-fields/{id}")
    @Timed
    public ResponseEntity<Void> deleteTMSCustomField(@PathVariable Long id) {
        log.debug("REST request to delete TMSCustomField : {}", id);
        try {
        	tMSCustomFieldService.delete(id);
        	return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
        } catch(Exception e) {
        	return ResponseEntity.badRequest().headers(HeaderUtil.serviceFailureAlert(ENTITY_NAME, AppConstants.IN_USED, "A TMS Custom Field cannot delete")).build();
        }
    }

    /**
     * SEARCH  /_search/tms-custom-fields?query=:query : search for the tMSCustomField corresponding
     * to the query.
     *
     * @param query the query of the tMSCustomField search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/tms-custom-fields")
    @Timed
    public ResponseEntity<List<TMSCustomFieldDTO>> searchTMSCustomFields(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of TMSCustomFields for query {}", query);
        Page<TMSCustomFieldDTO> page = tMSCustomFieldService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/tms-custom-fields");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
