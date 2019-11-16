import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ProjectWorkflows } from './project-workflows.model';
import { ProjectWorkflowsPopupService } from './project-workflows-popup.service';
import { ProjectWorkflowsService } from './project-workflows.service';

@Component({
    selector: 'jhi-project-workflows-delete-dialog',
    templateUrl: './project-workflows-delete-dialog.component.html'
})
export class ProjectWorkflowsDeleteDialogComponent {

    projectWorkflows: ProjectWorkflows;

    constructor(
        private projectWorkflowsService: ProjectWorkflowsService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.projectWorkflowsService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'projectWorkflowsListModification',
                content: 'Deleted an projectWorkflows'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-project-workflows-delete-popup',
    template: ''
})
export class ProjectWorkflowsDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private projectWorkflowsPopupService: ProjectWorkflowsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.projectWorkflowsPopupService
                .open(ProjectWorkflowsDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
