package fpt.dps.dtms.service;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fpt.dps.dtms.domain.BusinessUnit_;
// for static metamodels
import fpt.dps.dtms.domain.Customer_;
import fpt.dps.dtms.domain.DtmsMonitoring_;
import fpt.dps.dtms.domain.ProjectBugListDefault_;
import fpt.dps.dtms.domain.ProjectTemplates_;
import fpt.dps.dtms.domain.ProjectUsers_;
import fpt.dps.dtms.domain.Projects;
import fpt.dps.dtms.domain.Projects_;
import fpt.dps.dtms.domain.PurchaseOrders_;
import fpt.dps.dtms.domain.Tasks;
import fpt.dps.dtms.domain.User;
import fpt.dps.dtms.domain.enumeration.FixStatus;
import fpt.dps.dtms.domain.enumeration.MONITORINGROLE;
import fpt.dps.dtms.domain.enumeration.OPStatus;
import fpt.dps.dtms.domain.enumeration.PositionMonitoring;
import fpt.dps.dtms.domain.enumeration.ProjectRoles;
import fpt.dps.dtms.domain.enumeration.ProjectStatus;
import fpt.dps.dtms.domain.enumeration.ReviewStatus;
import fpt.dps.dtms.repository.ProjectsRepository;
import fpt.dps.dtms.repository.TasksRepository;
import fpt.dps.dtms.service.dto.DtmsMonitoringDTO;
import fpt.dps.dtms.service.dto.ProjectsCriteria;
import fpt.dps.dtms.service.dto.ProjectsDTO;
import fpt.dps.dtms.service.mapper.ProjectsMapper;
import io.github.jhipster.service.QueryService;

