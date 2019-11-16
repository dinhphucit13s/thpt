import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DtmsSharedModule } from '../../shared';
import {
    BugListDefaultService,
    BugListDefaultPopupService,
    BugListDefaultComponent,
    BugListDefaultDetailComponent,
    BugListDefaultDialogComponent,
    BugListDefaultPopupComponent,
    BugListDefaultDeletePopupComponent,
    BugListDefaultDeleteDialogComponent,
    bugListDefaultRoute,
    bugListDefaultPopupRoute,
    BugListDefaultResolvePagingParams,
} from './';
import {NgMultiSelectDropDownModule} from "ng-multiselect-dropdown";

const ENTITY_STATES = [
    ...bugListDefaultRoute,
    ...bugListDefaultPopupRoute,
];

@NgModule({
    imports: [
        DtmsSharedModule,
        RouterModule.forChild(ENTITY_STATES),
        NgMultiSelectDropDownModule.forRoot()
    ],
    declarations: [
        BugListDefaultComponent,
        BugListDefaultDetailComponent,
        BugListDefaultDialogComponent,
        BugListDefaultDeleteDialogComponent,
        BugListDefaultPopupComponent,
        BugListDefaultDeletePopupComponent,
    ],
    entryComponents: [
        BugListDefaultComponent,
        BugListDefaultDialogComponent,
        BugListDefaultPopupComponent,
        BugListDefaultDeleteDialogComponent,
        BugListDefaultDeletePopupComponent,
    ],
    providers: [
        BugListDefaultService,
        BugListDefaultPopupService,
        BugListDefaultResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DtmsBugListDefaultModule {}
