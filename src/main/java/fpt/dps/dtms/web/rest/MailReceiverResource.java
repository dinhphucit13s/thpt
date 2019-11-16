package fpt.dps.dtms.web.rest;

import com.codahale.metrics.annotation.Timed;
import fpt.dps.dtms.service.MailReceiverService;
import fpt.dps.dtms.web.rest.errors.BadRequestAlertException;
import fpt.dps.dtms.web.rest.util.HeaderUtil;
import fpt.dps.dtms.web.rest.util.PaginationUtil;
import fpt.dps.dtms.service.dto.MailReceiverDTO;
import fpt.dps.dtms.service.dto.MailReceiverCriteria;
import fpt.dps.dtms.service.MailReceiverQueryService;
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
 * REST controller for managing MailReceiver.
 */
@RestController
@RequestMapping("/api")
public class MailReceiverResource {

    private final Logger log = LoggerFactory.getLogger(MailReceiverResource.class);

    private static final String ENTITY_NAME = "mailReceiver";

    private final MailReceiverService mailReceiverService;

    private final MailReceiverQueryService mailReceiverQueryService;

    public MailReceiverResource(MailReceiverService mailReceiverService, MailReceiverQueryService mailReceiverQueryService) {
        this.mailReceiverService = mailReceiverService;
        this.mailReceiverQueryService = mailReceiverQueryService;
    }

    /**
     * POST  /mail-receivers : Create a new mailReceiver.
     *
     * @param mailReceiverDTO the mailReceiverDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new mailReceiverDTO, or with status 400 (Bad Request) if the mailReceiver has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/mail-receivers")
    @Timed
    public ResponseEntity<MailReceiverDTO> createMailReceiver(@Valid @RequestBody MailReceiverDTO mailReceiverDTO) throws URISyntaxException {
        log.debug("REST request to save MailReceiver : {}", mailReceiverDTO);
        if (mailReceiverDTO.getId() != null) {
            throw new BadRequestAlertException("A new mailReceiver cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MailReceiverDTO result = mailReceiverService.save(mailReceiverDTO);
        return ResponseEntity.created(new URI("/api/mail-receivers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /mail-receivers : Updates an existing mailReceiver.
     *
     * @param mailReceiverDTO the mailReceiverDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated mailReceiverDTO,
     * or with status 400 (Bad Request) if the mailReceiverDTO is not valid,
     * or with status 500 (Internal Server Error) if the mailReceiverDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/mail-receivers")
    @Timed
    public ResponseEntity<MailReceiverDTO> updateMailReceiver(@Valid @RequestBody MailReceiverDTO mailReceiverDTO) throws URISyntaxException {
        log.debug("REST request to update MailReceiver : {}", mailReceiverDTO);
        if (mailReceiverDTO.getId() == null) {
            return createMailReceiver(mailReceiverDTO);
        }
        MailReceiverDTO result = mailReceiverService.save(mailReceiverDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, mailReceiverDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /mail-receivers : get all the mailReceivers.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of mailReceivers in body
     */
    @GetMapping("/mail-receivers")
    @Timed
    public ResponseEntity<List<MailReceiverDTO>> getAllMailReceivers(MailReceiverCriteria criteria, Pageable pageable) {
        log.debug("REST request to get MailReceivers by criteria: {}", criteria);
        Page<MailReceiverDTO> page = mailReceiverQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/mail-receivers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /mail-receivers/:id : get the "id" mailReceiver.
     *
     * @param id the id of the mailReceiverDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the mailReceiverDTO, or with status 404 (Not Found)
     */
    @GetMapping("/mail-receivers/{id}")
    @Timed
    public ResponseEntity<MailReceiverDTO> getMailReceiver(@PathVariable Long id) {
        log.debug("REST request to get MailReceiver : {}", id);
        MailReceiverDTO mailReceiverDTO = mailReceiverService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(mailReceiverDTO));
    }

    /**
     * DELETE  /mail-receivers/:id : delete the "id" mailReceiver.
     *
     * @param id the id of the mailReceiverDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/mail-receivers/{id}")
    @Timed
    public ResponseEntity<Void> deleteMailReceiver(@PathVariable Long id) {
        log.debug("REST request to delete MailReceiver : {}", id);
        mailReceiverService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/mail-receivers?query=:query : search for the mailReceiver corresponding
     * to the query.
     *
     * @param query the query of the mailReceiver search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/mail-receivers")
    @Timed
    public ResponseEntity<List<MailReceiverDTO>> searchMailReceivers(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of MailReceivers for query {}", query);
        Page<MailReceiverDTO> page = mailReceiverService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/mail-receivers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
