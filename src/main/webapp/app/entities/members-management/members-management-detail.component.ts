import { Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import {JhiEventManager, JhiDataUtils, JhiParseLinks} from 'ng-jhipster';

import { MembersManagement } from './members-management.model';
import {ProjectRoles, ProjectUsers} from '../project-users/project-users.model';
import { ProjectUsersService } from '../project-users/project-users.service';
import {MembersManagementService} from './members-management.service';
import {DatePipe} from '@angular/common';
import {Packages} from '../packages/packages.model';
import {ITEMS_PER_PAGE} from '../../shared';
import {GridOptions} from 'ag-grid';
import {FieldConfig} from '../../shared/dynamic-forms/field.interface';
import {AppConstants} from '../../shared/services/app-constants';
import {ButtonViewComponent} from '../../shared/dynamic-data-tables/buttons-view/button-view.component';

@Component({
    selector: 'jhi-members-management-detail',
    templateUrl: './members-management-detail.component.html'
})
export class MembersManagementDetailComponent implements OnInit, OnDestroy {

    projectUsersJson: any[];
    countProjects: ProjectUsers[];
    membersManagement: MembersManagement;
    memberCurrent: any;
    dateFormat = 'dd-MM-yyyy';
    getTimeDiff: any;
    result: any;
    dateStart: Date;
    dateEnd: Date;
    resultDate: any[] = [];
    highest = 'Member';
    /*Page*/
    routeData: any;
    totalItems: any;
    queryCount: any;
    itemsPerPage: any;
    page: any;
    proId: any;
    predicate: any;
    previousPage: any;
    reverse: any;
    links: any;

    frameworkComponents: any;
    columnDefs: any;
    columnDefsAg: any;
    rowData: any;
    gridApi: any;
    gridColumnApi: any;
    gridOptions: GridOptions;
    displayedColumns: FieldConfig[] = new Array<FieldConfig>();

    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private dataUtils: JhiDataUtils,
        private projectUsersService: ProjectUsersService,
        private datePipe: DatePipe,
        private router: Router,
        private activatedRoute: ActivatedRoute,
        private parseLinks: JhiParseLinks,
        private membersManagementService: MembersManagementService,
        private route: ActivatedRoute
    ) {
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.routeData = this.activatedRoute.data.subscribe((data) => {
            this.page = data.pagingParams.page;
            this.previousPage = data.pagingParams.page;
            this.reverse = data.pagingParams.ascending;
            this.predicate = data.pagingParams.predicate;
        });
        this.frameworkComponents = {
            actionsButton: ButtonViewComponent,
        };
    }

    ngOnInit() {
        this.displayedColumns = AppConstants.HistoryMembersItems;
        this.subscription = this.route.params.subscribe((params) => {
            this.memberCurrent = params['userLogin'];
            debugger;
            this.loadAll(this.memberCurrent);
        });
        this.registerChangeInPurchaseOrders();
    }

    getDays() {
        this.projectUsersJson.forEach((item) => {
            this.dateStart = new Date(item.startDate);
            this.dateEnd = new Date(item.endDate);
            this.getTimeDiff = this.dateEnd.getTime() - this.dateStart.getTime();
            this.result = this.getTimeDiff / (24 * 60 * 60 * 1000);
            item.days = this.result;
            debugger;
        });
    }

    getHighestRole() {
        this.projectUsersJson.forEach((item) => {
            if (item.roleName.toString() === 'PM') {
                this.highest = 'PM';
                return;
            } else if (item.roleName.toString() === 'TEAMLEAD') {
                this.highest = 'TEAMLEAD';
            } else {}
            debugger;
        });
    }
    loadPage(page: number) {
        this.page = page;
        if (page !== this.previousPage) {
            this.previousPage = page;
            debugger;
            this.transition();
        }
    }
    transition() {
        // members-management/:userLogin
        this.router.navigate(['/members-management', this.memberCurrent], {queryParams:
                {
                    page: this.page,
                    size: this.itemsPerPage,
                    sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
                }
        });
        this.loadAll(this.memberCurrent);
        debugger;
    }
    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }
    loadAll(userLogin?: any) {
        this.projectUsersService.getListProjectUserByUserLogin({
            page: this.page - 1,
            size: this.itemsPerPage,
            sort: this.sort()}, userLogin)
            .subscribe((res: HttpResponse<ProjectUsers[]>) => {
                this.onSuccess(res.body, res.headers),
                // this.projectUsers = res.body;
                this.getDays();
                this.getHighestRole();
            });
        this.projectUsersService.getListProjectUserByUserLogin({
            size: 10000000}, userLogin)
            .subscribe((res: HttpResponse<ProjectUsers[]>) => {
                this.countProjects = res.body;
                debugger;
            });
        this.membersManagementService.find(userLogin)
            .subscribe((res: HttpResponse<MembersManagement>) => {
                this.membersManagement = res.body;
            });
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }
    private onSuccess(data, headers) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = headers.get('X-Total-Count');
        this.queryCount = this.totalItems;
        // this.page = pagingParams.page;
        this.projectUsersJson = data;
        this.rowData = data;
    }
    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }
    previousState() {
        window.history.back();
    }
    trackId(index: number, item: ProjectUsers) {
        return item.id;
    }
    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInPurchaseOrders() {
        this.eventSubscriber = this.eventManager.subscribe(
            'membersManagementListModification',
            (response) => this.loadAll(this.membersManagement.id)
        );
    }
}
