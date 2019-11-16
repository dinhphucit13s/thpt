package fpt.dps.dtms.service;

import fpt.dps.dtms.domain.BugListDefault;
import fpt.dps.dtms.domain.DtmsMonitoring;
import fpt.dps.dtms.domain.ProjectUsers;
import fpt.dps.dtms.domain.Projects;
import fpt.dps.dtms.domain.enumeration.MONITORINGROLE;
import fpt.dps.dtms.domain.enumeration.PositionMonitoring;
import fpt.dps.dtms.domain.enumeration.ProjectRoles;
import fpt.dps.dtms.repository.BugListDefaultRepository;
import fpt.dps.dtms.repository.DtmsMonitoringRepository;
import fpt.dps.dtms.repository.ProjectUsersRepository;
import fpt.dps.dtms.repository.ProjectsRepository;
import fpt.dps.dtms.repository.search.DtmsMonitoringSearchRepository;
import fpt.dps.dtms.repository.search.ProjectsSearchRepository;
import fpt.dps.dtms.security.AuthoritiesConstants;
import fpt.dps.dtms.security.SecurityUtils;
import fpt.dps.dtms.service.dto.BugListDefaultDTO;
import fpt.dps.dtms.service.dto.DtmsMonitoringDTO;
import fpt.dps.dtms.service.dto.ProjectUsersCriteria;
import fpt.dps.dtms.service.dto.ProjectUsersDTO;
import fpt.dps.dtms.service.dto.ProjectWorkflowsCriteria;
import fpt.dps.dtms.service.dto.ProjectWorkflowsDTO;
import fpt.dps.dtms.service.dto.ProjectsDTO;
import fpt.dps.dtms.service.mapper.BugListDefaultMapper;
import fpt.dps.dtms.service.mapper.ProjectUsersMapper;
import fpt.dps.dtms.service.mapper.ProjectsMapper;
import fpt.dps.dtms.service.util.AppConstants;
import fpt.dps.dtms.service.util.ExcelGenerator;
import fpt.dps.dtms.web.rest.errors.BadRequestAlertException;
import fpt.dps.dtms.web.rest.errors.InternalServerErrorException;
import fpt.dps.dtms.web.rest.vm.ProjectBugListDefaultsVM;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import static org.elasticsearch.index.query.QueryBuilders.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Projects.
 */
@Service
@Transactional
public class ProjectsService {

    private final Logger log = LoggerFactory.getLogger(ProjectsService.class);

    private final ProjectsRepository projectsRepository;

    private final ProjectsMapper projectsMapper;
    
    private final BugListDefaultRepository bugListDefaultRepository;
    
    private final BugListDefaultMapper bugListDefaultMapper;

    private final ProjectsSearchRepository projectsSearchRepository;

    private final ProjectUsersService projectUsersService;

    private final ProjectUsersQueryService projectUsersQueryService;
    
    private final ProjectWorkflowsQueryService projectWorkflowsQueryService;
    
    private final ExcelGenerator excelGenerator;
    
    private final DtmsMonitoringRepository dtmsMonitoringRepository;
    
    private final DtmsMonitoringSearchRepository dtmsMonitoringSearchRepository;
    
    private final DtmsMonitoringService dtmsMonitoringService;
    
    private final ProjectUsersMapper projectUsersMapper;

    public ProjectsService(ProjectsRepository projectsRepository, ProjectsMapper projectsMapper, BugListDefaultMapper bugListDefaultMapper,
    		ProjectsSearchRepository projectsSearchRepository, BugListDefaultRepository bugListDefaultRepository, ExcelGenerator excelGenerator,
    		ProjectUsersService projectUsersService, ProjectUsersQueryService projectUsersQueryService, ProjectWorkflowsQueryService projectWorkflowsQueryService, 
    		DtmsMonitoringRepository dtmsMonitoringRepository, DtmsMonitoringSearchRepository dtmsMonitoringSearchRepository,
    		DtmsMonitoringService dtmsMonitoringService, ProjectUsersMapper projectUsersMapper) {
        this.projectsRepository = projectsRepository;
        this.projectsMapper = projectsMapper;
        this.bugListDefaultMapper = bugListDefaultMapper;
        this.projectsSearchRepository = projectsSearchRepository;
        this.bugListDefaultRepository = bugListDefaultRepository;
        this.projectUsersService = projectUsersService;
        this.projectUsersQueryService = projectUsersQueryService;
        this.projectWorkflowsQueryService = projectWorkflowsQueryService;
        this.excelGenerator = excelGenerator;
        this.dtmsMonitoringRepository = dtmsMonitoringRepository;
        this.dtmsMonitoringSearchRepository = dtmsMonitoringSearchRepository;
        this.dtmsMonitoringService = dtmsMonitoringService;
        this.projectUsersMapper = projectUsersMapper;
    }

