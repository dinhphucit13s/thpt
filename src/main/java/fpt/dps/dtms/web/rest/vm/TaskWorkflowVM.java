package fpt.dps.dtms.web.rest.vm;

import java.util.List;

public class TaskWorkflowVM {
	private String processKey;
	
	private String image;
	
	private List<String> workflowItems;

	public String getProcessKey() {
		return processKey;
	}

	public void setProcessKey(String processKey) {
		this.processKey = processKey;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public List<String> getWorkflowItems() {
		return workflowItems;
	}

	public void setWorkflowItems(List<String> workflowItems) {
		this.workflowItems = workflowItems;
	}
	
}
