package fpt.dps.dtms.service;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.queryparser.xml.builders.BooleanQueryBuilder;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import fpt.dps.dtms.domain.Attachments;
import fpt.dps.dtms.domain.Bugs;
import fpt.dps.dtms.domain.Packages;
import fpt.dps.dtms.domain.Projects;
import fpt.dps.dtms.domain.ProjectTemplates;
import fpt.dps.dtms.domain.ProjectUsers;
import fpt.dps.dtms.domain.Tasks;
import fpt.dps.dtms.domain.User;
import fpt.dps.dtms.domain.enumeration.BiddingStatus;
import fpt.dps.dtms.domain.enumeration.FIStatus;
import fpt.dps.dtms.domain.enumeration.FixStatus;
import fpt.dps.dtms.domain.enumeration.NotificationCategory;
import fpt.dps.dtms.domain.enumeration.OPStatus;
import fpt.dps.dtms.domain.enumeration.ReviewStatus;
import fpt.dps.dtms.domain.enumeration.TaskStatus;
import fpt.dps.dtms.repository.BugsRepository;
import fpt.dps.dtms.repository.PackagesRepository;
import fpt.dps.dtms.repository.ProjectsRepository;
import fpt.dps.dtms.repository.TasksRepository;
import fpt.dps.dtms.repository.UserRepository;
import fpt.dps.dtms.domain.PurchaseOrders;
import fpt.dps.dtms.domain.TaskBidding;
import fpt.dps.dtms.repository.PurchaseOrdersRepository;
import fpt.dps.dtms.repository.TaskTrackingTimeRepository;
import fpt.dps.dtms.repository.search.BugsSearchRepository;
import fpt.dps.dtms.repository.search.TMSCustomFieldScreenValueSearchRepository;
import fpt.dps.dtms.repository.search.TasksSearchRepository;
import fpt.dps.dtms.security.SecurityUtils;
import fpt.dps.dtms.service.dto.AttachmentsDTO;
import fpt.dps.dtms.service.dto.BugsDTO;
import fpt.dps.dtms.service.dto.NotificationDTO;
import fpt.dps.dtms.service.dto.PackagesDTO;
import fpt.dps.dtms.service.dto.ProjectWorkflowsCriteria;
import fpt.dps.dtms.service.dto.ProjectWorkflowsDTO;
import fpt.dps.dtms.service.dto.PurchaseOrdersDTO;
import fpt.dps.dtms.service.dto.TMSCustomFieldScreenValueDTO;
import fpt.dps.dtms.service.dto.TaskBiddingDTO;
import fpt.dps.dtms.service.dto.TasksCriteria;
import fpt.dps.dtms.service.dto.TasksDTO;
import fpt.dps.dtms.service.mapper.BugsMapper;
import fpt.dps.dtms.service.mapper.TMSCustomFieldMapper;
import fpt.dps.dtms.service.mapper.TMSCustomFieldScreenValueMapper;
import fpt.dps.dtms.service.mapper.TasksMapper;
import fpt.dps.dtms.service.util.ActivitiService;
import fpt.dps.dtms.service.util.AppConstants;
import fpt.dps.dtms.service.util.ExcelGenerator;
import fpt.dps.dtms.service.util.FieldConfigService;
import fpt.dps.dtms.web.rest.vm.FieldConfigVM;
import fpt.dps.dtms.web.rest.vm.IdListVM;
import fpt.dps.dtms.web.rest.vm.TMSDynamicCustomFieldVM;
import fpt.dps.dtms.web.rest.vm.external.MultiTasksReOpenVM;
import fpt.dps.dtms.web.rest.vm.external.ReviewRatioVM;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Service Implementation for managing Tasks.
 */
@Service
@Transactional
public class TasksService {

	private final Logger log = LoggerFactory.getLogger(TasksService.class);

	private final TasksRepository tasksRepository;
	
	private final UserRepository userRepository;

	private final TasksMapper tasksMapper;

	private final TasksSearchRepository tasksSearchRepository;

	private final TasksProcessingService tasksProcessingService;

	private final ProjectWorkflowsQueryService projectWorkflowsQueryService;

	private final PackagesRepository packagesRepository;

	private final PurchaseOrdersRepository purchaseOrdersRepository;
	
	private final ProjectsRepository projectsRepository;

	private final FieldConfigService fieldConfigService;

	private final ExcelGenerator excelGenerator;

	private final TasksQueryService tasksQueryService;
	
	private final NotificationService notificationService;
	
	private final NotificationTemplateService notificationTemplateService;
	
	private final ElasticsearchTemplate elasticsearchTemplate;
	
	private final TMSCustomFieldScreenValueService tMSCustomFieldScreenValueService;
	
	private final TMSCustomFieldScreenValueQueryService tMSCustomFieldScreenValueQueryService;
	
