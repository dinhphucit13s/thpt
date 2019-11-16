package fpt.dps.dtms.service;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import io.github.jhipster.service.QueryService;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import fpt.dps.dtms.domain.*; // for static metamodels
import fpt.dps.dtms.repository.PackagesRepository;
import fpt.dps.dtms.repository.PurchaseOrdersRepository;
import fpt.dps.dtms.repository.search.PackagesSearchRepository;
import fpt.dps.dtms.service.dto.PackagesCriteria;

import fpt.dps.dtms.domain.enumeration.OPStatus;
import fpt.dps.dtms.domain.enumeration.ReviewStatus;
import fpt.dps.dtms.domain.enumeration.FIStatus;
import fpt.dps.dtms.domain.enumeration.TaskStatus;

import fpt.dps.dtms.service.dto.PackagesDTO;
import fpt.dps.dtms.service.dto.ProjectUsersDTO;
import fpt.dps.dtms.service.dto.ProjectWorkflowsCriteria;
import fpt.dps.dtms.service.dto.ProjectWorkflowsDTO;
import fpt.dps.dtms.service.dto.PurchaseOrdersDTO;
import fpt.dps.dtms.service.dto.SelectDTO;
import fpt.dps.dtms.service.dto.TMSCustomFieldScreenValueDTO;
import fpt.dps.dtms.service.dto.ProjectsDTO;
import fpt.dps.dtms.service.mapper.PackagesMapper;
import fpt.dps.dtms.service.mapper.SelectMapper;
import fpt.dps.dtms.service.util.AppConstants;

