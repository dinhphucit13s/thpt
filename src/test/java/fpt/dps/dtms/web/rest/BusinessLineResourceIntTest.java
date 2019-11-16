package fpt.dps.dtms.web.rest;

import fpt.dps.dtms.DtmsApp;

import fpt.dps.dtms.domain.BusinessLine;
import fpt.dps.dtms.repository.BusinessLineRepository;
import fpt.dps.dtms.service.BusinessLineService;
import fpt.dps.dtms.repository.search.BusinessLineSearchRepository;
import fpt.dps.dtms.service.dto.BusinessLineDTO;
import fpt.dps.dtms.service.mapper.BusinessLineMapper;
import fpt.dps.dtms.web.rest.errors.ExceptionTranslator;
import fpt.dps.dtms.service.dto.BusinessLineCriteria;
import fpt.dps.dtms.service.BusinessLineQueryService;

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
 * Test class for the BusinessLineResource REST controller.
 *
 * @see BusinessLineResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DtmsApp.class)
public class BusinessLineResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private BusinessLineRepository businessLineRepository;

    @Autowired
    private BusinessLineMapper businessLineMapper;

    @Autowired
    private BusinessLineService businessLineService;

    @Autowired
    private BusinessLineSearchRepository businessLineSearchRepository;

    @Autowired
    private BusinessLineQueryService businessLineQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restBusinessLineMockMvc;

    private BusinessLine businessLine;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final BusinessLineResource businessLineResource = new BusinessLineResource(businessLineService, businessLineQueryService);
        this.restBusinessLineMockMvc = MockMvcBuilders.standaloneSetup(businessLineResource)
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
    public static BusinessLine createEntity(EntityManager em) {
        BusinessLine businessLine = new BusinessLine()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION);
        return businessLine;
    }

    @Before
    public void initTest() {
        businessLineSearchRepository.deleteAll();
        businessLine = createEntity(em);
    }

    @Test
    @Transactional
    public void createBusinessLine() throws Exception {
        int databaseSizeBeforeCreate = businessLineRepository.findAll().size();

        // Create the BusinessLine
        BusinessLineDTO businessLineDTO = businessLineMapper.toDto(businessLine);
        restBusinessLineMockMvc.perform(post("/api/business-lines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(businessLineDTO)))
            .andExpect(status().isCreated());

        // Validate the BusinessLine in the database
        List<BusinessLine> businessLineList = businessLineRepository.findAll();
        assertThat(businessLineList).hasSize(databaseSizeBeforeCreate + 1);
        BusinessLine testBusinessLine = businessLineList.get(businessLineList.size() - 1);
        assertThat(testBusinessLine.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testBusinessLine.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the BusinessLine in Elasticsearch
        BusinessLine businessLineEs = businessLineSearchRepository.findOne(testBusinessLine.getId());
        assertThat(businessLineEs).isEqualToIgnoringGivenFields(testBusinessLine);
    }

    @Test
    @Transactional
    public void createBusinessLineWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = businessLineRepository.findAll().size();

        // Create the BusinessLine with an existing ID
        businessLine.setId(1L);
        BusinessLineDTO businessLineDTO = businessLineMapper.toDto(businessLine);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBusinessLineMockMvc.perform(post("/api/business-lines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(businessLineDTO)))
            .andExpect(status().isBadRequest());

        // Validate the BusinessLine in the database
        List<BusinessLine> businessLineList = businessLineRepository.findAll();
        assertThat(businessLineList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = businessLineRepository.findAll().size();
        // set the field null
        businessLine.setName(null);

        // Create the BusinessLine, which fails.
        BusinessLineDTO businessLineDTO = businessLineMapper.toDto(businessLine);

        restBusinessLineMockMvc.perform(post("/api/business-lines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(businessLineDTO)))
            .andExpect(status().isBadRequest());

        List<BusinessLine> businessLineList = businessLineRepository.findAll();
        assertThat(businessLineList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBusinessLines() throws Exception {
        // Initialize the database
        businessLineRepository.saveAndFlush(businessLine);

        // Get all the businessLineList
        restBusinessLineMockMvc.perform(get("/api/business-lines?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(businessLine.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getBusinessLine() throws Exception {
        // Initialize the database
        businessLineRepository.saveAndFlush(businessLine);

        // Get the businessLine
        restBusinessLineMockMvc.perform(get("/api/business-lines/{id}", businessLine.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(businessLine.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getAllBusinessLinesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        businessLineRepository.saveAndFlush(businessLine);

        // Get all the businessLineList where name equals to DEFAULT_NAME
        defaultBusinessLineShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the businessLineList where name equals to UPDATED_NAME
        defaultBusinessLineShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllBusinessLinesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        businessLineRepository.saveAndFlush(businessLine);

        // Get all the businessLineList where name in DEFAULT_NAME or UPDATED_NAME
        defaultBusinessLineShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the businessLineList where name equals to UPDATED_NAME
        defaultBusinessLineShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllBusinessLinesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        businessLineRepository.saveAndFlush(businessLine);

        // Get all the businessLineList where name is not null
        defaultBusinessLineShouldBeFound("name.specified=true");

        // Get all the businessLineList where name is null
        defaultBusinessLineShouldNotBeFound("name.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultBusinessLineShouldBeFound(String filter) throws Exception {
        restBusinessLineMockMvc.perform(get("/api/business-lines?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(businessLine.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultBusinessLineShouldNotBeFound(String filter) throws Exception {
        restBusinessLineMockMvc.perform(get("/api/business-lines?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingBusinessLine() throws Exception {
        // Get the businessLine
        restBusinessLineMockMvc.perform(get("/api/business-lines/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBusinessLine() throws Exception {
        // Initialize the database
        businessLineRepository.saveAndFlush(businessLine);
        businessLineSearchRepository.save(businessLine);
        int databaseSizeBeforeUpdate = businessLineRepository.findAll().size();

        // Update the businessLine
        BusinessLine updatedBusinessLine = businessLineRepository.findOne(businessLine.getId());
        // Disconnect from session so that the updates on updatedBusinessLine are not directly saved in db
        em.detach(updatedBusinessLine);
        updatedBusinessLine
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION);
        BusinessLineDTO businessLineDTO = businessLineMapper.toDto(updatedBusinessLine);

        restBusinessLineMockMvc.perform(put("/api/business-lines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(businessLineDTO)))
            .andExpect(status().isOk());

        // Validate the BusinessLine in the database
        List<BusinessLine> businessLineList = businessLineRepository.findAll();
        assertThat(businessLineList).hasSize(databaseSizeBeforeUpdate);
        BusinessLine testBusinessLine = businessLineList.get(businessLineList.size() - 1);
        assertThat(testBusinessLine.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testBusinessLine.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the BusinessLine in Elasticsearch
        BusinessLine businessLineEs = businessLineSearchRepository.findOne(testBusinessLine.getId());
        assertThat(businessLineEs).isEqualToIgnoringGivenFields(testBusinessLine);
    }

    @Test
    @Transactional
    public void updateNonExistingBusinessLine() throws Exception {
        int databaseSizeBeforeUpdate = businessLineRepository.findAll().size();

        // Create the BusinessLine
        BusinessLineDTO businessLineDTO = businessLineMapper.toDto(businessLine);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restBusinessLineMockMvc.perform(put("/api/business-lines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(businessLineDTO)))
            .andExpect(status().isCreated());

        // Validate the BusinessLine in the database
        List<BusinessLine> businessLineList = businessLineRepository.findAll();
        assertThat(businessLineList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteBusinessLine() throws Exception {
        // Initialize the database
        businessLineRepository.saveAndFlush(businessLine);
        businessLineSearchRepository.save(businessLine);
        int databaseSizeBeforeDelete = businessLineRepository.findAll().size();

        // Get the businessLine
        restBusinessLineMockMvc.perform(delete("/api/business-lines/{id}", businessLine.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean businessLineExistsInEs = businessLineSearchRepository.exists(businessLine.getId());
        assertThat(businessLineExistsInEs).isFalse();

        // Validate the database is empty
        List<BusinessLine> businessLineList = businessLineRepository.findAll();
        assertThat(businessLineList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchBusinessLine() throws Exception {
        // Initialize the database
        businessLineRepository.saveAndFlush(businessLine);
        businessLineSearchRepository.save(businessLine);

        // Search the businessLine
        restBusinessLineMockMvc.perform(get("/api/_search/business-lines?query=id:" + businessLine.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(businessLine.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BusinessLine.class);
        BusinessLine businessLine1 = new BusinessLine();
        businessLine1.setId(1L);
        BusinessLine businessLine2 = new BusinessLine();
        businessLine2.setId(businessLine1.getId());
        assertThat(businessLine1).isEqualTo(businessLine2);
        businessLine2.setId(2L);
        assertThat(businessLine1).isNotEqualTo(businessLine2);
        businessLine1.setId(null);
        assertThat(businessLine1).isNotEqualTo(businessLine2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BusinessLineDTO.class);
        BusinessLineDTO businessLineDTO1 = new BusinessLineDTO();
        businessLineDTO1.setId(1L);
        BusinessLineDTO businessLineDTO2 = new BusinessLineDTO();
        assertThat(businessLineDTO1).isNotEqualTo(businessLineDTO2);
        businessLineDTO2.setId(businessLineDTO1.getId());
        assertThat(businessLineDTO1).isEqualTo(businessLineDTO2);
        businessLineDTO2.setId(2L);
        assertThat(businessLineDTO1).isNotEqualTo(businessLineDTO2);
        businessLineDTO1.setId(null);
        assertThat(businessLineDTO1).isNotEqualTo(businessLineDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(businessLineMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(businessLineMapper.fromId(null)).isNull();
    }
}
