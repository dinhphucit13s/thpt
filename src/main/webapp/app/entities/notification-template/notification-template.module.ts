import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DtmsSharedModule } from '../../shared';
import {
    NotificationTemplateService,
    NotificationTemplatePopupService,
    NotificationTemplateComponent,
    NotificationTemplateDetailComponent,
    NotificationTemplateDialogComponent,
    NotificationTemplatePopupComponent,
    NotificationTemplateDeletePopupComponent,
    NotificationTemplateDeleteDialogComponent,
    notificationTemplateRoute,
    notificationTemplatePopupRoute,
    NotificationTemplateResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...notificationTemplateRoute,
    ...notificationTemplatePopupRoute,
];

@NgModule({
    imports: [
        DtmsSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        NotificationTemplateComponent,
        NotificationTemplateDetailComponent,
        NotificationTemplateDialogComponent,
        NotificationTemplateDeleteDialogComponent,
        NotificationTemplatePopupComponent,
        NotificationTemplateDeletePopupComponent,
    ],
    entryComponents: [
        NotificationTemplateComponent,
        NotificationTemplateDialogComponent,
        NotificationTemplatePopupComponent,
        NotificationTemplateDeleteDialogComponent,
        NotificationTemplateDeletePopupComponent,
    ],
    providers: [
        NotificationTemplateService,
        NotificationTemplatePopupService,
        NotificationTemplateResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DtmsNotificationTemplateModule {}
