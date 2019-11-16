package fpt.dps.dtms.web.rest;

import fpt.dps.dtms.DtmsApp;

import fpt.dps.dtms.domain.TaskBidding;
import fpt.dps.dtms.domain.Tasks;
import fpt.dps.dtms.repository.TaskBiddingRepository;
import fpt.dps.dtms.service.TaskBiddingService;
import fpt.dps.dtms.repository.search.TaskBiddingSearchRepository;
import fpt.dps.dtms.service.dto.TaskBiddingDTO;
import fpt.dps.dtms.service.mapper.TaskBiddingMapper;
import fpt.dps.dtms.web.rest.errors.ExceptionTranslator;
import fpt.dps.dtms.service.dto.TaskBiddingCriteria;
import fpt.dps.dtms.service.TaskBiddingQueryService;

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

import fpt.dps.dtms.domain.enumeration.BiddingScope;
import fpt.dps.dtms.domain.enumeration.BiddingStatus;
/**
 * Test class for the TaskBiddingResource REST controller.
 *
 * @see TaskBiddingResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DtmsApp.class)
public class TaskBiddingResourceIntTest {

    private static final BiddingScope DEFAULT_BIDDING_SCOPE = BiddingScope.PROJECT;
    private static final BiddingScope UPDATED_BIDDING_SCOPE = BiddingScope.BU;

    private static final BiddingStatus DEFAULT_BIDDING_STATUS = BiddingStatus.NA;
    private static final BiddingStatus UPDATED_BIDDING_STATUS = BiddingStatus.HOLDING;

    @Autowired
    private TaskBiddingRepository taskBiddingRepository;

    @Autowired
    private TaskBiddingMapper taskBiddingMapper;

    @Autowired
    private TaskBiddingService taskBiddingService;

    @Autowired
    private TaskBiddingSearchRepository taskBiddingSearchRepository;

    @Autowired
    private TaskBiddingQueryService taskBiddingQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTaskBiddingMockMvc;

    private TaskBidding taskBidding;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TaskBiddingResource taskBiddingResource = new TaskBiddingResource(taskBiddingService, taskBiddingQueryService, null);
        this.restTaskBiddingMockMvc = MockMvcBuilders.standaloneSetup(taskBiddingResource)
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
    public static TaskBidding createEntity(EntityManager em) {
        TaskBidding taskBidding = new TaskBidding()
            .biddingScope(DEFAULT_BIDDING_SCOPE)
            .biddingStatus(DEFAULT_BIDDING_STATUS);
        return taskBidding;
    }

    @Before
    public void initTest() {
        taskBiddingSearchRepository.deleteAll();
        taskBidding = createEntity(em);
    }

    @Test
    @Transactional
    public void createTaskBidding() throws Exception {
        int databaseSizeBeforeCreate = taskBiddingRepository.findAll().size();

        // Create the TaskBidding
        TaskBiddingDTO taskBiddingDTO = taskBiddingMapper.toDto(taskBidding);
        restTaskBiddingMockMvc.perform(post("/api/task-biddings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(taskBiddingDTO)))
            .andExpect(status().isCreated());

        // Validate the TaskBidding in the database
        List<TaskBidding> taskBiddingList = taskBiddingRepository.findAll();
        assertThat(taskBiddingList).hasSize(databaseSizeBeforeCreate + 1);
        TaskBidding testTaskBidding = taskBiddingList.get(taskBiddingList.size() - 1);
        assertThat(testTaskBidding.getBiddingScope()).isEqualTo(DEFAULT_BIDDING_SCOPE);
        assertThat(testTaskBidding.getBiddingStatus()).isEqualTo(DEFAULT_BIDDING_STATUS);

        // Validate the TaskBidding in Elasticsearch
        TaskBidding taskBiddingEs = taskBiddingSearchRepository.findOne(testTaskBidding.getId());
        assertThat(taskBiddingEs).isEqualToIgnoringGivenFields(testTaskBidding);
    }

    @Test
    @Transactional
    public void createTaskBiddingWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = taskBiddingRepository.findAll().size();

        // Create the TaskBidding with an existing ID
        taskBidding.setId(1L);
        TaskBiddingDTO taskBiddingDTO = taskBiddingMapper.toDto(taskBidding);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTaskBiddingMockMvc.perform(post("/api/task-biddings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(taskBiddingDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TaskBidding in the database
        List<TaskBidding> taskBiddingList = taskBiddingRepository.findAll();
        assertThat(taskBiddingList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkBiddingScopeIsRequired() throws Exception {
        int databaseSizeBeforeTest = taskBiddingRepository.findAll().size();
        // set the field null
        taskBidding.setBiddingScope(null);

        // Create the TaskBidding, which fails.
        TaskBiddingDTO taskBiddingDTO = taskBiddingMapper.toDto(taskBidding);

        restTaskBiddingMockMvc.perform(post("/api/task-biddings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(taskBiddingDTO)))
            .andExpect(status().isBadRequest());

        List<TaskBidding> taskBiddingList = taskBiddingRepository.findAll();
        assertThat(taskBiddingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkBiddingStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = taskBiddingRepository.findAll().size();
        // set the field null
        taskBidding.setBiddingStatus(null);

        // Create the TaskBidding, which fails.
        TaskBiddingDTO taskBiddingDTO = taskBiddingMapper.toDto(taskBidding);

        restTaskBiddingMockMvc.perform(post("/api/task-biddings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(taskBiddingDTO)))
            .andExpect(status().isBadRequest());

        List<TaskBidding> taskBiddingList = taskBiddingRepository.findAll();
        assertThat(taskBiddingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTaskBiddings() throws Exception {
        // Initialize the database
        taskBiddingRepository.saveAndFlush(taskBidding);

        // Get all the taskBiddingList
        restTaskBiddingMockMvc.perform(get("/api/task-biddings?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(taskBidding.getId().intValue())))
            .andExpect(jsonPath("$.[*].biddingScope").value(hasItem(DEFAULT_BIDDING_SCOPE.toString())))
            .andExpect(jsonPath("$.[*].biddingStatus").value(hasItem(DEFAULT_BIDDING_STATUS.toString())));
    }

    @Test
    @Transactional
    public void getTaskBidding() throws Exception {
        // Initialize the database
        taskBiddingRepository.saveAndFlush(taskBidding);

        // Get the taskBidding
        restTaskBiddingMockMvc.perform(get("/api/task-biddings/{id}", taskBidding.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(taskBidding.getId().intValue()))
            .andExpect(jsonPath("$.biddingScope").value(DEFAULT_BIDDING_SCOPE.toString()))
            .andExpect(jsonPath("$.biddingStatus").value(DEFAULT_BIDDING_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getAllTaskBiddingsByBiddingScopeIsEqualToSomething() throws Exception {
        // Initialize the database
        taskBiddingRepository.saveAndFlush(taskBidding);

        // Get all the taskBiddingList where biddingScope equals to DEFAULT_BIDDING_SCOPE
        defaultTaskBiddingShouldBeFound("biddingScope.equals=" + DEFAULT_BIDDING_SCOPE);

        // Get all the taskBiddingList where biddingScope equals to UPDATED_BIDDING_SCOPE
        defaultTaskBiddingShouldNotBeFound("biddingScope.equals=" + UPDATED_BIDDING_SCOPE);
    }

    @Test
    @Transactional
    public void getAllTaskBiddingsByBiddingScopeIsInShouldWork() throws Exception {
        // Initialize the database
        taskBiddingRepository.saveAndFlush(taskBidding);

        // Get all the taskBiddingList where biddingScope in DEFAULT_BIDDING_SCOPE or UPDATED_BIDDING_SCOPE
        defaultTaskBiddingShouldBeFound("biddingScope.in=" + DEFAULT_BIDDING_SCOPE + "," + UPDATED_BIDDING_SCOPE);

        // Get all the taskBiddingList where biddingScope equals to UPDATED_BIDDING_SCOPE
        defaultTaskBiddingShouldNotBeFound("biddingScope.in=" + UPDATED_BIDDING_SCOPE);
    }

    @Test
    @Transactional
    public void getAllTaskBiddingsByBiddingScopeIsNullOrNotNull() throws Exception {
        // Initialize the database
        taskBiddingRepository.saveAndFlush(taskBidding);

        // Get all the taskBiddingList where biddingScope is not null
        defaultTaskBiddingShouldBeFound("biddingScope.specified=true");

        // Get all the taskBiddingList where biddingScope is null
        defaultTaskBiddingShouldNotBeFound("biddingScope.specified=false");
    }

    @Test
    @Transactional
    public void getAllTaskBiddingsByBiddingStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        taskBiddingRepository.saveAndFlush(taskBidding);

        // Get all the taskBiddingList where biddingStatus equals to DEFAULT_BIDDING_STATUS
        defaultTaskBiddingShouldBeFound("biddingStatus.equals=" + DEFAULT_BIDDING_STATUS);

        // Get all the taskBiddingList where biddingStatus equals to UPDATED_BIDDING_STATUS
        defaultTaskBiddingShouldNotBeFound("biddingStatus.equals=" + UPDATED_BIDDING_STATUS);
    }

    @Test
    @Transactional
    public void getAllTaskBiddingsByBiddingStatusIsInShouldWork() throws Exception {
        // Initialize the database
        taskBiddingRepository.saveAndFlush(taskBidding);

        // Get all the taskBiddingList where biddingStatus in DEFAULT_BIDDING_STATUS or UPDATED_BIDDING_STATUS
        defaultTaskBiddingShouldBeFound("biddingStatus.in=" + DEFAULT_BIDDING_STATUS + "," + UPDATED_BIDDING_STATUS);

        // Get all the taskBiddingList where biddingStatus equals to UPDATED_BIDDING_STATUS
        defaultTaskBiddingShouldNotBeFound("biddingStatus.in=" + UPDATED_BIDDING_STATUS);
    }

    @Test
    @Transactional
    public void getAllTaskBiddingsByBiddingStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        taskBiddingRepository.saveAndFlush(taskBidding);

        // Get all the taskBiddingList where biddingStatus is not null
        defaultTaskBiddingShouldBeFound("biddingStatus.specified=true");

        // Get all the taskBiddingList where biddingStatus is null
        defaultTaskBiddingShouldNotBeFound("biddingStatus.specified=false");
    }

    @Test
    @Transactional
    public void getAllTaskBiddingsByTaskIsEqualToSomething() throws Exception {
        // Initialize the database
        Tasks task = TasksResourceIntTest.createEntity(em);
        em.persist(task);
        em.flush();
        taskBidding.setTask(task);
        taskBiddingRepository.saveAndFlush(taskBidding);
        Long taskId = task.getId();

        // Get all the taskBiddingList where task equals to taskId
        defaultTaskBiddingShouldBeFound("taskId.equals=" + taskId);

        // Get all the taskBiddingList where task equals to taskId + 1
        defaultTaskBiddingShouldNotBeFound("taskId.equals=" + (taskId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultTaskBiddingShouldBeFound(String filter) throws Exception {
        restTaskBiddingMockMvc.perform(get("/api/task-biddings?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(taskBidding.getId().intValue())))
            .andExpect(jsonPath("$.[*].biddingScope").value(hasItem(DEFAULT_BIDDING_SCOPE.toString())))
            .andExpect(jsonPath("$.[*].biddingStatus").value(hasItem(DEFAULT_BIDDING_STATUS.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultTaskBiddingShouldNotBeFound(String filter) throws Exception {
        restTaskBiddingMockMvc.perform(get("/api/task-biddings?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingTaskBidding() throws Exception {
        // Get the taskBidding
        restTaskBiddingMockMvc.perform(get("/api/task-biddings/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTaskBidding() throws Exception {
        // Initialize the database
        taskBiddingRepository.saveAndFlush(taskBidding);
        taskBiddingSearchRepository.save(taskBidding);
        int databaseSizeBeforeUpdate = taskBiddingRepository.findAll().size();

        // Update the taskBidding
        TaskBidding updatedTaskBidding = taskBiddingRepository.findOne(taskBidding.getId());
        // Disconnect from session so that the updates on updatedTaskBidding are not directly saved in db
        em.detach(updatedTaskBidding);
        updatedTaskBidding
            .biddingScope(UPDATED_BIDDING_SCOPE)
            .biddingStatus(UPDATED_BIDDING_STATUS);
        TaskBiddingDTO taskBiddingDTO = taskBiddingMapper.toDto(updatedTaskBidding);

        restTaskBiddingMockMvc.perform(put("/api/task-biddings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(taskBiddingDTO)))
            .andExpect(status().isOk());

        // Validate the TaskBidding in the database
        List<TaskBidding> taskBiddingList = taskBiddingRepository.findAll();
        assertThat(taskBiddingList).hasSize(databaseSizeBeforeUpdate);
        TaskBidding testTaskBidding = taskBiddingList.get(taskBiddingList.size() - 1);
        assertThat(testTaskBidding.getBiddingScope()).isEqualTo(UPDATED_BIDDING_SCOPE);
        assertThat(testTaskBidding.getBiddingStatus()).isEqualTo(UPDATED_BIDDING_STATUS);

        // Validate the TaskBidding in Elasticsearch
        TaskBidding taskBiddingEs = taskBiddingSearchRepository.findOne(testTaskBidding.getId());
        assertThat(taskBiddingEs).isEqualToIgnoringGivenFields(testTaskBidding);
    }

    @Test
    @Transactional
    public void updateNonExistingTaskBidding() throws Exception {
        int databaseSizeBeforeUpdate = taskBiddingRepository.findAll().size();

        // Create the TaskBidding
        TaskBiddingDTO taskBiddingDTO = taskBiddingMapper.toDto(taskBidding);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTaskBiddingMockMvc.perform(put("/api/task-biddings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(taskBiddingDTO)))
            .andExpect(status().isCreated());

        // Validate the TaskBidding in the database
        List<TaskBidding> taskBiddingList = taskBiddingRepository.findAll();
        assertThat(taskBiddingList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTaskBidding() throws Exception {
        // Initialize the database
        taskBiddingRepository.saveAndFlush(taskBidding);
        taskBiddingSearchRepository.save(taskBidding);
        int databaseSizeBeforeDelete = taskBiddingRepository.findAll().size();

        // Get the taskBidding
        restTaskBiddingMockMvc.perform(delete("/api/task-biddings/{id}", taskBidding.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean taskBiddingExistsInEs = taskBiddingSearchRepository.exists(taskBidding.getId());
        assertThat(taskBiddingExistsInEs).isFalse();

        // Validate the database is empty
        List<TaskBidding> taskBiddingList = taskBiddingRepository.findAll();
        assertThat(taskBiddingList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchTaskBidding() throws Exception {
        // Initialize the database
        taskBiddingRepository.saveAndFlush(taskBidding);
        taskBiddingSearchRepository.save(taskBidding);

        // Search the taskBidding
        restTaskBiddingMockMvc.perform(get("/api/_search/task-biddings?query=id:" + taskBidding.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(taskBidding.getId().intValue())))
            .andExpect(jsonPath("$.[*].biddingScope").value(hasItem(DEFAULT_BIDDING_SCOPE.toString())))
            .andExpect(jsonPath("$.[*].biddingStatus").value(hasItem(DEFAULT_BIDDING_STATUS.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TaskBidding.class);
        TaskBidding taskBidding1 = new TaskBidding();
        taskBidding1.setId(1L);
        TaskBidding taskBidding2 = new TaskBidding();
        taskBidding2.setId(taskBidding1.getId());
        assertThat(taskBidding1).isEqualTo(taskBidding2);
        taskBidding2.setId(2L);
        assertThat(taskBidding1).isNotEqualTo(taskBidding2);
        taskBidding1.setId(null);
        assertThat(taskBidding1).isNotEqualTo(taskBidding2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TaskBiddingDTO.class);
        TaskBiddingDTO taskBiddingDTO1 = new TaskBiddingDTO();
        taskBiddingDTO1.setId(1L);
        TaskBiddingDTO taskBiddingDTO2 = new TaskBiddingDTO();
        assertThat(taskBiddingDTO1).isNotEqualTo(taskBiddingDTO2);
        taskBiddingDTO2.setId(taskBiddingDTO1.getId());
        assertThat(taskBiddingDTO1).isEqualTo(taskBiddingDTO2);
        taskBiddingDTO2.setId(2L);
        assertThat(taskBiddingDTO1).isNotEqualTo(taskBiddingDTO2);
        taskBiddingDTO1.setId(null);
        assertThat(taskBiddingDTO1).isNotEqualTo(taskBiddingDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(taskBiddingMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(taskBiddingMapper.fromId(null)).isNull();
    }
}