/**
 * Service for executing complex queries for Projects entities in the database.
 * The main input is a {@link ProjectsCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ProjectsDTO} or a {@link Page} of {@link ProjectsDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProjectsQueryService extends QueryService<Projects> {

    private final Logger log = LoggerFactory.getLogger(ProjectsQueryService.class);


    private final ProjectsRepository projectsRepository;

    private final ProjectsMapper projectsMapper;

    private final TasksRepository tasksRepository;

    boolean hasProjectIncludeTaskIsDoing = false;
    
    private final DtmsMonitoringService dtmsMonitoringService;

    public ProjectsQueryService(ProjectsRepository projectsRepository, ProjectsMapper projectsMapper, TasksRepository tasksRepository, DtmsMonitoringService dtmsMonitoringService) {
        this.projectsRepository = projectsRepository;
        this.projectsMapper = projectsMapper;
        this.tasksRepository = tasksRepository;
        this.dtmsMonitoringService = dtmsMonitoringService;
    }

    /**
     * Return a {@link List} of {@link ProjectsDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ProjectsDTO> findByCriteria(ProjectsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Projects> specification = createSpecification(criteria);
        return projectsMapper.toDto(projectsRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ProjectsDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ProjectsDTO> findByCriteria(ProjectsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Projects> specification = createSpecification(criteria);
        final Page<Projects> result = projectsRepository.findAll(specification, page);
        return result.map(projectsMapper::toDto);
    }

    /**
     * get all project by user_login
     * */
    @Transactional(readOnly = true)
    public List<ProjectsDTO> findByUserLoginAndRoleUser(String userLogin) {
        log.debug("find by user_login");
        return projectsMapper.toDto(projectsRepository.findByUserLoginAndRoleUser(userLogin));
    }

    /**
     * get all project by user_login and role PM
     * */
    @Transactional(readOnly = true)
    public ProjectsDTO findByUserLoginAndRolePM(String userLogin, Long projectId) {
        log.debug("find by user_login");
        return projectsMapper.toDto(projectsRepository.findByUserLoginAndRolePM(userLogin, projectId));
    }

    /**
     * @author ngocvx1
     * get all project by user_login role OP, REVIEWER, FI
     * */
    @Transactional(readOnly = true)
    public List<ProjectsDTO> findByUserLoginAndRoles(String userLogin) {
        log.debug("find by user_login");
        
        LocalDate today = LocalDate.now();

        List<ProjectStatus> status = new ArrayList<ProjectStatus>();
    	status.add(ProjectStatus.OPEN);
    	status.add(ProjectStatus.RUNNING);

    	List<ProjectRoles> roles = new ArrayList<ProjectRoles>();
    	roles.add(ProjectRoles.OPERATOR);
    	roles.add(ProjectRoles.REVIEWER);
    	roles.add(ProjectRoles.FI);
    	roles.add(ProjectRoles.PM);
    	roles.add(ProjectRoles.TEAMLEAD);

    	List<ProjectsDTO> projectsDTOs = projectsMapper.toDto(projectsRepository.findByUserLoginAndRoles(userLogin, status, roles, today));
    	hasProjectIncludeTaskIsDoing = false;
    	projectsDTOs = projectsDTOs.stream().map(p ->{
    		if(!hasProjectIncludeTaskIsDoing) {
	    		int tasks = tasksRepository.countTasksIsDoingInProject(userLogin, p.getId());
	    		if(tasks > 0) {
	    			p.setHasDoingTask(true);
	    			hasProjectIncludeTaskIsDoing = true;
	    		}
    		}
			return p;
		}).collect(Collectors.toList());
    	return projectsDTOs;
    }

    @Transactional(readOnly = true)
    public Page<ProjectsDTO> getAllProjectWithMonitoring(ProjectsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Projects> specification = createSpecification(criteria);
        List<ProjectsDTO> listProjectsDTO = new ArrayList<>();
        
        // get list Project that userLogin is PM
        List<Projects> projectsOfUser = new ArrayList<Projects>();
        projectsOfUser = projectsRepository.findAll(specification);
        if (projectsOfUser != null) {
        	listProjectsDTO.addAll(projectsMapper.toDto(projectsOfUser));        	
        }
        
        String userLogin  = criteria.getProjectLeadUserLogin().getEquals();
        List<DtmsMonitoringDTO> monitoring = dtmsMonitoringService.getDtmsMonitoringByUserLogin(PositionMonitoring.PROJECT, userLogin);
        for (DtmsMonitoringDTO dtmsMonitoringDTO : monitoring) {
        	// projectsOfUser.add(projectsRepository.findOne(dtmsMonitoringDTO.getPositionId()));
        	Projects projects = projectsRepository.findOne(dtmsMonitoringDTO.getPositionId());
        	if ( projects != null) {
	        	ProjectsDTO projectsDTO = projectsMapper.toDto(projects);
	        	String[] watcherUsers = dtmsMonitoringService.getArraysDtmsMonitoringUsers(PositionMonitoring.PROJECT, projectsDTO.getId(), MONITORINGROLE.ROLE_WATCHER);
	    		projectsDTO.setWatcherUsers(watcherUsers);
	    		
	    		String[] dedicatedUsers = dtmsMonitoringService.getArraysDtmsMonitoringUsers(PositionMonitoring.PROJECT, projectsDTO.getId(), MONITORINGROLE.ROLE_DEDICATED);
	    		projectsDTO.setDedicatedUsers(dedicatedUsers);
	    		
	    		listProjectsDTO.add(projectsDTO);
        	}
		}
        
        Page<ProjectsDTO> pageProjects = new PageImpl<ProjectsDTO>(listProjectsDTO, page, listProjectsDTO.size());
        
        return pageProjects;
    }
    
    /**
     * get all project by user_login and Role for PO
     * */
    @Transactional(readOnly = true)
    public List<ProjectsDTO> findByUserLoginAndRoleUserForPO(String userLogin) {
        log.debug("find by user_login");  
        List<Projects> projects = new ArrayList<Projects>();
        projects = projectsRepository.findByUserLoginForPO(userLogin);
        
        // district list Projects
        projects = projects.stream().distinct().collect(Collectors.toList());
        return projectsMapper.toDto(projects);
    }
    
    /**
     * @author ngocvx1
     * get all project by user_login role OP
     * */
    @Transactional(readOnly = true)
    public List<ProjectsDTO> findByUserLoginAndRoleOP(String userLogin) {
        log.debug("find by user_login and roles OP");
        List<ProjectStatus> status = new ArrayList<ProjectStatus>();
    	status.add(ProjectStatus.OPEN);
    	status.add(ProjectStatus.RUNNING);
        return projectsMapper.toDto(projectsRepository.findByUserLoginAndRolesOP(userLogin, status));
    }

    /**
     * Function to convert ProjectsCriteria to a {@link Specifications}
     */
    private Specifications<Projects> createSpecification(ProjectsCriteria criteria) {
        Specifications<Projects> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Projects_.id));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), Projects_.code));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Projects_.name));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildSpecification(criteria.getType(), Projects_.type));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), Projects_.status));
            }
            if (criteria.getStartTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStartTime(), Projects_.startTime));
            }
            if (criteria.getEndTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEndTime(), Projects_.endTime));
            }
            if (criteria.getProjectTemplatesId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getProjectTemplatesId(), Projects_.projectTemplates, ProjectTemplates_.id));
            }
            if (criteria.getProjectLeadId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getProjectLeadId(), Projects_.projectLead, ProjectUsers_.id));
            }
            if (criteria.getProjectLeadUserLogin() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getProjectLeadUserLogin(), Projects_.projectLead, ProjectUsers_.userLogin));
            }
            if (criteria.getPurchaseOrdersId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getPurchaseOrdersId(), Projects_.purchaseOrders, PurchaseOrders_.id));
            }
            if (criteria.getProjectUsersId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getProjectUsersId(), Projects_.projectUsers, ProjectUsers_.id));
            }
            if (criteria.getCustomerId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getCustomerId(), Projects_.customer, Customer_.id));
            }
            if (criteria.getProjectBugListDefaultId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getProjectBugListDefaultId(), Projects_.projectBugListDefaults, ProjectBugListDefault_.id));
            }
            if (criteria.getBusinessUnitId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getBusinessUnitId(), Projects_.businessUnit, BusinessUnit_.id));
            }
        }
        return specification;
    }

	public List<ProjectsDTO> findListProjectBiddingTaskOP(String userLogin, String modeBidding) {
		List<Projects> list = this.projectsRepository.findListProjectBiddingTaskOP(userLogin, modeBidding);
		return this.projectsMapper.toDto(list);
	}

	public List<ProjectsDTO> findListProjectBiddingTaskPM(String userLogin) {
		List<Projects> list = this.projectsRepository.findListProjectBiddingTaskPM(userLogin);
		return this.projectsMapper.toDto(list);
	}

	public List<ProjectsDTO> findByBusinessUnit(Long buId) {
        List<ProjectStatus> status = new ArrayList<ProjectStatus>();
    	status.add(ProjectStatus.OPEN);
    	status.add(ProjectStatus.RUNNING);
    	
    	return projectsMapper.toDto(this.projectsRepository.findByBusinessUnit(buId, status));
	}

}
