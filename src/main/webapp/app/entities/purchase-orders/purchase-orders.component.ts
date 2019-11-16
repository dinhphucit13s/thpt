import { Component, OnInit, OnDestroy, ViewChild, Inject, Injectable} from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiParseLinks, JhiAlertService, JhiDataUtils } from 'ng-jhipster';
import {MatPaginator, MatTableDataSource, MatSort} from '@angular/material';

import {PurchaseOrders, PurchaseOrderStatus} from './purchase-orders.model';
import { PurchaseOrdersService } from './purchase-orders.service';
import { ProjectWorkflowsService } from '../project-workflows/project-workflows.service';
import {BaseEntity, ITEMS_PER_PAGE, Principal, User, UserService, Account} from '../../shared';
import {ProjectWorkflows} from '../project-workflows/project-workflows.model';
import {ProjectUsers} from '../project-users/project-users.model';
import {ProjectUsersService} from '../project-users/project-users.service';
import {Projects} from '../projects/projects.model';
import {ProjectsService} from '../projects/projects.service';
import { ButtonViewComponent } from '../../shared/dynamic-data-tables/buttons-view/button-view.component';
import {AppConstants} from '../../shared/services/app-constants';
import {Validators} from '@angular/forms';
import { GridOptions } from 'ag-grid';
import {LocalStorage} from 'ngx-webstorage';
import {isNumber} from 'util';
import { DataService } from '../../shared/services/data.service';
import { Permission } from '../../account/login/permission.model';
import { PermissionService } from '../../account/login/permission.service';

@Component({
    selector: 'jhi-purchase-orders',
    templateUrl: './purchase-orders.component.html',
    providers: [PermissionService]

})
export class PurchaseOrdersComponent implements OnInit, OnDestroy {
    currentAccount: any;
    gridOptions: GridOptions;
    purchaseOrders: PurchaseOrders[];
    projects: Projects[];
    user: User;
    projectId: number;
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
    searchMat: string;
    dataSource: any;
    displayedColumns: any;
    templateId: number;

    /*Options show to page purchaseOrder*/
    idShow = true;
    nameShow = true;
    statusShow = true;
    startTimeShow = true;
    endTimeShow = true;
    descriptionsShow = true;
    projectShow = true;
    purchaseOrderLeadShow = true;
    selectedOptions: number;
    columnDefs: any;
    columnDefsArg: any;
    rowData: any = [];
    gridApi: any;
    gridColumnApi: any;
    frameworkComponents: any;
    reusePage: number;
    searchValue: any;
    permission: number;
    allPermission: Permission;
    /*role pm tl*/
    projectsPM: any[];
    projectsTEAMLEAD: any[];

    constructor(
        private projectUsersService: ProjectUsersService,
        private projectsService: ProjectsService,
        private purchaseOrdersService: PurchaseOrdersService,
        private projectWorkflowsService: ProjectWorkflowsService,
        private parseLinks: JhiParseLinks,
        private jhiAlertService: JhiAlertService,
        private principal: Principal,
        private activatedRoute: ActivatedRoute,
        private dataUtils: JhiDataUtils,
        private router: Router,
        private eventManager: JhiEventManager,
        private userService: UserService,
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
    searchView() {
        this.gridApi.setQuickFilter(this.searchValue);
    }
    loadAll(proId?: any) {
        if (this.currentSearch) {
            this.purchaseOrdersService.search({
                page: this.page - 1,
                query: this.currentSearch,
                projectId: this.selectedOptions !== undefined ? this.selectedOptions : proId,
                size: this.itemsPerPage,
                sort: this.sort()}).subscribe(
                    (res: HttpResponse<PurchaseOrders[]>) => this.onSuccess(res.body, res.headers),
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
            return;
        }
        this.purchaseOrdersService.queryPoWithMonitoringByProId({
            page: this.page - 1,
            size: this.itemsPerPage,
            sort: this.sort()}, this.selectedOptions !== undefined ? this.selectedOptions : proId).subscribe(
            (res: HttpResponse<PurchaseOrders[]>) => {
                this.onSuccess(res.body, res.headers);
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }
    showPurchaseByUserId(projectId: any) {
        this.rowData = [];
        this.loadPage(1);
        this.proId = projectId;
        sessionStorage.setItem('currentProjectId', projectId);
        this.loadAll(projectId);
    }

    onClickDialogPurchaseOrder(projectId) {
        sessionStorage.setItem('currentProjectId', projectId);
        this.router.navigate(['/', { outlets: { popup: 'purchase-orders-new'} }]);
    }
    loadPage(page: number) {
        this.page = page;
        if (page !== this.previousPage) {
            this.previousPage = page;
            this.transition();
        }
    }
    transition() {
        this.router.navigate(['/purchase-orders'], {queryParams:
            {
                page: this.page,
                size: this.itemsPerPage,
                search: this.currentSearch,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        });
        this.loadAll(this.selectedOptions);
    }

    clear() {
        this.page = 0;
        this.currentSearch = '';
        this.router.navigate(['/purchase-orders', {
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
        this.router.navigate(['/purchase-orders', {
            search: this.currentSearch,
            page: this.page,
            sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
        }]);
        this.loadAll();
    }
    loadListProjects() {
        this.projectsService.getListProjectForPOByUserLogin().subscribe(
            (res: HttpResponse<Projects[]>) => {
                this.projects = res.body;
                if (this.projects.length > 0) {
                    if (this.selectedOptions === undefined) {
                        this.selectedOptions = this.projects[0].id;
                    }
                    if ((sessionStorage.getItem('currentProjectId') !== null) && (sessionStorage.getItem('currentProjectId') !== 'NaN')) {
                        this.selectedOptions = Number(sessionStorage.getItem('currentProjectId'));
                    }
                    this.showPurchaseByUserId(this.selectedOptions);
                } else {
                    this.selectedOptions = 0;
                }
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }
    ngOnInit() {
        this.permissionService.findPermission().subscribe((roleResponse: HttpResponse<Permission>) => {
            const role: Permission = roleResponse.body;
            this.data.changeUserPermission(role);
            this.data.currentUserPermission.subscribe((userPermission) => this.allPermission = userPermission);
            for (const resource of this.allPermission.resources) {
                if (resource.name === 'PURCHASE_ORDERS') {
                    this.permission = resource.permission;
                    this.data.setPermission(resource.permission);
                    return;
                }
            }
        });
        this.activatedRoute.queryParams.subscribe((param) => {
            if (param['dashboardProId'] !== undefined) {
                this.selectedOptions = parseInt(param['dashboardProId'], 10);
                this.loadAll(this.selectedOptions);
                // set new project Id at sessionStorage when move from DashBoard to PurchaseOrder
                sessionStorage.setItem('currentProjectId', this.selectedOptions.toString());
            }
        });
        this.columnDefsArg = AppConstants.PurchaseOrderItems;
        this.registerChangeInPurchaseOrders();
        this.loadListProjects();
        // this.gridOptions = {
        //     domLayout: 'autoHeight',
        //     rowDragManaged: true,
        // }
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: PurchaseOrders) {
        return item.id;
    }
    trackByIndex(index: any, obj: any): any {
        return index;
    }
    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }
    registerChangeInPurchaseOrders() {
        this.eventSubscriber = this.eventManager.subscribe('purchaseOrdersListModification', (response) => this.loadAll());
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
        this.purchaseOrders = data;
        this.rowData = data;
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
