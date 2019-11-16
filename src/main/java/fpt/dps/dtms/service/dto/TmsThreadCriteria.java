package fpt.dps.dtms.service.dto;

import java.io.Serializable;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;






/**
 * Criteria class for the TmsThread entity. This class is used in TmsThreadResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /tms-threads?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TmsThreadCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter title;

    private IntegerFilter views;

    private IntegerFilter answers;

    private BooleanFilter closed;

    private BooleanFilter status;

    private LongFilter projectsId;

    private LongFilter assigneeId;

    public TmsThreadCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getTitle() {
        return title;
    }

    public void setTitle(StringFilter title) {
        this.title = title;
    }

    public IntegerFilter getViews() {
        return views;
    }

    public void setViews(IntegerFilter views) {
        this.views = views;
    }

    public IntegerFilter getAnswers() {
        return answers;
    }

    public void setAnswers(IntegerFilter answers) {
        this.answers = answers;
    }

    public BooleanFilter getClosed() {
        return closed;
    }

    public void setClosed(BooleanFilter closed) {
        this.closed = closed;
    }

    public BooleanFilter getStatus() {
        return status;
    }

    public void setStatus(BooleanFilter status) {
        this.status = status;
    }

    public LongFilter getProjectsId() {
        return projectsId;
    }

    public void setProjectsId(LongFilter projectsId) {
        this.projectsId = projectsId;
    }

    public LongFilter getAssigneeId() {
        return assigneeId;
    }

    public void setAssigneeId(LongFilter assigneeId) {
        this.assigneeId = assigneeId;
    }

    @Override
    public String toString() {
        return "TmsThreadCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (title != null ? "title=" + title + ", " : "") +
                (views != null ? "views=" + views + ", " : "") +
                (answers != null ? "answers=" + answers + ", " : "") +
                (closed != null ? "closed=" + closed + ", " : "") +
                (status != null ? "status=" + status + ", " : "") +
                (projectsId != null ? "projectsId=" + projectsId + ", " : "") +
                (assigneeId != null ? "assigneeId=" + assigneeId + ", " : "") +
            "}";
    }

}
