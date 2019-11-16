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
import {TrackingManagementService} from './tracking-management.service';
import {BugsService} from '../bugs/bugs.service';
import {Bugs} from '../bugs/bugs.model';
import {DataCollectionComponent} from '../../shared/dynamic-data-tables/data-table.component';
import {CustomReports} from '../custom-reports/custom-reports.model';
import {CustomReportsService} from '../custom-reports/custom-reports.service';
import * as moment from 'moment';
import {NgxSpinnerService} from 'ngx-spinner';

@Component({
    selector: 'jhi-tracking-management',
    templateUrl: './tracking-management.component.html',
    providers: [PermissionService]

})
export class TrackingManagementComponent implements OnInit, OnDestroy {
    account: Account;
    customReports: any;
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
    projetId: any;
    purchaseOrder: PurchaseOrders[];
    package: Packages[];
    taskId: any;
    bugs: Bugs[];
    packageId: any;
    packageName: any;
    purchaseOrderId: any;
    count: any;
    selectAllPackages: any;
    fromDate = '';
    toDate = '';
    userLoginExport: any;
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
        private customReportsService: CustomReportsService,
        private tasksService: TasksService,
        private bugsService: BugsService,
        private trackingService: TrackingManagementService,
        private projectUsersService: ProjectUsersService,
        private componentFactoryResolver: ComponentFactoryResolver,
        private dataUtils: JhiDataUtils,
        private router: Router,
        private eventManager: JhiEventManager,
        private data: DataService,
        private permissionService: PermissionService,
        private spinnerService: NgxSpinnerService
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
        this.customReports = new CustomReports();
        this.userLoginExport = null;
    }
    ngOnInit() {
        this.principal.identity().then((account) => {
            this.account = account;
            this.userLoginExport = this.account.login;
            this.getListColumnsCustomReport();
        });
        this.permissionService.findPermission().subscribe((roleResponse: HttpResponse<Permission>) => {
            const role: Permission = roleResponse.body;
            this.data.changeUserPermission(role);
            this.data.currentUserPermission.subscribe((userPermission) => this.allPermission = userPermission);
            for (const resource of this.allPermission.resources) {
                if (resource.name === 'TRACKING_MANAGEMENT') {
                    this.permission = resource.permission;
                    this.data.setPermission(resource.permission);
                    return;
                }
            }
        });
        this.loadListProjects();
        // this.columnDefs = AppConstants.TrackingItems;
        this.rowData = [];
        this.registerChangeInTrackingManagement();
    }
    getListColumnsCustomReport() {
        this.customReportsService.query('TrackingTasks', this.account.login).subscribe((res) => {
            if (res.body) {
                this.customReports = res.body;
                this.customReports.value = JSON.parse(this.customReports.value);
            }
        });
    }
    onGridReady(params) {
        this.gridApi = params.api;
        this.gridColumnApi = params.columnApi;

        params.api.sizeColumnsToFit();
    }
    saveCustomReport(fields?: any) {
        if (this.customReports.id !== undefined) {
            this.customReports.value = fields;
            this.customReportsService.update(this.customReports).subscribe((res) => {
                if (res.body !== undefined) {
                    this.search();
                }
            });
        } else {
            this.customReports.userLogin = this.account.login;
            this.customReports.pageName = 'TrackingTasks';
            this.customReports.value = fields;
            this.customReportsService.create(this.customReports).subscribe((res) => {
                if (res.body !== undefined) {
                    this.customReports = res.body;
                    this.customReports.value = JSON.parse(this.customReports.value);
                    this.search();
                }
            });
        }
    }
    loadAll() {
    }
    loadListProjects() {
        this.projectsService.getListProjectByUserLogin().subscribe(
            (res: HttpResponse<Projects[]>) => {
                this.projects = res.body;
                if (this.projects.length > 0) {
                    this.projetId = this.projects[0].id;
                    this.loadListPurchaseOrder(this.projetId);
                } else {
                    if (this.purchaseOrderId !== undefined) {
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
                    this.purchaseOrderId = '0';
                    if (this.package !== undefined) {
                        this.package = [];
                    }
                }
            }, (res: HttpErrorResponse) => this.onError(res.message)
        );
    }
    loadListPackageByPO(poId?: any) {
        this.packagesService.query({
            size: 1000000}, poId).subscribe(
            (res: HttpResponse<Packages[]>) => {
                this.package = res.body;
                this.packageId = '0';
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    loadPurchaseOrderByProject() {
        if (this.projetId === null) {
            this.purchaseOrderId = '0';
            this.packageId = '0';
        } else {
            this.loadListPurchaseOrder(this.projetId);
        }
    }
    loadPackageByPO() {
        if (this.purchaseOrderId === '0') {
            this.packageId = '0';
        } else {
            this.loadListPackageByPO(this.purchaseOrderId);
        }
    }

    loadScreen(flag) {
        this.flagTag = flag;
        this.search();
    }

    search() {
        const query = {
            page: this.page - 1,
            size: this.itemsPerPage,
            from: this.fromDate,
            to: this.toDate,
            userLogin: this.userLoginExport,
            sort: this.sort()
        };
        if (this.flagTag) {
            this.bugTrackingMembers(query);
        } else {
            this.bugTrackingTask(query);
        }
    }

    bugTrackingTask(query) {
        this.flagTag = false;
        this.columnDefs = AppConstants.TrackingItems;
        this.rowData = [];
        this.spinnerService.show();
        if (this.projetId !== undefined && this.purchaseOrderId === '0') {
            this.package = [];
            this.packageId = 0;
            query.projectId = this.projetId;
            {
            }
            this.tasksService.queryBugTrackingTasks(query).subscribe(
                (resJson: HttpResponse<any>) => {
                    this.onSuccess(resJson.body, resJson.headers);
                }, (res: HttpErrorResponse) => this.onError(res.message)
            );
            this.tasksService.queryRatioTaskHasBug({
                projectId: this.projetId,
                from: this.fromDate,
                to: this.toDate
            }).subscribe(
                (resJson: HttpResponse<any>) => {
                    this.ratioBug = resJson.body;
                    console.log(JSON.stringify(this.ratioBug));
                }
            );
        } else if (this.packageId === '0') {
            query.purchaseOrderId = this.purchaseOrderId;
            this.tasksService.queryBugTrackingTasks(query).subscribe(
                (resJson: HttpResponse<any>) => {
                    this.onSuccess(resJson.body, resJson.headers);
                }, (res: HttpErrorResponse) => this.onError(res.message)
            );
            this.tasksService.queryRatioTaskHasBug({
                purchaseOrderId: this.purchaseOrderId,
                from: this.fromDate,
                to: this.toDate
            }).subscribe(
                (resJson: HttpResponse<any>) => {
                    this.ratioBug = resJson.body;
                    console.log(JSON.stringify(this.ratioBug));
                }
            );
        } else {
            query.packId = this.packageId;
            this.tasksService.queryBugTrackingTasks(query).subscribe(
                (resJson: HttpResponse<any>) => {
                    this.onSuccess(resJson.body, resJson.headers);
                }, (res: HttpErrorResponse) => this.onError(res.message)
            );
            this.tasksService.queryRatioTaskHasBug({
                packId: this.packageId,
                from: this.fromDate,
                to: this.toDate
            }).subscribe(
                (resJson: HttpResponse<any>) => {
                    this.ratioBug = resJson.body;
                    console.log('p3' + this.ratioBug.toString());
                }
            );
        }
    }
    bugTrackingMembers(query) {
        this.flagTag = true;
        this.columnDefs = AppConstants.TrackingMemberItems;
        this.rowData = [];
        query.projectId = this.projetId;
        this.spinnerService.show();
        if (this.projetId !== undefined && this.purchaseOrderId === '0') {
            this.package = [];
            this.tasksService.queryBugTrackingMember(query).subscribe(
                (res: HttpResponse<any>) => {
                    this.onSuccess(res.body, res.headers);
                }
            );
        } else if (this.packageId === '0') {
            query.purchaseOrderId = this.purchaseOrderId;
            this.tasksService.queryBugTrackingMember(query).subscribe(
                (res: HttpResponse<any>) => {
                    this.onSuccess(res.body, res.headers);
                }
            );
        } else {
            query.packId = this.packageId;
            this.tasksService.queryBugTrackingMember(query).subscribe(
                (res: HttpResponse<any>) => {
                    this.onSuccess(res.body, res.headers);
                }
            );
        }

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
        this.router.navigate(['/tracking-management'], {queryParams:
                {
                    page: this.page,
                    size: this.itemsPerPage,
                    search: this.currentSearch,
                    sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
                }
        });
        const query = {
            page: this.page - 1,
            size: this.itemsPerPage,
            from: this.fromDate,
            to: this.toDate,
            sort: this.sort()
        };
        if (this.flagTag) {
            this.bugTrackingMembers(query);
        } else {
            this.bugTrackingTask(query);
        }

    }

    clear() {
        this.page = 0;
        this.currentSearch = '';
        this.router.navigate(['/tracking-management', {
            page: this.page,
            sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
        }]);
        this.loadAll();
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
        this.eventSubscriber = this.eventManager.subscribe('trackingManagementListModification',
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
        let checkForEach = true;
        data.forEach((row) => {
            if (checkForEach) {
                if (row.countOpRound > 0) {
                    this.generaRound(row.countOpRound, 'op', 'OP_');
                }
                if (row.countReview1Round > 0) {
                    this.generaRound(row.countReview1Round, 'review1', 'Review1_');
                }
                if (row.countReview2Round > 0) {
                    this.generaRound(row.countReview2Round, 'review2', 'Review2_');
                }
                if (row.countFiRound > 0) {
                    this.generaRound(row.countFiRound, 'fi', 'Fi_');
                }
                if (row.countFixerRound > 0) {
                    this.generaRound(row.countFixerRound, 'fixer', 'Fixer_');
                }
                checkForEach = false;
            } else {
                return false;
            }
        });
        this.rowData = data;
        this.spinnerService.hide();
    }
    private generaRound(roundNumber?: any, nameRound?: any, header?: any) {
        for (let i = 1; i <= roundNumber; i++) {
            this.columnDefs.push(this.jsonRound(i, nameRound, header));
        }
    }
    private jsonRound(index?: any, nameRound?: any, header?: any) {
        return {
            headerName: header + index,
            children: [
                {
                    headerName: 'Name',
                    field: nameRound + index,
                    cellRenderer: null,
                    colId: nameRound + index,
                    editable: false,
                    sort: 'asc',
                    sortable: true,
                    hide: false,
                },
                {
                    headerName: 'Actual Effort',
                    field: nameRound + index + 'Effort',
                    cellRenderer: null,
                    colId: nameRound + index + 'Effort',
                    editable: false,
                    sort: 'asc',
                    sortable: true,
                    hide: false,
                },
                {
                    headerName: 'Start Time',
                    field: nameRound + index + 'StartTime',
                    colId: nameRound + index + 'StartTime',
                    editable: false,
                    sort: 'asc',
                    sortable: true,
                    cellRenderer: (data) => {
                        return data.value != null ? moment(data.value).format('DD/MM/YYYY HH:mm:ss') : '';
                    },
                    hide: false,
                },
                {
                    headerName: 'End Time',
                    field: nameRound + index + 'EndTime',
                    colId: nameRound + index + 'EndTime',
                    editable: false,
                    sort: 'asc',
                    sortable: true,
                    cellRenderer: (data) => {
                        return data.value != null ? moment(data.value).format('DD/MM/YYYY HH:mm:ss') : '';
                    },
                    hide: false,
                }
            ],
        };
    }
    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
