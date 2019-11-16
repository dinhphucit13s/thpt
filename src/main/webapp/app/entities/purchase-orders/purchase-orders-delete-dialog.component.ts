import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { PurchaseOrders } from './purchase-orders.model';
import { PurchaseOrdersPopupService } from './purchase-orders-popup.service';
import { PurchaseOrdersService } from './purchase-orders.service';
import {HttpErrorResponse} from '@angular/common/http';

@Component({
    selector: 'jhi-purchase-orders-delete-dialog',
    templateUrl: './purchase-orders-delete-dialog.component.html'
})
export class PurchaseOrdersDeleteDialogComponent {

    purchaseOrders: PurchaseOrders;
    errorMessage: any;
    constructor(
        private purchaseOrdersService: PurchaseOrdersService,
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.purchaseOrdersService.delete(id).subscribe((response) => {
                this.eventManager.broadcast({
                    name: 'purchaseOrdersListModification',
                    content: 'Deleted an purchaseOrders'
                });
                this.activeModal.dismiss(true);
        }, (res: HttpErrorResponse) => {
            this.eventManager.broadcast({
                name: 'dtmsApp.httpError',
                content: res
            });
        });
    }
}

@Component({
    selector: 'jhi-purchase-orders-delete-popup',
    template: ''
})
export class PurchaseOrdersDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private purchaseOrdersPopupService: PurchaseOrdersPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.purchaseOrdersPopupService
                .open(PurchaseOrdersDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
