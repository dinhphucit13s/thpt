package fpt.dps.dtms.service.dto;


import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import javax.persistence.Lob;
import fpt.dps.dtms.domain.enumeration.PurchaseOrderStatus;

/**
 * A DTO for the PurchaseOrders entity.
 */
public class PurchaseOrdersDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String name;

    private PurchaseOrderStatus status;

    private Instant startTime;

    private Instant endTime;

    @Lob
    private String description;
    
    private String reviewRatio;

    private Long projectId;

    private String projectName;

    private Long projectTemplatesId;

    private String projectTemplatesName;

    private Long purchaseOrderLeadId;

    private String purchaseOrderLeadUserLogin;
    
    private Boolean isActiveOnAdmin;
    
    private DtmsMonitoringDTO dtmsMonitoringProject;
    
    private String[] watcherUsersPO;
    
    private String[] dedicatedUsersPO;
    
    public Long getProjectTemplatesId() {
		return projectTemplatesId;
	}

	public void setProjectTemplatesId(Long projectTemplatesId) {
		this.projectTemplatesId = projectTemplatesId;
	}

	public String getProjectTemplatesName() {
		return projectTemplatesName;
	}

	public void setProjectTemplatesName(String projectTemplatesName) {
		this.projectTemplatesName = projectTemplatesName;
	}

	public Boolean getIsActiveOnAdmin() {
    	return isActiveOnAdmin;
    }
    
    public void setIsActiveOnAdmin(Boolean isActiveOnAdmin) {
    	this.isActiveOnAdmin = isActiveOnAdmin;
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

    public PurchaseOrderStatus getStatus() {
        return status;
    }

    public void setStatus(PurchaseOrderStatus status) {
        this.status = status;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public Instant getEndTime() {
        return endTime;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReviewRatio() {
		return reviewRatio;
	}

	public void setReviewRatio(String reviewRatio) {
		this.reviewRatio = reviewRatio;
	}

	public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectsId) {
        this.projectId = projectsId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectsName) {
        this.projectName = projectsName;
    }

    public Long getPurchaseOrderLeadId() {
        return purchaseOrderLeadId;
    }

    public void setPurchaseOrderLeadId(Long projectUsersId) {
        this.purchaseOrderLeadId = projectUsersId;
    }

    public String getPurchaseOrderLeadUserLogin() {
        return purchaseOrderLeadUserLogin;
    }

    public void setPurchaseOrderLeadUserLogin(String projectUsersUserLogin) {
        this.purchaseOrderLeadUserLogin = projectUsersUserLogin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PurchaseOrdersDTO purchaseOrdersDTO = (PurchaseOrdersDTO) o;
        if(purchaseOrdersDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), purchaseOrdersDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PurchaseOrdersDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", status='" + getStatus() + "'" +
            ", startTime='" + getStartTime() + "'" +
            ", endTime='" + getEndTime() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }

	public String[] getWatcherUsersPO() {
		return watcherUsersPO;
	}

	public void setWatcherUsersPO(String[] watcherUsersPO) {
		this.watcherUsersPO = watcherUsersPO;
	}

	public String[] getDedicatedUsersPO() {
		return dedicatedUsersPO;
	}

	public void setDedicatedUsersPO(String[] dedicatedUsersPO) {
		this.dedicatedUsersPO = dedicatedUsersPO;
	}

	public DtmsMonitoringDTO getDtmsMonitoringProject() {
		return dtmsMonitoringProject;
	}

	public void setDtmsMonitoringProject(DtmsMonitoringDTO dtmsMonitoringProject) {
		this.dtmsMonitoringProject = dtmsMonitoringProject;
	}

}
