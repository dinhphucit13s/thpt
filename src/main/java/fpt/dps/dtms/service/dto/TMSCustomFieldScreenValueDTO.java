package fpt.dps.dtms.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the TMSCustomFieldScreenValue entity.
 */
public class TMSCustomFieldScreenValueDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    @Size(max = 1024)
    private String value;

    @Lob
    private String text;

    private Long purchaseOrdersId;

    private Long packagesId;

    private Long tasksId;

    private Long tmsCustomFieldScreenId;

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getPurchaseOrdersId() {
        return purchaseOrdersId;
    }

    public void setPurchaseOrdersId(Long purchaseOrdersId) {
        this.purchaseOrdersId = purchaseOrdersId;
    }

    public Long getPackagesId() {
        return packagesId;
    }

    public void setPackagesId(Long packagesId) {
        this.packagesId = packagesId;
    }

    public Long getTasksId() {
        return tasksId;
    }

    public void setTasksId(Long tasksId) {
        this.tasksId = tasksId;
    }

    public Long getTmsCustomFieldScreenId() {
		return tmsCustomFieldScreenId;
	}

	public void setTmsCustomFieldScreenId(Long tmsCustomFieldScreenId) {
		this.tmsCustomFieldScreenId = tmsCustomFieldScreenId;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TMSCustomFieldScreenValueDTO tMSCustomFieldScreenValueDTO = (TMSCustomFieldScreenValueDTO) o;
        if(tMSCustomFieldScreenValueDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), tMSCustomFieldScreenValueDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TMSCustomFieldScreenValueDTO{" +
            "id=" + getId() +
            ", value='" + getValue() + "'" +
            ", text='" + getText() + "'" +
            "}";
    }
}
