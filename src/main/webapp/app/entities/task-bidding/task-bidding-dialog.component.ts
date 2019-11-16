import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { TaskBidding } from './task-bidding.model';
import { TaskBiddingPopupService } from './task-bidding-popup.service';
import { TaskBiddingService } from './task-bidding.service';
import { Tasks, TasksService } from '../tasks';

@Component({
    selector: 'jhi-task-bidding-dialog',
    templateUrl: './task-bidding-dialog.component.html'
})
export class TaskBiddingDialogComponent implements OnInit {

    taskBidding: TaskBidding;
    isSaving: boolean;

    tasks: Tasks[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private taskBiddingService: TaskBiddingService,
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
        if (this.taskBidding.id !== undefined) {
            this.subscribeToSaveResponse(
                this.taskBiddingService.update(this.taskBidding));
        } else {
            this.subscribeToSaveResponse(
                this.taskBiddingService.create(this.taskBidding));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<TaskBidding>>) {
        result.subscribe((res: HttpResponse<TaskBidding>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: TaskBidding) {
        this.eventManager.broadcast({ name: 'taskBiddingListModification', content: 'OK'});
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
    selector: 'jhi-task-bidding-popup',
    template: ''
})
export class TaskBiddingPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private taskBiddingPopupService: TaskBiddingPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.taskBiddingPopupService
                    .open(TaskBiddingDialogComponent as Component, params['id']);
            } else {
                this.taskBiddingPopupService
                    .open(TaskBiddingDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
