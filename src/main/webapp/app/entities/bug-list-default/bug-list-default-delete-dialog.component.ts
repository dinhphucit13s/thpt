import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { BugListDefault } from './bug-list-default.model';
import { BugListDefaultPopupService } from './bug-list-default-popup.service';
import { BugListDefaultService } from './bug-list-default.service';
import {HttpErrorResponse} from '@angular/common/http';

@Component({
    selector: 'jhi-bug-list-default-delete-dialog',
    templateUrl: './bug-list-default-delete-dialog.component.html'
})
export class BugListDefaultDeleteDialogComponent {

    bugListDefault: BugListDefault;
    errorMessage: any;

    constructor(
        private bugListDefaultService: BugListDefaultService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.bugListDefaultService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'bugListDefaultListModification',
                content: 'Deleted an bugListDefault'
            });
            this.activeModal.dismiss(true);
        }, (res: HttpErrorResponse) => {
            this.errorMessage = res.message;
        });
    }
}

@Component({
    selector: 'jhi-bug-list-default-delete-popup',
    template: ''
})
export class BugListDefaultDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private bugListDefaultPopupService: BugListDefaultPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.bugListDefaultPopupService
                .open(BugListDefaultDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
