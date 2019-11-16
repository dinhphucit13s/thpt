import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Bugs } from './bugs.model';
import { BugsPopupService } from './bugs-popup.service';
import { BugsService } from './bugs.service';

@Component({
    selector: 'jhi-bugs-delete-dialog',
    templateUrl: './bugs-delete-dialog.component.html'
})
export class BugsDeleteDialogComponent {

    bugs: Bugs;

    constructor(
        private bugsService: BugsService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.bugsService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'bugsListModification',
                content: 'Deleted an bugs'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-bugs-delete-popup',
    template: ''
})
export class BugsDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private bugsPopupService: BugsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.bugsPopupService
                .open(BugsDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
