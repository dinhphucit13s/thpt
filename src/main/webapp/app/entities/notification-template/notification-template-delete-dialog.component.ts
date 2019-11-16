import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { NotificationTemplate } from './notification-template.model';
import { NotificationTemplatePopupService } from './notification-template-popup.service';
import { NotificationTemplateService } from './notification-template.service';

@Component({
    selector: 'jhi-notification-template-delete-dialog',
    templateUrl: './notification-template-delete-dialog.component.html'
})
export class NotificationTemplateDeleteDialogComponent {

    notificationTemplate: NotificationTemplate;

    constructor(
        private notificationTemplateService: NotificationTemplateService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.notificationTemplateService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'notificationTemplateListModification',
                content: 'Deleted an notificationTemplate'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-notification-template-delete-popup',
    template: ''
})
export class NotificationTemplateDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private notificationTemplatePopupService: NotificationTemplatePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.notificationTemplatePopupService
                .open(NotificationTemplateDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
