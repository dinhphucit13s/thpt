import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { OwlDateTimeModule, OwlNativeDateTimeModule } from 'ng-pick-datetime';
import { DtmsSharedModule } from '../../shared';
import { TrackingManagementResolvePagingParams } from './tracking-management.route';
import {
    trackingManagementRoute,
    TrackingManagementComponent,
    TrackingManagementService,
    TrackingManagementDetailComponent
} from './';
import {DataCollectionComponent} from '../../shared/dynamic-data-tables/data-table.component';
import {FlatpickrModule} from 'angularx-flatpickr';
import {NgSelectModule} from '@ng-select/ng-select';
import { NgxSpinnerModule } from 'ngx-spinner';
const ENTITY_STATES = [
    ...trackingManagementRoute,
];

@NgModule({
    imports: [
        DtmsSharedModule,
        OwlDateTimeModule,
        OwlNativeDateTimeModule,
        FlatpickrModule.forRoot(),
        RouterModule.forChild(ENTITY_STATES),
        NgSelectModule,
        NgxSpinnerModule,
    ],
    declarations: [
        TrackingManagementComponent,
        TrackingManagementDetailComponent
    ],
    entryComponents: [
        TrackingManagementComponent,
        TrackingManagementDetailComponent,
        DataCollectionComponent
    ],
    providers: [
        TrackingManagementService,
        TrackingManagementResolvePagingParams
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DtmsTrackingManagementModule {}
