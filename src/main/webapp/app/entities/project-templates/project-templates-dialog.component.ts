import { Component, OnInit, OnDestroy, ElementRef } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService, JhiDataUtils } from 'ng-jhipster';

import { ProjectTemplates } from './project-templates.model';
import { ProjectTemplatesPopupService } from './project-templates-popup.service';
import { ProjectTemplatesService } from './project-templates.service';
import { BusinessLine, BusinessLineService } from '../business-line';
import { Ng2ImgMaxService } from 'ng2-img-max';

@Component({
    selector: 'jhi-project-templates-dialog',
    templateUrl: './project-templates-dialog.component.html'
})
export class ProjectTemplatesDialogComponent implements OnInit {

    projectTemplates: ProjectTemplates;
    isSaving: boolean;

    businesslines: BusinessLine[];

    selectedBusinessLine: any;
    dropdownSettingsBL = {
        singleSelection: true,
        idField: 'id',
        textField: 'name',
        allowSearchFilter: true,
        enableCheckAll: false,
        'closeDropDownOnSelection': false
    };
    checkBusinessLine = true;

    constructor(
        public activeModal: NgbActiveModal,
        private dataUtils: JhiDataUtils,
        private jhiAlertService: JhiAlertService,
        private projectTemplatesService: ProjectTemplatesService,
        private businessLineService: BusinessLineService,
        private elementRef: ElementRef,
        private eventManager: JhiEventManager,
        private ng2ImgMax: Ng2ImgMaxService
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.businessLineService.query({size: 100000000})
            .subscribe((res: HttpResponse<BusinessLine[]>) => { this.businesslines = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
        if (this.projectTemplates.id) {
            this.selectedBusinessLine = [{id: this.projectTemplates.businessLineId, name: this.projectTemplates.businessLineName}];
        }
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }
    setFileData(event, entity, field, isImage) {
        const image = event.target.files[0];
        this.ng2ImgMax.resizeImage(image, 200, 200).subscribe(
            (result) => {
                const uploadedImage = new File([result], result.name);
                this.dataUtils.toBase64(uploadedImage, function(base64Data) {
                    entity[field] = base64Data;

                });
                entity[field + 'ContentType'] = result.type;
            },
            (error) => {
                console.log('Error!!!', error);
            }
        );
    }

    clearInputImage(field: string, fieldContentType: string, idInput: string) {
        this.dataUtils.clearInputImage(this.projectTemplates, this.elementRef, field, fieldContentType, idInput);
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.projectTemplates.id !== undefined) {
            this.subscribeToSaveResponse(
                this.projectTemplatesService.update(this.projectTemplates));
        } else {
            this.subscribeToSaveResponse(
                this.projectTemplatesService.create(this.projectTemplates));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ProjectTemplates>>) {
        result.subscribe((res: HttpResponse<ProjectTemplates>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: ProjectTemplates) {
        this.eventManager.broadcast({ name: 'projectTemplatesListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackBusinessLineById(index: number, item: BusinessLine) {
        return item.id;
    }

    onItemSelectBL(item: any) {
        this.projectTemplates.businessLineId = item.id;
        this.checkBusinessLine = true;
    }

    onDeSelectBL(item: any) {
        this.projectTemplates.businessLineId = null;
        this.checkBusinessLine = false;
    }
}

@Component({
    selector: 'jhi-project-templates-popup',
    template: ''
})
export class ProjectTemplatesPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private projectTemplatesPopupService: ProjectTemplatesPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.projectTemplatesPopupService
                    .open(ProjectTemplatesDialogComponent as Component, params['id']);
            } else {
                this.projectTemplatesPopupService
                    .open(ProjectTemplatesDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
