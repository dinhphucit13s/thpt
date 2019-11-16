package fpt.dps.dtms.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A TMSLogHistory.
 */
@Entity
@Table(name = "tms_log_history")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "tmsloghistory")
public class TMSLogHistory extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 255)
    @Column(name = "action", length = 255)
    private String action;

    @Lob
    @Column(name = "old_value")
    private String oldValue;

    @Lob
    @Column(name = "new_value")
    private String newValue;

    @ManyToOne
    private Projects projects;

    @ManyToOne
    private PurchaseOrders purchaseOrders;

    @ManyToOne
    private Packages packages;

    @ManyToOne
    private Tasks tasks;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAction() {
        return action;
    }

    public TMSLogHistory action(String action) {
        this.action = action;
        return this;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getOldValue() {
        return oldValue;
    }

    public TMSLogHistory oldValue(String oldValue) {
        this.oldValue = oldValue;
        return this;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    public String getNewValue() {
        return newValue;
    }

    public TMSLogHistory newValue(String newValue) {
        this.newValue = newValue;
        return this;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    public Projects getProjects() {
        return projects;
    }

    public TMSLogHistory projects(Projects projects) {
        this.projects = projects;
        return this;
    }

    public void setProjects(Projects projects) {
        this.projects = projects;
    }

    public PurchaseOrders getPurchaseOrders() {
        return purchaseOrders;
    }

    public TMSLogHistory purchaseOrders(PurchaseOrders purchaseOrders) {
        this.purchaseOrders = purchaseOrders;
        return this;
    }

    public void setPurchaseOrders(PurchaseOrders purchaseOrders) {
        this.purchaseOrders = purchaseOrders;
    }

    public Packages getPackages() {
        return packages;
    }

    public TMSLogHistory packages(Packages packages) {
        this.packages = packages;
        return this;
    }

    public void setPackages(Packages packages) {
        this.packages = packages;
    }

    public Tasks getTasks() {
        return tasks;
    }

    public TMSLogHistory tasks(Tasks tasks) {
        this.tasks = tasks;
        return this;
    }

    public void setTasks(Tasks tasks) {
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
        TMSLogHistory tMSLogHistory = (TMSLogHistory) o;
        if (tMSLogHistory.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), tMSLogHistory.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TMSLogHistory{" +
            "id=" + getId() +
            ", action='" + getAction() + "'" +
            ", oldValue='" + getOldValue() + "'" +
            ", newValue='" + getNewValue() + "'" +
            "}";
    }
}
