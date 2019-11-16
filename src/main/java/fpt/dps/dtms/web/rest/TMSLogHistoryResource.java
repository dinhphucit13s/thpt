package fpt.dps.dtms.web.rest;

import com.codahale.metrics.annotation.Timed;
import fpt.dps.dtms.service.TMSLogHistoryService;
import fpt.dps.dtms.web.rest.errors.BadRequestAlertException;
import fpt.dps.dtms.web.rest.util.HeaderUtil;
import fpt.dps.dtms.web.rest.util.PaginationUtil;
import fpt.dps.dtms.service.dto.TMSLogHistoryDTO;
import fpt.dps.dtms.service.dto.TMSLogHistoryCriteria;
import fpt.dps.dtms.security.AuthoritiesConstants;
import fpt.dps.dtms.service.TMSLogHistoryQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing TMSLogHistory.
 */
@RestController
@RequestMapping("/api")
public class TMSLogHistoryResource {

    private final Logger log = LoggerFactory.getLogger(TMSLogHistoryResource.class);

    private static final String ENTITY_NAME = "tMSLogHistory";

    private final TMSLogHistoryService tMSLogHistoryService;

    private final TMSLogHistoryQueryService tMSLogHistoryQueryService;

    public TMSLogHistoryResource(TMSLogHistoryService tMSLogHistoryService, TMSLogHistoryQueryService tMSLogHistoryQueryService) {
        this.tMSLogHistoryService = tMSLogHistoryService;
        this.tMSLogHistoryQueryService = tMSLogHistoryQueryService;
    }

    /**
     * POST  /tms-log-histories : Create a new tMSLogHistory.
     *
     * @param tMSLogHistoryDTO the tMSLogHistoryDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new tMSLogHistoryDTO, or with status 400 (Bad Request) if the tMSLogHistory has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/tms-log-histories")
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<TMSLogHistoryDTO> createTMSLogHistory(@Valid @RequestBody TMSLogHistoryDTO tMSLogHistoryDTO) throws URISyntaxException {
        log.debug("REST request to save TMSLogHistory : {}", tMSLogHistoryDTO);
        if (tMSLogHistoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new tMSLogHistory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TMSLogHistoryDTO result = tMSLogHistoryService.save(tMSLogHistoryDTO);
        return ResponseEntity.created(new URI("/api/tms-log-histories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /tms-log-histories : Updates an existing tMSLogHistory.
     *
     * @param tMSLogHistoryDTO the tMSLogHistoryDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated tMSLogHistoryDTO,
     * or with status 400 (Bad Request) if the tMSLogHistoryDTO is not valid,
     * or with status 500 (Internal Server Error) if the tMSLogHistoryDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/tms-log-histories")
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<TMSLogHistoryDTO> updateTMSLogHistory(@Valid @RequestBody TMSLogHistoryDTO tMSLogHistoryDTO) throws URISyntaxException {
        log.debug("REST request to update TMSLogHistory : {}", tMSLogHistoryDTO);
        if (tMSLogHistoryDTO.getId() == null) {
            return createTMSLogHistory(tMSLogHistoryDTO);
        }
        TMSLogHistoryDTO result = tMSLogHistoryService.save(tMSLogHistoryDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, tMSLogHistoryDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /tms-log-histories : get all the tMSLogHistories.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of tMSLogHistories in body
     */
    @GetMapping("/tms-log-histories")
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<List<TMSLogHistoryDTO>> getAllTMSLogHistories(TMSLogHistoryCriteria criteria, Pageable pageable) {
        log.debug("REST request to get TMSLogHistories by criteria: {}", criteria);
        Page<TMSLogHistoryDTO> page = tMSLogHistoryQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/tms-log-histories");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /tms-log-histories/:id : get the "id" tMSLogHistory.
     *
     * @param id the id of the tMSLogHistoryDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the tMSLogHistoryDTO, or with status 404 (Not Found)
     */
    @GetMapping("/tms-log-histories/{id}")
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<TMSLogHistoryDTO> getTMSLogHistory(@PathVariable Long id) {
        log.debug("REST request to get TMSLogHistory : {}", id);
        TMSLogHistoryDTO tMSLogHistoryDTO = tMSLogHistoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(tMSLogHistoryDTO));
    }

    /**
     * DELETE  /tms-log-histories/:id : delete the "id" tMSLogHistory.
     *
     * @param id the id of the tMSLogHistoryDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/tms-log-histories/{id}")
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<Void> deleteTMSLogHistory(@PathVariable Long id) {
        log.debug("REST request to delete TMSLogHistory : {}", id);
        tMSLogHistoryService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/tms-log-histories?query=:query : search for the tMSLogHistory corresponding
     * to the query.
     *
     * @param query the query of the tMSLogHistory search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/tms-log-histories")
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<List<TMSLogHistoryDTO>> searchTMSLogHistories(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of TMSLogHistories for query {}", query);
        Page<TMSLogHistoryDTO> page = tMSLogHistoryService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/tms-log-histories");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
