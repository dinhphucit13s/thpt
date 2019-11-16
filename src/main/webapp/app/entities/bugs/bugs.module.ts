import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DtmsSharedModule } from '../../shared';
import {
    BugsService,
    BugsPopupService,
    BugsComponent,
    BugsDetailComponent,
    BugsDialogComponent,
    BugsPopupComponent,
    BugsDeletePopupComponent,
    BugsDeleteDialogComponent,
    bugsRoute,
    bugsPopupRoute,
    BugsResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...bugsRoute,
    ...bugsPopupRoute,
];

@NgModule({
    imports: [
        DtmsSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        BugsComponent,
        BugsDetailComponent,
        BugsDialogComponent,
        BugsDeleteDialogComponent,
        BugsPopupComponent,
        BugsDeletePopupComponent,
    ],
    entryComponents: [
        BugsComponent,
        BugsDialogComponent,
        BugsPopupComponent,
        BugsDeleteDialogComponent,
        BugsDeletePopupComponent,
    ],
    providers: [
        BugsService,
        BugsPopupService,
        BugsResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DtmsBugsModule {}
