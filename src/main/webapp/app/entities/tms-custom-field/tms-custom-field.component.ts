import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse, HttpErrorResponse} from '@angular/common/http';
import { TitleCasePipe } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiParseLinks, JhiAlertService, JhiDataUtils } from 'ng-jhipster';

import { TMSCustomField } from './tms-custom-field.model';
import { TMSCustomFieldService } from './tms-custom-field.service';
import { ITEMS_PER_PAGE, Principal } from '../../shared';
import {Observable} from 'rxjs/Observable';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';
import {FieldConfig, FieldDataConfig} from '../../shared/dynamic-forms/field.interface';
import {GridOptions} from 'ag-grid';
import {AppConstants} from '../../shared/services/app-constants';
import {ButtonViewComponent} from '../../shared/dynamic-data-tables/buttons-view/button-view.component';
import {Permission} from '../../account/login/permission.model';
import {PermissionService} from '../../account/login/permission.service';
import {DataService} from '../../shared/services/data.service';

@Component({
    selector: 'jhi-tms-custom-field',
    templateUrl: './tms-custom-field.component.html'
})
export class TMSCustomFieldComponent implements OnInit, OnDestroy {

currentAccount: any;
    tMSCustomFields: TMSCustomField[];
    tMSCustomFiled: TMSCustomField;
    nameField: any;
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
    isSaving: boolean;
    // dynamic form
    frameworkComponents: any;
    columnDefs: any;
    columnPackages: any;
    columnTasks: any;
    rowData = new Array<any>();
    gridApi: any;
    gridColumnApi: any;
    gridOptions: GridOptions;
    displayedColumns: FieldConfig[] = new Array<FieldConfig>();
    nameExists: boolean;
    allPermission: Permission;
    permission: number;
    formatDateTime: FieldConfig[] = new Array<FieldConfig>();
    formatDate: FieldConfig[] = new Array<FieldConfig>();

    constructor(
        private tMSCustomFieldService: TMSCustomFieldService,
        private parseLinks: JhiParseLinks,
        private jhiAlertService: JhiAlertService,
        private principal: Principal,
        private activatedRoute: ActivatedRoute,
        private dataUtils: JhiDataUtils,
        private router: Router,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager,
        private titleCasePipe: TitleCasePipe,
        private permissionService: PermissionService,
        private data: DataService,
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
        this.nameExists = false;
        this.isSaving = false;
        this.tMSCustomFiled = new TMSCustomField();
        this.tMSCustomFiled.entityData = new FieldDataConfig();
    }

    loadAll() {
        if (this.currentSearch) {
            this.tMSCustomFieldService.search({
                page: this.page - 1,
                query: this.currentSearch,
                size: this.itemsPerPage,
                sort: this.sort()}).subscribe(
                    (res: HttpResponse<TMSCustomField[]>) => this.onSuccess(res.body, res.headers),
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
            return;
        }
        this.tMSCustomFieldService.query({
            page: this.page - 1,
            size: this.itemsPerPage,
            sort: this.sort()}).subscribe(
                (res: HttpResponse<TMSCustomField[]>) => this.onSuccess(res.body, res.headers),
                (res: HttpErrorResponse) => this.onError(res.message)
        );
    }
    save() {
        this.isSaving = true;
        this.tMSCustomFiled.entityData.field = this.tMSCustomFiled.entityData.headerName.replace(/\s/g, '');
        this.tMSCustomFieldService.checkCustomFieldName(this.tMSCustomFiled.entityData.headerName.replace(/\s/g, '')).subscribe((result) => {
            if (!result.body) {
                this.checkHeaderNameExists();
            } else {
                this.nameExists = true;
            }
            if (!this.nameExists) {
                this.subscribeToSaveResponse(
                    this.tMSCustomFieldService.create(this.tMSCustomFiled));
            }
        });
    }
    private checkHeaderNameExists() {
        this.nameExists = false;
        this.columnPackages = AppConstants.PackageItems;
        this.columnTasks = AppConstants.TaskItems;
        const index = this.columnPackages.findIndex((field) =>
            field.headerName.toLowerCase().replace(/\s/g, '') === this.tMSCustomFiled.entityData.headerName.toLowerCase().replace(/\s/g, ''));
        const indexTask = this.columnTasks.findIndex((field) => field.headerName.replace(/\s/g, '') === this.tMSCustomFiled.entityData.headerName.replace(/\s/g, ''));
        if (index >= 0 || indexTask >= 0) {
            this.nameExists = true;
        }
    }
    changeFieldName() {
        this.nameExists = false;
    }
    private subscribeToSaveResponse(result: Observable<HttpResponse<TMSCustomField>>) {
        result.subscribe((res: HttpResponse<TMSCustomField>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: TMSCustomField) {
        this.router.navigate(['/tms-custom-field-create/' + result.id]);
        this.isSaving = false;
    }

    private onSaveError() {
        this.isSaving = false;
    }
    loadPage(page: number) {
        this.page = page;
        if (page !== this.previousPage) {
            this.previousPage = page;
            this.transition();
        }
    }
    transition() {
        this.router.navigate(['/tms-custom-field'], {queryParams:
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
        this.router.navigate(['/tms-custom-field', {
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
        this.router.navigate(['/tms-custom-field', {
            search: this.currentSearch,
            page: this.page,
            sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
        }]);
        this.loadAll();
    }
    ngOnInit() {
        this.formatDateTime = AppConstants.FormatDateTime;
        this.permissionService.findPermission().subscribe((roleResponse: HttpResponse<Permission>) => {
            const role: Permission = roleResponse.body;
            this.data.changeUserPermission(role);
            this.data.currentUserPermission.subscribe((userPermission) => this.allPermission = userPermission);
            for (const resource of this.allPermission.resources) {
                if (resource.name === 'TMS_CUSTOMER_FIELD') {
                    this.permission = resource.permission;
                    this.data.setPermission(resource.permission);
                    return;
                }
            }
        });
        this.columnDefs = AppConstants.TMSCustomFieldItems;
        this.columnDefs.forEach((col) => {
            if (col.format === 'date') {
                this.formatDateTime.forEach((fieldt) => {
                    col.cellRenderer = fieldt.cellRenderer;
                });
            } else if (col.format === 'datetime') {
                this.formatDate.forEach((fieldt) => {
                    col.cellRenderer = fieldt.cellRenderer;
                });
            }
        });
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInTMSCustomFields();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: TMSCustomField) {
        return item.id;
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }
    registerChangeInTMSCustomFields() {
        this.eventSubscriber = this.eventManager.subscribe('tMSCustomFieldListModification', (response) => this.loadAll());
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
        this.tMSCustomFields = data;
        this.tMSCustomFields.forEach((entity) => {
            entity.entityData = JSON.parse(entity.entityData);
        });
        this.rowData = this.tMSCustomFields;
        this.columnDefs.forEach((col) => {
            if (col.format === 'date') {
                this.formatDateTime.forEach((fieldt) => {
                    col.cellRenderer = fieldt.cellRenderer;
                });
            } else if (col.format === 'datetime') {
                this.formatDate.forEach((fieldt) => {
                    col.cellRenderer = fieldt.cellRenderer;
                });
            }
        });
    }
    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
