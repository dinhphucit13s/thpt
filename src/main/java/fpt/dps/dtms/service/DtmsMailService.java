package fpt.dps.dtms.service;

import fpt.dps.dtms.domain.Attachments;
import fpt.dps.dtms.domain.Issues;
import fpt.dps.dtms.domain.Mail;
import fpt.dps.dtms.domain.MailReceiver;
import fpt.dps.dtms.repository.AttachmentsRepository;
import fpt.dps.dtms.repository.MailReceiverRepository;
import fpt.dps.dtms.repository.MailRepository;
import fpt.dps.dtms.repository.search.AttachmentsSearchRepository;
import fpt.dps.dtms.repository.search.MailSearchRepository;
import fpt.dps.dtms.service.dto.AttachmentsDTO;
import fpt.dps.dtms.service.dto.MailDTO;
import fpt.dps.dtms.service.dto.MailReceiverDTO;
import fpt.dps.dtms.service.mapper.AttachmentsMapper;
import fpt.dps.dtms.service.mapper.MailMapper;
import fpt.dps.dtms.service.mapper.MailReceiverMapper;
import fpt.dps.dtms.web.rest.TasksResource;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.elasticsearch.index.query.QueryBuilders.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Service Implementation for managing Mail.
 */
@Service
@Transactional
public class DtmsMailService {

    private final Logger log = LoggerFactory.getLogger(MailService.class);

    private final MailRepository mailRepository;
    
    private final MailReceiverRepository mailReceiverRepository;

    private final MailMapper mailMapper;

    private final MailSearchRepository mailSearchRepository;
    
    private final StorageService storageService;
    
    private final AttachmentsRepository attachmentsRepository;
    
    private final AttachmentsSearchRepository attachmentsSearchRepository;
    
    private final MailReceiverService mailReceiverService;
    
    private final AttachmentsMapper attachmentsMapper;

    public DtmsMailService(MailRepository mailRepository, MailMapper mailMapper, MailSearchRepository mailSearchRepository, MailReceiverService mailReceiverService, 
    		MailReceiverRepository mailReceiverRepository, AttachmentsRepository attachmentsRepository, AttachmentsSearchRepository attachmentsSearchRepository,
    		StorageService storageService, AttachmentsMapper attachmentsMapper) {
        this.mailRepository = mailRepository;
        this.mailMapper = mailMapper;
        this.mailSearchRepository = mailSearchRepository;
        this.mailReceiverService = mailReceiverService;
        this.mailReceiverRepository  = mailReceiverRepository;
        this.attachmentsRepository = attachmentsRepository;
        this.attachmentsSearchRepository = attachmentsSearchRepository;
        this.storageService =storageService;
        this.attachmentsMapper = attachmentsMapper;
    }

    /**
     * Save a mail.
     *
     * @param mailDTO the entity to save
     * @return the persisted entity
     */
    public MailDTO save(MailDTO mailDTO, String userLogin, List<MultipartFile> files) {
        log.debug("Request to save Mail : {}", mailDTO);
		Mail mail = new Mail();
		mail.setFrom(mailDTO.getFrom());
		mail.setSubject(mailDTO.getSubject());
		mail.setStartTime(mailDTO.getStartTime());
		mail.setEndTime(mailDTO.getEndTime());
		
        String[] receivedList = mail.getFrom().split(";");
        String body = String.format("{\"type\": \"mail\", \"content\": \"%s\"}", mailDTO.getBody());
        mail.setBody(body);
        mail = mailRepository.save(mail);
        Set<Attachments> attachments = new HashSet<>();
        if(files.size() > 0) {
	        Attachments attachment;
	        String[] folders = {"mail", mail.getId().toString()};
	        for (MultipartFile file : files) {
	        	attachment = new Attachments();
	        	String diskFile = storageService.store(file, folders);
	        	attachment.setMail(mail);
	        	attachment.setDiskFile(diskFile);
	        	attachment.setFilename(file.getOriginalFilename());
	        	attachment.setFileType(file.getContentType());
	        	attachmentsRepository.save(attachment);
	        	attachmentsSearchRepository.save(attachment);
	        	attachments.add(attachment);
			}
        }
        mail.setAttachments(attachments);
        mail = mailRepository.save(mail);
        MailDTO result = mailMapper.toDto(mail);
        
        mailSearchRepository.save(mail);
        
        if (mail != null) {
        	saveMailFrom(mail);
        	saveMailTo(mail, receivedList);
        }
        return result;
    }

