import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { TaskBidding } from './task-bidding.model';
import { TaskBiddingPopupService } from './task-bidding-popup.service';
import { TaskBiddingService } from './task-bidding.service';

@Component({
    selector: 'jhi-task-bidding-delete-dialog',
    templateUrl: './task-bidding-delete-dialog.component.html'
})
export class TaskBiddingDeleteDialogComponent {

    taskBidding: TaskBidding;

    constructor(
        private taskBiddingService: TaskBiddingService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.taskBiddingService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'taskBiddingListModification',
                content: 'Deleted an taskBidding'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-task-bidding-delete-popup',
    template: ''
})
export class TaskBiddingDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private taskBiddingPopupService: TaskBiddingPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.taskBiddingPopupService
                .open(TaskBiddingDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
