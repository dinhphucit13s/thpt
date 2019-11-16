package fpt.dps.dtms.web.rest.external;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

import fpt.dps.dtms.service.TmsPostService;
import fpt.dps.dtms.service.dto.TmsPostDTO;
import fpt.dps.dtms.web.rest.util.PaginationUtil;

@RestController
@RequestMapping("/api/external")
public class ExternalPostsResource {

	private final Logger log = LoggerFactory.getLogger(ExternalProjectsResource.class);
	
	private final TmsPostService tmsPostService;

	public ExternalPostsResource(TmsPostService tmsPostService) {
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
    @RequestMapping(value = "/posts/create", method = RequestMethod.POST,
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
    @RequestMapping(value = "/posts/update", method = RequestMethod.PUT,
    headers = {"content-type=multipart/mixed","content-type=multipart/form-data", "content-type=application/octet-stream",
    		"content-type=application/json", "content-type=application/x-www-form-urlencoded"})
    @Timed
    public ResponseEntity<TmsPostDTO> updatePost(@RequestPart(value = "posts", required = true) TmsPostDTO payload) throws IOException {
        log.debug("REST request to create issues");
        TmsPostDTO postDTO = this.tmsPostService.updatePosts(payload);
        return new ResponseEntity<>(postDTO, HttpStatus.OK);
    }
    
    /**
     * GET  /posts/threads/:threadId : get posts by thread "id".
     *
     * @param id the id of the TmsThreadDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the TmsThreadDTO, or with status 404 (Not Found)
     * @PreAuthorize("@jhiAuth.hasPermission('NOTES', 'VIEW')")
     */
    @GetMapping("/posts/threads/{threadId}/{exceptId}")
    @Timed
    public ResponseEntity<List<TmsPostDTO>> getAnswer(@PathVariable("threadId") Long threadId, @PathVariable("exceptId") Long exceptId, Pageable pageable) {
        log.debug("REST EXTERNAL request to get post by threadId ", threadId);
        Page<TmsPostDTO> page = this.tmsPostService.findAnswer(threadId, pageable, exceptId);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/external/posts/threads/{threadId}");
        
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
}
