package fpt.dps.dtms.web.rest;

import fpt.dps.dtms.DtmsApp;

import fpt.dps.dtms.domain.Issues;
import fpt.dps.dtms.domain.PurchaseOrders;
import fpt.dps.dtms.domain.Projects;
import fpt.dps.dtms.repository.IssuesRepository;
import fpt.dps.dtms.service.IssuesService;
import fpt.dps.dtms.repository.search.IssuesSearchRepository;
import fpt.dps.dtms.service.dto.IssuesDTO;
import fpt.dps.dtms.service.mapper.IssuesMapper;
import fpt.dps.dtms.web.rest.errors.ExceptionTranslator;
import fpt.dps.dtms.service.dto.IssuesCriteria;
import fpt.dps.dtms.service.IssuesQueryService;

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

import fpt.dps.dtms.domain.enumeration.IssueStatus;
/**
 * Test class for the IssuesResource REST controller.
 *
 * @see IssuesResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DtmsApp.class)
public class IssuesResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final IssueStatus DEFAULT_STATUS = IssueStatus.NA;
    private static final IssueStatus UPDATED_STATUS = IssueStatus.REVIEWING;

    @Autowired
    private IssuesRepository issuesRepository;

    @Autowired
    private IssuesMapper issuesMapper;

    @Autowired
    private IssuesService issuesService;

    @Autowired
    private IssuesSearchRepository issuesSearchRepository;

    @Autowired
    private IssuesQueryService issuesQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restIssuesMockMvc;

    private Issues issues;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final IssuesResource issuesResource = new IssuesResource(issuesService, issuesQueryService);
        this.restIssuesMockMvc = MockMvcBuilders.standaloneSetup(issuesResource)
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
    public static Issues createEntity(EntityManager em) {
        Issues issues = new Issues()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .status(DEFAULT_STATUS);
        return issues;
    }

    @Before
    public void initTest() {
        issuesSearchRepository.deleteAll();
        issues = createEntity(em);
    }

    @Test
    @Transactional
    public void createIssues() throws Exception {
        int databaseSizeBeforeCreate = issuesRepository.findAll().size();

        // Create the Issues
        IssuesDTO issuesDTO = issuesMapper.toDto(issues);
        restIssuesMockMvc.perform(post("/api/issues")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(issuesDTO)))
            .andExpect(status().isCreated());

        // Validate the Issues in the database
        List<Issues> issuesList = issuesRepository.findAll();
        assertThat(issuesList).hasSize(databaseSizeBeforeCreate + 1);
        Issues testIssues = issuesList.get(issuesList.size() - 1);
        assertThat(testIssues.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testIssues.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testIssues.getStatus()).isEqualTo(DEFAULT_STATUS);

        // Validate the Issues in Elasticsearch
        Issues issuesEs = issuesSearchRepository.findOne(testIssues.getId());
        assertThat(issuesEs).isEqualToIgnoringGivenFields(testIssues);
    }

    @Test
    @Transactional
    public void createIssuesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = issuesRepository.findAll().size();

        // Create the Issues with an existing ID
        issues.setId(1L);
        IssuesDTO issuesDTO = issuesMapper.toDto(issues);

        // An entity with an existing ID cannot be created, so this API call must fail
        restIssuesMockMvc.perform(post("/api/issues")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(issuesDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Issues in the database
        List<Issues> issuesList = issuesRepository.findAll();
        assertThat(issuesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = issuesRepository.findAll().size();
        // set the field null
        issues.setName(null);

        // Create the Issues, which fails.
        IssuesDTO issuesDTO = issuesMapper.toDto(issues);

        restIssuesMockMvc.perform(post("/api/issues")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(issuesDTO)))
            .andExpect(status().isBadRequest());

        List<Issues> issuesList = issuesRepository.findAll();
        assertThat(issuesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllIssues() throws Exception {
        // Initialize the database
        issuesRepository.saveAndFlush(issues);

        // Get all the issuesList
        restIssuesMockMvc.perform(get("/api/issues?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(issues.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void getIssues() throws Exception {
        // Initialize the database
        issuesRepository.saveAndFlush(issues);

        // Get the issues
        restIssuesMockMvc.perform(get("/api/issues/{id}", issues.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(issues.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getAllIssuesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        issuesRepository.saveAndFlush(issues);

        // Get all the issuesList where name equals to DEFAULT_NAME
        defaultIssuesShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the issuesList where name equals to UPDATED_NAME
        defaultIssuesShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllIssuesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        issuesRepository.saveAndFlush(issues);

        // Get all the issuesList where name in DEFAULT_NAME or UPDATED_NAME
        defaultIssuesShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the issuesList where name equals to UPDATED_NAME
        defaultIssuesShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllIssuesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        issuesRepository.saveAndFlush(issues);

        // Get all the issuesList where name is not null
        defaultIssuesShouldBeFound("name.specified=true");

        // Get all the issuesList where name is null
        defaultIssuesShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllIssuesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        issuesRepository.saveAndFlush(issues);

        // Get all the issuesList where description equals to DEFAULT_DESCRIPTION
        defaultIssuesShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the issuesList where description equals to UPDATED_DESCRIPTION
        defaultIssuesShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllIssuesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        issuesRepository.saveAndFlush(issues);

        // Get all the issuesList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultIssuesShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the issuesList where description equals to UPDATED_DESCRIPTION
        defaultIssuesShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllIssuesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        issuesRepository.saveAndFlush(issues);

        // Get all the issuesList where description is not null
        defaultIssuesShouldBeFound("description.specified=true");

        // Get all the issuesList where description is null
        defaultIssuesShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    public void getAllIssuesByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        issuesRepository.saveAndFlush(issues);

        // Get all the issuesList where status equals to DEFAULT_STATUS
        defaultIssuesShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the issuesList where status equals to UPDATED_STATUS
        defaultIssuesShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllIssuesByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        issuesRepository.saveAndFlush(issues);

        // Get all the issuesList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultIssuesShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the issuesList where status equals to UPDATED_STATUS
        defaultIssuesShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllIssuesByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        issuesRepository.saveAndFlush(issues);

        // Get all the issuesList where status is not null
        defaultIssuesShouldBeFound("status.specified=true");

        // Get all the issuesList where status is null
        defaultIssuesShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    public void getAllIssuesByPurchaseOrderIsEqualToSomething() throws Exception {
        // Initialize the database
        PurchaseOrders purchaseOrder = PurchaseOrdersResourceIntTest.createEntity(em);
        em.persist(purchaseOrder);
        em.flush();
        issues.setPurchaseOrder(purchaseOrder);
        issuesRepository.saveAndFlush(issues);
        Long purchaseOrderId = purchaseOrder.getId();

        // Get all the issuesList where purchaseOrder equals to purchaseOrderId
        defaultIssuesShouldBeFound("purchaseOrderId.equals=" + purchaseOrderId);

        // Get all the issuesList where purchaseOrder equals to purchaseOrderId + 1
        defaultIssuesShouldNotBeFound("purchaseOrderId.equals=" + (purchaseOrderId + 1));
    }


    @Test
    @Transactional
    public void getAllIssuesByProjectsIsEqualToSomething() throws Exception {
        // Initialize the database
        Projects projects = ProjectsResourceIntTest.createEntity(em);
        em.persist(projects);
        em.flush();
        issues.setProjects(projects);
        issuesRepository.saveAndFlush(issues);
        Long projectsId = projects.getId();

        // Get all the issuesList where projects equals to projectsId
        defaultIssuesShouldBeFound("projectsId.equals=" + projectsId);

        // Get all the issuesList where projects equals to projectsId + 1
        defaultIssuesShouldNotBeFound("projectsId.equals=" + (projectsId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultIssuesShouldBeFound(String filter) throws Exception {
        restIssuesMockMvc.perform(get("/api/issues?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(issues.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultIssuesShouldNotBeFound(String filter) throws Exception {
        restIssuesMockMvc.perform(get("/api/issues?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingIssues() throws Exception {
        // Get the issues
        restIssuesMockMvc.perform(get("/api/issues/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateIssues() throws Exception {
        // Initialize the database
        issuesRepository.saveAndFlush(issues);
        issuesSearchRepository.save(issues);
        int databaseSizeBeforeUpdate = issuesRepository.findAll().size();

        // Update the issues
        Issues updatedIssues = issuesRepository.findOne(issues.getId());
        // Disconnect from session so that the updates on updatedIssues are not directly saved in db
        em.detach(updatedIssues);
        updatedIssues
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .status(UPDATED_STATUS);
        IssuesDTO issuesDTO = issuesMapper.toDto(updatedIssues);

        restIssuesMockMvc.perform(put("/api/issues")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(issuesDTO)))
            .andExpect(status().isOk());

        // Validate the Issues in the database
        List<Issues> issuesList = issuesRepository.findAll();
        assertThat(issuesList).hasSize(databaseSizeBeforeUpdate);
        Issues testIssues = issuesList.get(issuesList.size() - 1);
        assertThat(testIssues.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testIssues.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testIssues.getStatus()).isEqualTo(UPDATED_STATUS);

        // Validate the Issues in Elasticsearch
        Issues issuesEs = issuesSearchRepository.findOne(testIssues.getId());
        assertThat(issuesEs).isEqualToIgnoringGivenFields(testIssues);
    }

    @Test
    @Transactional
    public void updateNonExistingIssues() throws Exception {
        int databaseSizeBeforeUpdate = issuesRepository.findAll().size();

        // Create the Issues
        IssuesDTO issuesDTO = issuesMapper.toDto(issues);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restIssuesMockMvc.perform(put("/api/issues")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(issuesDTO)))
            .andExpect(status().isCreated());

        // Validate the Issues in the database
        List<Issues> issuesList = issuesRepository.findAll();
        assertThat(issuesList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteIssues() throws Exception {
        // Initialize the database
        issuesRepository.saveAndFlush(issues);
        issuesSearchRepository.save(issues);
        int databaseSizeBeforeDelete = issuesRepository.findAll().size();

        // Get the issues
        restIssuesMockMvc.perform(delete("/api/issues/{id}", issues.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean issuesExistsInEs = issuesSearchRepository.exists(issues.getId());
        assertThat(issuesExistsInEs).isFalse();

        // Validate the database is empty
        List<Issues> issuesList = issuesRepository.findAll();
        assertThat(issuesList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchIssues() throws Exception {
        // Initialize the database
        issuesRepository.saveAndFlush(issues);
        issuesSearchRepository.save(issues);

        // Search the issues
        restIssuesMockMvc.perform(get("/api/_search/issues?query=id:" + issues.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(issues.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Issues.class);
        Issues issues1 = new Issues();
        issues1.setId(1L);
        Issues issues2 = new Issues();
        issues2.setId(issues1.getId());
        assertThat(issues1).isEqualTo(issues2);
        issues2.setId(2L);
        assertThat(issues1).isNotEqualTo(issues2);
        issues1.setId(null);
        assertThat(issues1).isNotEqualTo(issues2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(IssuesDTO.class);
        IssuesDTO issuesDTO1 = new IssuesDTO();
        issuesDTO1.setId(1L);
        IssuesDTO issuesDTO2 = new IssuesDTO();
        assertThat(issuesDTO1).isNotEqualTo(issuesDTO2);
        issuesDTO2.setId(issuesDTO1.getId());
        assertThat(issuesDTO1).isEqualTo(issuesDTO2);
        issuesDTO2.setId(2L);
        assertThat(issuesDTO1).isNotEqualTo(issuesDTO2);
        issuesDTO1.setId(null);
        assertThat(issuesDTO1).isNotEqualTo(issuesDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(issuesMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(issuesMapper.fromId(null)).isNull();
    }
}
