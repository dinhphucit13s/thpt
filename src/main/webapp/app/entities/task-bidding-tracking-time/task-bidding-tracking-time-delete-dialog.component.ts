import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { TaskBiddingTrackingTime } from './task-bidding-tracking-time.model';
import { TaskBiddingTrackingTimePopupService } from './task-bidding-tracking-time-popup.service';
import { TaskBiddingTrackingTimeService } from './task-bidding-tracking-time.service';

@Component({
    selector: 'jhi-task-bidding-tracking-time-delete-dialog',
    templateUrl: './task-bidding-tracking-time-delete-dialog.component.html'
})
export class TaskBiddingTrackingTimeDeleteDialogComponent {

    taskBiddingTrackingTime: TaskBiddingTrackingTime;

    constructor(
        private taskBiddingTrackingTimeService: TaskBiddingTrackingTimeService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.taskBiddingTrackingTimeService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'taskBiddingTrackingTimeListModification',
                content: 'Deleted an taskBiddingTrackingTime'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-task-bidding-tracking-time-delete-popup',
    template: ''
})
export class TaskBiddingTrackingTimeDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private taskBiddingTrackingTimePopupService: TaskBiddingTrackingTimePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.taskBiddingTrackingTimePopupService
                .open(TaskBiddingTrackingTimeDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
