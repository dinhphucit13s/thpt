package fpt.dps.dtms.service.dto;


import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the Packages entity.
 */
public class PackagesDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String name;

    private String op;

    private String reviewer;

    private String fi;

    private String delivery;

    private Instant estimateDelivery;

    private Integer target;

    private Instant startTime;

    private Instant endTime;

    @Lob
    private String description;

    private Long purchaseOrdersId;

    private String purchaseOrdersName;
    
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

    public String getOp() {
        return op;
    }

    public void setOp(String op) {
        this.op = op;
    }

    public String getReviewer() {
        return reviewer;
    }

    public void setReviewer(String reviewer) {
        this.reviewer = reviewer;
    }

    public String getFi() {
        return fi;
    }

    public void setFi(String fi) {
        this.fi = fi;
    }

    public String getDelivery() {
        return delivery;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }

    public Instant getEstimateDelivery() {
        return estimateDelivery;
    }

    public void setEstimateDelivery(Instant estimateDelivery) {
        this.estimateDelivery = estimateDelivery;
    }

    public Integer getTarget() {
        return target;
    }

    public void setTarget(Integer target) {
        this.target = target;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public Instant getEndTime() {
        return endTime;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

	public List<TMSCustomFieldScreenValueDTO> getTmsCustomFieldScreenValueDTO() {
		return tmsCustomFieldScreenValueDTO;
	}

	public void setTmsCustomFieldScreenValueDTO(List<TMSCustomFieldScreenValueDTO> tmsCustomFieldScreenValueDTO) {
		this.tmsCustomFieldScreenValueDTO = tmsCustomFieldScreenValueDTO;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PackagesDTO packagesDTO = (PackagesDTO) o;
        if(packagesDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), packagesDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PackagesDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", op='" + getOp() + "'" +
            ", reviewer='" + getReviewer() + "'" +
            ", fi='" + getFi() + "'" +
            ", delivery='" + getDelivery() + "'" +
            ", estimateDelivery='" + getEstimateDelivery() + "'" +
            ", target=" + getTarget() +
            ", startTime='" + getStartTime() + "'" +
            ", endTime='" + getEndTime() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
