package fpt.dps.dtms.web.rest.vm.external;

import java.util.HashSet;
import java.util.Set;

import fpt.dps.dtms.service.dto.AttachmentsDTO;
import fpt.dps.dtms.service.dto.TmsPostDTO;

public class QuestionAndAnswerVM {
	private Long id;
	private String title;
	private String status;
	private Long projectsId;
	private Long assigneeId;
	private Set<TmsPostDTO> posts = new HashSet<>();
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * @return the posts
	 */
	public Set<TmsPostDTO> getPosts() {
		return posts;
	}
	/**
	 * @param posts the posts to set
	 */
	public void setPosts(Set<TmsPostDTO> posts) {
		this.posts = posts;
	}
	private Set<AttachmentsDTO> attachments = new HashSet<>();
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * @return the projectId
	 */
	public Long getProjectsId() {
		return projectsId;
	}
	/**
	 * @param projectId the projectId to set
	 */
	public void setProjectsId(Long projectsId) {
		this.projectsId = projectsId;
	}
	/**
	 * @return the assigneeId
	 */
	public Long getAssigneeId() {
		return assigneeId;
	}
	/**
	 * @param assigneeId the assigneeId to set
	 */
	public void setAssigneeId(Long assigneeId) {
		this.assigneeId = assigneeId;
	} 
	/**
	 * @return the attachments
	 */
	public Set<AttachmentsDTO> getAttachments() {
		return attachments;
	}
	/**
	 * @param attachments the attachments to set
	 */
	public void setAttachments(Set<AttachmentsDTO> attachments) {
		this.attachments = attachments;
	}
}
