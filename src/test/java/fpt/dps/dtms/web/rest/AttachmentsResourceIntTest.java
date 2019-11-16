package fpt.dps.dtms.web.rest;

import fpt.dps.dtms.DtmsApp;

import fpt.dps.dtms.domain.Attachments;
import fpt.dps.dtms.repository.AttachmentsRepository;
import fpt.dps.dtms.service.AttachmentsService;
import fpt.dps.dtms.repository.search.AttachmentsSearchRepository;
import fpt.dps.dtms.service.dto.AttachmentsDTO;
import fpt.dps.dtms.service.mapper.AttachmentsMapper;
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

/**
 * Test class for the AttachmentsResource REST controller.
 *
 * @see AttachmentsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DtmsApp.class)
public class AttachmentsResourceIntTest {

    private static final String DEFAULT_FILENAME = "AAAAAAAAAA";
    private static final String UPDATED_FILENAME = "BBBBBBBBBB";

    private static final String DEFAULT_DISK_FILE = "AAAAAAAAAA";
    private static final String UPDATED_DISK_FILE = "BBBBBBBBBB";

    private static final String DEFAULT_FILE_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_FILE_TYPE = "BBBBBBBBBB";

    @Autowired
    private AttachmentsRepository attachmentsRepository;

    @Autowired
    private AttachmentsMapper attachmentsMapper;

    @Autowired
    private AttachmentsService attachmentsService;

    @Autowired
    private AttachmentsSearchRepository attachmentsSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restAttachmentsMockMvc;

    private Attachments attachments;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AttachmentsResource attachmentsResource = new AttachmentsResource(attachmentsService);
        this.restAttachmentsMockMvc = MockMvcBuilders.standaloneSetup(attachmentsResource)
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
    public static Attachments createEntity(EntityManager em) {
        Attachments attachments = new Attachments()
            .filename(DEFAULT_FILENAME)
            .diskFile(DEFAULT_DISK_FILE)
            .fileType(DEFAULT_FILE_TYPE);
        return attachments;
    }

    @Before
    public void initTest() {
        attachmentsSearchRepository.deleteAll();
        attachments = createEntity(em);
    }

    @Test
    @Transactional
    public void createAttachments() throws Exception {
        int databaseSizeBeforeCreate = attachmentsRepository.findAll().size();

        // Create the Attachments
        AttachmentsDTO attachmentsDTO = attachmentsMapper.toDto(attachments);
        restAttachmentsMockMvc.perform(post("/api/attachments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(attachmentsDTO)))
            .andExpect(status().isCreated());

        // Validate the Attachments in the database
        List<Attachments> attachmentsList = attachmentsRepository.findAll();
        assertThat(attachmentsList).hasSize(databaseSizeBeforeCreate + 1);
        Attachments testAttachments = attachmentsList.get(attachmentsList.size() - 1);
        assertThat(testAttachments.getFilename()).isEqualTo(DEFAULT_FILENAME);
        assertThat(testAttachments.getDiskFile()).isEqualTo(DEFAULT_DISK_FILE);
        assertThat(testAttachments.getFileType()).isEqualTo(DEFAULT_FILE_TYPE);

        // Validate the Attachments in Elasticsearch
        Attachments attachmentsEs = attachmentsSearchRepository.findOne(testAttachments.getId());
        assertThat(attachmentsEs).isEqualToIgnoringGivenFields(testAttachments);
    }

    @Test
    @Transactional
    public void createAttachmentsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = attachmentsRepository.findAll().size();

        // Create the Attachments with an existing ID
        attachments.setId(1L);
        AttachmentsDTO attachmentsDTO = attachmentsMapper.toDto(attachments);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAttachmentsMockMvc.perform(post("/api/attachments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(attachmentsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Attachments in the database
        List<Attachments> attachmentsList = attachmentsRepository.findAll();
        assertThat(attachmentsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkFilenameIsRequired() throws Exception {
        int databaseSizeBeforeTest = attachmentsRepository.findAll().size();
        // set the field null
        attachments.setFilename(null);

        // Create the Attachments, which fails.
        AttachmentsDTO attachmentsDTO = attachmentsMapper.toDto(attachments);

        restAttachmentsMockMvc.perform(post("/api/attachments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(attachmentsDTO)))
            .andExpect(status().isBadRequest());

        List<Attachments> attachmentsList = attachmentsRepository.findAll();
        assertThat(attachmentsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDiskFileIsRequired() throws Exception {
        int databaseSizeBeforeTest = attachmentsRepository.findAll().size();
        // set the field null
        attachments.setDiskFile(null);

        // Create the Attachments, which fails.
        AttachmentsDTO attachmentsDTO = attachmentsMapper.toDto(attachments);

        restAttachmentsMockMvc.perform(post("/api/attachments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(attachmentsDTO)))
            .andExpect(status().isBadRequest());

        List<Attachments> attachmentsList = attachmentsRepository.findAll();
        assertThat(attachmentsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAttachments() throws Exception {
        // Initialize the database
        attachmentsRepository.saveAndFlush(attachments);

        // Get all the attachmentsList
        restAttachmentsMockMvc.perform(get("/api/attachments?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(attachments.getId().intValue())))
            .andExpect(jsonPath("$.[*].filename").value(hasItem(DEFAULT_FILENAME.toString())))
            .andExpect(jsonPath("$.[*].diskFile").value(hasItem(DEFAULT_DISK_FILE.toString())))
            .andExpect(jsonPath("$.[*].fileType").value(hasItem(DEFAULT_FILE_TYPE.toString())));
    }

    @Test
    @Transactional
    public void getAttachments() throws Exception {
        // Initialize the database
        attachmentsRepository.saveAndFlush(attachments);

        // Get the attachments
        restAttachmentsMockMvc.perform(get("/api/attachments/{id}", attachments.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(attachments.getId().intValue()))
            .andExpect(jsonPath("$.filename").value(DEFAULT_FILENAME.toString()))
            .andExpect(jsonPath("$.diskFile").value(DEFAULT_DISK_FILE.toString()))
            .andExpect(jsonPath("$.fileType").value(DEFAULT_FILE_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAttachments() throws Exception {
        // Get the attachments
        restAttachmentsMockMvc.perform(get("/api/attachments/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAttachments() throws Exception {
        // Initialize the database
        attachmentsRepository.saveAndFlush(attachments);
        attachmentsSearchRepository.save(attachments);
        int databaseSizeBeforeUpdate = attachmentsRepository.findAll().size();

        // Update the attachments
        Attachments updatedAttachments = attachmentsRepository.findOne(attachments.getId());
        // Disconnect from session so that the updates on updatedAttachments are not directly saved in db
        em.detach(updatedAttachments);
        updatedAttachments
            .filename(UPDATED_FILENAME)
            .diskFile(UPDATED_DISK_FILE)
            .fileType(UPDATED_FILE_TYPE);
        AttachmentsDTO attachmentsDTO = attachmentsMapper.toDto(updatedAttachments);

        restAttachmentsMockMvc.perform(put("/api/attachments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(attachmentsDTO)))
            .andExpect(status().isOk());

        // Validate the Attachments in the database
        List<Attachments> attachmentsList = attachmentsRepository.findAll();
        assertThat(attachmentsList).hasSize(databaseSizeBeforeUpdate);
        Attachments testAttachments = attachmentsList.get(attachmentsList.size() - 1);
        assertThat(testAttachments.getFilename()).isEqualTo(UPDATED_FILENAME);
        assertThat(testAttachments.getDiskFile()).isEqualTo(UPDATED_DISK_FILE);
        assertThat(testAttachments.getFileType()).isEqualTo(UPDATED_FILE_TYPE);

        // Validate the Attachments in Elasticsearch
        Attachments attachmentsEs = attachmentsSearchRepository.findOne(testAttachments.getId());
        assertThat(attachmentsEs).isEqualToIgnoringGivenFields(testAttachments);
    }

    @Test
    @Transactional
    public void updateNonExistingAttachments() throws Exception {
        int databaseSizeBeforeUpdate = attachmentsRepository.findAll().size();

        // Create the Attachments
        AttachmentsDTO attachmentsDTO = attachmentsMapper.toDto(attachments);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restAttachmentsMockMvc.perform(put("/api/attachments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(attachmentsDTO)))
            .andExpect(status().isCreated());

        // Validate the Attachments in the database
        List<Attachments> attachmentsList = attachmentsRepository.findAll();
        assertThat(attachmentsList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteAttachments() throws Exception {
        // Initialize the database
        attachmentsRepository.saveAndFlush(attachments);
        attachmentsSearchRepository.save(attachments);
        int databaseSizeBeforeDelete = attachmentsRepository.findAll().size();

        // Get the attachments
        restAttachmentsMockMvc.perform(delete("/api/attachments/{id}", attachments.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean attachmentsExistsInEs = attachmentsSearchRepository.exists(attachments.getId());
        assertThat(attachmentsExistsInEs).isFalse();

        // Validate the database is empty
        List<Attachments> attachmentsList = attachmentsRepository.findAll();
        assertThat(attachmentsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchAttachments() throws Exception {
        // Initialize the database
        attachmentsRepository.saveAndFlush(attachments);
        attachmentsSearchRepository.save(attachments);

        // Search the attachments
        restAttachmentsMockMvc.perform(get("/api/_search/attachments?query=id:" + attachments.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(attachments.getId().intValue())))
            .andExpect(jsonPath("$.[*].filename").value(hasItem(DEFAULT_FILENAME.toString())))
            .andExpect(jsonPath("$.[*].diskFile").value(hasItem(DEFAULT_DISK_FILE.toString())))
            .andExpect(jsonPath("$.[*].fileType").value(hasItem(DEFAULT_FILE_TYPE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Attachments.class);
        Attachments attachments1 = new Attachments();
        attachments1.setId(1L);
        Attachments attachments2 = new Attachments();
        attachments2.setId(attachments1.getId());
        assertThat(attachments1).isEqualTo(attachments2);
        attachments2.setId(2L);
        assertThat(attachments1).isNotEqualTo(attachments2);
        attachments1.setId(null);
        assertThat(attachments1).isNotEqualTo(attachments2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AttachmentsDTO.class);
        AttachmentsDTO attachmentsDTO1 = new AttachmentsDTO();
        attachmentsDTO1.setId(1L);
        AttachmentsDTO attachmentsDTO2 = new AttachmentsDTO();
        assertThat(attachmentsDTO1).isNotEqualTo(attachmentsDTO2);
        attachmentsDTO2.setId(attachmentsDTO1.getId());
        assertThat(attachmentsDTO1).isEqualTo(attachmentsDTO2);
        attachmentsDTO2.setId(2L);
        assertThat(attachmentsDTO1).isNotEqualTo(attachmentsDTO2);
        attachmentsDTO1.setId(null);
        assertThat(attachmentsDTO1).isNotEqualTo(attachmentsDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(attachmentsMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(attachmentsMapper.fromId(null)).isNull();
    }
}
