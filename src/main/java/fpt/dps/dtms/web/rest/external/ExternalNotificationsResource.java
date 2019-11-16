package fpt.dps.dtms.web.rest.external;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

import fpt.dps.dtms.service.NotificationQueryService;
import fpt.dps.dtms.service.NotificationService;
import fpt.dps.dtms.service.dto.NotificationDTO;
import fpt.dps.dtms.web.rest.util.HeaderUtil;
import fpt.dps.dtms.web.rest.util.PaginationUtil;
import fpt.dps.dtms.web.rest.vm.external.MultiNotificationVM;

/**
 * REST EXTERNAL controller for managing Notifications.
 */
@RestController
@RequestMapping("/api/external")
public class ExternalNotificationsResource {
    private final Logger log = LoggerFactory.getLogger(ExternalNotesResource.class);

    private static final String ENTITY_NAME = "notifications";
    
    private final NotificationService notificationService;
    
    private final NotificationQueryService notificationQueryService;

	public ExternalNotificationsResource(NotificationService notificationService, NotificationQueryService notificationQueryService) {
		this.notificationService = notificationService;
		this.notificationQueryService = notificationQueryService;
	}
    
    /**
     * GET  /notifications/:userName : get list notification by user name.
     *
     * @param userName is the userName of user login
     * @return the ResponseEntity with status 200 (OK) and with body the notificationsDTO list, or with status 404 (Not Found)
     */
    @GetMapping("/notifications/{userName}/{modeViewNotify}")
    @Timed
    public ResponseEntity<List<NotificationDTO>> getNotificationList(@PathVariable("userName") String userName,
    		@PathVariable("modeViewNotify") String modeViewNotify, Pageable pageable) {
    	log.debug("REST EXTERNAL request to get Notifications list by userName : {}", userName);
    	Page<NotificationDTO> notificationDtos = this.notificationQueryService.getNotificationList(userName, modeViewNotify, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(notificationDtos, "/api/external/notifications/{userName}");
        
        return new ResponseEntity<>(notificationDtos.getContent(), headers, HttpStatus.OK);
    }
    
    /**
     * DELETE  /notifications/:id : delete the "id" notifications.
     *
     * @param id the id of the notificationsDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/notifications/{id}")
    @Timed
    public ResponseEntity<Void> deleteNotification(@PathVariable("id") Long id) {
        log.debug("REST EXTERNAL request to delete Notification : {}", id);
        this.notificationService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
    
    @PutMapping("/notifications")
    @Timed
    public ResponseEntity<NotificationDTO> updateNotification(@RequestBody NotificationDTO notificationPayload) {
        log.debug("REST EXTERNAL request to update notification : {}", notificationPayload);
        NotificationDTO notificationDto = this.notificationService.save(notificationPayload);
        return ResponseEntity.ok().body(notificationDto);
    }
    
    @PostMapping("/notifications/deleteMulti")
    @Timed
    public ResponseEntity<Void> deleteMultiNotification(@RequestBody MultiNotificationVM notificationPayload) {
        log.debug("REST EXTERNAL request to delete multi notification");
        Set<NotificationDTO> notificationDtoList = notificationPayload.getNotifications();
        this.notificationService.deleteMultiNotification(notificationDtoList);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, "delete multi")).build();
    }
}
