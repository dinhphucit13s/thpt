import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiDataUtils } from 'ng-jhipster';

import { TMSCustomField } from './tms-custom-field.model';
import { TMSCustomFieldService } from './tms-custom-field.service';

@Component({
    selector: 'jhi-tms-custom-field-detail',
    templateUrl: './tms-custom-field-detail.component.html'
})
export class TMSCustomFieldDetailComponent implements OnInit, OnDestroy {

    tMSCustomField: TMSCustomField;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private dataUtils: JhiDataUtils,
        private tMSCustomFieldService: TMSCustomFieldService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInTMSCustomFields();
    }

    load(id) {
        this.tMSCustomFieldService.find(id)
            .subscribe((tMSCustomFieldResponse: HttpResponse<TMSCustomField>) => {
                this.tMSCustomField = tMSCustomFieldResponse.body;
                this.tMSCustomField.entityData = JSON.parse(this.tMSCustomField.entityData);
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

    registerChangeInTMSCustomFields() {
        this.eventSubscriber = this.eventManager.subscribe(
            'tMSCustomFieldListModification',
            (response) => this.load(this.tMSCustomField.id)
        );
    }
}
