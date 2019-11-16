package fpt.dps.dtms.web.rest;

import com.codahale.metrics.annotation.Timed;
import fpt.dps.dtms.service.BusinessLineService;
import fpt.dps.dtms.web.rest.errors.BadRequestAlertException;
import fpt.dps.dtms.web.rest.util.HeaderUtil;
import fpt.dps.dtms.web.rest.util.PaginationUtil;
import fpt.dps.dtms.service.dto.BusinessLineDTO;
import fpt.dps.dtms.service.util.AppConstants;
import fpt.dps.dtms.service.dto.BusinessLineCriteria;
import fpt.dps.dtms.service.BusinessLineQueryService;
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
 * REST controller for managing BusinessLine.
 */
@RestController
@RequestMapping("/api")
public class BusinessLineResource {

    private final Logger log = LoggerFactory.getLogger(BusinessLineResource.class);

    private static final String ENTITY_NAME = "businessLine";

    private final BusinessLineService businessLineService;

    private final BusinessLineQueryService businessLineQueryService;

    public BusinessLineResource(BusinessLineService businessLineService, BusinessLineQueryService businessLineQueryService) {
        this.businessLineService = businessLineService;
        this.businessLineQueryService = businessLineQueryService;
    }

    /**
     * POST  /business-lines : Create a new businessLine.
     *
     * @param businessLineDTO the businessLineDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new businessLineDTO, or with status 400 (Bad Request) if the businessLine has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/business-lines")
    @Timed
    @PreAuthorize("@jhiAuth.hasPermission('BUSSINESS_LINE', 'CREATE')")
    public ResponseEntity<BusinessLineDTO> createBusinessLine(@Valid @RequestBody BusinessLineDTO businessLineDTO) throws URISyntaxException {
        log.debug("REST request to save BusinessLine : {}", businessLineDTO);
        if (businessLineDTO.getId() != null) {
            throw new BadRequestAlertException("A new businessLine cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BusinessLineDTO result = businessLineService.save(businessLineDTO);
        return ResponseEntity.created(new URI("/api/business-lines/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /business-lines : Updates an existing businessLine.
     *
     * @param businessLineDTO the businessLineDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated businessLineDTO,
     * or with status 400 (Bad Request) if the businessLineDTO is not valid,
     * or with status 500 (Internal Server Error) if the businessLineDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/business-lines")
    @Timed
    @PreAuthorize("@jhiAuth.hasPermission('BUSSINESS_LINE', 'EDIT')")
    public ResponseEntity<BusinessLineDTO> updateBusinessLine(@Valid @RequestBody BusinessLineDTO businessLineDTO) throws URISyntaxException {
        log.debug("REST request to update BusinessLine : {}", businessLineDTO);
        if (businessLineDTO.getId() == null) {
            return createBusinessLine(businessLineDTO);
        }
        BusinessLineDTO result = businessLineService.save(businessLineDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, businessLineDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /business-lines : get all the businessLines.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of businessLines in body
     */
    @GetMapping("/business-lines")
    @Timed
    @PreAuthorize("@jhiAuth.hasPermission('BUSSINESS_LINE', 'VIEW')")
    public ResponseEntity<List<BusinessLineDTO>> getAllBusinessLines(BusinessLineCriteria criteria, Pageable pageable) {
        log.debug("REST request to get BusinessLines by criteria: {}", criteria);
        Page<BusinessLineDTO> page = businessLineQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/business-lines");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /business-lines/:id : get the "id" businessLine.
     *
     * @param id the id of the businessLineDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the businessLineDTO, or with status 404 (Not Found)
     */
    @GetMapping("/business-lines/{id}")
    @Timed
    @PreAuthorize("@jhiAuth.hasPermission('BUSSINESS_LINE', 'VIEW')")
    public ResponseEntity<BusinessLineDTO> getBusinessLine(@PathVariable Long id) {
        log.debug("REST request to get BusinessLine : {}", id);
        BusinessLineDTO businessLineDTO = businessLineService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(businessLineDTO));
    }

    /**
     * DELETE  /business-lines/:id : delete the "id" businessLine.
     *
     * @param id the id of the businessLineDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/business-lines/{id}")
    @Timed
    @PreAuthorize("@jhiAuth.hasPermission('BUSSINESS_LINE', 'DELETE')")
    public ResponseEntity<Void> deleteBusinessLine(@PathVariable Long id) {
        log.debug("REST request to delete BusinessLine : {}", id);
        try {
        	businessLineService.delete(id);
        	return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();        	
        } catch(Exception e) {
        	return ResponseEntity.badRequest().headers(HeaderUtil.serviceFailureAlert(ENTITY_NAME, AppConstants.IN_USED, "A business line cannot delete")).build();
        }
    }

    /**
     * SEARCH  /_search/business-lines?query=:query : search for the businessLine corresponding
     * to the query.
     *
     * @param query the query of the businessLine search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/business-lines")
    @Timed
    @PreAuthorize("@jhiAuth.hasPermission('BUSSINESS_LINE', 'VIEW')")
    public ResponseEntity<List<BusinessLineDTO>> searchBusinessLines(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of BusinessLines for query {}", query);
        Page<BusinessLineDTO> page = businessLineService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/business-lines");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
