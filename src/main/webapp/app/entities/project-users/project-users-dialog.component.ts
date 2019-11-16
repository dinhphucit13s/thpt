import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { ProjectUsers } from './project-users.model';
import { ProjectUsersPopupService } from './project-users-popup.service';
import { ProjectUsersService } from './project-users.service';
import { Projects, ProjectsService } from '../projects';
import { User, UserService } from '../../shared';

@Component({
    selector: 'jhi-project-users-dialog',
    templateUrl: './project-users-dialog.component.html'
})
export class ProjectUsersDialogComponent implements OnInit {

    projectUsers: ProjectUsers;
    dateFormat = require('dateformat');
    isSaving: boolean;
    users: User[];
    usersSelect: User[];
    projects: Projects[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private projectUsersService: ProjectUsersService,
        private projectsService: ProjectsService,
        private eventManager: JhiEventManager,
        private userService: UserService
    ) {
    }
    onChangeMat(userLogin: any) {

    }
    ngOnInit() {
        this.isSaving = false;
        this.projectsService.query()
            .subscribe((res: HttpResponse<Projects[]>) => { this.projects = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
        this.userService.query()
            .subscribe((res: HttpResponse<User[]>) => { this.users = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.projectUsers.startDate = this.dateFormat(this.projectUsers.startDate, "yyyy-mm-dd'T'HH:MM:ss");
        this.projectUsers.endDate = this.dateFormat(this.projectUsers.endDate, "yyyy-mm-dd'T'HH:MM:ss");
        this.isSaving = true;
        if (this.projectUsers.id !== undefined) {
            this.subscribeToSaveResponse(
                this.projectUsersService.update(this.projectUsers));
        } else {
            this.subscribeToSaveResponse(
                this.projectUsersService.create(this.projectUsers));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ProjectUsers>>) {
        result.subscribe((res: HttpResponse<ProjectUsers>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: ProjectUsers) {
        this.eventManager.broadcast({ name: 'projectUsersListModification', content: 'OK'});
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

    trackUsersById(index: number, item: User) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-project-users-popup',
    template: ''
})
export class ProjectUsersPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private projectUsersPopupService: ProjectUsersPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.projectUsersPopupService
                    .open(ProjectUsersDialogComponent as Component, params['id']);
            } else {
                this.projectUsersPopupService
                    .open(ProjectUsersDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
