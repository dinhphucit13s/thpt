package fpt.dps.dtms.web.rest;

import fpt.dps.dtms.DtmsApp;

import fpt.dps.dtms.domain.Mail;
import fpt.dps.dtms.repository.MailRepository;
import fpt.dps.dtms.repository.search.MailSearchRepository;
import fpt.dps.dtms.service.dto.MailDTO;
import fpt.dps.dtms.service.mapper.MailMapper;
import fpt.dps.dtms.web.rest.errors.ExceptionTranslator;
import fpt.dps.dtms.service.dto.MailCriteria;
import fpt.dps.dtms.service.AttachmentsService;
import fpt.dps.dtms.service.DtmsMailService;
import fpt.dps.dtms.service.IssuesService;
import fpt.dps.dtms.service.MailQueryService;
import fpt.dps.dtms.service.MailReceiverService;
import fpt.dps.dtms.service.StorageService;

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
 * Test class for the MailResource REST controller.
 *
 * @see MailResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DtmsApp.class)
public class MailResourceIntTest {

    private static final String DEFAULT_FROM = "AAAAAAAAAA";
    private static final String UPDATED_FROM = "BBBBBBBBBB";

    private static final String DEFAULT_SUBJECT = "AAAAAAAAAA";
    private static final String UPDATED_SUBJECT = "BBBBBBBBBB";

    private static final String DEFAULT_BODY = "AAAAAAAAAA";
    private static final String UPDATED_BODY = "BBBBBBBBBB";

    private static final Instant DEFAULT_START_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private MailRepository mailRepository;

    @Autowired
    private MailMapper mailMapper;

    @Autowired
    private DtmsMailService mailService;

    @Autowired
    private MailSearchRepository mailSearchRepository;

    @Autowired
    private MailQueryService mailQueryService;
    
    @Autowired
    private IssuesService issuesService;
    
    @Autowired
    private MailReceiverService mailReceiverService;
    

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;
    
    @Autowired
    private StorageService storageService;
    
    @Autowired
    private AttachmentsService attachmentsService;

    @Autowired
    private EntityManager em;

    private MockMvc restMailMockMvc;

    private Mail mail;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MailResource mailResource = new MailResource(mailService, mailQueryService, mailReceiverService, issuesService, attachmentsService, storageService);
        this.restMailMockMvc = MockMvcBuilders.standaloneSetup(mailResource)
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
    public static Mail createEntity(EntityManager em) {
        Mail mail = new Mail()
            .from(DEFAULT_FROM)
            .subject(DEFAULT_SUBJECT)
            .body(DEFAULT_BODY)
            .startTime(DEFAULT_START_TIME)
            .endTime(DEFAULT_END_TIME);
        return mail;
    }

    @Before
    public void initTest() {
        mailSearchRepository.deleteAll();
        mail = createEntity(em);
    }

