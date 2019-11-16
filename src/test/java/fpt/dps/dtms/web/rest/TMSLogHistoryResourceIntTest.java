package fpt.dps.dtms.web.rest;

import fpt.dps.dtms.DtmsApp;

import fpt.dps.dtms.domain.TMSLogHistory;
import fpt.dps.dtms.domain.Projects;
import fpt.dps.dtms.domain.PurchaseOrders;
import fpt.dps.dtms.domain.Packages;
import fpt.dps.dtms.domain.Tasks;
import fpt.dps.dtms.repository.TMSLogHistoryRepository;
import fpt.dps.dtms.service.TMSLogHistoryService;
import fpt.dps.dtms.repository.search.TMSLogHistorySearchRepository;
import fpt.dps.dtms.service.dto.TMSLogHistoryDTO;
import fpt.dps.dtms.service.mapper.TMSLogHistoryMapper;
import fpt.dps.dtms.web.rest.errors.ExceptionTranslator;
import fpt.dps.dtms.service.dto.TMSLogHistoryCriteria;
import fpt.dps.dtms.service.TMSLogHistoryQueryService;

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
 * Test class for the TMSLogHistoryResource REST controller.
 *
 * @see TMSLogHistoryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DtmsApp.class)
public class TMSLogHistoryResourceIntTest {

    private static final String DEFAULT_ACTION = "AAAAAAAAAA";
    private static final String UPDATED_ACTION = "BBBBBBBBBB";

    private static final String DEFAULT_OLD_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_OLD_VALUE = "BBBBBBBBBB";

    private static final String DEFAULT_NEW_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_NEW_VALUE = "BBBBBBBBBB";

    @Autowired
    private TMSLogHistoryRepository tMSLogHistoryRepository;

    @Autowired
    private TMSLogHistoryMapper tMSLogHistoryMapper;

    @Autowired
    private TMSLogHistoryService tMSLogHistoryService;

    @Autowired
    private TMSLogHistorySearchRepository tMSLogHistorySearchRepository;

    @Autowired
    private TMSLogHistoryQueryService tMSLogHistoryQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTMSLogHistoryMockMvc;

    private TMSLogHistory tMSLogHistory;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TMSLogHistoryResource tMSLogHistoryResource = new TMSLogHistoryResource(tMSLogHistoryService, tMSLogHistoryQueryService);
        this.restTMSLogHistoryMockMvc = MockMvcBuilders.standaloneSetup(tMSLogHistoryResource)
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
    public static TMSLogHistory createEntity(EntityManager em) {
        TMSLogHistory tMSLogHistory = new TMSLogHistory()
            .action(DEFAULT_ACTION)
            .oldValue(DEFAULT_OLD_VALUE)
            .newValue(DEFAULT_NEW_VALUE);
        return tMSLogHistory;
    }

    @Before
    public void initTest() {
        tMSLogHistorySearchRepository.deleteAll();
        tMSLogHistory = createEntity(em);
    }

