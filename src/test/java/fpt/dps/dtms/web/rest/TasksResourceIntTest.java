package fpt.dps.dtms.web.rest;

import fpt.dps.dtms.DtmsApp;

import fpt.dps.dtms.domain.Tasks;
import fpt.dps.dtms.domain.TMSCustomFieldScreenValue;
import fpt.dps.dtms.domain.Packages;
import fpt.dps.dtms.repository.TasksRepository;
import fpt.dps.dtms.service.TasksService;
import fpt.dps.dtms.repository.search.TasksSearchRepository;
import fpt.dps.dtms.service.dto.TasksDTO;
import fpt.dps.dtms.service.mapper.TasksMapper;
import fpt.dps.dtms.web.rest.errors.ExceptionTranslator;
import fpt.dps.dtms.service.dto.TasksCriteria;
import fpt.dps.dtms.service.TasksQueryService;

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

import fpt.dps.dtms.domain.enumeration.TaskSeverity;
import fpt.dps.dtms.domain.enumeration.TaskPriority;
import fpt.dps.dtms.domain.enumeration.TaskAvailability;
import fpt.dps.dtms.domain.enumeration.OPStatus;
import fpt.dps.dtms.domain.enumeration.ReviewStatus;
import fpt.dps.dtms.domain.enumeration.FixStatus;
import fpt.dps.dtms.domain.enumeration.ReviewStatus;
import fpt.dps.dtms.domain.enumeration.FIStatus;
import fpt.dps.dtms.domain.enumeration.ErrorSeverity;
import fpt.dps.dtms.domain.enumeration.TaskStatus;
/**
 * Test class for the TasksResource REST controller.
 *
 * @see TasksResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DtmsApp.class)
public class TasksResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final TaskSeverity DEFAULT_SEVERITY = TaskSeverity.NORMAL;
    private static final TaskSeverity UPDATED_SEVERITY = TaskSeverity.MEDIUM;

    private static final TaskPriority DEFAULT_PRIORITY = TaskPriority.NORMAL;
    private static final TaskPriority UPDATED_PRIORITY = TaskPriority.MEDIUM;

    private static final String DEFAULT_DATA = "AAAAAAAAAA";
    private static final String UPDATED_DATA = "BBBBBBBBBB";

    private static final String DEFAULT_FILE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FILE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final TaskAvailability DEFAULT_AVAILABILITY = TaskAvailability.NA;
    private static final TaskAvailability UPDATED_AVAILABILITY = TaskAvailability.OK;

    private static final Integer DEFAULT_FRAME = 1;
    private static final Integer UPDATED_FRAME = 2;

    private static final Integer DEFAULT_ACTUAL_OBJECT = 1;
    private static final Integer UPDATED_ACTUAL_OBJECT = 2;

    private static final OPStatus DEFAULT_OP_STATUS = OPStatus.NA;
    private static final OPStatus UPDATED_OP_STATUS = OPStatus.OPEN;

    private static final Instant DEFAULT_OP_ESTIMATE_START_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_OP_ESTIMATE_START_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_OP_ESTIMATE_END_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_OP_ESTIMATE_END_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_OP_START_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_OP_START_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_OP_END_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_OP_END_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final ReviewStatus DEFAULT_REVIEW_1_STATUS = ReviewStatus.NA;
    private static final ReviewStatus UPDATED_REVIEW_1_STATUS = ReviewStatus.OPEN;

    private static final Instant DEFAULT_REVIEW_1_START_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_REVIEW_1_START_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_REVIEW_1_END_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_REVIEW_1_END_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final FixStatus DEFAULT_FIX_STATUS = FixStatus.NA;
    private static final FixStatus UPDATED_FIX_STATUS = FixStatus.OPEN;

    private static final Instant DEFAULT_FIX_START_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FIX_START_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_FIX_END_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FIX_END_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final ReviewStatus DEFAULT_REVIEW_2_STATUS = ReviewStatus.NA;
    private static final ReviewStatus UPDATED_REVIEW_2_STATUS = ReviewStatus.OPEN;

    private static final Instant DEFAULT_REVIEW_2_START_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_REVIEW_2_START_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_REVIEW_2_END_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_REVIEW_2_END_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final FIStatus DEFAULT_FI_STATUS = FIStatus.NA;
    private static final FIStatus UPDATED_FI_STATUS = FIStatus.OPEN;

    private static final Instant DEFAULT_FI_START_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FI_START_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_FI_END_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FI_END_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_DURATION = 1;
    private static final Integer UPDATED_DURATION = 2;

    private static final Integer DEFAULT_TARGET = 1;
    private static final Integer UPDATED_TARGET = 2;

    private static final Integer DEFAULT_ERROR_QUANTITY = 1;
    private static final Integer UPDATED_ERROR_QUANTITY = 2;

    private static final ErrorSeverity DEFAULT_ERROR_SEVERITY = ErrorSeverity.NA;
    private static final ErrorSeverity UPDATED_ERROR_SEVERITY = ErrorSeverity.TRIVIAL;

    private static final TaskStatus DEFAULT_STATUS = TaskStatus.NA;
    private static final TaskStatus UPDATED_STATUS = TaskStatus.OPEN;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Long DEFAULT_PARENT = 1L;
    private static final Long UPDATED_PARENT = 2L;

    private static final String DEFAULT_OP = "AAAAAAAAAA";
    private static final String UPDATED_OP = "BBBBBBBBBB";

    private static final String DEFAULT_REVIEW_1 = "AAAAAAAAAA";
    private static final String UPDATED_REVIEW_1 = "BBBBBBBBBB";

    private static final String DEFAULT_REVIEW_2 = "AAAAAAAAAA";
    private static final String UPDATED_REVIEW_2 = "BBBBBBBBBB";

    private static final String DEFAULT_FIXER = "AAAAAAAAAA";
    private static final String UPDATED_FIXER = "BBBBBBBBBB";

    private static final String DEFAULT_FI = "AAAAAAAAAA";
    private static final String UPDATED_FI = "BBBBBBBBBB";

    @Autowired
    private TasksRepository tasksRepository;

    @Autowired
    private TasksMapper tasksMapper;

    @Autowired
    private TasksService tasksService;

    @Autowired
    private TasksSearchRepository tasksSearchRepository;

    @Autowired
    private TasksQueryService tasksQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTasksMockMvc;

    private Tasks tasks;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TasksResource tasksResource = new TasksResource(tasksService, tasksQueryService);
        this.restTasksMockMvc = MockMvcBuilders.standaloneSetup(tasksResource)
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
    public static Tasks createEntity(EntityManager em) {
        Tasks tasks = new Tasks()
            .name(DEFAULT_NAME)
            .severity(DEFAULT_SEVERITY)
            .priority(DEFAULT_PRIORITY)
            .data(DEFAULT_DATA)
            .fileName(DEFAULT_FILE_NAME)
            .type(DEFAULT_TYPE)
            .availability(DEFAULT_AVAILABILITY)
            .frame(DEFAULT_FRAME)
            .actualObject(DEFAULT_ACTUAL_OBJECT)
            .opStatus(DEFAULT_OP_STATUS)
            .estimateStartTime(DEFAULT_OP_ESTIMATE_START_TIME)
            .estimateEndTime(DEFAULT_OP_ESTIMATE_END_TIME)
            .opStartTime(DEFAULT_OP_START_TIME)
            .opEndTime(DEFAULT_OP_END_TIME)
            .review1Status(DEFAULT_REVIEW_1_STATUS)
            .review1StartTime(DEFAULT_REVIEW_1_START_TIME)
            .review1EndTime(DEFAULT_REVIEW_1_END_TIME)
            .fixStatus(DEFAULT_FIX_STATUS)
            .fixStartTime(DEFAULT_FIX_START_TIME)
            .fixEndTime(DEFAULT_FIX_END_TIME)
            .review2Status(DEFAULT_REVIEW_2_STATUS)
            .review2StartTime(DEFAULT_REVIEW_2_START_TIME)
            .review2EndTime(DEFAULT_REVIEW_2_END_TIME)
            .fiStatus(DEFAULT_FI_STATUS)
            .fiStartTime(DEFAULT_FI_START_TIME)
            .fiEndTime(DEFAULT_FI_END_TIME)
            .duration(DEFAULT_DURATION)
            .target(DEFAULT_TARGET)
            .errorQuantity(DEFAULT_ERROR_QUANTITY)
            .errorSeverity(DEFAULT_ERROR_SEVERITY)
            .status(DEFAULT_STATUS)
            .description(DEFAULT_DESCRIPTION)
            .parent(DEFAULT_PARENT)
            .op(DEFAULT_OP)
            .review1(DEFAULT_REVIEW_1)
            .review2(DEFAULT_REVIEW_2)
            .fixer(DEFAULT_FIXER)
            .fi(DEFAULT_FI);
        return tasks;
    }

    @Before
    public void initTest() {
        tasksSearchRepository.deleteAll();
        tasks = createEntity(em);
    }

    @Test
    @Transactional
    public void createTasks() throws Exception {
        int databaseSizeBeforeCreate = tasksRepository.findAll().size();

        // Create the Tasks
        TasksDTO tasksDTO = tasksMapper.toDto(tasks);
        restTasksMockMvc.perform(post("/api/tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tasksDTO)))
            .andExpect(status().isCreated());

        // Validate the Tasks in the database
        List<Tasks> tasksList = tasksRepository.findAll();
        assertThat(tasksList).hasSize(databaseSizeBeforeCreate + 1);
        Tasks testTasks = tasksList.get(tasksList.size() - 1);
        assertThat(testTasks.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTasks.getSeverity()).isEqualTo(DEFAULT_SEVERITY);
        assertThat(testTasks.getPriority()).isEqualTo(DEFAULT_PRIORITY);
        assertThat(testTasks.getData()).isEqualTo(DEFAULT_DATA);
        assertThat(testTasks.getFileName()).isEqualTo(DEFAULT_FILE_NAME);
        assertThat(testTasks.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testTasks.getAvailability()).isEqualTo(DEFAULT_AVAILABILITY);
        assertThat(testTasks.getFrame()).isEqualTo(DEFAULT_FRAME);
        assertThat(testTasks.getActualObject()).isEqualTo(DEFAULT_ACTUAL_OBJECT);
        assertThat(testTasks.getOpStatus()).isEqualTo(DEFAULT_OP_STATUS);
        assertThat(testTasks.getEstimateStartTime()).isEqualTo(DEFAULT_OP_ESTIMATE_START_TIME);
        assertThat(testTasks.getEstimateEndTime()).isEqualTo(DEFAULT_OP_ESTIMATE_END_TIME);
        assertThat(testTasks.getOpStartTime()).isEqualTo(DEFAULT_OP_START_TIME);
        assertThat(testTasks.getOpEndTime()).isEqualTo(DEFAULT_OP_END_TIME);
        assertThat(testTasks.getReview1Status()).isEqualTo(DEFAULT_REVIEW_1_STATUS);
        assertThat(testTasks.getReview1StartTime()).isEqualTo(DEFAULT_REVIEW_1_START_TIME);
        assertThat(testTasks.getReview1EndTime()).isEqualTo(DEFAULT_REVIEW_1_END_TIME);
        assertThat(testTasks.getFixStatus()).isEqualTo(DEFAULT_FIX_STATUS);
        assertThat(testTasks.getFixStartTime()).isEqualTo(DEFAULT_FIX_START_TIME);
        assertThat(testTasks.getFixEndTime()).isEqualTo(DEFAULT_FIX_END_TIME);
        assertThat(testTasks.getReview2Status()).isEqualTo(DEFAULT_REVIEW_2_STATUS);
        assertThat(testTasks.getReview2StartTime()).isEqualTo(DEFAULT_REVIEW_2_START_TIME);
        assertThat(testTasks.getReview2EndTime()).isEqualTo(DEFAULT_REVIEW_2_END_TIME);
        assertThat(testTasks.getFiStatus()).isEqualTo(DEFAULT_FI_STATUS);
        assertThat(testTasks.getFiStartTime()).isEqualTo(DEFAULT_FI_START_TIME);
        assertThat(testTasks.getFiEndTime()).isEqualTo(DEFAULT_FI_END_TIME);
        assertThat(testTasks.getDuration()).isEqualTo(DEFAULT_DURATION);
        assertThat(testTasks.getTarget()).isEqualTo(DEFAULT_TARGET);
        assertThat(testTasks.getErrorQuantity()).isEqualTo(DEFAULT_ERROR_QUANTITY);
        assertThat(testTasks.getErrorSeverity()).isEqualTo(DEFAULT_ERROR_SEVERITY);
        assertThat(testTasks.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testTasks.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testTasks.getParent()).isEqualTo(DEFAULT_PARENT);
        assertThat(testTasks.getOp()).isEqualTo(DEFAULT_OP);
        assertThat(testTasks.getReview1()).isEqualTo(DEFAULT_REVIEW_1);
        assertThat(testTasks.getReview2()).isEqualTo(DEFAULT_REVIEW_2);
        assertThat(testTasks.getFixer()).isEqualTo(DEFAULT_FIXER);
        assertThat(testTasks.getFi()).isEqualTo(DEFAULT_FI);

        // Validate the Tasks in Elasticsearch
        Tasks tasksEs = tasksSearchRepository.findOne(testTasks.getId());
        assertThat(tasksEs).isEqualToIgnoringGivenFields(testTasks);
    }

    @Test
    @Transactional
    public void createTasksWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = tasksRepository.findAll().size();

        // Create the Tasks with an existing ID
        tasks.setId(1L);
        TasksDTO tasksDTO = tasksMapper.toDto(tasks);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTasksMockMvc.perform(post("/api/tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tasksDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Tasks in the database
        List<Tasks> tasksList = tasksRepository.findAll();
        assertThat(tasksList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = tasksRepository.findAll().size();
        // set the field null
        tasks.setName(null);

        // Create the Tasks, which fails.
        TasksDTO tasksDTO = tasksMapper.toDto(tasks);

        restTasksMockMvc.perform(post("/api/tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tasksDTO)))
            .andExpect(status().isBadRequest());

        List<Tasks> tasksList = tasksRepository.findAll();
        assertThat(tasksList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTasks() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList
        restTasksMockMvc.perform(get("/api/tasks?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tasks.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].severity").value(hasItem(DEFAULT_SEVERITY.toString())))
            .andExpect(jsonPath("$.[*].priority").value(hasItem(DEFAULT_PRIORITY.toString())))
            .andExpect(jsonPath("$.[*].data").value(hasItem(DEFAULT_DATA.toString())))
            .andExpect(jsonPath("$.[*].fileName").value(hasItem(DEFAULT_FILE_NAME.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].availability").value(hasItem(DEFAULT_AVAILABILITY.toString())))
            .andExpect(jsonPath("$.[*].frame").value(hasItem(DEFAULT_FRAME)))
            .andExpect(jsonPath("$.[*].actualObject").value(hasItem(DEFAULT_ACTUAL_OBJECT)))
            .andExpect(jsonPath("$.[*].opStatus").value(hasItem(DEFAULT_OP_STATUS.toString())))
            .andExpect(jsonPath("$.[*].estimateStartTime").value(hasItem(DEFAULT_OP_ESTIMATE_START_TIME.toString())))
            .andExpect(jsonPath("$.[*].estimateEndTime").value(hasItem(DEFAULT_OP_ESTIMATE_END_TIME.toString())))
            .andExpect(jsonPath("$.[*].opStartTime").value(hasItem(DEFAULT_OP_START_TIME.toString())))
            .andExpect(jsonPath("$.[*].opEndTime").value(hasItem(DEFAULT_OP_END_TIME.toString())))
            .andExpect(jsonPath("$.[*].review1Status").value(hasItem(DEFAULT_REVIEW_1_STATUS.toString())))
            .andExpect(jsonPath("$.[*].review1StartTime").value(hasItem(DEFAULT_REVIEW_1_START_TIME.toString())))
            .andExpect(jsonPath("$.[*].review1EndTime").value(hasItem(DEFAULT_REVIEW_1_END_TIME.toString())))
            .andExpect(jsonPath("$.[*].fixStatus").value(hasItem(DEFAULT_FIX_STATUS.toString())))
            .andExpect(jsonPath("$.[*].fixStartTime").value(hasItem(DEFAULT_FIX_START_TIME.toString())))
            .andExpect(jsonPath("$.[*].fixEndTime").value(hasItem(DEFAULT_FIX_END_TIME.toString())))
            .andExpect(jsonPath("$.[*].review2Status").value(hasItem(DEFAULT_REVIEW_2_STATUS.toString())))
            .andExpect(jsonPath("$.[*].review2StartTime").value(hasItem(DEFAULT_REVIEW_2_START_TIME.toString())))
            .andExpect(jsonPath("$.[*].review2EndTime").value(hasItem(DEFAULT_REVIEW_2_END_TIME.toString())))
            .andExpect(jsonPath("$.[*].fiStatus").value(hasItem(DEFAULT_FI_STATUS.toString())))
            .andExpect(jsonPath("$.[*].fiStartTime").value(hasItem(DEFAULT_FI_START_TIME.toString())))
            .andExpect(jsonPath("$.[*].fiEndTime").value(hasItem(DEFAULT_FI_END_TIME.toString())))
            .andExpect(jsonPath("$.[*].duration").value(hasItem(DEFAULT_DURATION)))
            .andExpect(jsonPath("$.[*].target").value(hasItem(DEFAULT_TARGET)))
            .andExpect(jsonPath("$.[*].errorQuantity").value(hasItem(DEFAULT_ERROR_QUANTITY)))
            .andExpect(jsonPath("$.[*].errorSeverity").value(hasItem(DEFAULT_ERROR_SEVERITY.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].parent").value(hasItem(DEFAULT_PARENT.intValue())))
            .andExpect(jsonPath("$.[*].op").value(hasItem(DEFAULT_OP.toString())))
            .andExpect(jsonPath("$.[*].review1").value(hasItem(DEFAULT_REVIEW_1.toString())))
            .andExpect(jsonPath("$.[*].review2").value(hasItem(DEFAULT_REVIEW_2.toString())))
            .andExpect(jsonPath("$.[*].fixer").value(hasItem(DEFAULT_FIXER.toString())))
            .andExpect(jsonPath("$.[*].fi").value(hasItem(DEFAULT_FI.toString())));
    }

    @Test
    @Transactional
    public void getTasks() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get the tasks
        restTasksMockMvc.perform(get("/api/tasks/{id}", tasks.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(tasks.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.severity").value(DEFAULT_SEVERITY.toString()))
            .andExpect(jsonPath("$.priority").value(DEFAULT_PRIORITY.toString()))
            .andExpect(jsonPath("$.data").value(DEFAULT_DATA.toString()))
            .andExpect(jsonPath("$.fileName").value(DEFAULT_FILE_NAME.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.availability").value(DEFAULT_AVAILABILITY.toString()))
            .andExpect(jsonPath("$.frame").value(DEFAULT_FRAME))
            .andExpect(jsonPath("$.actualObject").value(DEFAULT_ACTUAL_OBJECT))
            .andExpect(jsonPath("$.opStatus").value(DEFAULT_OP_STATUS.toString()))
            .andExpect(jsonPath("$.estimateStartTime").value(DEFAULT_OP_ESTIMATE_START_TIME.toString()))
            .andExpect(jsonPath("$.estimateEndTime").value(DEFAULT_OP_ESTIMATE_END_TIME.toString()))
            .andExpect(jsonPath("$.opStartTime").value(DEFAULT_OP_START_TIME.toString()))
            .andExpect(jsonPath("$.opEndTime").value(DEFAULT_OP_END_TIME.toString()))
            .andExpect(jsonPath("$.review1Status").value(DEFAULT_REVIEW_1_STATUS.toString()))
            .andExpect(jsonPath("$.review1StartTime").value(DEFAULT_REVIEW_1_START_TIME.toString()))
            .andExpect(jsonPath("$.review1EndTime").value(DEFAULT_REVIEW_1_END_TIME.toString()))
            .andExpect(jsonPath("$.fixStatus").value(DEFAULT_FIX_STATUS.toString()))
            .andExpect(jsonPath("$.fixStartTime").value(DEFAULT_FIX_START_TIME.toString()))
            .andExpect(jsonPath("$.fixEndTime").value(DEFAULT_FIX_END_TIME.toString()))
            .andExpect(jsonPath("$.review2Status").value(DEFAULT_REVIEW_2_STATUS.toString()))
            .andExpect(jsonPath("$.review2StartTime").value(DEFAULT_REVIEW_2_START_TIME.toString()))
            .andExpect(jsonPath("$.review2EndTime").value(DEFAULT_REVIEW_2_END_TIME.toString()))
            .andExpect(jsonPath("$.fiStatus").value(DEFAULT_FI_STATUS.toString()))
            .andExpect(jsonPath("$.fiStartTime").value(DEFAULT_FI_START_TIME.toString()))
            .andExpect(jsonPath("$.fiEndTime").value(DEFAULT_FI_END_TIME.toString()))
            .andExpect(jsonPath("$.duration").value(DEFAULT_DURATION))
            .andExpect(jsonPath("$.target").value(DEFAULT_TARGET))
            .andExpect(jsonPath("$.errorQuantity").value(DEFAULT_ERROR_QUANTITY))
            .andExpect(jsonPath("$.errorSeverity").value(DEFAULT_ERROR_SEVERITY.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.parent").value(DEFAULT_PARENT.intValue()))
            .andExpect(jsonPath("$.op").value(DEFAULT_OP.toString()))
            .andExpect(jsonPath("$.review1").value(DEFAULT_REVIEW_1.toString()))
            .andExpect(jsonPath("$.review2").value(DEFAULT_REVIEW_2.toString()))
            .andExpect(jsonPath("$.fixer").value(DEFAULT_FIXER.toString()))
            .andExpect(jsonPath("$.fi").value(DEFAULT_FI.toString()));
    }

    @Test
    @Transactional
    public void getAllTasksByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where name equals to DEFAULT_NAME
        defaultTasksShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the tasksList where name equals to UPDATED_NAME
        defaultTasksShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllTasksByNameIsInShouldWork() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where name in DEFAULT_NAME or UPDATED_NAME
        defaultTasksShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the tasksList where name equals to UPDATED_NAME
        defaultTasksShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllTasksByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where name is not null
        defaultTasksShouldBeFound("name.specified=true");

        // Get all the tasksList where name is null
        defaultTasksShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllTasksBySeverityIsEqualToSomething() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where severity equals to DEFAULT_SEVERITY
        defaultTasksShouldBeFound("severity.equals=" + DEFAULT_SEVERITY);

        // Get all the tasksList where severity equals to UPDATED_SEVERITY
        defaultTasksShouldNotBeFound("severity.equals=" + UPDATED_SEVERITY);
    }

    @Test
    @Transactional
    public void getAllTasksBySeverityIsInShouldWork() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where severity in DEFAULT_SEVERITY or UPDATED_SEVERITY
        defaultTasksShouldBeFound("severity.in=" + DEFAULT_SEVERITY + "," + UPDATED_SEVERITY);

        // Get all the tasksList where severity equals to UPDATED_SEVERITY
        defaultTasksShouldNotBeFound("severity.in=" + UPDATED_SEVERITY);
    }

    @Test
    @Transactional
    public void getAllTasksBySeverityIsNullOrNotNull() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where severity is not null
        defaultTasksShouldBeFound("severity.specified=true");

        // Get all the tasksList where severity is null
        defaultTasksShouldNotBeFound("severity.specified=false");
    }

    @Test
    @Transactional
    public void getAllTasksByPriorityIsEqualToSomething() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where priority equals to DEFAULT_PRIORITY
        defaultTasksShouldBeFound("priority.equals=" + DEFAULT_PRIORITY);

        // Get all the tasksList where priority equals to UPDATED_PRIORITY
        defaultTasksShouldNotBeFound("priority.equals=" + UPDATED_PRIORITY);
    }

    @Test
    @Transactional
    public void getAllTasksByPriorityIsInShouldWork() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where priority in DEFAULT_PRIORITY or UPDATED_PRIORITY
        defaultTasksShouldBeFound("priority.in=" + DEFAULT_PRIORITY + "," + UPDATED_PRIORITY);

        // Get all the tasksList where priority equals to UPDATED_PRIORITY
        defaultTasksShouldNotBeFound("priority.in=" + UPDATED_PRIORITY);
    }

    @Test
    @Transactional
    public void getAllTasksByPriorityIsNullOrNotNull() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where priority is not null
        defaultTasksShouldBeFound("priority.specified=true");

        // Get all the tasksList where priority is null
        defaultTasksShouldNotBeFound("priority.specified=false");
    }

    @Test
    @Transactional
    public void getAllTasksByDataIsEqualToSomething() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where data equals to DEFAULT_DATA
        defaultTasksShouldBeFound("data.equals=" + DEFAULT_DATA);

        // Get all the tasksList where data equals to UPDATED_DATA
        defaultTasksShouldNotBeFound("data.equals=" + UPDATED_DATA);
    }

    @Test
    @Transactional
    public void getAllTasksByDataIsInShouldWork() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where data in DEFAULT_DATA or UPDATED_DATA
        defaultTasksShouldBeFound("data.in=" + DEFAULT_DATA + "," + UPDATED_DATA);

        // Get all the tasksList where data equals to UPDATED_DATA
        defaultTasksShouldNotBeFound("data.in=" + UPDATED_DATA);
    }

    @Test
    @Transactional
    public void getAllTasksByDataIsNullOrNotNull() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where data is not null
        defaultTasksShouldBeFound("data.specified=true");

        // Get all the tasksList where data is null
        defaultTasksShouldNotBeFound("data.specified=false");
    }

    @Test
    @Transactional
    public void getAllTasksByFileNameIsEqualToSomething() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where fileName equals to DEFAULT_FILE_NAME
        defaultTasksShouldBeFound("fileName.equals=" + DEFAULT_FILE_NAME);

        // Get all the tasksList where fileName equals to UPDATED_FILE_NAME
        defaultTasksShouldNotBeFound("fileName.equals=" + UPDATED_FILE_NAME);
    }

    @Test
    @Transactional
    public void getAllTasksByFileNameIsInShouldWork() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where fileName in DEFAULT_FILE_NAME or UPDATED_FILE_NAME
        defaultTasksShouldBeFound("fileName.in=" + DEFAULT_FILE_NAME + "," + UPDATED_FILE_NAME);

        // Get all the tasksList where fileName equals to UPDATED_FILE_NAME
        defaultTasksShouldNotBeFound("fileName.in=" + UPDATED_FILE_NAME);
    }

    @Test
    @Transactional
    public void getAllTasksByFileNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where fileName is not null
        defaultTasksShouldBeFound("fileName.specified=true");

        // Get all the tasksList where fileName is null
        defaultTasksShouldNotBeFound("fileName.specified=false");
    }

    @Test
    @Transactional
    public void getAllTasksByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where type equals to DEFAULT_TYPE
        defaultTasksShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the tasksList where type equals to UPDATED_TYPE
        defaultTasksShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllTasksByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultTasksShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the tasksList where type equals to UPDATED_TYPE
        defaultTasksShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllTasksByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where type is not null
        defaultTasksShouldBeFound("type.specified=true");

        // Get all the tasksList where type is null
        defaultTasksShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    public void getAllTasksByAvailabilityIsEqualToSomething() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where availability equals to DEFAULT_AVAILABILITY
        defaultTasksShouldBeFound("availability.equals=" + DEFAULT_AVAILABILITY);

        // Get all the tasksList where availability equals to UPDATED_AVAILABILITY
        defaultTasksShouldNotBeFound("availability.equals=" + UPDATED_AVAILABILITY);
    }

    @Test
    @Transactional
    public void getAllTasksByAvailabilityIsInShouldWork() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where availability in DEFAULT_AVAILABILITY or UPDATED_AVAILABILITY
        defaultTasksShouldBeFound("availability.in=" + DEFAULT_AVAILABILITY + "," + UPDATED_AVAILABILITY);

        // Get all the tasksList where availability equals to UPDATED_AVAILABILITY
        defaultTasksShouldNotBeFound("availability.in=" + UPDATED_AVAILABILITY);
    }

    @Test
    @Transactional
    public void getAllTasksByAvailabilityIsNullOrNotNull() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where availability is not null
        defaultTasksShouldBeFound("availability.specified=true");

        // Get all the tasksList where availability is null
        defaultTasksShouldNotBeFound("availability.specified=false");
    }

    @Test
    @Transactional
    public void getAllTasksByFrameIsEqualToSomething() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where frame equals to DEFAULT_FRAME
        defaultTasksShouldBeFound("frame.equals=" + DEFAULT_FRAME);

        // Get all the tasksList where frame equals to UPDATED_FRAME
        defaultTasksShouldNotBeFound("frame.equals=" + UPDATED_FRAME);
    }

    @Test
    @Transactional
    public void getAllTasksByFrameIsInShouldWork() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where frame in DEFAULT_FRAME or UPDATED_FRAME
        defaultTasksShouldBeFound("frame.in=" + DEFAULT_FRAME + "," + UPDATED_FRAME);

        // Get all the tasksList where frame equals to UPDATED_FRAME
        defaultTasksShouldNotBeFound("frame.in=" + UPDATED_FRAME);
    }

    @Test
    @Transactional
    public void getAllTasksByFrameIsNullOrNotNull() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where frame is not null
        defaultTasksShouldBeFound("frame.specified=true");

        // Get all the tasksList where frame is null
        defaultTasksShouldNotBeFound("frame.specified=false");
    }

    @Test
    @Transactional
    public void getAllTasksByFrameIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where frame greater than or equals to DEFAULT_FRAME
        defaultTasksShouldBeFound("frame.greaterOrEqualThan=" + DEFAULT_FRAME);

        // Get all the tasksList where frame greater than or equals to UPDATED_FRAME
        defaultTasksShouldNotBeFound("frame.greaterOrEqualThan=" + UPDATED_FRAME);
    }

    @Test
    @Transactional
    public void getAllTasksByFrameIsLessThanSomething() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where frame less than or equals to DEFAULT_FRAME
        defaultTasksShouldNotBeFound("frame.lessThan=" + DEFAULT_FRAME);

        // Get all the tasksList where frame less than or equals to UPDATED_FRAME
        defaultTasksShouldBeFound("frame.lessThan=" + UPDATED_FRAME);
    }


    @Test
    @Transactional
    public void getAllTasksByActualObjectIsEqualToSomething() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where actualObject equals to DEFAULT_ACTUAL_OBJECT
        defaultTasksShouldBeFound("actualObject.equals=" + DEFAULT_ACTUAL_OBJECT);

        // Get all the tasksList where actualObject equals to UPDATED_ACTUAL_OBJECT
        defaultTasksShouldNotBeFound("actualObject.equals=" + UPDATED_ACTUAL_OBJECT);
    }

    @Test
    @Transactional
    public void getAllTasksByActualObjectIsInShouldWork() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where actualObject in DEFAULT_ACTUAL_OBJECT or UPDATED_ACTUAL_OBJECT
        defaultTasksShouldBeFound("actualObject.in=" + DEFAULT_ACTUAL_OBJECT + "," + UPDATED_ACTUAL_OBJECT);

        // Get all the tasksList where actualObject equals to UPDATED_ACTUAL_OBJECT
        defaultTasksShouldNotBeFound("actualObject.in=" + UPDATED_ACTUAL_OBJECT);
    }

    @Test
    @Transactional
    public void getAllTasksByActualObjectIsNullOrNotNull() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where actualObject is not null
        defaultTasksShouldBeFound("actualObject.specified=true");

        // Get all the tasksList where actualObject is null
        defaultTasksShouldNotBeFound("actualObject.specified=false");
    }

    @Test
    @Transactional
    public void getAllTasksByActualObjectIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where actualObject greater than or equals to DEFAULT_ACTUAL_OBJECT
        defaultTasksShouldBeFound("actualObject.greaterOrEqualThan=" + DEFAULT_ACTUAL_OBJECT);

        // Get all the tasksList where actualObject greater than or equals to UPDATED_ACTUAL_OBJECT
        defaultTasksShouldNotBeFound("actualObject.greaterOrEqualThan=" + UPDATED_ACTUAL_OBJECT);
    }

    @Test
    @Transactional
    public void getAllTasksByActualObjectIsLessThanSomething() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where actualObject less than or equals to DEFAULT_ACTUAL_OBJECT
        defaultTasksShouldNotBeFound("actualObject.lessThan=" + DEFAULT_ACTUAL_OBJECT);

        // Get all the tasksList where actualObject less than or equals to UPDATED_ACTUAL_OBJECT
        defaultTasksShouldBeFound("actualObject.lessThan=" + UPDATED_ACTUAL_OBJECT);
    }


    @Test
    @Transactional
    public void getAllTasksByOpStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where opStatus equals to DEFAULT_OP_STATUS
        defaultTasksShouldBeFound("opStatus.equals=" + DEFAULT_OP_STATUS);

        // Get all the tasksList where opStatus equals to UPDATED_OP_STATUS
        defaultTasksShouldNotBeFound("opStatus.equals=" + UPDATED_OP_STATUS);
    }

    @Test
    @Transactional
    public void getAllTasksByOpStatusIsInShouldWork() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where opStatus in DEFAULT_OP_STATUS or UPDATED_OP_STATUS
        defaultTasksShouldBeFound("opStatus.in=" + DEFAULT_OP_STATUS + "," + UPDATED_OP_STATUS);

        // Get all the tasksList where opStatus equals to UPDATED_OP_STATUS
        defaultTasksShouldNotBeFound("opStatus.in=" + UPDATED_OP_STATUS);
    }

    @Test
    @Transactional
    public void getAllTasksByOpStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where opStatus is not null
        defaultTasksShouldBeFound("opStatus.specified=true");

        // Get all the tasksList where opStatus is null
        defaultTasksShouldNotBeFound("opStatus.specified=false");
    }

    @Test
    @Transactional
    public void getAllTasksByEstimateStartTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where estimateStartTime equals to DEFAULT_OP_ESTIMATE_START_TIME
        defaultTasksShouldBeFound("estimateStartTime.equals=" + DEFAULT_OP_ESTIMATE_START_TIME);

        // Get all the tasksList where estimateStartTime equals to UPDATED_OP_ESTIMATE_START_TIME
        defaultTasksShouldNotBeFound("estimateStartTime.equals=" + UPDATED_OP_ESTIMATE_START_TIME);
    }

    @Test
    @Transactional
    public void getAllTasksByEstimateStartTimeIsInShouldWork() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where estimateStartTime in DEFAULT_OP_ESTIMATE_START_TIME or UPDATED_OP_ESTIMATE_START_TIME
        defaultTasksShouldBeFound("estimateStartTime.in=" + DEFAULT_OP_ESTIMATE_START_TIME + "," + UPDATED_OP_ESTIMATE_START_TIME);

        // Get all the tasksList where estimateStartTime equals to UPDATED_OP_ESTIMATE_START_TIME
        defaultTasksShouldNotBeFound("estimateStartTime.in=" + UPDATED_OP_ESTIMATE_START_TIME);
    }

    @Test
    @Transactional
    public void getAllTasksByEstimateStartTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where estimateStartTime is not null
        defaultTasksShouldBeFound("estimateStartTime.specified=true");

        // Get all the tasksList where estimateStartTime is null
        defaultTasksShouldNotBeFound("estimateStartTime.specified=false");
    }

    @Test
    @Transactional
    public void getAllTasksByEstimateEndTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where estimateEndTime equals to DEFAULT_OP_ESTIMATE_END_TIME
        defaultTasksShouldBeFound("estimateEndTime.equals=" + DEFAULT_OP_ESTIMATE_END_TIME);

        // Get all the tasksList where estimateEndTime equals to UPDATED_OP_ESTIMATE_END_TIME
        defaultTasksShouldNotBeFound("estimateEndTime.equals=" + UPDATED_OP_ESTIMATE_END_TIME);
    }

    @Test
    @Transactional
    public void getAllTasksByEstimateEndTimeIsInShouldWork() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where estimateEndTime in DEFAULT_OP_ESTIMATE_END_TIME or UPDATED_OP_ESTIMATE_END_TIME
        defaultTasksShouldBeFound("estimateEndTime.in=" + DEFAULT_OP_ESTIMATE_END_TIME + "," + UPDATED_OP_ESTIMATE_END_TIME);

        // Get all the tasksList where estimateEndTime equals to UPDATED_OP_ESTIMATE_END_TIME
        defaultTasksShouldNotBeFound("estimateEndTime.in=" + UPDATED_OP_ESTIMATE_END_TIME);
    }

    @Test
    @Transactional
    public void getAllTasksByEstimateEndTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where estimateEndTime is not null
        defaultTasksShouldBeFound("estimateEndTime.specified=true");

        // Get all the tasksList where estimateEndTime is null
        defaultTasksShouldNotBeFound("estimateEndTime.specified=false");
    }

    @Test
    @Transactional
    public void getAllTasksByOpStartTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where opStartTime equals to DEFAULT_OP_START_TIME
        defaultTasksShouldBeFound("opStartTime.equals=" + DEFAULT_OP_START_TIME);

        // Get all the tasksList where opStartTime equals to UPDATED_OP_START_TIME
        defaultTasksShouldNotBeFound("opStartTime.equals=" + UPDATED_OP_START_TIME);
    }

    @Test
    @Transactional
    public void getAllTasksByOpStartTimeIsInShouldWork() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where opStartTime in DEFAULT_OP_START_TIME or UPDATED_OP_START_TIME
        defaultTasksShouldBeFound("opStartTime.in=" + DEFAULT_OP_START_TIME + "," + UPDATED_OP_START_TIME);

        // Get all the tasksList where opStartTime equals to UPDATED_OP_START_TIME
        defaultTasksShouldNotBeFound("opStartTime.in=" + UPDATED_OP_START_TIME);
    }

    @Test
    @Transactional
    public void getAllTasksByOpStartTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where opStartTime is not null
        defaultTasksShouldBeFound("opStartTime.specified=true");

        // Get all the tasksList where opStartTime is null
        defaultTasksShouldNotBeFound("opStartTime.specified=false");
    }

    @Test
    @Transactional
    public void getAllTasksByOpEndTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where opEndTime equals to DEFAULT_OP_END_TIME
        defaultTasksShouldBeFound("opEndTime.equals=" + DEFAULT_OP_END_TIME);

        // Get all the tasksList where opEndTime equals to UPDATED_OP_END_TIME
        defaultTasksShouldNotBeFound("opEndTime.equals=" + UPDATED_OP_END_TIME);
    }

    @Test
    @Transactional
    public void getAllTasksByOpEndTimeIsInShouldWork() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where opEndTime in DEFAULT_OP_END_TIME or UPDATED_OP_END_TIME
        defaultTasksShouldBeFound("opEndTime.in=" + DEFAULT_OP_END_TIME + "," + UPDATED_OP_END_TIME);

        // Get all the tasksList where opEndTime equals to UPDATED_OP_END_TIME
        defaultTasksShouldNotBeFound("opEndTime.in=" + UPDATED_OP_END_TIME);
    }

    @Test
    @Transactional
    public void getAllTasksByOpEndTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where opEndTime is not null
        defaultTasksShouldBeFound("opEndTime.specified=true");

        // Get all the tasksList where opEndTime is null
        defaultTasksShouldNotBeFound("opEndTime.specified=false");
    }

    @Test
    @Transactional
    public void getAllTasksByReview1StatusIsEqualToSomething() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where review1Status equals to DEFAULT_REVIEW_1_STATUS
        defaultTasksShouldBeFound("review1Status.equals=" + DEFAULT_REVIEW_1_STATUS);

        // Get all the tasksList where review1Status equals to UPDATED_REVIEW_1_STATUS
        defaultTasksShouldNotBeFound("review1Status.equals=" + UPDATED_REVIEW_1_STATUS);
    }

    @Test
    @Transactional
    public void getAllTasksByReview1StatusIsInShouldWork() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where review1Status in DEFAULT_REVIEW_1_STATUS or UPDATED_REVIEW_1_STATUS
        defaultTasksShouldBeFound("review1Status.in=" + DEFAULT_REVIEW_1_STATUS + "," + UPDATED_REVIEW_1_STATUS);

        // Get all the tasksList where review1Status equals to UPDATED_REVIEW_1_STATUS
        defaultTasksShouldNotBeFound("review1Status.in=" + UPDATED_REVIEW_1_STATUS);
    }

    @Test
    @Transactional
    public void getAllTasksByReview1StatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where review1Status is not null
        defaultTasksShouldBeFound("review1Status.specified=true");

        // Get all the tasksList where review1Status is null
        defaultTasksShouldNotBeFound("review1Status.specified=false");
    }

    @Test
    @Transactional
    public void getAllTasksByReview1StartTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where review1StartTime equals to DEFAULT_REVIEW_1_START_TIME
        defaultTasksShouldBeFound("review1StartTime.equals=" + DEFAULT_REVIEW_1_START_TIME);

        // Get all the tasksList where review1StartTime equals to UPDATED_REVIEW_1_START_TIME
        defaultTasksShouldNotBeFound("review1StartTime.equals=" + UPDATED_REVIEW_1_START_TIME);
    }

    @Test
    @Transactional
    public void getAllTasksByReview1StartTimeIsInShouldWork() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where review1StartTime in DEFAULT_REVIEW_1_START_TIME or UPDATED_REVIEW_1_START_TIME
        defaultTasksShouldBeFound("review1StartTime.in=" + DEFAULT_REVIEW_1_START_TIME + "," + UPDATED_REVIEW_1_START_TIME);

        // Get all the tasksList where review1StartTime equals to UPDATED_REVIEW_1_START_TIME
        defaultTasksShouldNotBeFound("review1StartTime.in=" + UPDATED_REVIEW_1_START_TIME);
    }

    @Test
    @Transactional
    public void getAllTasksByReview1StartTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where review1StartTime is not null
        defaultTasksShouldBeFound("review1StartTime.specified=true");

        // Get all the tasksList where review1StartTime is null
        defaultTasksShouldNotBeFound("review1StartTime.specified=false");
    }

    @Test
    @Transactional
    public void getAllTasksByReview1EndTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where review1EndTime equals to DEFAULT_REVIEW_1_END_TIME
        defaultTasksShouldBeFound("review1EndTime.equals=" + DEFAULT_REVIEW_1_END_TIME);

        // Get all the tasksList where review1EndTime equals to UPDATED_REVIEW_1_END_TIME
        defaultTasksShouldNotBeFound("review1EndTime.equals=" + UPDATED_REVIEW_1_END_TIME);
    }

    @Test
    @Transactional
    public void getAllTasksByReview1EndTimeIsInShouldWork() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where review1EndTime in DEFAULT_REVIEW_1_END_TIME or UPDATED_REVIEW_1_END_TIME
        defaultTasksShouldBeFound("review1EndTime.in=" + DEFAULT_REVIEW_1_END_TIME + "," + UPDATED_REVIEW_1_END_TIME);

        // Get all the tasksList where review1EndTime equals to UPDATED_REVIEW_1_END_TIME
        defaultTasksShouldNotBeFound("review1EndTime.in=" + UPDATED_REVIEW_1_END_TIME);
    }

    @Test
    @Transactional
    public void getAllTasksByReview1EndTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where review1EndTime is not null
        defaultTasksShouldBeFound("review1EndTime.specified=true");

        // Get all the tasksList where review1EndTime is null
        defaultTasksShouldNotBeFound("review1EndTime.specified=false");
    }

    @Test
    @Transactional
    public void getAllTasksByFixStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where fixStatus equals to DEFAULT_FIX_STATUS
        defaultTasksShouldBeFound("fixStatus.equals=" + DEFAULT_FIX_STATUS);

        // Get all the tasksList where fixStatus equals to UPDATED_FIX_STATUS
        defaultTasksShouldNotBeFound("fixStatus.equals=" + UPDATED_FIX_STATUS);
    }

    @Test
    @Transactional
    public void getAllTasksByFixStatusIsInShouldWork() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where fixStatus in DEFAULT_FIX_STATUS or UPDATED_FIX_STATUS
        defaultTasksShouldBeFound("fixStatus.in=" + DEFAULT_FIX_STATUS + "," + UPDATED_FIX_STATUS);

        // Get all the tasksList where fixStatus equals to UPDATED_FIX_STATUS
        defaultTasksShouldNotBeFound("fixStatus.in=" + UPDATED_FIX_STATUS);
    }

    @Test
    @Transactional
    public void getAllTasksByFixStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where fixStatus is not null
        defaultTasksShouldBeFound("fixStatus.specified=true");

        // Get all the tasksList where fixStatus is null
        defaultTasksShouldNotBeFound("fixStatus.specified=false");
    }

    @Test
    @Transactional
    public void getAllTasksByFixStartTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where fixStartTime equals to DEFAULT_FIX_START_TIME
        defaultTasksShouldBeFound("fixStartTime.equals=" + DEFAULT_FIX_START_TIME);

        // Get all the tasksList where fixStartTime equals to UPDATED_FIX_START_TIME
        defaultTasksShouldNotBeFound("fixStartTime.equals=" + UPDATED_FIX_START_TIME);
    }

    @Test
    @Transactional
    public void getAllTasksByFixStartTimeIsInShouldWork() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where fixStartTime in DEFAULT_FIX_START_TIME or UPDATED_FIX_START_TIME
        defaultTasksShouldBeFound("fixStartTime.in=" + DEFAULT_FIX_START_TIME + "," + UPDATED_FIX_START_TIME);

        // Get all the tasksList where fixStartTime equals to UPDATED_FIX_START_TIME
        defaultTasksShouldNotBeFound("fixStartTime.in=" + UPDATED_FIX_START_TIME);
    }

    @Test
    @Transactional
    public void getAllTasksByFixStartTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where fixStartTime is not null
        defaultTasksShouldBeFound("fixStartTime.specified=true");

        // Get all the tasksList where fixStartTime is null
        defaultTasksShouldNotBeFound("fixStartTime.specified=false");
    }

    @Test
    @Transactional
    public void getAllTasksByFixEndTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where fixEndTime equals to DEFAULT_FIX_END_TIME
        defaultTasksShouldBeFound("fixEndTime.equals=" + DEFAULT_FIX_END_TIME);

        // Get all the tasksList where fixEndTime equals to UPDATED_FIX_END_TIME
        defaultTasksShouldNotBeFound("fixEndTime.equals=" + UPDATED_FIX_END_TIME);
    }

    @Test
    @Transactional
    public void getAllTasksByFixEndTimeIsInShouldWork() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where fixEndTime in DEFAULT_FIX_END_TIME or UPDATED_FIX_END_TIME
        defaultTasksShouldBeFound("fixEndTime.in=" + DEFAULT_FIX_END_TIME + "," + UPDATED_FIX_END_TIME);

        // Get all the tasksList where fixEndTime equals to UPDATED_FIX_END_TIME
        defaultTasksShouldNotBeFound("fixEndTime.in=" + UPDATED_FIX_END_TIME);
    }

    @Test
    @Transactional
    public void getAllTasksByFixEndTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where fixEndTime is not null
        defaultTasksShouldBeFound("fixEndTime.specified=true");

        // Get all the tasksList where fixEndTime is null
        defaultTasksShouldNotBeFound("fixEndTime.specified=false");
    }

    @Test
    @Transactional
    public void getAllTasksByReview2StatusIsEqualToSomething() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where review2Status equals to DEFAULT_REVIEW_2_STATUS
        defaultTasksShouldBeFound("review2Status.equals=" + DEFAULT_REVIEW_2_STATUS);

        // Get all the tasksList where review2Status equals to UPDATED_REVIEW_2_STATUS
        defaultTasksShouldNotBeFound("review2Status.equals=" + UPDATED_REVIEW_2_STATUS);
    }

    @Test
    @Transactional
    public void getAllTasksByReview2StatusIsInShouldWork() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where review2Status in DEFAULT_REVIEW_2_STATUS or UPDATED_REVIEW_2_STATUS
        defaultTasksShouldBeFound("review2Status.in=" + DEFAULT_REVIEW_2_STATUS + "," + UPDATED_REVIEW_2_STATUS);

        // Get all the tasksList where review2Status equals to UPDATED_REVIEW_2_STATUS
        defaultTasksShouldNotBeFound("review2Status.in=" + UPDATED_REVIEW_2_STATUS);
    }

    @Test
    @Transactional
    public void getAllTasksByReview2StatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where review2Status is not null
        defaultTasksShouldBeFound("review2Status.specified=true");

        // Get all the tasksList where review2Status is null
        defaultTasksShouldNotBeFound("review2Status.specified=false");
    }

    @Test
    @Transactional
    public void getAllTasksByReview2StartTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where review2StartTime equals to DEFAULT_REVIEW_2_START_TIME
        defaultTasksShouldBeFound("review2StartTime.equals=" + DEFAULT_REVIEW_2_START_TIME);

        // Get all the tasksList where review2StartTime equals to UPDATED_REVIEW_2_START_TIME
        defaultTasksShouldNotBeFound("review2StartTime.equals=" + UPDATED_REVIEW_2_START_TIME);
    }

    @Test
    @Transactional
    public void getAllTasksByReview2StartTimeIsInShouldWork() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where review2StartTime in DEFAULT_REVIEW_2_START_TIME or UPDATED_REVIEW_2_START_TIME
        defaultTasksShouldBeFound("review2StartTime.in=" + DEFAULT_REVIEW_2_START_TIME + "," + UPDATED_REVIEW_2_START_TIME);

        // Get all the tasksList where review2StartTime equals to UPDATED_REVIEW_2_START_TIME
        defaultTasksShouldNotBeFound("review2StartTime.in=" + UPDATED_REVIEW_2_START_TIME);
    }

    @Test
    @Transactional
    public void getAllTasksByReview2StartTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where review2StartTime is not null
        defaultTasksShouldBeFound("review2StartTime.specified=true");

        // Get all the tasksList where review2StartTime is null
        defaultTasksShouldNotBeFound("review2StartTime.specified=false");
    }

    @Test
    @Transactional
    public void getAllTasksByReview2EndTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where review2EndTime equals to DEFAULT_REVIEW_2_END_TIME
        defaultTasksShouldBeFound("review2EndTime.equals=" + DEFAULT_REVIEW_2_END_TIME);

        // Get all the tasksList where review2EndTime equals to UPDATED_REVIEW_2_END_TIME
        defaultTasksShouldNotBeFound("review2EndTime.equals=" + UPDATED_REVIEW_2_END_TIME);
    }

    @Test
    @Transactional
    public void getAllTasksByReview2EndTimeIsInShouldWork() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where review2EndTime in DEFAULT_REVIEW_2_END_TIME or UPDATED_REVIEW_2_END_TIME
        defaultTasksShouldBeFound("review2EndTime.in=" + DEFAULT_REVIEW_2_END_TIME + "," + UPDATED_REVIEW_2_END_TIME);

        // Get all the tasksList where review2EndTime equals to UPDATED_REVIEW_2_END_TIME
        defaultTasksShouldNotBeFound("review2EndTime.in=" + UPDATED_REVIEW_2_END_TIME);
    }

    @Test
    @Transactional
    public void getAllTasksByReview2EndTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where review2EndTime is not null
        defaultTasksShouldBeFound("review2EndTime.specified=true");

        // Get all the tasksList where review2EndTime is null
        defaultTasksShouldNotBeFound("review2EndTime.specified=false");
    }

    @Test
    @Transactional
    public void getAllTasksByFiStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where fiStatus equals to DEFAULT_FI_STATUS
        defaultTasksShouldBeFound("fiStatus.equals=" + DEFAULT_FI_STATUS);

        // Get all the tasksList where fiStatus equals to UPDATED_FI_STATUS
        defaultTasksShouldNotBeFound("fiStatus.equals=" + UPDATED_FI_STATUS);
    }

    @Test
    @Transactional
    public void getAllTasksByFiStatusIsInShouldWork() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where fiStatus in DEFAULT_FI_STATUS or UPDATED_FI_STATUS
        defaultTasksShouldBeFound("fiStatus.in=" + DEFAULT_FI_STATUS + "," + UPDATED_FI_STATUS);

        // Get all the tasksList where fiStatus equals to UPDATED_FI_STATUS
        defaultTasksShouldNotBeFound("fiStatus.in=" + UPDATED_FI_STATUS);
    }

    @Test
    @Transactional
    public void getAllTasksByFiStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where fiStatus is not null
        defaultTasksShouldBeFound("fiStatus.specified=true");

        // Get all the tasksList where fiStatus is null
        defaultTasksShouldNotBeFound("fiStatus.specified=false");
    }

    @Test
    @Transactional
    public void getAllTasksByFiStartTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where fiStartTime equals to DEFAULT_FI_START_TIME
        defaultTasksShouldBeFound("fiStartTime.equals=" + DEFAULT_FI_START_TIME);

        // Get all the tasksList where fiStartTime equals to UPDATED_FI_START_TIME
        defaultTasksShouldNotBeFound("fiStartTime.equals=" + UPDATED_FI_START_TIME);
    }

    @Test
    @Transactional
    public void getAllTasksByFiStartTimeIsInShouldWork() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where fiStartTime in DEFAULT_FI_START_TIME or UPDATED_FI_START_TIME
        defaultTasksShouldBeFound("fiStartTime.in=" + DEFAULT_FI_START_TIME + "," + UPDATED_FI_START_TIME);

        // Get all the tasksList where fiStartTime equals to UPDATED_FI_START_TIME
        defaultTasksShouldNotBeFound("fiStartTime.in=" + UPDATED_FI_START_TIME);
    }

    @Test
    @Transactional
    public void getAllTasksByFiStartTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where fiStartTime is not null
        defaultTasksShouldBeFound("fiStartTime.specified=true");

        // Get all the tasksList where fiStartTime is null
        defaultTasksShouldNotBeFound("fiStartTime.specified=false");
    }

    @Test
    @Transactional
    public void getAllTasksByFiEndTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where fiEndTime equals to DEFAULT_FI_END_TIME
        defaultTasksShouldBeFound("fiEndTime.equals=" + DEFAULT_FI_END_TIME);

        // Get all the tasksList where fiEndTime equals to UPDATED_FI_END_TIME
        defaultTasksShouldNotBeFound("fiEndTime.equals=" + UPDATED_FI_END_TIME);
    }

    @Test
    @Transactional
    public void getAllTasksByFiEndTimeIsInShouldWork() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where fiEndTime in DEFAULT_FI_END_TIME or UPDATED_FI_END_TIME
        defaultTasksShouldBeFound("fiEndTime.in=" + DEFAULT_FI_END_TIME + "," + UPDATED_FI_END_TIME);

        // Get all the tasksList where fiEndTime equals to UPDATED_FI_END_TIME
        defaultTasksShouldNotBeFound("fiEndTime.in=" + UPDATED_FI_END_TIME);
    }

    @Test
    @Transactional
    public void getAllTasksByFiEndTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where fiEndTime is not null
        defaultTasksShouldBeFound("fiEndTime.specified=true");

        // Get all the tasksList where fiEndTime is null
        defaultTasksShouldNotBeFound("fiEndTime.specified=false");
    }

    @Test
    @Transactional
    public void getAllTasksByDurationIsEqualToSomething() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where duration equals to DEFAULT_DURATION
        defaultTasksShouldBeFound("duration.equals=" + DEFAULT_DURATION);

        // Get all the tasksList where duration equals to UPDATED_DURATION
        defaultTasksShouldNotBeFound("duration.equals=" + UPDATED_DURATION);
    }

    @Test
    @Transactional
    public void getAllTasksByDurationIsInShouldWork() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where duration in DEFAULT_DURATION or UPDATED_DURATION
        defaultTasksShouldBeFound("duration.in=" + DEFAULT_DURATION + "," + UPDATED_DURATION);

        // Get all the tasksList where duration equals to UPDATED_DURATION
        defaultTasksShouldNotBeFound("duration.in=" + UPDATED_DURATION);
    }

    @Test
    @Transactional
    public void getAllTasksByDurationIsNullOrNotNull() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where duration is not null
        defaultTasksShouldBeFound("duration.specified=true");

        // Get all the tasksList where duration is null
        defaultTasksShouldNotBeFound("duration.specified=false");
    }

    @Test
    @Transactional
    public void getAllTasksByDurationIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where duration greater than or equals to DEFAULT_DURATION
        defaultTasksShouldBeFound("duration.greaterOrEqualThan=" + DEFAULT_DURATION);

        // Get all the tasksList where duration greater than or equals to UPDATED_DURATION
        defaultTasksShouldNotBeFound("duration.greaterOrEqualThan=" + UPDATED_DURATION);
    }

    @Test
    @Transactional
    public void getAllTasksByDurationIsLessThanSomething() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where duration less than or equals to DEFAULT_DURATION
        defaultTasksShouldNotBeFound("duration.lessThan=" + DEFAULT_DURATION);

        // Get all the tasksList where duration less than or equals to UPDATED_DURATION
        defaultTasksShouldBeFound("duration.lessThan=" + UPDATED_DURATION);
    }


    @Test
    @Transactional
    public void getAllTasksByTargetIsEqualToSomething() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where target equals to DEFAULT_TARGET
        defaultTasksShouldBeFound("target.equals=" + DEFAULT_TARGET);

        // Get all the tasksList where target equals to UPDATED_TARGET
        defaultTasksShouldNotBeFound("target.equals=" + UPDATED_TARGET);
    }

    @Test
    @Transactional
    public void getAllTasksByTargetIsInShouldWork() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where target in DEFAULT_TARGET or UPDATED_TARGET
        defaultTasksShouldBeFound("target.in=" + DEFAULT_TARGET + "," + UPDATED_TARGET);

        // Get all the tasksList where target equals to UPDATED_TARGET
        defaultTasksShouldNotBeFound("target.in=" + UPDATED_TARGET);
    }

    @Test
    @Transactional
    public void getAllTasksByTargetIsNullOrNotNull() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where target is not null
        defaultTasksShouldBeFound("target.specified=true");

        // Get all the tasksList where target is null
        defaultTasksShouldNotBeFound("target.specified=false");
    }

    @Test
    @Transactional
    public void getAllTasksByTargetIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where target greater than or equals to DEFAULT_TARGET
        defaultTasksShouldBeFound("target.greaterOrEqualThan=" + DEFAULT_TARGET);

        // Get all the tasksList where target greater than or equals to UPDATED_TARGET
        defaultTasksShouldNotBeFound("target.greaterOrEqualThan=" + UPDATED_TARGET);
    }

    @Test
    @Transactional
    public void getAllTasksByTargetIsLessThanSomething() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where target less than or equals to DEFAULT_TARGET
        defaultTasksShouldNotBeFound("target.lessThan=" + DEFAULT_TARGET);

        // Get all the tasksList where target less than or equals to UPDATED_TARGET
        defaultTasksShouldBeFound("target.lessThan=" + UPDATED_TARGET);
    }


    @Test
    @Transactional
    public void getAllTasksByErrorQuantityIsEqualToSomething() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where errorQuantity equals to DEFAULT_ERROR_QUANTITY
        defaultTasksShouldBeFound("errorQuantity.equals=" + DEFAULT_ERROR_QUANTITY);

        // Get all the tasksList where errorQuantity equals to UPDATED_ERROR_QUANTITY
        defaultTasksShouldNotBeFound("errorQuantity.equals=" + UPDATED_ERROR_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllTasksByErrorQuantityIsInShouldWork() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where errorQuantity in DEFAULT_ERROR_QUANTITY or UPDATED_ERROR_QUANTITY
        defaultTasksShouldBeFound("errorQuantity.in=" + DEFAULT_ERROR_QUANTITY + "," + UPDATED_ERROR_QUANTITY);

        // Get all the tasksList where errorQuantity equals to UPDATED_ERROR_QUANTITY
        defaultTasksShouldNotBeFound("errorQuantity.in=" + UPDATED_ERROR_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllTasksByErrorQuantityIsNullOrNotNull() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where errorQuantity is not null
        defaultTasksShouldBeFound("errorQuantity.specified=true");

        // Get all the tasksList where errorQuantity is null
        defaultTasksShouldNotBeFound("errorQuantity.specified=false");
    }

    @Test
    @Transactional
    public void getAllTasksByErrorQuantityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where errorQuantity greater than or equals to DEFAULT_ERROR_QUANTITY
        defaultTasksShouldBeFound("errorQuantity.greaterOrEqualThan=" + DEFAULT_ERROR_QUANTITY);

        // Get all the tasksList where errorQuantity greater than or equals to UPDATED_ERROR_QUANTITY
        defaultTasksShouldNotBeFound("errorQuantity.greaterOrEqualThan=" + UPDATED_ERROR_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllTasksByErrorQuantityIsLessThanSomething() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where errorQuantity less than or equals to DEFAULT_ERROR_QUANTITY
        defaultTasksShouldNotBeFound("errorQuantity.lessThan=" + DEFAULT_ERROR_QUANTITY);

        // Get all the tasksList where errorQuantity less than or equals to UPDATED_ERROR_QUANTITY
        defaultTasksShouldBeFound("errorQuantity.lessThan=" + UPDATED_ERROR_QUANTITY);
    }


    @Test
    @Transactional
    public void getAllTasksByErrorSeverityIsEqualToSomething() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where errorSeverity equals to DEFAULT_ERROR_SEVERITY
        defaultTasksShouldBeFound("errorSeverity.equals=" + DEFAULT_ERROR_SEVERITY);

        // Get all the tasksList where errorSeverity equals to UPDATED_ERROR_SEVERITY
        defaultTasksShouldNotBeFound("errorSeverity.equals=" + UPDATED_ERROR_SEVERITY);
    }

    @Test
    @Transactional
    public void getAllTasksByErrorSeverityIsInShouldWork() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where errorSeverity in DEFAULT_ERROR_SEVERITY or UPDATED_ERROR_SEVERITY
        defaultTasksShouldBeFound("errorSeverity.in=" + DEFAULT_ERROR_SEVERITY + "," + UPDATED_ERROR_SEVERITY);

        // Get all the tasksList where errorSeverity equals to UPDATED_ERROR_SEVERITY
        defaultTasksShouldNotBeFound("errorSeverity.in=" + UPDATED_ERROR_SEVERITY);
    }

    @Test
    @Transactional
    public void getAllTasksByErrorSeverityIsNullOrNotNull() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where errorSeverity is not null
        defaultTasksShouldBeFound("errorSeverity.specified=true");

        // Get all the tasksList where errorSeverity is null
        defaultTasksShouldNotBeFound("errorSeverity.specified=false");
    }

    @Test
    @Transactional
    public void getAllTasksByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where status equals to DEFAULT_STATUS
        defaultTasksShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the tasksList where status equals to UPDATED_STATUS
        defaultTasksShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllTasksByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultTasksShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the tasksList where status equals to UPDATED_STATUS
        defaultTasksShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllTasksByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where status is not null
        defaultTasksShouldBeFound("status.specified=true");

        // Get all the tasksList where status is null
        defaultTasksShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    public void getAllTasksByParentIsEqualToSomething() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where parent equals to DEFAULT_PARENT
        defaultTasksShouldBeFound("parent.equals=" + DEFAULT_PARENT);

        // Get all the tasksList where parent equals to UPDATED_PARENT
        defaultTasksShouldNotBeFound("parent.equals=" + UPDATED_PARENT);
    }

    @Test
    @Transactional
    public void getAllTasksByParentIsInShouldWork() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where parent in DEFAULT_PARENT or UPDATED_PARENT
        defaultTasksShouldBeFound("parent.in=" + DEFAULT_PARENT + "," + UPDATED_PARENT);

        // Get all the tasksList where parent equals to UPDATED_PARENT
        defaultTasksShouldNotBeFound("parent.in=" + UPDATED_PARENT);
    }

    @Test
    @Transactional
    public void getAllTasksByParentIsNullOrNotNull() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where parent is not null
        defaultTasksShouldBeFound("parent.specified=true");

        // Get all the tasksList where parent is null
        defaultTasksShouldNotBeFound("parent.specified=false");
    }

    @Test
    @Transactional
    public void getAllTasksByParentIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where parent greater than or equals to DEFAULT_PARENT
        defaultTasksShouldBeFound("parent.greaterOrEqualThan=" + DEFAULT_PARENT);

        // Get all the tasksList where parent greater than or equals to UPDATED_PARENT
        defaultTasksShouldNotBeFound("parent.greaterOrEqualThan=" + UPDATED_PARENT);
    }

    @Test
    @Transactional
    public void getAllTasksByParentIsLessThanSomething() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where parent less than or equals to DEFAULT_PARENT
        defaultTasksShouldNotBeFound("parent.lessThan=" + DEFAULT_PARENT);

        // Get all the tasksList where parent less than or equals to UPDATED_PARENT
        defaultTasksShouldBeFound("parent.lessThan=" + UPDATED_PARENT);
    }


    @Test
    @Transactional
    public void getAllTasksByOpIsEqualToSomething() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where op equals to DEFAULT_OP
        defaultTasksShouldBeFound("op.equals=" + DEFAULT_OP);

        // Get all the tasksList where op equals to UPDATED_OP
        defaultTasksShouldNotBeFound("op.equals=" + UPDATED_OP);
    }

    @Test
    @Transactional
    public void getAllTasksByOpIsInShouldWork() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where op in DEFAULT_OP or UPDATED_OP
        defaultTasksShouldBeFound("op.in=" + DEFAULT_OP + "," + UPDATED_OP);

        // Get all the tasksList where op equals to UPDATED_OP
        defaultTasksShouldNotBeFound("op.in=" + UPDATED_OP);
    }

    @Test
    @Transactional
    public void getAllTasksByOpIsNullOrNotNull() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where op is not null
        defaultTasksShouldBeFound("op.specified=true");

        // Get all the tasksList where op is null
        defaultTasksShouldNotBeFound("op.specified=false");
    }

    @Test
    @Transactional
    public void getAllTasksByReview1IsEqualToSomething() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where review1 equals to DEFAULT_REVIEW_1
        defaultTasksShouldBeFound("review1.equals=" + DEFAULT_REVIEW_1);

        // Get all the tasksList where review1 equals to UPDATED_REVIEW_1
        defaultTasksShouldNotBeFound("review1.equals=" + UPDATED_REVIEW_1);
    }

    @Test
    @Transactional
    public void getAllTasksByReview1IsInShouldWork() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where review1 in DEFAULT_REVIEW_1 or UPDATED_REVIEW_1
        defaultTasksShouldBeFound("review1.in=" + DEFAULT_REVIEW_1 + "," + UPDATED_REVIEW_1);

        // Get all the tasksList where review1 equals to UPDATED_REVIEW_1
        defaultTasksShouldNotBeFound("review1.in=" + UPDATED_REVIEW_1);
    }

    @Test
    @Transactional
    public void getAllTasksByReview1IsNullOrNotNull() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where review1 is not null
        defaultTasksShouldBeFound("review1.specified=true");

        // Get all the tasksList where review1 is null
        defaultTasksShouldNotBeFound("review1.specified=false");
    }

    @Test
    @Transactional
    public void getAllTasksByReview2IsEqualToSomething() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where review2 equals to DEFAULT_REVIEW_2
        defaultTasksShouldBeFound("review2.equals=" + DEFAULT_REVIEW_2);

        // Get all the tasksList where review2 equals to UPDATED_REVIEW_2
        defaultTasksShouldNotBeFound("review2.equals=" + UPDATED_REVIEW_2);
    }

    @Test
    @Transactional
    public void getAllTasksByReview2IsInShouldWork() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where review2 in DEFAULT_REVIEW_2 or UPDATED_REVIEW_2
        defaultTasksShouldBeFound("review2.in=" + DEFAULT_REVIEW_2 + "," + UPDATED_REVIEW_2);

        // Get all the tasksList where review2 equals to UPDATED_REVIEW_2
        defaultTasksShouldNotBeFound("review2.in=" + UPDATED_REVIEW_2);
    }

    @Test
    @Transactional
    public void getAllTasksByReview2IsNullOrNotNull() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where review2 is not null
        defaultTasksShouldBeFound("review2.specified=true");

        // Get all the tasksList where review2 is null
        defaultTasksShouldNotBeFound("review2.specified=false");
    }

    @Test
    @Transactional
    public void getAllTasksByFixerIsEqualToSomething() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where fixer equals to DEFAULT_FIXER
        defaultTasksShouldBeFound("fixer.equals=" + DEFAULT_FIXER);

        // Get all the tasksList where fixer equals to UPDATED_FIXER
        defaultTasksShouldNotBeFound("fixer.equals=" + UPDATED_FIXER);
    }

    @Test
    @Transactional
    public void getAllTasksByFixerIsInShouldWork() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where fixer in DEFAULT_FIXER or UPDATED_FIXER
        defaultTasksShouldBeFound("fixer.in=" + DEFAULT_FIXER + "," + UPDATED_FIXER);

        // Get all the tasksList where fixer equals to UPDATED_FIXER
        defaultTasksShouldNotBeFound("fixer.in=" + UPDATED_FIXER);
    }

    @Test
    @Transactional
    public void getAllTasksByFixerIsNullOrNotNull() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where fixer is not null
        defaultTasksShouldBeFound("fixer.specified=true");

        // Get all the tasksList where fixer is null
        defaultTasksShouldNotBeFound("fixer.specified=false");
    }

    @Test
    @Transactional
    public void getAllTasksByFiIsEqualToSomething() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where fi equals to DEFAULT_FI
        defaultTasksShouldBeFound("fi.equals=" + DEFAULT_FI);

        // Get all the tasksList where fi equals to UPDATED_FI
        defaultTasksShouldNotBeFound("fi.equals=" + UPDATED_FI);
    }

    @Test
    @Transactional
    public void getAllTasksByFiIsInShouldWork() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where fi in DEFAULT_FI or UPDATED_FI
        defaultTasksShouldBeFound("fi.in=" + DEFAULT_FI + "," + UPDATED_FI);

        // Get all the tasksList where fi equals to UPDATED_FI
        defaultTasksShouldNotBeFound("fi.in=" + UPDATED_FI);
    }

    @Test
    @Transactional
    public void getAllTasksByFiIsNullOrNotNull() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList where fi is not null
        defaultTasksShouldBeFound("fi.specified=true");

        // Get all the tasksList where fi is null
        defaultTasksShouldNotBeFound("fi.specified=false");
    }

    @Test
    @Transactional
    public void getAllTasksByTmsCustomFieldScreenValueIsEqualToSomething() throws Exception {
        // Initialize the database
        TMSCustomFieldScreenValue tmsCustomFieldScreenValue = TMSCustomFieldScreenValueResourceIntTest.createEntity(em);
        em.persist(tmsCustomFieldScreenValue);
        em.flush();
        tasks.addTmsCustomFieldScreenValue(tmsCustomFieldScreenValue);
        tasksRepository.saveAndFlush(tasks);
        Long tmsCustomFieldScreenValueId = tmsCustomFieldScreenValue.getId();

        // Get all the tasksList where tmsCustomFieldScreenValue equals to tmsCustomFieldScreenValueId
        defaultTasksShouldBeFound("tmsCustomFieldScreenValueId.equals=" + tmsCustomFieldScreenValueId);

        // Get all the tasksList where tmsCustomFieldScreenValue equals to tmsCustomFieldScreenValueId + 1
        defaultTasksShouldNotBeFound("tmsCustomFieldScreenValueId.equals=" + (tmsCustomFieldScreenValueId + 1));
    }


    @Test
    @Transactional
    public void getAllTasksByPackagesIsEqualToSomething() throws Exception {
        // Initialize the database
        Packages packages = PackagesResourceIntTest.createEntity(em);
        em.persist(packages);
        em.flush();
        tasks.setPackages(packages);
        tasksRepository.saveAndFlush(tasks);
        Long packagesId = packages.getId();

        // Get all the tasksList where packages equals to packagesId
        defaultTasksShouldBeFound("packagesId.equals=" + packagesId);

        // Get all the tasksList where packages equals to packagesId + 1
        defaultTasksShouldNotBeFound("packagesId.equals=" + (packagesId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultTasksShouldBeFound(String filter) throws Exception {
        restTasksMockMvc.perform(get("/api/tasks?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tasks.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].severity").value(hasItem(DEFAULT_SEVERITY.toString())))
            .andExpect(jsonPath("$.[*].priority").value(hasItem(DEFAULT_PRIORITY.toString())))
            .andExpect(jsonPath("$.[*].data").value(hasItem(DEFAULT_DATA.toString())))
            .andExpect(jsonPath("$.[*].fileName").value(hasItem(DEFAULT_FILE_NAME.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].availability").value(hasItem(DEFAULT_AVAILABILITY.toString())))
            .andExpect(jsonPath("$.[*].frame").value(hasItem(DEFAULT_FRAME)))
            .andExpect(jsonPath("$.[*].actualObject").value(hasItem(DEFAULT_ACTUAL_OBJECT)))
            .andExpect(jsonPath("$.[*].opStatus").value(hasItem(DEFAULT_OP_STATUS.toString())))
            .andExpect(jsonPath("$.[*].estimateStartTime").value(hasItem(DEFAULT_OP_ESTIMATE_START_TIME.toString())))
            .andExpect(jsonPath("$.[*].estimateEndTime").value(hasItem(DEFAULT_OP_ESTIMATE_END_TIME.toString())))
            .andExpect(jsonPath("$.[*].opStartTime").value(hasItem(DEFAULT_OP_START_TIME.toString())))
            .andExpect(jsonPath("$.[*].opEndTime").value(hasItem(DEFAULT_OP_END_TIME.toString())))
            .andExpect(jsonPath("$.[*].review1Status").value(hasItem(DEFAULT_REVIEW_1_STATUS.toString())))
            .andExpect(jsonPath("$.[*].review1StartTime").value(hasItem(DEFAULT_REVIEW_1_START_TIME.toString())))
            .andExpect(jsonPath("$.[*].review1EndTime").value(hasItem(DEFAULT_REVIEW_1_END_TIME.toString())))
            .andExpect(jsonPath("$.[*].fixStatus").value(hasItem(DEFAULT_FIX_STATUS.toString())))
            .andExpect(jsonPath("$.[*].fixStartTime").value(hasItem(DEFAULT_FIX_START_TIME.toString())))
            .andExpect(jsonPath("$.[*].fixEndTime").value(hasItem(DEFAULT_FIX_END_TIME.toString())))
            .andExpect(jsonPath("$.[*].review2Status").value(hasItem(DEFAULT_REVIEW_2_STATUS.toString())))
            .andExpect(jsonPath("$.[*].review2StartTime").value(hasItem(DEFAULT_REVIEW_2_START_TIME.toString())))
            .andExpect(jsonPath("$.[*].review2EndTime").value(hasItem(DEFAULT_REVIEW_2_END_TIME.toString())))
            .andExpect(jsonPath("$.[*].fiStatus").value(hasItem(DEFAULT_FI_STATUS.toString())))
            .andExpect(jsonPath("$.[*].fiStartTime").value(hasItem(DEFAULT_FI_START_TIME.toString())))
            .andExpect(jsonPath("$.[*].fiEndTime").value(hasItem(DEFAULT_FI_END_TIME.toString())))
            .andExpect(jsonPath("$.[*].duration").value(hasItem(DEFAULT_DURATION)))
            .andExpect(jsonPath("$.[*].target").value(hasItem(DEFAULT_TARGET)))
            .andExpect(jsonPath("$.[*].errorQuantity").value(hasItem(DEFAULT_ERROR_QUANTITY)))
            .andExpect(jsonPath("$.[*].errorSeverity").value(hasItem(DEFAULT_ERROR_SEVERITY.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].parent").value(hasItem(DEFAULT_PARENT.intValue())))
            .andExpect(jsonPath("$.[*].op").value(hasItem(DEFAULT_OP.toString())))
            .andExpect(jsonPath("$.[*].review1").value(hasItem(DEFAULT_REVIEW_1.toString())))
            .andExpect(jsonPath("$.[*].review2").value(hasItem(DEFAULT_REVIEW_2.toString())))
            .andExpect(jsonPath("$.[*].fixer").value(hasItem(DEFAULT_FIXER.toString())))
            .andExpect(jsonPath("$.[*].fi").value(hasItem(DEFAULT_FI.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultTasksShouldNotBeFound(String filter) throws Exception {
        restTasksMockMvc.perform(get("/api/tasks?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingTasks() throws Exception {
        // Get the tasks
        restTasksMockMvc.perform(get("/api/tasks/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTasks() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);
        tasksSearchRepository.save(tasks);
        int databaseSizeBeforeUpdate = tasksRepository.findAll().size();

        // Update the tasks
        Tasks updatedTasks = tasksRepository.findOne(tasks.getId());
        // Disconnect from session so that the updates on updatedTasks are not directly saved in db
        em.detach(updatedTasks);
        updatedTasks
            .name(UPDATED_NAME)
            .severity(UPDATED_SEVERITY)
            .priority(UPDATED_PRIORITY)
            .data(UPDATED_DATA)
            .fileName(UPDATED_FILE_NAME)
            .type(UPDATED_TYPE)
            .availability(UPDATED_AVAILABILITY)
            .frame(UPDATED_FRAME)
            .actualObject(UPDATED_ACTUAL_OBJECT)
            .opStatus(UPDATED_OP_STATUS)
            .estimateStartTime(UPDATED_OP_ESTIMATE_START_TIME)
            .estimateEndTime(UPDATED_OP_ESTIMATE_END_TIME)
            .opStartTime(UPDATED_OP_START_TIME)
            .opEndTime(UPDATED_OP_END_TIME)
            .review1Status(UPDATED_REVIEW_1_STATUS)
            .review1StartTime(UPDATED_REVIEW_1_START_TIME)
            .review1EndTime(UPDATED_REVIEW_1_END_TIME)
            .fixStatus(UPDATED_FIX_STATUS)
            .fixStartTime(UPDATED_FIX_START_TIME)
            .fixEndTime(UPDATED_FIX_END_TIME)
            .review2Status(UPDATED_REVIEW_2_STATUS)
            .review2StartTime(UPDATED_REVIEW_2_START_TIME)
            .review2EndTime(UPDATED_REVIEW_2_END_TIME)
            .fiStatus(UPDATED_FI_STATUS)
            .fiStartTime(UPDATED_FI_START_TIME)
            .fiEndTime(UPDATED_FI_END_TIME)
            .duration(UPDATED_DURATION)
            .target(UPDATED_TARGET)
            .errorQuantity(UPDATED_ERROR_QUANTITY)
            .errorSeverity(UPDATED_ERROR_SEVERITY)
            .status(UPDATED_STATUS)
            .description(UPDATED_DESCRIPTION)
            .parent(UPDATED_PARENT)
            .op(UPDATED_OP)
            .review1(UPDATED_REVIEW_1)
            .review2(UPDATED_REVIEW_2)
            .fixer(UPDATED_FIXER)
            .fi(UPDATED_FI);
        TasksDTO tasksDTO = tasksMapper.toDto(updatedTasks);

        restTasksMockMvc.perform(put("/api/tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tasksDTO)))
            .andExpect(status().isOk());

        // Validate the Tasks in the database
        List<Tasks> tasksList = tasksRepository.findAll();
        assertThat(tasksList).hasSize(databaseSizeBeforeUpdate);
        Tasks testTasks = tasksList.get(tasksList.size() - 1);
        assertThat(testTasks.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTasks.getSeverity()).isEqualTo(UPDATED_SEVERITY);
        assertThat(testTasks.getPriority()).isEqualTo(UPDATED_PRIORITY);
        assertThat(testTasks.getData()).isEqualTo(UPDATED_DATA);
        assertThat(testTasks.getFileName()).isEqualTo(UPDATED_FILE_NAME);
        assertThat(testTasks.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testTasks.getAvailability()).isEqualTo(UPDATED_AVAILABILITY);
        assertThat(testTasks.getFrame()).isEqualTo(UPDATED_FRAME);
        assertThat(testTasks.getActualObject()).isEqualTo(UPDATED_ACTUAL_OBJECT);
        assertThat(testTasks.getOpStatus()).isEqualTo(UPDATED_OP_STATUS);
        assertThat(testTasks.getEstimateStartTime()).isEqualTo(UPDATED_OP_ESTIMATE_START_TIME);
        assertThat(testTasks.getEstimateEndTime()).isEqualTo(UPDATED_OP_ESTIMATE_END_TIME);
        assertThat(testTasks.getOpStartTime()).isEqualTo(UPDATED_OP_START_TIME);
        assertThat(testTasks.getOpEndTime()).isEqualTo(UPDATED_OP_END_TIME);
        assertThat(testTasks.getReview1Status()).isEqualTo(UPDATED_REVIEW_1_STATUS);
        assertThat(testTasks.getReview1StartTime()).isEqualTo(UPDATED_REVIEW_1_START_TIME);
        assertThat(testTasks.getReview1EndTime()).isEqualTo(UPDATED_REVIEW_1_END_TIME);
        assertThat(testTasks.getFixStatus()).isEqualTo(UPDATED_FIX_STATUS);
        assertThat(testTasks.getFixStartTime()).isEqualTo(UPDATED_FIX_START_TIME);
        assertThat(testTasks.getFixEndTime()).isEqualTo(UPDATED_FIX_END_TIME);
        assertThat(testTasks.getReview2Status()).isEqualTo(UPDATED_REVIEW_2_STATUS);
        assertThat(testTasks.getReview2StartTime()).isEqualTo(UPDATED_REVIEW_2_START_TIME);
        assertThat(testTasks.getReview2EndTime()).isEqualTo(UPDATED_REVIEW_2_END_TIME);
        assertThat(testTasks.getFiStatus()).isEqualTo(UPDATED_FI_STATUS);
        assertThat(testTasks.getFiStartTime()).isEqualTo(UPDATED_FI_START_TIME);
        assertThat(testTasks.getFiEndTime()).isEqualTo(UPDATED_FI_END_TIME);
        assertThat(testTasks.getDuration()).isEqualTo(UPDATED_DURATION);
        assertThat(testTasks.getTarget()).isEqualTo(UPDATED_TARGET);
        assertThat(testTasks.getErrorQuantity()).isEqualTo(UPDATED_ERROR_QUANTITY);
        assertThat(testTasks.getErrorSeverity()).isEqualTo(UPDATED_ERROR_SEVERITY);
        assertThat(testTasks.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testTasks.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTasks.getParent()).isEqualTo(UPDATED_PARENT);
        assertThat(testTasks.getOp()).isEqualTo(UPDATED_OP);
        assertThat(testTasks.getReview1()).isEqualTo(UPDATED_REVIEW_1);
        assertThat(testTasks.getReview2()).isEqualTo(UPDATED_REVIEW_2);
        assertThat(testTasks.getFixer()).isEqualTo(UPDATED_FIXER);
        assertThat(testTasks.getFi()).isEqualTo(UPDATED_FI);

        // Validate the Tasks in Elasticsearch
        Tasks tasksEs = tasksSearchRepository.findOne(testTasks.getId());
        assertThat(tasksEs).isEqualToIgnoringGivenFields(testTasks);
    }

    @Test
    @Transactional
    public void updateNonExistingTasks() throws Exception {
        int databaseSizeBeforeUpdate = tasksRepository.findAll().size();

        // Create the Tasks
        TasksDTO tasksDTO = tasksMapper.toDto(tasks);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTasksMockMvc.perform(put("/api/tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tasksDTO)))
            .andExpect(status().isCreated());

        // Validate the Tasks in the database
        List<Tasks> tasksList = tasksRepository.findAll();
        assertThat(tasksList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTasks() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);
        tasksSearchRepository.save(tasks);
        int databaseSizeBeforeDelete = tasksRepository.findAll().size();

        // Get the tasks
        restTasksMockMvc.perform(delete("/api/tasks/{id}", tasks.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean tasksExistsInEs = tasksSearchRepository.exists(tasks.getId());
        assertThat(tasksExistsInEs).isFalse();

        // Validate the database is empty
        List<Tasks> tasksList = tasksRepository.findAll();
        assertThat(tasksList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchTasks() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);
        tasksSearchRepository.save(tasks);

        // Search the tasks
        restTasksMockMvc.perform(get("/api/_search/tasks?query=id:" + tasks.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tasks.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].severity").value(hasItem(DEFAULT_SEVERITY.toString())))
            .andExpect(jsonPath("$.[*].priority").value(hasItem(DEFAULT_PRIORITY.toString())))
            .andExpect(jsonPath("$.[*].data").value(hasItem(DEFAULT_DATA.toString())))
            .andExpect(jsonPath("$.[*].fileName").value(hasItem(DEFAULT_FILE_NAME.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].availability").value(hasItem(DEFAULT_AVAILABILITY.toString())))
            .andExpect(jsonPath("$.[*].frame").value(hasItem(DEFAULT_FRAME)))
            .andExpect(jsonPath("$.[*].actualObject").value(hasItem(DEFAULT_ACTUAL_OBJECT)))
            .andExpect(jsonPath("$.[*].opStatus").value(hasItem(DEFAULT_OP_STATUS.toString())))
            .andExpect(jsonPath("$.[*].estimateStartTime").value(hasItem(DEFAULT_OP_ESTIMATE_START_TIME.toString())))
            .andExpect(jsonPath("$.[*].estimateEndTime").value(hasItem(DEFAULT_OP_ESTIMATE_END_TIME.toString())))
            .andExpect(jsonPath("$.[*].opStartTime").value(hasItem(DEFAULT_OP_START_TIME.toString())))
            .andExpect(jsonPath("$.[*].opEndTime").value(hasItem(DEFAULT_OP_END_TIME.toString())))
            .andExpect(jsonPath("$.[*].review1Status").value(hasItem(DEFAULT_REVIEW_1_STATUS.toString())))
            .andExpect(jsonPath("$.[*].review1StartTime").value(hasItem(DEFAULT_REVIEW_1_START_TIME.toString())))
            .andExpect(jsonPath("$.[*].review1EndTime").value(hasItem(DEFAULT_REVIEW_1_END_TIME.toString())))
            .andExpect(jsonPath("$.[*].fixStatus").value(hasItem(DEFAULT_FIX_STATUS.toString())))
            .andExpect(jsonPath("$.[*].fixStartTime").value(hasItem(DEFAULT_FIX_START_TIME.toString())))
            .andExpect(jsonPath("$.[*].fixEndTime").value(hasItem(DEFAULT_FIX_END_TIME.toString())))
            .andExpect(jsonPath("$.[*].review2Status").value(hasItem(DEFAULT_REVIEW_2_STATUS.toString())))
            .andExpect(jsonPath("$.[*].review2StartTime").value(hasItem(DEFAULT_REVIEW_2_START_TIME.toString())))
            .andExpect(jsonPath("$.[*].review2EndTime").value(hasItem(DEFAULT_REVIEW_2_END_TIME.toString())))
            .andExpect(jsonPath("$.[*].fiStatus").value(hasItem(DEFAULT_FI_STATUS.toString())))
            .andExpect(jsonPath("$.[*].fiStartTime").value(hasItem(DEFAULT_FI_START_TIME.toString())))
            .andExpect(jsonPath("$.[*].fiEndTime").value(hasItem(DEFAULT_FI_END_TIME.toString())))
            .andExpect(jsonPath("$.[*].duration").value(hasItem(DEFAULT_DURATION)))
            .andExpect(jsonPath("$.[*].target").value(hasItem(DEFAULT_TARGET)))
            .andExpect(jsonPath("$.[*].errorQuantity").value(hasItem(DEFAULT_ERROR_QUANTITY)))
            .andExpect(jsonPath("$.[*].errorSeverity").value(hasItem(DEFAULT_ERROR_SEVERITY.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].parent").value(hasItem(DEFAULT_PARENT.intValue())))
            .andExpect(jsonPath("$.[*].op").value(hasItem(DEFAULT_OP.toString())))
            .andExpect(jsonPath("$.[*].review1").value(hasItem(DEFAULT_REVIEW_1.toString())))
            .andExpect(jsonPath("$.[*].review2").value(hasItem(DEFAULT_REVIEW_2.toString())))
            .andExpect(jsonPath("$.[*].fixer").value(hasItem(DEFAULT_FIXER.toString())))
            .andExpect(jsonPath("$.[*].fi").value(hasItem(DEFAULT_FI.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Tasks.class);
        Tasks tasks1 = new Tasks();
        tasks1.setId(1L);
        Tasks tasks2 = new Tasks();
        tasks2.setId(tasks1.getId());
        assertThat(tasks1).isEqualTo(tasks2);
        tasks2.setId(2L);
        assertThat(tasks1).isNotEqualTo(tasks2);
        tasks1.setId(null);
        assertThat(tasks1).isNotEqualTo(tasks2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TasksDTO.class);
        TasksDTO tasksDTO1 = new TasksDTO();
        tasksDTO1.setId(1L);
        TasksDTO tasksDTO2 = new TasksDTO();
        assertThat(tasksDTO1).isNotEqualTo(tasksDTO2);
        tasksDTO2.setId(tasksDTO1.getId());
        assertThat(tasksDTO1).isEqualTo(tasksDTO2);
        tasksDTO2.setId(2L);
        assertThat(tasksDTO1).isNotEqualTo(tasksDTO2);
        tasksDTO1.setId(null);
        assertThat(tasksDTO1).isNotEqualTo(tasksDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(tasksMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(tasksMapper.fromId(null)).isNull();
    }
}
