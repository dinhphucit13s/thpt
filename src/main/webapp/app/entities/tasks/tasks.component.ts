import {Component, OnInit, OnDestroy, ViewChild, ComponentFactory, ViewContainerRef, ComponentFactoryResolver} from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiParseLinks, JhiAlertService, JhiDataUtils } from 'ng-jhipster';
import {MatPaginator, MatSort, MatTableDataSource} from '@angular/material';
import { Tasks } from './tasks.model';
import { TasksService } from './tasks.service';
import { ProjectWorkflowsService } from '../project-workflows/project-workflows.service';
import { ITEMS_PER_PAGE, Principal } from '../../shared';
import {ProjectWorkflows} from '../project-workflows/project-workflows.model';
import {DynamicFormComponent} from '../../shared/dynamic-forms/dynamic-form/dynamic-form.component';
import {DataCollectionComponent} from '../../shared/dynamic-data-tables/data-table.component';
import {GridOptions} from 'ag-grid';
import { ButtonViewComponent } from '../../shared/dynamic-data-tables/buttons-view/button-view.component';
import { NumericEditorComponent } from '../../shared/dynamic-data-tables/numeric-editor/numeric-editor.component';
import {AppConstants} from '../../shared/services/app-constants';
import { DataService } from '../../shared/services/data.service';
import { Permission } from '../../account/login/permission.model';
import { PermissionService } from '../../account/login/permission.service';
import { Packages } from '../packages/packages.model';
import { PackagesService } from '../packages/packages.service';
import {DynamicFieldConfig, FieldConfig} from '../../shared/dynamic-forms/field.interface';
import {PurchaseOrders, PurchaseOrdersService} from '../purchase-orders';
import {TMSCustomFieldScreen} from '../tms-custom-field-screen/tms-custom-field-screen.model';
import {DynamicModel} from '../../shared/dynamic-model/dynamic-model.model';
declare var jquery: any;
declare var $: any;
@Component({
    selector: 'jhi-tasks',
    templateUrl: './tasks.component.html',
    providers: [PermissionService]
})
export class TasksComponent implements OnInit, OnDestroy {

currentAccount: any;
    tasks: Tasks[];
    package: Packages;
    error: any;
    success: any;
    eventSubscriber: Subscription;
    searchName = '';
    searchStatus = '*';
    searchDescription = '';
    searchAssignee = '';
    fromDate = '';
    toDate = '';
    routeData: any;
    links: any;
    totalItems: any;
    queryCount: any;
    itemsPerPage: any;
    page: any;
    predicate: any;
    previousPage: any;
    reverse: any;
    packageId: number;
    packageName: any;
    purchaseOrderId: any;
    purchaseOrderName: any;
    projectName: any;

    dataSource: any;
    // model config for CustomField
    dynamicFieldConfig: DynamicFieldConfig;
    displayedColumns: FieldConfig[] = new Array<FieldConfig>();
    tmsCustomFieldScreens: TMSCustomFieldScreen[];
    formatDateTime: FieldConfig[] = new Array<FieldConfig>();
    formatDate: FieldConfig[] = new Array<FieldConfig>();
    ellipsisFileName: FieldConfig[] = new Array<FieldConfig>();
    backgroundColor: FieldConfig[] = new Array<FieldConfig>();
    color: FieldConfig[] = new Array<FieldConfig>();
    templateId: number;
    frameworkComponents: any;
    columnDefs: any;
    rowData: any;
    gridApi: any;
    gridColumnApi: any;
    gridOptions: GridOptions;
    permission: number;
    allPermission: Permission;
    directLink: any = {
        tasks: '../../tasks"'
    };
    constructor(
        private tasksService: TasksService,
        private packageService: PackagesService,
        private parseLinks: JhiParseLinks,
        private jhiAlertService: JhiAlertService,
        private projectWorkflowsService: ProjectWorkflowsService,
        private principal: Principal,
        private activatedRoute: ActivatedRoute,
        private dataUtils: JhiDataUtils,
        private componentFactoryResolver: ComponentFactoryResolver,
        private router: Router,
        private eventManager: JhiEventManager,
        private data: DataService,
        private permissionService: PermissionService,
        private purchaseOrdersService: PurchaseOrdersService
    ) {
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.routeData = this.activatedRoute.data.subscribe((data) => {
            console.log(data);
            this.page = data.pagingParams.page;
            this.previousPage = data.pagingParams.page;
            this.reverse = data.pagingParams.ascending;
            this.predicate = data.pagingParams.predicate;
        });
        this.searchName = this.activatedRoute.snapshot && this.activatedRoute.snapshot.params['search'] ?
            this.activatedRoute.snapshot.params['search'] : '';
        this.frameworkComponents = {
            actionsButton: ButtonViewComponent,
            numericEditor: NumericEditorComponent,
        };
    }

