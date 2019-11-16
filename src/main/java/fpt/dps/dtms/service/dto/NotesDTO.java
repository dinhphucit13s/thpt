package fpt.dps.dtms.service.dto;


import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the Notes entity.
 */
public class NotesDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    @Lob
    private String description;

    private Long tasksId;

    private String tasksName;

    private Long bugId;
    
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Long getBugId() {
        return bugId;
    }

    public void setBugId(Long bugsId) {
        this.bugId = bugsId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        NotesDTO notesDTO = (NotesDTO) o;
        if(notesDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), notesDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "NotesDTO{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
