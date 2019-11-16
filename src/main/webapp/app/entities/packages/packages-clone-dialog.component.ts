import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import {JhiAlertService, JhiEventManager} from 'ng-jhipster';

import { Packages } from './packages.model';
import { PackagesPopupService } from './packages-popup.service';
import { PackagesService } from './packages.service';
import {HttpErrorResponse, HttpResponse} from '@angular/common/http';

// get purchase Order
import {PurchaseOrders, PurchaseOrdersService} from '../purchase-orders';
import {DtmsMonitoring} from '../dtms-monitoring/dtms-monitoring.model';
import {Observable} from 'rxjs/Observable';
import {Tasks} from '../tasks';
import { TasksService } from '../tasks';
import {LocalStorage} from 'ngx-webstorage';

@Component({
    selector: 'jhi-packages-clone-dialog',
    templateUrl: './packages-clone-dialog.component.html'
})
export class PackagesCloneDialogComponent implements OnInit {

    purchaseOrders: PurchaseOrders[];
    tasks: Tasks[];
    purchaseOrder: PurchaseOrders;
    poChosen: any = '';
    poId: any = '';
    id: any;
    isSaving: boolean;
    packages: Packages;
    errorMessage: any;
    routeSub: any;
    result: any;
    constructor(
        private packagesService: PackagesService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager,
        private jhiAlertService: JhiAlertService,
        public purchaseOrdersService: PurchaseOrdersService,
        private route: ActivatedRoute,
        private packagesPopupService: PackagesPopupService,
        public tasksService: TasksService
    ) {
    }

    ngOnInit() {
        this.getLoadPO();
    }

    clear() {
        this.activeModal.dismiss('cancel');
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
                console.log(this.purchaseOrders);
            });
    }

    // Click Clone Package and Move Task
    cloneTask() {
        // Set value for Clone Packages and Task
        this.packages.purchaseOrdersId = this.poChosen;
        this.packages.purchaseOrdersName = '';
        // Create Packages and Task
        this.subscribeToSavePackagesResponse(
            this.packagesService.createCloneTasksAndPackage(this.packages));
    }

    private subscribeToSavePackagesResponse(result: Observable<HttpResponse<Packages>>) {
        result.subscribe((res: HttpResponse<Packages>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: Packages) {
        this.eventManager.broadcast({ name: 'CloneTaskAndPackage', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }
}

@Component({
    selector: 'jhi-packages-clone-popup',
    template: ''
})
export class PackagesClonePopupComponent implements OnInit, OnDestroy {

    routeSub: any;
    constructor(
        private route: ActivatedRoute,
        private packagesPopupService: PackagesPopupService,
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.packagesPopupService
                .openClone(PackagesCloneDialogComponent as Component, params['id']);
        });

    }
    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
