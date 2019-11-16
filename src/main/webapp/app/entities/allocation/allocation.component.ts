import { Component, OnInit, OnDestroy, ViewChild, Inject, Injectable} from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiParseLinks, JhiAlertService, JhiDataUtils } from 'ng-jhipster';
import {PermissionService} from '../../account/login/permission.service';
import {ITEMS_PER_PAGE} from '../../shared';
import { MembersManagementService } from '../members-management/members-management.service';
import {AppConstants} from '../../shared/services/app-constants';
import {ButtonViewComponent} from '../../shared/dynamic-data-tables/buttons-view/button-view.component';
import {Projects} from '../projects/projects.model';
import {ProjectsService} from '../projects/projects.service';
import {Permission} from '../../account/login/permission.model';
import {DataService} from '../../shared/services/data.service';
import {DatePipe} from '@angular/common';
import {AllocationService} from './allocation.service';

@Component({
    selector: 'jhi-allocation',
    templateUrl: './allocation.component.html',
    providers: [PermissionService]

})

export class AllocationComponent implements OnInit, OnDestroy {
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
    displayedColumns: any;
    columnDefsArg: any;
    rowData: any;
    frameworkComponents: any;
    /*show project name*/
    proId: any;
    projects: Projects[];
    selectedOptions: number;
    /*permission*/
    allPermission: Permission;
    permission: number;
    dateFormat = require('dateformat');

    constructor(
        private activatedRoute: ActivatedRoute,
        private allocationService: AllocationService,
        private membersManagementService: MembersManagementService,
        private projectsService: ProjectsService,
        private parseLinks: JhiParseLinks,
        private jhiAlertService: JhiAlertService,
        private router: Router,
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
        this.allocationService.query({
            page: this.page - 1,
            size: this.itemsPerPage,
            sort: this.sort()}, this.proId).subscribe(
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
                if (resource.name === 'ALLOCATION') {
                    this.permission = resource.permission;
                    this.data.setPermission(resource.permission);
                    return;
                }
            }
        });
        this.columnDefsArg = AppConstants.AllocationItems;
        this.loadListProjects();
    }
    showMemberByUserId(userId: any) {
        this.loadPage(1);
        this.proId = userId;
        this.loadAll(this.proId);
    }
    loadListProjects() {
        this.projectsService.getListProjectByUserLogin().subscribe(
            (res: HttpResponse<Projects[]>) => {
                this.projects = res.body;
                if (this.projects.length > 0) {
                    this.selectedOptions = this.projects[0].id;
                    if (this.selectedOptions !== undefined) {
                        this.showMemberByUserId(this.selectedOptions);
                    }
                } else {
                    this.selectedOptions = 0;
                    this.rowData = [];
                }
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }
    search(query) {
        if (!query) {
            return this.clear();
        }
        this.page = 0;
        this.currentSearch = query;
        this.router.navigate(['/allocation', {
            search: this.currentSearch,
            page: this.page,
            sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
        }]);
        this.loadAll();
    }
    clear(selectedOptions?: any) {
        this.page = 0;
        this.currentSearch = '';
        this.router.navigate(['/allocation', {
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
        this.router.navigate(['/allocation'], {queryParams:
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
