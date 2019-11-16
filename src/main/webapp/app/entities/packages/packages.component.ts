import {Component, OnInit, OnDestroy, Input, ViewChild, Pipe, PipeTransform} from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiParseLinks, JhiAlertService, JhiDataUtils } from 'ng-jhipster';

import { ProjectWorkflowsService } from '../project-workflows/project-workflows.service';
import { Packages } from './packages.model';
import { PackagesService } from './packages.service';
import { ITEMS_PER_PAGE, Principal } from '../../shared';
import {ProjectWorkflows} from '../project-workflows/project-workflows.model';
import {MatPaginator, MatSort, MatTableDataSource} from '@angular/material';
import {LocalStorage} from 'ngx-webstorage';
import { GridOptions } from 'ag-grid';
import { ButtonViewComponent } from '../../shared/dynamic-data-tables/buttons-view/button-view.component';
import {AppConstants} from '../../shared/services/app-constants';
import { DataService } from '../../shared/services/data.service';
import { Permission } from '../../account/login/permission.model';
import { PermissionService } from '../../account/login/permission.service';
import {DynamicFieldConfig, FieldConfig} from '../../shared/dynamic-forms/field.interface';
import * as moment from 'moment';
import {PurchaseOrders, PurchaseOrdersService} from '../purchase-orders';
import {TMSCustomFieldScreen} from '../tms-custom-field-screen/tms-custom-field-screen.model';
import {DynamicModel} from '../../shared/dynamic-model/dynamic-model.model';

@Component({
    selector: 'jhi-packages',
    templateUrl: './packages.component.html',
    providers: [PermissionService]
})
export class PackagesComponent implements OnInit, OnDestroy {

currentAccount: any;
    packages: Packages[];
    error: any;
    success: any;
    eventSubscriber: Subscription;
    currentSearch: string;
    routeData: any;
    links: any;
    totalItems: any;
    queryCount: any;
    itemsPerPage: any;
    page: any;
    predicate: any;
    previousPage: any;
    reverse: any;
    purchaseOrderId: any;
    purchaseOrderName: any;
    projectName: any;

    frameworkComponents: any;
    columnDefs: any;
    rowData: any;
    gridApi: any;
    gridColumnApi: any;
    gridOptions: GridOptions;
    dynamicFieldConfig: DynamicFieldConfig;
    displayedColumns: FieldConfig[] = new Array<FieldConfig>();
    tmsCustomFieldScreens: TMSCustomFieldScreen[];
    formatDateTime: FieldConfig[] = new Array<FieldConfig>();
    formatDate: FieldConfig[] = new Array<FieldConfig>();
    backgroundColor: FieldConfig[] = new Array<FieldConfig>();
    dataMap: DynamicModel[];
    templateId: number;
    permission: number;
    allPermission: Permission;
    modelName: any;

