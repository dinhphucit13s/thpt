import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { MailReceiver } from './mail-receiver.model';
import { MailReceiverPopupService } from './mail-receiver-popup.service';
import { MailReceiverService } from './mail-receiver.service';
import { Mail, MailService } from '../mail';

@Component({
    selector: 'jhi-mail-receiver-dialog',
    templateUrl: './mail-receiver-dialog.component.html'
})
export class MailReceiverDialogComponent implements OnInit {

    mailReceiver: MailReceiver;
    isSaving: boolean;

    mail: Mail[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private mailReceiverService: MailReceiverService,
        private mailService: MailService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.mailService.query()
            .subscribe((res: HttpResponse<Mail[]>) => { this.mail = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.mailReceiver.id !== undefined) {
            this.subscribeToSaveResponse(
                this.mailReceiverService.update(this.mailReceiver));
        } else {
            this.subscribeToSaveResponse(
                this.mailReceiverService.create(this.mailReceiver));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<MailReceiver>>) {
        result.subscribe((res: HttpResponse<MailReceiver>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: MailReceiver) {
        this.eventManager.broadcast({ name: 'mailReceiverListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackMailById(index: number, item: Mail) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-mail-receiver-popup',
    template: ''
})
export class MailReceiverPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private mailReceiverPopupService: MailReceiverPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.mailReceiverPopupService
                    .open(MailReceiverDialogComponent as Component, params['id']);
            } else {
                this.mailReceiverPopupService
                    .open(MailReceiverDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
