package fpt.dps.dtms.service.dto;


import javax.persistence.Lob;
import javax.validation.constraints.*;

import fpt.dps.dtms.domain.TMSCustomField;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the TMSCustomFieldScreen entity.
 */
public class TMSCustomFieldScreenDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer sequence;
    
    @NotNull
    @Lob
    private String entityGridInput;
    
	@NotNull
	@Lob
    private String entityGridPm;

	@NotNull
	@Lob
    private String entityGridOp;

	private Long tmsCustomFieldId;
	
	private String entityData;

    private Long projectWorkflowsId;

    private String projectWorkflowsName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
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

	public Long getTmsCustomFieldId() {
		return tmsCustomFieldId;
	}

	public void setTmsCustomFieldId(Long tmsCustomFieldId) {
		this.tmsCustomFieldId = tmsCustomFieldId;
	}

	public String getEntityData() {
		return entityData;
	}

	public void setEntityData(String entityData) {
		this.entityData = entityData;
	}

	public Long getProjectWorkflowsId() {
        return projectWorkflowsId;
    }

    public void setProjectWorkflowsId(Long projectWorkflowsId) {
        this.projectWorkflowsId = projectWorkflowsId;
    }

    public String getProjectWorkflowsName() {
        return projectWorkflowsName;
    }

    public void setProjectWorkflowsName(String projectWorkflowsName) {
        this.projectWorkflowsName = projectWorkflowsName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TMSCustomFieldScreenDTO tMSCustomFieldScreenDTO = (TMSCustomFieldScreenDTO) o;
        if(tMSCustomFieldScreenDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), tMSCustomFieldScreenDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TMSCustomFieldScreenDTO{" +
            "id=" + getId() +
            ", sequence=" + getSequence() +
            "}";
    }
}
