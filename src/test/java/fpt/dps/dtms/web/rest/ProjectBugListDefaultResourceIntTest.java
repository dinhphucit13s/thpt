package fpt.dps.dtms.web.rest;

import fpt.dps.dtms.DtmsApp;

import fpt.dps.dtms.domain.ProjectBugListDefault;
import fpt.dps.dtms.domain.Projects;
import fpt.dps.dtms.domain.BugListDefault;
import fpt.dps.dtms.repository.ProjectBugListDefaultRepository;
import fpt.dps.dtms.service.ProjectBugListDefaultService;
import fpt.dps.dtms.repository.search.ProjectBugListDefaultSearchRepository;
import fpt.dps.dtms.service.dto.ProjectBugListDefaultDTO;
import fpt.dps.dtms.service.mapper.ProjectBugListDefaultMapper;
import fpt.dps.dtms.web.rest.errors.ExceptionTranslator;

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

/**
 * Test class for the ProjectBugListDefaultResource REST controller.
 *
 * @see ProjectBugListDefaultResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DtmsApp.class)
public class ProjectBugListDefaultResourceIntTest {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    @Autowired
    private ProjectBugListDefaultRepository projectBugListDefaultRepository;

    @Autowired
    private ProjectBugListDefaultMapper projectBugListDefaultMapper;

    @Autowired
    private ProjectBugListDefaultService projectBugListDefaultService;

    @Autowired
    private ProjectBugListDefaultSearchRepository projectBugListDefaultSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restProjectBugListDefaultMockMvc;

    private ProjectBugListDefault projectBugListDefault;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ProjectBugListDefaultResource projectBugListDefaultResource = new ProjectBugListDefaultResource(projectBugListDefaultService, null);
        this.restProjectBugListDefaultMockMvc = MockMvcBuilders.standaloneSetup(projectBugListDefaultResource)
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
    public static ProjectBugListDefault createEntity(EntityManager em) {
        ProjectBugListDefault projectBugListDefault = new ProjectBugListDefault()
            .code(DEFAULT_CODE);
        // Add required entity
        Projects project = ProjectsResourceIntTest.createEntity(em);
        em.persist(project);
        em.flush();
        projectBugListDefault.setProject(project);
        // Add required entity
        BugListDefault bugListDefault = BugListDefaultResourceIntTest.createEntity(em);
        em.persist(bugListDefault);
        em.flush();
        projectBugListDefault.setBugListDefault(bugListDefault);
        return projectBugListDefault;
    }

    @Before
    public void initTest() {
        projectBugListDefaultSearchRepository.deleteAll();
        projectBugListDefault = createEntity(em);
    }

    @Test
    @Transactional
    public void createProjectBugListDefault() throws Exception {
        int databaseSizeBeforeCreate = projectBugListDefaultRepository.findAll().size();

        // Create the ProjectBugListDefault
        ProjectBugListDefaultDTO projectBugListDefaultDTO = projectBugListDefaultMapper.toDto(projectBugListDefault);
        restProjectBugListDefaultMockMvc.perform(post("/api/project-bug-list-defaults")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(projectBugListDefaultDTO)))
            .andExpect(status().isCreated());

        // Validate the ProjectBugListDefault in the database
        List<ProjectBugListDefault> projectBugListDefaultList = projectBugListDefaultRepository.findAll();
        assertThat(projectBugListDefaultList).hasSize(databaseSizeBeforeCreate + 1);
        ProjectBugListDefault testProjectBugListDefault = projectBugListDefaultList.get(projectBugListDefaultList.size() - 1);
        assertThat(testProjectBugListDefault.getCode()).isEqualTo(DEFAULT_CODE);

        // Validate the ProjectBugListDefault in Elasticsearch
        ProjectBugListDefault projectBugListDefaultEs = projectBugListDefaultSearchRepository.findOne(testProjectBugListDefault.getId());
        assertThat(projectBugListDefaultEs).isEqualToIgnoringGivenFields(testProjectBugListDefault);
    }

    @Test
    @Transactional
    public void createProjectBugListDefaultWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = projectBugListDefaultRepository.findAll().size();

        // Create the ProjectBugListDefault with an existing ID
        projectBugListDefault.setId(1L);
        ProjectBugListDefaultDTO projectBugListDefaultDTO = projectBugListDefaultMapper.toDto(projectBugListDefault);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProjectBugListDefaultMockMvc.perform(post("/api/project-bug-list-defaults")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(projectBugListDefaultDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ProjectBugListDefault in the database
        List<ProjectBugListDefault> projectBugListDefaultList = projectBugListDefaultRepository.findAll();
        assertThat(projectBugListDefaultList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = projectBugListDefaultRepository.findAll().size();
        // set the field null
        projectBugListDefault.setCode(null);

        // Create the ProjectBugListDefault, which fails.
        ProjectBugListDefaultDTO projectBugListDefaultDTO = projectBugListDefaultMapper.toDto(projectBugListDefault);

        restProjectBugListDefaultMockMvc.perform(post("/api/project-bug-list-defaults")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(projectBugListDefaultDTO)))
            .andExpect(status().isBadRequest());

        List<ProjectBugListDefault> projectBugListDefaultList = projectBugListDefaultRepository.findAll();
        assertThat(projectBugListDefaultList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProjectBugListDefaults() throws Exception {
        // Initialize the database
        projectBugListDefaultRepository.saveAndFlush(projectBugListDefault);

        // Get all the projectBugListDefaultList
        restProjectBugListDefaultMockMvc.perform(get("/api/project-bug-list-defaults?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(projectBugListDefault.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())));
    }

    @Test
    @Transactional
    public void getProjectBugListDefault() throws Exception {
        // Initialize the database
        projectBugListDefaultRepository.saveAndFlush(projectBugListDefault);

        // Get the projectBugListDefault
        restProjectBugListDefaultMockMvc.perform(get("/api/project-bug-list-defaults/{id}", projectBugListDefault.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(projectBugListDefault.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingProjectBugListDefault() throws Exception {
        // Get the projectBugListDefault
        restProjectBugListDefaultMockMvc.perform(get("/api/project-bug-list-defaults/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProjectBugListDefault() throws Exception {
        // Initialize the database
        projectBugListDefaultRepository.saveAndFlush(projectBugListDefault);
        projectBugListDefaultSearchRepository.save(projectBugListDefault);
        int databaseSizeBeforeUpdate = projectBugListDefaultRepository.findAll().size();

        // Update the projectBugListDefault
        ProjectBugListDefault updatedProjectBugListDefault = projectBugListDefaultRepository.findOne(projectBugListDefault.getId());
        // Disconnect from session so that the updates on updatedProjectBugListDefault are not directly saved in db
        em.detach(updatedProjectBugListDefault);
        updatedProjectBugListDefault
            .code(UPDATED_CODE);
        ProjectBugListDefaultDTO projectBugListDefaultDTO = projectBugListDefaultMapper.toDto(updatedProjectBugListDefault);

        restProjectBugListDefaultMockMvc.perform(put("/api/project-bug-list-defaults")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(projectBugListDefaultDTO)))
            .andExpect(status().isOk());

        // Validate the ProjectBugListDefault in the database
        List<ProjectBugListDefault> projectBugListDefaultList = projectBugListDefaultRepository.findAll();
        assertThat(projectBugListDefaultList).hasSize(databaseSizeBeforeUpdate);
        ProjectBugListDefault testProjectBugListDefault = projectBugListDefaultList.get(projectBugListDefaultList.size() - 1);
        assertThat(testProjectBugListDefault.getCode()).isEqualTo(UPDATED_CODE);

        // Validate the ProjectBugListDefault in Elasticsearch
        ProjectBugListDefault projectBugListDefaultEs = projectBugListDefaultSearchRepository.findOne(testProjectBugListDefault.getId());
        assertThat(projectBugListDefaultEs).isEqualToIgnoringGivenFields(testProjectBugListDefault);
    }

    @Test
    @Transactional
    public void updateNonExistingProjectBugListDefault() throws Exception {
        int databaseSizeBeforeUpdate = projectBugListDefaultRepository.findAll().size();

        // Create the ProjectBugListDefault
        ProjectBugListDefaultDTO projectBugListDefaultDTO = projectBugListDefaultMapper.toDto(projectBugListDefault);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restProjectBugListDefaultMockMvc.perform(put("/api/project-bug-list-defaults")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(projectBugListDefaultDTO)))
            .andExpect(status().isCreated());

        // Validate the ProjectBugListDefault in the database
        List<ProjectBugListDefault> projectBugListDefaultList = projectBugListDefaultRepository.findAll();
        assertThat(projectBugListDefaultList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteProjectBugListDefault() throws Exception {
        // Initialize the database
        projectBugListDefaultRepository.saveAndFlush(projectBugListDefault);
        projectBugListDefaultSearchRepository.save(projectBugListDefault);
        int databaseSizeBeforeDelete = projectBugListDefaultRepository.findAll().size();

        // Get the projectBugListDefault
        restProjectBugListDefaultMockMvc.perform(delete("/api/project-bug-list-defaults/{id}", projectBugListDefault.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean projectBugListDefaultExistsInEs = projectBugListDefaultSearchRepository.exists(projectBugListDefault.getId());
        assertThat(projectBugListDefaultExistsInEs).isFalse();

        // Validate the database is empty
        List<ProjectBugListDefault> projectBugListDefaultList = projectBugListDefaultRepository.findAll();
        assertThat(projectBugListDefaultList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchProjectBugListDefault() throws Exception {
        // Initialize the database
        projectBugListDefaultRepository.saveAndFlush(projectBugListDefault);
        projectBugListDefaultSearchRepository.save(projectBugListDefault);

        // Search the projectBugListDefault
        restProjectBugListDefaultMockMvc.perform(get("/api/_search/project-bug-list-defaults?query=id:" + projectBugListDefault.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(projectBugListDefault.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProjectBugListDefault.class);
        ProjectBugListDefault projectBugListDefault1 = new ProjectBugListDefault();
        projectBugListDefault1.setId(1L);
        ProjectBugListDefault projectBugListDefault2 = new ProjectBugListDefault();
        projectBugListDefault2.setId(projectBugListDefault1.getId());
        assertThat(projectBugListDefault1).isEqualTo(projectBugListDefault2);
        projectBugListDefault2.setId(2L);
        assertThat(projectBugListDefault1).isNotEqualTo(projectBugListDefault2);
        projectBugListDefault1.setId(null);
        assertThat(projectBugListDefault1).isNotEqualTo(projectBugListDefault2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProjectBugListDefaultDTO.class);
        ProjectBugListDefaultDTO projectBugListDefaultDTO1 = new ProjectBugListDefaultDTO();
        projectBugListDefaultDTO1.setId(1L);
        ProjectBugListDefaultDTO projectBugListDefaultDTO2 = new ProjectBugListDefaultDTO();
        assertThat(projectBugListDefaultDTO1).isNotEqualTo(projectBugListDefaultDTO2);
        projectBugListDefaultDTO2.setId(projectBugListDefaultDTO1.getId());
        assertThat(projectBugListDefaultDTO1).isEqualTo(projectBugListDefaultDTO2);
        projectBugListDefaultDTO2.setId(2L);
        assertThat(projectBugListDefaultDTO1).isNotEqualTo(projectBugListDefaultDTO2);
        projectBugListDefaultDTO1.setId(null);
        assertThat(projectBugListDefaultDTO1).isNotEqualTo(projectBugListDefaultDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(projectBugListDefaultMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(projectBugListDefaultMapper.fromId(null)).isNull();
    }
}
