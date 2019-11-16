import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { TMSLogHistory } from './tms-log-history.model';
import { TMSLogHistoryPopupService } from './tms-log-history-popup.service';
import { TMSLogHistoryService } from './tms-log-history.service';

@Component({
    selector: 'jhi-tms-log-history-delete-dialog',
    templateUrl: './tms-log-history-delete-dialog.component.html'
})
export class TMSLogHistoryDeleteDialogComponent {

    tMSLogHistory: TMSLogHistory;

    constructor(
        private tMSLogHistoryService: TMSLogHistoryService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.tMSLogHistoryService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'tMSLogHistoryListModification',
                content: 'Deleted an tMSLogHistory'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-tms-log-history-delete-popup',
    template: ''
})
export class TMSLogHistoryDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private tMSLogHistoryPopupService: TMSLogHistoryPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.tMSLogHistoryPopupService
                .open(TMSLogHistoryDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
