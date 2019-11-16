import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { ITEMS_PER_PAGE, Principal } from '../../shared';
import { GridOptions } from 'ag-grid';
import { ButtonViewComponent } from '../../shared/dynamic-data-tables/buttons-view/button-view.component';
import { AppConstants } from '../../shared/services/app-constants';
import { DataService } from '../../shared/services/data.service';
import { Permission } from '../../account/login/permission.model';
import { PermissionService } from '../../account/login/permission.service';
import {BugListDefault} from '../bug-list-default';
import {ProjectBugListDefaultsService} from './project-bug-list-defaults.service';
import {ProjectBugListDefaults} from './project-bug-list-defaults.model';

@Component({
    selector: 'jhi-project-bug-list-defaults',
    templateUrl: './project-bug-list-defaults.component.html',
    providers: [PermissionService]
})
export class ProjectBugListDefaultsComponent implements OnInit, OnDestroy {
    currentAccount: any;
    projectBugListDefaultList: ProjectBugListDefaults[];
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
    projectId: number;
    projectName;
    subscription: Subscription;
    // reuse view
    columnDefsArg: any;
    rowData: any;
    gridApi: any;
    gridColumnApi: any;
    frameworkComponents: any;
    gridOptions: GridOptions;
    permission: number;
    allPermission: Permission;

    constructor(
        private projectBugListDefaultsService: ProjectBugListDefaultsService,
        private parseLinks: JhiParseLinks,
        private jhiAlertService: JhiAlertService,
        private principal: Principal,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private eventManager: JhiEventManager,
        private route: ActivatedRoute,
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

    loadAll() {
        console.log('this.currentSearch value');
        console.log(this.projectId);
        if (this.currentSearch) {
            this.projectBugListDefaultsService.search({
                page: this.page - 1,
                size: this.itemsPerPage,
                sort: this.sort()}, this.currentSearch, this.projectId).subscribe(
                (res: HttpResponse<any[]>) => this.onSuccess(res.body, res.headers),
                (res: HttpErrorResponse) => this.onError(res.message)
            );
            return;
        }
        this.projectBugListDefaultsService.queryByProject({
            page: this.page - 1,
            size: this.itemsPerPage,
            sort: this.sort()}, this.projectId).subscribe(
            (res: HttpResponse<any[]>) => this.onSuccess(res.body, res.headers),
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }
    loadPage(page: number) {
        this.page = page;
        if (page !== this.previousPage) {
            this.previousPage = page;
            this.transition();
        }
    }
    transition() {
        const url = `/project-bug-list-defaults/${this.projectId}/${this.projectName}`;
        this.router.navigate([url], {queryParams:
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
        this.currentSearch = '';
        const url = `/project-bug-list-defaults/${this.projectId}/${this.projectName}`;
        this.router.navigate([url], {queryParams:
                {
                    page: this.page,
                    size: this.itemsPerPage,
                    sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
                }
        });
        this.loadAll();
    }
    search(query) {
        if (!query) {
            return this.clear();
        }
        this.page = 0;
        this.currentSearch = query;
        const url = `/project-bug-list-defaults/${this.projectId}/${this.projectName}`;
        this.router.navigate([url, {
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
                if (resource.name === 'PROJECT_USERS') {
                    this.permission = resource.permission;
                    this.data.setPermission(resource.permission);
                    return;
                }
            }
        });
        this.columnDefsArg = AppConstants.ProjectBugListDefaultsItems;
        this.subscription = this.route.params.subscribe((params) => {
            this.projectId = params['id'];
            this.projectName = params['project-name'];
        });
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInProjectUsers();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInProjectUsers() {
        this.eventSubscriber = this.eventManager.subscribe('projectBugListDefaultsListModification', (response) => this.loadAll());
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
        this.projectBugListDefaultList = data;
        this.rowData = data;
    }
    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
