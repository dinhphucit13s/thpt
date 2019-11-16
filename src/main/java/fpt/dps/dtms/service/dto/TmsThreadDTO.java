package fpt.dps.dtms.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the TmsThread entity.
 */
public class TmsThreadDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    @NotNull
    private String title;

    private Integer views;

    private Integer answers;

    @NotNull
    private Boolean closed;

    @NotNull
    private Boolean status;

    private Long projectsId;

    private String projectsName;

    private Long assigneeId;

    private String assigneeUserLogin;
    
    private Set<TmsPostDTO> posts = new HashSet<>();

    /**
	 * @return the tmsPosts
	 */
	public Set<TmsPostDTO> getPosts() {
		return posts;
	}

	/**
	 * @param tmsPosts the tmsPosts to set
	 */
	public void setPosts(Set<TmsPostDTO> tmsPosts) {
		this.posts = tmsPosts;
	}

	/**
	 * @return the closed
	 */
	public Boolean getClosed() {
		return closed;
	}

	/**
	 * @return the status
	 */
	public Boolean getStatus() {
		return status;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getViews() {
        return views;
    }

    public void setViews(Integer views) {
        this.views = views;
    }

    public Integer getAnswers() {
        return answers;
    }

    public void setAnswers(Integer answers) {
        this.answers = answers;
    }

    public Boolean isClosed() {
        return closed;
    }

    public void setClosed(Boolean closed) {
        this.closed = closed;
    }

    public Boolean isStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Long getProjectsId() {
        return projectsId;
    }

    public void setProjectsId(Long projectsId) {
        this.projectsId = projectsId;
    }

    public String getProjectsName() {
        return projectsName;
    }

    public void setProjectsName(String projectsName) {
        this.projectsName = projectsName;
    }

    public Long getAssigneeId() {
        return assigneeId;
    }

    public void setAssigneeId(Long projectUsersId) {
        this.assigneeId = projectUsersId;
    }

    public String getAssigneeUserLogin() {
        return assigneeUserLogin;
    }

    public void setAssigneeUserLogin(String projectUsersUserLogin) {
        this.assigneeUserLogin = projectUsersUserLogin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TmsThreadDTO tmsThreadDTO = (TmsThreadDTO) o;
        if(tmsThreadDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), tmsThreadDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TmsThreadDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", views=" + getViews() +
            ", answers=" + getAnswers() +
            ", closed='" + isClosed() + "'" +
            ", status='" + isStatus() + "'" +
            "}";
    }
}
