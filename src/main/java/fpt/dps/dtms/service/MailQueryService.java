package fpt.dps.dtms.service;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
import fpt.dps.dtms.domain.enumeration.TaskStatus;
import fpt.dps.dtms.repository.AttachmentsRepository;
import fpt.dps.dtms.repository.MailReceiverRepository;
import fpt.dps.dtms.repository.MailRepository;
import fpt.dps.dtms.repository.search.MailSearchRepository;
import fpt.dps.dtms.service.dto.AttachmentsDTO;
import fpt.dps.dtms.service.dto.MailCriteria;

import fpt.dps.dtms.service.dto.MailDTO;
import fpt.dps.dtms.service.dto.ProjectUsersDTO;
import fpt.dps.dtms.service.mapper.AttachmentsMapper;
import fpt.dps.dtms.service.mapper.MailMapper;

/**
 * Service for executing complex queries for Mail entities in the database.
 * The main input is a {@link MailCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link MailDTO} or a {@link Page} of {@link MailDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MailQueryService extends QueryService<Mail> {

    private final Logger log = LoggerFactory.getLogger(MailQueryService.class);


    private final MailRepository mailRepository;
    
    private final MailReceiverRepository mailReceiverRepository;
    
    private final AttachmentsRepository attachmentsRepository;
    
    private final StorageService storageService;
    
    private final MailMapper mailMapper;
    
    private final AttachmentsMapper attachmentsMapper;

    private final MailSearchRepository mailSearchRepository;

    public MailQueryService(MailRepository mailRepository, MailMapper mailMapper, MailSearchRepository mailSearchRepository, MailReceiverRepository mailReceiverRepository,
    		AttachmentsRepository attachmentsRepository, AttachmentsMapper attachmentsMapper, StorageService storageService) {
        this.mailRepository = mailRepository;
        this.mailMapper = mailMapper;
        this.mailSearchRepository = mailSearchRepository;
        this.mailReceiverRepository = mailReceiverRepository;
        this.attachmentsRepository = attachmentsRepository;
        this.attachmentsMapper = attachmentsMapper;
        this.storageService = storageService;
    }

    /**
     * Return a {@link List} of {@link MailDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<MailDTO> findByCriteria(MailCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Mail> specification = createSpecification(criteria);
        return mailMapper.toDto(mailRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link MailDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MailDTO> findByCriteria(MailCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Mail> specification = createSpecification(criteria);
        final Page<Mail> result = mailRepository.findAll(specification, page);
        return result.map(mailMapper::toDto);
    }
    
    @Transactional(readOnly = true)
    public Page<MailDTO> getAllMailSend(String userLogin, Pageable page) {
    	Page<Mail> pageMail = mailRepository.getAllMailSendByUserLogin(userLogin, page);
    	return pageMail.map(mailMapper::toDto);
    }
    
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getAllMailByUserLogin(String userLogin, Pageable page) {
        log.debug("find by userLogin : {}, page: {}", userLogin, page);
        Page<Mail> pageMail = mailRepository.getAllMailByUserLogin(userLogin, page);
        List<Mail> listMail = pageMail.getContent();
        List<Map<String, Object>> result = listMail.stream().map(mail ->{
        	MailReceiver mailReceiver = mailReceiverRepository.getMailToByMailId(userLogin, mail.getId());
        	Map<String, Object> object = new HashMap<String, Object>();
        	object.put("id", mail.getId());
        	object.put("subject", mail.getSubject());
        	object.put("body", mail.getBody());
        	object.put("startTime", mail.getStartTime());
        	object.put("endTime", mail.getEndTime());
        	object.put("from", mail.getCreatedBy());
        	object.put("to", mail.getFrom());
        	object.put("status", mailReceiver.isStatus());
        	object.put("createdDate", mailReceiver.getCreatedDate());
        	return object;
        }).collect(Collectors.toList());
        Map<String, Object> totalPages = new HashMap<String, Object>();
        totalPages.put("total", pageMail.getTotalElements());
        result.add(totalPages);
        return result;
    }
    
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getAllMailUnreadByUserLogin(String userLogin, Pageable page) {
        log.debug("find by userLogin : {}, page: {}", userLogin, page);
        Page<Mail> pageMail = mailRepository.getAllMailUnreadByUserLogin(userLogin, page);
        List<Mail> listMail = pageMail.getContent();
        List<Map<String, Object>> result = listMail.stream().map(mail ->{
        	MailReceiver mailReceiver = mailReceiverRepository.getMailToByMailId(userLogin, mail.getId());
        	Map<String, Object> object = new HashMap<String, Object>();
        	object.put("id", mail.getId());
        	object.put("subject", mail.getSubject());
        	object.put("body", mail.getBody());
        	object.put("startTime", mail.getStartTime());
        	object.put("endTime", mail.getEndTime());
        	object.put("from", mail.getCreatedBy());
        	object.put("to", mail.getFrom());
        	object.put("status", mailReceiver.isStatus());
        	object.put("createdDate", mailReceiver.getCreatedDate());
        	return object;
        }).collect(Collectors.toList());
        Map<String, Object> totalPages = new HashMap<String, Object>();
        totalPages.put("total", pageMail.getTotalElements());
        result.add(totalPages);
        return result;
    }
    
    @Transactional(readOnly = true)
	public Integer countAllMailInboxByUserLogin(String userLogin) {
		return this.mailRepository.countAllMailInboxByUserLogin(userLogin);
	}
    
    @Transactional(readOnly = true)
    public Integer countAllMailUnseenByUserLogin(String userLogin) {
		// TODO Auto-generated method stub
		return this.mailRepository.countAllMailUnseenByUserLogin(userLogin);
	}
    
    @Transactional(readOnly = true)
    public Map<String, Object> getMail(String userLogin, Long id) {
    	Mail mail = mailRepository.findOne(id);
    	MailReceiver mailReceiver = mailReceiverRepository.getMailToByMailId(userLogin, id);
    	List<Attachments> attachmentListDomain = this.attachmentsRepository.findByParentId(id, "MAIL");
        List<AttachmentsDTO> attachmentDtos = new ArrayList<>();
		if (!CollectionUtils.isEmpty(attachmentListDomain)) {
			AttachmentsDTO attachmentsDto;
			for (Attachments attachment : attachmentListDomain) {
				attachmentsDto = new AttachmentsDTO();
				attachmentsDto = attachmentsMapper.toDto(attachment);
				//attachmentsDto.setValue(this.storageService.loadAttachment(attachment.getDiskFile()));
				attachmentDtos.add(attachmentsDto);
			}
		}
    	Map<String, Object> object = new HashMap<String, Object>();
    	object.put("id", mail.getId());
    	object.put("subject", mail.getSubject());
    	object.put("body", mail.getBody());
    	object.put("startTime", mail.getStartTime());
    	object.put("endTime", mail.getEndTime());
    	object.put("from", mail.getCreatedBy());
    	object.put("to", mail.getFrom());
    	object.put("status", mailReceiver.isStatus());
    	object.put("createdDate", mailReceiver.getCreatedDate());
    	object.put("attachments", attachmentDtos);
    	return object;
    }
    
    @Transactional(readOnly = true)
    public List<Map<String, Object>> searchMailByTitleAndSender(String userLogin, String query, Pageable page) {
        Page<Mail> pageMail = mailRepository.searchMailByTitleAndSender(query, page);
        List<Mail> listMail = pageMail.getContent();
        List<Map<String, Object>> result = listMail.stream().map(mail ->{
        	MailReceiver mailReceiver = mailReceiverRepository.getMailToByMailId(userLogin, mail.getId());
        	Map<String, Object> object = new HashMap<String, Object>();
        	object.put("id", mail.getId());
        	object.put("subject", mail.getSubject());
        	object.put("body", mail.getBody());
        	object.put("startTime", mail.getStartTime());
        	object.put("endTime", mail.getEndTime());
        	object.put("from", mail.getCreatedBy());
        	object.put("to", mail.getFrom());
        	object.put("status", mailReceiver.isStatus());
        	object.put("createdDate", mailReceiver.getCreatedDate());
        	return object;
        }).collect(Collectors.toList());
        Map<String, Object> totalPages = new HashMap<String, Object>();
        totalPages.put("total", pageMail.getTotalElements());
        result.add(totalPages);
        return result;
    }

    /**
     * Function to convert MailCriteria to a {@link Specifications}
     */
    private Specifications<Mail> createSpecification(MailCriteria criteria) {
        Specifications<Mail> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Mail_.id));
            }
            if (criteria.getFrom() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFrom(), Mail_.from));
            }
            if (criteria.getSubject() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSubject(), Mail_.subject));
            }
            if (criteria.getBody() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBody(), Mail_.body));
            }
            if (criteria.getStartTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStartTime(), Mail_.startTime));
            }
            if (criteria.getEndTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEndTime(), Mail_.endTime));
            }
        }
        return specification;
    }

	

}
