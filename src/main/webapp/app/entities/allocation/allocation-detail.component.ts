import { Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {HttpErrorResponse, HttpResponse} from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import {JhiEventManager, JhiDataUtils, JhiParseLinks, JhiAlertService} from 'ng-jhipster';

import { MembersManagement } from '../members-management/members-management.model';
import {ProjectRoles, ProjectUsers} from '../project-users/project-users.model';
import { ProjectUsersService } from '../project-users/project-users.service';
import {MembersManagementService} from '../members-management/members-management.service';
import {DatePipe} from '@angular/common';
import {Packages} from '../packages/packages.model';
import {ITEMS_PER_PAGE} from '../../shared';
import {GridOptions} from 'ag-grid';
import {FieldConfig} from '../../shared/dynamic-forms/field.interface';
import {AppConstants} from '../../shared/services/app-constants';
import {ButtonViewComponent} from '../../shared/dynamic-data-tables/buttons-view/button-view.component';
import {AllocationService} from './allocation.service';

@Component({
    selector: 'jhi-allocation-detail',
    templateUrl: './allocation-detail.component.html'
})
export class AllocationDetailComponent implements OnInit, OnDestroy {

    projectUserId: any;
    dateFormat = 'dd-MM-yyyy';
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

    allocationDetail: any;
    frameworkComponents: any;
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
        private allocationService: AllocationService,
        private datePipe: DatePipe,
        private router: Router,
        private jhiAlertService: JhiAlertService,
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
        this.displayedColumns = AppConstants.EffortPackageItems;
        this.subscription = this.route.params.subscribe((params) => {
            this.projectUserId = params['id'];
            this.loadAll(this.projectUserId);
        });
        this.registerChangeInPurchaseOrders();
    }
    loadPage(page: number) {
        this.page = page;
        if (page !== this.previousPage) {
            this.previousPage = page;
            this.transition();
        }
    }
    transition() {
        this.router.navigate(['/allocation', this.projectUserId], {queryParams:
                {
                    page: this.page,
                    size: this.itemsPerPage,
                    sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
                }
        });
        this.loadAll(this.projectUserId);
    }
    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }
    loadAll(projectUserId?: any) {
        this.allocationService.queryDetail({
            page: this.page - 1,
            size: this.itemsPerPage,
            sort: this.sort()}, projectUserId).subscribe(
            (resJson: HttpResponse<any[]>) => {
                this.allocationDetail = resJson.body;
                this.onSuccess(resJson.body, resJson.headers);
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
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
            'allocationListModification',
            (response) => this.loadAll(this.projectUserId)
        );
    }
}
