import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { BusinessUnitManager } from './business-unit-manager.model';
import { BusinessUnitManagerService } from './business-unit-manager.service';

@Component({
    selector: 'jhi-business-unit-manager-detail',
    templateUrl: './business-unit-manager-detail.component.html'
})
export class BusinessUnitManagerDetailComponent implements OnInit, OnDestroy {

    businessUnitManager: BusinessUnitManager;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private businessUnitManagerService: BusinessUnitManagerService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInBusinessUnitManagers();
    }

    load(id) {
        this.businessUnitManagerService.find(id)
            .subscribe((businessUnitManagerResponse: HttpResponse<BusinessUnitManager>) => {
                this.businessUnitManager = businessUnitManagerResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInBusinessUnitManagers() {
        this.eventSubscriber = this.eventManager.subscribe(
            'businessUnitManagerListModification',
            (response) => this.load(this.businessUnitManager.id)
        );
    }
}
