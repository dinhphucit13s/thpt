package fpt.dps.dtms.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * The Projects entity.
 */
@ApiModel(description = "The Projects entity.")
@Entity
@Table(name = "project_templates")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "projecttemplates")
public class ProjectTemplates extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @NotNull
    @Lob
    @Column(name = "image", nullable = false)
    private byte[] image;

    @Column(name = "image_content_type", nullable = false)
    private String imageContentType;

    @Lob
    @Column(name = "description")
    private String description;

    @ManyToOne
    private BusinessLine businessLine;

    @OneToMany(mappedBy = "projectTemplates")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Projects> projects = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public ProjectTemplates name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getImage() {
        return image;
    }

    public ProjectTemplates image(byte[] image) {
        this.image = image;
        return this;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return imageContentType;
    }

    public ProjectTemplates imageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
        return this;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public String getDescription() {
        return description;
    }

    public ProjectTemplates description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BusinessLine getBusinessLine() {
        return businessLine;
    }

    public ProjectTemplates businessLine(BusinessLine businessLine) {
        this.businessLine = businessLine;
        return this;
    }

    public void setBusinessLine(BusinessLine businessLine) {
        this.businessLine = businessLine;
    }

    public Set<Projects> getProjects() {
        return projects;
    }

    public ProjectTemplates projects(Set<Projects> projects) {
        this.projects = projects;
        return this;
    }

    public ProjectTemplates addProject(Projects projects) {
        this.projects.add(projects);
        projects.setProjectTemplates(this);
        return this;
    }

    public ProjectTemplates removeProject(Projects projects) {
        this.projects.remove(projects);
        projects.setProjectTemplates(null);
        return this;
    }

    public void setProjects(Set<Projects> projects) {
        this.projects = projects;
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
        ProjectTemplates projectTemplates = (ProjectTemplates) o;
        if (projectTemplates.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), projectTemplates.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ProjectTemplates{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", image='" + getImage() + "'" +
            ", imageContentType='" + getImageContentType() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
