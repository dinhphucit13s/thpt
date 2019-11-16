package fpt.dps.dtms.web.rest;

import fpt.dps.dtms.DtmsApp;

import fpt.dps.dtms.domain.TaskBiddingTrackingTime;
import fpt.dps.dtms.domain.Tasks;
import fpt.dps.dtms.repository.TaskBiddingTrackingTimeRepository;
import fpt.dps.dtms.service.TaskBiddingTrackingTimeService;
import fpt.dps.dtms.repository.search.TaskBiddingTrackingTimeSearchRepository;
import fpt.dps.dtms.service.dto.TaskBiddingTrackingTimeDTO;
import fpt.dps.dtms.service.mapper.TaskBiddingTrackingTimeMapper;
import fpt.dps.dtms.web.rest.errors.ExceptionTranslator;
import fpt.dps.dtms.service.dto.TaskBiddingTrackingTimeCriteria;
import fpt.dps.dtms.service.TaskBiddingTrackingTimeQueryService;

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
import java.time.temporal.ChronoUnit;
import java.util.List;

import static fpt.dps.dtms.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fpt.dps.dtms.domain.enumeration.BiddingScope;
/**
 * Test class for the TaskBiddingTrackingTimeResource REST controller.
 *
 * @see TaskBiddingTrackingTimeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DtmsApp.class)
public class TaskBiddingTrackingTimeResourceIntTest {

    private static final String DEFAULT_USER_LOGIN = "AAAAAAAAAA";
    private static final String UPDATED_USER_LOGIN = "BBBBBBBBBB";

    private static final String DEFAULT_ROLE = "AAAAAAAAAA";
    private static final String UPDATED_ROLE = "BBBBBBBBBB";

    private static final Instant DEFAULT_START_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_START_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_START_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_END_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_END_STATUS = "BBBBBBBBBB";

    private static final Integer DEFAULT_DURATION = 1;
    private static final Integer UPDATED_DURATION = 2;

    private static final BiddingScope DEFAULT_BIDDING_SCOPE = BiddingScope.PROJECT;
    private static final BiddingScope UPDATED_BIDDING_SCOPE = BiddingScope.BU;

    @Autowired
    private TaskBiddingTrackingTimeRepository taskBiddingTrackingTimeRepository;

    @Autowired
    private TaskBiddingTrackingTimeMapper taskBiddingTrackingTimeMapper;

    @Autowired
    private TaskBiddingTrackingTimeService taskBiddingTrackingTimeService;

    @Autowired
    private TaskBiddingTrackingTimeSearchRepository taskBiddingTrackingTimeSearchRepository;

    @Autowired
    private TaskBiddingTrackingTimeQueryService taskBiddingTrackingTimeQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTaskBiddingTrackingTimeMockMvc;

    private TaskBiddingTrackingTime taskBiddingTrackingTime;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TaskBiddingTrackingTimeResource taskBiddingTrackingTimeResource = new TaskBiddingTrackingTimeResource(taskBiddingTrackingTimeService, taskBiddingTrackingTimeQueryService);
        this.restTaskBiddingTrackingTimeMockMvc = MockMvcBuilders.standaloneSetup(taskBiddingTrackingTimeResource)
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
    public static TaskBiddingTrackingTime createEntity(EntityManager em) {
        TaskBiddingTrackingTime taskBiddingTrackingTime = new TaskBiddingTrackingTime()
            .userLogin(DEFAULT_USER_LOGIN)
            .role(DEFAULT_ROLE)
            .startTime(DEFAULT_START_TIME)
            .endTime(DEFAULT_END_TIME)
            .startStatus(DEFAULT_START_STATUS)
            .endStatus(DEFAULT_END_STATUS)
            .duration(DEFAULT_DURATION)
            .biddingScope(DEFAULT_BIDDING_SCOPE);
        // Add required entity
        Tasks task = TasksResourceIntTest.createEntity(em);
        em.persist(task);
        em.flush();
        taskBiddingTrackingTime.setTask(task);
        return taskBiddingTrackingTime;
    }

    @Before
    public void initTest() {
        taskBiddingTrackingTimeSearchRepository.deleteAll();
        taskBiddingTrackingTime = createEntity(em);
    }

    @Test
    @Transactional
    public void createTaskBiddingTrackingTime() throws Exception {
        int databaseSizeBeforeCreate = taskBiddingTrackingTimeRepository.findAll().size();

        // Create the TaskBiddingTrackingTime
        TaskBiddingTrackingTimeDTO taskBiddingTrackingTimeDTO = taskBiddingTrackingTimeMapper.toDto(taskBiddingTrackingTime);
        restTaskBiddingTrackingTimeMockMvc.perform(post("/api/task-bidding-tracking-times")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(taskBiddingTrackingTimeDTO)))
            .andExpect(status().isCreated());

        // Validate the TaskBiddingTrackingTime in the database
        List<TaskBiddingTrackingTime> taskBiddingTrackingTimeList = taskBiddingTrackingTimeRepository.findAll();
        assertThat(taskBiddingTrackingTimeList).hasSize(databaseSizeBeforeCreate + 1);
        TaskBiddingTrackingTime testTaskBiddingTrackingTime = taskBiddingTrackingTimeList.get(taskBiddingTrackingTimeList.size() - 1);
        assertThat(testTaskBiddingTrackingTime.getUserLogin()).isEqualTo(DEFAULT_USER_LOGIN);
        assertThat(testTaskBiddingTrackingTime.getRole()).isEqualTo(DEFAULT_ROLE);
        assertThat(testTaskBiddingTrackingTime.getStartTime()).isEqualTo(DEFAULT_START_TIME);
        assertThat(testTaskBiddingTrackingTime.getEndTime()).isEqualTo(DEFAULT_END_TIME);
        assertThat(testTaskBiddingTrackingTime.getStartStatus()).isEqualTo(DEFAULT_START_STATUS);
        assertThat(testTaskBiddingTrackingTime.getEndStatus()).isEqualTo(DEFAULT_END_STATUS);
        assertThat(testTaskBiddingTrackingTime.getDuration()).isEqualTo(DEFAULT_DURATION);
        assertThat(testTaskBiddingTrackingTime.getBiddingScope()).isEqualTo(DEFAULT_BIDDING_SCOPE);

        // Validate the TaskBiddingTrackingTime in Elasticsearch
        TaskBiddingTrackingTime taskBiddingTrackingTimeEs = taskBiddingTrackingTimeSearchRepository.findOne(testTaskBiddingTrackingTime.getId());
        assertThat(taskBiddingTrackingTimeEs).isEqualToIgnoringGivenFields(testTaskBiddingTrackingTime);
    }

    @Test
    @Transactional
    public void createTaskBiddingTrackingTimeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = taskBiddingTrackingTimeRepository.findAll().size();

        // Create the TaskBiddingTrackingTime with an existing ID
        taskBiddingTrackingTime.setId(1L);
        TaskBiddingTrackingTimeDTO taskBiddingTrackingTimeDTO = taskBiddingTrackingTimeMapper.toDto(taskBiddingTrackingTime);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTaskBiddingTrackingTimeMockMvc.perform(post("/api/task-bidding-tracking-times")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(taskBiddingTrackingTimeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TaskBiddingTrackingTime in the database
        List<TaskBiddingTrackingTime> taskBiddingTrackingTimeList = taskBiddingTrackingTimeRepository.findAll();
        assertThat(taskBiddingTrackingTimeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkUserLoginIsRequired() throws Exception {
        int databaseSizeBeforeTest = taskBiddingTrackingTimeRepository.findAll().size();
        // set the field null
        taskBiddingTrackingTime.setUserLogin(null);

        // Create the TaskBiddingTrackingTime, which fails.
        TaskBiddingTrackingTimeDTO taskBiddingTrackingTimeDTO = taskBiddingTrackingTimeMapper.toDto(taskBiddingTrackingTime);

        restTaskBiddingTrackingTimeMockMvc.perform(post("/api/task-bidding-tracking-times")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(taskBiddingTrackingTimeDTO)))
            .andExpect(status().isBadRequest());

        List<TaskBiddingTrackingTime> taskBiddingTrackingTimeList = taskBiddingTrackingTimeRepository.findAll();
        assertThat(taskBiddingTrackingTimeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkRoleIsRequired() throws Exception {
        int databaseSizeBeforeTest = taskBiddingTrackingTimeRepository.findAll().size();
        // set the field null
        taskBiddingTrackingTime.setRole(null);

        // Create the TaskBiddingTrackingTime, which fails.
        TaskBiddingTrackingTimeDTO taskBiddingTrackingTimeDTO = taskBiddingTrackingTimeMapper.toDto(taskBiddingTrackingTime);

        restTaskBiddingTrackingTimeMockMvc.perform(post("/api/task-bidding-tracking-times")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(taskBiddingTrackingTimeDTO)))
            .andExpect(status().isBadRequest());

        List<TaskBiddingTrackingTime> taskBiddingTrackingTimeList = taskBiddingTrackingTimeRepository.findAll();
        assertThat(taskBiddingTrackingTimeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStartTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = taskBiddingTrackingTimeRepository.findAll().size();
        // set the field null
        taskBiddingTrackingTime.setStartTime(null);

        // Create the TaskBiddingTrackingTime, which fails.
        TaskBiddingTrackingTimeDTO taskBiddingTrackingTimeDTO = taskBiddingTrackingTimeMapper.toDto(taskBiddingTrackingTime);

        restTaskBiddingTrackingTimeMockMvc.perform(post("/api/task-bidding-tracking-times")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(taskBiddingTrackingTimeDTO)))
            .andExpect(status().isBadRequest());

        List<TaskBiddingTrackingTime> taskBiddingTrackingTimeList = taskBiddingTrackingTimeRepository.findAll();
        assertThat(taskBiddingTrackingTimeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTaskBiddingTrackingTimes() throws Exception {
        // Initialize the database
        taskBiddingTrackingTimeRepository.saveAndFlush(taskBiddingTrackingTime);

        // Get all the taskBiddingTrackingTimeList
        restTaskBiddingTrackingTimeMockMvc.perform(get("/api/task-bidding-tracking-times?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(taskBiddingTrackingTime.getId().intValue())))
            .andExpect(jsonPath("$.[*].userLogin").value(hasItem(DEFAULT_USER_LOGIN.toString())))
            .andExpect(jsonPath("$.[*].role").value(hasItem(DEFAULT_ROLE.toString())))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME.toString())))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(DEFAULT_END_TIME.toString())))
            .andExpect(jsonPath("$.[*].startStatus").value(hasItem(DEFAULT_START_STATUS.toString())))
            .andExpect(jsonPath("$.[*].endStatus").value(hasItem(DEFAULT_END_STATUS.toString())))
            .andExpect(jsonPath("$.[*].duration").value(hasItem(DEFAULT_DURATION)))
            .andExpect(jsonPath("$.[*].biddingScope").value(hasItem(DEFAULT_BIDDING_SCOPE.toString())));
    }

    @Test
    @Transactional
    public void getTaskBiddingTrackingTime() throws Exception {
        // Initialize the database
        taskBiddingTrackingTimeRepository.saveAndFlush(taskBiddingTrackingTime);

        // Get the taskBiddingTrackingTime
        restTaskBiddingTrackingTimeMockMvc.perform(get("/api/task-bidding-tracking-times/{id}", taskBiddingTrackingTime.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(taskBiddingTrackingTime.getId().intValue()))
            .andExpect(jsonPath("$.userLogin").value(DEFAULT_USER_LOGIN.toString()))
            .andExpect(jsonPath("$.role").value(DEFAULT_ROLE.toString()))
            .andExpect(jsonPath("$.startTime").value(DEFAULT_START_TIME.toString()))
            .andExpect(jsonPath("$.endTime").value(DEFAULT_END_TIME.toString()))
            .andExpect(jsonPath("$.startStatus").value(DEFAULT_START_STATUS.toString()))
            .andExpect(jsonPath("$.endStatus").value(DEFAULT_END_STATUS.toString()))
            .andExpect(jsonPath("$.duration").value(DEFAULT_DURATION))
            .andExpect(jsonPath("$.biddingScope").value(DEFAULT_BIDDING_SCOPE.toString()));
    }

    @Test
    @Transactional
    public void getAllTaskBiddingTrackingTimesByUserLoginIsEqualToSomething() throws Exception {
        // Initialize the database
        taskBiddingTrackingTimeRepository.saveAndFlush(taskBiddingTrackingTime);

        // Get all the taskBiddingTrackingTimeList where userLogin equals to DEFAULT_USER_LOGIN
        defaultTaskBiddingTrackingTimeShouldBeFound("userLogin.equals=" + DEFAULT_USER_LOGIN);

        // Get all the taskBiddingTrackingTimeList where userLogin equals to UPDATED_USER_LOGIN
        defaultTaskBiddingTrackingTimeShouldNotBeFound("userLogin.equals=" + UPDATED_USER_LOGIN);
    }

    @Test
    @Transactional
    public void getAllTaskBiddingTrackingTimesByUserLoginIsInShouldWork() throws Exception {
        // Initialize the database
        taskBiddingTrackingTimeRepository.saveAndFlush(taskBiddingTrackingTime);

        // Get all the taskBiddingTrackingTimeList where userLogin in DEFAULT_USER_LOGIN or UPDATED_USER_LOGIN
        defaultTaskBiddingTrackingTimeShouldBeFound("userLogin.in=" + DEFAULT_USER_LOGIN + "," + UPDATED_USER_LOGIN);

        // Get all the taskBiddingTrackingTimeList where userLogin equals to UPDATED_USER_LOGIN
        defaultTaskBiddingTrackingTimeShouldNotBeFound("userLogin.in=" + UPDATED_USER_LOGIN);
    }

    @Test
    @Transactional
    public void getAllTaskBiddingTrackingTimesByUserLoginIsNullOrNotNull() throws Exception {
        // Initialize the database
        taskBiddingTrackingTimeRepository.saveAndFlush(taskBiddingTrackingTime);

        // Get all the taskBiddingTrackingTimeList where userLogin is not null
        defaultTaskBiddingTrackingTimeShouldBeFound("userLogin.specified=true");

        // Get all the taskBiddingTrackingTimeList where userLogin is null
        defaultTaskBiddingTrackingTimeShouldNotBeFound("userLogin.specified=false");
    }

    @Test
    @Transactional
    public void getAllTaskBiddingTrackingTimesByRoleIsEqualToSomething() throws Exception {
        // Initialize the database
        taskBiddingTrackingTimeRepository.saveAndFlush(taskBiddingTrackingTime);

        // Get all the taskBiddingTrackingTimeList where role equals to DEFAULT_ROLE
        defaultTaskBiddingTrackingTimeShouldBeFound("role.equals=" + DEFAULT_ROLE);

        // Get all the taskBiddingTrackingTimeList where role equals to UPDATED_ROLE
        defaultTaskBiddingTrackingTimeShouldNotBeFound("role.equals=" + UPDATED_ROLE);
    }

    @Test
    @Transactional
    public void getAllTaskBiddingTrackingTimesByRoleIsInShouldWork() throws Exception {
        // Initialize the database
        taskBiddingTrackingTimeRepository.saveAndFlush(taskBiddingTrackingTime);

        // Get all the taskBiddingTrackingTimeList where role in DEFAULT_ROLE or UPDATED_ROLE
        defaultTaskBiddingTrackingTimeShouldBeFound("role.in=" + DEFAULT_ROLE + "," + UPDATED_ROLE);

        // Get all the taskBiddingTrackingTimeList where role equals to UPDATED_ROLE
        defaultTaskBiddingTrackingTimeShouldNotBeFound("role.in=" + UPDATED_ROLE);
    }

    @Test
    @Transactional
    public void getAllTaskBiddingTrackingTimesByRoleIsNullOrNotNull() throws Exception {
        // Initialize the database
        taskBiddingTrackingTimeRepository.saveAndFlush(taskBiddingTrackingTime);

        // Get all the taskBiddingTrackingTimeList where role is not null
        defaultTaskBiddingTrackingTimeShouldBeFound("role.specified=true");

        // Get all the taskBiddingTrackingTimeList where role is null
        defaultTaskBiddingTrackingTimeShouldNotBeFound("role.specified=false");
    }

    @Test
    @Transactional
    public void getAllTaskBiddingTrackingTimesByStartTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        taskBiddingTrackingTimeRepository.saveAndFlush(taskBiddingTrackingTime);

        // Get all the taskBiddingTrackingTimeList where startTime equals to DEFAULT_START_TIME
        defaultTaskBiddingTrackingTimeShouldBeFound("startTime.equals=" + DEFAULT_START_TIME);

        // Get all the taskBiddingTrackingTimeList where startTime equals to UPDATED_START_TIME
        defaultTaskBiddingTrackingTimeShouldNotBeFound("startTime.equals=" + UPDATED_START_TIME);
    }

    @Test
    @Transactional
    public void getAllTaskBiddingTrackingTimesByStartTimeIsInShouldWork() throws Exception {
        // Initialize the database
        taskBiddingTrackingTimeRepository.saveAndFlush(taskBiddingTrackingTime);

        // Get all the taskBiddingTrackingTimeList where startTime in DEFAULT_START_TIME or UPDATED_START_TIME
        defaultTaskBiddingTrackingTimeShouldBeFound("startTime.in=" + DEFAULT_START_TIME + "," + UPDATED_START_TIME);

        // Get all the taskBiddingTrackingTimeList where startTime equals to UPDATED_START_TIME
        defaultTaskBiddingTrackingTimeShouldNotBeFound("startTime.in=" + UPDATED_START_TIME);
    }

    @Test
    @Transactional
    public void getAllTaskBiddingTrackingTimesByStartTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        taskBiddingTrackingTimeRepository.saveAndFlush(taskBiddingTrackingTime);

        // Get all the taskBiddingTrackingTimeList where startTime is not null
        defaultTaskBiddingTrackingTimeShouldBeFound("startTime.specified=true");

        // Get all the taskBiddingTrackingTimeList where startTime is null
        defaultTaskBiddingTrackingTimeShouldNotBeFound("startTime.specified=false");
    }

    @Test
    @Transactional
    public void getAllTaskBiddingTrackingTimesByEndTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        taskBiddingTrackingTimeRepository.saveAndFlush(taskBiddingTrackingTime);

        // Get all the taskBiddingTrackingTimeList where endTime equals to DEFAULT_END_TIME
        defaultTaskBiddingTrackingTimeShouldBeFound("endTime.equals=" + DEFAULT_END_TIME);

        // Get all the taskBiddingTrackingTimeList where endTime equals to UPDATED_END_TIME
        defaultTaskBiddingTrackingTimeShouldNotBeFound("endTime.equals=" + UPDATED_END_TIME);
    }

    @Test
    @Transactional
    public void getAllTaskBiddingTrackingTimesByEndTimeIsInShouldWork() throws Exception {
        // Initialize the database
        taskBiddingTrackingTimeRepository.saveAndFlush(taskBiddingTrackingTime);

        // Get all the taskBiddingTrackingTimeList where endTime in DEFAULT_END_TIME or UPDATED_END_TIME
        defaultTaskBiddingTrackingTimeShouldBeFound("endTime.in=" + DEFAULT_END_TIME + "," + UPDATED_END_TIME);

        // Get all the taskBiddingTrackingTimeList where endTime equals to UPDATED_END_TIME
        defaultTaskBiddingTrackingTimeShouldNotBeFound("endTime.in=" + UPDATED_END_TIME);
    }

    @Test
    @Transactional
    public void getAllTaskBiddingTrackingTimesByEndTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        taskBiddingTrackingTimeRepository.saveAndFlush(taskBiddingTrackingTime);

        // Get all the taskBiddingTrackingTimeList where endTime is not null
        defaultTaskBiddingTrackingTimeShouldBeFound("endTime.specified=true");

        // Get all the taskBiddingTrackingTimeList where endTime is null
        defaultTaskBiddingTrackingTimeShouldNotBeFound("endTime.specified=false");
    }

    @Test
    @Transactional
    public void getAllTaskBiddingTrackingTimesByStartStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        taskBiddingTrackingTimeRepository.saveAndFlush(taskBiddingTrackingTime);

        // Get all the taskBiddingTrackingTimeList where startStatus equals to DEFAULT_START_STATUS
        defaultTaskBiddingTrackingTimeShouldBeFound("startStatus.equals=" + DEFAULT_START_STATUS);

        // Get all the taskBiddingTrackingTimeList where startStatus equals to UPDATED_START_STATUS
        defaultTaskBiddingTrackingTimeShouldNotBeFound("startStatus.equals=" + UPDATED_START_STATUS);
    }

    @Test
    @Transactional
    public void getAllTaskBiddingTrackingTimesByStartStatusIsInShouldWork() throws Exception {
        // Initialize the database
        taskBiddingTrackingTimeRepository.saveAndFlush(taskBiddingTrackingTime);

        // Get all the taskBiddingTrackingTimeList where startStatus in DEFAULT_START_STATUS or UPDATED_START_STATUS
        defaultTaskBiddingTrackingTimeShouldBeFound("startStatus.in=" + DEFAULT_START_STATUS + "," + UPDATED_START_STATUS);

        // Get all the taskBiddingTrackingTimeList where startStatus equals to UPDATED_START_STATUS
        defaultTaskBiddingTrackingTimeShouldNotBeFound("startStatus.in=" + UPDATED_START_STATUS);
    }

    @Test
    @Transactional
    public void getAllTaskBiddingTrackingTimesByStartStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        taskBiddingTrackingTimeRepository.saveAndFlush(taskBiddingTrackingTime);

        // Get all the taskBiddingTrackingTimeList where startStatus is not null
        defaultTaskBiddingTrackingTimeShouldBeFound("startStatus.specified=true");

        // Get all the taskBiddingTrackingTimeList where startStatus is null
        defaultTaskBiddingTrackingTimeShouldNotBeFound("startStatus.specified=false");
    }

    @Test
    @Transactional
    public void getAllTaskBiddingTrackingTimesByEndStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        taskBiddingTrackingTimeRepository.saveAndFlush(taskBiddingTrackingTime);

        // Get all the taskBiddingTrackingTimeList where endStatus equals to DEFAULT_END_STATUS
        defaultTaskBiddingTrackingTimeShouldBeFound("endStatus.equals=" + DEFAULT_END_STATUS);

        // Get all the taskBiddingTrackingTimeList where endStatus equals to UPDATED_END_STATUS
        defaultTaskBiddingTrackingTimeShouldNotBeFound("endStatus.equals=" + UPDATED_END_STATUS);
    }

    @Test
    @Transactional
    public void getAllTaskBiddingTrackingTimesByEndStatusIsInShouldWork() throws Exception {
        // Initialize the database
        taskBiddingTrackingTimeRepository.saveAndFlush(taskBiddingTrackingTime);

        // Get all the taskBiddingTrackingTimeList where endStatus in DEFAULT_END_STATUS or UPDATED_END_STATUS
        defaultTaskBiddingTrackingTimeShouldBeFound("endStatus.in=" + DEFAULT_END_STATUS + "," + UPDATED_END_STATUS);

        // Get all the taskBiddingTrackingTimeList where endStatus equals to UPDATED_END_STATUS
        defaultTaskBiddingTrackingTimeShouldNotBeFound("endStatus.in=" + UPDATED_END_STATUS);
    }

    @Test
    @Transactional
    public void getAllTaskBiddingTrackingTimesByEndStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        taskBiddingTrackingTimeRepository.saveAndFlush(taskBiddingTrackingTime);

        // Get all the taskBiddingTrackingTimeList where endStatus is not null
        defaultTaskBiddingTrackingTimeShouldBeFound("endStatus.specified=true");

        // Get all the taskBiddingTrackingTimeList where endStatus is null
        defaultTaskBiddingTrackingTimeShouldNotBeFound("endStatus.specified=false");
    }

    @Test
    @Transactional
    public void getAllTaskBiddingTrackingTimesByDurationIsEqualToSomething() throws Exception {
        // Initialize the database
        taskBiddingTrackingTimeRepository.saveAndFlush(taskBiddingTrackingTime);

        // Get all the taskBiddingTrackingTimeList where duration equals to DEFAULT_DURATION
        defaultTaskBiddingTrackingTimeShouldBeFound("duration.equals=" + DEFAULT_DURATION);

        // Get all the taskBiddingTrackingTimeList where duration equals to UPDATED_DURATION
        defaultTaskBiddingTrackingTimeShouldNotBeFound("duration.equals=" + UPDATED_DURATION);
    }

    @Test
    @Transactional
    public void getAllTaskBiddingTrackingTimesByDurationIsInShouldWork() throws Exception {
        // Initialize the database
        taskBiddingTrackingTimeRepository.saveAndFlush(taskBiddingTrackingTime);

        // Get all the taskBiddingTrackingTimeList where duration in DEFAULT_DURATION or UPDATED_DURATION
        defaultTaskBiddingTrackingTimeShouldBeFound("duration.in=" + DEFAULT_DURATION + "," + UPDATED_DURATION);

        // Get all the taskBiddingTrackingTimeList where duration equals to UPDATED_DURATION
        defaultTaskBiddingTrackingTimeShouldNotBeFound("duration.in=" + UPDATED_DURATION);
    }

    @Test
    @Transactional
    public void getAllTaskBiddingTrackingTimesByDurationIsNullOrNotNull() throws Exception {
        // Initialize the database
        taskBiddingTrackingTimeRepository.saveAndFlush(taskBiddingTrackingTime);

        // Get all the taskBiddingTrackingTimeList where duration is not null
        defaultTaskBiddingTrackingTimeShouldBeFound("duration.specified=true");

        // Get all the taskBiddingTrackingTimeList where duration is null
        defaultTaskBiddingTrackingTimeShouldNotBeFound("duration.specified=false");
    }

    @Test
    @Transactional
    public void getAllTaskBiddingTrackingTimesByDurationIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        taskBiddingTrackingTimeRepository.saveAndFlush(taskBiddingTrackingTime);

        // Get all the taskBiddingTrackingTimeList where duration greater than or equals to DEFAULT_DURATION
        defaultTaskBiddingTrackingTimeShouldBeFound("duration.greaterOrEqualThan=" + DEFAULT_DURATION);

        // Get all the taskBiddingTrackingTimeList where duration greater than or equals to UPDATED_DURATION
        defaultTaskBiddingTrackingTimeShouldNotBeFound("duration.greaterOrEqualThan=" + UPDATED_DURATION);
    }

    @Test
    @Transactional
    public void getAllTaskBiddingTrackingTimesByDurationIsLessThanSomething() throws Exception {
        // Initialize the database
        taskBiddingTrackingTimeRepository.saveAndFlush(taskBiddingTrackingTime);

        // Get all the taskBiddingTrackingTimeList where duration less than or equals to DEFAULT_DURATION
        defaultTaskBiddingTrackingTimeShouldNotBeFound("duration.lessThan=" + DEFAULT_DURATION);

        // Get all the taskBiddingTrackingTimeList where duration less than or equals to UPDATED_DURATION
        defaultTaskBiddingTrackingTimeShouldBeFound("duration.lessThan=" + UPDATED_DURATION);
    }


    @Test
    @Transactional
    public void getAllTaskBiddingTrackingTimesByBiddingScopeIsEqualToSomething() throws Exception {
        // Initialize the database
        taskBiddingTrackingTimeRepository.saveAndFlush(taskBiddingTrackingTime);

        // Get all the taskBiddingTrackingTimeList where biddingScope equals to DEFAULT_BIDDING_SCOPE
        defaultTaskBiddingTrackingTimeShouldBeFound("biddingScope.equals=" + DEFAULT_BIDDING_SCOPE);

        // Get all the taskBiddingTrackingTimeList where biddingScope equals to UPDATED_BIDDING_SCOPE
        defaultTaskBiddingTrackingTimeShouldNotBeFound("biddingScope.equals=" + UPDATED_BIDDING_SCOPE);
    }

    @Test
    @Transactional
    public void getAllTaskBiddingTrackingTimesByBiddingScopeIsInShouldWork() throws Exception {
        // Initialize the database
        taskBiddingTrackingTimeRepository.saveAndFlush(taskBiddingTrackingTime);

        // Get all the taskBiddingTrackingTimeList where biddingScope in DEFAULT_BIDDING_SCOPE or UPDATED_BIDDING_SCOPE
        defaultTaskBiddingTrackingTimeShouldBeFound("biddingScope.in=" + DEFAULT_BIDDING_SCOPE + "," + UPDATED_BIDDING_SCOPE);

        // Get all the taskBiddingTrackingTimeList where biddingScope equals to UPDATED_BIDDING_SCOPE
        defaultTaskBiddingTrackingTimeShouldNotBeFound("biddingScope.in=" + UPDATED_BIDDING_SCOPE);
    }

    @Test
    @Transactional
    public void getAllTaskBiddingTrackingTimesByBiddingScopeIsNullOrNotNull() throws Exception {
        // Initialize the database
        taskBiddingTrackingTimeRepository.saveAndFlush(taskBiddingTrackingTime);

        // Get all the taskBiddingTrackingTimeList where biddingScope is not null
        defaultTaskBiddingTrackingTimeShouldBeFound("biddingScope.specified=true");

        // Get all the taskBiddingTrackingTimeList where biddingScope is null
        defaultTaskBiddingTrackingTimeShouldNotBeFound("biddingScope.specified=false");
    }

    @Test
    @Transactional
    public void getAllTaskBiddingTrackingTimesByTaskIsEqualToSomething() throws Exception {
        // Initialize the database
        Tasks task = TasksResourceIntTest.createEntity(em);
        em.persist(task);
        em.flush();
        taskBiddingTrackingTime.setTask(task);
        taskBiddingTrackingTimeRepository.saveAndFlush(taskBiddingTrackingTime);
        Long taskId = task.getId();

        // Get all the taskBiddingTrackingTimeList where task equals to taskId
        defaultTaskBiddingTrackingTimeShouldBeFound("taskId.equals=" + taskId);

        // Get all the taskBiddingTrackingTimeList where task equals to taskId + 1
        defaultTaskBiddingTrackingTimeShouldNotBeFound("taskId.equals=" + (taskId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultTaskBiddingTrackingTimeShouldBeFound(String filter) throws Exception {
        restTaskBiddingTrackingTimeMockMvc.perform(get("/api/task-bidding-tracking-times?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(taskBiddingTrackingTime.getId().intValue())))
            .andExpect(jsonPath("$.[*].userLogin").value(hasItem(DEFAULT_USER_LOGIN.toString())))
            .andExpect(jsonPath("$.[*].role").value(hasItem(DEFAULT_ROLE.toString())))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME.toString())))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(DEFAULT_END_TIME.toString())))
            .andExpect(jsonPath("$.[*].startStatus").value(hasItem(DEFAULT_START_STATUS.toString())))
            .andExpect(jsonPath("$.[*].endStatus").value(hasItem(DEFAULT_END_STATUS.toString())))
            .andExpect(jsonPath("$.[*].duration").value(hasItem(DEFAULT_DURATION)))
            .andExpect(jsonPath("$.[*].biddingScope").value(hasItem(DEFAULT_BIDDING_SCOPE.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultTaskBiddingTrackingTimeShouldNotBeFound(String filter) throws Exception {
        restTaskBiddingTrackingTimeMockMvc.perform(get("/api/task-bidding-tracking-times?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingTaskBiddingTrackingTime() throws Exception {
        // Get the taskBiddingTrackingTime
        restTaskBiddingTrackingTimeMockMvc.perform(get("/api/task-bidding-tracking-times/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTaskBiddingTrackingTime() throws Exception {
        // Initialize the database
        taskBiddingTrackingTimeRepository.saveAndFlush(taskBiddingTrackingTime);
        taskBiddingTrackingTimeSearchRepository.save(taskBiddingTrackingTime);
        int databaseSizeBeforeUpdate = taskBiddingTrackingTimeRepository.findAll().size();

        // Update the taskBiddingTrackingTime
        TaskBiddingTrackingTime updatedTaskBiddingTrackingTime = taskBiddingTrackingTimeRepository.findOne(taskBiddingTrackingTime.getId());
        // Disconnect from session so that the updates on updatedTaskBiddingTrackingTime are not directly saved in db
        em.detach(updatedTaskBiddingTrackingTime);
        updatedTaskBiddingTrackingTime
            .userLogin(UPDATED_USER_LOGIN)
            .role(UPDATED_ROLE)
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME)
            .startStatus(UPDATED_START_STATUS)
            .endStatus(UPDATED_END_STATUS)
            .duration(UPDATED_DURATION)
            .biddingScope(UPDATED_BIDDING_SCOPE);
        TaskBiddingTrackingTimeDTO taskBiddingTrackingTimeDTO = taskBiddingTrackingTimeMapper.toDto(updatedTaskBiddingTrackingTime);

        restTaskBiddingTrackingTimeMockMvc.perform(put("/api/task-bidding-tracking-times")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(taskBiddingTrackingTimeDTO)))
            .andExpect(status().isOk());

        // Validate the TaskBiddingTrackingTime in the database
        List<TaskBiddingTrackingTime> taskBiddingTrackingTimeList = taskBiddingTrackingTimeRepository.findAll();
        assertThat(taskBiddingTrackingTimeList).hasSize(databaseSizeBeforeUpdate);
        TaskBiddingTrackingTime testTaskBiddingTrackingTime = taskBiddingTrackingTimeList.get(taskBiddingTrackingTimeList.size() - 1);
        assertThat(testTaskBiddingTrackingTime.getUserLogin()).isEqualTo(UPDATED_USER_LOGIN);
        assertThat(testTaskBiddingTrackingTime.getRole()).isEqualTo(UPDATED_ROLE);
        assertThat(testTaskBiddingTrackingTime.getStartTime()).isEqualTo(UPDATED_START_TIME);
        assertThat(testTaskBiddingTrackingTime.getEndTime()).isEqualTo(UPDATED_END_TIME);
        assertThat(testTaskBiddingTrackingTime.getStartStatus()).isEqualTo(UPDATED_START_STATUS);
        assertThat(testTaskBiddingTrackingTime.getEndStatus()).isEqualTo(UPDATED_END_STATUS);
        assertThat(testTaskBiddingTrackingTime.getDuration()).isEqualTo(UPDATED_DURATION);
        assertThat(testTaskBiddingTrackingTime.getBiddingScope()).isEqualTo(UPDATED_BIDDING_SCOPE);

        // Validate the TaskBiddingTrackingTime in Elasticsearch
        TaskBiddingTrackingTime taskBiddingTrackingTimeEs = taskBiddingTrackingTimeSearchRepository.findOne(testTaskBiddingTrackingTime.getId());
        assertThat(taskBiddingTrackingTimeEs).isEqualToIgnoringGivenFields(testTaskBiddingTrackingTime);
    }

    @Test
    @Transactional
    public void updateNonExistingTaskBiddingTrackingTime() throws Exception {
        int databaseSizeBeforeUpdate = taskBiddingTrackingTimeRepository.findAll().size();

        // Create the TaskBiddingTrackingTime
        TaskBiddingTrackingTimeDTO taskBiddingTrackingTimeDTO = taskBiddingTrackingTimeMapper.toDto(taskBiddingTrackingTime);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTaskBiddingTrackingTimeMockMvc.perform(put("/api/task-bidding-tracking-times")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(taskBiddingTrackingTimeDTO)))
            .andExpect(status().isCreated());

        // Validate the TaskBiddingTrackingTime in the database
        List<TaskBiddingTrackingTime> taskBiddingTrackingTimeList = taskBiddingTrackingTimeRepository.findAll();
        assertThat(taskBiddingTrackingTimeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTaskBiddingTrackingTime() throws Exception {
        // Initialize the database
        taskBiddingTrackingTimeRepository.saveAndFlush(taskBiddingTrackingTime);
        taskBiddingTrackingTimeSearchRepository.save(taskBiddingTrackingTime);
        int databaseSizeBeforeDelete = taskBiddingTrackingTimeRepository.findAll().size();

        // Get the taskBiddingTrackingTime
        restTaskBiddingTrackingTimeMockMvc.perform(delete("/api/task-bidding-tracking-times/{id}", taskBiddingTrackingTime.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean taskBiddingTrackingTimeExistsInEs = taskBiddingTrackingTimeSearchRepository.exists(taskBiddingTrackingTime.getId());
        assertThat(taskBiddingTrackingTimeExistsInEs).isFalse();

        // Validate the database is empty
        List<TaskBiddingTrackingTime> taskBiddingTrackingTimeList = taskBiddingTrackingTimeRepository.findAll();
        assertThat(taskBiddingTrackingTimeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchTaskBiddingTrackingTime() throws Exception {
        // Initialize the database
        taskBiddingTrackingTimeRepository.saveAndFlush(taskBiddingTrackingTime);
        taskBiddingTrackingTimeSearchRepository.save(taskBiddingTrackingTime);

        // Search the taskBiddingTrackingTime
        restTaskBiddingTrackingTimeMockMvc.perform(get("/api/_search/task-bidding-tracking-times?query=id:" + taskBiddingTrackingTime.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(taskBiddingTrackingTime.getId().intValue())))
            .andExpect(jsonPath("$.[*].userLogin").value(hasItem(DEFAULT_USER_LOGIN.toString())))
            .andExpect(jsonPath("$.[*].role").value(hasItem(DEFAULT_ROLE.toString())))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME.toString())))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(DEFAULT_END_TIME.toString())))
            .andExpect(jsonPath("$.[*].startStatus").value(hasItem(DEFAULT_START_STATUS.toString())))
            .andExpect(jsonPath("$.[*].endStatus").value(hasItem(DEFAULT_END_STATUS.toString())))
            .andExpect(jsonPath("$.[*].duration").value(hasItem(DEFAULT_DURATION)))
            .andExpect(jsonPath("$.[*].biddingScope").value(hasItem(DEFAULT_BIDDING_SCOPE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TaskBiddingTrackingTime.class);
        TaskBiddingTrackingTime taskBiddingTrackingTime1 = new TaskBiddingTrackingTime();
        taskBiddingTrackingTime1.setId(1L);
        TaskBiddingTrackingTime taskBiddingTrackingTime2 = new TaskBiddingTrackingTime();
        taskBiddingTrackingTime2.setId(taskBiddingTrackingTime1.getId());
        assertThat(taskBiddingTrackingTime1).isEqualTo(taskBiddingTrackingTime2);
        taskBiddingTrackingTime2.setId(2L);
        assertThat(taskBiddingTrackingTime1).isNotEqualTo(taskBiddingTrackingTime2);
        taskBiddingTrackingTime1.setId(null);
        assertThat(taskBiddingTrackingTime1).isNotEqualTo(taskBiddingTrackingTime2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TaskBiddingTrackingTimeDTO.class);
        TaskBiddingTrackingTimeDTO taskBiddingTrackingTimeDTO1 = new TaskBiddingTrackingTimeDTO();
        taskBiddingTrackingTimeDTO1.setId(1L);
        TaskBiddingTrackingTimeDTO taskBiddingTrackingTimeDTO2 = new TaskBiddingTrackingTimeDTO();
        assertThat(taskBiddingTrackingTimeDTO1).isNotEqualTo(taskBiddingTrackingTimeDTO2);
        taskBiddingTrackingTimeDTO2.setId(taskBiddingTrackingTimeDTO1.getId());
        assertThat(taskBiddingTrackingTimeDTO1).isEqualTo(taskBiddingTrackingTimeDTO2);
        taskBiddingTrackingTimeDTO2.setId(2L);
        assertThat(taskBiddingTrackingTimeDTO1).isNotEqualTo(taskBiddingTrackingTimeDTO2);
        taskBiddingTrackingTimeDTO1.setId(null);
        assertThat(taskBiddingTrackingTimeDTO1).isNotEqualTo(taskBiddingTrackingTimeDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(taskBiddingTrackingTimeMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(taskBiddingTrackingTimeMapper.fromId(null)).isNull();
    }
}
