package fpt.dps.dtms.service;

import fpt.dps.dtms.domain.BugListDefault;
import fpt.dps.dtms.domain.User;
import fpt.dps.dtms.repository.BugListDefaultRepository;
import fpt.dps.dtms.repository.search.BugListDefaultSearchRepository;
import fpt.dps.dtms.service.dto.BugListDefaultDTO;
import fpt.dps.dtms.service.mapper.BugListDefaultMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

import java.util.List;

/**
 * Service Implementation for managing BugListDefault.
 */
@Service
@Transactional
public class BugListDefaultService {

    private final Logger log = LoggerFactory.getLogger(BugListDefaultService.class);

    private final BugListDefaultRepository bugListDefaultRepository;

    private final BugListDefaultMapper bugListDefaultMapper;

    private final BugListDefaultSearchRepository bugListDefaultSearchRepository;

    public BugListDefaultService(BugListDefaultRepository bugListDefaultRepository, BugListDefaultMapper bugListDefaultMapper, BugListDefaultSearchRepository bugListDefaultSearchRepository) {
        this.bugListDefaultRepository = bugListDefaultRepository;
        this.bugListDefaultMapper = bugListDefaultMapper;
        this.bugListDefaultSearchRepository = bugListDefaultSearchRepository;
    }

    /**
     * Save a bugListDefault.
     *
     * @param bugListDefaultDTO the entity to save
     * @return the persisted entity
     */
    public BugListDefaultDTO save(BugListDefaultDTO bugListDefaultDTO) {
        log.debug("Request to save BugListDefault : {}", bugListDefaultDTO);
        BugListDefault bugListDefault = bugListDefaultMapper.toEntity(bugListDefaultDTO);
        bugListDefault = bugListDefaultRepository.save(bugListDefault);
        BugListDefaultDTO result = bugListDefaultMapper.toDto(bugListDefault);
        bugListDefaultSearchRepository.save(bugListDefault);
        return result;
    }

    /**
     * Get all the bugListDefaults.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<BugListDefaultDTO> findAll(Pageable pageable) {
        log.debug("Request to get all BugListDefaults");
        return bugListDefaultRepository.findAll(pageable)
            .map(bugListDefaultMapper::toDto);
    }

    /**
     * Get one bugListDefault by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public BugListDefaultDTO findOne(Long id) {
        log.debug("Request to get BugListDefault : {}", id);
        BugListDefault bugListDefault = bugListDefaultRepository.findOne(id);
        return bugListDefaultMapper.toDto(bugListDefault);
    }

    /**
     * Delete the bugListDefault by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete BugListDefault : {}", id);
        bugListDefaultRepository.delete(id);
        bugListDefaultSearchRepository.delete(id);
    }

    /**
     * Search for the bugListDefault corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<BugListDefaultDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of BugListDefaults for query {}", query);
        Page<BugListDefault> result = bugListDefaultSearchRepository.findByDescriptionLike(query, pageable);
        return result.map(bugListDefaultMapper::toDto);
    }

	public List<BugListDefaultDTO> getAllBugListDefaultsUnExistInProject(Long projectId) {
		
		return bugListDefaultMapper.toDto(bugListDefaultRepository.getAllBugListDefaultsUnExistInProject(projectId));
	}
}
