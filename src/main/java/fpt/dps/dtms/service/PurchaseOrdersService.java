package fpt.dps.dtms.service;

import fpt.dps.dtms.domain.DtmsMonitoring;
import fpt.dps.dtms.domain.ProjectTemplates;
import fpt.dps.dtms.domain.ProjectUsers;
import fpt.dps.dtms.domain.Projects;
import fpt.dps.dtms.domain.PurchaseOrders;
import fpt.dps.dtms.domain.enumeration.MONITORINGROLE;
import fpt.dps.dtms.domain.enumeration.PositionMonitoring;
import fpt.dps.dtms.repository.DtmsMonitoringRepository;
import fpt.dps.dtms.repository.ProjectUsersRepository;
import fpt.dps.dtms.repository.ProjectsRepository;
import fpt.dps.dtms.repository.PurchaseOrdersRepository;
import fpt.dps.dtms.repository.search.DtmsMonitoringSearchRepository;
import fpt.dps.dtms.repository.search.PurchaseOrdersSearchRepository;
import fpt.dps.dtms.security.AuthoritiesConstants;
import fpt.dps.dtms.security.SecurityUtils;
import fpt.dps.dtms.service.dto.DtmsMonitoringDTO;
import fpt.dps.dtms.service.dto.ProjectUsersDTO;
import fpt.dps.dtms.service.dto.ProjectWorkflowsCriteria;
import fpt.dps.dtms.service.dto.ProjectWorkflowsDTO;
import fpt.dps.dtms.service.dto.ProjectsDTO;
import fpt.dps.dtms.service.dto.PurchaseOrdersDTO;
import fpt.dps.dtms.service.mapper.PurchaseOrdersMapper;
import fpt.dps.dtms.service.util.ExcelGenerator;
import fpt.dps.dtms.web.rest.errors.InternalServerErrorException;
import io.github.jhipster.service.filter.LongFilter;

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
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.elasticsearch.index.query.QueryStringQueryBuilder;

/**
 * Service Implementation for managing PurchaseOrders.
 */
@Service
@Transactional
public class PurchaseOrdersService {

    private final Logger log = LoggerFactory.getLogger(PurchaseOrdersService.class);

    private final PurchaseOrdersRepository purchaseOrdersRepository;

    private final PurchaseOrdersMapper purchaseOrdersMapper;

    private final PurchaseOrdersSearchRepository purchaseOrdersSearchRepository;
    
    private final ProjectUsersRepository projectUsersRepository;
    
    private final DtmsMonitoringRepository dtmsMonitoringRepository;
    
    private final DtmsMonitoringSearchRepository dtmsMonitoringSearchRepository;
    
    private final DtmsMonitoringService dtmsMonitoringService;
    
    private final ProjectWorkflowsQueryService projectWorkflowsQueryService;
    
    private final ExcelGenerator excelGenerator;

    public PurchaseOrdersService(PurchaseOrdersRepository purchaseOrdersRepository, PurchaseOrdersMapper purchaseOrdersMapper, PurchaseOrdersSearchRepository purchaseOrdersSearchRepository,
    		ProjectUsersRepository projectUsersRepository, DtmsMonitoringService dtmsMonitoringService, DtmsMonitoringRepository dtmsMonitoringRepository, DtmsMonitoringSearchRepository dtmsMonitoringSearchRepository,
    		ProjectWorkflowsQueryService projectWorkflowsQueryService, ExcelGenerator excelGenerator) {
        this.purchaseOrdersRepository = purchaseOrdersRepository;
        this.purchaseOrdersMapper = purchaseOrdersMapper;
        this.purchaseOrdersSearchRepository = purchaseOrdersSearchRepository;
        this.projectUsersRepository = projectUsersRepository;
        this.dtmsMonitoringService = dtmsMonitoringService;
        this.dtmsMonitoringRepository = dtmsMonitoringRepository;
        this.dtmsMonitoringSearchRepository = dtmsMonitoringSearchRepository;
        this.projectWorkflowsQueryService = projectWorkflowsQueryService;
        this.excelGenerator = excelGenerator;
    }

