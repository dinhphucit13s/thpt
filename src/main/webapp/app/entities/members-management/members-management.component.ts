import { Component, OnInit, OnDestroy, ViewChild, Inject, Injectable} from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiParseLinks, JhiAlertService, JhiDataUtils } from 'ng-jhipster';
import {PermissionService} from '../../account/login/permission.service';
import {ITEMS_PER_PAGE} from '../../shared';
import { MembersManagementService} from '../members-management/members-management.service';
import { MembersManagement } from '../members-management/members-management.model';
import {AppConstants} from '../../shared/services/app-constants';
import {ButtonViewComponent} from '../../shared/dynamic-data-tables/buttons-view/button-view.component';
import {Projects} from '../projects/projects.model';
import {ProjectsService} from '../projects/projects.service';
import {Permission} from '../../account/login/permission.model';
import {DataService} from '../../shared/services/data.service';
import {DatePipe} from '@angular/common';
import {LoginTracking, LoginTrackingService} from '../login-tracking';
import {BusinessLine} from '../business-line/business-line.model';
import {PurchaseOrders, PurchaseOrdersService} from '../purchase-orders';
import {Packages, PackagesService} from '../packages';

@Component({
    selector: 'jhi-members-management',
    templateUrl: './members-management.component.html',
    providers: [PermissionService]

})

export class MembersManagementComponent implements OnInit, OnDestroy {
    links: any;
    totalItems: any;
    queryCount: any;
    itemsPerPage: any;
    page: any;
    currentSearch: string;
    routeData: any;
    predicate: any;
    previousPage: any;
    reverse: any;
    membersManagementJson: any[];
    displayedColumns: any;
    columnDefsArg: any;
    rowData: any;
    frameworkComponents: any;
    /*show project name*/
    proId: any;
    projects: Projects[];
    projectId: any;
    purchaseOrder: PurchaseOrders[];
    purchaseOrderId: any;
    packages: Packages[];
    packageId: any;
    selectedOptions: number;
    /*permission*/
    allPermission: Permission;
    permission: number;
    dateFormat = require('dateformat');
    exportLink: any;

    constructor(
        private activatedRoute: ActivatedRoute,
        private membersManagementService: MembersManagementService,
        private parseLinks: JhiParseLinks,
        private jhiAlertService: JhiAlertService,
        private router: Router,
        private projectsService: ProjectsService,
        private purchaseOrdersService: PurchaseOrdersService,
        private packagesService: PackagesService,
        private loginTrackingService: LoginTrackingService,
        private permissionService: PermissionService,
        private data: DataService,
        private datePipe: DatePipe
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

    loadAll(proId?: any) {
        if (this.currentSearch) {
            this.membersManagementService.search({
                page: this.page - 1,
                query: this.currentSearch,
                size: this.itemsPerPage,
                sort: this.sort()}).subscribe(
                (res: HttpResponse<any[]>) => this.onSuccess(res.body, res.headers),
                (res: HttpErrorResponse) => this.onError(res.message)
            );
            return;
        }
        this.membersManagementService.query({
            page: this.page - 1,
            size: this.itemsPerPage,
            query: proId,
            sort: this.sort()}).subscribe(
            (resJson: HttpResponse<any[]>) => {
                this.onSuccess(resJson.body, resJson.headers);
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
                if (resource.name === 'MEMBERS_MANAGEMENT') {
                    this.permission = resource.permission;
                    this.data.setPermission(resource.permission);
                    return;
                }
            }
        });
        this.columnDefsArg = AppConstants.MembersManagementItems;
        this.rowData = [];
        this.loadListProjects();
    }
    showMemberByUserId(userId: any) {
        // this.loadPage(1);
        this.page = 1;
        this.proId = userId;
        this.loadAll(userId);
    }
    /*loadListProjectsOld() {
        this.projectsService.getListProjectByUserLogin().subscribe(
            (res: HttpResponse<Projects[]>) => {
                this.projects = res.body;
                if (this.projects.length > 0) {
                    this.selectedOptions = this.projects[0].id;
                    if (this.selectedOptions !== undefined) {
                        this.showMemberByUserId(this.selectedOptions);
                    }
                }
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }*/

