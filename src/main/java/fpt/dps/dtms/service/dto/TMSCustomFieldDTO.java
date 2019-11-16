package fpt.dps.dtms.service.dto;


import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the TMSCustomField entity.
 */
public class TMSCustomFieldDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    @Lob
    private String entityData;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public String getEntityData() {
		return entityData;
	}

	public void setEntityData(String entityData) {
		this.entityData = entityData;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TMSCustomFieldDTO tMSCustomFieldDTO = (TMSCustomFieldDTO) o;
        if(tMSCustomFieldDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), tMSCustomFieldDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TMSCustomFieldDTO{" +
            "id=" + getId() +
            ", entity='" + getEntityData() + "'" +
            "}";
    }
}
