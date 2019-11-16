package fpt.dps.dtms.service.dto;


import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import javax.persistence.Lob;
import fpt.dps.dtms.domain.enumeration.PurchaseOrderStatus;

/**
 * A DTO for the PurchaseOrders entity.
 */
public class SelectDTO implements Serializable {

	private Long id;
	
    @NotNull
    @Size(max = 255)
    private String name;
    
    /* Implement Bug Ratio base on total bug count at Tracking Management
     * Start*/
    private String totalBug;
    
    private String totalTasks;
    
    private String totalTasksPerBugs;

    public String getTotalBug() {
		return totalBug;
	}

	public void setTotalBug(String totalBug) {
		this.totalBug = totalBug;
	}

	public String getTotalTasks() {
		return totalTasks;
	}

	public void setTotalTasks(String totalTasks) {
		this.totalTasks = totalTasks;
	}
	
	public String getTotalTasksPerBugs() {
		return totalTasksPerBugs;
	}

	public void setTotalTasksPerBugs(String totalTasksPerBugs) {
		this.totalTasksPerBugs = totalTasksPerBugs;
	}
	/*End*/

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
    
    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SelectDTO{" + "name='" + getName() + "'" + "}";
    }
}
