package fpt.dps.dtms.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import fpt.dps.dtms.domain.enumeration.PurchaseOrderStatus;

/**
 * The Purchase Order entity.
 */
@ApiModel(description = "The Purchase Order entity.")
@Entity
@Table(name = "purchase_orders")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "purchaseorders")
public class PurchaseOrders extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private PurchaseOrderStatus status;

    @Column(name = "start_time")
    private Instant startTime;

    @Column(name = "end_time")
    private Instant endTime;

    @Lob
    @Column(name = "description")
    private String description;
    
    @Column(name = "review_ratio")
    private String reviewRatio;

    @OneToMany(mappedBy = "purchaseOrders")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<TMSCustomFieldScreenValue> tmsCustomFieldScreenValues = new HashSet<>();

    @ManyToOne
    private Projects project;

    @ManyToOne
    private ProjectTemplates projectTemplates;

	@ManyToOne
    private ProjectUsers purchaseOrderLead;

    @OneToMany(mappedBy = "purchaseOrders")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Packages> packages = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public PurchaseOrders name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PurchaseOrderStatus getStatus() {
        return status;
    }

    public PurchaseOrders status(PurchaseOrderStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(PurchaseOrderStatus status) {
        this.status = status;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public PurchaseOrders startTime(Instant startTime) {
        this.startTime = startTime;
        return this;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public Instant getEndTime() {
        return endTime;
    }

    public PurchaseOrders endTime(Instant endTime) {
        this.endTime = endTime;
        return this;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    public String getDescription() {
        return description;
    }

    public PurchaseOrders description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getReviewRatio() {
		return reviewRatio;
	}
    
    public PurchaseOrders reviewRatio(String reviewRatio) {
        this.reviewRatio = reviewRatio;
        return this;
    }

	public void setReviewRatio(String reviewRatio) {
		this.reviewRatio = reviewRatio;
	}

	public Set<TMSCustomFieldScreenValue> getTmsCustomFieldScreenValues() {
        return tmsCustomFieldScreenValues;
    }

    public PurchaseOrders tmsCustomFieldScreenValues(Set<TMSCustomFieldScreenValue> tMSCustomFieldScreenValues) {
        this.tmsCustomFieldScreenValues = tMSCustomFieldScreenValues;
        return this;
    }

    public PurchaseOrders addTmsCustomFieldScreenValue(TMSCustomFieldScreenValue tMSCustomFieldScreenValue) {
        this.tmsCustomFieldScreenValues.add(tMSCustomFieldScreenValue);
        tMSCustomFieldScreenValue.setPurchaseOrders(this);
        return this;
    }

    public PurchaseOrders removeTmsCustomFieldScreenValue(TMSCustomFieldScreenValue tMSCustomFieldScreenValue) {
        this.tmsCustomFieldScreenValues.remove(tMSCustomFieldScreenValue);
        tMSCustomFieldScreenValue.setPurchaseOrders(null);
        return this;
    }

    public void setTmsCustomFieldScreenValues(Set<TMSCustomFieldScreenValue> tMSCustomFieldScreenValues) {
        this.tmsCustomFieldScreenValues = tMSCustomFieldScreenValues;
    }

    public Projects getProject() {
        return project;
    }

    public PurchaseOrders project(Projects projects) {
        this.project = projects;
        return this;
    }

    public void setProject(Projects projects) {
        this.project = projects;
    }

    public ProjectUsers getPurchaseOrderLead() {
        return purchaseOrderLead;
    }

    public PurchaseOrders purchaseOrderLead(ProjectUsers projectUsers) {
        this.purchaseOrderLead = projectUsers;
        return this;
    }

    public void setPurchaseOrderLead(ProjectUsers projectUsers) {
        this.purchaseOrderLead = projectUsers;
    }
    
    public ProjectTemplates getProjectTemplates() {
        return projectTemplates;
    }

    public PurchaseOrders projectTemplates(ProjectTemplates projectTemplates) {
        this.projectTemplates = projectTemplates;
        return this;
    }

    public void setProjectTemplates(ProjectTemplates projectTemplates) {
        this.projectTemplates = projectTemplates;
    }

    public Set<Packages> getPackages() {
        return packages;
    }

    public PurchaseOrders packages(Set<Packages> packages) {
        this.packages = packages;
        return this;
    }

    public PurchaseOrders addPackages(Packages packages) {
        this.packages.add(packages);
        packages.setPurchaseOrders(this);
        return this;
    }

    public PurchaseOrders removePackages(Packages packages) {
        this.packages.remove(packages);
        packages.setPurchaseOrders(null);
        return this;
    }

    public void setPackages(Set<Packages> packages) {
        this.packages = packages;
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
        PurchaseOrders purchaseOrders = (PurchaseOrders) o;
        if (purchaseOrders.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), purchaseOrders.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PurchaseOrders{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", status='" + getStatus() + "'" +
            ", startTime='" + getStartTime() + "'" +
            ", endTime='" + getEndTime() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
