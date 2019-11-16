import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { HttpResponse } from '@angular/common/http';
import { NotificationTemplate } from './notification-template.model';
import { NotificationTemplateService } from './notification-template.service';

@Injectable()
export class NotificationTemplatePopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private notificationTemplateService: NotificationTemplateService

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
                this.notificationTemplateService.find(id)
                    .subscribe((notificationTemplateResponse: HttpResponse<NotificationTemplate>) => {
                        const notificationTemplate: NotificationTemplate = notificationTemplateResponse.body;
                        this.ngbModalRef = this.notificationTemplateModalRef(component, notificationTemplate);
                        resolve(this.ngbModalRef);
                    });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.notificationTemplateModalRef(component, new NotificationTemplate());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    notificationTemplateModalRef(component: Component, notificationTemplate: NotificationTemplate): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.notificationTemplate = notificationTemplate;
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
