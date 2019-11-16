package fpt.dps.dtms.web.rest;

import fpt.dps.dtms.DtmsApp;

import fpt.dps.dtms.domain.TMSCustomFieldScreenValue;
import fpt.dps.dtms.domain.PurchaseOrders;
import fpt.dps.dtms.domain.Packages;
import fpt.dps.dtms.domain.Tasks;
import fpt.dps.dtms.domain.TMSCustomField;
import fpt.dps.dtms.domain.TMSCustomFieldScreen;
import fpt.dps.dtms.repository.TMSCustomFieldScreenValueRepository;
import fpt.dps.dtms.service.TMSCustomFieldScreenValueService;
import fpt.dps.dtms.repository.search.TMSCustomFieldScreenValueSearchRepository;
import fpt.dps.dtms.service.dto.TMSCustomFieldScreenValueDTO;
import fpt.dps.dtms.service.mapper.TMSCustomFieldScreenValueMapper;
import fpt.dps.dtms.web.rest.errors.ExceptionTranslator;
import fpt.dps.dtms.service.dto.TMSCustomFieldScreenValueCriteria;
import fpt.dps.dtms.service.TMSCustomFieldScreenValueQueryService;

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
 * Test class for the TMSCustomFieldScreenValueResource REST controller.
 *
 * @see TMSCustomFieldScreenValueResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DtmsApp.class)
public class TMSCustomFieldScreenValueResourceIntTest {

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    private static final String DEFAULT_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_TEXT = "BBBBBBBBBB";

    @Autowired
    private TMSCustomFieldScreenValueRepository tMSCustomFieldScreenValueRepository;

    @Autowired
    private TMSCustomFieldScreenValueMapper tMSCustomFieldScreenValueMapper;

    @Autowired
    private TMSCustomFieldScreenValueService tMSCustomFieldScreenValueService;

    @Autowired
    private TMSCustomFieldScreenValueSearchRepository tMSCustomFieldScreenValueSearchRepository;

    @Autowired
    private TMSCustomFieldScreenValueQueryService tMSCustomFieldScreenValueQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTMSCustomFieldScreenValueMockMvc;

    private TMSCustomFieldScreenValue tMSCustomFieldScreenValue;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TMSCustomFieldScreenValueResource tMSCustomFieldScreenValueResource = new TMSCustomFieldScreenValueResource(tMSCustomFieldScreenValueService, tMSCustomFieldScreenValueQueryService);
        this.restTMSCustomFieldScreenValueMockMvc = MockMvcBuilders.standaloneSetup(tMSCustomFieldScreenValueResource)
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
    public static TMSCustomFieldScreenValue createEntity(EntityManager em) {
        TMSCustomFieldScreenValue tMSCustomFieldScreenValue = new TMSCustomFieldScreenValue()
            .value(DEFAULT_VALUE)
            .text(DEFAULT_TEXT);
        return tMSCustomFieldScreenValue;
    }

    @Before
    public void initTest() {
        tMSCustomFieldScreenValueSearchRepository.deleteAll();
        tMSCustomFieldScreenValue = createEntity(em);
    }

