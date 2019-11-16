import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DtmsSharedModule } from '../../shared';
import { DtmsAdminModule } from '../../admin/admin.module';
import {
    BusinessUnitManagerService,
    BusinessUnitManagerPopupService,
    BusinessUnitManagerComponent,
    BusinessUnitManagerDetailComponent,
    BusinessUnitManagerDialogComponent,
    BusinessUnitManagerPopupComponent,
    BusinessUnitManagerDeletePopupComponent,
    BusinessUnitManagerDeleteDialogComponent,
    businessUnitManagerRoute,
    businessUnitManagerPopupRoute,
    BusinessUnitManagerResolvePagingParams,
} from './';
import {OwlDateTimeModule} from 'ng-pick-datetime';
import {NgMultiSelectDropDownModule} from 'ng-multiselect-dropdown';
import {FlatpickrModule} from 'angularx-flatpickr';
import {FormsModule} from '@angular/forms';

const ENTITY_STATES = [
    ...businessUnitManagerRoute,
    ...businessUnitManagerPopupRoute,
];

@NgModule({
    imports: [
        FormsModule,
        FlatpickrModule.forRoot(),
        DtmsSharedModule,
        DtmsAdminModule,
        RouterModule.forChild(ENTITY_STATES),
        OwlDateTimeModule,
        NgMultiSelectDropDownModule.forRoot()
    ],
    declarations: [
        BusinessUnitManagerComponent,
        BusinessUnitManagerDetailComponent,
        BusinessUnitManagerDialogComponent,
        BusinessUnitManagerDeleteDialogComponent,
        BusinessUnitManagerPopupComponent,
        BusinessUnitManagerDeletePopupComponent,
    ],
    entryComponents: [
        BusinessUnitManagerComponent,
        BusinessUnitManagerDialogComponent,
        BusinessUnitManagerPopupComponent,
        BusinessUnitManagerDeleteDialogComponent,
        BusinessUnitManagerDeletePopupComponent,
    ],
    providers: [
        BusinessUnitManagerService,
        BusinessUnitManagerPopupService,
        BusinessUnitManagerResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DtmsBusinessUnitManagerModule {}
