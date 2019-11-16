import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { TaskBiddingTrackingTime } from './task-bidding-tracking-time.model';
import { TaskBiddingTrackingTimePopupService } from './task-bidding-tracking-time-popup.service';
import { TaskBiddingTrackingTimeService } from './task-bidding-tracking-time.service';
import { Tasks, TasksService } from '../tasks';

@Component({
    selector: 'jhi-task-bidding-tracking-time-dialog',
    templateUrl: './task-bidding-tracking-time-dialog.component.html'
})
export class TaskBiddingTrackingTimeDialogComponent implements OnInit {

    taskBiddingTrackingTime: TaskBiddingTrackingTime;
    isSaving: boolean;

    tasks: Tasks[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private taskBiddingTrackingTimeService: TaskBiddingTrackingTimeService,
        private tasksService: TasksService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.tasksService.query()
            .subscribe((res: HttpResponse<Tasks[]>) => { this.tasks = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.taskBiddingTrackingTime.id !== undefined) {
            this.subscribeToSaveResponse(
                this.taskBiddingTrackingTimeService.update(this.taskBiddingTrackingTime));
        } else {
            this.subscribeToSaveResponse(
                this.taskBiddingTrackingTimeService.create(this.taskBiddingTrackingTime));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<TaskBiddingTrackingTime>>) {
        result.subscribe((res: HttpResponse<TaskBiddingTrackingTime>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: TaskBiddingTrackingTime) {
        this.eventManager.broadcast({ name: 'taskBiddingTrackingTimeListModification', content: 'OK'});
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
    selector: 'jhi-task-bidding-tracking-time-popup',
    template: ''
})
export class TaskBiddingTrackingTimePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private taskBiddingTrackingTimePopupService: TaskBiddingTrackingTimePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.taskBiddingTrackingTimePopupService
                    .open(TaskBiddingTrackingTimeDialogComponent as Component, params['id']);
            } else {
                this.taskBiddingTrackingTimePopupService
                    .open(TaskBiddingTrackingTimeDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
