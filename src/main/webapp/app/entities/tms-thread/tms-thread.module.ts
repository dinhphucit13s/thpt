import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { NgMultiSelectDropDownModule } from 'ng-multiselect-dropdown';

import { DtmsSharedModule } from '../../shared';
import {
    TmsThreadService,
    TmsThreadPopupService,
    TmsThreadComponent,
    TmsThreadDetailComponent,
    TmsThreadDialogComponent,
    TmsThreadPopupComponent,
    TmsThreadDeletePopupComponent,
    TmsThreadDeleteDialogComponent,
    tmsThreadRoute,
    tmsThreadPopupRoute,
    TmsThreadResolvePagingParams,
} from './';
import {PostAnswerComponent} from './post-answer/post-answer.component';
import {TmsThreadClosedDialogComponent, TmsThreadClosedPopupComponent} from './tms-thread-closed-dialog.component';
import { NgxSpinnerModule } from 'ngx-spinner';

const ENTITY_STATES = [
    ...tmsThreadRoute,
    ...tmsThreadPopupRoute,
];

@NgModule({
    imports: [
        DtmsSharedModule,
        RouterModule.forChild(ENTITY_STATES),
        NgMultiSelectDropDownModule.forRoot(),
        NgxSpinnerModule,
    ],
    declarations: [
        TmsThreadComponent,
        TmsThreadDetailComponent,
        TmsThreadDialogComponent,
        TmsThreadDeleteDialogComponent,
        TmsThreadPopupComponent,
        TmsThreadDeletePopupComponent,
        PostAnswerComponent,
        TmsThreadClosedDialogComponent,
        TmsThreadClosedPopupComponent,
    ],
    entryComponents: [
        TmsThreadComponent,
        TmsThreadDialogComponent,
        TmsThreadPopupComponent,
        TmsThreadDeleteDialogComponent,
        TmsThreadDeletePopupComponent,
        TmsThreadClosedDialogComponent,
    ],
    providers: [
        TmsThreadService,
        TmsThreadPopupService,
        TmsThreadResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DtmsTmsThreadModule {}
