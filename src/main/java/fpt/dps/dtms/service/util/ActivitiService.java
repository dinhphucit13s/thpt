package fpt.dps.dtms.service.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import fpt.dps.dtms.web.rest.vm.TaskWorkflowVM;

/**
 * Service Implementation for managing Tasks.
 */
@Service
public class ActivitiService {

	private final Logger log = LoggerFactory.getLogger(ActivitiService.class);

	private final RuntimeService runtimeService;

	private final TaskService taskService;

	private final HistoryService historyService;

	private final ProcessEngine processEngine;

	public ActivitiService(RuntimeService runtimeService, TaskService taskService, HistoryService historyService,
			ProcessEngine processEngine) {
		this.runtimeService = runtimeService;
		this.taskService = taskService;
		this.historyService = historyService;
		this.processEngine = processEngine;
	}

	/**
	 * Get all deployed process flow in the system 
	 * @return
	 * @throws Exception
	 * @author TuHP
	 */
	public List<TaskWorkflowVM> getDeploymentResources() throws Exception {
		List<TaskWorkflowVM> taskWorkflowVMs = new ArrayList<TaskWorkflowVM>();
		RepositoryService repositoryService = processEngine.getRepositoryService();
		List<ProcessDefinition> definitions = repositoryService.createProcessDefinitionQuery().list();
		TaskWorkflowVM taskWorkflowVM;
		List<String> checkExistingProcess = new ArrayList<String>();
		for (ProcessDefinition processDefinition : definitions) {
			String processDefinitionKey = processDefinition.getKey();
			if (!checkExistingProcess.contains(processDefinitionKey)) {
				taskWorkflowVM = new TaskWorkflowVM();
				taskWorkflowVM.setProcessKey(processDefinitionKey);
				taskWorkflowVM.setImage(getWorkflowImage(processDefinition.getId(), "image"));
				taskWorkflowVM.setWorkflowItems(getWorkflowItems(processDefinition.getId()));
				taskWorkflowVMs.add(taskWorkflowVM);
				checkExistingProcess.add(processDefinitionKey);
			}
		}
		return taskWorkflowVMs;
	}

	/**
	 * Get all running process instances
	 * @param processDefinitionName
	 * @return
	 * @author TuHP
	 */
	public List<ProcessInstance> getAllRunningProcessInstances(String processDefinitionName) {
		// get process engine and services
		RuntimeService runtimeService = processEngine.getRuntimeService();
		RepositoryService repositoryService = processEngine.getRepositoryService();

		// query for latest process definition with given name
		ProcessDefinition myProcessDefinition = repositoryService.createProcessDefinitionQuery()
				.processDefinitionName(processDefinitionName).latestVersion().singleResult();

		// list all running/unsuspended instances of the process
		List<ProcessInstance> processInstances = runtimeService.createProcessInstanceQuery()
				.processDefinitionId(myProcessDefinition.getId()).active() // we only want the unsuspended process
																			// instances
				.list();

		return processInstances;
	}

	/**
	 * Get all process images which corresponding to each process flow
	 * @param procDefId
	 * @param resType
	 * @return
	 * @throws Exception
	 * @author TuHP
	 */
	public String getWorkflowImage(String procDefId, String resType) throws Exception {
		RepositoryService repositoryService = processEngine.getRepositoryService();
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
				.processDefinitionId(procDefId).singleResult();

		String resourceName = "";
		if (resType.equals("image")) {
			resourceName = processDefinition.getDiagramResourceName();
		} else if (resType.equals("xml")) {
			resourceName = processDefinition.getResourceName();
		}

		InputStream resourceAsStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(),
				resourceName);

		byte[] bytes = IOUtils.toByteArray(resourceAsStream);
		String imageStr = Base64.encodeBase64String(bytes);
		return imageStr;
	}
	
	/**
	 * Get all items(step) which belong to a process flow
	 * @param procDefId
	 * @return
	 * @throws Exception
	 * @author TuHP
	 */
	public List<String> getWorkflowItems(String procDefId) throws Exception {
		RepositoryService repositoryService = processEngine.getRepositoryService();
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
				.processDefinitionId(procDefId).singleResult();

		String resourceName = processDefinition.getResourceName();
		return getItems(resourceName);
	}

	/**
	 * Get a process image which described status of a processing flow 
	 * @param procDefId
	 * @param proInsId
	 * @param resType
	 * @return
	 * @throws Exception
	 * @author TuHP
	 */
	public String getWorkflowImageByProcessInstant(String procDefId, String proInsId, String resType) throws Exception {

		if (StringUtils.isBlank(procDefId)) {
			ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(proInsId)
					.singleResult();
			procDefId = processInstance.getProcessDefinitionId();
		}
		RepositoryService repositoryService = processEngine.getRepositoryService();
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
				.processDefinitionId(procDefId).singleResult();

		String resourceName = "";
		if (resType.equals("image")) {
			resourceName = processDefinition.getDiagramResourceName();
		} else if (resType.equals("xml")) {
			resourceName = processDefinition.getResourceName();
		}

		InputStream resourceAsStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(),
				resourceName);

		byte[] bytes = IOUtils.toByteArray(resourceAsStream);
		String imageStr = Base64.encodeBase64String(bytes);
		return imageStr;
	}

	/**
	 * Delete a process instance.
	 * @param procInsId
	 * @return
	 * @throws Exception
	 * @author TuHP
	 */
	public HistoricProcessInstance deleteProcessInstance(String procInsId) throws Exception {
		HistoricProcessInstance deletedInstance = historyService.createHistoricProcessInstanceQuery()
				.processInstanceId(procInsId).singleResult();

		if (deletedInstance == null) {
			throw new Exception(ActivitiConstants.ERR_DELETE_UNEXISTING_PROCESSINSTANCE);
		}
		historyService.deleteHistoricProcessInstance(deletedInstance.getId());
		return deletedInstance;
	}
	
	/**
	 * Get all items (step) in a process flow
	 * @param physicalFile
	 * @return
	 * @author TuHP
	 */
	private List<String> getItems(String physicalFile){
		List<String> workflowItems = null;
		SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
		try {
			SAXParser saxParser = saxParserFactory.newSAXParser();
	        XMLHandler handler = new XMLHandler();
	        saxParser.parse(new File(physicalFile), handler);
	        workflowItems = handler.getWorkflowItems();
	    } catch (ParserConfigurationException | SAXException | IOException e) {
	        e.printStackTrace();
	    }
		
		return workflowItems;
	}
}
