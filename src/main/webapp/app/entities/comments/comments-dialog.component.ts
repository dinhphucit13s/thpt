import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Comments } from './comments.model';
import { CommentsPopupService } from './comments-popup.service';
import { CommentsService } from './comments.service';
import { TmsPost, TmsPostService } from '../tms-post';

@Component({
    selector: 'jhi-comments-dialog',
    templateUrl: './comments-dialog.component.html'
})
export class CommentsDialogComponent implements OnInit {

    comments: Comments;
    isSaving: boolean;

    tmsposts: TmsPost[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private commentsService: CommentsService,
        private tmsPostService: TmsPostService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.tmsPostService.query()
            .subscribe((res: HttpResponse<TmsPost[]>) => { this.tmsposts = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.comments.id !== undefined) {
            this.subscribeToSaveResponse(
                this.commentsService.update(this.comments));
        } else {
            this.subscribeToSaveResponse(
                this.commentsService.create(this.comments));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<Comments>>) {
        result.subscribe((res: HttpResponse<Comments>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: Comments) {
        this.eventManager.broadcast({ name: 'commentsListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackTmsPostById(index: number, item: TmsPost) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-comments-popup',
    template: ''
})
export class CommentsPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private commentsPopupService: CommentsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.commentsPopupService
                    .open(CommentsDialogComponent as Component, params['id']);
            } else {
                this.commentsPopupService
                    .open(CommentsDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
