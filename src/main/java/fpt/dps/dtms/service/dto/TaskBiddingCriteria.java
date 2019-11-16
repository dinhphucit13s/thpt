package fpt.dps.dtms.service.dto;

import java.io.Serializable;
import fpt.dps.dtms.domain.enumeration.BiddingScope;
import fpt.dps.dtms.domain.enumeration.BiddingStatus;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;






/**
 * Criteria class for the TaskBidding entity. This class is used in TaskBiddingResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /task-biddings?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TaskBiddingCriteria implements Serializable {
    /**
     * Class for filtering BiddingScope
     */
    public static class BiddingScopeFilter extends Filter<BiddingScope> {
    }

    /**
     * Class for filtering BiddingStatus
     */
    public static class BiddingStatusFilter extends Filter<BiddingStatus> {
    }

    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private BiddingScopeFilter biddingScope;

    private BiddingStatusFilter biddingStatus;

    private LongFilter taskId;

    public TaskBiddingCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public BiddingScopeFilter getBiddingScope() {
        return biddingScope;
    }

    public void setBiddingScope(BiddingScopeFilter biddingScope) {
        this.biddingScope = biddingScope;
    }

    public BiddingStatusFilter getBiddingStatus() {
        return biddingStatus;
    }

    public void setBiddingStatus(BiddingStatusFilter biddingStatus) {
        this.biddingStatus = biddingStatus;
    }

    public LongFilter getTaskId() {
        return taskId;
    }

    public void setTaskId(LongFilter taskId) {
        this.taskId = taskId;
    }

    @Override
    public String toString() {
        return "TaskBiddingCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (biddingScope != null ? "biddingScope=" + biddingScope + ", " : "") +
                (biddingStatus != null ? "biddingStatus=" + biddingStatus + ", " : "") +
                (taskId != null ? "taskId=" + taskId + ", " : "") +
            "}";
    }

}
