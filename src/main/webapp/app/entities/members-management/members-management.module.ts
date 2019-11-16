import { NgModule, CUSTOM_ELEMENTS_SCHEMA, Inject, Injectable } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { RouterModule } from '@angular/router';
import { OwlDateTimeModule, OwlNativeDateTimeModule } from 'ng-pick-datetime';
import {MatIconModule} from '@angular/material/icon';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatToolbarModule , MatInputModule , MatTableModule, MatButtonModule, MatPaginatorModule, MatSortModule, MatFormFieldModule
    , MatCheckboxModule, MatSelectModule, MatOptionModule} from '@angular/material';
import { DtmsSharedModule } from '../../shared';
import { MembersManagementDetailComponent } from './members-management-detail.component';
import {
    MembersManagementService,
    MembersManagementComponent,
    membersManagementRoute,
    membersManagementPopupRoute,
} from './';
import {MembersManagementResolvePagingParams} from './members-management.route';

const ENTITY_STATES = [
    ...membersManagementRoute,
    ...membersManagementPopupRoute
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
        MembersManagementComponent,
        MembersManagementDetailComponent
    ],
    entryComponents: [
        MembersManagementComponent,
        MembersManagementDetailComponent
    ],
    providers: [
        MembersManagementService,
        MembersManagementResolvePagingParams
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA],
    bootstrap: [MembersManagementComponent],
})
export class DtmsMembersManagememt {}
