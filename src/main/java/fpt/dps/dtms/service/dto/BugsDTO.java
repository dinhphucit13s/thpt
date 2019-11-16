package fpt.dps.dtms.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import fpt.dps.dtms.domain.enumeration.BugStatus;
import fpt.dps.dtms.domain.enumeration.BugResolution;

/**
 * A DTO for the Bugs entity.
 */
/**
 * @author CongHK
 *
 */
public class BugsDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 10)
    @Lob
    private String description;

    @NotNull
    @Size(min = 2)
    private String code;
    
    private Integer iteration;

    private String stage;

    @NotNull
    private BugStatus status;

    @NotNull
    private BugResolution resolution;

    private String physicalPath;

    private Long tasksId;

    private String tasksName;
    
    private Set<NotesDTO> notes = new HashSet<>();
    
    private Set<AttachmentsDTO> attachments = new HashSet<>();
    
    private Set<AttachmentsDTO> attachmentsAppend = new HashSet<>();
    
    private Set<AttachmentsDTO> attachmentsRemove = new HashSet<>();
    
	/**
	 * @return the attachmentsAppend
	 */
	public Set<AttachmentsDTO> getAttachmentsAppend() {
		return attachmentsAppend;
	}

	/**
	 * @param attachmentsAppend the attachmentsAppend to set
	 */
	public void setAttachmentsAppend(Set<AttachmentsDTO> attachmentsAppend) {
		this.attachmentsAppend = attachmentsAppend;
	}

	/**
	 * @return the attachmentsRemove
	 */
	public Set<AttachmentsDTO> getAttachmentsRemove() {
		return attachmentsRemove;
	}

	/**
	 * @param attachmentsRemove the attachmentsRemove to set
	 */
	public void setAttachmentsRemove(Set<AttachmentsDTO> attachmentsRemove) {
		this.attachmentsRemove = attachmentsRemove;
	}

	public Set<AttachmentsDTO> getAttachments() {
		return attachments;
	}

	public void setAttachments(Set<AttachmentsDTO> attachments) {
		this.attachments = attachments;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<NotesDTO> getNotes() {
		return notes;
	}

	public void setNotes(Set<NotesDTO> notes) {
		this.notes = notes;
	}

	public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }
    
	public Integer getIteration() {
		return iteration != null ? iteration : 1;
	}

	public void setIteration(Integer iteration) {
		this.iteration = iteration != null ? iteration : 1;
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

    public String getPhysicalPath() {
        return physicalPath;
    }

    public void setPhysicalPath(String physicalPath) {
        this.physicalPath = physicalPath;
    }

    public Long getTasksId() {
        return tasksId;
    }

    public void setTasksId(Long tasksId) {
        this.tasksId = tasksId;
    }

    public String getTasksName() {
        return tasksName;
    }

    public void setTasksName(String tasksName) {
        this.tasksName = tasksName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BugsDTO bugsDTO = (BugsDTO) o;
        if(bugsDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), bugsDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "BugsDTO{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", code='" + getCode() + "'" +
            ", stage='" + getStage() + "'" +
            ", status='" + getStatus() + "'" +
            ", resolution='" + getResolution() + "'" +
            ", physicalPath='" + getPhysicalPath() + "'" +
            "}";
    }
}
