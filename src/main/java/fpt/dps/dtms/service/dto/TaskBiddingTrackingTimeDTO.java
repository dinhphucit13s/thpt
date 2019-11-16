package fpt.dps.dtms.service.dto;


import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import fpt.dps.dtms.domain.enumeration.BiddingScope;

/**
 * A DTO for the TaskBiddingTrackingTime entity.
 */
public class TaskBiddingTrackingTimeDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 1, max = 50)
    private String userLogin;

    @NotNull
    private String role;

    @NotNull
    private Instant startTime;

    private Instant endTime;

    private String startStatus;

    private String endStatus;

    private Integer duration;

    private BiddingScope biddingScope;

    private Long taskId;

    private String taskName;

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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public Instant getEndTime() {
        return endTime;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    public String getStartStatus() {
        return startStatus;
    }

    public void setStartStatus(String startStatus) {
        this.startStatus = startStatus;
    }

    public String getEndStatus() {
        return endStatus;
    }

    public void setEndStatus(String endStatus) {
        this.endStatus = endStatus;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public BiddingScope getBiddingScope() {
        return biddingScope;
    }

    public void setBiddingScope(BiddingScope biddingScope) {
        this.biddingScope = biddingScope;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long tasksId) {
        this.taskId = tasksId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String tasksName) {
        this.taskName = tasksName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TaskBiddingTrackingTimeDTO taskBiddingTrackingTimeDTO = (TaskBiddingTrackingTimeDTO) o;
        if(taskBiddingTrackingTimeDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), taskBiddingTrackingTimeDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TaskBiddingTrackingTimeDTO{" +
            "id=" + getId() +
            ", userLogin='" + getUserLogin() + "'" +
            ", role='" + getRole() + "'" +
            ", startTime='" + getStartTime() + "'" +
            ", endTime='" + getEndTime() + "'" +
            ", startStatus='" + getStartStatus() + "'" +
            ", endStatus='" + getEndStatus() + "'" +
            ", duration=" + getDuration() +
            ", biddingScope='" + getBiddingScope() + "'" +
            "}";
    }
}
