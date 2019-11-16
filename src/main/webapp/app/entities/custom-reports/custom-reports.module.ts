import { NgModule, CUSTOM_ELEMENTS_SCHEMA, Inject, Injectable } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { RouterModule } from '@angular/router';
import { MatToolbarModule , MatInputModule , MatTableModule, MatButtonModule, MatPaginatorModule, MatSortModule, MatFormFieldModule
    , MatCheckboxModule, MatSelectModule, MatOptionModule} from '@angular/material';
import { OwlDateTimeModule, OwlNativeDateTimeModule } from 'ng-pick-datetime';
import {MatIconModule} from '@angular/material/icon';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { DtmsSharedModule } from '../../shared';
import {CustomReportsService} from './custom-reports.service';
@NgModule({
    imports: [
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
    ],
    declarations: [
    ],
    entryComponents: [
    ],
    providers: [
        CustomReportsService
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA],
    bootstrap: [],
})
export class DtmsCustomReportsModule {}