/**
 * Service for executing complex queries for Packages entities in the database.
 * The main input is a {@link PackagesCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PackagesDTO} or a {@link Page} of {@link PackagesDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PackagesQueryService extends QueryService<Packages> {

    private final Logger log = LoggerFactory.getLogger(PackagesQueryService.class);


    private final PackagesRepository packagesRepository;
    
    private final ProjectWorkflowsQueryService projectWorkflowsQueryService;
    
    private final PurchaseOrdersService purchaseOrdersService;
    
    private final ProjectUsersService projectUsersService;
    
    private final TaskTrackingTimeQueryService taskTrackingTimeQueryService;

    private final PackagesMapper packagesMapper;
    
    private final SelectMapper selectMapper;
    
    private final PackagesSearchRepository packagesSearchRepository;
    
    private final PurchaseOrdersRepository purchaseOrdersRepository;
    
    private final TMSCustomFieldScreenValueQueryService tMSCustomFieldScreenValueQueryService;

    public PackagesQueryService(PackagesRepository packagesRepository, PackagesMapper packagesMapper, SelectMapper selectMapper, PackagesSearchRepository packagesSearchRepository, PurchaseOrdersService purchaseOrdersService, 
    		ProjectWorkflowsQueryService projectWorkflowsQueryService, PurchaseOrdersRepository purchaseOrdersRepository, ProjectUsersService projectUsersService, TaskTrackingTimeQueryService taskTrackingTimeQueryService,
    		TMSCustomFieldScreenValueQueryService tMSCustomFieldScreenValueQueryService) {
        this.packagesRepository = packagesRepository;
        this.packagesMapper = packagesMapper;
        this.selectMapper = selectMapper;
        this.packagesSearchRepository = packagesSearchRepository;
        this.purchaseOrdersService = purchaseOrdersService;
        this.projectWorkflowsQueryService = projectWorkflowsQueryService;
        this.purchaseOrdersRepository = purchaseOrdersRepository;
        this.projectUsersService = projectUsersService;
        this.taskTrackingTimeQueryService = taskTrackingTimeQueryService;
        this.tMSCustomFieldScreenValueQueryService = tMSCustomFieldScreenValueQueryService;
    }

    /**
     * Return a {@link List} of {@link PackagesDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PackagesDTO> findByCriteria(PackagesCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Packages> specification = createSpecification(criteria);
        return packagesMapper.toDto(packagesRepository.findAll(specification));
    }
    
    @Transactional(readOnly = true)
    public List<Packages> findAllPackagesByPurchaseOrderId(Long purchaseOrderId) {
        log.debug("find by criteria : {}", purchaseOrderId);
        return packagesRepository.findAllPackagesByPurchaseOrderId(purchaseOrderId);
    }
    
    @Transactional(readOnly = true)
    public Page<PackagesDTO> findPackagesByPurchaseOrderId(Long purchaseOrderId, Pageable page) {
        log.debug("find by purchase order");
        Page<Packages> result = packagesRepository.findAllPackagesPurchaseOrderId(purchaseOrderId, page);
        Page<PackagesDTO> packagesDTOs = packageListReuse(result, purchaseOrderId);
        List<PackagesDTO> listpackagesDTOs = packagesDTOs.getContent();
        for (PackagesDTO pack: listpackagesDTOs) {
        	pack.setTmsCustomFieldScreenValueDTO(tMSCustomFieldScreenValueQueryService.getAllTMSCustomFieldScreenValueByPackId(pack.getId()));
        }
        return packagesDTOs;
    }
    
    public List<PackagesDTO> findListPackageBiddingTask(Long purchaseOrderId) {
        log.debug("find by purchase order");
        List<Packages> result = packagesRepository.findListPackageBiddingTask(purchaseOrderId);
        return this.packagesMapper.toDto(result);
    }
    
    public Page<PackagesDTO> packageListReuse(Page<Packages> packageList, Long purchaseOrderId) {
    	Boolean checkRound = false;
    	PurchaseOrders purchase = purchaseOrdersRepository.findOne(purchaseOrderId);
		Projects project = purchase.getProject() != null ? purchase.getProject() : null;
        ProjectWorkflowsCriteria criteria = new ProjectWorkflowsCriteria();
		StringFilter nameFilter = new StringFilter();
		nameFilter.setEquals(AppConstants.TASK_ENTITY);
		criteria.setName(nameFilter);
		LongFilter filter = new LongFilter();
		filter.setEquals(project.getProjectTemplates().getId());
		criteria.setProjectTemplatesId(filter);
		List<ProjectWorkflowsDTO> projectWorkflowsDTOs = this.projectWorkflowsQueryService.findByCriteria(criteria);
		String[] COLUMNs = getTaskExportColumns(projectWorkflowsDTOs);
		for (int col = 0; col < COLUMNs.length; col++) {
			if (COLUMNs[col].equals("review2")) {
				checkRound = true;
				break;
			}
		}
        List<Packages> rob = packageList.getContent();
        for(Packages r : rob) {
        	Long pId = r.getId();
        	Long sum = packagesRepository.findAllTaskByPackagesId(pId);
    		r.setOp(getOpResult(pId) + "/" + sum + "(" + getRatioTask(Double.valueOf(getOpResult(pId).toString()), Double.valueOf(sum.toString())) + "%" + ")");
    		r.setReviewer(getReviewerResult(pId, checkRound) + "/" + sum + "(" + getRatioTask(Double.valueOf(getReviewerResult(pId, checkRound).toString()), Double.valueOf(sum.toString())) + "%" + ")");
    		r.setFi(getFiResult(pId) + "/" + sum + "(" + getRatioTask(Double.valueOf(getFiResult(pId) .toString()), Double.valueOf(sum.toString())) + "%" + ")");
    		r.setDelivery(getStatusClosedResult(pId) + "/" + sum + "(" + getRatioTask(Double.valueOf(getStatusClosedResult(pId).toString()), Double.valueOf(sum.toString())) + "%" + ")");
    	}
        return packageList.map(packagesMapper::toDto);
    }
    
    private String[] getTaskExportColumns(List<ProjectWorkflowsDTO> projectWorkflowsDTOs) {
		List<String> columns = new ArrayList<>();
		columns.add("id");
		for (ProjectWorkflowsDTO projectWorkflowsDTO : projectWorkflowsDTOs) {
			ObjectMapper mapper = new ObjectMapper();
			try {
				ArrayNode nodes = (ArrayNode) mapper.readTree(projectWorkflowsDTO.getPmGridDTO());
				for (int i = 0; i < nodes.size(); i++) {
					String colName = nodes.get(i).textValue();
					columns.add(colName);
				}
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		String[] arrColumns = new String[columns.size()];
		columns.toArray(arrColumns);
		return arrColumns;
	}

    /**
     * Return a {@link Page} of {@link PackagesDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PackagesDTO> findByCriteria(PackagesCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Packages> specification = createSpecification(criteria);
        final Page<Packages> result = packagesRepository.findAll(specification, page);
        List<Packages> rob = result.getContent();
        for(Packages r : rob) {
        	Long pId = r.getId();
        	Long sum = packagesRepository.findAllTaskByPackagesId(pId);
    		r.setOp(getOpResult(pId) + "/" + sum);
    		r.setReviewer(getReviewerResult(pId, true) + "/" + sum);
    		r.setFi(getFiResult(pId) + "/" + sum);
    		r.setDelivery(getStatusResult(pId) + "/" + sum);
    	}
        
        return result.map(packagesMapper::toDto);
    }
    
    @Transactional(readOnly = true)
	public Page<PackagesDTO> searchPackagesByName(String packName, Pageable pageable) {
		log.debug("Request search packages name");
		Page<Packages> packages = packagesRepository.searchPackagesByName(packName, pageable);
		return packages.map(packagesMapper::toDto);
	}
    
    @Transactional(readOnly = true)
	public List<Map<String, Object>> getEffortByPakage(Long id, Pageable pageable) {
		log.debug("Request search packages name");
		ProjectUsersDTO projectUser = projectUsersService.findOne(id);
		Page<Packages> packages = packagesRepository.getAllPackage(projectUser.getUserLogin(), projectUser.getProjectId(), pageable);
		List<Packages> listPackages = packages.getContent();
		List<Map<String, Object>> result = listPackages.stream().map(pack ->{
			Map<String, Object> object = new HashMap<String, Object>();
			object.put("packageName", pack.getName());
			object.put("totalTasks", packagesRepository.getSizeTasksAssignToPackage(pack.getId(), projectUser.getUserLogin()));
			object.put("actualEffort", taskTrackingTimeQueryService.sumActualEffortByPackage(pack.getId(), projectUser.getUserLogin(), projectUser.getStartDate(), projectUser.getEndDate()));
			return object;
		}).collect(Collectors.toList());
		Map<String, Object> totalPages = new HashMap<String, Object>();
        totalPages.put("total", packages.getTotalElements());
        result.add(totalPages);
        return result;
	}
    
    /*@Transactional(readOnly = true)
    public Page<SelectDTO> findByCriteriaSelect(PackagesCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Packages> specification = createSpecification(criteria);
        final Page<Packages> result = packagesRepository.findAll(specification, page);
        return result.map(selectMapper::toDto);
    }*/

    public String getRatioTask(Double header, Double sum) {
    	if (sum != 0) {
    		Double result = (header/sum)*100;
    		if (result != 0) {
        		return String.format("%.2f", result);
        	} else {
        		return "0";
        	}
    	} else {
    		return "0";
    	}
    }
    
    /*
     * set value for OP by Tasks
     * get all op status from Tasks table
     */
    public Integer getOpResult(Long packId) {
    	List<OPStatus> status = new ArrayList<OPStatus>();
    	status.add(OPStatus.DONE);
		List<Tasks> tasks = packagesRepository.findAllOpStatusByPackagesId(packId, status);
		return tasks.size();
    }
    
    /*
     * set value for REVIEWER by Tasks
     * get all reviewer status from Tasks table
     */
    public Integer getReviewerResult(Long packId, boolean review2) {
    	List<ReviewStatus> status = new ArrayList<ReviewStatus>();
    	status.add(ReviewStatus.DONE);
		List<Tasks> tasks = new ArrayList<Tasks>();
		if(review2) {
			tasks = packagesRepository.findAllReviewerStatusByPackagesId(packId, null, status);
		}else {
			tasks = packagesRepository.findAllReviewerStatusByPackagesId(packId, status, null);
		}
		return tasks.size();
    }
    
    /*
     * set value for Fi by Tasks
     * get all Fi status from Tasks table
     */
    public Integer getFiResult(Long packId) {
    	List<FIStatus> status = new ArrayList<FIStatus>();
    	status.add(FIStatus.DONE);
		List<Tasks> task = packagesRepository.findAllFiStatusByPackagesId(packId, status);
		return task.size();
    }
    
    /*
     * set value for Fi by Tasks
     * get all Fi status from Tasks table
     */
    public Integer getStatusResult(Long packId) {
    	List<TaskStatus> status = new ArrayList<TaskStatus>();
    	status.add(TaskStatus.DONE);
		List<Tasks> tasks = packagesRepository.findAllTaskStatusByPackagesId(packId, status);
		return tasks.size();
    }
    
    /*
     * set value for Fi by Tasks
     * get all Fi status from Tasks table
     */
    public Integer getStatusClosedResult(Long packId) {
    	List<TaskStatus> status = new ArrayList<TaskStatus>();
    	status.add(TaskStatus.CLOSED);
		List<Tasks> tasks = packagesRepository.findAllTaskStatusByPackagesId(packId, status);
		return tasks.size();
    }
    
    /**
     * Function to convert PackagesCriteria to a {@link Specifications}
     */
    private Specifications<Packages> createSpecification(PackagesCriteria criteria) {
        Specifications<Packages> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Packages_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Packages_.name));
            }
            if (criteria.getOp() != null) {
                specification = specification.and(buildStringSpecification(criteria.getOp(), Packages_.op));
            }
            if (criteria.getReviewer() != null) {
                specification = specification.and(buildStringSpecification(criteria.getReviewer(), Packages_.reviewer));
            }
            if (criteria.getFi() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFi(), Packages_.fi));
            }
            if (criteria.getDelivery() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDelivery(), Packages_.delivery));
            }
            if (criteria.getEstimateDelivery() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEstimateDelivery(), Packages_.estimateDelivery));
            }
            if (criteria.getTarget() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTarget(), Packages_.target));
            }
            if (criteria.getStartTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStartTime(), Packages_.startTime));
            }
            if (criteria.getEndTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEndTime(), Packages_.endTime));
            }
            if (criteria.getTmsCustomFieldScreenValueId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getTmsCustomFieldScreenValueId(), Packages_.tmsCustomFieldScreenValues, TMSCustomFieldScreenValue_.id));
            }
            if (criteria.getPurchaseOrdersId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getPurchaseOrdersId(), Packages_.purchaseOrders, PurchaseOrders_.id));
            }
            if (criteria.getTasksId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getTasksId(), Packages_.tasks, Tasks_.id));
            }
        }
        return specification;
    }

}
