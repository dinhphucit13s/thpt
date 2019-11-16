package fpt.dps.dtms.service.dto;

import java.io.Serializable;
import java.util.Objects;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import fpt.dps.dtms.domain.AbstractAuditingEntity;

public class AuthorityDTO extends AbstractAuditingEntity implements Serializable {
	
	@NotNull
    @Size(min = 2, max = 100)
    private String name;
	
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AuthorityDTO authorityDTO = (AuthorityDTO) o;
        if(authorityDTO.getName() == null || getName() == null) {
            return false;
        }
        return Objects.equals(getName(), authorityDTO.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getName());
    }

    @Override
    public String toString() {
        return "AuthorityDTO{" +
            ", name='" + getName() + "'" +
            "}";
    }
}
