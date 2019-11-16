import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Attachments } from './attachments.model';
import { AttachmentsPopupService } from './attachments-popup.service';
import { AttachmentsService } from './attachments.service';
import { Bugs, BugsService } from '../bugs';
import { Notes, NotesService } from '../notes';
import { Issues, IssuesService } from '../issues';
import { Mail, MailService } from '../mail';
import { Comments, CommentsService } from '../comments';
import { TmsPost, TmsPostService } from '../tms-post';

@Component({
    selector: 'jhi-attachments-dialog',
    templateUrl: './attachments-dialog.component.html'
})
export class AttachmentsDialogComponent implements OnInit {

    attachments: Attachments;
    isSaving: boolean;

    bugs: Bugs[];

    notes: Notes[];

    issues: Issues[];

    mail: Mail[];

    comments: Comments[];

    tmsposts: TmsPost[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private attachmentsService: AttachmentsService,
        private bugsService: BugsService,
        private notesService: NotesService,
        private issuesService: IssuesService,
        private mailService: MailService,
        private commentsService: CommentsService,
        private tmsPostService: TmsPostService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.bugsService.query()
            .subscribe((res: HttpResponse<Bugs[]>) => { this.bugs = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
        this.notesService.query()
            .subscribe((res: HttpResponse<Notes[]>) => { this.notes = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
        this.issuesService.query()
            .subscribe((res: HttpResponse<Issues[]>) => { this.issues = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
        this.mailService.query()
            .subscribe((res: HttpResponse<Mail[]>) => { this.mail = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
        this.commentsService.query()
            .subscribe((res: HttpResponse<Comments[]>) => { this.comments = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
        this.tmsPostService.query()
            .subscribe((res: HttpResponse<TmsPost[]>) => { this.tmsposts = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.attachments.id !== undefined) {
            this.subscribeToSaveResponse(
                this.attachmentsService.update(this.attachments));
        } else {
            this.subscribeToSaveResponse(
                this.attachmentsService.create(this.attachments));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<Attachments>>) {
        result.subscribe((res: HttpResponse<Attachments>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: Attachments) {
        this.eventManager.broadcast({ name: 'attachmentsListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackBugsById(index: number, item: Bugs) {
        return item.id;
    }

    trackNotesById(index: number, item: Notes) {
        return item.id;
    }

    trackIssuesById(index: number, item: Issues) {
        return item.id;
    }

    trackMailById(index: number, item: Mail) {
        return item.id;
    }

    trackCommentsById(index: number, item: Comments) {
        return item.id;
    }

    trackTmsPostById(index: number, item: TmsPost) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-attachments-popup',
    template: ''
})
export class AttachmentsPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private attachmentsPopupService: AttachmentsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.attachmentsPopupService
                    .open(AttachmentsDialogComponent as Component, params['id']);
            } else {
                this.attachmentsPopupService
                    .open(AttachmentsDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
