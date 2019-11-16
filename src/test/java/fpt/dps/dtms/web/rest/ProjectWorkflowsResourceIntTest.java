package fpt.dps.dtms.web.rest;

import fpt.dps.dtms.DtmsApp;

import fpt.dps.dtms.domain.ProjectWorkflows;
import fpt.dps.dtms.domain.ProjectTemplates;
import fpt.dps.dtms.repository.ProjectWorkflowsRepository;
import fpt.dps.dtms.service.ProjectWorkflowsService;
import fpt.dps.dtms.repository.search.ProjectWorkflowsSearchRepository;
import fpt.dps.dtms.service.dto.ProjectWorkflowsDTO;
import fpt.dps.dtms.service.mapper.ProjectWorkflowsMapper;
import fpt.dps.dtms.web.rest.errors.ExceptionTranslator;
import fpt.dps.dtms.service.dto.ProjectWorkflowsCriteria;
import fpt.dps.dtms.service.ProjectWorkflowsQueryService;

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
import java.util.List;

import static fpt.dps.dtms.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ProjectWorkflowsResource REST controller.
 *
 * @see ProjectWorkflowsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DtmsApp.class)
public class ProjectWorkflowsResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_STEP = 1;
    private static final Integer UPDATED_STEP = 2;

    private static final String DEFAULT_INPUT_DTO = "AAAAAAAAAA";
    private static final String UPDATED_INPUT_DTO = "BBBBBBBBBB";

    private static final String DEFAULT_OP_GRID_DTO = "AAAAAAAAAA";
    private static final String UPDATED_OP_GRID_DTO = "BBBBBBBBBB";

    private static final String DEFAULT_PM_GRID_DTO = "AAAAAAAAAA";
    private static final String UPDATED_PM_GRID_DTO = "BBBBBBBBBB";

    private static final String DEFAULT_NEXT_URI = "AAAAAAAAAA";
    private static final String UPDATED_NEXT_URI = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_ACTIVITY = "AAAAAAAAAA";
    private static final String UPDATED_ACTIVITY = "BBBBBBBBBB";

    @Autowired
    private ProjectWorkflowsRepository projectWorkflowsRepository;

    @Autowired
    private ProjectWorkflowsMapper projectWorkflowsMapper;

    @Autowired
    private ProjectWorkflowsService projectWorkflowsService;

    @Autowired
    private ProjectWorkflowsSearchRepository projectWorkflowsSearchRepository;

    @Autowired
    private ProjectWorkflowsQueryService projectWorkflowsQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restProjectWorkflowsMockMvc;

    private ProjectWorkflows projectWorkflows;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ProjectWorkflowsResource projectWorkflowsResource = new ProjectWorkflowsResource(projectWorkflowsService, projectWorkflowsQueryService);
        this.restProjectWorkflowsMockMvc = MockMvcBuilders.standaloneSetup(projectWorkflowsResource)
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
    public static ProjectWorkflows createEntity(EntityManager em) {
        ProjectWorkflows projectWorkflows = new ProjectWorkflows()
            .name(DEFAULT_NAME)
            .step(DEFAULT_STEP)
            .inputDTO(DEFAULT_INPUT_DTO)
            .opGridDTO(DEFAULT_OP_GRID_DTO)
            .pmGridDTO(DEFAULT_PM_GRID_DTO)
            .nextURI(DEFAULT_NEXT_URI)
            .description(DEFAULT_DESCRIPTION)
            .activity(DEFAULT_ACTIVITY);
        return projectWorkflows;
    }

    @Before
    public void initTest() {
        projectWorkflowsSearchRepository.deleteAll();
        projectWorkflows = createEntity(em);
    }

    @Test
    @Transactional
    public void createProjectWorkflows() throws Exception {
        int databaseSizeBeforeCreate = projectWorkflowsRepository.findAll().size();

        // Create the ProjectWorkflows
        ProjectWorkflowsDTO projectWorkflowsDTO = projectWorkflowsMapper.toDto(projectWorkflows);
        restProjectWorkflowsMockMvc.perform(post("/api/project-workflows")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(projectWorkflowsDTO)))
            .andExpect(status().isCreated());

        // Validate the ProjectWorkflows in the database
        List<ProjectWorkflows> projectWorkflowsList = projectWorkflowsRepository.findAll();
        assertThat(projectWorkflowsList).hasSize(databaseSizeBeforeCreate + 1);
        ProjectWorkflows testProjectWorkflows = projectWorkflowsList.get(projectWorkflowsList.size() - 1);
        assertThat(testProjectWorkflows.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testProjectWorkflows.getStep()).isEqualTo(DEFAULT_STEP);
        assertThat(testProjectWorkflows.getInputDTO()).isEqualTo(DEFAULT_INPUT_DTO);
        assertThat(testProjectWorkflows.getOpGridDTO()).isEqualTo(DEFAULT_OP_GRID_DTO);
        assertThat(testProjectWorkflows.getPmGridDTO()).isEqualTo(DEFAULT_PM_GRID_DTO);
        assertThat(testProjectWorkflows.getNextURI()).isEqualTo(DEFAULT_NEXT_URI);
        assertThat(testProjectWorkflows.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testProjectWorkflows.getActivity()).isEqualTo(DEFAULT_ACTIVITY);

        // Validate the ProjectWorkflows in Elasticsearch
        ProjectWorkflows projectWorkflowsEs = projectWorkflowsSearchRepository.findOne(testProjectWorkflows.getId());
        assertThat(projectWorkflowsEs).isEqualToIgnoringGivenFields(testProjectWorkflows);
    }

    @Test
    @Transactional
    public void createProjectWorkflowsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = projectWorkflowsRepository.findAll().size();

        // Create the ProjectWorkflows with an existing ID
        projectWorkflows.setId(1L);
        ProjectWorkflowsDTO projectWorkflowsDTO = projectWorkflowsMapper.toDto(projectWorkflows);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProjectWorkflowsMockMvc.perform(post("/api/project-workflows")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(projectWorkflowsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ProjectWorkflows in the database
        List<ProjectWorkflows> projectWorkflowsList = projectWorkflowsRepository.findAll();
        assertThat(projectWorkflowsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = projectWorkflowsRepository.findAll().size();
        // set the field null
        projectWorkflows.setName(null);

        // Create the ProjectWorkflows, which fails.
        ProjectWorkflowsDTO projectWorkflowsDTO = projectWorkflowsMapper.toDto(projectWorkflows);

        restProjectWorkflowsMockMvc.perform(post("/api/project-workflows")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(projectWorkflowsDTO)))
            .andExpect(status().isBadRequest());

        List<ProjectWorkflows> projectWorkflowsList = projectWorkflowsRepository.findAll();
        assertThat(projectWorkflowsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStepIsRequired() throws Exception {
        int databaseSizeBeforeTest = projectWorkflowsRepository.findAll().size();
        // set the field null
        projectWorkflows.setStep(null);

        // Create the ProjectWorkflows, which fails.
        ProjectWorkflowsDTO projectWorkflowsDTO = projectWorkflowsMapper.toDto(projectWorkflows);

        restProjectWorkflowsMockMvc.perform(post("/api/project-workflows")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(projectWorkflowsDTO)))
            .andExpect(status().isBadRequest());

        List<ProjectWorkflows> projectWorkflowsList = projectWorkflowsRepository.findAll();
        assertThat(projectWorkflowsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkInputDTOIsRequired() throws Exception {
        int databaseSizeBeforeTest = projectWorkflowsRepository.findAll().size();
        // set the field null
        projectWorkflows.setInputDTO(null);

        // Create the ProjectWorkflows, which fails.
        ProjectWorkflowsDTO projectWorkflowsDTO = projectWorkflowsMapper.toDto(projectWorkflows);

        restProjectWorkflowsMockMvc.perform(post("/api/project-workflows")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(projectWorkflowsDTO)))
            .andExpect(status().isBadRequest());

        List<ProjectWorkflows> projectWorkflowsList = projectWorkflowsRepository.findAll();
        assertThat(projectWorkflowsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkOpGridDTOIsRequired() throws Exception {
        int databaseSizeBeforeTest = projectWorkflowsRepository.findAll().size();
        // set the field null
        projectWorkflows.setOpGridDTO(null);

        // Create the ProjectWorkflows, which fails.
        ProjectWorkflowsDTO projectWorkflowsDTO = projectWorkflowsMapper.toDto(projectWorkflows);

        restProjectWorkflowsMockMvc.perform(post("/api/project-workflows")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(projectWorkflowsDTO)))
            .andExpect(status().isBadRequest());

        List<ProjectWorkflows> projectWorkflowsList = projectWorkflowsRepository.findAll();
        assertThat(projectWorkflowsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPmGridDTOIsRequired() throws Exception {
        int databaseSizeBeforeTest = projectWorkflowsRepository.findAll().size();
        // set the field null
        projectWorkflows.setPmGridDTO(null);

        // Create the ProjectWorkflows, which fails.
        ProjectWorkflowsDTO projectWorkflowsDTO = projectWorkflowsMapper.toDto(projectWorkflows);

        restProjectWorkflowsMockMvc.perform(post("/api/project-workflows")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(projectWorkflowsDTO)))
            .andExpect(status().isBadRequest());

        List<ProjectWorkflows> projectWorkflowsList = projectWorkflowsRepository.findAll();
        assertThat(projectWorkflowsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProjectWorkflows() throws Exception {
        // Initialize the database
        projectWorkflowsRepository.saveAndFlush(projectWorkflows);

        // Get all the projectWorkflowsList
        restProjectWorkflowsMockMvc.perform(get("/api/project-workflows?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(projectWorkflows.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].step").value(hasItem(DEFAULT_STEP)))
            .andExpect(jsonPath("$.[*].inputDTO").value(hasItem(DEFAULT_INPUT_DTO.toString())))
            .andExpect(jsonPath("$.[*].opGridDTO").value(hasItem(DEFAULT_OP_GRID_DTO.toString())))
            .andExpect(jsonPath("$.[*].pmGridDTO").value(hasItem(DEFAULT_PM_GRID_DTO.toString())))
            .andExpect(jsonPath("$.[*].nextURI").value(hasItem(DEFAULT_NEXT_URI.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].activity").value(hasItem(DEFAULT_ACTIVITY.toString())));
    }

    @Test
    @Transactional
    public void getProjectWorkflows() throws Exception {
        // Initialize the database
        projectWorkflowsRepository.saveAndFlush(projectWorkflows);

        // Get the projectWorkflows
        restProjectWorkflowsMockMvc.perform(get("/api/project-workflows/{id}", projectWorkflows.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(projectWorkflows.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.step").value(DEFAULT_STEP))
            .andExpect(jsonPath("$.inputDTO").value(DEFAULT_INPUT_DTO.toString()))
            .andExpect(jsonPath("$.opGridDTO").value(DEFAULT_OP_GRID_DTO.toString()))
            .andExpect(jsonPath("$.pmGridDTO").value(DEFAULT_PM_GRID_DTO.toString()))
            .andExpect(jsonPath("$.nextURI").value(DEFAULT_NEXT_URI.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.activity").value(DEFAULT_ACTIVITY.toString()));
    }

    @Test
    @Transactional
    public void getAllProjectWorkflowsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        projectWorkflowsRepository.saveAndFlush(projectWorkflows);

        // Get all the projectWorkflowsList where name equals to DEFAULT_NAME
        defaultProjectWorkflowsShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the projectWorkflowsList where name equals to UPDATED_NAME
        defaultProjectWorkflowsShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllProjectWorkflowsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        projectWorkflowsRepository.saveAndFlush(projectWorkflows);

        // Get all the projectWorkflowsList where name in DEFAULT_NAME or UPDATED_NAME
        defaultProjectWorkflowsShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the projectWorkflowsList where name equals to UPDATED_NAME
        defaultProjectWorkflowsShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllProjectWorkflowsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        projectWorkflowsRepository.saveAndFlush(projectWorkflows);

        // Get all the projectWorkflowsList where name is not null
        defaultProjectWorkflowsShouldBeFound("name.specified=true");

        // Get all the projectWorkflowsList where name is null
        defaultProjectWorkflowsShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllProjectWorkflowsByStepIsEqualToSomething() throws Exception {
        // Initialize the database
        projectWorkflowsRepository.saveAndFlush(projectWorkflows);

        // Get all the projectWorkflowsList where step equals to DEFAULT_STEP
        defaultProjectWorkflowsShouldBeFound("step.equals=" + DEFAULT_STEP);

        // Get all the projectWorkflowsList where step equals to UPDATED_STEP
        defaultProjectWorkflowsShouldNotBeFound("step.equals=" + UPDATED_STEP);
    }

    @Test
    @Transactional
    public void getAllProjectWorkflowsByStepIsInShouldWork() throws Exception {
        // Initialize the database
        projectWorkflowsRepository.saveAndFlush(projectWorkflows);

        // Get all the projectWorkflowsList where step in DEFAULT_STEP or UPDATED_STEP
        defaultProjectWorkflowsShouldBeFound("step.in=" + DEFAULT_STEP + "," + UPDATED_STEP);

        // Get all the projectWorkflowsList where step equals to UPDATED_STEP
        defaultProjectWorkflowsShouldNotBeFound("step.in=" + UPDATED_STEP);
    }

    @Test
    @Transactional
    public void getAllProjectWorkflowsByStepIsNullOrNotNull() throws Exception {
        // Initialize the database
        projectWorkflowsRepository.saveAndFlush(projectWorkflows);

        // Get all the projectWorkflowsList where step is not null
        defaultProjectWorkflowsShouldBeFound("step.specified=true");

        // Get all the projectWorkflowsList where step is null
        defaultProjectWorkflowsShouldNotBeFound("step.specified=false");
    }

    @Test
    @Transactional
    public void getAllProjectWorkflowsByStepIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        projectWorkflowsRepository.saveAndFlush(projectWorkflows);

        // Get all the projectWorkflowsList where step greater than or equals to DEFAULT_STEP
        defaultProjectWorkflowsShouldBeFound("step.greaterOrEqualThan=" + DEFAULT_STEP);

        // Get all the projectWorkflowsList where step greater than or equals to UPDATED_STEP
        defaultProjectWorkflowsShouldNotBeFound("step.greaterOrEqualThan=" + UPDATED_STEP);
    }

    @Test
    @Transactional
    public void getAllProjectWorkflowsByStepIsLessThanSomething() throws Exception {
        // Initialize the database
        projectWorkflowsRepository.saveAndFlush(projectWorkflows);

        // Get all the projectWorkflowsList where step less than or equals to DEFAULT_STEP
        defaultProjectWorkflowsShouldNotBeFound("step.lessThan=" + DEFAULT_STEP);

        // Get all the projectWorkflowsList where step less than or equals to UPDATED_STEP
        defaultProjectWorkflowsShouldBeFound("step.lessThan=" + UPDATED_STEP);
    }


    @Test
    @Transactional
    public void getAllProjectWorkflowsByNextURIIsEqualToSomething() throws Exception {
        // Initialize the database
        projectWorkflowsRepository.saveAndFlush(projectWorkflows);

        // Get all the projectWorkflowsList where nextURI equals to DEFAULT_NEXT_URI
        defaultProjectWorkflowsShouldBeFound("nextURI.equals=" + DEFAULT_NEXT_URI);

        // Get all the projectWorkflowsList where nextURI equals to UPDATED_NEXT_URI
        defaultProjectWorkflowsShouldNotBeFound("nextURI.equals=" + UPDATED_NEXT_URI);
    }

    @Test
    @Transactional
    public void getAllProjectWorkflowsByNextURIIsInShouldWork() throws Exception {
        // Initialize the database
        projectWorkflowsRepository.saveAndFlush(projectWorkflows);

        // Get all the projectWorkflowsList where nextURI in DEFAULT_NEXT_URI or UPDATED_NEXT_URI
        defaultProjectWorkflowsShouldBeFound("nextURI.in=" + DEFAULT_NEXT_URI + "," + UPDATED_NEXT_URI);

        // Get all the projectWorkflowsList where nextURI equals to UPDATED_NEXT_URI
        defaultProjectWorkflowsShouldNotBeFound("nextURI.in=" + UPDATED_NEXT_URI);
    }

    @Test
    @Transactional
    public void getAllProjectWorkflowsByNextURIIsNullOrNotNull() throws Exception {
        // Initialize the database
        projectWorkflowsRepository.saveAndFlush(projectWorkflows);

        // Get all the projectWorkflowsList where nextURI is not null
        defaultProjectWorkflowsShouldBeFound("nextURI.specified=true");

        // Get all the projectWorkflowsList where nextURI is null
        defaultProjectWorkflowsShouldNotBeFound("nextURI.specified=false");
    }

    @Test
    @Transactional
    public void getAllProjectWorkflowsByActivityIsEqualToSomething() throws Exception {
        // Initialize the database
        projectWorkflowsRepository.saveAndFlush(projectWorkflows);

        // Get all the projectWorkflowsList where activity equals to DEFAULT_ACTIVITY
        defaultProjectWorkflowsShouldBeFound("activity.equals=" + DEFAULT_ACTIVITY);

        // Get all the projectWorkflowsList where activity equals to UPDATED_ACTIVITY
        defaultProjectWorkflowsShouldNotBeFound("activity.equals=" + UPDATED_ACTIVITY);
    }

    @Test
    @Transactional
    public void getAllProjectWorkflowsByActivityIsInShouldWork() throws Exception {
        // Initialize the database
        projectWorkflowsRepository.saveAndFlush(projectWorkflows);

        // Get all the projectWorkflowsList where activity in DEFAULT_ACTIVITY or UPDATED_ACTIVITY
        defaultProjectWorkflowsShouldBeFound("activity.in=" + DEFAULT_ACTIVITY + "," + UPDATED_ACTIVITY);

        // Get all the projectWorkflowsList where activity equals to UPDATED_ACTIVITY
        defaultProjectWorkflowsShouldNotBeFound("activity.in=" + UPDATED_ACTIVITY);
    }

    @Test
    @Transactional
    public void getAllProjectWorkflowsByActivityIsNullOrNotNull() throws Exception {
        // Initialize the database
        projectWorkflowsRepository.saveAndFlush(projectWorkflows);

        // Get all the projectWorkflowsList where activity is not null
        defaultProjectWorkflowsShouldBeFound("activity.specified=true");

        // Get all the projectWorkflowsList where activity is null
        defaultProjectWorkflowsShouldNotBeFound("activity.specified=false");
    }

    @Test
    @Transactional
    public void getAllProjectWorkflowsByProjectTemplatesIsEqualToSomething() throws Exception {
        // Initialize the database
        ProjectTemplates projectTemplates = ProjectTemplatesResourceIntTest.createEntity(em);
        em.persist(projectTemplates);
        em.flush();
        projectWorkflows.setProjectTemplates(projectTemplates);
        projectWorkflowsRepository.saveAndFlush(projectWorkflows);
        Long projectTemplatesId = projectTemplates.getId();

        // Get all the projectWorkflowsList where projectTemplates equals to projectTemplatesId
        defaultProjectWorkflowsShouldBeFound("projectTemplatesId.equals=" + projectTemplatesId);

        // Get all the projectWorkflowsList where projectTemplates equals to projectTemplatesId + 1
        defaultProjectWorkflowsShouldNotBeFound("projectTemplatesId.equals=" + (projectTemplatesId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultProjectWorkflowsShouldBeFound(String filter) throws Exception {
        restProjectWorkflowsMockMvc.perform(get("/api/project-workflows?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(projectWorkflows.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].step").value(hasItem(DEFAULT_STEP)))
            .andExpect(jsonPath("$.[*].inputDTO").value(hasItem(DEFAULT_INPUT_DTO.toString())))
            .andExpect(jsonPath("$.[*].opGridDTO").value(hasItem(DEFAULT_OP_GRID_DTO.toString())))
            .andExpect(jsonPath("$.[*].pmGridDTO").value(hasItem(DEFAULT_PM_GRID_DTO.toString())))
            .andExpect(jsonPath("$.[*].nextURI").value(hasItem(DEFAULT_NEXT_URI.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].activity").value(hasItem(DEFAULT_ACTIVITY.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultProjectWorkflowsShouldNotBeFound(String filter) throws Exception {
        restProjectWorkflowsMockMvc.perform(get("/api/project-workflows?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingProjectWorkflows() throws Exception {
        // Get the projectWorkflows
        restProjectWorkflowsMockMvc.perform(get("/api/project-workflows/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProjectWorkflows() throws Exception {
        // Initialize the database
        projectWorkflowsRepository.saveAndFlush(projectWorkflows);
        projectWorkflowsSearchRepository.save(projectWorkflows);
        int databaseSizeBeforeUpdate = projectWorkflowsRepository.findAll().size();

        // Update the projectWorkflows
        ProjectWorkflows updatedProjectWorkflows = projectWorkflowsRepository.findOne(projectWorkflows.getId());
        // Disconnect from session so that the updates on updatedProjectWorkflows are not directly saved in db
        em.detach(updatedProjectWorkflows);
        updatedProjectWorkflows
            .name(UPDATED_NAME)
            .step(UPDATED_STEP)
            .inputDTO(UPDATED_INPUT_DTO)
            .opGridDTO(UPDATED_OP_GRID_DTO)
            .pmGridDTO(UPDATED_PM_GRID_DTO)
            .nextURI(UPDATED_NEXT_URI)
            .description(UPDATED_DESCRIPTION)
            .activity(UPDATED_ACTIVITY);
        ProjectWorkflowsDTO projectWorkflowsDTO = projectWorkflowsMapper.toDto(updatedProjectWorkflows);

        restProjectWorkflowsMockMvc.perform(put("/api/project-workflows")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(projectWorkflowsDTO)))
            .andExpect(status().isOk());

        // Validate the ProjectWorkflows in the database
        List<ProjectWorkflows> projectWorkflowsList = projectWorkflowsRepository.findAll();
        assertThat(projectWorkflowsList).hasSize(databaseSizeBeforeUpdate);
        ProjectWorkflows testProjectWorkflows = projectWorkflowsList.get(projectWorkflowsList.size() - 1);
        assertThat(testProjectWorkflows.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProjectWorkflows.getStep()).isEqualTo(UPDATED_STEP);
        assertThat(testProjectWorkflows.getInputDTO()).isEqualTo(UPDATED_INPUT_DTO);
        assertThat(testProjectWorkflows.getOpGridDTO()).isEqualTo(UPDATED_OP_GRID_DTO);
        assertThat(testProjectWorkflows.getPmGridDTO()).isEqualTo(UPDATED_PM_GRID_DTO);
        assertThat(testProjectWorkflows.getNextURI()).isEqualTo(UPDATED_NEXT_URI);
        assertThat(testProjectWorkflows.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testProjectWorkflows.getActivity()).isEqualTo(UPDATED_ACTIVITY);

        // Validate the ProjectWorkflows in Elasticsearch
        ProjectWorkflows projectWorkflowsEs = projectWorkflowsSearchRepository.findOne(testProjectWorkflows.getId());
        assertThat(projectWorkflowsEs).isEqualToIgnoringGivenFields(testProjectWorkflows);
    }

    @Test
    @Transactional
    public void updateNonExistingProjectWorkflows() throws Exception {
        int databaseSizeBeforeUpdate = projectWorkflowsRepository.findAll().size();

        // Create the ProjectWorkflows
        ProjectWorkflowsDTO projectWorkflowsDTO = projectWorkflowsMapper.toDto(projectWorkflows);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restProjectWorkflowsMockMvc.perform(put("/api/project-workflows")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(projectWorkflowsDTO)))
            .andExpect(status().isCreated());

        // Validate the ProjectWorkflows in the database
        List<ProjectWorkflows> projectWorkflowsList = projectWorkflowsRepository.findAll();
        assertThat(projectWorkflowsList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteProjectWorkflows() throws Exception {
        // Initialize the database
        projectWorkflowsRepository.saveAndFlush(projectWorkflows);
        projectWorkflowsSearchRepository.save(projectWorkflows);
        int databaseSizeBeforeDelete = projectWorkflowsRepository.findAll().size();

        // Get the projectWorkflows
        restProjectWorkflowsMockMvc.perform(delete("/api/project-workflows/{id}", projectWorkflows.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean projectWorkflowsExistsInEs = projectWorkflowsSearchRepository.exists(projectWorkflows.getId());
        assertThat(projectWorkflowsExistsInEs).isFalse();

        // Validate the database is empty
        List<ProjectWorkflows> projectWorkflowsList = projectWorkflowsRepository.findAll();
        assertThat(projectWorkflowsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchProjectWorkflows() throws Exception {
        // Initialize the database
        projectWorkflowsRepository.saveAndFlush(projectWorkflows);
        projectWorkflowsSearchRepository.save(projectWorkflows);

        // Search the projectWorkflows
        restProjectWorkflowsMockMvc.perform(get("/api/_search/project-workflows?query=id:" + projectWorkflows.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(projectWorkflows.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].step").value(hasItem(DEFAULT_STEP)))
            .andExpect(jsonPath("$.[*].inputDTO").value(hasItem(DEFAULT_INPUT_DTO.toString())))
            .andExpect(jsonPath("$.[*].opGridDTO").value(hasItem(DEFAULT_OP_GRID_DTO.toString())))
            .andExpect(jsonPath("$.[*].pmGridDTO").value(hasItem(DEFAULT_PM_GRID_DTO.toString())))
            .andExpect(jsonPath("$.[*].nextURI").value(hasItem(DEFAULT_NEXT_URI.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].activity").value(hasItem(DEFAULT_ACTIVITY.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProjectWorkflows.class);
        ProjectWorkflows projectWorkflows1 = new ProjectWorkflows();
        projectWorkflows1.setId(1L);
        ProjectWorkflows projectWorkflows2 = new ProjectWorkflows();
        projectWorkflows2.setId(projectWorkflows1.getId());
        assertThat(projectWorkflows1).isEqualTo(projectWorkflows2);
        projectWorkflows2.setId(2L);
        assertThat(projectWorkflows1).isNotEqualTo(projectWorkflows2);
        projectWorkflows1.setId(null);
        assertThat(projectWorkflows1).isNotEqualTo(projectWorkflows2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProjectWorkflowsDTO.class);
        ProjectWorkflowsDTO projectWorkflowsDTO1 = new ProjectWorkflowsDTO();
        projectWorkflowsDTO1.setId(1L);
        ProjectWorkflowsDTO projectWorkflowsDTO2 = new ProjectWorkflowsDTO();
        assertThat(projectWorkflowsDTO1).isNotEqualTo(projectWorkflowsDTO2);
        projectWorkflowsDTO2.setId(projectWorkflowsDTO1.getId());
        assertThat(projectWorkflowsDTO1).isEqualTo(projectWorkflowsDTO2);
        projectWorkflowsDTO2.setId(2L);
        assertThat(projectWorkflowsDTO1).isNotEqualTo(projectWorkflowsDTO2);
        projectWorkflowsDTO1.setId(null);
        assertThat(projectWorkflowsDTO1).isNotEqualTo(projectWorkflowsDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(projectWorkflowsMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(projectWorkflowsMapper.fromId(null)).isNull();
    }
}
