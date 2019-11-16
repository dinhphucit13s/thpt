import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { MailReceiver } from './mail-receiver.model';
import { MailReceiverPopupService } from './mail-receiver-popup.service';
import { MailReceiverService } from './mail-receiver.service';

@Component({
    selector: 'jhi-mail-receiver-delete-dialog',
    templateUrl: './mail-receiver-delete-dialog.component.html'
})
export class MailReceiverDeleteDialogComponent {

    mailReceiver: MailReceiver;

    constructor(
        private mailReceiverService: MailReceiverService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.mailReceiverService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'mailReceiverListModification',
                content: 'Deleted an mailReceiver'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-mail-receiver-delete-popup',
    template: ''
})
export class MailReceiverDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private mailReceiverPopupService: MailReceiverPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.mailReceiverPopupService
                .open(MailReceiverDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
