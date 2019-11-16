package fpt.dps.dtms.web.rest;

import fpt.dps.dtms.DtmsApp;

import fpt.dps.dtms.domain.TMSCustomFieldScreen;
import fpt.dps.dtms.domain.TMSCustomField;
import fpt.dps.dtms.domain.ProjectWorkflows;
import fpt.dps.dtms.repository.TMSCustomFieldScreenRepository;
import fpt.dps.dtms.service.TMSCustomFieldScreenService;
import fpt.dps.dtms.repository.search.TMSCustomFieldScreenSearchRepository;
import fpt.dps.dtms.service.dto.TMSCustomFieldScreenDTO;
import fpt.dps.dtms.service.mapper.TMSCustomFieldScreenMapper;
import fpt.dps.dtms.web.rest.errors.ExceptionTranslator;
import fpt.dps.dtms.service.dto.TMSCustomFieldScreenCriteria;
import fpt.dps.dtms.service.TMSCustomFieldScreenQueryService;

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
 * Test class for the TMSCustomFieldScreenResource REST controller.
 *
 * @see TMSCustomFieldScreenResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DtmsApp.class)
public class TMSCustomFieldScreenResourceIntTest {

    private static final Integer DEFAULT_SEQUENCE = 1;
    private static final Integer UPDATED_SEQUENCE = 2;

    @Autowired
    private TMSCustomFieldScreenRepository tMSCustomFieldScreenRepository;

    @Autowired
    private TMSCustomFieldScreenMapper tMSCustomFieldScreenMapper;

    @Autowired
    private TMSCustomFieldScreenService tMSCustomFieldScreenService;

    @Autowired
    private TMSCustomFieldScreenSearchRepository tMSCustomFieldScreenSearchRepository;

    @Autowired
    private TMSCustomFieldScreenQueryService tMSCustomFieldScreenQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTMSCustomFieldScreenMockMvc;

    private TMSCustomFieldScreen tMSCustomFieldScreen;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TMSCustomFieldScreenResource tMSCustomFieldScreenResource = new TMSCustomFieldScreenResource(tMSCustomFieldScreenService, tMSCustomFieldScreenQueryService);
        this.restTMSCustomFieldScreenMockMvc = MockMvcBuilders.standaloneSetup(tMSCustomFieldScreenResource)
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
    public static TMSCustomFieldScreen createEntity(EntityManager em) {
        TMSCustomFieldScreen tMSCustomFieldScreen = new TMSCustomFieldScreen()
            .sequence(DEFAULT_SEQUENCE);
        return tMSCustomFieldScreen;
    }

    @Before
    public void initTest() {
        tMSCustomFieldScreenSearchRepository.deleteAll();
        tMSCustomFieldScreen = createEntity(em);
    }

