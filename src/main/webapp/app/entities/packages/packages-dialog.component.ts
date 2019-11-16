import {Component, ElementRef, ViewContainerRef, ComponentFactoryResolver, ComponentFactory, OnInit, OnDestroy, ViewChild} from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import {Form, FormGroup, Validators} from '@angular/forms';
import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService, JhiDataUtils } from 'ng-jhipster';
import { Packages } from './packages.model';
import { PackagesPopupService } from './packages-popup.service';
import { ProjectWorkflowsService } from '../project-workflows/project-workflows.service';
import { PackagesService } from './packages.service';
import { PurchaseOrders, PurchaseOrdersService } from '../purchase-orders';
import { TMSCustomFieldScreen } from '../tms-custom-field-screen/tms-custom-field-screen.model';
/*
* Test Dynamic Form
* */
import {DynamicFieldConfig, FieldConfig, Validator} from '../../shared/dynamic-forms/field.interface';
import { AppConstants } from '../../shared/services/app-constants';
import { DynamicFormComponent } from '../../shared/dynamic-forms/dynamic-form/dynamic-form.component';
import {LocalStorage} from 'ngx-webstorage';
import {ProjectWorkflows} from '../project-workflows/project-workflows.model';
import {el} from '@angular/platform-browser/testing/src/browser_util';
import {TMSCustomFieldScreenValue} from '../tms-custom-field-screen-value';

@Component({
    selector: 'jhi-packages-dialog',
    templateUrl: './packages-dialog.component.html'
})
export class PackagesDialogComponent implements OnInit {
    packages: Packages;
    isSaving: boolean;
    dateFormat = require('dateformat');
    purchaseorders: PurchaseOrders[];
    initFieldConfig: any;
    templateId: any;
    formStringify: any;
    purchaseOrderId: number;
    form: DynamicFormComponent;
    // config dynamic field
    regConfig: FieldConfig[] = new Array<FieldConfig>();
    tmsCustomFieldScreens: TMSCustomFieldScreen[];
    dynamicFieldConfig: DynamicFieldConfig;
    validatePattern: Validator[] = new Array<Validator>();
    // @ViewChild(DynamicFormComponent) form: DynamicFormComponent;
    @ViewChild('child', {read: ViewContainerRef}) myChild;
    formComponentFactory: ComponentFactory<DynamicFormComponent>;

    constructor(
        public activeModal: NgbActiveModal,
        private dataUtils: JhiDataUtils,
        private jhiAlertService: JhiAlertService,
        private packagesService: PackagesService,
        private projectWorkflowsService: ProjectWorkflowsService,
        private purchaseOrdersService: PurchaseOrdersService,
        private eventManager: JhiEventManager,
        private activatedRoute: ActivatedRoute,
        private componentFactoryResolver: ComponentFactoryResolver
    ) {
    }

