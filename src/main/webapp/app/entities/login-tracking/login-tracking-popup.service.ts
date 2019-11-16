import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { HttpResponse } from '@angular/common/http';
import { DatePipe } from '@angular/common';
import { LoginTracking } from './login-tracking.model';
import { LoginTrackingService } from './login-tracking.service';

@Injectable()
export class LoginTrackingPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private datePipe: DatePipe,
        private modalService: NgbModal,
        private router: Router,
        private loginTrackingService: LoginTrackingService

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
                this.loginTrackingService.find(id)
                    .subscribe((loginTrackingResponse: HttpResponse<LoginTracking>) => {
                        const loginTracking: LoginTracking = loginTrackingResponse.body;
                        loginTracking.startTime = this.datePipe
                            .transform(loginTracking.startTime, 'yyyy-MM-ddTHH:mm:ss');
                        loginTracking.endTime = this.datePipe
                            .transform(loginTracking.endTime, 'yyyy-MM-ddTHH:mm:ss');
                        this.ngbModalRef = this.loginTrackingModalRef(component, loginTracking);
                        resolve(this.ngbModalRef);
                    });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.loginTrackingModalRef(component, new LoginTracking());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    loginTrackingModalRef(component: Component, loginTracking: LoginTracking): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.loginTracking = loginTracking;
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
