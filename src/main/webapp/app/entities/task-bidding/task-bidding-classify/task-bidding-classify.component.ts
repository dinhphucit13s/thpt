import {Component, Input, OnDestroy, OnInit} from '@angular/core';
import {Subscription} from 'rxjs/Subscription';
import {BiddingStatus, TaskBidding} from '../task-bidding.model';
import {JhiAlertService, JhiEventManager, JhiParseLinks} from 'ng-jhipster';
import {ITEMS_PER_PAGE, Principal} from '../../../shared';
import {ActivatedRoute, Router} from '@angular/router';
import {TaskBiddingService} from '../task-bidding.service';
import {AppConstants} from '../../../shared/services/app-constants';
import {ProjectsService} from '../../projects';
import {PurchaseOrdersService} from '../../purchase-orders';
import {PackagesService} from '../../packages';
import {FIStatus, OPStatus, ReviewStatus, TasksService, TaskStatus} from '../../tasks';
import {ButtonViewComponent} from '../../../shared/dynamic-data-tables/buttons-view/button-view.component';
import {FixStatus} from '../../tasks/tasks.model';

@Component({
    selector: 'jhi-task-bidding-classify',
    templateUrl: './task-bidding-classify.component.html',
    styleUrls: ['./task-bidding-classify.component.css']
})

export class TaskBiddingClassifyComponent implements OnInit, OnDestroy {

    @Input() modeBidding: any;

    currentUserLogin: any;
    taskBiddings: TaskBidding[] = [];
    // error: any;
    // success: any;
    // eventSubscriber: Subscription;

    // paging
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
    rowData: any = [];

    projectList: any = [];
    projectChosen: any = '';
    purchaseOrdersList: any = [];
    poChosen: any = '';
    packageList: any = [];
    packageChosen: any = '';
    frameworkComponents: any;
    isRowSelectable;

    selectedRows: any = [];

    projectSearch: any;
    poSearch: any;
    packageSearch: any;
    initial: boolean;
    mode: string;

    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(private taskBiddingService: TaskBiddingService,
                private tasksService: TasksService,
                private parseLinks: JhiParseLinks,
                private alertService: JhiAlertService,
                private principal: Principal,
                private activatedRoute: ActivatedRoute,
                private router: Router,
                private eventManager: JhiEventManager,
                private projectService: ProjectsService,
                private purchaseOrdersService: PurchaseOrdersService,
                private packagesService: PackagesService) {
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
        this.isRowSelectable = function(rowNode) {
            return rowNode.data ? rowNode.data.biddingStatus === BiddingStatus.NA : false;
        };
        this.mode = this.activatedRoute.snapshot.queryParams['mode'];
    }

