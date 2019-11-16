package fpt.dps.dtms.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

import fpt.dps.dtms.domain.enumeration.PositionMonitoring;

import fpt.dps.dtms.domain.enumeration.MONITORINGROLE;

/**
 * A DtmsMonitoring.
 */
@Entity
@Table(name = "dtms_monitoring")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "dtmsmonitoring")
public class DtmsMonitoring extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "position", nullable = false)
    private PositionMonitoring position;

    @NotNull
    @Column(name = "position_id", nullable = false)
    private Long positionId;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "jhi_role", nullable = false)
    private MONITORINGROLE role;

    @NotNull
    @Size(min = 2, max = 1000)
    @Column(name = "members", length = 1000, nullable = false)
    private String members;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PositionMonitoring getPosition() {
        return position;
    }

    public DtmsMonitoring position(PositionMonitoring position) {
        this.position = position;
        return this;
    }

    public void setPosition(PositionMonitoring position) {
        this.position = position;
    }

    public Long getPositionId() {
        return positionId;
    }

    public DtmsMonitoring positionId(Long positionId) {
        this.positionId = positionId;
        return this;
    }

    public void setPositionId(Long positionId) {
        this.positionId = positionId;
    }

    public MONITORINGROLE getRole() {
        return role;
    }

    public DtmsMonitoring role(MONITORINGROLE role) {
        this.role = role;
        return this;
    }

    public void setRole(MONITORINGROLE role) {
        this.role = role;
    }

    public String getMembers() {
        return members;
    }

    public DtmsMonitoring members(String members) {
        this.members = members;
        return this;
    }

    public void setMembers(String members) {
        this.members = members;
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
        DtmsMonitoring dtmsMonitoring = (DtmsMonitoring) o;
        if (dtmsMonitoring.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), dtmsMonitoring.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "DtmsMonitoring{" +
            "id=" + getId() +
            ", position='" + getPosition() + "'" +
            ", positionId=" + getPositionId() +
            ", role='" + getRole() + "'" +
            ", members='" + getMembers() + "'" +
            "}";
    }
}
