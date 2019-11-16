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
 * Criteria class for the TMSLogHistory entity. This class is used in TMSLogHistoryResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /tms-log-histories?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TMSLogHistoryCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter action;

    private LongFilter projectsId;

    private LongFilter purchaseOrdersId;

    private LongFilter packagesId;

    private LongFilter tasksId;

    public TMSLogHistoryCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getAction() {
        return action;
    }

    public void setAction(StringFilter action) {
        this.action = action;
    }

    public LongFilter getProjectsId() {
        return projectsId;
    }

    public void setProjectsId(LongFilter projectsId) {
        this.projectsId = projectsId;
    }

    public LongFilter getPurchaseOrdersId() {
        return purchaseOrdersId;
    }

    public void setPurchaseOrdersId(LongFilter purchaseOrdersId) {
        this.purchaseOrdersId = purchaseOrdersId;
    }

    public LongFilter getPackagesId() {
        return packagesId;
    }

    public void setPackagesId(LongFilter packagesId) {
        this.packagesId = packagesId;
    }

    public LongFilter getTasksId() {
        return tasksId;
    }

    public void setTasksId(LongFilter tasksId) {
        this.tasksId = tasksId;
    }

    @Override
    public String toString() {
        return "TMSLogHistoryCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (action != null ? "action=" + action + ", " : "") +
                (projectsId != null ? "projectsId=" + projectsId + ", " : "") +
                (purchaseOrdersId != null ? "purchaseOrdersId=" + purchaseOrdersId + ", " : "") +
                (packagesId != null ? "packagesId=" + packagesId + ", " : "") +
                (tasksId != null ? "tasksId=" + tasksId + ", " : "") +
            "}";
    }

}
