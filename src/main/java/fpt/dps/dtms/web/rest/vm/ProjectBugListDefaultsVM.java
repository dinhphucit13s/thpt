package fpt.dps.dtms.web.rest.vm;

import java.util.HashSet;
import java.util.Set;

import fpt.dps.dtms.service.dto.BugListDefaultDTO;

public class ProjectBugListDefaultsVM {
	private Long projectId;
	private Set<BugListDefaultDTO> bugListDefaultList = new HashSet<>();
	
	public Long getProjectId() {
		return projectId;
	}
	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}
	public Set<BugListDefaultDTO> getBugListDefaultList() {
		return bugListDefaultList;
	}
	public void setBugListDefaultList(Set<BugListDefaultDTO> bugListDefaultList) {
		this.bugListDefaultList = bugListDefaultList;
	}
}
