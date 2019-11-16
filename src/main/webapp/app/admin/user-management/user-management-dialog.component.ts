import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { DatePipe } from '@angular/common';

import { NgbActiveModal, NgbDateParserFormatter } from '@ng-bootstrap/ng-bootstrap';
import {JhiAlertService, JhiDateUtils, JhiEventManager} from 'ng-jhipster';

import { UserModalService } from './user-modal.service';
import { JhiLanguageHelper, User, UserService } from '../../shared';
import { NgbDateCustomParserFormatter} from '../../shared/services/NgbDateCustomParserFormatter';
import {AppConstants} from '../../shared/services/app-constants';
import {BusinessUnit, BusinessUnitService} from '../../entities/business-unit';
import { TimeZones } from './timezones.model';
import { TimeZoneService} from './timezones.service';
import {HttpErrorResponse, HttpResponse} from '@angular/common/http';

@Component({
    selector: 'jhi-user-mgmt-dialog',
    templateUrl: './user-management-dialog.component.html',
    providers: [
        {provide: NgbDateParserFormatter, useClass: NgbDateCustomParserFormatter}
    ]
})
export class UserMgmtDialogComponent implements OnInit {
    user: User;
    businessUnit: BusinessUnit[];
    _columnDef: any;
    _rowData: any;
    _rowSelected: any;
    languages: any[];
    timeZones: TimeZones[];
    dateFormat = require('dateformat');
    authorities: any[];
    isSaving: Boolean;
    confirmPassword: string;
    minDob: Date = new Date('January 01, 1900 00:00:00');
    isExistEmail = false;
    constructor(
        public activeModal: NgbActiveModal,
        private languageHelper: JhiLanguageHelper,
        private userService: UserService,
        private businessUnitService: BusinessUnitService,
        private eventManager: JhiEventManager,
        private datePipe: DatePipe,
        private dateUtils: JhiDateUtils,
        private jhiAlertService: JhiAlertService,
        private timeZoneService: TimeZoneService,
    ) {}

    ngOnInit() {
        this._columnDef = AppConstants.RolesItems;
        this._rowSelected = this.user.authorities;
        this.isSaving = false;
        this.isExistEmail = false;
        this.authorities = [];
        this.userService.authorities().subscribe((authorities) => {
            this.authorities = authorities;
            this._rowData = authorities;
        });
        this.languageHelper.getAll().then((languages) => {
            this.languages = languages;
        });
        this.businessUnitService.query()
            .subscribe((res: HttpResponse<BusinessUnit[]>) => { this.businessUnit = res.body; });

        this.timeZoneService.query().subscribe(
        (res: HttpResponse<TimeZones[]>) => {
               this.timeZones = res.body;
               console.log('TimeZone' + JSON.stringify(this.timeZones));
        });
    }

    loadAuthorities(roles?: any) {
        this.user.authorities = new Array();
        roles.forEach((role) => {
            this.user.authorities.push(role.name);
        });
    }

    trackBusinessUnitById(index: number, item: BusinessUnit) {
        return item.id;
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    timeZoneChanges() {
        console.log(this.user.userProfile.timezoneId);
    }
    save() {
        this.isSaving = true;
        const payloadUserClone = Object.assign({}, this.user);
        payloadUserClone.dob = this.dateUtils
            .convertLocalDateFromServer([payloadUserClone.dob.year, payloadUserClone.dob.month, payloadUserClone.dob.day].join('-'));
        if (payloadUserClone.id !== null) {
            this.userService.update(payloadUserClone).subscribe((response) => this.onSaveSuccess(response), (err: HttpErrorResponse) => this.onSaveError(err));
        } else {
            this.userService.create(payloadUserClone).subscribe((response) => this.onSaveSuccess(response), (err: HttpErrorResponse) => this.onSaveError(err));
        }
    }

    private onSaveSuccess(result) {
        this.eventManager.broadcast({ name: 'userListModification', content: 'OK' });
        this.isSaving = false;
        this.activeModal.dismiss(result.body);
    }

    private onSaveError(err: any) {
        this.isSaving = false;
        if (err.error.errorKey === 'emailexists') {
            this.isExistEmail = true;
        }
        this.onError(err.error);
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    changeEmail() {
        this.isExistEmail = false;
    }
}

@Component({
    selector: 'jhi-user-dialog',
    template: ''
})
export class UserDialogComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private userModalService: UserModalService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['login'] ) {
                this.userModalService.open(UserMgmtDialogComponent as Component, params['login']);
            } else {
                this.userModalService.open(UserMgmtDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