    @Test
    @Transactional
    public void createMail() throws Exception {
        int databaseSizeBeforeCreate = mailRepository.findAll().size();

        // Create the Mail
        MailDTO mailDTO = mailMapper.toDto(mail);
        restMailMockMvc.perform(post("/api/mail")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mailDTO)))
            .andExpect(status().isCreated());

        // Validate the Mail in the database
        List<Mail> mailList = mailRepository.findAll();
        assertThat(mailList).hasSize(databaseSizeBeforeCreate + 1);
        Mail testMail = mailList.get(mailList.size() - 1);
        assertThat(testMail.getFrom()).isEqualTo(DEFAULT_FROM);
        assertThat(testMail.getSubject()).isEqualTo(DEFAULT_SUBJECT);
        assertThat(testMail.getBody()).isEqualTo(DEFAULT_BODY);
        assertThat(testMail.getStartTime()).isEqualTo(DEFAULT_START_TIME);
        assertThat(testMail.getEndTime()).isEqualTo(DEFAULT_END_TIME);

        // Validate the Mail in Elasticsearch
        Mail mailEs = mailSearchRepository.findOne(testMail.getId());
        assertThat(mailEs).isEqualToIgnoringGivenFields(testMail);
    }

    @Test
    @Transactional
    public void createMailWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = mailRepository.findAll().size();

        // Create the Mail with an existing ID
        mail.setId(1L);
        MailDTO mailDTO = mailMapper.toDto(mail);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMailMockMvc.perform(post("/api/mail")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mailDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Mail in the database
        List<Mail> mailList = mailRepository.findAll();
        assertThat(mailList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkFromIsRequired() throws Exception {
        int databaseSizeBeforeTest = mailRepository.findAll().size();
        // set the field null
        mail.setFrom(null);

        // Create the Mail, which fails.
        MailDTO mailDTO = mailMapper.toDto(mail);

        restMailMockMvc.perform(post("/api/mail")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mailDTO)))
            .andExpect(status().isBadRequest());

        List<Mail> mailList = mailRepository.findAll();
        assertThat(mailList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSubjectIsRequired() throws Exception {
        int databaseSizeBeforeTest = mailRepository.findAll().size();
        // set the field null
        mail.setSubject(null);

        // Create the Mail, which fails.
        MailDTO mailDTO = mailMapper.toDto(mail);

        restMailMockMvc.perform(post("/api/mail")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mailDTO)))
            .andExpect(status().isBadRequest());

        List<Mail> mailList = mailRepository.findAll();
        assertThat(mailList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkBodyIsRequired() throws Exception {
        int databaseSizeBeforeTest = mailRepository.findAll().size();
        // set the field null
        mail.setBody(null);

        // Create the Mail, which fails.
        MailDTO mailDTO = mailMapper.toDto(mail);

        restMailMockMvc.perform(post("/api/mail")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mailDTO)))
            .andExpect(status().isBadRequest());

        List<Mail> mailList = mailRepository.findAll();
        assertThat(mailList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMail() throws Exception {
        // Initialize the database
        mailRepository.saveAndFlush(mail);

        // Get all the mailList
        restMailMockMvc.perform(get("/api/mail?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mail.getId().intValue())))
            .andExpect(jsonPath("$.[*].from").value(hasItem(DEFAULT_FROM.toString())))
            .andExpect(jsonPath("$.[*].subject").value(hasItem(DEFAULT_SUBJECT.toString())))
            .andExpect(jsonPath("$.[*].body").value(hasItem(DEFAULT_BODY.toString())))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME.toString())))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(DEFAULT_END_TIME.toString())));
    }

    @Test
    @Transactional
    public void getMail() throws Exception {
        // Initialize the database
        mailRepository.saveAndFlush(mail);

        // Get the mail
        restMailMockMvc.perform(get("/api/mail/{id}", mail.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(mail.getId().intValue()))
            .andExpect(jsonPath("$.from").value(DEFAULT_FROM.toString()))
            .andExpect(jsonPath("$.subject").value(DEFAULT_SUBJECT.toString()))
            .andExpect(jsonPath("$.body").value(DEFAULT_BODY.toString()))
            .andExpect(jsonPath("$.startTime").value(DEFAULT_START_TIME.toString()))
            .andExpect(jsonPath("$.endTime").value(DEFAULT_END_TIME.toString()));
    }

    @Test
    @Transactional
    public void getAllMailByFromIsEqualToSomething() throws Exception {
        // Initialize the database
        mailRepository.saveAndFlush(mail);

        // Get all the mailList where from equals to DEFAULT_FROM
        defaultMailShouldBeFound("from.equals=" + DEFAULT_FROM);

        // Get all the mailList where from equals to UPDATED_FROM
        defaultMailShouldNotBeFound("from.equals=" + UPDATED_FROM);
    }

    @Test
    @Transactional
    public void getAllMailByFromIsInShouldWork() throws Exception {
        // Initialize the database
        mailRepository.saveAndFlush(mail);

        // Get all the mailList where from in DEFAULT_FROM or UPDATED_FROM
        defaultMailShouldBeFound("from.in=" + DEFAULT_FROM + "," + UPDATED_FROM);

        // Get all the mailList where from equals to UPDATED_FROM
        defaultMailShouldNotBeFound("from.in=" + UPDATED_FROM);
    }

    @Test
    @Transactional
    public void getAllMailByFromIsNullOrNotNull() throws Exception {
        // Initialize the database
        mailRepository.saveAndFlush(mail);

        // Get all the mailList where from is not null
        defaultMailShouldBeFound("from.specified=true");

        // Get all the mailList where from is null
        defaultMailShouldNotBeFound("from.specified=false");
    }

    @Test
    @Transactional
    public void getAllMailBySubjectIsEqualToSomething() throws Exception {
        // Initialize the database
        mailRepository.saveAndFlush(mail);

        // Get all the mailList where subject equals to DEFAULT_SUBJECT
        defaultMailShouldBeFound("subject.equals=" + DEFAULT_SUBJECT);

        // Get all the mailList where subject equals to UPDATED_SUBJECT
        defaultMailShouldNotBeFound("subject.equals=" + UPDATED_SUBJECT);
    }

    @Test
    @Transactional
    public void getAllMailBySubjectIsInShouldWork() throws Exception {
        // Initialize the database
        mailRepository.saveAndFlush(mail);

        // Get all the mailList where subject in DEFAULT_SUBJECT or UPDATED_SUBJECT
        defaultMailShouldBeFound("subject.in=" + DEFAULT_SUBJECT + "," + UPDATED_SUBJECT);

        // Get all the mailList where subject equals to UPDATED_SUBJECT
        defaultMailShouldNotBeFound("subject.in=" + UPDATED_SUBJECT);
    }

    @Test
    @Transactional
    public void getAllMailBySubjectIsNullOrNotNull() throws Exception {
        // Initialize the database
        mailRepository.saveAndFlush(mail);

        // Get all the mailList where subject is not null
        defaultMailShouldBeFound("subject.specified=true");

        // Get all the mailList where subject is null
        defaultMailShouldNotBeFound("subject.specified=false");
    }

    @Test
    @Transactional
    public void getAllMailByBodyIsEqualToSomething() throws Exception {
        // Initialize the database
        mailRepository.saveAndFlush(mail);

        // Get all the mailList where body equals to DEFAULT_BODY
        defaultMailShouldBeFound("body.equals=" + DEFAULT_BODY);

        // Get all the mailList where body equals to UPDATED_BODY
        defaultMailShouldNotBeFound("body.equals=" + UPDATED_BODY);
    }

    @Test
    @Transactional
    public void getAllMailByBodyIsInShouldWork() throws Exception {
        // Initialize the database
        mailRepository.saveAndFlush(mail);

        // Get all the mailList where body in DEFAULT_BODY or UPDATED_BODY
        defaultMailShouldBeFound("body.in=" + DEFAULT_BODY + "," + UPDATED_BODY);

        // Get all the mailList where body equals to UPDATED_BODY
        defaultMailShouldNotBeFound("body.in=" + UPDATED_BODY);
    }

    @Test
    @Transactional
    public void getAllMailByBodyIsNullOrNotNull() throws Exception {
        // Initialize the database
        mailRepository.saveAndFlush(mail);

        // Get all the mailList where body is not null
        defaultMailShouldBeFound("body.specified=true");

        // Get all the mailList where body is null
        defaultMailShouldNotBeFound("body.specified=false");
    }

    @Test
    @Transactional
    public void getAllMailByStartTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        mailRepository.saveAndFlush(mail);

        // Get all the mailList where startTime equals to DEFAULT_START_TIME
        defaultMailShouldBeFound("startTime.equals=" + DEFAULT_START_TIME);

        // Get all the mailList where startTime equals to UPDATED_START_TIME
        defaultMailShouldNotBeFound("startTime.equals=" + UPDATED_START_TIME);
    }

    @Test
    @Transactional
    public void getAllMailByStartTimeIsInShouldWork() throws Exception {
        // Initialize the database
        mailRepository.saveAndFlush(mail);

        // Get all the mailList where startTime in DEFAULT_START_TIME or UPDATED_START_TIME
        defaultMailShouldBeFound("startTime.in=" + DEFAULT_START_TIME + "," + UPDATED_START_TIME);

        // Get all the mailList where startTime equals to UPDATED_START_TIME
        defaultMailShouldNotBeFound("startTime.in=" + UPDATED_START_TIME);
    }

    @Test
    @Transactional
    public void getAllMailByStartTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        mailRepository.saveAndFlush(mail);

        // Get all the mailList where startTime is not null
        defaultMailShouldBeFound("startTime.specified=true");

        // Get all the mailList where startTime is null
        defaultMailShouldNotBeFound("startTime.specified=false");
    }

    @Test
    @Transactional
    public void getAllMailByEndTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        mailRepository.saveAndFlush(mail);

        // Get all the mailList where endTime equals to DEFAULT_END_TIME
        defaultMailShouldBeFound("endTime.equals=" + DEFAULT_END_TIME);

        // Get all the mailList where endTime equals to UPDATED_END_TIME
        defaultMailShouldNotBeFound("endTime.equals=" + UPDATED_END_TIME);
    }

    @Test
    @Transactional
    public void getAllMailByEndTimeIsInShouldWork() throws Exception {
        // Initialize the database
        mailRepository.saveAndFlush(mail);

        // Get all the mailList where endTime in DEFAULT_END_TIME or UPDATED_END_TIME
        defaultMailShouldBeFound("endTime.in=" + DEFAULT_END_TIME + "," + UPDATED_END_TIME);

        // Get all the mailList where endTime equals to UPDATED_END_TIME
        defaultMailShouldNotBeFound("endTime.in=" + UPDATED_END_TIME);
    }

    @Test
    @Transactional
    public void getAllMailByEndTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        mailRepository.saveAndFlush(mail);

        // Get all the mailList where endTime is not null
        defaultMailShouldBeFound("endTime.specified=true");

        // Get all the mailList where endTime is null
        defaultMailShouldNotBeFound("endTime.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultMailShouldBeFound(String filter) throws Exception {
        restMailMockMvc.perform(get("/api/mail?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mail.getId().intValue())))
            .andExpect(jsonPath("$.[*].from").value(hasItem(DEFAULT_FROM.toString())))
            .andExpect(jsonPath("$.[*].subject").value(hasItem(DEFAULT_SUBJECT.toString())))
            .andExpect(jsonPath("$.[*].body").value(hasItem(DEFAULT_BODY.toString())))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME.toString())))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(DEFAULT_END_TIME.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultMailShouldNotBeFound(String filter) throws Exception {
        restMailMockMvc.perform(get("/api/mail?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingMail() throws Exception {
        // Get the mail
        restMailMockMvc.perform(get("/api/mail/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMail() throws Exception {
        // Initialize the database
        mailRepository.saveAndFlush(mail);
        mailSearchRepository.save(mail);
        int databaseSizeBeforeUpdate = mailRepository.findAll().size();

        // Update the mail
        Mail updatedMail = mailRepository.findOne(mail.getId());
        // Disconnect from session so that the updates on updatedMail are not directly saved in db
        em.detach(updatedMail);
        updatedMail
            .from(UPDATED_FROM)
            .subject(UPDATED_SUBJECT)
            .body(UPDATED_BODY)
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME);
        MailDTO mailDTO = mailMapper.toDto(updatedMail);

        restMailMockMvc.perform(put("/api/mail")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mailDTO)))
            .andExpect(status().isOk());

        // Validate the Mail in the database
        List<Mail> mailList = mailRepository.findAll();
        assertThat(mailList).hasSize(databaseSizeBeforeUpdate);
        Mail testMail = mailList.get(mailList.size() - 1);
        assertThat(testMail.getFrom()).isEqualTo(UPDATED_FROM);
        assertThat(testMail.getSubject()).isEqualTo(UPDATED_SUBJECT);
        assertThat(testMail.getBody()).isEqualTo(UPDATED_BODY);
        assertThat(testMail.getStartTime()).isEqualTo(UPDATED_START_TIME);
        assertThat(testMail.getEndTime()).isEqualTo(UPDATED_END_TIME);

        // Validate the Mail in Elasticsearch
        Mail mailEs = mailSearchRepository.findOne(testMail.getId());
        assertThat(mailEs).isEqualToIgnoringGivenFields(testMail);
    }

    @Test
    @Transactional
    public void updateNonExistingMail() throws Exception {
        int databaseSizeBeforeUpdate = mailRepository.findAll().size();

        // Create the Mail
        MailDTO mailDTO = mailMapper.toDto(mail);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restMailMockMvc.perform(put("/api/mail")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mailDTO)))
            .andExpect(status().isCreated());

        // Validate the Mail in the database
        List<Mail> mailList = mailRepository.findAll();
        assertThat(mailList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteMail() throws Exception {
        // Initialize the database
        mailRepository.saveAndFlush(mail);
        mailSearchRepository.save(mail);
        int databaseSizeBeforeDelete = mailRepository.findAll().size();

        // Get the mail
        restMailMockMvc.perform(delete("/api/mail/{id}", mail.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean mailExistsInEs = mailSearchRepository.exists(mail.getId());
        assertThat(mailExistsInEs).isFalse();

        // Validate the database is empty
        List<Mail> mailList = mailRepository.findAll();
        assertThat(mailList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchMail() throws Exception {
        // Initialize the database
        mailRepository.saveAndFlush(mail);
        mailSearchRepository.save(mail);

        // Search the mail
        restMailMockMvc.perform(get("/api/_search/mail?query=id:" + mail.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mail.getId().intValue())))
            .andExpect(jsonPath("$.[*].from").value(hasItem(DEFAULT_FROM.toString())))
            .andExpect(jsonPath("$.[*].subject").value(hasItem(DEFAULT_SUBJECT.toString())))
            .andExpect(jsonPath("$.[*].body").value(hasItem(DEFAULT_BODY.toString())))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME.toString())))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(DEFAULT_END_TIME.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Mail.class);
        Mail mail1 = new Mail();
        mail1.setId(1L);
        Mail mail2 = new Mail();
        mail2.setId(mail1.getId());
        assertThat(mail1).isEqualTo(mail2);
        mail2.setId(2L);
        assertThat(mail1).isNotEqualTo(mail2);
        mail1.setId(null);
        assertThat(mail1).isNotEqualTo(mail2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MailDTO.class);
        MailDTO mailDTO1 = new MailDTO();
        mailDTO1.setId(1L);
        MailDTO mailDTO2 = new MailDTO();
        assertThat(mailDTO1).isNotEqualTo(mailDTO2);
        mailDTO2.setId(mailDTO1.getId());
        assertThat(mailDTO1).isEqualTo(mailDTO2);
        mailDTO2.setId(2L);
        assertThat(mailDTO1).isNotEqualTo(mailDTO2);
        mailDTO1.setId(null);
        assertThat(mailDTO1).isNotEqualTo(mailDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(mailMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(mailMapper.fromId(null)).isNull();
    }
}
