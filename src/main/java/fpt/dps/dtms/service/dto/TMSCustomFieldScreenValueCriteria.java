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
 * Criteria class for the TMSCustomFieldScreenValue entity. This class is used in TMSCustomFieldScreenValueResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /tms-custom-field-screen-values?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TMSCustomFieldScreenValueCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter value;

    private LongFilter purchaseOrdersId;

    private LongFilter packagesId;

    private LongFilter tasksId;

    private LongFilter tmsCustomFieldId;

    public TMSCustomFieldScreenValueCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getValue() {
        return value;
    }

    public void setValue(StringFilter value) {
        this.value = value;
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

    public LongFilter getTmsCustomFieldId() {
        return tmsCustomFieldId;
    }

    public void setTmsCustomFieldId(LongFilter tmsCustomFieldId) {
        this.tmsCustomFieldId = tmsCustomFieldId;
    }

    @Override
    public String toString() {
        return "TMSCustomFieldScreenValueCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (value != null ? "value=" + value + ", " : "") +
                (purchaseOrdersId != null ? "purchaseOrdersId=" + purchaseOrdersId + ", " : "") +
                (packagesId != null ? "packagesId=" + packagesId + ", " : "") +
                (tasksId != null ? "tasksId=" + tasksId + ", " : "") +
                (tmsCustomFieldId != null ? "tmsCustomFieldId=" + tmsCustomFieldId + ", " : "") +
            "}";
    }

}
