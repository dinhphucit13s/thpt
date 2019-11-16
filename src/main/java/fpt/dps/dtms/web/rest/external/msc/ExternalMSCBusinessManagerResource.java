package fpt.dps.dtms.web.rest.external.msc;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

import fpt.dps.dtms.service.BusinessUnitManagerService;
import fpt.dps.dtms.service.BusinessUnitQueryService;
import fpt.dps.dtms.service.UserService;
import fpt.dps.dtms.service.dto.BusinessUnitMSCDTO;
import fpt.dps.dtms.service.dto.BusinessUnitManagerDTO;
import fpt.dps.dtms.service.dto.BusinessUnitManagerMSCDTO;

/**
 * REST controller for managing BusinessUnit.
 */
@RestController
@RequestMapping("/api/external/msc")
public class ExternalMSCBusinessManagerResource {
	private final Logger log = LoggerFactory.getLogger(ExternalMSCBusinessManagerResource.class);
	private static final String ENTITY_NAME = "externalMSCBusinessManagerResource";
	
	private final BusinessUnitQueryService businessUnitQueryService;
	
	private final BusinessUnitManagerService businessUnitManagerService;
	
    public ExternalMSCBusinessManagerResource(BusinessUnitQueryService businessUnitQueryService, BusinessUnitManagerService businessUnitManagerService) {
        this.businessUnitQueryService = businessUnitQueryService;
        this.businessUnitManagerService = businessUnitManagerService;
    }
    
    @GetMapping("/business-units-manager")
    @Timed
    public ResponseEntity<List<BusinessUnitManagerMSCDTO>> getAllBusinessUnitsMSC() {
        log.debug("REST request to get BusinessUnits by criteria: {}");
        List<BusinessUnitManagerMSCDTO> page = businessUnitManagerService.findAlForMSCl();
        
        return new ResponseEntity<>(page, HttpStatus.OK);
    }
}
