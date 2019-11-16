import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Issues } from './issues.model';
import { IssuesPopupService } from './issues-popup.service';
import { IssuesService } from './issues.service';
import { PurchaseOrders, PurchaseOrdersService } from '../purchase-orders';
import { Projects, ProjectsService } from '../projects';

@Component({
    selector: 'jhi-issues-dialog',
    templateUrl: './issues-dialog.component.html'
})
export class IssuesDialogComponent implements OnInit {

    issues: Issues;
    isSaving: boolean;

    purchaseorders: PurchaseOrders[];

    projects: Projects[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private issuesService: IssuesService,
        private purchaseOrdersService: PurchaseOrdersService,
        private projectsService: ProjectsService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.purchaseOrdersService.query()
            .subscribe((res: HttpResponse<PurchaseOrders[]>) => { this.purchaseorders = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
        this.projectsService.query()
            .subscribe((res: HttpResponse<Projects[]>) => { this.projects = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.issues.id !== undefined) {
            this.subscribeToSaveResponse(
                this.issuesService.update(this.issues));
        } else {
            this.subscribeToSaveResponse(
                this.issuesService.create(this.issues));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<Issues>>) {
        result.subscribe((res: HttpResponse<Issues>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: Issues) {
        this.eventManager.broadcast({ name: 'issuesListModification', content: 'OK'});
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

    trackProjectsById(index: number, item: Projects) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-issues-popup',
    template: ''
})
export class IssuesPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private issuesPopupService: IssuesPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.issuesPopupService
                    .open(IssuesDialogComponent as Component, params['id']);
            } else {
                this.issuesPopupService
                    .open(IssuesDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
