import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { OwlDateTimeModule, OwlNativeDateTimeModule } from 'ng-pick-datetime';
import { DtmsSharedModule } from '../../shared';
import { EffortResolvePagingParams } from './effort.route';
import {
    effortRoute,
    EffortComponent,
    EffortService
} from './';
import {DataCollectionComponent} from '../../shared/dynamic-data-tables/data-table.component';
const ENTITY_STATES = [
    ...effortRoute,
];

@NgModule({
    imports: [
        DtmsSharedModule,
        OwlDateTimeModule,
        OwlNativeDateTimeModule,
        RouterModule.forChild(ENTITY_STATES),
    ],
    declarations: [
        EffortComponent
    ],
    entryComponents: [
        EffortComponent,
        DataCollectionComponent
    ],
    providers: [
        EffortService,
        EffortResolvePagingParams
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DtmsEffortModule {}
