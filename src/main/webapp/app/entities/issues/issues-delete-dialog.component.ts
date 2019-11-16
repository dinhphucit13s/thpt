import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Issues } from './issues.model';
import { IssuesPopupService } from './issues-popup.service';
import { IssuesService } from './issues.service';
import {HttpErrorResponse} from "@angular/common/http";

@Component({
    selector: 'jhi-issues-delete-dialog',
    templateUrl: './issues-delete-dialog.component.html'
})
export class IssuesDeleteDialogComponent {

    issues: Issues;

    constructor(
        private issuesService: IssuesService,
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
export class IssuesDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private issuesPopupService: IssuesPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.issuesPopupService
                .open(IssuesDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