    loadListProjects() {
        this.projectsService.getListProjectByUserLogin().subscribe(
            (res: HttpResponse<Projects[]>) => {
                this.projects = res.body;
                if (this.projects.length > 0) {
                    this.projectId = this.projects[0].id;
                    this.selectedOptions = this.projects[0].id;
                    if (this.selectedOptions !== undefined) {
                        this.showMemberByUserId(this.selectedOptions);
                    }
                    this.loadListPurchaseOrder(this.projectId);
                } else {
                    if (this.purchaseOrder !== undefined) {
                        this.purchaseOrder = [];
                    } else if (this.packages !== undefined) {
                        this.packages = [];
                    }
                    this.rowData = undefined;
                }
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

    loadListPurchaseOrder(projectId?: any) {
        this.purchaseOrdersService.query({
            size: 1000000}, projectId).subscribe(
            (res: HttpResponse<PurchaseOrders[]>) => {
                this.purchaseOrder = res.body;
                if (this.purchaseOrder.length > 0) {
                    this.purchaseOrderId = this.purchaseOrder[0].id;
                    this.selectedOptions = this.purchaseOrder[0].id;
                    if (this.selectedOptions !== undefined) {
                        this.showMemberByUserId(this.selectedOptions);
                    }
                    this.loadListPackageByPOId(this.purchaseOrderId);
                } else {
                    this.purchaseOrderId = 'selectAllPO';
                    if (this.packages !== undefined) {
                        this.packages = [];
                    }
                    // this.rowData = undefined;
                }
            }, (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    loadPackageByPO() {
        if (this.purchaseOrderId === 'selectAllPO') {
            this.packageId = null;
        } else {
            this.loadListPackageByPOId(this.purchaseOrderId);
        }
    }

    loadListPackageByPOId(poId?: any) {
        this.packagesService.query({
            size: 1000000}, poId).subscribe(
            (res: HttpResponse<Packages[]>) => {
                this.packages = res.body;
                this.packageId = 'selectAllPackages';
                this.selectedOptions = this.packages[0].id;
                if (this.selectedOptions !== undefined) {
                    this.showMemberByUserId(this.selectedOptions);
                }
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    loadMemberByPackage() {
        this.loadListMember();
    }

    loadListMember() {
        if (this.projectId !== undefined && this.purchaseOrderId === 'selectAllPO') {
            this.packages = [];
            this.packageId = null;
            this.membersManagementService.findByCondition({
                page: this.page - 1,
                size: this.itemsPerPage,
                projectId: this.projectId,
                sort: this.sort()}).subscribe(
                (resJson: HttpResponse<any>) => {
                    // this.rowData = resJson.body;
                    this.onSuccess(resJson.body, resJson.headers);
                }, (res: HttpErrorResponse) => this.onError(res.message)
            );
            this.exportLink = 'api/user/export?projectId=' + this.projectId;
        }else if (this.packageId === 'selectAllPackages') {
            this.membersManagementService.findByCondition({
                page: this.page - 1,
                size: this.itemsPerPage,
                purchaseOrderId: this.purchaseOrderId,
                projectId: this.projectId,
                sort: this.sort()}).subscribe(
                (resJson: HttpResponse<any>) => {
                    // this.rowData = resJson.body;
                    this.onSuccess(resJson.body, resJson.headers);
                }, (res: HttpErrorResponse) => this.onError(res.message)
            );
            this.exportLink = 'api/user/export?purchaseOrderId=' + this.purchaseOrderId;
        }else {
            this.membersManagementService.findByCondition({
                page: this.page - 1,
                size: this.itemsPerPage,
                packId: this.packageId,
                projectId: this.projectId,
                sort: this.sort()}).subscribe(
                (resJson: HttpResponse<any>) => {
                    // this.rowData = resJson.body;
                    this.onSuccess(resJson.body, resJson.headers);
                }, (res: HttpErrorResponse) => this.onError(res.message)
            );
            this.exportLink = 'api/user/export?packId=' + this.packageId;
        }
    }

    search(query) {
        if (!query) {
            return this.clear();
        }
        this.page = 0;
        this.currentSearch = query;
        this.router.navigate(['/members-management', {
            search: this.currentSearch,
            page: this.page,
            sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
        }]);
        this.loadAll();
    }
    clear(selectedOptions?: any) {
        this.page = 0;
        this.currentSearch = '';
        this.router.navigate(['/members-management', {
            page: this.page,
            sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
        }]);
        this.loadAll(selectedOptions);
    }
    loadPage(page: number) {
        this.page = page;
        if (page !== this.previousPage) {
            this.previousPage = page;
            this.transition();
        }
    }
    transition() {
        this.router.navigate(['/members-management'], {queryParams:
                {
                    page: this.page,
                    size: this.itemsPerPage,
                    search: this.currentSearch,
                    sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
                }
        });
        this.loadAll(this.selectedOptions);
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
    ngOnDestroy() {

    }
}
