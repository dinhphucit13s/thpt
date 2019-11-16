package fpt.dps.dtms.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fpt.dps.dtms.domain.CustomReports;
import fpt.dps.dtms.domain.TMSCustomField;
import fpt.dps.dtms.repository.CustomReportsRepository;
import fpt.dps.dtms.service.dto.CustomReportsDTO;
import fpt.dps.dtms.service.dto.TMSCustomFieldDTO;
import fpt.dps.dtms.service.mapper.CustomReportsMapper;

/**
 * Service Implementation for managing BusinessLine.
 */
@Service
@Transactional
public class CustomReportsService {
	private final Logger log = LoggerFactory.getLogger(CustomReportsService.class);
	private final CustomReportsRepository customReportsRepository;
	private final CustomReportsMapper customReportsMapper;
	
	public CustomReportsService(CustomReportsRepository customReportsRepository, CustomReportsMapper customReportsMapper) {
		this.customReportsRepository = customReportsRepository;
		this.customReportsMapper = customReportsMapper;
	}
	
	/**
     * Save a tMSCustomField.
     *
     * @param tMSCustomFieldDTO the entity to save
     * @return the persisted entity
     */
    public CustomReportsDTO save(CustomReportsDTO customReportsDTO) {
        log.debug("Request to save CustomReportsDTO : {}", customReportsDTO);
        CustomReports customReports = customReportsMapper.toEntity(customReportsDTO);
        customReports = customReportsRepository.save(customReports);
        CustomReportsDTO result = customReportsMapper.toDto(customReports);
        return result;
    }
	
}
