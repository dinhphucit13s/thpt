import { Component, OnInit, OnDestroy } from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiDataUtils } from 'ng-jhipster';

import { TMSCustomField } from './tms-custom-field.model';
import { TMSCustomFieldPopupService } from './tms-custom-field-popup.service';
import { TMSCustomFieldService } from './tms-custom-field.service';
import {TMSCustomFieldDialogComponent} from './tms-custom-field-dialog.component';
import {FieldConfig, FieldDataConfig} from '../../shared/dynamic-forms/field.interface';
import {Properties} from '../../shared/services/properties.model';
import {Subscription} from 'rxjs/Subscription';
import {AppConstants} from '../../shared/services/app-constants';

@Component({
    selector: 'jhi-tms-custom-field-create',
    templateUrl: './tms-custom-field-create.component.html'
})
export class TMSCustomFieldCreateComponent implements OnInit {
    tMSCustomFields: TMSCustomField[];
    tMSCustomField: TMSCustomField;
    customFieldId: any;
    isSaving: boolean;
    showField: boolean;
    showOptions: boolean;
    typeValueDefault: boolean;
    eventSubscriber: Subscription;
    nameExists: boolean;
    columnPackages: any;
    columnTasks: any;

    constructor(
        private router: Router,
        public activeModal: NgbActiveModal,
        private dataUtils: JhiDataUtils,
        private activatedRoute: ActivatedRoute,
        private tMSCustomFieldService: TMSCustomFieldService,
        private eventManager: JhiEventManager
    ) {
        this.tMSCustomField = new TMSCustomField();
        this.tMSCustomField.entityData = new FieldDataConfig();
    }

