package fpt.dps.dtms.web.rest;

import com.codahale.metrics.annotation.Timed;
import fpt.dps.dtms.web.rest.errors.BadRequestAlertException;
import fpt.dps.dtms.web.rest.errors.InternalServerErrorException;
import fpt.dps.dtms.web.rest.util.HeaderUtil;
import fpt.dps.dtms.web.rest.util.PaginationUtil;
import fpt.dps.dtms.service.dto.MailDTO;
import fpt.dps.dtms.service.dto.AttachmentsDTO;
import fpt.dps.dtms.service.dto.MailCriteria;
import fpt.dps.dtms.security.AuthoritiesConstants;
import fpt.dps.dtms.security.SecurityUtils;
import fpt.dps.dtms.service.AttachmentsService;
import fpt.dps.dtms.service.DtmsMailService;
import fpt.dps.dtms.service.IssuesService;
import fpt.dps.dtms.service.MailQueryService;
import fpt.dps.dtms.service.MailReceiverService;
import fpt.dps.dtms.service.StorageService;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Mail.
 */
@RestController
@RequestMapping("/api")
public class MailResource {

    private final Logger log = LoggerFactory.getLogger(MailResource.class);

    private static final String ENTITY_NAME = "mail";

    private final DtmsMailService mailService;
    
    private final IssuesService issuesService;

    private final MailQueryService mailQueryService;
    
    private final MailReceiverService mailReceiverService;
    
    private final AttachmentsService attachmentsService;
    
    private final StorageService storageService;

    public MailResource(DtmsMailService mailService, MailQueryService mailQueryService, MailReceiverService mailReceiverService, IssuesService issuesService, AttachmentsService attachmentsService, StorageService storageService) {
        this.mailService = mailService;
        this.mailQueryService = mailQueryService;
        this.mailReceiverService = mailReceiverService;
        this.issuesService = issuesService;
        this.attachmentsService = attachmentsService;
        this.storageService = storageService;
    }

    /**
     * POST  /mail : Create a new mail.
     *
     * @param mailDTO the mailDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new mailDTO, or with status 400 (Bad Request) if the mail has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/mail", method = RequestMethod.POST,
    	    headers = {"content-type=application/octet-stream", "content-type=multipart/mixed","content-type=multipart/form-data"})
    @Timed
    @PreAuthorize("@jhiAuth.hasPermission('MAIL', 'CREATE')")
    public ResponseEntity<MailDTO> createMail(@RequestParam(value = "attachments", required = true) List<MultipartFile> files, 
    		@Valid @RequestPart(value = "mail", required = true) MailDTO mailDTO) throws URISyntaxException {
        log.debug("REST request to save Mail : {}", mailDTO);
        if (mailDTO.getId() != null) {
            throw new BadRequestAlertException("A new mail cannot already have an ID", ENTITY_NAME, "idexists");
        }
        final String userLogin = SecurityUtils.getCurrentUserLogin()
				.orElseThrow(() -> new InternalServerErrorException("Current user login not found"));
        MailDTO result = mailService.save(mailDTO, userLogin, files);
        return ResponseEntity.created(new URI("/api/mail/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }
    
    /*@RequestMapping(value = "/packages/import", method = RequestMethod.POST,
    	    headers = {"content-type=application/octet-stream", "content-type=multipart/mixed","content-type=multipart/form-data", 
    	    		"content-type=application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",})
    @Timed
    public ResponseEntity<String> packageImport(@RequestParam("excelFile") MultipartFile file, @Valid @RequestPart(value = "poId", required = true) Long poID) {
    	log.info(poID.toString());
    	try {
    		packagesService.autoGenPackages_Tasks(file, poID);
    		return ResponseEntity.ok().headers(HeaderUtil.importAlert(ENTITY_NAME, poID.toString())).build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Import packages and task fail!!! " + e.toString());
		}
	}*/

