package fpt.dps.dtms.service.dto;

import java.io.Serializable;
import java.util.Objects;

import javax.validation.constraints.NotNull;

import fpt.dps.dtms.domain.AbstractAuditingEntity;

public class BusinessUnitMSCDTO {
	private Long id;

    @NotNull
    private String code;

    @NotNull
    private String name;
    
    private String businessUnitLead;

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

    public String getBusinessUnitLead() {
		return businessUnitLead;
	}

	public void setBusinessUnitLead(String businessUnitLead) {
		this.businessUnitLead = businessUnitLead;
	}

	@Override
    public String toString() {
        return "BusinessUnitMSCDTO{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            "}";
    }
}
