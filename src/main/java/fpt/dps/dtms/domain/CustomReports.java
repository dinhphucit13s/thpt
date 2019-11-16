package fpt.dps.dtms.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import com.fasterxml.jackson.annotation.JsonBackReference;

import fpt.dps.dtms.domain.enumeration.ProjectRoles;

/**
 * A CustomReports.
 */
@Entity
@Table(name = "custom_reports")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "customreports")
public class CustomReports extends AbstractAuditingEntity implements Serializable{
	private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 50)
    @Column(name = "user_login", length = 50)
    private String userLogin;

    @Size(max = 100)
    @Column(name = "page_name")
    private String pageName;
    
    @Lob
    @Column(name = "value")
    private String value;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserLogin() {
		return userLogin;
	}

	public void setUserLogin(String userLogin) {
		this.userLogin = userLogin;
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
	
	public CustomReports userLogin(String userLogin){
		this.userLogin = userLogin;
		return this;
	}
	
	public CustomReports pageName(String pageName){
		this.pageName = pageName;
		return this;
	}
	
	public CustomReports value(String value){
		this.value = value;
		return this;
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
