import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { LoginTracking } from './login-tracking.model';
import { LoginTrackingPopupService } from './login-tracking-popup.service';
import { LoginTrackingService } from './login-tracking.service';

@Component({
    selector: 'jhi-login-tracking-delete-dialog',
    templateUrl: './login-tracking-delete-dialog.component.html'
})
export class LoginTrackingDeleteDialogComponent {

    loginTracking: LoginTracking;

    constructor(
        private loginTrackingService: LoginTrackingService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.loginTrackingService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'loginTrackingListModification',
                content: 'Deleted an loginTracking'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-login-tracking-delete-popup',
    template: ''
})
export class LoginTrackingDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private loginTrackingPopupService: LoginTrackingPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.loginTrackingPopupService
                .open(LoginTrackingDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
