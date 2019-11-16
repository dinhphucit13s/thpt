package fpt.dps.dtms.web.rest;

import com.codahale.metrics.annotation.Timed;

import fpt.dps.dtms.service.NotificationQueryService;
import fpt.dps.dtms.service.NotificationService;
import fpt.dps.dtms.web.rest.errors.BadRequestAlertException;
import fpt.dps.dtms.web.rest.util.HeaderUtil;
import fpt.dps.dtms.web.rest.util.PaginationUtil;
import fpt.dps.dtms.web.rest.vm.external.MultiNotificationVM;
import fpt.dps.dtms.service.dto.NotificationDTO;
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
import java.util.Set;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Notification.
 */
@RestController
@RequestMapping("/api")
public class NotificationResource {

    private final Logger log = LoggerFactory.getLogger(NotificationResource.class);

    private static final String ENTITY_NAME = "notification";

    private final NotificationService notificationService;
    
    private final NotificationQueryService notificationQueryService;

    public NotificationResource(NotificationService notificationService, NotificationQueryService notificationQueryService) {
        this.notificationService = notificationService;
        this.notificationQueryService = notificationQueryService;
    }

    /**
     * POST  /notifications : Create a new notification.
     *
     * @param notificationDTO the notificationDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new notificationDTO, or with status 400 (Bad Request) if the notification has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/notifications")
    @Timed
    public ResponseEntity<NotificationDTO> createNotification(@Valid @RequestBody NotificationDTO notificationDTO) throws URISyntaxException {
        log.debug("REST request to save Notification : {}", notificationDTO);
        if (notificationDTO.getId() != null) {
            throw new BadRequestAlertException("A new notification cannot already have an ID", ENTITY_NAME, "idexists");
        }
        NotificationDTO result = notificationService.save(notificationDTO);
        return ResponseEntity.created(new URI("/api/notifications/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /notifications : Updates an existing notification.
     *
     * @param notificationDTO the notificationDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated notificationDTO,
     * or with status 400 (Bad Request) if the notificationDTO is not valid,
     * or with status 500 (Internal Server Error) if the notificationDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/notifications")
    @Timed
    public ResponseEntity<NotificationDTO> updateNotification(@Valid @RequestBody NotificationDTO notificationDTO) throws URISyntaxException {
        log.debug("REST request to update Notification : {}", notificationDTO);
        if (notificationDTO.getId() == null) {
            return createNotification(notificationDTO);
        }
        NotificationDTO result = notificationService.save(notificationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, notificationDTO.getId().toString()))
            .body(result);
    }

    
	/**
	 * GET  /notifications/:userName : get list notification by user name.
	 *
	 * @param userName is the userName of user login
	 * @return the ResponseEntity with status 200 (OK) and with body the notificationsDTO list, or with status 404 (Not Found)
	 */
	@GetMapping("/notifications/{userName}/{modeViewNotify}")
	@Timed
	public ResponseEntity<List<NotificationDTO>> getNotificationListPM(@PathVariable("userName") String userName,
			@PathVariable("modeViewNotify") String modeViewNotify, Pageable pageable) {
		Page<NotificationDTO> notificationDtos = this.notificationQueryService.getNotificationList(userName, modeViewNotify, pageable);
	    log.debug("REST EXTERNAL request to get Notifications list by userName : {}", userName);
	    HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(notificationDtos, "/api/notifications/{userName}");
	    
	    return new ResponseEntity<>(notificationDtos.getContent(), headers, HttpStatus.OK);
	}

    @GetMapping("/notifications/{userLogin}/count-unread")
    @Timed
    public ResponseEntity<Integer> countNotificationUnreadOfUserLogin(@PathVariable(value = "userLogin") String userLogin) {
        log.debug("REST request to count Notification Unread of userLogin : {}", userLogin);
        Integer result = this.notificationQueryService.countNotificationUnread(userLogin);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(result));
    }
    
    /**
     * GET  /notifications/:id : get the "id" notification.
     *
     * @param id the id of the notificationDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the notificationDTO, or with status 404 (Not Found)
     */
    @GetMapping("/notifications/{id}")
    @Timed
    public ResponseEntity<NotificationDTO> getNotification(@PathVariable Long id) {
        log.debug("REST request to get Notification : {}", id);
        NotificationDTO notificationDTO = notificationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(notificationDTO));
    }

    /**
     * DELETE  /notifications/:id : delete the "id" notification.
     *
     * @param id the id of the notificationDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/notifications/{id}")
    @Timed
    public ResponseEntity<Void> deleteNotification(@PathVariable Long id) {
        log.debug("REST request to delete Notification : {}", id);
        notificationService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
    
    @PostMapping("/notifications/deleteMulti")
    @Timed
    public ResponseEntity<Void> deleteMultiNotification(@RequestBody MultiNotificationVM notificationPayload) {
        log.debug("REST EXTERNAL request to delete multi notification");
        Set<NotificationDTO> notificationDtoList = notificationPayload.getNotifications();
        this.notificationService.deleteMultiNotification(notificationDtoList);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, "delete multi")).build();
    }

    /**
     * SEARCH  /_search/notifications?query=:query : search for the notification corresponding
     * to the query.
     *
     * @param query the query of the notification search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/notifications")
    @Timed
    public ResponseEntity<List<NotificationDTO>> searchNotifications(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Notifications for query {}", query);
        Page<NotificationDTO> page = notificationService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/notifications");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
