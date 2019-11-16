package fpt.dps.dtms.web.rest;

import fpt.dps.dtms.DtmsApp;

import fpt.dps.dtms.domain.Bugs;
import fpt.dps.dtms.repository.BugsRepository;
import fpt.dps.dtms.service.BugsService;
import fpt.dps.dtms.repository.search.BugsSearchRepository;
import fpt.dps.dtms.service.dto.BugsDTO;
import fpt.dps.dtms.service.mapper.BugsMapper;
import fpt.dps.dtms.web.rest.errors.ExceptionTranslator;
import fpt.dps.dtms.web.rest.external.ExternalBugsResource;

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

import fpt.dps.dtms.domain.enumeration.BugStatus;
import fpt.dps.dtms.domain.enumeration.BugResolution;
/**
 * Test class for the BugsResource REST controller.
 *
 * @see BugsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DtmsApp.class)
public class BugsResourceIntTest {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_STAGE = "AAAAAAAAAA";
    private static final String UPDATED_STAGE = "BBBBBBBBBB";

    private static final BugStatus DEFAULT_STATUS = BugStatus.OPEN;
    private static final BugStatus UPDATED_STATUS = BugStatus.FIXING;

    private static final BugResolution DEFAULT_RESOLUTION = BugResolution.NA;
    private static final BugResolution UPDATED_RESOLUTION = BugResolution.FIXED;

    private static final String DEFAULT_PHYSICAL_PATH = "AAAAAAAAAA";
    private static final String UPDATED_PHYSICAL_PATH = "BBBBBBBBBB";

    @Autowired
    private BugsRepository bugsRepository;

    @Autowired
    private BugsMapper bugsMapper;

    @Autowired
    private BugsService bugsService;

    @Autowired
    private BugsSearchRepository bugsSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restBugsMockMvc;

    private Bugs bugs;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ExternalBugsResource bugsResource = new ExternalBugsResource(bugsService, null);
        this.restBugsMockMvc = MockMvcBuilders.standaloneSetup(bugsResource)
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
    public static Bugs createEntity(EntityManager em) {
        Bugs bugs = new Bugs()
            .description(DEFAULT_DESCRIPTION)
            .code(DEFAULT_CODE)
            .stage(DEFAULT_STAGE)
            .status(DEFAULT_STATUS)
            .resolution(DEFAULT_RESOLUTION)
            .physicalPath(DEFAULT_PHYSICAL_PATH);
        return bugs;
    }

    @Before
    public void initTest() {
        bugsSearchRepository.deleteAll();
        bugs = createEntity(em);
    }

    @Test
    @Transactional
    public void createBugs() throws Exception {
        int databaseSizeBeforeCreate = bugsRepository.findAll().size();

        // Create the Bugs
        BugsDTO bugsDTO = bugsMapper.toDto(bugs);
        restBugsMockMvc.perform(post("/api/bugs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bugsDTO)))
            .andExpect(status().isCreated());

        // Validate the Bugs in the database
        List<Bugs> bugsList = bugsRepository.findAll();
        assertThat(bugsList).hasSize(databaseSizeBeforeCreate + 1);
        Bugs testBugs = bugsList.get(bugsList.size() - 1);
        assertThat(testBugs.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testBugs.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testBugs.getStage()).isEqualTo(DEFAULT_STAGE);
        assertThat(testBugs.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testBugs.getResolution()).isEqualTo(DEFAULT_RESOLUTION);
        assertThat(testBugs.getPhysicalPath()).isEqualTo(DEFAULT_PHYSICAL_PATH);

        // Validate the Bugs in Elasticsearch
        Bugs bugsEs = bugsSearchRepository.findOne(testBugs.getId());
        assertThat(bugsEs).isEqualToIgnoringGivenFields(testBugs);
    }

    @Test
    @Transactional
    public void createBugsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = bugsRepository.findAll().size();

        // Create the Bugs with an existing ID
        bugs.setId(1L);
        BugsDTO bugsDTO = bugsMapper.toDto(bugs);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBugsMockMvc.perform(post("/api/bugs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bugsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Bugs in the database
        List<Bugs> bugsList = bugsRepository.findAll();
        assertThat(bugsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = bugsRepository.findAll().size();
        // set the field null
        bugs.setDescription(null);

        // Create the Bugs, which fails.
        BugsDTO bugsDTO = bugsMapper.toDto(bugs);

        restBugsMockMvc.perform(post("/api/bugs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bugsDTO)))
            .andExpect(status().isBadRequest());

        List<Bugs> bugsList = bugsRepository.findAll();
        assertThat(bugsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = bugsRepository.findAll().size();
        // set the field null
        bugs.setCode(null);

        // Create the Bugs, which fails.
        BugsDTO bugsDTO = bugsMapper.toDto(bugs);

        restBugsMockMvc.perform(post("/api/bugs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bugsDTO)))
            .andExpect(status().isBadRequest());

        List<Bugs> bugsList = bugsRepository.findAll();
        assertThat(bugsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = bugsRepository.findAll().size();
        // set the field null
        bugs.setStatus(null);

        // Create the Bugs, which fails.
        BugsDTO bugsDTO = bugsMapper.toDto(bugs);

        restBugsMockMvc.perform(post("/api/bugs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bugsDTO)))
            .andExpect(status().isBadRequest());

        List<Bugs> bugsList = bugsRepository.findAll();
        assertThat(bugsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkResolutionIsRequired() throws Exception {
        int databaseSizeBeforeTest = bugsRepository.findAll().size();
        // set the field null
        bugs.setResolution(null);

        // Create the Bugs, which fails.
        BugsDTO bugsDTO = bugsMapper.toDto(bugs);

        restBugsMockMvc.perform(post("/api/bugs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bugsDTO)))
            .andExpect(status().isBadRequest());

        List<Bugs> bugsList = bugsRepository.findAll();
        assertThat(bugsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBugs() throws Exception {
        // Initialize the database
        bugsRepository.saveAndFlush(bugs);

        // Get all the bugsList
        restBugsMockMvc.perform(get("/api/bugs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bugs.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].stage").value(hasItem(DEFAULT_STAGE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].resolution").value(hasItem(DEFAULT_RESOLUTION.toString())))
            .andExpect(jsonPath("$.[*].physicalPath").value(hasItem(DEFAULT_PHYSICAL_PATH.toString())));
    }

    @Test
    @Transactional
    public void getBugs() throws Exception {
        // Initialize the database
        bugsRepository.saveAndFlush(bugs);

        // Get the bugs
        restBugsMockMvc.perform(get("/api/bugs/{id}", bugs.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(bugs.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.stage").value(DEFAULT_STAGE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.resolution").value(DEFAULT_RESOLUTION.toString()))
            .andExpect(jsonPath("$.physicalPath").value(DEFAULT_PHYSICAL_PATH.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingBugs() throws Exception {
        // Get the bugs
        restBugsMockMvc.perform(get("/api/bugs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBugs() throws Exception {
        // Initialize the database
        bugsRepository.saveAndFlush(bugs);
        bugsSearchRepository.save(bugs);
        int databaseSizeBeforeUpdate = bugsRepository.findAll().size();

        // Update the bugs
        Bugs updatedBugs = bugsRepository.findOne(bugs.getId());
        // Disconnect from session so that the updates on updatedBugs are not directly saved in db
        em.detach(updatedBugs);
        updatedBugs
            .description(UPDATED_DESCRIPTION)
            .code(UPDATED_CODE)
            .stage(UPDATED_STAGE)
            .status(UPDATED_STATUS)
            .resolution(UPDATED_RESOLUTION)
            .physicalPath(UPDATED_PHYSICAL_PATH);
        BugsDTO bugsDTO = bugsMapper.toDto(updatedBugs);

        restBugsMockMvc.perform(put("/api/bugs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bugsDTO)))
            .andExpect(status().isOk());

        // Validate the Bugs in the database
        List<Bugs> bugsList = bugsRepository.findAll();
        assertThat(bugsList).hasSize(databaseSizeBeforeUpdate);
        Bugs testBugs = bugsList.get(bugsList.size() - 1);
        assertThat(testBugs.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testBugs.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testBugs.getStage()).isEqualTo(UPDATED_STAGE);
        assertThat(testBugs.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testBugs.getResolution()).isEqualTo(UPDATED_RESOLUTION);
        assertThat(testBugs.getPhysicalPath()).isEqualTo(UPDATED_PHYSICAL_PATH);

        // Validate the Bugs in Elasticsearch
        Bugs bugsEs = bugsSearchRepository.findOne(testBugs.getId());
        assertThat(bugsEs).isEqualToIgnoringGivenFields(testBugs);
    }

    @Test
    @Transactional
    public void updateNonExistingBugs() throws Exception {
        int databaseSizeBeforeUpdate = bugsRepository.findAll().size();

        // Create the Bugs
        BugsDTO bugsDTO = bugsMapper.toDto(bugs);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restBugsMockMvc.perform(put("/api/bugs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bugsDTO)))
            .andExpect(status().isCreated());

        // Validate the Bugs in the database
        List<Bugs> bugsList = bugsRepository.findAll();
        assertThat(bugsList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteBugs() throws Exception {
        // Initialize the database
        bugsRepository.saveAndFlush(bugs);
        bugsSearchRepository.save(bugs);
        int databaseSizeBeforeDelete = bugsRepository.findAll().size();

        // Get the bugs
        restBugsMockMvc.perform(delete("/api/bugs/{id}", bugs.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean bugsExistsInEs = bugsSearchRepository.exists(bugs.getId());
        assertThat(bugsExistsInEs).isFalse();

        // Validate the database is empty
        List<Bugs> bugsList = bugsRepository.findAll();
        assertThat(bugsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchBugs() throws Exception {
        // Initialize the database
        bugsRepository.saveAndFlush(bugs);
        bugsSearchRepository.save(bugs);

        // Search the bugs
        restBugsMockMvc.perform(get("/api/_search/bugs?query=id:" + bugs.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bugs.getId().intValue())))
           .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].stage").value(hasItem(DEFAULT_STAGE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].resolution").value(hasItem(DEFAULT_RESOLUTION.toString())))
            .andExpect(jsonPath("$.[*].physicalPath").value(hasItem(DEFAULT_PHYSICAL_PATH.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Bugs.class);
        Bugs bugs1 = new Bugs();
        bugs1.setId(1L);
        Bugs bugs2 = new Bugs();
        bugs2.setId(bugs1.getId());
        assertThat(bugs1).isEqualTo(bugs2);
        bugs2.setId(2L);
        assertThat(bugs1).isNotEqualTo(bugs2);
        bugs1.setId(null);
        assertThat(bugs1).isNotEqualTo(bugs2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BugsDTO.class);
        BugsDTO bugsDTO1 = new BugsDTO();
        bugsDTO1.setId(1L);
        BugsDTO bugsDTO2 = new BugsDTO();
        assertThat(bugsDTO1).isNotEqualTo(bugsDTO2);
        bugsDTO2.setId(bugsDTO1.getId());
        assertThat(bugsDTO1).isEqualTo(bugsDTO2);
        bugsDTO2.setId(2L);
        assertThat(bugsDTO1).isNotEqualTo(bugsDTO2);
        bugsDTO1.setId(null);
        assertThat(bugsDTO1).isNotEqualTo(bugsDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(bugsMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(bugsMapper.fromId(null)).isNull();
    }
}
