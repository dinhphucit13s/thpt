import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { BusinessUnit } from './business-unit.model';
import { BusinessUnitPopupService } from './business-unit-popup.service';
import { BusinessUnitService } from './business-unit.service';

@Component({
    selector: 'jhi-business-unit-dialog',
    templateUrl: './business-unit-dialog.component.html'
})
export class BusinessUnitDialogComponent implements OnInit {

    businessUnit: BusinessUnit;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private businessUnitService: BusinessUnitService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.businessUnit.id !== undefined) {
            this.subscribeToSaveResponse(
                this.businessUnitService.update(this.businessUnit));
        } else {
            this.subscribeToSaveResponse(
                this.businessUnitService.create(this.businessUnit));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<BusinessUnit>>) {
        result.subscribe((res: HttpResponse<BusinessUnit>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: BusinessUnit) {
        this.eventManager.broadcast({ name: 'businessUnitListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }
}

@Component({
    selector: 'jhi-business-unit-popup',
    template: ''
})
export class BusinessUnitPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private businessUnitPopupService: BusinessUnitPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.businessUnitPopupService
                    .open(BusinessUnitDialogComponent as Component, params['id']);
            } else {
                this.businessUnitPopupService
                    .open(BusinessUnitDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
