package fpt.dps.dtms.service.dto;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Lob;
import javax.validation.constraints.Size;

import fpt.dps.dtms.domain.CustomReports;

/**
 * A DTO for the CustomReports entity.
 */
public class CustomReportsDTO extends AbstractAuditingDTO implements Serializable{
	private Long id;
	
	@Size(max = 50)
	private String userLogin;
	
	@Size(max = 100)
	private String pageName;
		
	@Lob
    private String value;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserLogin() {
		return userLogin;
	}

	public void setUserLogin(String useLogin) {
		this.userLogin = useLogin;
	}

	public String getPageName() {
		return pageName;
	}

	public void setPageName(String pageName) {
		this.pageName = pageName;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
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
	        CustomReports customReports = (CustomReports) o;
	        if (customReports.getId() == null || getId() == null) {
	            return false;
	        }
	        return Objects.equals(getId(), customReports.getId());
	    }
		
		@Override
	    public int hashCode() {
	        return Objects.hashCode(getId());
	    }

	    @Override
	    public String toString() {
	        return "ProjectUsers{" +
	            "id=" + getId() +
	            ", userLogin='" + getUserLogin() + "'" +
	            ", pageName='" + getPageName() + "'" +
	            "}";
	    }
}
