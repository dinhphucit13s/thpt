import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DtmsSharedModule } from '../../shared';
import {
    BusinessLineService,
    BusinessLinePopupService,
    BusinessLineComponent,
    BusinessLineDetailComponent,
    BusinessLineDialogComponent,
    BusinessLinePopupComponent,
    BusinessLineDeletePopupComponent,
    BusinessLineDeleteDialogComponent,
    businessLineRoute,
    businessLinePopupRoute,
    BusinessLineResolvePagingParams,
} from './';
import {AgGridModule} from 'ag-grid-angular';
import {ButtonViewComponent} from '../../shared/dynamic-data-tables/buttons-view/button-view.component';

const ENTITY_STATES = [
    ...businessLineRoute,
    ...businessLinePopupRoute,
];

@NgModule({
    imports: [
        DtmsSharedModule,
        RouterModule.forChild(ENTITY_STATES),
        AgGridModule.withComponents([ButtonViewComponent]),
    ],
    declarations: [
        BusinessLineComponent,
        BusinessLineDetailComponent,
        BusinessLineDialogComponent,
        BusinessLineDeleteDialogComponent,
        BusinessLinePopupComponent,
        BusinessLineDeletePopupComponent,
    ],
    entryComponents: [
        BusinessLineComponent,
        BusinessLineDialogComponent,
        BusinessLinePopupComponent,
        BusinessLineDeleteDialogComponent,
        BusinessLineDeletePopupComponent,
    ],
    providers: [
        BusinessLineService,
        BusinessLinePopupService,
        BusinessLineResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DtmsBusinessLineModule {}
