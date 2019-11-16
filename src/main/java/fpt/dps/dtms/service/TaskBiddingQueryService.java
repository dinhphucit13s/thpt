package fpt.dps.dtms.service;


import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;
import fpt.dps.dtms.domain.*; // for static metamodels
import fpt.dps.dtms.repository.TaskBiddingRepository;
import fpt.dps.dtms.repository.TasksRepository;
import fpt.dps.dtms.repository.search.TaskBiddingSearchRepository;
import fpt.dps.dtms.service.dto.TaskBiddingCriteria;

import fpt.dps.dtms.service.dto.TaskBiddingDTO;
import fpt.dps.dtms.service.dto.TasksDTO;
import fpt.dps.dtms.service.mapper.TaskBiddingMapper;
import fpt.dps.dtms.service.util.FieldConfigService;
import fpt.dps.dtms.domain.enumeration.BiddingScope;
import fpt.dps.dtms.domain.enumeration.BiddingStatus;
import fpt.dps.dtms.domain.enumeration.FIStatus;
import fpt.dps.dtms.domain.enumeration.FixStatus;
import fpt.dps.dtms.domain.enumeration.OPStatus;
import fpt.dps.dtms.domain.enumeration.ReviewStatus;

/**
 * Service for executing complex queries for TaskBidding entities in the database.
 * The main input is a {@link TaskBiddingCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TaskBiddingDTO} or a {@link Page} of {@link TaskBiddingDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TaskBiddingQueryService extends QueryService<TaskBidding> {

    private final Logger log = LoggerFactory.getLogger(TaskBiddingQueryService.class);


    private final TaskBiddingRepository taskBiddingRepository;

    private final TaskBiddingMapper taskBiddingMapper;

    private final TaskBiddingSearchRepository taskBiddingSearchRepository;

    public TaskBiddingQueryService(TaskBiddingRepository taskBiddingRepository, TaskBiddingMapper taskBiddingMapper,
    		TaskBiddingSearchRepository taskBiddingSearchRepository, FieldConfigService fieldConfigService) {
        this.taskBiddingRepository = taskBiddingRepository;
        this.taskBiddingMapper = taskBiddingMapper;
        this.taskBiddingSearchRepository = taskBiddingSearchRepository;
    }

    /**
     * Return a {@link List} of {@link TaskBiddingDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TaskBiddingDTO> findByCriteria(TaskBiddingCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<TaskBidding> specification = createSpecification(criteria);
        return taskBiddingMapper.toDto(taskBiddingRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link TaskBiddingDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TaskBiddingDTO> findByCriteria(TaskBiddingCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<TaskBidding> specification = createSpecification(criteria);
        final Page<TaskBidding> result = taskBiddingRepository.findAll(specification, page);
        return result.map(taskBiddingMapper::toDto);
    }
    
    @Transactional(readOnly = true)
    public List<TaskBiddingDTO> getBiddingTasksOP(String userLogin) {
    	List<OPStatus> statusOP = new ArrayList<OPStatus>();
        statusOP.add(OPStatus.OPEN);
        statusOP.add(OPStatus.DOING);
        statusOP.add(OPStatus.PENDING);
        statusOP.add(OPStatus.REOPEN);
        
        List<FixStatus> statusFix = new ArrayList<FixStatus>();
        statusFix.add(FixStatus.OPEN);
        statusFix.add(FixStatus.DOING);
        statusFix.add(FixStatus.PENDING);
        statusFix.add(FixStatus.REOPEN);
        
        List<TaskBidding> taskBiddingDomain = this.taskBiddingRepository.getBiddingTaskOPAndFixByUser(userLogin, statusOP, statusFix);
        return this.taskBiddingMapper.toDto(taskBiddingDomain);
    };
    
    /**
     * Get List task bidding by userLogin and round
     * @param userLogin
     * @param round
     * @return List task bidding
     */
    @Transactional(readOnly = true)
    public List<TaskBiddingDTO> getAllBiddingTasksOfUserByRole(String userLogin, String round) {
    	List<TaskBidding> tasksBidding = new ArrayList<>();
    	
    	List<OPStatus> statusOP = new ArrayList<OPStatus>();
        statusOP.add(OPStatus.OPEN);
        statusOP.add(OPStatus.DOING);
        statusOP.add(OPStatus.PENDING);
        statusOP.add(OPStatus.REOPEN);
        
        List<FixStatus> statusFix = new ArrayList<FixStatus>();
        statusFix.add(FixStatus.OPEN);
        statusFix.add(FixStatus.DOING);
        statusFix.add(FixStatus.PENDING);
        statusFix.add(FixStatus.REOPEN);
        
        List<ReviewStatus> statusReview = new ArrayList<ReviewStatus>();
        statusReview.add(ReviewStatus.OPEN);
        statusReview.add(ReviewStatus.DOING);
        statusReview.add(ReviewStatus.REOPEN);
        statusReview.add(ReviewStatus.PENDING);
        
        List<FIStatus> statusFI = new ArrayList<FIStatus>();
        statusFI.add(FIStatus.OPEN);
        statusFI.add(FIStatus.DOING);
        statusFI.add(FIStatus.PENDING);
        
    	switch (round) {
		case "OPERATOR":
			tasksBidding = taskBiddingRepository.getBiddingTaskOPAndFixByUser(userLogin, statusOP, statusFix);
			break;
		case "REVIEWER":
			tasksBidding = taskBiddingRepository.getBiddingTaskRVByUser(userLogin, statusReview);
			break;
		case "FI":
			tasksBidding = taskBiddingRepository.getBiddingTaskFIByUser(userLogin, statusFI);
			break;
		default:
			break;
			}
    	
//    	List<TaskBiddingDTO> listTaskBiddingDTO = this.taskBiddingMapper.toDto(tasksBidding);
//    	switch (round) {
//		case "OPERATOR":
//			for (TaskBiddingDTO taskBiddingDTO : listTaskBiddingDTO) {
//				//taskBiddingDTO.setStep("OP");
//				taskBiddingDTO.setPic(userLogin);
//				taskBiddingDTO.setStartDate(taskBiddingDTO.getTask().getOpStartTime());
//				taskBiddingDTO.setEndDate(taskBiddingDTO.getTask().getOpEndTime());
//			}
//			break;
//		case "REVIEWER":
//			for (TaskBiddingDTO taskBiddingDTO : listTaskBiddingDTO) {
//				taskBiddingDTO.setPic(userLogin);
//				if (userLogin.equalsIgnoreCase(taskBiddingDTO.getTask().getReview1())) {
//					//taskBiddingDTO.setStep("REVIEW1");
//					taskBiddingDTO.setStartDate(taskBiddingDTO.getTask().getReview1StartTime());
//					taskBiddingDTO.setEndDate(taskBiddingDTO.getTask().getReview1EndTime());
//				} else if (userLogin.equalsIgnoreCase(taskBiddingDTO.getTask().getReview2())) {
//					//taskBiddingDTO.setStep("REVIEW2");
//					taskBiddingDTO.setStartDate(taskBiddingDTO.getTask().getReview2StartTime());
//					taskBiddingDTO.setEndDate(taskBiddingDTO.getTask().getReview2EndTime());
//				}
//			}
//			break;
//		case "FI":
//			for (TaskBiddingDTO taskBiddingDTO : listTaskBiddingDTO) {
//				//taskBiddingDTO.setStep("FI");
//				taskBiddingDTO.setPic(userLogin);
//				taskBiddingDTO.setStartDate(taskBiddingDTO.getTask().getFiStartTime());
//				taskBiddingDTO.setEndDate(taskBiddingDTO.getTask().getFiEndTime());
//			}
//			break;
//    	}
    	
    	return this.taskBiddingMapper.toDto(tasksBidding);
    }
    
    /**
     * Function to convert TaskBiddingCriteria to a {@link Specifications}
     */
    private Specifications<TaskBidding> createSpecification(TaskBiddingCriteria criteria) {
        Specifications<TaskBidding> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), TaskBidding_.id));
            }
            if (criteria.getBiddingScope() != null) {
                specification = specification.and(buildSpecification(criteria.getBiddingScope(), TaskBidding_.biddingScope));
            }
            if (criteria.getBiddingStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getBiddingStatus(), TaskBidding_.biddingStatus));
            }
            if (criteria.getTaskId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getTaskId(), TaskBidding_.task, Tasks_.id));
            }
        }
        return specification;
    }

	public Page<TaskBiddingDTO> findAllTasksBiddingByMode(Long projectId, Long poId, Long packageId, String modeBidding, String userLogin,
			Pageable pageable) {
		List<BiddingStatus> biddingStatus = new ArrayList<BiddingStatus>();
		biddingStatus.add(BiddingStatus.NA);
		biddingStatus.add(BiddingStatus.HOLDING);
		
		Page<TaskBidding> page = this.taskBiddingRepository.findAllTasksBiddingByMode(projectId, poId, packageId, modeBidding,
				biddingStatus, userLogin, pageable);
		return page.map(taskBiddingMapper::toDto);
	}
}
