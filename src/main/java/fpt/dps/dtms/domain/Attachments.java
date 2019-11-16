package fpt.dps.dtms.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Attachments.
 */
@Entity
@Table(name = "attachments")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "attachments")
public class Attachments extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 2000)
    @Column(name = "filename", length = 2000, nullable = false)
    private String filename;

    @NotNull
    @Size(max = 2000)
    @Column(name = "disk_file", length = 2000, nullable = false)
    private String diskFile;

    @Size(max = 100)
    @Column(name = "file_type", length = 100)
    private String fileType;

    @ManyToOne
    private Bugs bugs;

    @ManyToOne
    private Notes notes;

    @ManyToOne
    private Issues issues;

    @ManyToOne
    private Mail mail;

    @ManyToOne
    private Comments comment;

    @ManyToOne
    private TmsPost post;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public Attachments filename(String filename) {
        this.filename = filename;
        return this;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getDiskFile() {
        return diskFile;
    }

    public Attachments diskFile(String diskFile) {
        this.diskFile = diskFile;
        return this;
    }

    public void setDiskFile(String diskFile) {
        this.diskFile = diskFile;
    }

    public String getFileType() {
        return fileType;
    }

    public Attachments fileType(String fileType) {
        this.fileType = fileType;
        return this;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public Bugs getBugs() {
        return bugs;
    }

    public Attachments bugs(Bugs bugs) {
        this.bugs = bugs;
        return this;
    }

    public void setBugs(Bugs bugs) {
        this.bugs = bugs;
    }

    public Notes getNotes() {
        return notes;
    }

    public Attachments notes(Notes notes) {
        this.notes = notes;
        return this;
    }

    public void setNotes(Notes notes) {
        this.notes = notes;
    }

    public Issues getIssues() {
        return issues;
    }

    public Attachments issues(Issues issues) {
        this.issues = issues;
        return this;
    }

    public void setIssues(Issues issues) {
        this.issues = issues;
    }

    public Mail getMail() {
        return mail;
    }

    public Attachments mail(Mail mail) {
        this.mail = mail;
        return this;
    }

    public void setMail(Mail mail) {
        this.mail = mail;
    }

    public Comments getComment() {
        return comment;
    }

    public Attachments comment(Comments comments) {
        this.comment = comments;
        return this;
    }

    public void setComment(Comments comments) {
        this.comment = comments;
    }

    public TmsPost getPost() {
        return post;
    }

    public Attachments post(TmsPost tmsPost) {
        this.post = tmsPost;
        return this;
    }

    public void setPost(TmsPost tmsPost) {
        this.post = tmsPost;
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
        Attachments attachments = (Attachments) o;
        if (attachments.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), attachments.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Attachments{" +
            "id=" + getId() +
            ", filename='" + getFilename() + "'" +
            ", diskFile='" + getDiskFile() + "'" +
            ", fileType='" + getFileType() + "'" +
            "}";
    }
}
