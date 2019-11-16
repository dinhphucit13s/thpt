import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService, JhiDataUtils } from 'ng-jhipster';

import { TMSCustomFieldScreenValue } from './tms-custom-field-screen-value.model';
import { TMSCustomFieldScreenValuePopupService } from './tms-custom-field-screen-value-popup.service';
import { TMSCustomFieldScreenValueService } from './tms-custom-field-screen-value.service';
import { PurchaseOrders, PurchaseOrdersService } from '../purchase-orders';
import { Packages, PackagesService } from '../packages';
import { Tasks, TasksService } from '../tasks';
import { TMSCustomField, TMSCustomFieldService } from '../tms-custom-field';

@Component({
    selector: 'jhi-tms-custom-field-screen-value-dialog',
    templateUrl: './tms-custom-field-screen-value-dialog.component.html'
})
export class TMSCustomFieldScreenValueDialogComponent implements OnInit {

    tMSCustomFieldScreenValue: TMSCustomFieldScreenValue;
    isSaving: boolean;

    purchaseorders: PurchaseOrders[];

    packages: Packages[];

    tasks: Tasks[];

    tmscustomfields: TMSCustomField[];

    constructor(
        public activeModal: NgbActiveModal,
        private dataUtils: JhiDataUtils,
        private jhiAlertService: JhiAlertService,
        private tMSCustomFieldScreenValueService: TMSCustomFieldScreenValueService,
        private purchaseOrdersService: PurchaseOrdersService,
        private packagesService: PackagesService,
        private tasksService: TasksService,
        private tMSCustomFieldService: TMSCustomFieldService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.purchaseOrdersService.query()
            .subscribe((res: HttpResponse<PurchaseOrders[]>) => { this.purchaseorders = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
        this.packagesService.query()
            .subscribe((res: HttpResponse<Packages[]>) => { this.packages = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
        this.tasksService.query()
            .subscribe((res: HttpResponse<Tasks[]>) => { this.tasks = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
        this.tMSCustomFieldService.query()
            .subscribe((res: HttpResponse<TMSCustomField[]>) => { this.tmscustomfields = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
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
        if (this.tMSCustomFieldScreenValue.id !== undefined) {
            this.subscribeToSaveResponse(
                this.tMSCustomFieldScreenValueService.update(this.tMSCustomFieldScreenValue));
        } else {
            this.subscribeToSaveResponse(
                this.tMSCustomFieldScreenValueService.create(this.tMSCustomFieldScreenValue));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<TMSCustomFieldScreenValue>>) {
        result.subscribe((res: HttpResponse<TMSCustomFieldScreenValue>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: TMSCustomFieldScreenValue) {
        this.eventManager.broadcast({ name: 'tMSCustomFieldScreenValueListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackPurchaseOrdersById(index: number, item: PurchaseOrders) {
        return item.id;
    }

    trackPackagesById(index: number, item: Packages) {
        return item.id;
    }

    trackTasksById(index: number, item: Tasks) {
        return item.id;
    }

    trackTMSCustomFieldById(index: number, item: TMSCustomField) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-tms-custom-field-screen-value-popup',
    template: ''
})
export class TMSCustomFieldScreenValuePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private tMSCustomFieldScreenValuePopupService: TMSCustomFieldScreenValuePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.tMSCustomFieldScreenValuePopupService
                    .open(TMSCustomFieldScreenValueDialogComponent as Component, params['id']);
            } else {
                this.tMSCustomFieldScreenValuePopupService
                    .open(TMSCustomFieldScreenValueDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
