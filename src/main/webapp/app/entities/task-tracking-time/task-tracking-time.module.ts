import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DtmsSharedModule } from '../../shared';
import {
    TaskTrackingTimeService,
    TaskTrackingTimePopupService,
    TaskTrackingTimeComponent,
    TaskTrackingTimeDetailComponent,
    TaskTrackingTimeDialogComponent,
    TaskTrackingTimePopupComponent,
    TaskTrackingTimeDeletePopupComponent,
    TaskTrackingTimeDeleteDialogComponent,
    taskTrackingTimeRoute,
    taskTrackingTimePopupRoute,
} from './';

const ENTITY_STATES = [
    ...taskTrackingTimeRoute,
    ...taskTrackingTimePopupRoute,
];

@NgModule({
    imports: [
        DtmsSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        TaskTrackingTimeComponent,
        TaskTrackingTimeDetailComponent,
        TaskTrackingTimeDialogComponent,
        TaskTrackingTimeDeleteDialogComponent,
        TaskTrackingTimePopupComponent,
        TaskTrackingTimeDeletePopupComponent,
    ],
    entryComponents: [
        TaskTrackingTimeComponent,
        TaskTrackingTimeDialogComponent,
        TaskTrackingTimePopupComponent,
        TaskTrackingTimeDeleteDialogComponent,
        TaskTrackingTimeDeletePopupComponent,
    ],
    providers: [
        TaskTrackingTimeService,
        TaskTrackingTimePopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DtmsTaskTrackingTimeModule {}
