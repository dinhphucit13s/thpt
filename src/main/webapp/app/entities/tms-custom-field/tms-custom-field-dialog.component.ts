import {Component, OnInit, OnDestroy, EventEmitter, Inject} from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiDataUtils } from 'ng-jhipster';

import { TMSCustomField } from './tms-custom-field.model';
import { TMSCustomFieldScreen } from '../tms-custom-field-screen/tms-custom-field-screen.model';
import { TMSCustomFieldPopupService } from './tms-custom-field-popup.service';
import { TMSCustomFieldService } from './tms-custom-field.service';
import { Validator } from '../../shared/dynamic-forms/field.interface';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material';

@Component({
    selector: 'jhi-tms-custom-field-dialog',
    templateUrl: './tms-custom-field-dialog.component.html',
    styleUrls: ['./tms-custom-field.component.css']
})
export class TMSCustomFieldDialogComponent implements OnInit {
    onNewField = new EventEmitter();
    closePopup = new EventEmitter();
    tMSCustomField: TMSCustomField;
    isSaving: boolean;
    validatePattern: Validator;
    validateMaxLength: Validator;
    validateMinLength: Validator;
    validateMax: Validator;
    validateMin: Validator;
    validLength: boolean;
    validValue: boolean;
    validOff: boolean;
    pageName: any;

    constructor(
        public activeModal: NgbActiveModal,
        private dataUtils: JhiDataUtils,
        private tMSCustomFieldService: TMSCustomFieldService,
        private eventManager: JhiEventManager,
        @Inject(MAT_DIALOG_DATA) public data: any
    ) {
        this.validatePattern = new Validator('pattern');
        this.validateMaxLength = new Validator('maxlength');
        this.validateMinLength = new Validator('minlength');
        this.validateMax = new Validator('max');
        this.validateMin = new Validator('min');
        this.validLength = false;
        this.validValue = false;
        this.validOff = false;
    }

    ngOnInit() {
        this.isSaving = false;
        this.mapperValidate();
        this.showHiddenFieldByType();
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }

    setFileData(event, entity, field, isImage) {
        this.dataUtils.setFileData(event, entity, field, isImage);
    }

    clear() {
        this.closePopup.emit();
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        this.addValidations();
        this.onNewField.emit(this.tMSCustomField);
    }
    mapperValidate() {
        if (this.tMSCustomField.entityData.entityPropertiesView.validations && this.tMSCustomField.entityData.entityPropertiesView.validations.length > 0) {
            this.tMSCustomField.entityData.entityPropertiesView.validations.forEach((valid) => {
                if (valid.name === 'pattern') {
                    this.validatePattern = valid;
                }
                if (valid.name === 'maxlength') {
                    this.validateMaxLength = valid;
                }
                if (valid.name === 'minlength') {
                    this.validateMinLength = valid;
                }
                if (valid.name === 'max') {
                    this.validateMax = valid;
                }
                if (valid.name === 'min') {
                    this.validateMin = valid;
                }
            });
        }
    }
    addValidations() {
        if (this.validatePattern.validator) {
            this.validatePattern.message = this.validatePattern.validator;
            this.checkValidations(this.validatePattern);
        }
        if (this.validateMaxLength.validator) {
            this.validateMaxLength.message = this.validateMaxLength.validator;
            this.checkValidations(this.validateMaxLength);
        }
        if (this.validateMinLength.validator) {
            this.validateMinLength.message = this.validateMinLength.validator;
            this.checkValidations(this.validateMinLength);
        }
        if (this.validateMax.validator) {
            this.validateMax.message = this.validateMax.validator;
            this.checkValidations(this.validateMax);
        }
        if (this.validateMin.validator) {
            this.validateMin.message = this.validateMin.validator;
            this.checkValidations(this.validateMin);
        }
    }
    checkValidations(validate?: any) {
        const index = this.tMSCustomField.entityData.entityPropertiesView.validations.findIndex((valid) => valid.name === validate.name);
        if (index < 0) {
            this.tMSCustomField.entityData.entityPropertiesView.validations.push(validate);
        }
    }
    showHiddenFieldByType() {
        if (this.tMSCustomField.entityData.entityPropertiesData.inputType === 'text' || this.tMSCustomField.entityData.entityPropertiesData.type === 'textarea'
        || this.tMSCustomField.entityData.entityPropertiesData.inputType === 'password') {
            this.validLength = true;
            this.validOff = true;
        } else if (this.tMSCustomField.entityData.entityPropertiesData.inputType === 'number' || this.tMSCustomField.entityData.entityPropertiesData.type === 'date') {
            this.validValue = true;
            this.validOff = false;
        } else {
            // this.validOff = false;
        }
    }
    private subscribeToSaveResponse(result: Observable<HttpResponse<TMSCustomField>>) {
        result.subscribe((res: HttpResponse<TMSCustomField>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: TMSCustomField) {
        this.eventManager.broadcast({ name: 'tMSCustomPropertiesFieldModification', content: result});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }
}

@Component({
    selector: 'jhi-tms-custom-field-popup',
    template: ''
})
export class TMSCustomFieldPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private tMSCustomFieldPopupService: TMSCustomFieldPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.tMSCustomFieldPopupService
                    .open(TMSCustomFieldDialogComponent as Component, params['id']);
            } else if ( params['screenId'] ) {
                this.tMSCustomFieldPopupService
                    .openEdit(TMSCustomFieldDialogComponent as Component, params['screenId']);
            } else {
                this.tMSCustomFieldPopupService
                    .open(TMSCustomFieldDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
