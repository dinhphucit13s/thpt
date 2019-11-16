import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { Mail } from './mail.model';
import { MailService } from './mail.service';

@Component({
    selector: 'jhi-mail-detail',
    templateUrl: './mail-detail.component.html'
})
export class MailDetailComponent implements OnInit, OnDestroy {

    mail: Mail;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private mailService: MailService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInMail();
    }

    load(id) {
        this.mailService.find(id)
            .subscribe((mailResponse: HttpResponse<Mail>) => {
                this.mail = mailResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInMail() {
        this.eventSubscriber = this.eventManager.subscribe(
            'mailListModification',
            (response) => this.load(this.mail.id)
        );
    }
}