    ngOnInit() {
        this.activatedRoute.params.subscribe((parmas) => {
            this.customFieldId = parmas['id'];
            if (this.customFieldId !== undefined) {
                this.tMSCustomFieldService.find(this.customFieldId).subscribe((tMSCustomFieldResponse: HttpResponse<TMSCustomField>) => {
                    this.tMSCustomField = tMSCustomFieldResponse.body;
                    this.tMSCustomField.entityData = JSON.parse(this.tMSCustomField.entityData);
                    this.mergeProperties();
                    this.showFieldByType();
                    this.checkShowOptions();
                });
            }
        });
        this.isSaving = false;
        this.showField = false;
        this.showOptions = true;
        this.typeValueDefault = true;
    }
    checkShowOptions() {
        if (this.tMSCustomField.entityData.urlOption) {
            this.showOptions = false;
        } else {
            this.showOptions = true;
        }
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
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        this.setTypeOfField();
        if (this.tMSCustomField.id !== undefined) {
            this.subscribeToSaveResponse(
                this.tMSCustomFieldService.update(this.tMSCustomField));
        } else {
            this.subscribeToSaveResponse(
                this.tMSCustomFieldService.create(this.tMSCustomField));
        }
    }
    // set type and inputType
    setTypeOfField() {
        this.tMSCustomField.entityData.format = null;
        if (this.tMSCustomField.entityData.urlOption) {
            this.tMSCustomField.entityData.urlOption = this.tMSCustomField.entityData.urlOption.replace(/\s/g, '');
        }
        if (this.tMSCustomField.entityData.urlOption !== null && this.tMSCustomField.entityData.urlOption !== '') {
            this.tMSCustomField.entityData.options = null;
        }
        if (this.tMSCustomField.entityData.reuseData !== null && this.tMSCustomField.entityData.reuseData !== '') {
            this.tMSCustomField.entityData.urlOption = null;
            this.tMSCustomField.entityData.options = null;
        }
        if (this.tMSCustomField.entityData.type === 'STRING') {
            this.tMSCustomField.entityData.inputType = 'text';
            this.tMSCustomField.entityData.type = 'input';
            this.valueDefaultOfSelect();
        } else if (this.tMSCustomField.entityData.type === 'NUMERIC') {
            this.tMSCustomField.entityData.inputType = 'number';
            this.tMSCustomField.entityData.type = 'input';
            this.valueDefaultOfSelect();
        } else if (this.tMSCustomField.entityData.type === 'EMAIL') {
            this.tMSCustomField.entityData.inputType = this.tMSCustomField.entityData.type.toLowerCase();
            this.tMSCustomField.entityData.type = 'input';
            this.valueDefaultOfSelect();
        } else if (this.tMSCustomField.entityData.type === 'PASSWORD') {
            this.tMSCustomField.entityData.inputType = this.tMSCustomField.entityData.type.toLowerCase();
            this.tMSCustomField.entityData.type = 'input';
            this.valueDefaultOfSelect();
        } else if (this.tMSCustomField.entityData.type === 'LIST') {
            this.tMSCustomField.entityData.type = 'select';
            this.tMSCustomField.entityData.inputType = 'single';
        } else if (this.tMSCustomField.entityData.type === 'MULTI_SELECTION_LIST') {
            this.tMSCustomField.entityData.type = 'select';
            this.tMSCustomField.entityData.inputType = 'multiple';
        } else if (this.tMSCustomField.entityData.type === 'SHORT_DATE') {
            this.tMSCustomField.entityData.format = 'date';
            this.tMSCustomField.entityData.type = 'date';
            this.tMSCustomField.entityData.inputType = null;
            this.valueDefaultOfSelect();
        } else if (this.tMSCustomField.entityData.type === 'LONG_DATE') {
            this.tMSCustomField.entityData.type = 'date';
            this.tMSCustomField.entityData.format = 'datetime';
            this.tMSCustomField.entityData.inputType = null;
            this.valueDefaultOfSelect();
        } else {
            this.tMSCustomField.entityData.type = this.tMSCustomField.entityData.type.toLowerCase();
        }
    }
    valueDefaultOfSelect() {
        this.tMSCustomField.entityData.options = null;
        this.tMSCustomField.entityData.urlOption = null;
        this.tMSCustomField.entityData.reuseData = null;
    }
    // two biding value
    mergeProperties() {
        if (this.tMSCustomField.entityData.inputType === 'text') {
            this.tMSCustomField.entityData.type = 'STRING';
        } else if (this.tMSCustomField.entityData.inputType === 'number') {
            this.tMSCustomField.entityData.type = 'NUMERIC';
        } else if (this.tMSCustomField.entityData.inputType === 'password') {
            this.tMSCustomField.entityData.type = 'PASSWORD';
        } else if (this.tMSCustomField.entityData.inputType === 'email') {
            this.tMSCustomField.entityData.type = 'EMAIL';
        } else if (this.tMSCustomField.entityData.inputType === 'single') {
            this.tMSCustomField.entityData.type = 'LIST';
        } else if (this.tMSCustomField.entityData.inputType === 'multiple') {
            this.tMSCustomField.entityData.type = 'MULTI_SELECTION_LIST';
        } else if (this.tMSCustomField.entityData.format === 'date') {
            this.tMSCustomField.entityData.type = 'SHORT_DATE';
        } else if (this.tMSCustomField.entityData.format === 'datetime') {
            this.tMSCustomField.entityData.type = 'LONG_DATE';
        } else {
            this.tMSCustomField.entityData.type = this.tMSCustomField.entityData.type.toUpperCase();
        }
    }
    // show field by type
    showFieldByType() {
        // this.tMSCustomField.entityData.value = null;
        if (this.tMSCustomField.entityData.type === 'LIST' || this.tMSCustomField.entityData.type === 'MULTI_SELECTION_LIST') {
            this.showField = true;
        } else {
            this.showField = false;
        }
        // check type
        if (this.tMSCustomField.entityData.type !== 'NUMERIC' && this.tMSCustomField.entityData.type !== 'FLOAT' && this.tMSCustomField.entityData.type !== 'EMAIL'
             && this.tMSCustomField.entityData.type !== 'PASSWORD') {
            this.typeValueDefault = true;
        } else {
            this.typeValueDefault = false;
        }
    }
    private checkHeaderNameExists() {
        this.nameExists = false;
        this.columnPackages = AppConstants.PackageItems;
        this.columnTasks = AppConstants.TaskItems;
        const index = this.columnPackages.findIndex((field) =>
            field.headerName.toLowerCase().replace(/\s/g, '') === this.tMSCustomField.entityData.headerName.toLowerCase().replace(/\s/g, ''));
        const indexTask = this.columnTasks.findIndex((field) => field.headerName.replace(/\s/g, '') === this.tMSCustomField.entityData.headerName.replace(/\s/g, ''));
        this.tMSCustomFields.forEach((field) => {
            if (field.entityData.headerName.toLowerCase().replace(/\s/g, '') === this.tMSCustomField.entityData.headerName.toLowerCase().replace(/\s/g, '')) {
                this.nameExists = true;
            }
        });
        if (index >= 0 || indexTask >= 0) {
            this.nameExists = true;
        }
    }
    // show value format default
    valueOptionDefault() {
        if (this.tMSCustomField.entityData.options === null || this.tMSCustomField.entityData.options === '') {
            this.tMSCustomField.entityData.options = '[\'NORMAL\', \'MEDIUM\', \'HIGH\']';
        }
    }
    private subscribeToSaveResponse(result: Observable<HttpResponse<TMSCustomField>>) {
        result.subscribe((res: HttpResponse<TMSCustomField>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: TMSCustomField) {
        this.router.navigate(['/tms-custom-field']);
        this.eventManager.broadcast({ name: 'tMSCustomFieldListModification', content: 'OK'});
        this.isSaving = false;
        // this.activeModal.dismiss(result);
    }

    previousState() {
        window.history.back();
    }

    private onSaveError() {
        this.isSaving = false;
    }
}