    ngOnInit() {
        this.initial = true;
        const currentUser = JSON.parse(localStorage.getItem('currentUser'));
        if (!currentUser) {
            this.alertService.error('Invalid action');
            this.router.navigate(['/']);
            return;
        }
        this.currentUserLogin = currentUser.login.toLocaleLowerCase();
        this.columnDefsArg = AppConstants.TaskBiddingClassify;
        this.getListProject();
        this.registerUnBiddingTask();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerUnBiddingTask() {
        this.eventSubscriber = this.eventManager.subscribe(
            'taskUnBiddingModification',
            (response) => this.unBiddingTask()
        );
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    onChangeProject() {
        this.poChosen = '';
        this.packageChosen = '';
        this.packageList = new Array();
        this.getListPurchaseOrder();
    }

    onChangePO() {
        this.getListPackage();
        this.packageChosen = '';
    }

    getListProject() {
        this.projectService.getListProjectByUserLogin(this.currentUserLogin).subscribe((res) => {
            this.projectList = res.body;
            if (this.projectList.length > 0) {
                this.projectChosen = this.projectList[0].id;
                this.getListPurchaseOrder();
            } else {
                this.loadDataBidding();
            }
        });
    }

    getListPurchaseOrder() {
        if (this.projectChosen && this.projectChosen !== '') {
            this.purchaseOrdersService.getListPurchaseOrderBiddingTask(this.projectChosen, this.currentUserLogin).subscribe((res) => {
                this.purchaseOrdersList = res.body;
                if (this.purchaseOrdersList.length > 0) {
                    this.poChosen = this.purchaseOrdersList[0].id;
                    this.getListPackage();
                } else {
                    this.loadDataBidding();
                }
            });
        } else {
            this.purchaseOrdersList = new Array();
        }
    }

    getListPackage() {
        if (this.poChosen && this.poChosen !== '') {
            this.packagesService.getListPackageBiddingTask(this.poChosen).subscribe((res) => {
                this.packageList = res.body;
                if (this.packageList.length > 0) {
                    this.packageChosen = this.packageList[0].id;
                }
                if (this.initial) {
                    this.loadDataBidding();
                }
            });
        } else {
            this.packageList = new Array();
        }
    }

    loadDataBidding() {
        this.projectSearch = this.projectChosen;
        this.poSearch = this.poChosen;
        this.packageSearch = this.packageChosen;
        this.initial = false;
        this.getListTaskBiddingByMode();
    }

    getListTaskBiddingByMode() {
        this.taskBiddingService.getListTaskBiddingByMode(
            {  projectId: this.projectSearch,
                    poId: this.poSearch,
                    packageId: this.packageSearch,
                    modeBidding: this.modeBidding,
                    userLogin: this.currentUserLogin,
                    page: this.page - 1,
                    size: this.itemsPerPage,
                    sort: this.sort()}).subscribe( (res) => {
                        // this.links = this.parseLinks.parse(res.headers.get('link'));
                        this.totalItems = res.headers.get('X-Total-Count');
                        this.queryCount = this.totalItems;
                        this.taskBiddings = res.body;
                        this.taskBiddings.map((taskBidding) => {
                            switch (taskBidding.biddingRound) {
                                case 'op':
                                    taskBidding.pic = taskBidding.task.op;
                                    taskBidding.startDate = taskBidding.task.opStartTime;
                                    taskBidding.endDate = taskBidding.task.opEndTime;
                                    break;
                                case 'review1':
                                    taskBidding.pic = taskBidding.task.review1;
                                    taskBidding.startDate = taskBidding.task.review1StartTime;
                                    taskBidding.endDate = taskBidding.task.review1EndTime;
                                    break;
                                case 'review2':
                                    taskBidding.pic = taskBidding.task.review2;
                                    taskBidding.startDate = taskBidding.task.review2StartTime;
                                    taskBidding.endDate = taskBidding.task.review2EndTime;
                                    break;
                                case 'fixer':
                                    taskBidding.pic = taskBidding.task.fixer;
                                    taskBidding.startDate = taskBidding.task.fixStartTime;
                                    taskBidding.endDate = taskBidding.task.fixEndTime;
                                    break;
                                case 'fi':
                                    taskBidding.pic = taskBidding.task.fi;
                                    taskBidding.startDate = taskBidding.task.fiStartTime;
                                    taskBidding.endDate = taskBidding.task.fiEndTime;
                                    break;
                            }
                            return taskBidding;
                        });
        });
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
                    mode: this.mode,
                    page: this.page,
                    size: this.itemsPerPage,
                    sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
                }
        });
        this.getListTaskBiddingByMode();
    }

    unBiddingTask() {
        const idList = new Array();
        this.selectedRows.forEach((row) => {
            idList.push(row.id);
        });
        const payload: any = Object.assign({}, {'idList': idList});
        this.taskBiddingService.unBiddingTask(payload).subscribe((res) => {
            if (res.statusText === 'OK') {
                this.selectedRows = new Array();
                this.getListTaskBiddingByMode();
            }
        });
    }

    getSelectedRow(row) {
        this.selectedRows = row;
    }

    private onSuccess(data, headers) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = headers.get('X-Total-Count');
        this.queryCount = this.totalItems;
        // this.page = pagingParams.page;
        // this.taskBiddings = data;
    }
    private onError(error) {
        this.alertService.error(error.message, null, null);
    }
}
