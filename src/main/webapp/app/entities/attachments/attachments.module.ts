import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DtmsSharedModule } from '../../shared';
import {
    AttachmentsService,
    AttachmentsPopupService,
    AttachmentsComponent,
    AttachmentsDetailComponent,
    AttachmentsDialogComponent,
    AttachmentsPopupComponent,
    AttachmentsDeletePopupComponent,
    AttachmentsDeleteDialogComponent,
    attachmentsRoute,
    attachmentsPopupRoute,
    AttachmentsResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...attachmentsRoute,
    ...attachmentsPopupRoute,
];

@NgModule({
    imports: [
        DtmsSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        AttachmentsComponent,
        AttachmentsDetailComponent,
        AttachmentsDialogComponent,
        AttachmentsDeleteDialogComponent,
        AttachmentsPopupComponent,
        AttachmentsDeletePopupComponent,
    ],
    entryComponents: [
        AttachmentsComponent,
        AttachmentsDialogComponent,
        AttachmentsPopupComponent,
        AttachmentsDeleteDialogComponent,
        AttachmentsDeletePopupComponent,
    ],
    providers: [
        AttachmentsService,
        AttachmentsPopupService,
        AttachmentsResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DtmsAttachmentsModule {}
