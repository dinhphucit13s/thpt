package fpt.dps.dtms.web.rest;

import com.codahale.metrics.annotation.Timed;
import fpt.dps.dtms.service.NotificationTemplateService;
import fpt.dps.dtms.web.rest.errors.BadRequestAlertException;
import fpt.dps.dtms.web.rest.util.HeaderUtil;
import fpt.dps.dtms.web.rest.util.PaginationUtil;
import fpt.dps.dtms.service.dto.NotificationTemplateDTO;
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
 * REST controller for managing NotificationTemplate.
 */
@RestController
@RequestMapping("/api")
public class NotificationTemplateResource {

    private final Logger log = LoggerFactory.getLogger(NotificationTemplateResource.class);

    private static final String ENTITY_NAME = "notificationTemplate";

    private final NotificationTemplateService notificationTemplateService;

    public NotificationTemplateResource(NotificationTemplateService notificationTemplateService) {
        this.notificationTemplateService = notificationTemplateService;
    }

    /**
     * POST  /notification-templates : Create a new notificationTemplate.
     *
     * @param notificationTemplateDTO the notificationTemplateDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new notificationTemplateDTO, or with status 400 (Bad Request) if the notificationTemplate has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/notification-templates")
    @Timed
    public ResponseEntity<NotificationTemplateDTO> createNotificationTemplate(@Valid @RequestBody NotificationTemplateDTO notificationTemplateDTO) throws URISyntaxException {
        log.debug("REST request to save NotificationTemplate : {}", notificationTemplateDTO);
        if (notificationTemplateDTO.getId() != null) {
            throw new BadRequestAlertException("A new notificationTemplate cannot already have an ID", ENTITY_NAME, "idexists");
        }
        NotificationTemplateDTO result = notificationTemplateService.save(notificationTemplateDTO);
        return ResponseEntity.created(new URI("/api/notification-templates/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /notification-templates : Updates an existing notificationTemplate.
     *
     * @param notificationTemplateDTO the notificationTemplateDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated notificationTemplateDTO,
     * or with status 400 (Bad Request) if the notificationTemplateDTO is not valid,
     * or with status 500 (Internal Server Error) if the notificationTemplateDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/notification-templates")
    @Timed
    public ResponseEntity<NotificationTemplateDTO> updateNotificationTemplate(@Valid @RequestBody NotificationTemplateDTO notificationTemplateDTO) throws URISyntaxException {
        log.debug("REST request to update NotificationTemplate : {}", notificationTemplateDTO);
        if (notificationTemplateDTO.getId() == null) {
            return createNotificationTemplate(notificationTemplateDTO);
        }
        NotificationTemplateDTO result = notificationTemplateService.save(notificationTemplateDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, notificationTemplateDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /notification-templates : get all the notificationTemplates.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of notificationTemplates in body
     */
    @GetMapping("/notification-templates")
    @Timed
    public ResponseEntity<List<NotificationTemplateDTO>> getAllNotificationTemplates(Pageable pageable) {
        log.debug("REST request to get a page of NotificationTemplates");
        Page<NotificationTemplateDTO> page = notificationTemplateService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/notification-templates");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /notification-templates/:id : get the "id" notificationTemplate.
     *
     * @param id the id of the notificationTemplateDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the notificationTemplateDTO, or with status 404 (Not Found)
     */
    @GetMapping("/notification-templates/{id}")
    @Timed
    public ResponseEntity<NotificationTemplateDTO> getNotificationTemplate(@PathVariable Long id) {
        log.debug("REST request to get NotificationTemplate : {}", id);
        NotificationTemplateDTO notificationTemplateDTO = notificationTemplateService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(notificationTemplateDTO));
    }

    /**
     * DELETE  /notification-templates/:id : delete the "id" notificationTemplate.
     *
     * @param id the id of the notificationTemplateDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/notification-templates/{id}")
    @Timed
    public ResponseEntity<Void> deleteNotificationTemplate(@PathVariable Long id) {
        log.debug("REST request to delete NotificationTemplate : {}", id);
        notificationTemplateService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/notification-templates?query=:query : search for the notificationTemplate corresponding
     * to the query.
     *
     * @param query the query of the notificationTemplate search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/notification-templates")
    @Timed
    public ResponseEntity<List<NotificationTemplateDTO>> searchNotificationTemplates(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of NotificationTemplates for query {}", query);
        Page<NotificationTemplateDTO> page = notificationTemplateService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/notification-templates");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
