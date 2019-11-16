package fpt.dps.dtms.web.rest;

import com.codahale.metrics.annotation.Timed;
import fpt.dps.dtms.service.CommentsService;
import fpt.dps.dtms.web.rest.errors.BadRequestAlertException;
import fpt.dps.dtms.web.rest.util.HeaderUtil;
import fpt.dps.dtms.service.dto.CommentsDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Comments.
 */
@RestController
@RequestMapping("/api")
public class CommentsResource {

    private final Logger log = LoggerFactory.getLogger(CommentsResource.class);

    private static final String ENTITY_NAME = "comments";

    private final CommentsService commentsService;

    public CommentsResource(CommentsService commentsService) {
        this.commentsService = commentsService;
    }

    /**
     * GET  /comments : get all the comments.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of comments in body
     */
    @GetMapping("/comments")
    @Timed
    public List<CommentsDTO> getAllComments() {
        log.debug("REST request to get all Comments");
        return commentsService.findAll();
        }

    /**
     * GET  /comments/:id : get the "id" comments.
     *
     * @param id the id of the commentsDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the commentsDTO, or with status 404 (Not Found)
     */
    @GetMapping("/comments/{id}")
    @Timed
    public ResponseEntity<CommentsDTO> getComments(@PathVariable Long id) {
        log.debug("REST request to get Comments : {}", id);
        CommentsDTO commentsDTO = commentsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(commentsDTO));
    }

    /**
     * SEARCH  /_search/comments?query=:query : search for the comments corresponding
     * to the query.
     *
     * @param query the query of the comments search
     * @return the result of the search
     */
    @GetMapping("/_search/comments")
    @Timed
    public List<CommentsDTO> searchComments(@RequestParam String query) {
        log.debug("REST request to search Comments for query {}", query);
        return commentsService.search(query);
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
