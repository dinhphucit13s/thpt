import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { DtmsMonitoring } from './dtms-monitoring.model';
import { DtmsMonitoringService } from './dtms-monitoring.service';

@Component({
    selector: 'jhi-dtms-monitoring-detail',
    templateUrl: './dtms-monitoring-detail.component.html'
})
export class DtmsMonitoringDetailComponent implements OnInit, OnDestroy {

    dtmsMonitoring: DtmsMonitoring;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private dtmsMonitoringService: DtmsMonitoringService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInDtmsMonitorings();
    }

    load(id) {
        this.dtmsMonitoringService.find(id)
            .subscribe((dtmsMonitoringResponse: HttpResponse<DtmsMonitoring>) => {
                this.dtmsMonitoring = dtmsMonitoringResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInDtmsMonitorings() {
        this.eventSubscriber = this.eventManager.subscribe(
            'dtmsMonitoringListModification',
            (response) => this.load(this.dtmsMonitoring.id)
        );
    }
}
