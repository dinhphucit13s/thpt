import {Component, OnInit, OnDestroy, ViewChild, Inject, Injectable, ViewContainerRef, ComponentFactory, ComponentFactoryResolver} from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiParseLinks, JhiAlertService, JhiDataUtils } from 'ng-jhipster';

import {BaseEntity, ITEMS_PER_PAGE, Principal, User, UserService, Account} from '../../shared';
import { ButtonViewComponent } from '../../shared/dynamic-data-tables/buttons-view/button-view.component';
import {AppConstants} from '../../shared/services/app-constants';
import { GridOptions } from 'ag-grid';
import { DataService } from '../../shared/services/data.service';
import { Permission } from '../../account/login/permission.model';
import { PermissionService } from '../../account/login/permission.service';
import {Projects} from '../projects/projects.model';
import {ProjectsService} from '../projects/projects.service';
import {PurchaseOrdersService} from '../purchase-orders/purchase-orders.service';
import {PurchaseOrders} from '../purchase-orders/purchase-orders.model';
import {ProjectUsersService} from '../project-users/project-users.service';
import {PackagesService} from '../packages/packages.service';
import {Packages} from '../packages/packages.model';
import {TasksService} from '../tasks/tasks.service';
import {TrackingManagementService} from '../tracking-management/tracking-management.service';
import {BugsService} from '../bugs/bugs.service';
import {Bugs} from '../bugs/bugs.model';
import {DataCollectionComponent} from '../../shared/dynamic-data-tables/data-table.component';
import {EffortService} from './effort.service';

@Component({
    selector: 'jhi-effort',
    templateUrl: './effort.component.html',
    providers: [PermissionService]

})
export class EffortComponent implements OnInit, OnDestroy {
    currentAccount: any;
    gridOptions: GridOptions;
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
    proId: any;
    predicate: any;
    previousPage: any;
    reverse: any;
    displayedColumns: any;
    templateId: number;
    selectedOptions: number;
    columnDefs: any;
    rowData: any;
    gridApi: any;
    gridColumnApi: any;
    frameworkComponents: any;
    searchValue: any;
    permission: number;
    allPermission: Permission;
    /*filter select*/
    ratioBug: any;
    flagTag = false;
    projects: Projects[];
    projectId: any;
    purchaseOrder: PurchaseOrders[];
    package: Packages[];
    taskId: any;
    bugs: Bugs[];
    packageId: any;
    packageName: any;
    purchaseOrderId: any;
    count: any;
    selectAllPackages: any;
    /*Observable*/
    form: DataCollectionComponent;
    @ViewChild('tracking', {read: ViewContainerRef}) myChild;
    formComponentFactory: ComponentFactory<DataCollectionComponent>;

