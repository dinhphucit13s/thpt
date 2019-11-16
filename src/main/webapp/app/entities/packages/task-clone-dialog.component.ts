import { Component, OnInit, OnDestroy } from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import {JhiAlertService, JhiEventManager} from 'ng-jhipster';

import { Packages } from './packages.model';
import { PackagesService } from './packages.service';
import {HttpErrorResponse, HttpResponse} from '@angular/common/http';

// get purchase Order
import {PurchaseOrders, PurchaseOrdersService} from '../purchase-orders';
import {Observable} from 'rxjs/Observable';
import {Tasks} from '../tasks';
import { TasksService } from '../tasks';
import {Subscription} from 'rxjs/Subscription';

@Component({
    selector: 'jhi-task-clone-dialog',
    templateUrl: './task-clone-dialog.component.html',
    styleUrls: ['./tasks-clone-dialog.component.css']
})
export class TaskCloneComponent implements OnInit, OnDestroy {

    purchaseOrders: PurchaseOrders[];
    tasks: Tasks[];
    purchaseOrder: PurchaseOrders;
    packageLst: Packages[];
    poChosen: any = '';
    packageChosen: any = '';
    poId: any = '';
    id: any;
    eventSubscriber: Subscription;
    isSaving: boolean;
    packages: Packages;
    errorMessage: any;
    routeSub: any;
    result: any;
    taskCheckClassify: any;
    constructor(
        private packagesService: PackagesService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager,
        private jhiAlertService: JhiAlertService,
        public purchaseOrdersService: PurchaseOrdersService,
        private route: ActivatedRoute,
        private router: Router,
        public tasksService: TasksService
    ) {
    }

    ngOnInit() {
        /*Set packages clone*/
        this.routeSub = this.route.params.subscribe((params) => {
            if (params['id']) {
                sessionStorage.setItem('packageFrom', params['id']);
            }
        });
        this.getLoadPO();

        /*register event clone Tasks*/
        this.eventCloneAllTask();
    }

    ngOnDestroy() {
        this.eventSubscriber.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    clear() {
        /*return Screen Tasks*/
        this.router.navigate(['/' + 'purchase-orders/' + sessionStorage.getItem('currentPurchaseOrderId')]);
    }
    // get List PO in popup Clone
    getLoadPO() {
        this.id = sessionStorage.getItem('currentProjectId');
        this.poId = sessionStorage.getItem('currentPurchaseOrderId');
        this.purchaseOrdersService.clone(null, this.id)
            .subscribe((purchaseOrdersResponse: HttpResponse<PurchaseOrders[]>) => {
                this.purchaseOrders = purchaseOrdersResponse.body;
                for (const po of this.purchaseOrders) {
                    if (po.id.toString() === this.poId) {
                        this.purchaseOrders.splice(this.purchaseOrders.indexOf(po), 1);
                    }
                }
                if (this.poChosen !== null ) {
                    this.getLoadPackage(this.poChosen);
                }
            });
    }
    // get List PA in popup Clone
    getLoadPackage(poOption: any) {
        if (this.poChosen && this.poChosen !== '') {
            this.packagesService.getPackages(null, poOption)
                .subscribe((packageResponse: HttpResponse<Packages[]>) => {
                    this.packageLst = packageResponse.body;
                });
        } else {
            this.packageLst = new Array();
        }
    }

    onChangePO() {
        this.getLoadPO();
        this.packageChosen = '';
    }

    checkTaskByClassify(classify: any) {
        this.taskCheckClassify = classify;
    }

    eventCloneAllTask() {
        this.eventSubscriber = this.eventManager.subscribe(
            'CloneTaskOnPackages',
            (response) => {
                this.cloneTasks();
                this.poChosen = '';
                this.packageChosen = '';
            });
    }

    /*Excute clone Tasks have status*/
    cloneTasks() {
        if (this.taskCheckClassify === 'ALL') {
            this.packagesService.cloneAllTask(this.packageChosen, this.poChosen, sessionStorage.getItem('packageFrom')).subscribe( (res) => {
            });
        } else {
            this.packagesService.cloneOpenTask(this.packageChosen, this.poChosen, sessionStorage.getItem('packageFrom')).subscribe( (res) => {
            });
        }
    }
}
