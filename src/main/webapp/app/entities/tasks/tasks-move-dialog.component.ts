import { Component, OnInit, OnDestroy } from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import {JhiEventManager, JhiAlertService, JhiDataUtils, JhiParseLinks} from 'ng-jhipster';
import {TasksPopupService} from './tasks-popup.service';
import {TasksService} from './tasks.service';
import {ITEMS_PER_PAGE, JhiTrackerService} from '../../shared';

import {PurchaseOrders} from '../purchase-orders';
import { PurchaseOrdersService} from '../purchase-orders';
import { Packages } from '../packages';
import { PackagesService } from '../packages';

import {Tasks} from './index';
import {AppConstants} from '../../shared/services/app-constants';
import {Subscription} from 'rxjs/Subscription';
import {ButtonViewComponent} from '../../shared/dynamic-data-tables/buttons-view/button-view.component';
import {DataService} from '../../shared/services/data.service';
import {Permission} from '../../account/login/permission.model';
import {PermissionService} from '../../account/login/permission.service';
import {forEach} from '@angular/router/src/utils/collection';

declare var jquery: any;
declare var $: any;

@Component({
    selector: 'jhi-task-move-dialog',
    templateUrl: './tasks-move-dialog.component.html',
    styleUrls: ['./tasks-move-dialog.component.css']
})
export class TasksMoveDialogComponent implements OnInit {
    purchaseOrders: PurchaseOrders[];
    tasks: Tasks[];
    purchaseOrder: PurchaseOrders;
    poChosen: any = '';
    packageChosen: any = '';
    currentPackageId: any = '';
    packages: Packages[];
    id: any;
    columnDefsArg: any;
    selectedRows: any = [];
    package_id: number;
    listContentProgress: any[] = new Array();
    frameworkComponents: any;
    isProgressDone = false;
    eventSubscriber: Subscription;
    rowData: any;
    searchName = '';
    searchStatus = '*';
    searchDescription = '';
    searchAssignee = '';
    fromDate = '';
    toDate = '';
    routeData: any;
    links: any;
    totalItems: any;
    queryCount: any;
    itemsPerPage: any;
    page: any;
    predicate: any;
    previousPage: any;
    reverse: any;
    packageId: number;
    packageName: any;
    purchaseOrderId: any;
    purchaseOrderName: any;
    currentUserLogin: any;
    allPermission: Permission;
    constructor(
        private tasksService: TasksService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager,
        private router: Router,
        private parseLinks: JhiParseLinks,
        private jhiAlertService: JhiAlertService,
        private trackerService: JhiTrackerService,
        private activatedRoute: ActivatedRoute,
        private dataService: DataService,
        public purchaseOrdersService: PurchaseOrdersService,
        private permissionService: PermissionService,
        public packagesService: PackagesService
    ) {
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.routeData = this.activatedRoute.data.subscribe((data) => {
            this.page = data.pagingParams.page;
            this.previousPage = data.pagingParams.page;
            this.reverse = data.pagingParams.ascending;
            this.predicate = data.pagingParams.predicate;
        });
        this.searchName = this.activatedRoute.snapshot && this.activatedRoute.snapshot.params['searchTasks'] ?
            this.activatedRoute.snapshot.params['searchTasks'] : '';
        this.frameworkComponents = {
            actionsButton: ButtonViewComponent,
        };
    }
     ngOnInit() {
         this.currentPackageId = sessionStorage.getItem('currentPackageId');
         this.getLoadPO();
         this.columnDefsArg = AppConstants.TaskMove;

         /*Call Funtion move tasks*/
         this.createTasksMove();

         /*Reload Tasks when Init*/
         this.search();
     }

    /*Load Purcharse Order to Combobox Purcharse Order*/
    getLoadPO() {
        this.id = sessionStorage.getItem('currentProjectId');
        this.purchaseOrdersService.clone(null, this.id)
            .subscribe((purchaseOrdersResponse: HttpResponse<PurchaseOrders[]>) => {
                this.purchaseOrders = purchaseOrdersResponse.body;
            });
       this.getLoadPackages();
    }

    /*Load Package to Combobox packages*/
    getLoadPackages() {
        console.log(this.poChosen);
        if (this.poChosen && this.poChosen !== '') {
            this.packagesService.getPackages(null, this.poChosen).subscribe((res) => {
                this.packages = res.body;
                console.log(this.packages);
                console.log(this.currentPackageId);
               for (const po of this.packages) {
                   console.log(po.id);
                   if (po.id.toString() === this.currentPackageId) {
                        this.packages.splice(this.packages.indexOf(po), 1);
                   }
               }
                if (this.packages !== null && this.packages[0] !== undefined) {
                    this.packageChosen = this.packages[0].id;
                }
            });
        } else {
            this.packages = new Array();
        }
    }

