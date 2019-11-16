package fpt.dps.dtms.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the AuthorityResource entity.
 */
public class AuthorityResourceDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String name;

    @NotNull
    private Integer permission;

    @Size(max = 150)
    private String authorityName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPermission() {
        return permission;
    }

    public void setPermission(Integer permission) {
        this.permission = permission;
    }

    public String getAuthorityName() {
        return authorityName;
    }

    public void setAuthorityName(String authorityName) {
        this.authorityName = authorityName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AuthorityResourceDTO authorityResourceDTO = (AuthorityResourceDTO) o;
        if(authorityResourceDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), authorityResourceDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AuthorityResourceDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", permission=" + getPermission() +
            ", authorityName='" + getAuthorityName() + "'" +
            "}";
    }
}
