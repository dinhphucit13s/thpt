package fpt.dps.dtms.service;

import fpt.dps.dtms.domain.Packages;
import fpt.dps.dtms.domain.ProjectUsers;
import fpt.dps.dtms.domain.Projects;
import fpt.dps.dtms.domain.User;
import fpt.dps.dtms.repository.ProjectUsersRepository;
import fpt.dps.dtms.repository.UserRepository;
import fpt.dps.dtms.repository.search.ProjectUsersSearchRepository;
import fpt.dps.dtms.service.dto.ProjectUsersDTO;
import fpt.dps.dtms.service.dto.ProjectWorkflowsCriteria;
import fpt.dps.dtms.service.dto.ProjectWorkflowsDTO;
import fpt.dps.dtms.service.dto.UserDTO;
import fpt.dps.dtms.service.mapper.ProjectUsersMapper;
import fpt.dps.dtms.service.util.AppConstants;
import fpt.dps.dtms.service.util.ExcelGenerator;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Service Implementation for managing ProjectUsers.
 */
@Service
@Transactional
public class ProjectUsersService {

    private final Logger log = LoggerFactory.getLogger(ProjectUsersService.class);

    private final ProjectUsersRepository projectUsersRepository;
    
    private final UserRepository userRepository;

    private final ProjectUsersMapper projectUsersMapper;

    private final ProjectUsersSearchRepository projectUsersSearchRepository;
    
    private final UserService userService;
    
    private final ExcelGenerator excelGenerator;

    public ProjectUsersService(ProjectUsersRepository projectUsersRepository, ProjectUsersMapper projectUsersMapper, UserRepository userRepository,
    		ProjectUsersSearchRepository projectUsersSearchRepository, UserService userService, ExcelGenerator excelGenerator) {
        this.projectUsersRepository = projectUsersRepository;
        this.projectUsersMapper = projectUsersMapper;
        this.projectUsersSearchRepository = projectUsersSearchRepository;
        this.userService = userService;
        this.excelGenerator = excelGenerator;
        this.userRepository = userRepository;
    }

    /**
     * Save a projectUsers.
     *
     * @param projectUsersDTO the entity to save
     * @return the persisted entity
     */
    public ProjectUsersDTO save(ProjectUsersDTO projectUsersDTO) {
        log.debug("Request to save ProjectUsers : {}", projectUsersDTO);
        ProjectUsers projectUsers = projectUsersMapper.toEntity(projectUsersDTO);
        projectUsers = projectUsersRepository.save(projectUsers);
        ProjectUsersDTO result = projectUsersMapper.toDto(projectUsers);
        projectUsersSearchRepository.save(projectUsers);
        
        String roleName = null;
        switch(projectUsersDTO.getRoleName()){
        case PM :
        	roleName = "ROLE_PM";
        	break;
        case TEAMLEAD :
        	roleName = "ROLE_TL";
        	break;
        case REVIEWER :
        	roleName = "ROLE_REVIEW";
        	break;
        case OPERATOR :
        	roleName = "ROLE_OP";
        	break;
        case FI :
        	roleName = "ROLE_FI";
        	break;
        }
        
        Optional<User> user1 = userService.getOneUserByLogin(projectUsersDTO.getUserLogin(),roleName);
        if(!user1.isPresent()){
            /*Optional<UserDTO> opUserDTO = userService.getUserWithAuthoritiesByLogin(projectUsersDTO.getUserLogin()).map(UserDTO::new);
            if(opUserDTO.isPresent()){
            	UserDTO userDTO = opUserDTO.get();
            	userDTO.getAuthorities().add(roleName);
            	userService.updateUser(userDTO);
            }*/
            UserDTO opUserDTO = userService.getUserWithAuthoritiesByLogin(projectUsersDTO.getUserLogin());
            opUserDTO.getAuthorities().add(roleName);
            userService.updateUser(opUserDTO);
        }
        return result;
    }

    /**
     * Get all the projectUsers.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ProjectUsersDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ProjectUsers");
        return projectUsersRepository.findAll(pageable)
            .map(projectUsersMapper::toDto);
    }

    /**
     * Get one projectUsers by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public ProjectUsersDTO findOne(Long id) {
        log.debug("Request to get ProjectUsers : {}", id);
        ProjectUsers projectUsers = projectUsersRepository.findOne(id);
        return projectUsersMapper.toDto(projectUsers);
    }

    /**
     * Delete the projectUsers by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ProjectUsers : {}", id);
        projectUsersRepository.delete(id);
        projectUsersSearchRepository.delete(id);
    }

    /**
     * Search for the projectUsers corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ProjectUsersDTO> search(String[] query, Pageable pageable) {
        log.debug("Request to search for a page of ProjectUsers for query {}", query.toString());
        Page<ProjectUsers> result = projectUsersRepository.findByUserLoginLikeAndProjectId(query[0], Long.valueOf(query[1]),pageable);
        return result.map(projectUsersMapper::toDto);
    }

    /**
	 * Import project user base one user list from excel file
	 * @param file: excel file
     * @return 
	 * @throws IOException
	 */
	public Map<String, List<ProjectUsersDTO>> importProjectUsers(MultipartFile file, Long projectId) throws IOException {
		
		Map<String, List<ProjectUsersDTO>> result = new HashMap<String, List<ProjectUsersDTO>>();
		
		List<ProjectUsersDTO> existingProjectUsers = new ArrayList<ProjectUsersDTO>();
		List<ProjectUsersDTO> notExistingUsers = new ArrayList<ProjectUsersDTO>();
		List<ProjectUsersDTO> successProjectUsers = new ArrayList<ProjectUsersDTO>();
		
		List<ProjectUsersDTO> projectUsersDTOs = this.excelGenerator.getProjectUsersFromFile(file, projectId);
		
		if(projectUsersDTOs.size() > 0) {
			for (ProjectUsersDTO projectUsersDTO : projectUsersDTOs) {
				// check user existing in system
				User user = this.userService.getOneUserByLogin(projectUsersDTO.getUserLogin());
				if	(user != null) {
					// check user existing in project
					ProjectUsers existingProjectUser = projectUsersRepository.findProjectUsers(projectUsersDTO.getUserLogin().toLowerCase(), projectUsersDTO.getProjectId());
			        if (existingProjectUser != null) {
			        	existingProjectUsers.add(projectUsersDTO);
			            continue;
			        }
			        
			        this.save(projectUsersDTO);
			        successProjectUsers.add(projectUsersDTO);
				}
				else {
					notExistingUsers.add(projectUsersDTO);
				}
			}
		}
		
		result.put("success", successProjectUsers);
		result.put("existing", existingProjectUsers);
		result.put("notExisting", notExistingUsers);
		
		return result;
	}

	public InputStreamResource exportTemplate() throws IOException {
		// TODO Auto-generated method stub
		ByteArrayInputStream in = excelGenerator.exportTemplateProjectUserToExcel();
		return new InputStreamResource(in);
	}
	
	/**
	 * Create excel file for member management
	 *
	 * @param packageID
	 * @throws IOException
	 */
	public InputStreamResource exportExcel(Long projectId) throws IOException {
		List<ProjectUsers> listUser = projectUsersRepository.getAllUsersForSelects(projectId);
		if(CollectionUtils.isNotEmpty(listUser)) {
				ByteArrayInputStream in = excelGenerator.exportAllocationToExcel(listUser, projectId);
				return new InputStreamResource(in);
		}else {
			return null;
		}

	}
    
}
