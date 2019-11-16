import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { TaskTrackingTime } from './task-tracking-time.model';
import { TaskTrackingTimePopupService } from './task-tracking-time-popup.service';
import { TaskTrackingTimeService } from './task-tracking-time.service';
import {HttpErrorResponse} from "@angular/common/http";

@Component({
    selector: 'jhi-task-tracking-time-delete-dialog',
    templateUrl: './task-tracking-time-delete-dialog.component.html'
})
export class TaskTrackingTimeDeleteDialogComponent {

    taskTrackingTime: TaskTrackingTime;

    constructor(
        private taskTrackingTimeService: TaskTrackingTimeService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.taskTrackingTimeService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'taskTrackingTimeListModification',
                content: 'Deleted an taskTrackingTime'
            });
            this.activeModal.dismiss(true);
        }, (res: HttpErrorResponse) => {
            this.eventManager.broadcast({
                name: 'dtmsApp.httpError',
                content: res
            });
        });
    }
}

@Component({
    selector: 'jhi-task-tracking-time-delete-popup',
    template: ''
})
export class TaskTrackingTimeDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private taskTrackingTimePopupService: TaskTrackingTimePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.taskTrackingTimePopupService
                .open(TaskTrackingTimeDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
