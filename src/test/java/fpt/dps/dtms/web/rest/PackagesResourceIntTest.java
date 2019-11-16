package fpt.dps.dtms.web.rest;

import fpt.dps.dtms.DtmsApp;

import fpt.dps.dtms.domain.Packages;
import fpt.dps.dtms.domain.TMSCustomFieldScreenValue;
import fpt.dps.dtms.domain.PurchaseOrders;
import fpt.dps.dtms.domain.Tasks;
import fpt.dps.dtms.repository.PackagesRepository;
import fpt.dps.dtms.service.PackagesService;
import fpt.dps.dtms.service.TasksService;
import fpt.dps.dtms.repository.search.PackagesSearchRepository;
import fpt.dps.dtms.service.dto.PackagesDTO;
import fpt.dps.dtms.service.mapper.PackagesMapper;
import fpt.dps.dtms.web.rest.errors.ExceptionTranslator;
import fpt.dps.dtms.service.dto.PackagesCriteria;
import fpt.dps.dtms.service.PackagesQueryService;

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

/**
 * Test class for the PackagesResource REST controller.
 *
 * @see PackagesResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DtmsApp.class)
public class PackagesResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_OP = "AAAAAAAAAA";
    private static final String UPDATED_OP = "BBBBBBBBBB";

    private static final String DEFAULT_REVIEWER = "AAAAAAAAAA";
    private static final String UPDATED_REVIEWER = "BBBBBBBBBB";

    private static final String DEFAULT_FI = "AAAAAAAAAA";
    private static final String UPDATED_FI = "BBBBBBBBBB";

    private static final String DEFAULT_DELIVERY = "AAAAAAAAAA";
    private static final String UPDATED_DELIVERY = "BBBBBBBBBB";

    private static final Instant DEFAULT_ESTIMATE_DELIVERY = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ESTIMATE_DELIVERY = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_TARGET = 1;
    private static final Integer UPDATED_TARGET = 2;

    private static final Instant DEFAULT_START_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private PackagesRepository packagesRepository;

    @Autowired
    private PackagesMapper packagesMapper;

    @Autowired
    private PackagesService packagesService;

    @Autowired
    private PackagesSearchRepository packagesSearchRepository;

    @Autowired
    private PackagesQueryService packagesQueryService;
    
    @Autowired
    private TasksService tasksService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPackagesMockMvc;

    private Packages packages;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PackagesResource packagesResource = new PackagesResource(packagesService, packagesQueryService, tasksService);
        this.restPackagesMockMvc = MockMvcBuilders.standaloneSetup(packagesResource)
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
    public static Packages createEntity(EntityManager em) {
        Packages packages = new Packages()
            .name(DEFAULT_NAME)
            .op(DEFAULT_OP)
            .reviewer(DEFAULT_REVIEWER)
            .fi(DEFAULT_FI)
            .delivery(DEFAULT_DELIVERY)
            .estimateDelivery(DEFAULT_ESTIMATE_DELIVERY)
            .target(DEFAULT_TARGET)
            .startTime(DEFAULT_START_TIME)
            .endTime(DEFAULT_END_TIME)
            .description(DEFAULT_DESCRIPTION);
        return packages;
    }

    @Before
    public void initTest() {
        packagesSearchRepository.deleteAll();
        packages = createEntity(em);
    }

    @Test
    @Transactional
    public void createPackages() throws Exception {
        int databaseSizeBeforeCreate = packagesRepository.findAll().size();

        // Create the Packages
        PackagesDTO packagesDTO = packagesMapper.toDto(packages);
        restPackagesMockMvc.perform(post("/api/packages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(packagesDTO)))
            .andExpect(status().isCreated());

        // Validate the Packages in the database
        List<Packages> packagesList = packagesRepository.findAll();
        assertThat(packagesList).hasSize(databaseSizeBeforeCreate + 1);
        Packages testPackages = packagesList.get(packagesList.size() - 1);
        assertThat(testPackages.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPackages.getOp()).isEqualTo(DEFAULT_OP);
        assertThat(testPackages.getReviewer()).isEqualTo(DEFAULT_REVIEWER);
        assertThat(testPackages.getFi()).isEqualTo(DEFAULT_FI);
        assertThat(testPackages.getDelivery()).isEqualTo(DEFAULT_DELIVERY);
        assertThat(testPackages.getEstimateDelivery()).isEqualTo(DEFAULT_ESTIMATE_DELIVERY);
        assertThat(testPackages.getTarget()).isEqualTo(DEFAULT_TARGET);
        assertThat(testPackages.getStartTime()).isEqualTo(DEFAULT_START_TIME);
        assertThat(testPackages.getEndTime()).isEqualTo(DEFAULT_END_TIME);
        assertThat(testPackages.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the Packages in Elasticsearch
        Packages packagesEs = packagesSearchRepository.findOne(testPackages.getId());
        assertThat(packagesEs).isEqualToIgnoringGivenFields(testPackages);
    }

    @Test
    @Transactional
    public void createPackagesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = packagesRepository.findAll().size();

        // Create the Packages with an existing ID
        packages.setId(1L);
        PackagesDTO packagesDTO = packagesMapper.toDto(packages);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPackagesMockMvc.perform(post("/api/packages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(packagesDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Packages in the database
        List<Packages> packagesList = packagesRepository.findAll();
        assertThat(packagesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = packagesRepository.findAll().size();
        // set the field null
        packages.setName(null);

        // Create the Packages, which fails.
        PackagesDTO packagesDTO = packagesMapper.toDto(packages);

        restPackagesMockMvc.perform(post("/api/packages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(packagesDTO)))
            .andExpect(status().isBadRequest());

        List<Packages> packagesList = packagesRepository.findAll();
        assertThat(packagesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPackages() throws Exception {
        // Initialize the database
        packagesRepository.saveAndFlush(packages);

        // Get all the packagesList
        restPackagesMockMvc.perform(get("/api/packages?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(packages.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].op").value(hasItem(DEFAULT_OP.toString())))
            .andExpect(jsonPath("$.[*].reviewer").value(hasItem(DEFAULT_REVIEWER.toString())))
            .andExpect(jsonPath("$.[*].fi").value(hasItem(DEFAULT_FI.toString())))
            .andExpect(jsonPath("$.[*].delivery").value(hasItem(DEFAULT_DELIVERY.toString())))
            .andExpect(jsonPath("$.[*].estimateDelivery").value(hasItem(DEFAULT_ESTIMATE_DELIVERY.toString())))
            .andExpect(jsonPath("$.[*].target").value(hasItem(DEFAULT_TARGET)))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME.toString())))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(DEFAULT_END_TIME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getPackages() throws Exception {
        // Initialize the database
        packagesRepository.saveAndFlush(packages);

        // Get the packages
        restPackagesMockMvc.perform(get("/api/packages/{id}", packages.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(packages.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.op").value(DEFAULT_OP.toString()))
            .andExpect(jsonPath("$.reviewer").value(DEFAULT_REVIEWER.toString()))
            .andExpect(jsonPath("$.fi").value(DEFAULT_FI.toString()))
            .andExpect(jsonPath("$.delivery").value(DEFAULT_DELIVERY.toString()))
            .andExpect(jsonPath("$.estimateDelivery").value(DEFAULT_ESTIMATE_DELIVERY.toString()))
            .andExpect(jsonPath("$.target").value(DEFAULT_TARGET))
            .andExpect(jsonPath("$.startTime").value(DEFAULT_START_TIME.toString()))
            .andExpect(jsonPath("$.endTime").value(DEFAULT_END_TIME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getAllPackagesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        packagesRepository.saveAndFlush(packages);

        // Get all the packagesList where name equals to DEFAULT_NAME
        defaultPackagesShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the packagesList where name equals to UPDATED_NAME
        defaultPackagesShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllPackagesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        packagesRepository.saveAndFlush(packages);

        // Get all the packagesList where name in DEFAULT_NAME or UPDATED_NAME
        defaultPackagesShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the packagesList where name equals to UPDATED_NAME
        defaultPackagesShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllPackagesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        packagesRepository.saveAndFlush(packages);

        // Get all the packagesList where name is not null
        defaultPackagesShouldBeFound("name.specified=true");

        // Get all the packagesList where name is null
        defaultPackagesShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllPackagesByOpIsEqualToSomething() throws Exception {
        // Initialize the database
        packagesRepository.saveAndFlush(packages);

        // Get all the packagesList where op equals to DEFAULT_OP
        defaultPackagesShouldBeFound("op.equals=" + DEFAULT_OP);

        // Get all the packagesList where op equals to UPDATED_OP
        defaultPackagesShouldNotBeFound("op.equals=" + UPDATED_OP);
    }

    @Test
    @Transactional
    public void getAllPackagesByOpIsInShouldWork() throws Exception {
        // Initialize the database
        packagesRepository.saveAndFlush(packages);

        // Get all the packagesList where op in DEFAULT_OP or UPDATED_OP
        defaultPackagesShouldBeFound("op.in=" + DEFAULT_OP + "," + UPDATED_OP);

        // Get all the packagesList where op equals to UPDATED_OP
        defaultPackagesShouldNotBeFound("op.in=" + UPDATED_OP);
    }

    @Test
    @Transactional
    public void getAllPackagesByOpIsNullOrNotNull() throws Exception {
        // Initialize the database
        packagesRepository.saveAndFlush(packages);

        // Get all the packagesList where op is not null
        defaultPackagesShouldBeFound("op.specified=true");

        // Get all the packagesList where op is null
        defaultPackagesShouldNotBeFound("op.specified=false");
    }

    @Test
    @Transactional
    public void getAllPackagesByReviewerIsEqualToSomething() throws Exception {
        // Initialize the database
        packagesRepository.saveAndFlush(packages);

        // Get all the packagesList where reviewer equals to DEFAULT_REVIEWER
        defaultPackagesShouldBeFound("reviewer.equals=" + DEFAULT_REVIEWER);

        // Get all the packagesList where reviewer equals to UPDATED_REVIEWER
        defaultPackagesShouldNotBeFound("reviewer.equals=" + UPDATED_REVIEWER);
    }

    @Test
    @Transactional
    public void getAllPackagesByReviewerIsInShouldWork() throws Exception {
        // Initialize the database
        packagesRepository.saveAndFlush(packages);

        // Get all the packagesList where reviewer in DEFAULT_REVIEWER or UPDATED_REVIEWER
        defaultPackagesShouldBeFound("reviewer.in=" + DEFAULT_REVIEWER + "," + UPDATED_REVIEWER);

        // Get all the packagesList where reviewer equals to UPDATED_REVIEWER
        defaultPackagesShouldNotBeFound("reviewer.in=" + UPDATED_REVIEWER);
    }

    @Test
    @Transactional
    public void getAllPackagesByReviewerIsNullOrNotNull() throws Exception {
        // Initialize the database
        packagesRepository.saveAndFlush(packages);

        // Get all the packagesList where reviewer is not null
        defaultPackagesShouldBeFound("reviewer.specified=true");

        // Get all the packagesList where reviewer is null
        defaultPackagesShouldNotBeFound("reviewer.specified=false");
    }

    @Test
    @Transactional
    public void getAllPackagesByFiIsEqualToSomething() throws Exception {
        // Initialize the database
        packagesRepository.saveAndFlush(packages);

        // Get all the packagesList where fi equals to DEFAULT_FI
        defaultPackagesShouldBeFound("fi.equals=" + DEFAULT_FI);

        // Get all the packagesList where fi equals to UPDATED_FI
        defaultPackagesShouldNotBeFound("fi.equals=" + UPDATED_FI);
    }

    @Test
    @Transactional
    public void getAllPackagesByFiIsInShouldWork() throws Exception {
        // Initialize the database
        packagesRepository.saveAndFlush(packages);

        // Get all the packagesList where fi in DEFAULT_FI or UPDATED_FI
        defaultPackagesShouldBeFound("fi.in=" + DEFAULT_FI + "," + UPDATED_FI);

        // Get all the packagesList where fi equals to UPDATED_FI
        defaultPackagesShouldNotBeFound("fi.in=" + UPDATED_FI);
    }

    @Test
    @Transactional
    public void getAllPackagesByFiIsNullOrNotNull() throws Exception {
        // Initialize the database
        packagesRepository.saveAndFlush(packages);

        // Get all the packagesList where fi is not null
        defaultPackagesShouldBeFound("fi.specified=true");

        // Get all the packagesList where fi is null
        defaultPackagesShouldNotBeFound("fi.specified=false");
    }

    @Test
    @Transactional
    public void getAllPackagesByDeliveryIsEqualToSomething() throws Exception {
        // Initialize the database
        packagesRepository.saveAndFlush(packages);

        // Get all the packagesList where delivery equals to DEFAULT_DELIVERY
        defaultPackagesShouldBeFound("delivery.equals=" + DEFAULT_DELIVERY);

        // Get all the packagesList where delivery equals to UPDATED_DELIVERY
        defaultPackagesShouldNotBeFound("delivery.equals=" + UPDATED_DELIVERY);
    }

    @Test
    @Transactional
    public void getAllPackagesByDeliveryIsInShouldWork() throws Exception {
        // Initialize the database
        packagesRepository.saveAndFlush(packages);

        // Get all the packagesList where delivery in DEFAULT_DELIVERY or UPDATED_DELIVERY
        defaultPackagesShouldBeFound("delivery.in=" + DEFAULT_DELIVERY + "," + UPDATED_DELIVERY);

        // Get all the packagesList where delivery equals to UPDATED_DELIVERY
        defaultPackagesShouldNotBeFound("delivery.in=" + UPDATED_DELIVERY);
    }

    @Test
    @Transactional
    public void getAllPackagesByDeliveryIsNullOrNotNull() throws Exception {
        // Initialize the database
        packagesRepository.saveAndFlush(packages);

        // Get all the packagesList where delivery is not null
        defaultPackagesShouldBeFound("delivery.specified=true");

        // Get all the packagesList where delivery is null
        defaultPackagesShouldNotBeFound("delivery.specified=false");
    }

    @Test
    @Transactional
    public void getAllPackagesByEstimateDeliveryIsEqualToSomething() throws Exception {
        // Initialize the database
        packagesRepository.saveAndFlush(packages);

        // Get all the packagesList where estimateDelivery equals to DEFAULT_ESTIMATE_DELIVERY
        defaultPackagesShouldBeFound("estimateDelivery.equals=" + DEFAULT_ESTIMATE_DELIVERY);

        // Get all the packagesList where estimateDelivery equals to UPDATED_ESTIMATE_DELIVERY
        defaultPackagesShouldNotBeFound("estimateDelivery.equals=" + UPDATED_ESTIMATE_DELIVERY);
    }

    @Test
    @Transactional
    public void getAllPackagesByEstimateDeliveryIsInShouldWork() throws Exception {
        // Initialize the database
        packagesRepository.saveAndFlush(packages);

        // Get all the packagesList where estimateDelivery in DEFAULT_ESTIMATE_DELIVERY or UPDATED_ESTIMATE_DELIVERY
        defaultPackagesShouldBeFound("estimateDelivery.in=" + DEFAULT_ESTIMATE_DELIVERY + "," + UPDATED_ESTIMATE_DELIVERY);

        // Get all the packagesList where estimateDelivery equals to UPDATED_ESTIMATE_DELIVERY
        defaultPackagesShouldNotBeFound("estimateDelivery.in=" + UPDATED_ESTIMATE_DELIVERY);
    }

    @Test
    @Transactional
    public void getAllPackagesByEstimateDeliveryIsNullOrNotNull() throws Exception {
        // Initialize the database
        packagesRepository.saveAndFlush(packages);

        // Get all the packagesList where estimateDelivery is not null
        defaultPackagesShouldBeFound("estimateDelivery.specified=true");

        // Get all the packagesList where estimateDelivery is null
        defaultPackagesShouldNotBeFound("estimateDelivery.specified=false");
    }

    @Test
    @Transactional
    public void getAllPackagesByTargetIsEqualToSomething() throws Exception {
        // Initialize the database
        packagesRepository.saveAndFlush(packages);

        // Get all the packagesList where target equals to DEFAULT_TARGET
        defaultPackagesShouldBeFound("target.equals=" + DEFAULT_TARGET);

        // Get all the packagesList where target equals to UPDATED_TARGET
        defaultPackagesShouldNotBeFound("target.equals=" + UPDATED_TARGET);
    }

    @Test
    @Transactional
    public void getAllPackagesByTargetIsInShouldWork() throws Exception {
        // Initialize the database
        packagesRepository.saveAndFlush(packages);

        // Get all the packagesList where target in DEFAULT_TARGET or UPDATED_TARGET
        defaultPackagesShouldBeFound("target.in=" + DEFAULT_TARGET + "," + UPDATED_TARGET);

        // Get all the packagesList where target equals to UPDATED_TARGET
        defaultPackagesShouldNotBeFound("target.in=" + UPDATED_TARGET);
    }

    @Test
    @Transactional
    public void getAllPackagesByTargetIsNullOrNotNull() throws Exception {
        // Initialize the database
        packagesRepository.saveAndFlush(packages);

        // Get all the packagesList where target is not null
        defaultPackagesShouldBeFound("target.specified=true");

        // Get all the packagesList where target is null
        defaultPackagesShouldNotBeFound("target.specified=false");
    }

    @Test
    @Transactional
    public void getAllPackagesByTargetIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        packagesRepository.saveAndFlush(packages);

        // Get all the packagesList where target greater than or equals to DEFAULT_TARGET
        defaultPackagesShouldBeFound("target.greaterOrEqualThan=" + DEFAULT_TARGET);

        // Get all the packagesList where target greater than or equals to UPDATED_TARGET
        defaultPackagesShouldNotBeFound("target.greaterOrEqualThan=" + UPDATED_TARGET);
    }

    @Test
    @Transactional
    public void getAllPackagesByTargetIsLessThanSomething() throws Exception {
        // Initialize the database
        packagesRepository.saveAndFlush(packages);

        // Get all the packagesList where target less than or equals to DEFAULT_TARGET
        defaultPackagesShouldNotBeFound("target.lessThan=" + DEFAULT_TARGET);

        // Get all the packagesList where target less than or equals to UPDATED_TARGET
        defaultPackagesShouldBeFound("target.lessThan=" + UPDATED_TARGET);
    }


    @Test
    @Transactional
    public void getAllPackagesByStartTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        packagesRepository.saveAndFlush(packages);

        // Get all the packagesList where startTime equals to DEFAULT_START_TIME
        defaultPackagesShouldBeFound("startTime.equals=" + DEFAULT_START_TIME);

        // Get all the packagesList where startTime equals to UPDATED_START_TIME
        defaultPackagesShouldNotBeFound("startTime.equals=" + UPDATED_START_TIME);
    }

    @Test
    @Transactional
    public void getAllPackagesByStartTimeIsInShouldWork() throws Exception {
        // Initialize the database
        packagesRepository.saveAndFlush(packages);

        // Get all the packagesList where startTime in DEFAULT_START_TIME or UPDATED_START_TIME
        defaultPackagesShouldBeFound("startTime.in=" + DEFAULT_START_TIME + "," + UPDATED_START_TIME);

        // Get all the packagesList where startTime equals to UPDATED_START_TIME
        defaultPackagesShouldNotBeFound("startTime.in=" + UPDATED_START_TIME);
    }

    @Test
    @Transactional
    public void getAllPackagesByStartTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        packagesRepository.saveAndFlush(packages);

        // Get all the packagesList where startTime is not null
        defaultPackagesShouldBeFound("startTime.specified=true");

        // Get all the packagesList where startTime is null
        defaultPackagesShouldNotBeFound("startTime.specified=false");
    }

    @Test
    @Transactional
    public void getAllPackagesByEndTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        packagesRepository.saveAndFlush(packages);

        // Get all the packagesList where endTime equals to DEFAULT_END_TIME
        defaultPackagesShouldBeFound("endTime.equals=" + DEFAULT_END_TIME);

        // Get all the packagesList where endTime equals to UPDATED_END_TIME
        defaultPackagesShouldNotBeFound("endTime.equals=" + UPDATED_END_TIME);
    }

    @Test
    @Transactional
    public void getAllPackagesByEndTimeIsInShouldWork() throws Exception {
        // Initialize the database
        packagesRepository.saveAndFlush(packages);

        // Get all the packagesList where endTime in DEFAULT_END_TIME or UPDATED_END_TIME
        defaultPackagesShouldBeFound("endTime.in=" + DEFAULT_END_TIME + "," + UPDATED_END_TIME);

        // Get all the packagesList where endTime equals to UPDATED_END_TIME
        defaultPackagesShouldNotBeFound("endTime.in=" + UPDATED_END_TIME);
    }

    @Test
    @Transactional
    public void getAllPackagesByEndTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        packagesRepository.saveAndFlush(packages);

        // Get all the packagesList where endTime is not null
        defaultPackagesShouldBeFound("endTime.specified=true");

        // Get all the packagesList where endTime is null
        defaultPackagesShouldNotBeFound("endTime.specified=false");
    }

    @Test
    @Transactional
    public void getAllPackagesByTmsCustomFieldScreenValueIsEqualToSomething() throws Exception {
        // Initialize the database
        TMSCustomFieldScreenValue tmsCustomFieldScreenValue = TMSCustomFieldScreenValueResourceIntTest.createEntity(em);
        em.persist(tmsCustomFieldScreenValue);
        em.flush();
        packages.addTmsCustomFieldScreenValue(tmsCustomFieldScreenValue);
        packagesRepository.saveAndFlush(packages);
        Long tmsCustomFieldScreenValueId = tmsCustomFieldScreenValue.getId();

        // Get all the packagesList where tmsCustomFieldScreenValue equals to tmsCustomFieldScreenValueId
        defaultPackagesShouldBeFound("tmsCustomFieldScreenValueId.equals=" + tmsCustomFieldScreenValueId);

        // Get all the packagesList where tmsCustomFieldScreenValue equals to tmsCustomFieldScreenValueId + 1
        defaultPackagesShouldNotBeFound("tmsCustomFieldScreenValueId.equals=" + (tmsCustomFieldScreenValueId + 1));
    }


    @Test
    @Transactional
    public void getAllPackagesByPurchaseOrdersIsEqualToSomething() throws Exception {
        // Initialize the database
        PurchaseOrders purchaseOrders = PurchaseOrdersResourceIntTest.createEntity(em);
        em.persist(purchaseOrders);
        em.flush();
        packages.setPurchaseOrders(purchaseOrders);
        packagesRepository.saveAndFlush(packages);
        Long purchaseOrdersId = purchaseOrders.getId();

        // Get all the packagesList where purchaseOrders equals to purchaseOrdersId
        defaultPackagesShouldBeFound("purchaseOrdersId.equals=" + purchaseOrdersId);

        // Get all the packagesList where purchaseOrders equals to purchaseOrdersId + 1
        defaultPackagesShouldNotBeFound("purchaseOrdersId.equals=" + (purchaseOrdersId + 1));
    }


    @Test
    @Transactional
    public void getAllPackagesByTasksIsEqualToSomething() throws Exception {
        // Initialize the database
        Tasks tasks = TasksResourceIntTest.createEntity(em);
        em.persist(tasks);
        em.flush();
        packages.addTasks(tasks);
        packagesRepository.saveAndFlush(packages);
        Long tasksId = tasks.getId();

        // Get all the packagesList where tasks equals to tasksId
        defaultPackagesShouldBeFound("tasksId.equals=" + tasksId);

        // Get all the packagesList where tasks equals to tasksId + 1
        defaultPackagesShouldNotBeFound("tasksId.equals=" + (tasksId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultPackagesShouldBeFound(String filter) throws Exception {
        restPackagesMockMvc.perform(get("/api/packages?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(packages.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].op").value(hasItem(DEFAULT_OP.toString())))
            .andExpect(jsonPath("$.[*].reviewer").value(hasItem(DEFAULT_REVIEWER.toString())))
            .andExpect(jsonPath("$.[*].fi").value(hasItem(DEFAULT_FI.toString())))
            .andExpect(jsonPath("$.[*].delivery").value(hasItem(DEFAULT_DELIVERY.toString())))
            .andExpect(jsonPath("$.[*].estimateDelivery").value(hasItem(DEFAULT_ESTIMATE_DELIVERY.toString())))
            .andExpect(jsonPath("$.[*].target").value(hasItem(DEFAULT_TARGET)))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME.toString())))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(DEFAULT_END_TIME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultPackagesShouldNotBeFound(String filter) throws Exception {
        restPackagesMockMvc.perform(get("/api/packages?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingPackages() throws Exception {
        // Get the packages
        restPackagesMockMvc.perform(get("/api/packages/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePackages() throws Exception {
        // Initialize the database
        packagesRepository.saveAndFlush(packages);
        packagesSearchRepository.save(packages);
        int databaseSizeBeforeUpdate = packagesRepository.findAll().size();

        // Update the packages
        Packages updatedPackages = packagesRepository.findOne(packages.getId());
        // Disconnect from session so that the updates on updatedPackages are not directly saved in db
        em.detach(updatedPackages);
        updatedPackages
            .name(UPDATED_NAME)
            .op(UPDATED_OP)
            .reviewer(UPDATED_REVIEWER)
            .fi(UPDATED_FI)
            .delivery(UPDATED_DELIVERY)
            .estimateDelivery(UPDATED_ESTIMATE_DELIVERY)
            .target(UPDATED_TARGET)
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME)
            .description(UPDATED_DESCRIPTION);
        PackagesDTO packagesDTO = packagesMapper.toDto(updatedPackages);

        restPackagesMockMvc.perform(put("/api/packages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(packagesDTO)))
            .andExpect(status().isOk());

        // Validate the Packages in the database
        List<Packages> packagesList = packagesRepository.findAll();
        assertThat(packagesList).hasSize(databaseSizeBeforeUpdate);
        Packages testPackages = packagesList.get(packagesList.size() - 1);
        assertThat(testPackages.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPackages.getOp()).isEqualTo(UPDATED_OP);
        assertThat(testPackages.getReviewer()).isEqualTo(UPDATED_REVIEWER);
        assertThat(testPackages.getFi()).isEqualTo(UPDATED_FI);
        assertThat(testPackages.getDelivery()).isEqualTo(UPDATED_DELIVERY);
        assertThat(testPackages.getEstimateDelivery()).isEqualTo(UPDATED_ESTIMATE_DELIVERY);
        assertThat(testPackages.getTarget()).isEqualTo(UPDATED_TARGET);
        assertThat(testPackages.getStartTime()).isEqualTo(UPDATED_START_TIME);
        assertThat(testPackages.getEndTime()).isEqualTo(UPDATED_END_TIME);
        assertThat(testPackages.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the Packages in Elasticsearch
        Packages packagesEs = packagesSearchRepository.findOne(testPackages.getId());
        assertThat(packagesEs).isEqualToIgnoringGivenFields(testPackages);
    }

    @Test
    @Transactional
    public void updateNonExistingPackages() throws Exception {
        int databaseSizeBeforeUpdate = packagesRepository.findAll().size();

        // Create the Packages
        PackagesDTO packagesDTO = packagesMapper.toDto(packages);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPackagesMockMvc.perform(put("/api/packages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(packagesDTO)))
            .andExpect(status().isCreated());

        // Validate the Packages in the database
        List<Packages> packagesList = packagesRepository.findAll();
        assertThat(packagesList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePackages() throws Exception {
        // Initialize the database
        packagesRepository.saveAndFlush(packages);
        packagesSearchRepository.save(packages);
        int databaseSizeBeforeDelete = packagesRepository.findAll().size();

        // Get the packages
        restPackagesMockMvc.perform(delete("/api/packages/{id}", packages.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean packagesExistsInEs = packagesSearchRepository.exists(packages.getId());
        assertThat(packagesExistsInEs).isFalse();

        // Validate the database is empty
        List<Packages> packagesList = packagesRepository.findAll();
        assertThat(packagesList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchPackages() throws Exception {
        // Initialize the database
        packagesRepository.saveAndFlush(packages);
        packagesSearchRepository.save(packages);

        // Search the packages
        restPackagesMockMvc.perform(get("/api/_search/packages?query=id:" + packages.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(packages.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].op").value(hasItem(DEFAULT_OP.toString())))
            .andExpect(jsonPath("$.[*].reviewer").value(hasItem(DEFAULT_REVIEWER.toString())))
            .andExpect(jsonPath("$.[*].fi").value(hasItem(DEFAULT_FI.toString())))
            .andExpect(jsonPath("$.[*].delivery").value(hasItem(DEFAULT_DELIVERY.toString())))
            .andExpect(jsonPath("$.[*].estimateDelivery").value(hasItem(DEFAULT_ESTIMATE_DELIVERY.toString())))
            .andExpect(jsonPath("$.[*].target").value(hasItem(DEFAULT_TARGET)))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME.toString())))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(DEFAULT_END_TIME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Packages.class);
        Packages packages1 = new Packages();
        packages1.setId(1L);
        Packages packages2 = new Packages();
        packages2.setId(packages1.getId());
        assertThat(packages1).isEqualTo(packages2);
        packages2.setId(2L);
        assertThat(packages1).isNotEqualTo(packages2);
        packages1.setId(null);
        assertThat(packages1).isNotEqualTo(packages2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PackagesDTO.class);
        PackagesDTO packagesDTO1 = new PackagesDTO();
        packagesDTO1.setId(1L);
        PackagesDTO packagesDTO2 = new PackagesDTO();
        assertThat(packagesDTO1).isNotEqualTo(packagesDTO2);
        packagesDTO2.setId(packagesDTO1.getId());
        assertThat(packagesDTO1).isEqualTo(packagesDTO2);
        packagesDTO2.setId(2L);
        assertThat(packagesDTO1).isNotEqualTo(packagesDTO2);
        packagesDTO1.setId(null);
        assertThat(packagesDTO1).isNotEqualTo(packagesDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(packagesMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(packagesMapper.fromId(null)).isNull();
    }
}
