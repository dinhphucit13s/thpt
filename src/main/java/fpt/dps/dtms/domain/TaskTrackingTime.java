package fpt.dps.dtms.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A TaskTrackingTime.
 */
@Entity
@Table(name = "task_tracking_time")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "tasktrackingtime")
public class TaskTrackingTime extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "task_id", nullable = false)
    private Long taskId;

    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "user_login", length = 50, nullable = false)
    private String userLogin;

    @NotNull
    @Column(name = "jhi_role", nullable = false)
    private String role;

    @NotNull
    @Column(name = "start_time", nullable = false)
    private Instant startTime;

    @Column(name = "end_time")
    private Instant endTime;

    @Column(name = "start_status")
    private String startStatus;

    @Column(name = "end_status")
    private String endStatus;

    @Column(name = "duration")
    private Integer duration;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTaskId() {
        return taskId;
    }

    public TaskTrackingTime taskId(Long taskId) {
        this.taskId = taskId;
        return this;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public TaskTrackingTime userLogin(String userLogin) {
        this.userLogin = userLogin;
        return this;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public String getRole() {
        return role;
    }

    public TaskTrackingTime role(String role) {
        this.role = role;
        return this;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public TaskTrackingTime startTime(Instant startTime) {
        this.startTime = startTime;
        return this;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public Instant getEndTime() {
        return endTime;
    }

    public TaskTrackingTime endTime(Instant endTime) {
        this.endTime = endTime;
        return this;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    public String getStartStatus() {
        return startStatus;
    }

    public TaskTrackingTime startStatus(String startStatus) {
        this.startStatus = startStatus;
        return this;
    }

    public void setStartStatus(String startStatus) {
        this.startStatus = startStatus;
    }

    public String getEndStatus() {
        return endStatus;
    }

    public TaskTrackingTime endStatus(String endStatus) {
        this.endStatus = endStatus;
        return this;
    }

    public void setEndStatus(String endStatus) {
        this.endStatus = endStatus;
    }

    public Integer getDuration() {
        return duration;
    }

    public TaskTrackingTime duration(Integer duration) {
        this.duration = duration;
        return this;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TaskTrackingTime taskTrackingTime = (TaskTrackingTime) o;
        if (taskTrackingTime.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), taskTrackingTime.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TaskTrackingTime{" +
            "id=" + getId() +
            ", taskId=" + getTaskId() +
            ", userLogin='" + getUserLogin() + "'" +
            ", role='" + getRole() + "'" +
            ", startTime='" + getStartTime() + "'" +
            ", endTime='" + getEndTime() + "'" +
            ", startStatus='" + getStartStatus() + "'" +
            ", endStatus='" + getEndStatus() + "'" +
            ", duration=" + getDuration() +
            "}";
    }
}
