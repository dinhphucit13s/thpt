package fpt.dps.dtms.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the TMSLogHistory entity.
 */
public class TMSLogHistoryDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    @Size(max = 255)
    private String action;

    @Lob
    private String oldValue;

    @Lob
    private String newValue;

    private Long projectsId;

    private String projectsName;

    private Long purchaseOrdersId;

    private String purchaseOrdersName;

    private Long packagesId;

    private String packagesName;

    private Long tasksId;

    private String tasksName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getOldValue() {
        return oldValue;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    public Long getProjectsId() {
        return projectsId;
    }

    public void setProjectsId(Long projectsId) {
        this.projectsId = projectsId;
    }

    public String getProjectsName() {
        return projectsName;
    }

    public void setProjectsName(String projectsName) {
        this.projectsName = projectsName;
    }

    public Long getPurchaseOrdersId() {
        return purchaseOrdersId;
    }

    public void setPurchaseOrdersId(Long purchaseOrdersId) {
        this.purchaseOrdersId = purchaseOrdersId;
    }

    public String getPurchaseOrdersName() {
        return purchaseOrdersName;
    }

    public void setPurchaseOrdersName(String purchaseOrdersName) {
        this.purchaseOrdersName = purchaseOrdersName;
    }

    public Long getPackagesId() {
        return packagesId;
    }

    public void setPackagesId(Long packagesId) {
        this.packagesId = packagesId;
    }

    public String getPackagesName() {
        return packagesName;
    }

    public void setPackagesName(String packagesName) {
        this.packagesName = packagesName;
    }

    public Long getTasksId() {
        return tasksId;
    }

    public void setTasksId(Long tasksId) {
        this.tasksId = tasksId;
    }

    public String getTasksName() {
        return tasksName;
    }

    public void setTasksName(String tasksName) {
        this.tasksName = tasksName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TMSLogHistoryDTO tMSLogHistoryDTO = (TMSLogHistoryDTO) o;
        if(tMSLogHistoryDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), tMSLogHistoryDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TMSLogHistoryDTO{" +
            "id=" + getId() +
            ", action='" + getAction() + "'" +
            ", oldValue='" + getOldValue() + "'" +
            ", newValue='" + getNewValue() + "'" +
            "}";
    }
}
