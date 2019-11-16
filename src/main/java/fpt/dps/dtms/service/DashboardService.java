package fpt.dps.dtms.service;

import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fpt.dps.dtms.domain.Packages;
import fpt.dps.dtms.domain.Tasks;
import fpt.dps.dtms.domain.enumeration.FIStatus;
import fpt.dps.dtms.domain.enumeration.OPStatus;
import fpt.dps.dtms.domain.enumeration.ReviewStatus;
import fpt.dps.dtms.domain.enumeration.TaskStatus;
import fpt.dps.dtms.repository.PackagesRepository;
import fpt.dps.dtms.repository.ProjectUsersRepository;
import fpt.dps.dtms.repository.ProjectsRepository;
import fpt.dps.dtms.repository.PurchaseOrdersRepository;
import fpt.dps.dtms.repository.TasksRepository;
import fpt.dps.dtms.service.dto.TasksCriteria;
import fpt.dps.dtms.service.dto.TasksCriteria.TaskStatusFilter;
import fpt.dps.dtms.service.dto.TasksDTO;
import fpt.dps.dtms.service.mapper.TasksMapper;
import fpt.dps.dtms.service.util.AppConstants;
import fpt.dps.dtms.service.util.CommonFunction;
import fpt.dps.dtms.service.util.FieldConfigService;
import fpt.dps.dtms.web.rest.util.PaginationUtil;
import fpt.dps.dtms.web.rest.vm.FieldConfigVM;
import fpt.dps.dtms.web.rest.vm.TMSDynamicCustomFieldVM;
import io.github.jhipster.service.filter.InstantFilter;
import io.github.jhipster.service.filter.LongFilter;
import org.springframework.data.domain.Page;

/**
 * Service Implementation for managing BusinessLine.
 */
@Service
@Transactional
public class DashboardService {

    private final Logger log = LoggerFactory.getLogger(DashboardService.class);

    private final PackagesRepository packagesRepository;
    
    private final TasksRepository tasksRepository;
    
    private final PurchaseOrdersRepository purchaseOrdersRepository;
    
    private final ProjectUsersRepository projectUsersRepository;
    
    private final FieldConfigService fieldConfigService;
    
    private final TasksQueryService tasksQueryService;
	
	private final PackagesQueryService packagesQueryService;
	
	private final ProjectsRepository projectsRepository;
	
	private final TasksMapper tasksMapper;

    public DashboardService(PackagesRepository packagesRepository, TasksRepository tasksRepository, PurchaseOrdersRepository purchaseOrdersRepository,
    		ProjectUsersRepository projectUsersRepository, FieldConfigService fieldConfigService,TasksQueryService tasksQueryService,
    		PackagesQueryService packagesQueryService, TasksMapper tasksMapper,  ProjectsRepository projectsRepository) {
    	this.packagesRepository = packagesRepository;
    	this.tasksRepository = tasksRepository;
    	this.purchaseOrdersRepository = purchaseOrdersRepository;
    	this.projectUsersRepository = projectUsersRepository;
    	this.fieldConfigService = fieldConfigService;
    	this.tasksQueryService = tasksQueryService;
		this.packagesQueryService = packagesQueryService;
		this.tasksMapper = tasksMapper;
		this.projectsRepository = projectsRepository;
    }

    /**
     * Get the number of Packages which relating to Project.
     *
     * @param id : project id
     * @return the number of packages
     */
    @Transactional(readOnly = true)
	public Integer getAllPackagesRelateToProject(Long id) {
		return this.packagesRepository.getSizePackageRelatingToProject(id);
	}

    /**
     * Get the number of Users which allocated to Project.
     *
     * @param id : project id
     * @return the number of users
     */
    @Transactional(readOnly = true)
	public Integer getAllUsersRelateToProject(Long id) {
		// TODO Auto-generated method stub
		return this.projectUsersRepository.getSizeUserRelatingToProject(id);
	}

