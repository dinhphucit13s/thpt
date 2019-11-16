package fpt.dps.dtms.service;

import fpt.dps.dtms.domain.DtmsMonitoring;
import fpt.dps.dtms.domain.enumeration.MONITORINGROLE;
import fpt.dps.dtms.domain.enumeration.PositionMonitoring;
import fpt.dps.dtms.repository.DtmsMonitoringRepository;
import fpt.dps.dtms.repository.search.DtmsMonitoringSearchRepository;
import fpt.dps.dtms.service.dto.DtmsMonitoringDTO;
import fpt.dps.dtms.service.mapper.DtmsMonitoringMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Service Implementation for managing DtmsMonitoring.
 */
@Service
@Transactional
public class DtmsMonitoringService {

    private final Logger log = LoggerFactory.getLogger(DtmsMonitoringService.class);

    private final DtmsMonitoringRepository dtmsMonitoringRepository;

    private final DtmsMonitoringMapper dtmsMonitoringMapper;

    private final DtmsMonitoringSearchRepository dtmsMonitoringSearchRepository;

    public DtmsMonitoringService(DtmsMonitoringRepository dtmsMonitoringRepository, DtmsMonitoringMapper dtmsMonitoringMapper, DtmsMonitoringSearchRepository dtmsMonitoringSearchRepository) {
        this.dtmsMonitoringRepository = dtmsMonitoringRepository;
        this.dtmsMonitoringMapper = dtmsMonitoringMapper;
        this.dtmsMonitoringSearchRepository = dtmsMonitoringSearchRepository;
    }

    /**
     * Save a dtmsMonitoring.
     *
     * @param dtmsMonitoringDTO the entity to save
     * @return the persisted entity
     */
    public DtmsMonitoringDTO save(DtmsMonitoringDTO dtmsMonitoringDTO) {
        log.debug("Request to save DtmsMonitoring : {}", dtmsMonitoringDTO);
        DtmsMonitoring dtmsMonitoring = dtmsMonitoringMapper.toEntity(dtmsMonitoringDTO);
        dtmsMonitoring = dtmsMonitoringRepository.save(dtmsMonitoring);
        DtmsMonitoringDTO result = dtmsMonitoringMapper.toDto(dtmsMonitoring);
        dtmsMonitoringSearchRepository.save(dtmsMonitoring);
        return result;
    }

    /**
     * Get all the dtmsMonitorings.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<DtmsMonitoringDTO> findAll(Pageable pageable) {
        log.debug("Request to get all DtmsMonitorings");
        return dtmsMonitoringRepository.findAll(pageable)
            .map(dtmsMonitoringMapper::toDto);
    }

    /**
     * Get one dtmsMonitoring by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public DtmsMonitoringDTO findOne(Long id) {
        log.debug("Request to get DtmsMonitoring : {}", id);
        DtmsMonitoring dtmsMonitoring = dtmsMonitoringRepository.findOne(id);
        return dtmsMonitoringMapper.toDto(dtmsMonitoring);
    }

    /**
     * Delete the dtmsMonitoring by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete DtmsMonitoring : {}", id);
        dtmsMonitoringRepository.delete(id);
        dtmsMonitoringSearchRepository.delete(id);
    }

    /**
     * Search for the dtmsMonitoring corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<DtmsMonitoringDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of DtmsMonitorings for query {}", query);
        Page<DtmsMonitoring> result = dtmsMonitoringSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(dtmsMonitoringMapper::toDto);
    }

    public List<DtmsMonitoringDTO> getDtmsMonitoringUsers(PositionMonitoring position, Long positionId, MONITORINGROLE role) {
    	log.debug("Request to get DtmsMonitoring Users ");
    	List<DtmsMonitoring> result = dtmsMonitoringRepository.getDtmsMonitoringUsers(position, positionId, role);
    	return dtmsMonitoringMapper.toDto(result);
    }

    public String[] getArraysDtmsMonitoringUsers(PositionMonitoring position, Long positionId, MONITORINGROLE role) {
    	log.debug("Request to get DtmsMonitoring Users ");
    	List<DtmsMonitoring> result = dtmsMonitoringRepository.getDtmsMonitoringUsers(position, positionId, role);
    	List<String> listUsername = new ArrayList<>();
    	for (DtmsMonitoring dtmsMonitoring : result) {
			listUsername.add(dtmsMonitoring.getMembers());
		}
    	return listUsername.toArray(new String[0]);
    }

    public List<DtmsMonitoringDTO> getDtmsMonitoringByUserLogin(PositionMonitoring position, String userLogin) {
    	log.debug("Request to get DtmsMonitoring by user login");
    	List<DtmsMonitoring> result = dtmsMonitoringRepository.getDtmsMonitoringByUserLogin(position, userLogin);
    	return dtmsMonitoringMapper.toDto(result);
    }

    public List<DtmsMonitoringDTO> getDtmsMonitoringByUserAndPosition(PositionMonitoring position, String userLogin) {
    	log.debug("Request to get DtmsMonitoring By User and Position ");
    	List<DtmsMonitoring> result = dtmsMonitoringRepository.getDtmsMonitoringByUserLoginAndPosition(position, userLogin);
    	return dtmsMonitoringMapper.toDto(result);
    }
    
    public DtmsMonitoringDTO findByAllCondition(PositionMonitoring position, Long positionId, String userLogin, MONITORINGROLE role) {
    	log.debug("Request to get DtmsMonitoring by all condition ");
    	DtmsMonitoring result = dtmsMonitoringRepository.findByAllCondition(position, positionId, userLogin, role);
    	return dtmsMonitoringMapper.toDto(result);
    }
}
