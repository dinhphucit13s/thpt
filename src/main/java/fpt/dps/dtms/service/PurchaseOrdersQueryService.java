package fpt.dps.dtms.service;


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

import io.github.jhipster.service.QueryService;
import fpt.dps.dtms.domain.*; // for static metamodels
import fpt.dps.dtms.repository.PurchaseOrdersRepository;
import fpt.dps.dtms.repository.search.PurchaseOrdersSearchRepository;
import fpt.dps.dtms.security.AuthoritiesConstants;
import fpt.dps.dtms.security.SecurityUtils;
import fpt.dps.dtms.service.dto.DtmsMonitoringDTO;
import fpt.dps.dtms.service.dto.ProjectUsersDTO;
import fpt.dps.dtms.service.dto.ProjectsCriteria;
import fpt.dps.dtms.service.dto.ProjectsDTO;
import fpt.dps.dtms.service.dto.PurchaseOrdersCriteria;

import fpt.dps.dtms.service.dto.PurchaseOrdersDTO;
import fpt.dps.dtms.service.dto.SelectDTO;
import fpt.dps.dtms.service.mapper.PurchaseOrdersMapper;
import fpt.dps.dtms.service.mapper.SelectMapper;
import fpt.dps.dtms.web.rest.errors.InternalServerErrorException;
import fpt.dps.dtms.domain.enumeration.MONITORINGROLE;
import fpt.dps.dtms.domain.enumeration.PositionMonitoring;
import fpt.dps.dtms.domain.enumeration.PurchaseOrderStatus;

