import {Component, OnDestroy, OnInit} from '@angular/core';
import {JhiEventManager} from 'ng-jhipster';
import {TaskBidding} from '../task-bidding.model';
import {TaskBiddingPopupService} from '../task-bidding-popup.service';
import {ActivatedRoute} from '@angular/router';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';
import {TaskBiddingService} from '../task-bidding.service';
import {TaskBiddingDeleteDialogComponent} from '../task-bidding-delete-dialog.component';

@Component({
    selector: 'jhi-task-bidding-classify-dialog-unbidding',
    templateUrl: './task-bidding-classify-dialog-unbidding.component.html'
})

export class TaskBiddingClassifyDialogUnbiddingComponent {
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

    confirmUnbidding() {
        this.eventManager.broadcast({
            name: 'taskUnBiddingModification',
            content: 'UnBidding the taskBidding'
        });
        this.activeModal.dismiss(true);
    }
}

@Component({
    selector: 'jhi-task-bidding-classify-popup-unbidding',
    template: ''
})
export class TaskBiddingClassifyPopupUnbiddingComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private taskBiddingPopupService: TaskBiddingPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.taskBiddingPopupService
                .open(TaskBiddingClassifyDialogUnbiddingComponent as Component);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
