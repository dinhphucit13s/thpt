import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DtmsSharedModule } from '../../shared';
import {
    ProjectBugListDefaultService,
    ProjectBugListDefaultPopupService,
    ProjectBugListDefaultComponent,
    ProjectBugListDefaultDetailComponent,
    ProjectBugListDefaultDialogComponent,
    ProjectBugListDefaultPopupComponent,
    ProjectBugListDefaultDeletePopupComponent,
    ProjectBugListDefaultDeleteDialogComponent,
    projectBugListDefaultRoute,
    projectBugListDefaultPopupRoute,
    ProjectBugListDefaultResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...projectBugListDefaultRoute,
    ...projectBugListDefaultPopupRoute,
];

@NgModule({
    imports: [
        DtmsSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        ProjectBugListDefaultComponent,
        ProjectBugListDefaultDetailComponent,
        ProjectBugListDefaultDialogComponent,
        ProjectBugListDefaultDeleteDialogComponent,
        ProjectBugListDefaultPopupComponent,
        ProjectBugListDefaultDeletePopupComponent,
    ],
    entryComponents: [
        ProjectBugListDefaultComponent,
        ProjectBugListDefaultDialogComponent,
        ProjectBugListDefaultPopupComponent,
        ProjectBugListDefaultDeleteDialogComponent,
        ProjectBugListDefaultDeletePopupComponent,
    ],
    providers: [
        ProjectBugListDefaultService,
        ProjectBugListDefaultPopupService,
        ProjectBugListDefaultResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DtmsProjectBugListDefaultModule {}
