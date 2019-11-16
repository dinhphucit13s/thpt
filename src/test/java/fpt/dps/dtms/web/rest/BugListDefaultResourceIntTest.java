package fpt.dps.dtms.web.rest;

import fpt.dps.dtms.DtmsApp;

import fpt.dps.dtms.domain.BugListDefault;
import fpt.dps.dtms.repository.BugListDefaultRepository;
import fpt.dps.dtms.service.BugListDefaultService;
import fpt.dps.dtms.repository.search.BugListDefaultSearchRepository;
import fpt.dps.dtms.service.dto.BugListDefaultDTO;
import fpt.dps.dtms.service.mapper.BugListDefaultMapper;
import fpt.dps.dtms.web.rest.errors.ExceptionTranslator;
import fpt.dps.dtms.service.dto.BugListDefaultCriteria;
import fpt.dps.dtms.service.BugListDefaultQueryService;

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
 * Test class for the BugListDefaultResource REST controller.
 *
 * @see BugListDefaultResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DtmsApp.class)
public class BugListDefaultResourceIntTest {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_STATUS = false;
    private static final Boolean UPDATED_STATUS = true;

    @Autowired
    private BugListDefaultRepository bugListDefaultRepository;

    @Autowired
    private BugListDefaultMapper bugListDefaultMapper;

    @Autowired
    private BugListDefaultService bugListDefaultService;

    @Autowired
    private BugListDefaultSearchRepository bugListDefaultSearchRepository;

    @Autowired
    private BugListDefaultQueryService bugListDefaultQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restBugListDefaultMockMvc;

    private BugListDefault bugListDefault;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final BugListDefaultResource bugListDefaultResource = new BugListDefaultResource(bugListDefaultService, bugListDefaultQueryService);
        this.restBugListDefaultMockMvc = MockMvcBuilders.standaloneSetup(bugListDefaultResource)
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
    public static BugListDefault createEntity(EntityManager em) {
        BugListDefault bugListDefault = new BugListDefault()
            .description(DEFAULT_DESCRIPTION)
            .status(DEFAULT_STATUS);
        return bugListDefault;
    }

    @Before
    public void initTest() {
        bugListDefaultSearchRepository.deleteAll();
        bugListDefault = createEntity(em);
    }

