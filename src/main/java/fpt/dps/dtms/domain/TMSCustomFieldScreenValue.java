package fpt.dps.dtms.domain;

import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * The TMSCustomFieldScreenValue entity.
 */
@ApiModel(description = "The TMSCustomFieldScreenValue entity.")
@Entity
@Table(name = "tms_custom_field_screen_value")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "tmscustomfieldscreenvalue")
public class TMSCustomFieldScreenValue extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 1024)
    @Column(name = "jhi_value", length = 1024)
    private String value;

    @Lob
    @Column(name = "text")
    private String text;

    @ManyToOne
    private PurchaseOrders purchaseOrders;

    @ManyToOne
    private Packages packages;

    @ManyToOne
    private Tasks tasks;

	@ManyToOne
    private TMSCustomFieldScreen tmsCustomFieldScreen;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public TMSCustomFieldScreenValue value(String value) {
        this.value = value;
        return this;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public TMSCustomFieldScreenValue text(String text) {
        this.text = text;
        return this;
    }

    public void setText(String text) {
        this.text = text;
    }

    public PurchaseOrders getPurchaseOrders() {
        return purchaseOrders;
    }

    public TMSCustomFieldScreenValue purchaseOrders(PurchaseOrders purchaseOrders) {
        this.purchaseOrders = purchaseOrders;
        return this;
    }

    public void setPurchaseOrders(PurchaseOrders purchaseOrders) {
        this.purchaseOrders = purchaseOrders;
    }

    public Packages getPackages() {
        return packages;
    }

    public TMSCustomFieldScreenValue packages(Packages packages) {
        this.packages = packages;
        return this;
    }

    public void setPackages(Packages packages) {
        this.packages = packages;
    }

    public Tasks getTasks() {
        return tasks;
    }

    public TMSCustomFieldScreenValue tasks(Tasks tasks) {
        this.tasks = tasks;
        return this;
    }

    public void setTasks(Tasks tasks) {
        this.tasks = tasks;
    }
    
    public TMSCustomFieldScreen getTMSCustomFieldScreen() {
		return tmsCustomFieldScreen;
	}

	public void setTMSCustomFieldScreen(TMSCustomFieldScreen tMSCustomFieldScreen) {
		this.tmsCustomFieldScreen = tMSCustomFieldScreen;
	}
	
    public TMSCustomFieldScreenValue tmsCustomFieldScreen(TMSCustomFieldScreen tMSCustomFieldScreen) {
        this.tmsCustomFieldScreen = tMSCustomFieldScreen;
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
        TMSCustomFieldScreenValue tMSCustomFieldScreenValue = (TMSCustomFieldScreenValue) o;
        if (tMSCustomFieldScreenValue.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), tMSCustomFieldScreenValue.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TMSCustomFieldScreenValue{" +
            "id=" + getId() +
            ", value='" + getValue() + "'" +
            ", text='" + getText() + "'" +
            "}";
    }
}
