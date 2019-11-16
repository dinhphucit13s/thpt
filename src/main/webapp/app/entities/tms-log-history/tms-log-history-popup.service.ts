import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { HttpResponse } from '@angular/common/http';
import { TMSLogHistory } from './tms-log-history.model';
import { TMSLogHistoryService } from './tms-log-history.service';

@Injectable()
export class TMSLogHistoryPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private tMSLogHistoryService: TMSLogHistoryService

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
                this.tMSLogHistoryService.find(id)
                    .subscribe((tMSLogHistoryResponse: HttpResponse<TMSLogHistory>) => {
                        const tMSLogHistory: TMSLogHistory = tMSLogHistoryResponse.body;
                        this.ngbModalRef = this.tMSLogHistoryModalRef(component, tMSLogHistory);
                        resolve(this.ngbModalRef);
                    });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.tMSLogHistoryModalRef(component, new TMSLogHistory());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    tMSLogHistoryModalRef(component: Component, tMSLogHistory: TMSLogHistory): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.tMSLogHistory = tMSLogHistory;
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
