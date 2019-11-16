package fpt.dps.dtms.service.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import fpt.dps.dtms.domain.Projects;
import fpt.dps.dtms.domain.PurchaseOrders;
import fpt.dps.dtms.domain.Tasks;
import fpt.dps.dtms.repository.ProjectsRepository;
import fpt.dps.dtms.service.ProjectWorkflowsQueryService;
import fpt.dps.dtms.service.TMSCustomFieldScreenQueryService;
import fpt.dps.dtms.service.TMSCustomFieldScreenService;
import fpt.dps.dtms.service.TMSCustomFieldScreenValueQueryService;
import fpt.dps.dtms.service.dto.ProjectWorkflowsCriteria;
import fpt.dps.dtms.service.dto.ProjectWorkflowsDTO;
import fpt.dps.dtms.service.dto.TMSCustomFieldScreenDTO;
import fpt.dps.dtms.service.dto.TMSCustomFieldScreenValueDTO;
import fpt.dps.dtms.service.dto.TasksDTO;
import fpt.dps.dtms.web.rest.vm.FieldConfigVM;
import fpt.dps.dtms.web.rest.vm.TMSDynamicCustomFieldVM;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

@Service
public class FieldConfigService {
	private final ProjectsRepository projectsRepository;
	
	private final ProjectWorkflowsQueryService projectWorkflowsQueryService;
	
	private final TMSCustomFieldScreenQueryService tMSCustomFieldScreenQueryService;
	
	private final TMSCustomFieldScreenValueQueryService tMSCustomFieldScreenValueQueryService;
	
	private final TMSCustomFieldScreenService tMSCustomFieldScreenService;
	
	public FieldConfigService(ProjectsRepository projectsRepository, ProjectWorkflowsQueryService projectWorkflowsQueryService,
			TMSCustomFieldScreenQueryService tMSCustomFieldScreenQueryService, TMSCustomFieldScreenValueQueryService tMSCustomFieldScreenValueQueryService,
			TMSCustomFieldScreenService tMSCustomFieldScreenService) {
		this.projectsRepository = projectsRepository;
		this.projectWorkflowsQueryService = projectWorkflowsQueryService;
		this.tMSCustomFieldScreenQueryService = tMSCustomFieldScreenQueryService;
		this.tMSCustomFieldScreenValueQueryService = tMSCustomFieldScreenValueQueryService;
		this.tMSCustomFieldScreenService = tMSCustomFieldScreenService;
	}
	/*
	public  List<FieldConfigVM> getAllFieldConfig(Long id, String entity){
		ProjectWorkflowsDTO projectWorkflowsDTO = this.getProjectWorkflowDTO(id, entity);
		ObjectMapper mapper = new ObjectMapper();
		List<FieldConfigVM> fieldConfigVMs = null;
		try {
			fieldConfigVMs = mapper.readValue(projectWorkflowsDTO.getInputDTO(), new TypeReference<List<FieldConfigVM>>(){});
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
		return fieldConfigVMs;
	}
	*/
	
