import {Component, OnInit, OnDestroy, ChangeDetectorRef} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService, JhiDataUtils } from 'ng-jhipster';

import {ProjectWorkflows, TaskWorkflowVM} from './project-workflows.model';
import { ProjectWorkflowsPopupService } from './project-workflows-popup.service';
import { ProjectWorkflowsService } from './project-workflows.service';
import { ProjectTemplates, ProjectTemplatesService } from '../project-templates';
import { DataService } from '../../shared/services/data.service';
import { MatDialog } from '@angular/material';
import {Item} from '../../shared/services/item.model';
import {AppConstants} from '../../shared/services/app-constants';
import {TMSCustomField} from '../tms-custom-field/tms-custom-field.model';
import {TMSCustomFieldScreen} from '../tms-custom-field-screen/tms-custom-field-screen.model';
import {TMSCustomFieldService} from '../tms-custom-field/tms-custom-field.service';
import {Subscription} from 'rxjs/Subscription';
import {TMSCustomFieldScreenService} from '../tms-custom-field-screen/tms-custom-field-screen.service';
import {DynamicPropertiesField, FieldPropertiesConfig} from '../../shared/dynamic-forms/field.interface';
import {TMSCustomFieldDialogComponent} from '../tms-custom-field';
import {TMSCustomFieldScreenDeleteDialogComponent} from '../tms-custom-field-screen/tms-custom-field-screen-delete-dialog.component';
declare var $: any;
@Component({
    selector: 'jhi-project-workflows-dialog',
    templateUrl: './project-workflows-dialog.component.html'
})
export class ProjectWorkflowsDialogComponent implements OnInit {
    projectWorkflows: ProjectWorkflows[];
    tmsCustomFieldScreen: TMSCustomFieldScreen;
    tMSCustomFields: any[];
    itemField: any;
    newCustomFieldPackage: TMSCustomField;
    newCustomFieldTask: TMSCustomField;
    customField: any;
    customFieldDelete: any;
    isSaving: boolean;
    mode: string;
    eventSubscriber: Subscription;
    templateId: number;
    // workFlow
    poWorkflow: ProjectWorkflows;
    packageWorkflow: ProjectWorkflows;
    packageWorkflowCustomFields: ProjectWorkflows;
    taskWorkflowCustomFields: ProjectWorkflows;
    taskWorkflow: ProjectWorkflows;
    taskWorkflowVM: TaskWorkflowVM[];
    customFieldId: any;
    tabChange: any;
    // search
    searchCustomField: any;
    dropdownSettings = {};
    // list custom field
    listCustomFieldScreenPackage: any;
    listCustomFieldScreenTask: any;
    constructor(
        private route: ActivatedRoute,
        private ref: ChangeDetectorRef,
        public activeModal: NgbActiveModal,
        private dataUtils: JhiDataUtils,
        private router: Router,
        private jhiAlertService: JhiAlertService,
        private tMSCustomFieldService: TMSCustomFieldService,
        private tMSCustomFieldScreenService: TMSCustomFieldScreenService,
        private projectWorkflowsService: ProjectWorkflowsService,
        private projectTemplatesService: ProjectTemplatesService,
        private eventManager: JhiEventManager,
        private data: DataService,
        private dialog: MatDialog,
    ) {
        this.itemField = new Array<TMSCustomField>();
        this.listCustomFieldScreenPackage = new Array<TMSCustomFieldScreen>();
        this.listCustomFieldScreenTask = new Array<TMSCustomFieldScreen>();
        this.packageWorkflow = new ProjectWorkflows();
        // this.packageWorkflow.tmsCustomFields = new Array<TMSCustomFieldScreen>();
        this.packageWorkflowCustomFields = new ProjectWorkflows();
        this.packageWorkflowCustomFields.entityDTO = new Array<any>();
        this.taskWorkflow = new ProjectWorkflows();
        this.taskWorkflowCustomFields = new ProjectWorkflows();
        this.taskWorkflowCustomFields.entityDTO = new Array<any>();
        this.tabChange = 'Package';
    }

