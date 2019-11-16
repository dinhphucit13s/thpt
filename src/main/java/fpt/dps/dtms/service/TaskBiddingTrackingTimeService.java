package fpt.dps.dtms.service;

import fpt.dps.dtms.domain.TaskBiddingTrackingTime;
import fpt.dps.dtms.domain.TaskTrackingTime;
import fpt.dps.dtms.repository.TaskBiddingTrackingTimeRepository;
import fpt.dps.dtms.repository.search.TaskBiddingTrackingTimeSearchRepository;
import fpt.dps.dtms.service.dto.TaskBiddingTrackingTimeDTO;
import fpt.dps.dtms.service.mapper.TaskBiddingTrackingTimeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing TaskBiddingTrackingTime.
 */
@Service
@Transactional
public class TaskBiddingTrackingTimeService {

    private final Logger log = LoggerFactory.getLogger(TaskBiddingTrackingTimeService.class);

    private final TaskBiddingTrackingTimeRepository taskBiddingTrackingTimeRepository;

    private final TaskBiddingTrackingTimeMapper taskBiddingTrackingTimeMapper;

    private final TaskBiddingTrackingTimeSearchRepository taskBiddingTrackingTimeSearchRepository;

    public TaskBiddingTrackingTimeService(TaskBiddingTrackingTimeRepository taskBiddingTrackingTimeRepository, TaskBiddingTrackingTimeMapper taskBiddingTrackingTimeMapper, TaskBiddingTrackingTimeSearchRepository taskBiddingTrackingTimeSearchRepository) {
        this.taskBiddingTrackingTimeRepository = taskBiddingTrackingTimeRepository;
        this.taskBiddingTrackingTimeMapper = taskBiddingTrackingTimeMapper;
        this.taskBiddingTrackingTimeSearchRepository = taskBiddingTrackingTimeSearchRepository;
    }

    /**
     * Save a taskBiddingTrackingTime.
     *
     * @param taskBiddingTrackingTimeDTO the entity to save
     * @return the persisted entity
     */
    public TaskBiddingTrackingTime save(TaskBiddingTrackingTime taskBiddingTrackingTime) {
        log.debug("Request to save TaskBiddingTrackingTime : {}", taskBiddingTrackingTime);
        TaskBiddingTrackingTime result = taskBiddingTrackingTimeRepository.save(taskBiddingTrackingTime);
        taskBiddingTrackingTimeSearchRepository.save(taskBiddingTrackingTime);
        return result;
    }

    /**
     * Get all the taskBiddingTrackingTimes.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<TaskBiddingTrackingTimeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TaskBiddingTrackingTimes");
        return taskBiddingTrackingTimeRepository.findAll(pageable)
            .map(taskBiddingTrackingTimeMapper::toDto);
    }

    /**
     * Get one taskBiddingTrackingTime by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public TaskBiddingTrackingTimeDTO findOne(Long id) {
        log.debug("Request to get TaskBiddingTrackingTime : {}", id);
        TaskBiddingTrackingTime taskBiddingTrackingTime = taskBiddingTrackingTimeRepository.findOne(id);
        return taskBiddingTrackingTimeMapper.toDto(taskBiddingTrackingTime);
    }

    /**
     * Delete the taskBiddingTrackingTime by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete TaskBiddingTrackingTime : {}", id);
        taskBiddingTrackingTimeRepository.delete(id);
        taskBiddingTrackingTimeSearchRepository.delete(id);
    }

    /**
     * Search for the taskBiddingTrackingTime corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<TaskBiddingTrackingTimeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of TaskBiddingTrackingTimes for query {}", query);
        Page<TaskBiddingTrackingTime> result = taskBiddingTrackingTimeSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(taskBiddingTrackingTimeMapper::toDto);
    }

	public TaskBiddingTrackingTime findTaskIdAndUserLogin(Long id, String userLogin) {
		return this.taskBiddingTrackingTimeRepository.findTop1ByTaskIdAndUserLoginAndEndTimeOrderByIdDesc(id, userLogin, null);
	}
	
	public TaskBiddingTrackingTime findTaskIdAndRole(Long id, String role) {
		return this.taskBiddingTrackingTimeRepository.findTop1ByTaskIdAndRole(id, role);
	}

	public String getPreviousRound(Long taskId, String currentRound) {
		// TODO Auto-generated method stub
		return this.taskBiddingTrackingTimeRepository.getPreviousRound(taskId, currentRound);
	}
}