/**
 * Service for executing complex queries for PurchaseOrders entities in the database.
 * The main input is a {@link PurchaseOrdersCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PurchaseOrdersDTO} or a {@link Page} of {@link PurchaseOrdersDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PurchaseOrdersQueryService extends QueryService<PurchaseOrders> {

    private final Logger log = LoggerFactory.getLogger(PurchaseOrdersQueryService.class);


    private final PurchaseOrdersRepository purchaseOrdersRepository;

    private final PurchaseOrdersMapper purchaseOrdersMapper;
    
    private final SelectMapper selectMapper;

    private final PurchaseOrdersSearchRepository purchaseOrdersSearchRepository;
    
    private final ProjectsQueryService projectsQueryService;
    
    private final ProjectUsersQueryService projectUsersQueryService;
    
    private final DtmsMonitoringService dtmsMonitoringService;

    public PurchaseOrdersQueryService(PurchaseOrdersRepository purchaseOrdersRepository, PurchaseOrdersMapper purchaseOrdersMapper, SelectMapper selectMapper, 
    		PurchaseOrdersSearchRepository purchaseOrdersSearchRepository, ProjectsQueryService projectsQueryService, 
    		ProjectUsersQueryService projectUsersQueryService, DtmsMonitoringService dtmsMonitoringService) {
        this.purchaseOrdersRepository = purchaseOrdersRepository;
        this.purchaseOrdersMapper = purchaseOrdersMapper;
        this.selectMapper = selectMapper;
        this.purchaseOrdersSearchRepository = purchaseOrdersSearchRepository;
        this.projectsQueryService = projectsQueryService;
        this.projectUsersQueryService = projectUsersQueryService;
        this.dtmsMonitoringService  = dtmsMonitoringService;
    }

    /**
     * Return a {@link List} of {@link PurchaseOrdersDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SelectDTO> findByCriteria(PurchaseOrdersCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<PurchaseOrders> specification = createSpecification(criteria);
        List<PurchaseOrdersDTO> purSelect = purchaseOrdersMapper.toDto(purchaseOrdersRepository.findAll(specification));
        List<SelectDTO> result = purSelect.stream().map(pur ->{
        	SelectDTO obj = new SelectDTO();
        	obj.setId(pur.getId());
        	obj.setName(pur.getName());
        	return obj;
        }).collect(Collectors.toList());
        return result;
        //return purchaseOrdersMapper.toDto(purchaseOrdersRepository.findAll(specification));
    }
    
    /**
     * get all PO by role user login
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PurchaseOrdersDTO> findByCriteriaRole(PurchaseOrdersCriteria criteria) {
    	log.debug("find by criteria : {}", criteria);
        final Specifications<PurchaseOrders> specification = createSpecification(criteria);
        return purchaseOrdersMapper.toDto(purchaseOrdersRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PurchaseOrdersDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PurchaseOrdersDTO> findByCriteria(PurchaseOrdersCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<PurchaseOrders> specification = createSpecification(criteria);
        final Page<PurchaseOrders> result = purchaseOrdersRepository.findAll(specification, page);
        return result.map(purchaseOrdersMapper::toDto);
    }
    
    @Transactional(readOnly = true)
    public Page<PurchaseOrdersDTO> getAllPurchaseOrdersByProjectId(Long projectId, Pageable page, ProjectsCriteria criteria) {
        log.debug("find by project");
        Page<PurchaseOrders> listPO = null;
        if (!SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN)) {
			final String userLogin = SecurityUtils.getCurrentUserLogin()
					.orElseThrow(() -> new InternalServerErrorException("Current user login not found"));
			ProjectsDTO projectPM = projectsQueryService.findByUserLoginAndRolePM(userLogin, projectId);
			ProjectUsersDTO projectTL = projectUsersQueryService.findByUserLoginAndRoleTEAMLEAD(userLogin, projectId);
			if (projectPM != null) {
				listPO = purchaseOrdersRepository.getAllPurchaseOrdersByProjectId(projectId, page);
			} else {
				listPO = purchaseOrdersRepository.getAllPurchaseOrdersByProjectIdAndTeamLead(projectId, projectTL.getId(), page);
			}
			
		}else {
			listPO = purchaseOrdersRepository.getAllPurchaseOrdersByProjectId(projectId, page);
		}
        
        return listPO.map(purchaseOrdersMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<PurchaseOrdersDTO> getPurchaseOrdersWithMonitoringByProjectId(Long projectId, Pageable page, ProjectsCriteria criteria) {
        log.debug("find by project Id");
        Page<PurchaseOrders> listPO = null;
        if (!SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN)) {
			final String userLogin = SecurityUtils.getCurrentUserLogin()
					.orElseThrow(() -> new InternalServerErrorException("Current user login not found"));
			ProjectsDTO projectPM = projectsQueryService.findByUserLoginAndRolePM(userLogin, projectId);
			// ProjectUsersDTO projectTL = projectUsersQueryService.findByUserLoginAndRoleTEAMLEAD(userLogin, projectId);
			if (projectPM != null) {
				listPO = purchaseOrdersRepository.getAllPurchaseOrdersByProjectId(projectId, page);
			} else {
				listPO = purchaseOrdersRepository.getAllPurchaseOrdersByProjectId(projectId, page);
				
				if (listPO != null && listPO.getTotalElements() > 0 ) {
					// if user is Dedicate of project
					DtmsMonitoringDTO dtmsMonitoringDedicatePJ = dtmsMonitoringService.findByAllCondition(PositionMonitoring.PROJECT, projectId, userLogin, MONITORINGROLE.ROLE_DEDICATED);
					if (dtmsMonitoringDedicatePJ != null) {
						Page<PurchaseOrdersDTO> pagePODTO = listPO.map(purchaseOrdersMapper::toDto);
						pagePODTO.forEach(po -> {
							po.setDtmsMonitoringProject(dtmsMonitoringDedicatePJ);
							
							String[] dedicatedUsersPO = dtmsMonitoringService.getArraysDtmsMonitoringUsers(PositionMonitoring.PURCHASE_ORDER, po.getId(), MONITORINGROLE.ROLE_DEDICATED);
							po.setDedicatedUsersPO(dedicatedUsersPO);
	
							String[] watcherUsersPO = dtmsMonitoringService.getArraysDtmsMonitoringUsers(PositionMonitoring.PURCHASE_ORDER, po.getId(), MONITORINGROLE.ROLE_WATCHER);
							po.setWatcherUsersPO(watcherUsersPO);
							});
						return pagePODTO;
					}
					
					// if user is Watcher of project
					DtmsMonitoringDTO dtmsMonitoringWatcherPJ = dtmsMonitoringService.findByAllCondition(PositionMonitoring.PROJECT, projectId, userLogin, MONITORINGROLE.ROLE_WATCHER);
					if (dtmsMonitoringWatcherPJ != null) {
						Page<PurchaseOrdersDTO> pagePODTO = listPO.map(purchaseOrdersMapper::toDto);
						pagePODTO.forEach(po -> po.setDtmsMonitoringProject(dtmsMonitoringWatcherPJ));
						return pagePODTO;
					}
					
					// get all PO in project
					List<PurchaseOrders> listAllPO = new ArrayList<PurchaseOrders>();
					List<PurchaseOrders> listPOMonitoring = new ArrayList<PurchaseOrders>();
					List<PurchaseOrdersDTO> listPOFinal = new ArrayList<PurchaseOrdersDTO>();
					
					listAllPO = purchaseOrdersRepository.getAllPurchaseOrdersByProjectId(projectId);
					if (listAllPO != null && listAllPO.size() > 0) {
						List<PurchaseOrders> listPOTeamLead  = listAllPO.stream().filter(po -> (po.getPurchaseOrderLead() != null && po.getPurchaseOrderLead().getUserLogin().equalsIgnoreCase(userLogin))).collect(Collectors.toList());
		
						// get dedicated and watcher PO of user in dtmsMonotoring
						List<DtmsMonitoringDTO>  listMonitoring = dtmsMonitoringService.getDtmsMonitoringByUserLogin(PositionMonitoring.PURCHASE_ORDER, userLogin);
						if (listMonitoring != null && listMonitoring.size()>0) {
							listPOMonitoring = listAllPO.stream().filter(po -> listMonitoring.stream().anyMatch(e -> e.getPositionId()==po.getId())).collect(Collectors.toList());
						}
		
						// filter listPOMonitoring theo listPOTeamLead
						if (listPOTeamLead != null && listPOTeamLead.size() > 0) {
							listPOFinal.addAll(purchaseOrdersMapper.toDto(listPOTeamLead));
							listPOMonitoring = listPOMonitoring.stream().filter(po -> listPOTeamLead.stream().noneMatch(e -> e.getId() == po.getId())).collect(Collectors.toList());
						}
						
						List<PurchaseOrdersDTO> listPOMonitoringDTO = purchaseOrdersMapper.toDto(listPOMonitoring);
						for (PurchaseOrdersDTO poDTO : listPOMonitoringDTO) {
							String[] dedicatedUsersPO = dtmsMonitoringService.getArraysDtmsMonitoringUsers(PositionMonitoring.PURCHASE_ORDER, poDTO.getId(), MONITORINGROLE.ROLE_DEDICATED);
							poDTO.setDedicatedUsersPO(dedicatedUsersPO);
		
							String[] watcherUsersPO = dtmsMonitoringService.getArraysDtmsMonitoringUsers(PositionMonitoring.PURCHASE_ORDER, poDTO.getId(), MONITORINGROLE.ROLE_WATCHER);
				    		poDTO.setWatcherUsersPO(watcherUsersPO);
						}
						
						listPOFinal.addAll(listPOMonitoringDTO);
					}
					
					Page<PurchaseOrdersDTO> pagePO = new PageImpl<PurchaseOrdersDTO>(listPOFinal, page, listPOFinal.size());
					return pagePO;
				}
			}
		}else {
			listPO = purchaseOrdersRepository.getAllPurchaseOrdersByProjectId(projectId, page);
		}
        
        return listPO.map(purchaseOrdersMapper::toDto);
    }
    
    @Transactional(readOnly = true)
    public Page<SelectDTO> findByCriteriaSelect(PurchaseOrdersCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<PurchaseOrders> specification = createSpecification(criteria);
        final Page<PurchaseOrders> result = purchaseOrdersRepository.findAll(specification, page);
        return result.map(selectMapper::toDto);
    }

    /**
     * Function to convert PurchaseOrdersCriteria to a {@link Specifications}
     */
    private Specifications<PurchaseOrders> createSpecification(PurchaseOrdersCriteria criteria) {
        Specifications<PurchaseOrders> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), PurchaseOrders_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), PurchaseOrders_.name));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), PurchaseOrders_.status));
            }
            if (criteria.getStartTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStartTime(), PurchaseOrders_.startTime));
            }
            if (criteria.getEndTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEndTime(), PurchaseOrders_.endTime));
            }
            if (criteria.getTmsCustomFieldScreenValueId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getTmsCustomFieldScreenValueId(), PurchaseOrders_.tmsCustomFieldScreenValues, TMSCustomFieldScreenValue_.id));
            }
            if (criteria.getProjectId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getProjectId(), PurchaseOrders_.project, Projects_.id));
            }
            if (criteria.getPurchaseOrderLeadId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getPurchaseOrderLeadId(), PurchaseOrders_.purchaseOrderLead, ProjectUsers_.id));
            }
            if (criteria.getPackagesId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getPackagesId(), PurchaseOrders_.packages, Packages_.id));
            }
        }
        return specification;
    }

	public List<PurchaseOrdersDTO> getListPurchaseOrderBiddingTask(Long projectId, String userName) {
		return this.purchaseOrdersMapper.toDto(this.purchaseOrdersRepository.getListPurchaseOrderBiddingTask(projectId, userName));
	}
	
	public List<PurchaseOrdersDTO> getListPurchaseOrderBiddingTaskByProject(Long projectId) {
		return this.purchaseOrdersMapper.toDto(this.purchaseOrdersRepository.getAllPurchaseOrdersByProjectId(projectId));
	}

}
