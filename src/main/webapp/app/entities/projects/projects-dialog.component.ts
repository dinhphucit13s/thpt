import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService, JhiDataUtils } from 'ng-jhipster';

import { Projects } from './projects.model';
import { ProjectsPopupService } from './projects-popup.service';
import { ProjectsService } from './projects.service';
import { ProjectTemplates, ProjectTemplatesService } from '../project-templates';
import { ProjectUsers, ProjectUsersService } from '../project-users';
import { ITEMS_PER_PAGE, Principal, User, UserService } from '../../shared';
import { BugListDefault, BugListDefaultService } from '../bug-list-default';
import { Customer, CustomerService } from '../customer';
import { BusinessUnit, BusinessUnitService } from '../business-unit';

@Component({
    selector: 'jhi-projects-dialog',
    templateUrl: './projects-dialog.component.html'
})
export class ProjectsDialogComponent implements OnInit {
    currentAccount: any;

    projects: Projects;
    isSaving: boolean;
    dateFormat = require('dateformat');
    projecttemplates: ProjectTemplates[];
    minDate = new Date();
    projectusers: ProjectUsers[];
    userLogin: User[];

    buglistdefaults: BugListDefault[];
    customers: Customer[];
    businessUnits: BusinessUnit[];

    selectedProjectLead: any[];
    selectedWatcherUsers: any[];
    selectedTemplates: any[];
    selectedBusinessUnit: any[];
    selectedCustomer: any[];

    dropdownSettingsProjectLead: any;
    dropdownSettingsWatcherUser: any;
    dropdownSettingsDedicatedUser: any;
    dropdownSettingsTemplates: any;
    dropdownSettingsBusinessUnit: any;
    dropdownSettingsCustomer: any;
    checkProjectLead = true;
    checkTemplates = true;
    checkBusinessUnit = true;
    checkCustomer = true;

    constructor(
        public activeModal: NgbActiveModal,
        private dataUtils: JhiDataUtils,
        private jhiAlertService: JhiAlertService,
        private projectsService: ProjectsService,
        private projectTemplatesService: ProjectTemplatesService,
        private projectUsersService: ProjectUsersService,
        private eventManager: JhiEventManager,
        private userService: UserService,
        private bugListDefaultService: BugListDefaultService,
        private customerService: CustomerService,
        private businessUnitService: BusinessUnitService,
        private principal: Principal
    ) {
        this.dateFormat(this.minDate, "dd, mm, yyyy, h:MM:ss TT");
    }

