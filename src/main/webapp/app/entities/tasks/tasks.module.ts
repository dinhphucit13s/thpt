import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { MatToolbarModule , MatInputModule , MatTableModule, MatButtonModule, MatPaginatorModule, MatSortModule, MatFormFieldModule
    , MatCheckboxModule, MatSelectModule, MatOptionModule} from '@angular/material';
import {MatIconModule} from '@angular/material/icon';
import { DtmsSharedModule } from '../../shared';
import {DynamicFormComponent} from '../../shared/dynamic-forms/dynamic-form/dynamic-form.component';
import { AgGridModule } from 'ag-grid-angular';
import { ButtonViewComponent } from '../../shared/dynamic-data-tables/buttons-view/button-view.component';
import { DtmsBugsModule } from '../bugs/bugs.module';
import { DtmsSharedBugsModule } from '../reuse/shared-reuse.module';
import {FlatpickrModule} from 'angularx-flatpickr';
import {
    TasksService,
    TasksPopupService,
    TasksComponent,
    TasksDetailComponent,
    TasksDialogComponent,
    TasksPopupComponent,
    TasksDeletePopupComponent,
    TasksDeleteDialogComponent,
    tasksRoute,
    tasksPopupRoute,
    TasksResolvePagingParams,
    TasksImportPopupComponent,
    TasksImportDialogComponent,
} from './';

/*Import TAsk Move Dialog
PhuVD3*/
import { TasksMoveDialogComponent } from './tasks-move-dialog.component';

import { TasksDetailReviewerComponent } from './tasks-detail-reviewer/tasks-detail-reviewer.component';
import { TasksDetailReviewerContentComponent} from './tasks-detail-reviewer/tasks-detail-reviewer-content/tasks-detail-reviewer-content.component';
import { TasksDetailShowAttachmentComponent } from './tasks-detail-show-attachment/tasks-detail-show-attachment.component';

const ENTITY_STATES = [
    ...tasksRoute,
    ...tasksPopupRoute,
];

@NgModule({
    imports: [
        DtmsSharedModule,
        MatToolbarModule,
        MatInputModule,
        MatTableModule,
        MatButtonModule,
        MatPaginatorModule,
        MatSortModule,
        MatFormFieldModule,
        MatCheckboxModule,
        MatSelectModule,
        MatOptionModule,
        MatIconModule,
        AgGridModule.withComponents([ButtonViewComponent]),
        RouterModule.forChild(ENTITY_STATES),
        DtmsBugsModule,
        DtmsSharedBugsModule,
        FlatpickrModule.forRoot()
    ],
    declarations: [
        TasksComponent,
        TasksDetailComponent,
        TasksDialogComponent,
        TasksDeleteDialogComponent,
        TasksPopupComponent,
        TasksDeletePopupComponent,
        TasksImportPopupComponent,
        TasksImportDialogComponent,
        /*Declarations TasksMovePopupComponent and TasksMoveDialogComponent
        PhuVD3*/
        TasksMoveDialogComponent,

        TasksDetailReviewerComponent,
        TasksDetailReviewerContentComponent,
        TasksDetailShowAttachmentComponent
    ],
    entryComponents: [
        TasksComponent,
        TasksDialogComponent,
        TasksPopupComponent,
        TasksDeleteDialogComponent,
        TasksDeletePopupComponent,
        /*Declarations TasksMovePopupComponent and TasksMoveDialogComponent
        PhuVD3*/
        TasksMoveDialogComponent,

        DynamicFormComponent,
        TasksImportPopupComponent,
        TasksImportDialogComponent,
        TasksDetailShowAttachmentComponent
    ],
    providers: [
        TasksService,
        TasksPopupService,
        TasksResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DtmsTasksModule {}
