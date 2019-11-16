package fpt.dps.dtms.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;

import fpt.dps.dtms.domain.enumeration.BugResolution;
import fpt.dps.dtms.domain.enumeration.BugStatus;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A Bugs.
 */
@Entity
@Table(name = "bugs")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "bugs")
public class Bugs extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 10)
    @Lob
    @Column(name = "description", nullable = false)
    private String description;

    @NotNull
    @Size(min = 2)
    @Column(name = "code", nullable = false)
    private String code;
    
    @Column(name = "iteration")
    private Integer iteration;

    @Column(name = "stage")
    private String stage;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private BugStatus status;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "resolution", nullable = false)
    private BugResolution resolution;

    @Column(name = "physical_path")
    private String physicalPath;
    
    @ManyToOne
    private Tasks tasks;
    
    @OneToMany(mappedBy = "bugs", cascade=CascadeType.ALL, orphanRemoval=true)
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Attachments> attachments = new HashSet<>();
    
    @OneToMany(mappedBy = "bug", cascade=CascadeType.ALL, orphanRemoval=true)
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Notes> notes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    
    public Long getId() {
        return id;
    }

    /**
	 * @return the attachments
	 */
	public Set<Attachments> getAttachments() {
		return attachments;
	}

	/**
	 * @param attachments the attachments to set
	 */
	public void setAttachments(Set<Attachments> attachments) {
		this.attachments = attachments;
	}

	public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public Bugs description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getCode() {
        return code;
    }

    public Bugs code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getIteration() {
		return iteration;
	}

	public void setIteration(Integer iteration) {
		this.iteration = iteration;
	}

	public String getStage() {
        return stage;
    }

    public Bugs stage(String stage) {
        this.stage = stage;
        return this;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public BugStatus getStatus() {
        return status;
    }

    public Bugs status(BugStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(BugStatus status) {
        this.status = status;
    }

    public BugResolution getResolution() {
        return resolution;
    }

    public Bugs resolution(BugResolution resolution) {
        this.resolution = resolution;
        return this;
    }

    public void setResolution(BugResolution resolution) {
        this.resolution = resolution;
    }

    public String getPhysicalPath() {
        return physicalPath;
    }

    public Bugs physicalPath(String physicalPath) {
        this.physicalPath = physicalPath;
        return this;
    }

    public void setPhysicalPath(String physicalPath) {
        this.physicalPath = physicalPath;
    }

    public Tasks getTasks() {
        return tasks;
    }

    public Bugs tasks(Tasks tasks) {
        this.tasks = tasks;
        return this;
    }

    public void setTasks(Tasks tasks) {
        this.tasks = tasks;
    }
    
    public Set<Notes> getNotes() {
        return notes;
    }

    public Bugs notes(Set<Notes> notes) {
        this.notes = notes;
        return this;
    }

    public Bugs addNotes(Notes notes) {
        this.notes.add(notes);
        notes.setBug(this);
        return this;
    }

    public Bugs removeNotes(Notes notes) {
        this.notes.remove(notes);
        notes.setBug(null);
        return this;
    }

    public void setNotes(Set<Notes> notes) {
        this.notes = notes;
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
        Bugs bugs = (Bugs) o;
        if (bugs.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), bugs.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Bugs{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", code='" + getCode() + "'" +
            ", stage='" + getStage() + "'" +
            ", status='" + getStatus() + "'" +
            ", resolution='" + getResolution() + "'" +
            ", physicalPath='" + getPhysicalPath() + "'" +
            "}";
    }
}
