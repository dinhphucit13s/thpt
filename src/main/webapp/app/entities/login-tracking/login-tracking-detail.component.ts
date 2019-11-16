import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { LoginTracking } from './login-tracking.model';
import { LoginTrackingService } from './login-tracking.service';

@Component({
    selector: 'jhi-login-tracking-detail',
    templateUrl: './login-tracking-detail.component.html'
})
export class LoginTrackingDetailComponent implements OnInit, OnDestroy {

    loginTracking: LoginTracking;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private loginTrackingService: LoginTrackingService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInLoginTrackings();
    }

    load(id) {
        this.loginTrackingService.find(id)
            .subscribe((loginTrackingResponse: HttpResponse<LoginTracking>) => {
                this.loginTracking = loginTrackingResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInLoginTrackings() {
        this.eventSubscriber = this.eventManager.subscribe(
            'loginTrackingListModification',
            (response) => this.load(this.loginTracking.id)
        );
    }
}
