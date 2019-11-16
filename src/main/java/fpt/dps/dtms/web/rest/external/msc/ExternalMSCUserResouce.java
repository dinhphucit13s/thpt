package fpt.dps.dtms.web.rest.external.msc;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.codahale.metrics.annotation.Timed;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fpt.dps.dtms.service.UserService;
import fpt.dps.dtms.service.dto.UserDTO;
import fpt.dps.dtms.service.dto.msc.UserMSCDTO;

/**
 * REST controller for User Resource.
 */
@RestController
@RequestMapping("/api/external/msc")
public class ExternalMSCUserResouce {
	private final Logger log = LoggerFactory.getLogger(ExternalMSCUserResouce.class);
	private static final String ENTITY_NAME = "externalMSCUserResource";
	
	private final UserService userService;

    public ExternalMSCUserResouce(UserService userService) {
        this.userService = userService;
    }

    /**
     * GET  /business-units : get all the businessUnits.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of businessUnits in body
     */
    @GetMapping("/users")
    @Timed
    public ResponseEntity<List<UserMSCDTO>> getAllUsers() {
        final List<UserMSCDTO> listUsers = userService.getAllManagedUsersForMSC();
        return new ResponseEntity<>(listUsers, HttpStatus.OK);
    }
}
