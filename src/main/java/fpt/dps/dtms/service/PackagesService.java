package fpt.dps.dtms.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import fpt.dps.dtms.domain.Packages;
import fpt.dps.dtms.domain.ProjectTemplates;
import fpt.dps.dtms.domain.Projects;
import fpt.dps.dtms.domain.PurchaseOrders;
import fpt.dps.dtms.domain.Tasks;
import fpt.dps.dtms.domain.enumeration.TaskStatus;
import fpt.dps.dtms.repository.PackagesRepository;
import fpt.dps.dtms.repository.ProjectsRepository;
import fpt.dps.dtms.repository.PurchaseOrdersRepository;
import fpt.dps.dtms.repository.TasksRepository;
import fpt.dps.dtms.repository.search.PackagesSearchRepository;
import fpt.dps.dtms.repository.search.TMSCustomFieldScreenValueSearchRepository;
import fpt.dps.dtms.repository.search.TasksSearchRepository;
import fpt.dps.dtms.service.dto.PackagesDTO;
import fpt.dps.dtms.service.dto.ProjectWorkflowsCriteria;
import fpt.dps.dtms.service.dto.ProjectWorkflowsDTO;
import fpt.dps.dtms.service.dto.TMSCustomFieldScreenValueDTO;
import fpt.dps.dtms.service.dto.TasksDTO;
import fpt.dps.dtms.service.mapper.PackagesMapper;
import fpt.dps.dtms.service.mapper.TMSCustomFieldScreenValueMapper;
import fpt.dps.dtms.service.mapper.TasksMapper;
import fpt.dps.dtms.service.util.AppConstants;
import fpt.dps.dtms.service.util.ExcelGenerator;
import fpt.dps.dtms.service.util.FieldConfigService;
import fpt.dps.dtms.web.rest.vm.FieldConfigVM;
import fpt.dps.dtms.web.rest.vm.TMSDynamicCustomFieldVM;
import io.github.jhipster.service.filter.LongFilter;
import liquibase.util.file.FilenameUtils;

/**
 * Service Implementation for managing Packages.
 */
@Service
@Transactional
public class PackagesService {

	private final Logger log = LoggerFactory.getLogger(PackagesService.class);

	private final PackagesRepository packagesRepository;

	private final PackagesMapper packagesMapper;

	private final PackagesSearchRepository packagesSearchRepository;

	private final ProjectWorkflowsQueryService projectWorkflowsQueryService;

	private final ProjectsRepository projectsRepository;

	private final PurchaseOrdersRepository purchaseOrdersRepository;

	private final ExcelGenerator excelGenerator;

	private final FieldConfigService fieldConfigService;
	
	private final TMSCustomFieldScreenValueService tMSCustomFieldScreenValueService;
	
	private final PackagesQueryService packagesQueryService;
	
	private final TasksMapper tasksMapper;
	
	private final TasksRepository tasksRepository;
	
	private final TasksSearchRepository tasksSearchRepository;
	
	private final TMSCustomFieldScreenValueQueryService tMSCustomFieldScreenValueQueryService;
	
	private final TMSCustomFieldScreenValueSearchRepository tMSCustomFieldScreenValueSearchRepository;
	
	private final TMSCustomFieldScreenValueMapper tMSCustomFieldScreenValueMapper;
	

	public PackagesService(PackagesRepository packagesRepository, PackagesMapper packagesMapper,
			ProjectsRepository projectsRepository, PackagesSearchRepository packagesSearchRepository,
			ProjectWorkflowsQueryService projectWorkflowsQueryService, ExcelGenerator excelGenerator,
			PurchaseOrdersRepository purchaseOrdersRepository, FieldConfigService fieldConfigService,
			PackagesQueryService packagesQueryService, TasksMapper tasksMapper,
			TasksRepository tasksRepository, TasksSearchRepository tasksSearchRepository, TMSCustomFieldScreenValueService tMSCustomFieldScreenValueService,
			TMSCustomFieldScreenValueQueryService tMSCustomFieldScreenValueQueryService, TMSCustomFieldScreenValueSearchRepository tMSCustomFieldScreenValueSearchRepository,
			TMSCustomFieldScreenValueMapper tMSCustomFieldScreenValueMapper) {
		this.packagesRepository = packagesRepository;
		this.packagesMapper = packagesMapper;
		this.packagesSearchRepository = packagesSearchRepository;
		this.projectWorkflowsQueryService = projectWorkflowsQueryService;
		this.projectsRepository = projectsRepository;
		this.excelGenerator = excelGenerator;
		this.purchaseOrdersRepository = purchaseOrdersRepository;
		this.fieldConfigService = fieldConfigService;
		this.packagesQueryService = packagesQueryService;
		this.tasksMapper = tasksMapper;
		this.tasksRepository = tasksRepository;
		this.tasksSearchRepository = tasksSearchRepository;
		this.tMSCustomFieldScreenValueService = tMSCustomFieldScreenValueService;
		this.tMSCustomFieldScreenValueQueryService = tMSCustomFieldScreenValueQueryService;
		this.tMSCustomFieldScreenValueSearchRepository = tMSCustomFieldScreenValueSearchRepository;
		this.tMSCustomFieldScreenValueMapper = tMSCustomFieldScreenValueMapper;
	}

