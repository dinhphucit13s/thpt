import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';
import { ProjectUsers } from './project-users.model';
import { ProjectUsersPopupService } from './project-users-popup.service';
import { ProjectUsersService } from './project-users.service';
import { Projects, ProjectsService } from '../projects';
import { ITEMS_PER_PAGE, Principal, User, UserService } from '../../shared';
import { query } from '@angular/animations';

@Component({
    selector: 'jhi-project-users-create-dialog',
    templateUrl: './project-users-create-dialog.component.html'
})
export class ProjectUsersCreateDialogComponent implements OnInit {

    dropdownSettings = {};
    projectUsersList: ProjectUsers[];
    projectUsers: ProjectUsers;
    isSaving: boolean;
    users: User[];
    dateFormat = require('dateformat');
    projects: Projects[];
    private projectId;
    private projectName;
    private subscription: Subscription;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private projectUsersService: ProjectUsersService,
        private projectsService: ProjectsService,
        private eventManager: JhiEventManager,
        private userService: UserService,
        private principal: Principal,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.projectUsersList = new Array<ProjectUsers>();
        this.projectId = this.projectUsers.projectId;
        this.projectName = this.projectUsers.projectName;
        this.loadAll();
    }

    loadAll() {
        this.userService.queryforPojectUsers({
            query: this.projectId}
        ).subscribe(
            (res: HttpResponse<User[]>) => this.onSuccess(res.body, res.headers),
            (res: HttpResponse<any>) => this.onError(res.body)
        );
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.projectUsersList.forEach((pu) => {
           pu.startDate =  this.dateFormat(pu.startDate, "yyyy-mm-dd'T'HH:MM:ss");
           pu.endDate =  this.dateFormat(pu.endDate, "yyyy-mm-dd'T'HH:MM:ss");
        });
        this.isSaving = true;
        this.subscribeToSaveResponse(
            this.projectUsersService.createList(this.projectUsersList));
    }

    private onSuccess(data, headers) {
        this.users = data;
        this.users = this.users.filter((u) => u.activated);
        this.dropdownSettings = {
            singleSelection: false,
            idField: 'id',
            textField: 'login',
            itemsShowLimit: 2,
            allowSearchFilter: true,
            enableCheckAll: false,
            limitSelection: 1000000
        };
    }

    onItemSelect(item: any) {
        const pu = new ProjectUsers();
        pu.userLogin = item.login;
        pu.projectId = this.projectId;
        pu.projectName = this.projectName;
        this.projectUsersList.push(pu);
    }

    onDeSelect(item: any) {
        const index = this.projectUsersList.findIndex(x => x.userLogin === item.login);
        this.projectUsersList.splice(index, 1);
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ProjectUsers>>) {
        result.subscribe((res: HttpResponse<ProjectUsers>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: ProjectUsers) {
        this.isSaving = false;
        this.eventManager.broadcast({ name: 'projectUsersListModification', content: 'OK' });
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.isSaving = false;
        this.jhiAlertService.error(error.message, null, null);
    }

    trackProjectsById(index: number, item: Projects) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-project-users-create-popup',
    template: ''
})
export class ProjectUsersCreatePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private projectUsersPopupService: ProjectUsersPopupService
    ) { }

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.projectUsersPopupService
                .openNew(ProjectUsersCreateDialogComponent as Component, params['id'], params['id2']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
