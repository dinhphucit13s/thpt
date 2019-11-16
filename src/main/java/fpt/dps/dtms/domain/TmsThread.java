package fpt.dps.dtms.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A TmsThread.
 */
@Entity
@Table(name = "tms_thread")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "tmsthread")
public class TmsThread extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "views")
    private Integer views;

    @Column(name = "answers")
    private Integer answers;

    @NotNull
    @Column(name = "closed", nullable = false)
    private Boolean closed;

    @NotNull
    @Column(name = "status", nullable = false)
    private Boolean status;

    @ManyToOne(optional = false)
    @NotNull
    private Projects projects;

    @ManyToOne
    private ProjectUsers assignee;
    
    @OneToMany(mappedBy = "thread", cascade=CascadeType.ALL, orphanRemoval=true)
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<TmsPost> posts = new HashSet<>();
    
    /**
	 * @return the posts
	 */
	public Set<TmsPost> getPosts() {
		return posts;
	}

	/**
	 * @param posts the posts to set
	 */
	public void setPosts(Set<TmsPost> posts) {
		this.posts = posts;
	}

	// jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public TmsThread title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getViews() {
        return views;
    }

    public TmsThread views(Integer views) {
        this.views = views;
        return this;
    }

    public void setViews(Integer views) {
        this.views = views;
    }

    public Integer getAnswers() {
        return answers;
    }

    public TmsThread answers(Integer answers) {
        this.answers = answers;
        return this;
    }

    public void setAnswers(Integer answers) {
        this.answers = answers;
    }

    public Boolean isClosed() {
        return closed;
    }

    public TmsThread closed(Boolean closed) {
        this.closed = closed;
        return this;
    }

    public void setClosed(Boolean closed) {
        this.closed = closed;
    }

    public Boolean isStatus() {
        return status;
    }

    public TmsThread status(Boolean status) {
        this.status = status;
        return this;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Projects getProjects() {
        return projects;
    }

    public TmsThread projects(Projects projects) {
        this.projects = projects;
        return this;
    }

    public void setProjects(Projects projects) {
        this.projects = projects;
    }

    public ProjectUsers getAssignee() {
        return assignee;
    }

    public TmsThread assignee(ProjectUsers projectUsers) {
        this.assignee = projectUsers;
        return this;
    }

    public void setAssignee(ProjectUsers projectUsers) {
        this.assignee = projectUsers;
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
        TmsThread tmsThread = (TmsThread) o;
        if (tmsThread.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), tmsThread.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TmsThread{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", views=" + getViews() +
            ", answers=" + getAnswers() +
            ", closed='" + isClosed() + "'" +
            ", status='" + isStatus() + "'" +
            "}";
    }
}
