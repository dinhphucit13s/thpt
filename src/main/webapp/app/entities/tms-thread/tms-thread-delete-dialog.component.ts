import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { TmsThread } from './tms-thread.model';
import { TmsThreadPopupService } from './tms-thread-popup.service';
import { TmsThreadService } from './tms-thread.service';

@Component({
    selector: 'jhi-tms-thread-delete-dialog',
    templateUrl: './tms-thread-delete-dialog.component.html'
})
export class TmsThreadDeleteDialogComponent {

    tmsThread: TmsThread;

    constructor(
        private tmsThreadService: TmsThreadService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.tmsThreadService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'tmsThreadListModification',
                content: 'Deleted an tmsThread'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-tms-thread-delete-popup',
    template: ''
})
export class TmsThreadDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private tmsThreadPopupService: TmsThreadPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.tmsThreadPopupService
                .open(TmsThreadDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
