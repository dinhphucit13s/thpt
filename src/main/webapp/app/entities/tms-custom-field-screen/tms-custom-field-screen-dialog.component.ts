import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { TMSCustomFieldScreen } from './tms-custom-field-screen.model';
import { TMSCustomFieldScreenPopupService } from './tms-custom-field-screen-popup.service';
import { TMSCustomFieldScreenService } from './tms-custom-field-screen.service';
import { TMSCustomField, TMSCustomFieldService } from '../tms-custom-field';
import { ProjectWorkflows, ProjectWorkflowsService } from '../project-workflows';

@Component({
    selector: 'jhi-tms-custom-field-screen-dialog',
    templateUrl: './tms-custom-field-screen-dialog.component.html'
})
export class TMSCustomFieldScreenDialogComponent implements OnInit {

    tMSCustomFieldScreen: TMSCustomFieldScreen;
    isSaving: boolean;

    tmscustomfields: TMSCustomField[];

    projectworkflows: ProjectWorkflows[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private tMSCustomFieldScreenService: TMSCustomFieldScreenService,
        private tMSCustomFieldService: TMSCustomFieldService,
        private projectWorkflowsService: ProjectWorkflowsService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.tMSCustomFieldService
            .query({filter: 'tmscustomfieldscreen-is-null'})
            .subscribe((res: HttpResponse<TMSCustomField[]>) => {
                if (!this.tMSCustomFieldScreen.tmsCustomFieldId) {
                    this.tmscustomfields = res.body;
                } else {
                    this.tMSCustomFieldService
                        .find(this.tMSCustomFieldScreen.tmsCustomFieldId)
                        .subscribe((subRes: HttpResponse<TMSCustomField>) => {
                            this.tmscustomfields = [subRes.body].concat(res.body);
                        }, (subRes: HttpErrorResponse) => this.onError(subRes.message));
                }
            }, (res: HttpErrorResponse) => this.onError(res.message));
        this.projectWorkflowsService.query()
            .subscribe((res: HttpResponse<ProjectWorkflows[]>) => { this.projectworkflows = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.tMSCustomFieldScreen.id !== undefined) {
            this.subscribeToSaveResponse(
                this.tMSCustomFieldScreenService.update(this.tMSCustomFieldScreen));
        } else {
            this.subscribeToSaveResponse(
                this.tMSCustomFieldScreenService.create(this.tMSCustomFieldScreen));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<TMSCustomFieldScreen>>) {
        result.subscribe((res: HttpResponse<TMSCustomFieldScreen>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: TMSCustomFieldScreen) {
        this.eventManager.broadcast({ name: 'tMSCustomFieldScreenListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackTMSCustomFieldById(index: number, item: TMSCustomField) {
        return item.id;
    }

    trackProjectWorkflowsById(index: number, item: ProjectWorkflows) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-tms-custom-field-screen-popup',
    template: ''
})
export class TMSCustomFieldScreenPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private tMSCustomFieldScreenPopupService: TMSCustomFieldScreenPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.tMSCustomFieldScreenPopupService
                    .open(TMSCustomFieldScreenDialogComponent as Component, params['id']);
            } else {
                this.tMSCustomFieldScreenPopupService
                    .open(TMSCustomFieldScreenDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
