import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiParseLinks, JhiAlertService, JhiDataUtils } from 'ng-jhipster';

import { TMSCustomFieldScreenValue } from './tms-custom-field-screen-value.model';
import { TMSCustomFieldScreenValueService } from './tms-custom-field-screen-value.service';
import { ITEMS_PER_PAGE, Principal } from '../../shared';
import { DataService } from '../../shared/services/data.service';
import { Permission } from '../../account/login/permission.model';
import { PermissionService } from '../../account/login/permission.service';

@Component({
    selector: 'jhi-tms-custom-field-screen-value',
    templateUrl: './tms-custom-field-screen-value.component.html',
    providers: [PermissionService]
})
export class TMSCustomFieldScreenValueComponent implements OnInit, OnDestroy {

currentAccount: any;
    tMSCustomFieldScreenValues: TMSCustomFieldScreenValue[];
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
    permission: number;
    allPermission: Permission;

    constructor(
        private tMSCustomFieldScreenValueService: TMSCustomFieldScreenValueService,
        private parseLinks: JhiParseLinks,
        private jhiAlertService: JhiAlertService,
        private principal: Principal,
        private activatedRoute: ActivatedRoute,
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
    }

    loadAll() {
        if (this.currentSearch) {
            this.tMSCustomFieldScreenValueService.search({
                page: this.page - 1,
                query: this.currentSearch,
                size: this.itemsPerPage,
                sort: this.sort()}).subscribe(
                    (res: HttpResponse<TMSCustomFieldScreenValue[]>) => this.onSuccess(res.body, res.headers),
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
            return;
        }
        this.tMSCustomFieldScreenValueService.query({
            page: this.page - 1,
            size: this.itemsPerPage,
            sort: this.sort()}).subscribe(
                (res: HttpResponse<TMSCustomFieldScreenValue[]>) => this.onSuccess(res.body, res.headers),
                (res: HttpErrorResponse) => this.onError(res.message)
        );
    }
    loadPage(page: number) {
        if (page !== this.previousPage) {
            this.previousPage = page;
            this.transition();
        }
    }
    transition() {
        this.router.navigate(['/tms-custom-field-screen-value'], {queryParams:
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
        this.page = 0;
        this.currentSearch = '';
        this.router.navigate(['/tms-custom-field-screen-value', {
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
        this.router.navigate(['/tms-custom-field-screen-value', {
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
            this.data.currentUserPermission.subscribe(userPermission => this.allPermission = userPermission);
            for (let resource of this.allPermission.resources) {
                console.log(resource.name,resource.permission);
                if (resource.name == 'TMS_CUSTOMER_FIELD') {
                    this.permission = resource.permission;
                    console.log('this.permission',this.permission);
                }
            }
        });
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInTMSCustomFieldScreenValues();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: TMSCustomFieldScreenValue) {
        return item.id;
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }
    registerChangeInTMSCustomFieldScreenValues() {
        this.eventSubscriber = this.eventManager.subscribe('tMSCustomFieldScreenValueListModification', (response) => this.loadAll());
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
        this.tMSCustomFieldScreenValues = data;
    }
    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
