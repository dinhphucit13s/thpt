package fpt.dps.dtms.service.dto;


import javax.persistence.Lob;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the BugListDefault entity.
 */
public class BugListDefaultDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 10)
    @Lob
    private String description;

    private Boolean status;

    private Long businessLineId;

    private String businessLineName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean isStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Long getBusinessLineId() {
        return businessLineId;
    }

    public void setBusinessLineId(Long businessLineId) {
        this.businessLineId = businessLineId;
    }

    public String getBusinessLineName() {
        return businessLineName;
    }

    public void setBusinessLineName(String businessLineName) {
        this.businessLineName = businessLineName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BugListDefaultDTO bugListDefaultDTO = (BugListDefaultDTO) o;
        if(bugListDefaultDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), bugListDefaultDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "BugListDefaultDTO{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", status='" + isStatus() + "'" +
            "}";
    }
}