    /*Load paging*/
    loadPage(page: number) {
        this.page = page;
        if (page !== this.previousPage) {
            this.previousPage = page;
            this.transition();
        }
    }

    /*Call When excute paging*/
    transition() {
        this.router.navigate(['/' + 'tasks-move/' + this.currentPackageId], {queryParams:
                {
                    page: this.page,
                    size: this.itemsPerPage,
                    search: `${this.searchName}|${this.searchStatus}|${this.searchAssignee}|${this.searchDescription}`,
                    sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
                }
        });
        this.loadAll();
    }

    onChangePO() {
        this.getLoadPackages();
        this.packageChosen = '';
    }

    // Function click search tasks
    search() {
        if (!this.searchName && !this.searchStatus && !this.searchDescription && !this.searchAssignee && !this.fromDate && !this.toDate) {
            return this.clear();
        }
        this.page = 0;
        this.router.navigate(['/' + 'tasks-move/' + this.currentPackageId, {
            search: `${this.searchName}|${this.searchStatus}|${this.searchAssignee}|${this.searchDescription}|${this.fromDate}|${this.toDate}`,
            page: this.page,
            sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
        }]);
        this.loadAll();
    }

    loadAll() {
        if (this.searchName !== '' || this.searchStatus !== '*' || this.searchDescription !== '' || this.searchAssignee !== '' || this.fromDate !== '' || this.toDate !== '') {
            this.tasksService.search({
                page: this.page - 1,
                taskName: this.searchName,
                taskStatus: this.searchStatus,
                assignee: this.searchAssignee,
                description: this.searchDescription,
                packageId: this.currentPackageId,
                from: this.fromDate,
                to: this.toDate,
                size: this.itemsPerPage,
                sort: this.sort()}).subscribe(
                (res: HttpResponse<Tasks[]>) => {
                    const dataTas = new Array();
                    res.body.forEach((row) => {
                        if (row.status === 'OPEN' || row.status === 'NA') {
                            dataTas.push(row);
                        }
                    })
                    this.onSuccess(dataTas , res.headers);
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
            return;
        }
        this.tasksService.query({
            page: this.page - 1,
            size: this.itemsPerPage,
            sort: this.sort()}, this.currentPackageId).subscribe(
            (res: HttpResponse<Tasks[]>) => {
                const dataTas = new Array();
                res.body.forEach((row) => {
                    if (row.status === 'OPEN' || row.status === 'NA') {
                        dataTas.push(row);
                    }
                })
                this.onSuccess(dataTas, res.headers);
                // this.rowData = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    // Function when click move selected Row
    moveTasks() {
        const idList = new Array();
        if (this.selectedRows.length > 0 ) {
            this.selectedRows.forEach((row) => {
                idList.push(row.id);
            });
            const payload: any = Object.assign({}, {'idList': idList});
            this.tasksService.moveTasks(payload, this.packageChosen).subscribe( (res) => {
                if (res.statusText === 'OK') {
                    this.search();
                }
            });
        }
    }

    // Define Event when click button Move
    createTasksMove() {
        this.eventSubscriber = this.eventManager.subscribe(
            'moveTasksModification',
            (response) => {
                this.moveTasks();
            });
    }

    getSelectedRow(row) {
        this.selectedRows = row;
    }

    /*return result when sort*/
    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    private onSuccess(data, headers) {
        console.log(data);
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = headers.get('X-Total-Count');
        this.queryCount = this.totalItems;
        // this.page = pagingParams.page;
        this.tasks = data;
        this.rowData = data;
        this.selectedRows = new Array();
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }

    clear() {
        this.page = 0;
        this.searchName = '';
        this.searchStatus = '*';
        this.searchAssignee = '';
        this.searchDescription = '';
        this.fromDate = '';
        this.toDate = '';
        this.router.navigate(['/' + 'tasks-move/' + this.packageId, {
            page: this.page,
            sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
        }]);
        this.loadAll();
    }

    /*return Screen Tasks*/
    back() {
        this.router.navigate(['/' + 'packages/' + this.currentPackageId]);
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<any>>) {
        result.subscribe((res: HttpResponse<any>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError(res));
    }
    private onSaveSuccess(result: any) {
        this.eventManager.broadcast({ name: 'tasksListModification', content: 'OK'});
        this.isProgressDone = true;
        // this.activeModal.dismiss(result);
    }
    private onSaveError(res: any) {
        this.eventManager.broadcast({
            name: 'dtmsApp.httpError',
            content: res
        });
    }

}
