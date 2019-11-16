import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { HttpResponse } from '@angular/common/http';
import { DtmsMonitoring } from './dtms-monitoring.model';
import { DtmsMonitoringService } from './dtms-monitoring.service';

@Injectable()
export class DtmsMonitoringPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private dtmsMonitoringService: DtmsMonitoringService

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
                this.dtmsMonitoringService.find(id)
                    .subscribe((dtmsMonitoringResponse: HttpResponse<DtmsMonitoring>) => {
                        const dtmsMonitoring: DtmsMonitoring = dtmsMonitoringResponse.body;
                        this.ngbModalRef = this.dtmsMonitoringModalRef(component, dtmsMonitoring);
                        resolve(this.ngbModalRef);
                    });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.dtmsMonitoringModalRef(component, new DtmsMonitoring());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    dtmsMonitoringModalRef(component: Component, dtmsMonitoring: DtmsMonitoring): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.dtmsMonitoring = dtmsMonitoring;
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
