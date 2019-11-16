import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { OwlDateTimeModule, OwlNativeDateTimeModule} from 'ng-pick-datetime';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatToolbarModule , MatInputModule , MatTableModule, MatButtonModule, MatPaginatorModule, MatSortModule, MatFormFieldModule
    , MatCheckboxModule, MatSelectModule, MatOptionModule, MatIconModule, MatCardModule, MatListModule, MatDatepickerModule, MatNativeDateModule
    , MatRadioModule} from '@angular/material';
import { FormsModule } from '@angular/forms';
import {ProgressBarModule} from 'angular-progress-bar';
import { DtmsSharedModule } from '../../shared';
import { AgGridModule } from 'ag-grid-angular';
import { ButtonViewComponent } from '../../shared/dynamic-data-tables/buttons-view/button-view.component';
import {
    PackagesService,
    PackagesPopupService,
    PackagesComponent,
    PackagesDetailComponent,
    PackagesDialogComponent,
    PackagesPopupComponent,
    PackagesDeletePopupComponent,
    PackagesDeleteDialogComponent,
    PackagesImportPopupComponent,
    PackagesImportDialogComponent,
    packagesRoute,
    packagesPopupRoute,
    PackagesResolvePagingParams,
} from './';
import {TaskCloneComponent} from './task-clone-dialog.component';
import {CommonModule, DatePipe} from '@angular/common';
import {PackagesDeliveryDialogComponent, PackagesDeliveryPopupComponent} from './packages-delivery-dialog.component';
import { PackagesCloneDialogComponent, PackagesClonePopupComponent } from './packages-clone-dialog.component';
import {TaskCloneDialogComponent} from './task-clone-dialog.component/task-clone-assign-dialog.component';
import {TaskClonePopupDialogComponent} from './task-clone-dialog.component/task-clone-assign-dialog.component';
import {TaskClonePopupService} from './task-clone-dialog.component/task-clone-popup.service';

const ENTITY_STATES = [
    ...packagesRoute,
    ...packagesPopupRoute,
];

@NgModule({
    imports: [
        OwlDateTimeModule,
        OwlNativeDateTimeModule,
        BrowserAnimationsModule,
        DtmsSharedModule,
        ProgressBarModule,
        MatToolbarModule,
        MatInputModule,
        MatTableModule,
        MatButtonModule,
        MatPaginatorModule,
        MatSortModule,
        MatFormFieldModule,
        MatCheckboxModule,
        MatSelectModule,
        MatOptionModule,
        MatIconModule,
        MatCardModule,
        MatListModule,
        MatDatepickerModule,
        MatNativeDateModule,
        MatRadioModule,
        FormsModule,
        CommonModule,
        AgGridModule.withComponents([ButtonViewComponent]),
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        PackagesComponent,
        PackagesDetailComponent,
        PackagesDialogComponent,
        PackagesDeleteDialogComponent,
        PackagesPopupComponent,
        PackagesDeletePopupComponent,
        PackagesImportPopupComponent,
        PackagesImportDialogComponent,
        PackagesDeliveryDialogComponent,
        PackagesDeliveryPopupComponent,

        /*PhuVD3 Packages Clone*/
        PackagesCloneDialogComponent,
        PackagesClonePopupComponent,
        TaskCloneComponent,
        TaskCloneDialogComponent,
        TaskClonePopupDialogComponent
    ],
    entryComponents: [
        PackagesComponent,
        PackagesDialogComponent,
        PackagesPopupComponent,
        PackagesDeleteDialogComponent,
        PackagesDeletePopupComponent,
        PackagesImportPopupComponent,
        PackagesImportDialogComponent,
        PackagesDeliveryDialogComponent,
        PackagesDeliveryPopupComponent,

        /*PhuVD3 Packages Clone*/
        PackagesCloneDialogComponent,
        PackagesClonePopupComponent,
        TaskCloneComponent,
        TaskCloneDialogComponent,
        TaskClonePopupDialogComponent
    ],
    providers: [
        PackagesService,
        PackagesPopupService,
        TaskClonePopupService,
        PackagesResolvePagingParams,
        DatePipe
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DtmsPackagesModule {}
