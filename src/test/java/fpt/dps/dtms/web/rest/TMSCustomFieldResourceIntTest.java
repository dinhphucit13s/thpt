package fpt.dps.dtms.web.rest;

import fpt.dps.dtms.DtmsApp;

import fpt.dps.dtms.domain.TMSCustomField;
import fpt.dps.dtms.repository.TMSCustomFieldRepository;
import fpt.dps.dtms.service.TMSCustomFieldService;
import fpt.dps.dtms.repository.search.TMSCustomFieldSearchRepository;
import fpt.dps.dtms.service.dto.TMSCustomFieldDTO;
import fpt.dps.dtms.service.mapper.TMSCustomFieldMapper;
import fpt.dps.dtms.web.rest.errors.ExceptionTranslator;
import fpt.dps.dtms.service.dto.TMSCustomFieldCriteria;
import fpt.dps.dtms.service.TMSCustomFieldQueryService;

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
 * Test class for the TMSCustomFieldResource REST controller.
 *
 * @see TMSCustomFieldResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DtmsApp.class)
public class TMSCustomFieldResourceIntTest {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private TMSCustomFieldRepository tMSCustomFieldRepository;

    @Autowired
    private TMSCustomFieldMapper tMSCustomFieldMapper;

    @Autowired
    private TMSCustomFieldService tMSCustomFieldService;

    @Autowired
    private TMSCustomFieldSearchRepository tMSCustomFieldSearchRepository;

    @Autowired
    private TMSCustomFieldQueryService tMSCustomFieldQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTMSCustomFieldMockMvc;

    private TMSCustomField tMSCustomField;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TMSCustomFieldResource tMSCustomFieldResource = new TMSCustomFieldResource(tMSCustomFieldService, tMSCustomFieldQueryService);
        this.restTMSCustomFieldMockMvc = MockMvcBuilders.standaloneSetup(tMSCustomFieldResource)
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
    public static TMSCustomField createEntity(EntityManager em) {
        TMSCustomField tMSCustomField = new TMSCustomField()
            .entityData(DEFAULT_DESCRIPTION);
        return tMSCustomField;
    }

    @Before
    public void initTest() {
        tMSCustomFieldSearchRepository.deleteAll();
        tMSCustomField = createEntity(em);
    }

