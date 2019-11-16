package fpt.dps.dtms.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Comments entity.
 */
public class CommentsDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    @NotNull
    private String content;

    private Long postId;
    
    private Set<AttachmentsDTO> attachments = new HashSet<>();
    
    private Set<AttachmentsDTO> attachmentsAppend = new HashSet<>();
    
    private Set<AttachmentsDTO> attachmentsRemove = new HashSet<>();
    
    /**
	 * @return the attachments
	 */
	public Set<AttachmentsDTO> getAttachments() {
		return attachments;
	}

	/**
	 * @param attachments the attachments to set
	 */
	public void setAttachments(Set<AttachmentsDTO> attachments) {
		this.attachments = attachments;
	}

	/**
	 * @return the attachmentsAppend
	 */
	public Set<AttachmentsDTO> getAttachmentsAppend() {
		return attachmentsAppend;
	}

	/**
	 * @param attachmentsAppend the attachmentsAppend to set
	 */
	public void setAttachmentsAppend(Set<AttachmentsDTO> attachmentsAppend) {
		this.attachmentsAppend = attachmentsAppend;
	}

	/**
	 * @return the attachmentsRemove
	 */
	public Set<AttachmentsDTO> getAttachmentsRemove() {
		return attachmentsRemove;
	}

	/**
	 * @param attachmentsRemove the attachmentsRemove to set
	 */
	public void setAttachmentsRemove(Set<AttachmentsDTO> attachmentsRemove) {
		this.attachmentsRemove = attachmentsRemove;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

        CommentsDTO commentsDTO = (CommentsDTO) o;
        if(commentsDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), commentsDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CommentsDTO{" +
            "id=" + getId() +
            ", content='" + getContent() + "'" +
            "}";
    }
}
