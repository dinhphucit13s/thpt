import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiDataUtils } from 'ng-jhipster';

import { TMSLogHistory } from './tms-log-history.model';
import { TMSLogHistoryService } from './tms-log-history.service';

@Component({
    selector: 'jhi-tms-log-history-detail',
    templateUrl: './tms-log-history-detail.component.html'
})
export class TMSLogHistoryDetailComponent implements OnInit, OnDestroy {

    tMSLogHistory: TMSLogHistory;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private dataUtils: JhiDataUtils,
        private tMSLogHistoryService: TMSLogHistoryService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInTMSLogHistories();
    }

    load(id) {
        this.tMSLogHistoryService.find(id)
            .subscribe((tMSLogHistoryResponse: HttpResponse<TMSLogHistory>) => {
                this.tMSLogHistory = tMSLogHistoryResponse.body;
            });
    }
    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInTMSLogHistories() {
        this.eventSubscriber = this.eventManager.subscribe(
            'tMSLogHistoryListModification',
            (response) => this.load(this.tMSLogHistory.id)
        );
    }
}
