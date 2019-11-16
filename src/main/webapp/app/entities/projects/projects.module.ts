import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { OwlDateTimeModule, OwlNativeDateTimeModule } from 'ng-pick-datetime';
import { DtmsSharedModule } from '../../shared';
import { FormsModule } from '@angular/forms';
import { FlatpickrModule} from 'angularx-flatpickr';
import {
    ProjectsService,
    ProjectsPopupService,
    ProjectsComponent,
    ProjectsDetailComponent,
    ProjectsDialogComponent,
    ProjectsPopupComponent,
    ProjectsDeletePopupComponent,
    ProjectsDeleteDialogComponent,
    projectsRoute,
    projectsPopupRoute,
    ProjectsResolvePagingParams,
} from './';
import {DtmsProjectUsersModule} from '../project-users/project-users.module';
import {NgMultiSelectDropDownModule} from 'ng-multiselect-dropdown';
const ENTITY_STATES = [
    ...projectsRoute,
    ...projectsPopupRoute,
];

@NgModule({
    imports: [
        FormsModule,
        FlatpickrModule.forRoot(),
        DtmsSharedModule,
        OwlDateTimeModule,
        OwlNativeDateTimeModule,
        RouterModule.forChild(ENTITY_STATES),
        DtmsProjectUsersModule,
        NgMultiSelectDropDownModule.forRoot()
    ],
    declarations: [
        ProjectsComponent,
        ProjectsDetailComponent,
        ProjectsDialogComponent,
        ProjectsDeleteDialogComponent,
        ProjectsPopupComponent,
        ProjectsDeletePopupComponent,
    ],
    entryComponents: [
        ProjectsComponent,
        ProjectsDialogComponent,
        ProjectsPopupComponent,
        ProjectsDeleteDialogComponent,
        ProjectsDeletePopupComponent,
    ],
    providers: [
        ProjectsService,
        ProjectsPopupService,
        ProjectsResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DtmsProjectsModule {}