    constructor(
        private parseLinks: JhiParseLinks,
        private jhiAlertService: JhiAlertService,
        private principal: Principal,
        private activatedRoute: ActivatedRoute,
        private projectsService: ProjectsService,
        private purchaseOrdersService: PurchaseOrdersService,
        private packagesService: PackagesService,
        private tasksService: TasksService,
        private effortService: EffortService,
        private bugsService: BugsService,
        private trackingService: TrackingManagementService,
        private projectUsersService: ProjectUsersService,
        private componentFactoryResolver: ComponentFactoryResolver,
        private dataUtils: JhiDataUtils,
        private router: Router,
        private eventManager: JhiEventManager,
        private data: DataService,
        private permissionService: PermissionService
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

    onGridReady(params) {
        this.gridApi = params.api;
        this.gridColumnApi = params.columnApi;

        params.api.sizeColumnsToFit();
    }
    loadAll() {
    }
    loadListProjects() {
        this.projectsService.getListProjectByUserLogin().subscribe(
            (res: HttpResponse<Projects[]>) => {
                this.projects = res.body;
                if (this.projects.length > 0) {
                    this.projectId = this.projects[0].id;
                    this.loadListPurchaseOrder(this.projectId);
                } else {
                    if (this.purchaseOrder !== undefined) {
                        this.purchaseOrder.length = 0;
                    } else if (this.package !== undefined) {
                        this.package.length = 0;
                    }
                    this.rowData = undefined;
                }
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    loadListPurchaseOrder(projectId?: any) {
        this.purchaseOrdersService.query({
            size: 1000000}, projectId).subscribe(
            (res: HttpResponse<PurchaseOrders[]>) => {
                this.purchaseOrder = res.body;
                if (this.purchaseOrder.length > 0) {
                    this.purchaseOrderId = this.purchaseOrder[0].id;
                    this.loadListPackageByPO(this.purchaseOrderId);
                } else {
                    this.purchaseOrderId = 'selectAllPO';
                    if (this.package !== undefined) {
                        // this.package.length = 0;
                        this.package = [];
                    }
                    // this.rowData = undefined;
                }
            }, (res: HttpErrorResponse) => this.onError(res.message)
        );
    }
    loadListPackageByPO(poId?: any) {
        this.packagesService.query({
            size: 1000000}, poId).subscribe(
            (res: HttpResponse<Packages[]>) => {
                this.package = res.body;
                this.packageId = 'selectAllPackages';
                /*if (this.package.length > 0) {
                    this.packageId = this.package[0].id;
                    this.bugTrackingTask();
                } else {
                    this.rowData = undefined;
                }*/
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    loadPurchaseOrderByProject() {
        if (this.projectId === 'null') {
            this.purchaseOrderId = null;
            this.packageId = null;
        } else {
            this.loadListPurchaseOrder(this.projectId);
        }
    }
    loadPackageByPO() {
        if (this.purchaseOrderId === 'selectAllPO') {
            this.packageId = null;
            // this.bugTrackingTask();
        } else {
            this.loadListPackageByPO(this.purchaseOrderId);
        }
    }

    loadTasksByPackage() {
        this.bugTrackingTask();
    }

    bugTrackingTask() {
        if (this.projectId !== undefined && this.purchaseOrderId === 'selectAllPO') {
            this.package = [];
            this.packageId = null;
            this.effortService.query({
                page: this.page - 1,
                size: this.itemsPerPage,
                projectId: this.projectId,
                sort: this.sort()}).subscribe(
                (resJson: HttpResponse<any>) => {
                    this.rowData = resJson.body;
                }, (res: HttpErrorResponse) => this.onError(res.message)
            );
        }else if (this.packageId === 'selectAllPackages') {
            this.effortService.query({
                page: this.page - 1,
                size: this.itemsPerPage,
                purchaseOrderId: this.purchaseOrderId,
                projectId: this.projectId,
                sort: this.sort()}).subscribe(
                (resJson: HttpResponse<any>) => {
                    this.rowData = resJson.body;
                }, (res: HttpErrorResponse) => this.onError(res.message)
            );
        }else {
            this.effortService.query({
                page: this.page - 1,
                size: this.itemsPerPage,
                packId: this.packageId,
                projectId: this.projectId,
                sort: this.sort()}).subscribe(
                (resJson: HttpResponse<any>) => {
                    this.rowData = resJson.body;
                }, (res: HttpErrorResponse) => this.onError(res.message)
            );
        }
    }
    loadPage(page: number) {
        this.page = page;
        if (page !== this.previousPage) {
            this.previousPage = page;
            this.transition();
        }
    }
    transition() {
        this.router.navigate(['/effort'], {queryParams:
                {
                    page: this.page,
                    size: this.itemsPerPage,
                    search: this.currentSearch,
                    sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
                }
        });
        this.bugTrackingTask();

    }

    clear() {
        this.page = 0;
        this.currentSearch = '';
        this.router.navigate(['/effort', {
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
        this.router.navigate(['/effort', {
            search: this.currentSearch,
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
                if (resource.name === 'EFFORT') {
                    this.permission = resource.permission;
                    this.data.setPermission(resource.permission);
                    return;
                }
            }
        });
        this.columnDefs = AppConstants.EffortItems;
        this.rowData = [];
        this.loadListProjects();
        this.registerChangeInTrackingManagement();
        
    }
    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }
    trackByIndex(index: any, obj: any): any {
        return index;
    }
    trackPackageById(index: number, item: Packages) {
        this.packageName = item.name;
        return item.id;
    }
    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }
    registerChangeInTrackingManagement() {
        this.eventSubscriber = this.eventManager.subscribe('effortListModification',
            (response) => this.loadAll());
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
        this.rowData = data;
    }
    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
