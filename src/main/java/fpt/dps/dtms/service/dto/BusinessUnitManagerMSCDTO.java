package fpt.dps.dtms.service.dto;

public class BusinessUnitManagerMSCDTO {
	private Long id;
	
    private Long businessUnitId;

    private String businessUnitName;

    private Long managerId;

    private String managerLogin;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getBusinessUnitId() {
		return businessUnitId;
	}

	public void setBusinessUnitId(Long businessUnitId) {
		this.businessUnitId = businessUnitId;
	}

	public String getBusinessUnitName() {
		return businessUnitName;
	}

	public void setBusinessUnitName(String businessUnitName) {
		this.businessUnitName = businessUnitName;
	}

	public Long getManagerId() {
		return managerId;
	}

	public void setManagerId(Long managerId) {
		this.managerId = managerId;
	}

	public String getManagerLogin() {
		return managerLogin;
	}

	public void setManagerLogin(String managerLogin) {
		this.managerLogin = managerLogin;
	}
    
	@Override
    public String toString() {
        return "BusinessUnitManagerDTO{" +
            "id=" + getId() +
            "}";
    }
}
