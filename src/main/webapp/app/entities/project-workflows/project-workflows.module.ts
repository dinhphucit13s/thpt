import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { DtmsSharedModule } from '../../shared';
import {
    ProjectWorkflowsService,
    ProjectWorkflowsPopupService,
    ProjectWorkflowsComponent,
    ProjectWorkflowsDetailComponent,
    ProjectWorkflowsDialogComponent,
    ProjectWorkflowsPopupComponent,
    ProjectWorkflowsDeletePopupComponent,
    ProjectWorkflowsDeleteDialogComponent,
    projectWorkflowsRoute,
    projectWorkflowsPopupRoute,
    ProjectWorkflowsResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...projectWorkflowsRoute,
    ...projectWorkflowsPopupRoute,
];

@NgModule({
    imports: [
        DtmsSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        ProjectWorkflowsComponent,
        ProjectWorkflowsDetailComponent,
        ProjectWorkflowsDialogComponent,
        ProjectWorkflowsDeleteDialogComponent,
        ProjectWorkflowsPopupComponent,
        ProjectWorkflowsDeletePopupComponent,
    ],
    entryComponents: [
        ProjectWorkflowsComponent,
        ProjectWorkflowsDialogComponent,
        ProjectWorkflowsPopupComponent,
        ProjectWorkflowsDeleteDialogComponent,
        ProjectWorkflowsDeletePopupComponent,
    ],
    providers: [
        ProjectWorkflowsService,
        ProjectWorkflowsPopupService,
        ProjectWorkflowsResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DtmsProjectWorkflowsModule {}
