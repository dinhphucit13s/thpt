import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { TmsPost } from './tms-post.model';
import { TmsPostPopupService } from './tms-post-popup.service';
import { TmsPostService } from './tms-post.service';

@Component({
    selector: 'jhi-tms-post-delete-dialog',
    templateUrl: './tms-post-delete-dialog.component.html'
})
export class TmsPostDeleteDialogComponent {

    tmsPost: TmsPost;

    constructor(
        private tmsPostService: TmsPostService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.tmsPostService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'tmsPostListModification',
                content: 'Deleted an tmsPost'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-tms-post-delete-popup',
    template: ''
})
export class TmsPostDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private tmsPostPopupService: TmsPostPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.tmsPostPopupService
                .open(TmsPostDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