    @Transactional(readOnly = true)
	public Integer getAllTasksRelateToProject(Long id) {
		// TODO Auto-generated method stub
		return this.tasksRepository.getSizeTasksRelatingToProject(id);
	}

	@Transactional(readOnly = true)
	public Integer getAllPurchaseOrdersRelateToProject(Long id) {
		// TODO Auto-generated method stub
		return this.purchaseOrdersRepository.getSizePurchaseOrdersRelatingToProject(id);
	}
	
	@Transactional(readOnly = true)
	public Map<String, Object> getAllUnAssignTasksRelateToProject(Long id, Pageable pageable) {
		Map<String, Object> map = new HashMap<String, Object>();
		boolean hasOP = false, hasReview1 = false, hasReview2 = false, hasFI = false;
		// List<FieldConfigVM> fieldConfigVMs = this.fieldConfigService.getAllFieldConfig(id, AppConstants.TASK_ENTITY);
		TMSDynamicCustomFieldVM tmsDynamicCustomFieldVM = this.fieldConfigService.getAllFieldConfig(id, AppConstants.TASK_ENTITY);
		List<FieldConfigVM> fieldConfigVMs = tmsDynamicCustomFieldVM.getFieldConfigVMs();
		if(hasRound(id, AppConstants.ROUND_OP, fieldConfigVMs)) {
			hasOP = true;
		}
		if(hasRound(id, AppConstants.ROUND_REVIEW1, fieldConfigVMs)) {
			hasReview1 = true;
		}
		if(hasRound(id, AppConstants.ROUND_REVIEW2, fieldConfigVMs)) {
			hasReview2 = true;
		}
		if(hasRound(id, AppConstants.ROUND_FI, fieldConfigVMs)) {
			hasFI = true;
		}
		Page<Tasks> tasksPage = this.tasksRepository.findTasksUnAssignRelateToProjecId(id, hasOP, hasReview1, hasReview2, hasFI, pageable);
		List<Tasks> tasks = tasksPage.getContent();
		List<FieldConfigVM> fields = getTasksFieldConfigForDashboard();
		List<Map<String, Object>> result = tasks.stream().map(t ->{
			Map<String, Object> object = new HashMap<String, Object>();
				for (FieldConfigVM field : fields) {
					switch (field.getField()) {
					case "po":
						object.put("po", t.getPackages().getPurchaseOrders().getName());
						break;
					case "package":
						object.put("package", t.getPackages().getName());
						break;
					case "name":
						object.put("name", t.getName());
						break;
					case "status":
						object.put("status", t.getStatus());
						break;
					case "estimateStartDate":
						object.put("estimateStartDate", t.getEstimateStartTime() == null ? null : t.getEstimateStartTime());
						break;
					case "estimateEndDate":
							object.put("estimateEndDate", t.getEstimateEndTime()  == null ? null : t.getEstimateEndTime());
						break;
					case "actualStartDate":
						object.put("actualStartDate", t.getOpStartTime() == null ? null : t.getOpStartTime());
						break;
					case "late":
						//Instant now = Instant.now();
						//Duration between = Duration.between(Instant.now(), t.getEstimateEndTime());
						//object.put("late", between.abs().toHours());
						String late = t.getEstimateEndTime() == null ? null : CommonFunction.GetTimeBetweenTwoDays(t.getEstimateEndTime(), Instant.now(), null);
						object.put("late", late);
						break;
					}
				}
			return object;
		}).collect(Collectors.toList());
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(tasksPage, "/api/dashboard/projects/{id}/tasks-unassign");
		map.put("headers", headers);
		map.put("result", result);
		return map;
	}
	
