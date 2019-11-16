package fpt.dps.dtms.service.dto;

import java.io.Serializable;
import fpt.dps.dtms.domain.enumeration.TaskSeverity;
import fpt.dps.dtms.domain.enumeration.TaskPriority;
import fpt.dps.dtms.domain.enumeration.TaskAvailability;
import fpt.dps.dtms.domain.enumeration.OPStatus;
import fpt.dps.dtms.domain.enumeration.ReviewStatus;
import fpt.dps.dtms.domain.enumeration.FixStatus;
import fpt.dps.dtms.domain.enumeration.ReviewStatus;
import fpt.dps.dtms.domain.enumeration.FIStatus;
import fpt.dps.dtms.domain.enumeration.ErrorSeverity;
import fpt.dps.dtms.domain.enumeration.TaskStatus;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

import io.github.jhipster.service.filter.InstantFilter;




/**
 * Criteria class for the Tasks entity. This class is used in TasksResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /tasks?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TasksCriteria implements Serializable {
    /**
     * Class for filtering TaskSeverity
     */
    public static class TaskSeverityFilter extends Filter<TaskSeverity> {
    }

    /**
     * Class for filtering TaskPriority
     */
    public static class TaskPriorityFilter extends Filter<TaskPriority> {
    }

    /**
     * Class for filtering TaskAvailability
     */
    public static class TaskAvailabilityFilter extends Filter<TaskAvailability> {
    }

    /**
     * Class for filtering OPStatus
     */
    public static class OPStatusFilter extends Filter<OPStatus> {
    }

    /**
     * Class for filtering ReviewStatus
     */
    public static class ReviewStatusFilter extends Filter<ReviewStatus> {
    }

    /**
     * Class for filtering FixStatus
     */
    public static class FixStatusFilter extends Filter<FixStatus> {
    }

    /**
     * Class for filtering FIStatus
     */
    public static class FIStatusFilter extends Filter<FIStatus> {
    }

    /**
     * Class for filtering ErrorSeverity
     */
    public static class ErrorSeverityFilter extends Filter<ErrorSeverity> {
    }

    /**
     * Class for filtering TaskStatus
     */
    public static class TaskStatusFilter extends Filter<TaskStatus> {
    }

    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter name;

    private TaskSeverityFilter severity;

    private TaskPriorityFilter priority;

    private StringFilter data;

    private StringFilter fileName;

    private StringFilter type;

    private TaskAvailabilityFilter availability;

    private IntegerFilter frame;

    private IntegerFilter actualObject;

    private OPStatusFilter opStatus;

    private InstantFilter estimateStartTime;

    private InstantFilter estimateEndTime;

    private InstantFilter opStartTime;

    private InstantFilter opEndTime;

    private ReviewStatusFilter review1Status;

    private InstantFilter review1StartTime;

    private InstantFilter review1EndTime;

    private FixStatusFilter fixStatus;

    private InstantFilter fixStartTime;

    private InstantFilter fixEndTime;

    private ReviewStatusFilter review2Status;

    private InstantFilter review2StartTime;

    private InstantFilter review2EndTime;

    private FIStatusFilter fiStatus;

    private InstantFilter fiStartTime;

    private InstantFilter fiEndTime;

    private IntegerFilter duration;

    private IntegerFilter target;

    private IntegerFilter errorQuantity;

    private ErrorSeverityFilter errorSeverity;

    private TaskStatusFilter status;

    private LongFilter parent;

    private StringFilter op;

    private StringFilter review1;

    private StringFilter review2;

    private StringFilter fixer;

    private StringFilter fi;

    private LongFilter tmsCustomFieldScreenValueId;

    private LongFilter packagesId;

    public TasksCriteria() {
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

    public TaskSeverityFilter getSeverity() {
        return severity;
    }

    public void setSeverity(TaskSeverityFilter severity) {
        this.severity = severity;
    }

    public TaskPriorityFilter getPriority() {
        return priority;
    }

    public void setPriority(TaskPriorityFilter priority) {
        this.priority = priority;
    }

    public StringFilter getData() {
        return data;
    }

    public void setData(StringFilter data) {
        this.data = data;
    }

    public StringFilter getFileName() {
        return fileName;
    }

    public void setFileName(StringFilter fileName) {
        this.fileName = fileName;
    }

    public StringFilter getType() {
        return type;
    }

    public void setType(StringFilter type) {
        this.type = type;
    }

    public TaskAvailabilityFilter getAvailability() {
        return availability;
    }

    public void setAvailability(TaskAvailabilityFilter availability) {
        this.availability = availability;
    }

    public IntegerFilter getFrame() {
        return frame;
    }

    public void setFrame(IntegerFilter frame) {
        this.frame = frame;
    }

    public IntegerFilter getActualObject() {
        return actualObject;
    }

    public void setActualObject(IntegerFilter actualObject) {
        this.actualObject = actualObject;
    }

    public OPStatusFilter getOpStatus() {
        return opStatus;
    }

    public void setOpStatus(OPStatusFilter opStatus) {
        this.opStatus = opStatus;
    }

    public InstantFilter getEstimateStartTime() {
        return estimateStartTime;
    }

    public void setEstimateStartTime(InstantFilter estimateStartTime) {
        this.estimateStartTime = estimateStartTime;
    }

    public InstantFilter getEstimateEndTime() {
        return estimateEndTime;
    }

    public void setEstimateEndTime(InstantFilter estimateEndTime) {
        this.estimateEndTime = estimateEndTime;
    }

    public InstantFilter getOpStartTime() {
        return opStartTime;
    }

    public void setOpStartTime(InstantFilter opStartTime) {
        this.opStartTime = opStartTime;
    }

    public InstantFilter getOpEndTime() {
        return opEndTime;
    }

    public void setOpEndTime(InstantFilter opEndTime) {
        this.opEndTime = opEndTime;
    }

    public ReviewStatusFilter getReview1Status() {
        return review1Status;
    }

    public void setReview1Status(ReviewStatusFilter review1Status) {
        this.review1Status = review1Status;
    }

    public InstantFilter getReview1StartTime() {
        return review1StartTime;
    }

    public void setReview1StartTime(InstantFilter review1StartTime) {
        this.review1StartTime = review1StartTime;
    }

    public InstantFilter getReview1EndTime() {
        return review1EndTime;
    }

    public void setReview1EndTime(InstantFilter review1EndTime) {
        this.review1EndTime = review1EndTime;
    }

    public FixStatusFilter getFixStatus() {
        return fixStatus;
    }

    public void setFixStatus(FixStatusFilter fixStatus) {
        this.fixStatus = fixStatus;
    }

    public InstantFilter getFixStartTime() {
        return fixStartTime;
    }

    public void setFixStartTime(InstantFilter fixStartTime) {
        this.fixStartTime = fixStartTime;
    }

    public InstantFilter getFixEndTime() {
        return fixEndTime;
    }

    public void setFixEndTime(InstantFilter fixEndTime) {
        this.fixEndTime = fixEndTime;
    }

    public ReviewStatusFilter getReview2Status() {
        return review2Status;
    }

    public void setReview2Status(ReviewStatusFilter review2Status) {
        this.review2Status = review2Status;
    }

    public InstantFilter getReview2StartTime() {
        return review2StartTime;
    }

    public void setReview2StartTime(InstantFilter review2StartTime) {
        this.review2StartTime = review2StartTime;
    }

    public InstantFilter getReview2EndTime() {
        return review2EndTime;
    }

    public void setReview2EndTime(InstantFilter review2EndTime) {
        this.review2EndTime = review2EndTime;
    }

    public FIStatusFilter getFiStatus() {
        return fiStatus;
    }

    public void setFiStatus(FIStatusFilter fiStatus) {
        this.fiStatus = fiStatus;
    }

    public InstantFilter getFiStartTime() {
        return fiStartTime;
    }

    public void setFiStartTime(InstantFilter fiStartTime) {
        this.fiStartTime = fiStartTime;
    }

    public InstantFilter getFiEndTime() {
        return fiEndTime;
    }

    public void setFiEndTime(InstantFilter fiEndTime) {
        this.fiEndTime = fiEndTime;
    }

    public IntegerFilter getDuration() {
        return duration;
    }

    public void setDuration(IntegerFilter duration) {
        this.duration = duration;
    }

    public IntegerFilter getTarget() {
        return target;
    }

    public void setTarget(IntegerFilter target) {
        this.target = target;
    }

    public IntegerFilter getErrorQuantity() {
        return errorQuantity;
    }

    public void setErrorQuantity(IntegerFilter errorQuantity) {
        this.errorQuantity = errorQuantity;
    }

    public ErrorSeverityFilter getErrorSeverity() {
        return errorSeverity;
    }

    public void setErrorSeverity(ErrorSeverityFilter errorSeverity) {
        this.errorSeverity = errorSeverity;
    }

    public TaskStatusFilter getStatus() {
        return status;
    }

    public void setStatus(TaskStatusFilter status) {
        this.status = status;
    }

    public LongFilter getParent() {
        return parent;
    }

    public void setParent(LongFilter parent) {
        this.parent = parent;
    }

    public StringFilter getOp() {
        return op;
    }

    public void setOp(StringFilter op) {
        this.op = op;
    }

    public StringFilter getReview1() {
        return review1;
    }

    public void setReview1(StringFilter review1) {
        this.review1 = review1;
    }

    public StringFilter getReview2() {
        return review2;
    }

    public void setReview2(StringFilter review2) {
        this.review2 = review2;
    }

    public StringFilter getFixer() {
        return fixer;
    }

    public void setFixer(StringFilter fixer) {
        this.fixer = fixer;
    }

    public StringFilter getFi() {
        return fi;
    }

    public void setFi(StringFilter fi) {
        this.fi = fi;
    }

    public LongFilter getTmsCustomFieldScreenValueId() {
        return tmsCustomFieldScreenValueId;
    }

    public void setTmsCustomFieldScreenValueId(LongFilter tmsCustomFieldScreenValueId) {
        this.tmsCustomFieldScreenValueId = tmsCustomFieldScreenValueId;
    }

    public LongFilter getPackagesId() {
        return packagesId;
    }

    public void setPackagesId(LongFilter packagesId) {
        this.packagesId = packagesId;
    }

    @Override
    public String toString() {
        return "TasksCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (severity != null ? "severity=" + severity + ", " : "") +
                (priority != null ? "priority=" + priority + ", " : "") +
                (data != null ? "data=" + data + ", " : "") +
                (fileName != null ? "fileName=" + fileName + ", " : "") +
                (type != null ? "type=" + type + ", " : "") +
                (availability != null ? "availability=" + availability + ", " : "") +
                (frame != null ? "frame=" + frame + ", " : "") +
                (actualObject != null ? "actualObject=" + actualObject + ", " : "") +
                (opStatus != null ? "opStatus=" + opStatus + ", " : "") +
                (estimateStartTime != null ? "estimateStartTime=" + estimateStartTime + ", " : "") +
                (estimateEndTime != null ? "estimateEndTime=" + estimateEndTime + ", " : "") +
                (opStartTime != null ? "opStartTime=" + opStartTime + ", " : "") +
                (opEndTime != null ? "opEndTime=" + opEndTime + ", " : "") +
                (review1Status != null ? "review1Status=" + review1Status + ", " : "") +
                (review1StartTime != null ? "review1StartTime=" + review1StartTime + ", " : "") +
                (review1EndTime != null ? "review1EndTime=" + review1EndTime + ", " : "") +
                (fixStatus != null ? "fixStatus=" + fixStatus + ", " : "") +
                (fixStartTime != null ? "fixStartTime=" + fixStartTime + ", " : "") +
                (fixEndTime != null ? "fixEndTime=" + fixEndTime + ", " : "") +
                (review2Status != null ? "review2Status=" + review2Status + ", " : "") +
                (review2StartTime != null ? "review2StartTime=" + review2StartTime + ", " : "") +
                (review2EndTime != null ? "review2EndTime=" + review2EndTime + ", " : "") +
                (fiStatus != null ? "fiStatus=" + fiStatus + ", " : "") +
                (fiStartTime != null ? "fiStartTime=" + fiStartTime + ", " : "") +
                (fiEndTime != null ? "fiEndTime=" + fiEndTime + ", " : "") +
                (duration != null ? "duration=" + duration + ", " : "") +
                (target != null ? "target=" + target + ", " : "") +
                (errorQuantity != null ? "errorQuantity=" + errorQuantity + ", " : "") +
                (errorSeverity != null ? "errorSeverity=" + errorSeverity + ", " : "") +
                (status != null ? "status=" + status + ", " : "") +
                (parent != null ? "parent=" + parent + ", " : "") +
                (op != null ? "op=" + op + ", " : "") +
                (review1 != null ? "review1=" + review1 + ", " : "") +
                (review2 != null ? "review2=" + review2 + ", " : "") +
                (fixer != null ? "fixer=" + fixer + ", " : "") +
                (fi != null ? "fi=" + fi + ", " : "") +
                (tmsCustomFieldScreenValueId != null ? "tmsCustomFieldScreenValueId=" + tmsCustomFieldScreenValueId + ", " : "") +
                (packagesId != null ? "packagesId=" + packagesId + ", " : "") +
            "}";
    }

}
