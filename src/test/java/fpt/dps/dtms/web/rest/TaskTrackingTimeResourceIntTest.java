package fpt.dps.dtms.web.rest;

import fpt.dps.dtms.DtmsApp;

import fpt.dps.dtms.domain.TaskTrackingTime;
import fpt.dps.dtms.repository.TaskTrackingTimeRepository;
import fpt.dps.dtms.service.TaskTrackingTimeQueryService;
import fpt.dps.dtms.service.TaskTrackingTimeService;
import fpt.dps.dtms.repository.search.TaskTrackingTimeSearchRepository;
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
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static fpt.dps.dtms.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the TaskTrackingTimeResource REST controller.
 *
 * @see TaskTrackingTimeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DtmsApp.class)
public class TaskTrackingTimeResourceIntTest {

    private static final Long DEFAULT_TASK_ID = 1L;
    private static final Long UPDATED_TASK_ID = 2L;

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

    @Autowired
    private TaskTrackingTimeRepository taskTrackingTimeRepository;

    @Autowired
    private TaskTrackingTimeService taskTrackingTimeService;
    
    @Autowired
    private TaskTrackingTimeQueryService taskTrackingTimeQueryService;

    @Autowired
    private TaskTrackingTimeSearchRepository taskTrackingTimeSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTaskTrackingTimeMockMvc;

    private TaskTrackingTime taskTrackingTime;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TaskTrackingTimeResource taskTrackingTimeResource = new TaskTrackingTimeResource(taskTrackingTimeService, taskTrackingTimeQueryService);
        this.restTaskTrackingTimeMockMvc = MockMvcBuilders.standaloneSetup(taskTrackingTimeResource)
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
    public static TaskTrackingTime createEntity(EntityManager em) {
        TaskTrackingTime taskTrackingTime = new TaskTrackingTime()
            .taskId(DEFAULT_TASK_ID)
            .userLogin(DEFAULT_USER_LOGIN)
            .role(DEFAULT_ROLE)
            .startTime(DEFAULT_START_TIME)
            .endTime(DEFAULT_END_TIME)
            .startStatus(DEFAULT_START_STATUS)
            .endStatus(DEFAULT_END_STATUS)
            .duration(DEFAULT_DURATION);
        return taskTrackingTime;
    }

    @Before
    public void initTest() {
        taskTrackingTimeSearchRepository.deleteAll();
        taskTrackingTime = createEntity(em);
    }

    @Test
    @Transactional
    public void createTaskTrackingTime() throws Exception {
        int databaseSizeBeforeCreate = taskTrackingTimeRepository.findAll().size();

        // Create the TaskTrackingTime
        restTaskTrackingTimeMockMvc.perform(post("/api/task-tracking-times")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(taskTrackingTime)))
            .andExpect(status().isCreated());

        // Validate the TaskTrackingTime in the database
        List<TaskTrackingTime> taskTrackingTimeList = taskTrackingTimeRepository.findAll();
        assertThat(taskTrackingTimeList).hasSize(databaseSizeBeforeCreate + 1);
        TaskTrackingTime testTaskTrackingTime = taskTrackingTimeList.get(taskTrackingTimeList.size() - 1);
        assertThat(testTaskTrackingTime.getTaskId()).isEqualTo(DEFAULT_TASK_ID);
        assertThat(testTaskTrackingTime.getUserLogin()).isEqualTo(DEFAULT_USER_LOGIN);
        assertThat(testTaskTrackingTime.getRole()).isEqualTo(DEFAULT_ROLE);
        assertThat(testTaskTrackingTime.getStartTime()).isEqualTo(DEFAULT_START_TIME);
        assertThat(testTaskTrackingTime.getEndTime()).isEqualTo(DEFAULT_END_TIME);
        assertThat(testTaskTrackingTime.getStartStatus()).isEqualTo(DEFAULT_START_STATUS);
        assertThat(testTaskTrackingTime.getEndStatus()).isEqualTo(DEFAULT_END_STATUS);
        assertThat(testTaskTrackingTime.getDuration()).isEqualTo(DEFAULT_DURATION);

