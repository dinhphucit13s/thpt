package fpt.dps.dtms.service;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import fpt.dps.dtms.domain.MailReceiver;
import fpt.dps.dtms.domain.*; // for static metamodels
import fpt.dps.dtms.repository.MailReceiverRepository;
import fpt.dps.dtms.repository.search.MailReceiverSearchRepository;
import fpt.dps.dtms.service.dto.MailReceiverCriteria;

import fpt.dps.dtms.service.dto.MailReceiverDTO;
import fpt.dps.dtms.service.mapper.MailReceiverMapper;

/**
 * Service for executing complex queries for MailReceiver entities in the database.
 * The main input is a {@link MailReceiverCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link MailReceiverDTO} or a {@link Page} of {@link MailReceiverDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MailReceiverQueryService extends QueryService<MailReceiver> {

    private final Logger log = LoggerFactory.getLogger(MailReceiverQueryService.class);


    private final MailReceiverRepository mailReceiverRepository;

    private final MailReceiverMapper mailReceiverMapper;
    
    private final MailReceiverService mailReceiverService;

    private final MailReceiverSearchRepository mailReceiverSearchRepository;

    public MailReceiverQueryService(MailReceiverRepository mailReceiverRepository, MailReceiverMapper mailReceiverMapper, MailReceiverSearchRepository mailReceiverSearchRepository, MailReceiverService mailReceiverService) {
        this.mailReceiverRepository = mailReceiverRepository;
        this.mailReceiverMapper = mailReceiverMapper;
        this.mailReceiverSearchRepository = mailReceiverSearchRepository;
        this.mailReceiverService = mailReceiverService;
    }

    /**
     * Return a {@link List} of {@link MailReceiverDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<MailReceiverDTO> findByCriteria(MailReceiverCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<MailReceiver> specification = createSpecification(criteria);
        return mailReceiverMapper.toDto(mailReceiverRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link MailReceiverDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MailReceiverDTO> findByCriteria(MailReceiverCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<MailReceiver> specification = createSpecification(criteria);
        final Page<MailReceiver> result = mailReceiverRepository.findAll(specification, page);
        return result.map(mailReceiverMapper::toDto);
    }
    
    /**
     * Function to convert MailReceiverCriteria to a {@link Specifications}
     */
    private Specifications<MailReceiver> createSpecification(MailReceiverCriteria criteria) {
        Specifications<MailReceiver> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), MailReceiver_.id));
            }
            if (criteria.getFrom() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFrom(), MailReceiver_.from));
            }
            if (criteria.getTo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTo(), MailReceiver_.to));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), MailReceiver_.status));
            }
            if (criteria.getMailId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getMailId(), MailReceiver_.mail, Mail_.id));
            }
        }
        return specification;
    }

}
