import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DtmsSharedModule } from '../../shared';
import {
    TmsPostService,
    TmsPostPopupService,
    TmsPostComponent,
    TmsPostDetailComponent,
    TmsPostDialogComponent,
    TmsPostPopupComponent,
    TmsPostDeletePopupComponent,
    TmsPostDeleteDialogComponent,
    tmsPostRoute,
    tmsPostPopupRoute,
} from './';

import { NgxSpinnerModule } from 'ngx-spinner';

const ENTITY_STATES = [
    ...tmsPostRoute,
    ...tmsPostPopupRoute,
];

@NgModule({
    imports: [
        DtmsSharedModule,
        RouterModule.forChild(ENTITY_STATES),
        NgxSpinnerModule
    ],
    declarations: [
        TmsPostComponent,
        TmsPostDetailComponent,
        TmsPostDialogComponent,
        TmsPostDeleteDialogComponent,
        TmsPostPopupComponent,
        TmsPostDeletePopupComponent,
    ],
    entryComponents: [
        TmsPostComponent,
        TmsPostDialogComponent,
        TmsPostPopupComponent,
        TmsPostDeleteDialogComponent,
        TmsPostDeletePopupComponent,
    ],
    providers: [
        TmsPostService,
        TmsPostPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DtmsTmsPostModule {}
