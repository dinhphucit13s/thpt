package fpt.dps.dtms.web.rest.vm.external;

import java.util.Set;

public class LogBugVM {
	private Long taskId;
	private Set<BugAttachmentVM> bugAttachments;
	
	public Long getTaskId() {
		return taskId;
	}
	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}
	public Set<BugAttachmentVM> getBugAttachments() {
		return bugAttachments;
	}
	public void setBugAttachments(Set<BugAttachmentVM> bugAttachments) {
		this.bugAttachments = bugAttachments;
	}
}