    @Test
    @Transactional
    public void createBugListDefault() throws Exception {
        int databaseSizeBeforeCreate = bugListDefaultRepository.findAll().size();

        // Create the BugListDefault
        BugListDefaultDTO bugListDefaultDTO = bugListDefaultMapper.toDto(bugListDefault);
        restBugListDefaultMockMvc.perform(post("/api/bug-list-defaults")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bugListDefaultDTO)))
            .andExpect(status().isCreated());

        // Validate the BugListDefault in the database
        List<BugListDefault> bugListDefaultList = bugListDefaultRepository.findAll();
        assertThat(bugListDefaultList).hasSize(databaseSizeBeforeCreate + 1);
        BugListDefault testBugListDefault = bugListDefaultList.get(bugListDefaultList.size() - 1);
        assertThat(testBugListDefault.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testBugListDefault.isStatus()).isEqualTo(DEFAULT_STATUS);

        // Validate the BugListDefault in Elasticsearch
        BugListDefault bugListDefaultEs = bugListDefaultSearchRepository.findOne(testBugListDefault.getId());
        assertThat(bugListDefaultEs).isEqualToIgnoringGivenFields(testBugListDefault);
    }

    @Test
    @Transactional
    public void createBugListDefaultWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = bugListDefaultRepository.findAll().size();

        // Create the BugListDefault with an existing ID
        bugListDefault.setId(1L);
        BugListDefaultDTO bugListDefaultDTO = bugListDefaultMapper.toDto(bugListDefault);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBugListDefaultMockMvc.perform(post("/api/bug-list-defaults")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bugListDefaultDTO)))
            .andExpect(status().isBadRequest());

        // Validate the BugListDefault in the database
        List<BugListDefault> bugListDefaultList = bugListDefaultRepository.findAll();
        assertThat(bugListDefaultList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = bugListDefaultRepository.findAll().size();
        // set the field null
        bugListDefault.setDescription(null);

        // Create the BugListDefault, which fails.
        BugListDefaultDTO bugListDefaultDTO = bugListDefaultMapper.toDto(bugListDefault);

        restBugListDefaultMockMvc.perform(post("/api/bug-list-defaults")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bugListDefaultDTO)))
            .andExpect(status().isBadRequest());

        List<BugListDefault> bugListDefaultList = bugListDefaultRepository.findAll();
        assertThat(bugListDefaultList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBugListDefaults() throws Exception {
        // Initialize the database
        bugListDefaultRepository.saveAndFlush(bugListDefault);

        // Get all the bugListDefaultList
        restBugListDefaultMockMvc.perform(get("/api/bug-list-defaults?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bugListDefault.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.booleanValue())));
    }

    @Test
    @Transactional
    public void getBugListDefault() throws Exception {
        // Initialize the database
        bugListDefaultRepository.saveAndFlush(bugListDefault);

        // Get the bugListDefault
        restBugListDefaultMockMvc.perform(get("/api/bug-list-defaults/{id}", bugListDefault.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(bugListDefault.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.booleanValue()));
    }

    @Test
    @Transactional
    public void getAllBugListDefaultsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        bugListDefaultRepository.saveAndFlush(bugListDefault);

        // Get all the bugListDefaultList where description equals to DEFAULT_DESCRIPTION
        defaultBugListDefaultShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the bugListDefaultList where description equals to UPDATED_DESCRIPTION
        defaultBugListDefaultShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllBugListDefaultsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        bugListDefaultRepository.saveAndFlush(bugListDefault);

        // Get all the bugListDefaultList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultBugListDefaultShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the bugListDefaultList where description equals to UPDATED_DESCRIPTION
        defaultBugListDefaultShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllBugListDefaultsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        bugListDefaultRepository.saveAndFlush(bugListDefault);

        // Get all the bugListDefaultList where description is not null
        defaultBugListDefaultShouldBeFound("description.specified=true");

        // Get all the bugListDefaultList where description is null
        defaultBugListDefaultShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    public void getAllBugListDefaultsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        bugListDefaultRepository.saveAndFlush(bugListDefault);

        // Get all the bugListDefaultList where status equals to DEFAULT_STATUS
        defaultBugListDefaultShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the bugListDefaultList where status equals to UPDATED_STATUS
        defaultBugListDefaultShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllBugListDefaultsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        bugListDefaultRepository.saveAndFlush(bugListDefault);

        // Get all the bugListDefaultList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultBugListDefaultShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the bugListDefaultList where status equals to UPDATED_STATUS
        defaultBugListDefaultShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllBugListDefaultsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        bugListDefaultRepository.saveAndFlush(bugListDefault);

        // Get all the bugListDefaultList where status is not null
        defaultBugListDefaultShouldBeFound("status.specified=true");

        // Get all the bugListDefaultList where status is null
        defaultBugListDefaultShouldNotBeFound("status.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultBugListDefaultShouldBeFound(String filter) throws Exception {
        restBugListDefaultMockMvc.perform(get("/api/bug-list-defaults?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bugListDefault.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.booleanValue())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultBugListDefaultShouldNotBeFound(String filter) throws Exception {
        restBugListDefaultMockMvc.perform(get("/api/bug-list-defaults?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingBugListDefault() throws Exception {
        // Get the bugListDefault
        restBugListDefaultMockMvc.perform(get("/api/bug-list-defaults/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBugListDefault() throws Exception {
        // Initialize the database
        bugListDefaultRepository.saveAndFlush(bugListDefault);
        bugListDefaultSearchRepository.save(bugListDefault);
        int databaseSizeBeforeUpdate = bugListDefaultRepository.findAll().size();

        // Update the bugListDefault
        BugListDefault updatedBugListDefault = bugListDefaultRepository.findOne(bugListDefault.getId());
        // Disconnect from session so that the updates on updatedBugListDefault are not directly saved in db
        em.detach(updatedBugListDefault);
        updatedBugListDefault
            .description(UPDATED_DESCRIPTION)
            .status(UPDATED_STATUS);
        BugListDefaultDTO bugListDefaultDTO = bugListDefaultMapper.toDto(updatedBugListDefault);

        restBugListDefaultMockMvc.perform(put("/api/bug-list-defaults")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bugListDefaultDTO)))
            .andExpect(status().isOk());

        // Validate the BugListDefault in the database
        List<BugListDefault> bugListDefaultList = bugListDefaultRepository.findAll();
        assertThat(bugListDefaultList).hasSize(databaseSizeBeforeUpdate);
        BugListDefault testBugListDefault = bugListDefaultList.get(bugListDefaultList.size() - 1);
        assertThat(testBugListDefault.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testBugListDefault.isStatus()).isEqualTo(UPDATED_STATUS);

        // Validate the BugListDefault in Elasticsearch
        BugListDefault bugListDefaultEs = bugListDefaultSearchRepository.findOne(testBugListDefault.getId());
        assertThat(bugListDefaultEs).isEqualToIgnoringGivenFields(testBugListDefault);
    }

    @Test
    @Transactional
    public void updateNonExistingBugListDefault() throws Exception {
        int databaseSizeBeforeUpdate = bugListDefaultRepository.findAll().size();

        // Create the BugListDefault
        BugListDefaultDTO bugListDefaultDTO = bugListDefaultMapper.toDto(bugListDefault);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restBugListDefaultMockMvc.perform(put("/api/bug-list-defaults")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bugListDefaultDTO)))
            .andExpect(status().isCreated());

        // Validate the BugListDefault in the database
        List<BugListDefault> bugListDefaultList = bugListDefaultRepository.findAll();
        assertThat(bugListDefaultList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteBugListDefault() throws Exception {
        // Initialize the database
        bugListDefaultRepository.saveAndFlush(bugListDefault);
        bugListDefaultSearchRepository.save(bugListDefault);
        int databaseSizeBeforeDelete = bugListDefaultRepository.findAll().size();

        // Get the bugListDefault
        restBugListDefaultMockMvc.perform(delete("/api/bug-list-defaults/{id}", bugListDefault.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean bugListDefaultExistsInEs = bugListDefaultSearchRepository.exists(bugListDefault.getId());
        assertThat(bugListDefaultExistsInEs).isFalse();

        // Validate the database is empty
        List<BugListDefault> bugListDefaultList = bugListDefaultRepository.findAll();
        assertThat(bugListDefaultList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchBugListDefault() throws Exception {
        // Initialize the database
        bugListDefaultRepository.saveAndFlush(bugListDefault);
        bugListDefaultSearchRepository.save(bugListDefault);

        // Search the bugListDefault
        restBugListDefaultMockMvc.perform(get("/api/_search/bug-list-defaults?query=id:" + bugListDefault.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bugListDefault.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.booleanValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BugListDefault.class);
        BugListDefault bugListDefault1 = new BugListDefault();
        bugListDefault1.setId(1L);
        BugListDefault bugListDefault2 = new BugListDefault();
        bugListDefault2.setId(bugListDefault1.getId());
        assertThat(bugListDefault1).isEqualTo(bugListDefault2);
        bugListDefault2.setId(2L);
        assertThat(bugListDefault1).isNotEqualTo(bugListDefault2);
        bugListDefault1.setId(null);
        assertThat(bugListDefault1).isNotEqualTo(bugListDefault2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BugListDefaultDTO.class);
        BugListDefaultDTO bugListDefaultDTO1 = new BugListDefaultDTO();
        bugListDefaultDTO1.setId(1L);
        BugListDefaultDTO bugListDefaultDTO2 = new BugListDefaultDTO();
        assertThat(bugListDefaultDTO1).isNotEqualTo(bugListDefaultDTO2);
        bugListDefaultDTO2.setId(bugListDefaultDTO1.getId());
        assertThat(bugListDefaultDTO1).isEqualTo(bugListDefaultDTO2);
        bugListDefaultDTO2.setId(2L);
        assertThat(bugListDefaultDTO1).isNotEqualTo(bugListDefaultDTO2);
        bugListDefaultDTO1.setId(null);
        assertThat(bugListDefaultDTO1).isNotEqualTo(bugListDefaultDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(bugListDefaultMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(bugListDefaultMapper.fromId(null)).isNull();
    }
}
