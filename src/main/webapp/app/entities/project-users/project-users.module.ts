import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { NgMultiSelectDropDownModule } from 'ng-multiselect-dropdown';
import { NgJhipsterModule } from 'ng-jhipster';
import { OwlDateTimeModule, OwlNativeDateTimeModule} from 'ng-pick-datetime';
import { MatToolbarModule , MatInputModule , MatTableModule, MatButtonModule, MatPaginatorModule, MatSortModule, MatFormFieldModule
    , MatCheckboxModule, MatSelectModule, MatOptionModule} from '@angular/material';

import { DtmsSharedModule } from '../../shared';
import {
    ProjectUsersService,
    ProjectUsersPopupService,
    ProjectUsersComponent,
    ProjectUsersListComponent,
    ProjectUsersDetailComponent,
    ProjectUsersDialogComponent,
    ProjectUsersPopupComponent,
    ProjectUsersCreateDialogComponent,
    ProjectUsersCreatePopupComponent,
    ProjectUsersDeletePopupComponent,
    ProjectUsersDeleteDialogComponent,
    projectUsersRoute,
    projectUsersPopupRoute,
    ProjectUsersResolvePagingParams,
} from './';
import { FormsModule } from '@angular/forms';
import { FlatpickrModule} from 'angularx-flatpickr';
import {ProjectUsersImportDialogComponent, ProjectUsersImportPopupComponent} from './project-users-import-dialog.component';

const ENTITY_STATES = [
    ...projectUsersRoute,
    ...projectUsersPopupRoute,
];

@NgModule({
    imports: [
        FormsModule,
        FlatpickrModule.forRoot(),
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
        ProjectUsersComponent,
        ProjectUsersListComponent,
        ProjectUsersDetailComponent,
        ProjectUsersDialogComponent,
        ProjectUsersCreateDialogComponent,
        ProjectUsersDeleteDialogComponent,
        ProjectUsersPopupComponent,
        ProjectUsersCreatePopupComponent,
        ProjectUsersDeletePopupComponent,
        ProjectUsersImportDialogComponent,
        ProjectUsersImportPopupComponent
    ],
    entryComponents: [
        ProjectUsersComponent,
        ProjectUsersCreateDialogComponent,
        ProjectUsersDialogComponent,
        ProjectUsersPopupComponent,
        ProjectUsersCreatePopupComponent,
        ProjectUsersDeleteDialogComponent,
        ProjectUsersDeletePopupComponent,
        ProjectUsersImportDialogComponent,
        ProjectUsersImportPopupComponent
    ],
    exports: [
        ProjectUsersComponent,
        ProjectUsersListComponent,
        NgJhipsterModule
    ],
    providers: [
        ProjectUsersService,
        ProjectUsersPopupService,
        ProjectUsersResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DtmsProjectUsersModule {}