	private final TMSCustomFieldScreenValueSearchRepository tMSCustomFieldScreenValueSearchRepository;
	
	private final TMSCustomFieldScreenValueMapper tMSCustomFieldScreenValueMapper;
	
	private final TaskTrackingTimeRepository taskTrackingTimeRepository;
	
	private final BugsMapper bugsMapper;
	
	private final BugsService bugsService;
	
	private final BugsRepository bugsRepository;
	
	private final BugsSearchRepository bugsSearchRepository;
	
	public TasksService(TasksRepository tasksRepository, TasksMapper tasksMapper, TasksSearchRepository tasksSearchRepository, TasksProcessingService tasksProcessingService,
			ActivitiService activitiService, ProjectWorkflowsQueryService projectWorkflowsQueryService, PackagesRepository packagesRepository, FieldConfigService fieldConfigService,
			PurchaseOrdersRepository purchaseOrdersRepository, ExcelGenerator excelGenerator, TasksQueryService tasksQueryService, NotificationService notificationService,
			NotificationTemplateService notificationTemplateService, UserRepository userRepository, ElasticsearchTemplate elasticsearchTemplate,
			TMSCustomFieldScreenValueService tMSCustomFieldScreenValueService, TMSCustomFieldScreenValueQueryService tMSCustomFieldScreenValueQueryService,
			TaskTrackingTimeRepository taskTrackingTimeRepository, BugsMapper bugsMapper, BugsService bugsService, BugsRepository bugsRepository,
			BugsSearchRepository bugsSearchRepository, TMSCustomFieldScreenValueSearchRepository tMSCustomFieldScreenValueSearchRepository,
			TMSCustomFieldScreenValueMapper tMSCustomFieldScreenValueMapper, ProjectsRepository projectsRepository) {
		this.tasksRepository = tasksRepository;
		this.tasksMapper = tasksMapper;
		this.tasksSearchRepository = tasksSearchRepository;
		this.tasksProcessingService = tasksProcessingService;
		this.projectWorkflowsQueryService = projectWorkflowsQueryService;
		this.packagesRepository = packagesRepository;
		this.fieldConfigService = fieldConfigService;
		this.purchaseOrdersRepository = purchaseOrdersRepository;
		this.excelGenerator = excelGenerator;
		this.tasksQueryService = tasksQueryService;
		this.notificationService = notificationService;
		this.notificationTemplateService = notificationTemplateService;
		this.userRepository = userRepository;
		this.elasticsearchTemplate = elasticsearchTemplate;
		this.tMSCustomFieldScreenValueService = tMSCustomFieldScreenValueService;
		this.tMSCustomFieldScreenValueQueryService = tMSCustomFieldScreenValueQueryService;
		this.taskTrackingTimeRepository = taskTrackingTimeRepository;
		this.bugsMapper = bugsMapper;
		this.bugsService = bugsService;
		this.bugsRepository = bugsRepository;
		this.bugsSearchRepository = bugsSearchRepository;
		this.tMSCustomFieldScreenValueSearchRepository = tMSCustomFieldScreenValueSearchRepository;
		this.tMSCustomFieldScreenValueMapper = tMSCustomFieldScreenValueMapper;
		this.projectsRepository = projectsRepository;
	}

