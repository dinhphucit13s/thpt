import {Component, OnInit, OnDestroy, ViewContainerRef, ViewChild, ComponentFactory, ComponentFactoryResolver} from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService, JhiDataUtils } from 'ng-jhipster';

import { Tasks } from './tasks.model';
import { TasksPopupService } from './tasks-popup.service';
import { TasksService } from './tasks.service';
import { Packages, PackagesService } from '../packages';
import {DynamicFieldConfig, FieldConfig} from '../../shared/dynamic-forms/field.interface';
import {DynamicFormComponent} from '../../shared/dynamic-forms/dynamic-form/dynamic-form.component';
import {ProjectWorkflows} from '../project-workflows/project-workflows.model';
import { ProjectWorkflowsService } from '../project-workflows/project-workflows.service';
import {LocalStorage} from 'ngx-webstorage';
import {AppConstants} from '../../shared/services/app-constants';
import {TMSCustomFieldScreen} from '../tms-custom-field-screen/tms-custom-field-screen.model';
import {TMSCustomFieldScreenValue} from '../tms-custom-field-screen-value';
declare var jquery: any;
declare var $: any;
@Component({
    selector: 'jhi-tasks-dialog',
    templateUrl: './tasks-dialog.component.html'
})
export class TasksDialogComponent implements OnInit {

    tasks: Tasks;
    isSaving: boolean;
    initFieldConfig: any;
    templateId: any;
    formStringify: any;
    tam: any;
    purchaseId: number;
    package: Packages;
    dateFormat = require('dateformat');

    form: DynamicFormComponent;
    @ViewChild('child', {read: ViewContainerRef}) myChild;
    formComponentFactory: ComponentFactory<DynamicFormComponent>;
    regConfig: FieldConfig[] = new Array<FieldConfig>();
    tmsCustomFieldScreens: TMSCustomFieldScreen[];
    dynamicFieldConfig: DynamicFieldConfig;

    packages: Packages[];

    constructor(
        public activeModal: NgbActiveModal,
        private dataUtils: JhiDataUtils,
        private packageService: PackagesService,
        private jhiAlertService: JhiAlertService,
        private tasksService: TasksService,
        private packagesService: PackagesService,
        private eventManager: JhiEventManager,
        private componentFactoryResolver: ComponentFactoryResolver,
        private projectWorkflowsService: ProjectWorkflowsService
    ) {
    }

    ngOnInit() {
        this.purchaseId = parseInt(sessionStorage.getItem('currentPurchaseOrderId'), 10);
        this.getFieldsDisplay();
        this.isSaving = false;
    }

