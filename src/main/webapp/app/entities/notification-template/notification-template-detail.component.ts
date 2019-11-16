import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { NotificationTemplate } from './notification-template.model';
import { NotificationTemplateService } from './notification-template.service';

@Component({
    selector: 'jhi-notification-template-detail',
    templateUrl: './notification-template-detail.component.html'
})
export class NotificationTemplateDetailComponent implements OnInit, OnDestroy {

    notificationTemplate: NotificationTemplate;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private notificationTemplateService: NotificationTemplateService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInNotificationTemplates();
    }

    load(id) {
        this.notificationTemplateService.find(id)
            .subscribe((notificationTemplateResponse: HttpResponse<NotificationTemplate>) => {
                this.notificationTemplate = notificationTemplateResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInNotificationTemplates() {
        this.eventSubscriber = this.eventManager.subscribe(
            'notificationTemplateListModification',
            (response) => this.load(this.notificationTemplate.id)
        );
    }
}
