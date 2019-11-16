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
 * Criteria class for the TMSCustomFieldScreen entity. This class is used in TMSCustomFieldScreenResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /tms-custom-field-screens?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TMSCustomFieldScreenCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private IntegerFilter sequence;

    private LongFilter tmsCustomFieldId;

    private LongFilter projectWorkflowsId;

    public TMSCustomFieldScreenCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public IntegerFilter getSequence() {
        return sequence;
    }

    public void setSequence(IntegerFilter sequence) {
        this.sequence = sequence;
    }

    public LongFilter getTmsCustomFieldId() {
        return tmsCustomFieldId;
    }

    public void setTmsCustomFieldId(LongFilter tmsCustomFieldId) {
        this.tmsCustomFieldId = tmsCustomFieldId;
    }

    public LongFilter getProjectWorkflowsId() {
        return projectWorkflowsId;
    }

    public void setProjectWorkflowsId(LongFilter projectWorkflowsId) {
        this.projectWorkflowsId = projectWorkflowsId;
    }

    @Override
    public String toString() {
        return "TMSCustomFieldScreenCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (sequence != null ? "sequence=" + sequence + ", " : "") +
                (tmsCustomFieldId != null ? "tmsCustomFieldId=" + tmsCustomFieldId + ", " : "") +
                (projectWorkflowsId != null ? "projectWorkflowsId=" + projectWorkflowsId + ", " : "") +
            "}";
    }

}
