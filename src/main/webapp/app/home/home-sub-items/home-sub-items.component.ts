import {Component, Input, OnChanges, OnInit, SimpleChanges} from '@angular/core';
import {HttpResponse} from '@angular/common/http';
import {HomeService} from '../home.service';
import {ITEMS_PER_PAGE} from '../../shared';
import {FieldConfig} from '../../shared/dynamic-forms/field.interface';
import {AppConstants} from '../../shared/services/app-constants';

@Component({
    selector: 'jhi-home-sub',
    templateUrl: './home-sub-items.component.html',
    styleUrls: ['./home-sub-items.component.css']
})

export class HomeSubItemsComponent implements OnInit, OnChanges{
    @Input() type: any;
    @Input() projectId: any;
    title: any;

    columnDefsArg: any;
    rowData: any = [];

    // paging
    itemsPerPage: any;
    routeData: any;
    page: any;
    previousPage: any;
    reverse: any;
    predicate: any;
    links: any;
    totalItems: any;
    queryCount: any;
    formatDateTime: FieldConfig[] = [];

    constructor(private homService: HomeService) {
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.page = 1;
        this.previousPage = 1;
        this.reverse = true;
        this.predicate = 'id';
    }

    ngOnInit() {
        this.formatDateTime = AppConstants.FormatDateTime;
        // this.loadData();
    }

    loadData() {
        switch (this.type) {
            case 'packages-late':
                this.title = 'PACKAGES LATE';
                this.loadLatePackageColumn(this.projectId);
                break;
            case 'tasks-late':
                this.title = 'TASKS LATE';
                this.loadLateTaskColumn(this.projectId);
                break;
            case 'tasks-un-assign':
                this.title = 'TASKS UN-ASSIGN';
                this.loadUnAssignTaskColumn(this.projectId);
                break;
            default:
                this.title = '';
        }
    }

    ngOnChanges(changes: SimpleChanges) {
        console.log(changes);
        if (changes.projectId) {
            this.page = 1;
            this.previousPage = 1;
            this.reverse = true;
            this.loadData();
        }
    }

    loadLatePackageColumn(id) {
        this.homService.loadLatePackageColumn(id)
            .subscribe((res: HttpResponse<any>) => {
                this.columnDefsArg = res.body;
                this.columnDefsArg.forEach((field) => {
                    if (field.format === 'datetime') {
                        this.formatDateTime.forEach((fieldFormat) => {
                            field.cellRenderer = fieldFormat.cellRenderer;
                        });
                    }
                });
                this.loadLatePackageData(id);
            });
    }

    loadLatePackageData(id) {
        this.homService.loadLatePackageData({
            page: this.page - 1,
            size: this.itemsPerPage,
            sort: this.sort()}, id)
            .subscribe((res: HttpResponse<any>) => {
                this.onSuccess(res.body, res.headers);
            });
    }

    loadLateTaskColumn(id) {
        this.homService.loadLateTaskColumn(id)
            .subscribe((res: HttpResponse<any>) => {
                this.columnDefsArg = res.body;
                this.columnDefsArg.forEach((field) => {
                    if (field.field === 'estimateStartDate') {
                        this.formatDateTime.forEach((fieldFormat) => {
                            field.cellRenderer = fieldFormat.cellRenderer;
                        });
                    } else if (field.field === 'estimateEndDate') {
                        this.formatDateTime.forEach((fieldFormat) => {
                            field.cellRenderer = fieldFormat.cellRenderer;
                        });
                    } else if (field.field === 'actualStartDate') {
                        this.formatDateTime.forEach((fieldFormat) => {
                            field.cellRenderer = fieldFormat.cellRenderer;
                        });
                    }
                });
                this.loadLateTaskData(id);
            });
    }

    loadLateTaskData(id) {
        this.homService.loadLateTaskData({
            page: this.page - 1,
            size: this.itemsPerPage,
            sort: this.sort()}, id)
            .subscribe((res: HttpResponse<any>) => {
                this.onSuccess(res.body, res.headers);
            });
    }

    loadUnAssignTaskColumn(id) {
        this.homService.loadUnAssignTaskColumn(id)
            .subscribe((res: HttpResponse<any>) => {
                this.columnDefsArg = res.body;
                this.columnDefsArg.forEach((field) => {
                    if (field.field === 'estimateStartDate') {
                        this.formatDateTime.forEach((fieldFormat) => {
                            field.cellRenderer = fieldFormat.cellRenderer;
                        });
                    } else if (field.field === 'estimateEndDate') {
                        this.formatDateTime.forEach((fieldFormat) => {
                            field.cellRenderer = fieldFormat.cellRenderer;
                        });
                    } else if (field.field === 'actualStartDate') {
                        this.formatDateTime.forEach((fieldFormat) => {
                            field.cellRenderer = fieldFormat.cellRenderer;
                        });
                    }
                });
                this.loadUnAssignTaskData(id);
            });
    }

    loadUnAssignTaskData(id) {
        this.homService.loadUnAssignTaskData({
            page: this.page - 1,
            size: this.itemsPerPage,
            sort: this.sort()}, id)
            .subscribe((res: HttpResponse<any>) => {
                this.onSuccess(res.body, res.headers);
            });
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    private onSuccess(data, headers) {
        // this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = headers.get('X-Total-Count');
        this.queryCount = this.totalItems;
        this.rowData = data;
        // this.page = pagingParams.page;
    }

    loadPage(page: number) {
        this.page = page;
        if (page !== this.previousPage) {
            this.previousPage = page;
            // this.transition();
            switch (this.type) {
                case 'packages-late':
                    this.loadLatePackageData(this.projectId);
                    break;
                case 'tasks-late':
                    this.loadLateTaskData(this.projectId);
                    break;
                case 'tasks-un-assign':
                    this.loadUnAssignTaskData(this.projectId);
                    break;
                default:
                    break;
            }
        }
    }
}
