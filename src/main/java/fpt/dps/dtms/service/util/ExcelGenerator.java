package fpt.dps.dtms.service.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.dhatim.fastexcel.reader.ExcelReaderException;
import org.dhatim.fastexcel.reader.ReadableWorkbook;
import org.joda.time.DateTime;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.core.io.InputStreamResource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.mysql.fabric.xmlrpc.base.Array;

import fpt.dps.dtms.domain.BusinessUnit;
import fpt.dps.dtms.domain.Packages;
import fpt.dps.dtms.domain.ProjectUsers;
import fpt.dps.dtms.domain.PurchaseOrders;
import fpt.dps.dtms.domain.TMSCustomFieldScreenValue;
import fpt.dps.dtms.domain.TaskTrackingTime;
import fpt.dps.dtms.domain.Tasks;
import fpt.dps.dtms.domain.User;
import fpt.dps.dtms.domain.UserProfile;
import fpt.dps.dtms.domain.enumeration.ContractType;
import fpt.dps.dtms.domain.enumeration.ErrorSeverity;
import fpt.dps.dtms.domain.enumeration.FIStatus;
import fpt.dps.dtms.domain.enumeration.FixStatus;
import fpt.dps.dtms.domain.enumeration.OPStatus;
import fpt.dps.dtms.domain.enumeration.ProjectRoles;
import fpt.dps.dtms.domain.enumeration.ReviewStatus;
import fpt.dps.dtms.domain.enumeration.TaskAvailability;
import fpt.dps.dtms.domain.enumeration.TaskPriority;
import fpt.dps.dtms.domain.enumeration.TaskSeverity;
import fpt.dps.dtms.domain.enumeration.TaskStatus;
import fpt.dps.dtms.repository.BusinessUnitRepository;
import fpt.dps.dtms.repository.PackagesRepository;
import fpt.dps.dtms.repository.ProjectUsersRepository;
import fpt.dps.dtms.repository.TMSCustomFieldScreenValueRepository;
import fpt.dps.dtms.repository.TasksRepository;
import fpt.dps.dtms.repository.UserRepository;
import fpt.dps.dtms.repository.search.PackagesSearchRepository;
import fpt.dps.dtms.repository.search.TMSCustomFieldScreenValueSearchRepository;
import fpt.dps.dtms.repository.search.TasksSearchRepository;
import fpt.dps.dtms.security.SecurityUtils;
import fpt.dps.dtms.service.CommunicationService;
import fpt.dps.dtms.service.CustomReportsQueryService;
import fpt.dps.dtms.service.LoginTrackingQueryService;
import fpt.dps.dtms.service.ProjectUsersQueryService;
import fpt.dps.dtms.service.TMSCustomFieldScreenQueryService;
import fpt.dps.dtms.service.TMSCustomFieldScreenValueQueryService;
import fpt.dps.dtms.service.TMSCustomFieldScreenValueService;
import fpt.dps.dtms.service.TMSCustomFieldService;
import fpt.dps.dtms.service.TaskTrackingTimeQueryService;
import fpt.dps.dtms.service.TaskTrackingTimeService;
import fpt.dps.dtms.service.UserProfileService;
import fpt.dps.dtms.service.dto.CustomReportsDTO;
import fpt.dps.dtms.service.dto.LoginTrackingDTO;
import fpt.dps.dtms.service.dto.PackagesDTO;
import fpt.dps.dtms.service.dto.ProjectUsersDTO;
import fpt.dps.dtms.service.dto.ProjectWorkflowsDTO;
import fpt.dps.dtms.service.dto.TMSCustomFieldDTO;
import fpt.dps.dtms.service.dto.TMSCustomFieldScreenDTO;
import fpt.dps.dtms.service.dto.TMSCustomFieldScreenValueDTO;
import fpt.dps.dtms.service.dto.TasksDTO;
import fpt.dps.dtms.service.dto.UserDTO;
import fpt.dps.dtms.service.mapper.TMSCustomFieldScreenValueMapper;
import fpt.dps.dtms.service.mapper.TasksMapper;
import fpt.dps.dtms.web.rest.errors.InternalServerErrorException;
import fpt.dps.dtms.web.rest.vm.FieldConfigVM;
import fpt.dps.dtms.security.SecurityUtils;

/**
 * @author TuHP
 *
 */
@Service
@Transactional
public class ExcelGenerator {
	private Packages packages = null;

	private HashMap<String, Packages> mapPackages;

	private HashMap<String, List<Tasks>> mapTasks;

	private final ProjectUsersRepository projectUsersRepository;

	private final PackagesRepository packagesRepository;

	private final PackagesSearchRepository packagesSearchRepository;

	private final LoginTrackingQueryService loginTrackingQueryService;

	private final ProjectUsersQueryService projectUsersQueryService;

	private final TasksRepository tasksRepository;

	private final TasksSearchRepository tasksSearchRepository;

	private final UserRepository userRepository;

	private final UserProfileService userProfileService;

	private final TaskTrackingTimeQueryService taskTrackingTimeQueryService;

	private final TasksMapper tasksMapper;

	private final BusinessUnitRepository businessUnitRepository;

	private final TaskTrackingTimeService taskTrackingTimeService;

	private final TMSCustomFieldScreenQueryService tMSCustomFieldScreenQueryService;

	private final FieldConfigService fieldConfigService;
	
	private final TMSCustomFieldService tMSCustomFieldService;
	
	private final TMSCustomFieldScreenValueService tMSCustomFieldScreenValueService;
	
	private final TMSCustomFieldScreenValueQueryService tMSCustomFieldScreenValueQueryService;
	
	private final TMSCustomFieldScreenValueMapper tMSCustomFieldScreenValueMapper;
	
	private final TMSCustomFieldScreenValueRepository tMSCustomFieldScreenValueRepository;
	
	private final TMSCustomFieldScreenValueSearchRepository tMSCustomFieldScreenValueSearchRepository;
	
	private final CustomReportsQueryService customReportsQueryService;

	public ExcelGenerator(ProjectUsersRepository projectUsersRepository, PackagesRepository packagesRepository,
			PackagesSearchRepository packagesSearchRepository, TasksRepository tasksRepository, TasksMapper tasksMapper,
			TasksSearchRepository tasksSearchRepository, LoginTrackingQueryService loginTrackingQueryService,
			BusinessUnitRepository businessUnitRepository, UserRepository userRepository,
			UserProfileService userProfileService, ProjectUsersQueryService projectUsersQueryService,
			TaskTrackingTimeQueryService taskTrackingTimeQueryService,
			TaskTrackingTimeService taskTrackingTimeService, TMSCustomFieldScreenQueryService tMSCustomFieldScreenQueryService, FieldConfigService fieldConfigService,
			TMSCustomFieldService tMSCustomFieldService, TMSCustomFieldScreenValueService tMSCustomFieldScreenValueService, TMSCustomFieldScreenValueQueryService tMSCustomFieldScreenValueQueryService,
			TMSCustomFieldScreenValueMapper tMSCustomFieldScreenValueMapper, TMSCustomFieldScreenValueRepository tMSCustomFieldScreenValueRepository,
			TMSCustomFieldScreenValueSearchRepository tMSCustomFieldScreenValueSearchRepository, CustomReportsQueryService customReportsQueryService) {
		this.projectUsersRepository = projectUsersRepository;
		this.packagesRepository = packagesRepository;
		this.packagesSearchRepository = packagesSearchRepository;
		this.tasksRepository = tasksRepository;
		this.tasksSearchRepository = tasksSearchRepository;
		this.businessUnitRepository = businessUnitRepository;
		this.loginTrackingQueryService = loginTrackingQueryService;
		this.tasksMapper = tasksMapper;
		this.userRepository = userRepository;
		this.userProfileService = userProfileService;
		this.projectUsersQueryService = projectUsersQueryService;
		this.taskTrackingTimeQueryService = taskTrackingTimeQueryService;
		this.taskTrackingTimeService = taskTrackingTimeService;
		this.tMSCustomFieldScreenQueryService = tMSCustomFieldScreenQueryService;
		this.fieldConfigService = fieldConfigService;
		this.tMSCustomFieldService = tMSCustomFieldService;
		this.tMSCustomFieldScreenValueService = tMSCustomFieldScreenValueService;
		this.tMSCustomFieldScreenValueQueryService = tMSCustomFieldScreenValueQueryService;
		this.tMSCustomFieldScreenValueMapper = tMSCustomFieldScreenValueMapper;
		this.tMSCustomFieldScreenValueRepository = tMSCustomFieldScreenValueRepository;
		this.tMSCustomFieldScreenValueSearchRepository = tMSCustomFieldScreenValueSearchRepository;
		this.customReportsQueryService = customReportsQueryService;
	}

