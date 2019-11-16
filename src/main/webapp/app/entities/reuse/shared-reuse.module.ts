import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DtmsSharedModule } from '../../shared';
import { CommonModule } from '@angular/common';

import {
    ReuseBugsComponent
} from './';

@NgModule({
    imports: [
        CommonModule,
        RouterModule,
        DtmsSharedModule,
    ],
    exports: [
        ReuseBugsComponent
    ],
    declarations: [
        ReuseBugsComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DtmsSharedBugsModule {}
