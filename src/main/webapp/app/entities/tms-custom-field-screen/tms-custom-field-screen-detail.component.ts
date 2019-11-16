import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { TMSCustomFieldScreen } from './tms-custom-field-screen.model';
import { TMSCustomFieldScreenService } from './tms-custom-field-screen.service';

@Component({
    selector: 'jhi-tms-custom-field-screen-detail',
    templateUrl: './tms-custom-field-screen-detail.component.html'
})
export class TMSCustomFieldScreenDetailComponent implements OnInit, OnDestroy {

    tMSCustomFieldScreen: TMSCustomFieldScreen;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private tMSCustomFieldScreenService: TMSCustomFieldScreenService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInTMSCustomFieldScreens();
    }

    load(id) {
        this.tMSCustomFieldScreenService.find(id)
            .subscribe((tMSCustomFieldScreenResponse: HttpResponse<TMSCustomFieldScreen>) => {
                this.tMSCustomFieldScreen = tMSCustomFieldScreenResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInTMSCustomFieldScreens() {
        this.eventSubscriber = this.eventManager.subscribe(
            'tMSCustomFieldScreenListModification',
            (response) => this.load(this.tMSCustomFieldScreen.id)
        );
    }
}
