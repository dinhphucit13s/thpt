import {Component, OnInit, OnDestroy, ViewContainerRef, ViewChild, ComponentFactory, ComponentFactoryResolver} from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService, JhiDataUtils } from 'ng-jhipster';

import { PurchaseOrders } from './purchase-orders.model';
import { PurchaseOrdersPopupService } from './purchase-orders-popup.service';
import { PurchaseOrdersService } from './purchase-orders.service';
import { Projects, ProjectsService } from '../projects';
import { ProjectUsers, ProjectUsersService } from '../project-users';
import {DynamicFormComponent} from '../../shared/dynamic-forms/dynamic-form/dynamic-form.component';
import {FieldConfig} from '../../shared/dynamic-forms/field.interface';
import {AppConstants} from '../../shared/services/app-constants';
import {Principal, User, UserService} from '../../shared';
import {ProjectTemplates, ProjectTemplatesService} from '../project-templates';

@Component({
    selector: 'jhi-purchase-orders-dialog',
    templateUrl: './purchase-orders-dialog.component.html'
})
export class PurchaseOrdersDialogComponent implements OnInit {
    currentAccount: any;

    purchaseOrders: PurchaseOrders;
    isSaving: boolean;
    dateFormat = require('dateformat');
    projects: Projects[];
    projectId: any;
    maxDate = new Date(2019, 4, 15, 20, 30);
    minDate = new Date();
    projectusers: ProjectUsers[];
    projecttemplates: ProjectTemplates[];
    selectedTemplates: any[];
    dropdownSettingsTemplates: any;
    checkTemplates = true;

    /*dynamic form*/
    formStringify: any;
    initFieldConfig: any;
    form: DynamicFormComponent;
    @ViewChild('child', {read: ViewContainerRef}) myChild;
    formComponentFactory: ComponentFactory<DynamicFormComponent>;
    regConfig: FieldConfig[] = new Array<FieldConfig>();

    selectedPOLead: any;
    dropdownSettingsPOLead= {
        singleSelection: true,
        idField: 'id',
        textField: 'userLogin',
        allowSearchFilter: true,
        enableCheckAll: false,
        'closeDropDownOnSelection': false
    };
    dropdownSettingsWatcherUser: any;
    dropdownSettingsDedicatedUser: any;
    listAccountWatcher: User[];
    listAccountDedicated: User[];
    review1Ratio: number;
    review2Ratio: number;
    fiRatio: number;

    constructor(
        public activeModal: NgbActiveModal,
        private activatedRoute: ActivatedRoute,
        private dataUtils: JhiDataUtils,
        private jhiAlertService: JhiAlertService,
        private purchaseOrdersService: PurchaseOrdersService,
        private projectTemplatesService: ProjectTemplatesService,
        private projectsService: ProjectsService,
        private projectUsersService: ProjectUsersService,
        private userService: UserService,
        private eventManager: JhiEventManager,
        private componentFactoryResolver: ComponentFactoryResolver,
        private principal: Principal
    ) {
        this.dateFormat(this.minDate, 'dd, mm, yyyy, h:MM:ss TT');
    }