	/**
	 * Save a tasks.
	 *
	 * @param tasksDTO
	 *            the entity to save
	 * @return the persisted entity
	 */
	public TasksDTO save(TasksDTO tasksDTO, TaskBiddingDTO taskBiddingDTO) {
		log.debug("Request to save Tasks : {}", tasksDTO);
		Tasks tasks = tasksMapper.toEntity(tasksDTO);
//		tasks = tasksRepository.save(tasks);
		
		String template = this.notificationTemplateService.getContentTemplate(NotificationCategory.TASK);
		if (StringUtils.isEmpty(template)) {
			template = StringUtils.EMPTY;
		}
		String body = StringUtils.EMPTY;
		String content = StringUtils.EMPTY;
		Optional<String> currentUserLogin = SecurityUtils.getCurrentUserLogin();
		Packages packages = packagesRepository.findOne(tasksDTO.getPackagesId());
		if (packages != null) {
			if (tasksDTO.getId() != null) {
				Tasks tasksBeforeChangeDomain = this.tasksMapper.toEntity(this.tasksMapper.toDto(this.tasksRepository.findOne(tasksDTO.getId())));
				tasks = tasksRepository.save(tasks);
				PurchaseOrders purchaseOrders = packages.getPurchaseOrders();
				ProjectTemplates projectTemplate = purchaseOrders.getProjectTemplates();
				if (projectTemplate == null) {
					projectTemplate = purchaseOrders.getProject().getProjectTemplates();
				}
				ProjectWorkflowsCriteria criteria = new ProjectWorkflowsCriteria();
				LongFilter projectTemplateFilter = new LongFilter();
				projectTemplateFilter.setEquals(projectTemplate.getId());
				criteria.setProjectTemplatesId(projectTemplateFilter);

				StringFilter nameFilter = new StringFilter();
				nameFilter.setEquals("Task");
				criteria.setName(nameFilter);
				
				List<ProjectWorkflowsDTO> projectWorkflowsDTOs = this.projectWorkflowsQueryService
						.findByCriteria(criteria);
				
				if (projectWorkflowsDTOs.size() > 0 && (TaskStatus.DOING.equals(tasks.getStatus())
						|| TaskStatus.OPEN.equals(tasks.getStatus())
						|| TaskStatus.CANCEL.equals(tasks.getStatus())
						|| TaskStatus.PENDING.equals(tasks.getStatus()))) {
					String processKey = projectWorkflowsDTOs.get(0).getActivity();
					Map<String, String> processResult = tasksProcessingService.startProcessing(processKey, tasks, packages, taskBiddingDTO);
					
					//Update task to get affect after processed Task by work flow
					tasks = tasksRepository.save(tasks);
					if(processResult != null && processResult.size() > 1) {
						String role = this.getRole(processResult.get("nextRound"));
						if (StringUtils.isNotEmpty(processResult.get("user"))) {
						content = template.replace("%task_name%", tasks.getName()).replace("%status%", processResult.get("status")).replace("%user%", currentUserLogin.get());
						body = String.format("{\"type\": \"notification\", \"view\": \"op\", \"content\": \"%s\", \"project\": \"%s\", \"roles\": \"%s\", \"tasksId\": \"%s\"}",
								content, packages.getPurchaseOrders().getProject().getId(), role, tasks.getId());
							this.saveAndPushNotification(body, currentUserLogin.get(), processResult.get("user"));
						}
					}
					this.isChangeAssigneeNotify(tasksBeforeChangeDomain, tasks, processResult.get("nextRound"), packages, currentUserLogin.get());
				}
					
			} else {
				tasks = tasksRepository.save(tasks);
				if (TaskStatus.OPEN.equals(tasks.getStatus()) && StringUtils.isNotEmpty(tasks.getOp())) {
					content = template.replace("%task_name%", tasks.getName()).replace("%status%", AppConstants.OPEN).replace("%user%", currentUserLogin.get());
					body = String.format("{\"type\": \"notification\", \"view\": \"op\", \"content\": \"%s\", \"project\": \"%s\", \"roles\": \"OPERATOR\"}",
							content, packages.getPurchaseOrders().getProject().getId());
					this.saveAndPushNotification(body, currentUserLogin.get(), tasks.getOp());
				}
			}
		}
		List<TMSCustomFieldScreenValueDTO> listTMSCustomFieldScreenValueDTO = tasksDTO.getTmsCustomFieldScreenValueDTO();
		
		if(!CollectionUtils.isEmpty(listTMSCustomFieldScreenValueDTO)) {
			for (TMSCustomFieldScreenValueDTO tmsCustomField: listTMSCustomFieldScreenValueDTO) {
				if (tmsCustomField.getId() != null) {
					tMSCustomFieldScreenValueService.save(tmsCustomField);
					tMSCustomFieldScreenValueSearchRepository.save(tMSCustomFieldScreenValueMapper.toEntity(tmsCustomField));
				} else {
					tmsCustomField.setTasksId(tasks.getId());
					tMSCustomFieldScreenValueService.save(tmsCustomField);
					tMSCustomFieldScreenValueSearchRepository.save(tMSCustomFieldScreenValueMapper.toEntity(tmsCustomField));
				}
			}
		}
		TasksDTO result = tasksMapper.toDto(tasks);
		tasksSearchRepository.save(tasks);
		return result;
	}

	/**
	 * Get all the tasks.
	 *
	 * @param pageable
	 *            the pagination information
	 * @return the list of entities
	 */
	@Transactional(readOnly = true)
	public Page<TasksDTO> findAll(Pageable pageable) {
		log.debug("Request to get all Tasks");
		return tasksRepository.findAll(pageable).map(tasksMapper::toDto);
	}

	public List<FieldConfigVM> getTaskFieldConfig(Long poID) {
		PurchaseOrders purchaseOrders = purchaseOrdersRepository.findOne(poID);
		Projects project = purchaseOrders.getProject();
		// List<FieldConfigVM> fieldConfigVMs = this.fieldConfigService.getAllFieldConfig(project.getId(), AppConstants.TASK_ENTITY);
		TMSDynamicCustomFieldVM tmsDynamicCustomFieldVM = this.fieldConfigService.getAllFieldConfig(project.getId(), AppConstants.TASK_ENTITY);
		List<FieldConfigVM> fieldConfigVMs = tmsDynamicCustomFieldVM.getFieldConfigVMs();
		return fieldConfigVMs;
	}
	
