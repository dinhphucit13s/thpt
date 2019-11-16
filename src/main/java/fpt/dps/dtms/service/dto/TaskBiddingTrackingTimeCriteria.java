package fpt.dps.dtms.service.dto;

import java.io.Serializable;
import fpt.dps.dtms.domain.enumeration.BiddingScope;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

import io.github.jhipster.service.filter.InstantFilter;




/**
 * Criteria class for the TaskBiddingTrackingTime entity. This class is used in TaskBiddingTrackingTimeResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /task-bidding-tracking-times?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TaskBiddingTrackingTimeCriteria implements Serializable {
    /**
     * Class for filtering BiddingScope
     */
    public static class BiddingScopeFilter extends Filter<BiddingScope> {
    }

    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter userLogin;

    private StringFilter role;

    private InstantFilter startTime;

    private InstantFilter endTime;

    private StringFilter startStatus;

    private StringFilter endStatus;

    private IntegerFilter duration;

    private BiddingScopeFilter biddingScope;

    private LongFilter taskId;

    public TaskBiddingTrackingTimeCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(StringFilter userLogin) {
        this.userLogin = userLogin;
    }

    public StringFilter getRole() {
        return role;
    }

    public void setRole(StringFilter role) {
        this.role = role;
    }

    public InstantFilter getStartTime() {
        return startTime;
    }

    public void setStartTime(InstantFilter startTime) {
        this.startTime = startTime;
    }

    public InstantFilter getEndTime() {
        return endTime;
    }

    public void setEndTime(InstantFilter endTime) {
        this.endTime = endTime;
    }

    public StringFilter getStartStatus() {
        return startStatus;
    }

    public void setStartStatus(StringFilter startStatus) {
        this.startStatus = startStatus;
    }

    public StringFilter getEndStatus() {
        return endStatus;
    }

    public void setEndStatus(StringFilter endStatus) {
        this.endStatus = endStatus;
    }

    public IntegerFilter getDuration() {
        return duration;
    }

    public void setDuration(IntegerFilter duration) {
        this.duration = duration;
    }

    public BiddingScopeFilter getBiddingScope() {
        return biddingScope;
    }

    public void setBiddingScope(BiddingScopeFilter biddingScope) {
        this.biddingScope = biddingScope;
    }

    public LongFilter getTaskId() {
        return taskId;
    }

    public void setTaskId(LongFilter taskId) {
        this.taskId = taskId;
    }

    @Override
    public String toString() {
        return "TaskBiddingTrackingTimeCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (userLogin != null ? "userLogin=" + userLogin + ", " : "") +
                (role != null ? "role=" + role + ", " : "") +
                (startTime != null ? "startTime=" + startTime + ", " : "") +
                (endTime != null ? "endTime=" + endTime + ", " : "") +
                (startStatus != null ? "startStatus=" + startStatus + ", " : "") +
                (endStatus != null ? "endStatus=" + endStatus + ", " : "") +
                (duration != null ? "duration=" + duration + ", " : "") +
                (biddingScope != null ? "biddingScope=" + biddingScope + ", " : "") +
                (taskId != null ? "taskId=" + taskId + ", " : "") +
            "}";
    }

}
