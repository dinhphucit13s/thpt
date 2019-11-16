package fpt.dps.dtms.web.rest.vm.external;

import java.util.Set;

import fpt.dps.dtms.domain.enumeration.BugResolution;
import fpt.dps.dtms.domain.enumeration.BugStatus;
import fpt.dps.dtms.service.dto.AttachmentsDTO;
import fpt.dps.dtms.service.dto.NotesDTO;

public class BugAttachmentVM {
	private Long id;
	private String code;
	private String description;
	private Integer iteration;
	private String stage;
	private String physicalPath;
	private BugStatus status;
	private BugResolution resolution;
	private Set<AttachmentsDTO> attachments;
	private Set<NotesDTO> notes;
	public Set<NotesDTO> getNotes() {
		return notes;
	}
	public void setNotes(Set<NotesDTO> notes) {
		this.notes = notes;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getIteration() {
		return iteration != null ? iteration : 1;
	}
	public void setIteration(Integer iteration) {
		this.iteration = iteration != null ? iteration : 1;
	}
	public Set<AttachmentsDTO> getAttachments() {
		return attachments;
	}
	public void setAttachments(Set<AttachmentsDTO> attachments) {
		this.attachments = attachments;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public BugStatus getStatus() {
		return status;
	}
	public void setStatus(BugStatus status) {
		this.status = status;
	}
	public BugResolution getResolution() {
		return resolution;
	}
	public void setResolution(BugResolution resolution) {
		this.resolution = resolution;
	}
	public String getStage() {
		return stage;
	}
	public void setStage(String stage) {
		this.stage = stage;
	}
	public String getPhysicalPath() {
		return physicalPath;
	}
	public void setPhysicalPath(String physicalPath) {
		this.physicalPath = physicalPath;
	}
	
	
}
