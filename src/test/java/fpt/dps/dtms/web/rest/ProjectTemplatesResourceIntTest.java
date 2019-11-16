package fpt.dps.dtms.web.rest;

import fpt.dps.dtms.DtmsApp;

import fpt.dps.dtms.domain.ProjectTemplates;
import fpt.dps.dtms.domain.BusinessLine;
import fpt.dps.dtms.domain.Projects;
import fpt.dps.dtms.repository.ProjectTemplatesRepository;
import fpt.dps.dtms.service.ProjectTemplatesService;
import fpt.dps.dtms.repository.search.ProjectTemplatesSearchRepository;
import fpt.dps.dtms.service.dto.ProjectTemplatesDTO;
import fpt.dps.dtms.service.mapper.ProjectTemplatesMapper;
import fpt.dps.dtms.web.rest.errors.ExceptionTranslator;
import fpt.dps.dtms.service.dto.ProjectTemplatesCriteria;
import fpt.dps.dtms.service.ProjectTemplatesQueryService;

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
 * Test class for the ProjectTemplatesResource REST controller.
 *
 * @see ProjectTemplatesResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DtmsApp.class)
public class ProjectTemplatesResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private ProjectTemplatesRepository projectTemplatesRepository;

    @Autowired
    private ProjectTemplatesMapper projectTemplatesMapper;

    @Autowired
    private ProjectTemplatesService projectTemplatesService;

    @Autowired
    private ProjectTemplatesSearchRepository projectTemplatesSearchRepository;

    @Autowired
    private ProjectTemplatesQueryService projectTemplatesQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restProjectTemplatesMockMvc;

    private ProjectTemplates projectTemplates;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ProjectTemplatesResource projectTemplatesResource = new ProjectTemplatesResource(projectTemplatesService, projectTemplatesQueryService);
        this.restProjectTemplatesMockMvc = MockMvcBuilders.standaloneSetup(projectTemplatesResource)
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
    public static ProjectTemplates createEntity(EntityManager em) {
        ProjectTemplates projectTemplates = new ProjectTemplates()
            .name(DEFAULT_NAME)
            .image(DEFAULT_IMAGE)
            .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE)
            .description(DEFAULT_DESCRIPTION);
        return projectTemplates;
    }

    @Before
    public void initTest() {
        projectTemplatesSearchRepository.deleteAll();
        projectTemplates = createEntity(em);
    }

    @Test
    @Transactional
    public void createProjectTemplates() throws Exception {
        int databaseSizeBeforeCreate = projectTemplatesRepository.findAll().size();

        // Create the ProjectTemplates
        ProjectTemplatesDTO projectTemplatesDTO = projectTemplatesMapper.toDto(projectTemplates);
        restProjectTemplatesMockMvc.perform(post("/api/project-templates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(projectTemplatesDTO)))
            .andExpect(status().isCreated());

        // Validate the ProjectTemplates in the database
        List<ProjectTemplates> projectTemplatesList = projectTemplatesRepository.findAll();
        assertThat(projectTemplatesList).hasSize(databaseSizeBeforeCreate + 1);
        ProjectTemplates testProjectTemplates = projectTemplatesList.get(projectTemplatesList.size() - 1);
        assertThat(testProjectTemplates.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testProjectTemplates.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testProjectTemplates.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
        assertThat(testProjectTemplates.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the ProjectTemplates in Elasticsearch
        ProjectTemplates projectTemplatesEs = projectTemplatesSearchRepository.findOne(testProjectTemplates.getId());
        assertThat(projectTemplatesEs).isEqualToIgnoringGivenFields(testProjectTemplates);
    }

    @Test
    @Transactional
    public void createProjectTemplatesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = projectTemplatesRepository.findAll().size();

        // Create the ProjectTemplates with an existing ID
        projectTemplates.setId(1L);
        ProjectTemplatesDTO projectTemplatesDTO = projectTemplatesMapper.toDto(projectTemplates);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProjectTemplatesMockMvc.perform(post("/api/project-templates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(projectTemplatesDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ProjectTemplates in the database
        List<ProjectTemplates> projectTemplatesList = projectTemplatesRepository.findAll();
        assertThat(projectTemplatesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = projectTemplatesRepository.findAll().size();
        // set the field null
        projectTemplates.setName(null);

        // Create the ProjectTemplates, which fails.
        ProjectTemplatesDTO projectTemplatesDTO = projectTemplatesMapper.toDto(projectTemplates);

        restProjectTemplatesMockMvc.perform(post("/api/project-templates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(projectTemplatesDTO)))
            .andExpect(status().isBadRequest());

        List<ProjectTemplates> projectTemplatesList = projectTemplatesRepository.findAll();
        assertThat(projectTemplatesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkImageIsRequired() throws Exception {
        int databaseSizeBeforeTest = projectTemplatesRepository.findAll().size();
        // set the field null
        projectTemplates.setImage(null);

        // Create the ProjectTemplates, which fails.
        ProjectTemplatesDTO projectTemplatesDTO = projectTemplatesMapper.toDto(projectTemplates);

        restProjectTemplatesMockMvc.perform(post("/api/project-templates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(projectTemplatesDTO)))
            .andExpect(status().isBadRequest());

        List<ProjectTemplates> projectTemplatesList = projectTemplatesRepository.findAll();
        assertThat(projectTemplatesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProjectTemplates() throws Exception {
        // Initialize the database
        projectTemplatesRepository.saveAndFlush(projectTemplates);

        // Get all the projectTemplatesList
        restProjectTemplatesMockMvc.perform(get("/api/project-templates?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(projectTemplates.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getProjectTemplates() throws Exception {
        // Initialize the database
        projectTemplatesRepository.saveAndFlush(projectTemplates);

        // Get the projectTemplates
        restProjectTemplatesMockMvc.perform(get("/api/project-templates/{id}", projectTemplates.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(projectTemplates.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getAllProjectTemplatesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        projectTemplatesRepository.saveAndFlush(projectTemplates);

        // Get all the projectTemplatesList where name equals to DEFAULT_NAME
        defaultProjectTemplatesShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the projectTemplatesList where name equals to UPDATED_NAME
        defaultProjectTemplatesShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllProjectTemplatesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        projectTemplatesRepository.saveAndFlush(projectTemplates);

        // Get all the projectTemplatesList where name in DEFAULT_NAME or UPDATED_NAME
        defaultProjectTemplatesShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the projectTemplatesList where name equals to UPDATED_NAME
        defaultProjectTemplatesShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllProjectTemplatesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        projectTemplatesRepository.saveAndFlush(projectTemplates);

        // Get all the projectTemplatesList where name is not null
        defaultProjectTemplatesShouldBeFound("name.specified=true");

        // Get all the projectTemplatesList where name is null
        defaultProjectTemplatesShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllProjectTemplatesByBusinessLineIsEqualToSomething() throws Exception {
        // Initialize the database
        BusinessLine businessLine = BusinessLineResourceIntTest.createEntity(em);
        em.persist(businessLine);
        em.flush();
        projectTemplates.setBusinessLine(businessLine);
        projectTemplatesRepository.saveAndFlush(projectTemplates);
        Long businessLineId = businessLine.getId();

        // Get all the projectTemplatesList where businessLine equals to businessLineId
        defaultProjectTemplatesShouldBeFound("businessLineId.equals=" + businessLineId);

        // Get all the projectTemplatesList where businessLine equals to businessLineId + 1
        defaultProjectTemplatesShouldNotBeFound("businessLineId.equals=" + (businessLineId + 1));
    }


    @Test
    @Transactional
    public void getAllProjectTemplatesByProjectIsEqualToSomething() throws Exception {
        // Initialize the database
        Projects project = ProjectsResourceIntTest.createEntity(em);
        em.persist(project);
        em.flush();
        projectTemplates.addProject(project);
        projectTemplatesRepository.saveAndFlush(projectTemplates);
        Long projectId = project.getId();

        // Get all the projectTemplatesList where project equals to projectId
        defaultProjectTemplatesShouldBeFound("projectId.equals=" + projectId);

        // Get all the projectTemplatesList where project equals to projectId + 1
        defaultProjectTemplatesShouldNotBeFound("projectId.equals=" + (projectId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultProjectTemplatesShouldBeFound(String filter) throws Exception {
        restProjectTemplatesMockMvc.perform(get("/api/project-templates?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(projectTemplates.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultProjectTemplatesShouldNotBeFound(String filter) throws Exception {
        restProjectTemplatesMockMvc.perform(get("/api/project-templates?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingProjectTemplates() throws Exception {
        // Get the projectTemplates
        restProjectTemplatesMockMvc.perform(get("/api/project-templates/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProjectTemplates() throws Exception {
        // Initialize the database
        projectTemplatesRepository.saveAndFlush(projectTemplates);
        projectTemplatesSearchRepository.save(projectTemplates);
        int databaseSizeBeforeUpdate = projectTemplatesRepository.findAll().size();

        // Update the projectTemplates
        ProjectTemplates updatedProjectTemplates = projectTemplatesRepository.findOne(projectTemplates.getId());
        // Disconnect from session so that the updates on updatedProjectTemplates are not directly saved in db
        em.detach(updatedProjectTemplates);
        updatedProjectTemplates
            .name(UPDATED_NAME)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .description(UPDATED_DESCRIPTION);
        ProjectTemplatesDTO projectTemplatesDTO = projectTemplatesMapper.toDto(updatedProjectTemplates);

        restProjectTemplatesMockMvc.perform(put("/api/project-templates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(projectTemplatesDTO)))
            .andExpect(status().isOk());

        // Validate the ProjectTemplates in the database
        List<ProjectTemplates> projectTemplatesList = projectTemplatesRepository.findAll();
        assertThat(projectTemplatesList).hasSize(databaseSizeBeforeUpdate);
        ProjectTemplates testProjectTemplates = projectTemplatesList.get(projectTemplatesList.size() - 1);
        assertThat(testProjectTemplates.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProjectTemplates.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testProjectTemplates.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testProjectTemplates.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the ProjectTemplates in Elasticsearch
        ProjectTemplates projectTemplatesEs = projectTemplatesSearchRepository.findOne(testProjectTemplates.getId());
        assertThat(projectTemplatesEs).isEqualToIgnoringGivenFields(testProjectTemplates);
    }

    @Test
    @Transactional
    public void updateNonExistingProjectTemplates() throws Exception {
        int databaseSizeBeforeUpdate = projectTemplatesRepository.findAll().size();

        // Create the ProjectTemplates
        ProjectTemplatesDTO projectTemplatesDTO = projectTemplatesMapper.toDto(projectTemplates);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restProjectTemplatesMockMvc.perform(put("/api/project-templates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(projectTemplatesDTO)))
            .andExpect(status().isCreated());

        // Validate the ProjectTemplates in the database
        List<ProjectTemplates> projectTemplatesList = projectTemplatesRepository.findAll();
        assertThat(projectTemplatesList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteProjectTemplates() throws Exception {
        // Initialize the database
        projectTemplatesRepository.saveAndFlush(projectTemplates);
        projectTemplatesSearchRepository.save(projectTemplates);
        int databaseSizeBeforeDelete = projectTemplatesRepository.findAll().size();

        // Get the projectTemplates
        restProjectTemplatesMockMvc.perform(delete("/api/project-templates/{id}", projectTemplates.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean projectTemplatesExistsInEs = projectTemplatesSearchRepository.exists(projectTemplates.getId());
        assertThat(projectTemplatesExistsInEs).isFalse();

        // Validate the database is empty
        List<ProjectTemplates> projectTemplatesList = projectTemplatesRepository.findAll();
        assertThat(projectTemplatesList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchProjectTemplates() throws Exception {
        // Initialize the database
        projectTemplatesRepository.saveAndFlush(projectTemplates);
        projectTemplatesSearchRepository.save(projectTemplates);

        // Search the projectTemplates
        restProjectTemplatesMockMvc.perform(get("/api/_search/project-templates?query=id:" + projectTemplates.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(projectTemplates.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProjectTemplates.class);
        ProjectTemplates projectTemplates1 = new ProjectTemplates();
        projectTemplates1.setId(1L);
        ProjectTemplates projectTemplates2 = new ProjectTemplates();
        projectTemplates2.setId(projectTemplates1.getId());
        assertThat(projectTemplates1).isEqualTo(projectTemplates2);
        projectTemplates2.setId(2L);
        assertThat(projectTemplates1).isNotEqualTo(projectTemplates2);
        projectTemplates1.setId(null);
        assertThat(projectTemplates1).isNotEqualTo(projectTemplates2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProjectTemplatesDTO.class);
        ProjectTemplatesDTO projectTemplatesDTO1 = new ProjectTemplatesDTO();
        projectTemplatesDTO1.setId(1L);
        ProjectTemplatesDTO projectTemplatesDTO2 = new ProjectTemplatesDTO();
        assertThat(projectTemplatesDTO1).isNotEqualTo(projectTemplatesDTO2);
        projectTemplatesDTO2.setId(projectTemplatesDTO1.getId());
        assertThat(projectTemplatesDTO1).isEqualTo(projectTemplatesDTO2);
        projectTemplatesDTO2.setId(2L);
        assertThat(projectTemplatesDTO1).isNotEqualTo(projectTemplatesDTO2);
        projectTemplatesDTO1.setId(null);
        assertThat(projectTemplatesDTO1).isNotEqualTo(projectTemplatesDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(projectTemplatesMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(projectTemplatesMapper.fromId(null)).isNull();
    }
}
