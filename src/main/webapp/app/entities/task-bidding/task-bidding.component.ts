import {Component, OnInit, OnDestroy, ViewChild} from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { TaskBidding } from './task-bidding.model';
import { TaskBiddingService } from './task-bidding.service';
import { ITEMS_PER_PAGE, Principal } from '../../shared';
import {AppConstants} from '../../shared/services/app-constants';
import {Projects} from '../projects/projects.model';
import {Tasks} from '../tasks/tasks.model';
import {ButtonViewComponent} from '../../shared/dynamic-data-tables/buttons-view/button-view.component';
import {ButtonBiddingComponent} from '../../shared/dynamic-data-tables/button-bidding/button-bidding.component';
import {Permission} from '../../account/login/permission.model';
import {PermissionService} from '../../account/login/permission.service';
import {DataService} from '../../shared/services/data.service';
import {ProjectsService} from '../projects';
import {PurchaseOrdersService} from '../purchase-orders';
import {PackagesService} from '../packages';
import {NgbTabset} from '@ng-bootstrap/ng-bootstrap';

@Component({
    selector: 'jhi-task-bidding',
    templateUrl: './task-bidding.component.html'
})
export class TaskBiddingComponent implements OnInit, OnDestroy {
    @ViewChild('tabset') tabset: NgbTabset;
    currentAccount: any;
    taskBiddings: TaskBidding[];
    taskBidding: TaskBidding;
    error: any;
    success: any;
    eventSubscriber: Subscription;
    currentSearch: string;

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
    constructor(
        private taskBiddingService: TaskBiddingService,
        private parseLinks: JhiParseLinks,
        private jhiAlertService: JhiAlertService,
        private principal: Principal,
        private activatedRoute: ActivatedRoute,
        private router: Router,
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

    // onChangeProject() {
    //     console.log(this.projectChosen);
    //     this.getListPurchaseOrder();
    //     this.poChosen = '';
    //     this.packageChosen = '';
    //     this.packages = new Array();
    // }

    // onChangePO() {
    //     this.getListPackage();
    //     this.packageChosen = '';
    // }

    // getListProject() {
    //     this.projectService.getListProjectBiddingTask(this.currentUserLogin).subscribe((res) => {
    //         this.projects = res.body;
    //         if (this.projects.length > 0) {
    //             this.projectChosen = this.projects[0].id;
    //             this.getListPurchaseOrder();
    //         } else {
    //             this.loadAllTaskUnAssign();
    //         }
    //     });
    // }

    // getListPurchaseOrder() {
    //     if (this.projectChosen && this.projectChosen !== '') {
    //         this.purchaseOrdersService.getListPurchaseOrderBiddingTask(this.projectChosen, this.currentUserLogin).subscribe((res) => {
    //             this.purchaseOrders = res.body;
    //             if (this.purchaseOrders.length > 0) {
    //                 this.poChosen = this.purchaseOrders[0].id;
    //                 this.getListPackage();
    //             } else {
    //                 this.loadAllTaskUnAssign();
    //             }
    //         });
    //     } else {
    //         this.purchaseOrders = new Array();
    //     }
    // }

    // getListPackage() {
    //     if (this.poChosen && this.poChosen !== '') {
    //         this.packagesService.getListPackageBiddingTask(this.poChosen).subscribe((res) => {
    //             this.packages = res.body;
    //             if (this.packages.length > 0) {
    //                 this.packageChosen = this.packages[0].id;
    //             }
    //             this.loadAllTaskUnAssign();
    //         });
    //     } else {
    //         this.packages = new Array();
    //     }
    // }

    // loadAllTaskUnAssign() {
    //     this.taskBiddingService.queryTaskUnAssign({
    //         projectId: this.projectChosen,
    //         purchaseOrderId: this.poChosen,
    //         packageId: this.packageChosen,
    //         page: this.page - 1,
    //         size: this.itemsPerPage,
    //         sort: this.sort()}).subscribe(
    //         (res: HttpResponse<Tasks[]>) => this.onSuccess(res.body, res.headers),
    //         (res: HttpErrorResponse) => this.onError(res.message)
    //     );
    // }

    loadPage(page: number) {
        if (page !== this.previousPage) {
            this.previousPage = page;
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
        this.loadAll();
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
    search(query) {
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
    }
    ngOnInit() {
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
        console.log(this.currentUserLogin);
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.rowData = [];
        this.columnDefsArg = AppConstants.TaskBidding;
        // this.getListProject();
        this.registerTaskBiddings();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    changeTab(event) {
        this.router.navigate(['/task-bidding'], {queryParams: {mode: event.nextId}});
    }

    trackId(index: number, item: TaskBidding) {
        return item.id;
    }
    // registerChangeInTaskBiddings() {
    //     this.eventSubscriber = this.eventManager.subscribe(
    //         'taskBiddingListModification',
    //         (response) => this.loadAllTaskUnAssign());
    // }

    registerTaskBiddings() {
        this.eventSubscriber = this.eventManager.subscribe(
            'taskBiddingModification',
            (response) => this.createTaskBiddings());
    }

    createTaskBiddings() {
        const idList = new Array();
        if (this.selectedRows.length > 0 ) {
            this.selectedRows.forEach((row) => {
                idList.push(row.id);
            });
            const payload: any = Object.assign({}, {'idList': idList});
            this.taskBiddingService.biddingTask(payload, this.taskBiddingClassify).subscribe( (res) => {
                console.log(res);
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

    private onSuccess(data, headers) {
        console.log(data);
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = headers.get('X-Total-Count');
        this.queryCount = this.totalItems;
        // this.page = pagingParams.page;
        this.taskBiddings = data;
        this.rowData = data;
    }
    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
