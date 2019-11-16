import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { DtmsSharedModule } from '../../shared';
import { NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';
import { Ng2ImgMaxModule } from 'ng2-img-max';
import {
    ProjectTemplatesService,
    ProjectTemplatesPopupService,
    ProjectTemplatesComponent,
    ProjectTemplatesDetailComponent,
    ProjectTemplatesDialogComponent,
    ProjectTemplatesPopupComponent,
    ProjectTemplatesDeletePopupComponent,
    ProjectTemplatesDeleteDialogComponent,
    projectTemplatesRoute,
    projectTemplatesPopupRoute,
    ProjectTemplatesResolvePagingParams,
} from './';
import {NgMultiSelectDropDownModule} from "ng-multiselect-dropdown";

const ENTITY_STATES = [
    ...projectTemplatesRoute,
    ...projectTemplatesPopupRoute,
];

@NgModule({
    imports: [
        DtmsSharedModule,
        RouterModule.forChild(ENTITY_STATES),
        Ng2ImgMaxModule,
        NgMultiSelectDropDownModule.forRoot()
    ],
    declarations: [
        ProjectTemplatesComponent,
        ProjectTemplatesDetailComponent,
        ProjectTemplatesDialogComponent,
        ProjectTemplatesDeleteDialogComponent,
        ProjectTemplatesPopupComponent,
        ProjectTemplatesDeletePopupComponent,
    ],
    entryComponents: [
        ProjectTemplatesComponent,
        ProjectTemplatesDialogComponent,
        ProjectTemplatesPopupComponent,
        ProjectTemplatesDeleteDialogComponent,
        ProjectTemplatesDeletePopupComponent,
    ],
    providers: [
        ProjectTemplatesService,
        ProjectTemplatesPopupService,
        ProjectTemplatesResolvePagingParams,
        NgbActiveModal,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DtmsProjectTemplatesModule {}
