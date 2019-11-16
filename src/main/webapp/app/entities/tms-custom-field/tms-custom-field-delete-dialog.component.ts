import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { TMSCustomField } from './tms-custom-field.model';
import { TMSCustomFieldPopupService } from './tms-custom-field-popup.service';
import { TMSCustomFieldService } from './tms-custom-field.service';
import {HttpErrorResponse} from '@angular/common/http';

@Component({
    selector: 'jhi-tms-custom-field-delete-dialog',
    templateUrl: './tms-custom-field-delete-dialog.component.html'
})
export class TMSCustomFieldDeleteDialogComponent {

    tMSCustomField: TMSCustomField;

    constructor(
        private tMSCustomFieldService: TMSCustomFieldService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }
    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.tMSCustomFieldService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'tMSCustomFieldListModification',
                content: 'Deleted an tMSCustomField'
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
    selector: 'jhi-tms-custom-field-delete-popup',
    template: ''
})
export class TMSCustomFieldDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private tMSCustomFieldPopupService: TMSCustomFieldPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.tMSCustomFieldPopupService
                .open(TMSCustomFieldDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