    @Test
    @Transactional
    public void createTMSLogHistory() throws Exception {
        int databaseSizeBeforeCreate = tMSLogHistoryRepository.findAll().size();

        // Create the TMSLogHistory
        TMSLogHistoryDTO tMSLogHistoryDTO = tMSLogHistoryMapper.toDto(tMSLogHistory);
        restTMSLogHistoryMockMvc.perform(post("/api/tms-log-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tMSLogHistoryDTO)))
            .andExpect(status().isCreated());

        // Validate the TMSLogHistory in the database
        List<TMSLogHistory> tMSLogHistoryList = tMSLogHistoryRepository.findAll();
        assertThat(tMSLogHistoryList).hasSize(databaseSizeBeforeCreate + 1);
        TMSLogHistory testTMSLogHistory = tMSLogHistoryList.get(tMSLogHistoryList.size() - 1);
        assertThat(testTMSLogHistory.getAction()).isEqualTo(DEFAULT_ACTION);
        assertThat(testTMSLogHistory.getOldValue()).isEqualTo(DEFAULT_OLD_VALUE);
        assertThat(testTMSLogHistory.getNewValue()).isEqualTo(DEFAULT_NEW_VALUE);

        // Validate the TMSLogHistory in Elasticsearch
        TMSLogHistory tMSLogHistoryEs = tMSLogHistorySearchRepository.findOne(testTMSLogHistory.getId());
        assertThat(tMSLogHistoryEs).isEqualToIgnoringGivenFields(testTMSLogHistory);
    }

    @Test
    @Transactional
    public void createTMSLogHistoryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = tMSLogHistoryRepository.findAll().size();

        // Create the TMSLogHistory with an existing ID
        tMSLogHistory.setId(1L);
        TMSLogHistoryDTO tMSLogHistoryDTO = tMSLogHistoryMapper.toDto(tMSLogHistory);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTMSLogHistoryMockMvc.perform(post("/api/tms-log-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tMSLogHistoryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TMSLogHistory in the database
        List<TMSLogHistory> tMSLogHistoryList = tMSLogHistoryRepository.findAll();
        assertThat(tMSLogHistoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllTMSLogHistories() throws Exception {
        // Initialize the database
        tMSLogHistoryRepository.saveAndFlush(tMSLogHistory);

        // Get all the tMSLogHistoryList
        restTMSLogHistoryMockMvc.perform(get("/api/tms-log-histories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tMSLogHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].action").value(hasItem(DEFAULT_ACTION.toString())))
            .andExpect(jsonPath("$.[*].oldValue").value(hasItem(DEFAULT_OLD_VALUE.toString())))
            .andExpect(jsonPath("$.[*].newValue").value(hasItem(DEFAULT_NEW_VALUE.toString())));
    }

    @Test
    @Transactional
    public void getTMSLogHistory() throws Exception {
        // Initialize the database
        tMSLogHistoryRepository.saveAndFlush(tMSLogHistory);

        // Get the tMSLogHistory
        restTMSLogHistoryMockMvc.perform(get("/api/tms-log-histories/{id}", tMSLogHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(tMSLogHistory.getId().intValue()))
            .andExpect(jsonPath("$.action").value(DEFAULT_ACTION.toString()))
            .andExpect(jsonPath("$.oldValue").value(DEFAULT_OLD_VALUE.toString()))
            .andExpect(jsonPath("$.newValue").value(DEFAULT_NEW_VALUE.toString()));
    }

    @Test
    @Transactional
    public void getAllTMSLogHistoriesByActionIsEqualToSomething() throws Exception {
        // Initialize the database
        tMSLogHistoryRepository.saveAndFlush(tMSLogHistory);

        // Get all the tMSLogHistoryList where action equals to DEFAULT_ACTION
        defaultTMSLogHistoryShouldBeFound("action.equals=" + DEFAULT_ACTION);

        // Get all the tMSLogHistoryList where action equals to UPDATED_ACTION
        defaultTMSLogHistoryShouldNotBeFound("action.equals=" + UPDATED_ACTION);
    }

    @Test
    @Transactional
    public void getAllTMSLogHistoriesByActionIsInShouldWork() throws Exception {
        // Initialize the database
        tMSLogHistoryRepository.saveAndFlush(tMSLogHistory);

        // Get all the tMSLogHistoryList where action in DEFAULT_ACTION or UPDATED_ACTION
        defaultTMSLogHistoryShouldBeFound("action.in=" + DEFAULT_ACTION + "," + UPDATED_ACTION);

        // Get all the tMSLogHistoryList where action equals to UPDATED_ACTION
        defaultTMSLogHistoryShouldNotBeFound("action.in=" + UPDATED_ACTION);
    }

    @Test
    @Transactional
    public void getAllTMSLogHistoriesByActionIsNullOrNotNull() throws Exception {
        // Initialize the database
        tMSLogHistoryRepository.saveAndFlush(tMSLogHistory);

        // Get all the tMSLogHistoryList where action is not null
        defaultTMSLogHistoryShouldBeFound("action.specified=true");

        // Get all the tMSLogHistoryList where action is null
        defaultTMSLogHistoryShouldNotBeFound("action.specified=false");
    }

    @Test
    @Transactional
    public void getAllTMSLogHistoriesByProjectsIsEqualToSomething() throws Exception {
        // Initialize the database
        Projects projects = ProjectsResourceIntTest.createEntity(em);
        em.persist(projects);
        em.flush();
        tMSLogHistory.setProjects(projects);
        tMSLogHistoryRepository.saveAndFlush(tMSLogHistory);
        Long projectsId = projects.getId();

        // Get all the tMSLogHistoryList where projects equals to projectsId
        defaultTMSLogHistoryShouldBeFound("projectsId.equals=" + projectsId);

        // Get all the tMSLogHistoryList where projects equals to projectsId + 1
        defaultTMSLogHistoryShouldNotBeFound("projectsId.equals=" + (projectsId + 1));
    }


    @Test
    @Transactional
    public void getAllTMSLogHistoriesByPurchaseOrdersIsEqualToSomething() throws Exception {
        // Initialize the database
        PurchaseOrders purchaseOrders = PurchaseOrdersResourceIntTest.createEntity(em);
        em.persist(purchaseOrders);
        em.flush();
        tMSLogHistory.setPurchaseOrders(purchaseOrders);
        tMSLogHistoryRepository.saveAndFlush(tMSLogHistory);
        Long purchaseOrdersId = purchaseOrders.getId();

        // Get all the tMSLogHistoryList where purchaseOrders equals to purchaseOrdersId
        defaultTMSLogHistoryShouldBeFound("purchaseOrdersId.equals=" + purchaseOrdersId);

        // Get all the tMSLogHistoryList where purchaseOrders equals to purchaseOrdersId + 1
        defaultTMSLogHistoryShouldNotBeFound("purchaseOrdersId.equals=" + (purchaseOrdersId + 1));
    }


    @Test
    @Transactional
    public void getAllTMSLogHistoriesByPackagesIsEqualToSomething() throws Exception {
        // Initialize the database
        Packages packages = PackagesResourceIntTest.createEntity(em);
        em.persist(packages);
        em.flush();
        tMSLogHistory.setPackages(packages);
        tMSLogHistoryRepository.saveAndFlush(tMSLogHistory);
        Long packagesId = packages.getId();

        // Get all the tMSLogHistoryList where packages equals to packagesId
        defaultTMSLogHistoryShouldBeFound("packagesId.equals=" + packagesId);

        // Get all the tMSLogHistoryList where packages equals to packagesId + 1
        defaultTMSLogHistoryShouldNotBeFound("packagesId.equals=" + (packagesId + 1));
    }


    @Test
    @Transactional
    public void getAllTMSLogHistoriesByTasksIsEqualToSomething() throws Exception {
        // Initialize the database
        Tasks tasks = TasksResourceIntTest.createEntity(em);
        em.persist(tasks);
        em.flush();
        tMSLogHistory.setTasks(tasks);
        tMSLogHistoryRepository.saveAndFlush(tMSLogHistory);
        Long tasksId = tasks.getId();

        // Get all the tMSLogHistoryList where tasks equals to tasksId
        defaultTMSLogHistoryShouldBeFound("tasksId.equals=" + tasksId);

        // Get all the tMSLogHistoryList where tasks equals to tasksId + 1
        defaultTMSLogHistoryShouldNotBeFound("tasksId.equals=" + (tasksId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultTMSLogHistoryShouldBeFound(String filter) throws Exception {
        restTMSLogHistoryMockMvc.perform(get("/api/tms-log-histories?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tMSLogHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].action").value(hasItem(DEFAULT_ACTION.toString())))
            .andExpect(jsonPath("$.[*].oldValue").value(hasItem(DEFAULT_OLD_VALUE.toString())))
            .andExpect(jsonPath("$.[*].newValue").value(hasItem(DEFAULT_NEW_VALUE.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultTMSLogHistoryShouldNotBeFound(String filter) throws Exception {
        restTMSLogHistoryMockMvc.perform(get("/api/tms-log-histories?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingTMSLogHistory() throws Exception {
        // Get the tMSLogHistory
        restTMSLogHistoryMockMvc.perform(get("/api/tms-log-histories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTMSLogHistory() throws Exception {
        // Initialize the database
        tMSLogHistoryRepository.saveAndFlush(tMSLogHistory);
        tMSLogHistorySearchRepository.save(tMSLogHistory);
        int databaseSizeBeforeUpdate = tMSLogHistoryRepository.findAll().size();

        // Update the tMSLogHistory
        TMSLogHistory updatedTMSLogHistory = tMSLogHistoryRepository.findOne(tMSLogHistory.getId());
        // Disconnect from session so that the updates on updatedTMSLogHistory are not directly saved in db
        em.detach(updatedTMSLogHistory);
        updatedTMSLogHistory
            .action(UPDATED_ACTION)
            .oldValue(UPDATED_OLD_VALUE)
            .newValue(UPDATED_NEW_VALUE);
        TMSLogHistoryDTO tMSLogHistoryDTO = tMSLogHistoryMapper.toDto(updatedTMSLogHistory);

        restTMSLogHistoryMockMvc.perform(put("/api/tms-log-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tMSLogHistoryDTO)))
            .andExpect(status().isOk());

        // Validate the TMSLogHistory in the database
        List<TMSLogHistory> tMSLogHistoryList = tMSLogHistoryRepository.findAll();
        assertThat(tMSLogHistoryList).hasSize(databaseSizeBeforeUpdate);
        TMSLogHistory testTMSLogHistory = tMSLogHistoryList.get(tMSLogHistoryList.size() - 1);
        assertThat(testTMSLogHistory.getAction()).isEqualTo(UPDATED_ACTION);
        assertThat(testTMSLogHistory.getOldValue()).isEqualTo(UPDATED_OLD_VALUE);
        assertThat(testTMSLogHistory.getNewValue()).isEqualTo(UPDATED_NEW_VALUE);

        // Validate the TMSLogHistory in Elasticsearch
        TMSLogHistory tMSLogHistoryEs = tMSLogHistorySearchRepository.findOne(testTMSLogHistory.getId());
        assertThat(tMSLogHistoryEs).isEqualToIgnoringGivenFields(testTMSLogHistory);
    }

    @Test
    @Transactional
    public void updateNonExistingTMSLogHistory() throws Exception {
        int databaseSizeBeforeUpdate = tMSLogHistoryRepository.findAll().size();

        // Create the TMSLogHistory
        TMSLogHistoryDTO tMSLogHistoryDTO = tMSLogHistoryMapper.toDto(tMSLogHistory);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTMSLogHistoryMockMvc.perform(put("/api/tms-log-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tMSLogHistoryDTO)))
            .andExpect(status().isCreated());

        // Validate the TMSLogHistory in the database
        List<TMSLogHistory> tMSLogHistoryList = tMSLogHistoryRepository.findAll();
        assertThat(tMSLogHistoryList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTMSLogHistory() throws Exception {
        // Initialize the database
        tMSLogHistoryRepository.saveAndFlush(tMSLogHistory);
        tMSLogHistorySearchRepository.save(tMSLogHistory);
        int databaseSizeBeforeDelete = tMSLogHistoryRepository.findAll().size();

        // Get the tMSLogHistory
        restTMSLogHistoryMockMvc.perform(delete("/api/tms-log-histories/{id}", tMSLogHistory.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean tMSLogHistoryExistsInEs = tMSLogHistorySearchRepository.exists(tMSLogHistory.getId());
        assertThat(tMSLogHistoryExistsInEs).isFalse();

        // Validate the database is empty
        List<TMSLogHistory> tMSLogHistoryList = tMSLogHistoryRepository.findAll();
        assertThat(tMSLogHistoryList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchTMSLogHistory() throws Exception {
        // Initialize the database
        tMSLogHistoryRepository.saveAndFlush(tMSLogHistory);
        tMSLogHistorySearchRepository.save(tMSLogHistory);

        // Search the tMSLogHistory
        restTMSLogHistoryMockMvc.perform(get("/api/_search/tms-log-histories?query=id:" + tMSLogHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tMSLogHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].action").value(hasItem(DEFAULT_ACTION.toString())))
            .andExpect(jsonPath("$.[*].oldValue").value(hasItem(DEFAULT_OLD_VALUE.toString())))
            .andExpect(jsonPath("$.[*].newValue").value(hasItem(DEFAULT_NEW_VALUE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TMSLogHistory.class);
        TMSLogHistory tMSLogHistory1 = new TMSLogHistory();
        tMSLogHistory1.setId(1L);
        TMSLogHistory tMSLogHistory2 = new TMSLogHistory();
        tMSLogHistory2.setId(tMSLogHistory1.getId());
        assertThat(tMSLogHistory1).isEqualTo(tMSLogHistory2);
        tMSLogHistory2.setId(2L);
        assertThat(tMSLogHistory1).isNotEqualTo(tMSLogHistory2);
        tMSLogHistory1.setId(null);
        assertThat(tMSLogHistory1).isNotEqualTo(tMSLogHistory2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TMSLogHistoryDTO.class);
        TMSLogHistoryDTO tMSLogHistoryDTO1 = new TMSLogHistoryDTO();
        tMSLogHistoryDTO1.setId(1L);
        TMSLogHistoryDTO tMSLogHistoryDTO2 = new TMSLogHistoryDTO();
        assertThat(tMSLogHistoryDTO1).isNotEqualTo(tMSLogHistoryDTO2);
        tMSLogHistoryDTO2.setId(tMSLogHistoryDTO1.getId());
        assertThat(tMSLogHistoryDTO1).isEqualTo(tMSLogHistoryDTO2);
        tMSLogHistoryDTO2.setId(2L);
        assertThat(tMSLogHistoryDTO1).isNotEqualTo(tMSLogHistoryDTO2);
        tMSLogHistoryDTO1.setId(null);
        assertThat(tMSLogHistoryDTO1).isNotEqualTo(tMSLogHistoryDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(tMSLogHistoryMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(tMSLogHistoryMapper.fromId(null)).isNull();
    }
}
