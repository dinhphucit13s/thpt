package fpt.dps.dtms.domain;

import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * The TMSCustomField entity.
 */
@ApiModel(description = "The TMSCustomField entity.")
@Entity
@Table(name = "tms_custom_field")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "tmscustomfield")
public class TMSCustomField extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(name = "entity_data")
    private String entityData;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEntityData() {
        return entityData;
    }

    public TMSCustomField entityData(String entity) {
        this.entityData = entity;
        return this;
    }

    public void setEntityData(String entity) {
        this.entityData = entity;
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
        TMSCustomField tMSCustomField = (TMSCustomField) o;
        if (tMSCustomField.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), tMSCustomField.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TMSCustomField{" +
            "id=" + getId() +
            ", entity='" + getEntityData() + "'" +
            "}";
    }
}