	private void saveMailTo(Mail mail, String[] receivedList) {
		for (String receiver: receivedList) {
			MailReceiverDTO mailReceiverDTO = new MailReceiverDTO();
			ObjectMapper objMap = new ObjectMapper();
			mailReceiverDTO.setMailId(mail.getId());
			mailReceiverDTO.setStatus(false);
			mailReceiverDTO.setTo(receiver);
			mailReceiverDTO = this.mailReceiverService.save(mailReceiverDTO);
			Map<String, Object> object = new HashMap<String, Object>();
			object.put("id", mail.getId());
			object.put("subject", mail.getSubject());
			object.put("body", mail.getBody());
			object.put("startTime", mail.getStartTime());
			object.put("endTime", mail.getEndTime());
			object.put("from", mail.getCreatedBy());
			object.put("to", receiver);
			object.put("status", false);
			object.put("createdDate", mail.getCreatedDate());
			// Transfer data to web socket
			String jsonNotif = null;
			try {
				jsonNotif = objMap.writeValueAsString(object);
				log.info("Test Mail", jsonNotif);
				if (jsonNotif != null) {
					CommunicationService.mailToReceiver(receiver, jsonNotif);
				}
				//CommunicationService.mailToReceiver(receiver, jsonNotif);
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				jsonNotif = StringUtils.EMPTY;
			}
		}
	}
	
    private void saveMailFrom(Mail mail) {
    	MailReceiverDTO mailReceiverDTO = new MailReceiverDTO();
		mailReceiverDTO.setMailId(mail.getId());
		mailReceiverDTO.setFrom(mail.getCreatedBy());
		mailReceiverDTO.setStatus(false);
		mailReceiverDTO = this.mailReceiverService.save(mailReceiverDTO);
    }
    
    private Attachments processAttachments(AttachmentsDTO attachmentsDTO, Mail mail) throws IOException {
    	Attachments attachment = new Attachments();
    	byte[] imageByte= Base64.decodeBase64(attachmentsDTO.getValue());
    	if (imageByte != null) {
    		Date now = new Date();
    	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
    	    	
    	    String[] folders = {"mail", mail.getId().toString()};
    	    Path folderPath = storageService.init(folders);
    	    Path path = folderPath.resolve(attachmentsDTO.getFilename());
    	    Files.write(path, imageByte);
    	    Resource resource = new UrlResource(path.toUri());
    	    String physicalPath = null;
    	    if(resource.exists() || resource.isReadable()) {
                physicalPath = path.toFile().getPath();
                if(physicalPath.contains("\\")) {
                	physicalPath = physicalPath.replaceFirst("filestorage\\\\", "");
                }
                else if(physicalPath.contains("/")) {
                	physicalPath = physicalPath.replaceFirst("filestorage/", "");
                }
            }

    	    attachment.setFilename(dateFormat.format(now) + "_" + attachmentsDTO.getFilename());
    	    attachment.setFileType(attachmentsDTO.getFileType());
    	    attachment.setDiskFile(physicalPath);
    	    attachment.setMail(mail);
    	}
	    return attachment;
    }

    /**
     * Get all the mail.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<MailDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Mail");
        return mailRepository.findAll(pageable)
            .map(mailMapper::toDto);
    }

    /**
     * Get one mail by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Map<String, Object> findOne(Long id, String userLogin) {
    	
        log.debug("Request to get Mail : {}", id);
        Mail mail = mailRepository.findOne(id);
        MailReceiver mailReceiver = mailReceiverRepository.getMailToByMailId(userLogin, id);
        List<Attachments> attachmentListDomain = this.attachmentsRepository.findByParentId(id, "MAIL");
        List<AttachmentsDTO> attachmentDtos = new ArrayList<>();
		if (!CollectionUtils.isEmpty(attachmentListDomain)) {
			AttachmentsDTO attachmentsDto;
			for (Attachments attachment : attachmentListDomain) {
				attachmentsDto = new AttachmentsDTO();
				attachmentsDto = attachmentsMapper.toDto(attachment);
				attachmentDtos.add(attachmentsDto);
			}
		}
        Map<String, Object> object = new HashMap<String, Object>();
        object.put("id", mail.getId());
        object.put("to", mail.getFrom());
        object.put("from", mail.getCreatedBy());
        object.put("date", mail.getCreatedDate());
        object.put("subject", mail.getSubject());
        object.put("body", mail.getBody());
        object.put("startTime", mail.getStartTime());
        object.put("endTime", mail.getEndTime());
        if (mailReceiver != null) {
        	object.put("status", mailReceiver.isStatus());
        }
        object.put("attachments", attachmentDtos);
        
        return object;
    }

    /**
     * Delete the mail by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Mail : {}", id);
        mailRepository.delete(id);
        mailSearchRepository.delete(id);
    }

    /**
     * Search for the mail corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<MailDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Mail for query {}", query);
        Page<Mail> result = mailSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(mailMapper::toDto);
    }
}
