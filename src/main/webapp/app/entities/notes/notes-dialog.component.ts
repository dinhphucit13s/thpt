import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService, JhiDataUtils } from 'ng-jhipster';

import { Notes } from './notes.model';
import { NotesPopupService } from './notes-popup.service';
import { NotesService } from './notes.service';
import { Tasks, TasksService } from '../tasks';
import { Bugs, BugsService } from '../bugs';

@Component({
    selector: 'jhi-notes-dialog',
    templateUrl: './notes-dialog.component.html'
})
export class NotesDialogComponent implements OnInit {

    notes: Notes;
    isSaving: boolean;

    tasks: Tasks[];

    bugs: Bugs[];

    constructor(
        public activeModal: NgbActiveModal,
        private dataUtils: JhiDataUtils,
        private jhiAlertService: JhiAlertService,
        private notesService: NotesService,
        private tasksService: TasksService,
        private bugsService: BugsService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.tasksService.query()
            .subscribe((res: HttpResponse<Tasks[]>) => { this.tasks = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
        this.bugsService.query()
            .subscribe((res: HttpResponse<Bugs[]>) => { this.bugs = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
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
        if (this.notes.id !== undefined) {
            this.subscribeToSaveResponse(
                this.notesService.update(this.notes));
        } else {
            this.subscribeToSaveResponse(
                this.notesService.create(this.notes));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<Notes>>) {
        result.subscribe((res: HttpResponse<Notes>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: Notes) {
        this.eventManager.broadcast({ name: 'notesListModification', content: 'OK'});
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

    trackBugsById(index: number, item: Bugs) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-notes-popup',
    template: ''
})
export class NotesPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private notesPopupService: NotesPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.notesPopupService
                    .open(NotesDialogComponent as Component, params['id']);
            } else {
                this.notesPopupService
                    .open(NotesDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
