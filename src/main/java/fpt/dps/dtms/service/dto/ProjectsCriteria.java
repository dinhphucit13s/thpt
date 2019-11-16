package fpt.dps.dtms.service.dto;

import java.io.Serializable;
import fpt.dps.dtms.domain.enumeration.ProjectType;
import fpt.dps.dtms.domain.enumeration.ProjectStatus;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

import io.github.jhipster.service.filter.InstantFilter;




/**
 * Criteria class for the Projects entity. This class is used in ProjectsResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /projects?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ProjectsCriteria implements Serializable {
    /**
     * Class for filtering ProjectType
     */
    public static class ProjectTypeFilter extends Filter<ProjectType> {
    }

    /**
     * Class for filtering ProjectStatus
     */
    public static class ProjectStatusFilter extends Filter<ProjectStatus> {
    }

    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter code;

    private StringFilter name;

    private ProjectTypeFilter type;

    private ProjectStatusFilter status;

    private InstantFilter startTime;

    private InstantFilter endTime;

    private LongFilter projectTemplatesId;

    private LongFilter projectLeadId;

    private StringFilter projectLeadUserLogin;

    private LongFilter purchaseOrdersId;

    private LongFilter projectUsersId;

    private LongFilter customerId;

    private LongFilter projectBugListDefaultId;

    private LongFilter businessUnitId;

    public ProjectsCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getCode() {
        return code;
    }

    public void setCode(StringFilter code) {
        this.code = code;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public ProjectTypeFilter getType() {
        return type;
    }

    public void setType(ProjectTypeFilter type) {
        this.type = type;
    }

    public ProjectStatusFilter getStatus() {
        return status;
    }

    public void setStatus(ProjectStatusFilter status) {
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

    public LongFilter getProjectTemplatesId() {
        return projectTemplatesId;
    }

    public void setProjectTemplatesId(LongFilter projectTemplatesId) {
        this.projectTemplatesId = projectTemplatesId;
    }

    public LongFilter getProjectLeadId() {
        return projectLeadId;
    }

    public void setProjectLeadId(LongFilter projectLeadId) {
        this.projectLeadId = projectLeadId;
    }

    public StringFilter getProjectLeadUserLogin() {
		return projectLeadUserLogin;
	}

	public void setProjectLeadUserLogin(StringFilter projectLeadUserLogin) {
		this.projectLeadUserLogin = projectLeadUserLogin;
	}

	public LongFilter getPurchaseOrdersId() {
        return purchaseOrdersId;
    }

    public void setPurchaseOrdersId(LongFilter purchaseOrdersId) {
        this.purchaseOrdersId = purchaseOrdersId;
    }

    public LongFilter getProjectUsersId() {
        return projectUsersId;
    }

    public void setProjectUsersId(LongFilter projectUsersId) {
        this.projectUsersId = projectUsersId;
    }

    public LongFilter getCustomerId() {
        return customerId;
    }

    public void setCustomerId(LongFilter customerId) {
        this.customerId = customerId;
    }

    public LongFilter getProjectBugListDefaultId() {
        return projectBugListDefaultId;
    }

    public void setProjectBugListDefaultId(LongFilter projectBugListDefaultId) {
        this.projectBugListDefaultId = projectBugListDefaultId;
    }

    public LongFilter getBusinessUnitId() {
        return businessUnitId;
    }

    public void setBusinessUnitId(LongFilter businessUnitId) {
        this.businessUnitId = businessUnitId;
    }

    @Override
    public String toString() {
        return "ProjectsCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (code != null ? "code=" + code + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (type != null ? "type=" + type + ", " : "") +
                (status != null ? "status=" + status + ", " : "") +
                (startTime != null ? "startTime=" + startTime + ", " : "") +
                (endTime != null ? "endTime=" + endTime + ", " : "") +
                (projectTemplatesId != null ? "projectTemplatesId=" + projectTemplatesId + ", " : "") +
                (projectLeadId != null ? "projectLeadId=" + projectLeadId + ", " : "") +
                (purchaseOrdersId != null ? "purchaseOrdersId=" + purchaseOrdersId + ", " : "") +
                (projectUsersId != null ? "projectUsersId=" + projectUsersId + ", " : "") +
                (customerId != null ? "customerId=" + customerId + ", " : "") +
                (projectBugListDefaultId != null ? "projectBugListDefaultId=" + projectBugListDefaultId + ", " : "") +
                (businessUnitId != null ? "businessUnitId=" + businessUnitId + ", " : "") +
            "}";
    }

}