	public TMSDynamicCustomFieldVM getTaskDynamicFieldConfig(Long poID) {
		PurchaseOrders purchaseOrders = purchaseOrdersRepository.findOne(poID);
		TMSDynamicCustomFieldVM fieldConfigVMs = this.fieldConfigService.getAllDynamicFieldConfig(purchaseOrders, AppConstants.TASK_ENTITY);
		return fieldConfigVMs;
	}

	/**
	 * Get one tasks by id.
	 *
	 * @param id
	 *            the id of the entity
	 * @return the entity
	 */
	@Transactional(readOnly = true)
	public TasksDTO findOne(Long id) {
		log.debug("Request to get Tasks : {}", id);
		Tasks tasks = tasksRepository.findOne(id);
		TasksDTO tasksDTO = tasksMapper.toDto(tasks);
		tasksDTO.setTmsCustomFieldScreenValueDTO(tMSCustomFieldScreenValueQueryService.getAllTMSCustomFieldScreenValueByTaskId(id));
		return tasksDTO;
	}

	/**
	 * Delete the tasks by id.
	 *
	 * @param id
	 *            the id of the entity
	 * @throws Exception 
	 */
	public void delete(Long id) throws Exception {
		log.debug("Request to delete Tasks : {}", id);
		int taskExist = Integer.parseInt(taskTrackingTimeRepository.checkTaskIdExist(id).toString());
		if (taskExist != 1) {
			tasksRepository.delete(id);
			tasksSearchRepository.delete(id);
		} else {
			throw new Exception();
		}
	}

	/**
	 * Search for the tasks corresponding to the query.
	 *
	 * @param query
	 *            the query of the search
	 * @param pageable
	 *            the pagination information
	 * @return the list of entities
	 */
	
	@Transactional(readOnly = true)
	public Page<TasksDTO> search(Long packageId, String taskName, String taskStatus, String assignee, String description, Instant fromDate, Instant toDate, Pageable pageable) {
		String query = String.format("%s| %s |%s | %s", taskName , taskStatus , assignee , description);
		log.debug("Request to search for a page of Tasks for query {}", query);
		BoolQueryBuilder boolQueries = buildTaskFilterQuery(packageId, taskName, taskStatus, assignee, description,
				fromDate, toDate);
		SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(boolQueries).withPageable(pageable).build();
		Page<Tasks> result = elasticsearchTemplate.queryForPage(searchQuery, Tasks.class);
		List<TasksDTO> listTasksDTO = tasksMapper.toDto(result.getContent());
		for (TasksDTO taskDTO: listTasksDTO) {
			taskDTO.setTmsCustomFieldScreenValueDTO(tMSCustomFieldScreenValueMapper.toDto(tMSCustomFieldScreenValueSearchRepository.findAllByTasksId(taskDTO.getId())));
        }
		return result.map(tasksMapper::toDto);
	}

	/**
	 * @author NGOC-VX
	 * @param id project id
	 * @return fieldConfigVMs
	 */
	public TMSDynamicCustomFieldVM getTasksFieldConfigOpGridDTO(Long id) {
		//List<FieldConfigVM> fieldConfigVMs = this.fieldConfigService.getAllFieldConfigOpGridDTO(id, AppConstants.TASK_ENTITY);
		TMSDynamicCustomFieldVM fieldConfigVMs = this.fieldConfigService.getAllDynamicFieldConfigOP(id, AppConstants.TASK_ENTITY);
		return fieldConfigVMs;
	}