    ngOnInit() {
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });

        if (this.purchaseOrders.reviewRatio) {
            const reviewRatio = JSON.parse(this.purchaseOrders.reviewRatio);
            this.review1Ratio = reviewRatio.review1Ratio;
            this.review2Ratio = reviewRatio.review2Ratio;
            this.fiRatio = reviewRatio.fiRatio;
        }

        this.isSaving = false;
        // this.getFieldsDisplay();
        this.projectsService.query({size: 100000000})
            .subscribe((res: HttpResponse<Projects[]>) => { this.projects = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
        this.projectUsersService.queryRole({ query: this.purchaseOrders.projectId })
            .subscribe(
            (res: HttpResponse<ProjectUsers[]>) => { this.projectusers = res.body; },
            (res: HttpErrorResponse) => this.onError(res.message)
        );

        this.projectTemplatesService.query({size: 100000000})
            .subscribe(
                (res: HttpResponse<ProjectTemplates[]>) => {
                    // this.projecttemplates = res.body;
                    this.onSuccessTemplates(res.body, res.headers);
                },
                (res: HttpErrorResponse) => this.onError(res.message));

        if (this.purchaseOrders.purchaseOrderLeadId) {
            this.selectedPOLead = [{id: this.purchaseOrders.purchaseOrderLeadId, userLogin: this.purchaseOrders.purchaseOrderLeadUserLogin}];
        }

        this.userService.query({size: 100000000})
            .subscribe(
                (res: HttpResponse<User[]>) => {
                    this.onSuccessWatcherUser(res.body, res.headers);
                    this.onSuccessDedicatedUser(res.body, res.headers);
                },
                (res: HttpResponse<any>) => this.onError(res.body));
    }

    onItemSelectTemplates(item: any) {
        this.purchaseOrders.projectTemplatesId = item.id;
        this.checkTemplates = true;
    }

    onDeSelectTemplates(item: any) {
        this.purchaseOrders.projectTemplatesId = null;
        this.checkTemplates = false;
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

        if (this.purchaseOrders.id) {
            if (this.purchaseOrders.projectTemplatesId && this.purchaseOrders.projectTemplatesName) {
                this.selectedTemplates  = [{id: this.purchaseOrders.projectTemplatesId, name: this.purchaseOrders.projectTemplatesName}];
            }
        }
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

    getFieldsDisplay() {
        this.regConfig = AppConstants.PurchaseOrderItems;
        // set param for API
        this.regConfig.forEach((field) => {
            if (field.field === 'purchaseOrderLeadUserLogin') {
                field.paramOptions = this.purchaseOrders.projectId;
            }
        });
        if (this.purchaseOrders.id) {
            this.regConfig.forEach((field) => {
                field.value = this.purchaseOrders[field.field];
            });
        }
        this.formComponentFactory = this.componentFactoryResolver.resolveComponentFactory(DynamicFormComponent);
        const ref = this.myChild.createComponent(this.formComponentFactory);
        this.form = ref.instance;
        ref.instance.fields = this.regConfig;
    }
    save() {
        const reviewRatio: any = new Object();
        if (this.review1Ratio) {
            reviewRatio.review1Ratio = this.review1Ratio;
        }
        if (this.review2Ratio) {
            reviewRatio.review2Ratio = this.review2Ratio;
        }
        if (this.fiRatio) {
            reviewRatio.fiRatio = this.fiRatio;
        }
        if (Object.keys(reviewRatio).length > 0) {
            this.purchaseOrders.reviewRatio = JSON.stringify(reviewRatio);
        } else {
            this.purchaseOrders.reviewRatio = null;
        }
        if (this.purchaseOrders.startTime !== undefined) {
            this.purchaseOrders.startTime = this.dateFormat(this.purchaseOrders.startTime, 'yyyy-mm-dd\"T\"HH:MM:ss');
        }
        if (this.purchaseOrders.endTime !== undefined) {
            this.purchaseOrders.endTime = this.dateFormat(this.purchaseOrders.endTime, 'yyyy-mm-dd\"T\"HH:MM:ss');
        }
        this.isSaving = true;
        if (this.purchaseOrders.id !== undefined) {
            this.subscribeToSaveResponse(
                this.purchaseOrdersService.update(this.purchaseOrders));
        } else {
            this.subscribeToSaveResponse(
                this.purchaseOrdersService.create(this.purchaseOrders));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<PurchaseOrders>>) {
        result.subscribe((res: HttpResponse<PurchaseOrders>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: PurchaseOrders) {
        this.eventManager.broadcast({ name: 'purchaseOrdersListModification', content: 'OK'});
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

    trackProjectUsersById(index: number, item: ProjectUsers) {
        return item.id;
    }

    onItemSelectPOLead(item: any) {
        this.purchaseOrders.purchaseOrderLeadId = item.id;
    }

    onDeSelectPOLead(item: any) {
        this.purchaseOrders.purchaseOrderLeadId = null;
    }

    private onSuccessWatcherUser(data, headers) {
        this.listAccountWatcher = data;
        this.listAccountWatcher = this.listAccountWatcher.filter( (user) => user.activated);
        this.listAccountWatcher = this.listAccountWatcher.filter( (user) => user.login.toLocaleLowerCase() !== this.currentAccount.login.toLocaleLowerCase());

        this.dropdownSettingsWatcherUser = {
            singleSelection: false,
            idField: 'login',
            textField: 'login',
            'itemsShowLimit': 5,
            allowSearchFilter: true,
            enableCheckAll: false,
            'closeDropDownOnSelection': false
        };
    }

    onItemSelectWatcherUser(item: any) {
        console.log('WatcherUser onItemSelect', item);
        console.log('WatcherUser selected: ', this.purchaseOrders.watcherUsersPO);
    }

    onDeSelectWatcherUser(item: any) {
        console.log('WatcherUser onItemDESelect', item);
        console.log('WatcherUser selected: ', this.purchaseOrders.watcherUsersPO);
    }

    private onSuccessDedicatedUser(data, headers) {
        this.listAccountDedicated = data;
        this.listAccountDedicated = this.listAccountDedicated.filter( (user) => user.activated);
        this.listAccountDedicated = this.listAccountDedicated.filter( (user) => user.login.toLocaleLowerCase() !== this.currentAccount.login.toLocaleLowerCase());

        this.dropdownSettingsDedicatedUser = {
            singleSelection: false,
            idField: 'login',
            textField: 'login',
            'itemsShowLimit': 5,
            allowSearchFilter: true,
            enableCheckAll: false,
            'closeDropDownOnSelection': false
        };
    }

    onItemSelectDedicatedUser(item: any) {
        console.log('Dedicated onItemSelect', item);
        console.log('Dedicated selected: ', this.purchaseOrders.dedicatedUsersPO);
    }

    onDeSelectDedicatedUser(item: any) {
        console.log('Dedicated onItemDESelect', item);
        console.log('Dedicated selected: ', this.purchaseOrders.dedicatedUsersPO);
    }
}

@Component({
    selector: 'jhi-purchase-orders-popup',
    template: ''
})
export class PurchaseOrdersPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private purchaseOrdersPopupService: PurchaseOrdersPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.purchaseOrdersPopupService
                    .open(PurchaseOrdersDialogComponent as Component, params['id']);
            } else {
                this.purchaseOrdersPopupService
                    .openPO(PurchaseOrdersDialogComponent as Component, sessionStorage.getItem('currentProjectId'));
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
