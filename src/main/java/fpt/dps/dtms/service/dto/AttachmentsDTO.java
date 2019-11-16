package fpt.dps.dtms.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * A DTO for the Attachments entity.
 */
public class AttachmentsDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 2000)
    private String filename;

    @NotNull
    @Size(max = 2000)
    private String diskFile;

    @Size(max = 100)
    private String fileType;

    private String value;

    private Long bugsId;

    private String bugsDescription;

    private Long notesId;

    private String notesDescription;

    private Long issuesId;

    private String issuesName;

    private Long mailId;

    private Long commentId;

    private Long postId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getDiskFile() {
        return diskFile;
    }

    public void setDiskFile(String diskFile) {
        this.diskFile = diskFile;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }

    public Long getBugsId() {
        return bugsId;
    }

    public void setBugsId(Long bugsId) {
        this.bugsId = bugsId;
    }

    public String getBugsDescription() {
        return bugsDescription;
    }

    public void setBugsDescription(String bugsDescription) {
        this.bugsDescription = bugsDescription;
    }

    public Long getNotesId() {
        return notesId;
    }

    public void setNotesId(Long notesId) {
        this.notesId = notesId;
    }

    public String getNotesDescription() {
        return notesDescription;
    }

    public void setNotesDescription(String notesDescription) {
        this.notesDescription = notesDescription;
    }

    public Long getIssuesId() {
        return issuesId;
    }

    public void setIssuesId(Long issuesId) {
        this.issuesId = issuesId;
    }

    public String getIssuesName() {
        return issuesName;
    }

    public void setIssuesName(String issuesName) {
        this.issuesName = issuesName;
    }

    public Long getMailId() {
        return mailId;
    }

    public void setMailId(Long mailId) {
        this.mailId = mailId;
    }

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentsId) {
        this.commentId = commentsId;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long tmsPostId) {
        this.postId = tmsPostId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AttachmentsDTO attachmentsDTO = (AttachmentsDTO) o;
        if(attachmentsDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), attachmentsDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AttachmentsDTO{" +
            "id=" + getId() +
            ", filename='" + getFilename() + "'" +
            ", diskFile='" + getDiskFile() + "'" +
            ", fileType='" + getFileType() + "'" +
            "}";
    }
}
