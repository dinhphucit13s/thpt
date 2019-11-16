import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiDataUtils } from 'ng-jhipster';

import { BusinessLine } from './business-line.model';
import { BusinessLineService } from './business-line.service';

@Component({
    selector: 'jhi-business-line-detail',
    templateUrl: './business-line-detail.component.html'
})
export class BusinessLineDetailComponent implements OnInit, OnDestroy {

    businessLine: BusinessLine;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private dataUtils: JhiDataUtils,
        private businessLineService: BusinessLineService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInBusinessLines();
    }

    load(id) {
        this.businessLineService.find(id)
            .subscribe((businessLineResponse: HttpResponse<BusinessLine>) => {
                this.businessLine = businessLineResponse.body;
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

    registerChangeInBusinessLines() {
        this.eventSubscriber = this.eventManager.subscribe(
            'businessLineListModification',
            (response) => this.load(this.businessLine.id)
        );
    }
}
