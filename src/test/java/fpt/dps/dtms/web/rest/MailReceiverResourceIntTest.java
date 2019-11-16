package fpt.dps.dtms.web.rest;

import fpt.dps.dtms.DtmsApp;

import fpt.dps.dtms.domain.MailReceiver;
import fpt.dps.dtms.domain.Mail;
import fpt.dps.dtms.repository.MailReceiverRepository;
import fpt.dps.dtms.service.MailReceiverService;
import fpt.dps.dtms.repository.search.MailReceiverSearchRepository;
import fpt.dps.dtms.service.dto.MailReceiverDTO;
import fpt.dps.dtms.service.mapper.MailReceiverMapper;
import fpt.dps.dtms.web.rest.errors.ExceptionTranslator;
import fpt.dps.dtms.service.dto.MailReceiverCriteria;
import fpt.dps.dtms.service.MailReceiverQueryService;

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
 * Test class for the MailReceiverResource REST controller.
 *
 * @see MailReceiverResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DtmsApp.class)
public class MailReceiverResourceIntTest {

    private static final String DEFAULT_FROM = "AAAAAAAAAA";
    private static final String UPDATED_FROM = "BBBBBBBBBB";

    private static final String DEFAULT_TO = "AAAAAAAAAA";
    private static final String UPDATED_TO = "BBBBBBBBBB";

    private static final Boolean DEFAULT_STATUS = false;
    private static final Boolean UPDATED_STATUS = true;

    @Autowired
    private MailReceiverRepository mailReceiverRepository;

    @Autowired
    private MailReceiverMapper mailReceiverMapper;

    @Autowired
    private MailReceiverService mailReceiverService;

    @Autowired
    private MailReceiverSearchRepository mailReceiverSearchRepository;

    @Autowired
    private MailReceiverQueryService mailReceiverQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restMailReceiverMockMvc;

    private MailReceiver mailReceiver;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MailReceiverResource mailReceiverResource = new MailReceiverResource(mailReceiverService, mailReceiverQueryService);
        this.restMailReceiverMockMvc = MockMvcBuilders.standaloneSetup(mailReceiverResource)
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
    public static MailReceiver createEntity(EntityManager em) {
        MailReceiver mailReceiver = new MailReceiver()
            .from(DEFAULT_FROM)
            .to(DEFAULT_TO)
            .status(DEFAULT_STATUS);
        // Add required entity
        Mail mail = MailResourceIntTest.createEntity(em);
        em.persist(mail);
        em.flush();
        mailReceiver.setMail(mail);
        return mailReceiver;
    }

    @Before
    public void initTest() {
        mailReceiverSearchRepository.deleteAll();
        mailReceiver = createEntity(em);
    }

