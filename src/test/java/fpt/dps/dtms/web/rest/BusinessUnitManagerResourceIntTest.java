package fpt.dps.dtms.web.rest;

import fpt.dps.dtms.DtmsApp;

import fpt.dps.dtms.domain.BusinessUnitManager;
import fpt.dps.dtms.domain.BusinessUnit;
import fpt.dps.dtms.domain.User;
import fpt.dps.dtms.repository.BusinessUnitManagerRepository;
import fpt.dps.dtms.service.BusinessUnitManagerService;
import fpt.dps.dtms.repository.search.BusinessUnitManagerSearchRepository;
import fpt.dps.dtms.service.dto.BusinessUnitManagerDTO;
import fpt.dps.dtms.service.mapper.BusinessUnitManagerMapper;
import fpt.dps.dtms.web.rest.errors.ExceptionTranslator;
import fpt.dps.dtms.service.dto.BusinessUnitManagerCriteria;
import fpt.dps.dtms.service.BusinessUnitManagerQueryService;

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
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static fpt.dps.dtms.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the BusinessUnitManagerResource REST controller.
 *
 * @see BusinessUnitManagerResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DtmsApp.class)
public class BusinessUnitManagerResourceIntTest {

    private static final LocalDate DEFAULT_START_TIME = LocalDate.now();
    private static final LocalDate UPDATED_START_TIME = LocalDate.now();

    private static final LocalDate DEFAULT_END_TIME = LocalDate.now();
    private static final LocalDate UPDATED_END_TIME = LocalDate.now();

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private BusinessUnitManagerRepository businessUnitManagerRepository;

    @Autowired
    private BusinessUnitManagerMapper businessUnitManagerMapper;

    @Autowired
    private BusinessUnitManagerService businessUnitManagerService;

    @Autowired
    private BusinessUnitManagerSearchRepository businessUnitManagerSearchRepository;

    @Autowired
    private BusinessUnitManagerQueryService businessUnitManagerQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restBusinessUnitManagerMockMvc;

    private BusinessUnitManager businessUnitManager;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final BusinessUnitManagerResource businessUnitManagerResource = new BusinessUnitManagerResource(businessUnitManagerService, businessUnitManagerQueryService);
        this.restBusinessUnitManagerMockMvc = MockMvcBuilders.standaloneSetup(businessUnitManagerResource)
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
    public static BusinessUnitManager createEntity(EntityManager em) {
        BusinessUnitManager businessUnitManager = new BusinessUnitManager()
            .startTime(DEFAULT_START_TIME)
            .endTime(DEFAULT_END_TIME)
            .description(DEFAULT_DESCRIPTION);
        // Add required entity
        BusinessUnit businessUnit = BusinessUnitResourceIntTest.createEntity(em);
        em.persist(businessUnit);
        em.flush();
        businessUnitManager.setBusinessUnit(businessUnit);
        // Add required entity
        User manager = UserResourceIntTest.createEntity(em);
        em.persist(manager);
        em.flush();
        businessUnitManager.setManager(manager);
        return businessUnitManager;
    }

    @Before
    public void initTest() {
        businessUnitManagerSearchRepository.deleteAll();
        businessUnitManager = createEntity(em);
    }

