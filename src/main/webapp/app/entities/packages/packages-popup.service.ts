import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { HttpResponse } from '@angular/common/http';
import { DatePipe } from '@angular/common';
import { Packages } from './packages.model';
import { PackagesService } from './packages.service';

// Get OP PhuVD3
import { PurchaseOrdersService } from '../purchase-orders';
import { PurchaseOrders } from '../purchase-orders';

@Injectable()
export class PackagesPopupService {
    private ngbModalRef: NgbModalRef;
    packages: Packages;
    purchaseOrders: any = [];
    constructor(
        private datePipe: DatePipe,
        private modalService: NgbModal,
        private router: Router,
        private packagesService: PackagesService,
        public purchaseOrdersService: PurchaseOrdersService
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
                this.packagesService.find(id)
                    .subscribe((packagesResponse: HttpResponse<Packages>) => {
                        const packages: Packages = packagesResponse.body;
                        packages.estimateDelivery = this.datePipe
                            .transform(packages.estimateDelivery, 'yyyy-MM-ddTHH:mm:ss');
                        packages.startTime = this.datePipe
                            .transform(packages.startTime, 'yyyy-MM-ddTHH:mm:ss');
                        packages.endTime = this.datePipe
                            .transform(packages.endTime, 'yyyy-MM-ddTHH:mm:ss');
                        this.ngbModalRef = this.packagesModalRef(component, packages);
                        resolve(this.ngbModalRef);
                    });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.packagesModalRef(component, new Packages());
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
                setTimeout(() => {
                    const packages: Packages = new Packages();
                    packages.purchaseOrdersId = id;
                    this.ngbModalRef = this.packagesModalRef(component, packages);
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    packagesModalRef(component: Component, packages: Packages): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.packages = packages;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true, queryParamsHandling: 'merge' });
            this.ngbModalRef = null;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true, queryParamsHandling: 'merge' });
            this.ngbModalRef = null;
        });
        return modalRef;
    }

    // PhuVD3
    openClone(component: Component, id?: number | any): Promise<NgbModalRef> {
        return new Promise<NgbModalRef>((resolve, reject) => {
            const isOpen = this.ngbModalRef !== null;
            if (isOpen) {
                resolve(this.ngbModalRef);
            }

            if (id) {
                this.packagesService.find(id)
                    .subscribe((packagesResponse: HttpResponse<Packages>) => {
                        const packages: Packages = packagesResponse.body;
                        packages.estimateDelivery = this.datePipe
                            .transform(packages.estimateDelivery, 'yyyy-MM-ddTHH:mm:ss');
                        packages.startTime = this.datePipe
                            .transform(packages.startTime, 'yyyy-MM-ddTHH:mm:ss');
                        packages.endTime = this.datePipe
                            .transform(packages.endTime, 'yyyy-MM-ddTHH:mm:ss');
                        this.ngbModalRef = this.packagesModalRef(component, packages);
                        resolve(this.ngbModalRef);
                    });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.packagesModalRef(component, new Packages());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }
}
