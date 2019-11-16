package fpt.dps.dtms.service.dto;


import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import javax.persistence.Lob;
import fpt.dps.dtms.domain.enumeration.ProjectType;
import fpt.dps.dtms.domain.enumeration.ProjectStatus;

/**
 * A DTO for the Projects entity.
 */
public class ProjectsDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 20)
    private String code;

    @NotNull
    @Size(max = 100)
    private String name;

    private ProjectType type;

    @NotNull
    private ProjectStatus status;

    private Instant startTime;

    private Instant endTime;
    
    private Integer biddingHoldTime;

	@Lob
    private String description;

    private Long projectTemplatesId;

    private String projectTemplatesName;

    private Long projectLeadId;

    private String projectLeadUserLogin;

    private Long customerId;

    private String customerName;

    private boolean hasDoingTask;

    private Long businessUnitId;

    private String businessUnitName;
    
    private String[] watcherUsers;
    
    private String[] dedicatedUsers;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProjectType getType() {
        return type;
    }

    public void setType(ProjectType type) {
        this.type = type;
    }

    public ProjectStatus getStatus() {
        return status;
    }

    public void setStatus(ProjectStatus status) {
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
    
    public Integer getBiddingHoldTime() {
		return biddingHoldTime;
	}

	public void setBiddingHoldTime(Integer biddingHoldTime) {
		this.biddingHoldTime = biddingHoldTime;
	}

	public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

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

    public Long getProjectLeadId() {
        return projectLeadId;
    }

    public void setProjectLeadId(Long projectUsersId) {
        this.projectLeadId = projectUsersId;
    }

    public String getProjectLeadUserLogin() {
        return projectLeadUserLogin;
    }

    public void setProjectLeadUserLogin(String projectUsersUserLogin) {
        this.projectLeadUserLogin = projectUsersUserLogin;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public boolean isHasDoingTask() {
        return hasDoingTask;
    }

    public void setHasDoingTask(boolean hasDoingTask) {
        this.hasDoingTask = hasDoingTask;
    }

    public Long getBusinessUnitId() {
        return businessUnitId;
    }

    public void setBusinessUnitId(Long businessUnitId) {
        this.businessUnitId = businessUnitId;
    }

    public String getBusinessUnitName() {
        return businessUnitName;
    }

    public void setBusinessUnitName(String businessUnitName) {
        this.businessUnitName = businessUnitName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ProjectsDTO projectsDTO = (ProjectsDTO) o;
        if(projectsDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), projectsDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ProjectsDTO{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", type='" + getType() + "'" +
            ", status='" + getStatus() + "'" +
            ", startTime='" + getStartTime() + "'" +
            ", endTime='" + getEndTime() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }

	public String[] getWatcherUsers() {
		return watcherUsers;
	}

	public void setWatcherUsers(String[] watcherUsers) {
		this.watcherUsers = watcherUsers;
	}

	public String[] getDedicatedUsers() {
		return dedicatedUsers;
	}

	public void setDedicatedUsers(String[] dedicatedUsers) {
		this.dedicatedUsers = dedicatedUsers;
	}
}
