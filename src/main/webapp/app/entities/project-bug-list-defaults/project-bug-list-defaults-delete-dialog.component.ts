import {Component, OnInit, OnDestroy, Input, ViewChild} from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import {ProjectBugListDefaultsPopupService} from './project-bug-list-defaults-popup.service';
import {ProjectBugListDefaultsService} from './project-bug-list-defaults.service';
import {ProjectBugListDefaults} from './project-bug-list-defaults.model';
import {HttpErrorResponse} from '@angular/common/http';

@Component({
    selector: 'jhi-project-bug-list-defaults-delete-dialog',
    templateUrl: './project-bug-list-defaults-delete-dialog.component.html'
})
export class ProjectBugListDefaultsDeleteDialogComponent {
    bugListDefault: any;
    projectBugListDefault: ProjectBugListDefaults;
    projectId: any;
    errorMessage: any;
    constructor(
        private projectBugListDefaultsService: ProjectBugListDefaultsService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager,
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }
    confirmDelete(id: number) {
        this.projectBugListDefaultsService.delete(id).subscribe((response) => {
                this.eventManager.broadcast({
                    name: 'projectBugListDefaultsListModification',
                    content: 'Deleted an ProjectBugListDefaults'
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
    selector: 'jhi-project-bug-list-defaults-delete-popup',
    template: ''
})
export class ProjectBugListDefaultsDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private projectBugListDefaultsPopupService: ProjectBugListDefaultsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.projectBugListDefaultsPopupService
                .open(ProjectBugListDefaultsDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
