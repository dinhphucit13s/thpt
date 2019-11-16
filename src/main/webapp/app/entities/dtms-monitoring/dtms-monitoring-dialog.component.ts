import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { DtmsMonitoring } from './dtms-monitoring.model';
import { DtmsMonitoringPopupService } from './dtms-monitoring-popup.service';
import { DtmsMonitoringService } from './dtms-monitoring.service';

@Component({
    selector: 'jhi-dtms-monitoring-dialog',
    templateUrl: './dtms-monitoring-dialog.component.html'
})
export class DtmsMonitoringDialogComponent implements OnInit {

    dtmsMonitoring: DtmsMonitoring;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private dtmsMonitoringService: DtmsMonitoringService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.dtmsMonitoring.id !== undefined) {
            this.subscribeToSaveResponse(
                this.dtmsMonitoringService.update(this.dtmsMonitoring));
        } else {
            this.subscribeToSaveResponse(
                this.dtmsMonitoringService.create(this.dtmsMonitoring));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<DtmsMonitoring>>) {
        result.subscribe((res: HttpResponse<DtmsMonitoring>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: DtmsMonitoring) {
        this.eventManager.broadcast({ name: 'dtmsMonitoringListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }
}

@Component({
    selector: 'jhi-dtms-monitoring-popup',
    template: ''
})
export class DtmsMonitoringPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private dtmsMonitoringPopupService: DtmsMonitoringPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.dtmsMonitoringPopupService
                    .open(DtmsMonitoringDialogComponent as Component, params['id']);
            } else {
                this.dtmsMonitoringPopupService
                    .open(DtmsMonitoringDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
