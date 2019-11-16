import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { DtmsMonitoring } from './dtms-monitoring.model';
import { DtmsMonitoringPopupService } from './dtms-monitoring-popup.service';
import { DtmsMonitoringService } from './dtms-monitoring.service';

@Component({
    selector: 'jhi-dtms-monitoring-delete-dialog',
    templateUrl: './dtms-monitoring-delete-dialog.component.html'
})
export class DtmsMonitoringDeleteDialogComponent {

    dtmsMonitoring: DtmsMonitoring;

    constructor(
        private dtmsMonitoringService: DtmsMonitoringService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.dtmsMonitoringService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'dtmsMonitoringListModification',
                content: 'Deleted an dtmsMonitoring'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-dtms-monitoring-delete-popup',
    template: ''
})
export class DtmsMonitoringDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private dtmsMonitoringPopupService: DtmsMonitoringPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.dtmsMonitoringPopupService
                .open(DtmsMonitoringDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
