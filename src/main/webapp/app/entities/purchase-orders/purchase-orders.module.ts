import { NgModule, CUSTOM_ELEMENTS_SCHEMA, Inject, Injectable } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { RouterModule } from '@angular/router';
import { MatToolbarModule , MatInputModule , MatTableModule, MatButtonModule, MatPaginatorModule, MatSortModule, MatFormFieldModule
    , MatCheckboxModule, MatSelectModule, MatOptionModule} from '@angular/material';
import { OwlDateTimeModule, OwlNativeDateTimeModule } from 'ng-pick-datetime';
import {MatIconModule} from '@angular/material/icon';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { DtmsSharedModule } from '../../shared';
import { AgGridModule } from 'ag-grid-angular';
import { ButtonViewComponent } from '../../shared/dynamic-data-tables/buttons-view/button-view.component';
import {
    PurchaseOrdersService,
    PurchaseOrdersPopupService,
    PurchaseOrdersComponent,
    PurchaseOrdersDetailComponent,
    PurchaseOrdersDialogComponent,
    PurchaseOrdersPopupComponent,
    PurchaseOrdersDeletePopupComponent,
    PurchaseOrdersDeleteDialogComponent,
    purchaseOrdersRoute,
    purchaseOrdersPopupRoute,
    PurchaseOrdersResolvePagingParams,
} from './';
import { FormsModule } from '@angular/forms';
import { FlatpickrModule} from 'angularx-flatpickr';
import {NgMultiSelectDropDownModule} from 'ng-multiselect-dropdown';

const ENTITY_STATES = [
    ...purchaseOrdersRoute,
    ...purchaseOrdersPopupRoute,
];
@NgModule({
    imports: [
        FormsModule,
        BrowserModule,
        MatButtonModule,
        MatInputModule,
        MatSelectModule,
        MatOptionModule,
        MatFormFieldModule,
        MatCheckboxModule,
        BrowserAnimationsModule,
        MatToolbarModule,
        MatTableModule,
        MatPaginatorModule,
        MatSortModule,
        MatIconModule,
        OwlDateTimeModule,
        OwlNativeDateTimeModule,
        DtmsSharedModule,
        FlatpickrModule.forRoot(),
        AgGridModule.withComponents([ButtonViewComponent]),
        RouterModule.forChild(ENTITY_STATES),
        NgMultiSelectDropDownModule.forRoot()
    ],
    declarations: [
        PurchaseOrdersComponent,
        PurchaseOrdersDetailComponent,
        PurchaseOrdersDialogComponent,
        PurchaseOrdersDeleteDialogComponent,
        PurchaseOrdersPopupComponent,
        PurchaseOrdersDeletePopupComponent,
    ],
    entryComponents: [
        PurchaseOrdersComponent,
        PurchaseOrdersDialogComponent,
        PurchaseOrdersPopupComponent,
        PurchaseOrdersDeleteDialogComponent,
        PurchaseOrdersDeletePopupComponent,
    ],
    providers: [
        PurchaseOrdersService,
        PurchaseOrdersPopupService,
        PurchaseOrdersResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA],
    bootstrap: [PurchaseOrdersComponent],
})
export class DtmsPurchaseOrdersModule {}
