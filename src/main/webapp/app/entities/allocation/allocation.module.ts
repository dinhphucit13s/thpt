import { NgModule, CUSTOM_ELEMENTS_SCHEMA, Inject, Injectable } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { RouterModule } from '@angular/router';
import { OwlDateTimeModule, OwlNativeDateTimeModule } from 'ng-pick-datetime';
import {MatIconModule} from '@angular/material/icon';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatToolbarModule , MatInputModule , MatTableModule, MatButtonModule, MatPaginatorModule, MatSortModule, MatFormFieldModule
    , MatCheckboxModule, MatSelectModule, MatOptionModule} from '@angular/material';
import { DtmsSharedModule } from '../../shared';
import { AllocationDetailComponent } from '../allocation/allocation-detail.component';
import {
    AllocationService,
    AllocationComponent,
    AllocationResolvePagingParams,
    allocationRoute,
} from './';

const ENTITY_STATES = [
    ...allocationRoute
];
@NgModule({
    imports: [
        MatButtonModule,
        MatInputModule,
        MatSelectModule,
        MatOptionModule,
        MatFormFieldModule,
        MatCheckboxModule,
        MatToolbarModule,
        MatTableModule,
        MatPaginatorModule,
        MatSortModule,
        MatIconModule,
        BrowserModule,
        BrowserAnimationsModule,
        MatIconModule,
        OwlDateTimeModule,
        OwlNativeDateTimeModule,
        DtmsSharedModule,
        RouterModule.forChild(ENTITY_STATES),
    ],
    declarations: [
        AllocationComponent,
        AllocationDetailComponent
    ],
    entryComponents: [
        AllocationComponent,
        AllocationDetailComponent
    ],
    providers: [
        AllocationService,
        AllocationResolvePagingParams
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA],
    bootstrap: [AllocationComponent],
})
export class DtmsAllocation {}
