import {Component, OnInit, OnDestroy, EventEmitter} from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { TMSCustomFieldScreen } from './tms-custom-field-screen.model';
import { TMSCustomFieldScreenPopupService } from './tms-custom-field-screen-popup.service';
import { TMSCustomFieldScreenService } from './tms-custom-field-screen.service';
import {HttpErrorResponse} from '@angular/common/http';

@Component({
    selector: 'jhi-tms-custom-field-screen-delete-dialog',
    templateUrl: './tms-custom-field-screen-delete-dialog.component.html'
})
export class TMSCustomFieldScreenDeleteDialogComponent {
    closePopup = new EventEmitter();
    onDelete = new EventEmitter();
    tMSCustomFieldScreen: TMSCustomFieldScreen;
    constructor(
        private tMSCustomFieldScreenService: TMSCustomFieldScreenService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }
    clear() {
        this.closePopup.emit();
        this.activeModal.dismiss('cancel');
    }
    errorMessage(res) {
        this.eventManager.broadcast({
            name: 'dtmsApp.httpError',
            content: res
        });
    }
    confirmDelete(id: number) {
        this.onDelete.emit(this.tMSCustomFieldScreen);
    }
}

/*@Component({
    selector: 'jhi-tms-custom-field-screen-delete-popup',
    template: ''
})
export class TMSCustomFieldScreenDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private tMSCustomFieldScreenPopupService: TMSCustomFieldScreenPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if (params['id']) {
                this.tMSCustomFieldScreenPopupService
                    .open(TMSCustomFieldScreenDeleteDialogComponent as Component, params['id']);
            } else {
                this.tMSCustomFieldScreenPopupService
                    .open(TMSCustomFieldScreenDeleteDialogComponent as Component, undefined);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}*/
