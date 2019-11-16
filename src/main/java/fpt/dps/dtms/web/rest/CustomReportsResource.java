package fpt.dps.dtms.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

import fpt.dps.dtms.service.CustomReportsQueryService;
import fpt.dps.dtms.service.CustomReportsService;
import fpt.dps.dtms.service.dto.CustomReportsDTO;
import fpt.dps.dtms.service.dto.PackagesCriteria;
import fpt.dps.dtms.service.dto.PackagesDTO;
import fpt.dps.dtms.web.rest.errors.BadRequestAlertException;
import fpt.dps.dtms.web.rest.util.HeaderUtil;
import fpt.dps.dtms.web.rest.util.PaginationUtil;

/**
 * REST controller for managing CustomReports.
 */
@RestController
@RequestMapping("/api")
public class CustomReportsResource {
	private final Logger log = LoggerFactory.getLogger(CustomReportsResource.class);
	
	private static final String ENTITY_NAME = "customReports";
	
	private final CustomReportsService customReportsService;
	
	private final CustomReportsQueryService customReportsQueryService;
	
	public CustomReportsResource(CustomReportsService customReportsService, CustomReportsQueryService customReportsQueryService) {
		this.customReportsService = customReportsService;
		this.customReportsQueryService = customReportsQueryService;
	}
	
	/**
     * POST  /dashboad/reports : Create a new CustomReports.
     *
     * @param customReportsDTO the customReportsDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new customReportsDTO, or with status 400 (Bad Request) if the customReports has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/customReports")
    @Timed
    public ResponseEntity<CustomReportsDTO> createTMSCustomField(@RequestBody CustomReportsDTO customReportsDTO) throws URISyntaxException {
        log.debug("REST request to save CustomReportsDTO : {}", customReportsDTO);
        if (customReportsDTO.getId() != null) {
            throw new BadRequestAlertException("A new customReports cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CustomReportsDTO result = customReportsService.save(customReportsDTO);
        return ResponseEntity.created(new URI("/api/dashboad/reports/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }
    
    /**
     * PUT  /dashboad/reports : Updates an existing CustomReports.
     *
     * @param customReportsDTO the customReportsDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated customReportsDTO,
     * or with status 400 (Bad Request) if the tMSCustomFieldDTO is not valid,
     * or with status 500 (Internal Server Error) if the customReportsDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/customReports")
    @Timed
    public ResponseEntity<CustomReportsDTO> updateTMSCustomField(@RequestBody CustomReportsDTO customReportsDTO) throws URISyntaxException {
        log.debug("REST request to update customReportsDTO : {}", customReportsDTO);
        if (customReportsDTO.getId() == null) {
            return createTMSCustomField(customReportsDTO);
        }
        CustomReportsDTO result = customReportsService.save(customReportsDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, customReportsDTO.getId().toString()))
            .body(result);
    }
    
    /**
     * GET  /packages : get all the packages.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of packages in body
     */
    @GetMapping("/customReports")
    @Timed
    public ResponseEntity<CustomReportsDTO> getCustomReportsByPageName(@RequestParam(value = "pageName", required = false) String pageName, @RequestParam(value = "userLogin", required = false) String userLogin) {
        log.debug("REST request to get customReports by criteria: {}", pageName);
        CustomReportsDTO page = customReportsQueryService.findCustomReportsByPageName(pageName, userLogin);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }
}
