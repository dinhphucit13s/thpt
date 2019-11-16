package fpt.dps.dtms.web.rest;

import com.codahale.metrics.annotation.Timed;
import fpt.dps.dtms.service.LoginTrackingService;
import fpt.dps.dtms.web.rest.errors.BadRequestAlertException;
import fpt.dps.dtms.web.rest.util.HeaderUtil;
import fpt.dps.dtms.web.rest.util.PaginationUtil;
import fpt.dps.dtms.service.dto.LoginTrackingDTO;
import fpt.dps.dtms.service.dto.LoginTrackingCriteria;
import fpt.dps.dtms.service.LoginTrackingQueryService;
import io.github.jhipster.web.util.ResponseUtil;

import org.joda.time.convert.Converter;
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
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import javax.persistence.Convert;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing LoginTracking.
 */
@RestController
@RequestMapping("/api")
public class LoginTrackingResource {

    private final Logger log = LoggerFactory.getLogger(LoginTrackingResource.class);

    private static final String ENTITY_NAME = "loginTracking";

    private final LoginTrackingService loginTrackingService;

    private final LoginTrackingQueryService loginTrackingQueryService;

    public LoginTrackingResource(LoginTrackingService loginTrackingService, LoginTrackingQueryService loginTrackingQueryService) {
        this.loginTrackingService = loginTrackingService;
        this.loginTrackingQueryService = loginTrackingQueryService;
    }

    /**
     * POST  /login-trackings : Create a new loginTracking.
     *
     * @param loginTrackingDTO the loginTrackingDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new loginTrackingDTO, or with status 400 (Bad Request) if the loginTracking has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/login-trackings")
    @Timed
    public ResponseEntity<LoginTrackingDTO> createLoginTracking(@RequestBody LoginTrackingDTO loginTrackingDTO) throws URISyntaxException {
        log.debug("REST request to save LoginTracking : {}", loginTrackingDTO);
        if (loginTrackingDTO.getId() != null) {
            throw new BadRequestAlertException("A new loginTracking cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LoginTrackingDTO result = loginTrackingService.save(loginTrackingDTO);
        return ResponseEntity.created(new URI("/api/login-trackings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /login-trackings : Updates an existing loginTracking.
     *
     * @param loginTrackingDTO the loginTrackingDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated loginTrackingDTO,
     * or with status 400 (Bad Request) if the loginTrackingDTO is not valid,
     * or with status 500 (Internal Server Error) if the loginTrackingDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/login-trackings")
    @Timed
    public ResponseEntity<LoginTrackingDTO> updateLoginTracking(@RequestBody LoginTrackingDTO loginTrackingDTO) throws URISyntaxException {
        log.debug("REST request to update LoginTracking : {}", loginTrackingDTO);
        if (loginTrackingDTO.getId() == null) {
            return createLoginTracking(loginTrackingDTO);
        }
        LoginTrackingDTO result = loginTrackingService.save(loginTrackingDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, loginTrackingDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /login-trackings : get all the loginTrackings.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of loginTrackings in body
     */
    @GetMapping("/login-trackings")
    @Timed
    public ResponseEntity<List<LoginTrackingDTO>> getAllLoginTrackings(LoginTrackingCriteria criteria, Pageable pageable) {
        log.debug("REST request to get LoginTrackings by criteria: {}", criteria);
        Page<LoginTrackingDTO> page = loginTrackingQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/login-trackings");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /login-trackings/:id : get the "id" loginTracking.
     *
     * @param id the id of the loginTrackingDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the loginTrackingDTO, or with status 404 (Not Found)
     */
    @GetMapping("/login-trackings/{id}")
    @Timed
    public ResponseEntity<LoginTrackingDTO> getLoginTracking(@PathVariable Long id) {
        log.debug("REST request to get LoginTracking : {}", id);
        LoginTrackingDTO loginTrackingDTO = loginTrackingService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(loginTrackingDTO));
    }

    /**
     * DELETE  /login-trackings/:id : delete the "id" loginTracking.
     *
     * @param id the id of the loginTrackingDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/login-trackings/{id}")
    @Timed
    public ResponseEntity<Void> deleteLoginTracking(@PathVariable Long id) {
        log.debug("REST request to delete LoginTracking : {}", id);
        loginTrackingService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/login-trackings?query=:query : search for the loginTracking corresponding
     * to the query.
     *
     * @param query the query of the loginTracking search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/login-trackings")
    @Timed
    public ResponseEntity<List<LoginTrackingDTO>> searchLoginTrackings(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of LoginTrackings for query {}", query);
        Page<LoginTrackingDTO> page = loginTrackingService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/login-trackings");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