    @Test
    @Transactional
    public void createMailReceiver() throws Exception {
        int databaseSizeBeforeCreate = mailReceiverRepository.findAll().size();

        // Create the MailReceiver
        MailReceiverDTO mailReceiverDTO = mailReceiverMapper.toDto(mailReceiver);
        restMailReceiverMockMvc.perform(post("/api/mail-receivers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mailReceiverDTO)))
            .andExpect(status().isCreated());

        // Validate the MailReceiver in the database
        List<MailReceiver> mailReceiverList = mailReceiverRepository.findAll();
        assertThat(mailReceiverList).hasSize(databaseSizeBeforeCreate + 1);
        MailReceiver testMailReceiver = mailReceiverList.get(mailReceiverList.size() - 1);
        assertThat(testMailReceiver.getFrom()).isEqualTo(DEFAULT_FROM);
        assertThat(testMailReceiver.getTo()).isEqualTo(DEFAULT_TO);
        assertThat(testMailReceiver.isStatus()).isEqualTo(DEFAULT_STATUS);

        // Validate the MailReceiver in Elasticsearch
        MailReceiver mailReceiverEs = mailReceiverSearchRepository.findOne(testMailReceiver.getId());
        assertThat(mailReceiverEs).isEqualToIgnoringGivenFields(testMailReceiver);
    }

    @Test
    @Transactional
    public void createMailReceiverWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = mailReceiverRepository.findAll().size();

        // Create the MailReceiver with an existing ID
        mailReceiver.setId(1L);
        MailReceiverDTO mailReceiverDTO = mailReceiverMapper.toDto(mailReceiver);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMailReceiverMockMvc.perform(post("/api/mail-receivers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mailReceiverDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MailReceiver in the database
        List<MailReceiver> mailReceiverList = mailReceiverRepository.findAll();
        assertThat(mailReceiverList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = mailReceiverRepository.findAll().size();
        // set the field null
        mailReceiver.setStatus(null);

        // Create the MailReceiver, which fails.
        MailReceiverDTO mailReceiverDTO = mailReceiverMapper.toDto(mailReceiver);

        restMailReceiverMockMvc.perform(post("/api/mail-receivers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mailReceiverDTO)))
            .andExpect(status().isBadRequest());

        List<MailReceiver> mailReceiverList = mailReceiverRepository.findAll();
        assertThat(mailReceiverList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMailReceivers() throws Exception {
        // Initialize the database
        mailReceiverRepository.saveAndFlush(mailReceiver);

        // Get all the mailReceiverList
        restMailReceiverMockMvc.perform(get("/api/mail-receivers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mailReceiver.getId().intValue())))
            .andExpect(jsonPath("$.[*].from").value(hasItem(DEFAULT_FROM.toString())))
            .andExpect(jsonPath("$.[*].to").value(hasItem(DEFAULT_TO.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.booleanValue())));
    }

    @Test
    @Transactional
    public void getMailReceiver() throws Exception {
        // Initialize the database
        mailReceiverRepository.saveAndFlush(mailReceiver);

        // Get the mailReceiver
        restMailReceiverMockMvc.perform(get("/api/mail-receivers/{id}", mailReceiver.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(mailReceiver.getId().intValue()))
            .andExpect(jsonPath("$.from").value(DEFAULT_FROM.toString()))
            .andExpect(jsonPath("$.to").value(DEFAULT_TO.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.booleanValue()));
    }

    @Test
    @Transactional
    public void getAllMailReceiversByFromIsEqualToSomething() throws Exception {
        // Initialize the database
        mailReceiverRepository.saveAndFlush(mailReceiver);

        // Get all the mailReceiverList where from equals to DEFAULT_FROM
        defaultMailReceiverShouldBeFound("from.equals=" + DEFAULT_FROM);

        // Get all the mailReceiverList where from equals to UPDATED_FROM
        defaultMailReceiverShouldNotBeFound("from.equals=" + UPDATED_FROM);
    }

    @Test
    @Transactional
    public void getAllMailReceiversByFromIsInShouldWork() throws Exception {
        // Initialize the database
        mailReceiverRepository.saveAndFlush(mailReceiver);

        // Get all the mailReceiverList where from in DEFAULT_FROM or UPDATED_FROM
        defaultMailReceiverShouldBeFound("from.in=" + DEFAULT_FROM + "," + UPDATED_FROM);

        // Get all the mailReceiverList where from equals to UPDATED_FROM
        defaultMailReceiverShouldNotBeFound("from.in=" + UPDATED_FROM);
    }

    @Test
    @Transactional
    public void getAllMailReceiversByFromIsNullOrNotNull() throws Exception {
        // Initialize the database
        mailReceiverRepository.saveAndFlush(mailReceiver);

        // Get all the mailReceiverList where from is not null
        defaultMailReceiverShouldBeFound("from.specified=true");

        // Get all the mailReceiverList where from is null
        defaultMailReceiverShouldNotBeFound("from.specified=false");
    }

    @Test
    @Transactional
    public void getAllMailReceiversByToIsEqualToSomething() throws Exception {
        // Initialize the database
        mailReceiverRepository.saveAndFlush(mailReceiver);

        // Get all the mailReceiverList where to equals to DEFAULT_TO
        defaultMailReceiverShouldBeFound("to.equals=" + DEFAULT_TO);

        // Get all the mailReceiverList where to equals to UPDATED_TO
        defaultMailReceiverShouldNotBeFound("to.equals=" + UPDATED_TO);
    }

    @Test
    @Transactional
    public void getAllMailReceiversByToIsInShouldWork() throws Exception {
        // Initialize the database
        mailReceiverRepository.saveAndFlush(mailReceiver);

        // Get all the mailReceiverList where to in DEFAULT_TO or UPDATED_TO
        defaultMailReceiverShouldBeFound("to.in=" + DEFAULT_TO + "," + UPDATED_TO);

        // Get all the mailReceiverList where to equals to UPDATED_TO
        defaultMailReceiverShouldNotBeFound("to.in=" + UPDATED_TO);
    }

    @Test
    @Transactional
    public void getAllMailReceiversByToIsNullOrNotNull() throws Exception {
        // Initialize the database
        mailReceiverRepository.saveAndFlush(mailReceiver);

        // Get all the mailReceiverList where to is not null
        defaultMailReceiverShouldBeFound("to.specified=true");

        // Get all the mailReceiverList where to is null
        defaultMailReceiverShouldNotBeFound("to.specified=false");
    }

    @Test
    @Transactional
    public void getAllMailReceiversByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        mailReceiverRepository.saveAndFlush(mailReceiver);

        // Get all the mailReceiverList where status equals to DEFAULT_STATUS
        defaultMailReceiverShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the mailReceiverList where status equals to UPDATED_STATUS
        defaultMailReceiverShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllMailReceiversByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        mailReceiverRepository.saveAndFlush(mailReceiver);

        // Get all the mailReceiverList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultMailReceiverShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the mailReceiverList where status equals to UPDATED_STATUS
        defaultMailReceiverShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllMailReceiversByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        mailReceiverRepository.saveAndFlush(mailReceiver);

        // Get all the mailReceiverList where status is not null
        defaultMailReceiverShouldBeFound("status.specified=true");

        // Get all the mailReceiverList where status is null
        defaultMailReceiverShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    public void getAllMailReceiversByMailIsEqualToSomething() throws Exception {
        // Initialize the database
        Mail mail = MailResourceIntTest.createEntity(em);
        em.persist(mail);
        em.flush();
        mailReceiver.setMail(mail);
        mailReceiverRepository.saveAndFlush(mailReceiver);
        Long mailId = mail.getId();

        // Get all the mailReceiverList where mail equals to mailId
        defaultMailReceiverShouldBeFound("mailId.equals=" + mailId);

        // Get all the mailReceiverList where mail equals to mailId + 1
        defaultMailReceiverShouldNotBeFound("mailId.equals=" + (mailId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultMailReceiverShouldBeFound(String filter) throws Exception {
        restMailReceiverMockMvc.perform(get("/api/mail-receivers?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mailReceiver.getId().intValue())))
            .andExpect(jsonPath("$.[*].from").value(hasItem(DEFAULT_FROM.toString())))
            .andExpect(jsonPath("$.[*].to").value(hasItem(DEFAULT_TO.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.booleanValue())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultMailReceiverShouldNotBeFound(String filter) throws Exception {
        restMailReceiverMockMvc.perform(get("/api/mail-receivers?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingMailReceiver() throws Exception {
        // Get the mailReceiver
        restMailReceiverMockMvc.perform(get("/api/mail-receivers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMailReceiver() throws Exception {
        // Initialize the database
        mailReceiverRepository.saveAndFlush(mailReceiver);
        mailReceiverSearchRepository.save(mailReceiver);
        int databaseSizeBeforeUpdate = mailReceiverRepository.findAll().size();

        // Update the mailReceiver
        MailReceiver updatedMailReceiver = mailReceiverRepository.findOne(mailReceiver.getId());
        // Disconnect from session so that the updates on updatedMailReceiver are not directly saved in db
        em.detach(updatedMailReceiver);
        updatedMailReceiver
            .from(UPDATED_FROM)
            .to(UPDATED_TO)
            .status(UPDATED_STATUS);
        MailReceiverDTO mailReceiverDTO = mailReceiverMapper.toDto(updatedMailReceiver);

        restMailReceiverMockMvc.perform(put("/api/mail-receivers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mailReceiverDTO)))
            .andExpect(status().isOk());

        // Validate the MailReceiver in the database
        List<MailReceiver> mailReceiverList = mailReceiverRepository.findAll();
        assertThat(mailReceiverList).hasSize(databaseSizeBeforeUpdate);
        MailReceiver testMailReceiver = mailReceiverList.get(mailReceiverList.size() - 1);
        assertThat(testMailReceiver.getFrom()).isEqualTo(UPDATED_FROM);
        assertThat(testMailReceiver.getTo()).isEqualTo(UPDATED_TO);
        assertThat(testMailReceiver.isStatus()).isEqualTo(UPDATED_STATUS);

        // Validate the MailReceiver in Elasticsearch
        MailReceiver mailReceiverEs = mailReceiverSearchRepository.findOne(testMailReceiver.getId());
        assertThat(mailReceiverEs).isEqualToIgnoringGivenFields(testMailReceiver);
    }

    @Test
    @Transactional
    public void updateNonExistingMailReceiver() throws Exception {
        int databaseSizeBeforeUpdate = mailReceiverRepository.findAll().size();

        // Create the MailReceiver
        MailReceiverDTO mailReceiverDTO = mailReceiverMapper.toDto(mailReceiver);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restMailReceiverMockMvc.perform(put("/api/mail-receivers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mailReceiverDTO)))
            .andExpect(status().isCreated());

        // Validate the MailReceiver in the database
        List<MailReceiver> mailReceiverList = mailReceiverRepository.findAll();
        assertThat(mailReceiverList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteMailReceiver() throws Exception {
        // Initialize the database
        mailReceiverRepository.saveAndFlush(mailReceiver);
        mailReceiverSearchRepository.save(mailReceiver);
        int databaseSizeBeforeDelete = mailReceiverRepository.findAll().size();

        // Get the mailReceiver
        restMailReceiverMockMvc.perform(delete("/api/mail-receivers/{id}", mailReceiver.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean mailReceiverExistsInEs = mailReceiverSearchRepository.exists(mailReceiver.getId());
        assertThat(mailReceiverExistsInEs).isFalse();

        // Validate the database is empty
        List<MailReceiver> mailReceiverList = mailReceiverRepository.findAll();
        assertThat(mailReceiverList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchMailReceiver() throws Exception {
        // Initialize the database
        mailReceiverRepository.saveAndFlush(mailReceiver);
        mailReceiverSearchRepository.save(mailReceiver);

        // Search the mailReceiver
        restMailReceiverMockMvc.perform(get("/api/_search/mail-receivers?query=id:" + mailReceiver.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mailReceiver.getId().intValue())))
            .andExpect(jsonPath("$.[*].from").value(hasItem(DEFAULT_FROM.toString())))
            .andExpect(jsonPath("$.[*].to").value(hasItem(DEFAULT_TO.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.booleanValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MailReceiver.class);
        MailReceiver mailReceiver1 = new MailReceiver();
        mailReceiver1.setId(1L);
        MailReceiver mailReceiver2 = new MailReceiver();
        mailReceiver2.setId(mailReceiver1.getId());
        assertThat(mailReceiver1).isEqualTo(mailReceiver2);
        mailReceiver2.setId(2L);
        assertThat(mailReceiver1).isNotEqualTo(mailReceiver2);
        mailReceiver1.setId(null);
        assertThat(mailReceiver1).isNotEqualTo(mailReceiver2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MailReceiverDTO.class);
        MailReceiverDTO mailReceiverDTO1 = new MailReceiverDTO();
        mailReceiverDTO1.setId(1L);
        MailReceiverDTO mailReceiverDTO2 = new MailReceiverDTO();
        assertThat(mailReceiverDTO1).isNotEqualTo(mailReceiverDTO2);
        mailReceiverDTO2.setId(mailReceiverDTO1.getId());
        assertThat(mailReceiverDTO1).isEqualTo(mailReceiverDTO2);
        mailReceiverDTO2.setId(2L);
        assertThat(mailReceiverDTO1).isNotEqualTo(mailReceiverDTO2);
        mailReceiverDTO1.setId(null);
        assertThat(mailReceiverDTO1).isNotEqualTo(mailReceiverDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(mailReceiverMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(mailReceiverMapper.fromId(null)).isNull();
    }
}
