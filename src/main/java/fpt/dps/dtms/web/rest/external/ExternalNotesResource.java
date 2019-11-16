package fpt.dps.dtms.web.rest.external;

import com.codahale.metrics.annotation.Timed;
import fpt.dps.dtms.service.NotesService;
import fpt.dps.dtms.web.rest.errors.BadRequestAlertException;
import fpt.dps.dtms.web.rest.util.HeaderUtil;
import fpt.dps.dtms.web.rest.util.PaginationUtil;
import fpt.dps.dtms.service.dto.NotesDTO;
import fpt.dps.dtms.service.dto.TasksCriteria;
import fpt.dps.dtms.service.dto.AttachmentsDTO;
import fpt.dps.dtms.service.dto.NotesCriteria;
import fpt.dps.dtms.domain.Notes;
import fpt.dps.dtms.service.NotesQueryService;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
 * REST EXTERNAL controller for managing Notes.
 */
@RestController
@RequestMapping("/api/external")
public class ExternalNotesResource {

    private final Logger log = LoggerFactory.getLogger(ExternalNotesResource.class);

    private static final String ENTITY_NAME = "notes";

    private final NotesService notesService;

    private final NotesQueryService notesQueryService;

    public ExternalNotesResource(NotesService notesService, NotesQueryService notesQueryService) {
        this.notesService = notesService;
        this.notesQueryService = notesQueryService;
    }

    /**
     * POST  /notes : Create a new notes.
     *
     * @param notesDTO the notesDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new notesDTO, or with status 400 (Bad Request) if the notes has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     * @PreAuthorize("@jhiAuth.hasPermission('NOTES', 'CREATE')")
     */
    @PostMapping("/notes")
    @Timed
    public ResponseEntity<NotesDTO> createNotes(@Valid @RequestBody NotesDTO notesDTO) throws URISyntaxException {
        log.debug("REST EXTERNAL request to save Notes : {}", notesDTO);
        if (notesDTO.getId() != null) {
            throw new BadRequestAlertException("A new notes cannot already have an ID", ENTITY_NAME, "idexists");
        }
        NotesDTO result = notesService.save(notesDTO);
        return ResponseEntity.created(new URI("/api/external/notes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
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
     * POST  /notes : Create a new bug notes.
     *
     * @param notesDTO the notesDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new notesDTO, or with status 400 (Bad Request) if the notes has already an ID
     * @throws IOException 
     * @throws URISyntaxException if the Location URI syntax is incorrect
     * @PreAuthorize("@jhiAuth.hasPermission('NOTES', 'CREATE')")
     */
    @RequestMapping(value = "/notes/updateNotes", method = RequestMethod.POST,
    headers = {"content-type=multipart/mixed","content-type=multipart/form-data", "content-type=application/octet-stream",
    		"content-type=application/json", "content-type=application/x-www-form-urlencoded"})
    @Timed
    public ResponseEntity<NotesDTO> updateNotes(@RequestPart(value = "notes", required = true) NotesDTO notes) throws IOException {
        log.debug("REST EXTERNAL request to save bug Notes : {}", notes);
        NotesDTO result = this.notesService.updateNotes(notes);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * GET  /notes : get all the notes.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of notes in body
     * @PreAuthorize("@jhiAuth.hasPermission('NOTES', 'VIEW')")
     */
    @GetMapping("/notes")
    @Timed
    public ResponseEntity<List<NotesDTO>> getAllNotes(NotesCriteria criteria, Pageable pageable) {
        log.debug("REST EXTERNAL request to get Notes by criteria: {}", criteria);
        Page<NotesDTO> page = notesQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/notes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /notes/:id : get the "id" notes.
     *
     * @param id the id of the notesDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the notesDTO, or with status 404 (Not Found)
     * @PreAuthorize("@jhiAuth.hasPermission('NOTES', 'VIEW')")
     */
    @GetMapping("/notes/{id}")
    @Timed
    public ResponseEntity<NotesDTO> getNotes(@PathVariable Long id) {
        log.debug("REST EXTERNAL request to get Notes : {}", id);
        NotesDTO notesDTO = notesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(notesDTO));
    }
    
    /**
     * GET  /notes/tasks/:id : get notes the by task "id".
     *
     * @param id the id of the notesDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the notesDTO, or with status 404 (Not Found)
     * @PreAuthorize("@jhiAuth.hasPermission('NOTES', 'VIEW')")
     */
    @GetMapping("/notes/tasks/{id}")
    @Timed
    public ResponseEntity<List<NotesDTO>> getNotesByTaskId(@PathVariable Long id) {
        log.debug("REST EXTERNAL request to get Notes by tasks Id : {}", id);
        List<NotesDTO> listNotes = new ArrayList<>();
        
        NotesCriteria notesCriteria = new NotesCriteria();
        LongFilter tasksIdFilter  = new LongFilter();
        tasksIdFilter.setEquals(id);
        notesCriteria.setTasksId(tasksIdFilter);
        
        listNotes = notesQueryService.findByCriteria(notesCriteria);
        return new ResponseEntity<>(listNotes, HttpStatus.OK);
    }

    /**
     * DELETE  /notes/:id : delete the "id" notes.
     *
     * @param id the id of the notesDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     * @PreAuthorize("@jhiAuth.hasPermission('NOTES', 'DELETE')")
     */
    @DeleteMapping("/notes/{id}")
    @Timed
    public ResponseEntity<Void> deleteNotes(@PathVariable Long id) {
        log.debug("REST EXTERNAL request to delete Notes : {}", id);
        notesService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
    
    /**
     * SEARCH  /_search/notes?query=:query : search for the notes corresponding
     * to the query.
     *
     * @param query the query of the notes search
     * @param pageable the pagination information
     * @return the result of the search
     */
//    @GetMapping("/_search/notes")
//    @Timed
//    @PreAuthorize("@jhiAuth.hasPermission('NOTES', 'VIEW')")
//    public ResponseEntity<List<NotesDTO>> searchNotes(@RequestParam String query, Pageable pageable) {
//        log.debug("REST EXTERNAL request to search for a page of Notes for query {}", query);
//        Page<NotesDTO> page = notesService.search(query, pageable);
//        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/notes");
//        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
//    }

}
