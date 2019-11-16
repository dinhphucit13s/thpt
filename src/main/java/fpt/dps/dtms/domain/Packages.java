package fpt.dps.dtms.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * The Packages entity.
 */
@ApiModel(description = "The Packages entity.")
@Entity
@Table(name = "packages")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "packages")
public class Packages extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @Column(name = "op")
    private String op;

    @Column(name = "reviewer")
    private String reviewer;

    @Column(name = "fi")
    private String fi;

    @Column(name = "delivery")
    private String delivery;

    @Column(name = "estimate_delivery")
    private Instant estimateDelivery;

    @Column(name = "target")
    private Integer target;

    @Column(name = "start_time")
    private Instant startTime;

    @Column(name = "end_time")
    private Instant endTime;

    @Lob
    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "packages")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<TMSCustomFieldScreenValue> tmsCustomFieldScreenValues = new HashSet<>();

    @ManyToOne
    private PurchaseOrders purchaseOrders;

    @OneToMany(mappedBy = "packages")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Tasks> tasks = new HashSet<>();

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

    public Packages name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOp() {
        return op;
    }

    public Packages op(String op) {
        this.op = op;
        return this;
    }

    public void setOp(String op) {
        this.op = op;
    }

    public String getReviewer() {
        return reviewer;
    }

    public Packages reviewer(String reviewer) {
        this.reviewer = reviewer;
        return this;
    }

    public void setReviewer(String reviewer) {
        this.reviewer = reviewer;
    }

    public String getFi() {
        return fi;
    }

    public Packages fi(String fi) {
        this.fi = fi;
        return this;
    }

    public void setFi(String fi) {
        this.fi = fi;
    }

    public String getDelivery() {
        return delivery;
    }

    public Packages delivery(String delivery) {
        this.delivery = delivery;
        return this;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }

    public Instant getEstimateDelivery() {
        return estimateDelivery;
    }

    public Packages estimateDelivery(Instant estimateDelivery) {
        this.estimateDelivery = estimateDelivery;
        return this;
    }

    public void setEstimateDelivery(Instant estimateDelivery) {
        this.estimateDelivery = estimateDelivery;
    }

    public Integer getTarget() {
        return target;
    }

    public Packages target(Integer target) {
        this.target = target;
        return this;
    }

    public void setTarget(Integer target) {
        this.target = target;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public Packages startTime(Instant startTime) {
        this.startTime = startTime;
        return this;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public Instant getEndTime() {
        return endTime;
    }

    public Packages endTime(Instant endTime) {
        this.endTime = endTime;
        return this;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    public String getDescription() {
        return description;
    }

    public Packages description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<TMSCustomFieldScreenValue> getTmsCustomFieldScreenValues() {
        return tmsCustomFieldScreenValues;
    }

    public Packages tmsCustomFieldScreenValues(Set<TMSCustomFieldScreenValue> tMSCustomFieldScreenValues) {
        this.tmsCustomFieldScreenValues = tMSCustomFieldScreenValues;
        return this;
    }

    public Packages addTmsCustomFieldScreenValue(TMSCustomFieldScreenValue tMSCustomFieldScreenValue) {
        this.tmsCustomFieldScreenValues.add(tMSCustomFieldScreenValue);
        tMSCustomFieldScreenValue.setPackages(this);
        return this;
    }

    public Packages removeTmsCustomFieldScreenValue(TMSCustomFieldScreenValue tMSCustomFieldScreenValue) {
        this.tmsCustomFieldScreenValues.remove(tMSCustomFieldScreenValue);
        tMSCustomFieldScreenValue.setPackages(null);
        return this;
    }

    public void setTmsCustomFieldScreenValues(Set<TMSCustomFieldScreenValue> tMSCustomFieldScreenValues) {
        this.tmsCustomFieldScreenValues = tMSCustomFieldScreenValues;
    }

    public PurchaseOrders getPurchaseOrders() {
        return purchaseOrders;
    }

    public Packages purchaseOrders(PurchaseOrders purchaseOrders) {
        this.purchaseOrders = purchaseOrders;
        return this;
    }

    public void setPurchaseOrders(PurchaseOrders purchaseOrders) {
        this.purchaseOrders = purchaseOrders;
    }

    public Set<Tasks> getTasks() {
        return tasks;
    }

    public Packages tasks(Set<Tasks> tasks) {
        this.tasks = tasks;
        return this;
    }

    public Packages addTasks(Tasks tasks) {
        this.tasks.add(tasks);
        tasks.setPackages(this);
        return this;
    }

    public Packages removeTasks(Tasks tasks) {
        this.tasks.remove(tasks);
        tasks.setPackages(null);
        return this;
    }

    public void setTasks(Set<Tasks> tasks) {
        this.tasks = tasks;
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
        Packages packages = (Packages) o;
        if (packages.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), packages.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Packages{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", op='" + getOp() + "'" +
            ", reviewer='" + getReviewer() + "'" +
            ", fi='" + getFi() + "'" +
            ", delivery='" + getDelivery() + "'" +
            ", estimateDelivery='" + getEstimateDelivery() + "'" +
            ", target=" + getTarget() +
            ", startTime='" + getStartTime() + "'" +
            ", endTime='" + getEndTime() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
