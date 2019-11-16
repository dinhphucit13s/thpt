package fpt.dps.dtms.service.dto;


import javax.persistence.Column;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import fpt.dps.dtms.domain.enumeration.ProjectRoles;

/**
 * A DTO for the ProjectUsers entity.
 */
public class ProjectUsersDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    @Size(max = 50)
    private String userLogin;

    private ProjectRoles roleName;
    
    private LocalDate startDate; 

    private LocalDate endDate;
    
    private Float effortPlan;

    private Long projectId;

    private String projectName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public ProjectRoles getRoleName() {
        return roleName;
    }

    public void setRoleName(ProjectRoles roleName) {
        this.roleName = roleName;
    }

    public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}
	
	public Float getEffortPlan() {
		return effortPlan;
	}

	public void setEffortPlan(Float effortPlan) {
		this.effortPlan = effortPlan;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ProjectUsersDTO projectUsersDTO = (ProjectUsersDTO) o;
        if(projectUsersDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), projectUsersDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ProjectUsersDTO{" +
            "id=" + getId() +
            ", userLogin='" + getUserLogin() + "'" +
            ", roleName='" + getRoleName() + "'" +
            "}";
    }
}
