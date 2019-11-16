package fpt.dps.dtms.web.rest;

import fpt.dps.dtms.DtmsApp;

import fpt.dps.dtms.domain.LoginTracking;
import fpt.dps.dtms.repository.LoginTrackingRepository;
import fpt.dps.dtms.service.LoginTrackingService;
import fpt.dps.dtms.repository.search.LoginTrackingSearchRepository;
import fpt.dps.dtms.service.dto.LoginTrackingDTO;
import fpt.dps.dtms.service.mapper.LoginTrackingMapper;
import fpt.dps.dtms.web.rest.errors.ExceptionTranslator;
import fpt.dps.dtms.service.dto.LoginTrackingCriteria;
import fpt.dps.dtms.service.LoginTrackingQueryService;

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
 * Test class for the LoginTrackingResource REST controller.
 *
 * @see LoginTrackingResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DtmsApp.class)
public class LoginTrackingResourceIntTest {

    private static final String DEFAULT_LOGIN = "AAAAAAAAAA";
    private static final String UPDATED_LOGIN = "BBBBBBBBBB";

    private static final Instant DEFAULT_START_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private LoginTrackingRepository loginTrackingRepository;

    @Autowired
    private LoginTrackingMapper loginTrackingMapper;

    @Autowired
    private LoginTrackingService loginTrackingService;

    @Autowired
    private LoginTrackingSearchRepository loginTrackingSearchRepository;

    @Autowired
    private LoginTrackingQueryService loginTrackingQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restLoginTrackingMockMvc;

    private LoginTracking loginTracking;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final LoginTrackingResource loginTrackingResource = new LoginTrackingResource(loginTrackingService, loginTrackingQueryService);
        this.restLoginTrackingMockMvc = MockMvcBuilders.standaloneSetup(loginTrackingResource)
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
    public static LoginTracking createEntity(EntityManager em) {
        LoginTracking loginTracking = new LoginTracking()
            .login(DEFAULT_LOGIN)
            .startTime(DEFAULT_START_TIME)
            .endTime(DEFAULT_END_TIME);
        return loginTracking;
    }

    @Before
    public void initTest() {
        loginTrackingSearchRepository.deleteAll();
        loginTracking = createEntity(em);
    }

