import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Attachments } from './attachments.model';
import { AttachmentsPopupService } from './attachments-popup.service';
import { AttachmentsService } from './attachments.service';

@Component({
    selector: 'jhi-attachments-delete-dialog',
    templateUrl: './attachments-delete-dialog.component.html'
})
export class AttachmentsDeleteDialogComponent {

    attachments: Attachments;

    constructor(
        private attachmentsService: AttachmentsService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.attachmentsService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'attachmentsListModification',
                content: 'Deleted an attachments'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-attachments-delete-popup',
    template: ''
})
export class AttachmentsDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private attachmentsPopupService: AttachmentsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.attachmentsPopupService
                .open(AttachmentsDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
