import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ProjectTemplates } from './project-templates.model';
import { ProjectTemplatesPopupService } from './project-templates-popup.service';
import { ProjectTemplatesService } from './project-templates.service';
import {HttpErrorResponse} from "@angular/common/http";

@Component({
    selector: 'jhi-project-templates-delete-dialog',
    templateUrl: './project-templates-delete-dialog.component.html'
})
export class ProjectTemplatesDeleteDialogComponent {

    projectTemplates: ProjectTemplates;

    constructor(
        private projectTemplatesService: ProjectTemplatesService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.projectTemplatesService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'projectTemplatesListModification',
                content: 'Deleted an projectTemplates'
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
    selector: 'jhi-project-templates-delete-popup',
    template: ''
})
export class ProjectTemplatesDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private projectTemplatesPopupService: ProjectTemplatesPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.projectTemplatesPopupService
                .open(ProjectTemplatesDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
