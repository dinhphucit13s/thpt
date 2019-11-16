import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DtmsSharedModule } from '../../shared';
import {
    FeedbackService,
    FeedbackPopupService,
    FeedbackComponent,
    FeedbackDetailComponent,
    FeedbackDialogComponent,
    FeedbackPopupComponent,
    FeedbackDeletePopupComponent,
    FeedbackDeleteDialogComponent,
    feedbackRoute,
    feedbackPopupRoute,
    FeedbackResolvePagingParams,
} from './';
import {FeedbackShowAttachmentComponent} from "./feedback-show-attachment/feedback-show-attachment.component";

const ENTITY_STATES = [
    ...feedbackRoute,
    ...feedbackPopupRoute,
];

@NgModule({
    imports: [
        DtmsSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        FeedbackComponent,
        FeedbackDetailComponent,
        FeedbackDialogComponent,
        FeedbackDeleteDialogComponent,
        FeedbackPopupComponent,
        FeedbackDeletePopupComponent,
        FeedbackShowAttachmentComponent
    ],
    entryComponents: [
        FeedbackComponent,
        FeedbackDialogComponent,
        FeedbackPopupComponent,
        FeedbackDeleteDialogComponent,
        FeedbackDeletePopupComponent,
        FeedbackShowAttachmentComponent
    ],
    providers: [
        FeedbackService,
        FeedbackPopupService,
        FeedbackResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DtmsFeedbackModule {}