	public  TMSDynamicCustomFieldVM getAllFieldConfig(Long id, String entity){
		TMSDynamicCustomFieldVM tmsDynamicCustomFieldVM = new TMSDynamicCustomFieldVM();
		ProjectWorkflowsDTO projectWorkflowsDTO = this.getProjectWorkflowDTO(id, entity);
		ObjectMapper mapper = new ObjectMapper();
		List<FieldConfigVM> fieldConfigVMs = null;
		try {
			fieldConfigVMs = mapper.readValue(projectWorkflowsDTO.getInputDTO(), new TypeReference<List<FieldConfigVM>>(){});
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
		tmsDynamicCustomFieldVM.setFieldConfigVMs(fieldConfigVMs);
		tmsDynamicCustomFieldVM.setProcessId(projectWorkflowsDTO.getActivity());
		
		return tmsDynamicCustomFieldVM;
	}
	
	public TMSDynamicCustomFieldVM getAllDynamicFieldConfig(PurchaseOrders purchaseOrders, String entity){
		TMSDynamicCustomFieldVM tmsDynamicCustomFieldVM = new TMSDynamicCustomFieldVM();
		ProjectWorkflowsDTO projectWorkflowsDTO;
		
		if (purchaseOrders.getProjectTemplates() != null) {
			projectWorkflowsDTO = this.getPurchaseOrderWorkflowDTO(purchaseOrders, entity);
		} else {
			projectWorkflowsDTO = this.getProjectWorkflowDTO(purchaseOrders.getProject().getId() , entity);
		}
		
		List<TMSCustomFieldScreenDTO> tmsCustomFieldScreenDTOs = this.tMSCustomFieldScreenQueryService.getListTMSCustomFieldScreenByWFId(projectWorkflowsDTO.getId());
		ObjectMapper mapper = new ObjectMapper();
		List<FieldConfigVM> fieldConfigVMs = null;
		try {
			fieldConfigVMs = mapper.readValue(projectWorkflowsDTO.getInputDTO(), new TypeReference<List<FieldConfigVM>>(){});
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
		tmsDynamicCustomFieldVM.setFieldConfigVMs(fieldConfigVMs);
		tmsDynamicCustomFieldVM.setTmsCustomFieldScreenDTOs(tmsCustomFieldScreenDTOs);
		tmsDynamicCustomFieldVM.setProcessId(projectWorkflowsDTO.getActivity());
		return tmsDynamicCustomFieldVM;
	}
	
	public  List<FieldConfigVM> getAllFieldConfigOpGridDTO(Long id, String entity){
		ProjectWorkflowsDTO projectWorkflowsDTO = this.getProjectWorkflowDTO(id, entity);
		List<FieldConfigVM> fieldConfigVMs = new ArrayList<FieldConfigVM>();
		if(projectWorkflowsDTO != null) {
			ObjectMapper mapper = new ObjectMapper();
			try {
				fieldConfigVMs = mapper.readValue(projectWorkflowsDTO.getOpGridDTO(), new TypeReference<List<FieldConfigVM>>(){});
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
		}
		return fieldConfigVMs;
	}
	
	/**
	 * Get all field for screen user.
	 * @param id
	 * @param entity
	 * @return
	 */
	public  TMSDynamicCustomFieldVM getAllDynamicFieldConfigOP(Long id, String entity){
		TMSDynamicCustomFieldVM tmsDynamicCustomFieldVM = new TMSDynamicCustomFieldVM();
		ProjectWorkflowsDTO projectWorkflowsDTO = this.getProjectWorkflowDTO(id, entity);
		List<TMSCustomFieldScreenDTO> tmsCustomFieldScreenDTOs = this.tMSCustomFieldScreenQueryService.getListTMSCustomFieldScreenByWFId(projectWorkflowsDTO.getId());
		ObjectMapper mapper = new ObjectMapper();
		List<FieldConfigVM> fieldConfigVMs = null;
		try {
			fieldConfigVMs = mapper.readValue(projectWorkflowsDTO.getOpGridDTO(), new TypeReference<List<FieldConfigVM>>(){});
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
		tmsDynamicCustomFieldVM.setFieldConfigVMs(fieldConfigVMs);
		tmsDynamicCustomFieldVM.setTmsCustomFieldScreenDTOs(tmsCustomFieldScreenDTOs);
		tmsDynamicCustomFieldVM.setProcessId(projectWorkflowsDTO.getActivity());
		return tmsDynamicCustomFieldVM;
	}
	
	/**
	 * 	@author TuHP
	 *  Get ProjectWorkFlow by Project Id
	 *  @param projectId the Project Id
	 *  @return The ProjectWorkFlow
	 */
	private ProjectWorkflowsDTO getProjectWorkflowDTO(Long projectId, String entity) {
		ProjectWorkflowsDTO projectWorkflowsDTO = null;
		Projects project = projectsRepository.findOne(projectId);
		if(project != null) {
			ProjectWorkflowsCriteria criteria = new ProjectWorkflowsCriteria();
			LongFilter filter = new LongFilter();
			filter.setEquals(project.getProjectTemplates().getId());
			criteria.setProjectTemplatesId(filter);
			
			StringFilter nameFilter = new StringFilter();
			nameFilter.setEquals(entity);
			criteria.setName(nameFilter);
			
			List<ProjectWorkflowsDTO> projectWorkflowsDTOs = this.projectWorkflowsQueryService.findByCriteria(criteria);
			if(projectWorkflowsDTOs.size() > 0) {
				projectWorkflowsDTO = projectWorkflowsDTOs.get(0);
			}
		}
		
		return projectWorkflowsDTO;
	}
	
	/**
	 * 	@author TuHP
	 *  Get ProjectWorkFlow by Project Id
	 *  @param projectId the Project Id
	 *  @return The ProjectWorkFlow
	 */
	private ProjectWorkflowsDTO getPurchaseOrderWorkflowDTO(PurchaseOrders purchaseOrders, String entity) {
		ProjectWorkflowsDTO projectWorkflowsDTO = null;
			ProjectWorkflowsCriteria criteria = new ProjectWorkflowsCriteria();
			LongFilter filter = new LongFilter();
			filter.setEquals(purchaseOrders.getProjectTemplates().getId());
			criteria.setProjectTemplatesId(filter);
			
			StringFilter nameFilter = new StringFilter();
			nameFilter.setEquals(entity);
			criteria.setName(nameFilter);
			
			List<ProjectWorkflowsDTO> projectWorkflowsDTOs = this.projectWorkflowsQueryService.findByCriteria(criteria);
			if(projectWorkflowsDTOs.size() > 0) {
				projectWorkflowsDTO = projectWorkflowsDTOs.get(0);
		}
		
		return projectWorkflowsDTO;
	}
	
	/**
	 * Map data with name of field -> Task
	 * @param tasks
	 * @return
	 */
	public List<Map<String, Object>> MapDataWithNameOfField(TasksDTO taskDTOs) {
		List<Map<String, Object>> result = new ArrayList<Map<String,Object>>();
		List<TMSCustomFieldScreenValueDTO> tmsCustomFieldScreenValueDTOs = tMSCustomFieldScreenValueQueryService.getAllTMSCustomFieldScreenValueByTaskId(taskDTOs.getId());
		for (TMSCustomFieldScreenValueDTO customField: tmsCustomFieldScreenValueDTOs) {
			Map<String, Object> field = new HashMap<String, Object>();
			TMSCustomFieldScreenDTO tmsCustomFieldScreenDTO = tMSCustomFieldScreenService.findOne(customField.getTmsCustomFieldScreenId());
			ObjectMapper mapper = new ObjectMapper();
			try {
				ArrayNode nodes = (ArrayNode) mapper.readTree(tmsCustomFieldScreenDTO.getEntityGridPm());
				for (int i = 0; i < nodes.size(); i++) {
					String colName = nodes.get(i).textValue();
					if (customField.getValue() != null) {
						field.put(colName, customField.getValue());
					} else {
						field.put(colName, customField.getText());
					}
				}
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			result.add(field);
		}
		return result;
	}
}