	/**
	 * Save a packages.
	 *
	 * @param packagesDTO
	 *            the entity to save
	 * @return the persisted entity
	 */
	public PackagesDTO save(PackagesDTO packagesDTO) {
		log.debug("Request to save Packages : {}", packagesDTO);
		Packages packages = packagesMapper.toEntity(packagesDTO);
		packages = packagesRepository.save(packages);
		List<TMSCustomFieldScreenValueDTO> listTMSCustomFieldScreenValueDTO = packagesDTO.getTmsCustomFieldScreenValueDTO();
		for (TMSCustomFieldScreenValueDTO tmsCustomField: listTMSCustomFieldScreenValueDTO) {
			if (tmsCustomField.getId() != null) {
				tMSCustomFieldScreenValueService.save(tmsCustomField);
				tMSCustomFieldScreenValueSearchRepository.save(tMSCustomFieldScreenValueMapper.toEntity(tmsCustomField));
			} else {
				tmsCustomField.setPackagesId(packages.getId());
				tMSCustomFieldScreenValueService.save(tmsCustomField);
				tMSCustomFieldScreenValueSearchRepository.save(tMSCustomFieldScreenValueMapper.toEntity(tmsCustomField));
			}
		}
		PackagesDTO result = packagesMapper.toDto(packages);
		packagesSearchRepository.save(packages);
		return result;
	}

	/**
	 * Get all the packages.
	 *
	 * @param pageable
	 *            the pagination information
	 * @return the list of entities
	 */
	@Transactional(readOnly = true)
	public Page<PackagesDTO> findAll(Pageable pageable) {
		log.debug("Request to get all Packages");
		return packagesRepository.findAll(pageable).map(packagesMapper::toDto);
	}

	/**
	 * Get one packages by id.
	 *
	 * @param id
	 *            the id of the entity
	 * @return the entity
	 */
	@Transactional(readOnly = true)
	public PackagesDTO findOne(Long id) {
		log.debug("Request to get Packages : {}", id);
		Packages packages = packagesRepository.findOne(id);
		PackagesDTO packagesDTO = packagesMapper.toDto(packages);
		packagesDTO.setTmsCustomFieldScreenValueDTO(tMSCustomFieldScreenValueQueryService.getAllTMSCustomFieldScreenValueByPackId(id));
		return packagesDTO;
	}

	/**
	 * Delete the packages by id.
	 *
	 * @param id
	 *            the id of the entity
	 */
	public void delete(Long id) {
		log.debug("Request to delete Packages : {}", id);
		packagesRepository.delete(id);
		packagesSearchRepository.delete(id);
	}

	/**
	 * Search for the packages corresponding to the query.
	 *
	 * @param query
	 *            the query of the search
	 * @param pageable
	 *            the pagination information
	 * @return the list of entities
	 */
	@Transactional(readOnly = true)
	public Page<PackagesDTO> search(Long purchaseOrderId, String query, Pageable pageable) {
		log.debug("Request to search for a page of Packages for query {}", query);
		Page<Packages> result = packagesSearchRepository.findByPurchaseOrdersIdAndNameLike(purchaseOrderId, query, pageable);
		Page<PackagesDTO> packagesDTO = packagesQueryService.packageListReuse(result, purchaseOrderId);
		List<PackagesDTO> listDTOs = packagesDTO.getContent();
		for (PackagesDTO pack: listDTOs) {
        	pack.setTmsCustomFieldScreenValueDTO(tMSCustomFieldScreenValueMapper.toDto(tMSCustomFieldScreenValueSearchRepository.findAllByPackagesId(pack.getId())));
        }
		return packagesDTO;
	}

