import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DtmsSharedModule } from '../../shared';
import {
    BusinessUnitService,
    BusinessUnitPopupService,
    BusinessUnitComponent,
    BusinessUnitDetailComponent,
    BusinessUnitDialogComponent,
    BusinessUnitPopupComponent,
    BusinessUnitDeletePopupComponent,
    BusinessUnitDeleteDialogComponent,
    businessUnitRoute,
    businessUnitPopupRoute,
    BusinessUnitResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...businessUnitRoute,
    ...businessUnitPopupRoute,
];

@NgModule({
    imports: [
        DtmsSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        BusinessUnitComponent,
        BusinessUnitDetailComponent,
        BusinessUnitDialogComponent,
        BusinessUnitDeleteDialogComponent,
        BusinessUnitPopupComponent,
        BusinessUnitDeletePopupComponent,
    ],
    entryComponents: [
        BusinessUnitComponent,
        BusinessUnitDialogComponent,
        BusinessUnitPopupComponent,
        BusinessUnitDeleteDialogComponent,
        BusinessUnitDeletePopupComponent,
    ],
    providers: [
        BusinessUnitService,
        BusinessUnitPopupService,
        BusinessUnitResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DtmsBusinessUnitModule {}