	/**
	 * Create excel file
	 *
	 * @param packageID
	 * @throws IOException
	 */
	public InputStreamResource exportExcel(Long packageId, String taskName, String taskStatus, String assignee, String description, Instant fromDate, Instant toDate) throws IOException {
		Packages packages = packagesRepository.findOne(packageId);
		PurchaseOrders purchaseOrders = packages.getPurchaseOrders();
		ProjectTemplates projectTemplates = purchaseOrders.getProjectTemplates();
		if (projectTemplates == null) {
			projectTemplates = purchaseOrders.getProject().getProjectTemplates();
		}
		// Projects project = packages.getPurchaseOrders() != null ? packages.getPurchaseOrders().getProject() : null;
		TasksCriteria tasksCriteria = new TasksCriteria();
		LongFilter packageIDFilter = new LongFilter();
		packageIDFilter.setEquals(packageId);
		tasksCriteria.setPackagesId(packageIDFilter);
		//List<TasksDTO> taskDTOs = this.tasksQueryService.findByCriteria(tasksCriteria);
		//BoolQueryBuilder boolQueries = buildTaskFilterQuery(packageId, taskName, taskStatus, assignee, description,
		//		fromDate, toDate);
		//SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(boolQueries).build();
		//List<Tasks> tasks = elasticsearchTemplate.queryForList(searchQuery, Tasks.class);
		List<TaskStatus> statuses = new ArrayList<TaskStatus>();
		if(taskStatus.equals("*")) {
			statuses.add(TaskStatus.DOING);
			statuses.add(TaskStatus.PENDING);
			statuses.add(TaskStatus.NA);
			statuses.add(TaskStatus.OPEN);
			statuses.add(TaskStatus.DONE);
			statuses.add(TaskStatus.CANCEL);
			statuses.add(TaskStatus.CLOSED);
		}else {
			statuses.add(TaskStatus.valueOf(taskStatus));
		}
    	
		List<Tasks> tasks = this.tasksRepository.getAllTasksByPackageIdAndNameAndStatusAndFromDateAndToDateAndAssigneeAnDescription(packageId, taskName, statuses, assignee, description, fromDate, toDate);
		List<TasksDTO> taskDTOs = tasksMapper.toDto(tasks);
		if(projectTemplates != null) {
			ProjectWorkflowsCriteria criteria = new ProjectWorkflowsCriteria();
			StringFilter nameFilter = new StringFilter();
			nameFilter.setEquals(AppConstants.TASK_ENTITY);
			criteria.setName(nameFilter);
			LongFilter filter = new LongFilter();
			filter.setEquals(projectTemplates.getId());
			criteria.setProjectTemplatesId(filter);
			List<ProjectWorkflowsDTO> projectWorkflowsDTOs = this.projectWorkflowsQueryService.findByCriteria(criteria);
			if (CollectionUtils.isNotEmpty(projectWorkflowsDTOs)) {
				ByteArrayInputStream in = excelGenerator.exportTaskToExcel(projectWorkflowsDTOs, taskDTOs, purchaseOrders.getProject().getId());
				return new InputStreamResource(in);
			} else {
				return null;
			}
		} else {
			return null;
		}

	}

	/**
	 * Update task base one task list from excel file
	 * @param file: excel file
	 * @author TuHP
	 * @throws IOException
	 */

	public void updateTasks(MultipartFile file, Long packageID) throws IOException {
		Packages packages = packagesRepository.findOne(packageID);
		PurchaseOrders purchaseOrders = packages.getPurchaseOrders();
		ProjectTemplates projectTemplates = purchaseOrders.getProjectTemplates();
		if (projectTemplates == null) {
			projectTemplates = purchaseOrders.getProject().getProjectTemplates();
		}
		if(projectTemplates != null) {
			ProjectWorkflowsCriteria criteria = new ProjectWorkflowsCriteria();
			StringFilter nameFilter = new StringFilter();
			nameFilter.setEquals(AppConstants.TASK_ENTITY);
			criteria.setName(nameFilter);
			LongFilter filter = new LongFilter();
			filter.setEquals(projectTemplates.getId());
			criteria.setProjectTemplatesId(filter);
			List<ProjectWorkflowsDTO> projectWorkflowsDTOs = this.projectWorkflowsQueryService.findByCriteria(criteria);
			if (CollectionUtils.isNotEmpty(projectWorkflowsDTOs)) {
				excelGenerator.importTasks(projectWorkflowsDTOs, file, packages);
			}
		}
	}
	
	/**
	 * Get Role
	 * @param round
	 * @return role
	 */
	private String getRole(String round) {
		switch (round) {
		case "op":
			return "OPERATOR";
		case "fixer":
			return "OPERATOR";
		case "fi":
			return "FI";
		default:
			return "REVIEWER";
		}
	}
	
