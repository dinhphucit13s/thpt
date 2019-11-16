import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { HttpResponse } from '@angular/common/http';
import { DatePipe } from '@angular/common';
import { BusinessUnitManager } from './business-unit-manager.model';
import { BusinessUnitManagerService } from './business-unit-manager.service';

@Injectable()
export class BusinessUnitManagerPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private datePipe: DatePipe,
        private modalService: NgbModal,
        private router: Router,
        private businessUnitManagerService: BusinessUnitManagerService

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
                this.businessUnitManagerService.find(id)
                    .subscribe((businessUnitManagerResponse: HttpResponse<BusinessUnitManager>) => {
                        const businessUnitManager: BusinessUnitManager = businessUnitManagerResponse.body;
                        // businessUnitManager.startTime = this.datePipe
                        //     .transform(businessUnitManager.startTime, 'yyyy-MM-ddTHH:mm:ss');
                        // businessUnitManager.endTime = this.datePipe
                        //     .transform(businessUnitManager.endTime, 'yyyy-MM-ddTHH:mm:ss');
                        this.ngbModalRef = this.businessUnitManagerModalRef(component, businessUnitManager);
                        resolve(this.ngbModalRef);
                    });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.businessUnitManagerModalRef(component, new BusinessUnitManager());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    businessUnitManagerModalRef(component: Component, businessUnitManager: BusinessUnitManager): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.businessUnitManager = businessUnitManager;
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
