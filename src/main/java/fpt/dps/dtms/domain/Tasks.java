package fpt.dps.dtms.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import fpt.dps.dtms.domain.enumeration.TaskSeverity;

import fpt.dps.dtms.domain.enumeration.TaskPriority;

import fpt.dps.dtms.domain.enumeration.TaskAvailability;

import fpt.dps.dtms.domain.enumeration.OPStatus;

import fpt.dps.dtms.domain.enumeration.ReviewStatus;

import fpt.dps.dtms.domain.enumeration.FixStatus;

import fpt.dps.dtms.domain.enumeration.FIStatus;

import fpt.dps.dtms.domain.enumeration.ErrorSeverity;

import fpt.dps.dtms.domain.enumeration.TaskStatus;

/**
 * The Task entity.
 */
@ApiModel(description = "The Task entity.")
@Entity
@Table(name = "tasks")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "tasks")
public class Tasks extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "severity")
    private TaskSeverity severity;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority")
    private TaskPriority priority;

    @Size(max = 255)
    @Column(name = "data", length = 255)
    private String data;

    @Size(max = 2000)
    @Column(name = "file_name", length = 2000)
    private String fileName;

    @Column(name = "jhi_type")
    private String type;

    @Enumerated(EnumType.STRING)
    @Column(name = "availability")
    private TaskAvailability availability;

    @Column(name = "frame")
    private Integer frame;

    @Column(name = "actual_object")
    private Integer actualObject;

    @Enumerated(EnumType.STRING)
    @Column(name = "op_status")
    private OPStatus opStatus;

    @Column(name = "estimate_start_time")
    private Instant estimateStartTime;

    @Column(name = "estimate_end_time")
    private Instant estimateEndTime;

    @Column(name = "op_start_time")
    private Instant opStartTime;

    @Column(name = "op_end_time")
    private Instant opEndTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "review_1_status")
    private ReviewStatus review1Status;

    @Column(name = "review_1_start_time")
    private Instant review1StartTime;

    @Column(name = "review_1_end_time")
    private Instant review1EndTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "fix_status")
    private FixStatus fixStatus;

    @Column(name = "fix_start_time")
    private Instant fixStartTime;

    @Column(name = "fix_end_time")
    private Instant fixEndTime;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "review_2_status")
    private ReviewStatus review2Status;

    @Column(name = "review_2_start_time")
    private Instant review2StartTime;

    @Column(name = "review_2_end_time")
    private Instant review2EndTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "fi_status")
    private FIStatus fiStatus;

    @Column(name = "fi_start_time")
    private Instant fiStartTime;

    @Column(name = "fi_end_time")
    private Instant fiEndTime;

    @Column(name = "duration")
    private Integer duration;

    @Column(name = "target")
    private Integer target;

    @Column(name = "error_quantity")
    private Integer errorQuantity;

    @Enumerated(EnumType.STRING)
    @Column(name = "error_severity")
    private ErrorSeverity errorSeverity;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private TaskStatus status;

    @Lob
    @Column(name = "description")
    private String description;

    @Column(name = "parent")
    private Long parent;

    @Size(max = 50)
    @Column(name = "op", length = 50)
    private String op;

    @Size(max = 50)
    @Column(name = "review_1", length = 50)
    private String review1;

    @Size(max = 50)
    @Column(name = "review_2", length = 50)
    private String review2;

    @Size(max = 50)
    @Column(name = "fixer", length = 50)
    private String fixer;
    
    @Size(max = 50)
    @Column(name = "fi", length = 50)
    private String fi;

    @OneToMany(mappedBy = "tasks")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<TMSCustomFieldScreenValue> tmsCustomFieldScreenValues = new HashSet<>();

    @ManyToOne
    private Packages packages;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public Long getId() {
        return id;
	}
	 
	public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Tasks name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TaskSeverity getSeverity() {
        return severity;
    }

    public Tasks severity(TaskSeverity severity) {
        this.severity = severity;
        return this;
    }

    public void setSeverity(TaskSeverity severity) {
        this.severity = severity;
    }

    public TaskPriority getPriority() {
        return priority;
    }

    public Tasks priority(TaskPriority priority) {
        this.priority = priority;
        return this;
    }

    public void setPriority(TaskPriority priority) {
        this.priority = priority;
    }

    public String getData() {
        return data;
    }

    public Tasks data(String data) {
        this.data = data;
        return this;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getFileName() {
        return fileName;
    }

    public Tasks fileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getType() {
        return type;
    }

    public Tasks type(String type) {
        this.type = type;
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public TaskAvailability getAvailability() {
        return availability;
    }

    public Tasks availability(TaskAvailability availability) {
        this.availability = availability;
        return this;
    }

    public void setAvailability(TaskAvailability availability) {
        this.availability = availability;
    }

    public Integer getFrame() {
        return frame;
    }

    public Tasks frame(Integer frame) {
        this.frame = frame;
        return this;
    }

    public void setFrame(Integer frame) {
        this.frame = frame;
    }

    public Integer getActualObject() {
        return actualObject;
    }

    public Tasks actualObject(Integer actualObject) {
        this.actualObject = actualObject;
        return this;
    }

    public void setActualObject(Integer actualObject) {
        this.actualObject = actualObject;
    }

    public OPStatus getOpStatus() {
        return opStatus;
    }

    public Tasks opStatus(OPStatus opStatus) {
        this.opStatus = opStatus;
        return this;
    }

    public void setOpStatus(OPStatus opStatus) {
        this.opStatus = opStatus;
    }

    public Instant getEstimateStartTime() {
        return estimateStartTime;
    }

    public Tasks estimateStartTime(Instant estimateStartTime) {
        this.estimateStartTime = estimateStartTime;
        return this;
    }

    public void setEstimateStartTime(Instant estimateStartTime) {
        this.estimateStartTime = estimateStartTime;
    }

    public Instant getEstimateEndTime() {
        return estimateEndTime;
    }

    public Tasks estimateEndTime(Instant estimateEndTime) {
        this.estimateEndTime = estimateEndTime;
        return this;
    }

    public void setEstimateEndTime(Instant estimateEndTime) {
        this.estimateEndTime = estimateEndTime;
    }

    public Instant getOpStartTime() {
        return opStartTime;
    }

    public Tasks opStartTime(Instant opStartTime) {
        this.opStartTime = opStartTime;
        return this;
    }

    public void setOpStartTime(Instant opStartTime) {
        this.opStartTime = opStartTime;
    }

    public Instant getOpEndTime() {
        return opEndTime;
    }

    public Tasks opEndTime(Instant opEndTime) {
        this.opEndTime = opEndTime;
        return this;
    }

    public void setOpEndTime(Instant opEndTime) {
        this.opEndTime = opEndTime;
    }

    public ReviewStatus getReview1Status() {
        return review1Status;
    }

    public Tasks review1Status(ReviewStatus review1Status) {
        this.review1Status = review1Status;
        return this;
    }

    public void setReview1Status(ReviewStatus review1Status) {
        this.review1Status = review1Status;
    }

    public Instant getReview1StartTime() {
        return review1StartTime;
    }

    public Tasks review1StartTime(Instant review1StartTime) {
        this.review1StartTime = review1StartTime;
        return this;
    }

    public void setReview1StartTime(Instant review1StartTime) {
        this.review1StartTime = review1StartTime;
    }

    public Instant getReview1EndTime() {
        return review1EndTime;
    }

    public Tasks review1EndTime(Instant review1EndTime) {
        this.review1EndTime = review1EndTime;
        return this;
    }

    public void setReview1EndTime(Instant review1EndTime) {
        this.review1EndTime = review1EndTime;
    }

    public FixStatus getFixStatus() {
        return fixStatus;
    }

    public Tasks fixStatus(FixStatus fixStatus) {
        this.fixStatus = fixStatus;
        return this;
    }

    public void setFixStatus(FixStatus fixStatus) {
        this.fixStatus = fixStatus;
    }

    public Instant getFixStartTime() {
        return fixStartTime;
    }

    public Tasks fixStartTime(Instant fixStartTime) {
        this.fixStartTime = fixStartTime;
        return this;
    }

    public void setFixStartTime(Instant fixStartTime) {
        this.fixStartTime = fixStartTime;
    }

    public Instant getFixEndTime() {
        return fixEndTime;
    }

    public Tasks fixEndTime(Instant fixEndTime) {
        this.fixEndTime = fixEndTime;
        return this;
    }

    public void setFixEndTime(Instant fixEndTime) {
        this.fixEndTime = fixEndTime;
    }

    public ReviewStatus getReview2Status() {
        return review2Status;
    }

    public Tasks review2Status(ReviewStatus review2Status) {
        this.review2Status = review2Status;
        return this;
    }

    public void setReview2Status(ReviewStatus review2Status) {
        this.review2Status = review2Status;
    }

    public Instant getReview2StartTime() {
        return review2StartTime;
    }

    public Tasks review2StartTime(Instant review2StartTime) {
        this.review2StartTime = review2StartTime;
        return this;
    }

    public void setReview2StartTime(Instant review2StartTime) {
        this.review2StartTime = review2StartTime;
    }

    public Instant getReview2EndTime() {
        return review2EndTime;
    }

    public Tasks review2EndTime(Instant review2EndTime) {
        this.review2EndTime = review2EndTime;
        return this;
    }

    public void setReview2EndTime(Instant review2EndTime) {
        this.review2EndTime = review2EndTime;
    }

    public FIStatus getFiStatus() {
        return fiStatus;
    }

    public Tasks fiStatus(FIStatus fiStatus) {
        this.fiStatus = fiStatus;
        return this;
    }

    public void setFiStatus(FIStatus fiStatus) {
        this.fiStatus = fiStatus;
    }

    public Instant getFiStartTime() {
        return fiStartTime;
    }

    public Tasks fiStartTime(Instant fiStartTime) {
        this.fiStartTime = fiStartTime;
        return this;
    }

    public void setFiStartTime(Instant fiStartTime) {
        this.fiStartTime = fiStartTime;
    }

    public Instant getFiEndTime() {
        return fiEndTime;
    }

    public Tasks fiEndTime(Instant fiEndTime) {
        this.fiEndTime = fiEndTime;
        return this;
    }

    public void setFiEndTime(Instant fiEndTime) {
        this.fiEndTime = fiEndTime;
    }

    public Integer getDuration() {
        return duration;
    }

    public Tasks duration(Integer duration) {
        this.duration = duration;
        return this;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getTarget() {
        return target;
    }

    public Tasks target(Integer target) {
        this.target = target;
        return this;
    }

    public void setTarget(Integer target) {
        this.target = target;
    }

    public Integer getErrorQuantity() {
        return errorQuantity;
    }

    public Tasks errorQuantity(Integer errorQuantity) {
        this.errorQuantity = errorQuantity;
        return this;
    }

    public void setErrorQuantity(Integer errorQuantity) {
        this.errorQuantity = errorQuantity;
    }

    public ErrorSeverity getErrorSeverity() {
        return errorSeverity;
    }

    public Tasks errorSeverity(ErrorSeverity errorSeverity) {
        this.errorSeverity = errorSeverity;
        return this;
    }

    public void setErrorSeverity(ErrorSeverity errorSeverity) {
        this.errorSeverity = errorSeverity;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public Tasks status(TaskStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public Tasks description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getParent() {
        return parent;
    }

    public Tasks parent(Long parent) {
        this.parent = parent;
        return this;
    }

    public void setParent(Long parent) {
        this.parent = parent;
    }

    public String getOp() {
        return op;
    }

    public Tasks op(String op) {
        this.op = op;
        return this;
    }

    public void setOp(String op) {
        this.op = op;
    }

    public String getReview1() {
        return review1;
    }

    public Tasks review1(String review1) {
        this.review1 = review1;
        return this;
    }

    public void setReview1(String review1) {
        this.review1 = review1;
    }

    public String getReview2() {
        return review2;
    }

    public Tasks review2(String review2) {
        this.review2 = review2;
        return this;
    }

    public void setReview2(String review2) {
        this.review2 = review2;
    }

    public String getFixer() {
        return fixer;
    }

    public Tasks fixer(String fixer) {
        this.fixer = fixer;
        return this;
    }

    public void setFixer(String fixer) {
        this.fixer = fixer;
    }

    public String getFi() {
        return fi;
    }

    public Tasks fi(String fi) {
        this.fi = fi;
        return this;
    }

    public void setFi(String fi) {
        this.fi = fi;
    }

    public Set<TMSCustomFieldScreenValue> getTmsCustomFieldScreenValues() {
        return tmsCustomFieldScreenValues;
    }

    public Tasks tmsCustomFieldScreenValues(Set<TMSCustomFieldScreenValue> tMSCustomFieldScreenValues) {
        this.tmsCustomFieldScreenValues = tMSCustomFieldScreenValues;
        return this;
    }

    public Tasks addTmsCustomFieldScreenValue(TMSCustomFieldScreenValue tMSCustomFieldScreenValue) {
        this.tmsCustomFieldScreenValues.add(tMSCustomFieldScreenValue);
        tMSCustomFieldScreenValue.setTasks(this);
        return this;
    }

    public Tasks removeTmsCustomFieldScreenValue(TMSCustomFieldScreenValue tMSCustomFieldScreenValue) {
        this.tmsCustomFieldScreenValues.remove(tMSCustomFieldScreenValue);
        tMSCustomFieldScreenValue.setTasks(null);
        return this;
    }

    public void setTmsCustomFieldScreenValues(Set<TMSCustomFieldScreenValue> tMSCustomFieldScreenValues) {
        this.tmsCustomFieldScreenValues = tMSCustomFieldScreenValues;
    }

    public Packages getPackages() {
        return packages;
    }

    public Tasks packages(Packages packages) {
        this.packages = packages;
        return this;
    }

    public void setPackages(Packages packages) {
        this.packages = packages;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Tasks tasks = (Tasks) o;
        if (tasks.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), tasks.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Tasks{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", severity='" + getSeverity() + "'" +
            ", priority='" + getPriority() + "'" +
            ", data='" + getData() + "'" +
            ", fileName='" + getFileName() + "'" +
            ", type='" + getType() + "'" +
            ", availability='" + getAvailability() + "'" +
            ", frame=" + getFrame() +
            ", actualObject=" + getActualObject() +
            ", opStatus='" + getOpStatus() + "'" +
            ", estimateStartTime='" + getEstimateStartTime() + "'" +
            ", estimateEndTime='" + getEstimateEndTime() + "'" +
            ", opStartTime='" + getOpStartTime() + "'" +
            ", opEndTime='" + getOpEndTime() + "'" +
            ", review1Status='" + getReview1Status() + "'" +
            ", review1StartTime='" + getReview1StartTime() + "'" +
            ", review1EndTime='" + getReview1EndTime() + "'" +
            ", fixStatus='" + getFixStatus() + "'" +
            ", fixStartTime='" + getFixStartTime() + "'" +
            ", fixEndTime='" + getFixEndTime() + "'" +
            ", review2Status='" + getReview2Status() + "'" +
            ", review2StartTime='" + getReview2StartTime() + "'" +
            ", review2EndTime='" + getReview2EndTime() + "'" +
            ", fiStatus='" + getFiStatus() + "'" +
            ", fiStartTime='" + getFiStartTime() + "'" +
            ", fiEndTime='" + getFiEndTime() + "'" +
            ", duration=" + getDuration() +
            ", target=" + getTarget() +
            ", errorQuantity=" + getErrorQuantity() +
            ", errorSeverity='" + getErrorSeverity() + "'" +
            ", status='" + getStatus() + "'" +
            ", description='" + getDescription() + "'" +
            ", parent=" + getParent() +
            ", op='" + getOp() + "'" +
            ", review1='" + getReview1() + "'" +
            ", review2='" + getReview2() + "'" +
            ", fixer='" + getFixer() + "'" +
            ", fi='" + getFi() + "'" +
            "}";
    }
}
