import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { BusinessUnitManager } from './business-unit-manager.model';
import { BusinessUnitManagerPopupService } from './business-unit-manager-popup.service';
import { BusinessUnitManagerService } from './business-unit-manager.service';
import {HttpErrorResponse} from "@angular/common/http";

@Component({
    selector: 'jhi-business-unit-manager-delete-dialog',
    templateUrl: './business-unit-manager-delete-dialog.component.html'
})
export class BusinessUnitManagerDeleteDialogComponent {

    businessUnitManager: BusinessUnitManager;
    errorMessage: any;
    constructor(
        private businessUnitManagerService: BusinessUnitManagerService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.businessUnitManagerService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'businessUnitManagerListModification',
                content: 'Deleted an businessUnitManager'
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
    selector: 'jhi-business-unit-manager-delete-popup',
    template: ''
})
export class BusinessUnitManagerDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private businessUnitManagerPopupService: BusinessUnitManagerPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.businessUnitManagerPopupService
                .open(BusinessUnitManagerDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
