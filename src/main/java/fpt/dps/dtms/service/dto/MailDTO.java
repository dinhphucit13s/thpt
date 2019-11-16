package fpt.dps.dtms.service.dto;


import java.time.Instant;

import javax.persistence.Lob;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the Mail entity.
 */
public class MailDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    @NotNull
    private String from;

    @NotNull
    @Size(min = 5)
    private String subject;

    @NotNull
    @Lob
    private String body;

    private Instant startTime;

    private Instant endTime;
    
    private Set<AttachmentsDTO> attachments = new HashSet<>();
    
    public Set<AttachmentsDTO> getAttachments() {
		return attachments;
	}

	public void setAttachments(Set<AttachmentsDTO> attachments) {
		this.attachments = attachments;
	}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MailDTO mailDTO = (MailDTO) o;
        if(mailDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), mailDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MailDTO{" +
            "id=" + getId() +
            ", from='" + getFrom() + "'" +
            ", subject='" + getSubject() + "'" +
            ", body='" + getBody() + "'" +
            ", startTime='" + getStartTime() + "'" +
            ", endTime='" + getEndTime() + "'" +
            "}";
    }
}
