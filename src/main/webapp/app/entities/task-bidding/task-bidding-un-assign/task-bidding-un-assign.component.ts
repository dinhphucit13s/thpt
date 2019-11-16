import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { TaskBidding } from '../task-bidding.model';
import { TaskBiddingService } from '../task-bidding.service';
import { ITEMS_PER_PAGE, Principal } from '../../../shared';
import {AppConstants} from '../../../shared/services/app-constants';
import {Tasks} from '../../tasks/tasks.model';
import {ButtonViewComponent} from '../../../shared/dynamic-data-tables/buttons-view/button-view.component';
import {Permission} from '../../../account/login/permission.model';
import {PermissionService} from '../../../account/login/permission.service';
import {DataService} from '../../../shared/services/data.service';
import {ProjectsService} from '../../projects';
import {PurchaseOrdersService} from '../../purchase-orders';
import {PackagesService} from '../../packages';
import {TasksService} from '../../tasks';

@Component({
    selector: 'jhi-task-bidding-un-assign',
    templateUrl: './task-bidding-un-assign.component.html'
})
export class TaskBiddingUnAssignComponent implements OnInit, OnDestroy {

    currentAccount: any;
    taskBiddings: TaskBidding[];
    taskBidding: TaskBidding;
    error: any;
    success: any;
    eventSubscriber: Subscription;
    private subscription: Subscription;
    currentSearch: string;