    @Test
    @Transactional
    public void createLoginTracking() throws Exception {
        int databaseSizeBeforeCreate = loginTrackingRepository.findAll().size();

        // Create the LoginTracking
        LoginTrackingDTO loginTrackingDTO = loginTrackingMapper.toDto(loginTracking);
        restLoginTrackingMockMvc.perform(post("/api/login-trackings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(loginTrackingDTO)))
            .andExpect(status().isCreated());

        // Validate the LoginTracking in the database
        List<LoginTracking> loginTrackingList = loginTrackingRepository.findAll();
        assertThat(loginTrackingList).hasSize(databaseSizeBeforeCreate + 1);
        LoginTracking testLoginTracking = loginTrackingList.get(loginTrackingList.size() - 1);
        assertThat(testLoginTracking.getLogin()).isEqualTo(DEFAULT_LOGIN);
        assertThat(testLoginTracking.getStartTime()).isEqualTo(DEFAULT_START_TIME);
        assertThat(testLoginTracking.getEndTime()).isEqualTo(DEFAULT_END_TIME);

        // Validate the LoginTracking in Elasticsearch
        LoginTracking loginTrackingEs = loginTrackingSearchRepository.findOne(testLoginTracking.getId());
        assertThat(loginTrackingEs).isEqualToIgnoringGivenFields(testLoginTracking);
    }

    @Test
    @Transactional
    public void createLoginTrackingWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = loginTrackingRepository.findAll().size();

        // Create the LoginTracking with an existing ID
        loginTracking.setId(1L);
        LoginTrackingDTO loginTrackingDTO = loginTrackingMapper.toDto(loginTracking);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLoginTrackingMockMvc.perform(post("/api/login-trackings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(loginTrackingDTO)))
            .andExpect(status().isBadRequest());

        // Validate the LoginTracking in the database
        List<LoginTracking> loginTrackingList = loginTrackingRepository.findAll();
        assertThat(loginTrackingList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllLoginTrackings() throws Exception {
        // Initialize the database
        loginTrackingRepository.saveAndFlush(loginTracking);

        // Get all the loginTrackingList
        restLoginTrackingMockMvc.perform(get("/api/login-trackings?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(loginTracking.getId().intValue())))
            .andExpect(jsonPath("$.[*].login").value(hasItem(DEFAULT_LOGIN.toString())))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME.toString())))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(DEFAULT_END_TIME.toString())));
    }

    @Test
    @Transactional
    public void getLoginTracking() throws Exception {
        // Initialize the database
        loginTrackingRepository.saveAndFlush(loginTracking);

        // Get the loginTracking
        restLoginTrackingMockMvc.perform(get("/api/login-trackings/{id}", loginTracking.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(loginTracking.getId().intValue()))
            .andExpect(jsonPath("$.login").value(DEFAULT_LOGIN.toString()))
            .andExpect(jsonPath("$.startTime").value(DEFAULT_START_TIME.toString()))
            .andExpect(jsonPath("$.endTime").value(DEFAULT_END_TIME.toString()));
    }

    @Test
    @Transactional
    public void getAllLoginTrackingsByLoginIsEqualToSomething() throws Exception {
        // Initialize the database
        loginTrackingRepository.saveAndFlush(loginTracking);

        // Get all the loginTrackingList where login equals to DEFAULT_LOGIN
        defaultLoginTrackingShouldBeFound("login.equals=" + DEFAULT_LOGIN);

        // Get all the loginTrackingList where login equals to UPDATED_LOGIN
        defaultLoginTrackingShouldNotBeFound("login.equals=" + UPDATED_LOGIN);
    }

    @Test
    @Transactional
    public void getAllLoginTrackingsByLoginIsInShouldWork() throws Exception {
        // Initialize the database
        loginTrackingRepository.saveAndFlush(loginTracking);

        // Get all the loginTrackingList where login in DEFAULT_LOGIN or UPDATED_LOGIN
        defaultLoginTrackingShouldBeFound("login.in=" + DEFAULT_LOGIN + "," + UPDATED_LOGIN);

        // Get all the loginTrackingList where login equals to UPDATED_LOGIN
        defaultLoginTrackingShouldNotBeFound("login.in=" + UPDATED_LOGIN);
    }

    @Test
    @Transactional
    public void getAllLoginTrackingsByLoginIsNullOrNotNull() throws Exception {
        // Initialize the database
        loginTrackingRepository.saveAndFlush(loginTracking);

        // Get all the loginTrackingList where login is not null
        defaultLoginTrackingShouldBeFound("login.specified=true");

        // Get all the loginTrackingList where login is null
        defaultLoginTrackingShouldNotBeFound("login.specified=false");
    }

    @Test
    @Transactional
    public void getAllLoginTrackingsByStartTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        loginTrackingRepository.saveAndFlush(loginTracking);

        // Get all the loginTrackingList where startTime equals to DEFAULT_START_TIME
        defaultLoginTrackingShouldBeFound("startTime.equals=" + DEFAULT_START_TIME);

        // Get all the loginTrackingList where startTime equals to UPDATED_START_TIME
        defaultLoginTrackingShouldNotBeFound("startTime.equals=" + UPDATED_START_TIME);
    }

    @Test
    @Transactional
    public void getAllLoginTrackingsByStartTimeIsInShouldWork() throws Exception {
        // Initialize the database
        loginTrackingRepository.saveAndFlush(loginTracking);

        // Get all the loginTrackingList where startTime in DEFAULT_START_TIME or UPDATED_START_TIME
        defaultLoginTrackingShouldBeFound("startTime.in=" + DEFAULT_START_TIME + "," + UPDATED_START_TIME);

        // Get all the loginTrackingList where startTime equals to UPDATED_START_TIME
        defaultLoginTrackingShouldNotBeFound("startTime.in=" + UPDATED_START_TIME);
    }

    @Test
    @Transactional
    public void getAllLoginTrackingsByStartTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        loginTrackingRepository.saveAndFlush(loginTracking);

        // Get all the loginTrackingList where startTime is not null
        defaultLoginTrackingShouldBeFound("startTime.specified=true");

        // Get all the loginTrackingList where startTime is null
        defaultLoginTrackingShouldNotBeFound("startTime.specified=false");
    }

    @Test
    @Transactional
    public void getAllLoginTrackingsByEndTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        loginTrackingRepository.saveAndFlush(loginTracking);

        // Get all the loginTrackingList where endTime equals to DEFAULT_END_TIME
        defaultLoginTrackingShouldBeFound("endTime.equals=" + DEFAULT_END_TIME);

        // Get all the loginTrackingList where endTime equals to UPDATED_END_TIME
        defaultLoginTrackingShouldNotBeFound("endTime.equals=" + UPDATED_END_TIME);
    }

