import {Component, OnInit, Input, Output, EventEmitter, OnChanges, SimpleChanges, ChangeDetectorRef, AfterViewInit} from '@angular/core';
import {Item} from '../services/item.model';
import {ProjectWorkflows} from '../../entities/project-workflows';
import {Router} from '@angular/router';
import {PurchaseOrders} from '../../entities/purchase-orders';
import {TMSCustomFieldScreen} from '../../entities/tms-custom-field-screen/tms-custom-field-screen.model';
import {TMSCustomField} from '../../entities/tms-custom-field/tms-custom-field.model';
import {TMSCustomFieldDialogComponent} from '../../entities/tms-custom-field';
import {HttpErrorResponse, HttpResponse} from '@angular/common/http';
import {TMSCustomFieldService} from '../../entities/tms-custom-field/tms-custom-field.service';
import {MatDialog} from '@angular/material';
import {TMSCustomFieldScreenDeleteDialogComponent} from '../../entities/tms-custom-field-screen/tms-custom-field-screen-delete-dialog.component';
import {TMSCustomFieldScreenService} from '../../entities/tms-custom-field-screen/tms-custom-field-screen.service';

@Component({
    selector: 'jhi-workflow-items',
    templateUrl: './workflow-items.component.html',
    styleUrls: ['./workflow-items.component.css']
})
export class WorkflowItemsComponent implements OnInit, OnChanges {
    @Input() workflowObject: ProjectWorkflows;
    @Input() listCustomFieldScreen: any;
    @Input() newCustomField;
    @Input() hasOPColumn: boolean;
    @Input() hasActions: boolean;
    @Output() detectChange: EventEmitter<ProjectWorkflows>;
    @Output() editProperties = new EventEmitter();
    @Input() pageName: any;
    // @Output() deleteProperties = new EventEmitter();
    tMSCustomFields: any[];
    itemField: any;
    searchCustomField: any;
    customFieldDelete: any;
    stepItems: Item[];
    listEntities = new Array();
    dropdownSettings = {};

    constructor(
        private ref: ChangeDetectorRef,
        private router: Router,
        private tMSCustomFieldService: TMSCustomFieldService,
        private tMSCustomFieldScreenService: TMSCustomFieldScreenService,
        private dialog: MatDialog,
    ) {
        this.itemField = new Array<TMSCustomField>();
        this.listCustomFieldScreen = new Array<TMSCustomFieldScreen>();
        this.stepItems = new Array<Item>();
        this.detectChange = new EventEmitter<ProjectWorkflows>();
    }
    emitProperties(value: any) {
        this.editProperties.emit(value);
    }

    emitDeleteProperties(value: any) {
       // this.deleteProperties.emit(value);
    }

    onViewChange(stepItem) {
        if (!stepItem.properties.isActiveOnPM) {
            stepItem.properties.isActiveOnOP = false;
            stepItem.properties.isRequiredField = false;
        }
        this.detectChange.emit(this.workflowObject);
    }