    @Test
    @Transactional
    public void createBusinessUnitManager() throws Exception {
        int databaseSizeBeforeCreate = businessUnitManagerRepository.findAll().size();

        // Create the BusinessUnitManager
        BusinessUnitManagerDTO businessUnitManagerDTO = businessUnitManagerMapper.toDto(businessUnitManager);
        restBusinessUnitManagerMockMvc.perform(post("/api/business-unit-managers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(businessUnitManagerDTO)))
            .andExpect(status().isCreated());

        // Validate the BusinessUnitManager in the database
        List<BusinessUnitManager> businessUnitManagerList = businessUnitManagerRepository.findAll();
        assertThat(businessUnitManagerList).hasSize(databaseSizeBeforeCreate + 1);
        BusinessUnitManager testBusinessUnitManager = businessUnitManagerList.get(businessUnitManagerList.size() - 1);
        assertThat(testBusinessUnitManager.getStartTime()).isEqualTo(DEFAULT_START_TIME);
        assertThat(testBusinessUnitManager.getEndTime()).isEqualTo(DEFAULT_END_TIME);
        assertThat(testBusinessUnitManager.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the BusinessUnitManager in Elasticsearch
        BusinessUnitManager businessUnitManagerEs = businessUnitManagerSearchRepository.findOne(testBusinessUnitManager.getId());
        assertThat(businessUnitManagerEs).isEqualToIgnoringGivenFields(testBusinessUnitManager);
    }

    @Test
    @Transactional
    public void createBusinessUnitManagerWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = businessUnitManagerRepository.findAll().size();

        // Create the BusinessUnitManager with an existing ID
        businessUnitManager.setId(1L);
        BusinessUnitManagerDTO businessUnitManagerDTO = businessUnitManagerMapper.toDto(businessUnitManager);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBusinessUnitManagerMockMvc.perform(post("/api/business-unit-managers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(businessUnitManagerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the BusinessUnitManager in the database
        List<BusinessUnitManager> businessUnitManagerList = businessUnitManagerRepository.findAll();
        assertThat(businessUnitManagerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllBusinessUnitManagers() throws Exception {
        // Initialize the database
        businessUnitManagerRepository.saveAndFlush(businessUnitManager);

        // Get all the businessUnitManagerList
        restBusinessUnitManagerMockMvc.perform(get("/api/business-unit-managers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(businessUnitManager.getId().intValue())))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME.toString())))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(DEFAULT_END_TIME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getBusinessUnitManager() throws Exception {
        // Initialize the database
        businessUnitManagerRepository.saveAndFlush(businessUnitManager);

        // Get the businessUnitManager
        restBusinessUnitManagerMockMvc.perform(get("/api/business-unit-managers/{id}", businessUnitManager.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(businessUnitManager.getId().intValue()))
            .andExpect(jsonPath("$.startTime").value(DEFAULT_START_TIME.toString()))
            .andExpect(jsonPath("$.endTime").value(DEFAULT_END_TIME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getAllBusinessUnitManagersByStartTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        businessUnitManagerRepository.saveAndFlush(businessUnitManager);

        // Get all the businessUnitManagerList where startTime equals to DEFAULT_START_TIME
        defaultBusinessUnitManagerShouldBeFound("startTime.equals=" + DEFAULT_START_TIME);

        // Get all the businessUnitManagerList where startTime equals to UPDATED_START_TIME
        defaultBusinessUnitManagerShouldNotBeFound("startTime.equals=" + UPDATED_START_TIME);
    }

    @Test
    @Transactional
    public void getAllBusinessUnitManagersByStartTimeIsInShouldWork() throws Exception {
        // Initialize the database
        businessUnitManagerRepository.saveAndFlush(businessUnitManager);

        // Get all the businessUnitManagerList where startTime in DEFAULT_START_TIME or UPDATED_START_TIME
        defaultBusinessUnitManagerShouldBeFound("startTime.in=" + DEFAULT_START_TIME + "," + UPDATED_START_TIME);

        // Get all the businessUnitManagerList where startTime equals to UPDATED_START_TIME
        defaultBusinessUnitManagerShouldNotBeFound("startTime.in=" + UPDATED_START_TIME);
    }

    @Test
    @Transactional
    public void getAllBusinessUnitManagersByStartTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        businessUnitManagerRepository.saveAndFlush(businessUnitManager);

        // Get all the businessUnitManagerList where startTime is not null
        defaultBusinessUnitManagerShouldBeFound("startTime.specified=true");

        // Get all the businessUnitManagerList where startTime is null
        defaultBusinessUnitManagerShouldNotBeFound("startTime.specified=false");
    }

    @Test
    @Transactional
    public void getAllBusinessUnitManagersByEndTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        businessUnitManagerRepository.saveAndFlush(businessUnitManager);

        // Get all the businessUnitManagerList where endTime equals to DEFAULT_END_TIME
        defaultBusinessUnitManagerShouldBeFound("endTime.equals=" + DEFAULT_END_TIME);

        // Get all the businessUnitManagerList where endTime equals to UPDATED_END_TIME
        defaultBusinessUnitManagerShouldNotBeFound("endTime.equals=" + UPDATED_END_TIME);
    }

    @Test
    @Transactional
    public void getAllBusinessUnitManagersByEndTimeIsInShouldWork() throws Exception {
        // Initialize the database
        businessUnitManagerRepository.saveAndFlush(businessUnitManager);

        // Get all the businessUnitManagerList where endTime in DEFAULT_END_TIME or UPDATED_END_TIME
        defaultBusinessUnitManagerShouldBeFound("endTime.in=" + DEFAULT_END_TIME + "," + UPDATED_END_TIME);

        // Get all the businessUnitManagerList where endTime equals to UPDATED_END_TIME
        defaultBusinessUnitManagerShouldNotBeFound("endTime.in=" + UPDATED_END_TIME);
    }

    @Test
    @Transactional
    public void getAllBusinessUnitManagersByEndTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        businessUnitManagerRepository.saveAndFlush(businessUnitManager);

        // Get all the businessUnitManagerList where endTime is not null
        defaultBusinessUnitManagerShouldBeFound("endTime.specified=true");

        // Get all the businessUnitManagerList where endTime is null
        defaultBusinessUnitManagerShouldNotBeFound("endTime.specified=false");
    }

    @Test
    @Transactional
    public void getAllBusinessUnitManagersByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        businessUnitManagerRepository.saveAndFlush(businessUnitManager);

        // Get all the businessUnitManagerList where description equals to DEFAULT_DESCRIPTION
        defaultBusinessUnitManagerShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the businessUnitManagerList where description equals to UPDATED_DESCRIPTION
        defaultBusinessUnitManagerShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllBusinessUnitManagersByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        businessUnitManagerRepository.saveAndFlush(businessUnitManager);

        // Get all the businessUnitManagerList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultBusinessUnitManagerShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the businessUnitManagerList where description equals to UPDATED_DESCRIPTION
        defaultBusinessUnitManagerShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllBusinessUnitManagersByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        businessUnitManagerRepository.saveAndFlush(businessUnitManager);

        // Get all the businessUnitManagerList where description is not null
        defaultBusinessUnitManagerShouldBeFound("description.specified=true");

        // Get all the businessUnitManagerList where description is null
        defaultBusinessUnitManagerShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    public void getAllBusinessUnitManagersByBusinessUnitIsEqualToSomething() throws Exception {
        // Initialize the database
        BusinessUnit businessUnit = BusinessUnitResourceIntTest.createEntity(em);
        em.persist(businessUnit);
        em.flush();
        businessUnitManager.setBusinessUnit(businessUnit);
        businessUnitManagerRepository.saveAndFlush(businessUnitManager);
        Long businessUnitId = businessUnit.getId();

        // Get all the businessUnitManagerList where businessUnit equals to businessUnitId
        defaultBusinessUnitManagerShouldBeFound("businessUnitId.equals=" + businessUnitId);

        // Get all the businessUnitManagerList where businessUnit equals to businessUnitId + 1
        defaultBusinessUnitManagerShouldNotBeFound("businessUnitId.equals=" + (businessUnitId + 1));
    }


    @Test
    @Transactional
    public void getAllBusinessUnitManagersByManagerIsEqualToSomething() throws Exception {
        // Initialize the database
        User manager = UserResourceIntTest.createEntity(em);
        em.persist(manager);
        em.flush();
        businessUnitManager.setManager(manager);
        businessUnitManagerRepository.saveAndFlush(businessUnitManager);
        Long managerId = manager.getId();

        // Get all the businessUnitManagerList where manager equals to managerId
        defaultBusinessUnitManagerShouldBeFound("managerId.equals=" + managerId);

        // Get all the businessUnitManagerList where manager equals to managerId + 1
        defaultBusinessUnitManagerShouldNotBeFound("managerId.equals=" + (managerId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultBusinessUnitManagerShouldBeFound(String filter) throws Exception {
        restBusinessUnitManagerMockMvc.perform(get("/api/business-unit-managers?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(businessUnitManager.getId().intValue())))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME.toString())))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(DEFAULT_END_TIME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultBusinessUnitManagerShouldNotBeFound(String filter) throws Exception {
        restBusinessUnitManagerMockMvc.perform(get("/api/business-unit-managers?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingBusinessUnitManager() throws Exception {
        // Get the businessUnitManager
        restBusinessUnitManagerMockMvc.perform(get("/api/business-unit-managers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBusinessUnitManager() throws Exception {
        // Initialize the database
        businessUnitManagerRepository.saveAndFlush(businessUnitManager);
        businessUnitManagerSearchRepository.save(businessUnitManager);
        int databaseSizeBeforeUpdate = businessUnitManagerRepository.findAll().size();

        // Update the businessUnitManager
        BusinessUnitManager updatedBusinessUnitManager = businessUnitManagerRepository.findOne(businessUnitManager.getId());
        // Disconnect from session so that the updates on updatedBusinessUnitManager are not directly saved in db
        em.detach(updatedBusinessUnitManager);
        updatedBusinessUnitManager
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME)
            .description(UPDATED_DESCRIPTION);
        BusinessUnitManagerDTO businessUnitManagerDTO = businessUnitManagerMapper.toDto(updatedBusinessUnitManager);

        restBusinessUnitManagerMockMvc.perform(put("/api/business-unit-managers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(businessUnitManagerDTO)))
            .andExpect(status().isOk());

        // Validate the BusinessUnitManager in the database
        List<BusinessUnitManager> businessUnitManagerList = businessUnitManagerRepository.findAll();
        assertThat(businessUnitManagerList).hasSize(databaseSizeBeforeUpdate);
        BusinessUnitManager testBusinessUnitManager = businessUnitManagerList.get(businessUnitManagerList.size() - 1);
        assertThat(testBusinessUnitManager.getStartTime()).isEqualTo(UPDATED_START_TIME);
        assertThat(testBusinessUnitManager.getEndTime()).isEqualTo(UPDATED_END_TIME);
        assertThat(testBusinessUnitManager.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the BusinessUnitManager in Elasticsearch
        BusinessUnitManager businessUnitManagerEs = businessUnitManagerSearchRepository.findOne(testBusinessUnitManager.getId());
        assertThat(businessUnitManagerEs).isEqualToIgnoringGivenFields(testBusinessUnitManager);
    }

    @Test
    @Transactional
    public void updateNonExistingBusinessUnitManager() throws Exception {
        int databaseSizeBeforeUpdate = businessUnitManagerRepository.findAll().size();

        // Create the BusinessUnitManager
        BusinessUnitManagerDTO businessUnitManagerDTO = businessUnitManagerMapper.toDto(businessUnitManager);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restBusinessUnitManagerMockMvc.perform(put("/api/business-unit-managers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(businessUnitManagerDTO)))
            .andExpect(status().isCreated());

        // Validate the BusinessUnitManager in the database
        List<BusinessUnitManager> businessUnitManagerList = businessUnitManagerRepository.findAll();
        assertThat(businessUnitManagerList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteBusinessUnitManager() throws Exception {
        // Initialize the database
        businessUnitManagerRepository.saveAndFlush(businessUnitManager);
        businessUnitManagerSearchRepository.save(businessUnitManager);
        int databaseSizeBeforeDelete = businessUnitManagerRepository.findAll().size();

        // Get the businessUnitManager
        restBusinessUnitManagerMockMvc.perform(delete("/api/business-unit-managers/{id}", businessUnitManager.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean businessUnitManagerExistsInEs = businessUnitManagerSearchRepository.exists(businessUnitManager.getId());
        assertThat(businessUnitManagerExistsInEs).isFalse();

        // Validate the database is empty
        List<BusinessUnitManager> businessUnitManagerList = businessUnitManagerRepository.findAll();
        assertThat(businessUnitManagerList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchBusinessUnitManager() throws Exception {
        // Initialize the database
        businessUnitManagerRepository.saveAndFlush(businessUnitManager);
        businessUnitManagerSearchRepository.save(businessUnitManager);

        // Search the businessUnitManager
        restBusinessUnitManagerMockMvc.perform(get("/api/_search/business-unit-managers?query=id:" + businessUnitManager.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(businessUnitManager.getId().intValue())))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME.toString())))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(DEFAULT_END_TIME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BusinessUnitManager.class);
        BusinessUnitManager businessUnitManager1 = new BusinessUnitManager();
        businessUnitManager1.setId(1L);
        BusinessUnitManager businessUnitManager2 = new BusinessUnitManager();
        businessUnitManager2.setId(businessUnitManager1.getId());
        assertThat(businessUnitManager1).isEqualTo(businessUnitManager2);
        businessUnitManager2.setId(2L);
        assertThat(businessUnitManager1).isNotEqualTo(businessUnitManager2);
        businessUnitManager1.setId(null);
        assertThat(businessUnitManager1).isNotEqualTo(businessUnitManager2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BusinessUnitManagerDTO.class);
        BusinessUnitManagerDTO businessUnitManagerDTO1 = new BusinessUnitManagerDTO();
        businessUnitManagerDTO1.setId(1L);
        BusinessUnitManagerDTO businessUnitManagerDTO2 = new BusinessUnitManagerDTO();
        assertThat(businessUnitManagerDTO1).isNotEqualTo(businessUnitManagerDTO2);
        businessUnitManagerDTO2.setId(businessUnitManagerDTO1.getId());
        assertThat(businessUnitManagerDTO1).isEqualTo(businessUnitManagerDTO2);
        businessUnitManagerDTO2.setId(2L);
        assertThat(businessUnitManagerDTO1).isNotEqualTo(businessUnitManagerDTO2);
        businessUnitManagerDTO1.setId(null);
        assertThat(businessUnitManagerDTO1).isNotEqualTo(businessUnitManagerDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(businessUnitManagerMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(businessUnitManagerMapper.fromId(null)).isNull();
    }
}
