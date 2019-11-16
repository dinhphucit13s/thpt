package fpt.dps.dtms.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import fpt.dps.dtms.domain.enumeration.IssueStatus;

/**
 * A DTO for the Issues entity.
 */
public class IssuesDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String name;

    @Size(max = 1000)
    private String description;

    private IssueStatus status;

    private Long purchaseOrderId;

    private String purchaseOrderName;

    private Long projectsId;

    private String projectsName;
    
    private Set<AttachmentsDTO> attachments = new HashSet<>();
    
    private Set<AttachmentsDTO> attachmentsAppend = new HashSet<>();
    
    private Set<AttachmentsDTO> attachmentsRemove = new HashSet<>();

    public Set<AttachmentsDTO> getAttachments() {
		return attachments;
	}

	public void setAttachments(Set<AttachmentsDTO> attachments) {
		this.attachments = attachments;
	}

	public Set<AttachmentsDTO> getAttachmentsAppend() {
		return attachmentsAppend;
	}

	public void setAttachmentsAppend(Set<AttachmentsDTO> attachmentsAppend) {
		this.attachmentsAppend = attachmentsAppend;
	}

	public Set<AttachmentsDTO> getAttachmentsRemove() {
		return attachmentsRemove;
	}

	public void setAttachmentsRemove(Set<AttachmentsDTO> attachmentsRemove) {
		this.attachmentsRemove = attachmentsRemove;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public IssueStatus getStatus() {
        return status;
    }

    public void setStatus(IssueStatus status) {
        this.status = status;
    }

    public Long getPurchaseOrderId() {
        return purchaseOrderId;
    }

    public void setPurchaseOrderId(Long purchaseOrdersId) {
        this.purchaseOrderId = purchaseOrdersId;
    }

    public String getPurchaseOrderName() {
        return purchaseOrderName;
    }

    public void setPurchaseOrderName(String purchaseOrdersName) {
        this.purchaseOrderName = purchaseOrdersName;
    }

    public Long getProjectsId() {
        return projectsId;
    }

    public void setProjectsId(Long projectsId) {
        this.projectsId = projectsId;
    }

    public String getProjectsName() {
        return projectsName;
    }

    public void setProjectsName(String projectsName) {
        this.projectsName = projectsName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        IssuesDTO issuesDTO = (IssuesDTO) o;
        if(issuesDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), issuesDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "IssuesDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
