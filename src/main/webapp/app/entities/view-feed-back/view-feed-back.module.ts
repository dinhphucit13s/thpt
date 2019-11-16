import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DtmsSharedModule } from '../../shared';

import { ViewFeedBackComponent } from './view-feed-back.component';
import { ViewFeedBackDetailComponent } from './view-feed-back-detail.component';
import { IssuesDetailShowAttachmentComponent } from './issues-detail-show-attachment.component';
import {ViewFeedBackResolvePagingParams, viewFeedBackRoute} from './view-feed-back.route';
import { IssuesService } from '../issues/issues.service';
import { ViewFeedBackService } from './view-feed-back.service';

const ENTITY_STATES = [
    ...viewFeedBackRoute,
];

@NgModule({
    imports: [
        DtmsSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        ViewFeedBackComponent,
        ViewFeedBackDetailComponent,
        IssuesDetailShowAttachmentComponent
    ],
    entryComponents: [
        ViewFeedBackComponent,
        IssuesDetailShowAttachmentComponent
    ],
    providers: [
        IssuesService,
        ViewFeedBackService,
        ViewFeedBackResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DtmsViewFeedBackModule {}
