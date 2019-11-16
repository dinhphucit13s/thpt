package fpt.dps.dtms.service;

import fpt.dps.dtms.domain.TaskTrackingTime;
import fpt.dps.dtms.repository.TaskTrackingTimeRepository;
import fpt.dps.dtms.repository.search.TaskTrackingTimeSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

import java.util.List;

/**
 * Service Implementation for managing TaskTrackingTime.
 */
@Service
@Transactional
public class TaskTrackingTimeService {

    private final Logger log = LoggerFactory.getLogger(TaskTrackingTimeService.class);

    private final TaskTrackingTimeRepository taskTrackingTimeRepository;

    private final TaskTrackingTimeSearchRepository taskTrackingTimeSearchRepository;

    public TaskTrackingTimeService(TaskTrackingTimeRepository taskTrackingTimeRepository, TaskTrackingTimeSearchRepository taskTrackingTimeSearchRepository) {
        this.taskTrackingTimeRepository = taskTrackingTimeRepository;
        this.taskTrackingTimeSearchRepository = taskTrackingTimeSearchRepository;
    }

    /**
     * Save a taskTrackingTime.
     *
     * @param taskTrackingTime the entity to save
     * @return the persisted entity
     */
    public TaskTrackingTime save(TaskTrackingTime taskTrackingTime) {
        log.debug("Request to save TaskTrackingTime : {}", taskTrackingTime);
        TaskTrackingTime result = taskTrackingTimeRepository.save(taskTrackingTime);
        taskTrackingTimeSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the taskTrackingTimes.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<TaskTrackingTime> findAll(Pageable pageable) {
        log.debug("Request to get all TaskTrackingTimes");
        return taskTrackingTimeRepository.findAll(pageable);
    }
    
    @Transactional(readOnly = true)
    public TaskTrackingTime findTaskTrackingByRole(Long taskId) {
        log.debug("Request to get all TaskTrackingTimes");
        return taskTrackingTimeRepository.findTaskTrackingByRole(taskId);
    }

    /**
     * Get one taskTrackingTime by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public TaskTrackingTime findOne(Long id) {
        log.debug("Request to get TaskTrackingTime : {}", id);
        return taskTrackingTimeRepository.findOne(id);
    }

    /**
     * Delete the taskTrackingTime by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete TaskTrackingTime : {}", id);
        taskTrackingTimeRepository.delete(id);
        taskTrackingTimeSearchRepository.delete(id);
    }

    /**
     * Search for the taskTrackingTime corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<TaskTrackingTime> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of TaskTrackingTimes for query {}", query);
        Page<TaskTrackingTime> result = taskTrackingTimeSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }

	public TaskTrackingTime findTaskIdAndUserLogin(Long id, String userLogin) {
		return this.taskTrackingTimeRepository.findTop1ByTaskIdAndUserLoginAndEndTimeOrderByIdDesc(id, userLogin, null);
	}
	
	/**
	 * This method get all TaskTrackingTimes by Task id
	 * @param taskId
	 * @return
	 * @author TuHP
	 */
	public List<TaskTrackingTime> findAllByTaskId(Long taskId){
		return taskTrackingTimeRepository.findAllByTaskId(taskId);
	}

	/**
	 * This method get all TaskTrackingTimes by Task id and role
	 * @param taskId
	 * @param role
	 * @return
	 * @author HoiHT1
	 */
	public List<TaskTrackingTime> findAllByTaskIdAndRole(Long taskId, String role){
		return taskTrackingTimeRepository.findAllByTaskIdAndRole(taskId, role);
	}

	public List<TaskTrackingTime> findAllByTaskIdAndRoleAndEndStatus(Long taskId, String role, String status){
		return taskTrackingTimeRepository.findAllByTaskIdAndRoleAndEndStatus(taskId, role, status);
	}

	public Integer countAllByTaskIdAndRoleAndEndStatus(Long taskId, String role, String status){
		return taskTrackingTimeRepository.countAllByTaskIdAndRoleAndEndStatus(taskId, role, status);
	}

	/**
	 * Get TaskTrackingTimes before by Task id and difference user login
	 * @param taskId
	 * @param userLogin
	 * @return
	 */
	@Transactional(readOnly = true)
	public TaskTrackingTime findTaskTrackingBeforeByTaskIdAndUserLogin(Long taskId, String userLogin) {
		return taskTrackingTimeRepository.findTaskTrackingBeforeByTaskIdAndUserLogin(taskId, userLogin);
	}

	public String getPreviousRound(Long id, String currentRound) {
		return this.taskTrackingTimeRepository.getPreviousRound(id, currentRound);
	}
}