    ngOnInit() {
        this.isSaving = false;
        this.projectWorkflows = new Array<ProjectWorkflows>();
        this.route.params.subscribe((params) => {
            if (params['templateId']) {
                this.templateId = params['templateId'];
            }
        });
        // this.templateId = parseInt(localStorage.getItem('currentTemplateId'), 10);
        this.projectWorkflowsService.findByTemplate(this.templateId)
            .subscribe((res: HttpResponse<ProjectWorkflows[]>) => {
                if (res.body.length > 0) {
                    this.mode = 'update';
                    for (const wf of res.body) {
                        if (wf.name === 'Package') {
                            this.packageWorkflow = wf;
                            this.packageWorkflow.entityDTO = JSON.parse(wf.entityDTO);
                            this.packageWorkflow.activity = null;
                            if (wf.tmsCustomFields !== null && wf.tmsCustomFields.length > 0) {
                                this.listCustomFieldScreenPackage = wf.tmsCustomFields;
                                this.listCustomFieldScreenPackage.forEach((tmsCustomScreen) => {
                                    const dynamicPropertiesField: DynamicPropertiesField = new DynamicPropertiesField();
                                    dynamicPropertiesField.entityPropertiesData = JSON.parse(tmsCustomScreen.entityData);
                                    dynamicPropertiesField.entityPropertiesView = JSON.parse(tmsCustomScreen.entityGridInput);
                                    dynamicPropertiesField.entityPropertiesView = dynamicPropertiesField.entityPropertiesView[0];
                                    tmsCustomScreen.entityGridInput = dynamicPropertiesField;
                                    this.packageWorkflowCustomFields.entityDTO.push(dynamicPropertiesField);
                                });
                                this.packageWorkflowCustomFields = this.generateWorkflowDefaultData('Package', 1, 'packages', this.packageWorkflowCustomFields.entityDTO);
                            }
                        }

                        if (wf.name === 'Task') {
                            this.taskWorkflow = wf;
                            this.taskWorkflow.entityDTO = JSON.parse(wf.entityDTO);
                            if (wf.tmsCustomFields !== null && wf.tmsCustomFields.length > 0) {
                                this.listCustomFieldScreenTask = wf.tmsCustomFields;
                                this.listCustomFieldScreenTask.forEach((tmsCustomScreen) => {
                                    const dynamicPropertiesField: DynamicPropertiesField = new DynamicPropertiesField();
                                    dynamicPropertiesField.entityPropertiesData = JSON.parse(tmsCustomScreen.entityData);
                                    dynamicPropertiesField.entityPropertiesView = JSON.parse(tmsCustomScreen.entityGridInput);
                                    dynamicPropertiesField.entityPropertiesView = dynamicPropertiesField.entityPropertiesView[0];
                                    tmsCustomScreen.entityGridInput = dynamicPropertiesField;
                                    this.taskWorkflowCustomFields.entityDTO.push(dynamicPropertiesField);
                                });
                                this.taskWorkflowCustomFields = this.generateWorkflowDefaultData('Task', 1, 'tasks', this.taskWorkflowCustomFields.entityDTO);
                            }
                        }
                    }
                }else {
                    this.mode = 'create';
                   // this.poWorkflow = this.generateWorkflowDefaultData('PurchaseOrder', 1, 'purchase-orders', AppConstants.PurchaseOrderItems);
                    this.packageWorkflow = this.generateWorkflowDefaultData('Package', 1, 'packages', AppConstants.PackageItems);
                    this.taskWorkflow = this.generateWorkflowDefaultData('Task', 2, 'tasks', AppConstants.TaskItems);
                }
            }, (res: HttpErrorResponse) => {
                this.onError(res.message);
            });
        this.projectWorkflowsService.findDeployedProcesses()
            .subscribe((res: HttpResponse<TaskWorkflowVM[]>) => {
                if (res.body.length > 0) {
                    this.taskWorkflowVM = res.body;
                }
            }, (res: HttpErrorResponse) => {
                this.onError(res.message);
            });
        // this.registerChangeInTMSCustomFields();
    }
    tagChangeStatus(tag) {
        if (this.tabChange !== undefined) {
            this.tabChange = tag.nextId;
        }
    }
    searchCustomFields(event?: any) {
        if (event.keyCode === 13 || event === 'search') {
            this.tMSCustomFieldService.searchCustomField(this.searchCustomField).subscribe((res: HttpResponse<TMSCustomField[]>) => {
                res.body.forEach((field) => {
                    field.entityData = JSON.parse(field.entityData);
                    this.itemField.push(field.entityData);
                });
                this.tMSCustomFields = res.body;
            });
        }
    }
    // disable button add if exists in list.
    checkFieldName(field: any): boolean {
        let status = false;
        if (field.entityData.type === null) {
            return true;
        }
        if (this.tabChange === 'Package') {
            this.listCustomFieldScreenPackage.forEach((x) => {
                if (typeof x.entityGridInput !== 'string' && x.entityGridInput.entityPropertiesData.headerName === field.entityData.headerName) {
                    status = true;
                }
            });
            return status;
        } else {
            this.listCustomFieldScreenTask.forEach((x) => {
                if (typeof x.entityGridInput !== 'string' && x.entityGridInput.entityPropertiesData.headerName === field.entityData.headerName) {
                    status = true;
                }
            });
            return status;
        }
    }
    // show popup when click button add
    showPopupCustomField(item?: any) {
        this.tMSCustomFieldService.find(item.id)
            .subscribe((tMSCustomFieldResponse: HttpResponse<TMSCustomField>) => {
                const tMSCustomField: TMSCustomField = tMSCustomFieldResponse.body;
                const dynamicPropertiesField: DynamicPropertiesField = new DynamicPropertiesField();
                dynamicPropertiesField.entityPropertiesData = JSON.parse(tMSCustomField.entityData);
                dynamicPropertiesField.entityPropertiesView = new FieldPropertiesConfig();
                tMSCustomField.entityData = dynamicPropertiesField;
                const dialogRef = this.dialog.open(TMSCustomFieldDialogComponent,
                    {position: {top: (window.scrollY + 20) + 'px'}, width: '775px', data: {pageName: this.tabChange}, height: (window.innerHeight) + 'px'});
                const subDialog = dialogRef.componentInstance;
                subDialog.tMSCustomField = tMSCustomField;

                subDialog.onNewField.subscribe((x) => {
                    // this.newListCustomField(x);
                    if (this.tabChange === 'Package') {
                        this.newCustomFieldPackage = x;
                    } else if (this.tabChange === 'Task') {
                        this.newCustomFieldTask = x;
                    }
                    dialogRef.close(dialogRef);
                    this.ref.detectChanges();
                });
                subDialog.closePopup.subscribe((x) => {
                    dialogRef.close(dialogRef);
                });
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
        this.isSaving = true;
        this.generateOP_PMGridDTO(this.packageWorkflow, this.listCustomFieldScreenPackage, 'packages');
        this.generateOP_PMGridDTO(this.taskWorkflow, this.listCustomFieldScreenTask, 'tasks');
        this.projectWorkflows.push(this.packageWorkflow);
        this.projectWorkflows.push(this.taskWorkflow);

        if (this.mode === 'create') {
            this.subscribeToSaveResponse(
                this.projectWorkflowsService.create(this.projectWorkflows)
            );
        }
        if (this.mode === 'update') {
            this.subscribeToSaveResponse(
                this.projectWorkflowsService.update(this.projectWorkflows)
            );
        }
    }
    private generateOP_PMGridDTO(projectWorkflow: ProjectWorkflows, customFieldWorkflow: Array<TMSCustomFieldScreen>, screen: String) {
        const opGridDTO = new Array();
        const pmGridDTO = new Array();
        const inputDTO = new Array();
        for (let i = 0; i < projectWorkflow.entityDTO.length; i++) {
            const item = projectWorkflow.entityDTO[i];
            if (item.properties !== undefined) {
                if (item.properties.isActiveOnOP) {
                    opGridDTO.push(item);
                }
                if (item.properties.isActiveOnPM) {
                    if (item.field !== 'id' && !item.hideDynamicField) {
                        pmGridDTO.push(item.field);
                    }
                    inputDTO.push(item);
                }
            }
        }
        projectWorkflow.inputDTO = inputDTO;
        projectWorkflow.pmGridDTO = pmGridDTO;
        projectWorkflow.opGridDTO = opGridDTO;
        for (let i = 0; i < customFieldWorkflow.length; i++) {
            const item = customFieldWorkflow[i].entityGridInput;
            const entityGridOp = new Array();
            const entityGridPm = new Array();
            const entityGridInput = new Array();
            if (item.entityPropertiesView.properties !== undefined) {
                if (item.entityPropertiesView.properties.isActiveOnOP) {
                    entityGridOp.push(item.entityPropertiesView);
                }
                if (item.entityPropertiesView.properties.isActiveOnPM) {
                    if (item.entityPropertiesData.field !== 'id' && !item.entityPropertiesData.hideDynamicField) {
                        entityGridPm.push(item.entityPropertiesData.field);
                    }
                    entityGridInput.push(item.entityPropertiesView);
                } else {
                    entityGridInput.push(item.entityPropertiesView);
                }
            }
            customFieldWorkflow[i].entityData = item.entityPropertiesData;
            customFieldWorkflow[i].entityGridOp = entityGridOp;
            customFieldWorkflow[i].entityGridPm = entityGridPm;
            customFieldWorkflow[i].entityGridInput = entityGridInput;
            customFieldWorkflow[i].sequence = 1;
        }
        projectWorkflow.tmsCustomFields = customFieldWorkflow;
    }
    private generateWorkflowDefaultData(name, index, uri, items) {
        const wf = new ProjectWorkflows();
        wf.name = name;
        wf.step = index;
        wf.nextURI = uri;
        wf.projectTemplatesId = this.templateId;
        wf.entityDTO = items;
        wf.inputDTO = [];
        wf.opGridDTO = [];
        wf.pmGridDTO = [];
        return wf;
    }
    private subscribeToSaveResponse(result: Observable<HttpResponse<ProjectWorkflows>>) {
        result.subscribe((res: HttpResponse<ProjectWorkflows>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: ProjectWorkflows) {
        this.router.navigate(['/project-templates']);
        this.eventManager.broadcast({ name: 'projectWorkflowsListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        if (!error) {
            this.router.navigate(['/', { outlets: { popup: 'tms-custom-field-screen/delete'} }]);
        }
    }

    trackProjectTemplatesById(index: number, item: ProjectTemplates) {
        return item.id;
    }

    previousState() {
        window.history.back();
    }
}

@Component({
    selector: 'jhi-project-workflows-popup',
    template: ''
})
export class ProjectWorkflowsPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private projectWorkflowsPopupService: ProjectWorkflowsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['templateId'] ) {
                this.projectWorkflowsPopupService
                    .open(ProjectWorkflowsDialogComponent as Component, params['templateId']);
            } else {
                this.projectWorkflowsPopupService
                    .open(ProjectWorkflowsDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
