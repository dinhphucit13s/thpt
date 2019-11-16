import { Component, OnInit, OnDestroy } from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import {JhiEventManager, JhiDataUtils, JhiParseLinks, JhiAlertService} from 'ng-jhipster';
import { ITEMS_PER_PAGE } from '../../shared';

import { Projects } from './projects.model';
import { ProjectsService } from './projects.service';
import {AppConstants} from '../../shared/services/app-constants';
import {Packages} from '../packages/packages.model';
import { PackagesService } from './../packages/packages.service';
import {ButtonViewComponent} from '../../shared/dynamic-data-tables/buttons-view/button-view.component';
import {HomeService} from '../../home/home.service';
import {Principal} from '../../shared';
import {GridOptions} from 'ag-grid';
import {FieldConfig} from '../../shared/dynamic-forms/field.interface';

@Component({
    selector: 'jhi-projects-detail',
    templateUrl: './projects-detail.component.html'
})
export class ProjectsDetailComponent implements OnInit, OnDestroy {
    currentAccount: any;
    projects: Projects;
    columnDefsArg: any;
    rowData: any;
    links: any;
    queryCount: any;
    totalItems: any;
    itemsPerPage: any;
    page: any;
    predicate: any;
    previousPage: any;
    reverse: any;
    routeData: any;
    gridApi: any;
    gridColumnApi: any;
    frameworkComponents: any;
    gridOptions: GridOptions;
    projectId: number;
    isEdit = true;
    formatDateTime: FieldConfig[] = new Array<FieldConfig>();
    formatDate: FieldConfig[] = new Array<FieldConfig>();
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private dataUtils: JhiDataUtils,
        private parseLinks: JhiParseLinks,
        private jhiAlertService: JhiAlertService,
        private principal: Principal,
        private router: Router,
        private activatedRoute: ActivatedRoute,
        private projectsService: ProjectsService,
        private route: ActivatedRoute,
        private packagesService: PackagesService,
        private homService: HomeService,
    ) {
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.routeData = this.activatedRoute.data.subscribe((data) => {
            this.page = data.pagingParams.page;
            this.previousPage = data.pagingParams.page;
            this.reverse = data.pagingParams.ascending;
            this.predicate = data.pagingParams.predicate;
        });
    }

    ngOnInit() {
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.subscription = this.route.params.subscribe((params) => {
            this.projectId = params['id'];
            this.load(this.projectId);
            this.loadLatePackageColumn(this.projectId);
            this.loadLatePackageData(this.projectId);
        });
        this.registerChangeInProjects();
        this.frameworkComponents = {
            actionsButton: ButtonViewComponent,
        };
    }

    load(id) {
        this.projectsService.find(id)
            .subscribe((projectsResponse: HttpResponse<Projects>) => {
                this.projects = projectsResponse.body;
                if (this.projects.watcherUsers && this.projects.watcherUsers.indexOf(this.currentAccount.login) > -1) {
                    this.isEdit = false;
                }
            });
    }
    loadLatePackageColumn(id) {
        this.formatDateTime = AppConstants.FormatDateTime;
        this.formatDate = AppConstants.FormatDate;
        this.homService.loadLatePackageColumn(id)
            .subscribe((res: HttpResponse<any>) => {
                this.columnDefsArg = res.body;
                this.columnDefsArg.forEach((field) => {
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
            });
    }

    loadLatePackageData(id) {
        this.homService.loadLatePackageData({
            page: this.page - 1,
            size: this.itemsPerPage,
            sort: this.sort() } ,  id)
            .subscribe((res: HttpResponse<any>) => {
                this.onSuccess(res.body, res.headers);
            });
    }
    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    loadPage(page: number) {
        this.page = page;
        if (page !== this.previousPage) {
            this.previousPage = page;
            this.transition();
        }
    }
    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }
    transition() {
        this.router.navigate(['/' + 'purchase-orders/' + this.projectId], {queryParams:
                {
                    page: this.page,
                    size: this.itemsPerPage,
                    sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
                }
        });
    }
    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }
    previousState() {
        window.history.back();
    }

    private onSuccess(data, headers) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = headers.get('X-Total-Count');
        this.queryCount = this.totalItems;
        // this.page = pagingParams.page;
        this.rowData = data;
    }
    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInProjects() {
        this.eventSubscriber = this.eventManager.subscribe(
            'projectsListModification',
            (response) => this.load(this.projects.id)
        );
    }
}
