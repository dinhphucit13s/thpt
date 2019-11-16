import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DtmsSharedModule } from '../shared';
import {NgMultiSelectDropDownModule} from 'ng-multiselect-dropdown';
import { HOME_ROUTE, HomeComponent, WorkFlowResolvePagingParams } from './';
import {HomeService} from './home.service';
import {HomeSubItemsComponent} from './home-sub-items/home-sub-items.component';

@NgModule({
    imports: [
        DtmsSharedModule,
        RouterModule.forChild([HOME_ROUTE]),
        NgMultiSelectDropDownModule.forRoot()
    ],
    declarations: [
        HomeComponent,
        HomeSubItemsComponent
    ],
    entryComponents: [
    ],
    providers: [
        HomeService,
        WorkFlowResolvePagingParams
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DtmsHomeModule { }
