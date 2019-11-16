package fpt.dps.dtms.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A ProjectBugListDefault.
 */
@Entity
@Table(name = "project_bug_list_default")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "projectbuglistdefault")
public class ProjectBugListDefault extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 2)
    @Column(name = "code", nullable = false)
    private String code;

    @ManyToOne(optional = false)
    @NotNull
    private Projects project;

    @ManyToOne(optional = false)
    @NotNull
    private BugListDefault bugListDefault;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public ProjectBugListDefault code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Projects getProject() {
        return project;
    }

    public ProjectBugListDefault project(Projects projects) {
        this.project = projects;
        return this;
    }

    public void setProject(Projects projects) {
        this.project = projects;
    }

    public BugListDefault getBugListDefault() {
        return bugListDefault;
    }

    public ProjectBugListDefault bugListDefault(BugListDefault bugListDefault) {
        this.bugListDefault = bugListDefault;
        return this;
    }

    public void setBugListDefault(BugListDefault bugListDefault) {
        this.bugListDefault = bugListDefault;
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
        ProjectBugListDefault projectBugListDefault = (ProjectBugListDefault) o;
        if (projectBugListDefault.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), projectBugListDefault.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ProjectBugListDefault{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            "}";
    }
}