    constructor(
        private packagesService: PackagesService,
        private parseLinks: JhiParseLinks,
        private jhiAlertService: JhiAlertService,
        private projectWorkflowsService: ProjectWorkflowsService,
        private principal: Principal,
        private activatedRoute: ActivatedRoute,
        private dataUtils: JhiDataUtils,
        private router: Router,
        private eventManager: JhiEventManager,
        private data: DataService,
        private permissionService: PermissionService,
        private purchaseOrdersService: PurchaseOrdersService
    ) {
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.routeData = this.activatedRoute.data.subscribe((data) => {
            this.page = data.pagingParams.page;
            this.previousPage = data.pagingParams.page;
            this.reverse = data.pagingParams.ascending;
            this.predicate = data.pagingParams.predicate;
        });
        this.currentSearch = this.activatedRoute.snapshot && this.activatedRoute.snapshot.params['search'] ?
            this.activatedRoute.snapshot.params['search'] : '';
        this.frameworkComponents = {
            actionsButton: ButtonViewComponent,
        };
    }
    directLink: any = {
        packages: '../../packages'
    };
    loadAll() {
        if (this.currentSearch) {
            this.packagesService.search({
                page: this.page - 1,
                purchaseOrderId: this.purchaseOrderId,
                query: this.currentSearch,
                size: this.itemsPerPage,
                sort: this.sort()}).subscribe(
                (res: HttpResponse<Packages[]>) => this.onSuccess(res.body, res.headers),
                (res: HttpErrorResponse) => this.onError(res.message)
            );
            return;
        }
        this.packagesService.query({
            page: this.page - 1,
            size: this.itemsPerPage,
            sort: this.sort()}, this.purchaseOrderId).subscribe(
            (res: HttpResponse<Packages[]>) => {
                this.onSuccess(res.body, res.headers);
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }
    getAllColumnsDisplay() {
        this.formatDateTime = AppConstants.FormatDateTime;
        this.formatDate = AppConstants.FormatDate;
        this.backgroundColor = AppConstants.BackgroundColor;
        this.packagesService.loadPackageDynamicColumn(this.purchaseOrderId).subscribe((res: HttpResponse<any>) => {
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
                    this.formatDate.forEach((fieldt) => {
                        field.cellRenderer = fieldt.cellRenderer;
                    });
                }
                if (field.backgroundColor) {
                    // field.cellStyle: {'background-color': field.backgroundColor};
                    this.backgroundColor.forEach((cell) => {
                        field.cellStyle = cell.cellStyle;
                    });
                }
            });
        });
        /*this.packagesService.loadPackageColumn(this.purchaseOrderId).subscribe((res: HttpResponse<any>) => {
            this.displayedColumns = res.body;
            this.displayedColumns.forEach((field) => {
                if (field.format === 'datetime') {
                    this.formatDateTime.forEach((fieldt) => {
                        field.cellRenderer = fieldt.cellRenderer;
                    });
                } else if (field.format === 'date') {
                    this.formatDate.forEach((fieldt) => {
                        field.cellRenderer = fieldt.cellRenderer;
                    });
                }
            });
        });*/
    }
    onClickDialogPackage(purchaseOrdID) {
        this.router.navigate(['/', { outlets: { popup: 'packages-new'} }]);
    }
    loadPage(page: number) {
        this.page = page;
        if (page !== this.previousPage) {
            this.previousPage = page;
            this.transition();
        }
    }
    transition() {
        this.router.navigate(['/' + 'purchase-orders/' + this.purchaseOrderId], {queryParams:
            {
                page: this.page,
                size: this.itemsPerPage,
                search: this.currentSearch,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        });
        this.loadAll();
    }

    clear() {
        this.page = 0;
        this.currentSearch = '';
        this.router.navigate(['/purchase-orders/' + this.purchaseOrderId, {
            page: this.page,
            sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
        }]);
        this.loadAll();
    }
    search(query) {
        if (!query) {
            return this.clear();
        }
        this.page = 0;
        this.currentSearch = query;
        this.router.navigate(['/purchase-orders/' + this.purchaseOrderId, {
            search: this.currentSearch,
            page: this.page,
            purchaseOrderId: this.purchaseOrderId,
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
                if (resource.name === 'PACKAGE') {
                    this.permission = resource.permission;
                    this.data.setPermission(resource.permission);
                    return;
                }
            }
        });
        this.activatedRoute.params.subscribe((parmas) => {
             this.purchaseOrderId = parmas['id'];
             sessionStorage.setItem('currentPurchaseOrderId', this.purchaseOrderId);
        });
        this.purchaseOrdersService.find(this.purchaseOrderId).subscribe(
            (res) => {
                this.purchaseOrderName = res.body.name;
                this.projectName = res.body.projectName;
                this.checkPermission(res.body);
            },
            (err) => {
                console.log(err.message);
            }
        );
        this.getAllColumnsDisplay();
        this.loadAll();
        // this.gridOptions = <GridOptions> {
        //         //     domLayout: 'autoHeight',
        //         // };
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInPackages();
        this.cloneInPackages();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: Packages) {
        return item.id;
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }
    registerChangeInPackages() {
        this.eventSubscriber = this.eventManager.subscribe('packagesListModification', (response) => this.loadAll());
    }

    /*Event Reloaded Packages*/
    cloneInPackages() {
        this.eventSubscriber = this.eventManager.subscribe('CloneTaskAndPackage', (response) => this.loadAll());
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
        this.packages = data;
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
                if (rowData.tmsCustomFieldScreenValueDTO !== undefined) {
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
