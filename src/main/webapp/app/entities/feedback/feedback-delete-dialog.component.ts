import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { FeedbackPopupService } from './feedback-popup.service';
import { FeedbackService } from './feedback.service';
import {Issues} from "../issues";
import {HttpErrorResponse} from "@angular/common/http";

@Component({
    selector: 'jhi-issues-delete-dialog',
    templateUrl: './feedback-delete-dialog.component.html'
})
export class FeedbackDeleteDialogComponent {

    issues: Issues;

    constructor(
        private issuesService: FeedbackService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.issuesService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'issuesListModification',
                content: 'Deleted an issues'
            });
            this.activeModal.dismiss(true);
        }, (res: HttpErrorResponse) => {
            this.eventManager.broadcast({
                name: 'dtmsApp.httpError',
                content: res
            });
        });
    }
}

@Component({
    selector: 'jhi-issues-delete-popup',
    template: ''
})
export class FeedbackDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private issuesPopupService: FeedbackPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.issuesPopupService
                .open(FeedbackDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
