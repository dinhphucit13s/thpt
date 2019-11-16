import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { BusinessUnitManager } from './business-unit-manager.model';
import { BusinessUnitManagerPopupService } from './business-unit-manager-popup.service';
import { BusinessUnitManagerService } from './business-unit-manager.service';
import { BusinessUnit, BusinessUnitService } from '../business-unit';
import { User, UserService } from '../../shared';

@Component({
    selector: 'jhi-business-unit-manager-dialog',
    templateUrl: './business-unit-manager-dialog.component.html'
})
export class BusinessUnitManagerDialogComponent implements OnInit {

    businessUnitManager: BusinessUnitManager;
    isSaving: boolean;
    businessunits: BusinessUnit[];
    users: User[];
    dateFormat = require('dateformat');
    dropdownSettings = {
        singleSelection: true,
        idField: 'id',
        textField: 'login',
        allowSearchFilter: true,
        enableCheckAll: false,
        'closeDropDownOnSelection': false
    };
    dropdownSettingsBU = {
        singleSelection: true,
        idField: 'id',
        textField: 'name',
        allowSearchFilter: true,
        enableCheckAll: false,
        'closeDropDownOnSelection': false
    };

    selectedItems = [];
    selectedBU: any;

    checkManager = true;
    checkBusinessUnit = true;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private businessUnitManagerService: BusinessUnitManagerService,
        private businessUnitService: BusinessUnitService,
        private userService: UserService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.businessUnitService.query({size: 100000000})
            .subscribe(
                (res: HttpResponse<BusinessUnit[]>) => {
                        this.businessunits = res.body;
                        if (this.businessUnitManager.id) {
                            this.selectedBU  = [{id: this.businessUnitManager.businessUnitId, name: this.businessUnitManager.businessUnitName}];
                        }
                    },
                (res: HttpErrorResponse) => this.onError(res.message));
        this.userService.getAllUsers()
            .subscribe(
                (res: HttpResponse<User[]>) => this.onSuccess(res.body, res.headers),
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    private onSuccess(data, headers) {
        this.users = data;
        this.users = this.users.filter( user => user.activated);

        if (this.businessUnitManager.id) {
            this.selectedItems  = [{id: this.businessUnitManager.managerId, login: this.businessUnitManager.managerLogin}];
        }
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        if (this.businessUnitManager.startTime !== undefined) {
            this.businessUnitManager.startTime = this.dateFormat(this.businessUnitManager.startTime, `yyyy-mm-dd'T'HH:MM:ss`);
        }
        if (this.businessUnitManager.endTime !== undefined) {
            this.businessUnitManager.endTime = this.dateFormat(this.businessUnitManager.endTime, `yyyy-mm-dd'T'HH:MM:ss`);
        }
        this.isSaving = true;
        if (this.businessUnitManager.id !== undefined) {
            this.subscribeToSaveResponse(
                this.businessUnitManagerService.update(this.businessUnitManager));
        } else {
            this.subscribeToSaveResponse(
                this.businessUnitManagerService.create(this.businessUnitManager));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<BusinessUnitManager>>) {
        result.subscribe((res: HttpResponse<BusinessUnitManager>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: BusinessUnitManager) {
        this.eventManager.broadcast({ name: 'businessUnitManagerListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackBusinessUnitById(index: number, item: BusinessUnit) {
        return item.id;
    }

    trackUserById(index: number, item: User) {
        return item.id;
    }

    onItemSelect(item: any) {
        this.businessUnitManager.managerId = item.id;
        this.checkManager = true;
    }

    onDeSelect(item: any) {
        this.businessUnitManager.managerId = null;
        this.checkManager = false;
    }

    onItemSelectBU(item: any) {
        this.businessUnitManager.businessUnitId = item.id;
        this.checkBusinessUnit = true;
    }

    onDeSelectBU(item: any) {
        this.businessUnitManager.businessUnitId = null;
        this.checkBusinessUnit = false;
    }
}

@Component({
    selector: 'jhi-business-unit-manager-popup',
    template: ''
})
export class BusinessUnitManagerPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private businessUnitManagerPopupService: BusinessUnitManagerPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.businessUnitManagerPopupService
                    .open(BusinessUnitManagerDialogComponent as Component, params['id']);
            } else {
                this.businessUnitManagerPopupService
                    .open(BusinessUnitManagerDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
