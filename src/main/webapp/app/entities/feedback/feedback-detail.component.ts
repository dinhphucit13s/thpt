import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import {Feedback} from './feedback.model';
import { FeedbackService } from './feedback.service';
import {FeedbackShowAttachmentComponent} from "./feedback-show-attachment/feedback-show-attachment.component";
import {MatDialog} from "@angular/material";
import {Issues, IssuesService} from "../issues";

@Component({
    selector: 'jhi-issues-detail',
    templateUrl: './feedback-detail.component.html'
})
export class FeedbackDetailComponent implements OnInit, OnDestroy {

    feedback: Feedback;
    issues: Issues;
    issuesAttach: any;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private dialogAttachment: MatDialog,
        private eventManager: JhiEventManager,
        private feedbackService: FeedbackService,
        private issuesService: IssuesService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInIssues();
    }

    load(id) {
        /*this.issuesService.find(id)
            .subscribe((issuesResponse: HttpResponse<Issues>) => {
                this.issues = issuesResponse.body;
            });*/

        this.issuesService.findAttach(id)
            .subscribe((issuesResponse: HttpResponse<any>) => {
                this.issuesAttach = issuesResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInIssues() {
        this.eventSubscriber = this.eventManager.subscribe(
            'issuesListModification',
            (response) => this.load(this.issuesAttach.id)
        );
    }

    getAndShowAttachment(feedback: any) {
        this.showAttachment(feedback);
        if (feedback.attachments.findIndex(x => x.value === null) >= 0) {
            this.issuesService.findAttach(feedback.id).subscribe(res => {
                const newAttachmentList = res.body;
                if (feedback.attachmentsAppend) {
                    // newAttachmentList.concat(feedback.attachmentsAppend);
                    Array.prototype.push.apply(newAttachmentList, feedback.attachmentsAppend);
                }
                feedback.attachments = newAttachmentList;
            });
        }
    }

    showAttachment(feedback: any) {
        this.dialogAttachment.open(FeedbackShowAttachmentComponent, {
            data: {model: feedback, mode: 'view'}
        });
    }
}
