package fpt.dps.dtms.web.rest.external;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

import fpt.dps.dtms.service.CommentsService;
import fpt.dps.dtms.service.dto.CommentsDTO;
import fpt.dps.dtms.service.dto.NotesDTO;
import fpt.dps.dtms.service.dto.TmsThreadDTO;
import fpt.dps.dtms.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;

@RestController
@RequestMapping("/api/external")
public class ExternalCommentsResource {
	private final Logger log = LoggerFactory.getLogger(ExternalUserResource.class);
	
	private static final String ENTITY_NAME = "comments";
	
	private final CommentsService commentsService;
	
	public ExternalCommentsResource(CommentsService commentsService) {
		this.commentsService = commentsService;
	}
	
    /**
     * GET  /comments/posts/:postsId : get the "id" comments.
     *
     * @param id the id of the commentsDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the tmsThreadDTO, or with status 404 (Not Found)
     */
    @GetMapping("/comments/posts/{postId}")
    @Timed
    public ResponseEntity<List<CommentsDTO>> getCommentByPostId(@PathVariable("postId") Long postId) {
        log.debug("REST request to get comments by post : {}", postId);
        List<CommentsDTO> commentsDTOs = this.commentsService.getCommentByPostId(postId);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(commentsDTOs));
    }
    
    @RequestMapping(value = "/comments/create", method = RequestMethod.POST,
    headers = {"content-type=multipart/mixed","content-type=multipart/form-data", "content-type=application/octet-stream",
    		"content-type=application/json", "content-type=application/x-www-form-urlencoded"})
    @Timed
    public ResponseEntity<CommentsDTO> createComments(@RequestPart(value = "comments", required = true) CommentsDTO comments) throws IOException {
        log.debug("REST EXTERNAL request to save comments : {}", comments);
        CommentsDTO result = this.commentsService.createComments(comments);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/comments/update", method = RequestMethod.PUT,
    headers = {"content-type=multipart/mixed","content-type=multipart/form-data", "content-type=application/octet-stream",
    		"content-type=application/json", "content-type=application/x-www-form-urlencoded"})
    @Timed
    public ResponseEntity<CommentsDTO> updateComments(@RequestPart(value = "comments", required = true) CommentsDTO comments) throws IOException {
        log.debug("REST EXTERNAL request to save comments : {}", comments);
        CommentsDTO result = this.commentsService.updateComments(comments);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    
    /**
     * DELETE  /comments/:id : delete the "id" comments.
     *
     * @param id the id of the commentsDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/comments/{id}")
    @Timed
    public ResponseEntity<Void> deleteComments(@PathVariable Long id) {
        log.debug("REST request to delete Comments : {}", id);
        commentsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
