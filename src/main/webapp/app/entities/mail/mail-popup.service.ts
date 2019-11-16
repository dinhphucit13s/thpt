import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { HttpResponse } from '@angular/common/http';
import { DatePipe } from '@angular/common';
import { Mail } from './mail.model';
import { MailService } from './mail.service';

@Injectable()
export class MailPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private datePipe: DatePipe,
        private modalService: NgbModal,
        private router: Router,
        private mailService: MailService

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
                this.mailService.find(id)
                    .subscribe((mailResponse: HttpResponse<Mail>) => {
                        const mail: Mail = mailResponse.body;
                        mail.startTime = this.datePipe
                            .transform(mail.startTime, 'yyyy-MM-ddTHH:mm:ss');
                        mail.endTime = this.datePipe
                            .transform(mail.endTime, 'yyyy-MM-ddTHH:mm:ss');
                        this.ngbModalRef = this.mailModalRef(component, mail);
                        resolve(this.ngbModalRef);
                    });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.mailModalRef(component, new Mail());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    mailModalRef(component: Component, mail: Mail): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.mail = mail;
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
