import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DtmsSharedModule } from '../../shared';
import {
    DtmsMonitoringService,
    DtmsMonitoringPopupService,
    DtmsMonitoringComponent,
    DtmsMonitoringDetailComponent,
    DtmsMonitoringDialogComponent,
    DtmsMonitoringPopupComponent,
    DtmsMonitoringDeletePopupComponent,
    DtmsMonitoringDeleteDialogComponent,
    dtmsMonitoringRoute,
    dtmsMonitoringPopupRoute,
} from './';

const ENTITY_STATES = [
    ...dtmsMonitoringRoute,
    ...dtmsMonitoringPopupRoute,
];

@NgModule({
    imports: [
        DtmsSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        DtmsMonitoringComponent,
        DtmsMonitoringDetailComponent,
        DtmsMonitoringDialogComponent,
        DtmsMonitoringDeleteDialogComponent,
        DtmsMonitoringPopupComponent,
        DtmsMonitoringDeletePopupComponent,
    ],
    entryComponents: [
        DtmsMonitoringComponent,
        DtmsMonitoringDialogComponent,
        DtmsMonitoringPopupComponent,
        DtmsMonitoringDeleteDialogComponent,
        DtmsMonitoringDeletePopupComponent,
    ],
    providers: [
        DtmsMonitoringService,
        DtmsMonitoringPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DtmsDtmsMonitoringModule {}
