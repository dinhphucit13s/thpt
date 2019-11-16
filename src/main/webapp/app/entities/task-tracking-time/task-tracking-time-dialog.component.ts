import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { TaskTrackingTime } from './task-tracking-time.model';
import { TaskTrackingTimePopupService } from './task-tracking-time-popup.service';
import { TaskTrackingTimeService } from './task-tracking-time.service';

@Component({
    selector: 'jhi-task-tracking-time-dialog',
    templateUrl: './task-tracking-time-dialog.component.html'
})
export class TaskTrackingTimeDialogComponent implements OnInit {

    taskTrackingTime: TaskTrackingTime;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private taskTrackingTimeService: TaskTrackingTimeService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.taskTrackingTime.id !== undefined) {
            this.subscribeToSaveResponse(
                this.taskTrackingTimeService.update(this.taskTrackingTime));
        } else {
            this.subscribeToSaveResponse(
                this.taskTrackingTimeService.create(this.taskTrackingTime));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<TaskTrackingTime>>) {
        result.subscribe((res: HttpResponse<TaskTrackingTime>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: TaskTrackingTime) {
        this.eventManager.broadcast({ name: 'taskTrackingTimeListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }
}

@Component({
    selector: 'jhi-task-tracking-time-popup',
    template: ''
})
export class TaskTrackingTimePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private taskTrackingTimePopupService: TaskTrackingTimePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.taskTrackingTimePopupService
                    .open(TaskTrackingTimeDialogComponent as Component, params['id']);
            } else {
                this.taskTrackingTimePopupService
                    .open(TaskTrackingTimeDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
