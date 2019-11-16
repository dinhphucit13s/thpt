package fpt.dps.dtms.web.rest;

import fpt.dps.dtms.DtmsApp;

import fpt.dps.dtms.domain.ProjectUsers;
import fpt.dps.dtms.domain.Projects;
import fpt.dps.dtms.repository.ProjectUsersRepository;
import fpt.dps.dtms.service.ProjectUsersService;
import fpt.dps.dtms.service.TasksQueryService;
import fpt.dps.dtms.repository.search.ProjectUsersSearchRepository;
import fpt.dps.dtms.service.dto.ProjectUsersDTO;
import fpt.dps.dtms.service.mapper.ProjectUsersMapper;
import fpt.dps.dtms.web.rest.errors.ExceptionTranslator;
import fpt.dps.dtms.service.dto.ProjectUsersCriteria;
import fpt.dps.dtms.service.ProjectUsersQueryService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static fpt.dps.dtms.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fpt.dps.dtms.domain.enumeration.ProjectRoles;
/**
 * Test class for the ProjectUsersResource REST controller.
 *
 * @see ProjectUsersResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DtmsApp.class)
public class ProjectUsersResourceIntTest {

    private static final String DEFAULT_USER_LOGIN = "AAAAAAAAAA";
    private static final String UPDATED_USER_LOGIN = "BBBBBBBBBB";

    private static final ProjectRoles DEFAULT_ROLE_NAME = ProjectRoles.PM;
    private static final ProjectRoles UPDATED_ROLE_NAME = ProjectRoles.TEAMLEAD;

    @Autowired
    private ProjectUsersRepository projectUsersRepository;

    @Autowired
    private ProjectUsersMapper projectUsersMapper;

    @Autowired
    private ProjectUsersService projectUsersService;

    @Autowired
    private ProjectUsersSearchRepository projectUsersSearchRepository;

    @Autowired
    private ProjectUsersQueryService projectUsersQueryService;
    
    @Autowired
    private TasksQueryService tasksQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restProjectUsersMockMvc;

    private ProjectUsers projectUsers;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ProjectUsersResource projectUsersResource = new ProjectUsersResource(projectUsersService, projectUsersQueryService, tasksQueryService);
        this.restProjectUsersMockMvc = MockMvcBuilders.standaloneSetup(projectUsersResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProjectUsers createEntity(EntityManager em) {
        ProjectUsers projectUsers = new ProjectUsers()
            .userLogin(DEFAULT_USER_LOGIN)
            .roleName(DEFAULT_ROLE_NAME);
        return projectUsers;
    }

    @Before
    public void initTest() {
        projectUsersSearchRepository.deleteAll();
        projectUsers = createEntity(em);
    }

    @Test
    @Transactional
    public void createProjectUsers() throws Exception {
        int databaseSizeBeforeCreate = projectUsersRepository.findAll().size();

        // Create the ProjectUsers
        ProjectUsersDTO projectUsersDTO = projectUsersMapper.toDto(projectUsers);
        restProjectUsersMockMvc.perform(post("/api/project-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(projectUsersDTO)))
            .andExpect(status().isCreated());

        // Validate the ProjectUsers in the database
        List<ProjectUsers> projectUsersList = projectUsersRepository.findAll();
        assertThat(projectUsersList).hasSize(databaseSizeBeforeCreate + 1);
        ProjectUsers testProjectUsers = projectUsersList.get(projectUsersList.size() - 1);
        assertThat(testProjectUsers.getUserLogin()).isEqualTo(DEFAULT_USER_LOGIN);
        assertThat(testProjectUsers.getRoleName()).isEqualTo(DEFAULT_ROLE_NAME);

        // Validate the ProjectUsers in Elasticsearch
        ProjectUsers projectUsersEs = projectUsersSearchRepository.findOne(testProjectUsers.getId());
        assertThat(projectUsersEs).isEqualToIgnoringGivenFields(testProjectUsers);
    }

    @Test
    @Transactional
    public void createProjectUsersWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = projectUsersRepository.findAll().size();

        // Create the ProjectUsers with an existing ID
        projectUsers.setId(1L);
        ProjectUsersDTO projectUsersDTO = projectUsersMapper.toDto(projectUsers);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProjectUsersMockMvc.perform(post("/api/project-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(projectUsersDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ProjectUsers in the database
        List<ProjectUsers> projectUsersList = projectUsersRepository.findAll();
        assertThat(projectUsersList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllProjectUsers() throws Exception {
        // Initialize the database
        projectUsersRepository.saveAndFlush(projectUsers);

        // Get all the projectUsersList
        restProjectUsersMockMvc.perform(get("/api/project-users?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(projectUsers.getId().intValue())))
            .andExpect(jsonPath("$.[*].userLogin").value(hasItem(DEFAULT_USER_LOGIN.toString())))
            .andExpect(jsonPath("$.[*].roleName").value(hasItem(DEFAULT_ROLE_NAME.toString())));
    }

    @Test
    @Transactional
    public void getProjectUsers() throws Exception {
        // Initialize the database
        projectUsersRepository.saveAndFlush(projectUsers);

        // Get the projectUsers
        restProjectUsersMockMvc.perform(get("/api/project-users/{id}", projectUsers.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(projectUsers.getId().intValue()))
            .andExpect(jsonPath("$.userLogin").value(DEFAULT_USER_LOGIN.toString()))
            .andExpect(jsonPath("$.roleName").value(DEFAULT_ROLE_NAME.toString()));
    }

    @Test
    @Transactional
    public void getAllProjectUsersByUserLoginIsEqualToSomething() throws Exception {
        // Initialize the database
        projectUsersRepository.saveAndFlush(projectUsers);

        // Get all the projectUsersList where userLogin equals to DEFAULT_USER_LOGIN
        defaultProjectUsersShouldBeFound("userLogin.equals=" + DEFAULT_USER_LOGIN);

        // Get all the projectUsersList where userLogin equals to UPDATED_USER_LOGIN
        defaultProjectUsersShouldNotBeFound("userLogin.equals=" + UPDATED_USER_LOGIN);
    }

    @Test
    @Transactional
    public void getAllProjectUsersByUserLoginIsInShouldWork() throws Exception {
        // Initialize the database
        projectUsersRepository.saveAndFlush(projectUsers);

        // Get all the projectUsersList where userLogin in DEFAULT_USER_LOGIN or UPDATED_USER_LOGIN
        defaultProjectUsersShouldBeFound("userLogin.in=" + DEFAULT_USER_LOGIN + "," + UPDATED_USER_LOGIN);

        // Get all the projectUsersList where userLogin equals to UPDATED_USER_LOGIN
        defaultProjectUsersShouldNotBeFound("userLogin.in=" + UPDATED_USER_LOGIN);
    }

    @Test
    @Transactional
    public void getAllProjectUsersByUserLoginIsNullOrNotNull() throws Exception {
        // Initialize the database
        projectUsersRepository.saveAndFlush(projectUsers);

        // Get all the projectUsersList where userLogin is not null
        defaultProjectUsersShouldBeFound("userLogin.specified=true");

        // Get all the projectUsersList where userLogin is null
        defaultProjectUsersShouldNotBeFound("userLogin.specified=false");
    }

    @Test
    @Transactional
    public void getAllProjectUsersByRoleNameIsEqualToSomething() throws Exception {
        // Initialize the database
        projectUsersRepository.saveAndFlush(projectUsers);

        // Get all the projectUsersList where roleName equals to DEFAULT_ROLE_NAME
        defaultProjectUsersShouldBeFound("roleName.equals=" + DEFAULT_ROLE_NAME);

        // Get all the projectUsersList where roleName equals to UPDATED_ROLE_NAME
        defaultProjectUsersShouldNotBeFound("roleName.equals=" + UPDATED_ROLE_NAME);
    }

    @Test
    @Transactional
    public void getAllProjectUsersByRoleNameIsInShouldWork() throws Exception {
        // Initialize the database
        projectUsersRepository.saveAndFlush(projectUsers);

        // Get all the projectUsersList where roleName in DEFAULT_ROLE_NAME or UPDATED_ROLE_NAME
        defaultProjectUsersShouldBeFound("roleName.in=" + DEFAULT_ROLE_NAME + "," + UPDATED_ROLE_NAME);

        // Get all the projectUsersList where roleName equals to UPDATED_ROLE_NAME
        defaultProjectUsersShouldNotBeFound("roleName.in=" + UPDATED_ROLE_NAME);
    }

    @Test
    @Transactional
    public void getAllProjectUsersByRoleNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        projectUsersRepository.saveAndFlush(projectUsers);

        // Get all the projectUsersList where roleName is not null
        defaultProjectUsersShouldBeFound("roleName.specified=true");

        // Get all the projectUsersList where roleName is null
        defaultProjectUsersShouldNotBeFound("roleName.specified=false");
    }

    @Test
    @Transactional
    public void getAllProjectUsersByProjectIsEqualToSomething() throws Exception {
        // Initialize the database
        Projects project = ProjectsResourceIntTest.createEntity(em);
        em.persist(project);
        em.flush();
        projectUsers.setProject(project);
        projectUsersRepository.saveAndFlush(projectUsers);
        Long projectId = project.getId();

        // Get all the projectUsersList where project equals to projectId
        defaultProjectUsersShouldBeFound("projectId.equals=" + projectId);

        // Get all the projectUsersList where project equals to projectId + 1
        defaultProjectUsersShouldNotBeFound("projectId.equals=" + (projectId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultProjectUsersShouldBeFound(String filter) throws Exception {
        restProjectUsersMockMvc.perform(get("/api/project-users?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(projectUsers.getId().intValue())))
            .andExpect(jsonPath("$.[*].userLogin").value(hasItem(DEFAULT_USER_LOGIN.toString())))
            .andExpect(jsonPath("$.[*].roleName").value(hasItem(DEFAULT_ROLE_NAME.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultProjectUsersShouldNotBeFound(String filter) throws Exception {
        restProjectUsersMockMvc.perform(get("/api/project-users?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingProjectUsers() throws Exception {
        // Get the projectUsers
        restProjectUsersMockMvc.perform(get("/api/project-users/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProjectUsers() throws Exception {
        // Initialize the database
        projectUsersRepository.saveAndFlush(projectUsers);
        projectUsersSearchRepository.save(projectUsers);
        int databaseSizeBeforeUpdate = projectUsersRepository.findAll().size();

        // Update the projectUsers
        ProjectUsers updatedProjectUsers = projectUsersRepository.findOne(projectUsers.getId());
        // Disconnect from session so that the updates on updatedProjectUsers are not directly saved in db
        em.detach(updatedProjectUsers);
        updatedProjectUsers
            .userLogin(UPDATED_USER_LOGIN)
            .roleName(UPDATED_ROLE_NAME);
        ProjectUsersDTO projectUsersDTO = projectUsersMapper.toDto(updatedProjectUsers);

        restProjectUsersMockMvc.perform(put("/api/project-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(projectUsersDTO)))
            .andExpect(status().isOk());

        // Validate the ProjectUsers in the database
        List<ProjectUsers> projectUsersList = projectUsersRepository.findAll();
        assertThat(projectUsersList).hasSize(databaseSizeBeforeUpdate);
        ProjectUsers testProjectUsers = projectUsersList.get(projectUsersList.size() - 1);
        assertThat(testProjectUsers.getUserLogin()).isEqualTo(UPDATED_USER_LOGIN);
        assertThat(testProjectUsers.getRoleName()).isEqualTo(UPDATED_ROLE_NAME);

        // Validate the ProjectUsers in Elasticsearch
        ProjectUsers projectUsersEs = projectUsersSearchRepository.findOne(testProjectUsers.getId());
        assertThat(projectUsersEs).isEqualToIgnoringGivenFields(testProjectUsers);
    }

    @Test
    @Transactional
    public void updateNonExistingProjectUsers() throws Exception {
        int databaseSizeBeforeUpdate = projectUsersRepository.findAll().size();

        // Create the ProjectUsers
        ProjectUsersDTO projectUsersDTO = projectUsersMapper.toDto(projectUsers);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restProjectUsersMockMvc.perform(put("/api/project-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(projectUsersDTO)))
            .andExpect(status().isCreated());

        // Validate the ProjectUsers in the database
        List<ProjectUsers> projectUsersList = projectUsersRepository.findAll();
        assertThat(projectUsersList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteProjectUsers() throws Exception {
        // Initialize the database
        projectUsersRepository.saveAndFlush(projectUsers);
        projectUsersSearchRepository.save(projectUsers);
        int databaseSizeBeforeDelete = projectUsersRepository.findAll().size();

        // Get the projectUsers
        restProjectUsersMockMvc.perform(delete("/api/project-users/{id}", projectUsers.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean projectUsersExistsInEs = projectUsersSearchRepository.exists(projectUsers.getId());
        assertThat(projectUsersExistsInEs).isFalse();

        // Validate the database is empty
        List<ProjectUsers> projectUsersList = projectUsersRepository.findAll();
        assertThat(projectUsersList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchProjectUsers() throws Exception {
        // Initialize the database
        projectUsersRepository.saveAndFlush(projectUsers);
        projectUsersSearchRepository.save(projectUsers);

        // Search the projectUsers
        restProjectUsersMockMvc.perform(get("/api/_search/project-users?query=id:" + projectUsers.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(projectUsers.getId().intValue())))
            .andExpect(jsonPath("$.[*].userLogin").value(hasItem(DEFAULT_USER_LOGIN.toString())))
            .andExpect(jsonPath("$.[*].roleName").value(hasItem(DEFAULT_ROLE_NAME.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProjectUsers.class);
        ProjectUsers projectUsers1 = new ProjectUsers();
        projectUsers1.setId(1L);
        ProjectUsers projectUsers2 = new ProjectUsers();
        projectUsers2.setId(projectUsers1.getId());
        assertThat(projectUsers1).isEqualTo(projectUsers2);
        projectUsers2.setId(2L);
        assertThat(projectUsers1).isNotEqualTo(projectUsers2);
        projectUsers1.setId(null);
        assertThat(projectUsers1).isNotEqualTo(projectUsers2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProjectUsersDTO.class);
        ProjectUsersDTO projectUsersDTO1 = new ProjectUsersDTO();
        projectUsersDTO1.setId(1L);
        ProjectUsersDTO projectUsersDTO2 = new ProjectUsersDTO();
        assertThat(projectUsersDTO1).isNotEqualTo(projectUsersDTO2);
        projectUsersDTO2.setId(projectUsersDTO1.getId());
        assertThat(projectUsersDTO1).isEqualTo(projectUsersDTO2);
        projectUsersDTO2.setId(2L);
        assertThat(projectUsersDTO1).isNotEqualTo(projectUsersDTO2);
        projectUsersDTO1.setId(null);
        assertThat(projectUsersDTO1).isNotEqualTo(projectUsersDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(projectUsersMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(projectUsersMapper.fromId(null)).isNull();
    }
}