    workFlows: any;
    multiWorkFlow: any;
    currentUserLogin: any;
    purchaseOrderId: any;
    packageId: any;
    routeData: any;
    links: any;
    totalItems: any;
    queryCount: any;
    itemsPerPage: any;
    page: any;
    predicate: any;
    previousPage: any;
    reverse: any;
    columnDefsArg: any;
    isRowSelectable: any;
    rowData: any;
    frameworkComponents: any;
    permission: number;
    allPermission: Permission;
    projects: any = [];
    purchaseOrders: any = [];
    packages: any = [];
    projectChosen: any = '';
    poChosen: any = '';
    packageChosen: any = '';
    biddingScope: any = '';
    selectedRows: any = [];
    taskBiddingClassify: any;
    initial: boolean;
    constructor(
        private taskBiddingService: TaskBiddingService,
        private tasksService: TasksService,
        private parseLinks: JhiParseLinks,
        private jhiAlertService: JhiAlertService,
        private principal: Principal,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private route: ActivatedRoute,
        private eventManager: JhiEventManager,
        private permissionService: PermissionService,
        private dataService: DataService,
        private projectService: ProjectsService,
        private purchaseOrdersService: PurchaseOrdersService,
        private packagesService: PackagesService
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

    loadAll() {}

    onChangeProject() {
        console.log(this.projectChosen);
        this.getListPurchaseOrder();
        this.poChosen = '';
        this.packageChosen = '';
        this.packages = new Array();
    }

    onChangePO() {
        this.getListPackage();
        this.packageChosen = '';
    }

    getListProject() {
        this.projectService.getListProjectByUserLogin(this.currentUserLogin).subscribe((res) => {
            this.projects = res.body;
            if (this.projects.length > 0) {
                this.projectChosen = this.projects[0].id;
                this.getListPurchaseOrder();
            } else {
                this.rowData = [];
                this.processMultiWorkFlowOfPurchaseOrders();
            }
        });
    }

    getListPurchaseOrder() {
        if (this.projectChosen && this.projectChosen !== '') {
            this.purchaseOrdersService.getListPurchaseOrderBiddingTask(this.projectChosen, this.currentUserLogin).subscribe((res) => {
                this.purchaseOrders = res.body;
                if (this.purchaseOrders.length > 0) {
                    this.poChosen = this.purchaseOrders[0].id;
                    this.getListPackage();
                } else {
                    this.processMultiWorkFlowOfPurchaseOrders();
                }
            });
        } else {
            this.purchaseOrders = new Array();
        }
    }

    getListPackage() {
        if (this.poChosen && this.poChosen !== '') {
            this.packagesService.getListPackageBiddingTask(this.poChosen).subscribe((res) => {
                this.packages = res.body;
                if (this.packages.length > 0) {
                    this.packageChosen = this.packages[0].id;
                }
                if (this.initial) {
                    this.processMultiWorkFlowOfPurchaseOrders();
                }
            });
        } else {
            this.packages = new Array();
        }
    }
    processMultiWorkFlowOfPurchaseOrders() {
        this.workFlows = [];
        this.taskBiddingService.getMultiWorkFlowOfPurchaseOrders(this.projectChosen, this.poChosen).subscribe((res) => {
            this.multiWorkFlow = res.body;
            this.multiWorkFlow.map((flow) => {
                const roundList: any = [];
                flow.fieldConfigVM.forEach((field) => {
                    if (field.field === 'op'
                        || field.field === 'review1'
                        || field.field === 'review2'
                        || field.field === 'fi'
                        || field.field === 'fixer') {
                        roundList.push(field.field);
                    }
                });
                flow.fieldConfigVM = roundList;
                this.workFlows = roundList;
                return flow;
            });
            console.log(this.multiWorkFlow);
            this.loadAllTaskUnAssign();
        });
    }
    loadAllTaskUnAssign() {
        this.tasksService.queryTaskUnAssign({
            workFlow: this.workFlows,
            projectId: this.projectChosen,
            purchaseOrderId: this.poChosen,
            packageId: this.packageChosen,
            page: this.page - 1,
            size: this.itemsPerPage,
            sort: this.sort()}).subscribe(
            (res: HttpResponse<Tasks[]>) => this.onSuccess(res.body, res.headers),
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        if (this.poChosen) {
            const poId = this.poChosen;
            sessionStorage.setItem('currentPurchaseOrderId', poId);
        }
    }

    loadPage(page: number) {
        if (page !== this.previousPage) {
            this.previousPage = page;
            this.page = page;
            this.transition();
        }
    }
    transition() {
        this.router.navigate(['/task-bidding'], {queryParams:
                {
                    page: this.page,
                    size: this.itemsPerPage,
                    search: this.currentSearch,
                    sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
                }
        });
        this.loadAllTaskUnAssign();
    }

    clear() {
        this.page = 0;
        this.currentSearch = '';
        this.router.navigate(['/task-bidding', {
            page: this.page,
            sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
        }]);
        this.loadAll();
    }
    /* search(query) {
         if (!query) {
             return this.clear();
         }
         this.page = 0;
         this.currentSearch = query;
         this.router.navigate(['/task-bidding', {
             search: this.currentSearch,
             page: this.page,
             sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
         }]);
         this.loadAll();
     }*/
    ngOnInit() {
        this.initial = true;
        const currentUser = JSON.parse(localStorage.getItem('currentUser'));
        this.permissionService.findPermission().subscribe((roleResponse: HttpResponse<Permission>) => {
            const role: Permission = roleResponse.body;
            this.dataService.changeUserPermission(role);
            this.dataService.currentUserPermission.subscribe((userPermission) => this.allPermission = userPermission);
            for (const resource of this.allPermission.resources) {
                if (resource.name === 'TASK') {
                    this.permission = resource.permission;
                    this.dataService.setPermission(resource.permission);
                    return;
                }
            }
        });
        this.currentUserLogin = currentUser.login.toLocaleLowerCase();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.columnDefsArg = AppConstants.TaskBidding;
        this.subscription = this.route.params.subscribe((params) => {
            this.getListProject();
        });

        this.registerTaskBiddings();
        this.registerChangeInTasks();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventSubscriber.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: TaskBidding) {
        return item.id;
    }
    registerChangeInTaskBiddings() {
        this.eventSubscriber = this.eventManager.subscribe(
            'taskBiddingListModification',
            (response) => this.loadAllTaskUnAssign());
    }

    registerTaskBiddings() {
        this.eventSubscriber = this.eventManager.subscribe(
            'taskBiddingModification',
            (response) => {
                this.createTaskBiddings();
            });
    }

    createTaskBiddings() {
        const idList = new Array();
        if (this.selectedRows.length > 0 ) {
            this.selectedRows.forEach((row) => {
                idList.push(row.id);
            });
            const payload: any = Object.assign({}, {'idList': idList});
            this.taskBiddingService.biddingTask(payload, this.taskBiddingClassify).subscribe( (res) => {
                if (res.statusText === 'OK') {
                    this.loadAllTaskUnAssign();
                }
            });
        }
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    getSelectedRow(row) {
        this.selectedRows = row;
    }

    biddingTaskByClassify(classify: any) {
        this.taskBiddingClassify = classify;
    }

    registerChangeInTasks() {
        this.eventSubscriber = this.eventManager.subscribe('tasksListModification', (response) => this.loadAllTaskUnAssign());
    }

    private onSuccess(data, headers) {
        console.log(data);
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = headers.get('X-Total-Count');
        this.queryCount = this.totalItems;
        // this.page = pagingParams.page;
        this.taskBiddings = data;
        this.rowData = data;
        this.initial = false;
        this.selectedRows = new Array();
    }
    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
