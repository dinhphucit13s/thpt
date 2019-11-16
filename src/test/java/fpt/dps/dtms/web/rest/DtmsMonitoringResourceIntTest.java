package fpt.dps.dtms.web.rest;

import fpt.dps.dtms.DtmsApp;

import fpt.dps.dtms.domain.DtmsMonitoring;
import fpt.dps.dtms.repository.DtmsMonitoringRepository;
import fpt.dps.dtms.service.DtmsMonitoringService;
import fpt.dps.dtms.repository.search.DtmsMonitoringSearchRepository;
import fpt.dps.dtms.service.dto.DtmsMonitoringDTO;
import fpt.dps.dtms.service.mapper.DtmsMonitoringMapper;
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
import java.util.List;

import static fpt.dps.dtms.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fpt.dps.dtms.domain.enumeration.PositionMonitoring;
import fpt.dps.dtms.domain.enumeration.MONITORINGROLE;
/**
 * Test class for the DtmsMonitoringResource REST controller.
 *
 * @see DtmsMonitoringResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DtmsApp.class)
public class DtmsMonitoringResourceIntTest {

    private static final PositionMonitoring DEFAULT_POSITION = PositionMonitoring.PROJECT;
    private static final PositionMonitoring UPDATED_POSITION = PositionMonitoring.PURCHASE_ORDER;

    private static final Long DEFAULT_POSITION_ID = 1L;
    private static final Long UPDATED_POSITION_ID = 2L;

    private static final MONITORINGROLE DEFAULT_ROLE = MONITORINGROLE.ROLE_DEDICATED;
    private static final MONITORINGROLE UPDATED_ROLE = MONITORINGROLE.ROLE_WATCHER;

    private static final String DEFAULT_MEMBERS = "AAAAAAAAAA";
    private static final String UPDATED_MEMBERS = "BBBBBBBBBB";

    @Autowired
    private DtmsMonitoringRepository dtmsMonitoringRepository;

    @Autowired
    private DtmsMonitoringMapper dtmsMonitoringMapper;

    @Autowired
    private DtmsMonitoringService dtmsMonitoringService;

    @Autowired
    private DtmsMonitoringSearchRepository dtmsMonitoringSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restDtmsMonitoringMockMvc;

    private DtmsMonitoring dtmsMonitoring;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DtmsMonitoringResource dtmsMonitoringResource = new DtmsMonitoringResource(dtmsMonitoringService);
        this.restDtmsMonitoringMockMvc = MockMvcBuilders.standaloneSetup(dtmsMonitoringResource)
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
    public static DtmsMonitoring createEntity(EntityManager em) {
        DtmsMonitoring dtmsMonitoring = new DtmsMonitoring()
            .position(DEFAULT_POSITION)
            .positionId(DEFAULT_POSITION_ID)
            .role(DEFAULT_ROLE)
            .members(DEFAULT_MEMBERS);
        return dtmsMonitoring;
    }

    @Before
    public void initTest() {
        dtmsMonitoringSearchRepository.deleteAll();
        dtmsMonitoring = createEntity(em);
    }

    @Test
    @Transactional
    public void createDtmsMonitoring() throws Exception {
        int databaseSizeBeforeCreate = dtmsMonitoringRepository.findAll().size();

        // Create the DtmsMonitoring
        DtmsMonitoringDTO dtmsMonitoringDTO = dtmsMonitoringMapper.toDto(dtmsMonitoring);
        restDtmsMonitoringMockMvc.perform(post("/api/dtms-monitorings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dtmsMonitoringDTO)))
            .andExpect(status().isCreated());

        // Validate the DtmsMonitoring in the database
        List<DtmsMonitoring> dtmsMonitoringList = dtmsMonitoringRepository.findAll();
        assertThat(dtmsMonitoringList).hasSize(databaseSizeBeforeCreate + 1);
        DtmsMonitoring testDtmsMonitoring = dtmsMonitoringList.get(dtmsMonitoringList.size() - 1);
        assertThat(testDtmsMonitoring.getPosition()).isEqualTo(DEFAULT_POSITION);
        assertThat(testDtmsMonitoring.getPositionId()).isEqualTo(DEFAULT_POSITION_ID);
        assertThat(testDtmsMonitoring.getRole()).isEqualTo(DEFAULT_ROLE);
        assertThat(testDtmsMonitoring.getMembers()).isEqualTo(DEFAULT_MEMBERS);

        // Validate the DtmsMonitoring in Elasticsearch
        DtmsMonitoring dtmsMonitoringEs = dtmsMonitoringSearchRepository.findOne(testDtmsMonitoring.getId());
        assertThat(dtmsMonitoringEs).isEqualToIgnoringGivenFields(testDtmsMonitoring);
    }

    @Test
    @Transactional
    public void createDtmsMonitoringWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = dtmsMonitoringRepository.findAll().size();

        // Create the DtmsMonitoring with an existing ID
        dtmsMonitoring.setId(1L);
        DtmsMonitoringDTO dtmsMonitoringDTO = dtmsMonitoringMapper.toDto(dtmsMonitoring);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDtmsMonitoringMockMvc.perform(post("/api/dtms-monitorings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dtmsMonitoringDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DtmsMonitoring in the database
        List<DtmsMonitoring> dtmsMonitoringList = dtmsMonitoringRepository.findAll();
        assertThat(dtmsMonitoringList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkPositionIsRequired() throws Exception {
        int databaseSizeBeforeTest = dtmsMonitoringRepository.findAll().size();
        // set the field null
        dtmsMonitoring.setPosition(null);

        // Create the DtmsMonitoring, which fails.
        DtmsMonitoringDTO dtmsMonitoringDTO = dtmsMonitoringMapper.toDto(dtmsMonitoring);

        restDtmsMonitoringMockMvc.perform(post("/api/dtms-monitorings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dtmsMonitoringDTO)))
            .andExpect(status().isBadRequest());

        List<DtmsMonitoring> dtmsMonitoringList = dtmsMonitoringRepository.findAll();
        assertThat(dtmsMonitoringList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPositionIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = dtmsMonitoringRepository.findAll().size();
        // set the field null
        dtmsMonitoring.setPositionId(null);

        // Create the DtmsMonitoring, which fails.
        DtmsMonitoringDTO dtmsMonitoringDTO = dtmsMonitoringMapper.toDto(dtmsMonitoring);

        restDtmsMonitoringMockMvc.perform(post("/api/dtms-monitorings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dtmsMonitoringDTO)))
            .andExpect(status().isBadRequest());

        List<DtmsMonitoring> dtmsMonitoringList = dtmsMonitoringRepository.findAll();
        assertThat(dtmsMonitoringList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkRoleIsRequired() throws Exception {
        int databaseSizeBeforeTest = dtmsMonitoringRepository.findAll().size();
        // set the field null
        dtmsMonitoring.setRole(null);

        // Create the DtmsMonitoring, which fails.
        DtmsMonitoringDTO dtmsMonitoringDTO = dtmsMonitoringMapper.toDto(dtmsMonitoring);

        restDtmsMonitoringMockMvc.perform(post("/api/dtms-monitorings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dtmsMonitoringDTO)))
            .andExpect(status().isBadRequest());

        List<DtmsMonitoring> dtmsMonitoringList = dtmsMonitoringRepository.findAll();
        assertThat(dtmsMonitoringList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMembersIsRequired() throws Exception {
        int databaseSizeBeforeTest = dtmsMonitoringRepository.findAll().size();
        // set the field null
        dtmsMonitoring.setMembers(null);

        // Create the DtmsMonitoring, which fails.
        DtmsMonitoringDTO dtmsMonitoringDTO = dtmsMonitoringMapper.toDto(dtmsMonitoring);

        restDtmsMonitoringMockMvc.perform(post("/api/dtms-monitorings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dtmsMonitoringDTO)))
            .andExpect(status().isBadRequest());

        List<DtmsMonitoring> dtmsMonitoringList = dtmsMonitoringRepository.findAll();
        assertThat(dtmsMonitoringList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDtmsMonitorings() throws Exception {
        // Initialize the database
        dtmsMonitoringRepository.saveAndFlush(dtmsMonitoring);

        // Get all the dtmsMonitoringList
        restDtmsMonitoringMockMvc.perform(get("/api/dtms-monitorings?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dtmsMonitoring.getId().intValue())))
            .andExpect(jsonPath("$.[*].position").value(hasItem(DEFAULT_POSITION.toString())))
            .andExpect(jsonPath("$.[*].positionId").value(hasItem(DEFAULT_POSITION_ID.intValue())))
            .andExpect(jsonPath("$.[*].role").value(hasItem(DEFAULT_ROLE.toString())))
            .andExpect(jsonPath("$.[*].members").value(hasItem(DEFAULT_MEMBERS.toString())));
    }

    @Test
    @Transactional
    public void getDtmsMonitoring() throws Exception {
        // Initialize the database
        dtmsMonitoringRepository.saveAndFlush(dtmsMonitoring);

        // Get the dtmsMonitoring
        restDtmsMonitoringMockMvc.perform(get("/api/dtms-monitorings/{id}", dtmsMonitoring.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(dtmsMonitoring.getId().intValue()))
            .andExpect(jsonPath("$.position").value(DEFAULT_POSITION.toString()))
            .andExpect(jsonPath("$.positionId").value(DEFAULT_POSITION_ID.intValue()))
            .andExpect(jsonPath("$.role").value(DEFAULT_ROLE.toString()))
            .andExpect(jsonPath("$.members").value(DEFAULT_MEMBERS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDtmsMonitoring() throws Exception {
        // Get the dtmsMonitoring
        restDtmsMonitoringMockMvc.perform(get("/api/dtms-monitorings/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDtmsMonitoring() throws Exception {
        // Initialize the database
        dtmsMonitoringRepository.saveAndFlush(dtmsMonitoring);
        dtmsMonitoringSearchRepository.save(dtmsMonitoring);
        int databaseSizeBeforeUpdate = dtmsMonitoringRepository.findAll().size();

        // Update the dtmsMonitoring
        DtmsMonitoring updatedDtmsMonitoring = dtmsMonitoringRepository.findOne(dtmsMonitoring.getId());
        // Disconnect from session so that the updates on updatedDtmsMonitoring are not directly saved in db
        em.detach(updatedDtmsMonitoring);
        updatedDtmsMonitoring
            .position(UPDATED_POSITION)
            .positionId(UPDATED_POSITION_ID)
            .role(UPDATED_ROLE)
            .members(UPDATED_MEMBERS);
        DtmsMonitoringDTO dtmsMonitoringDTO = dtmsMonitoringMapper.toDto(updatedDtmsMonitoring);

        restDtmsMonitoringMockMvc.perform(put("/api/dtms-monitorings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dtmsMonitoringDTO)))
            .andExpect(status().isOk());

        // Validate the DtmsMonitoring in the database
        List<DtmsMonitoring> dtmsMonitoringList = dtmsMonitoringRepository.findAll();
        assertThat(dtmsMonitoringList).hasSize(databaseSizeBeforeUpdate);
        DtmsMonitoring testDtmsMonitoring = dtmsMonitoringList.get(dtmsMonitoringList.size() - 1);
        assertThat(testDtmsMonitoring.getPosition()).isEqualTo(UPDATED_POSITION);
        assertThat(testDtmsMonitoring.getPositionId()).isEqualTo(UPDATED_POSITION_ID);
        assertThat(testDtmsMonitoring.getRole()).isEqualTo(UPDATED_ROLE);
        assertThat(testDtmsMonitoring.getMembers()).isEqualTo(UPDATED_MEMBERS);

        // Validate the DtmsMonitoring in Elasticsearch
        DtmsMonitoring dtmsMonitoringEs = dtmsMonitoringSearchRepository.findOne(testDtmsMonitoring.getId());
        assertThat(dtmsMonitoringEs).isEqualToIgnoringGivenFields(testDtmsMonitoring);
    }

    @Test
    @Transactional
    public void updateNonExistingDtmsMonitoring() throws Exception {
        int databaseSizeBeforeUpdate = dtmsMonitoringRepository.findAll().size();

        // Create the DtmsMonitoring
        DtmsMonitoringDTO dtmsMonitoringDTO = dtmsMonitoringMapper.toDto(dtmsMonitoring);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restDtmsMonitoringMockMvc.perform(put("/api/dtms-monitorings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dtmsMonitoringDTO)))
            .andExpect(status().isCreated());

        // Validate the DtmsMonitoring in the database
        List<DtmsMonitoring> dtmsMonitoringList = dtmsMonitoringRepository.findAll();
        assertThat(dtmsMonitoringList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteDtmsMonitoring() throws Exception {
        // Initialize the database
        dtmsMonitoringRepository.saveAndFlush(dtmsMonitoring);
        dtmsMonitoringSearchRepository.save(dtmsMonitoring);
        int databaseSizeBeforeDelete = dtmsMonitoringRepository.findAll().size();

        // Get the dtmsMonitoring
        restDtmsMonitoringMockMvc.perform(delete("/api/dtms-monitorings/{id}", dtmsMonitoring.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean dtmsMonitoringExistsInEs = dtmsMonitoringSearchRepository.exists(dtmsMonitoring.getId());
        assertThat(dtmsMonitoringExistsInEs).isFalse();

        // Validate the database is empty
        List<DtmsMonitoring> dtmsMonitoringList = dtmsMonitoringRepository.findAll();
        assertThat(dtmsMonitoringList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchDtmsMonitoring() throws Exception {
        // Initialize the database
        dtmsMonitoringRepository.saveAndFlush(dtmsMonitoring);
        dtmsMonitoringSearchRepository.save(dtmsMonitoring);

        // Search the dtmsMonitoring
        restDtmsMonitoringMockMvc.perform(get("/api/_search/dtms-monitorings?query=id:" + dtmsMonitoring.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dtmsMonitoring.getId().intValue())))
            .andExpect(jsonPath("$.[*].position").value(hasItem(DEFAULT_POSITION.toString())))
            .andExpect(jsonPath("$.[*].positionId").value(hasItem(DEFAULT_POSITION_ID.intValue())))
            .andExpect(jsonPath("$.[*].role").value(hasItem(DEFAULT_ROLE.toString())))
            .andExpect(jsonPath("$.[*].members").value(hasItem(DEFAULT_MEMBERS.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DtmsMonitoring.class);
        DtmsMonitoring dtmsMonitoring1 = new DtmsMonitoring();
        dtmsMonitoring1.setId(1L);
        DtmsMonitoring dtmsMonitoring2 = new DtmsMonitoring();
        dtmsMonitoring2.setId(dtmsMonitoring1.getId());
        assertThat(dtmsMonitoring1).isEqualTo(dtmsMonitoring2);
        dtmsMonitoring2.setId(2L);
        assertThat(dtmsMonitoring1).isNotEqualTo(dtmsMonitoring2);
        dtmsMonitoring1.setId(null);
        assertThat(dtmsMonitoring1).isNotEqualTo(dtmsMonitoring2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DtmsMonitoringDTO.class);
        DtmsMonitoringDTO dtmsMonitoringDTO1 = new DtmsMonitoringDTO();
        dtmsMonitoringDTO1.setId(1L);
        DtmsMonitoringDTO dtmsMonitoringDTO2 = new DtmsMonitoringDTO();
        assertThat(dtmsMonitoringDTO1).isNotEqualTo(dtmsMonitoringDTO2);
        dtmsMonitoringDTO2.setId(dtmsMonitoringDTO1.getId());
        assertThat(dtmsMonitoringDTO1).isEqualTo(dtmsMonitoringDTO2);
        dtmsMonitoringDTO2.setId(2L);
        assertThat(dtmsMonitoringDTO1).isNotEqualTo(dtmsMonitoringDTO2);
        dtmsMonitoringDTO1.setId(null);
        assertThat(dtmsMonitoringDTO1).isNotEqualTo(dtmsMonitoringDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(dtmsMonitoringMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(dtmsMonitoringMapper.fromId(null)).isNull();
    }
}