    ngOnInit() {
        this.purchaseOrderId = parseInt(sessionStorage.getItem('currentPurchaseOrderId'), 10);
        this.getFieldsDisplay();
        this.isSaving = false;
        /*this.purchaseOrdersService.query()
            .subscribe((res: HttpResponse<PurchaseOrders[]>) => { this.purchaseorders = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));*/
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
    getFieldsDisplay() {
        /*this.regConfig = AppConstants.PackageItems;
        // set value for instance and call component
        this.formComponentFactory = this.componentFactoryResolver.resolveComponentFactory(DynamicFormComponent);
        const ref = this.myChild.createComponent(this.formComponentFactory);
        this.form = ref.instance;
        ref.instance.fields = this.regConfig;*/
        this.packagesService.loadPackageDynamicColumn(this.purchaseOrderId).subscribe((res: HttpResponse<any>) => {
            this.dynamicFieldConfig = res.body;
            this.regConfig = this.dynamicFieldConfig.fieldConfigVMs;
            this.tmsCustomFieldScreens = this.dynamicFieldConfig.tmsCustomFieldScreenDTOs;
            // two binding value with package
            if (this.packages.id) {
                this.regConfig.forEach((field) => {
                    field.value = this.packages[field.field];
                });
            }
            // add columns dynamic field and two binding
            if (this.tmsCustomFieldScreens !== undefined && this.tmsCustomFieldScreens.length > 0) {
                this.tmsCustomFieldScreens.forEach((tmsCustomField) => {
                    tmsCustomField.entityGridPm = JSON.parse(tmsCustomField.entityGridPm);
                    if (tmsCustomField.entityGridPm.length > 0) {
                        tmsCustomField.entityData = JSON.parse(tmsCustomField.entityData);
                        tmsCustomField.entityGridInput = JSON.parse(tmsCustomField.entityGridInput);
                        tmsCustomField.entityGridInput = tmsCustomField.entityGridInput[0];
                        const index = this.packages.tmsCustomFieldScreenValueDTO.findIndex((fieldValue) => fieldValue.tmsCustomFieldScreenId === tmsCustomField.id);
                        if (index >= 0) {
                            if (tmsCustomField.entityData.type !== null && tmsCustomField.entityData.type.toLowerCase() === 'textarea') {
                                tmsCustomField.entityData.value = this.packages.tmsCustomFieldScreenValueDTO[index].text;
                            } else {
                                if (tmsCustomField.entityData.type !== null && tmsCustomField.entityData.type.toLowerCase() === 'number') {
                                    tmsCustomField.entityData.value = parseInt(this.packages.tmsCustomFieldScreenValueDTO[index].value, 10);
                                } else {
                                    tmsCustomField.entityData.value = this.packages.tmsCustomFieldScreenValueDTO[index].value;
                                }
                            }
                        }
                        const copy: FieldConfig = Object.assign(tmsCustomField.entityData, tmsCustomField.entityGridInput);
                        this.regConfig.push(copy);
                    }
                });
            }
            // set value for instance and call component
            this.formComponentFactory = this.componentFactoryResolver.resolveComponentFactory(DynamicFormComponent);
            const ref = this.myChild.createComponent(this.formComponentFactory);
            this.form = ref.instance;
            ref.instance.fields = this.regConfig;
        });
    }
    save() {
        this.formStringify = JSON.stringify(this.form.value);
        this.initFieldConfig = JSON.parse(this.formStringify);
        this.regConfig.forEach((item) => {
            if (item.type === 'date' && this.initFieldConfig[item.field] !== null) {
                this.packages[item.field] = this.dateFormat(this.initFieldConfig[item.field], "yyyy-mm-dd'T'HH:MM:ss");
            } else {
                this.packages[item.field] = this.initFieldConfig[item.field];
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
        if (this.packages.id !== undefined) {
            this.subscribeToSaveResponse(
                this.packagesService.update(this.packages));
        } else {
            this.subscribeToSaveResponse(
                this.packagesService.create(this.packages));
        }
    }
    // Save field value
    private setDataCustomScreenValue(tmsCustomScreen: TMSCustomFieldScreen) {
        const index = this.packages.tmsCustomFieldScreenValueDTO.findIndex((ScreenField) => ScreenField.tmsCustomFieldScreenId === tmsCustomScreen.id);
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
            this.packages.tmsCustomFieldScreenValueDTO.push(tmsCustomFieldScreenValue);
        } else {
            if (tmsCustomScreen.entityData.type !== null && tmsCustomScreen.entityData.type.toLowerCase() === 'textarea') {
                this.packages.tmsCustomFieldScreenValueDTO[index].text = tmsCustomScreen.entityData.value.toString();
            } else {
                if (tmsCustomScreen.entityData.value) {
                    this.packages.tmsCustomFieldScreenValueDTO[index].value = tmsCustomScreen.entityData.value.toString();
                } else {
                    this.packages.tmsCustomFieldScreenValueDTO[index].value = tmsCustomScreen.entityData.value;
                }
            }
        }
    }
    submit(value: any) {}

    private subscribeToSaveResponse(result: Observable<HttpResponse<Packages>>) {
        result.subscribe((res: HttpResponse<Packages>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: Packages) {
        this.eventManager.broadcast({ name: 'packagesListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackPurchaseOrdersById(index: number, item: PurchaseOrders) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-packages-popup',
    template: ''
})
export class PackagesPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private packagesPopupService: PackagesPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.packagesPopupService
                    .open(PackagesDialogComponent as Component, params['id']);
            } else {
                this.packagesPopupService
                    .openPO(PackagesDialogComponent as Component, sessionStorage.getItem('currentPurchaseOrderId'));
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
