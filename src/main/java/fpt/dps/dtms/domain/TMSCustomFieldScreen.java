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
 * The TMSCustomFieldScreen entity.
 */
@ApiModel(description = "The TMSCustomFieldScreen entity.")
@Entity
@Table(name = "tms_custom_field_screen")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "tmscustomfieldscreen")
public class TMSCustomFieldScreen extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "jhi_sequence", nullable = false)
    private Integer sequence;
    
    @NotNull
    @Lob
    @Column(name = "entity_grid_input", nullable = false)
    private String entityGridInput;

	@NotNull
	@Lob
    @Column(name = "entity_grid_pm", nullable = false)
    private String entityGridPm;

	@NotNull
    @Lob
    @Column(name = "entity_grid_op", nullable = false)
    private String entityGridOp;

	@ManyToOne
    private TMSCustomField tmsCustomField;

    @ManyToOne
    private ProjectWorkflows projectWorkflows;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSequence() {
        return sequence;
    }

    public TMSCustomFieldScreen sequence(Integer sequence) {
        this.sequence = sequence;
        return this;
    }
    
    public String getEntityGridInput() {
		return entityGridInput;
	}

	public void setEntityGridInput(String entityGridInput) {
		this.entityGridInput = entityGridInput;
	}
    
	public String getEntityGridPm() {
		return entityGridPm;
	}

	public void setEntityGridPm(String entityGridPm) {
		this.entityGridPm = entityGridPm;
	}
	
	public String getEntityGridOp() {
		return entityGridOp;
	}

	public void setEntityGridOp(String entityGridOp) {
		this.entityGridOp = entityGridOp;
	}
	
    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public TMSCustomField getTmsCustomField() {
        return tmsCustomField;
    }

    public TMSCustomFieldScreen tmsCustomField(TMSCustomField tMSCustomField) {
        this.tmsCustomField = tMSCustomField;
        return this;
    }

    public void setTmsCustomField(TMSCustomField tMSCustomField) {
        this.tmsCustomField = tMSCustomField;
    }

    public ProjectWorkflows getProjectWorkflows() {
        return projectWorkflows;
    }

    public TMSCustomFieldScreen projectWorkflows(ProjectWorkflows projectWorkflows) {
        this.projectWorkflows = projectWorkflows;
        return this;
    }

    public void setProjectWorkflows(ProjectWorkflows projectWorkflows) {
        this.projectWorkflows = projectWorkflows;
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
        TMSCustomFieldScreen tMSCustomFieldScreen = (TMSCustomFieldScreen) o;
        if (tMSCustomFieldScreen.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), tMSCustomFieldScreen.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TMSCustomFieldScreen{" +
            "id=" + getId() +
            ", sequence=" + getSequence() +
            "}";
    }
}
