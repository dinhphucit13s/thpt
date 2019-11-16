package fpt.dps.dtms.web.rest.external;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

import fpt.dps.dtms.service.BusinessUnitQueryService;
import fpt.dps.dtms.service.BusinessUnitService;
import fpt.dps.dtms.service.dto.BusinessUnitCriteria;
import fpt.dps.dtms.service.dto.BusinessUnitDTO;
import fpt.dps.dtms.service.dto.BusinessUnitMSCDTO;
import fpt.dps.dtms.web.rest.util.PaginationUtil;

/**
 * REST controller for managing BU.
 */
@RestController
@RequestMapping("/api/external")
public class ExternalBusinessUnitResource {

    private final Logger log = LoggerFactory.getLogger(ExternalBugsResource.class);

    private static final String ENTITY_NAME = "business-units";
    
    private final BusinessUnitService businessUnitService;

    private final BusinessUnitQueryService businessUnitQueryService;

    public ExternalBusinessUnitResource(BusinessUnitService businessUnitService, BusinessUnitQueryService businessUnitQueryService) {
        this.businessUnitService = businessUnitService;
        this.businessUnitQueryService = businessUnitQueryService;
    }
    
    @GetMapping("/msc/business-units")
    @Timed
    public ResponseEntity<List<BusinessUnitMSCDTO>> getAllBusinessUnitsMSC() {
        log.debug("REST request to get BusinessUnits by criteria: {}");
        List<BusinessUnitMSCDTO> page = businessUnitQueryService.findAllForMSC();
        return new ResponseEntity<>(page, HttpStatus.OK);
    }
    
    /**
     * GET  /business-units : get all the businessUnits.
     *
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of businessUnits in body
     */
    @GetMapping("/business-units")
    @Timed
    public ResponseEntity<List<BusinessUnitDTO>> getAllBusinessUnits() {
        log.debug("REST request to get BusinessUnits {}");
        List<BusinessUnitDTO> result = businessUnitQueryService.findAll();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    
    /**
     * GET  /business-unit/effective : get all the businessUnits effective by user login.
     *
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of businessUnits in body
     */
    @GetMapping("/business-units/effective/{userLogin}")
    @Timed
    public ResponseEntity<List<BusinessUnitDTO>> getListEffectBUByUserLogin(@PathVariable("userLogin") String userLogin) {
        log.debug("REST request to get BusinessUnits {}");
        List<BusinessUnitDTO> result = businessUnitQueryService.findListEffectBUByUserLogin(userLogin);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
