import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiDataUtils } from 'ng-jhipster';

import { TMSCustomFieldScreenValue } from './tms-custom-field-screen-value.model';
import { TMSCustomFieldScreenValueService } from './tms-custom-field-screen-value.service';

@Component({
    selector: 'jhi-tms-custom-field-screen-value-detail',
    templateUrl: './tms-custom-field-screen-value-detail.component.html'
})
export class TMSCustomFieldScreenValueDetailComponent implements OnInit, OnDestroy {

    tMSCustomFieldScreenValue: TMSCustomFieldScreenValue;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private dataUtils: JhiDataUtils,
        private tMSCustomFieldScreenValueService: TMSCustomFieldScreenValueService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInTMSCustomFieldScreenValues();
    }

    load(id) {
        this.tMSCustomFieldScreenValueService.find(id)
            .subscribe((tMSCustomFieldScreenValueResponse: HttpResponse<TMSCustomFieldScreenValue>) => {
                this.tMSCustomFieldScreenValue = tMSCustomFieldScreenValueResponse.body;
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

    registerChangeInTMSCustomFieldScreenValues() {
        this.eventSubscriber = this.eventManager.subscribe(
            'tMSCustomFieldScreenValueListModification',
            (response) => this.load(this.tMSCustomFieldScreenValue.id)
        );
    }
}
