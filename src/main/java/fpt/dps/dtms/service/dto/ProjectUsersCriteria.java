package fpt.dps.dtms.service.dto;

import java.io.Serializable;
import fpt.dps.dtms.domain.enumeration.ProjectRoles;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;






/**
 * Criteria class for the ProjectUsers entity. This class is used in ProjectUsersResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /project-users?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ProjectUsersCriteria implements Serializable {
    /**
     * Class for filtering ProjectRoles
     */
    public static class ProjectRolesFilter extends Filter<ProjectRoles> {
    }

    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter userLogin;

    private ProjectRolesFilter roleName;

    private LongFilter projectId;

    public ProjectUsersCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(StringFilter userLogin) {
        this.userLogin = userLogin;
    }

    public ProjectRolesFilter getRoleName() {
        return roleName;
    }

    public void setRoleName(ProjectRolesFilter roleName) {
        this.roleName = roleName;
    }

    public LongFilter getProjectId() {
        return projectId;
    }

    public void setProjectId(LongFilter projectId) {
        this.projectId = projectId;
    }

    @Override
    public String toString() {
        return "ProjectUsersCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (userLogin != null ? "userLogin=" + userLogin + ", " : "") +
                (roleName != null ? "roleName=" + roleName + ", " : "") +
                (projectId != null ? "projectId=" + projectId + ", " : "") +
            "}";
    }

}