    /**
     * Save a purchaseOrders.
     *
     * @param purchaseOrdersDTO the entity to save
     * @return the persisted entity
     */
    public PurchaseOrdersDTO save(PurchaseOrdersDTO purchaseOrdersDTO) {
        log.debug("Request to save PurchaseOrders : {}", purchaseOrdersDTO);
        PurchaseOrders purchaseOrders = purchaseOrdersMapper.toEntity(purchaseOrdersDTO);
        purchaseOrders = purchaseOrdersRepository.save(purchaseOrders);      
        PurchaseOrdersDTO result = purchaseOrdersMapper.toDto(purchaseOrders);
        purchaseOrdersSearchRepository.save(purchaseOrders);
        
        // Set dtms_monitoring
        // set watcherUsers
        if (purchaseOrdersDTO.getWatcherUsersPO() != null) {
        	String[] watcherUsersPO = purchaseOrdersDTO.getWatcherUsersPO();
        	
        	// get all watcherUser in project
        	List<DtmsMonitoringDTO> listWatcherUser = this.dtmsMonitoringService.getDtmsMonitoringUsers(PositionMonitoring.PURCHASE_ORDER, purchaseOrders.getId(), MONITORINGROLE.ROLE_WATCHER);
        	
        	// get and Delete old WatcherUser 
        	List<DtmsMonitoringDTO> listWatcherUserDelete = listWatcherUser.stream().filter(us -> Arrays.stream(watcherUsersPO).noneMatch(us.getMembers()::equals)).collect(Collectors.toList());
        	for (DtmsMonitoringDTO dtmsMonitoringDTO : listWatcherUserDelete) {
				this.dtmsMonitoringService.delete(dtmsMonitoringDTO.getId());
			}
        	
        	// add new WatcherUser to User
        	List<String> newWatcherUsers = Arrays.stream(watcherUsersPO).filter(username -> listWatcherUser.stream().noneMatch(e -> e.getMembers().equals(username))).collect(Collectors.toList());     	
        	for (String userName : newWatcherUsers) {
				DtmsMonitoring dtmsMonitoring = new DtmsMonitoring();
				dtmsMonitoring.setPosition(PositionMonitoring.PURCHASE_ORDER);
				dtmsMonitoring.setPositionId(purchaseOrders.getId());
				dtmsMonitoring.setRole(MONITORINGROLE.ROLE_WATCHER);
				dtmsMonitoring.setMembers(userName);
				System.out.println("Watcher: " + userName);
				dtmsMonitoring = dtmsMonitoringRepository.save(dtmsMonitoring);
				dtmsMonitoringSearchRepository.save(dtmsMonitoring);
			}
		}
        
        // set dedicatedUsers
        if (purchaseOrdersDTO.getDedicatedUsersPO() != null) {
        	String[] dedicatedUsersPO = purchaseOrdersDTO.getDedicatedUsersPO();
        	
        	// get all DedicatedUsers in project
        	List<DtmsMonitoringDTO> listDedicatedUser = this.dtmsMonitoringService.getDtmsMonitoringUsers(PositionMonitoring.PURCHASE_ORDER, purchaseOrders.getId(), MONITORINGROLE.ROLE_DEDICATED);
        	
        	// get and Delete old WatcherUser 
        	List<DtmsMonitoringDTO> listDedicatedUserDelete = listDedicatedUser.stream().filter(us -> Arrays.stream(dedicatedUsersPO).noneMatch(us.getMembers()::equals)).collect(Collectors.toList());
        	for (DtmsMonitoringDTO dtmsMonitoringDTO : listDedicatedUserDelete) {
				this.dtmsMonitoringService.delete(dtmsMonitoringDTO.getId());
			}
        	
        	// add new WatcherUser to User
        	List<String> newDedicatedUsers = Arrays.stream(dedicatedUsersPO).filter(username -> listDedicatedUser.stream().noneMatch(e -> e.getMembers().equals(username))).collect(Collectors.toList());     	
        	for (String userName : newDedicatedUsers) {
				DtmsMonitoring dtmsMonitoring = new DtmsMonitoring();
				dtmsMonitoring.setPosition(PositionMonitoring.PURCHASE_ORDER);
				dtmsMonitoring.setPositionId(purchaseOrders.getId());
				dtmsMonitoring.setRole(MONITORINGROLE.ROLE_DEDICATED);
				dtmsMonitoring.setMembers(userName);
				System.out.println("DedicatedUsers: " + userName);
				dtmsMonitoring = dtmsMonitoringRepository.save(dtmsMonitoring);
				dtmsMonitoringSearchRepository.save(dtmsMonitoring);
			}
		}
        
        return result;
    }

