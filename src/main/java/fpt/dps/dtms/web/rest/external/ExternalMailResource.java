package fpt.dps.dtms.web.rest.external;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

import fpt.dps.dtms.service.StorageService;
import fpt.dps.dtms.service.AttachmentsService;
import fpt.dps.dtms.service.DtmsMailService;
import fpt.dps.dtms.service.MailQueryService;
import fpt.dps.dtms.service.MailReceiverQueryService;
import fpt.dps.dtms.service.MailReceiverService;
import fpt.dps.dtms.service.MailService;
import fpt.dps.dtms.service.NotificationService;
import fpt.dps.dtms.service.dto.AttachmentsDTO;
import fpt.dps.dtms.service.dto.MailDTO;
import fpt.dps.dtms.service.dto.MailReceiverDTO;
import fpt.dps.dtms.service.dto.NotesCriteria;
import fpt.dps.dtms.service.dto.NotificationDTO;
import fpt.dps.dtms.web.rest.util.HeaderUtil;
import fpt.dps.dtms.web.rest.util.PaginationUtil;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST EXTERNAL controller for managing Notifications.
 */
@RestController
@RequestMapping("/api/external")
public class ExternalMailResource {
    private final Logger log = LoggerFactory.getLogger(ExternalNotesResource.class);

    private static final String ENTITY_NAME = "mail";
    
private final DtmsMailService mailService;
    
    private final MailQueryService mailQueryService;
    
    private final MailReceiverService mailReceiverService;
    
    private final AttachmentsService attachmentsService;
    
    private final StorageService storageService;
    
    private final MailReceiverQueryService mailReceiverQueryService;

	public ExternalMailResource(DtmsMailService mailService, MailQueryService mailQueryService, MailReceiverQueryService mailReceiverQueryService, MailReceiverService mailReceiverService, StorageService storageService, AttachmentsService attachmentsService) {
		this.mailService = mailService;
		this.mailQueryService = mailQueryService;
		this.mailReceiverQueryService = mailReceiverQueryService;
		this.mailReceiverService = mailReceiverService;
		this.storageService = storageService;
		this.attachmentsService = attachmentsService;
	}
    
    /**
     * GET  /mail/:userName : get list mail by user name.
     *
     * @param userName is the userName of user login
     * @return the ResponseEntity with status 200 (OK) and with body the notificationsDTO list, or with status 404 (Not Found)
     */
    @GetMapping("/mail/{userName}")
    @Timed
    public ResponseEntity<List<Map<String, Object>>> getMailList(@PathVariable("userName") String userName, Pageable pageable) {
        log.debug("REST EXTERNAL request to get Notifications list by userName : {}", userName);
        List<Map<String, Object>> result = mailQueryService.getAllMailByUserLogin(userName, pageable);
        int totalPage = Integer.parseInt(result.get(result.size() - 1).get("total").toString());
        result.remove(result.size() - 1);
    	Page<Map<String, Object>> page = new PageImpl<>(result, pageable, totalPage);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/mail/{userName}");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    
    /**
     * GET  /mail/:userName : get list mail unread by user name.
     *
     * @param userName is the userName of user login
     * @return the ResponseEntity with status 200 (OK) and with body the notificationsDTO list, or with status 404 (Not Found)
     */
    @GetMapping("/mail/unread/{userName}")
    @Timed
    public ResponseEntity<List<Map<String, Object>>> getUnreadMailList(@PathVariable("userName") String userName, Pageable pageable) {
        log.debug("REST EXTERNAL request to get unread mail list by userName : {}", userName);
        List<Map<String, Object>> result = mailQueryService.getAllMailUnreadByUserLogin(userName, pageable);
        int totalPage = Integer.parseInt(result.get(result.size() - 1).get("total").toString());
        result.remove(result.size() - 1);
    	Page<Map<String, Object>> page = new PageImpl<>(result, pageable, totalPage);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/mail/{userName}");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    
    /**
     * GET  /mail/:userLogin/count-inbox: count mail inbox of user
     *
     * @param userLogin 
     * @return the ResponseEntity with status 200 (OK) and with body the number of mail, or with status 404 (Not Found)
     */
    @GetMapping("/mail/{userName}/count-inbox")
    @Timed
    public ResponseEntity<Integer> countMailInboxOfUserLogin(@PathVariable String userName) {
        log.debug("REST EXTERNAL request to count mail inbox of: {}", userName);
        Integer result = mailQueryService.countAllMailInboxByUserLogin(userName);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(result));
    }
    
    /**
     * GET  /mail/:userLogin/count-inbox-unread: count mail unread of user
     *
     * @param userLogin 
     * @return the ResponseEntity with status 200 (OK) and with body the number of mail, or with status 404 (Not Found)
     */
    @GetMapping("/mail/{userName}/count-inbox-unread")
    @Timed
    public ResponseEntity<Integer> countMailUnreadOfUserLogin(@PathVariable String userName) {
        log.debug("REST EXTERNAL request to count mail unread of: {}", userName);
        Integer result = mailQueryService.countAllMailUnseenByUserLogin(userName);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(result));
    }
    