	@Transactional(readOnly = true)
	public List<FieldConfigVM> getUnAssignTasksFieldConfigForDashboard() {
		List<FieldConfigVM> fieldConfigVMs = new ArrayList<>();
		// Create PO field.
		createFieldConfig(fieldConfigVMs, "PO", "po", "agTextColumnFilter", 200, false);
		// Create Package field.
		createFieldConfig(fieldConfigVMs, "Package", "package", "agTextColumnFilter", 200, false);
		// Create Name field.
		createFieldConfig(fieldConfigVMs, "Name", "name", "agTextColumnFilter", 250, false);
		// Create Status field.
		createFieldConfig(fieldConfigVMs, "Status", "status", "agTextColumnFilter", 120, false);
		// Create Status field.
		createFieldConfig(fieldConfigVMs, "Estimated Start Date", "estimateStartDate", "agTextColumnFilter", 120, true);
		// Create Status field.
		createFieldConfig(fieldConfigVMs, "Estimated End Date", "estimateEndDate", "agTextColumnFilter", 120, true);
		// Create Status field.
		createFieldConfig(fieldConfigVMs, "Actual Start Date", "actualStartDate", "agTextColumnFilter", 120, false);
		// Create Status field.
		createFieldConfig(fieldConfigVMs, "Late", "late", "agTextColumnFilter", 120, false);
		return fieldConfigVMs;
	}
	
	@Transactional(readOnly = true)
	public Map<String, Object> getAllLatedTasksRelateToProject(Long id, Pageable pageable) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<TaskStatus> status = new ArrayList<TaskStatus>();
    	status.add(TaskStatus.DOING);
    	status.add(TaskStatus.PENDING);
    	status.add(TaskStatus.NA);
    	status.add(TaskStatus.OPEN);
		Page<Tasks> tasksPage = this.tasksRepository.findLatedTasksRelatingProjectId(id, status, pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(tasksPage, "/api/dashboard/projects/{id}/tasks-late");
		map.put("headers", headers);
		List<Tasks> tasks = tasksPage.getContent();
		List<FieldConfigVM> fields = getTasksFieldConfigForDashboard();
		List<Map<String, Object>> result = tasks.stream().map(t ->{
			Map<String, Object> object = new HashMap<String, Object>();
				for (FieldConfigVM field : fields) {
					switch (field.getField()) {
					case "po":
						object.put("po", t.getPackages().getPurchaseOrders().getName());
						break;
					case "package":
						object.put("package", t.getPackages().getName());
						break;
					case "name":
						object.put("name", t.getName());
						break;
					case "status":
						object.put("status", t.getStatus());
						break;
					case "estimateStartDate":
						object.put("estimateStartDate", t.getEstimateStartTime());
						break;
					case "estimateEndDate":
							object.put("estimateEndDate", t.getEstimateEndTime());
						break;
					case "actualStartDate":
						object.put("actualStartDate", t.getOpStartTime());
						break;
					case "late":
//						Instant now = Instant.now();
//						Duration between = Duration.between(Instant.now(), t.getEstimateEndTime());
//						object.put("late", between.abs().toHours());
						object.put("late", CommonFunction.GetTimeBetweenTwoDays(t.getEstimateEndTime(), Instant.now(), null));
						break;
					}
				}
			return object;
		}).collect(Collectors.toList());
		map.put("result", result);
		return map;
	}
	
	@Transactional(readOnly = true)
	public List<FieldConfigVM> getTasksFieldConfigForDashboard() {
		List<FieldConfigVM> fieldConfigVMs = new ArrayList<>();
		// Create PO field.
		createFieldConfig(fieldConfigVMs, "PO", "po", "agTextColumnFilter", 200, false);
		// Create Package field.
		createFieldConfig(fieldConfigVMs, "Package", "package", "agTextColumnFilter", 200, false);
		// Create Name field.
		createFieldConfig(fieldConfigVMs, "Name", "name", "agTextColumnFilter", 250, false);
		// Create Status field.
		createFieldConfig(fieldConfigVMs, "Status", "status", "agTextColumnFilter", 120, false);
		// Create Status field.
		createFieldConfig(fieldConfigVMs, "Estimated Start Date", "estimateStartDate", "agTextColumnFilter", 120, true);
		// Create Status field.
		createFieldConfig(fieldConfigVMs, "Estimated End Date", "estimateEndDate", "agTextColumnFilter", 120, true);
		// Create Status field.
		createFieldConfig(fieldConfigVMs, "Actual Start Date", "actualStartDate", "agTextColumnFilter", 120, false);
		// Create Status field.
		createFieldConfig(fieldConfigVMs, "Late", "late", "agTextColumnFilter", 120, false);
		return fieldConfigVMs;
	}
	
