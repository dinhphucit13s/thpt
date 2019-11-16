import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { Attachments } from './attachments.model';
import { AttachmentsService } from './attachments.service';

@Component({
    selector: 'jhi-attachments-detail',
    templateUrl: './attachments-detail.component.html'
})
export class AttachmentsDetailComponent implements OnInit, OnDestroy {

    attachments: Attachments;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private attachmentsService: AttachmentsService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInAttachments();
    }

    load(id) {
        this.attachmentsService.find(id)
            .subscribe((attachmentsResponse: HttpResponse<Attachments>) => {
                this.attachments = attachmentsResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInAttachments() {
        this.eventSubscriber = this.eventManager.subscribe(
            'attachmentsListModification',
            (response) => this.load(this.attachments.id)
        );
    }
}
