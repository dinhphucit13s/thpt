package fpt.dps.dtms.web.rest;

import fpt.dps.dtms.DtmsApp;

import fpt.dps.dtms.domain.BusinessUnit;
import fpt.dps.dtms.repository.BusinessUnitRepository;
import fpt.dps.dtms.service.BusinessUnitService;
import fpt.dps.dtms.repository.search.BusinessUnitSearchRepository;
import fpt.dps.dtms.service.dto.BusinessUnitDTO;
import fpt.dps.dtms.service.mapper.BusinessUnitMapper;
import fpt.dps.dtms.web.rest.errors.ExceptionTranslator;
import fpt.dps.dtms.service.dto.BusinessUnitCriteria;
import fpt.dps.dtms.service.BusinessUnitQueryService;

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

/**
 * Test class for the BusinessUnitResource REST controller.
 *
 * @see BusinessUnitResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DtmsApp.class)
public class BusinessUnitResourceIntTest {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private BusinessUnitRepository businessUnitRepository;

    @Autowired
    private BusinessUnitMapper businessUnitMapper;

    @Autowired
    private BusinessUnitService businessUnitService;

    @Autowired
    private BusinessUnitSearchRepository businessUnitSearchRepository;

    @Autowired
    private BusinessUnitQueryService businessUnitQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restBusinessUnitMockMvc;

    private BusinessUnit businessUnit;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final BusinessUnitResource businessUnitResource = new BusinessUnitResource(businessUnitService, businessUnitQueryService);
        this.restBusinessUnitMockMvc = MockMvcBuilders.standaloneSetup(businessUnitResource)
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
    public static BusinessUnit createEntity(EntityManager em) {
        BusinessUnit businessUnit = new BusinessUnit()
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION);
        return businessUnit;
    }

    @Before
    public void initTest() {
        businessUnitSearchRepository.deleteAll();
        businessUnit = createEntity(em);
    }

    @Test
    @Transactional
    public void createBusinessUnit() throws Exception {
        int databaseSizeBeforeCreate = businessUnitRepository.findAll().size();

        // Create the BusinessUnit
        BusinessUnitDTO businessUnitDTO = businessUnitMapper.toDto(businessUnit);
        restBusinessUnitMockMvc.perform(post("/api/business-units")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(businessUnitDTO)))
            .andExpect(status().isCreated());

        // Validate the BusinessUnit in the database
        List<BusinessUnit> businessUnitList = businessUnitRepository.findAll();
        assertThat(businessUnitList).hasSize(databaseSizeBeforeCreate + 1);
        BusinessUnit testBusinessUnit = businessUnitList.get(businessUnitList.size() - 1);
        assertThat(testBusinessUnit.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testBusinessUnit.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testBusinessUnit.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the BusinessUnit in Elasticsearch
        BusinessUnit businessUnitEs = businessUnitSearchRepository.findOne(testBusinessUnit.getId());
        assertThat(businessUnitEs).isEqualToIgnoringGivenFields(testBusinessUnit);
    }

    @Test
    @Transactional
    public void createBusinessUnitWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = businessUnitRepository.findAll().size();

        // Create the BusinessUnit with an existing ID
        businessUnit.setId(1L);
        BusinessUnitDTO businessUnitDTO = businessUnitMapper.toDto(businessUnit);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBusinessUnitMockMvc.perform(post("/api/business-units")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(businessUnitDTO)))
            .andExpect(status().isBadRequest());

        // Validate the BusinessUnit in the database
        List<BusinessUnit> businessUnitList = businessUnitRepository.findAll();
        assertThat(businessUnitList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = businessUnitRepository.findAll().size();
        // set the field null
        businessUnit.setCode(null);

        // Create the BusinessUnit, which fails.
        BusinessUnitDTO businessUnitDTO = businessUnitMapper.toDto(businessUnit);

        restBusinessUnitMockMvc.perform(post("/api/business-units")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(businessUnitDTO)))
            .andExpect(status().isBadRequest());

        List<BusinessUnit> businessUnitList = businessUnitRepository.findAll();
        assertThat(businessUnitList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = businessUnitRepository.findAll().size();
        // set the field null
        businessUnit.setName(null);

        // Create the BusinessUnit, which fails.
        BusinessUnitDTO businessUnitDTO = businessUnitMapper.toDto(businessUnit);

        restBusinessUnitMockMvc.perform(post("/api/business-units")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(businessUnitDTO)))
            .andExpect(status().isBadRequest());

        List<BusinessUnit> businessUnitList = businessUnitRepository.findAll();
        assertThat(businessUnitList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBusinessUnits() throws Exception {
        // Initialize the database
        businessUnitRepository.saveAndFlush(businessUnit);

        // Get all the businessUnitList
        restBusinessUnitMockMvc.perform(get("/api/business-units?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(businessUnit.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getBusinessUnit() throws Exception {
        // Initialize the database
        businessUnitRepository.saveAndFlush(businessUnit);

        // Get the businessUnit
        restBusinessUnitMockMvc.perform(get("/api/business-units/{id}", businessUnit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(businessUnit.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getAllBusinessUnitsByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        businessUnitRepository.saveAndFlush(businessUnit);

        // Get all the businessUnitList where code equals to DEFAULT_CODE
        defaultBusinessUnitShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the businessUnitList where code equals to UPDATED_CODE
        defaultBusinessUnitShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllBusinessUnitsByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        businessUnitRepository.saveAndFlush(businessUnit);

        // Get all the businessUnitList where code in DEFAULT_CODE or UPDATED_CODE
        defaultBusinessUnitShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the businessUnitList where code equals to UPDATED_CODE
        defaultBusinessUnitShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllBusinessUnitsByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        businessUnitRepository.saveAndFlush(businessUnit);

        // Get all the businessUnitList where code is not null
        defaultBusinessUnitShouldBeFound("code.specified=true");

        // Get all the businessUnitList where code is null
        defaultBusinessUnitShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    public void getAllBusinessUnitsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        businessUnitRepository.saveAndFlush(businessUnit);

        // Get all the businessUnitList where name equals to DEFAULT_NAME
        defaultBusinessUnitShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the businessUnitList where name equals to UPDATED_NAME
        defaultBusinessUnitShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllBusinessUnitsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        businessUnitRepository.saveAndFlush(businessUnit);

        // Get all the businessUnitList where name in DEFAULT_NAME or UPDATED_NAME
        defaultBusinessUnitShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the businessUnitList where name equals to UPDATED_NAME
        defaultBusinessUnitShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllBusinessUnitsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        businessUnitRepository.saveAndFlush(businessUnit);

        // Get all the businessUnitList where name is not null
        defaultBusinessUnitShouldBeFound("name.specified=true");

        // Get all the businessUnitList where name is null
        defaultBusinessUnitShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllBusinessUnitsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        businessUnitRepository.saveAndFlush(businessUnit);

        // Get all the businessUnitList where description equals to DEFAULT_DESCRIPTION
        defaultBusinessUnitShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the businessUnitList where description equals to UPDATED_DESCRIPTION
        defaultBusinessUnitShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllBusinessUnitsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        businessUnitRepository.saveAndFlush(businessUnit);

        // Get all the businessUnitList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultBusinessUnitShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the businessUnitList where description equals to UPDATED_DESCRIPTION
        defaultBusinessUnitShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllBusinessUnitsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        businessUnitRepository.saveAndFlush(businessUnit);

        // Get all the businessUnitList where description is not null
        defaultBusinessUnitShouldBeFound("description.specified=true");

        // Get all the businessUnitList where description is null
        defaultBusinessUnitShouldNotBeFound("description.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultBusinessUnitShouldBeFound(String filter) throws Exception {
        restBusinessUnitMockMvc.perform(get("/api/business-units?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(businessUnit.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultBusinessUnitShouldNotBeFound(String filter) throws Exception {
        restBusinessUnitMockMvc.perform(get("/api/business-units?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingBusinessUnit() throws Exception {
        // Get the businessUnit
        restBusinessUnitMockMvc.perform(get("/api/business-units/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBusinessUnit() throws Exception {
        // Initialize the database
        businessUnitRepository.saveAndFlush(businessUnit);
        businessUnitSearchRepository.save(businessUnit);
        int databaseSizeBeforeUpdate = businessUnitRepository.findAll().size();

        // Update the businessUnit
        BusinessUnit updatedBusinessUnit = businessUnitRepository.findOne(businessUnit.getId());
        // Disconnect from session so that the updates on updatedBusinessUnit are not directly saved in db
        em.detach(updatedBusinessUnit);
        updatedBusinessUnit
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION);
        BusinessUnitDTO businessUnitDTO = businessUnitMapper.toDto(updatedBusinessUnit);

        restBusinessUnitMockMvc.perform(put("/api/business-units")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(businessUnitDTO)))
            .andExpect(status().isOk());

        // Validate the BusinessUnit in the database
        List<BusinessUnit> businessUnitList = businessUnitRepository.findAll();
        assertThat(businessUnitList).hasSize(databaseSizeBeforeUpdate);
        BusinessUnit testBusinessUnit = businessUnitList.get(businessUnitList.size() - 1);
        assertThat(testBusinessUnit.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testBusinessUnit.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testBusinessUnit.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the BusinessUnit in Elasticsearch
        BusinessUnit businessUnitEs = businessUnitSearchRepository.findOne(testBusinessUnit.getId());
        assertThat(businessUnitEs).isEqualToIgnoringGivenFields(testBusinessUnit);
    }

    @Test
    @Transactional
    public void updateNonExistingBusinessUnit() throws Exception {
        int databaseSizeBeforeUpdate = businessUnitRepository.findAll().size();

        // Create the BusinessUnit
        BusinessUnitDTO businessUnitDTO = businessUnitMapper.toDto(businessUnit);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restBusinessUnitMockMvc.perform(put("/api/business-units")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(businessUnitDTO)))
            .andExpect(status().isCreated());

        // Validate the BusinessUnit in the database
        List<BusinessUnit> businessUnitList = businessUnitRepository.findAll();
        assertThat(businessUnitList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteBusinessUnit() throws Exception {
        // Initialize the database
        businessUnitRepository.saveAndFlush(businessUnit);
        businessUnitSearchRepository.save(businessUnit);
        int databaseSizeBeforeDelete = businessUnitRepository.findAll().size();

        // Get the businessUnit
        restBusinessUnitMockMvc.perform(delete("/api/business-units/{id}", businessUnit.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean businessUnitExistsInEs = businessUnitSearchRepository.exists(businessUnit.getId());
        assertThat(businessUnitExistsInEs).isFalse();

        // Validate the database is empty
        List<BusinessUnit> businessUnitList = businessUnitRepository.findAll();
        assertThat(businessUnitList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchBusinessUnit() throws Exception {
        // Initialize the database
        businessUnitRepository.saveAndFlush(businessUnit);
        businessUnitSearchRepository.save(businessUnit);

        // Search the businessUnit
        restBusinessUnitMockMvc.perform(get("/api/_search/business-units?query=id:" + businessUnit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(businessUnit.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BusinessUnit.class);
        BusinessUnit businessUnit1 = new BusinessUnit();
        businessUnit1.setId(1L);
        BusinessUnit businessUnit2 = new BusinessUnit();
        businessUnit2.setId(businessUnit1.getId());
        assertThat(businessUnit1).isEqualTo(businessUnit2);
        businessUnit2.setId(2L);
        assertThat(businessUnit1).isNotEqualTo(businessUnit2);
        businessUnit1.setId(null);
        assertThat(businessUnit1).isNotEqualTo(businessUnit2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BusinessUnitDTO.class);
        BusinessUnitDTO businessUnitDTO1 = new BusinessUnitDTO();
        businessUnitDTO1.setId(1L);
        BusinessUnitDTO businessUnitDTO2 = new BusinessUnitDTO();
        assertThat(businessUnitDTO1).isNotEqualTo(businessUnitDTO2);
        businessUnitDTO2.setId(businessUnitDTO1.getId());
        assertThat(businessUnitDTO1).isEqualTo(businessUnitDTO2);
        businessUnitDTO2.setId(2L);
        assertThat(businessUnitDTO1).isNotEqualTo(businessUnitDTO2);
        businessUnitDTO1.setId(null);
        assertThat(businessUnitDTO1).isNotEqualTo(businessUnitDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(businessUnitMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(businessUnitMapper.fromId(null)).isNull();
    }
}
