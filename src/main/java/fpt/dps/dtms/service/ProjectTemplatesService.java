package fpt.dps.dtms.service;

import fpt.dps.dtms.domain.ProjectTemplates;
import fpt.dps.dtms.repository.ProjectTemplatesRepository;
import fpt.dps.dtms.repository.search.ProjectTemplatesSearchRepository;
import fpt.dps.dtms.service.dto.ProjectTemplatesDTO;
import fpt.dps.dtms.service.dto.ProjectWorkflowsCriteria;
import fpt.dps.dtms.service.dto.ProjectWorkflowsDTO;
import fpt.dps.dtms.service.dto.TasksDTO;
import fpt.dps.dtms.service.mapper.ProjectTemplatesMapper;
import fpt.dps.dtms.service.util.ExcelGenerator;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.LongFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import static org.elasticsearch.index.query.QueryBuilders.*;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Service Implementation for managing ProjectTemplates.
 */
@Service
@Transactional
public class ProjectTemplatesService {

	private final Logger log = LoggerFactory.getLogger(ProjectTemplatesService.class);

	private final ProjectTemplatesRepository projectTemplatesRepository;

	private final ProjectTemplatesMapper projectTemplatesMapper;

	private final ProjectTemplatesSearchRepository projectTemplatesSearchRepository;

	private final ProjectWorkflowsQueryService projectWorkflowsQueryService;
	
	private final ExcelGenerator excelGenerator;

	public ProjectTemplatesService(ProjectTemplatesRepository projectTemplatesRepository,
			ProjectTemplatesMapper projectTemplatesMapper, ExcelGenerator excelGenerator,
			ProjectTemplatesSearchRepository projectTemplatesSearchRepository,
			ProjectWorkflowsQueryService projectWorkflowsQueryService) {
		this.projectTemplatesRepository = projectTemplatesRepository;
		this.projectTemplatesMapper = projectTemplatesMapper;
		this.projectTemplatesSearchRepository = projectTemplatesSearchRepository;
		this.projectWorkflowsQueryService = projectWorkflowsQueryService;
		this.excelGenerator = excelGenerator;
	}

	/**
	 * Save a projectTemplates.
	 *
	 * @param projectTemplatesDTO
	 *            the entity to save
	 * @return the persisted entity
	 */
	public ProjectTemplatesDTO save(ProjectTemplatesDTO projectTemplatesDTO) {
		log.debug("Request to save ProjectTemplates : {}", projectTemplatesDTO);
		ProjectTemplates projectTemplates = projectTemplatesMapper.toEntity(projectTemplatesDTO);
		projectTemplates = projectTemplatesRepository.save(projectTemplates);
		ProjectTemplatesDTO result = projectTemplatesMapper.toDto(projectTemplates);
		projectTemplatesSearchRepository.save(projectTemplates);
		return result;
	}

	/**
	 * Get all the projectTemplates.
	 *
	 * @param pageable
	 *            the pagination information
	 * @return the list of entities
	 */
	@Transactional(readOnly = true)
	public Page<ProjectTemplatesDTO> findAll(Pageable pageable) {
		log.debug("Request to get all ProjectTemplates");
		return projectTemplatesRepository.findAll(pageable).map(projectTemplatesMapper::toDto);
	}

	/**
	 * Get one projectTemplates by id.
	 *
	 * @param id
	 *            the id of the entity
	 * @return the entity
	 */
	@Transactional(readOnly = true)
	public ProjectTemplatesDTO findOne(Long id) {
		log.debug("Request to get ProjectTemplates : {}", id);
		ProjectTemplates projectTemplates = projectTemplatesRepository.findOne(id);
		return projectTemplatesMapper.toDto(projectTemplates);
	}

	/**
	 * Delete the projectTemplates by id.
	 *
	 * @param id
	 *            the id of the entity
	 */
	public void delete(Long id) {
		log.debug("Request to delete ProjectTemplates : {}", id);
		projectTemplatesRepository.delete(id);
		projectTemplatesSearchRepository.delete(id);
	}

	/**
	 * Search for the projectTemplates corresponding to the query.
	 *
	 * @param query
	 *            the query of the search
	 * @param pageable
	 *            the pagination information
	 * @return the list of entities
	 */
	@Transactional(readOnly = true)
	public Page<ProjectTemplatesDTO> search(String query, Pageable pageable) {
		log.debug("Request to search for a page of ProjectTemplates for query {}", query);
		Page<ProjectTemplates> result = projectTemplatesSearchRepository.findByNameLike(query, pageable);
		return result.map(projectTemplatesMapper::toDto);
	}

	/**
	 * Create excel file
	 * 
	 * @param templateId
	 * @throws IOException
	 */
	public InputStreamResource exportExcel(Long templateId) throws IOException {
		ProjectWorkflowsCriteria criteria = new ProjectWorkflowsCriteria();
		LongFilter filter = new LongFilter();
		filter.setEquals(templateId);
		criteria.setProjectTemplatesId(filter);
		List<ProjectWorkflowsDTO> projectWorkflowsDTOs = this.projectWorkflowsQueryService.findByCriteria(criteria);
		//String[] columns = getColumns(projectWorkflowsDTOs);
		if (!CollectionUtils.isEmpty(projectWorkflowsDTOs)) {
			ByteArrayInputStream in = excelGenerator.taskManagementToExcel(projectWorkflowsDTOs, null);
			return new InputStreamResource(in);
		} else {
			return null;
		}
		
	}
	
	private String[] getColumns(List<ProjectWorkflowsDTO> projectWorkflowsDTOs) {
		List<String> columns = new ArrayList<>();
		for (ProjectWorkflowsDTO projectWorkflowsDTO : projectWorkflowsDTOs) {
			ObjectMapper mapper = new ObjectMapper();
			try {
				ArrayNode nodes = (ArrayNode)mapper.readTree(projectWorkflowsDTO.getPmGridDTO());
				for(int i = 1; i < nodes.size(); i ++) {
					columns.add(projectWorkflowsDTO.getName() + '_' + nodes.get(i).textValue());
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
}
