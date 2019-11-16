package fpt.dps.dtms.web.rest;

import com.codahale.metrics.annotation.Timed;
import fpt.dps.dtms.service.AuthorityResourceService;
import fpt.dps.dtms.web.rest.errors.BadRequestAlertException;
import fpt.dps.dtms.web.rest.util.HeaderUtil;
import fpt.dps.dtms.web.rest.util.PaginationUtil;
import fpt.dps.dtms.service.dto.AuthorityResourceDTO;
import fpt.dps.dtms.service.dto.AuthorityResourceCriteria;
import fpt.dps.dtms.security.AuthoritiesConstants;
import fpt.dps.dtms.security.AuthorityResourceConstants;
import fpt.dps.dtms.service.AuthorityResourceQueryService;
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
 * REST controller for managing AuthorityResource.
 */
@RestController
@RequestMapping("/api")
public class AuthorityResourceResource {

    private final Logger log = LoggerFactory.getLogger(AuthorityResourceResource.class);

    private static final String ENTITY_NAME = "authorityResource";

    private final AuthorityResourceService authorityResourceService;

    private final AuthorityResourceQueryService authorityResourceQueryService;

    public AuthorityResourceResource(AuthorityResourceService authorityResourceService, AuthorityResourceQueryService authorityResourceQueryService) {
        this.authorityResourceService = authorityResourceService;
        this.authorityResourceQueryService = authorityResourceQueryService;
    }

    /**
     * POST  /authority-resources : Create a new authorityResource.
     *
     * @param authorityResourceDTO the authorityResourceDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new authorityResourceDTO, or with status 400 (Bad Request) if the authorityResource has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/authority-resources")
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<AuthorityResourceDTO> createAuthorityResource(@Valid @RequestBody AuthorityResourceDTO authorityResourceDTO) throws URISyntaxException {
        log.debug("REST request to save AuthorityResource : {}", authorityResourceDTO);
        if (authorityResourceDTO.getId() != null) {
            throw new BadRequestAlertException("A new authorityResource cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AuthorityResourceDTO result = authorityResourceService.save(authorityResourceDTO);
        return ResponseEntity.created(new URI("/api/authority-resources/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /authority-resources : Updates an existing authorityResource.
     *
     * @param authorityResourceDTO the authorityResourceDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated authorityResourceDTO,
     * or with status 400 (Bad Request) if the authorityResourceDTO is not valid,
     * or with status 500 (Internal Server Error) if the authorityResourceDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/authority-resources")
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<AuthorityResourceDTO> updateAuthorityResource(@Valid @RequestBody AuthorityResourceDTO authorityResourceDTO) throws URISyntaxException {
        log.debug("REST request to update AuthorityResource : {}", authorityResourceDTO);
        if (authorityResourceDTO.getId() == null) {
            return createAuthorityResource(authorityResourceDTO);
        }
        AuthorityResourceDTO result = authorityResourceService.save(authorityResourceDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, authorityResourceDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /authority-resources : get all the authorityResources.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of authorityResources in body
     */
    @GetMapping("/authority-resources")
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<List<AuthorityResourceDTO>> getAllAuthorityResources(AuthorityResourceCriteria criteria, Pageable pageable) {
        log.debug("REST request to get AuthorityResources by criteria: {}", criteria);
        Page<AuthorityResourceDTO> page = authorityResourceQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/authority-resources");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /authority-resources/:id : get the "id" authorityResource.
     *
     * @param id the id of the authorityResourceDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the authorityResourceDTO, or with status 404 (Not Found)
     */
    @GetMapping("/authority-resources/{id}")
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<AuthorityResourceDTO> getAuthorityResource(@PathVariable Long id) {
        log.debug("REST request to get AuthorityResource : {}", id);
        AuthorityResourceDTO authorityResourceDTO = authorityResourceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(authorityResourceDTO));
    }

    /**
     * DELETE  /authority-resources/:id : delete the "id" authorityResource.
     *
     * @param id the id of the authorityResourceDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/authority-resources/{id}")
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<Void> deleteAuthorityResource(@PathVariable Long id) {
        log.debug("REST request to delete AuthorityResource : {}", id);
        authorityResourceService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/authority-resources?query=:query : search for the authorityResource corresponding
     * to the query.
     *
     * @param query the query of the authorityResource search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/authority-resources")
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<List<AuthorityResourceDTO>> searchAuthorityResources(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of AuthorityResources for query {}", query);
        Page<AuthorityResourceDTO> page = authorityResourceService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/authority-resources");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    
    @GetMapping("/authority-groups")
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<String[]> getAllAuthorityResources() {
        log.debug("REST request to get all Authority Groups");
        String[] resources = AuthorityResourceConstants.AUTHORITY_RESOURCES;
        return new ResponseEntity<>(resources, HttpStatus.OK);
    }

}
