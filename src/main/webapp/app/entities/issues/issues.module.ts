import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DtmsSharedModule } from '../../shared';
import {
    IssuesService,
    IssuesPopupService,
    IssuesComponent,
    IssuesDetailComponent,
    IssuesDialogComponent,
    IssuesPopupComponent,
    IssuesDeletePopupComponent,
    IssuesDeleteDialogComponent,
    issuesRoute,
    issuesPopupRoute,
    IssuesResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...issuesRoute,
    ...issuesPopupRoute,
];

@NgModule({
    imports: [
        DtmsSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        IssuesComponent,
        IssuesDetailComponent,
        IssuesDialogComponent,
        IssuesDeleteDialogComponent,
        IssuesPopupComponent,
        IssuesDeletePopupComponent,
    ],
    entryComponents: [
        IssuesComponent,
        IssuesDialogComponent,
        IssuesPopupComponent,
        IssuesDeleteDialogComponent,
        IssuesDeletePopupComponent,
    ],
    providers: [
        IssuesService,
        IssuesPopupService,
        IssuesResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DtmsIssuesModule {}
