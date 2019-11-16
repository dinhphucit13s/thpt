package fpt.dps.dtms.service.dto;


import javax.validation.constraints.*;

import fpt.dps.dtms.domain.TMSCustomFieldScreen;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the ProjectWorkflows entity.
 */
public class ProjectWorkflowsDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String name;

    @NotNull
    private Integer step;

    @NotNull
    @Lob
    private String entityDTO;

    @NotNull
    @Lob
    private String inputDTO;

    @NotNull
    @Lob
    private String opGridDTO;

    @NotNull
    @Lob
    private String pmGridDTO;

    @Size(max = 255)
    private String nextURI;

    @Lob
    private String description;

    private String activity;

    private Long projectTemplatesId;

	private String projectTemplatesName;

	private List<TMSCustomFieldScreenDTO> tmsCustomFields;

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

    public Integer getStep() {
        return step;
    }

    public void setStep(Integer step) {
        this.step = step;
    }

    public String getEntityDTO() {
		return entityDTO;
	}

	public void setEntityDTO(String entityDTO) {
		this.entityDTO = entityDTO;
	}

	public String getInputDTO() {
        return inputDTO;
    }

    public void setInputDTO(String inputDTO) {
        this.inputDTO = inputDTO;
    }

    public String getOpGridDTO() {
        return opGridDTO;
    }

    public void setOpGridDTO(String opGridDTO) {
        this.opGridDTO = opGridDTO;
    }

    public String getPmGridDTO() {
        return pmGridDTO;
    }

    public void setPmGridDTO(String pmGridDTO) {
        this.pmGridDTO = pmGridDTO;
    }

    public String getNextURI() {
        return nextURI;
    }

    public void setNextURI(String nextURI) {
        this.nextURI = nextURI;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
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

    public List<TMSCustomFieldScreenDTO> getTmsCustomFields() {
		return tmsCustomFields;
	}

	public void setTmsCustomFields(List<TMSCustomFieldScreenDTO> tmsCustomFields) {
		this.tmsCustomFields = tmsCustomFields;
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ProjectWorkflowsDTO projectWorkflowsDTO = (ProjectWorkflowsDTO) o;
        if(projectWorkflowsDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), projectWorkflowsDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ProjectWorkflowsDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", step=" + getStep() +
            ", entityDTO='" + getEntityDTO() + "'" +
            ", inputDTO='" + getInputDTO() + "'" +
            ", opGridDTO='" + getOpGridDTO() + "'" +
            ", pmGridDTO='" + getPmGridDTO() + "'" +
            ", nextURI='" + getNextURI() + "'" +
            ", description='" + getDescription() + "'" +
            ", activity='" + getActivity() + "'" +
            "}";
    }
}
