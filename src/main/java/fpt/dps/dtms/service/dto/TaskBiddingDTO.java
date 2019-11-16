package fpt.dps.dtms.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import fpt.dps.dtms.domain.enumeration.BiddingScope;
import fpt.dps.dtms.domain.enumeration.BiddingStatus;

/**
 * A DTO for the TaskBidding entity.
 */
public class TaskBiddingDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    @NotNull
    private BiddingScope biddingScope;

    @NotNull
    private BiddingStatus biddingStatus;
    
    private String biddingRound;
    
    private String pic;
    
    private Instant startDate;
    
    private Instant endDate;

    private TasksDTO task;
    
    private Integer biddingHoldTime;
    
    private Long purchaseOrdersId;
    
    private String purchaseOrdersName;
    
    private Long projectId;
    
    private String projectName;
    
    private String teamLead;
    
    public String getTeamLead() {
		return teamLead;
	}

	public void setTeamLead(String teamLead) {
		this.teamLead = teamLead;
	}

	public Long getPurchaseOrdersId() {
		return purchaseOrdersId;
	}

	public void setPurchaseOrdersId(Long purchaseOrdersId) {
		this.purchaseOrdersId = purchaseOrdersId;
	}

	public String getPurchaseOrdersName() {
		return purchaseOrdersName;
	}

	public void setPurchaseOrdersName(String purchaseOrdersName) {
		this.purchaseOrdersName = purchaseOrdersName;
	}

	public Long getProjectId() {
		return projectId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public Integer getBiddingHoldTime() {
		return biddingHoldTime;
	}

	public void setBiddingHoldTime(Integer biddingHoldTime) {
		this.biddingHoldTime = biddingHoldTime;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BiddingScope getBiddingScope() {
        return biddingScope;
    }

    public void setBiddingScope(BiddingScope biddingScope) {
        this.biddingScope = biddingScope;
    }

    public BiddingStatus getBiddingStatus() {
        return biddingStatus;
    }

    public void setBiddingStatus(BiddingStatus biddingStatus) {
        this.biddingStatus = biddingStatus;
    }
    
    public TasksDTO getTask() {
		return task;
	}

	public void setTask(TasksDTO task) {
		this.task = task;
	}
	
	public String getBiddingRound() {
		return biddingRound;
	}

	public void setBiddingRound(String biddingRound) {
		this.biddingRound = biddingRound;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}
	
	public Instant getStartDate() {
		return startDate;
	}

	public void setStartDate(Instant startDate) {
		this.startDate = startDate;
	}

	public Instant getEndDate() {
		return endDate;
	}

	public void setEndDate(Instant endDate) {
		this.endDate = endDate;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TaskBiddingDTO taskBiddingDTO = (TaskBiddingDTO) o;
        if(taskBiddingDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), taskBiddingDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TaskBiddingDTO{" +
            "id=" + getId() +
            ", biddingScope='" + getBiddingScope() + "'" +
            ", biddingStatus='" + getBiddingStatus() + "'" +
            "}";
    }
}
