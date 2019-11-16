import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DtmsSharedModule } from '../../shared';
import {
    TaskBiddingService,
    TaskBiddingPopupService,
    TaskBiddingComponent,
    TaskBiddingDetailComponent,
    TaskBiddingDialogComponent,
    TaskBiddingPopupComponent,
    TaskBiddingDeletePopupComponent,
    TaskBiddingDeleteDialogComponent,
    taskBiddingRoute,
    taskBiddingPopupRoute,
    TaskBiddingResolvePagingParams,
} from './';

import {
    TaskBiddingClassifyComponent
} from './task-bidding-classify/task-bidding-classify.component';
import {TaskBiddingClassifyDialogUnbiddingComponent, TaskBiddingClassifyPopupUnbiddingComponent} from './task-bidding-classify/task-bidding-classify-dialog-unbidding.component';
import {TaskBiddingAssignDialogComponent, TaskBiddingAssignPopupDialogComponent} from './task-bidding-assign-dialog.component';
import {TaskBiddingUnAssignComponent} from './task-bidding-un-assign/task-bidding-un-assign.component';

const ENTITY_STATES = [
    ...taskBiddingRoute,
    ...taskBiddingPopupRoute,
];

@NgModule({
    imports: [
        DtmsSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        TaskBiddingComponent,
        TaskBiddingDetailComponent,
        TaskBiddingDialogComponent,
        TaskBiddingDeleteDialogComponent,
        TaskBiddingPopupComponent,
        TaskBiddingDeletePopupComponent,
        TaskBiddingClassifyComponent,
        TaskBiddingClassifyDialogUnbiddingComponent,
        TaskBiddingClassifyPopupUnbiddingComponent,
        TaskBiddingAssignDialogComponent,
        TaskBiddingAssignPopupDialogComponent,
        TaskBiddingUnAssignComponent
    ],
    entryComponents: [
        TaskBiddingComponent,
        TaskBiddingDialogComponent,
        TaskBiddingPopupComponent,
        TaskBiddingDeleteDialogComponent,
        TaskBiddingDeletePopupComponent,
        TaskBiddingClassifyDialogUnbiddingComponent,
        TaskBiddingClassifyPopupUnbiddingComponent,
        TaskBiddingAssignDialogComponent,
        TaskBiddingAssignPopupDialogComponent,
        TaskBiddingUnAssignComponent
    ],
    providers: [
        TaskBiddingService,
        TaskBiddingPopupService,
        TaskBiddingResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DtmsTaskBiddingModule {}