    /**
     * Save a projects.
     *
     * @param projectsDTO the entity to save
     * @return the persisted entity
     */
    public ProjectsDTO save(ProjectsDTO projectsDTO) {
        log.debug("Request to save Projects : {}", projectsDTO);
        Projects projects = projectsMapper.toEntity(projectsDTO);
        projects = projectsRepository.save(projects);

        // Save Project Lead into Project Users

        ProjectUsersDTO projectUsersDTO = new ProjectUsersDTO();
        if(projectsDTO.getId() != null) {
        	//Process in case updating project
        	StringFilter userLoginFilter = new StringFilter();
        	userLoginFilter.setEquals(projectsDTO.getProjectLeadUserLogin());
        	LongFilter projectIdFilter = new LongFilter();
        	projectIdFilter.setEquals(projectsDTO.getId());
        	ProjectUsersCriteria projectUsersCriteria = new ProjectUsersCriteria();
        	projectUsersCriteria.setUserLogin(userLoginFilter);
        	projectUsersCriteria.setProjectId(projectIdFilter);
        	List<ProjectUsersDTO> projectUsersDTOs = this.projectUsersQueryService.findByCriteria(projectUsersCriteria);
        	if(projectUsersDTOs.size() > 0) {
        		projectUsersDTO = projectUsersDTOs.get(0);
        	}
        }

        projectUsersDTO.setUserLogin(projectsDTO.getProjectLeadUserLogin());
        projectUsersDTO.setRoleName(ProjectRoles.PM);
        projectUsersDTO.setProjectId(projects.getId());
        projectUsersDTO.setProjectName(projects.getName());
        
        LocalDate startDate = null;
        if(projectsDTO.getStartTime() != null) {
        	startDate = projectsDTO.getStartTime().atZone(ZoneId.systemDefault()).toLocalDate();
        }
        LocalDate endDate = null;
        if(projectsDTO.getEndTime() != null) {
        	endDate = projectsDTO.getEndTime().atZone(ZoneId.systemDefault()).toLocalDate();
        }
        
        projectUsersDTO.setStartDate(startDate);
        projectUsersDTO.setEndDate(endDate);
        projectUsersDTO.setEffortPlan(AppConstants.DEFAULT_EFFORT_PLAN);
        projectUsersDTO = this.projectUsersService.save(projectUsersDTO);
        ProjectUsers projectUser = projectUsersMapper.toEntity(projectUsersDTO);
        projects.setProjectLead(projectUser);
        projects = projectsRepository.save(projects);
        
        // Set dtms_monitoring
        // set watcherUsers
        if (projectsDTO.getWatcherUsers() != null) {
        	String[] watcherUsers = projectsDTO.getWatcherUsers();
        	
        	// get all watcherUser in project
        	List<DtmsMonitoringDTO> listWatcherUser = this.dtmsMonitoringService.getDtmsMonitoringUsers(PositionMonitoring.PROJECT, projects.getId(), MONITORINGROLE.ROLE_WATCHER);
        	
        	// get and Delete old WatcherUser 
        	List<DtmsMonitoringDTO> listWatcherUserDelete = listWatcherUser.stream().filter(us -> Arrays.stream(watcherUsers).noneMatch(us.getMembers()::equals)).collect(Collectors.toList());
        	for (DtmsMonitoringDTO dtmsMonitoringDTO : listWatcherUserDelete) {
				this.dtmsMonitoringService.delete(dtmsMonitoringDTO.getId());
			}
        	
        	// add new WatcherUser to User
        	List<String> newWatcherUsers = Arrays.stream(watcherUsers).filter(username -> listWatcherUser.stream().noneMatch(e -> e.getMembers().equals(username))).collect(Collectors.toList());     	
        	for (String userName : newWatcherUsers) {
				DtmsMonitoring dtmsMonitoring = new DtmsMonitoring();
				dtmsMonitoring.setPosition(PositionMonitoring.PROJECT);
				dtmsMonitoring.setPositionId(projects.getId());
				dtmsMonitoring.setRole(MONITORINGROLE.ROLE_WATCHER);
				dtmsMonitoring.setMembers(userName);
				System.out.println("Watcher: " + userName);
				dtmsMonitoring = dtmsMonitoringRepository.save(dtmsMonitoring);
				dtmsMonitoringSearchRepository.save(dtmsMonitoring);
			}
		}
        
        // set dedicatedUsers
        if (projectsDTO.getDedicatedUsers() != null) {
        	String[] dedicatedUsers = projectsDTO.getDedicatedUsers();
        	
        	// get all DedicatedUsers in project
        	List<DtmsMonitoringDTO> listDedicatedUser = this.dtmsMonitoringService.getDtmsMonitoringUsers(PositionMonitoring.PROJECT, projects.getId(), MONITORINGROLE.ROLE_DEDICATED);
        	
        	// get and Delete old WatcherUser 
        	List<DtmsMonitoringDTO> listDedicatedUserDelete = listDedicatedUser.stream().filter(us -> Arrays.stream(dedicatedUsers).noneMatch(us.getMembers()::equals)).collect(Collectors.toList());
        	for (DtmsMonitoringDTO dtmsMonitoringDTO : listDedicatedUserDelete) {
				this.dtmsMonitoringService.delete(dtmsMonitoringDTO.getId());
			}
        	
        	// add new WatcherUser to User
        	List<String> newDedicatedUsers = Arrays.stream(dedicatedUsers).filter(username -> listDedicatedUser.stream().noneMatch(e -> e.getMembers().equals(username))).collect(Collectors.toList());     	
        	for (String userName : newDedicatedUsers) {
				DtmsMonitoring dtmsMonitoring = new DtmsMonitoring();
				dtmsMonitoring.setPosition(PositionMonitoring.PROJECT);
				dtmsMonitoring.setPositionId(projects.getId());
				dtmsMonitoring.setRole(MONITORINGROLE.ROLE_DEDICATED);
				dtmsMonitoring.setMembers(userName);
				System.out.println("DedicatedUsers: " + userName);
				dtmsMonitoring = dtmsMonitoringRepository.save(dtmsMonitoring);
				dtmsMonitoringSearchRepository.save(dtmsMonitoring);
			}
		}

        ProjectsDTO result = projectsMapper.toDto(projects);
        projectsSearchRepository.save(projects);
        return result;
    }

