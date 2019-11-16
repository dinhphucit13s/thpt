package fpt.dps.dtms.service.dto.msc;

import java.time.Instant;
import java.util.stream.Collectors;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

import fpt.dps.dtms.config.Constants;
import fpt.dps.dtms.domain.Authority;
import fpt.dps.dtms.domain.User;

public class UserMSCDTO {
	private Long id;

    @NotBlank
    @Pattern(regexp = Constants.LOGIN_REGEX)
    @Size(min = 1, max = 50)
    private String login;
    
    private boolean activated = false;
    
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}
	
	public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

	public UserMSCDTO() {
        // Empty constructor needed for Jackson.
    }
	
	public UserMSCDTO(User user) {
        this.id = user.getId();
        this.login = user.getLogin();
        this.activated = user.getActivated();
    }
	
	@Override
    public String toString() {
        return "UserDTO{" +
            "login='" + login + '\'' +
            "}";
    }
}
