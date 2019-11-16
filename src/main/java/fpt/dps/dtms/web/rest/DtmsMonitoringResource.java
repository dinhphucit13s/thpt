package fpt.dps.dtms.web.rest;

import com.codahale.metrics.annotation.Timed;
import fpt.dps.dtms.service.DtmsMonitoringService;
import fpt.dps.dtms.web.rest.errors.BadRequestAlertException;
import fpt.dps.dtms.web.rest.util.HeaderUtil;
import fpt.dps.dtms.web.rest.util.PaginationUtil;
import fpt.dps.dtms.service.dto.DtmsMonitoringDTO;
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
 * REST controller for managing DtmsMonitoring.
 */
@RestController
@RequestMapping("/api")
public class DtmsMonitoringResource {

    private final Logger log = LoggerFactory.getLogger(DtmsMonitoringResource.class);

    private static final String ENTITY_NAME = "dtmsMonitoring";

    private final DtmsMonitoringService dtmsMonitoringService;

    public DtmsMonitoringResource(DtmsMonitoringService dtmsMonitoringService) {
        this.dtmsMonitoringService = dtmsMonitoringService;
    }

    /**
     * POST  /dtms-monitorings : Create a new dtmsMonitoring.
     *
     * @param dtmsMonitoringDTO the dtmsMonitoringDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new dtmsMonitoringDTO, or with status 400 (Bad Request) if the dtmsMonitoring has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/dtms-monitorings")
    @Timed
    public ResponseEntity<DtmsMonitoringDTO> createDtmsMonitoring(@Valid @RequestBody DtmsMonitoringDTO dtmsMonitoringDTO) throws URISyntaxException {
        log.debug("REST request to save DtmsMonitoring : {}", dtmsMonitoringDTO);
        if (dtmsMonitoringDTO.getId() != null) {
            throw new BadRequestAlertException("A new dtmsMonitoring cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DtmsMonitoringDTO result = dtmsMonitoringService.save(dtmsMonitoringDTO);
        return ResponseEntity.created(new URI("/api/dtms-monitorings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /dtms-monitorings : Updates an existing dtmsMonitoring.
     *
     * @param dtmsMonitoringDTO the dtmsMonitoringDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated dtmsMonitoringDTO,
     * or with status 400 (Bad Request) if the dtmsMonitoringDTO is not valid,
     * or with status 500 (Internal Server Error) if the dtmsMonitoringDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/dtms-monitorings")
    @Timed
    public ResponseEntity<DtmsMonitoringDTO> updateDtmsMonitoring(@Valid @RequestBody DtmsMonitoringDTO dtmsMonitoringDTO) throws URISyntaxException {
        log.debug("REST request to update DtmsMonitoring : {}", dtmsMonitoringDTO);
        if (dtmsMonitoringDTO.getId() == null) {
            return createDtmsMonitoring(dtmsMonitoringDTO);
        }
        DtmsMonitoringDTO result = dtmsMonitoringService.save(dtmsMonitoringDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, dtmsMonitoringDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /dtms-monitorings : get all the dtmsMonitorings.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of dtmsMonitorings in body
     */
    @GetMapping("/dtms-monitorings")
    @Timed
    public ResponseEntity<List<DtmsMonitoringDTO>> getAllDtmsMonitorings(Pageable pageable) {
        log.debug("REST request to get a page of DtmsMonitorings");
        Page<DtmsMonitoringDTO> page = dtmsMonitoringService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/dtms-monitorings");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /dtms-monitorings/:id : get the "id" dtmsMonitoring.
     *
     * @param id the id of the dtmsMonitoringDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the dtmsMonitoringDTO, or with status 404 (Not Found)
     */
    @GetMapping("/dtms-monitorings/{id}")
    @Timed
    public ResponseEntity<DtmsMonitoringDTO> getDtmsMonitoring(@PathVariable Long id) {
        log.debug("REST request to get DtmsMonitoring : {}", id);
        DtmsMonitoringDTO dtmsMonitoringDTO = dtmsMonitoringService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(dtmsMonitoringDTO));
    }

    /**
     * DELETE  /dtms-monitorings/:id : delete the "id" dtmsMonitoring.
     *
     * @param id the id of the dtmsMonitoringDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/dtms-monitorings/{id}")
    @Timed
    public ResponseEntity<Void> deleteDtmsMonitoring(@PathVariable Long id) {
        log.debug("REST request to delete DtmsMonitoring : {}", id);
        dtmsMonitoringService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/dtms-monitorings?query=:query : search for the dtmsMonitoring corresponding
     * to the query.
     *
     * @param query the query of the dtmsMonitoring search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/dtms-monitorings")
    @Timed
    public ResponseEntity<List<DtmsMonitoringDTO>> searchDtmsMonitorings(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of DtmsMonitorings for query {}", query);
        Page<DtmsMonitoringDTO> page = dtmsMonitoringService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/dtms-monitorings");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
