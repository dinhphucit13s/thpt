package fpt.dps.dtms.service;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.annotation.JsonIgnore;
import fpt.dps.dtms.domain.*;
import fpt.dps.dtms.repository.*;
import fpt.dps.dtms.repository.search.*;
import org.elasticsearch.indices.IndexAlreadyExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.ManyToMany;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ElasticsearchIndexService {

    private static final Lock reindexLock = new ReentrantLock();

    private final Logger log = LoggerFactory.getLogger(ElasticsearchIndexService.class);

    private final AttachmentsRepository attachmentsRepository;

    private final AttachmentsSearchRepository attachmentsSearchRepository;

    private final AuthorityResourceRepository authorityResourceRepository;

    private final AuthorityResourceSearchRepository authorityResourceSearchRepository;

    private final BugListDefaultRepository bugListDefaultRepository;

    private final BugListDefaultSearchRepository bugListDefaultSearchRepository;

    private final BugsRepository bugsRepository;

    private final BugsSearchRepository bugsSearchRepository;

    private final BusinessLineRepository businessLineRepository;

    private final BusinessLineSearchRepository businessLineSearchRepository;

    private final BusinessUnitRepository businessUnitRepository;

    private final BusinessUnitSearchRepository businessUnitSearchRepository;

    private final BusinessUnitManagerRepository businessUnitManagerRepository;

    private final BusinessUnitManagerSearchRepository businessUnitManagerSearchRepository;

    private final CommentsRepository commentsRepository;

    private final CommentsSearchRepository commentsSearchRepository;

    private final CustomerRepository customerRepository;

    private final CustomerSearchRepository customerSearchRepository;

    private final IssuesRepository issuesRepository;

    private final IssuesSearchRepository issuesSearchRepository;

    private final LoginTrackingRepository loginTrackingRepository;

    private final LoginTrackingSearchRepository loginTrackingSearchRepository;

    private final MailRepository mailRepository;

    private final MailSearchRepository mailSearchRepository;

    private final MailReceiverRepository mailReceiverRepository;

    private final MailReceiverSearchRepository mailReceiverSearchRepository;

    private final NotesRepository notesRepository;

    private final NotesSearchRepository notesSearchRepository;

    private final NotificationRepository notificationRepository;

    private final NotificationSearchRepository notificationSearchRepository;

    private final NotificationTemplateRepository notificationTemplateRepository;

    private final NotificationTemplateSearchRepository notificationTemplateSearchRepository;

    private final PackagesRepository packagesRepository;

    private final PackagesSearchRepository packagesSearchRepository;

    private final ProjectBugListDefaultRepository projectBugListDefaultRepository;

    private final ProjectBugListDefaultSearchRepository projectBugListDefaultSearchRepository;

    private final ProjectsRepository projectsRepository;

    private final ProjectsSearchRepository projectsSearchRepository;

    private final ProjectTemplatesRepository projectTemplatesRepository;

    private final ProjectTemplatesSearchRepository projectTemplatesSearchRepository;

    private final ProjectUsersRepository projectUsersRepository;

    private final ProjectUsersSearchRepository projectUsersSearchRepository;

    private final ProjectWorkflowsRepository projectWorkflowsRepository;

    private final ProjectWorkflowsSearchRepository projectWorkflowsSearchRepository;

    private final PurchaseOrdersRepository purchaseOrdersRepository;

    private final PurchaseOrdersSearchRepository purchaseOrdersSearchRepository;

    private final TasksRepository tasksRepository;

    private final TasksSearchRepository tasksSearchRepository;

    private final TaskTrackingTimeRepository taskTrackingTimeRepository;

    private final TaskTrackingTimeSearchRepository taskTrackingTimeSearchRepository;

    private final TMSCustomFieldRepository tMSCustomFieldRepository;

    private final TMSCustomFieldSearchRepository tMSCustomFieldSearchRepository;

    private final TMSCustomFieldScreenRepository tMSCustomFieldScreenRepository;

    private final TMSCustomFieldScreenSearchRepository tMSCustomFieldScreenSearchRepository;

    private final TMSCustomFieldScreenValueRepository tMSCustomFieldScreenValueRepository;

    private final TMSCustomFieldScreenValueSearchRepository tMSCustomFieldScreenValueSearchRepository;

    private final TMSLogHistoryRepository tMSLogHistoryRepository;

    private final TMSLogHistorySearchRepository tMSLogHistorySearchRepository;

    private final TmsPostRepository tmsPostRepository;

    private final TmsPostSearchRepository tmsPostSearchRepository;

    private final TmsThreadRepository tmsThreadRepository;

    private final TmsThreadSearchRepository tmsThreadSearchRepository;

    private final UserProfileRepository userProfileRepository;

    private final UserProfileSearchRepository userProfileSearchRepository;

    private final UserRepository userRepository;

    private final UserSearchRepository userSearchRepository;

    private final ElasticsearchTemplate elasticsearchTemplate;

    public ElasticsearchIndexService(
        UserRepository userRepository,
        UserSearchRepository userSearchRepository,
        AttachmentsRepository attachmentsRepository,
        AttachmentsSearchRepository attachmentsSearchRepository,
        AuthorityResourceRepository authorityResourceRepository,
        AuthorityResourceSearchRepository authorityResourceSearchRepository,
        BugListDefaultRepository bugListDefaultRepository,
        BugListDefaultSearchRepository bugListDefaultSearchRepository,
        BugsRepository bugsRepository,
        BugsSearchRepository bugsSearchRepository,
        BusinessLineRepository businessLineRepository,
        BusinessLineSearchRepository businessLineSearchRepository,
        BusinessUnitRepository businessUnitRepository,
        BusinessUnitSearchRepository businessUnitSearchRepository,
        BusinessUnitManagerRepository businessUnitManagerRepository,
        BusinessUnitManagerSearchRepository businessUnitManagerSearchRepository,
        CommentsRepository commentsRepository,
        CommentsSearchRepository commentsSearchRepository,
        CustomerRepository customerRepository,
        CustomerSearchRepository customerSearchRepository,
        IssuesRepository issuesRepository,
        IssuesSearchRepository issuesSearchRepository,
        LoginTrackingRepository loginTrackingRepository,
        LoginTrackingSearchRepository loginTrackingSearchRepository,
        MailRepository mailRepository,
        MailSearchRepository mailSearchRepository,
        MailReceiverRepository mailReceiverRepository,
        MailReceiverSearchRepository mailReceiverSearchRepository,
        NotesRepository notesRepository,
        NotesSearchRepository notesSearchRepository,
        NotificationRepository notificationRepository,
        NotificationSearchRepository notificationSearchRepository,
        NotificationTemplateRepository notificationTemplateRepository,
        NotificationTemplateSearchRepository notificationTemplateSearchRepository,
        PackagesRepository packagesRepository,
        PackagesSearchRepository packagesSearchRepository,
        ProjectBugListDefaultRepository projectBugListDefaultRepository,
        ProjectBugListDefaultSearchRepository projectBugListDefaultSearchRepository,
        ProjectsRepository projectsRepository,
        ProjectsSearchRepository projectsSearchRepository,
        ProjectTemplatesRepository projectTemplatesRepository,
        ProjectTemplatesSearchRepository projectTemplatesSearchRepository,
        ProjectUsersRepository projectUsersRepository,
        ProjectUsersSearchRepository projectUsersSearchRepository,
        ProjectWorkflowsRepository projectWorkflowsRepository,
        ProjectWorkflowsSearchRepository projectWorkflowsSearchRepository,
        PurchaseOrdersRepository purchaseOrdersRepository,
        PurchaseOrdersSearchRepository purchaseOrdersSearchRepository,
        TasksRepository tasksRepository,
        TasksSearchRepository tasksSearchRepository,
        TaskTrackingTimeRepository taskTrackingTimeRepository,
        TaskTrackingTimeSearchRepository taskTrackingTimeSearchRepository,
        TMSCustomFieldRepository tMSCustomFieldRepository,
        TMSCustomFieldSearchRepository tMSCustomFieldSearchRepository,
        TMSCustomFieldScreenRepository tMSCustomFieldScreenRepository,
        TMSCustomFieldScreenSearchRepository tMSCustomFieldScreenSearchRepository,
        TMSCustomFieldScreenValueRepository tMSCustomFieldScreenValueRepository,
        TMSCustomFieldScreenValueSearchRepository tMSCustomFieldScreenValueSearchRepository,
        TMSLogHistoryRepository tMSLogHistoryRepository,
        TMSLogHistorySearchRepository tMSLogHistorySearchRepository,
        TmsPostRepository tmsPostRepository,
        TmsPostSearchRepository tmsPostSearchRepository,
        TmsThreadRepository tmsThreadRepository,
        TmsThreadSearchRepository tmsThreadSearchRepository,
        UserProfileRepository userProfileRepository,
        UserProfileSearchRepository userProfileSearchRepository,
        ElasticsearchTemplate elasticsearchTemplate) {
        this.userRepository = userRepository;
        this.userSearchRepository = userSearchRepository;
        this.attachmentsRepository = attachmentsRepository;
        this.attachmentsSearchRepository = attachmentsSearchRepository;
        this.authorityResourceRepository = authorityResourceRepository;
        this.authorityResourceSearchRepository = authorityResourceSearchRepository;
        this.bugListDefaultRepository = bugListDefaultRepository;
        this.bugListDefaultSearchRepository = bugListDefaultSearchRepository;
        this.bugsRepository = bugsRepository;
        this.bugsSearchRepository = bugsSearchRepository;
        this.businessLineRepository = businessLineRepository;
        this.businessLineSearchRepository = businessLineSearchRepository;
        this.businessUnitRepository = businessUnitRepository;
        this.businessUnitSearchRepository = businessUnitSearchRepository;
        this.businessUnitManagerRepository = businessUnitManagerRepository;
        this.businessUnitManagerSearchRepository = businessUnitManagerSearchRepository;
        this.commentsRepository = commentsRepository;
        this.commentsSearchRepository = commentsSearchRepository;
        this.customerRepository = customerRepository;
        this.customerSearchRepository = customerSearchRepository;
        this.issuesRepository = issuesRepository;
        this.issuesSearchRepository = issuesSearchRepository;
        this.loginTrackingRepository = loginTrackingRepository;
        this.loginTrackingSearchRepository = loginTrackingSearchRepository;
        this.mailRepository = mailRepository;
        this.mailSearchRepository = mailSearchRepository;
        this.mailReceiverRepository = mailReceiverRepository;
        this.mailReceiverSearchRepository = mailReceiverSearchRepository;
        this.notesRepository = notesRepository;
        this.notesSearchRepository = notesSearchRepository;
        this.notificationRepository = notificationRepository;
        this.notificationSearchRepository = notificationSearchRepository;
        this.notificationTemplateRepository = notificationTemplateRepository;
        this.notificationTemplateSearchRepository = notificationTemplateSearchRepository;
        this.packagesRepository = packagesRepository;
        this.packagesSearchRepository = packagesSearchRepository;
        this.projectBugListDefaultRepository = projectBugListDefaultRepository;
        this.projectBugListDefaultSearchRepository = projectBugListDefaultSearchRepository;
        this.projectsRepository = projectsRepository;
        this.projectsSearchRepository = projectsSearchRepository;
        this.projectTemplatesRepository = projectTemplatesRepository;
        this.projectTemplatesSearchRepository = projectTemplatesSearchRepository;
        this.projectUsersRepository = projectUsersRepository;
        this.projectUsersSearchRepository = projectUsersSearchRepository;
        this.projectWorkflowsRepository = projectWorkflowsRepository;
        this.projectWorkflowsSearchRepository = projectWorkflowsSearchRepository;
        this.purchaseOrdersRepository = purchaseOrdersRepository;
        this.purchaseOrdersSearchRepository = purchaseOrdersSearchRepository;
        this.tasksRepository = tasksRepository;
        this.tasksSearchRepository = tasksSearchRepository;
        this.taskTrackingTimeRepository = taskTrackingTimeRepository;
        this.taskTrackingTimeSearchRepository = taskTrackingTimeSearchRepository;
        this.tMSCustomFieldRepository = tMSCustomFieldRepository;
        this.tMSCustomFieldSearchRepository = tMSCustomFieldSearchRepository;
        this.tMSCustomFieldScreenRepository = tMSCustomFieldScreenRepository;
        this.tMSCustomFieldScreenSearchRepository = tMSCustomFieldScreenSearchRepository;
        this.tMSCustomFieldScreenValueRepository = tMSCustomFieldScreenValueRepository;
        this.tMSCustomFieldScreenValueSearchRepository = tMSCustomFieldScreenValueSearchRepository;
        this.tMSLogHistoryRepository = tMSLogHistoryRepository;
        this.tMSLogHistorySearchRepository = tMSLogHistorySearchRepository;
        this.tmsPostRepository = tmsPostRepository;
        this.tmsPostSearchRepository = tmsPostSearchRepository;
        this.tmsThreadRepository = tmsThreadRepository;
        this.tmsThreadSearchRepository = tmsThreadSearchRepository;
        this.userProfileRepository = userProfileRepository;
        this.userProfileSearchRepository = userProfileSearchRepository;
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    @Async
    @Timed
    public void reindexAll() {
        if (reindexLock.tryLock()) {
            try {
                reindexForClass(Attachments.class, attachmentsRepository, attachmentsSearchRepository);
                reindexForClass(AuthorityResource.class, authorityResourceRepository, authorityResourceSearchRepository);
                reindexForClass(BugListDefault.class, bugListDefaultRepository, bugListDefaultSearchRepository);
                reindexForClass(Bugs.class, bugsRepository, bugsSearchRepository);
                reindexForClass(BusinessLine.class, businessLineRepository, businessLineSearchRepository);
                reindexForClass(BusinessUnit.class, businessUnitRepository, businessUnitSearchRepository);
                reindexForClass(BusinessUnitManager.class, businessUnitManagerRepository, businessUnitManagerSearchRepository);
                reindexForClass(Comments.class, commentsRepository, commentsSearchRepository);
                reindexForClass(Customer.class, customerRepository, customerSearchRepository);
                reindexForClass(Issues.class, issuesRepository, issuesSearchRepository);
                reindexForClass(LoginTracking.class, loginTrackingRepository, loginTrackingSearchRepository);
                reindexForClass(Mail.class, mailRepository, mailSearchRepository);
                reindexForClass(MailReceiver.class, mailReceiverRepository, mailReceiverSearchRepository);
                reindexForClass(Notes.class, notesRepository, notesSearchRepository);
                reindexForClass(Notification.class, notificationRepository, notificationSearchRepository);
                reindexForClass(NotificationTemplate.class, notificationTemplateRepository, notificationTemplateSearchRepository);
                reindexForClass(Packages.class, packagesRepository, packagesSearchRepository);
                reindexForClass(ProjectBugListDefault.class, projectBugListDefaultRepository, projectBugListDefaultSearchRepository);
                reindexForClass(Projects.class, projectsRepository, projectsSearchRepository);
                reindexForClass(ProjectTemplates.class, projectTemplatesRepository, projectTemplatesSearchRepository);
                reindexForClass(ProjectUsers.class, projectUsersRepository, projectUsersSearchRepository);
                reindexForClass(ProjectWorkflows.class, projectWorkflowsRepository, projectWorkflowsSearchRepository);
                reindexForClass(PurchaseOrders.class, purchaseOrdersRepository, purchaseOrdersSearchRepository);
                reindexForClass(Tasks.class, tasksRepository, tasksSearchRepository);
                reindexForClass(TaskTrackingTime.class, taskTrackingTimeRepository, taskTrackingTimeSearchRepository);
                reindexForClass(TMSCustomField.class, tMSCustomFieldRepository, tMSCustomFieldSearchRepository);
                reindexForClass(TMSCustomFieldScreen.class, tMSCustomFieldScreenRepository, tMSCustomFieldScreenSearchRepository);
                reindexForClass(TMSCustomFieldScreenValue.class, tMSCustomFieldScreenValueRepository, tMSCustomFieldScreenValueSearchRepository);
                reindexForClass(TMSLogHistory.class, tMSLogHistoryRepository, tMSLogHistorySearchRepository);
                reindexForClass(TmsPost.class, tmsPostRepository, tmsPostSearchRepository);
                reindexForClass(TmsThread.class, tmsThreadRepository, tmsThreadSearchRepository);
                reindexForClass(UserProfile.class, userProfileRepository, userProfileSearchRepository);
                reindexForClass(User.class, userRepository, userSearchRepository);

                log.info("Elasticsearch: Successfully performed reindexing");
            } finally {
                reindexLock.unlock();
            }
        } else {
            log.info("Elasticsearch: concurrent reindexing attempt");
        }
    }

    @SuppressWarnings("unchecked")
    private <T, ID extends Serializable> void reindexForClass(Class<T> entityClass, JpaRepository<T, ID> jpaRepository,
                                                              ElasticsearchRepository<T, ID> elasticsearchRepository) {
        elasticsearchTemplate.deleteIndex(entityClass);
        try {
            elasticsearchTemplate.createIndex(entityClass);
        } catch (IndexAlreadyExistsException e) {
            // Do nothing. Index was already concurrently recreated by some other service.
        }
        elasticsearchTemplate.putMapping(entityClass);
        if (jpaRepository.count() > 0) {
            // if a JHipster entity field is the owner side of a many-to-many relationship, it should be loaded manually
            List<Method> relationshipGetters = Arrays.stream(entityClass.getDeclaredFields())
                .filter(field -> field.getType().equals(Set.class))
                .filter(field -> field.getAnnotation(ManyToMany.class) != null)
                .filter(field -> field.getAnnotation(ManyToMany.class).mappedBy().isEmpty())
                .filter(field -> field.getAnnotation(JsonIgnore.class) == null)
                .map(field -> {
                    try {
                        return new PropertyDescriptor(field.getName(), entityClass).getReadMethod();
                    } catch (IntrospectionException e) {
                        log.error("Error retrieving getter for class {}, field {}. Field will NOT be indexed",
                            entityClass.getSimpleName(), field.getName(), e);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

            int size = 100;
            for (int i = 0; i <= jpaRepository.count() / size; i++) {
                Pageable page = new PageRequest(i, size);
                log.info("Indexing page {} of {}, size {}", i, jpaRepository.count() / size, size);
                Page<T> results = jpaRepository.findAll(page);
                results.map(result -> {
                    // if there are any relationships to load, do it now
                    relationshipGetters.forEach(method -> {
                        try {
                            // eagerly load the relationship set
                            ((Set) method.invoke(result)).size();
                        } catch (Exception ex) {
                            log.error(ex.getMessage());
                        }
                    });
                    return result;
                });
                elasticsearchRepository.save(results.getContent());
            }
        }
        log.info("Elasticsearch: Indexed all rows for {}", entityClass.getSimpleName());
    }
}
