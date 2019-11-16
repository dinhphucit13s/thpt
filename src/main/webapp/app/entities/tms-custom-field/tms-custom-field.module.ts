import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { TitleCasePipe } from '@angular/common';
import { FlatpickrModule} from 'angularx-flatpickr';
import { DtmsSharedModule } from '../../shared';
import { ButtonViewComponent } from '../../shared/dynamic-data-tables/buttons-view/button-view.component';
import {
    TMSCustomFieldService,
    TMSCustomFieldPopupService,
    TMSCustomFieldComponent,
    TMSCustomFieldDetailComponent,
    TMSCustomFieldDialogComponent,
    TMSCustomFieldPopupComponent,
    TMSCustomFieldCreateComponent,
    TMSCustomFieldDeletePopupComponent,
    TMSCustomFieldDeleteDialogComponent,
    tMSCustomFieldRoute,
    tMSCustomFieldPopupRoute,
    TMSCustomFieldResolvePagingParams,
} from './';
import {AgGridModule} from 'ag-grid-angular';
import {ColorPickerModule} from 'ngx-color-picker';

const ENTITY_STATES = [
    ...tMSCustomFieldRoute,
    ...tMSCustomFieldPopupRoute,
];

@NgModule({
    imports: [
        DtmsSharedModule,
        FlatpickrModule.forRoot(),
        AgGridModule.withComponents([ButtonViewComponent]),
        RouterModule.forChild(ENTITY_STATES),
        ColorPickerModule
    ],
    declarations: [
        TMSCustomFieldComponent,
        TMSCustomFieldDetailComponent,
        TMSCustomFieldDialogComponent,
        TMSCustomFieldDeleteDialogComponent,
        TMSCustomFieldPopupComponent,
        TMSCustomFieldCreateComponent,
        TMSCustomFieldDeletePopupComponent,
    ],
    entryComponents: [
        TMSCustomFieldComponent,
        TMSCustomFieldDialogComponent,
        TMSCustomFieldPopupComponent,
        TMSCustomFieldCreateComponent,
        TMSCustomFieldDeleteDialogComponent,
        TMSCustomFieldDeletePopupComponent,
    ],
    providers: [
        TMSCustomFieldService,
        TMSCustomFieldPopupService,
        TitleCasePipe,
        TMSCustomFieldResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DtmsTMSCustomFieldModule {}
