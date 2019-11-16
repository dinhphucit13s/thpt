package fpt.dps.dtms.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import fpt.dps.dtms.domain.enumeration.ProjectType;

import fpt.dps.dtms.domain.enumeration.ProjectStatus;

/**
 * The Projects entity.
 */
@ApiModel(description = "The Projects entity.")
@Entity
@Table(name = "projects")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "projects")
public class Projects extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 20)
    @Column(name = "code", length = 20, nullable = false)
    private String code;

    @NotNull
    @Size(max = 100)
    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "jhi_type")
    private ProjectType type;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ProjectStatus status;

    @Column(name = "start_time")
    private Instant startTime;

    @Column(name = "end_time")
    private Instant endTime;
    
    @Column(name = "bidding_hold_time")
    private Integer biddingHoldTime;

    @Lob
    @Column(name = "description")
    private String description;

    @ManyToOne
    private ProjectTemplates projectTemplates;

    @ManyToOne
    private ProjectUsers projectLead;

    @OneToMany(mappedBy = "project")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<PurchaseOrders> purchaseOrders = new HashSet<>();

    @OneToMany(mappedBy = "project", orphanRemoval=true)
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ProjectUsers> projectUsers = new HashSet<>();

    @ManyToOne
    private Customer customer;

    @OneToMany(mappedBy = "project")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ProjectBugListDefault> projectBugListDefaults = new HashSet<>();

    @ManyToOne
    private BusinessUnit businessUnit;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public Projects code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public Projects name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProjectType getType() {
        return type;
    }

    public Projects type(ProjectType type) {
        this.type = type;
        return this;
    }

    public void setType(ProjectType type) {
        this.type = type;
    }

    public ProjectStatus getStatus() {
        return status;
    }

    public Projects status(ProjectStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(ProjectStatus status) {
        this.status = status;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public Projects startTime(Instant startTime) {
        this.startTime = startTime;
        return this;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public Instant getEndTime() {
        return endTime;
    }

    public Projects endTime(Instant endTime) {
        this.endTime = endTime;
        return this;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    public Integer getBiddingHoldTime() {
		return biddingHoldTime;
	}

	public void setBiddingHoldTime(Integer biddingHoldTime) {
		this.biddingHoldTime = biddingHoldTime;
	}

	public String getDescription() {
        return description;
    }

    public Projects description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ProjectTemplates getProjectTemplates() {
        return projectTemplates;
    }

    public Projects projectTemplates(ProjectTemplates projectTemplates) {
        this.projectTemplates = projectTemplates;
        return this;
    }

    public void setProjectTemplates(ProjectTemplates projectTemplates) {
        this.projectTemplates = projectTemplates;
    }

    public ProjectUsers getProjectLead() {
        return projectLead;
    }

    public Projects projectLead(ProjectUsers projectUsers) {
        this.projectLead = projectUsers;
        return this;
    }

    public void setProjectLead(ProjectUsers projectUsers) {
        this.projectLead = projectUsers;
    }

    public Set<PurchaseOrders> getPurchaseOrders() {
        return purchaseOrders;
    }

    public Projects purchaseOrders(Set<PurchaseOrders> purchaseOrders) {
        this.purchaseOrders = purchaseOrders;
        return this;
    }

    public Projects addPurchaseOrders(PurchaseOrders purchaseOrders) {
        this.purchaseOrders.add(purchaseOrders);
        purchaseOrders.setProject(this);
        return this;
    }

    public Projects removePurchaseOrders(PurchaseOrders purchaseOrders) {
        this.purchaseOrders.remove(purchaseOrders);
        purchaseOrders.setProject(null);
        return this;
    }

    public void setPurchaseOrders(Set<PurchaseOrders> purchaseOrders) {
        this.purchaseOrders = purchaseOrders;
    }

    public Set<ProjectUsers> getProjectUsers() {
        return projectUsers;
    }

    public Projects projectUsers(Set<ProjectUsers> projectUsers) {
        this.projectUsers = projectUsers;
        return this;
    }

    public Projects addProjectUsers(ProjectUsers projectUsers) {
        this.projectUsers.add(projectUsers);
        projectUsers.setProject(this);
        return this;
    }

    public Projects removeProjectUsers(ProjectUsers projectUsers) {
        this.projectUsers.remove(projectUsers);
        projectUsers.setProject(null);
        return this;
    }

    public void setProjectUsers(Set<ProjectUsers> projectUsers) {
        this.projectUsers = projectUsers;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Projects customer(Customer customer) {
        this.customer = customer;
        return this;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Set<ProjectBugListDefault> getProjectBugListDefaults() {
        return projectBugListDefaults;
    }

    public Projects projectBugListDefaults(Set<ProjectBugListDefault> projectBugListDefaults) {
        this.projectBugListDefaults = projectBugListDefaults;
        return this;
    }

    public Projects addProjectBugListDefault(ProjectBugListDefault projectBugListDefault) {
        this.projectBugListDefaults.add(projectBugListDefault);
        projectBugListDefault.setProject(this);
        return this;
    }

    public Projects removeProjectBugListDefault(ProjectBugListDefault projectBugListDefault) {
        this.projectBugListDefaults.remove(projectBugListDefault);
        projectBugListDefault.setProject(null);
        return this;
    }

    public void setProjectBugListDefaults(Set<ProjectBugListDefault> projectBugListDefaults) {
        this.projectBugListDefaults = projectBugListDefaults;
    }

    public BusinessUnit getBusinessUnit() {
        return businessUnit;
    }

    public Projects businessUnit(BusinessUnit businessUnit) {
        this.businessUnit = businessUnit;
        return this;
    }

    public void setBusinessUnit(BusinessUnit businessUnit) {
        this.businessUnit = businessUnit;
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
        Projects projects = (Projects) o;
        if (projects.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), projects.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Projects{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", type='" + getType() + "'" +
            ", status='" + getStatus() + "'" +
            ", startTime='" + getStartTime() + "'" +
            ", endTime='" + getEndTime() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
