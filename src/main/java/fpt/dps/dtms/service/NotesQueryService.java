package fpt.dps.dtms.service;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import fpt.dps.dtms.domain.Notes;
import fpt.dps.dtms.domain.*; // for static metamodels
import fpt.dps.dtms.repository.NotesRepository;
import fpt.dps.dtms.repository.search.NotesSearchRepository;
import fpt.dps.dtms.service.dto.NotesCriteria;

import fpt.dps.dtms.service.dto.NotesDTO;
import fpt.dps.dtms.service.mapper.NotesMapper;

/**
 * Service for executing complex queries for Notes entities in the database.
 * The main input is a {@link NotesCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link NotesDTO} or a {@link Page} of {@link NotesDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NotesQueryService extends QueryService<Notes> {

    private final Logger log = LoggerFactory.getLogger(NotesQueryService.class);


    private final NotesRepository notesRepository;

    private final NotesMapper notesMapper;

    private final NotesSearchRepository notesSearchRepository;

    public NotesQueryService(NotesRepository notesRepository, NotesMapper notesMapper, NotesSearchRepository notesSearchRepository) {
        this.notesRepository = notesRepository;
        this.notesMapper = notesMapper;
        this.notesSearchRepository = notesSearchRepository;
    }

    /**
     * Return a {@link List} of {@link NotesDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<NotesDTO> findByCriteria(NotesCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Notes> specification = createSpecification(criteria);
        return notesMapper.toDto(notesRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link NotesDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NotesDTO> findByCriteria(NotesCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Notes> specification = createSpecification(criteria);
        final Page<Notes> result = notesRepository.findAll(specification, page);
        return result.map(notesMapper::toDto);
    }

    /**
     * Function to convert NotesCriteria to a {@link Specifications}
     */
    private Specifications<Notes> createSpecification(NotesCriteria criteria) {
        Specifications<Notes> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Notes_.id));
            }
            if (criteria.getTasksId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getTasksId(), Notes_.tasks, Tasks_.id));
            }
            if (criteria.getBugId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getBugId(), Notes_.bug, Bugs_.id));
            }
        }
        return specification;
    }

}
