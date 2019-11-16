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
 * Criteria class for the ProjectWorkflows entity. This class is used in ProjectWorkflowsResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /project-workflows?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ProjectWorkflowsCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter name;

    private IntegerFilter step;

    private StringFilter nextURI;

    private StringFilter activity;

    private LongFilter projectTemplatesId;

    public ProjectWorkflowsCriteria() {
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

    public IntegerFilter getStep() {
        return step;
    }

    public void setStep(IntegerFilter step) {
        this.step = step;
    }

    public StringFilter getNextURI() {
        return nextURI;
    }

    public void setNextURI(StringFilter nextURI) {
        this.nextURI = nextURI;
    }

    public StringFilter getActivity() {
        return activity;
    }

    public void setActivity(StringFilter activity) {
        this.activity = activity;
    }

    public LongFilter getProjectTemplatesId() {
        return projectTemplatesId;
    }

    public void setProjectTemplatesId(LongFilter projectTemplatesId) {
        this.projectTemplatesId = projectTemplatesId;
    }

    @Override
    public String toString() {
        return "ProjectWorkflowsCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (step != null ? "step=" + step + ", " : "") +
                (nextURI != null ? "nextURI=" + nextURI + ", " : "") +
                (activity != null ? "activity=" + activity + ", " : "") +
                (projectTemplatesId != null ? "projectTemplatesId=" + projectTemplatesId + ", " : "") +
            "}";
    }

}
