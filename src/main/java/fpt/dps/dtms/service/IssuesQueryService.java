package fpt.dps.dtms.service;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import io.github.jhipster.service.QueryService;
import fpt.dps.dtms.domain.*; // for static metamodels
import fpt.dps.dtms.repository.AttachmentsRepository;
import fpt.dps.dtms.repository.IssuesRepository;
import fpt.dps.dtms.repository.search.IssuesSearchRepository;
import fpt.dps.dtms.service.dto.AttachmentsDTO;
import fpt.dps.dtms.service.dto.IssuesCriteria;

import fpt.dps.dtms.service.dto.IssuesDTO;
import fpt.dps.dtms.service.mapper.AttachmentsMapper;
import fpt.dps.dtms.service.mapper.IssuesMapper;
import fpt.dps.dtms.domain.enumeration.IssueStatus;

/**
 * Service for executing complex queries for Issues entities in the database.
 * The main input is a {@link IssuesCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link IssuesDTO} or a {@link Page} of {@link IssuesDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class IssuesQueryService extends QueryService<Issues> {

    private final Logger log = LoggerFactory.getLogger(IssuesQueryService.class);

    private final IssuesRepository issuesRepository;
    
    private final AttachmentsRepository attachmentsRepository;

    private final IssuesMapper issuesMapper;
    
    private final AttachmentsMapper attachmentsMapper;
    
    private final StorageService storageService;

    private final IssuesSearchRepository issuesSearchRepository;

    public IssuesQueryService(IssuesRepository issuesRepository, IssuesMapper issuesMapper, IssuesSearchRepository issuesSearchRepository, AttachmentsRepository attachmentsRepository, AttachmentsMapper attachmentsMapper, StorageService storageService) {
        this.issuesRepository = issuesRepository;
        this.issuesMapper = issuesMapper;
        this.issuesSearchRepository = issuesSearchRepository;
        this.attachmentsRepository = attachmentsRepository;
        this.attachmentsMapper = attachmentsMapper;
        this.storageService = storageService;
    }

    /**
     * Return a {@link List} of {@link IssuesDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<IssuesDTO> findByCriteria(IssuesCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Issues> specification = createSpecification(criteria);
        return issuesMapper.toDto(issuesRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link IssuesDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<IssuesDTO> findByCriteria(IssuesCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Issues> specification = createSpecification(criteria);
        final Page<Issues> result = issuesRepository.findAll(specification, page);
        return result.map(issuesMapper::toDto);
    }
    
    @Transactional(readOnly = true)
    public Map<String, Object> findIssuesAttach(Long id) {
    	Issues issues = issuesRepository.findOne(id);
    	IssuesDTO issuesDTO = issuesMapper.toDto(issues);
        List<Attachments> attachmentListDomain = attachmentsRepository.findAllAttachmentsByIssuesId(id);
        List<AttachmentsDTO> attachmentDtos = new ArrayList<>();
		if (!CollectionUtils.isEmpty(attachmentListDomain)) {
			AttachmentsDTO attachmentsDto;
			for (Attachments attachment : attachmentListDomain) {
				attachmentsDto = new AttachmentsDTO();
				attachmentsDto = attachmentsMapper.toDto(attachment);
				attachmentsDto.setValue(this.storageService.loadAttachment(attachment.getDiskFile()));
				attachmentDtos.add(attachmentsDto);
			}
		}
        Map<String, Object> object = new HashMap<String, Object>();
        object.put("id", issues.getId());
        object.put("name", issues.getName());
        object.put("description", issues.getDescription());
        object.put("status", issues.getStatus());
        object.put("projectsName", issuesDTO.getProjectsName());
        object.put("projectsId", issuesDTO.getProjectsId());
        object.put("attachments", attachmentDtos);
        return object;
    }
    
    @Transactional(readOnly = true)
    public Map<String, Object> trackingIssuesTM_Campaign(String money) {
    	Map<String, Object> object = new HashMap<String, Object>();
    	int countIssues = issuesRepository.countIssueAccepted();
    	Long resultMoney = Long.parseLong(money) * countIssues;
    	object.put("countIssues", countIssues);
    	object.put("money", resultMoney);
    	return object;
    }
    
    @Transactional(readOnly = true)
    public Page<IssuesDTO> getAllIssuesByCampaign(Pageable page) {
        Page<Issues> result = issuesRepository.getAllIssuesByCampaign(page);
        return result.map(issuesMapper::toDto);
    }

    /**
     * Function to convert IssuesCriteria to a {@link Specifications}
     */
    private Specifications<Issues> createSpecification(IssuesCriteria criteria) {
        Specifications<Issues> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Issues_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Issues_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Issues_.description));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), Issues_.status));
            }
            if (criteria.getPurchaseOrderId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getPurchaseOrderId(), Issues_.purchaseOrder, PurchaseOrders_.id));
            }
            if (criteria.getProjectsId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getProjectsId(), Issues_.projects, Projects_.id));
            }
        }
        return specification;
    }

    @Transactional(readOnly = true)
	public Page<IssuesDTO> findIssueByProjectIdAndUser(Long id, String userLogin, Pageable pageable) {
		return this.issuesRepository.findIssueByProjectIdAndUser(id, userLogin, pageable).map(issuesMapper::toDto);
	}
}
