import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { OwlDateTimeModule, OwlNativeDateTimeModule } from 'ng-pick-datetime';
import { DtmsSharedModule } from '../../shared';
import { NgMultiSelectDropDownModule } from 'ng-multiselect-dropdown';
import { MailShowAttachmentComponent } from './mail-show-attachment/mail-show-attachment.component';
import {
    MailService,
    MailPopupService,
    MailComponent,
    MailDetailComponent,
    MailDialogComponent,
    MailPopupComponent,
    MailDeletePopupComponent,
    MailDeleteDialogComponent,
    mailRoute,
    mailPopupRoute,
} from './';
import {FlatpickrModule} from 'angularx-flatpickr';
import {FormsModule} from '@angular/forms';
import { ResizableDirective } from './resizable.directive';
// import {NgSelectModule} from '@ng-select/ng-select';

const ENTITY_STATES = [
    ...mailRoute,
    ...mailPopupRoute,
];

@NgModule({
    imports: [
        FormsModule,
        FlatpickrModule.forRoot(),
        NgMultiSelectDropDownModule.forRoot(),
        OwlDateTimeModule,
        OwlNativeDateTimeModule,
        DtmsSharedModule,
        RouterModule.forChild(ENTITY_STATES),
        /*NgSelectModule*/
    ],
    declarations: [
        MailShowAttachmentComponent,
        MailComponent,
        MailDetailComponent,
        MailDialogComponent,
        MailDeleteDialogComponent,
        MailPopupComponent,
        MailDeletePopupComponent,
        ResizableDirective,
    ],
    entryComponents: [
        MailShowAttachmentComponent,
        MailComponent,
        MailDialogComponent,
        MailPopupComponent,
        MailDeleteDialogComponent,
        MailDeletePopupComponent,
    ],
    providers: [
        MailService,
        MailPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DtmsMailModule {}
