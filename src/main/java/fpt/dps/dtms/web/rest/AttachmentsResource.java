package fpt.dps.dtms.web.rest;

import com.codahale.metrics.annotation.Timed;
import fpt.dps.dtms.service.AttachmentsService;
import fpt.dps.dtms.web.rest.errors.BadRequestAlertException;
import fpt.dps.dtms.web.rest.util.HeaderUtil;
import fpt.dps.dtms.web.rest.util.PaginationUtil;
import fpt.dps.dtms.service.dto.AttachmentsDTO;
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
 * REST controller for managing Attachments.
 */
@RestController
@RequestMapping("/api")
public class AttachmentsResource {

    private final Logger log = LoggerFactory.getLogger(AttachmentsResource.class);

    private static final String ENTITY_NAME = "attachments";

    private final AttachmentsService attachmentsService;

    public AttachmentsResource(AttachmentsService attachmentsService) {
        this.attachmentsService = attachmentsService;
    }

    /**
     * POST  /attachments : Create a new attachments.
     *
     * @param attachmentsDTO the attachmentsDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new attachmentsDTO, or with status 400 (Bad Request) if the attachments has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/attachments")
    @Timed
    public ResponseEntity<AttachmentsDTO> createAttachments(@Valid @RequestBody AttachmentsDTO attachmentsDTO) throws URISyntaxException {
        log.debug("REST request to save Attachments : {}", attachmentsDTO);
        if (attachmentsDTO.getId() != null) {
            throw new BadRequestAlertException("A new attachments cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AttachmentsDTO result = attachmentsService.save(attachmentsDTO);
        return ResponseEntity.created(new URI("/api/attachments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /attachments : Updates an existing attachments.
     *
     * @param attachmentsDTO the attachmentsDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated attachmentsDTO,
     * or with status 400 (Bad Request) if the attachmentsDTO is not valid,
     * or with status 500 (Internal Server Error) if the attachmentsDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/attachments")
    @Timed
    public ResponseEntity<AttachmentsDTO> updateAttachments(@Valid @RequestBody AttachmentsDTO attachmentsDTO) throws URISyntaxException {
        log.debug("REST request to update Attachments : {}", attachmentsDTO);
        if (attachmentsDTO.getId() == null) {
            return createAttachments(attachmentsDTO);
        }
        AttachmentsDTO result = attachmentsService.save(attachmentsDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, attachmentsDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /attachments : get all the attachments.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of attachments in body
     */
    @GetMapping("/attachments")
    @Timed
    public ResponseEntity<List<AttachmentsDTO>> getAllAttachments(Pageable pageable) {
        log.debug("REST request to get a page of Attachments");
        Page<AttachmentsDTO> page = attachmentsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/attachments");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /attachments/:id : get the "id" attachments.
     *
     * @param id the id of the attachmentsDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the attachmentsDTO, or with status 404 (Not Found)
     */
    @GetMapping("/attachments/{id}")
    @Timed
    public ResponseEntity<AttachmentsDTO> getAttachments(@PathVariable Long id) {
        log.debug("REST request to get Attachments : {}", id);
        AttachmentsDTO attachmentsDTO = attachmentsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(attachmentsDTO));
    }

    /**
     * DELETE  /attachments/:id : delete the "id" attachments.
     *
     * @param id the id of the attachmentsDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/attachments/{id}")
    @Timed
    public ResponseEntity<Void> deleteAttachments(@PathVariable Long id) {
        log.debug("REST request to delete Attachments : {}", id);
        attachmentsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/attachments?query=:query : search for the attachments corresponding
     * to the query.
     *
     * @param query the query of the attachments search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/attachments")
    @Timed
    public ResponseEntity<List<AttachmentsDTO>> searchAttachments(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Attachments for query {}", query);
        Page<AttachmentsDTO> page = attachmentsService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/attachments");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
