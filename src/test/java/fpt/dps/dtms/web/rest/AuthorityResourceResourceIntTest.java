package fpt.dps.dtms.web.rest;

import fpt.dps.dtms.DtmsApp;

import fpt.dps.dtms.domain.AuthorityResource;
import fpt.dps.dtms.repository.AuthorityResourceRepository;
import fpt.dps.dtms.service.AuthorityResourceService;
import fpt.dps.dtms.repository.search.AuthorityResourceSearchRepository;
import fpt.dps.dtms.service.dto.AuthorityResourceDTO;
import fpt.dps.dtms.service.mapper.AuthorityResourceMapper;
import fpt.dps.dtms.web.rest.errors.ExceptionTranslator;
import fpt.dps.dtms.service.dto.AuthorityResourceCriteria;
import fpt.dps.dtms.service.AuthorityResourceQueryService;

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
 * Test class for the AuthorityResourceResource REST controller.
 *
 * @see AuthorityResourceResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DtmsApp.class)
public class AuthorityResourceResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_PERMISSION = 1;
    private static final Integer UPDATED_PERMISSION = 2;

    private static final String DEFAULT_AUTHORITY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_AUTHORITY_NAME = "BBBBBBBBBB";

    @Autowired
    private AuthorityResourceRepository authorityResourceRepository;

    @Autowired
    private AuthorityResourceMapper authorityResourceMapper;

    @Autowired
    private AuthorityResourceService authorityResourceService;

    @Autowired
    private AuthorityResourceSearchRepository authorityResourceSearchRepository;

    @Autowired
    private AuthorityResourceQueryService authorityResourceQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restAuthorityResourceMockMvc;

    private AuthorityResource authorityResource;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AuthorityResourceResource authorityResourceResource = new AuthorityResourceResource(authorityResourceService, authorityResourceQueryService);
        this.restAuthorityResourceMockMvc = MockMvcBuilders.standaloneSetup(authorityResourceResource)
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
    public static AuthorityResource createEntity(EntityManager em) {
        AuthorityResource authorityResource = new AuthorityResource()
            .name(DEFAULT_NAME)
            .permission(DEFAULT_PERMISSION)
            .authorityName(DEFAULT_AUTHORITY_NAME);
        return authorityResource;
    }

    @Before
    public void initTest() {
        authorityResourceSearchRepository.deleteAll();
        authorityResource = createEntity(em);
    }

    @Test
    @Transactional
    public void createAuthorityResource() throws Exception {
        int databaseSizeBeforeCreate = authorityResourceRepository.findAll().size();

        // Create the AuthorityResource
        AuthorityResourceDTO authorityResourceDTO = authorityResourceMapper.toDto(authorityResource);
        restAuthorityResourceMockMvc.perform(post("/api/authority-resources")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(authorityResourceDTO)))
            .andExpect(status().isCreated());

        // Validate the AuthorityResource in the database
        List<AuthorityResource> authorityResourceList = authorityResourceRepository.findAll();
        assertThat(authorityResourceList).hasSize(databaseSizeBeforeCreate + 1);
        AuthorityResource testAuthorityResource = authorityResourceList.get(authorityResourceList.size() - 1);
        assertThat(testAuthorityResource.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testAuthorityResource.getPermission()).isEqualTo(DEFAULT_PERMISSION);
        assertThat(testAuthorityResource.getAuthorityName()).isEqualTo(DEFAULT_AUTHORITY_NAME);

        // Validate the AuthorityResource in Elasticsearch
        AuthorityResource authorityResourceEs = authorityResourceSearchRepository.findOne(testAuthorityResource.getId());
        assertThat(authorityResourceEs).isEqualToIgnoringGivenFields(testAuthorityResource);
    }

    @Test
    @Transactional
    public void createAuthorityResourceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = authorityResourceRepository.findAll().size();

        // Create the AuthorityResource with an existing ID
        authorityResource.setId(1L);
        AuthorityResourceDTO authorityResourceDTO = authorityResourceMapper.toDto(authorityResource);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAuthorityResourceMockMvc.perform(post("/api/authority-resources")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(authorityResourceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AuthorityResource in the database
        List<AuthorityResource> authorityResourceList = authorityResourceRepository.findAll();
        assertThat(authorityResourceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = authorityResourceRepository.findAll().size();
        // set the field null
        authorityResource.setName(null);

        // Create the AuthorityResource, which fails.
        AuthorityResourceDTO authorityResourceDTO = authorityResourceMapper.toDto(authorityResource);

        restAuthorityResourceMockMvc.perform(post("/api/authority-resources")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(authorityResourceDTO)))
            .andExpect(status().isBadRequest());

        List<AuthorityResource> authorityResourceList = authorityResourceRepository.findAll();
        assertThat(authorityResourceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPermissionIsRequired() throws Exception {
        int databaseSizeBeforeTest = authorityResourceRepository.findAll().size();
        // set the field null
        authorityResource.setPermission(null);

        // Create the AuthorityResource, which fails.
        AuthorityResourceDTO authorityResourceDTO = authorityResourceMapper.toDto(authorityResource);

        restAuthorityResourceMockMvc.perform(post("/api/authority-resources")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(authorityResourceDTO)))
            .andExpect(status().isBadRequest());

        List<AuthorityResource> authorityResourceList = authorityResourceRepository.findAll();
        assertThat(authorityResourceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAuthorityResources() throws Exception {
        // Initialize the database
        authorityResourceRepository.saveAndFlush(authorityResource);

        // Get all the authorityResourceList
        restAuthorityResourceMockMvc.perform(get("/api/authority-resources?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(authorityResource.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].permission").value(hasItem(DEFAULT_PERMISSION)))
            .andExpect(jsonPath("$.[*].authorityName").value(hasItem(DEFAULT_AUTHORITY_NAME.toString())));
    }

    @Test
    @Transactional
    public void getAuthorityResource() throws Exception {
        // Initialize the database
        authorityResourceRepository.saveAndFlush(authorityResource);

        // Get the authorityResource
        restAuthorityResourceMockMvc.perform(get("/api/authority-resources/{id}", authorityResource.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(authorityResource.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.permission").value(DEFAULT_PERMISSION))
            .andExpect(jsonPath("$.authorityName").value(DEFAULT_AUTHORITY_NAME.toString()));
    }

    @Test
    @Transactional
    public void getAllAuthorityResourcesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        authorityResourceRepository.saveAndFlush(authorityResource);

        // Get all the authorityResourceList where name equals to DEFAULT_NAME
        defaultAuthorityResourceShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the authorityResourceList where name equals to UPDATED_NAME
        defaultAuthorityResourceShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllAuthorityResourcesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        authorityResourceRepository.saveAndFlush(authorityResource);

        // Get all the authorityResourceList where name in DEFAULT_NAME or UPDATED_NAME
        defaultAuthorityResourceShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the authorityResourceList where name equals to UPDATED_NAME
        defaultAuthorityResourceShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllAuthorityResourcesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        authorityResourceRepository.saveAndFlush(authorityResource);

        // Get all the authorityResourceList where name is not null
        defaultAuthorityResourceShouldBeFound("name.specified=true");

        // Get all the authorityResourceList where name is null
        defaultAuthorityResourceShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllAuthorityResourcesByPermissionIsEqualToSomething() throws Exception {
        // Initialize the database
        authorityResourceRepository.saveAndFlush(authorityResource);

        // Get all the authorityResourceList where permission equals to DEFAULT_PERMISSION
        defaultAuthorityResourceShouldBeFound("permission.equals=" + DEFAULT_PERMISSION);

        // Get all the authorityResourceList where permission equals to UPDATED_PERMISSION
        defaultAuthorityResourceShouldNotBeFound("permission.equals=" + UPDATED_PERMISSION);
    }

    @Test
    @Transactional
    public void getAllAuthorityResourcesByPermissionIsInShouldWork() throws Exception {
        // Initialize the database
        authorityResourceRepository.saveAndFlush(authorityResource);

        // Get all the authorityResourceList where permission in DEFAULT_PERMISSION or UPDATED_PERMISSION
        defaultAuthorityResourceShouldBeFound("permission.in=" + DEFAULT_PERMISSION + "," + UPDATED_PERMISSION);

        // Get all the authorityResourceList where permission equals to UPDATED_PERMISSION
        defaultAuthorityResourceShouldNotBeFound("permission.in=" + UPDATED_PERMISSION);
    }

    @Test
    @Transactional
    public void getAllAuthorityResourcesByPermissionIsNullOrNotNull() throws Exception {
        // Initialize the database
        authorityResourceRepository.saveAndFlush(authorityResource);

        // Get all the authorityResourceList where permission is not null
        defaultAuthorityResourceShouldBeFound("permission.specified=true");

        // Get all the authorityResourceList where permission is null
        defaultAuthorityResourceShouldNotBeFound("permission.specified=false");
    }

    @Test
    @Transactional
    public void getAllAuthorityResourcesByPermissionIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        authorityResourceRepository.saveAndFlush(authorityResource);

        // Get all the authorityResourceList where permission greater than or equals to DEFAULT_PERMISSION
        defaultAuthorityResourceShouldBeFound("permission.greaterOrEqualThan=" + DEFAULT_PERMISSION);

        // Get all the authorityResourceList where permission greater than or equals to UPDATED_PERMISSION
        defaultAuthorityResourceShouldNotBeFound("permission.greaterOrEqualThan=" + UPDATED_PERMISSION);
    }

    @Test
    @Transactional
    public void getAllAuthorityResourcesByPermissionIsLessThanSomething() throws Exception {
        // Initialize the database
        authorityResourceRepository.saveAndFlush(authorityResource);

        // Get all the authorityResourceList where permission less than or equals to DEFAULT_PERMISSION
        defaultAuthorityResourceShouldNotBeFound("permission.lessThan=" + DEFAULT_PERMISSION);

        // Get all the authorityResourceList where permission less than or equals to UPDATED_PERMISSION
        defaultAuthorityResourceShouldBeFound("permission.lessThan=" + UPDATED_PERMISSION);
    }


    @Test
    @Transactional
    public void getAllAuthorityResourcesByAuthorityNameIsEqualToSomething() throws Exception {
        // Initialize the database
        authorityResourceRepository.saveAndFlush(authorityResource);

        // Get all the authorityResourceList where authorityName equals to DEFAULT_AUTHORITY_NAME
        defaultAuthorityResourceShouldBeFound("authorityName.equals=" + DEFAULT_AUTHORITY_NAME);

        // Get all the authorityResourceList where authorityName equals to UPDATED_AUTHORITY_NAME
        defaultAuthorityResourceShouldNotBeFound("authorityName.equals=" + UPDATED_AUTHORITY_NAME);
    }

    @Test
    @Transactional
    public void getAllAuthorityResourcesByAuthorityNameIsInShouldWork() throws Exception {
        // Initialize the database
        authorityResourceRepository.saveAndFlush(authorityResource);

        // Get all the authorityResourceList where authorityName in DEFAULT_AUTHORITY_NAME or UPDATED_AUTHORITY_NAME
        defaultAuthorityResourceShouldBeFound("authorityName.in=" + DEFAULT_AUTHORITY_NAME + "," + UPDATED_AUTHORITY_NAME);

        // Get all the authorityResourceList where authorityName equals to UPDATED_AUTHORITY_NAME
        defaultAuthorityResourceShouldNotBeFound("authorityName.in=" + UPDATED_AUTHORITY_NAME);
    }

    @Test
    @Transactional
    public void getAllAuthorityResourcesByAuthorityNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        authorityResourceRepository.saveAndFlush(authorityResource);

        // Get all the authorityResourceList where authorityName is not null
        defaultAuthorityResourceShouldBeFound("authorityName.specified=true");

        // Get all the authorityResourceList where authorityName is null
        defaultAuthorityResourceShouldNotBeFound("authorityName.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultAuthorityResourceShouldBeFound(String filter) throws Exception {
        restAuthorityResourceMockMvc.perform(get("/api/authority-resources?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(authorityResource.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].permission").value(hasItem(DEFAULT_PERMISSION)))
            .andExpect(jsonPath("$.[*].authorityName").value(hasItem(DEFAULT_AUTHORITY_NAME.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultAuthorityResourceShouldNotBeFound(String filter) throws Exception {
        restAuthorityResourceMockMvc.perform(get("/api/authority-resources?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingAuthorityResource() throws Exception {
        // Get the authorityResource
        restAuthorityResourceMockMvc.perform(get("/api/authority-resources/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAuthorityResource() throws Exception {
        // Initialize the database
        authorityResourceRepository.saveAndFlush(authorityResource);
        authorityResourceSearchRepository.save(authorityResource);
        int databaseSizeBeforeUpdate = authorityResourceRepository.findAll().size();

        // Update the authorityResource
        AuthorityResource updatedAuthorityResource = authorityResourceRepository.findOne(authorityResource.getId());
        // Disconnect from session so that the updates on updatedAuthorityResource are not directly saved in db
        em.detach(updatedAuthorityResource);
        updatedAuthorityResource
            .name(UPDATED_NAME)
            .permission(UPDATED_PERMISSION)
            .authorityName(UPDATED_AUTHORITY_NAME);
        AuthorityResourceDTO authorityResourceDTO = authorityResourceMapper.toDto(updatedAuthorityResource);

        restAuthorityResourceMockMvc.perform(put("/api/authority-resources")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(authorityResourceDTO)))
            .andExpect(status().isOk());

        // Validate the AuthorityResource in the database
        List<AuthorityResource> authorityResourceList = authorityResourceRepository.findAll();
        assertThat(authorityResourceList).hasSize(databaseSizeBeforeUpdate);
        AuthorityResource testAuthorityResource = authorityResourceList.get(authorityResourceList.size() - 1);
        assertThat(testAuthorityResource.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAuthorityResource.getPermission()).isEqualTo(UPDATED_PERMISSION);
        assertThat(testAuthorityResource.getAuthorityName()).isEqualTo(UPDATED_AUTHORITY_NAME);

        // Validate the AuthorityResource in Elasticsearch
        AuthorityResource authorityResourceEs = authorityResourceSearchRepository.findOne(testAuthorityResource.getId());
        assertThat(authorityResourceEs).isEqualToIgnoringGivenFields(testAuthorityResource);
    }

    @Test
    @Transactional
    public void updateNonExistingAuthorityResource() throws Exception {
        int databaseSizeBeforeUpdate = authorityResourceRepository.findAll().size();

        // Create the AuthorityResource
        AuthorityResourceDTO authorityResourceDTO = authorityResourceMapper.toDto(authorityResource);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restAuthorityResourceMockMvc.perform(put("/api/authority-resources")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(authorityResourceDTO)))
            .andExpect(status().isCreated());

        // Validate the AuthorityResource in the database
        List<AuthorityResource> authorityResourceList = authorityResourceRepository.findAll();
        assertThat(authorityResourceList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteAuthorityResource() throws Exception {
        // Initialize the database
        authorityResourceRepository.saveAndFlush(authorityResource);
        authorityResourceSearchRepository.save(authorityResource);
        int databaseSizeBeforeDelete = authorityResourceRepository.findAll().size();

        // Get the authorityResource
        restAuthorityResourceMockMvc.perform(delete("/api/authority-resources/{id}", authorityResource.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean authorityResourceExistsInEs = authorityResourceSearchRepository.exists(authorityResource.getId());
        assertThat(authorityResourceExistsInEs).isFalse();

        // Validate the database is empty
        List<AuthorityResource> authorityResourceList = authorityResourceRepository.findAll();
        assertThat(authorityResourceList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchAuthorityResource() throws Exception {
        // Initialize the database
        authorityResourceRepository.saveAndFlush(authorityResource);
        authorityResourceSearchRepository.save(authorityResource);

        // Search the authorityResource
        restAuthorityResourceMockMvc.perform(get("/api/_search/authority-resources?query=id:" + authorityResource.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(authorityResource.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].permission").value(hasItem(DEFAULT_PERMISSION)))
            .andExpect(jsonPath("$.[*].authorityName").value(hasItem(DEFAULT_AUTHORITY_NAME.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AuthorityResource.class);
        AuthorityResource authorityResource1 = new AuthorityResource();
        authorityResource1.setId(1L);
        AuthorityResource authorityResource2 = new AuthorityResource();
        authorityResource2.setId(authorityResource1.getId());
        assertThat(authorityResource1).isEqualTo(authorityResource2);
        authorityResource2.setId(2L);
        assertThat(authorityResource1).isNotEqualTo(authorityResource2);
        authorityResource1.setId(null);
        assertThat(authorityResource1).isNotEqualTo(authorityResource2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AuthorityResourceDTO.class);
        AuthorityResourceDTO authorityResourceDTO1 = new AuthorityResourceDTO();
        authorityResourceDTO1.setId(1L);
        AuthorityResourceDTO authorityResourceDTO2 = new AuthorityResourceDTO();
        assertThat(authorityResourceDTO1).isNotEqualTo(authorityResourceDTO2);
        authorityResourceDTO2.setId(authorityResourceDTO1.getId());
        assertThat(authorityResourceDTO1).isEqualTo(authorityResourceDTO2);
        authorityResourceDTO2.setId(2L);
        assertThat(authorityResourceDTO1).isNotEqualTo(authorityResourceDTO2);
        authorityResourceDTO1.setId(null);
        assertThat(authorityResourceDTO1).isNotEqualTo(authorityResourceDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(authorityResourceMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(authorityResourceMapper.fromId(null)).isNull();
    }
}
