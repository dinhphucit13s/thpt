import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService, JhiDataUtils } from 'ng-jhipster';

import { TMSLogHistory } from './tms-log-history.model';
import { TMSLogHistoryPopupService } from './tms-log-history-popup.service';
import { TMSLogHistoryService } from './tms-log-history.service';
import { Projects, ProjectsService } from '../projects';
import { PurchaseOrders, PurchaseOrdersService } from '../purchase-orders';
import { Packages, PackagesService } from '../packages';
import { Tasks, TasksService } from '../tasks';

@Component({
    selector: 'jhi-tms-log-history-dialog',
    templateUrl: './tms-log-history-dialog.component.html'
})
export class TMSLogHistoryDialogComponent implements OnInit {

    tMSLogHistory: TMSLogHistory;
    isSaving: boolean;

    projects: Projects[];

    purchaseorders: PurchaseOrders[];

    packages: Packages[];

    tasks: Tasks[];

    constructor(
        public activeModal: NgbActiveModal,
        private dataUtils: JhiDataUtils,
        private jhiAlertService: JhiAlertService,
        private tMSLogHistoryService: TMSLogHistoryService,
        private projectsService: ProjectsService,
        private purchaseOrdersService: PurchaseOrdersService,
        private packagesService: PackagesService,
        private tasksService: TasksService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.projectsService.query()
            .subscribe((res: HttpResponse<Projects[]>) => { this.projects = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
        this.purchaseOrdersService.query()
            .subscribe((res: HttpResponse<PurchaseOrders[]>) => { this.purchaseorders = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
        this.packagesService.query()
            .subscribe((res: HttpResponse<Packages[]>) => { this.packages = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
        this.tasksService.query()
            .subscribe((res: HttpResponse<Tasks[]>) => { this.tasks = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
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
        if (this.tMSLogHistory.id !== undefined) {
            this.subscribeToSaveResponse(
                this.tMSLogHistoryService.update(this.tMSLogHistory));
        } else {
            this.subscribeToSaveResponse(
                this.tMSLogHistoryService.create(this.tMSLogHistory));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<TMSLogHistory>>) {
        result.subscribe((res: HttpResponse<TMSLogHistory>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: TMSLogHistory) {
        this.eventManager.broadcast({ name: 'tMSLogHistoryListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackProjectsById(index: number, item: Projects) {
        return item.id;
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
}

@Component({
    selector: 'jhi-tms-log-history-popup',
    template: ''
})
export class TMSLogHistoryPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private tMSLogHistoryPopupService: TMSLogHistoryPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.tMSLogHistoryPopupService
                    .open(TMSLogHistoryDialogComponent as Component, params['id']);
            } else {
                this.tMSLogHistoryPopupService
                    .open(TMSLogHistoryDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