	@Transactional(readOnly = true)
	public Map<String, Object> getAllLatedPackagesRelateToProject(Long id, Pageable pageable) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<TaskStatus> taskStatus = new ArrayList<TaskStatus>();
        taskStatus.add(TaskStatus.DONE);
        taskStatus.add(TaskStatus.CLOSED);
        taskStatus.add(TaskStatus.CANCEL);
		
		Page<Packages> packagesPage = this.packagesRepository.findLatedPackagesRelatingProjectId(id, taskStatus, pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(packagesPage, "/api/dashboard/projects/{id}/packages-late");
		map.put("headers", headers);
		List<Packages> packages = packagesPage.getContent();
		List<FieldConfigVM> fields = getPackageFieldConfigForDashboard(id);
		List<Map<String, Object>> result = packages.stream().map(p ->{
			Map<String, Object> object = null;
			// Get task plan done
			TasksCriteria criteriaPlan = new TasksCriteria();
			LongFilter pId = new LongFilter();
			pId.setEquals(p.getId());
			criteriaPlan.setPackagesId(pId);
			InstantFilter estimateEndTime = new InstantFilter();
			estimateEndTime.setLessOrEqualThan(Instant.now());
			criteriaPlan.setEstimateEndTime(estimateEndTime);
			List<TasksDTO> planDone = this.tasksQueryService.findByCriteria(criteriaPlan);

			// Get task actual don
			TasksCriteria criteriaActual = new TasksCriteria();
			criteriaActual.setPackagesId(pId);
			TaskStatusFilter status = new TaskStatusFilter();
			status.setEquals(TaskStatus.DONE);
			criteriaActual.setStatus(status);
			List<TasksDTO> actualDone = this.tasksQueryService.findByCriteria(criteriaActual);
			DecimalFormat df = new DecimalFormat("###.#");
			if (planDone.size() > actualDone.size()) {
				object = new HashMap<String, Object>();
				for (FieldConfigVM field : fields) {
					switch (field.getField()) {
					case "po":
						object.put("po", p.getPurchaseOrders().getName());
						break;
					case "quantity":
						object.put("quantity", p.getTasks().size());
						break;
					case "actual":
						object.put("actual", df.format((actualDone.size() * 100 )/ p.getTasks().size() ) + " %");
						break;
					case "late":
						int late = planDone.size() - actualDone.size();
						int size = p.getTasks().size();
						double percent = Math.floor((late * 100) / size);
						object.put("late", df.format(percent)+ " %");
						break;
					case "op":
						object.put("op", packagesQueryService.getOpResult(p.getId()));
						break;
					case "reviewer":
						// List<FieldConfigVM> fieldConfigVMs = this.fieldConfigService.getAllFieldConfig(id, AppConstants.TASK_ENTITY);
						TMSDynamicCustomFieldVM tmsDynamicCustomFieldVM = this.fieldConfigService.getAllFieldConfig(id, AppConstants.TASK_ENTITY);
						List<FieldConfigVM> fieldConfigVMs = tmsDynamicCustomFieldVM.getFieldConfigVMs();
						object.put("reviewer", packagesQueryService.getReviewerResult(p.getId(), hasRound(id, AppConstants.ROUND_REVIEW2, fieldConfigVMs)));
						break;
					case "fi":
						object.put("fi", packagesQueryService.getFiResult(p.getId()));
						break;
					default:
						setObjectValue(field.getField(), object, p);
						break;
					}
				}
			}
			return object;
		}).collect(Collectors.toList());
		map.put("result", result.parallelStream().filter(Objects::nonNull).collect(Collectors.toList()));
		return map;
	}
	
