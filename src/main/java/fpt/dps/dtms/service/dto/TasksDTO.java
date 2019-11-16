package fpt.dps.dtms.service.dto;


import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Objects;
import javax.persistence.Lob;
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

/**
 * A DTO for the Tasks entity.
 */
public class TasksDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String name;

    private TaskSeverity severity;

    private TaskPriority priority;

    @Size(max = 255)
    private String data;

    @Size(max = 2000)
    private String fileName;

    private String type;

    private TaskAvailability availability;

    private Integer frame;

    private Integer actualObject;

    private OPStatus opStatus;

    private Instant estimateStartTime;

    private Instant estimateEndTime;

    private Instant opStartTime;

    private Instant opEndTime;

    private ReviewStatus review1Status;

    private Instant review1StartTime;

    private Instant review1EndTime;

    private FixStatus fixStatus;

    private Instant fixStartTime;

    private Instant fixEndTime;

    private ReviewStatus review2Status;

    private Instant review2StartTime;

    private Instant review2EndTime;

    private FIStatus fiStatus;

    private Instant fiStartTime;

    private Instant fiEndTime;

    private Integer duration;

    private Integer target;

    private Integer errorQuantity;

    private ErrorSeverity errorSeverity;

    private TaskStatus status;

    @Lob
    private String description;

    private Long parent;

    private String op;

    @Size(max = 50)
    private String review1;

    @Size(max = 50)
    private String review2;

    @Size(max = 50)
    private String fixer;
    
    @Size(max = 50)
    private String fi;

    private Long packagesId;

    private String packagesName;
    
    private List<TMSCustomFieldScreenValueDTO> tmsCustomFieldScreenValueDTO;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

	public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TaskSeverity getSeverity() {
        return severity;
    }

    public void setSeverity(TaskSeverity severity) {
        this.severity = severity;
    }

    public TaskPriority getPriority() {
        return priority;
    }

    public void setPriority(TaskPriority priority) {
        this.priority = priority;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public TaskAvailability getAvailability() {
        return availability;
    }

    public void setAvailability(TaskAvailability availability) {
        this.availability = availability;
    }

    public Integer getFrame() {
        return frame;
    }

    public void setFrame(Integer frame) {
        this.frame = frame;
    }

    public Integer getActualObject() {
        return actualObject;
    }

    public void setActualObject(Integer actualObject) {
        this.actualObject = actualObject;
    }

    public OPStatus getOpStatus() {
        return opStatus;
    }

    public void setOpStatus(OPStatus opStatus) {
    	this.opStatus = opStatus != null ? opStatus : OPStatus.NA;
    }

    public Instant getEstimateStartTime() {
        return estimateStartTime;
    }

    public void setEstimateStartTime(Instant estimateStartTime) {
        this.estimateStartTime = estimateStartTime;
    }

    public Instant getEstimateEndTime() {
        return estimateEndTime;
    }

    public void setEstimateEndTime(Instant estimateEndTime) {
        this.estimateEndTime = estimateEndTime;
    }

    public Instant getOpStartTime() {
        return opStartTime;
    }

    public void setOpStartTime(Instant opStartTime) {
        this.opStartTime = opStartTime;
    }

    public Instant getOpEndTime() {
        return opEndTime;
    }

    public void setOpEndTime(Instant opEndTime) {
        this.opEndTime = opEndTime;
    }

    public ReviewStatus getReview1Status() {
        return review1Status;
    }

    public void setReview1Status(ReviewStatus review1Status) {
    	this.review1Status = review1Status != null ? review1Status : ReviewStatus.NA;
    }

    public Instant getReview1StartTime() {
        return review1StartTime;
    }

    public void setReview1StartTime(Instant review1StartTime) {
        this.review1StartTime = review1StartTime;
    }

    public Instant getReview1EndTime() {
        return review1EndTime;
    }

    public void setReview1EndTime(Instant review1EndTime) {
        this.review1EndTime = review1EndTime;
    }

    public FixStatus getFixStatus() {
        return fixStatus;
    }

    public void setFixStatus(FixStatus fixStatus) {
        this.fixStatus = fixStatus != null ? fixStatus : FixStatus.NA;
    }

    public Instant getFixStartTime() {
        return fixStartTime;
    }

    public void setFixStartTime(Instant fixStartTime) {
        this.fixStartTime = fixStartTime;
    }

    public Instant getFixEndTime() {
        return fixEndTime;
    }

    public void setFixEndTime(Instant fixEndTime) {
        this.fixEndTime = fixEndTime;
    }

    public ReviewStatus getReview2Status() {
        return review2Status;
    }

    public void setReview2Status(ReviewStatus review2Status) {
        this.review2Status = review2Status != null ? review2Status : ReviewStatus.NA;
    }

    public Instant getReview2StartTime() {
        return review2StartTime;
    }

    public void setReview2StartTime(Instant review2StartTime) {
        this.review2StartTime = review2StartTime;
    }

    public Instant getReview2EndTime() {
        return review2EndTime;
    }

    public void setReview2EndTime(Instant review2EndTime) {
        this.review2EndTime = review2EndTime;
    }

    public FIStatus getFiStatus() {
        return fiStatus;
    }

    public void setFiStatus(FIStatus fiStatus) {
        this.fiStatus = fiStatus != null ? fiStatus : FIStatus.NA;
    }

    public Instant getFiStartTime() {
        return fiStartTime;
    }

    public void setFiStartTime(Instant fiStartTime) {
        this.fiStartTime = fiStartTime;
    }

    public Instant getFiEndTime() {
        return fiEndTime;
    }

    public void setFiEndTime(Instant fiEndTime) {
        this.fiEndTime = fiEndTime;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getTarget() {
        return target;
    }

    public void setTarget(Integer target) {
        this.target = target;
    }

    public Integer getErrorQuantity() {
        return errorQuantity;
    }

    public void setErrorQuantity(Integer errorQuantity) {
        this.errorQuantity = errorQuantity;
    }

    public ErrorSeverity getErrorSeverity() {
        return errorSeverity;
    }

    public void setErrorSeverity(ErrorSeverity errorSeverity) {
        this.errorSeverity = errorSeverity;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getParent() {
        return parent;
    }

    public void setParent(Long parent) {
        this.parent = parent;
    }

    public String getOp() {
        return op;
    }

    public void setOp(String op) {
        this.op = op;
    }

    public String getReview1() {
        return review1;
    }

    public void setReview1(String review1) {
        this.review1 = review1;
    }

    public String getReview2() {
        return review2;
    }

    public void setReview2(String review2) {
        this.review2 = review2;
    }

    public String getFixer() {
        return fixer;
    }

    public void setFixer(String fixer) {
        this.fixer = fixer;
    }

    public String getFi() {
        return fi;
    }

    public void setFi(String fi) {
        this.fi = fi;
    }

    public Long getPackagesId() {
        return packagesId;
    }

    public void setPackagesId(Long packagesId) {
        this.packagesId = packagesId;
    }

    public String getPackagesName() {
        return packagesName;
    }

    public void setPackagesName(String packagesName) {
        this.packagesName = packagesName;
    }

    public List<TMSCustomFieldScreenValueDTO> getTmsCustomFieldScreenValueDTO() {
		return tmsCustomFieldScreenValueDTO;
	}

	public void setTmsCustomFieldScreenValueDTO(List<TMSCustomFieldScreenValueDTO> tmsCustomFieldScreenValueDTO) {
		this.tmsCustomFieldScreenValueDTO = tmsCustomFieldScreenValueDTO;
	}

	 /*Labeling row  incase Task has bug waiting to fix
    and  incase Task had bug fixed and waiting to review
    START*/
	private int bugCount;
	
	private int bugCountRV1;

	private int bugCountRV2;
	
	private int bugCountFI;
	
	public int getBugCount() {
		return bugCount;
	}

	public void setBugCount(int bugCount) {
		this.bugCount = bugCount;
	}
	public int getBugCountRV1() {
		return bugCountRV1;
	}

	public void setBugCountRV1(int bugCountRV1) {
		this.bugCountRV1 = bugCountRV1;
	}

	public int getBugCountRV2() {
		return bugCountRV2;
	}

	public void setBugCountRV2(int bugCountRV2) {
		this.bugCountRV2 = bugCountRV2;
	}

	public int getBugCountFI() {
		return bugCountFI;
	}

	public void setBugCountFI(int bugCountFI) {
		this.bugCountFI = bugCountFI;
	}
	/*END*/

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TasksDTO tasksDTO = (TasksDTO) o;
        if(tasksDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), tasksDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TasksDTO{" +
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
