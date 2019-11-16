package fpt.dps.dtms.web.rest.external;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

import fpt.dps.dtms.service.AttachmentsService;
import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing attachments.
 */
@RestController
@RequestMapping("/api/external")
public class ExternalAttachmentsResource {

    private final Logger log = LoggerFactory.getLogger(ExternalBugsResource.class);

    private static final String ENTITY_NAME = "bugs";
    
    private AttachmentsService attachmentsService;

	public ExternalAttachmentsResource(AttachmentsService attachmentsService) {
		this.attachmentsService = attachmentsService;
	}
    
    /**
     * GET  /bugs/:id : get the "id" bugs.
     *
     * @param id the id of the bugsDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the bugsDTO, or with status 404 (Not Found)
     */
    @GetMapping("/attachments/{id}")
    @Timed
    public ResponseEntity<String> getValuesBase64(@PathVariable Long id) {
        log.debug("REST request to get Bugs : {}", id);
        String base64String = attachmentsService.getValuesBase64(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(base64String));
    }
}
