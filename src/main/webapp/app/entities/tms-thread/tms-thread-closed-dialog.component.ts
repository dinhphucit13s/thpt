import {Component, OnDestroy, OnInit} from '@angular/core';
import {NgbActiveModal, NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {JhiEventManager} from 'ng-jhipster';
import {TmsThreadPopupService} from './tms-thread-popup.service';
import {TmsThreadDeleteDialogComponent} from './tms-thread-delete-dialog.component';
import {ActivatedRoute} from '@angular/router';

@Component({
    selector: 'jhi-thread-closed-dialog',
    templateUrl: './tms-thread-closed-dialog.component.html',
    styleUrls: ['./tms-thread-closed-dialog.component.css']
})
export class TmsThreadClosedDialogComponent {
    constructor(
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmChangeToClose() {
        this.eventManager.broadcast({
            name: 'tmsThreadClosedModification',
            content: 'Switch to Closed an tmsThread'
        });
        this.activeModal.dismiss(true);
    }
}

@Component({
    selector: 'jhi-tms-thread-closed-popup',
    template: ''
})

export class TmsThreadClosedPopupComponent implements OnInit, OnDestroy {
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private tmsThreadPopupService: TmsThreadPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.tmsThreadPopupService
                .open(TmsThreadClosedDialogComponent as Component);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }

}
