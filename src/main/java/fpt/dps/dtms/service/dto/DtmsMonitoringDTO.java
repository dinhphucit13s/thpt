package fpt.dps.dtms.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import fpt.dps.dtms.domain.enumeration.PositionMonitoring;
import fpt.dps.dtms.domain.enumeration.MONITORINGROLE;

/**
 * A DTO for the DtmsMonitoring entity.
 */
public class DtmsMonitoringDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    @NotNull
    private PositionMonitoring position;

    @NotNull
    private Long positionId;

    @NotNull
    private MONITORINGROLE role;

    @NotNull
    @Size(min = 2, max = 1000)
    private String members;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PositionMonitoring getPosition() {
        return position;
    }

    public void setPosition(PositionMonitoring position) {
        this.position = position;
    }

    public Long getPositionId() {
        return positionId;
    }

    public void setPositionId(Long positionId) {
        this.positionId = positionId;
    }

    public MONITORINGROLE getRole() {
        return role;
    }

    public void setRole(MONITORINGROLE role) {
        this.role = role;
    }

    public String getMembers() {
        return members;
    }

    public void setMembers(String members) {
        this.members = members;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DtmsMonitoringDTO dtmsMonitoringDTO = (DtmsMonitoringDTO) o;
        if(dtmsMonitoringDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), dtmsMonitoringDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "DtmsMonitoringDTO{" +
            "id=" + getId() +
            ", position='" + getPosition() + "'" +
            ", positionId=" + getPositionId() +
            ", role='" + getRole() + "'" +
            ", members='" + getMembers() + "'" +
            "}";
    }
}
