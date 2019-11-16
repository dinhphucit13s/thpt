package fpt.dps.dtms.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fpt.dps.dtms.domain.Attachments;
import fpt.dps.dtms.domain.Attachments_;
import fpt.dps.dtms.domain.Bugs_;
import fpt.dps.dtms.repository.AttachmentsRepository;
import fpt.dps.dtms.repository.search.AttachmentsSearchRepository;
import fpt.dps.dtms.service.dto.AttachmentsCriteria;
import fpt.dps.dtms.service.dto.AttachmentsDTO;
import fpt.dps.dtms.service.mapper.AttachmentsMapper;
import io.github.jhipster.service.QueryService;

/**
 * Service Implementation for managing Attachments.
 */
@Service
@Transactional(readOnly = true)
public class AttachmentsQueryService extends QueryService<Attachments>{
	
	private final Logger log = LoggerFactory.getLogger(AttachmentsQueryService.class);

	private final AttachmentsRepository attachmentsRepository;

	private final AttachmentsMapper attachmentsMapper;

    private final AttachmentsSearchRepository attachmentsSearchRepository;
    
    public AttachmentsQueryService(AttachmentsRepository attachmentsRepository, AttachmentsMapper attachmentsMapper,
			AttachmentsSearchRepository attachmentsSearchRepository) {
		super();
		this.attachmentsRepository = attachmentsRepository;
		this.attachmentsMapper = attachmentsMapper;
		this.attachmentsSearchRepository = attachmentsSearchRepository;
	}

	@Transactional(readOnly = true)
    public Page<AttachmentsDTO> findAttachmentsByBugsId(Long bugsId, Pageable page) {
        log.debug("find by Bugs ID");
        Page<Attachments> result = attachmentsRepository.findAllAttachmentsByBugsId(bugsId, page);
        return result.map(attachmentsMapper::toDto);
    }

    
    /**
     * Return a {@link List} of {@link AttachmentsDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AttachmentsDTO> findByCriteria(AttachmentsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Attachments> specification = createSpecification(criteria);
        return attachmentsMapper.toDto(attachmentsRepository.findAll(specification));
    }
    
    /**
     * Return a {@link Page} of {@link AttachmentsDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AttachmentsDTO> findByCriteria(AttachmentsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Attachments> specification = createSpecification(criteria);
        final Page<Attachments> result = attachmentsRepository.findAll(specification, page);
        return result.map(attachmentsMapper::toDto);
    }
    
    /**
     * 
     * @param criteria
     * @return
     */
    private Specifications<Attachments> createSpecification(AttachmentsCriteria criteria) {
        Specifications<Attachments> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Attachments_.id));
            }
            if (criteria.getBugsId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getBugsId(), Attachments_.bugs, Bugs_.id));
            }
        }
        return specification;
    }
    

}