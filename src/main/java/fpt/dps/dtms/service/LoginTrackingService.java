package fpt.dps.dtms.service;

import fpt.dps.dtms.domain.LoginTracking;
import fpt.dps.dtms.repository.LoginTrackingRepository;
import fpt.dps.dtms.repository.search.LoginTrackingSearchRepository;
import fpt.dps.dtms.service.dto.LoginTrackingDTO;
import fpt.dps.dtms.service.mapper.LoginTrackingMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

import java.time.Instant;
import java.time.ZoneId;

/**
 * Service Implementation for managing LoginTracking.
 */
@Service
@Transactional
public class LoginTrackingService {

    private final Logger log = LoggerFactory.getLogger(LoginTrackingService.class);

    private final LoginTrackingRepository loginTrackingRepository;

    private final LoginTrackingMapper loginTrackingMapper;

    private final LoginTrackingSearchRepository loginTrackingSearchRepository;

    public LoginTrackingService(LoginTrackingRepository loginTrackingRepository, LoginTrackingMapper loginTrackingMapper, LoginTrackingSearchRepository loginTrackingSearchRepository) {
        this.loginTrackingRepository = loginTrackingRepository;
        this.loginTrackingMapper = loginTrackingMapper;
        this.loginTrackingSearchRepository = loginTrackingSearchRepository;
    }

    /**
     * Save a loginTracking.
     *
     * @param loginTrackingDTO the entity to save
     * @return the persisted entity
     */
    public LoginTrackingDTO save(LoginTrackingDTO loginTrackingDTO) {
        log.debug("Request to save LoginTracking : {}", loginTrackingDTO);
        LoginTracking loginTracking = loginTrackingMapper.toEntity(loginTrackingDTO);
        loginTracking = loginTrackingRepository.save(loginTracking);
        LoginTrackingDTO result = loginTrackingMapper.toDto(loginTracking);
        loginTrackingSearchRepository.save(loginTracking);
        return result;
    }

    /**
     * Get all the loginTrackings.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<LoginTrackingDTO> findAll(Pageable pageable) {
        log.debug("Request to get all LoginTrackings");
        return loginTrackingRepository.findAll(pageable)
            .map(loginTrackingMapper::toDto);
    }

    /**
     * Get one loginTracking by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public LoginTrackingDTO findOne(Long id) {
        log.debug("Request to get LoginTracking : {}", id);
        LoginTracking loginTracking = loginTrackingRepository.findOne(id);
        return loginTrackingMapper.toDto(loginTracking);
    }

    /**
     * Delete the loginTracking by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete LoginTracking : {}", id);
        loginTrackingRepository.delete(id);
        loginTrackingSearchRepository.delete(id);
    }

    /**
     * Search for the loginTracking corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<LoginTrackingDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of LoginTrackings for query {}", query);
        Page<LoginTracking> result = loginTrackingSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(loginTrackingMapper::toDto);
    }
    
    /**
     * Update endtime to loginTracking when user sign out or exit  
     */
	public void updateLoginTracking(String login) {
		LoginTracking loginTracking = loginTrackingRepository.findFirstByLoginOrderByStartTimeDesc(login);
		if(loginTracking != null) {
			loginTracking.setEndTime(Instant.now());
		}
		loginTrackingRepository.save(loginTracking);
		loginTrackingSearchRepository.save(loginTracking);
	}
}
