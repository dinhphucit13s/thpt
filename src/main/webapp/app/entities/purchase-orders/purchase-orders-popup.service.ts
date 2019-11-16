import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { HttpResponse } from '@angular/common/http';
import { DatePipe } from '@angular/common';
import { PurchaseOrders } from './purchase-orders.model';
import { PurchaseOrdersService } from './purchase-orders.service';

@Injectable()
export class PurchaseOrdersPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private datePipe: DatePipe,
        private modalService: NgbModal,
        private router: Router,
        private purchaseOrdersService: PurchaseOrdersService

    ) {
        this.ngbModalRef = null;
    }

    open(component: Component, id?: number | any): Promise<NgbModalRef> {
        return new Promise<NgbModalRef>((resolve, reject) => {
            const isOpen = this.ngbModalRef !== null;
            if (isOpen) {
                resolve(this.ngbModalRef);
            }

            if (id) {
                this.purchaseOrdersService.find(id)
                    .subscribe((purchaseOrdersResponse: HttpResponse<PurchaseOrders>) => {
                        const purchaseOrders: PurchaseOrders = purchaseOrdersResponse.body;
                        purchaseOrders.startTime = this.datePipe
                            .transform(purchaseOrders.startTime, 'yyyy-MM-ddTHH:mm:ss');
                        purchaseOrders.endTime = this.datePipe
                            .transform(purchaseOrders.endTime, 'yyyy-MM-ddTHH:mm:ss');
                        this.ngbModalRef = this.purchaseOrdersModalRef(component, purchaseOrders);
                        resolve(this.ngbModalRef);
                    });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.purchaseOrdersModalRef(component, new PurchaseOrders());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    openPO(component: Component, id?: number | any): Promise<NgbModalRef> {
        return new Promise<NgbModalRef>((resolve, reject) => {
            const isOpen = this.ngbModalRef !== null;
            if (isOpen) {
                resolve(this.ngbModalRef);
            }

            if (id) {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                const purchaseOrder: PurchaseOrders = new PurchaseOrders();
                purchaseOrder.projectId = id;
                setTimeout(() => {
                    this.ngbModalRef = this.purchaseOrdersModalRef(component, purchaseOrder);
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    purchaseOrdersModalRef(component: Component, purchaseOrders: PurchaseOrders): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.purchaseOrders = purchaseOrders;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true, queryParamsHandling: 'merge' });
            this.ngbModalRef = null;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true, queryParamsHandling: 'merge' });
            this.ngbModalRef = null;
        });
        return modalRef;
    }
}
