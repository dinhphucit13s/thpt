package fpt.dps.dtms.service.dto;


import javax.persistence.Lob;
import javax.validation.constraints.*;

import fpt.dps.dtms.domain.AbstractAuditingEntity;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the BusinessUnit entity.
 */
public class BusinessUnitDTO extends AbstractAuditingEntity implements Serializable {

    private Long id;

    @NotNull
    private String code;

    @NotNull
    private String name;

    @Lob
    private String description;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BusinessUnitDTO businessUnitDTO = (BusinessUnitDTO) o;
        if(businessUnitDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), businessUnitDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "BusinessUnitDTO{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
