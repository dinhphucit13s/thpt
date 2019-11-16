import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DtmsSharedModule } from '../../shared';
import {
    TMSCustomFieldScreenValueService,
    TMSCustomFieldScreenValuePopupService,
    TMSCustomFieldScreenValueComponent,
    TMSCustomFieldScreenValueDetailComponent,
    TMSCustomFieldScreenValueDialogComponent,
    TMSCustomFieldScreenValuePopupComponent,
    TMSCustomFieldScreenValueDeletePopupComponent,
    TMSCustomFieldScreenValueDeleteDialogComponent,
    tMSCustomFieldScreenValueRoute,
    tMSCustomFieldScreenValuePopupRoute,
    TMSCustomFieldScreenValueResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...tMSCustomFieldScreenValueRoute,
    ...tMSCustomFieldScreenValuePopupRoute,
];

@NgModule({
    imports: [
        DtmsSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        TMSCustomFieldScreenValueComponent,
        TMSCustomFieldScreenValueDetailComponent,
        TMSCustomFieldScreenValueDialogComponent,
        TMSCustomFieldScreenValueDeleteDialogComponent,
        TMSCustomFieldScreenValuePopupComponent,
        TMSCustomFieldScreenValueDeletePopupComponent,
    ],
    entryComponents: [
        TMSCustomFieldScreenValueComponent,
        TMSCustomFieldScreenValueDialogComponent,
        TMSCustomFieldScreenValuePopupComponent,
        TMSCustomFieldScreenValueDeleteDialogComponent,
        TMSCustomFieldScreenValueDeletePopupComponent,
    ],
    providers: [
        TMSCustomFieldScreenValueService,
        TMSCustomFieldScreenValuePopupService,
        TMSCustomFieldScreenValueResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DtmsTMSCustomFieldScreenValueModule {}
