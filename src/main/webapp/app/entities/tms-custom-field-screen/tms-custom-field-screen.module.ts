import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { DtmsSharedModule } from '../../shared';
import {
    TMSCustomFieldScreenService,
    TMSCustomFieldScreenPopupService,
    TMSCustomFieldScreenComponent,
    TMSCustomFieldScreenDetailComponent,
    TMSCustomFieldScreenDialogComponent,
    TMSCustomFieldScreenPopupComponent,
    // TMSCustomFieldScreenDeletePopupComponent,
    TMSCustomFieldScreenDeleteDialogComponent,
    tMSCustomFieldScreenRoute,
    tMSCustomFieldScreenPopupRoute,
    TMSCustomFieldScreenResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...tMSCustomFieldScreenRoute,
    ...tMSCustomFieldScreenPopupRoute,
];

@NgModule({
    imports: [
        DtmsSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        TMSCustomFieldScreenComponent,
        TMSCustomFieldScreenDetailComponent,
        TMSCustomFieldScreenDialogComponent,
        TMSCustomFieldScreenDeleteDialogComponent,
        TMSCustomFieldScreenPopupComponent,
        // TMSCustomFieldScreenDeletePopupComponent,
    ],
    entryComponents: [
        TMSCustomFieldScreenComponent,
        TMSCustomFieldScreenDialogComponent,
        TMSCustomFieldScreenPopupComponent,
        TMSCustomFieldScreenDeleteDialogComponent,
        // TMSCustomFieldScreenDeletePopupComponent,
    ],
    providers: [
        TMSCustomFieldScreenService,
        TMSCustomFieldScreenPopupService,
        TMSCustomFieldScreenResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DtmsTMSCustomFieldScreenModule {}
