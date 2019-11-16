package fpt.dps.dtms.web.rest;

import fpt.dps.dtms.DtmsApp;

import fpt.dps.dtms.domain.NotificationTemplate;
import fpt.dps.dtms.repository.NotificationTemplateRepository;
import fpt.dps.dtms.service.NotificationTemplateService;
import fpt.dps.dtms.repository.search.NotificationTemplateSearchRepository;
import fpt.dps.dtms.service.dto.NotificationTemplateDTO;
import fpt.dps.dtms.service.mapper.NotificationTemplateMapper;
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

import fpt.dps.dtms.domain.enumeration.NotificationCategory;
/**
 * Test class for the NotificationTemplateResource REST controller.
 *
 * @see NotificationTemplateResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DtmsApp.class)
public class NotificationTemplateResourceIntTest {

    private static final NotificationCategory DEFAULT_TYPE = NotificationCategory.TASK;
    private static final NotificationCategory UPDATED_TYPE = NotificationCategory.TASK;

    private static final String DEFAULT_TEMPLATE = "AAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_TEMPLATE = "BBBBBBBBBBBBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private NotificationTemplateRepository notificationTemplateRepository;

    @Autowired
    private NotificationTemplateMapper notificationTemplateMapper;

    @Autowired
    private NotificationTemplateService notificationTemplateService;

    @Autowired
    private NotificationTemplateSearchRepository notificationTemplateSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restNotificationTemplateMockMvc;

    private NotificationTemplate notificationTemplate;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final NotificationTemplateResource notificationTemplateResource = new NotificationTemplateResource(notificationTemplateService);
        this.restNotificationTemplateMockMvc = MockMvcBuilders.standaloneSetup(notificationTemplateResource)
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
    public static NotificationTemplate createEntity(EntityManager em) {
        NotificationTemplate notificationTemplate = new NotificationTemplate()
            .type(DEFAULT_TYPE)
            .template(DEFAULT_TEMPLATE)
            .description(DEFAULT_DESCRIPTION);
        return notificationTemplate;
    }

    @Before
    public void initTest() {
        notificationTemplateSearchRepository.deleteAll();
        notificationTemplate = createEntity(em);
    }

    @Test
    @Transactional
    public void createNotificationTemplate() throws Exception {
        int databaseSizeBeforeCreate = notificationTemplateRepository.findAll().size();

        // Create the NotificationTemplate
        NotificationTemplateDTO notificationTemplateDTO = notificationTemplateMapper.toDto(notificationTemplate);
        restNotificationTemplateMockMvc.perform(post("/api/notification-templates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(notificationTemplateDTO)))
            .andExpect(status().isCreated());

        // Validate the NotificationTemplate in the database
        List<NotificationTemplate> notificationTemplateList = notificationTemplateRepository.findAll();
        assertThat(notificationTemplateList).hasSize(databaseSizeBeforeCreate + 1);
        NotificationTemplate testNotificationTemplate = notificationTemplateList.get(notificationTemplateList.size() - 1);
        assertThat(testNotificationTemplate.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testNotificationTemplate.getTemplate()).isEqualTo(DEFAULT_TEMPLATE);
        assertThat(testNotificationTemplate.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the NotificationTemplate in Elasticsearch
        NotificationTemplate notificationTemplateEs = notificationTemplateSearchRepository.findOne(testNotificationTemplate.getId());
        assertThat(notificationTemplateEs).isEqualToIgnoringGivenFields(testNotificationTemplate);
    }

    @Test
    @Transactional
    public void createNotificationTemplateWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = notificationTemplateRepository.findAll().size();

        // Create the NotificationTemplate with an existing ID
        notificationTemplate.setId(1L);
        NotificationTemplateDTO notificationTemplateDTO = notificationTemplateMapper.toDto(notificationTemplate);

        // An entity with an existing ID cannot be created, so this API call must fail
        restNotificationTemplateMockMvc.perform(post("/api/notification-templates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(notificationTemplateDTO)))
            .andExpect(status().isBadRequest());

        // Validate the NotificationTemplate in the database
        List<NotificationTemplate> notificationTemplateList = notificationTemplateRepository.findAll();
        assertThat(notificationTemplateList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTemplateIsRequired() throws Exception {
        int databaseSizeBeforeTest = notificationTemplateRepository.findAll().size();
        // set the field null
        notificationTemplate.setTemplate(null);

        // Create the NotificationTemplate, which fails.
        NotificationTemplateDTO notificationTemplateDTO = notificationTemplateMapper.toDto(notificationTemplate);

        restNotificationTemplateMockMvc.perform(post("/api/notification-templates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(notificationTemplateDTO)))
            .andExpect(status().isBadRequest());

        List<NotificationTemplate> notificationTemplateList = notificationTemplateRepository.findAll();
        assertThat(notificationTemplateList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllNotificationTemplates() throws Exception {
        // Initialize the database
        notificationTemplateRepository.saveAndFlush(notificationTemplate);

        // Get all the notificationTemplateList
        restNotificationTemplateMockMvc.perform(get("/api/notification-templates?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(notificationTemplate.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].template").value(hasItem(DEFAULT_TEMPLATE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getNotificationTemplate() throws Exception {
        // Initialize the database
        notificationTemplateRepository.saveAndFlush(notificationTemplate);

        // Get the notificationTemplate
        restNotificationTemplateMockMvc.perform(get("/api/notification-templates/{id}", notificationTemplate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(notificationTemplate.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.template").value(DEFAULT_TEMPLATE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingNotificationTemplate() throws Exception {
        // Get the notificationTemplate
        restNotificationTemplateMockMvc.perform(get("/api/notification-templates/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateNotificationTemplate() throws Exception {
        // Initialize the database
        notificationTemplateRepository.saveAndFlush(notificationTemplate);
        notificationTemplateSearchRepository.save(notificationTemplate);
        int databaseSizeBeforeUpdate = notificationTemplateRepository.findAll().size();

        // Update the notificationTemplate
        NotificationTemplate updatedNotificationTemplate = notificationTemplateRepository.findOne(notificationTemplate.getId());
        // Disconnect from session so that the updates on updatedNotificationTemplate are not directly saved in db
        em.detach(updatedNotificationTemplate);
        updatedNotificationTemplate
            .type(UPDATED_TYPE)
            .template(UPDATED_TEMPLATE)
            .description(UPDATED_DESCRIPTION);
        NotificationTemplateDTO notificationTemplateDTO = notificationTemplateMapper.toDto(updatedNotificationTemplate);

        restNotificationTemplateMockMvc.perform(put("/api/notification-templates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(notificationTemplateDTO)))
            .andExpect(status().isOk());

        // Validate the NotificationTemplate in the database
        List<NotificationTemplate> notificationTemplateList = notificationTemplateRepository.findAll();
        assertThat(notificationTemplateList).hasSize(databaseSizeBeforeUpdate);
        NotificationTemplate testNotificationTemplate = notificationTemplateList.get(notificationTemplateList.size() - 1);
        assertThat(testNotificationTemplate.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testNotificationTemplate.getTemplate()).isEqualTo(UPDATED_TEMPLATE);
        assertThat(testNotificationTemplate.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the NotificationTemplate in Elasticsearch
        NotificationTemplate notificationTemplateEs = notificationTemplateSearchRepository.findOne(testNotificationTemplate.getId());
        assertThat(notificationTemplateEs).isEqualToIgnoringGivenFields(testNotificationTemplate);
    }

    @Test
    @Transactional
    public void updateNonExistingNotificationTemplate() throws Exception {
        int databaseSizeBeforeUpdate = notificationTemplateRepository.findAll().size();

        // Create the NotificationTemplate
        NotificationTemplateDTO notificationTemplateDTO = notificationTemplateMapper.toDto(notificationTemplate);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restNotificationTemplateMockMvc.perform(put("/api/notification-templates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(notificationTemplateDTO)))
            .andExpect(status().isCreated());

        // Validate the NotificationTemplate in the database
        List<NotificationTemplate> notificationTemplateList = notificationTemplateRepository.findAll();
        assertThat(notificationTemplateList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteNotificationTemplate() throws Exception {
        // Initialize the database
        notificationTemplateRepository.saveAndFlush(notificationTemplate);
        notificationTemplateSearchRepository.save(notificationTemplate);
        int databaseSizeBeforeDelete = notificationTemplateRepository.findAll().size();

        // Get the notificationTemplate
        restNotificationTemplateMockMvc.perform(delete("/api/notification-templates/{id}", notificationTemplate.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean notificationTemplateExistsInEs = notificationTemplateSearchRepository.exists(notificationTemplate.getId());
        assertThat(notificationTemplateExistsInEs).isFalse();

        // Validate the database is empty
        List<NotificationTemplate> notificationTemplateList = notificationTemplateRepository.findAll();
        assertThat(notificationTemplateList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchNotificationTemplate() throws Exception {
        // Initialize the database
        notificationTemplateRepository.saveAndFlush(notificationTemplate);
        notificationTemplateSearchRepository.save(notificationTemplate);

        // Search the notificationTemplate
        restNotificationTemplateMockMvc.perform(get("/api/_search/notification-templates?query=id:" + notificationTemplate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(notificationTemplate.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].template").value(hasItem(DEFAULT_TEMPLATE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NotificationTemplate.class);
        NotificationTemplate notificationTemplate1 = new NotificationTemplate();
        notificationTemplate1.setId(1L);
        NotificationTemplate notificationTemplate2 = new NotificationTemplate();
        notificationTemplate2.setId(notificationTemplate1.getId());
        assertThat(notificationTemplate1).isEqualTo(notificationTemplate2);
        notificationTemplate2.setId(2L);
        assertThat(notificationTemplate1).isNotEqualTo(notificationTemplate2);
        notificationTemplate1.setId(null);
        assertThat(notificationTemplate1).isNotEqualTo(notificationTemplate2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NotificationTemplateDTO.class);
        NotificationTemplateDTO notificationTemplateDTO1 = new NotificationTemplateDTO();
        notificationTemplateDTO1.setId(1L);
        NotificationTemplateDTO notificationTemplateDTO2 = new NotificationTemplateDTO();
        assertThat(notificationTemplateDTO1).isNotEqualTo(notificationTemplateDTO2);
        notificationTemplateDTO2.setId(notificationTemplateDTO1.getId());
        assertThat(notificationTemplateDTO1).isEqualTo(notificationTemplateDTO2);
        notificationTemplateDTO2.setId(2L);
        assertThat(notificationTemplateDTO1).isNotEqualTo(notificationTemplateDTO2);
        notificationTemplateDTO1.setId(null);
        assertThat(notificationTemplateDTO1).isNotEqualTo(notificationTemplateDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(notificationTemplateMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(notificationTemplateMapper.fromId(null)).isNull();
    }
}
