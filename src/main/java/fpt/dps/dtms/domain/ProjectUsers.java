package fpt.dps.dtms.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;

import com.fasterxml.jackson.annotation.JsonBackReference;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import fpt.dps.dtms.domain.enumeration.ProjectRoles;

/**
 * A ProjectUsers.
 */
@Entity
@Table(name = "project_users")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "projectusers")
public class ProjectUsers extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 50)
    @Column(name = "user_login", length = 50)
    private String userLogin;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_name")
    private ProjectRoles roleName;
    
    @Column(name = "start_date")
    private LocalDate startDate; 

    @Column(name = "end_date")
    private LocalDate endDate;
    
    @Column(name = "effort_plan")
    private Float effortPlan;
    
	@ManyToOne
    @JsonBackReference
    private Projects project;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public ProjectUsers userLogin(String userLogin) {
        this.userLogin = userLogin;
        return this;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public ProjectRoles getRoleName() {
        return roleName;
    }

    public ProjectUsers roleName(ProjectRoles roleName) {
        this.roleName = roleName;
        return this;
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
	
	public Projects getProject() {
        return project;
    }

    public ProjectUsers project(Projects projects) {
        this.project = projects;
        return this;
    }

    public void setProject(Projects projects) {
        this.project = projects;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProjectUsers projectUsers = (ProjectUsers) o;
        if (projectUsers.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), projectUsers.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ProjectUsers{" +
            "id=" + getId() +
            ", userLogin='" + getUserLogin() + "'" +
            ", roleName='" + getRoleName() + "'" +
            "}";
    }
}
