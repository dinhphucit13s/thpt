import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService, JhiDataUtils } from 'ng-jhipster';

import { Bugs } from './bugs.model';
import { BugsPopupService } from './bugs-popup.service';
import { BugsService } from './bugs.service';
import { Tasks, TasksService } from '../tasks';

@Component({
    selector: 'jhi-bugs-dialog',
    templateUrl: './bugs-dialog.component.html'
})
export class BugsDialogComponent implements OnInit {

    bugs: Bugs;
    isSaving: boolean;

    tasks: Tasks[];

    constructor(
        public activeModal: NgbActiveModal,
        private dataUtils: JhiDataUtils,
        private jhiAlertService: JhiAlertService,
        private bugsService: BugsService,
        private tasksService: TasksService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.tasksService.query()
            .subscribe((res: HttpResponse<Tasks[]>) => { this.tasks = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }

    setFileData(event, entity, field, isImage) {
        this.dataUtils.setFileData(event, entity, field, isImage);
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.bugs.id !== undefined) {
            this.subscribeToSaveResponse(
                this.bugsService.update(this.bugs));
        } else {
            this.subscribeToSaveResponse(
                this.bugsService.create(this.bugs));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<Bugs>>) {
        result.subscribe((res: HttpResponse<Bugs>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: Bugs) {
        this.eventManager.broadcast({ name: 'bugsListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackTasksById(index: number, item: Tasks) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-bugs-popup',
    template: ''
})
export class BugsPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private bugsPopupService: BugsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.bugsPopupService
                    .open(BugsDialogComponent as Component, params['id']);
            } else {
                this.bugsPopupService
                    .open(BugsDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
