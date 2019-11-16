package fpt.dps.dtms.domain;

import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * The Projects entity.
 */
@ApiModel(description = "The Projects entity.")
@Entity
@Table(name = "project_workflows")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "projectworkflows")
public class ProjectWorkflows extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @NotNull
    @Column(name = "step", nullable = false)
    private Integer step;
    
    @NotNull
    @Lob
    @Column(name = "entity_dto", nullable = false)
    private String entityDTO;

    @NotNull
    @Lob
    @Column(name = "input_dto", nullable = false)
    private String inputDTO;

    @NotNull
    @Lob
    @Column(name = "op_grid_dto", nullable = false)
    private String opGridDTO;

    @NotNull
    @Lob
    @Column(name = "pm_grid_dto", nullable = false)
    private String pmGridDTO;

    @Size(max = 255)
    @Column(name = "next_uri", length = 255)
    private String nextURI;

    @Lob
    @Column(name = "description")
    private String description;

    @Column(name = "activity")
    private String activity;

    @ManyToOne
    private ProjectTemplates projectTemplates;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public ProjectWorkflows name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStep() {
        return step;
    }

    public ProjectWorkflows step(Integer step) {
        this.step = step;
        return this;
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

    public ProjectWorkflows inputDTO(String inputDTO) {
        this.inputDTO = inputDTO;
        return this;
    }

    public void setInputDTO(String inputDTO) {
        this.inputDTO = inputDTO;
    }

    public String getOpGridDTO() {
        return opGridDTO;
    }

    public ProjectWorkflows opGridDTO(String opGridDTO) {
        this.opGridDTO = opGridDTO;
        return this;
    }

    public void setOpGridDTO(String opGridDTO) {
        this.opGridDTO = opGridDTO;
    }

    public String getPmGridDTO() {
        return pmGridDTO;
    }

    public ProjectWorkflows pmGridDTO(String pmGridDTO) {
        this.pmGridDTO = pmGridDTO;
        return this;
    }

    public void setPmGridDTO(String pmGridDTO) {
        this.pmGridDTO = pmGridDTO;
    }

    public String getNextURI() {
        return nextURI;
    }

    public ProjectWorkflows nextURI(String nextURI) {
        this.nextURI = nextURI;
        return this;
    }

    public void setNextURI(String nextURI) {
        this.nextURI = nextURI;
    }

    public String getDescription() {
        return description;
    }

    public ProjectWorkflows description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getActivity() {
        return activity;
    }

    public ProjectWorkflows activity(String activity) {
        this.activity = activity;
        return this;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public ProjectTemplates getProjectTemplates() {
        return projectTemplates;
    }

    public ProjectWorkflows projectTemplates(ProjectTemplates projectTemplates) {
        this.projectTemplates = projectTemplates;
        return this;
    }

    public void setProjectTemplates(ProjectTemplates projectTemplates) {
        this.projectTemplates = projectTemplates;
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
        ProjectWorkflows projectWorkflows = (ProjectWorkflows) o;
        if (projectWorkflows.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), projectWorkflows.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ProjectWorkflows{" +
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
