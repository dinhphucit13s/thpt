package fpt.dps.dtms.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

import fpt.dps.dtms.domain.enumeration.BiddingScope;

/**
 * A TaskBiddingTrackingTime.
 */
@Entity
@Table(name = "task_bidding_tracking_time")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "taskbiddingtrackingtime")
public class TaskBiddingTrackingTime extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    @Enumerated(EnumType.STRING)
    @Column(name = "bidding_scope")
    private BiddingScope biddingScope;

    @ManyToOne(optional = false)
    @NotNull
    private Tasks task;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public TaskBiddingTrackingTime userLogin(String userLogin) {
        this.userLogin = userLogin;
        return this;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public String getRole() {
        return role;
    }

    public TaskBiddingTrackingTime role(String role) {
        this.role = role;
        return this;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public TaskBiddingTrackingTime startTime(Instant startTime) {
        this.startTime = startTime;
        return this;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public Instant getEndTime() {
        return endTime;
    }

    public TaskBiddingTrackingTime endTime(Instant endTime) {
        this.endTime = endTime;
        return this;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    public String getStartStatus() {
        return startStatus;
    }

    public TaskBiddingTrackingTime startStatus(String startStatus) {
        this.startStatus = startStatus;
        return this;
    }

    public void setStartStatus(String startStatus) {
        this.startStatus = startStatus;
    }

    public String getEndStatus() {
        return endStatus;
    }

    public TaskBiddingTrackingTime endStatus(String endStatus) {
        this.endStatus = endStatus;
        return this;
    }

    public void setEndStatus(String endStatus) {
        this.endStatus = endStatus;
    }

    public Integer getDuration() {
        return duration;
    }

    public TaskBiddingTrackingTime duration(Integer duration) {
        this.duration = duration;
        return this;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public BiddingScope getBiddingScope() {
        return biddingScope;
    }

    public TaskBiddingTrackingTime biddingScope(BiddingScope biddingScope) {
        this.biddingScope = biddingScope;
        return this;
    }

    public void setBiddingScope(BiddingScope biddingScope) {
        this.biddingScope = biddingScope;
    }

    public Tasks getTask() {
        return task;
    }

    public TaskBiddingTrackingTime task(Tasks tasks) {
        this.task = tasks;
        return this;
    }

    public void setTask(Tasks tasks) {
        this.task = tasks;
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
        TaskBiddingTrackingTime taskBiddingTrackingTime = (TaskBiddingTrackingTime) o;
        if (taskBiddingTrackingTime.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), taskBiddingTrackingTime.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TaskBiddingTrackingTime{" +
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
