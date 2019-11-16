package fpt.dps.dtms.service;

import fpt.dps.dtms.domain.MailReceiver;
import fpt.dps.dtms.repository.MailReceiverRepository;
import fpt.dps.dtms.repository.search.MailReceiverSearchRepository;
import fpt.dps.dtms.service.dto.MailReceiverDTO;
import fpt.dps.dtms.service.mapper.MailReceiverMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing MailReceiver.
 */
@Service
@Transactional
public class MailReceiverService {

    private final Logger log = LoggerFactory.getLogger(MailReceiverService.class);

    private final MailReceiverRepository mailReceiverRepository;

    private final MailReceiverMapper mailReceiverMapper;

    private final MailReceiverSearchRepository mailReceiverSearchRepository;

    public MailReceiverService(MailReceiverRepository mailReceiverRepository, MailReceiverMapper mailReceiverMapper, MailReceiverSearchRepository mailReceiverSearchRepository) {
        this.mailReceiverRepository = mailReceiverRepository;
        this.mailReceiverMapper = mailReceiverMapper;
        this.mailReceiverSearchRepository = mailReceiverSearchRepository;
    }

    /**
     * Save a mailReceiver.
     *
     * @param mailReceiverDTO the entity to save
     * @return the persisted entity
     */
    public MailReceiverDTO save(MailReceiverDTO mailReceiverDTO) {
        log.debug("Request to save MailReceiver : {}", mailReceiverDTO);
        MailReceiver mailReceiver = mailReceiverMapper.toEntity(mailReceiverDTO);
        mailReceiver = mailReceiverRepository.save(mailReceiver);
        MailReceiverDTO result = mailReceiverMapper.toDto(mailReceiver);
        mailReceiverSearchRepository.save(mailReceiver);
        return result;
    }
    
    public Boolean setStatusMailReceiver(String userLogin, Long id) {
        MailReceiver mailReceiver = mailReceiverRepository.getMailToByMailId(userLogin, id);
        mailReceiver.setStatus(true);
        if (mailReceiverRepository.save(mailReceiver) != null) {
        	return true;
        }
        return false;
    }

    /**
     * Get all the mailReceivers.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<MailReceiverDTO> findAll(Pageable pageable) {
        log.debug("Request to get all MailReceivers");
        return mailReceiverRepository.findAll(pageable)
            .map(mailReceiverMapper::toDto);
    }

    /**
     * Get one mailReceiver by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public MailReceiverDTO findOne(Long id) {
        log.debug("Request to get MailReceiver : {}", id);
        MailReceiver mailReceiver = mailReceiverRepository.findOne(id);
        return mailReceiverMapper.toDto(mailReceiver);
    }

    /**
     * Delete the mailReceiver by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete MailReceiver : {}", id);
        mailReceiverRepository.delete(id);
        mailReceiverSearchRepository.delete(id);
    }
    
    /**
     * Delete the mailReceiver To by id mail and user login.
     *
     * @param id the id of the entity
     */
    public void deleteMailTo(Long id, String userName) {
        log.debug("Request to delete MailReceiver : {}", id);
        MailReceiver mailReceiver = mailReceiverRepository.getMailToByMailId(userName, id);
        mailReceiverRepository.delete(mailReceiver.getId());
        mailReceiverSearchRepository.delete(mailReceiver.getId());
    }
    
    /**
     * Delete the mailReceiver From by id mail and user login.
     *
     * @param id the id of the entity
     */
    public void deleteMailFrom(Long id, String userName) {
        log.debug("Request to delete MailReceiver : {}", id);
        MailReceiver mailReceiver = mailReceiverRepository.getMailFromByMailId(userName, id);
        mailReceiverRepository.delete(mailReceiver.getId());
        mailReceiverSearchRepository.delete(mailReceiver.getId());
    }

    /**
     * Search for the mailReceiver corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<MailReceiverDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of MailReceivers for query {}", query);
        Page<MailReceiver> result = mailReceiverSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(mailReceiverMapper::toDto);
    }
}
