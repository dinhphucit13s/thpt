import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import {HttpErrorResponse, HttpResponse} from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';
import {MatDialog} from '@angular/material';
import { IssuesDetailShowAttachmentComponent } from '../view-feed-back/issues-detail-show-attachment.component';

import { Issues } from '../issues/issues.model';
import { IssuesService } from '../issues/issues.service';
import {Observable} from 'rxjs/Observable';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';

@Component({
    selector: 'jhi-view-feed-back-detail',
    templateUrl: './view-feed-back-detail.component.html'
})
export class ViewFeedBackDetailComponent implements OnInit, OnDestroy {

    issues: Issues;
    issuesAttach: any;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager,
        private issuesService: IssuesService,
        private route: ActivatedRoute,
        private dialog: MatDialog
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInIssues();
    }

    getAndShowAttachmentOfNotes(issue: any) {
        this.showAttachment(issue);
        if (issue.attachments.findIndex((x) => x.value === null) >= 0) {
            this.issuesService.findAttach(issue.id).subscribe((res) => {
                const newAttachmentList = res.body;
                if (issue.attachmentsAppend) {
                    Array.prototype.push.apply(newAttachmentList, issue.attachmentsAppend);
                }
                issue.attachment = newAttachmentList;
            });
        }
    }

    showAttachment(bug: Issues) {
        const dialogRef = this.dialog.open(IssuesDetailShowAttachmentComponent, {
            data: { model: bug , mode: 'edit', positionTop: window.scrollY }
        });
    }

    load(id) {
        this.issuesService.find(id)
            .subscribe((issuesResponse: HttpResponse<Issues>) => {
                this.issues = issuesResponse.body;
            });
        this.issuesService.findAttach(id)
            .subscribe((issuesResponse: HttpResponse<any>) => {
                this.issuesAttach = issuesResponse.body;
            });
    }

    changStatusIssue(issuesAttach) {
        this.subscribeToSaveResponse(
            this.issuesService.update(issuesAttach));
    }
    private subscribeToSaveResponse(result: Observable<HttpResponse<Issues>>) {
        result.subscribe(
            (res: HttpResponse<Issues>) => this.onSaveSuccess(res.body),
            (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: Issues) {
        this.eventManager.broadcast({ name: 'viewFeedBackModification', content: 'OK'});
        this.activeModal.dismiss(result);
        this.previousState();
    }

    private onSaveError() {
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
            (response) => this.load(this.issues.id)
        );
    }
}
