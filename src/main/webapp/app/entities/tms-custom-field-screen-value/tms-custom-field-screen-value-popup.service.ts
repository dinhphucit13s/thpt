import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { HttpResponse } from '@angular/common/http';
import { TMSCustomFieldScreenValue } from './tms-custom-field-screen-value.model';
import { TMSCustomFieldScreenValueService } from './tms-custom-field-screen-value.service';

@Injectable()
export class TMSCustomFieldScreenValuePopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private tMSCustomFieldScreenValueService: TMSCustomFieldScreenValueService

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
                this.tMSCustomFieldScreenValueService.find(id)
                    .subscribe((tMSCustomFieldScreenValueResponse: HttpResponse<TMSCustomFieldScreenValue>) => {
                        const tMSCustomFieldScreenValue: TMSCustomFieldScreenValue = tMSCustomFieldScreenValueResponse.body;
                        this.ngbModalRef = this.tMSCustomFieldScreenValueModalRef(component, tMSCustomFieldScreenValue);
                        resolve(this.ngbModalRef);
                    });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.tMSCustomFieldScreenValueModalRef(component, new TMSCustomFieldScreenValue());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    tMSCustomFieldScreenValueModalRef(component: Component, tMSCustomFieldScreenValue: TMSCustomFieldScreenValue): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.tMSCustomFieldScreenValue = tMSCustomFieldScreenValue;
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
