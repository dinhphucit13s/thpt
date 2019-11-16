package fpt.dps.dtms.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fpt.dps.dtms.domain.CustomReports;
import fpt.dps.dtms.domain.Packages;
import fpt.dps.dtms.repository.CustomReportsRepository;
import fpt.dps.dtms.repository.CustomerRepository;
import fpt.dps.dtms.service.dto.CustomReportsDTO;
import fpt.dps.dtms.service.dto.PackagesCriteria;
import fpt.dps.dtms.service.dto.PackagesDTO;
import fpt.dps.dtms.service.mapper.CustomReportsMapper;
import io.github.jhipster.service.QueryService;

/**
 * Service for executing complex queries for Packages entities in the database.
 * The main input is a {@link PackagesCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PackagesDTO} or a {@link Page} of {@link PackagesDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CustomReportsQueryService extends QueryService<CustomReports>{
	private final Logger log = LoggerFactory.getLogger(CustomReportsQueryService.class);
	
	private final CustomReportsRepository customReportsRepository;
	
	private final CustomReportsMapper customReportsMapper;
	
	public CustomReportsQueryService(CustomReportsRepository customReportsRepository, CustomReportsMapper customReportsMapper) {
		this.customReportsRepository = customReportsRepository;
		this.customReportsMapper = customReportsMapper;
	}
	
	@Transactional(readOnly = true)
    public CustomReportsDTO findCustomReportsByPageName(String pageName, String userLogin) {
        log.debug("find by criteria : {}", pageName);
        return customReportsMapper.toDto(customReportsRepository.findCustomReportByPageName(pageName, userLogin));
    }
}
