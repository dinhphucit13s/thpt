package fpt.dps.dtms.web.rest;

import fpt.dps.dtms.DtmsApp;

import fpt.dps.dtms.domain.Projects;
import fpt.dps.dtms.domain.ProjectTemplates;
import fpt.dps.dtms.domain.ProjectUsers;
import fpt.dps.dtms.domain.PurchaseOrders;
import fpt.dps.dtms.domain.ProjectUsers;
import fpt.dps.dtms.domain.Customer;
import fpt.dps.dtms.domain.ProjectBugListDefault;
import fpt.dps.dtms.repository.ProjectsRepository;
import fpt.dps.dtms.service.ProjectsService;
import fpt.dps.dtms.repository.search.ProjectsSearchRepository;
import fpt.dps.dtms.service.dto.ProjectsDTO;
import fpt.dps.dtms.service.mapper.ProjectsMapper;
import fpt.dps.dtms.web.rest.errors.ExceptionTranslator;
import fpt.dps.dtms.service.dto.ProjectsCriteria;
import fpt.dps.dtms.service.ProjectsQueryService;

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
import org.springframework.util.Base64Utils;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static fpt.dps.dtms.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fpt.dps.dtms.domain.enumeration.ProjectType;
import fpt.dps.dtms.domain.enumeration.ProjectStatus;
/**
 * Test class for the ProjectsResource REST controller.
 *
 * @see ProjectsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DtmsApp.class)
public class ProjectsResourceIntTest {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final ProjectType DEFAULT_TYPE = ProjectType.NA;
    private static final ProjectType UPDATED_TYPE = ProjectType.MAINTENANCE;

    private static final ProjectStatus DEFAULT_STATUS = ProjectStatus.OPEN;
    private static final ProjectStatus UPDATED_STATUS = ProjectStatus.RUNNING;

    private static final Instant DEFAULT_START_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private ProjectsRepository projectsRepository;

    @Autowired
    private ProjectsMapper projectsMapper;

    @Autowired
    private ProjectsService projectsService;

    @Autowired
    private ProjectsSearchRepository projectsSearchRepository;

    @Autowired
    private ProjectsQueryService projectsQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restProjectsMockMvc;

    private Projects projects;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ProjectsResource projectsResource = new ProjectsResource(projectsService, projectsQueryService, null);
        this.restProjectsMockMvc = MockMvcBuilders.standaloneSetup(projectsResource)
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
    public static Projects createEntity(EntityManager em) {
        Projects projects = new Projects()
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME)
            .type(DEFAULT_TYPE)
            .status(DEFAULT_STATUS)
            .startTime(DEFAULT_START_TIME)
            .endTime(DEFAULT_END_TIME)
            .description(DEFAULT_DESCRIPTION);
        return projects;
    }

    @Before
    public void initTest() {
        projectsSearchRepository.deleteAll();
        projects = createEntity(em);
    }

    @Test
    @Transactional
    public void createProjects() throws Exception {
        int databaseSizeBeforeCreate = projectsRepository.findAll().size();

        // Create the Projects
        ProjectsDTO projectsDTO = projectsMapper.toDto(projects);
        restProjectsMockMvc.perform(post("/api/projects")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(projectsDTO)))
            .andExpect(status().isCreated());

        // Validate the Projects in the database
        List<Projects> projectsList = projectsRepository.findAll();
        assertThat(projectsList).hasSize(databaseSizeBeforeCreate + 1);
        Projects testProjects = projectsList.get(projectsList.size() - 1);
        assertThat(testProjects.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testProjects.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testProjects.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testProjects.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testProjects.getStartTime()).isEqualTo(DEFAULT_START_TIME);
        assertThat(testProjects.getEndTime()).isEqualTo(DEFAULT_END_TIME);
        assertThat(testProjects.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the Projects in Elasticsearch
        Projects projectsEs = projectsSearchRepository.findOne(testProjects.getId());
        assertThat(projectsEs).isEqualToIgnoringGivenFields(testProjects);
    }

    @Test
    @Transactional
    public void createProjectsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = projectsRepository.findAll().size();

        // Create the Projects with an existing ID
        projects.setId(1L);
        ProjectsDTO projectsDTO = projectsMapper.toDto(projects);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProjectsMockMvc.perform(post("/api/projects")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(projectsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Projects in the database
        List<Projects> projectsList = projectsRepository.findAll();
        assertThat(projectsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = projectsRepository.findAll().size();
        // set the field null
        projects.setCode(null);

        // Create the Projects, which fails.
        ProjectsDTO projectsDTO = projectsMapper.toDto(projects);

        restProjectsMockMvc.perform(post("/api/projects")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(projectsDTO)))
            .andExpect(status().isBadRequest());

        List<Projects> projectsList = projectsRepository.findAll();
        assertThat(projectsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = projectsRepository.findAll().size();
        // set the field null
        projects.setName(null);

        // Create the Projects, which fails.
        ProjectsDTO projectsDTO = projectsMapper.toDto(projects);

        restProjectsMockMvc.perform(post("/api/projects")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(projectsDTO)))
            .andExpect(status().isBadRequest());

        List<Projects> projectsList = projectsRepository.findAll();
        assertThat(projectsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = projectsRepository.findAll().size();
        // set the field null
        projects.setStatus(null);

        // Create the Projects, which fails.
        ProjectsDTO projectsDTO = projectsMapper.toDto(projects);

        restProjectsMockMvc.perform(post("/api/projects")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(projectsDTO)))
            .andExpect(status().isBadRequest());

        List<Projects> projectsList = projectsRepository.findAll();
        assertThat(projectsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProjects() throws Exception {
        // Initialize the database
        projectsRepository.saveAndFlush(projects);

        // Get all the projectsList
        restProjectsMockMvc.perform(get("/api/projects?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(projects.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME.toString())))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(DEFAULT_END_TIME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getProjects() throws Exception {
        // Initialize the database
        projectsRepository.saveAndFlush(projects);

        // Get the projects
        restProjectsMockMvc.perform(get("/api/projects/{id}", projects.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(projects.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.startTime").value(DEFAULT_START_TIME.toString()))
            .andExpect(jsonPath("$.endTime").value(DEFAULT_END_TIME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getAllProjectsByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        projectsRepository.saveAndFlush(projects);

        // Get all the projectsList where code equals to DEFAULT_CODE
        defaultProjectsShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the projectsList where code equals to UPDATED_CODE
        defaultProjectsShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllProjectsByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        projectsRepository.saveAndFlush(projects);

        // Get all the projectsList where code in DEFAULT_CODE or UPDATED_CODE
        defaultProjectsShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the projectsList where code equals to UPDATED_CODE
        defaultProjectsShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllProjectsByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        projectsRepository.saveAndFlush(projects);

        // Get all the projectsList where code is not null
        defaultProjectsShouldBeFound("code.specified=true");

        // Get all the projectsList where code is null
        defaultProjectsShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    public void getAllProjectsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        projectsRepository.saveAndFlush(projects);

        // Get all the projectsList where name equals to DEFAULT_NAME
        defaultProjectsShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the projectsList where name equals to UPDATED_NAME
        defaultProjectsShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllProjectsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        projectsRepository.saveAndFlush(projects);

        // Get all the projectsList where name in DEFAULT_NAME or UPDATED_NAME
        defaultProjectsShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the projectsList where name equals to UPDATED_NAME
        defaultProjectsShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllProjectsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        projectsRepository.saveAndFlush(projects);

        // Get all the projectsList where name is not null
        defaultProjectsShouldBeFound("name.specified=true");

        // Get all the projectsList where name is null
        defaultProjectsShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllProjectsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        projectsRepository.saveAndFlush(projects);

        // Get all the projectsList where type equals to DEFAULT_TYPE
        defaultProjectsShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the projectsList where type equals to UPDATED_TYPE
        defaultProjectsShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllProjectsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        projectsRepository.saveAndFlush(projects);

        // Get all the projectsList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultProjectsShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the projectsList where type equals to UPDATED_TYPE
        defaultProjectsShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllProjectsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        projectsRepository.saveAndFlush(projects);

        // Get all the projectsList where type is not null
        defaultProjectsShouldBeFound("type.specified=true");

        // Get all the projectsList where type is null
        defaultProjectsShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    public void getAllProjectsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        projectsRepository.saveAndFlush(projects);

        // Get all the projectsList where status equals to DEFAULT_STATUS
        defaultProjectsShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the projectsList where status equals to UPDATED_STATUS
        defaultProjectsShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllProjectsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        projectsRepository.saveAndFlush(projects);

        // Get all the projectsList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultProjectsShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the projectsList where status equals to UPDATED_STATUS
        defaultProjectsShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllProjectsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        projectsRepository.saveAndFlush(projects);

        // Get all the projectsList where status is not null
        defaultProjectsShouldBeFound("status.specified=true");

        // Get all the projectsList where status is null
        defaultProjectsShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    public void getAllProjectsByStartTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        projectsRepository.saveAndFlush(projects);

        // Get all the projectsList where startTime equals to DEFAULT_START_TIME
        defaultProjectsShouldBeFound("startTime.equals=" + DEFAULT_START_TIME);

        // Get all the projectsList where startTime equals to UPDATED_START_TIME
        defaultProjectsShouldNotBeFound("startTime.equals=" + UPDATED_START_TIME);
    }

    @Test
    @Transactional
    public void getAllProjectsByStartTimeIsInShouldWork() throws Exception {
        // Initialize the database
        projectsRepository.saveAndFlush(projects);

        // Get all the projectsList where startTime in DEFAULT_START_TIME or UPDATED_START_TIME
        defaultProjectsShouldBeFound("startTime.in=" + DEFAULT_START_TIME + "," + UPDATED_START_TIME);

        // Get all the projectsList where startTime equals to UPDATED_START_TIME
        defaultProjectsShouldNotBeFound("startTime.in=" + UPDATED_START_TIME);
    }

    @Test
    @Transactional
    public void getAllProjectsByStartTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        projectsRepository.saveAndFlush(projects);

        // Get all the projectsList where startTime is not null
        defaultProjectsShouldBeFound("startTime.specified=true");

        // Get all the projectsList where startTime is null
        defaultProjectsShouldNotBeFound("startTime.specified=false");
    }

    @Test
    @Transactional
    public void getAllProjectsByEndTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        projectsRepository.saveAndFlush(projects);

        // Get all the projectsList where endTime equals to DEFAULT_END_TIME
        defaultProjectsShouldBeFound("endTime.equals=" + DEFAULT_END_TIME);

        // Get all the projectsList where endTime equals to UPDATED_END_TIME
        defaultProjectsShouldNotBeFound("endTime.equals=" + UPDATED_END_TIME);
    }

    @Test
    @Transactional
    public void getAllProjectsByEndTimeIsInShouldWork() throws Exception {
        // Initialize the database
        projectsRepository.saveAndFlush(projects);

        // Get all the projectsList where endTime in DEFAULT_END_TIME or UPDATED_END_TIME
        defaultProjectsShouldBeFound("endTime.in=" + DEFAULT_END_TIME + "," + UPDATED_END_TIME);

        // Get all the projectsList where endTime equals to UPDATED_END_TIME
        defaultProjectsShouldNotBeFound("endTime.in=" + UPDATED_END_TIME);
    }

    @Test
    @Transactional
    public void getAllProjectsByEndTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        projectsRepository.saveAndFlush(projects);

        // Get all the projectsList where endTime is not null
        defaultProjectsShouldBeFound("endTime.specified=true");

        // Get all the projectsList where endTime is null
        defaultProjectsShouldNotBeFound("endTime.specified=false");
    }

    @Test
    @Transactional
    public void getAllProjectsByProjectTemplatesIsEqualToSomething() throws Exception {
        // Initialize the database
        ProjectTemplates projectTemplates = ProjectTemplatesResourceIntTest.createEntity(em);
        em.persist(projectTemplates);
        em.flush();
        projects.setProjectTemplates(projectTemplates);
        projectsRepository.saveAndFlush(projects);
        Long projectTemplatesId = projectTemplates.getId();

        // Get all the projectsList where projectTemplates equals to projectTemplatesId
        defaultProjectsShouldBeFound("projectTemplatesId.equals=" + projectTemplatesId);

        // Get all the projectsList where projectTemplates equals to projectTemplatesId + 1
        defaultProjectsShouldNotBeFound("projectTemplatesId.equals=" + (projectTemplatesId + 1));
    }


    @Test
    @Transactional
    public void getAllProjectsByProjectLeadIsEqualToSomething() throws Exception {
        // Initialize the database
        ProjectUsers projectLead = ProjectUsersResourceIntTest.createEntity(em);
        em.persist(projectLead);
        em.flush();
        projects.setProjectLead(projectLead);
        projectsRepository.saveAndFlush(projects);
        Long projectLeadId = projectLead.getId();

        // Get all the projectsList where projectLead equals to projectLeadId
        defaultProjectsShouldBeFound("projectLeadId.equals=" + projectLeadId);

        // Get all the projectsList where projectLead equals to projectLeadId + 1
        defaultProjectsShouldNotBeFound("projectLeadId.equals=" + (projectLeadId + 1));
    }


    @Test
    @Transactional
    public void getAllProjectsByPurchaseOrdersIsEqualToSomething() throws Exception {
        // Initialize the database
        PurchaseOrders purchaseOrders = PurchaseOrdersResourceIntTest.createEntity(em);
        em.persist(purchaseOrders);
        em.flush();
        projects.addPurchaseOrders(purchaseOrders);
        projectsRepository.saveAndFlush(projects);
        Long purchaseOrdersId = purchaseOrders.getId();

        // Get all the projectsList where purchaseOrders equals to purchaseOrdersId
        defaultProjectsShouldBeFound("purchaseOrdersId.equals=" + purchaseOrdersId);

        // Get all the projectsList where purchaseOrders equals to purchaseOrdersId + 1
        defaultProjectsShouldNotBeFound("purchaseOrdersId.equals=" + (purchaseOrdersId + 1));
    }


    @Test
    @Transactional
    public void getAllProjectsByProjectUsersIsEqualToSomething() throws Exception {
        // Initialize the database
        ProjectUsers projectUsers = ProjectUsersResourceIntTest.createEntity(em);
        em.persist(projectUsers);
        em.flush();
        projects.addProjectUsers(projectUsers);
        projectsRepository.saveAndFlush(projects);
        Long projectUsersId = projectUsers.getId();

        // Get all the projectsList where projectUsers equals to projectUsersId
        defaultProjectsShouldBeFound("projectUsersId.equals=" + projectUsersId);

        // Get all the projectsList where projectUsers equals to projectUsersId + 1
        defaultProjectsShouldNotBeFound("projectUsersId.equals=" + (projectUsersId + 1));
    }


    @Test
    @Transactional
    public void getAllProjectsByCustomerIsEqualToSomething() throws Exception {
        // Initialize the database
        Customer customer = CustomerResourceIntTest.createEntity(em);
        em.persist(customer);
        em.flush();
        projects.setCustomer(customer);
        projectsRepository.saveAndFlush(projects);
        Long customerId = customer.getId();

        // Get all the projectsList where customer equals to customerId
        defaultProjectsShouldBeFound("customerId.equals=" + customerId);

        // Get all the projectsList where customer equals to customerId + 1
        defaultProjectsShouldNotBeFound("customerId.equals=" + (customerId + 1));
    }


    @Test
    @Transactional
    public void getAllProjectsByProjectBugListDefaultIsEqualToSomething() throws Exception {
        // Initialize the database
        ProjectBugListDefault projectBugListDefault = ProjectBugListDefaultResourceIntTest.createEntity(em);
        em.persist(projectBugListDefault);
        em.flush();
        projects.addProjectBugListDefault(projectBugListDefault);
        projectsRepository.saveAndFlush(projects);
        Long projectBugListDefaultId = projectBugListDefault.getId();

        // Get all the projectsList where projectBugListDefault equals to projectBugListDefaultId
        defaultProjectsShouldBeFound("projectBugListDefaultId.equals=" + projectBugListDefaultId);

        // Get all the projectsList where projectBugListDefault equals to projectBugListDefaultId + 1
        defaultProjectsShouldNotBeFound("projectBugListDefaultId.equals=" + (projectBugListDefaultId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultProjectsShouldBeFound(String filter) throws Exception {
        restProjectsMockMvc.perform(get("/api/projects?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(projects.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME.toString())))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(DEFAULT_END_TIME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultProjectsShouldNotBeFound(String filter) throws Exception {
        restProjectsMockMvc.perform(get("/api/projects?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingProjects() throws Exception {
        // Get the projects
        restProjectsMockMvc.perform(get("/api/projects/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProjects() throws Exception {
        // Initialize the database
        projectsRepository.saveAndFlush(projects);
        projectsSearchRepository.save(projects);
        int databaseSizeBeforeUpdate = projectsRepository.findAll().size();

        // Update the projects
        Projects updatedProjects = projectsRepository.findOne(projects.getId());
        // Disconnect from session so that the updates on updatedProjects are not directly saved in db
        em.detach(updatedProjects);
        updatedProjects
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .type(UPDATED_TYPE)
            .status(UPDATED_STATUS)
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME)
            .description(UPDATED_DESCRIPTION);
        ProjectsDTO projectsDTO = projectsMapper.toDto(updatedProjects);

        restProjectsMockMvc.perform(put("/api/projects")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(projectsDTO)))
            .andExpect(status().isOk());

        // Validate the Projects in the database
        List<Projects> projectsList = projectsRepository.findAll();
        assertThat(projectsList).hasSize(databaseSizeBeforeUpdate);
        Projects testProjects = projectsList.get(projectsList.size() - 1);
        assertThat(testProjects.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testProjects.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProjects.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testProjects.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testProjects.getStartTime()).isEqualTo(UPDATED_START_TIME);
        assertThat(testProjects.getEndTime()).isEqualTo(UPDATED_END_TIME);
        assertThat(testProjects.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the Projects in Elasticsearch
        Projects projectsEs = projectsSearchRepository.findOne(testProjects.getId());
        assertThat(projectsEs).isEqualToIgnoringGivenFields(testProjects);
    }

    @Test
    @Transactional
    public void updateNonExistingProjects() throws Exception {
        int databaseSizeBeforeUpdate = projectsRepository.findAll().size();

        // Create the Projects
        ProjectsDTO projectsDTO = projectsMapper.toDto(projects);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restProjectsMockMvc.perform(put("/api/projects")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(projectsDTO)))
            .andExpect(status().isCreated());

        // Validate the Projects in the database
        List<Projects> projectsList = projectsRepository.findAll();
        assertThat(projectsList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteProjects() throws Exception {
        // Initialize the database
        projectsRepository.saveAndFlush(projects);
        projectsSearchRepository.save(projects);
        int databaseSizeBeforeDelete = projectsRepository.findAll().size();

        // Get the projects
        restProjectsMockMvc.perform(delete("/api/projects/{id}", projects.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean projectsExistsInEs = projectsSearchRepository.exists(projects.getId());
        assertThat(projectsExistsInEs).isFalse();

        // Validate the database is empty
        List<Projects> projectsList = projectsRepository.findAll();
        assertThat(projectsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchProjects() throws Exception {
        // Initialize the database
        projectsRepository.saveAndFlush(projects);
        projectsSearchRepository.save(projects);

        // Search the projects
        restProjectsMockMvc.perform(get("/api/_search/projects?query=id:" + projects.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(projects.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME.toString())))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(DEFAULT_END_TIME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Projects.class);
        Projects projects1 = new Projects();
        projects1.setId(1L);
        Projects projects2 = new Projects();
        projects2.setId(projects1.getId());
        assertThat(projects1).isEqualTo(projects2);
        projects2.setId(2L);
        assertThat(projects1).isNotEqualTo(projects2);
        projects1.setId(null);
        assertThat(projects1).isNotEqualTo(projects2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProjectsDTO.class);
        ProjectsDTO projectsDTO1 = new ProjectsDTO();
        projectsDTO1.setId(1L);
        ProjectsDTO projectsDTO2 = new ProjectsDTO();
        assertThat(projectsDTO1).isNotEqualTo(projectsDTO2);
        projectsDTO2.setId(projectsDTO1.getId());
        assertThat(projectsDTO1).isEqualTo(projectsDTO2);
        projectsDTO2.setId(2L);
        assertThat(projectsDTO1).isNotEqualTo(projectsDTO2);
        projectsDTO1.setId(null);
        assertThat(projectsDTO1).isNotEqualTo(projectsDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(projectsMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(projectsMapper.fromId(null)).isNull();
    }
}
