import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { HttpResponse } from '@angular/common/http';
import { TMSCustomFieldScreen } from './tms-custom-field-screen.model';
import { TMSCustomFieldScreenService } from './tms-custom-field-screen.service';
import {TMSCustomField} from '../tms-custom-field/tms-custom-field.model';
import {TMSCustomFieldService} from '../tms-custom-field/tms-custom-field.service';

@Injectable()
export class TMSCustomFieldScreenPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private tMSCustomFieldScreenService: TMSCustomFieldScreenService,
        private tMSCustomFieldService: TMSCustomFieldService,

    ) {
        this.ngbModalRef = null;
    }

    open(component: Component, id?: number | any, fieldId?: number | any): Promise<NgbModalRef> {
        return new Promise<NgbModalRef>((resolve, reject) => {
            const isOpen = this.ngbModalRef !== null;
            if (isOpen) {
                resolve(this.ngbModalRef);
            }
            if (id) {
                this.tMSCustomFieldScreenService.find(id)
                    .subscribe((tMSCustomFieldScreenResponse: HttpResponse<TMSCustomFieldScreen>) => {
                        const tMSCustomFieldScreen: TMSCustomFieldScreen = tMSCustomFieldScreenResponse.body;
                        tMSCustomFieldScreen.entityGridInput = JSON.parse(tMSCustomFieldScreen.entityGridInput);
                        tMSCustomFieldScreen.entityGridPm = JSON.parse(tMSCustomFieldScreen.entityGridPm);
                        tMSCustomFieldScreen.entityGridOp = JSON.parse(tMSCustomFieldScreen.entityGridOp);
                        this.ngbModalRef = this.tMSCustomFieldScreenModalRef(component, tMSCustomFieldScreen);
                        resolve(this.ngbModalRef);
                    });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.tMSCustomFieldScreenModalRef(component, new TMSCustomFieldScreen());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    tMSCustomFieldScreenModalRef(component: Component, tMSCustomFieldScreen: TMSCustomFieldScreen): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.tMSCustomFieldScreen = tMSCustomFieldScreen;
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
