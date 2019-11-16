import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiDataUtils } from 'ng-jhipster';

import { BusinessLine } from './business-line.model';
import { BusinessLinePopupService } from './business-line-popup.service';
import { BusinessLineService } from './business-line.service';

@Component({
    selector: 'jhi-business-line-dialog',
    templateUrl: './business-line-dialog.component.html'
})
export class BusinessLineDialogComponent implements OnInit {

    businessLine: BusinessLine;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private dataUtils: JhiDataUtils,
        private businessLineService: BusinessLineService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }

    setFileData(event, entity, field, isImage) {
        this.dataUtils.setFileData(event, entity, field, isImage);
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.businessLine.id !== undefined) {
            this.subscribeToSaveResponse(
                this.businessLineService.update(this.businessLine));
        } else {
            this.subscribeToSaveResponse(
                this.businessLineService.create(this.businessLine));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<BusinessLine>>) {
        result.subscribe((res: HttpResponse<BusinessLine>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: BusinessLine) {
        this.eventManager.broadcast({ name: 'businessLineListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }
}

@Component({
    selector: 'jhi-business-line-popup',
    template: ''
})
export class BusinessLinePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private businessLinePopupService: BusinessLinePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.businessLinePopupService
                    .open(BusinessLineDialogComponent as Component, params['id']);
            } else {
                this.businessLinePopupService
                    .open(BusinessLineDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
