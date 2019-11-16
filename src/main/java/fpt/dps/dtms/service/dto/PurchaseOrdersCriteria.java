package fpt.dps.dtms.service.dto;

import java.io.Serializable;
import fpt.dps.dtms.domain.enumeration.PurchaseOrderStatus;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

import io.github.jhipster.service.filter.InstantFilter;




/**
 * Criteria class for the PurchaseOrders entity. This class is used in PurchaseOrdersResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /purchase-orders?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PurchaseOrdersCriteria implements Serializable {
    /**
     * Class for filtering PurchaseOrderStatus
     */
    public static class PurchaseOrderStatusFilter extends Filter<PurchaseOrderStatus> {
    }

    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter name;

    private PurchaseOrderStatusFilter status;

    private InstantFilter startTime;

    private InstantFilter endTime;

    private LongFilter tmsCustomFieldScreenValueId;

    private LongFilter projectId;

    private LongFilter purchaseOrderLeadId;

    private LongFilter packagesId;

    public PurchaseOrdersCriteria() {
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

    public PurchaseOrderStatusFilter getStatus() {
        return status;
    }

    public void setStatus(PurchaseOrderStatusFilter status) {
        this.status = status;
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

    public LongFilter getProjectId() {
        return projectId;
    }

    public void setProjectId(LongFilter projectId) {
        this.projectId = projectId;
    }

    public LongFilter getPurchaseOrderLeadId() {
        return purchaseOrderLeadId;
    }

    public void setPurchaseOrderLeadId(LongFilter purchaseOrderLeadId) {
        this.purchaseOrderLeadId = purchaseOrderLeadId;
    }

    public LongFilter getPackagesId() {
        return packagesId;
    }

    public void setPackagesId(LongFilter packagesId) {
        this.packagesId = packagesId;
    }

    @Override
    public String toString() {
        return "PurchaseOrdersCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (status != null ? "status=" + status + ", " : "") +
                (startTime != null ? "startTime=" + startTime + ", " : "") +
                (endTime != null ? "endTime=" + endTime + ", " : "") +
                (tmsCustomFieldScreenValueId != null ? "tmsCustomFieldScreenValueId=" + tmsCustomFieldScreenValueId + ", " : "") +
                (projectId != null ? "projectId=" + projectId + ", " : "") +
                (purchaseOrderLeadId != null ? "purchaseOrderLeadId=" + purchaseOrderLeadId + ", " : "") +
                (packagesId != null ? "packagesId=" + packagesId + ", " : "") +
            "}";
    }

}
