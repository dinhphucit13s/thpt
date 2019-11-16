import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DtmsSharedModule } from '../../shared';
import {
    LoginTrackingService,
    LoginTrackingPopupService,
    LoginTrackingComponent,
    LoginTrackingDetailComponent,
    LoginTrackingDialogComponent,
    LoginTrackingPopupComponent,
    LoginTrackingDeletePopupComponent,
    LoginTrackingDeleteDialogComponent,
    loginTrackingRoute,
    loginTrackingPopupRoute,
} from './';

const ENTITY_STATES = [
    ...loginTrackingRoute,
    ...loginTrackingPopupRoute,
];

@NgModule({
    imports: [
        DtmsSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        LoginTrackingComponent,
        LoginTrackingDetailComponent,
        LoginTrackingDialogComponent,
        LoginTrackingDeleteDialogComponent,
        LoginTrackingPopupComponent,
        LoginTrackingDeletePopupComponent,
    ],
    entryComponents: [
        LoginTrackingComponent,
        LoginTrackingDialogComponent,
        LoginTrackingPopupComponent,
        LoginTrackingDeleteDialogComponent,
        LoginTrackingDeletePopupComponent,
    ],
    providers: [
        LoginTrackingService,
        LoginTrackingPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DtmsLoginTrackingModule {}
