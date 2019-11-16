package fpt.dps.dtms.web.rest;

import com.codahale.metrics.annotation.Timed;
import fpt.dps.dtms.service.AuthorityResourceService;
import fpt.dps.dtms.service.RoleService;
import fpt.dps.dtms.web.rest.errors.BadRequestAlertException;
import fpt.dps.dtms.web.rest.util.HeaderUtil;
import fpt.dps.dtms.web.rest.util.PaginationUtil;
import fpt.dps.dtms.web.rest.vm.AuthorityVM;
import fpt.dps.dtms.web.rest.vm.UserAuthorityVM;
import fpt.dps.dtms.service.dto.AuthorityDTO;
import fpt.dps.dtms.service.dto.PackagesDTO;
import fpt.dps.dtms.service.AuthorityResourceQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing AuthorityResource.
 */
@RestController
@RequestMapping("/api")
public class RoleResource {

    private final Logger log = LoggerFactory.getLogger(AuthorityResourceResource.class);

    private static final String ENTITY_NAME = "authorityResource";
    
    private final RoleService roleService;

    public RoleResource(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping("/roles")
    @Timed
    @PreAuthorize("@jhiAuth.hasPermission('ADMIN_RESOURCE', 'CREATE')")
    public ResponseEntity<AuthorityDTO> createRole(@Valid @RequestBody AuthorityVM authorityVM) throws URISyntaxException {
        log.debug("REST request to save AuthorityVM : {}", authorityVM);
        AuthorityDTO result = roleService.save(authorityVM);
        return ResponseEntity.created(new URI("/api/roles/" + result.getName()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getName().toString()))
            .body(result);
    }

    @PutMapping("/roles")
    @Timed
    @PreAuthorize("@jhiAuth.hasPermission('ADMIN_RESOURCE', 'EDIT')")
    public ResponseEntity<AuthorityDTO> updateAuthorityResource(@Valid @RequestBody AuthorityVM authorityVM) throws URISyntaxException {
        log.debug("REST request to update Authority : {}", authorityVM);
        if (authorityVM.getName() == null) {
            return createRole(authorityVM);
        }
        AuthorityDTO result = roleService.save(authorityVM);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, authorityVM.getName().toString()))
            .body(result);
    }

    @GetMapping("/roles")
	@Timed
	@PreAuthorize("@jhiAuth.hasPermission('ADMIN_RESOURCE', 'VIEW')")
	public ResponseEntity<List<AuthorityDTO>> getAllPackages(@RequestParam(value = "status", required = false) Boolean status, Pageable pageable) {
		log.debug("REST request to get a page of ");
		Page<AuthorityDTO> page = roleService.findAll(pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/roles");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

    @GetMapping("/roles/{name}")
    @Timed
    @PreAuthorize("@jhiAuth.hasPermission('ADMIN_RESOURCE', 'VIEW')")
    public ResponseEntity<AuthorityVM> getAuthorityResource(@PathVariable String name) {
        log.debug("REST request to get Authority : {}", name);
        AuthorityVM authorityVM = roleService.findOne(name);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(authorityVM));
    }
    
    @GetMapping("/permission")
    @Timed
    public ResponseEntity<UserAuthorityVM> getAuthorityResourceByUsername() {
        log.debug("REST request to get permission of current ");
        UserAuthorityVM authorityVM = roleService.findPermissionOfCurrentUser();
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(authorityVM));
    }

    @DeleteMapping("/roles/{name}")
    @Timed
    @PreAuthorize("@jhiAuth.hasPermission('ADMIN_RESOURCE', 'DELETE')")
    public ResponseEntity<Void> deleteAuthority(@PathVariable String name) {
        log.debug("REST request to delete Authority : {}", name);
        roleService.delete(name);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, name)).build();
    }
}