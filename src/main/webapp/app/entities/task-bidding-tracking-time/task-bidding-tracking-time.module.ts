import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DtmsSharedModule } from '../../shared';
import {
    TaskBiddingTrackingTimeService,
    TaskBiddingTrackingTimePopupService,
    TaskBiddingTrackingTimeComponent,
    TaskBiddingTrackingTimeDetailComponent,
    TaskBiddingTrackingTimeDialogComponent,
    TaskBiddingTrackingTimePopupComponent,
    TaskBiddingTrackingTimeDeletePopupComponent,
    TaskBiddingTrackingTimeDeleteDialogComponent,
    taskBiddingTrackingTimeRoute,
    taskBiddingTrackingTimePopupRoute,
} from './';

const ENTITY_STATES = [
    ...taskBiddingTrackingTimeRoute,
    ...taskBiddingTrackingTimePopupRoute,
];

@NgModule({
    imports: [
        DtmsSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        TaskBiddingTrackingTimeComponent,
        TaskBiddingTrackingTimeDetailComponent,
        TaskBiddingTrackingTimeDialogComponent,
        TaskBiddingTrackingTimeDeleteDialogComponent,
        TaskBiddingTrackingTimePopupComponent,
        TaskBiddingTrackingTimeDeletePopupComponent,
    ],
    entryComponents: [
        TaskBiddingTrackingTimeComponent,
        TaskBiddingTrackingTimeDialogComponent,
        TaskBiddingTrackingTimePopupComponent,
        TaskBiddingTrackingTimeDeleteDialogComponent,
        TaskBiddingTrackingTimeDeletePopupComponent,
    ],
    providers: [
        TaskBiddingTrackingTimeService,
        TaskBiddingTrackingTimePopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DtmsTaskBiddingTrackingTimeModule {}
