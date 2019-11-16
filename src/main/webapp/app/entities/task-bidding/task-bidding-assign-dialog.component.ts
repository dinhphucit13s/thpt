import {Component, OnDestroy, OnInit} from '@angular/core';
import {JhiEventManager} from 'ng-jhipster';
import {TaskBidding} from './task-bidding.model';
import {TaskBiddingPopupService} from './task-bidding-popup.service';
import {ActivatedRoute} from '@angular/router';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';
import {TaskBiddingService} from './task-bidding.service';

@Component({
    selector: 'jhi-task-bidding-assign-dialog',
    templateUrl: './task-bidding-assign-dialog.component.html'
})

export class TaskBiddingAssignDialogComponent {
    taskBidding: TaskBidding[];

    constructor(
        private taskBiddingService: TaskBiddingService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmUnbidding() {
        this.eventManager.broadcast({
            name: 'taskBiddingModification',
            content: 'Bidding the taskBidding'
        });
        this.activeModal.dismiss(true);
    }
}

@Component({
    selector: 'jhi-task-bidding-assign-popup-dialog',
    template: ''
})

export class TaskBiddingAssignPopupDialogComponent implements OnInit, OnDestroy {
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private taskBiddingPopupService: TaskBiddingPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.taskBiddingPopupService
                .open(TaskBiddingAssignDialogComponent as Component);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
