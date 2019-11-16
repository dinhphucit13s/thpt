import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiTrackerService } from './../shared/tracker/tracker.service';
import {CommonModule, DatePipe} from '@angular/common';
import { OwlDateTimeModule, OwlNativeDateTimeModule} from 'ng-pick-datetime';
import { DtmsSharedModule } from '../shared';
import { EntityAuditModule } from './entity-audit/entity-audit.module';
import { DtmsElasticsearchReindexModule } from './elasticsearch-reindex/elasticsearch-reindex.module';
/* jhipster-needle-add-admin-module-import - JHipster will add admin modules imports here */
import { FormsModule } from '@angular/forms';
import { FlatpickrModule} from 'angularx-flatpickr';
import { TimeZoneService } from './user-management/timezones.service';
import {
    adminState,
    AuditsComponent,
    UserMgmtComponent,
    UserDialogComponent,
    UserDeleteDialogComponent,
    UserMgmtDetailComponent,
    UserMgmtDialogComponent,
    UserMgmtDeleteDialogComponent,
    UserMgmtImportDialogComponent,
    LogsComponent,
    JhiMetricsMonitoringModalComponent,
    JhiMetricsMonitoringComponent,
    JhiHealthModalComponent,
    JhiHealthCheckComponent,
    JhiConfigurationComponent,
    JhiDocsComponent,
    AuditsService,
    JhiConfigurationService,
    JhiHealthService,
    JhiMetricsService,
    JhiTrackerComponent,
    LogsService,
    UserResolvePagingParams,
    UserResolve,
    UserModalService
} from './';
import {UserMgmtImportPopupComponent} from './user-management/user-management-import-dialog.component';
import {UserManagementResetPassDialogComponent} from './user-management/user-management-reset-pass';
import {UserManagementResetComponent} from './user-management/user-management-reset-pass';
@NgModule({
    imports: [
        FormsModule,
        FlatpickrModule.forRoot(),
        OwlDateTimeModule,
        OwlNativeDateTimeModule,
        DtmsSharedModule,
        RouterModule.forChild(adminState),
        EntityAuditModule,
        DtmsElasticsearchReindexModule,
        /* jhipster-needle-add-admin-module - JHipster will add admin modules here */
    ],
    exports: [
        DatePipe
    ],
    declarations: [
        AuditsComponent,
        UserMgmtComponent,
        UserManagementResetPassDialogComponent,
        UserDialogComponent,
        UserManagementResetComponent,
        UserDeleteDialogComponent,
        UserMgmtDetailComponent,
        UserMgmtDialogComponent,
        UserMgmtDeleteDialogComponent,
        UserMgmtImportPopupComponent,
        UserMgmtImportDialogComponent,
        LogsComponent,
        JhiConfigurationComponent,
        JhiHealthCheckComponent,
        JhiHealthModalComponent,
        JhiDocsComponent,
        JhiTrackerComponent,
        JhiMetricsMonitoringComponent,
        JhiMetricsMonitoringModalComponent
    ],
    entryComponents: [
        UserManagementResetPassDialogComponent,
        UserMgmtDialogComponent,
        UserMgmtDeleteDialogComponent,
        UserMgmtImportPopupComponent,
        UserMgmtImportDialogComponent,
        JhiHealthModalComponent,
        JhiMetricsMonitoringModalComponent,
    ],
    providers: [
        AuditsService,
        JhiConfigurationService,
        JhiHealthService,
        JhiMetricsService,
        LogsService,
        JhiTrackerService,
        UserResolvePagingParams,
        UserResolve,
        UserModalService,
        TimeZoneService,
        DatePipe
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DtmsAdminModule { }