	private void setObjectValue(String fieldName, Map<String, Object> object, Packages packages) {
		try {
			Class<?> classPackage = packages.getClass();
			Field field = classPackage.getDeclaredField(fieldName);
			field.setAccessible(true);
			object.put(fieldName, field.get(packages));
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List<FieldConfigVM> getPackageFieldConfigForDashboard(Long id) {
		List<FieldConfigVM> fieldConfigVMs = getPackageFieldConfig(id);
		// fieldConfigVMs.remove(fieldConfigVMs.size() -1);
		fieldConfigVMs.remove(0);
		// Create PO field.
		// Create Quantity field.
		FieldConfigVM poFieldConfigVM = new FieldConfigVM();
		poFieldConfigVM.setHeaderName("PO");
		poFieldConfigVM.setField("po");
		poFieldConfigVM.setColId("po");
		poFieldConfigVM.setSort("asc");
		poFieldConfigVM.setSortable(true);
		poFieldConfigVM.setFilter("agNumberColumnFilter");
		poFieldConfigVM.setWidth(250);
		fieldConfigVMs.add(0, poFieldConfigVM);
		// Create Quantity field.
		FieldConfigVM quantityFieldConfigVM = new FieldConfigVM();
		quantityFieldConfigVM.setHeaderName("Quantity");
		quantityFieldConfigVM.setField("quantity");
		quantityFieldConfigVM.setColId("quantity");
		quantityFieldConfigVM.setSort("asc");
		quantityFieldConfigVM.setSortable(true);
		quantityFieldConfigVM.setFilter("agNumberColumnFilter");
		quantityFieldConfigVM.setWidth(120);
		fieldConfigVMs.add(2, quantityFieldConfigVM);
		// Create Actual Done field.
		createFieldConfig(fieldConfigVMs, "Actual Done (%)", "actual", "agTextColumnFilter", 120, false);
		// Create Late field.
		createFieldConfig(fieldConfigVMs, "Late (%)", "late", "agTextColumnFilter", 120, false);
		return fieldConfigVMs;
	}
	
	public List<FieldConfigVM> getPackageFieldConfig(Long id) {
		// List<FieldConfigVM> fieldConfigVMs = this.fieldConfigService.getAllFieldConfig(id, AppConstants.PACKAGE_ENTITY);
		TMSDynamicCustomFieldVM tmsDynamicCustomFieldVM = this.fieldConfigService.getAllFieldConfig(id, AppConstants.PACKAGE_ENTITY);
		List<FieldConfigVM> fieldConfigVMs = tmsDynamicCustomFieldVM.getFieldConfigVMs();
		return fieldConfigVMs;
	}
	
	private boolean hasRound(Long id, String round, List<FieldConfigVM> fieldConfigVMs) {
		for (FieldConfigVM fieldConfigVM : fieldConfigVMs) {
			if(fieldConfigVM.getField().equals(round)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @param fieldConfigVMs
	 * @param taskFieldConfigVM
	 */
	private void createFieldConfig(List<FieldConfigVM> fieldConfigVMs, String headerName, String field, String filter, Integer width, boolean hide) {
		// Create PO field.
		FieldConfigVM fieldConfigVM = new FieldConfigVM();
		fieldConfigVM.setHeaderName(headerName);
		fieldConfigVM.setField(field);
		fieldConfigVM.setColId(field);
		fieldConfigVM.setHide(hide);
		fieldConfigVM.setSort("asc");
		fieldConfigVM.setSortable(true);
		fieldConfigVM.setFilter(filter);
		fieldConfigVM.setWidth(width);
		fieldConfigVMs.add(fieldConfigVM);
	}

	public List<TasksDTO> getTasksByFilter(Long projectId, String filter) {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentDateFormat = sf.format(new Date());
		return tasksMapper.toDto(this.tasksRepository.findTasksByFilter(projectId, filter, currentDateFormat));
	}

}