    @Test
    @Transactional
    public void getAllLoginTrackingsByEndTimeIsInShouldWork() throws Exception {
        // Initialize the database
        loginTrackingRepository.saveAndFlush(loginTracking);

        // Get all the loginTrackingList where endTime in DEFAULT_END_TIME or UPDATED_END_TIME
        defaultLoginTrackingShouldBeFound("endTime.in=" + DEFAULT_END_TIME + "," + UPDATED_END_TIME);

        // Get all the loginTrackingList where endTime equals to UPDATED_END_TIME
        defaultLoginTrackingShouldNotBeFound("endTime.in=" + UPDATED_END_TIME);
    }

    @Test
    @Transactional
    public void getAllLoginTrackingsByEndTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        loginTrackingRepository.saveAndFlush(loginTracking);

        // Get all the loginTrackingList where endTime is not null
        defaultLoginTrackingShouldBeFound("endTime.specified=true");

        // Get all the loginTrackingList where endTime is null
        defaultLoginTrackingShouldNotBeFound("endTime.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultLoginTrackingShouldBeFound(String filter) throws Exception {
        restLoginTrackingMockMvc.perform(get("/api/login-trackings?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(loginTracking.getId().intValue())))
            .andExpect(jsonPath("$.[*].login").value(hasItem(DEFAULT_LOGIN.toString())))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME.toString())))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(DEFAULT_END_TIME.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultLoginTrackingShouldNotBeFound(String filter) throws Exception {
        restLoginTrackingMockMvc.perform(get("/api/login-trackings?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingLoginTracking() throws Exception {
        // Get the loginTracking
        restLoginTrackingMockMvc.perform(get("/api/login-trackings/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLoginTracking() throws Exception {
        // Initialize the database
        loginTrackingRepository.saveAndFlush(loginTracking);
        loginTrackingSearchRepository.save(loginTracking);
        int databaseSizeBeforeUpdate = loginTrackingRepository.findAll().size();

        // Update the loginTracking
        LoginTracking updatedLoginTracking = loginTrackingRepository.findOne(loginTracking.getId());
        // Disconnect from session so that the updates on updatedLoginTracking are not directly saved in db
        em.detach(updatedLoginTracking);
        updatedLoginTracking
            .login(UPDATED_LOGIN)
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME);
        LoginTrackingDTO loginTrackingDTO = loginTrackingMapper.toDto(updatedLoginTracking);

        restLoginTrackingMockMvc.perform(put("/api/login-trackings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(loginTrackingDTO)))
            .andExpect(status().isOk());

        // Validate the LoginTracking in the database
        List<LoginTracking> loginTrackingList = loginTrackingRepository.findAll();
        assertThat(loginTrackingList).hasSize(databaseSizeBeforeUpdate);
        LoginTracking testLoginTracking = loginTrackingList.get(loginTrackingList.size() - 1);
        assertThat(testLoginTracking.getLogin()).isEqualTo(UPDATED_LOGIN);
        assertThat(testLoginTracking.getStartTime()).isEqualTo(UPDATED_START_TIME);
        assertThat(testLoginTracking.getEndTime()).isEqualTo(UPDATED_END_TIME);

        // Validate the LoginTracking in Elasticsearch
        LoginTracking loginTrackingEs = loginTrackingSearchRepository.findOne(testLoginTracking.getId());
        assertThat(loginTrackingEs).isEqualToIgnoringGivenFields(testLoginTracking);
    }

    @Test
    @Transactional
    public void updateNonExistingLoginTracking() throws Exception {
        int databaseSizeBeforeUpdate = loginTrackingRepository.findAll().size();

        // Create the LoginTracking
        LoginTrackingDTO loginTrackingDTO = loginTrackingMapper.toDto(loginTracking);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restLoginTrackingMockMvc.perform(put("/api/login-trackings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(loginTrackingDTO)))
            .andExpect(status().isCreated());

        // Validate the LoginTracking in the database
        List<LoginTracking> loginTrackingList = loginTrackingRepository.findAll();
        assertThat(loginTrackingList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteLoginTracking() throws Exception {
        // Initialize the database
        loginTrackingRepository.saveAndFlush(loginTracking);
        loginTrackingSearchRepository.save(loginTracking);
        int databaseSizeBeforeDelete = loginTrackingRepository.findAll().size();

        // Get the loginTracking
        restLoginTrackingMockMvc.perform(delete("/api/login-trackings/{id}", loginTracking.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean loginTrackingExistsInEs = loginTrackingSearchRepository.exists(loginTracking.getId());
        assertThat(loginTrackingExistsInEs).isFalse();

        // Validate the database is empty
        List<LoginTracking> loginTrackingList = loginTrackingRepository.findAll();
        assertThat(loginTrackingList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchLoginTracking() throws Exception {
        // Initialize the database
        loginTrackingRepository.saveAndFlush(loginTracking);
        loginTrackingSearchRepository.save(loginTracking);

        // Search the loginTracking
        restLoginTrackingMockMvc.perform(get("/api/_search/login-trackings?query=id:" + loginTracking.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(loginTracking.getId().intValue())))
            .andExpect(jsonPath("$.[*].login").value(hasItem(DEFAULT_LOGIN.toString())))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME.toString())))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(DEFAULT_END_TIME.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LoginTracking.class);
        LoginTracking loginTracking1 = new LoginTracking();
        loginTracking1.setId(1L);
        LoginTracking loginTracking2 = new LoginTracking();
        loginTracking2.setId(loginTracking1.getId());
        assertThat(loginTracking1).isEqualTo(loginTracking2);
        loginTracking2.setId(2L);
        assertThat(loginTracking1).isNotEqualTo(loginTracking2);
        loginTracking1.setId(null);
        assertThat(loginTracking1).isNotEqualTo(loginTracking2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LoginTrackingDTO.class);
        LoginTrackingDTO loginTrackingDTO1 = new LoginTrackingDTO();
        loginTrackingDTO1.setId(1L);
        LoginTrackingDTO loginTrackingDTO2 = new LoginTrackingDTO();
        assertThat(loginTrackingDTO1).isNotEqualTo(loginTrackingDTO2);
        loginTrackingDTO2.setId(loginTrackingDTO1.getId());
        assertThat(loginTrackingDTO1).isEqualTo(loginTrackingDTO2);
        loginTrackingDTO2.setId(2L);
        assertThat(loginTrackingDTO1).isNotEqualTo(loginTrackingDTO2);
        loginTrackingDTO1.setId(null);
        assertThat(loginTrackingDTO1).isNotEqualTo(loginTrackingDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(loginTrackingMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(loginTrackingMapper.fromId(null)).isNull();
    }
}