    /**
     * Get all the projects.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ProjectsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Projects");
        return projectsRepository.findAll(pageable)
            .map(projectsMapper::toDto);
    }

    /**
     * Get one projects by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public ProjectsDTO findOne(Long id) {
        log.debug("Request to get Projects : {}", id);
        Projects projects = projectsRepository.findOne(id);
        return projectsMapper.toDto(projects);
    }

    /**
     * Delete the projects by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Projects : {}", id);
        projectsRepository.delete(id);
        projectsSearchRepository.delete(id);
    }

    /**
     * Search for the projects corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ProjectsDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Projects for query {}", query);
        final String userLogin = SecurityUtils.getCurrentUserLogin()
				.orElseThrow(() -> new InternalServerErrorException("Current user login not found"));
        Page<Projects> result = null;
        if (SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN)) {
        	result = projectsSearchRepository.findByNameLike(query, pageable);
        }
        else {
        	result = projectsSearchRepository.findByProjectLeadUserLoginAndNameLike(userLogin, query, pageable);
        }
        return result.map(projectsMapper::toDto);
    }

	public Page<BugListDefaultDTO> findBugListDefaultsByProjectId(Long id, Pageable pageable) {
		/*Page<BugListDefault> bugListDefaultDomain = projectsRepository.findBugListDefaultsByProjectId(id, pageable);
		return bugListDefaultDomain.map(bugListDefaultMapper::toDto);*/
		return null;
	}

	/**
	 * Conghk
	 * @param projectBugListDefaultsVM
	 * @return
	 */
	public ProjectsDTO updateBugListDefaultOfProject(ProjectBugListDefaultsVM projectBugListDefaultsVM) {
		Projects project = projectsRepository.findOne(projectBugListDefaultsVM.getProjectId());
		Set<BugListDefaultDTO> bugListDefaultDTOList = projectBugListDefaultsVM.getBugListDefaultList();
		if (project != null) {
			/*Set<BugListDefault> bugListDefaultList = project.getBugListDefaults();
			BugListDefault bugListDefault;
			for (BugListDefaultDTO bugListDefaultDto : bugListDefaultDTOList) {
				bugListDefault = bugListDefaultRepository.findOne(bugListDefaultDto.getId());
				if (bugListDefault != null) {
					bugListDefaultList.add(bugListDefault);
				}
			}
			project.setBugListDefaults(bugListDefaultList);*/
			projectsRepository.save(project);
		} else {
			throw new BadRequestAlertException("A new projects cannot already have an ID", "projects", "idexists");
		}
		return projectsMapper.toDto(project);
	}

	public void deleteProjectBugListDefaults(Long projectId, Long bugListDefaultId) {
		Projects project = projectsRepository.findOne(projectId);
		if (project != null) {
			/*Set<BugListDefault> bugListDefaultList = project.getBugListDefaults();
			BugListDefault bugListDefault = bugListDefaultRepository.findOne(bugListDefaultId);
			if(bugListDefault != null) {
				bugListDefaultList.remove(bugListDefault);
				project.setBugListDefaults(bugListDefaultList);
				projectsRepository.save(project);
			} else {
				throw new BadRequestAlertException("A bug cannot already have an ID", "bug list defaults", "unexist");
			}*/
		} else {
			throw new BadRequestAlertException("A projects cannot already have an ID", "projects", "unexist");
		}
	}

	public Page<BugListDefaultDTO> findBugListDefaultsByProjectIdFollowSearch(String searchValue, Long proId,
			Pageable pageable) {
		/*Page<BugListDefault> bugListDefaultDomain = projectsRepository.findBugListDefaultsByProjectIdFollow(searchValue, proId, pageable);
		return bugListDefaultDomain.map(bugListDefaultMapper::toDto);*/
		return null;
	}
	
	/**
	 * Create excel file
	 * 
	 * @param projectId
	 * @throws IOException
	 */
	public InputStreamResource exportExcel(Long projectId) throws IOException {
		Projects project = projectsRepository.findOne(projectId);
		if(project != null) {
			ProjectWorkflowsCriteria criteria = new ProjectWorkflowsCriteria();
			LongFilter filter = new LongFilter();
			filter.setEquals(project.getProjectTemplates().getId());
			criteria.setProjectTemplatesId(filter);
			List<ProjectWorkflowsDTO> projectWorkflowsDTOs = this.projectWorkflowsQueryService.findByCriteria(criteria);
			//String[] columns = getColumns(projectWorkflowsDTOs);
			if (!CollectionUtils.isEmpty(projectWorkflowsDTOs)) {
				ByteArrayInputStream in = excelGenerator.taskManagementToExcel(projectWorkflowsDTOs, project.getId());
				return new InputStreamResource(in);
			} else {
				return null;
			}
		}else {
			return null;
		}
		
	}

	// get Project feedback "TM_CAMPAIGN"
	public ProjectsDTO getFeedbackProject() {
		Projects project = this.projectsRepository.findByCode("TM_CAMPAIGN");
		return this.projectsMapper.toDto(project);
	}
}