    @Test
    @Transactional
    public void createTMSCustomFieldScreen() throws Exception {
        int databaseSizeBeforeCreate = tMSCustomFieldScreenRepository.findAll().size();

        // Create the TMSCustomFieldScreen
        TMSCustomFieldScreenDTO tMSCustomFieldScreenDTO = tMSCustomFieldScreenMapper.toDto(tMSCustomFieldScreen);
        restTMSCustomFieldScreenMockMvc.perform(post("/api/tms-custom-field-screens")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tMSCustomFieldScreenDTO)))
            .andExpect(status().isCreated());

        // Validate the TMSCustomFieldScreen in the database
        List<TMSCustomFieldScreen> tMSCustomFieldScreenList = tMSCustomFieldScreenRepository.findAll();
        assertThat(tMSCustomFieldScreenList).hasSize(databaseSizeBeforeCreate + 1);
        TMSCustomFieldScreen testTMSCustomFieldScreen = tMSCustomFieldScreenList.get(tMSCustomFieldScreenList.size() - 1);
        assertThat(testTMSCustomFieldScreen.getSequence()).isEqualTo(DEFAULT_SEQUENCE);

        // Validate the TMSCustomFieldScreen in Elasticsearch
        TMSCustomFieldScreen tMSCustomFieldScreenEs = tMSCustomFieldScreenSearchRepository.findOne(testTMSCustomFieldScreen.getId());
        assertThat(tMSCustomFieldScreenEs).isEqualToIgnoringGivenFields(testTMSCustomFieldScreen);
    }

    @Test
    @Transactional
    public void createTMSCustomFieldScreenWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = tMSCustomFieldScreenRepository.findAll().size();

        // Create the TMSCustomFieldScreen with an existing ID
        tMSCustomFieldScreen.setId(1L);
        TMSCustomFieldScreenDTO tMSCustomFieldScreenDTO = tMSCustomFieldScreenMapper.toDto(tMSCustomFieldScreen);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTMSCustomFieldScreenMockMvc.perform(post("/api/tms-custom-field-screens")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tMSCustomFieldScreenDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TMSCustomFieldScreen in the database
        List<TMSCustomFieldScreen> tMSCustomFieldScreenList = tMSCustomFieldScreenRepository.findAll();
        assertThat(tMSCustomFieldScreenList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkSequenceIsRequired() throws Exception {
        int databaseSizeBeforeTest = tMSCustomFieldScreenRepository.findAll().size();
        // set the field null
        tMSCustomFieldScreen.setSequence(null);

        // Create the TMSCustomFieldScreen, which fails.
        TMSCustomFieldScreenDTO tMSCustomFieldScreenDTO = tMSCustomFieldScreenMapper.toDto(tMSCustomFieldScreen);

        restTMSCustomFieldScreenMockMvc.perform(post("/api/tms-custom-field-screens")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tMSCustomFieldScreenDTO)))
            .andExpect(status().isBadRequest());

        List<TMSCustomFieldScreen> tMSCustomFieldScreenList = tMSCustomFieldScreenRepository.findAll();
        assertThat(tMSCustomFieldScreenList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTMSCustomFieldScreens() throws Exception {
        // Initialize the database
        tMSCustomFieldScreenRepository.saveAndFlush(tMSCustomFieldScreen);

        // Get all the tMSCustomFieldScreenList
        restTMSCustomFieldScreenMockMvc.perform(get("/api/tms-custom-field-screens?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tMSCustomFieldScreen.getId().intValue())))
            .andExpect(jsonPath("$.[*].sequence").value(hasItem(DEFAULT_SEQUENCE)));
    }

    @Test
    @Transactional
    public void getTMSCustomFieldScreen() throws Exception {
        // Initialize the database
        tMSCustomFieldScreenRepository.saveAndFlush(tMSCustomFieldScreen);

        // Get the tMSCustomFieldScreen
        restTMSCustomFieldScreenMockMvc.perform(get("/api/tms-custom-field-screens/{id}", tMSCustomFieldScreen.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(tMSCustomFieldScreen.getId().intValue()))
            .andExpect(jsonPath("$.sequence").value(DEFAULT_SEQUENCE));
    }

    @Test
    @Transactional
    public void getAllTMSCustomFieldScreensBySequenceIsEqualToSomething() throws Exception {
        // Initialize the database
        tMSCustomFieldScreenRepository.saveAndFlush(tMSCustomFieldScreen);

        // Get all the tMSCustomFieldScreenList where sequence equals to DEFAULT_SEQUENCE
        defaultTMSCustomFieldScreenShouldBeFound("sequence.equals=" + DEFAULT_SEQUENCE);

        // Get all the tMSCustomFieldScreenList where sequence equals to UPDATED_SEQUENCE
        defaultTMSCustomFieldScreenShouldNotBeFound("sequence.equals=" + UPDATED_SEQUENCE);
    }

    @Test
    @Transactional
    public void getAllTMSCustomFieldScreensBySequenceIsInShouldWork() throws Exception {
        // Initialize the database
        tMSCustomFieldScreenRepository.saveAndFlush(tMSCustomFieldScreen);

        // Get all the tMSCustomFieldScreenList where sequence in DEFAULT_SEQUENCE or UPDATED_SEQUENCE
        defaultTMSCustomFieldScreenShouldBeFound("sequence.in=" + DEFAULT_SEQUENCE + "," + UPDATED_SEQUENCE);

        // Get all the tMSCustomFieldScreenList where sequence equals to UPDATED_SEQUENCE
        defaultTMSCustomFieldScreenShouldNotBeFound("sequence.in=" + UPDATED_SEQUENCE);
    }

    @Test
    @Transactional
    public void getAllTMSCustomFieldScreensBySequenceIsNullOrNotNull() throws Exception {
        // Initialize the database
        tMSCustomFieldScreenRepository.saveAndFlush(tMSCustomFieldScreen);

        // Get all the tMSCustomFieldScreenList where sequence is not null
        defaultTMSCustomFieldScreenShouldBeFound("sequence.specified=true");

        // Get all the tMSCustomFieldScreenList where sequence is null
        defaultTMSCustomFieldScreenShouldNotBeFound("sequence.specified=false");
    }

    @Test
    @Transactional
    public void getAllTMSCustomFieldScreensBySequenceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tMSCustomFieldScreenRepository.saveAndFlush(tMSCustomFieldScreen);

        // Get all the tMSCustomFieldScreenList where sequence greater than or equals to DEFAULT_SEQUENCE
        defaultTMSCustomFieldScreenShouldBeFound("sequence.greaterOrEqualThan=" + DEFAULT_SEQUENCE);

        // Get all the tMSCustomFieldScreenList where sequence greater than or equals to UPDATED_SEQUENCE
        defaultTMSCustomFieldScreenShouldNotBeFound("sequence.greaterOrEqualThan=" + UPDATED_SEQUENCE);
    }

    @Test
    @Transactional
    public void getAllTMSCustomFieldScreensBySequenceIsLessThanSomething() throws Exception {
        // Initialize the database
        tMSCustomFieldScreenRepository.saveAndFlush(tMSCustomFieldScreen);

        // Get all the tMSCustomFieldScreenList where sequence less than or equals to DEFAULT_SEQUENCE
        defaultTMSCustomFieldScreenShouldNotBeFound("sequence.lessThan=" + DEFAULT_SEQUENCE);

        // Get all the tMSCustomFieldScreenList where sequence less than or equals to UPDATED_SEQUENCE
        defaultTMSCustomFieldScreenShouldBeFound("sequence.lessThan=" + UPDATED_SEQUENCE);
    }


    @Test
    @Transactional
    public void getAllTMSCustomFieldScreensByTmsCustomFieldIsEqualToSomething() throws Exception {
        // Initialize the database
        TMSCustomField tmsCustomField = TMSCustomFieldResourceIntTest.createEntity(em);
        em.persist(tmsCustomField);
        em.flush();
        tMSCustomFieldScreen.setTmsCustomField(tmsCustomField);
        tMSCustomFieldScreenRepository.saveAndFlush(tMSCustomFieldScreen);
        Long tmsCustomFieldId = tmsCustomField.getId();

        // Get all the tMSCustomFieldScreenList where tmsCustomField equals to tmsCustomFieldId
        defaultTMSCustomFieldScreenShouldBeFound("tmsCustomFieldId.equals=" + tmsCustomFieldId);

        // Get all the tMSCustomFieldScreenList where tmsCustomField equals to tmsCustomFieldId + 1
        defaultTMSCustomFieldScreenShouldNotBeFound("tmsCustomFieldId.equals=" + (tmsCustomFieldId + 1));
    }


    @Test
    @Transactional
    public void getAllTMSCustomFieldScreensByProjectWorkflowsIsEqualToSomething() throws Exception {
        // Initialize the database
        ProjectWorkflows projectWorkflows = ProjectWorkflowsResourceIntTest.createEntity(em);
        em.persist(projectWorkflows);
        em.flush();
        tMSCustomFieldScreen.setProjectWorkflows(projectWorkflows);
        tMSCustomFieldScreenRepository.saveAndFlush(tMSCustomFieldScreen);
        Long projectWorkflowsId = projectWorkflows.getId();

        // Get all the tMSCustomFieldScreenList where projectWorkflows equals to projectWorkflowsId
        defaultTMSCustomFieldScreenShouldBeFound("projectWorkflowsId.equals=" + projectWorkflowsId);

        // Get all the tMSCustomFieldScreenList where projectWorkflows equals to projectWorkflowsId + 1
        defaultTMSCustomFieldScreenShouldNotBeFound("projectWorkflowsId.equals=" + (projectWorkflowsId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultTMSCustomFieldScreenShouldBeFound(String filter) throws Exception {
        restTMSCustomFieldScreenMockMvc.perform(get("/api/tms-custom-field-screens?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tMSCustomFieldScreen.getId().intValue())))
            .andExpect(jsonPath("$.[*].sequence").value(hasItem(DEFAULT_SEQUENCE)));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultTMSCustomFieldScreenShouldNotBeFound(String filter) throws Exception {
        restTMSCustomFieldScreenMockMvc.perform(get("/api/tms-custom-field-screens?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingTMSCustomFieldScreen() throws Exception {
        // Get the tMSCustomFieldScreen
        restTMSCustomFieldScreenMockMvc.perform(get("/api/tms-custom-field-screens/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTMSCustomFieldScreen() throws Exception {
        // Initialize the database
        tMSCustomFieldScreenRepository.saveAndFlush(tMSCustomFieldScreen);
        tMSCustomFieldScreenSearchRepository.save(tMSCustomFieldScreen);
        int databaseSizeBeforeUpdate = tMSCustomFieldScreenRepository.findAll().size();

        // Update the tMSCustomFieldScreen
        TMSCustomFieldScreen updatedTMSCustomFieldScreen = tMSCustomFieldScreenRepository.findOne(tMSCustomFieldScreen.getId());
        // Disconnect from session so that the updates on updatedTMSCustomFieldScreen are not directly saved in db
        em.detach(updatedTMSCustomFieldScreen);
        updatedTMSCustomFieldScreen
            .sequence(UPDATED_SEQUENCE);
        TMSCustomFieldScreenDTO tMSCustomFieldScreenDTO = tMSCustomFieldScreenMapper.toDto(updatedTMSCustomFieldScreen);

        restTMSCustomFieldScreenMockMvc.perform(put("/api/tms-custom-field-screens")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tMSCustomFieldScreenDTO)))
            .andExpect(status().isOk());

        // Validate the TMSCustomFieldScreen in the database
        List<TMSCustomFieldScreen> tMSCustomFieldScreenList = tMSCustomFieldScreenRepository.findAll();
        assertThat(tMSCustomFieldScreenList).hasSize(databaseSizeBeforeUpdate);
        TMSCustomFieldScreen testTMSCustomFieldScreen = tMSCustomFieldScreenList.get(tMSCustomFieldScreenList.size() - 1);
        assertThat(testTMSCustomFieldScreen.getSequence()).isEqualTo(UPDATED_SEQUENCE);

        // Validate the TMSCustomFieldScreen in Elasticsearch
        TMSCustomFieldScreen tMSCustomFieldScreenEs = tMSCustomFieldScreenSearchRepository.findOne(testTMSCustomFieldScreen.getId());
        assertThat(tMSCustomFieldScreenEs).isEqualToIgnoringGivenFields(testTMSCustomFieldScreen);
    }

    @Test
    @Transactional
    public void updateNonExistingTMSCustomFieldScreen() throws Exception {
        int databaseSizeBeforeUpdate = tMSCustomFieldScreenRepository.findAll().size();

        // Create the TMSCustomFieldScreen
        TMSCustomFieldScreenDTO tMSCustomFieldScreenDTO = tMSCustomFieldScreenMapper.toDto(tMSCustomFieldScreen);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTMSCustomFieldScreenMockMvc.perform(put("/api/tms-custom-field-screens")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tMSCustomFieldScreenDTO)))
            .andExpect(status().isCreated());

        // Validate the TMSCustomFieldScreen in the database
        List<TMSCustomFieldScreen> tMSCustomFieldScreenList = tMSCustomFieldScreenRepository.findAll();
        assertThat(tMSCustomFieldScreenList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTMSCustomFieldScreen() throws Exception {
        // Initialize the database
        tMSCustomFieldScreenRepository.saveAndFlush(tMSCustomFieldScreen);
        tMSCustomFieldScreenSearchRepository.save(tMSCustomFieldScreen);
        int databaseSizeBeforeDelete = tMSCustomFieldScreenRepository.findAll().size();

        // Get the tMSCustomFieldScreen
        restTMSCustomFieldScreenMockMvc.perform(delete("/api/tms-custom-field-screens/{id}", tMSCustomFieldScreen.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean tMSCustomFieldScreenExistsInEs = tMSCustomFieldScreenSearchRepository.exists(tMSCustomFieldScreen.getId());
        assertThat(tMSCustomFieldScreenExistsInEs).isFalse();

        // Validate the database is empty
        List<TMSCustomFieldScreen> tMSCustomFieldScreenList = tMSCustomFieldScreenRepository.findAll();
        assertThat(tMSCustomFieldScreenList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchTMSCustomFieldScreen() throws Exception {
        // Initialize the database
        tMSCustomFieldScreenRepository.saveAndFlush(tMSCustomFieldScreen);
        tMSCustomFieldScreenSearchRepository.save(tMSCustomFieldScreen);

        // Search the tMSCustomFieldScreen
        restTMSCustomFieldScreenMockMvc.perform(get("/api/_search/tms-custom-field-screens?query=id:" + tMSCustomFieldScreen.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tMSCustomFieldScreen.getId().intValue())))
            .andExpect(jsonPath("$.[*].sequence").value(hasItem(DEFAULT_SEQUENCE)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TMSCustomFieldScreen.class);
        TMSCustomFieldScreen tMSCustomFieldScreen1 = new TMSCustomFieldScreen();
        tMSCustomFieldScreen1.setId(1L);
        TMSCustomFieldScreen tMSCustomFieldScreen2 = new TMSCustomFieldScreen();
        tMSCustomFieldScreen2.setId(tMSCustomFieldScreen1.getId());
        assertThat(tMSCustomFieldScreen1).isEqualTo(tMSCustomFieldScreen2);
        tMSCustomFieldScreen2.setId(2L);
        assertThat(tMSCustomFieldScreen1).isNotEqualTo(tMSCustomFieldScreen2);
        tMSCustomFieldScreen1.setId(null);
        assertThat(tMSCustomFieldScreen1).isNotEqualTo(tMSCustomFieldScreen2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TMSCustomFieldScreenDTO.class);
        TMSCustomFieldScreenDTO tMSCustomFieldScreenDTO1 = new TMSCustomFieldScreenDTO();
        tMSCustomFieldScreenDTO1.setId(1L);
        TMSCustomFieldScreenDTO tMSCustomFieldScreenDTO2 = new TMSCustomFieldScreenDTO();
        assertThat(tMSCustomFieldScreenDTO1).isNotEqualTo(tMSCustomFieldScreenDTO2);
        tMSCustomFieldScreenDTO2.setId(tMSCustomFieldScreenDTO1.getId());
        assertThat(tMSCustomFieldScreenDTO1).isEqualTo(tMSCustomFieldScreenDTO2);
        tMSCustomFieldScreenDTO2.setId(2L);
        assertThat(tMSCustomFieldScreenDTO1).isNotEqualTo(tMSCustomFieldScreenDTO2);
        tMSCustomFieldScreenDTO1.setId(null);
        assertThat(tMSCustomFieldScreenDTO1).isNotEqualTo(tMSCustomFieldScreenDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(tMSCustomFieldScreenMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(tMSCustomFieldScreenMapper.fromId(null)).isNull();
    }
}
