import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { TMSCustomFieldScreenValue } from './tms-custom-field-screen-value.model';
import { TMSCustomFieldScreenValuePopupService } from './tms-custom-field-screen-value-popup.service';
import { TMSCustomFieldScreenValueService } from './tms-custom-field-screen-value.service';

@Component({
    selector: 'jhi-tms-custom-field-screen-value-delete-dialog',
    templateUrl: './tms-custom-field-screen-value-delete-dialog.component.html'
})
export class TMSCustomFieldScreenValueDeleteDialogComponent {

    tMSCustomFieldScreenValue: TMSCustomFieldScreenValue;

    constructor(
        private tMSCustomFieldScreenValueService: TMSCustomFieldScreenValueService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.tMSCustomFieldScreenValueService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'tMSCustomFieldScreenValueListModification',
                content: 'Deleted an tMSCustomFieldScreenValue'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-tms-custom-field-screen-value-delete-popup',
    template: ''
})
export class TMSCustomFieldScreenValueDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private tMSCustomFieldScreenValuePopupService: TMSCustomFieldScreenValuePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.tMSCustomFieldScreenValuePopupService
                .open(TMSCustomFieldScreenValueDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
