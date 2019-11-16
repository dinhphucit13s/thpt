import {Component, OnDestroy, OnInit} from '@angular/core';
import {JhiEventManager} from 'ng-jhipster';

import {TaskClonePopupService} from './task-clone-popup.service';
import {ActivatedRoute} from '@angular/router';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';
import {TaskBidding} from '../../task-bidding';
import {TaskBiddingService} from '../../task-bidding';
import {PackagesDialogComponent} from '../packages-dialog.component';

@Component({
    selector: 'jhi-task-clone-assign-dialog',
    templateUrl: './task-clone-assign-dialog.component.html'
})

export class TaskCloneDialogComponent {

    constructor(
        private taskBiddingService: TaskBiddingService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmCloneTasks() {
        this.eventManager.broadcast({
            name: 'CloneTaskOnPackages',
            content: 'Clone Task On Packages'
        });
        this.activeModal.dismiss(true);
    }
}

@Component({
    selector: 'jhi-task-clone-assign-popup-dialog',
    template: ''
})

export class TaskClonePopupDialogComponent implements OnInit, OnDestroy {
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private taskClonePopupService: TaskClonePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.taskClonePopupService
                .open(TaskCloneDialogComponent as Component);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
