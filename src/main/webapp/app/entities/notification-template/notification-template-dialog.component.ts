import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { NotificationTemplate } from './notification-template.model';
import { NotificationTemplatePopupService } from './notification-template-popup.service';
import { NotificationTemplateService } from './notification-template.service';

@Component({
    selector: 'jhi-notification-template-dialog',
    templateUrl: './notification-template-dialog.component.html'
})
export class NotificationTemplateDialogComponent implements OnInit {

    notificationTemplate: NotificationTemplate;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private notificationTemplateService: NotificationTemplateService,
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
        if (this.notificationTemplate.id !== undefined) {
            this.subscribeToSaveResponse(
                this.notificationTemplateService.update(this.notificationTemplate));
        } else {
            this.subscribeToSaveResponse(
                this.notificationTemplateService.create(this.notificationTemplate));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<NotificationTemplate>>) {
        result.subscribe((res: HttpResponse<NotificationTemplate>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: NotificationTemplate) {
        this.eventManager.broadcast({ name: 'notificationTemplateListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }
}

@Component({
    selector: 'jhi-notification-template-popup',
    template: ''
})
export class NotificationTemplatePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private notificationTemplatePopupService: NotificationTemplatePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.notificationTemplatePopupService
                    .open(NotificationTemplateDialogComponent as Component, params['id']);
            } else {
                this.notificationTemplatePopupService
                    .open(NotificationTemplateDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
