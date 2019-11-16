import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';
import { Projects } from './projects.model';
import { ProjectsPopupService } from './projects-popup.service';
import { ProjectsService } from './projects.service';
import {HttpErrorResponse} from '@angular/common/http';

@Component({
    selector: 'jhi-projects-delete-dialog',
    templateUrl: './projects-delete-dialog.component.html'
})
export class ProjectsDeleteDialogComponent {

    projects: Projects;

    constructor(
        private projectsService: ProjectsService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.projectsService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'projectsListModification',
                content: 'Deleted an projects'
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
    selector: 'jhi-projects-delete-popup',
    template: ''
})
export class ProjectsDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private projectsPopupService: ProjectsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.projectsPopupService
                .open(ProjectsDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
