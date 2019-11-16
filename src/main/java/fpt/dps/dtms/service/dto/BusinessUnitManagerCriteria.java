package fpt.dps.dtms.service.dto;

import java.io.Serializable;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;


import io.github.jhipster.service.filter.LocalDateFilter;



/**
 * Criteria class for the BusinessUnitManager entity. This class is used in BusinessUnitManagerResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /business-unit-managers?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class BusinessUnitManagerCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private LocalDateFilter startTime;

    private LocalDateFilter endTime;

    private StringFilter description;

    private LongFilter businessUnitId;

    private LongFilter managerId;

    public BusinessUnitManagerCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LocalDateFilter getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateFilter startTime) {
        this.startTime = startTime;
    }

    public LocalDateFilter getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateFilter endTime) {
        this.endTime = endTime;
    }

    public StringFilter getDescription() {
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public LongFilter getBusinessUnitId() {
        return businessUnitId;
    }

    public void setBusinessUnitId(LongFilter businessUnitId) {
        this.businessUnitId = businessUnitId;
    }

    public LongFilter getManagerId() {
        return managerId;
    }

    public void setManagerId(LongFilter managerId) {
        this.managerId = managerId;
    }

    @Override
    public String toString() {
        return "BusinessUnitManagerCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (startTime != null ? "startTime=" + startTime + ", " : "") +
                (endTime != null ? "endTime=" + endTime + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (businessUnitId != null ? "businessUnitId=" + businessUnitId + ", " : "") +
                (managerId != null ? "managerId=" + managerId + ", " : "") +
            "}";
    }

}
