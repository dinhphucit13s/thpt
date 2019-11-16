package fpt.dps.dtms.web.rest;

import com.codahale.metrics.annotation.Timed;
import fpt.dps.dtms.service.NotesService;
import fpt.dps.dtms.web.rest.errors.BadRequestAlertException;
import fpt.dps.dtms.web.rest.util.HeaderUtil;
import fpt.dps.dtms.web.rest.util.PaginationUtil;
import fpt.dps.dtms.service.dto.NotesDTO;
import fpt.dps.dtms.service.dto.AttachmentsDTO;
import fpt.dps.dtms.service.dto.NotesCriteria;
import fpt.dps.dtms.service.NotesQueryService;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Notes.
 */
@RestController
@RequestMapping("/api")
public class NotesResource {

    private final Logger log = LoggerFactory.getLogger(NotesResource.class);

    private static final String ENTITY_NAME = "notes";

    private final NotesService notesService;

    private final NotesQueryService notesQueryService;

    public NotesResource(NotesService notesService, NotesQueryService notesQueryService) {
        this.notesService = notesService;
        this.notesQueryService = notesQueryService;
    }

    /**
     * POST  /notes : Create a new notes.
     *
     * @param notesDTO the notesDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new notesDTO, or with status 400 (Bad Request) if the notes has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/notes")
    @Timed
    public ResponseEntity<NotesDTO> createNotes(@Valid @RequestBody NotesDTO notesDTO) throws URISyntaxException {
        log.debug("REST request to save Notes : {}", notesDTO);
        if (notesDTO.getId() != null) {
            throw new BadRequestAlertException("A new notes cannot already have an ID", ENTITY_NAME, "idexists");
        }
        NotesDTO result = notesService.save(notesDTO);
        return ResponseEntity.created(new URI("/api/notes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /notes : Updates an existing notes.
     *
     * @param notesDTO the notesDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated notesDTO,
     * or with status 400 (Bad Request) if the notesDTO is not valid,
     * or with status 500 (Internal Server Error) if the notesDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/notes")
    @Timed
    public ResponseEntity<NotesDTO> updateNotes(@Valid @RequestBody NotesDTO notesDTO) throws URISyntaxException {
        log.debug("REST request to update Notes : {}", notesDTO);
        if (notesDTO.getId() == null) {
            return createNotes(notesDTO);
        }
        NotesDTO result = notesService.save(notesDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, notesDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /notes : get all the notes.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of notes in body
     */
    @GetMapping("/notes")
    @Timed
    public ResponseEntity<List<NotesDTO>> getAllNotes(NotesCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Notes by criteria: {}", criteria);
        Page<NotesDTO> page = notesQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/notes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /notes/:id : get the "id" notes.
     *
     * @param id the id of the notesDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the notesDTO, or with status 404 (Not Found)
     */
    @GetMapping("/notes/{id}")
    @Timed
    public ResponseEntity<NotesDTO> getNotes(@PathVariable Long id) {
        log.debug("REST request to get Notes : {}", id);
        NotesDTO notesDTO = notesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(notesDTO));
    }

    /**
     * DELETE  /notes/:id : delete the "id" notes.
     *
     * @param id the id of the notesDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/notes/{id}")
    @Timed
    public ResponseEntity<Void> deleteNotes(@PathVariable Long id) {
        log.debug("REST request to delete Notes : {}", id);
        notesService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
    
    /**
     * conghk
     * GET  /bugs : get all bug list defaults.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of bugs in body
     */
    @GetMapping("/notes/attachment")
    @Timed
    public ResponseEntity<List<AttachmentsDTO>> getAttachmentByNotesId(@RequestParam(value = "notesId", required = false) Long notesId) {
        log.debug("REST request to get a attachment list of Notes");
        List<AttachmentsDTO> attachmentList = new ArrayList<>();
        attachmentList = this.notesService.getAttachmentByNotesId(notesId);
        return new ResponseEntity<>(attachmentList, HttpStatus.OK);
    }
    
    /**
     * POST  /notes : Create a new bug notes.
     *
     * @param notesDTO the notesDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new notesDTO, or with status 400 (Bad Request) if the notes has already an ID
     * @throws IOException 
     * @throws URISyntaxException if the Location URI syntax is incorrect
     * @PreAuthorize("@jhiAuth.hasPermission('NOTES', 'CREATE')")
     */
    @RequestMapping(value = "/notes/createNotesOfBug", method = RequestMethod.POST,
    headers = {"content-type=multipart/mixed","content-type=multipart/form-data", "content-type=application/octet-stream",
    		"content-type=application/json", "content-type=application/x-www-form-urlencoded"})
    @Timed
    public ResponseEntity<NotesDTO> createBugNotes(@RequestPart(value = "notes", required = true) NotesDTO notes) throws IOException {
        log.debug("REST EXTERNAL request to save bug Notes : {}", notes);
        NotesDTO result = this.notesService.createBugNotes(notes);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * SEARCH  /_search/notes?query=:query : search for the notes corresponding
     * to the query.
     *
     * @param query the query of the notes search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/notes")
    @Timed
    public ResponseEntity<List<NotesDTO>> searchNotes(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Notes for query {}", query);
        Page<NotesDTO> page = notesService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/notes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
