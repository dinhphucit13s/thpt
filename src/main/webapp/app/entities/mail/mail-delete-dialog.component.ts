import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Mail } from './mail.model';
import { MailPopupService } from './mail-popup.service';
import { MailService } from './mail.service';

@Component({
    selector: 'jhi-mail-delete-dialog',
    templateUrl: './mail-delete-dialog.component.html'
})
export class MailDeleteDialogComponent {

    mail: Mail;

    constructor(
        private mailService: MailService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.mailService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'mailListModification',
                content: 'Deleted an mail'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-mail-delete-popup',
    template: ''
})
export class MailDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private mailPopupService: MailPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.mailPopupService
                .open(MailDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
