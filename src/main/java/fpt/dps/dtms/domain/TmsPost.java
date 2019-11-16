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
 * A TmsPost.
 */
@Entity
@Table(name = "tms_post")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "tmspost")
public class TmsPost extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "comments")
    private Integer comments;

    @ManyToOne(optional = false)
    @NotNull
    private TmsThread thread;
    
    @OneToMany(mappedBy = "post", cascade=CascadeType.ALL, orphanRemoval=true)
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Attachments> attachments = new HashSet<>();

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

	// jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public TmsPost content(String content) {
        this.content = content;
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getComments() {
        return comments;
    }

    public TmsPost comments(Integer comments) {
        this.comments = comments;
        return this;
    }

    public void setComments(Integer comments) {
        this.comments = comments;
    }

    public TmsThread getThread() {
        return thread;
    }

    public TmsPost thread(TmsThread tmsThread) {
        this.thread = tmsThread;
        return this;
    }

    public void setThread(TmsThread tmsThread) {
        this.thread = tmsThread;
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
        TmsPost tmsPost = (TmsPost) o;
        if (tmsPost.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), tmsPost.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TmsPost{" +
            "id=" + getId() +
            ", content='" + getContent() + "'" +
            ", comments=" + getComments() +
            "}";
    }
}
