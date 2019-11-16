import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { MailReceiver } from './mail-receiver.model';
import { MailReceiverService } from './mail-receiver.service';

@Component({
    selector: 'jhi-mail-receiver-detail',
    templateUrl: './mail-receiver-detail.component.html'
})
export class MailReceiverDetailComponent implements OnInit, OnDestroy {

    mailReceiver: MailReceiver;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private mailReceiverService: MailReceiverService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInMailReceivers();
    }

    load(id) {
        this.mailReceiverService.find(id)
            .subscribe((mailReceiverResponse: HttpResponse<MailReceiver>) => {
                this.mailReceiver = mailReceiverResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInMailReceivers() {
        this.eventSubscriber = this.eventManager.subscribe(
            'mailReceiverListModification',
            (response) => this.load(this.mailReceiver.id)
        );
    }
}