	private void saveAndPushNotification(String body, String from, String to) {
		try {
			ObjectMapper objMap = new ObjectMapper();
			NotificationDTO notificationDTO = new NotificationDTO();
			notificationDTO.setBody(body);
			notificationDTO.setFrom(from.toLowerCase());
			notificationDTO.setStatus(false);
			notificationDTO.setTo(to.toLowerCase());
			notificationDTO = this.notificationService.save(notificationDTO);
			
			// Transfer data to web socket
			String jsonNotification = null;
			try {
				jsonNotification = objMap.writeValueAsString(notificationDTO);
			} catch (JsonProcessingException e) {
				jsonNotification = StringUtils.EMPTY;
			}
			CommunicationService.notifyToUser(to.toLowerCase(), jsonNotification.replace("\\\"", "\"")
					.replace("\"{", "{").replace("}\"", "}"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @param tasksbeforeChange
	 * @param tasksAfterChange
	 * @param currentRound
	 * @param packages
	 * @param currentUserLogin
	 */
	private void isChangeAssigneeNotify(Tasks tasksbeforeChange, Tasks tasksAfterChange, String currentRound,
			Packages packages, String currentUserLogin) {
		String status = this.tasksProcessingService.getCurrentStatus(tasksAfterChange, currentRound);
		if (status.equals(AppConstants.OPEN) || status.equals(AppConstants.PENDING) || status.equals(AppConstants.REOPEN)) {
			String oldAssignee = this.getAssigneeOfTasks(tasksbeforeChange, currentRound);
			String newAssignee = this.getAssigneeOfTasks(tasksAfterChange, currentRound);
			if (!oldAssignee.toLowerCase().equals(newAssignee.toLowerCase())) {
				String role = this.getRole(currentRound);
				String content = StringUtils.EMPTY;
				String body = StringUtils.EMPTY;
				String template = this.notificationTemplateService
						.getContentTemplate(NotificationCategory.REASSIGNED_TASK);
				if (StringUtils.isEmpty(template)) {
					template = StringUtils.EMPTY;
				}
				if (StringUtils.isNotEmpty(oldAssignee)) {
					content = template.replace("%task_name%", tasksbeforeChange.getName()).replace("%user%",
							newAssignee);
					body = String.format("{\"type\": \"notification\", \"view\": \"op\", \"content\": \"%s\"}",
							content);

					this.saveAndPushNotification(body, currentUserLogin, oldAssignee);
				}
				
				if (StringUtils.isNotEmpty(newAssignee)) {
					content = template.replace("%task_name%", tasksAfterChange.getName()).replace("%user%", "you");
					body = String.format(
							"{\"type\": \"notification\", \"view\": \"op\", \"content\": \"%s\", \"project\": \"%s\", \"roles\": \"%s\", \"tasksId\": \"%s\"}",
							content, packages.getPurchaseOrders().getProject().getId(), role, tasksAfterChange.getId());
					this.saveAndPushNotification(body, currentUserLogin, newAssignee);
				}
			}
		}
	}
	
	/**
	 * Get the current status at current round when processing task
	 * @param tasks
	 * @param round
	 * @return current round status of task
	 */
	private String getAssigneeOfTasks(Tasks tasks, String round) {
		try {
			switch (round) {
			case AppConstants.ROUND_REVIEW1:
				return tasks.getReview1().toString();
			case AppConstants.ROUND_REVIEW2:
				return tasks.getReview2().toString();
			case AppConstants.ROUND_FIXER:
				return tasks.getFixer().toString();
			case AppConstants.ROUND_FI:
				return tasks.getFi().toString();
			}
			return tasks.getOp().toString();
		} catch (Exception e) {
			return StringUtils.EMPTY;
		}
		
	}
	
	/**
	 * Generate elasticsearch string in case filter tasks
	 * @param packageId
	 * @param taskName
	 * @param taskStatus
	 * @param assignee
	 * @param description
	 * @param fromDate
	 * @param toDate
	 * @return
	 */
	private BoolQueryBuilder buildTaskFilterQuery(Long packageId, String taskName, String taskStatus, String assignee,
			String description, Instant fromDate, Instant toDate) {
		BoolQueryBuilder boolQueries = QueryBuilders.boolQuery();
		if(packageId != null) {
			boolQueries.must(QueryBuilders.matchQuery("packages.id", packageId));
		}
		
		if(!StringUtils.isEmpty(taskName)) {
			boolQueries.must(QueryBuilders.matchPhrasePrefixQuery("name", taskName));
		}
		
		if(!StringUtils.isEmpty(assignee)) {	
			BoolQueryBuilder role = QueryBuilders.boolQuery();
			role.should(QueryBuilders.matchPhrasePrefixQuery("op", assignee));
			role.should(QueryBuilders.matchPhrasePrefixQuery("review1", assignee));
			role.should(QueryBuilders.matchPhrasePrefixQuery("review2", assignee));
			role.should(QueryBuilders.matchPhrasePrefixQuery("fi", assignee));
			role.should(QueryBuilders.matchPhrasePrefixQuery("fixer", assignee));
			boolQueries.must(role);
		}
		
		if(!taskStatus.equals("*")) {
			boolQueries.must(QueryBuilders.matchPhrasePrefixQuery("status", taskStatus));
		}
		
		if(!StringUtils.isEmpty(description)) {			
			boolQueries.must(QueryBuilders.matchPhrasePrefixQuery("description", description));
		}
		
		boolQueries.must(QueryBuilders.rangeQuery("estimateStartTime").gte(fromDate).lte(toDate));
		boolQueries.must(QueryBuilders.rangeQuery("estimateEndTime").gte(fromDate).lte(toDate));
		return boolQueries;
	}
	 /** get select Tasks destination 
		 *  which includes all the PO 
		 *  (has PROCESSING, OPEN status)
		 *  PhuVD3
		 */
	    @Transactional(readOnly = true)
	    public List<TasksDTO> findListTasksClone(Long packageId) {
	        log.debug("Request to get all PurchaseOrders");
	        return tasksMapper.toDto(this.tasksRepository.getAllTasksByClone(packageId));
	    }

	    /** get select Tasks destination 
		 *  which includes all the PO 
		 *  (has All status)
		 *  PhuVD3
		 */
	    @Transactional(readOnly = true)
	    public List<TasksDTO> findAllListTasksClone(Long packageId) {
	        log.debug("Request to get all PurchaseOrders");
	        return tasksMapper.toDto(this.tasksRepository.getAllTasksByClonePa(packageId));
	    }

	    /** save Tasks destination 
		 *  which includes all the tasks
		 *  (OPEN status)
		 * @author PhuVD3
		 */
	    public void cloneTasks(List<TasksDTO> lstTasks, Long packageId) {
	        log.debug("Request to clone Tasks : {}");

	        for (TasksDTO tasksDTO : lstTasks) {
	        	tasksDTO.setId(null);
	        	tasksDTO.setPackagesId(packageId);
	        	Tasks task = tasksMapper.toEntity(tasksDTO);
	        	tasksRepository.save(task);
			}
		}

	    /** Update Tasks destination 
		 *  which includes all the tasks
		 *  @author PhuVD3
		 */
	    public void moveTasksList(IdListVM idList, Long packageId) {
	        log.debug("Request to move Tasks : {}");

	        List<Long> idArr = idList.getIdList();
	        Tasks task;
	        for (Long id : idArr) {
	        	task = this.tasksRepository.findOne(id);
	        	task.setPackages(this.packagesRepository.findOne(packageId));
	        	tasksRepository.save(task);
			}
		}
	    
	    /** save Tasks destination 
		 *  which includes all the tasks
		 *  (OPEN status)
		 * @author PhuVD3
		 */
	    public void moveTasksClone(List<TasksDTO> lstTasks, Long packageId) {
	        log.debug("Request to move Tasks : {}");

	        for (TasksDTO tasksDTO : lstTasks) {
	        	Tasks task = tasksMapper.toEntity(tasksDTO);
	        	task.setPackages(this.packagesRepository.findOne(packageId));
	        	tasksRepository.save(task);
			}
		}

		public List<TasksDTO> updateReOpenMultiTasks(MultiTasksReOpenVM multiTasksReOpenVM) throws IOException {
			Set<TasksDTO> listTasksReOpen = multiTasksReOpenVM.getListTasksReOpen();
			BugsDTO bugCommon = multiTasksReOpenVM.getBugCommon();;
			Bugs bugs = new Bugs();
			List<TasksDTO> result = new ArrayList<>();
			TasksDTO taskResult;
			Attachments attachmentsDomain;
			Set<Attachments> attachmentsDomainSet = new HashSet<>();
			Set<AttachmentsDTO> attachmentDtos = bugCommon.getAttachments();
			for (TasksDTO tasksDTO : listTasksReOpen) {
				taskResult = save(tasksDTO, null);
				bugCommon.setTasksId(taskResult.getId());
				bugs = this.bugsMapper.toEntity(bugCommon);
				bugs.setAttachments(attachmentsDomainSet);
				bugs = this.bugsRepository.save(bugs);
				
				if (!CollectionUtils.isEmpty(attachmentDtos)) {
					for (AttachmentsDTO attachmentDto : attachmentDtos) {
						attachmentsDomain = new Attachments();
						attachmentsDomain = this.bugsService.processAttachments(attachmentDto, bugs);
						attachmentsDomainSet.add(attachmentsDomain);
					}
					bugs.setAttachments(attachmentsDomainSet);
				}
				bugs = this.bugsRepository.save(bugs);
				this.bugsSearchRepository.save(bugs);
				result.add(taskResult);
			}
			return result;
		}

		public Map<String, Object> getCurrentTasksHasBugByRound(String step, Long packagesId) {
			Map<String, Object> result = new HashMap<String, Object>();
			ReviewRatioVM reviewRatioVM = this.getReviewRatio(packagesId);
			if (reviewRatioVM != null) {
				Integer currentRatioExceptBugs = null;
				switch (step) {
				case "review1":
					currentRatioExceptBugs = reviewRatioVM.getReview1Ratio();
					break;
				case "review2":
					currentRatioExceptBugs = reviewRatioVM.getReview2Ratio();
					break;
				default:
					currentRatioExceptBugs = reviewRatioVM.getFiRatio();
					break;
				}
				
				if (currentRatioExceptBugs != null) {
					
					String[] listStatus = {AppConstants.OPEN, AppConstants.REOPEN, AppConstants.DOING, AppConstants.PENDING};
					int countTasksHasBugsInPackage = this.tasksRepository.countTaskHasBugInPackagesByRound(packagesId, step, listStatus);
					int countTasksInPackages = this.tasksRepository.countTasksInPackages(packagesId);
					Float currentTasksHasBugInPackages = Float.valueOf(countTasksHasBugsInPackage)/Float.valueOf(countTasksInPackages) * 100;
					result.put("currentRatioExceptBugs", currentRatioExceptBugs);
					result.put("currentTasksHasBugInPackages", currentTasksHasBugInPackages);
				}
			}
			return result;
		}
		
		private ReviewRatioVM getReviewRatio(Long packagesId) {
			Packages packages = this.packagesRepository.findOne(packagesId);
			PurchaseOrders purchaseOrders = packages.getPurchaseOrders();
			ReviewRatioVM reviewRatioVM = null;
			String reviewRatio = purchaseOrders.getReviewRatio();
			if (reviewRatio != null) {
				ObjectMapper mapper = new ObjectMapper();
				try {
					reviewRatioVM = mapper.readValue(reviewRatio, new TypeReference<ReviewRatioVM>(){});
				} catch (JsonParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			return reviewRatioVM;
		}

		public InputStreamResource exportExcelTasksHistory(Long projectId, String roles, String beginTime, String endTime, String userLogin) throws ParseException, IOException {
			Projects projects = this.projectsRepository.findOne(projectId);
			if (projects != null) {
				ProjectTemplates projectTemplates = projects.getProjectTemplates();
				if (projectTemplates != null) {
					ProjectWorkflowsCriteria criteria = new ProjectWorkflowsCriteria();
					StringFilter nameFilter = new StringFilter();
					nameFilter.setEquals(AppConstants.TASK_ENTITY);
					criteria.setName(nameFilter);
					LongFilter filter = new LongFilter();
					filter.setEquals(projectTemplates.getId());
					criteria.setProjectTemplatesId(filter);
					List<ProjectWorkflowsDTO> projectWorkflowsDTOs = this.projectWorkflowsQueryService.findByCriteria(criteria);
					if (CollectionUtils.isNotEmpty(projectWorkflowsDTOs)) {
						Instant from = null;
				        Instant to = null;
						SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
						if (StringUtils.isNotEmpty(beginTime)) {
							beginTime = beginTime.replace("/", "-");
							from = sf.parse(beginTime).toInstant().atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay().toInstant(ZoneOffset.UTC);
						} 

						if (StringUtils.isNotEmpty(endTime)) {
							endTime = endTime.replace("/", "-");
							to = sf.parse(endTime).toInstant().atZone(ZoneId.systemDefault()).toLocalDate().atTime(LocalTime.MAX).toInstant(ZoneOffset.UTC);
						}
						
						List<TaskStatus> taskStatus = new ArrayList<TaskStatus>();
				        taskStatus.add(TaskStatus.OPEN);
				        taskStatus.add(TaskStatus.DOING);
				        taskStatus.add(TaskStatus.PENDING);
				        taskStatus.add(TaskStatus.DONE);
				        taskStatus.add(TaskStatus.CANCEL);
				        taskStatus.add(TaskStatus.CLOSED);
				        
				        List<OPStatus> statusOP = new ArrayList<OPStatus>();
				        statusOP.add(OPStatus.DONE);
				        
				        List<ReviewStatus> statusReview = new ArrayList<ReviewStatus>();
				        statusReview.add(ReviewStatus.DONE);
				        statusReview.add(ReviewStatus.NOT_GOOD);
				        
				        List<FIStatus> statusFI = new ArrayList<FIStatus>();
				        statusFI.add(FIStatus.DONE);
				        statusFI.add(FIStatus.NOT_GOOD);
				        
				        List<FixStatus> statusFix = new ArrayList<FixStatus>();
				        statusFix.add(FixStatus.DONE);
						
						List<Tasks> tasksList = null;
						switch (roles) {
						case "OPERATOR": 
							tasksList = tasksRepository.findTasksDoneOfUserByRolesOPInProject(userLogin, projectId, statusOP, statusFix, from, to, taskStatus);
							break;
						case "REVIEWER":
							tasksList = tasksRepository.findTasksDoneOfUserByRolesReviewerInProject(userLogin, projectId, statusReview, from, to, taskStatus);
							break;
						case "FI":
							tasksList = tasksRepository.findTasksDoneOfUserByRolesFIInProject(userLogin, projectId, statusFI, from, to, taskStatus);
							break;
						default:
							tasksList = tasksRepository.findTasksDoneOfUserByRolesOPInProject(userLogin, projectId, statusOP, statusFix, from, to, taskStatus);
							break;
						}
						
						List<TasksDTO> tasksListDTO = this.tasksMapper.toDto(tasksList);
						
						ByteArrayInputStream in = excelGenerator.exportTaskToExcel(projectWorkflowsDTOs, tasksListDTO, projectId);
						return new InputStreamResource(in);
					}
				}
				
			}
			
			
			return null;
		}
}
