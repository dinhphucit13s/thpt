package fpt.dps.dtms.service.dto;

import java.io.Serializable;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

import io.github.jhipster.service.filter.InstantFilter;




/**
 * Criteria class for the Packages entity. This class is used in PackagesResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /packages?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PackagesCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter name;

    private StringFilter op;

    private StringFilter reviewer;

    private StringFilter fi;

    private StringFilter delivery;

    private InstantFilter estimateDelivery;

    private IntegerFilter target;

    private InstantFilter startTime;

    private InstantFilter endTime;

    private LongFilter tmsCustomFieldScreenValueId;

    private LongFilter purchaseOrdersId;

    private LongFilter tasksId;

    public PackagesCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getOp() {
        return op;
    }

    public void setOp(StringFilter op) {
        this.op = op;
    }

    public StringFilter getReviewer() {
        return reviewer;
    }

    public void setReviewer(StringFilter reviewer) {
        this.reviewer = reviewer;
    }

    public StringFilter getFi() {
        return fi;
    }

    public void setFi(StringFilter fi) {
        this.fi = fi;
    }

    public StringFilter getDelivery() {
        return delivery;
    }

    public void setDelivery(StringFilter delivery) {
        this.delivery = delivery;
    }

    public InstantFilter getEstimateDelivery() {
        return estimateDelivery;
    }

    public void setEstimateDelivery(InstantFilter estimateDelivery) {
        this.estimateDelivery = estimateDelivery;
    }

    public IntegerFilter getTarget() {
        return target;
    }

    public void setTarget(IntegerFilter target) {
        this.target = target;
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

    public LongFilter getTmsCustomFieldScreenValueId() {
        return tmsCustomFieldScreenValueId;
    }

    public void setTmsCustomFieldScreenValueId(LongFilter tmsCustomFieldScreenValueId) {
        this.tmsCustomFieldScreenValueId = tmsCustomFieldScreenValueId;
    }

    public LongFilter getPurchaseOrdersId() {
        return purchaseOrdersId;
    }

    public void setPurchaseOrdersId(LongFilter purchaseOrdersId) {
        this.purchaseOrdersId = purchaseOrdersId;
    }

    public LongFilter getTasksId() {
        return tasksId;
    }

    public void setTasksId(LongFilter tasksId) {
        this.tasksId = tasksId;
    }

    @Override
    public String toString() {
        return "PackagesCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (op != null ? "op=" + op + ", " : "") +
                (reviewer != null ? "reviewer=" + reviewer + ", " : "") +
                (fi != null ? "fi=" + fi + ", " : "") +
                (delivery != null ? "delivery=" + delivery + ", " : "") +
                (estimateDelivery != null ? "estimateDelivery=" + estimateDelivery + ", " : "") +
                (target != null ? "target=" + target + ", " : "") +
                (startTime != null ? "startTime=" + startTime + ", " : "") +
                (endTime != null ? "endTime=" + endTime + ", " : "") +
                (tmsCustomFieldScreenValueId != null ? "tmsCustomFieldScreenValueId=" + tmsCustomFieldScreenValueId + ", " : "") +
                (purchaseOrdersId != null ? "purchaseOrdersId=" + purchaseOrdersId + ", " : "") +
                (tasksId != null ? "tasksId=" + tasksId + ", " : "") +
            "}";
    }

}
