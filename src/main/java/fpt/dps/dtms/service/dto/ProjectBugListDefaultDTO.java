package fpt.dps.dtms.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the ProjectBugListDefault entity.
 */
public class ProjectBugListDefaultDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 2)
    private String code;

    private Long projectId;

    private String projectName;

    private Long bugListDefaultId;

    private String bugListDefaultDescription;

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

    public Long getBugListDefaultId() {
        return bugListDefaultId;
    }

    public void setBugListDefaultId(Long bugListDefaultId) {
        this.bugListDefaultId = bugListDefaultId;
    }

    public String getBugListDefaultDescription() {
        return bugListDefaultDescription;
    }

    public void setBugListDefaultDescription(String bugListDefaultDescription) {
        this.bugListDefaultDescription = bugListDefaultDescription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ProjectBugListDefaultDTO projectBugListDefaultDTO = (ProjectBugListDefaultDTO) o;
        if(projectBugListDefaultDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), projectBugListDefaultDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ProjectBugListDefaultDTO{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            "}";
    }
}
