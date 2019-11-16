import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { DtmsAuthorityResourceModule } from './authority-resource/authority-resource.module';
import { DtmsBusinessLineModule } from './business-line/business-line.module';
import { DtmsProjectTemplatesModule } from './project-templates/project-templates.module';
import { DtmsProjectWorkflowsModule } from './project-workflows/project-workflows.module';
import { DtmsProjectsModule } from './projects/projects.module';
import { DtmsProjectUsersModule } from './project-users/project-users.module';
import { DtmsPurchaseOrdersModule } from './purchase-orders/purchase-orders.module';
import { DtmsMembersManagememt } from './members-management/members-management.module';
import { DtmsAllocation } from './allocation/allocation.module';
import { DtmsEffortModule } from './effort/effort.module';
import { DtmsTrackingManagementModule } from './tracking-management/tracking-management.module';
import { DtmsProjectBugListDefaultsModule} from './project-bug-list-defaults/project-bug-list-defaults.module';
import { DtmsPackagesModule } from './packages/packages.module';
import { DtmsTasksModule } from './tasks/tasks.module';
import { DtmsTMSCustomFieldModule } from './tms-custom-field/tms-custom-field.module';
import { DtmsTMSCustomFieldScreenModule } from './tms-custom-field-screen/tms-custom-field-screen.module';
import { DtmsTMSCustomFieldScreenValueModule } from './tms-custom-field-screen-value/tms-custom-field-screen-value.module';
import { DtmsIssuesModule } from './issues/issues.module';
import { DtmsViewFeedBackModule } from './view-feed-back/view-feed-back.module';
import { DtmsTMSLogHistoryModule } from './tms-log-history/tms-log-history.module';
import { DtmsNotesModule } from './notes/notes.module';
import { DtmsTaskTrackingTimeModule } from './task-tracking-time/task-tracking-time.module';
import { DtmsCustomerModule } from './customer/customer.module';
import { DtmsCustomReportsModule } from './custom-reports/custom-reports.module';
import { DtmsBugListDefaultModule } from './bug-list-default/bug-list-default.module';
import { DtmsBugsModule } from './bugs/bugs.module';
import { DtmsAttachmentsModule } from './attachments/attachments.module';
import { DtmsProjectBugListDefaultModule } from './project-bug-list-default/project-bug-list-default.module';
import { DtmsLoginTrackingModule } from './login-tracking/login-tracking.module';
import { DtmsNotificationTemplateModule } from './notification-template/notification-template.module';
import { DtmsNotificationModule } from './notification/notification.module';
import { DtmsMailModule } from './mail/mail.module';
import { DtmsMailReceiverModule } from './mail-receiver/mail-receiver.module';
import { DtmsBusinessUnitModule } from './business-unit/business-unit.module';
import { DtmsBusinessUnitManagerModule } from './business-unit-manager/business-unit-manager.module';
import {DtmsFeedbackModule} from './feedback/feedback.module';
import { DtmsTmsThreadModule } from './tms-thread/tms-thread.module';
import { DtmsTmsPostModule } from './tms-post/tms-post.module';
import { DtmsCommentsModule } from './comments/comments.module';
import { DtmsTaskBiddingModule } from './task-bidding/task-bidding.module';
import { DtmsTaskBiddingTrackingTimeModule } from './task-bidding-tracking-time/task-bidding-tracking-time.module';
import { DtmsDtmsMonitoringModule } from './dtms-monitoring/dtms-monitoring.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        DtmsAuthorityResourceModule,
        DtmsBusinessLineModule,
        DtmsProjectTemplatesModule,
        DtmsProjectWorkflowsModule,
        DtmsProjectsModule,
        DtmsProjectUsersModule,
        DtmsPurchaseOrdersModule,
        DtmsMembersManagememt,
        DtmsAllocation,
        DtmsEffortModule,
        DtmsTrackingManagementModule,
        DtmsProjectBugListDefaultsModule,
        DtmsPackagesModule,
        DtmsTasksModule,
        DtmsTMSCustomFieldModule,
        DtmsTMSCustomFieldScreenModule,
        DtmsTMSCustomFieldScreenValueModule,
        DtmsIssuesModule,
        DtmsViewFeedBackModule,
        DtmsTMSLogHistoryModule,
        DtmsNotesModule,
        DtmsTaskTrackingTimeModule,
        DtmsCustomerModule,
        DtmsCustomReportsModule,
        DtmsBugListDefaultModule,
        DtmsBugsModule,
        DtmsAttachmentsModule,
        DtmsProjectBugListDefaultModule,
        DtmsLoginTrackingModule,
        DtmsNotificationTemplateModule,
        DtmsNotificationModule,
        DtmsMailModule,
        DtmsMailReceiverModule,
        DtmsBusinessUnitModule,
        DtmsBusinessUnitManagerModule,
        DtmsFeedbackModule,
        DtmsTmsThreadModule,
        DtmsTmsPostModule,
        DtmsCommentsModule,
        DtmsTaskBiddingModule,
        DtmsTaskBiddingTrackingTimeModule,
        DtmsDtmsMonitoringModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DtmsEntityModule {}
