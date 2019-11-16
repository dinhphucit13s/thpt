package fpt.dps.dtms.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import fpt.dps.dtms.domain.ProjectUsers;
import fpt.dps.dtms.domain.Projects;
import fpt.dps.dtms.domain.*; // for static metamodels
import fpt.dps.dtms.repository.ProjectUsersRepository;
import fpt.dps.dtms.repository.search.ProjectUsersSearchRepository;
import fpt.dps.dtms.repository.PurchaseOrdersRepository;
import fpt.dps.dtms.repository.UserRepository;
import fpt.dps.dtms.service.dto.ProjectUsersCriteria;

import fpt.dps.dtms.service.dto.ProjectUsersDTO;
import fpt.dps.dtms.service.dto.SelectDTO;
import fpt.dps.dtms.service.dto.ProjectsDTO;
import fpt.dps.dtms.service.mapper.ProjectsMapper;
import fpt.dps.dtms.service.mapper.ProjectUsersMapper;
import fpt.dps.dtms.domain.enumeration.ProjectRoles;

import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Collectors;

/**
 * Service for executing complex queries for ProjectUsers entities in the database.
 * The main input is a {@link ProjectUsersCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ProjectUsersDTO} or a {@link Page} of {@link ProjectUsersDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProjectUsersQueryService extends QueryService<ProjectUsers> {

    private final Logger log = LoggerFactory.getLogger(ProjectUsersQueryService.class);


    private final ProjectUsersRepository projectUsersRepository;

    private final ProjectUsersMapper projectUsersMapper;
    
    private final TaskTrackingTimeQueryService taskTrackingTimeQueryService;
    
    private final UserProfileService userProfileService;
    
    private final ProjectsMapper projectsMapper;
    
    private final UserRepository userRepository;

    private final PurchaseOrdersRepository purchaseOrdersRepository;
    
    private final ProjectUsersSearchRepository projectUsersSearchRepository;

    public ProjectUsersQueryService(ProjectUsersRepository projectUsersRepository, ProjectUsersMapper projectUsersMapper, ProjectsMapper projectsMapper, UserRepository userRepository,
    		ProjectUsersSearchRepository projectUsersSearchRepository, PurchaseOrdersRepository purchaseOrdersRepository, TaskTrackingTimeQueryService taskTrackingTimeQueryService, UserProfileService userProfileService) {
        this.projectUsersRepository = projectUsersRepository;
        this.projectUsersMapper = projectUsersMapper;
        this.projectsMapper = projectsMapper;
        this.projectUsersSearchRepository = projectUsersSearchRepository;
        this.purchaseOrdersRepository = purchaseOrdersRepository;
        this.taskTrackingTimeQueryService = taskTrackingTimeQueryService;
        this.userRepository = userRepository;
        this.userProfileService = userProfileService;
    }

    /**
     * Return a {@link List} of {@link ProjectUsersDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ProjectUsersDTO> findByCriteria(ProjectUsersCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<ProjectUsers> specification = createSpecification(criteria);
        return projectUsersMapper.toDto(projectUsersRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ProjectUsersDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ProjectUsersDTO> findByCriteria(ProjectUsersCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<ProjectUsers> specification = createSpecification(criteria);
        final Page<ProjectUsers> result = projectUsersRepository.findAll(specification, page);
        return result.map(projectUsersMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<ProjectUsersDTO> findByProjectIdAndExcludeSpecifyUser(Long projectID, Pageable page) {
    	
        final Page<ProjectUsers> result = projectUsersRepository.findByProjectIdAndExcludeSpecifyUser(projectID, page);
        return result.map(projectUsersMapper::toDto);
    }
    
    @Transactional(readOnly = true)
    public List<ProjectUsersDTO> getAllMemberByProjectId(Long projectId) {
    	return projectUsersMapper.toDto(projectUsersRepository.getAllUsersForSelects(projectId));
    }
    
    @Transactional(readOnly = true)
    public Page<ProjectUsersDTO> getAllUsersByPrjectId(Long projectId, Pageable page) {
    	return projectUsersRepository.getAllUsersByPrjectId(projectId, page).map(projectUsersMapper::toDto);
    }
    
    @Transactional(readOnly = true)
    public List<SelectDTO> getAllUsersForSelects(Long poID) {
    	PurchaseOrders purchaseOrders = purchaseOrdersRepository.findOne(poID);
		Projects project = purchaseOrders.getProject();
    	List<ProjectUsers> userList = projectUsersRepository.getAllUsersForSelects(project.getId());
    	List<SelectDTO> result = userList.stream().map(user ->{
        	SelectDTO obj = new SelectDTO();
        	obj.setId(user.getId());
        	obj.setName(user.getUserLogin());
        	return obj;
        }).collect(Collectors.toList());
        return result;
    }
  
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getAllUserAllocation(Long id, Pageable page) {
    	Page<ProjectUsers> projectUsers = projectUsersRepository.getAllUsersByPrjectId(id, page);
    	List<ProjectUsers> listProjectUsers = projectUsers.getContent();
    	List<Map<String, Object>> result = listProjectUsers.stream().map(user ->{
    		Map<String, Object> object = new HashMap<String, Object>();
    		User userContract = userRepository.findByLogin(user.getUserLogin());
    		if (userContract != null) {
    			object.put("contractType", userContract.getContractType());
    			UserProfile userProfile = userProfileService.findByUserId(userContract.getId());
    			if (userProfile != null) {
    				object.put("workingLocation", userProfile.getWorkingLocation());
    			} else {
    				object.put("workingLocation", "");
    			}
    		} else {
    			object.put("workingLocation", "");
    			object.put("contractType", "");
    		}
    		object.put("id", user.getId());
    		object.put("userLogin", user.getUserLogin());
    		object.put("roleName", user.getRoleName());
    		object.put("effortPlan", user.getEffortPlan());
    		object.put("startDate", user.getStartDate());
    		object.put("endDate", user.getEndDate());
    		object.put("totalEffortPlan", getDatesBetween(user.getStartDate(), user.getEndDate(), user.getEffortPlan()));
    		object.put("workingEffort", taskTrackingTimeQueryService.sumActualEffort(id, user.getUserLogin(), user.getStartDate(), user.getEndDate()));
    		
    		return object;
    	}).collect(Collectors.toList());
    	Map<String, Object> totalPages = new HashMap<String, Object>();
        totalPages.put("total", projectUsers.getTotalElements());
        result.add(totalPages);
        return result;
    }
    
    public Float getDatesBetween(LocalDate startDate, LocalDate endDate, Float effort) {
        long numOfDaysBetween = ChronoUnit.DAYS.between(startDate, endDate) + 1;
        List<LocalDate> listDate = IntStream.iterate(0, i -> i + 1)
          .limit(numOfDaysBetween)
          .mapToObj(i -> startDate.plusDays(i))
          .collect(Collectors.toList());
        for(LocalDate localDate:listDate) {
    		Date date = java.sql.Date.valueOf(localDate);
    		if (date.getDay() == 0 || date.getDay() == 6) {
    			numOfDaysBetween --;
    		}
    	}
        return numOfDaysBetween*effort;
    }
    
    @Transactional(readOnly = true)
    public Page<ProjectUsersDTO> findHistoryByUserLogin(String userLogin, Pageable page) {
        final Page<ProjectUsers> result = projectUsersRepository.findHistoryByUserLogin(userLogin, page);
        return result.map(projectUsersMapper::toDto);
    }
    
    /**
     * get all project by user_login and role TEAMLEAD
     * */
    @Transactional(readOnly = true)
    public ProjectUsersDTO findByUserLoginAndRoleTEAMLEAD(String userLogin, Long projectId) {
        log.debug("find by user_login");
        return projectUsersMapper.toDto(projectUsersRepository.findByUserLoginAndRoleTEAMLEAD(userLogin, projectId));
    }
    
    @Transactional(readOnly = true)
    public List<ProjectUsersDTO> findByProjectIdAndExcludeSpecifyUserRole(Long projectID) {
//    	final List<ProjectUsers> result = projectUsersRepository.findByProjectIdAndExcludeSpecifyUserRole(projectID);
//        return result.map(projectUsersMapper::toDto);
        return projectUsersMapper.toDto(projectUsersRepository.findByProjectIdAndExcludeSpecifyUserRole(projectID));
    }
    
    @Transactional(readOnly = true)
    public List<SelectDTO> findByProjectIdAndExcludeSpecifyUserRoleForSelect(Long projectID) {
        List<ProjectUsers> userList = projectUsersRepository.findByProjectIdAndExcludeSpecifyUserRole(projectID);
        List<SelectDTO> result = userList.stream().map(user ->{
        	SelectDTO obj = new SelectDTO();
        	obj.setId(user.getId());
        	obj.setName(user.getUserLogin());
        	return obj;
        }).collect(Collectors.toList());
        return result;
    }
    
    /**
     * Function to convert ProjectUsersCriteria to a {@link Specifications}
     */
    private Specifications<ProjectUsers> createSpecification(ProjectUsersCriteria criteria) {
        Specifications<ProjectUsers> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), ProjectUsers_.id));
            }
            if (criteria.getUserLogin() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUserLogin(), ProjectUsers_.userLogin));
            }
            if (criteria.getRoleName() != null) {
                specification = specification.and(buildSpecification(criteria.getRoleName(), ProjectUsers_.roleName));
            }
            if (criteria.getProjectId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getProjectId(), ProjectUsers_.project, Projects_.id));
            }
        }
        return specification;
    }

}