    // func for custom field
    newListCustomField(item?: any) {
        const customFieldScreen = new TMSCustomFieldScreen();
        if (item) {
            const index = this.workflowObject.entityDTO.findIndex((custom) =>
                custom.entityPropertiesData.headerName === item.entityData.entityPropertiesData.headerName);
            if (index < 0) {
                customFieldScreen.entityGridInput = item.entityData;
                customFieldScreen.tmsCustomFieldId = item.id;
                this.listCustomFieldScreen.push(customFieldScreen);
                this.workflowObject.entityDTO.push(item.entityData);
            } else {
                this.workflowObject.entityDTO[index] = item.entityData;
                this.listCustomFieldScreen.forEach((tmsCustomScreen) => {
                    if (tmsCustomScreen.entityGridInput.entityPropertiesData.headerName === item.entityData.entityPropertiesData.headerName) {
                        tmsCustomScreen.entityGridInput = item.entityData;
                    }
                });
            }
        }
    }
    // edit properties for custom field.
    editCustomFieldProperties(field) {
        let tmsCustomFieldScreen = new TMSCustomFieldScreen();
        this.listCustomFieldScreen.forEach((tmsCustom) => {
            if (tmsCustom.entityGridInput.entityPropertiesData.headerName === field.entityPropertiesData.headerName) {
                tmsCustom.entityGridInput = field;
                tmsCustomFieldScreen = tmsCustom;
            }
        });
        this.tMSCustomFieldService.find(tmsCustomFieldScreen.tmsCustomFieldId)
            .subscribe((tMSCustomFieldResponse: HttpResponse<TMSCustomField>) => {
                const tMSCustomField: TMSCustomField = tMSCustomFieldResponse.body;
                tMSCustomField.entityData = tmsCustomFieldScreen.entityGridInput;
                const dialogRef = this.dialog.open(TMSCustomFieldDialogComponent,
                    {position: {top: (window.scrollY + 30) + 'px'}, width: '775px', data: {pageName: this.pageName}, height: (window.innerHeight) + 'px'});
                const subDialog = dialogRef.componentInstance;
                subDialog.tMSCustomField = tMSCustomField;

                subDialog.onNewField.subscribe((x) => {
                    this.newListCustomField(x);
                    dialogRef.close(dialogRef);
                });
                subDialog.closePopup.subscribe((x) => {
                    dialogRef.close(dialogRef);
                });
            });
    }
    // delete custom field.
    deleteProperties(field): any {
        this.listCustomFieldScreen.forEach((customField) => {
            if (customField.entityGridInput.entityPropertiesData.headerName === field.entityPropertiesData.headerName) {
                this.customFieldDelete = customField;
                const dialogRef = this.dialog.open(TMSCustomFieldScreenDeleteDialogComponent, {position: {top: (window.scrollY + 200) + 'px'}, width: '775px', data: {pageName: this.pageName}});
                const subDialog = dialogRef.componentInstance;
                subDialog.tMSCustomFieldScreen = this.customFieldDelete;
                subDialog.closePopup.subscribe((x) => {
                    dialogRef.close(dialogRef);
                });
                    if (this.customFieldDelete.id !== undefined) {
                        subDialog.onDelete.subscribe((x) => {
                            this.tMSCustomFieldScreenService.delete(this.customFieldDelete.id).subscribe((response) => {
                                this.deleteCustomFieldOnScreen(this.customFieldDelete);
                                dialogRef.close(dialogRef);
                            }, (res: HttpErrorResponse) => {
                                subDialog.errorMessage(res);
                            });
                        });
                    } else {
                        subDialog.onDelete.subscribe((x) => {
                            this.deleteCustomFieldOnScreen(this.customFieldDelete);
                            dialogRef.close(dialogRef);
                        });
                    }
            }
        });
    }

    // delete custom field on Screen.
    deleteCustomFieldOnScreen(field) {
        if (typeof field.entity === 'string') {
            field.entity = JSON.parse(field.entity);
        }
        this.listCustomFieldScreen.forEach((customItem) => {
            if (typeof customItem.entityGridInput === 'string') {
                customItem.entityGridInput = JSON.parse(customItem.entityGridInput);
            }
        });
        const index = this.listCustomFieldScreen.findIndex((fieldItem) =>
            fieldItem.entityGridInput.entityPropertiesData.headerName === field.entityGridInput.entityPropertiesData.headerName);
        this.listCustomFieldScreen.splice(index, 1);
        const indexDTO = this.workflowObject.entityDTO.findIndex((customFieldFlow) =>
            customFieldFlow.entityPropertiesData.headerName === field.entityGridInput.entityPropertiesData.headerName);
        this.workflowObject.entityDTO.splice(indexDTO, 1);
    }

    onItemSelect(item: any) {
    }

    onDeSelect(item: any) {
    }

    ngOnInit() {
        console.log(this.workflowObject);
    }
    ngOnChanges(changes: SimpleChanges) {
        for (const propName in changes) {
            if (propName === 'newCustomField') {
                this.newListCustomField(this.newCustomField);
            }
        }
    }
}