	/**
	 * Export the columns and datatemplate to excel file
	 *
	 * @param projectWorkflowsDTOs: contains the package and task columns
	 * @return the excel file
	 */
	public ByteArrayInputStream taskManagementToExcel(List<ProjectWorkflowsDTO> projectWorkflowsDTOs, Long projectId)
			throws IOException {
		String[] COLUMNs = getExportColumns(projectWorkflowsDTOs);
		try (XSSFWorkbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream();) {
			XSSFSheet sheet = workbook.createSheet("Template");
			this.settingExcelSheet(sheet);
			DataFormat format = workbook.createDataFormat();
			CreationHelper createHelper = workbook.getCreationHelper();
			Font headerFont = workbook.createFont();
			headerFont.setBold(true);
			headerFont.setColor(IndexedColors.WHITE.getIndex());

			CellStyle headerCellStyle;

			// Row for Header
			Row headerRow = sheet.createRow(0);
			Row rowDataTemp = sheet.createRow(1);
			List<FieldConfigVM> fieldConfigVMs = getAllFieldConfig(projectWorkflowsDTOs);

			// Header
			for (int col = 0; col < COLUMNs.length; col++) {
				String colName = COLUMNs[col];
				headerCellStyle = workbook.createCellStyle();
				headerCellStyle.setFont(headerFont);
				headerCellStyle.setFillForegroundColor(getCellBackGroundColor(colName));
				headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				Cell cell = headerRow.createCell(col);
				cell.setCellValue(colName);
				cell.setCellStyle(headerCellStyle);
				for (FieldConfigVM field : fieldConfigVMs) {
					String fieldName = (field.getPosition() + "_" + field.getField()).toUpperCase();
					if (fieldName.equals(colName.toUpperCase()) && field.getOptions() != null) {
						cell = rowDataTemp.createCell(col);
						List<String> list = (ArrayList<String>) field.getOptions();
						if (list.size() > 0) {
							setExplicitListConstraint(sheet, cell, list.toArray(new String[0]), list.get(0), 1, 1, col,
									col);
						}
					}

					if (projectId != null && fieldName.equals(colName.toUpperCase())
							&& (field.getField().equals(AppConstants.ROUND_OP)
									|| field.getField().equals(AppConstants.ROUND_REVIEW1)
									|| field.getField().equals(AppConstants.ROUND_REVIEW2)
									|| field.getField().equals(AppConstants.ROUND_FIXER)
									|| field.getField().equals(AppConstants.ROUND_FI))) {
						cell = rowDataTemp.createCell(col);
						List<String> userLogins = projectUsersRepository.getAllUserLoginByProjectId(projectId);
						if (userLogins.size() > 0) {
							setExplicitListConstraint(sheet, cell, userLogins.toArray(new String[0]), userLogins.get(0),
									1, 1, col, col);
						}
					}

					if (fieldName.equals(colName.toUpperCase()) && field.getType() != null
							&& field.getType().equals("date")) {
						CellStyle cellStyle = workbook.createCellStyle();
						cell = rowDataTemp.createCell(col);
						cell.setCellValue(Calendar.getInstance());
						cell.setCellStyle(cellStyle);
						cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("m/dd/yyyy h:mm"));
					}
				}
			}

			workbook.write(out);
			return new ByteArrayInputStream(out.toByteArray());
		}
	}

	/**
	 * Export tasks to excel file in case user want to update tasks
	 *
	 * @param projectWorkflowsDTOs: contains the package and task columns
	 * @return the excel file
	 */
	public ByteArrayInputStream exportTaskToExcel(List<ProjectWorkflowsDTO> projectWorkflowsDTOs,
			List<TasksDTO> taskDTOs, Long projectId) throws IOException {
		String[] COLUMNs = getTaskExportColumns(projectWorkflowsDTOs);
		try (XSSFWorkbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream();) {
			XSSFSheet sheet = workbook.createSheet("Tasks");
			this.settingExcelSheet(sheet);
			DataFormat format = workbook.createDataFormat();
			CreationHelper createHelper = workbook.getCreationHelper();
			Font headerFont = workbook.createFont();
			headerFont.setBold(true);
			headerFont.setColor(IndexedColors.WHITE.getIndex());

			CellStyle headerCellStyle;

			// Row for Header
			Row headerRow = sheet.createRow(0);
			// Row rowDataTemp = sheet.createRow(1);
			List<FieldConfigVM> fieldConfigVMs = getAllTaskFieldConfig(projectWorkflowsDTOs);

			// Header
			for (int col = 0; col < COLUMNs.length; col++) {
				String colName = COLUMNs[col];
				headerCellStyle = workbook.createCellStyle();
				headerCellStyle.setFont(headerFont);
				headerCellStyle.setFillForegroundColor(getCellBackGroundColor(colName));
				headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				Cell cell = headerRow.createCell(col);
				cell.setCellValue(colName);
				cell.setCellStyle(headerCellStyle);
			}

			// Loop all tasks and fill to corresponding cell
			List<String> projectUsers = projectUsersRepository.getAllUserLoginByProjectId(projectId);
			int rowIndex = 0;
			for (TasksDTO taskDTO : taskDTOs) {
				rowIndex += 1;
				CellStyle cellStyle = workbook.createCellStyle();
				List<Map<String, Object>> result = fieldConfigService.MapDataWithNameOfField(taskDTO);
				this.generateTasks(rowIndex, COLUMNs, sheet, taskDTO,  result, projectUsers, fieldConfigVMs, createHelper,
						cellStyle);
			}

			workbook.write(out);
			return new ByteArrayInputStream(out.toByteArray());
		}
	}

	/**
	 * Export member to excel file
	 *
	 * @param projectWorkflowsDTOs: contains the package and task columns
	 * @return the excel file
	 */
	public ByteArrayInputStream exportDataToExcel(List<Map<String, Object>> listUsers) throws IOException {
		String[] COLUMNs = columnsMemberManagement();
		try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream();) {
			Sheet sheet = workbook.createSheet("Member_Management");
			sheet.setDefaultColumnWidth(10);
			DataFormat format = workbook.createDataFormat();
			CreationHelper createHelper = workbook.getCreationHelper();
			Font headerFont = workbook.createFont();
			headerFont.setBold(true);
			headerFont.setColor(IndexedColors.WHITE.getIndex());

			CellStyle headerCellStyle;

			// Row for Header
			Row headerRow = sheet.createRow(0);

			// Header
			for (int i = 0; i < COLUMNs.length; i++) {
				String colName = COLUMNs[i];
				headerCellStyle = workbook.createCellStyle();
				headerCellStyle.setFont(headerFont);
				headerCellStyle.setFillForegroundColor(getCellBackGroundColor(colName));
				headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(colName);
				cell.setCellStyle(headerCellStyle);
			}
			int rowIndex = 0;
			for (Map<String, Object> user : listUsers) {
				rowIndex += 1;
				CellStyle cellStyle = workbook.createCellStyle();
				this.generateMember(rowIndex, COLUMNs, sheet, user, createHelper, cellStyle);
			}
			workbook.write(out);
			return new ByteArrayInputStream(out.toByteArray());
		}
	}

	/**
	 * Export all tasks to excel file
	 * @param tasks
	 * @return
	 * @throws IOException
	 * @author TuHP
	 */
	public ByteArrayInputStream exportTrackingManagementTasks(List<Tasks> tasks, String userLogin) throws IOException {
		String[] COLUMNs = columnsTrackingManagementTask(tasks, userLogin);
		try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream();) {
			Sheet sheet = workbook.createSheet("Report");
			sheet.setDefaultColumnWidth(25);
			CreationHelper createHelper = workbook.getCreationHelper();
			Font headerFont = workbook.createFont();
			headerFont.setBold(true);
			headerFont.setColor(IndexedColors.WHITE.getIndex());

			CellStyle headerCellStyle;

			// Row for Header
			Row headerRow = sheet.createRow(0);

			// Header
			for (int i = 0; i < COLUMNs.length; i++) {
				String colName = COLUMNs[i];
				headerCellStyle = workbook.createCellStyle();
				headerCellStyle.setFont(headerFont);
				headerCellStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
				headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(colName);
				cell.setCellStyle(headerCellStyle);
			}
			int rowIndex = 0;
			for (Tasks task : tasks) {
				rowIndex += 1;
				CellStyle cellStyle = workbook.createCellStyle();
				this.generateTasks(rowIndex, COLUMNs, sheet, task, createHelper, cellStyle);
			}
			workbook.write(out);
			return new ByteArrayInputStream(out.toByteArray());
		}
	}

	/**
	 * Generate Excel Row value base on the list columns
	 * @param rowIndex
	 * @param columns
	 * @param sheet
	 * @param task
	 * @param createHelper
	 * @param cellStyle
	 * @return
	 * @author TuHP
	 */
	private Row generateTasks(int rowIndex, String[] columns, Sheet sheet, Tasks task, CreationHelper createHelper,
			CellStyle cellStyle) {
		Row row = sheet.createRow(rowIndex);
		for (int i = 0; i < columns.length; i++) {
			try {
				String colName = columns[i];
				int cellIndex = i;

				Cell cell = row.createCell(cellIndex);
				switch (colName) {
				case "No":
					cell = this.setValueToMemberExcelCell(Integer.class.getTypeName(), sheet, cell, createHelper,
							cellStyle, rowIndex, colName, i, rowIndex);
					break;
				case "Date":
					LocalDate localDate = LocalDateTime
							.ofInstant(task.getEstimateEndTime(), AppConstants.SYSTEM_ZONE_ID).toLocalDate();
					cell = this.setValueToMemberExcelCell(LocalDate.class.getTypeName(), sheet, cell, createHelper,
							cellStyle, localDate, colName, i, rowIndex);
					break;
				case "Purchase Order Name":
					cell = this.setValueToMemberExcelCell(String.class.getTypeName(), sheet, cell, createHelper,
							cellStyle, task.getPackages().getPurchaseOrders().getName(), colName, i, rowIndex);
					break;
				case "Package Name":
					cell = this.setValueToMemberExcelCell(String.class.getTypeName(), sheet, cell, createHelper,
							cellStyle, task.getPackages().getName(), colName, i, rowIndex);
					break;
				case "Task Name":
					cell = this.setValueToMemberExcelCell(String.class.getTypeName(), sheet, cell, createHelper,
							cellStyle, task.getName(), colName, i, rowIndex);
					break;
				case "Total Bugs Open":
					cell = this.setValueToMemberExcelCell(Integer.class.getTypeName(), sheet, cell, createHelper,
							cellStyle, tasksRepository.countBugOpenByTaskId(task.getId()), colName, i, rowIndex);
					break;
					
				case "Total Bugs":
					cell = this.setValueToMemberExcelCell(Integer.class.getTypeName(), sheet, cell, createHelper,
							cellStyle, tasksRepository.countBugByTaskId(task.getId()), colName, i, rowIndex);
					break;
				case "Frame":
					cell = this.setValueToMemberExcelCell(Integer.class.getTypeName(), sheet, cell, createHelper,
							cellStyle, task.getFrame(), colName, i, rowIndex);
					break;
				case "Status":
					cell = this.setValueToMemberExcelCell(String.class.getTypeName(), sheet, cell, createHelper,
							cellStyle, task.getStatus().toString(), colName, i, rowIndex);
					break;
				default:
					List<TaskTrackingTime> taskTrackings = this.taskTrackingTimeService.findAllByTaskIdAndRoleAndEndStatus(task.getId(), getTaskTrackingTimesByRoundName(colName), "DONE");
					int number = 0;
					for (int j = 1; j <= 100; j++) {
						if (colName.contains(String.valueOf(j))) {
							number = j - 1;
							break;
						}
					}
					cell = generateRoundFixer(colName, taskTrackings.get(number), cell, i, rowIndex, sheet, createHelper, cellStyle);
					break;
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IndexOutOfBoundsException e) {
				// e.printStackTrace();
			} catch (NullPointerException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return row;
	}
	
	/**
	 * Get round
	 * @param colName
	 * @return
	 */
	private String getTaskTrackingTimesByRoundName(String colName) {
		if (colName.contains("OP_")) {
			return AppConstants.ROUND_OP;
		} else if (colName.contains("Review1_")) {
			return AppConstants.ROUND_REVIEW1;
		} else if (colName.contains("Review2_")) {
			return AppConstants.ROUND_REVIEW2;
		} else if (colName.contains("Fixer_")) {
			return AppConstants.ROUND_FIXER;
		} else if (colName.contains("Fi_")) {
			return AppConstants.ROUND_FI;
		}
		return null;
	}
	/**
	 * Generate data by round
	 * @param colName
	 * @param taskTrackingTime
	 * @param cell
	 * @param i
	 * @param rowIndex
	 * @param sheet
	 * @param createHelper
	 * @param cellStyle
	 * @return
	 */
	private Cell generateRoundFixer(String colName, TaskTrackingTime taskTrackingTime, Cell cell, int i, int rowIndex, Sheet sheet,
			CreationHelper createHelper, CellStyle cellStyle) {
		try {
			if (taskTrackingTime != null) {
				String colNameDefault = null;
				if (colName.contains("EndTime")) {
					colNameDefault = "EndTime";
				} else if (colName.contains("StartTime")) {
					colNameDefault = "StartTime";
				} else if (colName.contains("Effort")) {
					colNameDefault = "Effort";
				} else {
					colNameDefault = "Fixer";
				}
				switch (colNameDefault) {
				case "Fixer":
					return this.setValueToMemberExcelCell(String.class.getTypeName(), sheet, cell, createHelper,
						cellStyle, taskTrackingTime.getUserLogin(), colName, i, rowIndex);
				case "Effort":
					return this.setValueToMemberExcelCell(Integer.class.getTypeName(), sheet, cell, createHelper,
						cellStyle, this.getFixerEffort(taskTrackingTime), colName, i, rowIndex);
				case "StartTime":
					return this.setValueToMemberExcelCell(Instant.class.getTypeName(), sheet, cell, createHelper,
							cellStyle, taskTrackingTime.getStartTime(), colName, i, rowIndex);
				case "EndTime":
					return this.setValueToMemberExcelCell(Instant.class.getTypeName(), sheet, cell, createHelper,
							cellStyle, taskTrackingTime.getEndTime(), colName, i, rowIndex);
				default:
				}
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IndexOutOfBoundsException e) {
			// e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Get member effort by role
	 * @param taskTrackingTimes
	 * @param userLogin
	 * @return
	 */
	private int getFixerEffort(TaskTrackingTime TaskTracking) {
		return TaskTracking.getDuration() != null ? TaskTracking.getDuration() : 0;
	}

	// list columns for export
	private String[] columnsMemberManagement() {
		List<String> COLUMNs = new ArrayList<>();
		/* COLUMNs.add("id"); */
		COLUMNs.add("firstName");
		COLUMNs.add("lastName");
		COLUMNs.add("email");
		COLUMNs.add("status");
		COLUMNs.add("startTime");
		String[] arrColumns = new String[COLUMNs.size()];
		COLUMNs.toArray(arrColumns);
		return arrColumns;
	}

	// list columns for export TrackingManagementTasks
	private String[] columnsTrackingManagementTask(List<Tasks> listTasks, String userLogin) {
		List<String> COLUMNs = new ArrayList<>();
		COLUMNs.add("No");
		COLUMNs.add("Date");
		COLUMNs.add("Purchase Order Name");
		COLUMNs.add("Package Name");
		COLUMNs.add("Task Name");
		COLUMNs.add("Total Bugs Open");
		COLUMNs.add("Total Bugs");
		COLUMNs.add("Frame");
		COLUMNs.add("Status");
		List<String> listFieldReportOP = columnsNumberByRound(listTasks, AppConstants.ROUND_OP, "OP");
		COLUMNs.addAll(listFieldReportOP);
		List<String> listFieldReportRV1 = columnsNumberByRound(listTasks, AppConstants.ROUND_REVIEW1, "Review1");
		COLUMNs.addAll(listFieldReportRV1);
		List<String> listFieldReport = columnsDynamicCustomReport(userLogin);
		for(String name: listFieldReport) {
			if (name != null) {
				List<String> listField = new ArrayList<>();
				if (name.equals("Review2")) {
					listField = columnsNumberByRound(listTasks, AppConstants.ROUND_REVIEW2, name);
				} else if (name.equals("Fi")) {
					listField = columnsNumberByRound(listTasks, AppConstants.ROUND_FI, name);
				} else if (name.equals("Fixer")) {
					listField = columnsNumberByRound(listTasks, AppConstants.ROUND_FIXER, name);
				}
				COLUMNs.addAll(listField);
			}
		}
		String[] arrColumns = new String[COLUMNs.size()];
		COLUMNs.toArray(arrColumns);
		return arrColumns;
	}
	
	/**
	 * Get all row record by round
	 * @param listTasks
	 * @param round
	 * @return
	 */
	private List<String> columnsNumberByRound(List<Tasks> listTasks, String round, String columnName) {
		int roundNum = 0;
		for (Tasks task: listTasks) {
			int count = taskTrackingTimeService.countAllByTaskIdAndRoleAndEndStatus(task.getId(), round, "DONE");
			if (count > roundNum) {
				roundNum = count;
			}
		}
		List<String> columns = new ArrayList<>();
		for (int i = 1; i <= roundNum; i++) {
			columns.add(columnName + "_" + i);
			columns.add(columnName + "_" + i + " Effort");
			columns.add(columnName + "_" + i + " StartTime");
			columns.add(columnName + "_" + i + " EndTime");
		}
		return columns;
	}
	
	private List<String> columnsDynamicCustomReport(String userLogin) {
		List<String> columns = new ArrayList<>();
		CustomReportsDTO customReportDTO = customReportsQueryService.findCustomReportsByPageName("TrackingTasks", userLogin);
		if (customReportDTO != null) {
			ObjectMapper mapper = new ObjectMapper();
			try {
				ArrayNode nodes = (ArrayNode) mapper.readTree(customReportDTO.getValue());
				for (int i = 0; i < nodes.size(); i++) {
					columns.add(nodes.get(i).textValue());
				}
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return columns;
	}

	/**
	 * Export allocation to excel file
	 *
	 * @param projectWorkflowsDTOs: contains the package and task columns
	 * @return the excel file
	 */
	public ByteArrayInputStream exportAllocationToExcel(List<ProjectUsers> listUsers, Long projectId)
			throws IOException {
		String[] COLUMNs = columnsAllocation();
		try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream();) {
			Sheet sheet = workbook.createSheet("Allocation");
			sheet.setDefaultColumnWidth(10);
			DataFormat format = workbook.createDataFormat();
			CreationHelper createHelper = workbook.getCreationHelper();
			Font headerFont = workbook.createFont();
			headerFont.setBold(true);
			headerFont.setColor(IndexedColors.WHITE.getIndex());

			CellStyle headerCellStyle;

			// Row for Header
			Row headerRow = sheet.createRow(0);

			// Header
			for (int i = 0; i < COLUMNs.length; i++) {
				String colName = COLUMNs[i];
				headerCellStyle = workbook.createCellStyle();
				headerCellStyle.setFont(headerFont);
				headerCellStyle.setFillForegroundColor(getCellBackGroundColor(colName));
				headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(colName);
				cell.setCellStyle(headerCellStyle);
			}
			int rowIndex = 0;
			for (ProjectUsers user : listUsers) {
				rowIndex += 1;
				CellStyle cellStyle = workbook.createCellStyle();
				this.generateAllocation(rowIndex, COLUMNs, sheet, user, projectId, createHelper, cellStyle);
			}
			workbook.write(out);
			return new ByteArrayInputStream(out.toByteArray());
		}
	}

	// list columns for export
	private String[] columnsAllocation() {
		List<String> COLUMNs = new ArrayList<>();
		COLUMNs.add("userLogin");
		COLUMNs.add("roleName");
		COLUMNs.add("effortPlan");
		COLUMNs.add("startDate");
		COLUMNs.add("endDate");
		COLUMNs.add("contractType");
		COLUMNs.add("totalEffortPlan");
		COLUMNs.add("workingEffort");
		COLUMNs.add("workingLocation");
		String[] arrColumns = new String[COLUMNs.size()];
		COLUMNs.toArray(arrColumns);
		return arrColumns;
	}

	/**
	 * Create template data which includes the list of value in an excel cell
	 */
	private static void setExplicitListConstraint(Sheet sheet, Cell cell, String[] list, String defaultValue,
			int firstRow, int lastRow, int firstCol, int lastCol) {
		DataValidationHelper helper = sheet.getDataValidationHelper();
		CellRangeAddressList addressList = new CellRangeAddressList(firstRow, lastRow, firstCol, lastCol);
		DataValidationConstraint constraint = helper.createExplicitListConstraint(list);
		DataValidation validation = helper.createValidation(constraint, addressList);
		validation.setErrorStyle(DataValidation.ErrorStyle.STOP);
		validation.createErrorBox("Error !!!", "Input wrong value.");
		validation.setEmptyCellAllowed(true);
		sheet.addValidationData(validation);
		cell.setCellValue(defaultValue);
	}

	/**
	 * Import packages and tasks from excel into database
	 *
	 * @param projectWorkflowsDTOs
	 * @param file
	 * @param purchaseOrders
	 */
	public void importTasks(List<ProjectWorkflowsDTO> projectWorkflowsDTOs, MultipartFile file, Packages packages) throws IOException {
		InputStream fileInputStream = file.getInputStream();
		try (ReadableWorkbook wb = new ReadableWorkbook(fileInputStream)) {
			org.dhatim.fastexcel.reader.Sheet sheet = wb.getFirstSheet();
			Optional<String> currentUserLogin = SecurityUtils.getCurrentUserLogin();
			CommunicationService.progressImportTaskToUser(currentUserLogin.get(),
					String.format("{\"totalRowsImport\": \"%s\"}", sheet.read().size() - 1));
			try (Stream<org.dhatim.fastexcel.reader.Row> rows = sheet.openStream()) {
				List<String[]> lstColumns = getImportColumns(projectWorkflowsDTOs);
				rows.forEach(row -> {
					int index = row.getRowNum();
					if (index == 1 || row.getCell(0) == null || row.getCell(0).getValue() == null) {
						return;
					}
					System.out.println("row index: " + index);
					//Packages _packages, int startIndex, String[] lstColumns, String[] lstColumnsCustomField, org.dhatim.fastexcel.reader.Row row,
					// String currentUserLogin, List<ProjectWorkflowsDTO> projectWorkflowsDTOs
					Tasks tasks = saveTasks(packages, 1, lstColumns.get(0), lstColumns.get(1), row, currentUserLogin.get(), projectWorkflowsDTOs, true);
					System.out.println("########" + tasks.toString());
				});
			}
			StringBuffer contentLog = new StringBuffer(String.format(
					"<div style=\\\"margin-top:15px\\\"><b>Finish import Task</b></div>"));
			CommunicationService.progressImportTaskToUser(currentUserLogin.get(),
					String.format("{\"finishImport\": \"%s\"}", contentLog.toString()));
		}
	}

	/**
	 * Import packages and tasks from excel into database
	 *
	 * @param projectWorkflowsDTOs
	 * @param file
	 * @param purchaseOrders
	 *
	 * @author TuHP
	 */
	public void importPackageAndTaskToDatabase(List<ProjectWorkflowsDTO> projectWorkflowsDTOs, MultipartFile file,
			PurchaseOrders purchaseOrders) throws IOException {
		InputStream fileInputStream = file.getInputStream();
		try (ReadableWorkbook wb = new ReadableWorkbook(fileInputStream)) {
			org.dhatim.fastexcel.reader.Sheet sheet = wb.getFirstSheet();
			Optional<String> currentUserLogin = SecurityUtils.getCurrentUserLogin();
			CommunicationService.progressImportTaskToUser(currentUserLogin.get(),
					String.format("{\"totalRowsImport\": \"%s\"}", sheet.read().size() - 1));
			try (Stream<org.dhatim.fastexcel.reader.Row> rows = sheet.openStream()) {
				List<String[]> lstColumns = getImportColumns(projectWorkflowsDTOs);
				mapPackages = new HashMap<>();
				rows.forEach(row -> {
					int index = row.getRowNum();
					if (index == 1 || row.getCell(0) == null || row.getCell(0).getValue() == null) {
						return;
					}
					System.out.println("row index: " + index);
					String packageName = row.getCell(0).getValue().toString();
					packages = mapPackages.get(packageName);
					if (packages == null) {
						packages = packagesRepository.findPackageByNameAndPurchaseOrderId(packageName,
								purchaseOrders.getId());

						if (packages == null) {
							Packages packageNew = new Packages();
							packages = savePackages(lstColumns, row, purchaseOrders, currentUserLogin.get(), projectWorkflowsDTOs, packageNew);
							CommunicationService.progressImportTaskToUser(currentUserLogin.get(),
									String.format("{\"progressPackages\": \"%s\"}",
											"<div><b>Package " + packageName + " was created successfully</b></div>"));
						} else {
							packages = savePackages(lstColumns, row, purchaseOrders, currentUserLogin.get(), projectWorkflowsDTOs, packages);
							CommunicationService.progressImportTaskToUser(currentUserLogin.get(),
									String.format("{\"progressPackages\": \"%s\"}",
											"<div><b>Package " + packageName + " was updated successfully</b></div>"));
						}
						mapPackages.put(packageName, packages);
						System.out.println("########" + packages.toString());
					}
					if (packages != null) {
						CommunicationService.progressImportTaskToUser(currentUserLogin.get(),
								String.format("{\"progressPackages\": \"%s\"}",
										"<div><b>Package " + packageName + " is existed</b></div>"));
						int startIndex = lstColumns.get(0).length + lstColumns.get(1).length;
						Tasks tasks = saveTasks(packages, startIndex, lstColumns.get(2), lstColumns.get(3), row,
								currentUserLogin.get(), projectWorkflowsDTOs, false);
						System.out.println("########" + tasks.toString());
					}

				});
			}
			wb.close();
			StringBuffer contentLog = new StringBuffer(String.format(
					"<div style=\\\"margin-top:15px\\\"><b>Finish import Package and Task</b></div>"));
			CommunicationService.progressImportTaskToUser(currentUserLogin.get(),
					String.format("{\"finishImport\": \"%s\"}", contentLog.toString()));
		}
	}

	/**
	 * Import Packages and Tasks from excel to database. This method only use for
	 * TOYOTA project to report data after operator finish their jobs
	 *
	 * @param projectWorkflowsDTOs
	 * @param file
	 * @param purchaseOrders
	 * @throws IOException
	 * @author TuHP
	 */
	public void importPackageAndTaskToDatabaseForToyota(List<ProjectWorkflowsDTO> projectWorkflowsDTOs,
			MultipartFile file, PurchaseOrders purchaseOrders) throws IOException {
		InputStream fileInputStream = file.getInputStream();
		try (ReadableWorkbook wb = new ReadableWorkbook(fileInputStream)) {
			org.dhatim.fastexcel.reader.Sheet sheet = wb.getFirstSheet();
			Optional<String> currentUserLogin = SecurityUtils.getCurrentUserLogin();
			CommunicationService.progressImportTaskToUser(currentUserLogin.get(),
					String.format("{\"totalRowsImport\": \"%s\"}", sheet.read().size() - 1));
			try (Stream<org.dhatim.fastexcel.reader.Row> rows = sheet.openStream()) {
				CommunicationService.progressImportTaskToUser(currentUserLogin.get(),
						String.format("{\"progressPackages\": \"%s\"}",
								"<div><b>======Progress for Purchase Order: " + purchaseOrders.getName() + "======</b></div>"));
				//Contain all packages includes news and existed
				mapPackages = new HashMap<>();
				//Contain all tasks was existed in the packages
				mapTasks = new HashMap<>();
				//Contain the excel cell address corresponding to the columns we get data.
				Map<String, Integer> cellAddress = new HashMap<>();
				//Contain all the OP rounds
				List<String> annotationIndexs = new ArrayList<String>();
				rows.forEach(row -> {
					int index = row.getRowNum();
					// Process excel header before processing import task.
					if (index == 1) {
						getColumnIndex(cellAddress, annotationIndexs, row);
						return;
					}
					System.out.println("row index: " + index);
					String packageName = row.getCell(cellAddress.get(AppConstants.PACKAGE_COLUMN_NAME)).getValue()
							.toString();
					packages = mapPackages.get(packageName);
					if (packages == null) {
						//Finding a package by purchase order and package name
						packages = packagesRepository.findPackageByNameAndPurchaseOrderId(packageName,
								purchaseOrders.getId());
						//If does not exist package then create new
						if (packages == null) {
							packages = new Packages();
							packages.setName(packageName);
							packages.setStartTime(Instant.now());
							packages.setEndTime(Instant.now());
							packages.setPurchaseOrders(purchaseOrders);
							packages = this.packagesRepository.save(packages);
							packagesSearchRepository.save(packages);
							CommunicationService.progressImportTaskToUser(currentUserLogin.get(),
									String.format("{\"progressPackages\": \"%s\"}",
											"<div><b>------Package " + packageName + " is created successfully------</b></div>"));
						} else {
							CommunicationService.progressImportTaskToUser(currentUserLogin.get(),
									String.format("{\"progressPackages\": \"%s\"}",
											"<div><b>------Package " + packageName + " is existed------</b></div>"));
							//Add all task belong to a package to package
							if (!mapTasks.containsKey(packageName)) {
								List<Tasks> lstTasks = tasksRepository
										.groupTasksByTaskNameAndPackageId(packages.getId());
								mapTasks.put(packageName, lstTasks);
							}
						}
						mapPackages.put(packageName, packages);
						System.out.println("########" + packages.toString());
					}
					if (packages != null) {
						String taskStatus = row.getCell(cellAddress.get(AppConstants.PHASE_COLUMN_NAME)).getValue()
								.toString();
						String taskName = getStringValue(cellAddress, row, AppConstants.TASK_COLUMN_NAME);
						//Only process task which has status is ACCEPTANCE
						if (taskStatus.toUpperCase().equals("ACCEPTANCE")) {
							Instant taskEstEndedTime = getDateTime(cellAddress, row,
									AppConstants.TASK_EST_END_TIME_COLUMN_NAME);
							List<Tasks> lstTasks = mapTasks.get(packageName);
							List<String> opTemps = getStringValueForOP(cellAddress, annotationIndexs, row,
									AppConstants.TASK_OP_COLUMN_NAME);
							Tasks task = null;
							if (lstTasks != null) {
								for (Tasks _task : lstTasks) {
									//Check whether task is existing on db or not. If existed we by pass this task and remove out the list.
									if (_task.getName().equals(taskName) && _task.getType().equals(opTemps.get(1))) {
										task = _task;
										mapTasks.get(packageName).remove(_task);
										CommunicationService.progressImportTaskToUser(currentUserLogin.get(),
												String.format("{\"progressImport\": \"%s\"}",
														"<div><b>Task " + taskName + " is existed</b></div>"));
										break;
									}
								}
							}
							//If task is not existed on db then create it.
							if (task == null) {
								task = new Tasks();
								task.setName(taskName);
								task.setPackages(packages);
								tasksRepository.save(task);

								Integer taskFrame = getIntegerValue(cellAddress, row, AppConstants.TASK_FRAME);
								Instant taskEstStartedTime = getDateTime(cellAddress, row,
										AppConstants.TASK_EST_START_TIME_COLUMN_NAME);
								// List<String> opTemps = getStringValueForOP(cellAddress, annotationIndexs,
								// row, AppConstants.TASK_OP_COLUMN_NAME);
								if (opTemps != null) {
									String op = opTemps.get(0);
									task.setOp(op);
									task.setOpStatus(OPStatus.DONE);
									task.setType(opTemps.get(1));
									Instant taskOpActualStartTime = getDateTime(cellAddress, row,
											AppConstants.TASK_OP_ACTUAL_START_TIME_COLUMN_NAME);
									Instant taskOpActualEndTime = getDateTime(cellAddress, row,
											AppConstants.TASK_OP_ACTUAL_END_TIME_COLUMN_NAME);
									int opDuration = getDuration(cellAddress, row,
											AppConstants.TASK_OP_ACTUAL_DURATION_COLUMN_NAME, opTemps.get(1));
									saveTrackingTime(task.getId(), op, opDuration, taskOpActualStartTime,
											taskOpActualEndTime, AppConstants.ROUND_OP);
								}
								String review1ColumnName = String.format(AppConstants.TASK_REVIEW1_COLUMN_NAME,
										opTemps.get(1));
								String review1 = getStringValue(cellAddress, row, review1ColumnName);
								if (review1 != null) {
									task.setReview1(review1);
									task.setReview1Status(ReviewStatus.DONE);
									int review1Duration = getDuration(cellAddress, row,
											AppConstants.TASK_REVIEW1_DURATION_COLUMN_NAME, opTemps.get(1));
									saveTrackingTime(task.getId(), review1, review1Duration, Instant.now(),
											Instant.now(), AppConstants.ROUND_REVIEW1);
								}
								String review2ColumnName = String.format(AppConstants.TASK_REVIEW2_COLUMN_NAME,
										opTemps.get(1));
								String review2 = getStringValue(cellAddress, row, review2ColumnName);
								if (review2 != null) {
									task.setReview2(review2);
									task.setReview2Status(ReviewStatus.DONE);
									int review2Duration = getDuration(cellAddress, row,
											AppConstants.TASK_REVIEW2_DURATION_COLUMN_NAME, opTemps.get(1));
									saveTrackingTime(task.getId(), review2, review2Duration, Instant.now(),
											Instant.now(), AppConstants.ROUND_REVIEW2);
								}
								String fiColumnName = String.format(AppConstants.TASK_FI_COLUMN_NAME, opTemps.get(1));
								String fi = getStringValue(cellAddress, row, fiColumnName);
								if (fi != null) {
									task.setFi(fi);
									task.setFiStatus(FIStatus.DONE);
									String fiDurationColumnName = String
											.format(AppConstants.TASK_FI_DURATION_COLUMN_NAME, opTemps.get(1));
									int fiDuration = getDurationForFI(cellAddress, row, fiDurationColumnName);
									saveTrackingTime(task.getId(), fi, fiDuration, Instant.now(), Instant.now(),
											AppConstants.ROUND_FI);
								}

								task.setEstimateStartTime(taskEstStartedTime);
								task.setEstimateEndTime(taskEstEndedTime);
								task.setStatus(TaskStatus.DONE);
								task.setFrame(taskFrame);
								tasksRepository.save(task);
								tasksSearchRepository.save(task);
								CommunicationService.progressImportTaskToUser(currentUserLogin.get(),
										String.format("{\"progressImport\": \"%s\"}",
												"<div><b>Task " + taskName + " is created successfully</b></div>"));
								System.out.println("########" + task.toString());
							}
						}else {
							CommunicationService.progressImportTaskToUser(currentUserLogin.get(),
									String.format("{\"progressImport\": \"%s\"}",
											"Task "+ taskName +" has status " + taskStatus));
						}
					}

				});
			}
			wb.close();
			StringBuffer contentLog = new StringBuffer(String.format(
					"<div style=\\\"margin-top:15px\\\"><b>Finish import Package and Task</b></div>"));
			CommunicationService.progressImportTaskToUser(currentUserLogin.get(),
					String.format("{\"finishImport\": \"%s\"}", contentLog.toString()));
		}
	}

	/**
	 * @param task
	 * @param op
	 * @param opDuration
	 * @param taskOpActualStartTime
	 * @param taskOpActualEndTime
	 */
	private void saveTrackingTime(long taskId, String pic, int duration, Instant taskActualStartTime,
			Instant taskActualEndTime, String round) {
		TaskTrackingTime taskTrackingTime = new TaskTrackingTime();
		taskTrackingTime.setRole(round);
		taskTrackingTime.setTaskId(taskId);
		taskTrackingTime.setUserLogin(pic);
		taskTrackingTime.setStartTime(taskActualStartTime);
		taskTrackingTime.setEndTime(taskActualEndTime);
		taskTrackingTime.setStartStatus(AppConstants.DOING);
		taskTrackingTime.setEndStatus(AppConstants.DONE);
		taskTrackingTime.setDuration(duration);
		taskTrackingTimeService.save(taskTrackingTime);
	}

	/**
	 * @param cellAddress
	 * @param row
	 * @return
	 */
	private int getDuration(Map<String, Integer> cellAddress, org.dhatim.fastexcel.reader.Row row, String columnName,
			String annotationIndex) {
		String tempColumnName = String.format(columnName, annotationIndex);
		try {
			org.dhatim.fastexcel.reader.Cell cellWorkTime = row.getCell(cellAddress.get(tempColumnName));
			int duration = 0;
			if (cellWorkTime != null) {
				BigDecimal opDurationbyHour = cellWorkTime.asNumber();
				BigDecimal result = opDurationbyHour.multiply(new BigDecimal("60")).setScale(0, RoundingMode.HALF_UP);
				duration = result.intValue();
			}
			return duration;
		} catch (Exception ex) {
			CommunicationService.progressImportTaskToUser(getUserLogin(),
					String.format("{\"progressImport failed\": \"%s\"}", "<div><b>Row: " + (row.getRowNum() -1)
							+ " at Column: " + columnName + " wrong number format</b></div>"));
			throw ex;
		}
	}

	/**
	 * @param cellAddress
	 * @param row
	 * @return
	 */
	private int getDurationForFI(Map<String, Integer> cellAddress, org.dhatim.fastexcel.reader.Row row,
			String columnName) {
		try {
			int duration = 0;
			String fiDurationColumn = "";
			for (int i = 2; i < cellAddress.size(); i++) {
				fiDurationColumn = String.format("%s%s", columnName, i);
				if (cellAddress.get(fiDurationColumn) == null) {
					break;
				}
				org.dhatim.fastexcel.reader.Cell cellWorkTime = row.getCell(cellAddress.get(fiDurationColumn));
				if (cellWorkTime != null) {
					BigDecimal opDurationbyHour = cellWorkTime.asNumber();
					duration += opDurationbyHour.multiply(new BigDecimal("60")).intValue();
				}
			}
			return duration;
		} catch (Exception ex) {
			CommunicationService.progressImportTaskToUser(getUserLogin(),
					String.format("{\"progressImport failed\": \"%s\"}", "<div><b>Row: " + (row.getRowNum() -1)
							+ " at Column: " + columnName + " wrong number format</b></div>"));
			throw ex;
		}
	}

	/**
	 * @param cellAddress
	 * @param row
	 * @param columnName
	 * @return
	 */
	private Integer getIntegerValue(Map<String, Integer> cellAddress, org.dhatim.fastexcel.reader.Row row,
			String columnName) {
		System.out.println("get Integer value: " + cellAddress.get(columnName) + "__" + columnName);
		org.dhatim.fastexcel.reader.Cell cell = row.getCell(cellAddress.get(columnName));
		try {
			if (cell == null) {
				return null;
			}
			return Integer.parseInt(cell.getValue().toString());
		} catch (NumberFormatException ex) {
			CommunicationService.progressImportTaskToUser(getUserLogin(),
					String.format("{\"progressImport failed\": \"%s\"}", "<div><b>Row: " + (row.getRowNum() -1)
							+ " at Column: " + columnName + " wrong number format</b></div>"));
			throw ex;
		}
	}

	/**
	 * @param cellAddress
	 * @param row
	 * @param columnName
	 * @return
	 */
	private List<String> getStringValueForOP(Map<String, Integer> cellAddress, List<String> annotationIndexs,
			org.dhatim.fastexcel.reader.Row row, String columnName) {
		System.out.println("get String value: " + cellAddress.get(columnName) + "__" + columnName);
		String tempOP = null;
		String columnNameTemp = StringUtils.EMPTY;
		String annotationIndexTemp = StringUtils.EMPTY;
		for (String annotationIndex : annotationIndexs) {
			columnNameTemp = String.format("%s_%s", columnName, annotationIndex);
			org.dhatim.fastexcel.reader.Cell cell = null;
			try {
				Integer cellIndex = cellAddress.get(columnNameTemp);
				cell = row.getCell(cellIndex);
			} catch (Exception ex) {
				continue;
			}
			if (cell != null) {
				tempOP = cell.getValue().toString().toLowerCase();
				annotationIndexTemp = annotationIndex;
			} else {
				break;
			}
			// return cell == null ? null : cell.getValue().toString().toLowerCase();
		}

		if (StringUtils.isEmpty(tempOP)) {
			return null;
		}

		List<String> opTemps = new ArrayList<>();
		opTemps.add(tempOP);
		opTemps.add(annotationIndexTemp);
		return opTemps;
	}

	/**
	 * @param cellAddress
	 * @param row
	 * @param columnName
	 * @return
	 */
	private String getStringValue(Map<String, Integer> cellAddress, org.dhatim.fastexcel.reader.Row row,
			String columnName) {
		System.out.println("get String value: " + cellAddress.get(columnName) + "__" + columnName);
		if (cellAddress.get(columnName) != null) {
			org.dhatim.fastexcel.reader.Cell cell = row.getCell(cellAddress.get(columnName));
			return cell == null ? null : cell.getValue().toString().toLowerCase();
		} else {
			return null;
		}
	}

	/**
	 * @param cellAddress
	 * @param row
	 * @param columnName
	 * @return
	 */
	private Instant getDateTime(Map<String, Integer> cellAddress, org.dhatim.fastexcel.reader.Row row,
			String columnName) {
		try {
		Instant taskDateTime = null;
		org.dhatim.fastexcel.reader.Cell cellDateTime = row.getCell(cellAddress.get(columnName));
		if (cellDateTime != null) {
			ZonedDateTime result = ZonedDateTime.parse(cellDateTime.getValue().toString(),
					DateTimeFormatter.ISO_DATE_TIME);
			taskDateTime = Instant.from(result);
		}
		return taskDateTime;
		}catch(Exception ex) {
			CommunicationService.progressImportTaskToUser(getUserLogin(),
					String.format("{\"progressImport failed\": \"%s\"}",
							"<div><b>Row: " + (row.getRowNum() -1) + " at Column: "+ columnName +" wrong datetime format</b></div>"));
			throw ex;
		}
	}

	private String getUserLogin() {
		Optional<String> currentUserLogin = SecurityUtils.getCurrentUserLogin();
		if (currentUserLogin.isPresent()) {
			return currentUserLogin.get();
		}
		return StringUtils.EMPTY;
	}

	/**
	 * This method is using to get the list of columns index
	 *
	 * @param cellAddress
	 * @param cell
	 *
	 * @author TuHP
	 */
	private void getColumnIndex(Map<String, Integer> cellAddress, List<String> annotationIndexs,
			org.dhatim.fastexcel.reader.Row row) {
		row.forEach(cell -> {
			if (cell != null) {
				String columnHeaderName = cell.getText().toUpperCase();
				if (columnHeaderName.contains(AppConstants.PACKAGE_COLUMN_NAME)) {
					cellAddress.put(columnHeaderName, cell.getColumnIndex());
				} else if (columnHeaderName.contains(AppConstants.TASK_COLUMN_NAME)) {
					cellAddress.put(columnHeaderName, cell.getColumnIndex());
				} else if (columnHeaderName.contains(AppConstants.PHASE_COLUMN_NAME)) {
					cellAddress.put(columnHeaderName, cell.getColumnIndex());
				} else if (columnHeaderName.contains(AppConstants.TASK_EST_START_TIME_COLUMN_NAME)) {
					cellAddress.put(columnHeaderName, cell.getColumnIndex());
				} else if (columnHeaderName.contains(AppConstants.TASK_EST_END_TIME_COLUMN_NAME)) {
					cellAddress.put(columnHeaderName, cell.getColumnIndex());
				} else if (columnHeaderName.contains(AppConstants.TASK_FRAME)) {
					cellAddress.put(columnHeaderName, cell.getColumnIndex());
				} else if (columnHeaderName.indexOf(AppConstants.TASK_OP_COLUMN_NAME) == 0) {
					cellAddress.put(columnHeaderName, cell.getColumnIndex());
					String[] annotations = columnHeaderName.split("_");
					if (annotations.length == 3) {
						annotationIndexs.add(annotations[2]);
					}
				} else if (columnHeaderName.contains(AppConstants.TASK_OP_ACTUAL_START_TIME_COLUMN_NAME)) {
					cellAddress.put(columnHeaderName, cell.getColumnIndex());
				} else if (columnHeaderName.contains(AppConstants.TASK_OP_ACTUAL_END_TIME_COLUMN_NAME)) {
					cellAddress.put(columnHeaderName, cell.getColumnIndex());
				} else if (columnHeaderName.contains(AppConstants.TASK_OP_ACTUAL_DURATION_COLUMN_NAME)) {
					cellAddress.put(columnHeaderName, cell.getColumnIndex());
				} else if (columnHeaderName.contains(AppConstants.TASK_REVIEW_COLUMN_NAME)) {
					cellAddress.put(columnHeaderName, cell.getColumnIndex());
				} else if (columnHeaderName.contains(AppConstants.TASK_REVIEW_DURATION_COLUMN_NAME)) {
					cellAddress.put(columnHeaderName, cell.getColumnIndex());
				} else if (columnHeaderName.contains(AppConstants.TASK_OP_COLUMN_NAME)) {
					cellAddress.put(columnHeaderName, cell.getColumnIndex());
				} else if (columnHeaderName.contains(AppConstants.TASK_OP_COLUMN_NAME)) {
					cellAddress.put(columnHeaderName, cell.getColumnIndex());
				}
			}
		});
	}

	/**
	 * Save package into database
	 */
	private Packages savePackages(List<String[]> lstColumns, org.dhatim.fastexcel.reader.Row row,
			PurchaseOrders purchaseOrders, String currentUserLogin, List<ProjectWorkflowsDTO> projectWorkflowsDTOs, Packages packages) {
		Packages _packages = generatePackages(0, lstColumns.get(0), row, currentUserLogin, packages);
		_packages.setPurchaseOrders(purchaseOrders);
		_packages = this.packagesRepository.save(_packages);
		packagesSearchRepository.save(_packages);
		List<TMSCustomFieldScreenDTO> tmsCustomFieldScreenDTOs = getAllCustomFieldScreen(projectWorkflowsDTOs, "Package");
		String[] columns = lstColumns.get(1);
		for (int i = 0; i < columns.length; i++) {
			int cellIndex = lstColumns.get(0).length + i;
			saveCustomField(_packages.getId(), tmsCustomFieldScreenDTOs, "Package", cellIndex, columns[i], row, currentUserLogin);
		}
		return _packages;
	}

	/**
	 * Save task into database
	 */
	private Tasks saveTasks(Packages _packages, int startIndex, String[] lstColumns, String[] lstColumnsCustomField, org.dhatim.fastexcel.reader.Row row,
			String currentUserLogin, List<ProjectWorkflowsDTO> projectWorkflowsDTOs, Boolean isUpdatedTask) {
		Tasks _tasks = generateTasks(startIndex, lstColumns, row, _packages, currentUserLogin, isUpdatedTask);
		List<TMSCustomFieldScreenDTO> tmsCustomFieldScreenDTOs = getAllCustomFieldScreen(projectWorkflowsDTOs, "Task");
		String[] columns = lstColumnsCustomField;
		for (int i = 0; i < columns.length; i++) {
			int cellIndex = startIndex + lstColumns.length + i;
			saveCustomField(_tasks.getId(), tmsCustomFieldScreenDTOs, "Task", cellIndex, columns[i], row, currentUserLogin);
		}
		return _tasks;
	}
	
	/**
	 * Get list field active on WF.
	 * @param projectWorkflowsDTOs
	 * @param nameWF
	 * @return
	 */
	private List<TMSCustomFieldScreenDTO> getAllCustomFieldScreen(List<ProjectWorkflowsDTO> projectWorkflowsDTOs, String nameWF) {
		List<TMSCustomFieldScreenDTO> tmsCustomFieldScreens = new ArrayList<>();
		for (ProjectWorkflowsDTO wf: projectWorkflowsDTOs) {
			if (wf.getName().equals(nameWF)) {
				Long wfId = wf.getId();
				List<TMSCustomFieldScreenDTO> tmsCustomFieldScreenDTOs = this.tMSCustomFieldScreenQueryService.getListTMSCustomFieldScreenByWFId(wfId);
				for (TMSCustomFieldScreenDTO tmsCustom: tmsCustomFieldScreenDTOs) {
					ObjectMapper mapper = new ObjectMapper();
					try {
						ArrayNode nodesCustom = (ArrayNode) mapper.readTree(tmsCustom.getEntityGridPm());
						if (nodesCustom.size() > 0) {
							tmsCustomFieldScreens.add(tmsCustom);
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		return tmsCustomFieldScreens;
	}
	
	/**
	 * save custom field into database
	 * @param id
	 * @param tmsCustomFieldScreenDTOs
	 * @param screenUse
	 * @param cellIndex
	 * @param colName
	 * @param row
	 * @param currentUserLogin
	 */
	private void saveCustomField(Long id, List<TMSCustomFieldScreenDTO> tmsCustomFieldScreenDTOs, String screenUse, int cellIndex, String colName,
			org.dhatim.fastexcel.reader.Row row, String currentUserLogin) {
		for (TMSCustomFieldScreenDTO customFieldScreens : tmsCustomFieldScreenDTOs) {
			ObjectMapper mapperNode = new ObjectMapper();
			ArrayNode nodesCustom;
			try {
				nodesCustom = (ArrayNode) mapperNode.readTree(customFieldScreens.getEntityGridPm());
				String typeField = getTypeOfCustomField(customFieldScreens.getEntityData());
				if (colName.equals(nodesCustom.get(0).textValue())) {
					TMSCustomFieldDTO tMSCustomFieldDTO = tMSCustomFieldService
							.findOne(customFieldScreens.getTmsCustomFieldId());
					TMSCustomFieldScreenValueDTO tmsCustomFieldScreenValueDTO = tMSCustomFieldScreenValueQueryService.getTMSCustomFieldScreenValueByPackageOrTaskAndScreen(id, customFieldScreens.getId(), screenUse);
					if (tmsCustomFieldScreenValueDTO == null) {
						tmsCustomFieldScreenValueDTO = new TMSCustomFieldScreenValueDTO();
					}
					if (screenUse.equals("Package")) {
						tmsCustomFieldScreenValueDTO.setPackagesId(id);
					} else if (screenUse.equals("Task")) {
						tmsCustomFieldScreenValueDTO.setTasksId(id);
					} else {
						tmsCustomFieldScreenValueDTO.setPurchaseOrdersId(id);
					}
					tmsCustomFieldScreenValueDTO.setTmsCustomFieldScreenId(customFieldScreens.getId());
					ObjectMapper mapper = new ObjectMapper();
					JsonNode node = null;
					try {
						node = mapper.readTree(tMSCustomFieldDTO.getEntityData());
						TMSCustomFieldScreenValue _tmsCustomFieldValue = new TMSCustomFieldScreenValue();
						if (node.get("type").asText().equals("textarea")) {
							_tmsCustomFieldValue = generateCustomFields(cellIndex, colName, "text", row,
									currentUserLogin,
									tMSCustomFieldScreenValueMapper.toEntity(tmsCustomFieldScreenValueDTO), typeField);
						} else {
							_tmsCustomFieldValue = generateCustomFields(cellIndex, colName, "value", row,
									currentUserLogin,
									tMSCustomFieldScreenValueMapper.toEntity(tmsCustomFieldScreenValueDTO), typeField);
						}
						if (_tmsCustomFieldValue.getText() != null || _tmsCustomFieldValue.getValue() != null) {
							this.tMSCustomFieldScreenValueRepository.save(_tmsCustomFieldValue);
							this.tMSCustomFieldScreenValueSearchRepository.save(_tmsCustomFieldValue);
						}
					} catch (JsonProcessingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			} catch (JsonProcessingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	
	// get type of customField.
	private String getTypeOfCustomField(String entityData) {
		try {
			JSONObject jsonField = new JSONObject(entityData);
			if (jsonField.getString("type").compareToIgnoreCase("date") == 0) {
				return "java.time.Instant";
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "java.lang.String";
	}

	/**
	 * Read data from excel and generate to task
	 */
	private Tasks generateTasks(int startIndex, String[] columns, org.dhatim.fastexcel.reader.Row row,
			Packages packages, String currentUserLogin, Boolean isUpdatedTask) {
		Tasks task;
		TasksDTO taskDto;
		StringBuffer contentLog = new StringBuffer(String.format(
				"<div style=\\\"margin-top:15px\\\"><b>Progress import data to row %s</b></div>", row.getRowNum() - 1));
		boolean isSaving = true;
		if (!isUpdatedTask) {
			task = new Tasks();
			task.setPackages(packages);

		} else {
			org.dhatim.fastexcel.reader.Cell cell = row.getCell(0);
			task = new Tasks();
			long taskID = (long) Double.parseDouble(cell.getText());
			task.setId(taskID);
			task.setPackages(packages);
			//task = this.tasksRepository.findOne(taskID);
		}
		taskDto = this.tasksMapper.toDto(task);

		for (int i = 0; i < columns.length; i++) {
			String colName = columns[i];
			try {
				// String colName = columns[i];
				int cellIndex = startIndex + i;
				org.dhatim.fastexcel.reader.Cell cell = null;
				if (cellIndex < row.getCellCount()) {
					cell = row.getCell(cellIndex);
				}
				if (cell != null) {
					Class<?> classTask = taskDto.getClass();
					Field field = classTask.getDeclaredField(colName);
					field.setAccessible(true);
					System.out.println(cellIndex + "::colName::" + colName);
					Object value;
					if(AppConstants.TASK_DURATION.equals(colName)) {
						value = CommonFunction.convertTimeStringToMinute(cell.getValue().toString());
					}else {
						value = convertValueToCorrectFieldType(field.getType().getTypeName(), cell);
					}
					field.set(taskDto, value);
					contentLog.append(String.format(
							"<div>import data to column %s <span class=\\\"text-primary\\\">success</span></div>",
							colName));
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
				isSaving = false;
				contentLog.append(String.format(
						"<div>import data to column %s <span class=\\\"text-danger\\\">error</span>: %s</div>", colName,
						"Input wrong type"));
			} catch (IllegalAccessException e) {
				e.printStackTrace();
				isSaving = false;
				contentLog.append(String.format(
						"<div>import data to column %s <span class=\\\"text-danger\\\">error</span>: %s</div>", colName,
						"IllegalAccessException"));
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
				isSaving = false;
				contentLog.append(String.format(
						"<div>import data to column %s <span class=\\\"text-danger\\\">error</span>: %s</div>", colName,
						"NoSuchFieldException"));
			} catch (SecurityException e) {
				e.printStackTrace();
				isSaving = false;
				contentLog.append(String.format(
						"<div>import data to column %s <span class=\\\"text-danger\\\">error</span>: %s</div>", colName,
						"SecurityException"));
			} catch (IndexOutOfBoundsException e) {
				e.printStackTrace();
				isSaving = false;
				contentLog.append(String.format(
						"<div>import data to column %s <span class=\\\"text-danger\\\">error</span>: %s</div>", colName,
						"Please Input on field"));
			} catch (NullPointerException e) {
				e.printStackTrace();
				isSaving = false;
				contentLog.append(String.format(
						"<div>import data to column %s <span class=\\\"text-danger\\\">error</span>: %s</div>", colName,
						"Field is required"));
			} catch (ExcelReaderException e) {
				e.printStackTrace();
				isSaving = false;
				contentLog.append(String.format(
						"<div>import data to column %s <span class=\\\"text-danger\\\">error</span>: %s</div>", colName,
						"Input wrong DATE type"));
			} catch (Exception e) {
				e.printStackTrace();
				isSaving = false;
				contentLog.append(String.format(
						"<div>import data to column %s <span class=\\\"text-danger\\\">error</span>: %s</div>", colName,
						e.getMessage()));
			}
		}
		if (isSaving) {
			task = this.tasksMapper.toEntity(taskDto);
			task = this.tasksRepository.save(task);
			tasksSearchRepository.save(task);
		}
		CommunicationService.progressImportTaskToUser(currentUserLogin,
				String.format("{\"progressImport\": \"%s\"}", contentLog.toString()));
		return task;
	}

	/**
	 * Read data from excel and generate to package
	 */
	private Packages generatePackages(int startIndex, String[] columns, org.dhatim.fastexcel.reader.Row row,
			String currentUserLogin, Packages packages) {

		StringBuffer contentLog = new StringBuffer(
				String.format("<div style=\\\"margin-top:15px\\\"><b>Progress import package %s</b></div>",
						row.getCell(0).getValue().toString()));
		for (int i = 0; i < columns.length; i++) {
			String colName = columns[i];
			try {
				int cellIndex = startIndex + i;
				org.dhatim.fastexcel.reader.Cell cell = row.getCell(cellIndex);
				if (cell != null) {
					Class<?> classPackage = packages.getClass();
					Field field = classPackage.getDeclaredField(colName);
					field.setAccessible(true);
					Object value = convertValueToCorrectFieldType(field.getType().getTypeName(), cell);
					field.set(packages, value);
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
				contentLog.append(String.format(
						"<div>import data to column %s <span class=\\\"text-danger\\\">error</span>: %s</div>", colName,
						"Input wrong type"));
			} catch (IllegalAccessException e) {
				e.printStackTrace();
				contentLog.append(String.format(
						"<div>import data to column %s <span class=\\\"text-danger\\\">error</span>: %s</div>", colName,
						"IllegalAccessException"));
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
				contentLog.append(String.format(
						"<div>import data to column %s <span class=\\\"text-danger\\\">error</span>: %s</div>", colName,
						"NoSuchFieldException"));
			} catch (SecurityException e) {
				e.printStackTrace();
				contentLog.append(String.format(
						"<div>import data to column %s <span class=\\\"text-danger\\\">error</span>: %s</div>", colName,
						"SecurityException"));
			} catch (IndexOutOfBoundsException e) {
				e.printStackTrace();
				contentLog.append(String.format(
						"<div>import data to column %s <span class=\\\"text-danger\\\">error</span>: %s</div>", colName,
						"Please Input on field"));
			} catch (NullPointerException e) {
				e.printStackTrace();
				contentLog.append(String.format(
						"<div>import data to column %s <span class=\\\"text-danger\\\">error</span>: %s</div>", colName,
						"Field is required"));
			} catch (ExcelReaderException e) {
				e.printStackTrace();
				contentLog.append(String.format(
						"<div>import data to column %s <span class=\\\"text-danger\\\">error</span>: %s</div>", colName,
						"Input wrong DATE type"));
			} catch (Exception e) {
				e.printStackTrace();
				contentLog.append(String.format(
						"<div>import data to column %s <span class=\\\"text-danger\\\">error</span>: %s</div>", colName,
						e.getMessage()));
			}
		}
		CommunicationService.progressImportTaskToUser(currentUserLogin,
				String.format("{\"progressPackages\": \"%s\"}", contentLog.toString()));
		return packages;
	}
	
	/**
	 * Read data from excel and generate to package
	 */
	private TMSCustomFieldScreenValue generateCustomFields(int cellIndex, String colName, String fieldName, org.dhatim.fastexcel.reader.Row row,
			String currentUserLogin, TMSCustomFieldScreenValue tmsCustomFieldScreenValue, String typeField) {
		StringBuffer contentLog = new StringBuffer(
				String.format("<div style=\\\"margin-top:15px\\\"><b>Progress import package %s</b></div>",
						row.getCell(0).getValue().toString()));
			try {
				org.dhatim.fastexcel.reader.Cell cell = row.getCell(cellIndex);
				if (cell != null) {
					Class<?> classCustomField = tmsCustomFieldScreenValue.getClass();
					Field field = classCustomField.getDeclaredField(fieldName);
					field.setAccessible(true);
					Object value = convertValueToCorrectFieldType(typeField, cell);
					if (typeField.compareTo("java.time.Instant") == 0) {
						Instant dateUtil = (Instant) value;
						SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
						String dateString = formatter.format(new Date(dateUtil.getEpochSecond() * 1000L));
						field.set(tmsCustomFieldScreenValue, dateString);
					} else {
						field.set(tmsCustomFieldScreenValue, value);
					}
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
				contentLog.append(String.format(
						"<div>import data to column %s <span class=\\\"text-danger\\\">error</span>: %s</div>", colName,
						"Input wrong type"));
			} catch (IllegalAccessException e) {
				e.printStackTrace();
				contentLog.append(String.format(
						"<div>import data to column %s <span class=\\\"text-danger\\\">error</span>: %s</div>", colName,
						"IllegalAccessException"));
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
				contentLog.append(String.format(
						"<div>import data to column %s <span class=\\\"text-danger\\\">error</span>: %s</div>", colName,
						"NoSuchFieldException"));
			} catch (SecurityException e) {
				e.printStackTrace();
				contentLog.append(String.format(
						"<div>import data to column %s <span class=\\\"text-danger\\\">error</span>: %s</div>", colName,
						"SecurityException"));
			} catch (IndexOutOfBoundsException e) {
				e.printStackTrace();
				contentLog.append(String.format(
						"<div>import data to column %s <span class=\\\"text-danger\\\">error</span>: %s</div>", colName,
						"Please Input on field"));
			} catch (NullPointerException e) {
				e.printStackTrace();
				contentLog.append(String.format(
						"<div>import data to column %s <span class=\\\"text-danger\\\">error</span>: %s</div>", colName,
						"Field is required"));
			} catch (ExcelReaderException e) {
				e.printStackTrace();
				contentLog.append(String.format(
						"<div>import data to column %s <span class=\\\"text-danger\\\">error</span>: %s</div>", colName,
						"Input wrong DATE type"));
			} catch (Exception e) {
				e.printStackTrace();
				contentLog.append(String.format(
						"<div>import data to column %s <span class=\\\"text-danger\\\">error</span>: %s</div>", colName,
						e.getMessage()));
			}
		CommunicationService.progressImportTaskToUser(currentUserLogin,
				String.format("{\"progressPackages\": \"%s\"}", contentLog.toString()));
		return tmsCustomFieldScreenValue;
	}

	/**
	 * Read a value from excel and convert to to correct data type
	 */
	private Object convertValueToCorrectFieldType(String typeName, org.dhatim.fastexcel.reader.Cell cell)
			throws Exception {
		Object value = cell.getValue();
		Object valueTest = null;
		Object valueTestData = null;
		switch (typeName) {
		case "java.time.Instant":
			System.out.println("cell.asDate" + cell.toString());
			System.out.println(value + ":::" + cell.asDate());
			return value == null ? null : ZonedDateTime.of(cell.asDate(), AppConstants.SYSTEM_ZONE_ID).toInstant();
		case "java.lang.Integer":
			value = value != null ? value : 0;
			Double doubleValue = Double.parseDouble(value.toString());
			return doubleValue.intValue();
		case "java.lang.Long":
			value = value != null ? value : 0;
			return Long.parseLong(value.toString());
		case "fpt.dps.dtms.domain.enumeration.TaskSeverity":
			value = value != null ? value : "NA";
			return TaskSeverity.valueOf(value.toString());
		case "fpt.dps.dtms.domain.enumeration.TaskPriority":
			value = value != null ? value : "NA";
			return TaskPriority.valueOf(value.toString());
		case "fpt.dps.dtms.domain.enumeration.TaskAvailability":
			value = value != null ? value : "NA";
			return TaskAvailability.valueOf(value.toString());
		case "fpt.dps.dtms.domain.enumeration.OPStatus":
			value = value != null ? value : "NA";
			return OPStatus.valueOf(value.toString());
		case "fpt.dps.dtms.domain.enumeration.ReviewStatus":
			value = value != null ? value : "NA";
			return ReviewStatus.valueOf(value.toString());
		case "fpt.dps.dtms.domain.enumeration.FixStatus":
			value = value != null ? value : "NA";
			return FixStatus.valueOf(value.toString());
		case "fpt.dps.dtms.domain.enumeration.FIStatus":
			value = value != null ? value : "NA";
			return FIStatus.valueOf(value.toString());
		case "fpt.dps.dtms.domain.enumeration.ErrorSeverity":
			value = value != null ? value : "NA";
			return ErrorSeverity.valueOf(value.toString());
		case "fpt.dps.dtms.domain.enumeration.TaskStatus":
			value = value != null ? value : "NA";
			return TaskStatus.valueOf(value.toString());
		default:
			return value == null ? null : value.toString();
		}
	}

	/**
	 * Read a value from database and set to excel
	 */
	private Cell setValueToExcelCell(String typeName, Sheet sheet, Cell cell, CreationHelper createHelper,
			CellStyle cellStyle, Object value, List<String> projectUsers, List<FieldConfigVM> fieldConfigVMs,
			String colName, int col, int rowIndex) throws Exception {
		switch (typeName) {
		case "java.time.Instant":
			System.out.println("cell.asDate" + cell.toString());
			cell.setCellStyle(cellStyle);
			cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("m/dd/yyyy h:mm"));
			ZonedDateTime zdt = ZonedDateTime.ofInstant((Instant) value, AppConstants.SYSTEM_ZONE_ID);
			Calendar cal = GregorianCalendar.from(zdt);
			cell.setCellValue(cal);
			return cell;
		case "java.lang.Integer":
			value = value != null ? value : 0;
			cell.setCellValue((Integer) value);
			return cell;
		case "java.lang.Long":
			value = value != null ? value : 0;
			if (colName.toLowerCase().equals("id")) {
				cell.getCellStyle().setLocked(true);
			}
			cell.setCellValue((Long) value);
			return cell;
		case "fpt.dps.dtms.domain.enumeration.TaskSeverity":
			value = value != null ? value : "";
			// cell.setCellValue(value.toString());
			return generateListDataToCell(sheet, cell, fieldConfigVMs, colName, col, rowIndex, value.toString());
		case "fpt.dps.dtms.domain.enumeration.TaskPriority":
			value = value != null ? value : "";
			// cell.setCellValue((String)value);
			return generateListDataToCell(sheet, cell, fieldConfigVMs, colName, col, rowIndex, value.toString());
		case "fpt.dps.dtms.domain.enumeration.TaskAvailability":
			value = value != null ? value : "";
			// cell.setCellValue(value.toString());
			return generateListDataToCell(sheet, cell, fieldConfigVMs, colName, col, rowIndex, value.toString());
		case "fpt.dps.dtms.domain.enumeration.OPStatus":
			value = value != null ? value : "";
			// cell.setCellValue(value.toString());
			return generateListDataToCell(sheet, cell, fieldConfigVMs, colName, col, rowIndex, value.toString());
		case "fpt.dps.dtms.domain.enumeration.ReviewStatus":
			value = value != null ? value : "";
			// cell.setCellValue(value.toString());
			return generateListDataToCell(sheet, cell, fieldConfigVMs, colName, col, rowIndex, value.toString());
		case "fpt.dps.dtms.domain.enumeration.FixStatus":
			value = value != null ? value : "";
			// cell.setCellValue(value.toString());
			return generateListDataToCell(sheet, cell, fieldConfigVMs, colName, col, rowIndex, value.toString());
		case "fpt.dps.dtms.domain.enumeration.FIStatus":
			value = value != null ? value : "";
			// cell.setCellValue(value.toString());
			return generateListDataToCell(sheet, cell, fieldConfigVMs, colName, col, rowIndex, value.toString());
		case "fpt.dps.dtms.domain.enumeration.ErrorSeverity":
			value = value != null ? value : "";
			// cell.setCellValue(value.toString());
			return generateListDataToCell(sheet, cell, fieldConfigVMs, colName, col, rowIndex, value.toString());
		case "fpt.dps.dtms.domain.enumeration.TaskStatus":
			value = value != null ? value : "";
			// cell.setCellValue(value.toString());
			return generateListDataToCell(sheet, cell, fieldConfigVMs, colName, col, rowIndex, value.toString());
		/*
		 * case "fpt.dps.dtms.domain.ProjectUsers": value = value != null ? value : "";
		 * return generateListUserToCell(sheet, cell, projectUsers, fieldConfigVMs,
		 * colName, col, rowIndex, value.toString());
		 */
		default:
			value = value != null ? value : "";
			if (colName.equals(AppConstants.ROUND_OP) || colName.equals(AppConstants.ROUND_REVIEW1)
					|| colName.equals(AppConstants.ROUND_REVIEW2) || colName.equals(AppConstants.ROUND_FIXER)
					|| colName.equals(AppConstants.ROUND_FI)) {
				return generateListUserToCell(sheet, cell, projectUsers, fieldConfigVMs, colName, col, rowIndex,
						value.toString());
			}
			cell.setCellValue(value.toString());
			return cell;
		}
	}

	/**
	 * Read a value from excel and convert to to correct data type for members
	 * management
	 */
	private Cell setValueToMemberExcelCell(String typeName, Sheet sheet, Cell cell, CreationHelper createHelper,
			CellStyle cellStyle, Object value, String colName, int col, int rowIndex) throws Exception {
		switch (typeName) {
		case "java.time.LocalDate":
			cell.setCellStyle(cellStyle);
			cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("m/dd/yyyy"));
			Instant localDate = ((LocalDate) value).atStartOfDay(ZoneId.systemDefault()).toInstant();
			ZonedDateTime zdtLocal = ZonedDateTime.ofInstant((Instant) localDate, AppConstants.SYSTEM_ZONE_ID);
			Calendar calendar = GregorianCalendar.from(zdtLocal);
			cell.setCellValue(calendar);
			return cell;
		case "java.time.Instant":
			System.out.println("cell.asDate" + cell.toString());
			cell.setCellStyle(cellStyle);
			cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("m/dd/yyyy h:mm"));
			ZonedDateTime zdt = ZonedDateTime.ofInstant((Instant) value, AppConstants.SYSTEM_ZONE_ID);
			Calendar cal = GregorianCalendar.from(zdt);
			cell.setCellValue(cal);
			return cell;
		case "java.lang.Integer":
			value = value != null ? value : 0;
			cell.setCellValue((Integer) value);
			return cell;
		case "java.lang.Long":
			value = value != null ? value : 0;
			cell.setCellValue((Long) value);
			return cell;
		case "java.lang.Float":
			value = value != null ? value : 0;
			cell.setCellValue((Float) value);
			return cell;
		case "boolean":
			value = (Boolean) value != true ? "offline" : "online";
			cell.setCellValue((String) value);
			return cell;
		case "fpt.dps.dtms.domain.enumeration.ProjectRoles":
			value = value != null ? value : "";
			cell.setCellValue(value.toString());
			return cell;
		case "fpt.dps.dtms.domain.enumeration.ContractType":
			value = value != null ? value : "";
			cell.setCellValue(value.toString());
			return cell;
		default:
			value = value != null ? value : "";
			cell.setCellValue((String) value);
			return cell;
		}
	}

	private Cell generateListDataToCell(Sheet sheet, Cell cell, List<FieldConfigVM> fieldConfigVMs, String colName,
			int col, int rowIndex, String defaultvalue) {
		for (FieldConfigVM field : fieldConfigVMs) {
			String fieldName = field.getField().toUpperCase();
			if (fieldName.equals(colName.toUpperCase()) && field.getOptions() != null) {
				List<String> list = (ArrayList<String>) field.getOptions();
				if (list.size() > 0) {
					setExplicitListConstraint(sheet, cell, list.toArray(new String[0]), defaultvalue, rowIndex,
							rowIndex, col, col);
				}
				break;
			}
		}
		return cell;
	}

	private Cell generateListUserToCell(Sheet sheet, Cell cell, List<String> projectUsers,
			List<FieldConfigVM> fieldConfigVMs, String colName, int col, int rowIndex, String defaultvalue) {
		if (projectUsers.size() > 0) {
			setExplicitListConstraint(sheet, cell, projectUsers.toArray(new String[0]), defaultvalue, rowIndex,
					rowIndex, col, col);
		}
		return cell;
	}

	/**
	 * Generate task field to corresponding excel cell.
	 */
	private Row generateTasks(int rowIndex, String[] columns, Sheet sheet, TasksDTO taskDTO, List<Map<String, Object>> result, List<String> projectUsers,
			List<FieldConfigVM> fieldConfigVMs, CreationHelper createHelper, CellStyle cellStyle) {
		Row row = sheet.createRow(rowIndex);
		for (int i = 0; i < columns.length; i++) {
			try {
				String colName = columns[i];

				int cellIndex = i;
				Cell cell = row.createCell(cellIndex);
				// cellStyle.setLocked(true);
				// cell.setCellStyle(cellStyle);
				// if (cell != null) {
				Class<?> classTask = taskDTO.getClass();
				List<String> listName = new ArrayList<String>();
				for (Field fieldClass: classTask.getDeclaredFields()) {
						listName.add(fieldClass.getName());
				}
				if (listName.contains(colName)) {
					Field field = classTask.getDeclaredField(colName);
					field.setAccessible(true);
					Object value = field.get(taskDTO);
					if (value != null) {
						cell = this.setValueToExcelCell(field.getType().getTypeName(), sheet, cell, createHelper, cellStyle,
								field.get(taskDTO), projectUsers, fieldConfigVMs, colName, i, rowIndex);
					}
				} else {
					if (result.size() > 0) {
						Boolean checkExists = true;
						for (Map<String, Object> dynamidField: result) {
							if (dynamidField.containsKey(colName)) {
								checkExists = false;
								cell = this.setValueToExcelCell("String", sheet, cell, createHelper, cellStyle,
										dynamidField.get(colName), projectUsers, fieldConfigVMs, colName, i, rowIndex);
							}
						}
						if (checkExists) {
							cell = this.setValueToExcelCell("String", sheet, cell, createHelper, cellStyle,
									null, projectUsers, fieldConfigVMs, colName, i, rowIndex);
						}
					}
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IndexOutOfBoundsException e) {
				// e.printStackTrace();
			} catch (NullPointerException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return row;
	}

	/**
	 * Read data from excel and generate to task
	 */
	private Row generateMember(int rowIndex, String[] columns, Sheet sheet, Map<String, Object> user, CreationHelper createHelper,
			CellStyle cellStyle) {
		User u = new User();
		this.transferUserData(user, u);
		Row row = sheet.createRow(rowIndex);
		for (int i = 0; i < columns.length; i++) {
			try {
				Instant timeNow = new Date().toInstant();
				timeNow = timeNow.atZone(ZoneOffset.UTC).withHour(0).withMinute(0).withSecond(0).withNano(0)
						.toInstant();
				LoginTrackingDTO loginTrackingDTO = loginTrackingQueryService.getTimeLoginByUser(u.getLogin(),
						timeNow);
				String colName = columns[i];
				int cellIndex = i;
				Cell cell = row.createCell(cellIndex);
				if (!colName.equals("startTime")) {
					Class<?> classUser = u.getClass();
					Field field = classUser.getDeclaredField(colName);
					field.setAccessible(true);
					System.out.println(cellIndex + "::colName::" + colName);
					cell = this.setValueToMemberExcelCell(field.getType().getTypeName(), sheet, cell, createHelper,
							cellStyle, field.get(u), colName, i, rowIndex);
				} else if (colName.equals("startTime") && loginTrackingDTO != null) {
					Class<?> classLoginTrackingDTO = loginTrackingDTO.getClass();
					Field field = classLoginTrackingDTO.getDeclaredField(colName);
					field.setAccessible(true);
					System.out.println(cellIndex + "::colName::" + colName);
					cell = this.setValueToMemberExcelCell(field.getType().getTypeName(), sheet, cell, createHelper,
							cellStyle, field.get(loginTrackingDTO), colName, i, rowIndex);
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IndexOutOfBoundsException e) {
				// e.printStackTrace();
			} catch (NullPointerException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return row;
	}
	
	private User transferUserData(Map<String, Object> hash_map, User user) {
		if (hash_map.get("firstName") != null) {
			user.setFirstName(hash_map.get("firstName").toString());
		}
		if (hash_map.get("lastName") != null) {
			user.setLastName(hash_map.get("lastName").toString());
		}
		if (hash_map.get("startTime") != null) {
			user.setLastModifiedDate((Instant) hash_map.get("startTime"));
		}
		if (hash_map.get("login") != null) {
			user.setLogin(hash_map.get("login").toString());
			user.setId(this.userRepository.findUserLogin(user.getLogin()));
		}
		if (hash_map.get("email") != null) {
			user.setEmail(hash_map.get("email").toString());
		}
		if (hash_map.get("status") != null) {
			user.setStatus((boolean) hash_map.get("status"));
		}
		
		return user;
	}

	/**
	 * Read data from excel and generate to task
	 */
	private Row generateAllocation(int rowIndex, String[] columns, Sheet sheet, ProjectUsers user, Long projectId,
			CreationHelper createHelper, CellStyle cellStyle) {
		Row row = sheet.createRow(rowIndex);
		for (int i = 0; i < columns.length; i++) {
			try {
				String colName = columns[i];
				User userContract = userRepository.findByLogin(user.getUserLogin());
				int cellIndex = i;
				Cell cell = row.createCell(cellIndex);
				if (colName.equals("contractType") && userContract != null) {
					Class<?> classUser = userContract.getClass();
					Field field = classUser.getDeclaredField(colName);
					field.setAccessible(true);
					cell = this.setValueToMemberExcelCell(field.getType().getTypeName(), sheet, cell, createHelper,
							cellStyle, field.get(userContract), colName, i, rowIndex);
				} else if (colName.equals("workingLocation")) {
					UserProfile userProfile = userProfileService.findByUserId(userContract.getId());
					Class<?> classUser = userProfile.getClass();
					Field field = classUser.getDeclaredField(colName);
					field.setAccessible(true);
					cell = this.setValueToMemberExcelCell(field.getType().getTypeName(), sheet, cell, createHelper,
							cellStyle, field.get(userProfile), colName, i, rowIndex);
				} else if (colName.equals("totalEffortPlan")) {
					Float effortPlan = projectUsersQueryService.getDatesBetween(user.getStartDate(), user.getEndDate(),
							user.getEffortPlan());
					cell = this.setValueToMemberExcelCell("java.lang.Float", sheet, cell, createHelper, cellStyle,
							effortPlan, colName, i, rowIndex);
				} else if (colName.equals("workingEffort")) {
					String effortPlan = taskTrackingTimeQueryService.sumActualEffort(projectId, user.getUserLogin(),
							user.getStartDate(), user.getEndDate());
					cell = this.setValueToMemberExcelCell("java.lang.String", sheet, cell, createHelper, cellStyle,
							effortPlan, colName, i, rowIndex);
				} else {
					Class<?> classUser = user.getClass();
					Field field = classUser.getDeclaredField(colName);
					field.setAccessible(true);
					System.out.println(cellIndex + "::colName::" + colName);
					cell = this.setValueToMemberExcelCell(field.getType().getTypeName(), sheet, cell, createHelper,
							cellStyle, field.get(user), colName, i, rowIndex);
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IndexOutOfBoundsException e) {
				// e.printStackTrace();
			} catch (NullPointerException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return row;
	}

	/**
	 * Get cell background color
	 */
	private short getCellBackGroundColor(String value) {
		short colorIndex = IndexedColors.AQUA.getIndex();
		if (value.contains("Package")) {
			colorIndex = IndexedColors.GREEN.getIndex();
		}
		if (value.contains("Task")) {
			colorIndex = IndexedColors.AQUA.getIndex();
		}
		return colorIndex;
	}

	/**
	 * Get all config fields for package and task
	 */
	public List<FieldConfigVM> getAllFieldConfig(List<ProjectWorkflowsDTO> projectWorkflowsDTOs) {
		ObjectMapper mapper = new ObjectMapper();
		List<FieldConfigVM> packageFieldConfigs = null;
		List<FieldConfigVM> taskFieldConfigs = null;
		try {
			packageFieldConfigs = mapper.readValue(projectWorkflowsDTOs.get(0).getInputDTO(),
					new TypeReference<List<FieldConfigVM>>() {
					});
			taskFieldConfigs = mapper.readValue(projectWorkflowsDTOs.get(1).getInputDTO(),
					new TypeReference<List<FieldConfigVM>>() {
					});
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<FieldConfigVM> result = Stream.concat(packageFieldConfigs.stream(), taskFieldConfigs.stream())
				.collect(Collectors.toList());

		return result;
	}

	/**
	 * Get all config fields for task
	 */
	public List<FieldConfigVM> getAllTaskFieldConfig(List<ProjectWorkflowsDTO> projectWorkflowsDTOs) {
		ObjectMapper mapper = new ObjectMapper();
		List<FieldConfigVM> taskFieldConfigs = null;
		try {
			taskFieldConfigs = mapper.readValue(projectWorkflowsDTOs.get(0).getInputDTO(),
					new TypeReference<List<FieldConfigVM>>() {
					});
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return taskFieldConfigs;
	}

	/**
	 * Get all config fields to export to excel template
	 */
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
				List<String> listCustomColumns = getAllColumnsDynamic(projectWorkflowsDTO.getId());
				if (listCustomColumns.size() > 0) {
					columns.addAll(listCustomColumns);
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
	 * Get all config fields to export to excel template
	 */
	private String[] getExportColumns(List<ProjectWorkflowsDTO> projectWorkflowsDTOs) {
		List<String> columns = new ArrayList<>();
		for (ProjectWorkflowsDTO projectWorkflowsDTO : projectWorkflowsDTOs) {
			ObjectMapper mapper = new ObjectMapper();
			try {
				ArrayNode nodes = (ArrayNode) mapper.readTree(projectWorkflowsDTO.getPmGridDTO());
				for (int i = 0; i < nodes.size(); i++) {
					String colName = nodes.get(i).textValue();
					columns.add(projectWorkflowsDTO.getName() + '_' + colName);
				}
				List<String> columnsDynamic = getExportColumnsDynamic(projectWorkflowsDTO);
				columns.addAll(columnsDynamic);
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
	 * Get all config columns and custom columns
	 * @param wfId
	 * @return
	 */
	private List<String> getExportColumnsDynamic(ProjectWorkflowsDTO projectWorkflowsDTO) {
		List<String> columns = new ArrayList<>();
		List<TMSCustomFieldScreenDTO> tmsCustomFieldScreenDTOs = this.tMSCustomFieldScreenQueryService.getListTMSCustomFieldScreenByWFId(projectWorkflowsDTO.getId());
		for (TMSCustomFieldScreenDTO tmsCustom: tmsCustomFieldScreenDTOs) {
			ObjectMapper mapper = new ObjectMapper();
			try {
				ArrayNode nodesCustom = (ArrayNode) mapper.readTree(tmsCustom.getEntityGridPm());
				for (int i = 0; i < nodesCustom.size(); i++) {
					columns.add(projectWorkflowsDTO.getName() + '_' + nodesCustom.get(i).textValue());
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return columns;
	}

	/**
	 * Get all config colums
	 */
	private List<String[]> getImportColumns(List<ProjectWorkflowsDTO> projectWorkflowsDTOs) {
		List<String[]> lstColumns = new ArrayList<>();
		List<String> columns;
		for (ProjectWorkflowsDTO projectWorkflowsDTO : projectWorkflowsDTOs) {
			columns = new ArrayList<>();
			ObjectMapper mapper = new ObjectMapper();
			try {
				ArrayNode nodes = (ArrayNode) mapper.readTree(projectWorkflowsDTO.getPmGridDTO());
				for (int i = 0; i < nodes.size(); i++) {
					columns.add(nodes.get(i).textValue());
				}
				String[] arrColumns = new String[columns.size()];
				columns.toArray(arrColumns);
				lstColumns.add(arrColumns);
				List<String> listCustomColumns = getAllColumnsDynamic(projectWorkflowsDTO.getId());
				String[] arrColumnsCustomField = new String[listCustomColumns.size()];
				listCustomColumns.toArray(arrColumnsCustomField);
				lstColumns.add(arrColumnsCustomField);
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return lstColumns;
	}

	/**
	 * Get all config columns and custom columns
	 * @param wfId
	 * @return
	 */
	private List<String> getAllColumnsDynamic(Long wfId) {
		List<String> columns = new ArrayList<>();
		List<TMSCustomFieldScreenDTO> tmsCustomFieldScreenDTOs = this.tMSCustomFieldScreenQueryService.getListTMSCustomFieldScreenByWFId(wfId);
		for (TMSCustomFieldScreenDTO tmsCustom: tmsCustomFieldScreenDTOs) {
			ObjectMapper mapper = new ObjectMapper();
			try {
				ArrayNode nodesCustom = (ArrayNode) mapper.readTree(tmsCustom.getEntityGridPm());
				for (int i = 0; i < nodesCustom.size(); i++) {
					columns.add(nodesCustom.get(i).textValue());
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return columns;
	}

	/**
	 * Read excel file and generate user objects.
	 *
	 * @param file
	 * @return list of users to save into database
	 */
	public List<UserDTO> getUsersFromFile(MultipartFile file) throws IOException {
		InputStream fileInputStream = file.getInputStream();
		List<UserDTO> users = new ArrayList<UserDTO>();
		try (ReadableWorkbook wb = new ReadableWorkbook(fileInputStream)) {
			org.dhatim.fastexcel.reader.Sheet sheet = wb.getFirstSheet();
			try (Stream<org.dhatim.fastexcel.reader.Row> rows = sheet.openStream()) {
				rows.forEach(row -> {
					// try {
					int index = row.getRowNum();
					if (index == 1 || row.getCell(0) == null || row.getCell(0).getValue() == null) {
						return;
					}
					System.out.println("row index: " + index);
					UserDTO user = new UserDTO();
					String login = row.getCell(0).getValue().toString();
					login = login.trim().toLowerCase();
					user.setLogin(login.trim());
					String password = row.getCell(1).getValue() == null ? AppConstants.DEFAULT_PASSWORD
							: row.getCell(1).getValue().toString();
					user.setPassword(password);
					if (row.getCell(2).getValue() != null) {
						String firstName = row.getCell(2).getValue().toString();
						user.setFirstName(firstName.trim());
					}
					if (row.getCell(3).getValue() != null) {
						String lastname = row.getCell(3).getValue().toString();
						user.setLastName(lastname.trim());
					}
					Object objEmail = row.getCell(4).getValue();
					if (objEmail != null) {
						user.setEmail(objEmail.toString().trim());
					} else {
						/// throw new BadRequestAlertException("A new issues cannot already have an ID",
						/// "fds", "idexists");
						// throw new NotFoundException(AppConstants.NOT_FOUND + "Email");
						// throw new NotFoundException(password);
					}
					Object objContractType = row.getCell(5).getValue();
					if (objContractType != null) {
						user.setContractType(ContractType.valueOf(objContractType.toString().trim()));
					} else {
						user.setContractType(ContractType.FULLTIME);
					}
					Object objBuCode = row.getCell(6).getValue();
					if (objBuCode != null) {
						BusinessUnit businessUnit = businessUnitRepository.findOneBycode(objBuCode.toString().trim());
						if (businessUnit == null) {
							// throw new NotFoundException(AppConstants.NOT_FOUND + buCode.toString());
						} else {
							user.setBusinessUnitId(businessUnit.getId());
						}
					} else {
						// throw new NotFoundException(AppConstants.NOT_FOUND + "Business Code");
					}
					Object objRole = row.getCell(7).getValue();
					if (objRole != null) {
						String[] roles = objRole.toString().split(",");
						Set<String> authorities = new HashSet<>();
						for (String role : roles) {
							authorities.add(role);
						}
						user.setAuthorities(authorities);
					}
					users.add(user);
					// }catch (Exception ex) {
					// }
				});
			}
		}
		return users;
	}

	/**
	 * Read excel file and generate user objects.
	 *
	 * @param file
	 * @return list of users to save into database
	 */
	public List<UserDTO> getTasksFromFile(MultipartFile file) throws IOException {
		InputStream fileInputStream = file.getInputStream();
		List<UserDTO> users = new ArrayList<UserDTO>();
		try (ReadableWorkbook wb = new ReadableWorkbook(fileInputStream)) {
			org.dhatim.fastexcel.reader.Sheet sheet = wb.getFirstSheet();
			try (Stream<org.dhatim.fastexcel.reader.Row> rows = sheet.openStream()) {
				rows.forEach(row -> {
					int index = row.getRowNum();
					if (index == 1 || row.getCell(0) == null || row.getCell(0).getValue() == null) {
						return;
					}
					System.out.println("row index: " + index);
					UserDTO user = new UserDTO();
					user.setLogin(row.getCell(0).getValue().toString());
					user.setPassword(row.getCell(1).getValue().toString());
					if (row.getCell(2).getValue() != null) {
						user.setFirstName(row.getCell(2).getValue().toString());
					}
					if (row.getCell(3).getValue() != null) {
						user.setLastName(row.getCell(3).getValue().toString());
					}
					user.setEmail(row.getCell(4).getValue().toString());
					users.add(user);
				});
			}
		}
		return users;
	}

	/**
	 * Read excel file and generate projectUser objects.
	 *
	 * @param file
	 * @return list of users to save into database
	 */
	public List<ProjectUsersDTO> getProjectUsersFromFile(MultipartFile file, Long projectId) throws IOException {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");

		InputStream fileInputStream = file.getInputStream();
		List<ProjectUsersDTO> projectUsers = new ArrayList<ProjectUsersDTO>();
		try (ReadableWorkbook wb = new ReadableWorkbook(fileInputStream)) {
			org.dhatim.fastexcel.reader.Sheet sheet = wb.getFirstSheet();
			try (Stream<org.dhatim.fastexcel.reader.Row> rows = sheet.openStream()) {
				rows.forEach(row -> {
					int index = row.getRowNum();
					if (index == 1 || row.getCell(0) == null || row.getCell(0).getValue() == null) {
						return;
					}
					System.out.println("row index: " + index);
					ProjectUsersDTO projectUser = new ProjectUsersDTO();

					String login = row.getCell(0).getValue().toString();
					projectUser.setUserLogin(login.trim().toLowerCase());

					LocalDate localStartDate;
					try {
						String startDate = row.getCell(1).getValue().toString().trim();
						localStartDate = LocalDate.parse(startDate, formatter);
					} catch (DateTimeParseException ex) {
						localStartDate = ZonedDateTime.of(row.getCell(1).asDate(), AppConstants.SYSTEM_ZONE_ID)
								.toLocalDate();
					}
					projectUser.setStartDate(localStartDate);

					LocalDate localEndDate;
					try {
						String endDate = row.getCell(2).getValue().toString().trim();
						localEndDate = LocalDate.parse(endDate, formatter);
					} catch (DateTimeParseException ex) {
						localEndDate = ZonedDateTime.of(row.getCell(2).asDate(), AppConstants.SYSTEM_ZONE_ID)
								.toLocalDate();
					}
					projectUser.setEndDate(localEndDate);

					Object role = row.getCell(3).getValue();
					if (role != null) {
						projectUser.setRoleName(ProjectRoles.valueOf(role.toString().toUpperCase().trim()));
					}

					Object objEffortPlan = row.getCell(4).getValue();
					if (objEffortPlan != null) {
						Float effortPlan = Float.parseFloat(objEffortPlan.toString());
						projectUser.setEffortPlan(effortPlan);
					}
					projectUser.setProjectId(projectId);

					projectUsers.add(projectUser);
				});
			}
		}
		return projectUsers;
	}

	/**
	 * Export template Project User to excel file
	 *
	 * @return the excel file
	 */
	public ByteArrayInputStream exportTemplateProjectUserToExcel() throws IOException {
		List<String> COLUMNs = new ArrayList<>();
		COLUMNs.add("userLogin");
		COLUMNs.add("startDate");
		COLUMNs.add("endDate");
		COLUMNs.add("roleName");
		COLUMNs.add("effortPlan");

		try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream();) {
			Sheet sheet = workbook.createSheet("Project_User");
			sheet.setDefaultColumnWidth(10);
			DataFormat format = workbook.createDataFormat();
			CreationHelper createHelper = workbook.getCreationHelper();
			Font headerFont = workbook.createFont();
			headerFont.setBold(true);
			headerFont.setColor(IndexedColors.WHITE.getIndex());

			CellStyle headerCellStyle;

			// Row for Header
			Row headerRow = sheet.createRow(0);

			// Header
			int i = 0;
			for (String col : COLUMNs) {
				String colName = col;
				headerCellStyle = workbook.createCellStyle();
				headerCellStyle.setFont(headerFont);
				headerCellStyle.setFillForegroundColor(getCellBackGroundColor(colName));
				headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(colName);
				cell.setCellStyle(headerCellStyle);
				i++;
			}

			ProjectUsersDTO projectUser = new ProjectUsersDTO();
			projectUser.setUserLogin("user");
			projectUser.setStartDate(LocalDate.now());
			projectUser.setEndDate(LocalDate.now().plus(10, ChronoUnit.DAYS));
			projectUser.setRoleName(ProjectRoles.OPERATOR);
			projectUser.setEffortPlan((float) 8);

			CellStyle cellStyle = workbook.createCellStyle();
			this.setValueToProjectUsersExcelCell(1, COLUMNs, sheet, projectUser, createHelper, cellStyle);

			workbook.write(out);
			return new ByteArrayInputStream(out.toByteArray());
		}
	}

	private Row setValueToProjectUsersExcelCell(int rowIndex, List<String> columns, Sheet sheet,
			ProjectUsersDTO projectUsers, CreationHelper createHelper, CellStyle cellStyle) {
		Row row = sheet.createRow(rowIndex);
		int i = 0;
		for (String col : columns) {
			try {
				String colName = col;
				int cellIndex = i;
				Cell cell = row.createCell(cellIndex);

				if (colName.equals("userLogin")) {
					cell.setCellValue(projectUsers.getUserLogin());
				} else if (colName.equals("startDate")) {
					cell.setCellValue(projectUsers.getStartDate().format(DateTimeFormatter.ofPattern("d/M/yyyy")));
				} else if (colName.equals("endDate")) {
					cell.setCellValue(projectUsers.getEndDate().format(DateTimeFormatter.ofPattern("d/M/yyyy")));
				} else if (colName.equals("roleName")) {
					String[] dataList = Stream.of(ProjectRoles.values()).map(ProjectRoles::name).toArray(String[]::new);
					setExplicitListConstraint(sheet, cell, dataList, dataList[2], rowIndex, rowIndex, i, i);
					// cell.setCellValue("OPERATOR");
				} else if (colName.equals("effortPlan")) {
					cell.setCellValue(projectUsers.getEffortPlan());
				}
				i++;
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IndexOutOfBoundsException e) {
				// e.printStackTrace();
			} catch (NullPointerException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return row;
	}

	private void settingExcelSheet(XSSFSheet sheet) {
		try {
			sheet.setDefaultColumnWidth(10);
			// Restrict deleting columns
			sheet.lockDeleteColumns(true);
			// Restrict formatting cells
			sheet.lockFormatCells(true);
			// Restrict formatting columns
			sheet.lockFormatColumns(true);
			// Restrict inserting columns
			sheet.lockInsertColumns(true);
			// Lock the sheet
			// sheet.enableLocking();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
