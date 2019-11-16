package fpt.dps.dtms.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A BusinessUnitManager.
 */
@Entity
@Table(name = "business_unit_manager")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "businessunitmanager")
public class BusinessUnitManager extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(name = "description")
    private String description;

    @Column(name = "start_time")
    private LocalDate startTime;

    @Column(name = "end_time")
    private LocalDate endTime;

    @ManyToOne(optional = false)
    @NotNull
    private BusinessUnit businessUnit;

    @ManyToOne(optional = false)
    @NotNull
    private User manager;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

   public String getDescription() {
       return description;
   }

   public BusinessUnitManager description(String description) {
       this.description = description;
       return this;
   }

   public void setDescription(String description) {
       this.description = description;
   }

    public LocalDate getStartTime() {
        return startTime;
    }

    public BusinessUnitManager startTime(LocalDate startTime) {
        this.startTime = startTime;
        return this;
    }

    public void setStartTime(LocalDate startTime) {
        this.startTime = startTime;
    }

    public LocalDate getEndTime() {
        return endTime;
    }

    public BusinessUnitManager endTime(LocalDate endTime) {
        this.endTime = endTime;
        return this;
    }

    public void setEndTime(LocalDate endTime) {
        this.endTime = endTime;
    }

    public BusinessUnit getBusinessUnit() {
        return businessUnit;
    }

    public BusinessUnitManager businessUnit(BusinessUnit businessUnit) {
        this.businessUnit = businessUnit;
        return this;
    }

    public void setBusinessUnit(BusinessUnit businessUnit) {
        this.businessUnit = businessUnit;
    }

    public User getManager() {
        return manager;
    }

    public BusinessUnitManager manager(User user) {
        this.manager = user;
        return this;
    }

    public void setManager(User user) {
        this.manager = user;
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
        BusinessUnitManager businessUnitManager = (BusinessUnitManager) o;
        if (businessUnitManager.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), businessUnitManager.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "BusinessUnitManager{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", startTime='" + getStartTime() + "'" +
            ", endTime='" + getEndTime() + "'" +
            "}";
    }
}
