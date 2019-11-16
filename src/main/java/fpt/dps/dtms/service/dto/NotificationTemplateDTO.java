package fpt.dps.dtms.service.dto;


import javax.persistence.Lob;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import fpt.dps.dtms.domain.enumeration.NotificationCategory;

/**
 * A DTO for the NotificationTemplate entity.
 */
public class NotificationTemplateDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    private NotificationCategory type;

    @NotNull
    @Size(min = 20, max = 2000)
    private String template;

    @Lob
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public NotificationCategory getType() {
        return type;
    }

    public void setType(NotificationCategory type) {
        this.type = type;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        NotificationTemplateDTO notificationTemplateDTO = (NotificationTemplateDTO) o;
        if(notificationTemplateDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), notificationTemplateDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "NotificationTemplateDTO{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", template='" + getTemplate() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
