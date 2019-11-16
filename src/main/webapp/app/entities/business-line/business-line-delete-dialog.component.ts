import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { BusinessLine } from './business-line.model';
import { BusinessLinePopupService } from './business-line-popup.service';
import { BusinessLineService } from './business-line.service';
import {HttpErrorResponse} from "@angular/common/http";

@Component({
    selector: 'jhi-business-line-delete-dialog',
    templateUrl: './business-line-delete-dialog.component.html'
})
export class BusinessLineDeleteDialogComponent {

    businessLine: BusinessLine;

    constructor(
        private businessLineService: BusinessLineService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.businessLineService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'businessLineListModification',
                content: 'Deleted an businessLine'
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
    selector: 'jhi-business-line-delete-popup',
    template: ''
})
export class BusinessLineDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private businessLinePopupService: BusinessLinePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.businessLinePopupService
                .open(BusinessLineDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
