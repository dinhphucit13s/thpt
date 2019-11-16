import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { ProjectUsers } from './project-users.model';
import { ProjectUsersService } from './project-users.service';
import { ITEMS_PER_PAGE, Principal } from '../../shared';
import {GridOptions} from 'ag-grid';
import {ButtonViewComponent} from '../../shared/dynamic-data-tables/buttons-view/button-view.component';
import {AppConstants} from '../../shared/services/app-constants';
import { DataService } from '../../shared/services/data.service';
import { Permission } from '../../account/login/permission.model';
import { PermissionService } from '../../account/login/permission.service';
import {Projects, ProjectsService} from '../projects';

@Component({
    selector: 'jhi-project-users-list',
    templateUrl: './project-users-list.component.html',
    providers: [PermissionService]
})
export class ProjectUsersListComponent implements OnInit, OnDestroy {
    currentAccount: any;
    projectUsers: ProjectUsers[];
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
    project: Projects;
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
        private projectUsersService: ProjectUsersService,
        private parseLinks: JhiParseLinks,
        private jhiAlertService: JhiAlertService,
        private principal: Principal,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private eventManager: JhiEventManager,
        private route: ActivatedRoute,
        private data: DataService,
        private permissionService: PermissionService,
        private projectsService: ProjectsService
    ) {
        this.itemsPerPage = ITEMS_PER_PAGE;
        // tslint:disable-next-line:no-shadowed-variable
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
        this.projectsService.find(this.projectId).subscribe(
            (res) => {
                this.project = res.body;
                this.checkPermission(this.project);
            }
        );

        if (this.currentSearch) {
            this.projectUsersService.search({
                page: this.page - 1,
                query: [ this.currentSearch, this.projectId ],
                size: this.itemsPerPage,
                sort: this.sort()}).subscribe(
                    (res: HttpResponse<ProjectUsers[]>) => this.onSuccess(res.body, res.headers),
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
            return;
        }
        this.projectUsersService.query({
            page: this.page - 1,
            size: this.itemsPerPage,
            sort: this.sort()}, this.projectId).subscribe(
                (res: HttpResponse<ProjectUsers[]>) => this.onSuccess(res.body, res.headers),
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
        const url = `/project-users-list/${this.projectId}/${this.projectName}`;
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
        const url = `/project-users-list/${this.projectId}/${this.projectName}`;
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
        const url = `/project-users-list/${this.projectId}/${this.projectName}`;
        this.router.navigate([url, {
            search: this.currentSearch,
            page: this.page,
            sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
        }]);
        this.loadAll();
    }
    ngOnInit() {
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
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
        this.columnDefsArg = AppConstants.ProjectUserListItems;
        this.subscription = this.route.params.subscribe((params) => {
            this.projectId = params['id'];
            this.projectName = params['id2'];
        });
        this.loadAll();
        this.registerChangeInProjectUsers();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: ProjectUsers) {
        return item.id;
    }
    registerChangeInProjectUsers() {
        this.eventSubscriber = this.eventManager.subscribe('projectUsersListModification', (response) => this.loadAll());
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
        this.projectUsers = data;
        this.rowData = data;
    }
    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }

    checkPermission(project: Projects) {
        if (project.watcherUsers) {
            if (project.watcherUsers.indexOf(this.currentAccount.login) > -1) {
                this.permission = 1;
            }
        }

        if (project.dedicatedUsers) {
            if (project.dedicatedUsers.indexOf(this.currentAccount.login) > -1) {
                this.permission = 3;
            }
        }

        if (project.projectLeadUserLogin
            && project.projectLeadUserLogin.toLocaleLowerCase() === this.currentAccount.login.toLocaleLowerCase()) {
            this.permission = 4;
        }
    }
}
