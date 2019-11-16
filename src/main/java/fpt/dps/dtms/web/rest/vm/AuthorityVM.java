package fpt.dps.dtms.web.rest.vm;

import java.util.Set;

import fpt.dps.dtms.domain.AuthorityResource;
import fpt.dps.dtms.service.dto.AuthorityDTO;

public class AuthorityVM extends AuthorityDTO {
	private Set<AuthorityResource> resources;

	public Set<AuthorityResource> getResources() {
		return resources;
	}

	public void setResources(Set<AuthorityResource> resources) {
		this.resources = resources;
	}
}
