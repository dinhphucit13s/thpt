package fpt.dps.dtms.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

import fpt.dps.dtms.domain.enumeration.BiddingScope;

import fpt.dps.dtms.domain.enumeration.BiddingStatus;

/**
 * A TaskBidding.
 */
@Entity
@Table(name = "task_bidding")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "taskbidding")
public class TaskBidding extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "bidding_scope", nullable = false)
    private BiddingScope biddingScope;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "bidding_status", nullable = false)
    private BiddingStatus biddingStatus;
    
    @Column(name = "bidding_round", nullable = false)
    private String biddingRound;

    @OneToOne
    private Tasks task;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBiddingRound() {
		return biddingRound;
	}

	public void setBiddingRound(String biddingRound) {
		this.biddingRound = biddingRound;
	}

	public BiddingScope getBiddingScope() {
        return biddingScope;
    }

    public TaskBidding biddingScope(BiddingScope biddingScope) {
        this.biddingScope = biddingScope;
        return this;
    }

    public void setBiddingScope(BiddingScope biddingScope) {
        this.biddingScope = biddingScope;
    }

    public BiddingStatus getBiddingStatus() {
        return biddingStatus;
    }

    public TaskBidding biddingStatus(BiddingStatus biddingStatus) {
        this.biddingStatus = biddingStatus;
        return this;
    }

    public void setBiddingStatus(BiddingStatus biddingStatus) {
        this.biddingStatus = biddingStatus;
    }

    public Tasks getTask() {
        return task;
    }

    public TaskBidding task(Tasks tasks) {
        this.task = tasks;
        return this;
    }

    public void setTask(Tasks tasks) {
        this.task = tasks;
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
        TaskBidding taskBidding = (TaskBidding) o;
        if (taskBidding.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), taskBidding.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TaskBidding{" +
            "id=" + getId() +
            ", biddingScope='" + getBiddingScope() + "'" +
            ", biddingStatus='" + getBiddingStatus() + "'" +
            "}";
    }
}