    @Test
    @Transactional
    public void createTMSCustomFieldScreenValue() throws Exception {
        int databaseSizeBeforeCreate = tMSCustomFieldScreenValueRepository.findAll().size();

        // Create the TMSCustomFieldScreenValue
        TMSCustomFieldScreenValueDTO tMSCustomFieldScreenValueDTO = tMSCustomFieldScreenValueMapper.toDto(tMSCustomFieldScreenValue);
        restTMSCustomFieldScreenValueMockMvc.perform(post("/api/tms-custom-field-screen-values")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tMSCustomFieldScreenValueDTO)))
            .andExpect(status().isCreated());

        // Validate the TMSCustomFieldScreenValue in the database
        List<TMSCustomFieldScreenValue> tMSCustomFieldScreenValueList = tMSCustomFieldScreenValueRepository.findAll();
        assertThat(tMSCustomFieldScreenValueList).hasSize(databaseSizeBeforeCreate + 1);
        TMSCustomFieldScreenValue testTMSCustomFieldScreenValue = tMSCustomFieldScreenValueList.get(tMSCustomFieldScreenValueList.size() - 1);
        assertThat(testTMSCustomFieldScreenValue.getValue()).isEqualTo(DEFAULT_VALUE);
        assertThat(testTMSCustomFieldScreenValue.getText()).isEqualTo(DEFAULT_TEXT);

        // Validate the TMSCustomFieldScreenValue in Elasticsearch
        TMSCustomFieldScreenValue tMSCustomFieldScreenValueEs = tMSCustomFieldScreenValueSearchRepository.findOne(testTMSCustomFieldScreenValue.getId());
        assertThat(tMSCustomFieldScreenValueEs).isEqualToIgnoringGivenFields(testTMSCustomFieldScreenValue);
    }

    @Test
    @Transactional
    public void createTMSCustomFieldScreenValueWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = tMSCustomFieldScreenValueRepository.findAll().size();

        // Create the TMSCustomFieldScreenValue with an existing ID
        tMSCustomFieldScreenValue.setId(1L);
        TMSCustomFieldScreenValueDTO tMSCustomFieldScreenValueDTO = tMSCustomFieldScreenValueMapper.toDto(tMSCustomFieldScreenValue);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTMSCustomFieldScreenValueMockMvc.perform(post("/api/tms-custom-field-screen-values")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tMSCustomFieldScreenValueDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TMSCustomFieldScreenValue in the database
        List<TMSCustomFieldScreenValue> tMSCustomFieldScreenValueList = tMSCustomFieldScreenValueRepository.findAll();
        assertThat(tMSCustomFieldScreenValueList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllTMSCustomFieldScreenValues() throws Exception {
        // Initialize the database
        tMSCustomFieldScreenValueRepository.saveAndFlush(tMSCustomFieldScreenValue);

        // Get all the tMSCustomFieldScreenValueList
        restTMSCustomFieldScreenValueMockMvc.perform(get("/api/tms-custom-field-screen-values?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tMSCustomFieldScreenValue.getId().intValue())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.toString())))
            .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT.toString())));
    }

    @Test
    @Transactional
    public void getTMSCustomFieldScreenValue() throws Exception {
        // Initialize the database
        tMSCustomFieldScreenValueRepository.saveAndFlush(tMSCustomFieldScreenValue);

        // Get the tMSCustomFieldScreenValue
        restTMSCustomFieldScreenValueMockMvc.perform(get("/api/tms-custom-field-screen-values/{id}", tMSCustomFieldScreenValue.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(tMSCustomFieldScreenValue.getId().intValue()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.toString()))
            .andExpect(jsonPath("$.text").value(DEFAULT_TEXT.toString()));
    }

    @Test
    @Transactional
    public void getAllTMSCustomFieldScreenValuesByValueIsEqualToSomething() throws Exception {
        // Initialize the database
        tMSCustomFieldScreenValueRepository.saveAndFlush(tMSCustomFieldScreenValue);

        // Get all the tMSCustomFieldScreenValueList where value equals to DEFAULT_VALUE
        defaultTMSCustomFieldScreenValueShouldBeFound("value.equals=" + DEFAULT_VALUE);

        // Get all the tMSCustomFieldScreenValueList where value equals to UPDATED_VALUE
        defaultTMSCustomFieldScreenValueShouldNotBeFound("value.equals=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void getAllTMSCustomFieldScreenValuesByValueIsInShouldWork() throws Exception {
        // Initialize the database
        tMSCustomFieldScreenValueRepository.saveAndFlush(tMSCustomFieldScreenValue);

        // Get all the tMSCustomFieldScreenValueList where value in DEFAULT_VALUE or UPDATED_VALUE
        defaultTMSCustomFieldScreenValueShouldBeFound("value.in=" + DEFAULT_VALUE + "," + UPDATED_VALUE);

        // Get all the tMSCustomFieldScreenValueList where value equals to UPDATED_VALUE
        defaultTMSCustomFieldScreenValueShouldNotBeFound("value.in=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void getAllTMSCustomFieldScreenValuesByValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        tMSCustomFieldScreenValueRepository.saveAndFlush(tMSCustomFieldScreenValue);

        // Get all the tMSCustomFieldScreenValueList where value is not null
        defaultTMSCustomFieldScreenValueShouldBeFound("value.specified=true");

        // Get all the tMSCustomFieldScreenValueList where value is null
        defaultTMSCustomFieldScreenValueShouldNotBeFound("value.specified=false");
    }

    @Test
    @Transactional
    public void getAllTMSCustomFieldScreenValuesByPurchaseOrdersIsEqualToSomething() throws Exception {
        // Initialize the database
        PurchaseOrders purchaseOrders = PurchaseOrdersResourceIntTest.createEntity(em);
        em.persist(purchaseOrders);
        em.flush();
        tMSCustomFieldScreenValue.setPurchaseOrders(purchaseOrders);
        tMSCustomFieldScreenValueRepository.saveAndFlush(tMSCustomFieldScreenValue);
        Long purchaseOrdersId = purchaseOrders.getId();

        // Get all the tMSCustomFieldScreenValueList where purchaseOrders equals to purchaseOrdersId
        defaultTMSCustomFieldScreenValueShouldBeFound("purchaseOrdersId.equals=" + purchaseOrdersId);

        // Get all the tMSCustomFieldScreenValueList where purchaseOrders equals to purchaseOrdersId + 1
        defaultTMSCustomFieldScreenValueShouldNotBeFound("purchaseOrdersId.equals=" + (purchaseOrdersId + 1));
    }


    @Test
    @Transactional
    public void getAllTMSCustomFieldScreenValuesByPackagesIsEqualToSomething() throws Exception {
        // Initialize the database
        Packages packages = PackagesResourceIntTest.createEntity(em);
        em.persist(packages);
        em.flush();
        tMSCustomFieldScreenValue.setPackages(packages);
        tMSCustomFieldScreenValueRepository.saveAndFlush(tMSCustomFieldScreenValue);
        Long packagesId = packages.getId();

        // Get all the tMSCustomFieldScreenValueList where packages equals to packagesId
        defaultTMSCustomFieldScreenValueShouldBeFound("packagesId.equals=" + packagesId);

        // Get all the tMSCustomFieldScreenValueList where packages equals to packagesId + 1
        defaultTMSCustomFieldScreenValueShouldNotBeFound("packagesId.equals=" + (packagesId + 1));
    }


    @Test
    @Transactional
    public void getAllTMSCustomFieldScreenValuesByTasksIsEqualToSomething() throws Exception {
        // Initialize the database
        Tasks tasks = TasksResourceIntTest.createEntity(em);
        em.persist(tasks);
        em.flush();
        tMSCustomFieldScreenValue.setTasks(tasks);
        tMSCustomFieldScreenValueRepository.saveAndFlush(tMSCustomFieldScreenValue);
        Long tasksId = tasks.getId();

        // Get all the tMSCustomFieldScreenValueList where tasks equals to tasksId
        defaultTMSCustomFieldScreenValueShouldBeFound("tasksId.equals=" + tasksId);

        // Get all the tMSCustomFieldScreenValueList where tasks equals to tasksId + 1
        defaultTMSCustomFieldScreenValueShouldNotBeFound("tasksId.equals=" + (tasksId + 1));
    }


    @Test
    @Transactional
    public void getAllTMSCustomFieldScreenValuesByTmsCustomFieldIsEqualToSomething() throws Exception {
        // Initialize the database
        TMSCustomFieldScreen tmsCustomFieldScreen = TMSCustomFieldScreenResourceIntTest.createEntity(em);
        em.persist(tmsCustomFieldScreen);
        em.flush();
        tMSCustomFieldScreenValue.setTMSCustomFieldScreen(tmsCustomFieldScreen);
        tMSCustomFieldScreenValueRepository.saveAndFlush(tMSCustomFieldScreenValue);
        Long tmsCustomFieldScreenId = tmsCustomFieldScreen.getId();

        // Get all the tMSCustomFieldScreenValueList where tmsCustomField equals to tmsCustomFieldId
        defaultTMSCustomFieldScreenValueShouldBeFound("tmsCustomFieldId.equals=" + tmsCustomFieldScreenId);

        // Get all the tMSCustomFieldScreenValueList where tmsCustomField equals to tmsCustomFieldId + 1
        defaultTMSCustomFieldScreenValueShouldNotBeFound("tmsCustomFieldId.equals=" + (tmsCustomFieldScreenId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultTMSCustomFieldScreenValueShouldBeFound(String filter) throws Exception {
        restTMSCustomFieldScreenValueMockMvc.perform(get("/api/tms-custom-field-screen-values?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tMSCustomFieldScreenValue.getId().intValue())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.toString())))
            .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultTMSCustomFieldScreenValueShouldNotBeFound(String filter) throws Exception {
        restTMSCustomFieldScreenValueMockMvc.perform(get("/api/tms-custom-field-screen-values?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingTMSCustomFieldScreenValue() throws Exception {
        // Get the tMSCustomFieldScreenValue
        restTMSCustomFieldScreenValueMockMvc.perform(get("/api/tms-custom-field-screen-values/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTMSCustomFieldScreenValue() throws Exception {
        // Initialize the database
        tMSCustomFieldScreenValueRepository.saveAndFlush(tMSCustomFieldScreenValue);
        tMSCustomFieldScreenValueSearchRepository.save(tMSCustomFieldScreenValue);
        int databaseSizeBeforeUpdate = tMSCustomFieldScreenValueRepository.findAll().size();

        // Update the tMSCustomFieldScreenValue
        TMSCustomFieldScreenValue updatedTMSCustomFieldScreenValue = tMSCustomFieldScreenValueRepository.findOne(tMSCustomFieldScreenValue.getId());
        // Disconnect from session so that the updates on updatedTMSCustomFieldScreenValue are not directly saved in db
        em.detach(updatedTMSCustomFieldScreenValue);
        updatedTMSCustomFieldScreenValue
            .value(UPDATED_VALUE)
            .text(UPDATED_TEXT);
        TMSCustomFieldScreenValueDTO tMSCustomFieldScreenValueDTO = tMSCustomFieldScreenValueMapper.toDto(updatedTMSCustomFieldScreenValue);

        restTMSCustomFieldScreenValueMockMvc.perform(put("/api/tms-custom-field-screen-values")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tMSCustomFieldScreenValueDTO)))
            .andExpect(status().isOk());

        // Validate the TMSCustomFieldScreenValue in the database
        List<TMSCustomFieldScreenValue> tMSCustomFieldScreenValueList = tMSCustomFieldScreenValueRepository.findAll();
        assertThat(tMSCustomFieldScreenValueList).hasSize(databaseSizeBeforeUpdate);
        TMSCustomFieldScreenValue testTMSCustomFieldScreenValue = tMSCustomFieldScreenValueList.get(tMSCustomFieldScreenValueList.size() - 1);
        assertThat(testTMSCustomFieldScreenValue.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testTMSCustomFieldScreenValue.getText()).isEqualTo(UPDATED_TEXT);

        // Validate the TMSCustomFieldScreenValue in Elasticsearch
        TMSCustomFieldScreenValue tMSCustomFieldScreenValueEs = tMSCustomFieldScreenValueSearchRepository.findOne(testTMSCustomFieldScreenValue.getId());
        assertThat(tMSCustomFieldScreenValueEs).isEqualToIgnoringGivenFields(testTMSCustomFieldScreenValue);
    }

    @Test
    @Transactional
    public void updateNonExistingTMSCustomFieldScreenValue() throws Exception {
        int databaseSizeBeforeUpdate = tMSCustomFieldScreenValueRepository.findAll().size();

        // Create the TMSCustomFieldScreenValue
        TMSCustomFieldScreenValueDTO tMSCustomFieldScreenValueDTO = tMSCustomFieldScreenValueMapper.toDto(tMSCustomFieldScreenValue);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTMSCustomFieldScreenValueMockMvc.perform(put("/api/tms-custom-field-screen-values")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tMSCustomFieldScreenValueDTO)))
            .andExpect(status().isCreated());

        // Validate the TMSCustomFieldScreenValue in the database
        List<TMSCustomFieldScreenValue> tMSCustomFieldScreenValueList = tMSCustomFieldScreenValueRepository.findAll();
        assertThat(tMSCustomFieldScreenValueList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTMSCustomFieldScreenValue() throws Exception {
        // Initialize the database
        tMSCustomFieldScreenValueRepository.saveAndFlush(tMSCustomFieldScreenValue);
        tMSCustomFieldScreenValueSearchRepository.save(tMSCustomFieldScreenValue);
        int databaseSizeBeforeDelete = tMSCustomFieldScreenValueRepository.findAll().size();

        // Get the tMSCustomFieldScreenValue
        restTMSCustomFieldScreenValueMockMvc.perform(delete("/api/tms-custom-field-screen-values/{id}", tMSCustomFieldScreenValue.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean tMSCustomFieldScreenValueExistsInEs = tMSCustomFieldScreenValueSearchRepository.exists(tMSCustomFieldScreenValue.getId());
        assertThat(tMSCustomFieldScreenValueExistsInEs).isFalse();

        // Validate the database is empty
        List<TMSCustomFieldScreenValue> tMSCustomFieldScreenValueList = tMSCustomFieldScreenValueRepository.findAll();
        assertThat(tMSCustomFieldScreenValueList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchTMSCustomFieldScreenValue() throws Exception {
        // Initialize the database
        tMSCustomFieldScreenValueRepository.saveAndFlush(tMSCustomFieldScreenValue);
        tMSCustomFieldScreenValueSearchRepository.save(tMSCustomFieldScreenValue);

        // Search the tMSCustomFieldScreenValue
        restTMSCustomFieldScreenValueMockMvc.perform(get("/api/_search/tms-custom-field-screen-values?query=id:" + tMSCustomFieldScreenValue.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tMSCustomFieldScreenValue.getId().intValue())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.toString())))
            .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TMSCustomFieldScreenValue.class);
        TMSCustomFieldScreenValue tMSCustomFieldScreenValue1 = new TMSCustomFieldScreenValue();
        tMSCustomFieldScreenValue1.setId(1L);
        TMSCustomFieldScreenValue tMSCustomFieldScreenValue2 = new TMSCustomFieldScreenValue();
        tMSCustomFieldScreenValue2.setId(tMSCustomFieldScreenValue1.getId());
        assertThat(tMSCustomFieldScreenValue1).isEqualTo(tMSCustomFieldScreenValue2);
        tMSCustomFieldScreenValue2.setId(2L);
        assertThat(tMSCustomFieldScreenValue1).isNotEqualTo(tMSCustomFieldScreenValue2);
        tMSCustomFieldScreenValue1.setId(null);
        assertThat(tMSCustomFieldScreenValue1).isNotEqualTo(tMSCustomFieldScreenValue2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TMSCustomFieldScreenValueDTO.class);
        TMSCustomFieldScreenValueDTO tMSCustomFieldScreenValueDTO1 = new TMSCustomFieldScreenValueDTO();
        tMSCustomFieldScreenValueDTO1.setId(1L);
        TMSCustomFieldScreenValueDTO tMSCustomFieldScreenValueDTO2 = new TMSCustomFieldScreenValueDTO();
        assertThat(tMSCustomFieldScreenValueDTO1).isNotEqualTo(tMSCustomFieldScreenValueDTO2);
        tMSCustomFieldScreenValueDTO2.setId(tMSCustomFieldScreenValueDTO1.getId());
        assertThat(tMSCustomFieldScreenValueDTO1).isEqualTo(tMSCustomFieldScreenValueDTO2);
        tMSCustomFieldScreenValueDTO2.setId(2L);
        assertThat(tMSCustomFieldScreenValueDTO1).isNotEqualTo(tMSCustomFieldScreenValueDTO2);
        tMSCustomFieldScreenValueDTO1.setId(null);
        assertThat(tMSCustomFieldScreenValueDTO1).isNotEqualTo(tMSCustomFieldScreenValueDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(tMSCustomFieldScreenValueMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(tMSCustomFieldScreenValueMapper.fromId(null)).isNull();
    }
}