    getFieldsDisplay() {
        this.tasksService.loadTaskDynamicColumn(this.purchaseId).subscribe((res: HttpResponse<any>) => {
            this.dynamicFieldConfig = res.body;
            this.regConfig = this.dynamicFieldConfig.fieldConfigVMs;
            this.tmsCustomFieldScreens = this.dynamicFieldConfig.tmsCustomFieldScreenDTOs;
            this.regConfig.forEach((field) => {
                field.value = this.tasks[field.field];
            });
            // add columns dynamic field and two binding
            if (this.tmsCustomFieldScreens !== undefined && this.tmsCustomFieldScreens.length > 0) {
                this.tmsCustomFieldScreens.forEach((tmsCustomField) => {
                    tmsCustomField.entityGridPm = JSON.parse(tmsCustomField.entityGridPm);
                    if (tmsCustomField.entityGridPm.length > 0) {
                        tmsCustomField.entityData = JSON.parse(tmsCustomField.entityData);
                        tmsCustomField.entityGridInput = JSON.parse(tmsCustomField.entityGridInput);
                        tmsCustomField.entityGridInput = tmsCustomField.entityGridInput[0];
                        const index = this.tasks.tmsCustomFieldScreenValueDTO.findIndex((fieldValue) => fieldValue.tmsCustomFieldScreenId === tmsCustomField.id);
                        if (index >= 0) {
                            if (tmsCustomField.entityData.type !== null && tmsCustomField.entityData.type.toLowerCase() === 'textarea') {
                                tmsCustomField.entityData.value = this.tasks.tmsCustomFieldScreenValueDTO[index].text;
                            } else {
                                if (tmsCustomField.entityData.type !== null && tmsCustomField.entityData.type.toLowerCase() === 'number') {
                                    tmsCustomField.entityData.value = parseInt(this.tasks.tmsCustomFieldScreenValueDTO[index].value, 10);
                                } else {
                                    tmsCustomField.entityData.value = this.tasks.tmsCustomFieldScreenValueDTO[index].value;
                                }
                            }
                        }
                        const copy: FieldConfig = Object.assign(tmsCustomField.entityData, tmsCustomField.entityGridInput);
                        this.regConfig.push(copy);
                    }
                });
            }
            this.formComponentFactory = this.componentFactoryResolver.resolveComponentFactory(DynamicFormComponent);
            let ref = this.myChild.createComponent(this.formComponentFactory);
            this.form = ref.instance;
            ref.instance.fields = this.regConfig;
        });
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
        this.formStringify = JSON.stringify(this.form.value);
        this.initFieldConfig = JSON.parse(this.formStringify);
        this.regConfig.forEach((item) => {
            if (item.type === 'date' && this.initFieldConfig[item.field] !== null) {
                this.tasks[item.field] = this.dateFormat(this.initFieldConfig[item.field], "yyyy-mm-dd'T'HH:MM:ss");
            } else {
                this.tasks[item.field] = this.initFieldConfig[item.field];
            }
            // set data output for field
            this.tmsCustomFieldScreens = this.tmsCustomFieldScreens.filter((tmsCustomDTO) => tmsCustomDTO.entityGridPm.length > 0)
            const index = this.tmsCustomFieldScreens.findIndex((field) => field.entityData.field === item.field);
            if (index >= 0) {
                this.tmsCustomFieldScreens[index].entityData.value = this.initFieldConfig[item.field];
            }
        });
        this.tmsCustomFieldScreens.forEach((field) => {
            this.setDataCustomScreenValue(field);
        });
        this.isSaving = true;
        if (this.tasks.id) {
            this.subscribeToSaveResponse(
                this.tasksService.update(this.tasks));
        } else {
            this.subscribeToSaveResponse(
                this.tasksService.create(this.tasks));
        }
    }
    // Save field value
    private setDataCustomScreenValue(tmsCustomScreen: TMSCustomFieldScreen) {
        const index = this.tasks.tmsCustomFieldScreenValueDTO.findIndex((ScreenField) => ScreenField.tmsCustomFieldScreenId === tmsCustomScreen.id);
        if (index < 0) {
            const tmsCustomFieldScreenValue: TMSCustomFieldScreenValue = new TMSCustomFieldScreenValue();
            tmsCustomFieldScreenValue.tmsCustomFieldScreenId = tmsCustomScreen.id;
            if (tmsCustomScreen.entityData.type !== null && tmsCustomScreen.entityData.type.toLowerCase() === 'textarea') {
                tmsCustomFieldScreenValue.text = tmsCustomScreen.entityData.value.toString();
            } else {
                if (tmsCustomScreen.entityData.value) {
                    tmsCustomFieldScreenValue.value = tmsCustomScreen.entityData.value.toString();
                } else {
                    tmsCustomFieldScreenValue.value = tmsCustomScreen.entityData.value;
                }
            }
            this.tasks.tmsCustomFieldScreenValueDTO.push(tmsCustomFieldScreenValue);
        } else {
            if (tmsCustomScreen.entityData.type !== null && tmsCustomScreen.entityData.type.toLowerCase() === 'textarea') {
                this.tasks.tmsCustomFieldScreenValueDTO[index].text = tmsCustomScreen.entityData.value.toString();
            } else {
                if (tmsCustomScreen.entityData.value) {
                    this.tasks.tmsCustomFieldScreenValueDTO[index].value = tmsCustomScreen.entityData.value.toString();
                } else {
                    this.tasks.tmsCustomFieldScreenValueDTO[index].value = tmsCustomScreen.entityData.value;
                }
            }
        }
    }
    detechDuration() {
        if (!this.form.value.duration || this.form.value.duration === '') {
            return false;
        }
        const regex = /\b((\d+w)?(\d+d)?(\d+h)?(\d+m))\b|(\b((\d+w)?(\d+d)?(\d+h)(\d+m)?)\b)|\b((\d+w)?(\d+d)(\d+h)?(\d+m)?)\b|\b((\d+w)(\d+d)?(\d+h)?(\d+m)?)\b/g;
        const index = this.form.fields.findIndex((x) => x.field === 'duration');
        if ((regex.test(this.form.value.duration) && !/\s/.test(this.form.value.duration)) || !this.form.value.duration || this.form.value.duration === '') {
            this.form.fields[index].properties.isValidField = false;
            return false;
        }
            this.form.fields[index].properties.isValidField = true;

        return true;
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<Tasks>>) {
        result.subscribe((res: HttpResponse<Tasks>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: Tasks) {
        this.eventManager.broadcast({ name: 'tasksListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackPackagesById(index: number, item: Packages) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-tasks-popup',
    template: ''
})
export class TasksPopupComponent implements OnInit, OnDestroy {

    routeSub: any;
    constructor(
        private route: ActivatedRoute,
        private activatedRoute: ActivatedRoute,
        private tasksPopupService: TasksPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if (params['id'] && params['name']) {
                this.tasksPopupService
                    .openClone(TasksDialogComponent as Component, params['id']);
            } else if (params['id']) {
                this.tasksPopupService
                    .open(TasksDialogComponent as Component, params['id']);
            } else {
                this.tasksPopupService
                    .openTask(TasksDialogComponent as Component, sessionStorage.getItem('currentPackageId'));
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
