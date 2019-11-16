import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DtmsSharedModule } from '../../shared';
import {
    TMSLogHistoryService,
    TMSLogHistoryPopupService,
    TMSLogHistoryComponent,
    TMSLogHistoryDetailComponent,
    TMSLogHistoryDialogComponent,
    TMSLogHistoryPopupComponent,
    TMSLogHistoryDeletePopupComponent,
    TMSLogHistoryDeleteDialogComponent,
    tMSLogHistoryRoute,
    tMSLogHistoryPopupRoute,
    TMSLogHistoryResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...tMSLogHistoryRoute,
    ...tMSLogHistoryPopupRoute,
];

@NgModule({
    imports: [
        DtmsSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        TMSLogHistoryComponent,
        TMSLogHistoryDetailComponent,
        TMSLogHistoryDialogComponent,
        TMSLogHistoryDeleteDialogComponent,
        TMSLogHistoryPopupComponent,
        TMSLogHistoryDeletePopupComponent,
    ],
    entryComponents: [
        TMSLogHistoryComponent,
        TMSLogHistoryDialogComponent,
        TMSLogHistoryPopupComponent,
        TMSLogHistoryDeleteDialogComponent,
        TMSLogHistoryDeletePopupComponent,
    ],
    providers: [
        TMSLogHistoryService,
        TMSLogHistoryPopupService,
        TMSLogHistoryResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DtmsTMSLogHistoryModule {}
