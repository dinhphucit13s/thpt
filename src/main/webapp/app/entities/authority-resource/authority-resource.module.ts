import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DtmsSharedModule } from '../../shared';
import {
    AuthorityResourceService,
    AuthorityResourcePopupService,
    AuthorityResourceComponent,
    AuthorityResourceDetailComponent,
    AuthorityResourceDialogComponent,
    AuthorityResourcePopupComponent,
    AuthorityResourceDeletePopupComponent,
    AuthorityResourceDeleteDialogComponent,
    authorityResourceRoute,
    authorityResourcePopupRoute,
    AuthorityResourceResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...authorityResourceRoute,
    ...authorityResourcePopupRoute,
];

@NgModule({
    imports: [
        DtmsSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        AuthorityResourceComponent,
        AuthorityResourceDetailComponent,
        AuthorityResourceDialogComponent,
        AuthorityResourceDeleteDialogComponent,
        AuthorityResourcePopupComponent,
        AuthorityResourceDeletePopupComponent,
    ],
    entryComponents: [
        AuthorityResourceComponent,
        AuthorityResourceDialogComponent,
        AuthorityResourcePopupComponent,
        AuthorityResourceDeleteDialogComponent,
        AuthorityResourceDeletePopupComponent,
    ],
    providers: [
        AuthorityResourceService,
        AuthorityResourcePopupService,
        AuthorityResourceResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DtmsAuthorityResourceModule {}
