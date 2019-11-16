package fpt.dps.dtms.web.rest;

import fpt.dps.dtms.DtmsApp;

import fpt.dps.dtms.domain.PurchaseOrders;
import fpt.dps.dtms.domain.TMSCustomFieldScreenValue;
import fpt.dps.dtms.domain.Projects;
import fpt.dps.dtms.domain.ProjectUsers;
import fpt.dps.dtms.domain.Packages;
import fpt.dps.dtms.repository.PurchaseOrdersRepository;
import fpt.dps.dtms.service.PurchaseOrdersService;
import fpt.dps.dtms.repository.search.PurchaseOrdersSearchRepository;
import fpt.dps.dtms.service.dto.PurchaseOrdersDTO;
import fpt.dps.dtms.service.mapper.PurchaseOrdersMapper;
import fpt.dps.dtms.web.rest.errors.ExceptionTranslator;
import fpt.dps.dtms.service.dto.PurchaseOrdersCriteria;
import fpt.dps.dtms.service.PurchaseOrdersQueryService;

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

import fpt.dps.dtms.domain.enumeration.PurchaseOrderStatus;
/**
 * Test class for the PurchaseOrdersResource REST controller.
 *
 * @see PurchaseOrdersResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DtmsApp.class)
public class PurchaseOrdersResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final PurchaseOrderStatus DEFAULT_STATUS = PurchaseOrderStatus.OPEN;
    private static final PurchaseOrderStatus UPDATED_STATUS = PurchaseOrderStatus.PROCESSING;

    private static final Instant DEFAULT_START_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private PurchaseOrdersRepository purchaseOrdersRepository;

    @Autowired
    private PurchaseOrdersMapper purchaseOrdersMapper;

    @Autowired
    private PurchaseOrdersService purchaseOrdersService;

    @Autowired
    private PurchaseOrdersSearchRepository purchaseOrdersSearchRepository;

    @Autowired
    private PurchaseOrdersQueryService purchaseOrdersQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPurchaseOrdersMockMvc;

    private PurchaseOrders purchaseOrders;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PurchaseOrdersResource purchaseOrdersResource = new PurchaseOrdersResource(purchaseOrdersService, purchaseOrdersQueryService, null);
        this.restPurchaseOrdersMockMvc = MockMvcBuilders.standaloneSetup(purchaseOrdersResource)
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
    public static PurchaseOrders createEntity(EntityManager em) {
        PurchaseOrders purchaseOrders = new PurchaseOrders()
            .name(DEFAULT_NAME)
            .status(DEFAULT_STATUS)
            .startTime(DEFAULT_START_TIME)
            .endTime(DEFAULT_END_TIME)
            .description(DEFAULT_DESCRIPTION);
        return purchaseOrders;
    }

    @Before
    public void initTest() {
        purchaseOrdersSearchRepository.deleteAll();
        purchaseOrders = createEntity(em);
    }

    @Test
    @Transactional
    public void createPurchaseOrders() throws Exception {
        int databaseSizeBeforeCreate = purchaseOrdersRepository.findAll().size();

        // Create the PurchaseOrders
        PurchaseOrdersDTO purchaseOrdersDTO = purchaseOrdersMapper.toDto(purchaseOrders);
        restPurchaseOrdersMockMvc.perform(post("/api/purchase-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(purchaseOrdersDTO)))
            .andExpect(status().isCreated());

        // Validate the PurchaseOrders in the database
        List<PurchaseOrders> purchaseOrdersList = purchaseOrdersRepository.findAll();
        assertThat(purchaseOrdersList).hasSize(databaseSizeBeforeCreate + 1);
        PurchaseOrders testPurchaseOrders = purchaseOrdersList.get(purchaseOrdersList.size() - 1);
        assertThat(testPurchaseOrders.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPurchaseOrders.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testPurchaseOrders.getStartTime()).isEqualTo(DEFAULT_START_TIME);
        assertThat(testPurchaseOrders.getEndTime()).isEqualTo(DEFAULT_END_TIME);
        assertThat(testPurchaseOrders.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the PurchaseOrders in Elasticsearch
        PurchaseOrders purchaseOrdersEs = purchaseOrdersSearchRepository.findOne(testPurchaseOrders.getId());
        assertThat(purchaseOrdersEs).isEqualToIgnoringGivenFields(testPurchaseOrders);
    }

    @Test
    @Transactional
    public void createPurchaseOrdersWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = purchaseOrdersRepository.findAll().size();

        // Create the PurchaseOrders with an existing ID
        purchaseOrders.setId(1L);
        PurchaseOrdersDTO purchaseOrdersDTO = purchaseOrdersMapper.toDto(purchaseOrders);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPurchaseOrdersMockMvc.perform(post("/api/purchase-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(purchaseOrdersDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PurchaseOrders in the database
        List<PurchaseOrders> purchaseOrdersList = purchaseOrdersRepository.findAll();
        assertThat(purchaseOrdersList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = purchaseOrdersRepository.findAll().size();
        // set the field null
        purchaseOrders.setName(null);

        // Create the PurchaseOrders, which fails.
        PurchaseOrdersDTO purchaseOrdersDTO = purchaseOrdersMapper.toDto(purchaseOrders);

        restPurchaseOrdersMockMvc.perform(post("/api/purchase-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(purchaseOrdersDTO)))
            .andExpect(status().isBadRequest());

        List<PurchaseOrders> purchaseOrdersList = purchaseOrdersRepository.findAll();
        assertThat(purchaseOrdersList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPurchaseOrders() throws Exception {
        // Initialize the database
        purchaseOrdersRepository.saveAndFlush(purchaseOrders);

        // Get all the purchaseOrdersList
        restPurchaseOrdersMockMvc.perform(get("/api/purchase-orders?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(purchaseOrders.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME.toString())))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(DEFAULT_END_TIME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getPurchaseOrders() throws Exception {
        // Initialize the database
        purchaseOrdersRepository.saveAndFlush(purchaseOrders);

        // Get the purchaseOrders
        restPurchaseOrdersMockMvc.perform(get("/api/purchase-orders/{id}", purchaseOrders.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(purchaseOrders.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.startTime").value(DEFAULT_START_TIME.toString()))
            .andExpect(jsonPath("$.endTime").value(DEFAULT_END_TIME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getAllPurchaseOrdersByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        purchaseOrdersRepository.saveAndFlush(purchaseOrders);

        // Get all the purchaseOrdersList where name equals to DEFAULT_NAME
        defaultPurchaseOrdersShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the purchaseOrdersList where name equals to UPDATED_NAME
        defaultPurchaseOrdersShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllPurchaseOrdersByNameIsInShouldWork() throws Exception {
        // Initialize the database
        purchaseOrdersRepository.saveAndFlush(purchaseOrders);

        // Get all the purchaseOrdersList where name in DEFAULT_NAME or UPDATED_NAME
        defaultPurchaseOrdersShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the purchaseOrdersList where name equals to UPDATED_NAME
        defaultPurchaseOrdersShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllPurchaseOrdersByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        purchaseOrdersRepository.saveAndFlush(purchaseOrders);

        // Get all the purchaseOrdersList where name is not null
        defaultPurchaseOrdersShouldBeFound("name.specified=true");

        // Get all the purchaseOrdersList where name is null
        defaultPurchaseOrdersShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllPurchaseOrdersByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        purchaseOrdersRepository.saveAndFlush(purchaseOrders);

        // Get all the purchaseOrdersList where status equals to DEFAULT_STATUS
        defaultPurchaseOrdersShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the purchaseOrdersList where status equals to UPDATED_STATUS
        defaultPurchaseOrdersShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllPurchaseOrdersByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        purchaseOrdersRepository.saveAndFlush(purchaseOrders);

        // Get all the purchaseOrdersList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultPurchaseOrdersShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the purchaseOrdersList where status equals to UPDATED_STATUS
        defaultPurchaseOrdersShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllPurchaseOrdersByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        purchaseOrdersRepository.saveAndFlush(purchaseOrders);

        // Get all the purchaseOrdersList where status is not null
        defaultPurchaseOrdersShouldBeFound("status.specified=true");

        // Get all the purchaseOrdersList where status is null
        defaultPurchaseOrdersShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    public void getAllPurchaseOrdersByStartTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        purchaseOrdersRepository.saveAndFlush(purchaseOrders);

        // Get all the purchaseOrdersList where startTime equals to DEFAULT_START_TIME
        defaultPurchaseOrdersShouldBeFound("startTime.equals=" + DEFAULT_START_TIME);

        // Get all the purchaseOrdersList where startTime equals to UPDATED_START_TIME
        defaultPurchaseOrdersShouldNotBeFound("startTime.equals=" + UPDATED_START_TIME);
    }

    @Test
    @Transactional
    public void getAllPurchaseOrdersByStartTimeIsInShouldWork() throws Exception {
        // Initialize the database
        purchaseOrdersRepository.saveAndFlush(purchaseOrders);

        // Get all the purchaseOrdersList where startTime in DEFAULT_START_TIME or UPDATED_START_TIME
        defaultPurchaseOrdersShouldBeFound("startTime.in=" + DEFAULT_START_TIME + "," + UPDATED_START_TIME);

        // Get all the purchaseOrdersList where startTime equals to UPDATED_START_TIME
        defaultPurchaseOrdersShouldNotBeFound("startTime.in=" + UPDATED_START_TIME);
    }

    @Test
    @Transactional
    public void getAllPurchaseOrdersByStartTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        purchaseOrdersRepository.saveAndFlush(purchaseOrders);

        // Get all the purchaseOrdersList where startTime is not null
        defaultPurchaseOrdersShouldBeFound("startTime.specified=true");

        // Get all the purchaseOrdersList where startTime is null
        defaultPurchaseOrdersShouldNotBeFound("startTime.specified=false");
    }

    @Test
    @Transactional
    public void getAllPurchaseOrdersByEndTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        purchaseOrdersRepository.saveAndFlush(purchaseOrders);

        // Get all the purchaseOrdersList where endTime equals to DEFAULT_END_TIME
        defaultPurchaseOrdersShouldBeFound("endTime.equals=" + DEFAULT_END_TIME);

        // Get all the purchaseOrdersList where endTime equals to UPDATED_END_TIME
        defaultPurchaseOrdersShouldNotBeFound("endTime.equals=" + UPDATED_END_TIME);
    }

    @Test
    @Transactional
    public void getAllPurchaseOrdersByEndTimeIsInShouldWork() throws Exception {
        // Initialize the database
        purchaseOrdersRepository.saveAndFlush(purchaseOrders);

        // Get all the purchaseOrdersList where endTime in DEFAULT_END_TIME or UPDATED_END_TIME
        defaultPurchaseOrdersShouldBeFound("endTime.in=" + DEFAULT_END_TIME + "," + UPDATED_END_TIME);

        // Get all the purchaseOrdersList where endTime equals to UPDATED_END_TIME
        defaultPurchaseOrdersShouldNotBeFound("endTime.in=" + UPDATED_END_TIME);
    }

    @Test
    @Transactional
    public void getAllPurchaseOrdersByEndTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        purchaseOrdersRepository.saveAndFlush(purchaseOrders);

        // Get all the purchaseOrdersList where endTime is not null
        defaultPurchaseOrdersShouldBeFound("endTime.specified=true");

        // Get all the purchaseOrdersList where endTime is null
        defaultPurchaseOrdersShouldNotBeFound("endTime.specified=false");
    }

    @Test
    @Transactional
    public void getAllPurchaseOrdersByTmsCustomFieldScreenValueIsEqualToSomething() throws Exception {
        // Initialize the database
        TMSCustomFieldScreenValue tmsCustomFieldScreenValue = TMSCustomFieldScreenValueResourceIntTest.createEntity(em);
        em.persist(tmsCustomFieldScreenValue);
        em.flush();
        purchaseOrders.addTmsCustomFieldScreenValue(tmsCustomFieldScreenValue);
        purchaseOrdersRepository.saveAndFlush(purchaseOrders);
        Long tmsCustomFieldScreenValueId = tmsCustomFieldScreenValue.getId();

        // Get all the purchaseOrdersList where tmsCustomFieldScreenValue equals to tmsCustomFieldScreenValueId
        defaultPurchaseOrdersShouldBeFound("tmsCustomFieldScreenValueId.equals=" + tmsCustomFieldScreenValueId);

        // Get all the purchaseOrdersList where tmsCustomFieldScreenValue equals to tmsCustomFieldScreenValueId + 1
        defaultPurchaseOrdersShouldNotBeFound("tmsCustomFieldScreenValueId.equals=" + (tmsCustomFieldScreenValueId + 1));
    }


    @Test
    @Transactional
    public void getAllPurchaseOrdersByProjectIsEqualToSomething() throws Exception {
        // Initialize the database
        Projects project = ProjectsResourceIntTest.createEntity(em);
        em.persist(project);
        em.flush();
        purchaseOrders.setProject(project);
        purchaseOrdersRepository.saveAndFlush(purchaseOrders);
        Long projectId = project.getId();

        // Get all the purchaseOrdersList where project equals to projectId
        defaultPurchaseOrdersShouldBeFound("projectId.equals=" + projectId);

        // Get all the purchaseOrdersList where project equals to projectId + 1
        defaultPurchaseOrdersShouldNotBeFound("projectId.equals=" + (projectId + 1));
    }


    @Test
    @Transactional
    public void getAllPurchaseOrdersByPurchaseOrderLeadIsEqualToSomething() throws Exception {
        // Initialize the database
        ProjectUsers purchaseOrderLead = ProjectUsersResourceIntTest.createEntity(em);
        em.persist(purchaseOrderLead);
        em.flush();
        purchaseOrders.setPurchaseOrderLead(purchaseOrderLead);
        purchaseOrdersRepository.saveAndFlush(purchaseOrders);
        Long purchaseOrderLeadId = purchaseOrderLead.getId();

        // Get all the purchaseOrdersList where purchaseOrderLead equals to purchaseOrderLeadId
        defaultPurchaseOrdersShouldBeFound("purchaseOrderLeadId.equals=" + purchaseOrderLeadId);

        // Get all the purchaseOrdersList where purchaseOrderLead equals to purchaseOrderLeadId + 1
        defaultPurchaseOrdersShouldNotBeFound("purchaseOrderLeadId.equals=" + (purchaseOrderLeadId + 1));
    }


    @Test
    @Transactional
    public void getAllPurchaseOrdersByPackagesIsEqualToSomething() throws Exception {
        // Initialize the database
        Packages packages = PackagesResourceIntTest.createEntity(em);
        em.persist(packages);
        em.flush();
        purchaseOrders.addPackages(packages);
        purchaseOrdersRepository.saveAndFlush(purchaseOrders);
        Long packagesId = packages.getId();

        // Get all the purchaseOrdersList where packages equals to packagesId
        defaultPurchaseOrdersShouldBeFound("packagesId.equals=" + packagesId);

        // Get all the purchaseOrdersList where packages equals to packagesId + 1
        defaultPurchaseOrdersShouldNotBeFound("packagesId.equals=" + (packagesId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultPurchaseOrdersShouldBeFound(String filter) throws Exception {
        restPurchaseOrdersMockMvc.perform(get("/api/purchase-orders?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(purchaseOrders.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME.toString())))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(DEFAULT_END_TIME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultPurchaseOrdersShouldNotBeFound(String filter) throws Exception {
        restPurchaseOrdersMockMvc.perform(get("/api/purchase-orders?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingPurchaseOrders() throws Exception {
        // Get the purchaseOrders
        restPurchaseOrdersMockMvc.perform(get("/api/purchase-orders/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePurchaseOrders() throws Exception {
        // Initialize the database
        purchaseOrdersRepository.saveAndFlush(purchaseOrders);
        purchaseOrdersSearchRepository.save(purchaseOrders);
        int databaseSizeBeforeUpdate = purchaseOrdersRepository.findAll().size();

        // Update the purchaseOrders
        PurchaseOrders updatedPurchaseOrders = purchaseOrdersRepository.findOne(purchaseOrders.getId());
        // Disconnect from session so that the updates on updatedPurchaseOrders are not directly saved in db
        em.detach(updatedPurchaseOrders);
        updatedPurchaseOrders
            .name(UPDATED_NAME)
            .status(UPDATED_STATUS)
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME)
            .description(UPDATED_DESCRIPTION);
        PurchaseOrdersDTO purchaseOrdersDTO = purchaseOrdersMapper.toDto(updatedPurchaseOrders);

        restPurchaseOrdersMockMvc.perform(put("/api/purchase-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(purchaseOrdersDTO)))
            .andExpect(status().isOk());

        // Validate the PurchaseOrders in the database
        List<PurchaseOrders> purchaseOrdersList = purchaseOrdersRepository.findAll();
        assertThat(purchaseOrdersList).hasSize(databaseSizeBeforeUpdate);
        PurchaseOrders testPurchaseOrders = purchaseOrdersList.get(purchaseOrdersList.size() - 1);
        assertThat(testPurchaseOrders.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPurchaseOrders.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testPurchaseOrders.getStartTime()).isEqualTo(UPDATED_START_TIME);
        assertThat(testPurchaseOrders.getEndTime()).isEqualTo(UPDATED_END_TIME);
        assertThat(testPurchaseOrders.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the PurchaseOrders in Elasticsearch
        PurchaseOrders purchaseOrdersEs = purchaseOrdersSearchRepository.findOne(testPurchaseOrders.getId());
        assertThat(purchaseOrdersEs).isEqualToIgnoringGivenFields(testPurchaseOrders);
    }

    @Test
    @Transactional
    public void updateNonExistingPurchaseOrders() throws Exception {
        int databaseSizeBeforeUpdate = purchaseOrdersRepository.findAll().size();

        // Create the PurchaseOrders
        PurchaseOrdersDTO purchaseOrdersDTO = purchaseOrdersMapper.toDto(purchaseOrders);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPurchaseOrdersMockMvc.perform(put("/api/purchase-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(purchaseOrdersDTO)))
            .andExpect(status().isCreated());

        // Validate the PurchaseOrders in the database
        List<PurchaseOrders> purchaseOrdersList = purchaseOrdersRepository.findAll();
        assertThat(purchaseOrdersList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePurchaseOrders() throws Exception {
        // Initialize the database
        purchaseOrdersRepository.saveAndFlush(purchaseOrders);
        purchaseOrdersSearchRepository.save(purchaseOrders);
        int databaseSizeBeforeDelete = purchaseOrdersRepository.findAll().size();

        // Get the purchaseOrders
        restPurchaseOrdersMockMvc.perform(delete("/api/purchase-orders/{id}", purchaseOrders.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean purchaseOrdersExistsInEs = purchaseOrdersSearchRepository.exists(purchaseOrders.getId());
        assertThat(purchaseOrdersExistsInEs).isFalse();

        // Validate the database is empty
        List<PurchaseOrders> purchaseOrdersList = purchaseOrdersRepository.findAll();
        assertThat(purchaseOrdersList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchPurchaseOrders() throws Exception {
        // Initialize the database
        purchaseOrdersRepository.saveAndFlush(purchaseOrders);
        purchaseOrdersSearchRepository.save(purchaseOrders);

        // Search the purchaseOrders
        restPurchaseOrdersMockMvc.perform(get("/api/_search/purchase-orders?query=id:" + purchaseOrders.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(purchaseOrders.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME.toString())))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(DEFAULT_END_TIME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PurchaseOrders.class);
        PurchaseOrders purchaseOrders1 = new PurchaseOrders();
        purchaseOrders1.setId(1L);
        PurchaseOrders purchaseOrders2 = new PurchaseOrders();
        purchaseOrders2.setId(purchaseOrders1.getId());
        assertThat(purchaseOrders1).isEqualTo(purchaseOrders2);
        purchaseOrders2.setId(2L);
        assertThat(purchaseOrders1).isNotEqualTo(purchaseOrders2);
        purchaseOrders1.setId(null);
        assertThat(purchaseOrders1).isNotEqualTo(purchaseOrders2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PurchaseOrdersDTO.class);
        PurchaseOrdersDTO purchaseOrdersDTO1 = new PurchaseOrdersDTO();
        purchaseOrdersDTO1.setId(1L);
        PurchaseOrdersDTO purchaseOrdersDTO2 = new PurchaseOrdersDTO();
        assertThat(purchaseOrdersDTO1).isNotEqualTo(purchaseOrdersDTO2);
        purchaseOrdersDTO2.setId(purchaseOrdersDTO1.getId());
        assertThat(purchaseOrdersDTO1).isEqualTo(purchaseOrdersDTO2);
        purchaseOrdersDTO2.setId(2L);
        assertThat(purchaseOrdersDTO1).isNotEqualTo(purchaseOrdersDTO2);
        purchaseOrdersDTO1.setId(null);
        assertThat(purchaseOrdersDTO1).isNotEqualTo(purchaseOrdersDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(purchaseOrdersMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(purchaseOrdersMapper.fromId(null)).isNull();
    }
}
