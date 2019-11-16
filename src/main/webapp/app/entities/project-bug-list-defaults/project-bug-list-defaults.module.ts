import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { NgMultiSelectDropDownModule } from 'ng-multiselect-dropdown';
import { NgJhipsterModule } from 'ng-jhipster';
import { OwlDateTimeModule, OwlNativeDateTimeModule} from 'ng-pick-datetime';
import { MatToolbarModule , MatInputModule , MatTableModule, MatButtonModule, MatPaginatorModule, MatSortModule, MatFormFieldModule
    , MatCheckboxModule, MatSelectModule, MatOptionModule} from '@angular/material';

import { DtmsSharedModule } from '../../shared';
import {
    ProjectBugListDefaultsComponent,
    ProjectBugListDefaultsResolvePagingParams,
} from './';
import {projectBugListDefaultsPopupRoute, projectBugListDefaultsRoute} from './project-bug-list-defaults.route';
import {ProjectBugListDefaultsService} from './project-bug-list-defaults.service';
import {BugListDefaultService} from '../bug-list-default';
import {ProjectBugListDefaultsCreateDialogComponent, ProjectBugListDefaultsCreatePopupComponent} from './project-bug-list-defaults-create-dialog.component';
import {ProjectBugListDefaultsPopupService} from './project-bug-list-defaults-popup.service';
import {ProjectBugListDefaultsDeleteDialogComponent, ProjectBugListDefaultsDeletePopupComponent} from './project-bug-list-defaults-delete-dialog.component';

const ENTITY_STATES = [
    ...projectBugListDefaultsRoute,
    ...projectBugListDefaultsPopupRoute
];

@NgModule({
    imports: [
        NgMultiSelectDropDownModule.forRoot(),
        MatInputModule,
        MatTableModule,
        MatButtonModule,
        MatPaginatorModule,
        MatSortModule,
        MatFormFieldModule,
        OwlDateTimeModule,
        OwlNativeDateTimeModule,
        MatCheckboxModule,
        MatSelectModule,
        MatOptionModule,
        DtmsSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        ProjectBugListDefaultsComponent,
        ProjectBugListDefaultsCreateDialogComponent,
        ProjectBugListDefaultsDeleteDialogComponent,
        ProjectBugListDefaultsCreatePopupComponent,
        ProjectBugListDefaultsDeletePopupComponent
    ],
    entryComponents: [
        ProjectBugListDefaultsComponent,
        ProjectBugListDefaultsCreateDialogComponent,
        ProjectBugListDefaultsDeleteDialogComponent,
        ProjectBugListDefaultsCreatePopupComponent,
        ProjectBugListDefaultsDeletePopupComponent
    ],
    exports: [
        ProjectBugListDefaultsComponent,
        ProjectBugListDefaultsCreateDialogComponent,
        ProjectBugListDefaultsDeleteDialogComponent,
        ProjectBugListDefaultsCreatePopupComponent,
        ProjectBugListDefaultsDeletePopupComponent,
        NgJhipsterModule
    ],
    providers: [
        BugListDefaultService,
        ProjectBugListDefaultsService,
        ProjectBugListDefaultsResolvePagingParams,
        ProjectBugListDefaultsPopupService
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DtmsProjectBugListDefaultsModule {}
