package fpt.dps.dtms.web.rest;

import com.codahale.metrics.annotation.Timed;
import fpt.dps.dtms.service.TmsPostService;
import fpt.dps.dtms.web.rest.errors.BadRequestAlertException;
import fpt.dps.dtms.web.rest.util.HeaderUtil;
import fpt.dps.dtms.web.rest.util.PaginationUtil;
import fpt.dps.dtms.service.dto.TmsPostDTO;
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

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing TmsPost.
 */
@RestController
@RequestMapping("/api")
public class TmsPostResource {

    private final Logger log = LoggerFactory.getLogger(TmsPostResource.class);

    private static final String ENTITY_NAME = "tmsPost";

    private final TmsPostService tmsPostService;

    public TmsPostResource(TmsPostService tmsPostService) {
        this.tmsPostService = tmsPostService;
    }

    /**
     * conghk
     * GET  /posts : create issues.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of bugs in body
     * @throws IOException 
     */
    @RequestMapping(value = "/tms-posts/create", method = RequestMethod.POST,
    headers = {"content-type=multipart/mixed","content-type=multipart/form-data", "content-type=application/octet-stream",
    		"content-type=application/json", "content-type=application/x-www-form-urlencoded"})
    @Timed
    public ResponseEntity<TmsPostDTO> createPost(@RequestPart(value = "posts", required = true) TmsPostDTO payload) throws IOException {
        log.debug("REST request to create issues");
        TmsPostDTO postDTO = this.tmsPostService.createPosts(payload);
        return new ResponseEntity<>(postDTO, HttpStatus.OK);
    }
    
    /**
     * conghk
     * GET  /posts : create issues.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of bugs in body
     * @throws IOException 
     */
    @RequestMapping(value = "/tms-posts/update", method = RequestMethod.PUT,
    headers = {"content-type=multipart/mixed","content-type=multipart/form-data", "content-type=application/octet-stream",
    		"content-type=application/json", "content-type=application/x-www-form-urlencoded"})
    @Timed
    public ResponseEntity<TmsPostDTO> updatePost(@RequestPart(value = "posts", required = true) TmsPostDTO payload) throws IOException {
        log.debug("REST request to create issues");
        TmsPostDTO postDTO = this.tmsPostService.updatePosts(payload);
        return new ResponseEntity<>(postDTO, HttpStatus.OK);
    }

    /**
     * GET  /tms-posts : get all the tmsPosts.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of tmsPosts in body
     */
    @GetMapping("/tms-posts")
    @Timed
    public List<TmsPostDTO> getAllTmsPosts() {
        log.debug("REST request to get all TmsPosts");
        return tmsPostService.findAll();
        }

    /**
     * GET  /tms-posts/:id : get the "id" tmsPost.
     *
     * @param id the id of the tmsPostDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the tmsPostDTO, or with status 404 (Not Found)
     */
    @GetMapping("/tms-posts/{id}")
    @Timed
    public ResponseEntity<TmsPostDTO> getTmsPost(@PathVariable Long id) {
        log.debug("REST request to get TmsPost : {}", id);
        TmsPostDTO tmsPostDTO = tmsPostService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(tmsPostDTO));
    }

    /**
     * DELETE  /tms-posts/:id : delete the "id" tmsPost.
     *
     * @param id the id of the tmsPostDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/tms-posts/{id}")
    @Timed
    public ResponseEntity<Void> deleteTmsPost(@PathVariable Long id) {
        log.debug("REST request to delete TmsPost : {}", id);
        tmsPostService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/tms-posts?query=:query : search for the tmsPost corresponding
     * to the query.
     *
     * @param query the query of the tmsPost search
     * @return the result of the search
     */
    @GetMapping("/_search/tms-posts")
    @Timed
    public List<TmsPostDTO> searchTmsPosts(@RequestParam String query) {
        log.debug("REST request to search TmsPosts for query {}", query);
        return tmsPostService.search(query);
    }
    
    /**
     * GET  /posts/threads/:threadId : get posts by thread "id".
     *
     * @param id the id of the TmsThreadDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the TmsThreadDTO, or with status 404 (Not Found)
     * @PreAuthorize("@jhiAuth.hasPermission('NOTES', 'VIEW')")
     */
    @GetMapping("/tms-posts/threads/{threadId}/{exceptId}")
    @Timed
    public ResponseEntity<List<TmsPostDTO>> getAnswer(@PathVariable("threadId") Long threadId, @PathVariable("exceptId") Long exceptId, Pageable pageable) {
        log.debug("REST EXTERNAL request to get post by threadId ", threadId);
        Page<TmsPostDTO> page = this.tmsPostService.findAnswer(threadId, pageable, exceptId);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/external/posts/threads/{threadId}");
        
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
