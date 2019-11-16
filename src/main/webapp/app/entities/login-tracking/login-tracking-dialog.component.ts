import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { LoginTracking } from './login-tracking.model';
import { LoginTrackingPopupService } from './login-tracking-popup.service';
import { LoginTrackingService } from './login-tracking.service';

@Component({
    selector: 'jhi-login-tracking-dialog',
    templateUrl: './login-tracking-dialog.component.html'
})
export class LoginTrackingDialogComponent implements OnInit {

    loginTracking: LoginTracking;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private loginTrackingService: LoginTrackingService,
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
        if (this.loginTracking.id !== undefined) {
            this.subscribeToSaveResponse(
                this.loginTrackingService.update(this.loginTracking));
        } else {
            this.subscribeToSaveResponse(
                this.loginTrackingService.create(this.loginTracking));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<LoginTracking>>) {
        result.subscribe((res: HttpResponse<LoginTracking>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: LoginTracking) {
        this.eventManager.broadcast({ name: 'loginTrackingListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }
}

@Component({
    selector: 'jhi-login-tracking-popup',
    template: ''
})
export class LoginTrackingPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private loginTrackingPopupService: LoginTrackingPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.loginTrackingPopupService
                    .open(LoginTrackingDialogComponent as Component, params['id']);
            } else {
                this.loginTrackingPopupService
                    .open(LoginTrackingDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