    /**
     * GET  /mail : get all the mail.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of mail in body
     */
    @GetMapping("/mail")
    @Timed
    @PreAuthorize("@jhiAuth.hasPermission('MAIL', 'VIEW')")
    public ResponseEntity<List<MailDTO>> getAllMail(MailCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Mail by criteria: {}", criteria);
        Page<MailDTO> page = mailQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/mail");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /mail : get all the mail send by user login.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of mail in body
     */
    @GetMapping("/mail-send")
    @Timed
    public ResponseEntity<List<MailDTO>> getAllMailSend(@RequestParam(value = "userLogin", required = false) String userLogin, Pageable pageable) {
        log.debug("REST request to get Mail by user login: {}", userLogin);
        Page<MailDTO> page = mailQueryService.getAllMailSend(userLogin, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/mail-send");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    
    @GetMapping("/mail/attachment")
    @Timed
    public ResponseEntity<AttachmentsDTO> getAttachmentByIssuesId(@RequestParam(value = "id", required = false) Long id) {
        log.debug("REST request to get a attachment list of issuesId");
        AttachmentsDTO attach = this.attachmentsService.findOne(id);
        attach.setValue(this.storageService.loadAttachment(attach.getDiskFile()));
        return new ResponseEntity<>(attach, HttpStatus.OK);
    }
    
    /**
     * GET  /mail : get all the mail receiver by user login.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of mail in body
     */
    @GetMapping("/mail-receiver")
    @Timed
    public ResponseEntity<List<Map<String, Object>>> getAllMailReceiver(@RequestParam(value = "userLogin", required = false) String userLogin, Pageable pageable) {
        log.debug("REST request to get Mail by user login: {}", userLogin);
        List<Map<String, Object>> result = mailQueryService.getAllMailByUserLogin(userLogin, pageable);
        int totalPage = Integer.parseInt(result.get(result.size() - 1).get("total").toString());
        result.remove(result.size() - 1);
    	Page<Map<String, Object>> page = new PageImpl<>(result, pageable, totalPage);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/mail-receiver");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /mail : get all the mail receiver by user login.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of mail in body
     */
    @GetMapping("/mail-receiver/unread")
    @Timed
    public ResponseEntity<List<Map<String, Object>>> getAllMailReceiverUnread(@RequestParam(value = "userLogin", required = false) String userLogin, Pageable pageable) {
        log.debug("REST request to get Mail Unread by user login: {}", userLogin);
        List<Map<String, Object>> result = mailQueryService.getAllMailUnreadByUserLogin(userLogin, pageable);
        int totalPage = Integer.parseInt(result.get(result.size() - 1).get("total").toString());
        result.remove(result.size() - 1);
    	Page<Map<String, Object>> page = new PageImpl<>(result, pageable, totalPage);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/mail-receiver");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    
    /**
     * GET  /mail-receiver/{userLogin}/count-mailUnseen: count mail unseen of user
     *
     * @param userLogin 
     * @return the ResponseEntity with status 200 (OK) and with body the number of mail, or with status 404 (Not Found)
     */
    @GetMapping("/mail-receiver/{userLogin}/count-mailUnseen")
    @Timed
    public ResponseEntity<Integer> countMailUnseenOfUserLogin(@PathVariable(value = "userLogin") String userLogin) {
        log.debug("REST request to count mail inbox of: {}", userLogin);
        Integer result = mailQueryService.countAllMailUnseenByUserLogin(userLogin);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(result));
    }
    
    /**
     * GET  /mail-receiver/count-inbox: count mail inbox of user
     *
     * @param userLogin 
     * @return the ResponseEntity with status 200 (OK) and with body the number of mail, or with status 404 (Not Found)
     */
    @GetMapping("/mail-receiver/count-inbox")
    @Timed
    public ResponseEntity<Integer> countMailInboxOfUserLogin(@RequestParam(value = "userLogin", required = true) String userLogin) {
        log.debug("REST request to count mail inbox of: {}", userLogin);
        Integer result = mailQueryService.countAllMailInboxByUserLogin(userLogin);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(result));
    }
    
    /**
     * GET  /mail/:id : get the "id" mail.
     *
     * @param id the id of the mailDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the mailDTO, or with status 404 (Not Found)
     */
    @GetMapping("/mail/{id}/{userLogin}")
    @Timed
    @PreAuthorize("@jhiAuth.hasPermission('MAIL', 'VIEW')")
    public ResponseEntity<Map<String, Object>> getMail(@PathVariable Long id, @PathVariable String userLogin) {
        log.debug("REST request to get Mail : {}", id);
        Map<String, Object> mailDTO = mailService.findOne(id, userLogin);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(mailDTO));
    }
    
    @GetMapping("/mail-status")
    @Timed
    public ResponseEntity<Boolean> updateStatusMail(@RequestParam Long id, @RequestParam String userLogin) {
        log.debug("REST request to get Mail : {}", id);
        Boolean result = mailReceiverService.setStatusMailReceiver(userLogin, id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    
    @GetMapping("/mail/attach/{id}")
    @Timed
    public ResponseEntity<AttachmentsDTO> getAttachment(@PathVariable Long id) {
        log.debug("REST request to get Mail : {}", id);
        AttachmentsDTO attach = this.attachmentsService.findOne(id);
        attach.setValue(this.storageService.loadAttachment(attach.getDiskFile()));
        return new ResponseEntity<>(attach, HttpStatus.OK);
    }

    /**
     * DELETE  /mail/:id : delete the "id" mail.
     *
     * @param id the id of the mailDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/mail/{id}")
    @Timed
    @PreAuthorize("@jhiAuth.hasPermission('MAIL', 'DELETE')")
    public ResponseEntity<Void> deleteMail(@PathVariable Long id) {
        log.debug("REST request to delete Mail : {}", id);
        mailService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
    
    /**
     * DELETE  /mail/:id : delete the "id" mail and user login.
     *
     * @param id the id of the mailDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/mail/{dest}/{id}/{userName}")
    @Timed
    @PreAuthorize("@jhiAuth.hasPermission('MAIL', 'DELETE')")
    public ResponseEntity<Void> deleteMail(@PathVariable("dest") String dest, @PathVariable("id") Long id, @PathVariable("userName") String userName) {
        log.debug("REST request to delete Mail : {}", id);
        if (dest.equals("to")) {
        	mailReceiverService.deleteMailTo(id, userName);
        } else if (dest.equals("from")) {
        	mailReceiverService.deleteMailFrom(id, userName);
        }
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/mail?query=:query : search for the mail corresponding
     * to the query.
     *
     * @param query the query of the mail search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/mail")
    @Timed
    @PreAuthorize("@jhiAuth.hasPermission('MAIL', 'VIEW')")
    public ResponseEntity<List<MailDTO>> searchMail(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Mail for query {}", query);
        Page<MailDTO> page = mailService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/mail");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
