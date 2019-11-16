import {Component, OnInit, OnDestroy, AfterViewInit} from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { BugListDefault } from './bug-list-default.model';
import { BugListDefaultPopupService } from './bug-list-default-popup.service';
import { BugListDefaultService } from './bug-list-default.service';
import { BusinessLine, BusinessLineService } from '../business-line';
declare var jquery: any;
declare var $: any;

@Component({
    selector: 'jhi-bug-list-default-dialog',
    templateUrl: './bug-list-default-dialog.component.html'
})
export class BugListDefaultDialogComponent implements OnInit, AfterViewInit {

    bugListDefault: BugListDefault;
    isSaving: boolean;
    businessLines: BusinessLine[];

    selectedBL: any;
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
        private jhiAlertService: JhiAlertService,
        private bugListDefaultService: BugListDefaultService,
        private businessLineService: BusinessLineService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        console.log(this.bugListDefault.description);
        this.businessLineService.query({size: 100000000})
            .subscribe(
                (res: HttpResponse<BusinessLine[]>) => {
                    this.businessLines = res.body;
                    },
                (res: HttpErrorResponse) => this.onError(res.message));
        if (this.bugListDefault.id) {
            this.selectedBL = [{id: this.bugListDefault.businessLineId, name: this.bugListDefault.businessLineName}];
        }
    }

    ngAfterViewInit(): void {
        $('#field_description').keydown(function( event ) {
            if ($('#field_description').val().length < 1 && event.which === 32) {
                event.preventDefault();
            }
        });
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.bugListDefault.description) {
            this.bugListDefault.description = this.bugListDefault.description.trim();
        }
        if (this.bugListDefault.id !== undefined) {
            this.subscribeToSaveResponse(
                this.bugListDefaultService.update(this.bugListDefault));
        } else {
            this.subscribeToSaveResponse(
                this.bugListDefaultService.create(this.bugListDefault));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<BugListDefault>>) {
        result.subscribe((res: HttpResponse<BugListDefault>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: BugListDefault) {
        this.eventManager.broadcast({ name: 'bugListDefaultListModification', content: 'OK'});
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
        this.bugListDefault.businessLineId = item.id;
        this.checkBusinessLine = true;
    }

    onDeSelectBL(item: any) {
        this.bugListDefault.businessLineId = null;
        this.checkBusinessLine = false;
    }
}

@Component({
    selector: 'jhi-bug-list-default-popup',
    template: ''
})
export class BugListDefaultPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private bugListDefaultPopupService: BugListDefaultPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.bugListDefaultPopupService
                    .open(BugListDefaultDialogComponent as Component, params['id']);
            } else {
                this.bugListDefaultPopupService
                    .open(BugListDefaultDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