	/**
	 * Auto Generate Packages_Tasks from excel
	 *
	 * @param packagesDTO
	 *            the entity to save
	 * @return the persisted entity
	 * @throws IOException
	 */
	public void autoGenPackages_Tasks(MultipartFile file, Long poID) throws IOException {
		log.debug("Request to auto gen Packages and Tasks");
		PurchaseOrders purchaseOrders = purchaseOrdersRepository.findOne(poID);
		ProjectTemplates projectTemplates = purchaseOrders.getProjectTemplates();
		if (projectTemplates == null) {
			projectTemplates = purchaseOrders.getProject().getProjectTemplates();
		}
		List<ProjectWorkflowsDTO> projectWorkflowsDTOs = getProjectWorkflowDTOs(projectTemplates);
		if (CollectionUtils.isNotEmpty(projectWorkflowsDTOs)) {
			String fileName = FilenameUtils.removeExtension(file.getOriginalFilename());
			if(fileName.toUpperCase().contains(AppConstants.TOYOTA_FILE_NAME)){
				//Only process for TOYOTA team.
				excelGenerator.importPackageAndTaskToDatabaseForToyota(projectWorkflowsDTOs, file, purchaseOrders);
			}else {
				excelGenerator.importPackageAndTaskToDatabase(projectWorkflowsDTOs, file, purchaseOrders);
			}
		}
	}
	
	public  List<FieldConfigVM> getPackageFieldConfig(Long poID) {
		PurchaseOrders purchaseOrders = purchaseOrdersRepository.findOne(poID);
		Projects project = purchaseOrders.getProject();
		// List<FieldConfigVM> fieldConfigVMs = this.fieldConfigService.getAllFieldConfig(project.getId(), AppConstants.PACKAGE_ENTITY);
		TMSDynamicCustomFieldVM tmsDynamicCustomFieldVM = this.fieldConfigService.getAllFieldConfig(project.getId(), AppConstants.PACKAGE_ENTITY);
		List<FieldConfigVM> fieldConfigVMs = tmsDynamicCustomFieldVM.getFieldConfigVMs();
		return fieldConfigVMs;
	}
	
	public TMSDynamicCustomFieldVM getPackageDynamicFieldConfig(Long poID) {
		PurchaseOrders purchaseOrders = purchaseOrdersRepository.findOne(poID);
		TMSDynamicCustomFieldVM fieldConfigVMs = this.fieldConfigService.getAllDynamicFieldConfig(purchaseOrders, AppConstants.PACKAGE_ENTITY);
		return fieldConfigVMs;
	}

	/**
	 * @author TuHP Get list of ProjectWorkFlow
	 * @param poId
	 *            the PurchaseOrder Id
	 * @return The ProjectWorkFlow list
	 */
	@Transactional(readOnly = true)
	private List<ProjectWorkflowsDTO> getProjectWorkflowDTOs(ProjectTemplates projectTemplates) {
		List<ProjectWorkflowsDTO> projectWorkflowsDTOs = null;
		if (projectTemplates != null) {
			ProjectWorkflowsCriteria criteria = new ProjectWorkflowsCriteria();
			LongFilter filter = new LongFilter();
			filter.setEquals(projectTemplates.getId());
			criteria.setProjectTemplatesId(filter);
			projectWorkflowsDTOs = this.projectWorkflowsQueryService.findByCriteria(criteria);
		}
		return projectWorkflowsDTOs;
	}
	
	/**
	 * @author NgocVX1
	 * Delivery tasks in package. Task status is DONE, change to CLOSED.
	 * 
	 * @param idPackage
	 * @return list task delivery
	 */
	public List<TasksDTO> delivertyTasksInPackage(Long idPackage) {
		List<TasksDTO> listTasksDelivery = new ArrayList<TasksDTO>();
		// get all Task in package
		Pageable page = new PageRequest(0, 1000000);
		List<Tasks> listTasks = this.tasksRepository.getAllTasksByPackageId(idPackage, page).getContent();
		for (Tasks task : listTasks) {
			if(TaskStatus.DONE.equals(task.getStatus())) {
				task.setStatus(TaskStatus.CLOSED);
				task = this.tasksRepository.save(task);
				tasksSearchRepository.save(task);
				listTasksDelivery.add(this.tasksMapper.toDto(task));
			}
		}
		
		return listTasksDelivery;
	}
}
