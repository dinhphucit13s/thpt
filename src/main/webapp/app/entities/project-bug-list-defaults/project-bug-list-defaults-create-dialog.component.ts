import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';
import { Projects, ProjectsService } from '../projects';
import { ITEMS_PER_PAGE, Principal, User, UserService } from '../../shared';
import {ProjectBugListDefaultsPopupService} from './project-bug-list-defaults-popup.service';
import {ProjectBugListDefaultsService} from './project-bug-list-defaults.service';
import {BugListDefault, BugListDefaultService} from '../bug-list-default';
import {ProjectBugListDefaults} from './project-bug-list-defaults.model';
declare var $: any;
@Component({
    selector: 'jhi-project-bug-list-defaults-create-dialog',
    templateUrl: './project-bug-list-defaults-create-dialog.component.html'
})
export class ProjectBugListDefaultsCreateDialogComponent implements OnInit {

    dropdownSettings = {};
    projectUsersList: Projects[];
    bugListDefaultsList: BugListDefault[];
    projectBugListDefaults: ProjectBugListDefaults[];
    listProjectBugListDefault: ProjectBugListDefaults[];
    projectBug: ProjectBugListDefaults;
    projectId: any;
    projectName: any;
    isSaving: boolean;
    bugListDefaults: BugListDefault;
    dateFormat = require('dateformat');
    minDate = new Date();
    bugCodeExist = true;
    flagSave = false;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private projectBugListDefaultsService: ProjectBugListDefaultsService,
        private projectsService: ProjectsService,
        private eventManager: JhiEventManager,
        private bugListDefaultsService: BugListDefaultService,
    ) {
        this.dateFormat(this.minDate, 'yyyy-mm-dd\'T\'HH:MM:ss');
    }

    ngOnInit() {
        this.projectBugListDefaults = new Array<ProjectBugListDefaults>();
        this.projectId = this.projectBug.projectId;
        this.projectName = this.projectBug.projectName;
        this.loadAll();
    }

    loadAll() {
        this.bugListDefaultsService.findBugListDefaultsUnexistInProject(this.projectId).subscribe(
            (res: HttpResponse<User[]>) => this.onSuccess(res.body, res.headers),
            (res: HttpResponse<any>) => this.onError(res.body)
        );
        this.projectBugListDefaultsService.queryByProject( {
            size: 1000000,
        }, this.projectId).subscribe(
            (res: HttpResponse<User[]>) => {
                this.listProjectBugListDefault = res.body;
            }
        );
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    checkValue(id?: any) {
        const idBugCode = 'file_code' + id;
        const inputValue = (<HTMLInputElement>document.getElementById(idBugCode)).value;
        const items = document.getElementsByClassName('bugCode');
        for (let i = 0; i < items.length; i++) {
            for (let j = i + 1; j < items.length; j++) {
                if ((<HTMLInputElement>items[i]).value === ((<HTMLInputElement>items[j]).value)) {
                    this.bugCodeExist = false;
                } else {
                     this.bugCodeExist = true;
                }
            }
        }
        if (this.listProjectBugListDefault.findIndex((bugcode) => bugcode.code === inputValue) > -1) {
            this.bugCodeExist = false;
        } else {
            this.bugCodeExist = true;
        }
    }

    save() {
        if (!this.flagSave) {
            this.subscribeToSaveResponse(
                this.projectBugListDefaultsService.create(this.projectBugListDefaults));
            this.flagSave = true;
        }
    }

    private onSuccess(data, headers) {
        this.bugListDefaults = data;
        this.dropdownSettings = {
            singleSelection: false,
            idField: 'id',
            textField: 'description',
            allowSearchFilter: true,
            enableCheckAll: false
        };
    }

    onItemSelect(item: any) {
        const bld = new ProjectBugListDefaults();
        bld.bugListDefaultDescription = item.description;
        bld.bugListDefaultId = item.id;
        bld.projectId = this.projectId;
        bld.projectName = this.projectName;
        this.projectBugListDefaults.push(bld);
        console.log(this.projectBugListDefaults);
    }

    onDeSelect(bugToRemove: any) {
        const index = this.projectBugListDefaults.findIndex((bug) => bug.id === bugToRemove.id);
        this.projectBugListDefaults.splice(index, 1);
        console.log(this.projectBugListDefaults);
    }

     private subscribeToSaveResponse(result: Observable<HttpResponse<ProjectBugListDefaults>>) {
         result.subscribe((res: HttpResponse<ProjectBugListDefaults>) =>
             this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
     }

    private onSaveSuccess(result: ProjectBugListDefaults) {
        this.eventManager.broadcast({ name: 'projectBugListDefaultsListModification', content: 'OK' });
        this.isSaving = false;
        this.activeModal.dismiss(result);
        // this.loadAll();
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
}

@Component({
    selector: 'jhi-project-bug-list-defaults-create-popup',
    template: ''
})
export class ProjectBugListDefaultsCreatePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private projectBugListDefaultsPopupService: ProjectBugListDefaultsPopupService
    ) { }

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.projectBugListDefaultsPopupService
                .openNew(ProjectBugListDefaultsCreateDialogComponent as Component, params['id'], params['project-name']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
