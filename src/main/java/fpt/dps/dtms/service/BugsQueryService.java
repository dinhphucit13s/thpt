package fpt.dps.dtms.service;

import fpt.dps.dtms.domain.Attachments;
import fpt.dps.dtms.domain.Bugs;
import fpt.dps.dtms.domain.Bugs_;
import fpt.dps.dtms.domain.Notes;
import fpt.dps.dtms.domain.Tasks_;
import fpt.dps.dtms.repository.AttachmentsRepository;
import fpt.dps.dtms.repository.BugsRepository;
import fpt.dps.dtms.repository.search.BugsSearchRepository;
import fpt.dps.dtms.service.dto.AttachmentsDTO;
import fpt.dps.dtms.service.dto.BugsCriteria;
import fpt.dps.dtms.service.dto.BugsDTO;
import fpt.dps.dtms.service.dto.NotesDTO;
import fpt.dps.dtms.service.dto.TasksDTO;
import fpt.dps.dtms.service.mapper.BugsMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import io.github.jhipster.service.QueryService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.domain.Specifications;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Bugs.
 */
@Service
@Transactional(readOnly = true)
public class BugsQueryService extends QueryService<Bugs>{
	
	private final Logger log = LoggerFactory.getLogger(BugsQueryService.class);

	private final BugsRepository bugsRepository;

	private final BugsMapper bugsMapper;

    private final BugsSearchRepository bugsSearchRepository;
    
    private final AttachmentsRepository attachmentsRepository;

    public BugsQueryService(BugsRepository bugsRepository, BugsMapper bugsMapper, BugsSearchRepository bugsSearchRepository,
    		AttachmentsRepository attachmentsRepository) {
    	this.attachmentsRepository = attachmentsRepository;
        this.bugsRepository = bugsRepository;
        this.bugsMapper = bugsMapper;
        this.bugsSearchRepository = bugsSearchRepository;
    }
    
    @Transactional(readOnly = true)
    public Page<Bugs> findBugsByTaskId(Long taskId, Pageable page) {
        log.debug("find by Task ID");
        Page<Bugs> result = bugsRepository.findAllBugByTaskId(taskId, page);
        return result;
    }
    
    @Transactional(readOnly = true)
    public Integer countBugByUserAssign(Long taskId, String rowRV) {
        log.debug("count bugs by Task Assign");
        return bugsRepository.countBugByUserAssign(taskId, rowRV);
    }
    
    @Transactional(readOnly = true)
    public String countBugByUserAssignList(List<TasksDTO> tasksDTO, String userLogin) {
        log.debug("count bugs by Task Assign");
        Integer test = 0;
        Integer taskBug = 0;
        Boolean flag = true;
        for (TasksDTO task: tasksDTO) {
        	if (task.getOp().equalsIgnoreCase(userLogin)) {
        		if (bugsRepository.countBugByUserAssign(task.getId(), "review1") != 0 && flag) {
        			taskBug ++;
        			flag = false;
        		}
        		test += bugsRepository.countBugByUserAssign(task.getId(), "review1");
        	} else if (task.getReview1().equalsIgnoreCase(userLogin)) {
        		if (bugsRepository.countBugByUserAssign(task.getId(), "review1") != 0 && flag) {
        			taskBug ++;
        			flag = false;
        		}
        		test += bugsRepository.countBugByUserAssign(task.getId(), "review2");
        	} else if (task.getReview2().equalsIgnoreCase(userLogin)) {
        		if (bugsRepository.countBugByUserAssign(task.getId(), "review1") != 0 && flag) {
        			taskBug ++;
        			flag = false;
        		}
        		test += bugsRepository.countBugByUserAssign(task.getId(), "fi");
        	} else {}
        	flag = true;
        }
        return test.toString() + taskBug.toString();
    }
    
    @Transactional(readOnly = true)
    public Integer countBugOpenByTaskIdAndUserLog(Long taskId, String userLogin) {
        log.debug("count bugs by Task ID and create by user");
        return bugsRepository.countBugOpenByTaskIdAndUserLog(taskId, userLogin);
    }
    
    /**
     * Return a {@link List} of {@link BugsDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<BugsDTO> findByCriteria(BugsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Bugs> specification = createSpecification(criteria);
        List<Bugs> bugs = bugsRepository.findAll(specification);
        return bugsMapper.toDto(bugs);
    }
    
    /**
     * Return a {@link Page} of {@link BugsDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<BugsDTO> findByCriteria(BugsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Bugs> specification = createSpecification(criteria);
        final Page<Bugs> result = bugsRepository.findAll(specification, page);
        return result.map(bugsMapper::toDto);
    }
    
    /**
     * 
     * @param criteria
     * @return
     */
    private Specifications<Bugs> createSpecification(BugsCriteria criteria) {
        Specifications<Bugs> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Bugs_.id));
            }
            if (criteria.getTasksId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getTasksId(), Bugs_.tasks, Tasks_.id));
            }
            if (criteria.getStage() != null) {
                specification = specification.and(buildSpecification(criteria.getStage(), Bugs_.stage));
            }
        }
        return specification;
    }

	public List<BugsDTO> findBugsByTaskId(Long id) {
		List<Bugs> bugsList = this.bugsRepository.findBugsByTaskId(id);
		log.debug("BUGLISTTTTTT: ", bugsList);
		return bugsMapper.toDto(bugsList);
	}
	/**
	 * count Bug By TasksId
	 * @param id
	 * @return
	 * @author PhuVD3
	 */
	public int countBugByTasksId(Long id) {
		return this.bugsRepository.countBugByTasksId(id);
	}
	
	/**
	 * count Bug By TasksId RV1 or RV2 or FI
	 * @param id
	 * @return
	 * @author PhuVD3
	 */
	public int countBugRVFIByTasksId(Long id, String stage) {
		return this.bugsRepository.countBugRVFIByTasksId(id, stage);
	}
	
}
