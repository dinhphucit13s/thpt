package fpt.dps.dtms.service;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.zip.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import fpt.dps.dtms.service.dto.AttachmentsDTO;

@Service
@Transactional
public class StorageService {
	Logger log = LoggerFactory.getLogger(this.getClass().getName());
	private final Path rootLocation = Paths.get("filestorage");
	
	public String store(MultipartFile file, String... folders) {
		log.debug("Start: Create file :"+ file.getOriginalFilename());
		try {
			String fileURL = null;
			String physicalPath = null;
			Path folderPath = this.init(folders);
            byte[] bytes = file.getBytes();
            Path path = folderPath.resolve(file.getOriginalFilename());
            Files.write(path, bytes);
            Resource resource = new UrlResource(path.toUri());
            if(resource.exists() || resource.isReadable()) {
            	physicalPath = path.toFile().getPath();
            	if(physicalPath.contains("\\")) {
            		physicalPath = physicalPath.replaceFirst("filestorage\\\\", "");
            	}
            	else if(physicalPath.contains("/")) {
            		physicalPath = physicalPath.replaceFirst("filestorage/", "");
            	}
            	fileURL = URLEncoder.encode(physicalPath);
            }
	        log.debug("End: Create file :"+ file.getOriginalFilename());
	        log.debug("End: Path file :"+ fileURL);
	        return physicalPath;
		} catch (Exception e) {
			throw new RuntimeException("FAIL!");
		}
	}

	public boolean deleteFile(String fullFileName) {
		boolean retval = false;
		try {
			String pathDecode = URLDecoder.decode(fullFileName, "UTF-8");
			Path fileToDeletePath = rootLocation.resolve(pathDecode);
			log.debug(fileToDeletePath.toUri().getPath());
			Files.delete(fileToDeletePath);
			retval = true;
		} catch (Exception e) {
			throw new RuntimeException("Could not delete package: " + e.getMessage());
		}
		return retval;
	}

	public String loadFile(String filename, String... folders) {
		try {
			StringBuilder strFolders = new StringBuilder();
			if (folders.length > 0) {
				for (String folder : folders) {
					strFolders.append(folder);
					strFolders.append("/");
				}
			}
			strFolders.append(filename);
			log.debug("strFolders: " + strFolders.toString());
			Path file = rootLocation.resolve(filename);
			log.debug(file.toUri().getPath());
			byte[] fileContent = FileUtils.readFileToByteArray(new File(file.toUri().getPath()));
			return Base64.getEncoder().encodeToString(fileContent);
			/*Resource resource = new UrlResource(file.toUri());
			log.debug(resource.exists() + ":" + resource.isReadable() + ":" + resource.getFilename());
			if (resource.exists() || resource.isReadable()) {
				return resource;
			} else {
				throw new RuntimeException("FAIL!");
			}*/
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	
	public Resource downFile(String diskFile) {
		try {
			Path file = rootLocation.resolve(diskFile);
			log.debug(file.toUri().getPath());
			Resource resource = new UrlResource(file.toUri());
			log.debug(resource.exists() + ":" + resource.isReadable() + ":" + resource.getFilename());
			if (resource.exists() || resource.isReadable()) {
				return resource;
			} else {
				throw new RuntimeException("FAIL!");
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	
	public String loadAttachment(String folders) {
		try {
			Path file = rootLocation.resolve(folders);
			log.debug(file.toUri().getPath());
			byte[] fileContent = FileUtils.readFileToByteArray(new File(file.toUri().getPath()));
			return Base64.getEncoder().encodeToString(fileContent);
			/*Resource resource = new UrlResource(file.toUri());
			log.debug(resource.exists() + ":" + resource.isReadable() + ":" + resource.getFilename());
			if (resource.exists() || resource.isReadable()) {
				return resource;
			} else {
				throw new RuntimeException("FAIL!");
			}*/
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	public void deleteAll(Path folders) {
		FileSystemUtils.deleteRecursively(folders.toFile());
	}

	/*
	 * Init storage folder before creating a file.
	 */
	Path init(String... folders) {
		log.debug("Initing folder before creating files.");
		Path folderPath = null;
		try {
			StringBuilder strFolders = new StringBuilder();
			if (folders.length > 0) {
				for (String folder : folders) {
					strFolders.append(folder);
					strFolders.append("/");
					folderPath = this.rootLocation.resolve(strFolders.toString());
					if (!Files.exists(folderPath)) {
						Files.createDirectories(folderPath);
					}
				}

			}
			return folderPath;
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Could not initialize storage: ");
		}
	}
}