        // Validate the TaskTrackingTime in Elasticsearch
        TaskTrackingTime taskTrackingTimeEs = taskTrackingTimeSearchRepository.findOne(testTaskTrackingTime.getId());
        assertThat(taskTrackingTimeEs).isEqualToIgnoringGivenFields(testTaskTrackingTime);
    }

    @Test
    @Transactional
    public void createTaskTrackingTimeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = taskTrackingTimeRepository.findAll().size();

        // Create the TaskTrackingTime with an existing ID
        taskTrackingTime.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTaskTrackingTimeMockMvc.perform(post("/api/task-tracking-times")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(taskTrackingTime)))
            .andExpect(status().isBadRequest());

        // Validate the TaskTrackingTime in the database
        List<TaskTrackingTime> taskTrackingTimeList = taskTrackingTimeRepository.findAll();
        assertThat(taskTrackingTimeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTaskIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = taskTrackingTimeRepository.findAll().size();
        // set the field null
        taskTrackingTime.setTaskId(null);

        // Create the TaskTrackingTime, which fails.

        restTaskTrackingTimeMockMvc.perform(post("/api/task-tracking-times")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(taskTrackingTime)))
            .andExpect(status().isBadRequest());

        List<TaskTrackingTime> taskTrackingTimeList = taskTrackingTimeRepository.findAll();
        assertThat(taskTrackingTimeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUserLoginIsRequired() throws Exception {
        int databaseSizeBeforeTest = taskTrackingTimeRepository.findAll().size();
        // set the field null
        taskTrackingTime.setUserLogin(null);

        // Create the TaskTrackingTime, which fails.

        restTaskTrackingTimeMockMvc.perform(post("/api/task-tracking-times")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(taskTrackingTime)))
            .andExpect(status().isBadRequest());

        List<TaskTrackingTime> taskTrackingTimeList = taskTrackingTimeRepository.findAll();
        assertThat(taskTrackingTimeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkRoleIsRequired() throws Exception {
        int databaseSizeBeforeTest = taskTrackingTimeRepository.findAll().size();
        // set the field null
        taskTrackingTime.setRole(null);

        // Create the TaskTrackingTime, which fails.

        restTaskTrackingTimeMockMvc.perform(post("/api/task-tracking-times")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(taskTrackingTime)))
            .andExpect(status().isBadRequest());

        List<TaskTrackingTime> taskTrackingTimeList = taskTrackingTimeRepository.findAll();
        assertThat(taskTrackingTimeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStartTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = taskTrackingTimeRepository.findAll().size();
        // set the field null
        taskTrackingTime.setStartTime(null);

        // Create the TaskTrackingTime, which fails.

        restTaskTrackingTimeMockMvc.perform(post("/api/task-tracking-times")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(taskTrackingTime)))
            .andExpect(status().isBadRequest());

        List<TaskTrackingTime> taskTrackingTimeList = taskTrackingTimeRepository.findAll();
        assertThat(taskTrackingTimeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTaskTrackingTimes() throws Exception {
        // Initialize the database
        taskTrackingTimeRepository.saveAndFlush(taskTrackingTime);

        // Get all the taskTrackingTimeList
        restTaskTrackingTimeMockMvc.perform(get("/api/task-tracking-times?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(taskTrackingTime.getId().intValue())))
            .andExpect(jsonPath("$.[*].taskId").value(hasItem(DEFAULT_TASK_ID.intValue())))
            .andExpect(jsonPath("$.[*].userLogin").value(hasItem(DEFAULT_USER_LOGIN.toString())))
            .andExpect(jsonPath("$.[*].role").value(hasItem(DEFAULT_ROLE.toString())))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME.toString())))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(DEFAULT_END_TIME.toString())))
            .andExpect(jsonPath("$.[*].startStatus").value(hasItem(DEFAULT_START_STATUS.toString())))
            .andExpect(jsonPath("$.[*].endStatus").value(hasItem(DEFAULT_END_STATUS.toString())))
            .andExpect(jsonPath("$.[*].duration").value(hasItem(DEFAULT_DURATION)));
    }

    @Test
    @Transactional
    public void getTaskTrackingTime() throws Exception {
        // Initialize the database
        taskTrackingTimeRepository.saveAndFlush(taskTrackingTime);

        // Get the taskTrackingTime
        restTaskTrackingTimeMockMvc.perform(get("/api/task-tracking-times/{id}", taskTrackingTime.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(taskTrackingTime.getId().intValue()))
            .andExpect(jsonPath("$.taskId").value(DEFAULT_TASK_ID.intValue()))
            .andExpect(jsonPath("$.userLogin").value(DEFAULT_USER_LOGIN.toString()))
            .andExpect(jsonPath("$.role").value(DEFAULT_ROLE.toString()))
            .andExpect(jsonPath("$.startTime").value(DEFAULT_START_TIME.toString()))
            .andExpect(jsonPath("$.endTime").value(DEFAULT_END_TIME.toString()))
            .andExpect(jsonPath("$.startStatus").value(DEFAULT_START_STATUS.toString()))
            .andExpect(jsonPath("$.endStatus").value(DEFAULT_END_STATUS.toString()))
            .andExpect(jsonPath("$.duration").value(DEFAULT_DURATION));
    }

    @Test
    @Transactional
    public void getNonExistingTaskTrackingTime() throws Exception {
        // Get the taskTrackingTime
        restTaskTrackingTimeMockMvc.perform(get("/api/task-tracking-times/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTaskTrackingTime() throws Exception {
        // Initialize the database
        taskTrackingTimeService.save(taskTrackingTime);

        int databaseSizeBeforeUpdate = taskTrackingTimeRepository.findAll().size();

        // Update the taskTrackingTime
        TaskTrackingTime updatedTaskTrackingTime = taskTrackingTimeRepository.findOne(taskTrackingTime.getId());
        // Disconnect from session so that the updates on updatedTaskTrackingTime are not directly saved in db
        em.detach(updatedTaskTrackingTime);
        updatedTaskTrackingTime
            .taskId(UPDATED_TASK_ID)
            .userLogin(UPDATED_USER_LOGIN)
            .role(UPDATED_ROLE)
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME)
            .startStatus(UPDATED_START_STATUS)
            .endStatus(UPDATED_END_STATUS)
            .duration(UPDATED_DURATION);

        restTaskTrackingTimeMockMvc.perform(put("/api/task-tracking-times")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTaskTrackingTime)))
            .andExpect(status().isOk());

        // Validate the TaskTrackingTime in the database
        List<TaskTrackingTime> taskTrackingTimeList = taskTrackingTimeRepository.findAll();
        assertThat(taskTrackingTimeList).hasSize(databaseSizeBeforeUpdate);
        TaskTrackingTime testTaskTrackingTime = taskTrackingTimeList.get(taskTrackingTimeList.size() - 1);
        assertThat(testTaskTrackingTime.getTaskId()).isEqualTo(UPDATED_TASK_ID);
        assertThat(testTaskTrackingTime.getUserLogin()).isEqualTo(UPDATED_USER_LOGIN);
        assertThat(testTaskTrackingTime.getRole()).isEqualTo(UPDATED_ROLE);
        assertThat(testTaskTrackingTime.getStartTime()).isEqualTo(UPDATED_START_TIME);
        assertThat(testTaskTrackingTime.getEndTime()).isEqualTo(UPDATED_END_TIME);
        assertThat(testTaskTrackingTime.getStartStatus()).isEqualTo(UPDATED_START_STATUS);
        assertThat(testTaskTrackingTime.getEndStatus()).isEqualTo(UPDATED_END_STATUS);
        assertThat(testTaskTrackingTime.getDuration()).isEqualTo(UPDATED_DURATION);

        // Validate the TaskTrackingTime in Elasticsearch
        TaskTrackingTime taskTrackingTimeEs = taskTrackingTimeSearchRepository.findOne(testTaskTrackingTime.getId());
        assertThat(taskTrackingTimeEs).isEqualToIgnoringGivenFields(testTaskTrackingTime);
    }

    @Test
    @Transactional
    public void updateNonExistingTaskTrackingTime() throws Exception {
        int databaseSizeBeforeUpdate = taskTrackingTimeRepository.findAll().size();

        // Create the TaskTrackingTime

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTaskTrackingTimeMockMvc.perform(put("/api/task-tracking-times")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(taskTrackingTime)))
            .andExpect(status().isCreated());

        // Validate the TaskTrackingTime in the database
        List<TaskTrackingTime> taskTrackingTimeList = taskTrackingTimeRepository.findAll();
        assertThat(taskTrackingTimeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTaskTrackingTime() throws Exception {
        // Initialize the database
        taskTrackingTimeService.save(taskTrackingTime);

        int databaseSizeBeforeDelete = taskTrackingTimeRepository.findAll().size();

        // Get the taskTrackingTime
        restTaskTrackingTimeMockMvc.perform(delete("/api/task-tracking-times/{id}", taskTrackingTime.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean taskTrackingTimeExistsInEs = taskTrackingTimeSearchRepository.exists(taskTrackingTime.getId());
        assertThat(taskTrackingTimeExistsInEs).isFalse();

        // Validate the database is empty
        List<TaskTrackingTime> taskTrackingTimeList = taskTrackingTimeRepository.findAll();
        assertThat(taskTrackingTimeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchTaskTrackingTime() throws Exception {
        // Initialize the database
        taskTrackingTimeService.save(taskTrackingTime);

        // Search the taskTrackingTime
        restTaskTrackingTimeMockMvc.perform(get("/api/_search/task-tracking-times?query=id:" + taskTrackingTime.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(taskTrackingTime.getId().intValue())))
            .andExpect(jsonPath("$.[*].taskId").value(hasItem(DEFAULT_TASK_ID.intValue())))
            .andExpect(jsonPath("$.[*].userLogin").value(hasItem(DEFAULT_USER_LOGIN.toString())))
            .andExpect(jsonPath("$.[*].role").value(hasItem(DEFAULT_ROLE.toString())))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME.toString())))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(DEFAULT_END_TIME.toString())))
            .andExpect(jsonPath("$.[*].startStatus").value(hasItem(DEFAULT_START_STATUS.toString())))
            .andExpect(jsonPath("$.[*].endStatus").value(hasItem(DEFAULT_END_STATUS.toString())))
            .andExpect(jsonPath("$.[*].duration").value(hasItem(DEFAULT_DURATION)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TaskTrackingTime.class);
        TaskTrackingTime taskTrackingTime1 = new TaskTrackingTime();
        taskTrackingTime1.setId(1L);
        TaskTrackingTime taskTrackingTime2 = new TaskTrackingTime();
        taskTrackingTime2.setId(taskTrackingTime1.getId());
        assertThat(taskTrackingTime1).isEqualTo(taskTrackingTime2);
        taskTrackingTime2.setId(2L);
        assertThat(taskTrackingTime1).isNotEqualTo(taskTrackingTime2);
        taskTrackingTime1.setId(null);
        assertThat(taskTrackingTime1).isNotEqualTo(taskTrackingTime2);
    }
}
