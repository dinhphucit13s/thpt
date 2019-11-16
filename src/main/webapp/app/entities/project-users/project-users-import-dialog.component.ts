import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService, JhiDataUtils } from 'ng-jhipster';
import {JhiTrackerService} from '../../shared';
import {ProjectUsersService} from './project-users.service';
import {ProjectUsersPopupService} from './project-users-popup.service';
import {ProjectUsers} from './project-users.model';

@Component({
    selector: 'jhi-project-users-import-dialog',
    templateUrl: './project-users-import-dialog.component.html',
    styleUrls: ['./project-users-import-dialog.component.css']
})
export class ProjectUsersImportDialogComponent implements OnInit {
    files: any;
    isSaving: boolean;
    listProjectUsers: any[] = new Array();
    listContentProgress: any[] = new Array();
    totalFileImport: number;

    projectUsers: ProjectUsers;
    projectId: any;
    projectName: any;

    isImportDone = false;
    successList: any[] = new Array();
    existingList: any[] = new Array();
    notExistingList: any[] = new Array();

    constructor(
        private projectUsersService: ProjectUsersService,
        private jhiAlertService: JhiAlertService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager,
        private trackerService: JhiTrackerService,
    ) {
    }

    ngOnInit() {
        this.projectId = this.projectUsers.projectId;
        this.projectName = this.projectUsers.projectName;

        this.trackerService.receive().subscribe((res) => {
            console.log(res);
            const data = JSON.parse(res);
            if (data.progressImport) {
                this.listContentProgress.push(data.progressImport);
            }
            if (data.totalRowsImport) {
                this.totalFileImport = data.totalRowsImport;
            }
            if (data.progressPackages) {
                this.listProjectUsers.push(data.progressPackages);
            }
        });
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    import() {
        this.totalFileImport = 0;
        this.listProjectUsers = new Array();
        this.listContentProgress = new Array();
        this.isSaving = true;
        const formData: FormData = new FormData();
        formData.append('excelFile', this.files);
        formData.append('projectId', new Blob([JSON.stringify(this.projectId)], {
            type: 'application/json'
        }));
        this.subscribeToSaveResponse(
            this.projectUsersService.import(formData)
        );
    }
    onFileChange(fileInput: any) {
        this.files = fileInput.target.files[0];
    }
    private subscribeToSaveResponse(result: Observable<HttpResponse<any>>) {
        result.subscribe(
            (res: HttpResponse<any>) => {
                this.onSaveSuccess(res.body);
            },
            (res: HttpErrorResponse) => this.onSaveError(res));
    }
    private onSaveSuccess(result: any) {
        this.eventManager.broadcast({ name: 'projectUsersListModification', content: 'OK'});
        this.isSaving = false;
        this.isImportDone = true;
        this.successList = result.success;
        this.existingList = result.existing;
        this.notExistingList = result.notExisting;
        // this.activeModal.dismiss(result);
    }
    private onSaveError(res: any) {
        this.isSaving = false;
        this.eventManager.broadcast({
            name: 'dtmsApp.httpError',
            content: res
        });
    }

}

@Component({
    selector: 'jhi-project-users-import-dialog',
    template: ''
})
export class ProjectUsersImportPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private projectUsersPopupService: ProjectUsersPopupService,
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.projectUsersPopupService
                .openImport(ProjectUsersImportDialogComponent as Component, params['id'], params['id2']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
