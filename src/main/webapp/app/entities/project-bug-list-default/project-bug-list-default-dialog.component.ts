import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { ProjectBugListDefault } from './project-bug-list-default.model';
import { ProjectBugListDefaultPopupService } from './project-bug-list-default-popup.service';
import { ProjectBugListDefaultService } from './project-bug-list-default.service';
import { Projects, ProjectsService } from '../projects';
import { BugListDefault, BugListDefaultService } from '../bug-list-default';

@Component({
    selector: 'jhi-project-bug-list-default-dialog',
    templateUrl: './project-bug-list-default-dialog.component.html'
})
export class ProjectBugListDefaultDialogComponent implements OnInit {

    projectBugListDefault: ProjectBugListDefault;
    isSaving: boolean;

    projects: Projects[];

    buglistdefaults: BugListDefault[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private projectBugListDefaultService: ProjectBugListDefaultService,
        private projectsService: ProjectsService,
        private bugListDefaultService: BugListDefaultService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.projectsService.query()
            .subscribe((res: HttpResponse<Projects[]>) => { this.projects = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
        this.bugListDefaultService.query()
            .subscribe((res: HttpResponse<BugListDefault[]>) => { this.buglistdefaults = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.projectBugListDefault.id !== undefined) {
            this.subscribeToSaveResponse(
                this.projectBugListDefaultService.update(this.projectBugListDefault));
        } else {
            this.subscribeToSaveResponse(
                this.projectBugListDefaultService.create(this.projectBugListDefault));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ProjectBugListDefault>>) {
        result.subscribe((res: HttpResponse<ProjectBugListDefault>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: ProjectBugListDefault) {
        this.eventManager.broadcast({ name: 'projectBugListDefaultListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackProjectsById(index: number, item: Projects) {
        return item.id;
    }

    trackBugListDefaultById(index: number, item: BugListDefault) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-project-bug-list-default-popup',
    template: ''
})
export class ProjectBugListDefaultPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private projectBugListDefaultPopupService: ProjectBugListDefaultPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.projectBugListDefaultPopupService
                    .open(ProjectBugListDefaultDialogComponent as Component, params['id']);
            } else {
                this.projectBugListDefaultPopupService
                    .open(ProjectBugListDefaultDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
