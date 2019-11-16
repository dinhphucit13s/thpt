package fpt.dps.dtms.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

import fpt.dps.dtms.domain.enumeration.NotificationCategory;

/**
 * A NotificationTemplate.
 */
@Entity
@Table(name = "notification_template")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "notificationtemplate")
public class NotificationTemplate extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "jhi_type")
    private NotificationCategory type;

    @NotNull
    @Size(min = 20, max = 2000)
    @Column(name = "template", length = 2000, nullable = false)
    private String template;

    @Lob
    @Column(name = "description")
    private String description;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public NotificationCategory getType() {
        return type;
    }

    public NotificationTemplate type(NotificationCategory type) {
        this.type = type;
        return this;
    }

    public void setType(NotificationCategory type) {
        this.type = type;
    }

    public String getTemplate() {
        return template;
    }

    public NotificationTemplate template(String template) {
        this.template = template;
        return this;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getDescription() {
        return description;
    }

    public NotificationTemplate description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
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
        NotificationTemplate notificationTemplate = (NotificationTemplate) o;
        if (notificationTemplate.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), notificationTemplate.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "NotificationTemplate{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", template='" + getTemplate() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
