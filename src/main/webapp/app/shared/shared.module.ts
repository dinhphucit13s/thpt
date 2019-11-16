import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import {CommonModule, DatePipe} from '@angular/common';
import { NgMultiSelectDropDownModule } from 'ng-multiselect-dropdown';
import { OwlDateTimeModule, OwlNativeDateTimeModule} from 'ng-pick-datetime';
import {
    DtmsSharedLibsModule,
    DtmsSharedCommonModule,
    CSRFService,
    AuthServerProvider,
    AccountService,
    UserService,
    StateStorageService,
    LoginService,
    LoginModalService,
    JhiLoginModalComponent,
    Principal,
    JhiTrackerService,
    HasAnyAuthorityDirective,
} from './';
import { MatToolbarModule , MatInputModule , MatTableModule, MatButtonModule, MatPaginatorModule, MatSortModule, MatFormFieldModule, MatRadioModule
    , MatCheckboxModule, MatSelectModule, MatOptionModule, MatIconModule, MatCardModule, MatListModule, MatDatepickerModule, MatNativeDateModule, MatAutocompleteModule,
    MatButtonToggleModule,
    MatChipsModule,
    MatDialogModule,
    MatDividerModule,
    MatExpansionModule,
    MatGridListModule,
    MatMenuModule,
    MatProgressBarModule,
    MatProgressSpinnerModule,
    MatRippleModule,
    MatSidenavModule,
    MatSliderModule,
    MatSlideToggleModule,
    MatSnackBarModule,
    MatStepperModule,
    MatTabsModule,
    MatTooltipModule} from '@angular/material';
import { WorkflowItemsComponent } from './workflow-items/workflow-items.component';
import { WorkflowItemComponent } from './workflow-items/workflow-item.component';
import { FilterDirective } from './auth/filter.directive';

import { ButtonComponent } from './dynamic-forms/button/button.component';
import { ButtonBiddingComponent } from './dynamic-data-tables/button-bidding/button-bidding.component';
import { ButtonViewComponent } from './dynamic-data-tables/buttons-view/button-view.component';
import { NumericEditorComponent } from './dynamic-data-tables/numeric-editor/numeric-editor.component';
import { InputComponent } from './dynamic-forms/input/input.component';
import { TextareaComponent } from './dynamic-forms/textarea/textarea.component';
import { SelectComponent } from './dynamic-forms/select/select.component';
import { CheckboxComponent } from './dynamic-forms/checkbox/checkbox.component';
import { DateComponent } from './dynamic-forms/date/date.component';
import { DynamicFieldDirective } from './dynamic-forms/dynamic-field/dynamic-field.directive';
import { RadiobuttonComponent } from './dynamic-forms/radiobutton/radiobutton.component';
import { DynamicFormComponent } from './dynamic-forms/dynamic-form/dynamic-form.component';
import { DataCollectionComponent } from './dynamic-data-tables/data-table.component';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import {WorkflowActivityComponent} from './workflow-items/workflow-activity.component';
import {WorkflowProcessComponent} from './workflow-items/workflow-process.component';
import {RouterModule} from '@angular/router';
import { LightboxModule } from 'ngx-lightbox';
import { AgGridModule } from 'ag-grid-angular';
import {FlatpickrModule} from 'angularx-flatpickr';
import { NgxSpinnerModule } from 'ngx-spinner';
@NgModule({
    imports: [
        DtmsSharedLibsModule,
        DtmsSharedCommonModule,
        ReactiveFormsModule,
        OwlDateTimeModule,
        OwlNativeDateTimeModule,
        CommonModule,
        FormsModule,
        FlatpickrModule.forRoot(),
        MatToolbarModule,
        MatInputModule,
        MatTableModule,
        MatButtonModule,
        MatPaginatorModule,
        MatSortModule,
        MatFormFieldModule,
        MatCheckboxModule,
        MatSelectModule,
        MatOptionModule,
        MatIconModule,
        MatCardModule,
        MatListModule,
        MatDatepickerModule,
        MatNativeDateModule,
        MatRadioModule,
        MatButtonToggleModule,
        MatChipsModule,
        MatDialogModule,
        MatDividerModule,
        MatExpansionModule,
        MatGridListModule,
        MatMenuModule,
        MatProgressBarModule,
        MatProgressSpinnerModule,
        MatRippleModule,
        MatSidenavModule,
        MatSliderModule,
        MatSlideToggleModule,
        MatSnackBarModule,
        MatStepperModule,
        MatTabsModule,
        MatTooltipModule,
        RouterModule,
        LightboxModule,
        AgGridModule.withComponents([ButtonViewComponent, NumericEditorComponent]),
        NgMultiSelectDropDownModule.forRoot(),
        NgxSpinnerModule,
    ],
    declarations: [
        JhiLoginModalComponent,
        HasAnyAuthorityDirective,
        FilterDirective,
        WorkflowItemsComponent,
        WorkflowItemComponent,
        WorkflowActivityComponent,
        ButtonComponent,
        ButtonViewComponent,
        ButtonBiddingComponent,
        NumericEditorComponent,
        InputComponent,
        TextareaComponent,
        SelectComponent,
        DateComponent,
        CheckboxComponent,
        DynamicFormComponent,
        DynamicFieldDirective,
        RadiobuttonComponent,
        DataCollectionComponent,
        WorkflowProcessComponent,
    ],
    providers: [
        LoginService,
        LoginModalService,
        AccountService,
        StateStorageService,
        DataCollectionComponent,
        Principal,
        CSRFService,
        JhiTrackerService,
        AuthServerProvider,
        UserService,
        DatePipe,
        DynamicFieldDirective
    ],
    entryComponents: [
        JhiLoginModalComponent,
        ButtonComponent,
        ButtonViewComponent,
        ButtonBiddingComponent,
        NumericEditorComponent,
        InputComponent,
        TextareaComponent,
        SelectComponent,
        DateComponent,
        CheckboxComponent,
        RadiobuttonComponent,
        DynamicFormComponent
    ],
    exports: [
        DtmsSharedCommonModule,
        JhiLoginModalComponent,
        HasAnyAuthorityDirective,
        DatePipe,
        FilterDirective,
        WorkflowItemsComponent,
        WorkflowItemComponent,
        WorkflowActivityComponent,
        ButtonComponent,
        ButtonViewComponent,
        ButtonBiddingComponent,
        NumericEditorComponent,
        InputComponent,
        TextareaComponent,
        SelectComponent,
        DateComponent,
        CheckboxComponent,
        RadiobuttonComponent,
        DataCollectionComponent,
        WorkflowProcessComponent,
        DynamicFieldDirective,
        MatToolbarModule,
        MatInputModule,
        MatTableModule,
        MatButtonModule,
        MatPaginatorModule,
        MatSortModule,
        MatFormFieldModule,
        MatCheckboxModule,
        MatSelectModule,
        MatOptionModule,
        MatIconModule,
        MatCardModule,
        MatListModule,
        MatDatepickerModule,
        MatNativeDateModule,
        MatRadioModule,
        MatButtonToggleModule,
        MatChipsModule,
        MatDialogModule,
        MatDividerModule,
        MatExpansionModule,
        MatGridListModule,
        MatMenuModule,
        MatProgressBarModule,
        MatProgressSpinnerModule,
        MatRippleModule,
        MatSidenavModule,
        MatSliderModule,
        MatSlideToggleModule,
        MatSnackBarModule,
        MatStepperModule,
        MatTabsModule,
        MatTooltipModule,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]

})
export class DtmsSharedModule {}