    ngOnInit() {
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });

        this.isSaving = false;
        this.projectTemplatesService.query({size: 100000000})
            .subscribe(
                (res: HttpResponse<ProjectTemplates[]>) => {
                    // this.projecttemplates = res.body;
                    this.onSuccessTemplates(res.body, res.headers);
                    },
                (res: HttpErrorResponse) => this.onError(res.message));

        this.userService.query({size: 100000000})
            .subscribe(
                (res: HttpResponse<User[]>) => {
                    // this.userLogin = res.body;
                    this.onSuccessProjectLead(res.body, res.headers);
                    this.onSuccessWatcherUser(res.body, res.headers);
                    this.onSuccessDedicatedUser(res.body, res.headers);
                    },
                (res: HttpResponse<any>) => this.onError(res.body));

        this.bugListDefaultService.query({size: 100000000})
            .subscribe((res: HttpResponse<BugListDefault[]>) => { this.buglistdefaults = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));

        this.customerService.query({size: 100000000})
            .subscribe(
                (res: HttpResponse<Customer[]>) => {
                    // this.customers = res.body;
                    this.onSuccessCustomer(res.body, res.headers);
                    },
                (res: HttpErrorResponse) => this.onError(res.message));

        this.businessUnitService.query({size: 100000000})
            .subscribe(
                (res: HttpResponse<BusinessUnit[]>) => {
                    // this.businessUnits = res.body;
                    this.onSuccessBusinessUnit(res.body, res.headers);
                    },
                (res: HttpErrorResponse) => this.onError(res.message));
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
        if (this.projects.startTime !== undefined) {
            this.projects.startTime = this.dateFormat(this.projects.startTime, "yyyy-mm-dd'T'HH:MM:ss");
        }
        if (this.projects.endTime !== undefined) {
            this.projects.endTime = this.dateFormat(this.projects.endTime, "yyyy-mm-dd'T'HH:MM:ss");
        }
        this.isSaving = true;
        if (this.projects.id !== undefined) {
            this.subscribeToSaveResponse(
                this.projectsService.update(this.projects));
        } else {
            this.subscribeToSaveResponse(
                this.projectsService.create(this.projects));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<Projects>>) {
        result.subscribe((res: HttpResponse<Projects>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: Projects) {
        this.eventManager.broadcast({ name: 'projectsListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackProjectTemplatesById(index: number, item: ProjectTemplates) {
        return item.id;
    }

    trackProjectUsersById(index: number, item: ProjectUsers) {
        return item.id;
    }

    trackUsersByLogin(index: number, item: User) {
        return item.login;
    }

    trackBugListDefaultById(index: number, item: BugListDefault) {
        return item.id;
    }

    trackCustomerById(index: number, item: Customer) {
        return item.id;
    }

    trackBusinessUnitById(index: number, item: BusinessUnit) {
        return item.id;
    }

    getSelected(selectedVals: Array<any>, option: any) {
        if (selectedVals) {
            for (let i = 0; i < selectedVals.length; i++) {
                if (option.id === selectedVals[i].id) {
                    return selectedVals[i];
                }
            }
        }
        return option;
    }

    private onSuccessProjectLead(data, headers) {
        this.userLogin = data;
        this.userLogin = this.userLogin.filter( (user) => user.activated);
        this.dropdownSettingsProjectLead = {
            singleSelection: true,
            idField: 'login',
            textField: 'login',
            allowSearchFilter: true,
            enableCheckAll: false,
            'closeDropDownOnSelection': false
        };

        if (this.projects.id) {
            this.selectedProjectLead  = [this.userLogin.find((u) => u.login === this.projects.projectLeadUserLogin)];
        }
    }

    onItemSelectProjectLead(item: any) {
        this.projects.projectLeadUserLogin = item;
        this.checkProjectLead = true;
    }

    onDeSelectProjectLead(item: any) {
        this.projects.projectLeadUserLogin = null;
        this.checkProjectLead = false;
    }

    private onSuccessTemplates(data, headers) {
        this.projecttemplates = data;
        this.dropdownSettingsTemplates = {
            singleSelection: true,
            idField: 'id',
            textField: 'name',
            allowSearchFilter: true,
            enableCheckAll: false,
            'closeDropDownOnSelection': false
        };

        if (this.projects.id) {
            this.selectedTemplates  = [{id: this.projects.projectTemplatesId, name: this.projects.projectTemplatesName}];
        }
    }

    onItemSelectTemplates(item: any) {
        this.projects.projectTemplatesId = item.id;
        this.checkTemplates = true;
    }

    onDeSelectTemplates(item: any) {
        this.projects.projectTemplatesId = null;
        this.checkTemplates = false;
    }

    private onSuccessBusinessUnit(data, headers) {
        this.businessUnits = data;
        this.dropdownSettingsBusinessUnit = {
            singleSelection: true,
            idField: 'id',
            textField: 'name',
            allowSearchFilter: true,
            enableCheckAll: false,
            'closeDropDownOnSelection': false
        };

        if (this.projects.id) {
            this.selectedBusinessUnit  = [{id: this.projects.businessUnitId, name: this.projects.businessUnitName}];
        }
    }

    onItemSelectBusinessUnit(item: any) {
        this.projects.businessUnitId = item.id;
        this.checkBusinessUnit = true;
    }

    onDeSelectBusinessUnit(item: any) {
        this.projects.businessUnitId = null;
        this.checkBusinessUnit = false;
    }

    private onSuccessCustomer(data, headers) {
        this.customers = data;
        this.dropdownSettingsCustomer = {
            singleSelection: true,
            idField: 'id',
            textField: 'name',
            allowSearchFilter: true,
            enableCheckAll: false,
            'closeDropDownOnSelection': false
        };

        if (this.projects.id) {
            this.selectedCustomer  = [{id: this.projects.customerId, name: this.projects.customerName}];
        }
    }

    onItemSelectCustomer(item: any) {
        this.projects.customerId = item.id;
        this.checkCustomer = true;
    }

    onDeSelectCustomer(item: any) {
        this.projects.customerName = null;
        this.checkCustomer = false;
    }

    private onSuccessWatcherUser(data, headers) {
        this.userLogin = data;
        this.userLogin = this.userLogin.filter( (user) => user.activated);
        this.userLogin = this.userLogin.filter( (user) => user.login.toLocaleLowerCase() !== this.currentAccount.login.toLocaleLowerCase());

        this.dropdownSettingsWatcherUser = {
            singleSelection: false,
            idField: 'login',
            textField: 'login',
            'itemsShowLimit': 5,
            allowSearchFilter: true,
            enableCheckAll: false,
            'closeDropDownOnSelection': false
        };

        /*if (this.projects.id) {
            this.selectedProjectLead  = [this.userLogin.find((u) => u.login === this.projects.projectLeadUserLogin)];
        }*/
    }

    onItemSelectWatcherUser(item: any) {
        console.log('WatcherUser onItemSelect', item);
        console.log('WatcherUser selected: ', this.projects.watcherUsers);
    }

    onDeSelectWatcherUser(item: any) {
        console.log('WatcherUser onItemDESelect', item);
        console.log('WatcherUser selected: ', this.projects.watcherUsers);
    }

    private onSuccessDedicatedUser(data, headers) {
        this.userLogin = data;
        this.userLogin = this.userLogin.filter( (user) => user.activated);
        this.userLogin = this.userLogin.filter( (user) => user.login.toLocaleLowerCase() !== this.currentAccount.login.toLocaleLowerCase());

        this.dropdownSettingsDedicatedUser = {
            singleSelection: false,
            idField: 'login',
            textField: 'login',
            'itemsShowLimit': 5,
            allowSearchFilter: true,
            enableCheckAll: false,
            'closeDropDownOnSelection': false
        };

        /*if (this.projects.id) {
            this.selectedProjectLead  = [this.userLogin.find((u) => u.login === this.projects.projectLeadUserLogin)];
        }*/
    }

    onItemSelectDedicatedUser(item: any) {
        console.log('Dedicated onItemSelect', item);
        console.log('Dedicated selected: ', this.projects.dedicatedUsers);
    }

    onDeSelectDedicatedUser(item: any) {
        console.log('Dedicated onItemDESelect', item);
        console.log('Dedicated selected: ', this.projects.dedicatedUsers);
    }
}

@Component({
    selector: 'jhi-projects-popup',
    template: ''
})
export class ProjectsPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private projectsPopupService: ProjectsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.projectsPopupService
                    .open(ProjectsDialogComponent as Component, params['id']);
            } else {
                this.projectsPopupService
                    .open(ProjectsDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