    /**
     * Get all the purchaseOrders.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<PurchaseOrdersDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PurchaseOrders");
        return purchaseOrdersRepository.findAll(pageable)
            .map(purchaseOrdersMapper::toDto);
    }

    /**
     * Get one purchaseOrders by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public PurchaseOrdersDTO findOne(Long id) {
        log.debug("Request to get PurchaseOrders : {}", id);
        PurchaseOrders purchaseOrders = purchaseOrdersRepository.findOne(id);
        return purchaseOrdersMapper.toDto(purchaseOrders);
    }

    /**
     * Delete the purchaseOrders by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete PurchaseOrders : {}", id);
        purchaseOrdersRepository.delete(id);
        purchaseOrdersSearchRepository.delete(id);
    }

    /**
     * Search for the purchaseOrders corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<PurchaseOrdersDTO> search(Long projectId, String query, Pageable pageable) {
        log.debug("Request to search for a page of PurchaseOrders for query {}", query);
        final String userLogin = SecurityUtils.getCurrentUserLogin()
				.orElseThrow(() -> new InternalServerErrorException("Current user login not found"));
        Page<PurchaseOrders> result = null;
        if (SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN)) {
        	result = purchaseOrdersSearchRepository.findByProjectIdAndNameLike(projectId, query, pageable);
        }else {
			ProjectUsers projectUser = projectUsersRepository.findByUserLoginAndRolePM(userLogin, projectId);
			//query = "*"+ query;
			if(projectUser != null) {
				result = purchaseOrdersSearchRepository.findByProjectIdAndNameLike(projectId, query, pageable);
			}else {
				projectUser = projectUsersRepository.findByUserLoginAndRoleTEAMLEAD(userLogin, projectId);
				if(projectUser != null) {
					result = purchaseOrdersSearchRepository.findByProjectIdAndPurchaseOrderLeadIdAndNameLike(projectId, projectUser.getId(), query, pageable);
				}
			}
        }
        return result == null ? null : result.map(purchaseOrdersMapper::toDto);
    }
    
    /** get select PO destination 
	 *  which includes all the PO 
	 *  (has PROCESSING, OPEN status)
	 *  @author PhuVD3
	 */
    @Transactional(readOnly = true)
    public List<PurchaseOrdersDTO> findListPurchaseOrdersClone(Long projectId) {
        log.debug("Request to get all PurchaseOrders");
        return purchaseOrdersMapper.toDto(this.purchaseOrdersRepository.getListPurchaseOrdersClone(projectId));
    }

	/**
	 * Create excel file
	 * 
	 * @param projectId
	 * @throws IOException
	 */
	public InputStreamResource exportExcel(Long purchaseOrderId) throws IOException {
		PurchaseOrders purchaseOrders = this.purchaseOrdersRepository.findOne(purchaseOrderId);
		if(purchaseOrders != null) {
			ProjectTemplates projectTemplates = purchaseOrders.getProjectTemplates();
			if (projectTemplates == null) {
				projectTemplates = purchaseOrders.getProject().getProjectTemplates();
			}
			ProjectWorkflowsCriteria criteria = new ProjectWorkflowsCriteria();
			LongFilter filter = new LongFilter();
			filter.setEquals(projectTemplates.getId());
			criteria.setProjectTemplatesId(filter);
			List<ProjectWorkflowsDTO> projectWorkflowsDTOs = this.projectWorkflowsQueryService.findByCriteria(criteria);
			//String[] columns = getColumns(projectWorkflowsDTOs);
			if (!CollectionUtils.isEmpty(projectWorkflowsDTOs)) {
				ByteArrayInputStream in = excelGenerator.taskManagementToExcel(projectWorkflowsDTOs, purchaseOrders.getProject().getId());
				return new InputStreamResource(in);
			} else {
				return null;
			}
		}else {
			return null;
		}
		
	}
    
}
