package fpt.dps.dtms.web.rest.vm;

import java.util.Set;

import fpt.dps.dtms.domain.AuthorityResource;

public class UserAuthorityVM {
	private String username;
	private Set<AuthorityResource> resources;

	public Set<AuthorityResource> getResources() {
		return resources;
	}

	public void setResources(Set<AuthorityResource> resources) {
		this.resources = resources;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