    /**
     * GET  /mail/:id/:userName : get list mail by user name and id mail.
     *
     * @param userName is the userName of user login
     * @return the ResponseEntity with status 200 (OK) and with body the notificationsDTO list, or with status 404 (Not Found)
     */
    @GetMapping("/mail/{id}/{userName}")
    @Timed
    public ResponseEntity<Boolean> setStatusMailReceiver(@PathVariable("id") Long id,
    		@PathVariable("userName") String userName) {
        log.debug("Set status mail by userName : {}", userName);
        Boolean result = mailReceiverService.setStatusMailReceiver(userName, id);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, id.toString()))
                .body(result);
    }
    
    /**
     * GET  /mail/:id : get the "id" mail.
     *
     * @param id the id of the mailDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the mailDTO, or with status 404 (Not Found)
     */
    @GetMapping("/mail/find/{id}/{userName}")
    @Timed
    public ResponseEntity<Map<String, Object>> getMail(@PathVariable("id") Long id,
    		@PathVariable("userName") String userName) {
        log.debug("REST request to get Mail : {}", id);
        Map<String, Object> mail = mailQueryService.getMail(userName, id);
        return new ResponseEntity<>(mail, HttpStatus.OK);
    }
    
    @GetMapping("/mail/find/attach/{id}")
    @Timed
    public ResponseEntity<AttachmentsDTO> getAttachment(@PathVariable("id") Long id) {
        log.debug("REST request to get Mail : {}", id);
        AttachmentsDTO attach = this.attachmentsService.findOne(id);
        attach.setValue(this.storageService.loadAttachment(attach.getDiskFile()));
        return new ResponseEntity<>(attach, HttpStatus.OK);
    }
    
    /**
     * DELETE  /notifications/:id : delete the "id" notifications.
     *
     * @param id the id of the notificationsDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/mail/delete/{id}/{userName}")
    @Timed
    public ResponseEntity<Void> deleteMail(@PathVariable("id") Long id, @PathVariable("userName") String userName) {
    	log.debug("REST request to delete MailReceiver : {}", id);
        mailReceiverService.deleteMailTo(id, userName);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
    
    /**
     * GET  /mail/:userName : get list mail by user name.
     *
     * @param userName is the userName of user login
     * @return the ResponseEntity with status 200 (OK) and with body the notificationsDTO list, or with status 404 (Not Found)
     */
    @GetMapping("/mail/search/{userName}/{query}")
    @Timed
    public ResponseEntity<List<Map<String, Object>>> getMailListByQuerySearch(@PathVariable("userName") String userName, @PathVariable("query") String query, Pageable pageable) {
        List<Map<String, Object>> result = mailQueryService.searchMailByTitleAndSender(userName, query, pageable);
        int totalPage = Integer.parseInt(result.get(result.size() - 1).get("total").toString());
        result.remove(result.size() - 1);
    	Page<Map<String, Object>> page = new PageImpl<>(result, pageable, totalPage);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/mail/{userName}");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    
}
