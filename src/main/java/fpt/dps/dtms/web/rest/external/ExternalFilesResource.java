package fpt.dps.dtms.web.rest.external;

import java.nio.file.FileAlreadyExistsException;
import java.util.HashMap;

import javax.validation.Valid;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hazelcast.spi.impl.operationservice.impl.responses.Response;

import fpt.dps.dtms.service.AttachmentsService;
import fpt.dps.dtms.service.StorageService;
import fpt.dps.dtms.service.dto.AttachmentsDTO;

/**
 * REST controller for managing UserProfile.
 */
@RestController
@RequestMapping("/api/external")
public class ExternalFilesResource {
	private static final String ENTITY_NAME = "file";
	
	private final Logger log = LoggerFactory.getLogger(ExternalFilesResource.class);

    private final StorageService storageService;
    
    private final AttachmentsService attachmentsService;

    public ExternalFilesResource(StorageService storageService, AttachmentsService attachmentsService) {
        this.storageService = storageService;
        this.attachmentsService = attachmentsService;
    }
    
    @GetMapping("/file/download")
	@ResponseBody
	public ResponseEntity<Resource> download(@RequestParam Long id) {
    	log.debug("request load file: "+ id);
    	AttachmentsDTO attach = this.attachmentsService.findOne(id);
		Resource fileResource = storageService.downFile(attach.getDiskFile());
		
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, fileResource.getFilename())
				.contentType(MediaType.parseMediaType(attach.getFileType()))
				.body(fileResource);
    }
}