    loadAll() {
        if (this.searchName !== '' || this.searchStatus !== '*' || this.searchDescription !== '' || this.searchAssignee !== '' || this.fromDate !== '' || this.toDate !== '') {
            this.tasksService.search({
                page: this.page - 1,
                taskName: this.searchName,
                taskStatus: this.searchStatus,
                assignee: this.searchAssignee,
                description: this.searchDescription,
                packageId: this.packageId,
                from: this.fromDate,
                to: this.toDate,
                size: this.itemsPerPage,
                sort: this.sort()}).subscribe(
                    (res: HttpResponse<Tasks[]>) => {
                        this.onSuccess(res.body, res.headers);
                    },
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
            return;
        }
        this.tasksService.query({
            page: this.page - 1,
            size: this.itemsPerPage,
            sort: this.sort()}, this.packageId).subscribe(
            (res: HttpResponse<Tasks[]>) => {
                this.onSuccess(res.body, res.headers);
                // this.rowData = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    onClickTaskMove(packageId) {
        sessionStorage.setItem('currentPackageId', packageId);
        this.router.navigate(['/tasks-move'], { queryParams: {currentPackageId: packageId}});
    }
    onClickDialogTask(packageId) {
        sessionStorage.setItem('currentPackageId', packageId);
        this.router.navigate(['/', { outlets: { popup: 'tasks-new'} }]);
    }
    getAllColumnsDisplay() {
        this.formatDateTime = AppConstants.FormatDateTime;
        this.formatDate = AppConstants.FormatDate;
        this.ellipsisFileName = AppConstants.ellipsisFileName;
        this.backgroundColor = AppConstants.BackgroundColor;
        this.color = AppConstants.TextColor;
        this.tasksService.loadTaskDynamicColumn(sessionStorage.getItem('currentPurchaseOrderId')).subscribe((res: HttpResponse<any>) => {
            this.dynamicFieldConfig = res.body;
            this.displayedColumns = this.dynamicFieldConfig.fieldConfigVMs;
            this.tmsCustomFieldScreens = this.dynamicFieldConfig.tmsCustomFieldScreenDTOs;
            // add custom field for ag-grid columns
            if (this.tmsCustomFieldScreens !== undefined && this.tmsCustomFieldScreens.length > 0) {
                this.tmsCustomFieldScreens.forEach((tmsCustomField) => {
                    tmsCustomField.entityGridPm = JSON.parse(tmsCustomField.entityGridPm);
                    if (tmsCustomField.entityGridPm.length > 0) {
                        tmsCustomField.entityData = JSON.parse(tmsCustomField.entityData);
                        tmsCustomField.entityGridInput = JSON.parse(tmsCustomField.entityGridInput);
                        tmsCustomField.entityGridInput = tmsCustomField.entityGridInput[0];
                        const copy: FieldConfig = Object.assign(tmsCustomField.entityData, tmsCustomField.entityGridInput);
                        this.displayedColumns.push(copy);
                    }
                });
            }
            this.displayedColumns.forEach((field) => {
                if (field.format === 'datetime') {
                    this.formatDateTime.forEach((fieldt) => {
                        field.cellRenderer = fieldt.cellRenderer;
                    });
                } else if (field.format === 'date') {
                    this.formatDateTime.forEach((fieldt) => {
                        field.cellRenderer = fieldt.cellRenderer;
                    });
                }
                if (field.field === 'fileName') {
                    this.ellipsisFileName.forEach((fieldt) => {
                        field.cellRenderer = fieldt.cellRenderer;
                    });
                }
                if (field.backgroundColor) {
                    // field.cellStyle: {'background-color': field.backgroundColor};
                    this.backgroundColor.forEach((cell) => {
                        field.cellStyle = cell.cellStyle;
                    });
                }
                if (field.field === 'status') {
                    this.color.forEach((cell) => {
                        field.cellStyle = cell.cellStyle;
                    });
                }
            });
        });
    }

    loadPage(page: number) {
        this.page = page;
        if (page !== this.previousPage) {
            this.previousPage = page;
            this.transition();
        }
    }
    transition() {
        this.router.navigate(['/' + 'packages/' + this.packageId], {queryParams:
            {
                page: this.page,
                size: this.itemsPerPage,
                search: `${this.searchName}|${this.searchStatus}|${this.searchAssignee}|${this.searchDescription}`,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        });
        this.loadAll();
    }

    clear() {
        this.page = 0;
        this.searchName = '';
        this.searchStatus = '*';
        this.searchAssignee = '';
        this.searchDescription = '';
        this.fromDate = '';
        this.toDate = '';
        this.router.navigate(['/' + 'packages/' + this.packageId, {
            page: this.page,
            sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
        }]);
        this.loadAll();
    }
    search() {
        if (!this.searchName && !this.searchStatus && !this.searchDescription && !this.searchAssignee && !this.fromDate && !this.toDate) {
            return this.clear();
        }
        this.page = 0;
        this.router.navigate(['/' + 'packages/' + this.packageId, {
            search: `${this.searchName}|${this.searchStatus}|${this.searchAssignee}|${this.searchDescription}|${this.fromDate}|${this.toDate}`,
            page: this.page,
            sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
        }]);
        this.loadAll();
    }
    ngOnInit() {
        this.permissionService.findPermission().subscribe((roleResponse: HttpResponse<Permission>) => {
            const role: Permission = roleResponse.body;
            this.data.changeUserPermission(role);
            this.data.currentUserPermission.subscribe((userPermission) => this.allPermission = userPermission);
            for (const resource of this.allPermission.resources) {
                if (resource.name === 'TASK') {
                    this.permission = resource.permission;
                    this.data.setPermission(resource.permission);
                    return;
                }
            }
        });
        this.activatedRoute.params.subscribe((param) => {
            this.packageId = param['id'];
        });

        this.packageService.find(this.packageId).subscribe((res: HttpResponse<Packages>) => {
            this.package = res.body;
            this.packageName = this.package.name;
            this.purchaseOrderName = this.package.purchaseOrdersName;
            this.purchaseOrderId = this.package.purchaseOrdersId;

            this.purchaseOrdersService.find(this.purchaseOrderId).subscribe(
                (res) => {
                    this.projectName = res.body.projectName;
                    this.checkPermission(res.body);
                },
                (err) => {
                    console.log(err.message);
                }
            );
        });
        this.getAllColumnsDisplay();
        this.loadAll();
        // this.getAllColumnsDisplay();
        // this.gridOptions = <GridOptions> {
        //     domLayout: 'autoHeight',
        // };
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInTasks();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: Tasks) {
        return item.id;
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }
    registerChangeInTasks() {
        this.eventSubscriber = this.eventManager.subscribe('tasksListModification', (response) => this.loadAll());
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    private onSuccess(data, headers) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = headers.get('X-Total-Count');
        this.queryCount = this.totalItems;
        // this.page = pagingParams.page;
        this.tasks = data;
        this.rowData = data;
        this.rowData.forEach((row) => {
            this.mapperData(row);
        });
    }
    // Create model for new field, add to rowData.
    private mapperData(rowData: any) {
        this.displayedColumns.forEach((item) => {
            const index = this.tmsCustomFieldScreens.findIndex((field) => field.entityData.field === item.field);
            if (index >= 0) {
                if (rowData.tmsCustomFieldScreenValueDTO !== undefined && rowData.tmsCustomFieldScreenValueDTO !== null) {
                    const indexValue = rowData.tmsCustomFieldScreenValueDTO.findIndex((value) => value.tmsCustomFieldScreenId === this.tmsCustomFieldScreens[index].id);
                    if (indexValue >= 0) {
                        const dynamicModel: DynamicModel = new DynamicModel();
                        let row: any;
                        dynamicModel.name = item.field;
                        if (rowData.tmsCustomFieldScreenValueDTO[indexValue].value !== null) {
                            dynamicModel.value = rowData.tmsCustomFieldScreenValueDTO[indexValue].value;
                        } else {
                            dynamicModel.value = rowData.tmsCustomFieldScreenValueDTO[indexValue].text;
                        }
                        row = {
                            [dynamicModel.name]: dynamicModel.value
                        };
                        Object.assign(rowData, row);
                    }
                }
            }
        });
    }
    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }

    navigateToPO() {
        this.router.navigate(['/purchase-orders']);
    }

    navigateToPackage() {
        this.router.navigate(['/purchase-orders/' + this.purchaseOrderId]);
    }

    checkPermission(po: PurchaseOrders) {
        if (po.dtmsMonitoringProject) {
            if (po.dtmsMonitoringProject.role.toString() === 'ROLE_DEDICATED') {
                this.permission = 3;
                this.data.setPermission(this.permission);
            } else if (po.dtmsMonitoringProject.role.toString() === 'ROLE_WATCHER') {
                this.permission = 1;
                this.data.setPermission(this.permission);
            }
        } else {
            if (po.watcherUsersPO && po.watcherUsersPO.indexOf(this.currentAccount.login) > -1) {
                this.permission = 1;
                this.data.setPermission(this.permission);
            }
            if (po.dedicatedUsersPO && po.dedicatedUsersPO.indexOf(this.currentAccount.login) > -1) {
                this.permission = 3;
                this.data.setPermission(this.permission);
            }
        }
    }
}
