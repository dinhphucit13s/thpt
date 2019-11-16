import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ProjectBugListDefault } from './project-bug-list-default.model';
import { ProjectBugListDefaultPopupService } from './project-bug-list-default-popup.service';
import { ProjectBugListDefaultService } from './project-bug-list-default.service';

@Component({
    selector: 'jhi-project-bug-list-default-delete-dialog',
    templateUrl: './project-bug-list-default-delete-dialog.component.html'
})
export class ProjectBugListDefaultDeleteDialogComponent {

    projectBugListDefault: ProjectBugListDefault;

    constructor(
        private projectBugListDefaultService: ProjectBugListDefaultService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.projectBugListDefaultService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'projectBugListDefaultListModification',
                content: 'Deleted an projectBugListDefault'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-project-bug-list-default-delete-popup',
    template: ''
})
export class ProjectBugListDefaultDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private projectBugListDefaultPopupService: ProjectBugListDefaultPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.projectBugListDefaultPopupService
                .open(ProjectBugListDefaultDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