    @Test
    @Transactional
    public void createTMSCustomField() throws Exception {
        int databaseSizeBeforeCreate = tMSCustomFieldRepository.findAll().size();

        // Create the TMSCustomField
        TMSCustomFieldDTO tMSCustomFieldDTO = tMSCustomFieldMapper.toDto(tMSCustomField);
        restTMSCustomFieldMockMvc.perform(post("/api/tms-custom-fields")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tMSCustomFieldDTO)))
            .andExpect(status().isCreated());

        // Validate the TMSCustomField in the database
        List<TMSCustomField> tMSCustomFieldList = tMSCustomFieldRepository.findAll();
        assertThat(tMSCustomFieldList).hasSize(databaseSizeBeforeCreate + 1);
        TMSCustomField testTMSCustomField = tMSCustomFieldList.get(tMSCustomFieldList.size() - 1);
        assertThat(testTMSCustomField.getEntityData()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the TMSCustomField in Elasticsearch
        TMSCustomField tMSCustomFieldEs = tMSCustomFieldSearchRepository.findOne(testTMSCustomField.getId());
        assertThat(tMSCustomFieldEs).isEqualToIgnoringGivenFields(testTMSCustomField);
    }

    @Test
    @Transactional
    public void createTMSCustomFieldWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = tMSCustomFieldRepository.findAll().size();

        // Create the TMSCustomField with an existing ID
        tMSCustomField.setId(1L);
        TMSCustomFieldDTO tMSCustomFieldDTO = tMSCustomFieldMapper.toDto(tMSCustomField);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTMSCustomFieldMockMvc.perform(post("/api/tms-custom-fields")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tMSCustomFieldDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TMSCustomField in the database
        List<TMSCustomField> tMSCustomFieldList = tMSCustomFieldRepository.findAll();
        assertThat(tMSCustomFieldList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllTMSCustomFields() throws Exception {
        // Initialize the database
        tMSCustomFieldRepository.saveAndFlush(tMSCustomField);

        // Get all the tMSCustomFieldList
        restTMSCustomFieldMockMvc.perform(get("/api/tms-custom-fields?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tMSCustomField.getId().intValue())))
            .andExpect(jsonPath("$.[*].entity").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getTMSCustomField() throws Exception {
        // Initialize the database
        tMSCustomFieldRepository.saveAndFlush(tMSCustomField);

        // Get the tMSCustomField
        restTMSCustomFieldMockMvc.perform(get("/api/tms-custom-fields/{id}", tMSCustomField.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(tMSCustomField.getId().intValue()))
            .andExpect(jsonPath("$.entity").value(DEFAULT_DESCRIPTION.toString()));
    }
    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultTMSCustomFieldShouldBeFound(String filter) throws Exception {
        restTMSCustomFieldMockMvc.perform(get("/api/tms-custom-fields?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tMSCustomField.getId().intValue())))
            .andExpect(jsonPath("$.[*].entity").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultTMSCustomFieldShouldNotBeFound(String filter) throws Exception {
        restTMSCustomFieldMockMvc.perform(get("/api/tms-custom-fields?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingTMSCustomField() throws Exception {
        // Get the tMSCustomField
        restTMSCustomFieldMockMvc.perform(get("/api/tms-custom-fields/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTMSCustomField() throws Exception {
        // Initialize the database
        tMSCustomFieldRepository.saveAndFlush(tMSCustomField);
        tMSCustomFieldSearchRepository.save(tMSCustomField);
        int databaseSizeBeforeUpdate = tMSCustomFieldRepository.findAll().size();

        // Update the tMSCustomField
        TMSCustomField updatedTMSCustomField = tMSCustomFieldRepository.findOne(tMSCustomField.getId());
        // Disconnect from session so that the updates on updatedTMSCustomField are not directly saved in db
        em.detach(updatedTMSCustomField);
        updatedTMSCustomField
            .entityData(UPDATED_DESCRIPTION);
        TMSCustomFieldDTO tMSCustomFieldDTO = tMSCustomFieldMapper.toDto(updatedTMSCustomField);

        restTMSCustomFieldMockMvc.perform(put("/api/tms-custom-fields")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tMSCustomFieldDTO)))
            .andExpect(status().isOk());

        // Validate the TMSCustomField in the database
        List<TMSCustomField> tMSCustomFieldList = tMSCustomFieldRepository.findAll();
        assertThat(tMSCustomFieldList).hasSize(databaseSizeBeforeUpdate);
        TMSCustomField testTMSCustomField = tMSCustomFieldList.get(tMSCustomFieldList.size() - 1);
        assertThat(testTMSCustomField.getEntityData()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the TMSCustomField in Elasticsearch
        TMSCustomField tMSCustomFieldEs = tMSCustomFieldSearchRepository.findOne(testTMSCustomField.getId());
        assertThat(tMSCustomFieldEs).isEqualToIgnoringGivenFields(testTMSCustomField);
    }

    @Test
    @Transactional
    public void updateNonExistingTMSCustomField() throws Exception {
        int databaseSizeBeforeUpdate = tMSCustomFieldRepository.findAll().size();

        // Create the TMSCustomField
        TMSCustomFieldDTO tMSCustomFieldDTO = tMSCustomFieldMapper.toDto(tMSCustomField);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTMSCustomFieldMockMvc.perform(put("/api/tms-custom-fields")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tMSCustomFieldDTO)))
            .andExpect(status().isCreated());

        // Validate the TMSCustomField in the database
        List<TMSCustomField> tMSCustomFieldList = tMSCustomFieldRepository.findAll();
        assertThat(tMSCustomFieldList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTMSCustomField() throws Exception {
        // Initialize the database
        tMSCustomFieldRepository.saveAndFlush(tMSCustomField);
        tMSCustomFieldSearchRepository.save(tMSCustomField);
        int databaseSizeBeforeDelete = tMSCustomFieldRepository.findAll().size();

        // Get the tMSCustomField
        restTMSCustomFieldMockMvc.perform(delete("/api/tms-custom-fields/{id}", tMSCustomField.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean tMSCustomFieldExistsInEs = tMSCustomFieldSearchRepository.exists(tMSCustomField.getId());
        assertThat(tMSCustomFieldExistsInEs).isFalse();

        // Validate the database is empty
        List<TMSCustomField> tMSCustomFieldList = tMSCustomFieldRepository.findAll();
        assertThat(tMSCustomFieldList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchTMSCustomField() throws Exception {
        // Initialize the database
        tMSCustomFieldRepository.saveAndFlush(tMSCustomField);
        tMSCustomFieldSearchRepository.save(tMSCustomField);

        // Search the tMSCustomField
        restTMSCustomFieldMockMvc.perform(get("/api/_search/tms-custom-fields?query=id:" + tMSCustomField.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tMSCustomField.getId().intValue())))
            .andExpect(jsonPath("$.[*].entity").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TMSCustomField.class);
        TMSCustomField tMSCustomField1 = new TMSCustomField();
        tMSCustomField1.setId(1L);
        TMSCustomField tMSCustomField2 = new TMSCustomField();
        tMSCustomField2.setId(tMSCustomField1.getId());
        assertThat(tMSCustomField1).isEqualTo(tMSCustomField2);
        tMSCustomField2.setId(2L);
        assertThat(tMSCustomField1).isNotEqualTo(tMSCustomField2);
        tMSCustomField1.setId(null);
        assertThat(tMSCustomField1).isNotEqualTo(tMSCustomField2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TMSCustomFieldDTO.class);
        TMSCustomFieldDTO tMSCustomFieldDTO1 = new TMSCustomFieldDTO();
        tMSCustomFieldDTO1.setId(1L);
        TMSCustomFieldDTO tMSCustomFieldDTO2 = new TMSCustomFieldDTO();
        assertThat(tMSCustomFieldDTO1).isNotEqualTo(tMSCustomFieldDTO2);
        tMSCustomFieldDTO2.setId(tMSCustomFieldDTO1.getId());
        assertThat(tMSCustomFieldDTO1).isEqualTo(tMSCustomFieldDTO2);
        tMSCustomFieldDTO2.setId(2L);
        assertThat(tMSCustomFieldDTO1).isNotEqualTo(tMSCustomFieldDTO2);
        tMSCustomFieldDTO1.setId(null);
        assertThat(tMSCustomFieldDTO1).isNotEqualTo(tMSCustomFieldDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(tMSCustomFieldMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(tMSCustomFieldMapper.fromId(null)).isNull();
    }
}
