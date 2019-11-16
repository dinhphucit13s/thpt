package fpt.dps.dtms.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the TmsPost entity.
 */
public class TmsPostDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    @NotNull
    private String content;

    private Integer comments;

    private Long threadId;
    
    private Set<AttachmentsDTO> attachments = new HashSet<>();
    
    private Set<AttachmentsDTO> attachmentsAppend = new HashSet<>();

    private Set<AttachmentsDTO> attachmentsRemove = new HashSet<>();
    
    private Set<CommentsDTO> commentsDTOs = new HashSet<>();
    
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

	/**
	 * @return the commentsDTOs
	 */
	public Set<CommentsDTO> getCommentsDTOs() {
		return commentsDTOs;
	}

	/**
	 * @param commentsDTOs the commentsDTOs to set
	 */
	public void setCommentsDTOs(Set<CommentsDTO> commentsDTOs) {
		this.commentsDTOs = commentsDTOs;
	}

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

    public Integer getComments() {
        return comments;
    }

    public void setComments(Integer comments) {
        this.comments = comments;
    }

    public Long getThreadId() {
        return threadId;
    }

    public void setThreadId(Long tmsThreadId) {
        this.threadId = tmsThreadId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TmsPostDTO tmsPostDTO = (TmsPostDTO) o;
        if(tmsPostDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), tmsPostDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TmsPostDTO{" +
            "id=" + getId() +
            ", content='" + getContent() + "'" +
            ", comments=" + getComments() +
            "}";
    }
}
